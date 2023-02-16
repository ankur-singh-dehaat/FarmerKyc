package com.dehaat.kyc.framework.entity.model

import com.dehaat.kyc.model.IdProofType

data class DigitalDocumentEntity(
	val identityProofEntity: IdentityProofEntity?,
	val bankProofEntity: List<BankProofEntity>?,
	val idProofVerificationStatus: String?,
	val bankVerificationStatus: String?
) {
	data class IdentityProofEntity(
		val kycId: Long,
		val identityProofNumber: String,
		val frontUrl: String?,
		val verificationStatus: String,
		val identityProofType: Int,
		val kycName: String?,
		val idProofType: IdProofType,
		val lastUpdatedOn: String?
	)

	data class BankProofEntity(
		val kycId: Long?,
		val ifscCode: String?,
		val accountNumber: String?,
		val accountHolderName: String?,
		val passbookPhoto: String?,
		val verificationStatus: String
	)
}
