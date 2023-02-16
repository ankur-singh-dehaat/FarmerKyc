package com.dehaat.kyc.features.idproof.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dehaat.kyc.feature.captureimage.CaptureIdProofPhoto
import com.dehaat.kyc.feature.ckycverification.KycVerificationScreen
import com.dehaat.kyc.feature.idprooftypeselection.IdProofTypeSelectionScreen
import com.dehaat.kyc.feature.updatekyc.AddKycDetailsScreen
import com.dehaat.kyc.features.FarmerKycViewModel
import com.dehaat.kyc.features.composables.kycsuccess.KycEntryScreen
import com.dehaat.kyc.features.composables.navigation.navigateToAddKycDetailsScreen
import com.dehaat.kyc.features.composables.navigation.navigateToCaptureOtp
import com.dehaat.kyc.features.composables.navigation.navigateToCapturePhoto
import com.dehaat.kyc.features.composables.navigation.navigateToIdProofSelectionScreen
import com.dehaat.kyc.features.composables.navigation.navigateToKyc
import com.dehaat.kyc.features.composables.navigation.navigateToStartScreen
import com.dehaat.kyc.features.idproof.composable.otp.IdProofOtpScreen
import com.dehaat.kyc.utils.ui.navBaseComposable
import com.dehaat.kyc.utils.ui.onClick

@Composable
fun KycIdProofNavigation(
	finish: onClick,
	farmerKycViewModel: FarmerKycViewModel,
	navController: NavHostController = rememberNavController()
) = NavHost(
	navController = navController,
	startDestination = KycIdProofRoute.KycSuccess.route
) {
	navBaseComposable(route = KycIdProofRoute.KycSuccess.route) {
		KycEntryScreen(
			finish = finish,
			viewModel = farmerKycViewModel
		) {
			navController.navigateToIdProofSelectionScreen()
		}
	}

	navBaseComposable(route = KycIdProofRoute.KycIdProofSelection.route) {
		IdProofTypeSelectionScreen(
			finish = finish,
			viewModel = farmerKycViewModel
		) {
			navController.navigateToCapturePhoto(
				idProofType = farmerKycViewModel.idProofType,
				farmerId = farmerKycViewModel.farmerId
			)
		}
	}

	navBaseComposable(route = KycIdProofRoute.KycCapturePhoto.route) {
		CaptureIdProofPhoto(
			onBackPress = { navController.navigateUp() },
		) { ocrDetails ->
			navController.navigateToAddKycDetailsScreen(
				ocrDetails = ocrDetails,
				farmerId = farmerKycViewModel.farmerId,
				documentId = farmerKycViewModel.documentId,
				idProofType = farmerKycViewModel.idProofType
			)
		}
	}

	navBaseComposable(route = KycIdProofRoute.KycUpdateKycRecord.route) {
		AddKycDetailsScreen(
			onBackPress = { navController.navigateUp() },
			captureIdProofPhoto = { navController.navigateUp() }
		) { kycId, ocrDetails ->
			farmerKycViewModel.updateOcrDetails(ocrDetails)
			farmerKycViewModel.updateKycId(kycId)
			navController.navigateToCaptureOtp(
				farmerId = farmerKycViewModel.farmerId,
				name = farmerKycViewModel.farmerName,
				phoneNumber = farmerKycViewModel.farmerNumber,
				proofTypeId = farmerKycViewModel.documentId,
				farmerAuthId = farmerKycViewModel.farmerAuthId
			)
		}
	}

	navBaseComposable(route = KycIdProofRoute.KycCaptureOtp.route) {
		IdProofOtpScreen {
			navController.navigateToKyc(
				ocrDetails = farmerKycViewModel.ocrDetails,
				farmerId = farmerKycViewModel.farmerId,
				idProofType = farmerKycViewModel.idProofType,
				kycId = farmerKycViewModel.kycId,
				registerSale = false
			)
		}
	}

	navBaseComposable(route = KycIdProofRoute.KycSubmitKycDetails.route) {
		KycVerificationScreen(
			registerSale = false,
			retryCKyc = {
				farmerKycViewModel.updateIdProofType(it)
				navController.navigateToIdProofSelectionScreen()
			}
		) {
			navController.navigateToStartScreen()
		}
	}
}
