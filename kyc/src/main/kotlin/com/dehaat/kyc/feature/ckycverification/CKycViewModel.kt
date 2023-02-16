package com.dehaat.kyc.feature.ckycverification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orFalse
import com.dehaat.androidbase.helper.orZero
import com.dehaat.androidbase.utils.DateFormat
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.feature.ckycverification.model.CKycViewModelState
import com.dehaat.kyc.feature.ckycverification.model.Status
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.framework.network.usecase.GetAadhaarCKycUseCase
import com.dehaat.kyc.framework.network.usecase.GetPanCKycUseCase
import com.dehaat.kyc.framework.network.usecase.RegisterSaleUseCase
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.utils.ui.Constants
import com.dehaat.kyc.utils.ui.DD_MM_YYYY_SLASH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CKycViewModel @Inject constructor(
	private val aadhaarCKycUseCase: GetAadhaarCKycUseCase,
	private val panCKycUseCase: GetPanCKycUseCase,
	private val registerSaleUseCase: RegisterSaleUseCase,
	savedStateHandle: SavedStateHandle
) : ViewModel() {
	val farmerId by lazy { savedStateHandle.get<Long>(Constants.FARMER_ID).orZero() }
	private val isRegisterSale by lazy { savedStateHandle.get<Boolean>(REGISTER_SALE).orFalse() }

	private val ocrDetails by lazy { savedStateHandle.get<OcrDetails>(OCR_DETAILS) }
	val idProofType by lazy {
		when (savedStateHandle.get<Boolean>(Constants.IS_AADHAAR_C_KYC)) {
			true -> IdProofType.Aadhaar
			false -> IdProofType.Pan
			else -> IdProofType.Aadhaar
		}
	}
	private val kycId by lazy { savedStateHandle.get<Long>(Constants.KYC_ID).orZero() }

	private val registerSaleRequest: RegisterSaleRequest? by lazy {
		savedStateHandle.get<RegisterSaleRequest>(REGISTER_SALE_REQUEST)
	}
	private val otpHashCode by lazy {
		savedStateHandle.get<String>(Constants.OTP_HASH_CODE).orEmpty()
	}

	private val viewModelState = MutableStateFlow(CKycViewModelState())
	val uiState = viewModelState.map {
		it.toUiState()
	}.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
	)

	private val _recordSaleStatus = MutableSharedFlow<Status>()
	val recordSaleStatus: SharedFlow<Status> = _recordSaleStatus

	private val _cKycComplete = MutableSharedFlow<Boolean>()
	val cKycComplete: SharedFlow<Boolean> = _cKycComplete

	private val _errorMessage = MutableSharedFlow<String>()
	val errorMessage: SharedFlow<String> = _errorMessage

	init {
		getInitialData()
	}

	private fun getInitialData() = callInViewModelScope {
		viewModelState.update { it.copy(isLoading = true) }
		when (idProofType) {
			IdProofType.Aadhaar -> completeAadhaarCKyc(
				ocrDetails as? OcrDetails.AadhaarOcrDetails,
				kycId
			)
			IdProofType.Pan -> completePanCKyc(
				ocrDetails as? OcrDetails.PanOcrDetails,
				kycId
			)
			else -> Unit
		}
	}

	private fun completeAadhaarCKyc(
		ocrDetails: OcrDetails.AadhaarOcrDetails?,
		id: Long
	) = callInViewModelScope {
		val response = aadhaarCKycUseCase(
			id = id,
			dateOfBirth = getDateInDDMMYY(ocrDetails?.dateOfBirth.orEmpty()),
			gender = ocrDetails?.gender?.firstOrNull()?.uppercaseChar(),
			name = ocrDetails?.name.orEmpty(),
			aadhaarLastFourDigits = ocrDetails?.aadhaarNumber.orEmpty().takeLast(4)
		)

		when (response) {
			is APIResultEntity.Success -> {
				val isCKycSuccessful =
					response.data?.metaData?.identityMaster?.ckyc?.details?.kycStatus == SUCCESS
				val cKycStatus = if (isCKycSuccessful) {
					Status.Success
				} else {
					Status.Failed
				}
				viewModelState.update { it.copy(cKycStatus = cKycStatus) }
				if (isCKycSuccessful) {
					if (isRegisterSale) {
						registerSale(response.data?.id)
					} else {
						_cKycComplete.emit(true)
					}
				} else {
					response.data?.metaData?.errorMessage?.let {
						_errorMessage.emit(it)
					}
					viewModelState.update {
						it.copy(
							isSuccess = false,
							isLoading = false,
							isError = true,
							cKycStatus = Status.Failed
						)
					}
				}
			}
			is APIResultEntity.Failure -> {
				viewModelState.update {
					it.copy(
						isSuccess = false,
						isLoading = false,
						isError = true,
						cKycStatus = Status.Failed
					)
				}
			}
		}
	}

	private fun completePanCKyc(
		ocrDetails: OcrDetails.PanOcrDetails?,
		id: Long
	) = callInViewModelScope {
		val response = panCKycUseCase(
			id = id,
			panNumber = ocrDetails?.panNumber.orEmpty()
		)

		when (response) {
			is APIResultEntity.Success -> {
				val isCKycSuccessful =
					response.data?.verificationStatus == APPROVED
				val cKycStatus = if (isCKycSuccessful) {
					Status.Success
				} else {
					Status.Failed
				}
				viewModelState.update {
					it.copy(cKycStatus = cKycStatus)
				}
				if (isCKycSuccessful) {
					if (isRegisterSale) {
						registerSale(response.data?.id)
					} else {
						_cKycComplete.emit(true)
					}
				} else {
					response.data?.metaData?.errorMessage?.let {
						_errorMessage.emit(it)
					}
					viewModelState.update {
						it.copy(
							isSuccess = false,
							isLoading = false,
							isError = true,
							cKycStatus = Status.Failed
						)
					}
				}
			}
			is APIResultEntity.Failure -> {
				viewModelState.update {
					it.copy(
						isSuccess = false,
						isLoading = false,
						isError = true,
						cKycStatus = Status.Failed
					)
				}
			}
		}
	}

	private suspend fun registerSale(
		kycId: Long? = null
	) = when (
		registerSaleUseCase(
			farmerId = farmerId,
			request = registerSaleRequest?.apply {
				kycId?.let { insurance = insurance?.copy(identityProofId = it) }
			},
			requestOtp = false
		)
	) {
		is APIResultEntity.Success -> {
			viewModelState.update {
				it.copy(
					isSuccess = true,
					isLoading = false,
					isError = false,
					saleStatus = Status.Success
				)
			}
			callInViewModelScope {
				_recordSaleStatus.emit(Status.Success)
			}
		}
		is APIResultEntity.Failure -> viewModelState.update {
			it.copy(
				isSuccess = false,
				isLoading = false,
				isError = true,
				saleStatus = Status.Failed
			)
		}
	}

	private fun getDateInDDMMYY(
		date: String,
		format: String = DD_MM_YYYY_SLASH,
		toFormat: String = DateFormat.dd_MM_yyy
	) = date.toDate(format)?.let {
		SimpleDateFormat(toFormat, Locale.getDefault()).format(it)
	}.orEmpty()

	fun String.toDate(format: String): Date? {
		return try {
			val utcDateFormat = SimpleDateFormat(format, Locale.getDefault())
			utcDateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
			utcDateFormat.parse(this)
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
	}

	companion object {
		private const val SUCCESS = "SUCCESS"
		private const val APPROVED = "APPROVED"
		const val NAME = "NAME"
		const val NUMBER = "NUMBER"
		const val FARMER_ID = "FARMER_ID"
		const val OCR_DETAILS = "OCR_DETAILS"
		const val PROOF_TYPE = "PROOF_TYPE"
		const val REGISTER_SALE = "REGISTER_SALE"
		const val REGISTER_SALE_REQUEST = "REGISTER_SALE_REQUEST"
		const val KYC_COMPLETE = "KYC_COMPLETE"
		const val PROOF_ID = "PROOF_ID"
	}
}
