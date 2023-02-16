package com.dehaat.kyc.features.recordsale.navigation

import com.dehaat.kyc.features.idproof.navigation.KycIdProofRoute

sealed class KycRecordSaleRoute(val route: String) {
	object KycSuccess : KycRecordSaleRoute(KycIdProofRoute.KycSuccess.route)

	object KycIdProofSelection : KycRecordSaleRoute(KycIdProofRoute.KycIdProofSelection.route)

	object KycCapturePhoto : KycRecordSaleRoute(KycIdProofRoute.KycCapturePhoto.route)

	object KycUpdateKycRecord : KycRecordSaleRoute(KycIdProofRoute.KycUpdateKycRecord.route)

	object KycCapturePaymentModes : KycRecordSaleRoute("kyc_id_proof_capture_payment_modes")

	object KycCaptureOtp : KycRecordSaleRoute("kyc_id_proof_capture_otp")

	object KycSubmitKycDetails : KycRecordSaleRoute(KycIdProofRoute.KycSubmitKycDetails.route)

	object RecordSaleSuccess: KycRecordSaleRoute("kyc_id_proof_record_sale_success")
}
