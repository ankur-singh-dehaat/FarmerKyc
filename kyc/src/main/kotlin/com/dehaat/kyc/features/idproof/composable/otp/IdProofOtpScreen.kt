package com.dehaat.kyc.features.idproof.composable.otp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehaat.kyc.feature.addkyc.CKycOtpViewModel
import com.dehaat.kyc.features.composables.otp.OtpScreen

@Composable
fun IdProofOtpScreen(
	viewModel: CKycOtpViewModel = hiltViewModel(),
	navigateTocKyc: (String) -> Unit
) {
	val uiState by viewModel.uiState.collectAsState()
	OtpScreen(
		uiState = uiState,
		isRecordSale = false,
		sendOtpViaCall = viewModel::sendOtpViaCall,
		sendOtpViaSms = viewModel::sendOtpViaSms,
		onSubmitOtpClick = { uiState.hashCode?.let(navigateTocKyc) },
		enterOtp = viewModel::enterOtp
	)

	LaunchedEffect(Unit) {
		viewModel.otpHashCode.collect(navigateTocKyc)
	}
}
