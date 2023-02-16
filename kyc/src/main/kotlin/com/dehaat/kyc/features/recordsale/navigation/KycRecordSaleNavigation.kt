package com.dehaat.kyc.features.recordsale.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dehaat.kyc.feature.captureimage.CaptureIdProofPhoto
import com.dehaat.kyc.feature.capturepayment.CapturePaymentScreen
import com.dehaat.kyc.feature.ckycverification.KycVerificationScreen
import com.dehaat.kyc.feature.idprooftypeselection.IdProofTypeSelectionScreen
import com.dehaat.kyc.feature.updatekyc.AddKycDetailsScreen
import com.dehaat.kyc.features.FarmerKycViewModel
import com.dehaat.kyc.features.composables.kycsuccess.KycEntryScreen
import com.dehaat.kyc.features.composables.navigation.navigateToAddKycDetailsScreen
import com.dehaat.kyc.features.composables.navigation.navigateToCapturePaymentModes
import com.dehaat.kyc.features.composables.navigation.navigateToCapturePhoto
import com.dehaat.kyc.features.composables.navigation.navigateToIdProofSelectionScreen
import com.dehaat.kyc.features.composables.navigation.navigateToKyc
import com.dehaat.kyc.features.composables.navigation.navigateToRecordSaleOtp
import com.dehaat.kyc.features.composables.navigation.navigateToRecordSaleSuccess
import com.dehaat.kyc.features.composables.otp.RecordSaleOtpScreen
import com.dehaat.kyc.features.recordsale.composable.otp.RecordSaleOtpViewModel
import com.dehaat.kyc.features.recordsale.composable.recordsalesuccess.RecordSaleSuccessScreen
import com.dehaat.kyc.navigation.KycExecutor
import com.dehaat.kyc.utils.ui.navBaseComposable
import com.dehaat.kyc.utils.ui.onClick

@Composable
fun KycRecordSaleNavigation(
	finish: onClick,
	farmerKycViewModel: FarmerKycViewModel,
	navController: NavHostController = rememberNavController(),
	recordSaleSuccess: onClick,
	continueWithoutInsurance: onClick
) = NavHost(
	navController = navController,
	startDestination = KycRecordSaleRoute.KycSuccess.route
) {
	navBaseComposable(route = KycRecordSaleRoute.KycSuccess.route) {
		KycEntryScreen(
			finish = finish,
			viewModel = farmerKycViewModel
		) {
			navController.navigateToIdProofSelectionScreen()
		}
	}

	navBaseComposable(route = KycRecordSaleRoute.KycIdProofSelection.route) {
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

	navBaseComposable(route = KycRecordSaleRoute.KycCapturePhoto.route) {
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

	navBaseComposable(route = KycRecordSaleRoute.KycUpdateKycRecord.route) {
		AddKycDetailsScreen(
			onBackPress = { navController.navigateUp() },
			captureIdProofPhoto = { navController.navigateUp() }
		) { kycId, ocrDetails ->
			farmerKycViewModel.updateKycId(kycId)
			farmerKycViewModel.updateOcrDetails(ocrDetails)
			navController.navigateToCapturePaymentModes(farmerKycViewModel.payableAmount)
		}
	}

	navBaseComposable(route = KycRecordSaleRoute.KycCapturePaymentModes.route) {
		CapturePaymentScreen(
			onBackPress = { navController.navigateUp() }
		) {
			navController.navigateToRecordSaleOtp(
				farmerName = farmerKycViewModel.farmerName,
				phoneNumber = farmerKycViewModel.farmerNumber,
				farmerId = farmerKycViewModel.farmerId,
				paymentModes = it
			)
		}
	}

	navBaseComposable(route = KycRecordSaleRoute.KycCaptureOtp.route) {
		val viewModel = hiltViewModel<RecordSaleOtpViewModel>()
		with(viewModel) {
			farmerKycViewModel.registerSaleRequest?.let { saleReq ->
				updateRegisterSaleRequest(saleReq)
				sendOtpViaSms(false)
			} ?: KycExecutor.getRegisterSaleRequest(viewModel.paymentModes)?.let { saleReq ->
				updateRegisterSaleRequest(saleReq)
				farmerKycViewModel.updateSaleRequest(saleReq)
				sendOtpViaSms(false)
			}
		}
		RecordSaleOtpScreen(
			viewModel = viewModel,
			continueWithoutInsurance = continueWithoutInsurance,
			updateOtpHash = farmerKycViewModel::updateOtpHashCode
		) {
			navController.navigateToKyc(
				ocrDetails = farmerKycViewModel.ocrDetails,
				farmerId = farmerKycViewModel.farmerId,
				idProofType = farmerKycViewModel.idProofType,
				kycId = farmerKycViewModel.kycId,
				registerSale = true,
				registerSaleRequest = farmerKycViewModel.registerSaleRequest
			)
		}
	}

	navBaseComposable(route = KycRecordSaleRoute.KycSubmitKycDetails.route) {
		KycVerificationScreen(
			registerSale = true,
			continueWithoutInsurance = continueWithoutInsurance,
			recordSalesSuccess = {
				navController.navigateToRecordSaleSuccess()
			},
			retryCKyc = {
				farmerKycViewModel.updateIdProofType(it)
				navController.navigateToIdProofSelectionScreen()
			}
		)
	}

	navBaseComposable(route = KycRecordSaleRoute.RecordSaleSuccess.route) {
		RecordSaleSuccessScreen(recordSaleSuccess)
	}
}
