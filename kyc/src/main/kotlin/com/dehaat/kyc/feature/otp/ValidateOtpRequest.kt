package com.dehaat.kyc.feature.otp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidateOtpRequest (
	@Json(name = "otp")
	val otp: String,
	@Json(name = "farmer_id")
	val farmerId: Long? = null,
	@Json(name = "document_type")
	val documentType: String? = null,
)
