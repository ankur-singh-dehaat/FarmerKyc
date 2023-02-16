package com.dehaat.kyc.feature.ckycverification.model

data class AadharCKycData(
	val farmerId: Long,
	val docType: String,
	val dateOfBirth: String,
	val gender: String,
	val name: String,
	val aadharLastFourDigits: String
)
