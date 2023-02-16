package com.dehaat.kyc.feature.ckyc.model

import com.dehaat.kyc.model.IdProofType

data class CKycCompleted(
	val name: String?,
	val cardType: IdProofType?,
	val cardNumber: String?,
	val lastUpdatedOn: String?
)
