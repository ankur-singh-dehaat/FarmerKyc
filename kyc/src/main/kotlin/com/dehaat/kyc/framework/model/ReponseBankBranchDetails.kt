package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseBankBranchDetails(

	@Json(name = "BRANCH") val branch: String?,
	@Json(name = "BANK") val bank: String?
)

@JsonClass(generateAdapter = true)
data class BankDetailsUploadData(
	@Json(name = "account_number")
	val accountNumber: String,

	@Json(name = "ifsc_code")
	val ifscCode: String,

	@Json(name = "account_holder_name")
	val accountHolderName: String,

	@Json(name = "bank_ifsc_detail")
	val ifscDetails: IFSCDetails?
)

@JsonClass(generateAdapter = true)
data class IFSCDetails(
	@Json(name = "bank_name")
	val bankName: String,

	@Json(name = "branch_name")
	val branchName: String
)
