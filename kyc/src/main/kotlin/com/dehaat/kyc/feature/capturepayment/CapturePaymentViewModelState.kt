package com.dehaat.kyc.feature.capturepayment

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dehaat.kyc.framework.model.AvailablePaymentMode
import com.dehaat.kyc.framework.model.RegisterSalePaymentModeRequest
import com.dehaat.kyc.model.UiState

data class CapturePaymentViewModelState(
	val availablePaymentMode: SnapshotStateList<AvailablePaymentMode> = mutableStateListOf(),
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val selectedPaymentModes: List<RegisterSalePaymentModeRequest> = emptyList(),
	val errorMessage: String = "",
	val pendingAmount: String = "",
	val validAmount: Boolean = false
) {
	fun toUiState() = CapturePaymentUiState(
		availablePaymentMode = availablePaymentMode,
		selectedPaymentModes = selectedPaymentModes,
		pendingAmount = pendingAmount,
		validAmount = validAmount,
		uiState = when {
			isLoading -> UiState.Loading
			isError -> UiState.Error(errorMessage)
			isSuccess -> UiState.Success
			else -> UiState.Success
		}
	)
}

data class CapturePaymentUiState(
	val availablePaymentMode: List<AvailablePaymentMode>,
	val selectedPaymentModes: List<RegisterSalePaymentModeRequest>,
	val pendingAmount: String,
	val validAmount: Boolean,
	val uiState: UiState
)
