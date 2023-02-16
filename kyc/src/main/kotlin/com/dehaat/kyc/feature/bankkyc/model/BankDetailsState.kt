package com.dehaat.kyc.feature.bankkyc.model

data class BankDetailsState(
	var bankName: String?,
	val ifscCode: String?,
	val accountNumber: String?,
	val isPassbookAttached: Boolean,
	val accountHolderName: String?,
	val passbookPhoto: String?,
	val verificationStatus: VerificationStatus,
	val kycId: String
) {
	fun toVerifiedBankDetails() = BankVerifiedDetails(
		accountHolderName = accountHolderName,
		passbookPhoto = passbookPhoto,
		ifscCode = ifscCode,
		accountNumber = accountNumber
	)
}
