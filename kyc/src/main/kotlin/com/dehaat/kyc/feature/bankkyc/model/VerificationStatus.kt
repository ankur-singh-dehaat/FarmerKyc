package com.dehaat.kyc.feature.bankkyc.model

sealed class VerificationStatus(val value: String) {
	object Pending : VerificationStatus("PENDING")
	object Approved : VerificationStatus("APPROVED")
	object Rejected : VerificationStatus("REJECTED")
	object Verified : VerificationStatus("VERIFIED")
	object Submitted : VerificationStatus("DOCUMENT_SUBMITTED")
}
