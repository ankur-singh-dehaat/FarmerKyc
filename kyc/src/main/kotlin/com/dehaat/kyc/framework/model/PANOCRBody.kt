package com.dehaat.kyc.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PANOCRBody(
    @Json(name = "pan_card_image")
    val panCardImage: String?
)
