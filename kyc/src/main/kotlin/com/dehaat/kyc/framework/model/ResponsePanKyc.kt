package com.dehaat.kyc.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponsePanKyc(
    @Json(name = "isValid")
    val isValid: Boolean,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "middleName")
    val middleName: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "number")
    val number: String,
    @Json(name = "panStatus")
    val panStatus: String,
)
