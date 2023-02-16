package com.dehaat.kyc.feature.bankkyc.model

import com.dehaat.kyc.framework.model.BottomSheetData
import com.dehaat.kyc.model.UiState
import java.io.File

data class BankDetailsViewModelState(
	val verificationStatus: VerificationStatus = VerificationStatus.Pending,
	val isSubmitEnabled: Boolean = false,
	val verifiedData: BankDetailsVerifiedData? = null,
	val ctaState: CtaState = CtaState.Enabled,
	val previewImage: File? = null,
	val previewImagePath: String = "",
	val isLoading: Boolean = false,
	val isSuccess: Boolean = false,
	val isError: Boolean = false,
	val bankIdentityProofId: Long? = null,
	val bankDetailsValidationState: BankDetailsValidationState = BankDetailsValidationState(),
	val bottomSheetData: BottomSheetData = BottomSheetData(),
	val bankBranchDetails: BankBranchDetailsViewData? = null
) {

	fun toUiState() = BankDetailsUiState(
		verificationStatus = verificationStatus,
		isSubmitEnabled = isSubmitEnabled,
		verifiedData = verifiedData,
		previewImage = previewImage,
		previewImagePath = previewImagePath,
		bankIdentityProofId = bankIdentityProofId,
		bankDetailsValidationState = bankDetailsValidationState,
		bottomSheetData = bottomSheetData,
		bankBranchDetails = bankBranchDetails,
		uiState = when {
			isLoading -> UiState.Loading
			isSuccess -> UiState.Success
			isError -> UiState.Error()
			else -> UiState.Success
		}
	)
}

data class BankDetailsUiState(
	val verificationStatus: VerificationStatus,
	val isSubmitEnabled: Boolean,
	val bankIdentityProofId: Long?,
	val verifiedData: BankDetailsVerifiedData?,
	val previewImage: File?,
	val previewImagePath: String,
	val bankDetailsValidationState: BankDetailsValidationState,
	val bottomSheetData: BottomSheetData,
	val uiState: UiState,
	val bankBranchDetails: BankBranchDetailsViewData?
)
