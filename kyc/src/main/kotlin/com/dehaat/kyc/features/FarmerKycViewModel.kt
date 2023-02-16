package com.dehaat.kyc.features

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orZero
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.feature.bankkyc.model.BankVerifiedDetails
import com.dehaat.kyc.features.composables.kycsuccess.model.KycStatusUiState
import com.dehaat.kyc.features.model.DocumentDetails
import com.dehaat.kyc.features.model.FarmerKycViewModelState
import com.dehaat.kyc.features.recordsale.KycRecordSaleActivity
import com.dehaat.kyc.framework.entity.model.DigitalDocumentEntity
import com.dehaat.kyc.framework.entity.model.MasterDataEntity
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.framework.network.usecase.GetAllDigitalDocumentUseCase
import com.dehaat.kyc.framework.network.usecase.GetBankBranchFromIfscUseCase
import com.dehaat.kyc.framework.network.usecase.GetDocumentUseCase
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.navigation.KycExecutor
import com.dehaat.kyc.utils.ui.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class FarmerKycViewModel @Inject constructor(
	private val getDocumentUseCase: GetDocumentUseCase,
	private val getAllDigitalDocumentUseCase: GetAllDigitalDocumentUseCase,
	private val ifscUseCase: GetBankBranchFromIfscUseCase,
	private val farmerKycMapper: FarmerKycMapper,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val viewModelState = MutableStateFlow(FarmerKycViewModelState())
	val uiState = viewModelState.map {
		it.toUiState()
	}.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
	)

	val farmerId by lazy { savedStateHandle.get<Long>(Constants.FARMER_ID).orZero() }
	val farmerName by lazy { savedStateHandle.get<String>(Constants.NAME).orEmpty() }
	val farmerNumber by lazy { savedStateHandle.get<String>(Constants.NUMBER).orEmpty() }
	val farmerAuthId by lazy { savedStateHandle.get<String>(Constants.FARMER_AUTH_ID).orEmpty() }
	val payableAmount by lazy {
		savedStateHandle.get<Double>(KycRecordSaleActivity.PAYABLE_AMOUNT) ?: 0.0
	}

	private val masterDataType by lazy {
		savedStateHandle.get<String>(Constants.MASTER_DATA_TYPE).orEmpty()
	}

	val isBankVerification: Boolean
		get() = masterDataType == Constants.BankDocumentType

	val idProofType: IdProofType
		get() = viewModelState.value.idProofType

	val sampleImageMap: Map<IdProofType, DocumentDetails>
		get() = viewModelState.value.documentDetailsMap

	val documentId: Long
		get() = with(viewModelState.value) {
			documentDetailsMap[idProofType]?.documentId.orZero()
		}

	val ocrDetails: OcrDetails?
		get() = viewModelState.value.ocrDetails

	val kycId: Long
		get() = viewModelState.value.kycId.orZero()

	private val _kycPending = MutableSharedFlow<Boolean>()
	val kycPending: SharedFlow<Boolean> = _kycPending

	fun getInitialData() = callInViewModelScope {
		if (isBankVerification) viewModelState.update {
			it.copy(idProofType = IdProofType.Bank)
		}
		showLoader()
		val masterData = getDocumentUseCase.getDocuments(masterDataType)
		processMasterData(masterData)

		showLoader()
		val response = getAllDigitalDocumentUseCase.getAllDigitalDocuments(farmerId)
		processDocuments(response)
	}

	private fun processMasterData(
		masterData: APIResultEntity<MasterDataEntity?>
	) = when (masterData) {
		is APIResultEntity.Failure.ErrorFailure -> {
			showError(masterData.responseMessage)
		}
		is APIResultEntity.Failure.ErrorException -> {
			showError("")
		}
		is APIResultEntity.Success -> {
			val kycMap = farmerKycMapper.toBottomSheetMap(masterData.data?.documentEntity)
			viewModelState.update {
				it.copy(documentDetailsMap = kycMap)
			}
		}
	}

	private fun processDocuments(
		response: APIResultEntity<DigitalDocumentEntity?>
	) = when (response) {
		is APIResultEntity.Failure -> {
			showError("")
		}
		is APIResultEntity.Success -> {
			if (isBankVerification) {
				updateBankDetails(
					response.data?.bankProofEntity,
					response.data?.bankVerificationStatus
				)
			} else {
				updateIdProofDetails(response.data?.identityProofEntity)
			}
		}
	}

	private fun updateIdProofDetails(response: DigitalDocumentEntity.IdentityProofEntity?) {
		if (response?.verificationStatus == APPROVED) viewModelState.update {
			it.copy(kycStatusUiState = farmerKycMapper.toKycSuccessUiState(response))
		} else callInViewModelScope {
			viewModelState.update {
				it.copy(kycId = response?.kycId)
			}
			_kycPending.emit(true)
		}
	}

	private fun updateBankDetails(
		response: List<DigitalDocumentEntity.BankProofEntity>?,
		bankVerificationStatus: String?
	) = callInViewModelScope {
		val bankKycStatus = farmerKycMapper.toBankDetails(response).onEach {
			when (val ifscResponse = ifscUseCase(it.ifscCode.orEmpty())) {
				is APIResultEntity.Success -> {
					it.bankName = ifscResponse.data?.bank.orEmpty()
					it.passBookPhoto = KycExecutor.getPreSignedUrl(it.passBookPhoto)
				}
				else -> Unit
			}
		}

		viewModelState.update {
			it.copy(
				kycStatusUiState = KycStatusUiState.BankUiState(bankKycStatus),
				bankVerificationStatus = farmerKycMapper.toVerificationStatus(bankVerificationStatus)
			)
		}
	}

	fun getBankVerifiedData(
		kycId: Long?
	) = with(
		(viewModelState.value.kycStatusUiState as? KycStatusUiState.BankUiState)?.bankDetails?.firstOrNull {
			it.kycId == kycId
		}
	) {
		BankVerifiedDetails(
			accountNumber = this?.bankAccountNumber,
			accountHolderName = this?.accountHolderName,
			ifscCode = this?.ifscCode,
			passbookPhoto = this?.passBookPhoto
		)
	}

	private fun showLoader() = viewModelState.update {
		it.copy(kycStatusUiState = KycStatusUiState.Loading)
	}

	private fun showError(errorMessage: String) = viewModelState.update {
		it.copy(kycStatusUiState = KycStatusUiState.Error(errorMessage))
	}

	fun updateIdProofType(
		idProofType: IdProofType
	) = viewModelState.update {
		it.copy(idProofType = idProofType)
	}

	fun updateOcrDetails(ocrDetails: OcrDetails?) = viewModelState.update {
		it.copy(ocrDetails = ocrDetails)
	}

	fun updateKycId(kycId: Long?) = viewModelState.update {
		it.copy(kycId = kycId)
	}

	var registerSaleRequest: RegisterSaleRequest? = null

	fun updateSaleRequest(saleReq: RegisterSaleRequest?) {
		registerSaleRequest = saleReq
	}

	var hashCode: String? = null
	fun updateOtpHashCode(hash: String) = registerSaleRequest?.apply {
		insurance = insurance?.copy(otpHash = hash, identityProofId = viewModelState.value.kycId)
		hashCode = hash
	}

	companion object {
		private const val APPROVED = "APPROVED"
	}
}
