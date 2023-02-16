package com.dehaat.kyc.feature.bankkyc

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class BankOtpViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle
) : ViewModel() {/*
	private val farmerAuthId by lazy {
		savedStateHandle.get<String>(Constants.FARMER_AUTH_ID).orEmpty()
	}
	private val farmerId by lazy { savedStateHandle[Constants.FARMER_ID] ?: 0L }
	private val documentType by lazy { savedStateHandle.get<String>(Constants.DOC_TYPE).orEmpty() }
	private val bankDetails by lazy { savedStateHandle.get<BankDetailsViewData>(Constants.BANK_DETAILS) }
	private val number by lazy { savedStateHandle.get<String>(Constants.NUMBER).orEmpty() }
	private val name by lazy { savedStateHandle.get<String>(Constants.NAME).orEmpty() }

	private val viewModelState = MutableStateFlow(OtpViewModelState())
	val uiState = viewModelState
		.map { it.toUiState() }
		.stateIn(
			viewModelScope,
			SharingStarted.Eagerly,
			viewModelState.value.toUiState()
		)

	private var otpTimer = MutableStateFlow<String?>(null)

	init {
		viewModelState.update {
			it.copy(phone = number, name = name)
		}
		startOtpTimer()
		sendOtpViaSms()
		callInViewModelScope {
			otpTimer.collectLatest { time -> viewModelState.update { it.copy(timer = time) } }
		}
	}

	fun sendOtpViaSms() = callInViewModelScope {
		showLoading(true)
		startOtpTimer()
		when (val result =
			sendOtpViaSmsUseCase(farmerId, documentType)) {
			is APIResultEntity.Success -> {
				showLoading(false)
			}
			is APIResultEntity.Failure -> {
				showLoading(false)
				_uiEvent.emit(UiEvent.ShowSnackbar(result.getFailureError()))
			}
		}
	}

	fun enterOtp(otp: String) = viewModelState.update {
		it.copy(otp = otp, isValidOtp = otp.length == 4)
	}

	fun validateOtp() = callInViewModelScope {
		showLoading(true)
		when (val result =
			validateOtpCaseCase(
				viewModelState.value.otp,
				farmerId,
				documentType
			)
		) {
			is APIResultEntity.Success -> {
				viewModelState.update {
					it.copy(
						hashCode = result.data.hashcode
					)
				}
				showLoading(false)
				submitDocument()
			}
			is APIResultEntity.Failure.ErrorException -> {
				showLoading(false)
			}
			is APIResultEntity.Failure.ErrorFailure -> {
				showLoading(false)
				_uiEvent.emit(
					UiEvent.ShowSnackbar(
						ErrorMessageParser.getErrorMessage(
							result.responseMessage,
							result.responseErrorBody
						).orEmpty()
					)
				)
			}
		}
	}

	private fun submitDocument() = callInViewModelScope {
		bankDetails?.let {
			showLoading(true)
			when (val response = updateBankAccountUseCase(
				farmerId.toString(),
				it.accountId,
				RequestAddBankEntity(
					accountNumber = it.accountNumber,
					ifscCode = it.ifscCode,
					accountHolderName = it.accountHolderName,
					otpHash = viewModelState.value.hashCode,
					ifscDetails = mapper.toIfscDetailsEntity(it.ifscDetails)
				)
			)) {
				is APIResultEntity.Success -> {
					showLoading(false)
					if (!it.imageUrl.isNullOrEmpty()) {
						updateAccountDetails(
							it.imageUrl,
							response.data?.userBankAccountDetails?.firstOrNull()?.id.orEmpty(),
							response.data?.userBankAccountDetails?.firstOrNull()?.bankDocumentType.orZero()
						)
					} else {
						showLoading(false)
					}
				}
				is APIResultEntity.Failure -> {
					showLoading(false)
				}
			}
		}
	}

	private fun updateAccountDetails(
		imageUrl: String,
		bankDetailsId: String,
		bankDocumentType: Int
	) = callInViewModelScope {
		showLoading(true)
		when (val response = updateBankAccountDetailsUseCase(
			farmerId.toString(),
			bankDetailsId, //bankdetails
			RequestBankDetailsEntity(bankDocumentType, imageUrl) // document type
		)) {
			is APIResultEntity.Success -> {
				showLoading(false)
			}
			is APIResultEntity.Failure -> {
				showLoading(false)
			}
		}
	}


	fun sendOtpViaCall() = callInViewModelScope {
		showLoading(true)
		startOtpTimer()
		when (sendOtpViaCallUseCase(farmerAuthId)) {
			is APIResultEntity.Success -> {
				showLoading(false)
			}
			is APIResultEntity.Failure -> {
				showLoading(false)
			}
		}
	}

	private fun startOtpTimer() = callInViewModelScope {
		var seconds = 20
		otpTimer.emit("00:$seconds")
		while (seconds-- > 0) {
			delay(1000)
			val formatted = when (seconds) {
				0 -> null
				in 1..9 -> "00:0$seconds"
				in 10..20 -> "00:$seconds"
				else -> null
			}
			otpTimer.emit(formatted)
		}
	}

	private fun showLoading(isLoading: Boolean) = viewModelState.update {
		it.copy(isLoading = isLoading)
	}*/
}
