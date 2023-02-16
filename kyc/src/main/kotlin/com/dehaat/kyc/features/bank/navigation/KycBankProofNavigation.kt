package com.dehaat.kyc.features.bank.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dehaat.kyc.feature.bankkyc.BankKycSuccessScreen
import com.dehaat.kyc.feature.bankkyc.model.BankVerifiedDetails
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.features.FarmerKycViewModel
import com.dehaat.kyc.features.bank.KycBankProofActivity
import com.dehaat.kyc.features.composables.kycsuccess.KycEntryScreen
import com.dehaat.kyc.features.composables.navigation.navigateToPendingBankRecord
import com.dehaat.kyc.features.composables.navigation.navigateToVerifiedBankRecord
import com.dehaat.kyc.utils.ui.navBaseComposable
import com.dehaat.kyc.utils.ui.onClick

@Composable
fun KycBankProofNavigation(
	finish: onClick,
	farmerKycViewModel: FarmerKycViewModel,
	navController: NavHostController = rememberNavController()
) = NavHost(
	navController = navController,
	startDestination = KycBankProofRoute.KycSuccess.route
) {
	navBaseComposable(route = KycBankProofRoute.KycSuccess.route) {
		KycEntryScreen(
			finish = finish,
			viewModel = farmerKycViewModel,
			onBankDetailsClick = { kycId, verificationStatus ->
				when (verificationStatus) {
					VerificationStatus.Pending -> navController.navigateToPendingBankRecord(kycId)
					VerificationStatus.Rejected -> navController.navigateToPendingBankRecord(kycId)
					else -> navController.navigateToVerifiedBankRecord(
						farmerKycViewModel.getBankVerifiedData(
							kycId
						)
					)
				}
				navController.navigateToVerifiedBankRecord(
					farmerKycViewModel.getBankVerifiedData(
						kycId
					)
				)
			},
			onAddBankDetails = {
				navController.navigateToPendingBankRecord()
			}
		)
	}

	navBaseComposable(route = KycBankProofRoute.KycVerifiedBankRecord.route) {
		val bankVerifiedDetails =
			it.arguments?.getParcelable<BankVerifiedDetails>(KycBankProofActivity.BANK_VERIFIED_DETAILS)
		BankKycSuccessScreen(
			finish = finish,
			bankVerifiedDetails = bankVerifiedDetails
		)
	}

	navBaseComposable(route = KycBankProofRoute.KycPendingBankRecord.route) {
		Text(text = "Pending")
	}

	navBaseComposable(route = KycBankProofRoute.KycBankOtp.route) {
		Text(text = "OTP")
	}

	navBaseComposable(route = KycBankProofRoute.KycCapturePhoto.route) {
		Text(text = "Capture photo")
	}
}
