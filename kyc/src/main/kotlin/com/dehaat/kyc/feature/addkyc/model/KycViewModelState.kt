package com.dehaat.kyc.feature.addkyc.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dehaat.kyc.feature.bankkyc.model.BankDetailsState
import com.dehaat.kyc.feature.ckyc.model.CKycCompleted
import com.dehaat.kyc.framework.model.BottomSheetData
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.model.UiState

data class KycViewModelState(
	val idProofType: IdProofType = IdProofType.Aadhaar,
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val kycId: Long = 0,
	val errorMessage: String = "",
	val completedKycState: CKycCompleted? = null,
	val bottomSheetMap: Map<IdProofType, BottomSheetData> = emptyMap(),
	val bankDetails: SnapshotStateList<BankDetailsState> = mutableStateListOf()
) {
	fun toUiState() = KycUiState(
		idProofType = idProofType,
		completedKycState = completedKycState,
		kycId = kycId,
		bankDetails = bankDetails,
		uiState = when {
			isLoading -> UiState.Loading
			isError -> UiState.Error(errorMessage)
			isSuccess -> UiState.Success
			else -> UiState.Success
		}
	)
}

data class KycUiState(
	val idProofType: IdProofType,
	val completedKycState: CKycCompleted?,
	val kycId: Long,
	val bankDetails: List<BankDetailsState>,
	val uiState: UiState
)
