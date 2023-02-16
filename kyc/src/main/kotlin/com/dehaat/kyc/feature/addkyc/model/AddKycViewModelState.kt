package com.dehaat.kyc.feature.addkyc.model

import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.model.UiState

data class AddKycViewModelState(
	val idProofType: IdProofType = IdProofType.Aadhaar,
	val imagePath: String? = null,
	val ocrDetails: OcrDetails? = null,
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val errorMessage: String = "",
	val isSubmitEnabled: Boolean = false
) {
	fun toUiState() = AddKycUiState(
		idProofType = idProofType,
		imagePath = imagePath,
		ocrDetails = ocrDetails,
		isIdProofSelectable = ocrDetails.isNotNull(),
		submitEnabled = isSubmitEnabled,
		uiState = when {
			isLoading -> UiState.Loading
			isError -> UiState.Error(errorMessage)
			isSuccess -> UiState.Success
			else -> UiState.Success
		}
	)
}

data class AddKycUiState(
	val idProofType: IdProofType,
	val imagePath: String?,
	val ocrDetails: OcrDetails?,
	val isIdProofSelectable: Boolean,
	val submitEnabled: Boolean,
	val uiState: UiState
)
