package com.dehaat.kyc.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseAadhaarOCR(
    @Json(name = "result")
    val result: Result?,
    @Json(name = "error")
    val error: String?
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "aadhaarNumber")
        val aadhaarNumber: String?,
        @Json(name = "address")
        val address: String?,
        @Json(name = "dob")
        val dob: String?,
        @Json(name = "fullName")
        val fullName: String?,
        @Json(name = "gender")
        val gender: String?
    )
}
