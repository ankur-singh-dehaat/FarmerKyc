package com.dehaat.kyc.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponsePanKycVerification(
    @Json(name = "verification_status")
    val verificationStatus: String?,
    @Json(name = "id")
    val id: Long?,
    @Json(name = "meta_data")
    val metaData: MetaData?
) {
    @JsonClass(generateAdapter = true)
    data class MetaData(
        @Json(name = "identity_master_error")
        val errorMessage: String?
    )
}
