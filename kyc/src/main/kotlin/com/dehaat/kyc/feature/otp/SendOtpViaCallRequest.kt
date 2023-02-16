package com.dehaat.kyc.feature.otp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendOtpViaCallRequest(
	@Json(name = "farmer_auth_id")
	val farmerAuthId: String,
	@Json(name = "otp_case")
	val otpCase: String
)
