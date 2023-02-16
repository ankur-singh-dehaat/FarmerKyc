package com.dehaat.kyc.features.idproof.navigation

sealed class KycIdProofRoute(val route: String) {
	object KycSuccess : KycIdProofRoute("kyc_id_proof_kyc_success")

	object KycIdProofSelection : KycIdProofRoute("kyc_id_proof_selection")

	object KycCapturePhoto : KycIdProofRoute("kyc_id_proof_capture_photo")

	object KycUpdateKycRecord : KycIdProofRoute("kyc_id_proof_update_record")

	object KycCaptureOtp : KycIdProofRoute("kyc_id_proof_capture_otp")

	object KycSubmitKycDetails : KycIdProofRoute("kyc_id_proof_kyc")
}
