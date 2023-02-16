package com.dehaat.kyc.feature.bankkyc

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.androidbase.helper.orZero
import com.dehaat.kyc.feature.bankkyc.model.BankDetailsValidationState
import com.dehaat.kyc.feature.bankkyc.model.BankDetailsViewModelState
import com.dehaat.kyc.feature.bankkyc.model.BankVerifiedDetails
import com.dehaat.kyc.feature.bankkyc.model.CtaState
import com.dehaat.kyc.feature.bankkyc.model.RequestBankDetailsEntity
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.framework.model.AllDigitalDocumentEntity
import com.dehaat.kyc.framework.model.BankDetailsRequest
import com.dehaat.kyc.framework.model.BankDetailsUploadData
import com.dehaat.kyc.framework.model.BottomSheetData
import com.dehaat.kyc.framework.model.IFSCDetails
import com.dehaat.kyc.framework.model.MasterDataEntity
import com.dehaat.kyc.framework.network.usecase.AddBankDetailsUseCase
import com.dehaat.kyc.framework.network.usecase.AddBankDocumentsUseCase
import com.dehaat.kyc.framework.network.usecase.GetAllDigitalDocumentUseCase
import com.dehaat.kyc.framework.network.usecase.GetBankBranchFromIfscUseCase
import com.dehaat.kyc.framework.network.usecase.GetDocumentUseCase
import com.dehaat.kyc.framework.network.usecase.UpdateBankDetailsUseCase
import com.dehaat.kyc.framework.network.usecase.UpdateBankDocumentsUseCase
import com.dehaat.kyc.model.IdProof
import com.dehaat.kyc.navigation.KycExecutor
import com.dehaat.kyc.utils.aws.AmazonS3Uploader
import com.dehaat.kyc.utils.ui.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class BankVerificationViewModel @Inject constructor(
	private val getDocumentUseCase: GetDocumentUseCase,
	private val mapper: BankKycViewDataMapper,
	private val getAllDigitalDocumentsUseCase: GetAllDigitalDocumentUseCase,
	private val getBankBranchFromIfscUseCase: GetBankBranchFromIfscUseCase,
	private val addBankAccountDetailsUseCase: AddBankDetailsUseCase,
	private val updateBankDetailsUseCase: UpdateBankDetailsUseCase,
	private val addBankDocumentsUseCase: AddBankDocumentsUseCase,
	private val updateBankDocumentsUseCase: UpdateBankDocumentsUseCase,
	private val amazonS3Uploader: AmazonS3Uploader,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	val farmerId by lazy { savedStateHandle[Constants.FARMER_ID] ?: 0L }

	private val bankId by lazy { savedStateHandle.get<String>(KYC_ID) }

	private val viewModelState = MutableStateFlow(BankDetailsViewModelState())
	val uiState = viewModelState.map {
		it.toUiState()
	}.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
	)

	private val _navigateSuccessBankVerification = MutableSharedFlow<BankVerifiedDetails>()
	val navigateSuccessBankVerification: SharedFlow<BankVerifiedDetails> =
		_navigateSuccessBankVerification

	private val _navigateToOtp = MutableSharedFlow<Boolean>()
	val navigateToOtp: SharedFlow<Boolean> = _navigateToOtp

	private var bankIdType = IdProof(0, "")

	init {
		getInitialData()
	}

	private fun getInitialData() = callInViewModelScope {
		updateLoader(true)
		val masterData = getDocumentUseCase("bank_document_type")
		processMasterData(masterData)
		val response = getAllDigitalDocumentsUseCase(farmerId)
		processDocuments(response)
	}

	private fun processMasterData(
		masterData: APIResultEntity<MasterDataEntity?>
	) = when (masterData) {
		is APIResultEntity.Success -> {
			masterData.data?.bankDocumentTypeDocument?.forEach {
				if (it.name == PASSBOOK) bankIdType =
					IdProof(it.id.toLongOrNull().orZero(), it.sampleImage)
			}
		}
		is APIResultEntity.Failure -> {
			updateLoader(false)
		}
	}

	private suspend fun processDocuments(
		response: APIResultEntity<AllDigitalDocumentEntity?>
	) = when (response) {
		is APIResultEntity.Failure -> {
			updateLoader(false)
		}
		is APIResultEntity.Success -> {
			val bankDetails = response.data?.userResponseBankAccount?.firstOrNull {
				it.id == bankId
			}
			val verificationState = mapper.toVerificationStatus(bankDetails?.verificationStatus)
			val verifiedData = mapper.toBankVerifiedData(bankDetails).apply {
				this?.passbookPhoto = KycExecutor.getPreSignedUrl(this?.passbookPhoto)
			}
			viewModelState.update {
				it.copy(
					verificationStatus = verificationState,
					verifiedData = verifiedData,
					ctaState = getCtaState(verificationState),
					isSuccess = true,
					isLoading = false,
					isError = false,
					bankDetailsValidationState = mapper.toBankDetailsValidationState(verifiedData),
					bottomSheetData = BottomSheetData(
						cardType = "Bank Passbook",
						sampleUrl = bankIdType.sampleUrl
					),
					bankIdentityProofId = verifiedData?.id
				)
			}
			verifiedData?.ifsc?.let { checkIfscCodeValidation(it) }
		}
	}

	private fun addBankDetailsDocument() = callInViewModelScope {
		val bankDetails = viewModelState.value.bankDetailsValidationState
		when (val result =
			getBankBranchFromIfscUseCase(bankDetails.ifscCode)) {
			is APIResultEntity.Success -> {
				addBankDetails(
					bankDetails = bankDetails,
					ifscDetails = IFSCDetails(
						bankName = result.data?.bank.orEmpty(),
						branchName = result.data?.branch.orEmpty()
					)
				)
			}
			is APIResultEntity.Failure -> {
				addBankDetails(bankDetails)
			}
		}
	}

	private fun updateBankDetailsDocument(bankId: Long) = callInViewModelScope {
		val bankDetails = viewModelState.value.bankDetailsValidationState
		when (val result =
			getBankBranchFromIfscUseCase(bankDetails.ifscCode)) {
			is APIResultEntity.Success -> {
				updateBankDetails(bankDetails, bankId)
			}
			is APIResultEntity.Failure -> {
				updateBankDetails(bankDetails, bankId)
			}
		}
	}

	private suspend fun addBankDetails(
		bankDetails: BankDetailsValidationState,
		ifscDetails: IFSCDetails? = null
	) {
		val response = addBankDocumentsUseCase(
			farmerId = farmerId,
			bankDetails = BankDetailsUploadData(
				accountNumber = bankDetails.accountNumber,
				ifscCode = bankDetails.ifscCode,
				accountHolderName = bankDetails.bankAccountHolderName,
				ifscDetails = ifscDetails
			)
		)

		when (response) {
			is APIResultEntity.Success -> {
				viewModelState.update { it.copy(bankIdentityProofId = response.data?.id) }
				viewModelState.value.previewImage?.let {
					uploadImageToS3Bucket(it) { awsPath ->
						addPassbookRecords(awsPath, response.data?.id.orZero())
					}
				}
			}
			else -> {
				updateLoader(false)
			}
		}
	}

	private suspend fun updateBankDetails(
		bankDetails: BankDetailsValidationState,
		bankId: Long
	) = callInViewModelScope {
		val response = if (bankId < 0) {
			addBankAccountDetailsUseCase(
				farmerId.toString(),
				bankId.toString(),
				RequestBankDetailsEntity(
					bankDocumentType = bankIdType.id.toInt(),
					frontUrl = viewModelState.value.previewImage?.path.orEmpty()
				)
			)
		} else {
			updateBankDetailsUseCase(
				farmerId = farmerId.toString(),
				bankAccountDetailsId = bankId.toString(),
				RequestBankDetailsEntity(
					bankIdType.id.toInt(),
					viewModelState.value.previewImage?.path.orEmpty()
				)
			)
		}
		when (response) {
			is APIResultEntity.Success -> {
				viewModelState.update { it.copy(bankIdentityProofId = response.data?.id) }
				viewModelState.value.previewImage?.let {
					uploadImageToS3Bucket(it) { awsPath ->
						updatePassbookRecords(awsPath, response.data?.id.orZero())
					}
				}
			}
			else -> updateLoader(false)
		}
	}

	private fun addPassbookRecords(imagePath: String, id: Long) = callInViewModelScope {
		val result = addBankAccountDetailsUseCase(
			farmerId.toString(),
			id.toString(),
			RequestBankDetailsEntity(bankIdType.id.toInt(), imagePath)
		)
		when (result) {
			is APIResultEntity.Success -> {
				val verifiedDetails = viewModelState.value.bankDetailsValidationState
				_navigateSuccessBankVerification.emit(
					BankVerifiedDetails(
						accountNumber = verifiedDetails.accountNumber,
						accountHolderName = verifiedDetails.bankAccountHolderName,
						ifscCode = verifiedDetails.ifscCode,
						passbookPhoto = verifiedDetails.passbookPhoto
					)
				)
				updateLoader(false)
			}
			is APIResultEntity.Failure -> {
				updateLoader(false)
			}
		}
	}

	private fun updatePassbookRecords(imagePath: String, id: Long) = callInViewModelScope {
		val response = updateBankDocumentsUseCase(
			farmerId = farmerId,
			BankDetailsRequest(
				accountNumber = viewModelState.value.bankDetailsValidationState.accountNumber,
				ifscCode = viewModelState.value.bankDetailsValidationState.ifscCode
			)
		)

		when (response) {
			is APIResultEntity.Success -> {
				val verifiedDetails = viewModelState.value.bankDetailsValidationState
				_navigateSuccessBankVerification.emit(
					BankVerifiedDetails(
						accountNumber = verifiedDetails.accountNumber,
						accountHolderName = verifiedDetails.bankAccountHolderName,
						ifscCode = verifiedDetails.ifscCode,
						passbookPhoto = verifiedDetails.passbookPhoto
					)
				)
				updateLoader(false)
			}
			is APIResultEntity.Failure -> {
				updateLoader(false)
			}
		}
	}

	private fun uploadImageToS3Bucket(
		file: File,
		uploadedDocumentPath: (String) -> Unit
	) {
		amazonS3Uploader.uploadImageToKYCs3Bucket(
			file,
			farmerId,
			false,
			uploadSuccess = { previewImagePath ->
				if (previewImagePath.isNotEmpty()) {
					viewModelState.update {
						it.copy(previewImagePath = previewImagePath)
					}
					uploadedDocumentPath(previewImagePath)
				} else {
					updateLoader(false)
				}
			},
			uploadFailure = {
				updateLoader(false)
			}
		)
	}

	private fun getBankDetailsUpdateStatus(verificationState: VerificationStatus) =
		when (verificationState) {
			VerificationStatus.Pending -> true
			VerificationStatus.Submitted -> false
			VerificationStatus.Approved -> false
			VerificationStatus.Rejected -> true
			else -> false
		}

	fun checkAccountNumberValidation(
		accountNumber: String
	) = viewModelState.update {
		it.copy(
			bankDetailsValidationState = it.bankDetailsValidationState.copy(
				isAccountNumberValid = accountNumber.matches(Regex(Constants.BANK_NUMBER_REGEX)),
				isConfirmAccountNumberValid = if (it.bankDetailsValidationState.confirmAccountNumber.isNotEmpty()) {
					accountNumber == it.bankDetailsValidationState.confirmAccountNumber
				} else {
					null
				},
				accountNumber = accountNumber
			)
		)
	}

	fun checkConfirmAccountNumberValidation(
		confirmAccountNumber: String
	) = viewModelState.update {
		it.copy(
			bankDetailsValidationState =
			it.bankDetailsValidationState.copy(
				isConfirmAccountNumberValid = it.bankDetailsValidationState.accountNumber == confirmAccountNumber,
				confirmAccountNumber = confirmAccountNumber
			)
		)
	}

	fun checkIfscCodeValidation(
		ifscCode: String
	) = callInViewModelScope {
		if (ifscCode.isNotBlank() && ifscCode.matches(Regex(Constants.IFSC_CODE_REGEX))) {
			when (val result = getBankBranchFromIfscUseCase.invoke(ifscCode)) {
				is APIResultEntity.Success -> {
					val bankBranchDetails = mapper.toBankBranchDetailsViewData(result.data)
					viewModelState.update {
						it.copy(
							bankDetailsValidationState =
							it.bankDetailsValidationState.copy(
								isIfscCodeValid = true,
								bankAndBranchName = "${bankBranchDetails.bank}, ${bankBranchDetails.branch}",
								ifscCode = ifscCode
							),
							bankBranchDetails = bankBranchDetails
						)
					}
				}
				is APIResultEntity.Failure -> {
					viewModelState.update {
						it.copy(
							bankDetailsValidationState =
							it.bankDetailsValidationState.copy(
								isIfscCodeValid = false,
								ifscCode = ifscCode
							)
						)
					}
				}
			}
		} else {
			viewModelState.update {
				it.copy(
					bankDetailsValidationState =
					it.bankDetailsValidationState.copy(
						isIfscCodeValid = false,
						ifscCode = ifscCode
					)
				)
			}
		}
	}

	private fun getCtaState(verificationState: VerificationStatus) = when (verificationState) {
		VerificationStatus.Pending -> CtaState.Disabled
		VerificationStatus.Rejected -> CtaState.Enabled
		else -> CtaState.Enabled
	}

	fun checkAccountHolderNameValidation(
		accountHolderName: String
	) = viewModelState.update {
		it.copy(
			bankDetailsValidationState =
			it.bankDetailsValidationState.copy(
				isAccountHolderNameValid = accountHolderName.isNotEmpty(),
				bankAccountHolderName = accountHolderName
			)
		)
	}

	fun submitBankDetails() = callInViewModelScope {
		updateLoader(true)
		viewModelState.value.bankIdentityProofId?.let {
			updateBankDetailsDocument(it)
		} ?: addBankDetailsDocument()
	}

	private fun updateLoader(
		loading: Boolean = false
	) = viewModelState.update {
		it.copy(isLoading = loading)
	}

	fun updatePreviewPhoto(previewImage: File? = null) = viewModelState.update {
		it.copy(
			previewImage = previewImage,
			ctaState = if (previewImage.isNotNull() && it.bankDetailsValidationState.isValid) CtaState.Enabled else CtaState.Disabled,
			bankDetailsValidationState = it.bankDetailsValidationState.copy(isPassBookPhotoValid = previewImage.isNotNull())
		)
	}

	fun navigateToOtpScreen() = callInViewModelScope {
		viewModelState.value.previewImage?.let {
			updateLoader(true)
			uploadImageToS3Bucket(it) {
				callInViewModelScope {
					_navigateToOtp.emit(true)
				}
			}
		} ?: _navigateToOtp.emit(true)
	}

	companion object {
		const val KYC_ID = "KYC_ID"
		const val VERIFICATION_STATUS = "VERIFICATION_STATUS"
		const val PASSBOOK = "Passbook"
		const val PASSBOOK_PHOTO = "PASSBOOK_PHOTO"
	}
}
