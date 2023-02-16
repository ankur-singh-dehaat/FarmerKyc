package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IdProofRequest(
	@Json(name = "identity_proof_type")
	val proofType: Long,

	@Json(name = "identity_proof_number")
	val proofNumber: String?
)
