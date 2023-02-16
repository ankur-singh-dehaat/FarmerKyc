package com.dehaat.kyc.features.composables.otp.model

import com.dehaat.kyc.model.InsuranceErrorResponse

data class OtpUiState(
	val farmerName: String,
	val phoneNumber: String,
	val otp: String,
	val isValidOtp: Boolean,
	val isInvalidOtp: Boolean,
	val reSendOtpViaSms: Boolean,
	val reSendOtpViaCall: Boolean,
	val verificationStatus: String,
	val phone: String,
	val name: String,
	val isLoading: Boolean,
	val isError: Boolean,
	val errorMessage: String?,
	val timer: String?,
	val bankIdProofId: String,
	val hashCode: String?,
	val errorResponse: InsuranceErrorResponse?
)
