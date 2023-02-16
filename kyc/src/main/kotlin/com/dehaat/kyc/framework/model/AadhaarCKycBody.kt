package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AadhaarCKycBody(
	@Json(name = "dob")
	val dateOfBirth: String,
	@Json(name = "gender")
	val gender: String?,
	@Json(name = "name_on_aadhaar")
	val name: String,
	@Json(name = "aadhaar_last_four_digit")
	val aadhaarLastFourDigits: String,
	@Json(name = "doc_type")
	val docType: String = "AADHAAR",
	@Json(name = "ckyc_purpose")
	val cKycPurpose: String = "DeHaat Insurance KYC"
)
