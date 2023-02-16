package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvailablePaymentMode(
	@Json(name = "id") val id: Long,

	@Json(name = "name") val name: String,

	@Json(name = "display_name") val displayName: String,

	@Json(name = "image") var imageUrl: String? = null,

	@Json(name = "is_credit") val isCredit: Boolean = false,

	var isSelected: Boolean = false,

	val creditLimit: Double? = null,
	var amount: String = ""
)
