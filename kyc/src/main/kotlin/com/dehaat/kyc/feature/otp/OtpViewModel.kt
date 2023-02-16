package com.dehaat.kyc.feature.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.kyc.features.composables.otp.model.OtpViewModelState
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.framework.network.usecase.RegisterSaleUseCase
import com.dehaat.kyc.model.InsuranceErrorResponse
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
	private val validateOtpCaseCase: ValidateOtpUseCase,
	private val registerSaleUseCase: RegisterSaleUseCase,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val farmerId by lazy { savedStateHandle[FARMER_ID] ?: 0L }
	private val number by lazy { savedStateHandle.get<String>(NUMBER).orEmpty() }
	private val name by lazy { savedStateHandle.get<String>(NAME).orEmpty() }

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
			it.copy(phone = number, name = name)
		}
		startOtpTimer()
	}

	fun updateRegisterSaleRequest(registerSaleRequest: RegisterSaleRequest?) = Unit

	fun sendOtpViaSms(isResendOtp: Boolean = true) = callInViewModelScope {
		startOtpTimer()
		viewModelState.update { it.copy(reSendOtpViaSms = isResendOtp) }
		when (val result = registerSaleUseCase(farmerId, null, true)) {
			is APIResultEntity.Failure.ErrorFailure -> {
				viewModelState.update {
					it.copy(errorMessage = result.responseErrorBody, isError = true)
				}
				dismissErrorMessage()
			}
			else -> Unit
		}
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
			validateOtpCaseCase(
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
						errorResponse = getInsuranceError(result.responseErrorBody),
						isLoading = false
					)
				}
				dismissErrorMessage()
			}
		}
	}

	private fun getInsuranceError(errorBody: String?) = try {
		Gson().fromJson(
			errorBody,
			InsuranceErrorResponse::class.java
		)
	} catch (e: Exception) {
		null
	}

	fun sendOtpViaCall() = callInViewModelScope {
		startOtpTimer()
		viewModelState.update { it.copy(reSendOtpViaCall = true) }
		viewModelState.update { it.copy(reSendOtpViaCall = false) }
	}

	private fun startOtpTimer() = callInViewModelScope {
		var seconds = 20
		viewModelState.update { it.copy(timer = "00:$seconds") }
		while (seconds-- > 0) {
			delay(1000)
			val formatted = when (seconds) {
				0 -> null
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

	private fun dismissErrorMessage() = callInViewModelScope {
		delay(5000L)
		viewModelState.update {
			it.copy(errorMessage = null, isError = false)
		}
	}

	companion object {
		const val NAME = "NAME"
		const val NUMBER = "NUMBER"
		const val FARMER_AUTH_ID = "FARMER_AUTH_ID"
		const val FARMER_ID = "FARMER_ID"
		const val PAYMENT_MODES = "PAYMENT_MODES"
	}
}
