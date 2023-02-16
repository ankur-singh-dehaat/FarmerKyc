package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json

data class BankDetailsRequest(
	@Json(name = "account_number")
	val accountNumber: String,

	@Json(name = "ifsc_code")
	val ifscCode: String
)
