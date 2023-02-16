package com.dehaat.kyc.model

sealed class UiState {
	object Loading : UiState()
	data class Error(val message: String = "") : UiState()
	object Success : UiState()
}
