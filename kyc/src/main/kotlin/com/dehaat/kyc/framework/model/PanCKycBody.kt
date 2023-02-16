package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PanCKycBody(
	@Json(name = "pan_number")
	val panNumber: String
)
