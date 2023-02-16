package com.dehaat.kyc.features.composables.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import com.dehaat.kyc.feature.addkyc.AddKycViewModel
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.feature.bankkyc.model.BankVerifiedDetails
import com.dehaat.kyc.feature.ckycverification.CKycViewModel
import com.dehaat.kyc.features.bank.KycBankProofActivity
import com.dehaat.kyc.features.bank.navigation.KycBankProofRoute
import com.dehaat.kyc.features.idproof.navigation.KycIdProofRoute
import com.dehaat.kyc.features.recordsale.KycRecordSaleActivity
import com.dehaat.kyc.features.recordsale.composable.otp.RecordSaleOtpViewModel
import com.dehaat.kyc.features.recordsale.navigation.KycRecordSaleRoute
import com.dehaat.kyc.framework.model.RegisterSalePaymentModeRequest
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.utils.ui.Constants
import com.dehaat.kyc.utils.ui.navigateTo


fun NavHostController.navigateToStartScreen() = navigateTo(
	route = KycIdProofRoute.KycSuccess.route
)

fun NavHostController.navigateToIdProofSelectionScreen() = navigateTo(
	route = KycIdProofRoute.KycIdProofSelection.route
)

fun NavHostController.navigateToCapturePhoto(
	idProofType: IdProofType,
	farmerId: Long
) = this.navigateTo(
	route = KycIdProofRoute.KycCapturePhoto.route,
	args = bundleOf(
		Constants.ID_PROOF_TYPE to idProofType,
		Constants.FARMER_ID to farmerId
	)
)

fun NavHostController.navigateToAddKycDetailsScreen(
	ocrDetails: OcrDetails,
	farmerId: Long,
	documentId: Long,
	idProofType: IdProofType
) = navigateTo(
	route = KycIdProofRoute.KycUpdateKycRecord.route,
	args = bundleOf(
		AddKycViewModel.OCR_DETAILS to ocrDetails,
		Constants.FARMER_ID to farmerId,
		Constants.DOCUMENT_ID to documentId,
		Constants.ID_PROOF_TYPE to idProofType
	)
)

fun NavHostController.navigateToCapturePaymentModes(
	payableAmount: Double
) = navigateTo(
	route = KycRecordSaleRoute.KycCapturePaymentModes.route,
	args = bundleOf(
		KycRecordSaleActivity.PAYABLE_AMOUNT to payableAmount
	)
)

fun NavHostController.navigateToRecordSaleOtp(
	farmerName: String,
	phoneNumber: String,
	farmerId: Long,
	paymentModes: List<RegisterSalePaymentModeRequest>
) = navigateTo(
	route = KycRecordSaleRoute.KycCaptureOtp.route,
	args = bundleOf(
		Constants.NAME to farmerName,
		Constants.NUMBER to phoneNumber,
		Constants.FARMER_ID to farmerId,
		RecordSaleOtpViewModel.PAYMENT_MODES to paymentModes
	)
)

fun NavHostController.navigateToCaptureOtp(
	farmerId: Long,
	name: String,
	phoneNumber: String,
	proofTypeId: Long,
	farmerAuthId: String
) = navigateTo(
	route = KycIdProofRoute.KycCaptureOtp.route,
	args = bundleOf(
		Constants.FARMER_ID to farmerId,
		Constants.NAME to name,
		Constants.NUMBER to phoneNumber,
		Constants.DOCUMENT_TYPE to proofTypeId,
		Constants.FARMER_AUTH_ID to farmerAuthId
	)
)

fun NavHostController.navigateToKyc(
	ocrDetails: OcrDetails?,
	farmerId: Long,
	idProofType: IdProofType,
	kycId: Long,
	registerSale: Boolean,
	registerSaleRequest: RegisterSaleRequest? = null
) = navigateTo(
	route = KycIdProofRoute.KycSubmitKycDetails.route,
	args = bundleOf(
		Constants.FARMER_ID to farmerId,
		CKycViewModel.OCR_DETAILS to ocrDetails,
		Constants.IS_AADHAAR_C_KYC to (idProofType is IdProofType.Aadhaar),
		Constants.KYC_ID to kycId,
		CKycViewModel.REGISTER_SALE to registerSale,
		CKycViewModel.REGISTER_SALE_REQUEST to registerSaleRequest
	)
)

fun NavHostController.navigateToRecordSaleSuccess() = navigateTo(
	route = KycRecordSaleRoute.RecordSaleSuccess.route
)

fun NavHostController.navigateToVerifiedBankRecord(
	bankVerifiedDetails: BankVerifiedDetails
) = navigateTo(
	route = KycBankProofRoute.KycVerifiedBankRecord.route,
	args = bundleOf(
		KycBankProofActivity.BANK_VERIFIED_DETAILS to bankVerifiedDetails
	)
)

fun NavHostController.navigateToPendingBankRecord(kycId: Long? = null) = navigateTo(
	route = KycBankProofRoute.KycPendingBankRecord.route,
	args = bundleOf(
		Constants.KYC_ID to kycId
	)
)

fun NavHostController.navigateToBankRecordOtp() = navigateTo(
	route = KycBankProofRoute.KycBankOtp.route
)



