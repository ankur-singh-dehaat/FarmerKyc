package com.dehaat.kyc.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InsuranceErrorResponse(
	@Json(name = "insurance_type")
	val insuranceType: String,
	@Json(name = "reason")
	val reason: String,
	@Json(name = "cart_threshold_data")
	val cartThresholdData: CartThresholdData
) {
	@JsonClass(generateAdapter = true)
	data class CartThresholdData(
		@Json(name = "minimum_cart_value")
		val minimumCartValue: String,
		@Json(name = "maximum_cart_value")
		val maximumCartValue: String
	)
}
