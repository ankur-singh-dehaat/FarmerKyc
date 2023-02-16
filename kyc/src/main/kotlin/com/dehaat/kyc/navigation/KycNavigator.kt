package com.dehaat.kyc.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import com.dehaat.kyc.feature.addkyc.AddKycViewModel
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.feature.bankkyc.BankVerificationViewModel
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.utils.ui.Constants
import com.dehaat.kyc.utils.ui.navigateTo

fun NavHostController.navigateToKycVerificationScreen(
	ocrDetails: OcrDetails?,
	farmerId: Long,
	name: String,
	phoneNumber: String,
	idProofType: IdProofType,
	proofId: Long,
	registerSale: Boolean,
	isKycSuccessful: Boolean,
	registerSaleRequest: RegisterSaleRequest?
) = navigateTo(
	route = KycRoute.KycVerification.route,
	args = KycRoute.KycVerification.getArgs(
		ocrDetails = ocrDetails,
		farmerId = farmerId,
		name = name,
		phoneNumber = phoneNumber,
		idProofType = idProofType,
		proofId = proofId,
		registerSale = registerSale,
		isKycSuccessful = isKycSuccessful,
		registerSaleRequest = registerSaleRequest
	)
)

fun NavHostController.navigateToKycOtpScreen(
	farmerId: Long,
	name: String,
	phoneNumber: String,
	proofTypeId: Long,
	farmerAuthId: String
) = navigateTo(
	route = KycRoute.KycVerificationOtp.route,
	args = bundleOf(
		Constants.FARMER_ID to farmerId,
		Constants.NAME to name,
		Constants.NUMBER to phoneNumber,
		Constants.DOCUMENT_TYPE to proofTypeId,
		Constants.FARMER_AUTH_ID to farmerAuthId
	)
)

fun NavHostController.navigateToIdProofTypeSelectionScreen(
	idProofType: IdProofType? = IdProofType.Aadhaar
) {
	popBackStack()
	navigateTo(
		route = KycRoute.IdProofTypeSelection.route,
		args = bundleOf(
			Constants.ID_PROOF_TYPE to idProofType
		)
	)
}

fun NavHostController.navigateToCapturePhotoScreen(
	idProofType: IdProofType
) = navigateTo(
	route = KycRoute.CapturePhoto.route,
	args = bundleOf(
		Constants.ID_PROOF_TYPE to idProofType
	)
)

fun NavHostController.navigateToUpdateKycScreen(
	ocrDetails: OcrDetails,
	farmerId: Long,
	proofType: Long,
	idProofType: IdProofType
) = navigateTo(
	route = KycRoute.UpdateKyc.route,
	args = bundleOf(
		AddKycViewModel.OCR_DETAILS to ocrDetails,
		Constants.FARMER_ID to farmerId,
		Constants.ID_PROOF_TYPE to proofType,
		Constants.ID_PROOF_TYPE to idProofType
	)
)

fun NavHostController.navigateToUpdateBankKycScreen(
	farmerId: Long,
	farmerName: String,
	kycId: String? = null,
	verificationStatus: String? = null
) = navigateTo(
	route = KycRoute.BankVerification.route,
	args = bundleOf(
		Constants.FARMER_ID to farmerId,
		Constants.NAME to farmerName,
		BankVerificationViewModel.KYC_ID to kycId,
		BankVerificationViewModel.VERIFICATION_STATUS to verificationStatus
	)
)
