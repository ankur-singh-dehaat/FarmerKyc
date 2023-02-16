package com.dehaat.kyc.feature.otp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidateOtpResponse(
	@Json(name = "hashcode")
	val hashcode: String
)
