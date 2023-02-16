package com.dehaat.kyc.feature.otp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendOtpRequest(
	@Json(name = "farmer_id")
	val farmerId: Long,
	@Json(name = "document_type")
	val documentType: String
)
