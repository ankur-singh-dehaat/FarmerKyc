package com.dehaat.kyc.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponsePanOCR(
    @Json(name = "fullName")
    val name: String?,
    @Json(name = "panNumber")
    val panNo: String?
)
