package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AadharOCRBody(
	@Json(name = "aadhaar_front")
	val aadharFront: String?,
	@Json(name = "aadhaar_back")
	val aadharBack: String?
)
