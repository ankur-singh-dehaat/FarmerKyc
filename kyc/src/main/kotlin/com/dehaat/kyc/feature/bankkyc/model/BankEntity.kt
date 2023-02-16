package com.dehaat.kyc.feature.bankkyc.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class RequestAddBankEntity(
	val accountNumber: String,
	val ifscCode: String,
	val accountHolderName: String,
	val ifscDetails: IfscDetailsEntity,
	val otpHash: String
)

data class IfscDetailsEntity(
	val bankName: String?,
	val branchName: String?,
)

data class RequestBankDetailsEntity(
	val bankDocumentType: Int,
	val frontUrl: String,
)

@JsonClass(generateAdapter = true)
data class BankDetails(
	val id: Long,

	@Json(name = "account_number")
	var accountNumber: String,

	@Json(name = "ifsc_code")
	var ifscCode: String,

	@Json(name = "verification_status")
	var verificationStatus: String
)

@JsonClass(generateAdapter = true)
data class RequestBankDetails(
	@Json(name = "front_url")
	val frontUrl: String,
	@Json(name = "bank_document_type")
	val bankDocumentType: Int
)
