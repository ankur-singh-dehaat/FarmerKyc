package com.dehaat.kyc.feature.addkyc

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.kyc.features.composables.otp.model.OtpViewModelState
import com.dehaat.kyc.framework.network.usecase.SendKycOtpUseCase
import com.dehaat.kyc.framework.network.usecase.ValidateKycOtpUseCase
import com.dehaat.kyc.utils.ui.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class CKycOtpViewModel @Inject constructor(
	private val sendKycOtpUseCase: SendKycOtpUseCase,
	private val validateKycOtpUseCase: ValidateKycOtpUseCase,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val farmerId by lazy { savedStateHandle[Constants.FARMER_ID] ?: 0L }
	private val farmerAuthId by lazy {
		savedStateHandle.get<String>(Constants.FARMER_AUTH_ID).orEmpty()
	}
	private val phoneNumber by lazy { savedStateHandle.get<String>(Constants.NUMBER).orEmpty() }
	private val farmerName by lazy { savedStateHandle.get<String>(Constants.NAME).orEmpty() }

	private val viewModelState = MutableStateFlow(OtpViewModelState())
	val uiState = viewModelState
		.map { it.toUiState() }
		.stateIn(
			viewModelScope,
			SharingStarted.Eagerly,
			viewModelState.value.toUiState()
		)

	private val _otpHashCode = MutableSharedFlow<String>()
	val otpHashCode: SharedFlow<String> = _otpHashCode

	init {
		viewModelState.update {
			it.copy(phoneNumber = phoneNumber, farmerName = farmerName)
		}
		sendOtpViaSms(false)
	}

	fun sendOtpViaSms(isResendOtp: Boolean = true) = callInViewModelScope {
		startOtpTimer()
		viewModelState.update { it.copy(reSendOtpViaSms = isResendOtp) }
		sendKycOtpUseCase(farmerId)
		viewModelState.update { it.copy(reSendOtpViaSms = false) }
	}

	fun enterOtp(otp: String) {
		viewModelState.update {
			it.copy(otp = otp)
		}
		if (otp.length == 4) {
			validateOtp()
		} else {
			viewModelState.update {
				it.copy(isInvalidOtp = false)
			}
		}
	}

	fun validateOtp() = callInViewModelScope {
		showLoading(true)
		when (val result =
			validateKycOtpUseCase(
				viewModelState.value.otp,
				farmerId
			)
		) {
			is APIResultEntity.Success -> {
				viewModelState.update {
					it.copy(
						hashCode = result.data?.hashcode,
						isInvalidOtp = false,
						isError = false,
						isLoading = false,
						isValidOtp = true
					)
				}
				_otpHashCode.emit(result.data?.hashcode.orEmpty())
			}
			is APIResultEntity.Failure.ErrorException -> {
				showLoading(false)
			}
			is APIResultEntity.Failure.ErrorFailure -> {
				viewModelState.update {
					it.copy(
						isInvalidOtp = true,
						errorMessage = null,
						isLoading = false,
						isError = true
					)
				}
			}
		}
	}


	fun sendOtpViaCall() = callInViewModelScope {
		startOtpTimer()
		viewModelState.update { it.copy(reSendOtpViaCall = true) }
		sendKycOtpUseCase.sendOtpViaCall(farmerAuthId)
		viewModelState.update { it.copy(reSendOtpViaCall = false) }
	}

	private fun startOtpTimer() = callInViewModelScope {
		var seconds = 20
		viewModelState.update { it.copy(timer = "00:$seconds") }
		while (seconds-- > 0) {
			delay(1000)
			val formatted = when (seconds) {
				in 1..9 -> "00:0$seconds"
				in 10..20 -> "00:$seconds"
				else -> null
			}
			viewModelState.update { it.copy(timer = formatted) }
		}
	}

	private fun showLoading(isLoading: Boolean) = viewModelState.update {
		it.copy(isLoading = isLoading)
	}
}
