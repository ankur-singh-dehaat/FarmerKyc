package com.dehaat.kyc.features.composables.kycsuccess.model

import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.model.IdProofType

sealed class KycStatusUiState {
	object Loading : KycStatusUiState()

	data class VerifiedIdProof(
		val name: String?,
		val cardType: IdProofType?,
		val cardNumber: String?,
		val lastUpdatedOn: String?
	) : KycStatusUiState()

	data class Error(
		val errorMessage: String
	) : KycStatusUiState()

	data class BankUiState(
		val bankDetails: List<BankDetails>
	) : KycStatusUiState() {
		data class BankDetails(
			val ifscCode: String?,
			val bankAccountNumber: String?,
			val verificationStatus: VerificationStatus,
			val kycId: Long?,
			val accountHolderName: String?,
			var passBookPhoto: String? = "",
			var bankName: String? = ""
		)
	}
}
