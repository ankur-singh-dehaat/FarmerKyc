package com.dehaat.kyc.framework.model

import com.dehaat.kyc.model.IdProofType

data class AllDigitalDocumentEntity(
	val userResponseIdentityProof: List<IdentityProofEntity>,
	val userResponseBankAccount: List<BankAccountEntity>
)

data class IdentityProofEntity(
	val id: Long,
	val identityProofNumber: String,
	val backUrl: String?,
	val frontUrl: String?,
	val verificationStatus: String,
	val identityProofType: Int,
	val metaData: MetaDataEntity?,
	val kycName: String?,
	val lastUpdatedOn: String?
)

data class MetaDataEntity(
	val agentNotes: List<String>?,
	val idMasterCKycResponse: IdMasterCKycResponse?,
	val idProofType: IdProofType
) {
	data class IdMasterCKycResponse(
		val details: Details?
	) {
		data class Details(
			val cKycNo: String?
		)
	}
}

data class BankAccountEntity(
	val id: String,
	val userBankAccountDetails: List<BankAccountDetailEntity>,
	var accountNumber: String,
	val ifscCode: String,
	val accountHolderName: String?,
	val verificationStatus: String,
	val metaData: MetaDataEntity?,
)

data class BankAccountDetailEntity(

	val id: String,
	val frontUrl: String?,
	val verificationStatus: String,
	val userBankAccount: Int,
	val bankDocumentType: Int
)
