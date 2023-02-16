package com.dehaat.kyc.feature.captureimage.model

import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.model.UiState

data class OcrViewModelState(
	val idProofType: IdProofType = IdProofType.Aadhaar,
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val errorMessage: String = "",
	val isBlurredImage: Boolean = false
) {
	fun toUiState() = OcrUiState(
		isBlurredImage = isBlurredImage,
		idProofType = idProofType,
		uiState = when {
			isLoading -> UiState.Loading
			isError -> UiState.Error(errorMessage)
			isSuccess -> UiState.Success
			else -> UiState.Success
		}
	)
}

data class OcrUiState(
	val isBlurredImage: Boolean,
	val idProofType: IdProofType,
	val uiState: UiState
)
