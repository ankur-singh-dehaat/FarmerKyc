package com.dehaat.kyc.feature

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.androidbase.helper.orZero
import com.dehaat.kyc.KycActivity.Companion.PAYABLE_AMOUNT
import com.dehaat.kyc.feature.addkyc.model.KycViewModelState
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.framework.model.AllDigitalDocumentEntity
import com.dehaat.kyc.framework.model.BankAccountEntity
import com.dehaat.kyc.framework.model.BottomSheetData
import com.dehaat.kyc.framework.model.MasterDataEntity
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.framework.network.usecase.GetAllDigitalDocumentUseCase
import com.dehaat.kyc.framework.network.usecase.GetBankBranchFromIfscUseCase
import com.dehaat.kyc.framework.network.usecase.GetDocumentUseCase
import com.dehaat.kyc.model.IdProof
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.model.KycMapper
import com.dehaat.kyc.utils.ui.Constants
import com.dehaat.kyc.utils.ui.Constants.FARMER_ID
import com.dehaat.kyc.utils.ui.Constants.NAME
import com.dehaat.kyc.utils.ui.Constants.NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class KycViewModel @Inject constructor(
	private val getDocumentUseCase: GetDocumentUseCase,
	private val getAllDigitalDocumentUseCase: GetAllDigitalDocumentUseCase,
	private val ifscUseCase: GetBankBranchFromIfscUseCase,
	private val mapper: KycMapper,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	val name = savedStateHandle.get<String>(NAME).orEmpty()
	val phoneNumber = savedStateHandle.get<String>(NUMBER).orEmpty()
	val farmerId by lazy { savedStateHandle[FARMER_ID] ?: 0L }
	val farmerAuthId by lazy { savedStateHandle.get<String>(Constants.FARMER_AUTH_ID).orEmpty() }
	val payableAmount by lazy { savedStateHandle.get<Double>(PAYABLE_AMOUNT) ?: 0.0 }

	private val viewModelState = MutableStateFlow(KycViewModelState())
	val uiState = viewModelState.map {
		it.toUiState()
	}.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
	)

	private val _cKycPending = MutableSharedFlow<Boolean>()
	val cKycPending: SharedFlow<Boolean> = _cKycPending

	private val idProofMap = mutableMapOf<IdProofType, IdProof>()
	val proofTypeId: Long
		get() = idProofMap[viewModelState.value.idProofType]?.id.orZero()
	var proofId: Long = 0

	val idProofType: IdProofType
		get() = viewModelState.value.idProofType

	val bottomSheetMap: Map<IdProofType, BottomSheetData>
		get() = viewModelState.value.bottomSheetMap

	val isCKycComplete: Boolean
		get() = viewModelState.value.completedKycState.isNotNull()

	fun getInitialData() = callInViewModelScope {
		updateLoader(true)
		val masterData = getDocumentUseCase(IdentityProofType)
		processMasterData(masterData)
		updateLoader(true)
		val response = getAllDigitalDocumentUseCase(farmerId)
		processDocuments(response)
	}

	private fun processMasterData(
		masterData: APIResultEntity<MasterDataEntity?>
	) = when (masterData) {
		is APIResultEntity.Failure -> {
			updateLoader(false)
		}
		is APIResultEntity.Success -> {
			val kycMap = mutableMapOf<IdProofType, BottomSheetData>()
			masterData.data?.identityDocumentProofType?.forEach {
				when (it.name) {
					AADHAAR_CARD -> {
						kycMap[IdProofType.Aadhaar] = BottomSheetData(
							cardType = AADHAAR_CARD,
							sampleUrl = it.sampleImage
						)
						idProofMap[IdProofType.Aadhaar] = IdProof(it.id.toLong(), it.sampleImage)
					}
					PAN_CARD -> {
						kycMap[IdProofType.Pan] = BottomSheetData(
							cardType = PAN_CARD,
							sampleUrl = it.sampleImage
						)
						idProofMap[IdProofType.Pan] = IdProof(it.id.toLong(), it.sampleImage)
					}
				}
			}
			viewModelState.update {
				it.copy(bottomSheetMap = kycMap)
			}
			updateLoader(false)
		}
	}

	private fun processDocuments(
		response: APIResultEntity<AllDigitalDocumentEntity?>
	) = when (response) {
		is APIResultEntity.Failure -> {
			updateLoader(false)
		}
		is APIResultEntity.Success -> {
			val idProofData = response.data?.userResponseIdentityProof
			val idProofDetails = idProofData?.firstOrNull()
			val isPending = idProofDetails?.verificationStatus != APPROVED
			callInViewModelScope {
				_cKycPending.emit(isPending)
			}

			val verifiedData = mapper.toVerifiedData(idProofDetails)

			val cardType = verifiedData?.cardType?.let {
				when (it) {
					idProofMap[IdProofType.Aadhaar]?.id.toString() -> IdProofType.Aadhaar
					idProofMap[IdProofType.Pan]?.id.toString() -> IdProofType.Pan
					else -> IdProofType.Aadhaar
				}
			} ?: IdProofType.Aadhaar
			viewModelState.update {
				it.copy(
					isSuccess = true,
					isLoading = false,
					isError = false,
					kycId = idProofDetails?.id.orZero(),
					idProofType = cardType,
					completedKycState = if (isPending) {
						null
					} else {
						mapper.toCompletedKycState(idProofDetails)
					},
				)
			}
			getBankDetails(response.data?.userResponseBankAccount.orEmpty())
		}
	}

	private fun getBankDetails(userResponseBankAccount: List<BankAccountEntity>) =
		callInViewModelScope {
			val bankDetails = mapper.toBankDetails(userResponseBankAccount).onEach {
				when (val response = ifscUseCase(it.ifscCode.orEmpty())) {
					is APIResultEntity.Success -> {
						it.bankName = response.data?.bank.orEmpty()
					}
					else -> Unit
				}
			}

			viewModelState.update {
				it.copy(bankDetails = bankDetails.toMutableStateList())
			}
		}

	private fun updateLoader(isLoading: Boolean) = viewModelState.update {
		it.copy(isLoading = isLoading)
	}

	fun updateIdProofType(idProofType: IdProofType) = viewModelState.update {
		it.copy(idProofType = idProofType)
	}

	var ocrDetails: OcrDetails? = null

	fun updateOcrDetails(ocrDetails: OcrDetails?) {
		this.ocrDetails = ocrDetails
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
		const val BankDocumentType = "bank_document_type"
		const val IdentityProofType = "identity_proof_type"
		private const val AADHAAR_CARD = "Aadhar Card"
		private const val PAN_CARD = "PAN Card"
		private const val APPROVED = "APPROVED"
	}
}
