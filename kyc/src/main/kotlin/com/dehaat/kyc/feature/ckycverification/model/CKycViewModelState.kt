package com.dehaat.kyc.feature.ckycverification.model

data class CKycViewModelState(
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val cKycStatus: Status = Status.Failed,
	val saleStatus: Status = Status.Failed
) {
	fun toUiState() = CKycUiState(
		isLoading = isLoading,
		isError = isError,
		isSuccess = isSuccess,
		cKycStatus = cKycStatus,
		saleStatus = saleStatus
	)
}

data class CKycUiState(
	val isLoading: Boolean,
	val isError: Boolean,
	val isSuccess: Boolean,
	val cKycStatus: Status,
	val saleStatus: Status
)
