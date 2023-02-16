package com.dehaat.kyc.features.model

import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.features.composables.kycsuccess.model.KycStatusUiState
import com.dehaat.kyc.model.IdProofType

data class FarmerKycViewModelState(
	val idProofType: IdProofType = IdProofType.Aadhaar,
	val kycStatusUiState: KycStatusUiState = KycStatusUiState.Loading,
	val documentDetailsMap: Map<IdProofType, DocumentDetails> = emptyMap(),
	val bankVerificationStatus: VerificationStatus? = null,
	val kycId: Long? = null,
	val ocrDetails: OcrDetails? = null
) {
	fun toUiState() = KycUiState(
		idProofType = idProofType,
		kycStatusUiState = kycStatusUiState,
		bankVerificationStatus = bankVerificationStatus
	)
}

data class KycUiState(
	val idProofType: IdProofType,
	val kycStatusUiState: KycStatusUiState,
	val bankVerificationStatus: VerificationStatus?
)
