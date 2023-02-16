package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FarmerIdentityProofDetailItem(
    @Json(name = "id")
    val id: Long,

    @Json(name = "identity_proof_number")
    var docNumber: String,

    @Json(name = "identity_proof_type")
    val docTypeId: Long,

    @Json(name = "verification_status")
    var verificationStatus: String
)
