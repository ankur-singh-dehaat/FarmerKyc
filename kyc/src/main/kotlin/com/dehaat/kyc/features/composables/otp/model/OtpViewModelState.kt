package com.dehaat.kyc.features.composables.otp.model

import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.model.InsuranceErrorResponse

data class OtpViewModelState(
	val otp: String = "",
	val isValidOtp: Boolean = false,
	val isInvalidOtp: Boolean = false,
	val reSendOtpViaSms: Boolean = false,
	val reSendOtpViaCall: Boolean = false,
	val verificationStatus: String = "",
	val phone: String = "",
	val name: String = "",
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val errorMessage: String? = null,
	val timer: String? = null,
	val hashCode: String? = null,
	val bankIdProofId: String = "",
	val errorResponse: InsuranceErrorResponse? = null,
	val farmerName: String = "",
	val phoneNumber: String = ""
) {
	fun toUiState() = OtpUiState(
		otp = otp,
		isValidOtp = isValidOtp,
		isInvalidOtp = isInvalidOtp,
		reSendOtpViaSms = reSendOtpViaSms,
		reSendOtpViaCall = reSendOtpViaCall,
		verificationStatus = verificationStatus,
		phone = phone,
		name = name,
		isLoading = isLoading,
		isError = isError,
		errorMessage = errorMessage,
		timer = timer,
		bankIdProofId = bankIdProofId,
		hashCode = hashCode,
		errorResponse = errorResponse,
		farmerName = farmerName,
		phoneNumber = phoneNumber
	)
}
