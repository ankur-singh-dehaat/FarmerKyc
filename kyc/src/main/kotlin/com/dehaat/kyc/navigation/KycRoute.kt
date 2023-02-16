package com.dehaat.kyc.navigation

import androidx.core.os.bundleOf
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.feature.ckycverification.CKycViewModel
import com.dehaat.kyc.feature.otp.OtpViewModel
import com.dehaat.kyc.features.recordsale.KycRecordSaleActivity
import com.dehaat.kyc.framework.model.RegisterSalePaymentModeRequest
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.utils.ui.Constants

sealed class KycRoute(val route: String) {

	object IdProofTypeSelection : KycRoute("add_kyc_route")

	object CKycComplete : KycRoute("add_kyc_ckyc_complete_route")

	object BankKyc : KycRoute("add_kyc_bank_kyc")

	object BankVerification : KycRoute("add_kyc_bank_verification")

	object BankOtp : KycRoute("add_kyc_bank_otp")

	object BankVerifiedScreen : KycRoute("add_kyc_verified_bank")

	object CapturePhoto : KycRoute("add_kyc_capture_id_proof_photo_route") {
		fun getArgs(
			idProofType: IdProofType
		) = bundleOf(
			Constants.ID_PROOF_TYPE to idProofType
		)
	}

	object UpdateKyc : KycRoute("add_kyc_update_kyc_route")

	object CapturePayment : KycRoute("add_kyc_capture_payment_route") {
		fun getArgs(payableAmount: Double) =
			bundleOf(KycRecordSaleActivity.PAYABLE_AMOUNT to payableAmount.toString())
	}

	object OtpValidation : KycRoute("add_kyc_otp_validation_route") {
		fun getArgs(
			paymentModes: List<RegisterSalePaymentModeRequest>,
			farmerId: Long,
			name: String,
			phoneNumber: String
		) = bundleOf(
			OtpViewModel.PAYMENT_MODES to paymentModes,
			OtpViewModel.FARMER_ID to farmerId,
			OtpViewModel.NAME to name,
			OtpViewModel.NUMBER to phoneNumber
		)
	}

	object KycVerification : KycRoute("add_kyc_verification_route") {
		fun getArgs(
			ocrDetails: OcrDetails?,
			farmerId: Long,
			name: String,
			phoneNumber: String,
			idProofType: IdProofType,
			proofId: Long,
			registerSale: Boolean,
			isKycSuccessful: Boolean,
			registerSaleRequest: RegisterSaleRequest?
		) = bundleOf(
			CKycViewModel.FARMER_ID to farmerId,
			CKycViewModel.NAME to name,
			CKycViewModel.NUMBER to phoneNumber,
			CKycViewModel.OCR_DETAILS to ocrDetails,
			Constants.IS_AADHAAR_C_KYC to (idProofType is IdProofType.Aadhaar),
			CKycViewModel.PROOF_ID to proofId,
			//Constants.OTP_HASH_CODE to otpHash,
			CKycViewModel.REGISTER_SALE to registerSale,
			CKycViewModel.KYC_COMPLETE to isKycSuccessful,
			CKycViewModel.REGISTER_SALE_REQUEST to registerSaleRequest
		)
	}

	object KycVerificationOtp : KycRoute("add_kyc_otp_verification_route")

	object RecordSaleComplete : KycRoute("add_kyc_record_sale_complete")
}
