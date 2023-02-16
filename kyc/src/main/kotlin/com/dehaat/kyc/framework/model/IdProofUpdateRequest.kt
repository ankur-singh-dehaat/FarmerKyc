package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IdProofUpdateRequest(
	@Json(name = "verification_status")
	val verificationStatus: String = "APPROVED"
)
