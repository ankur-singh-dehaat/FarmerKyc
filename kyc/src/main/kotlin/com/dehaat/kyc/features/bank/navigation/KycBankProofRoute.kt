package com.dehaat.kyc.features.bank.navigation

import com.dehaat.kyc.features.idproof.navigation.KycIdProofRoute

sealed class KycBankProofRoute(val route: String) {
	object KycSuccess : KycBankProofRoute(KycIdProofRoute.KycSuccess.route)

	object KycVerifiedBankRecord : KycBankProofRoute("kyc_bank_proof_verified")

	object KycPendingBankRecord : KycBankProofRoute("kyc_bank_proof_pending")

	object KycCapturePhoto: KycBankProofRoute(KycIdProofRoute.KycCapturePhoto.route)

	object KycBankOtp: KycBankProofRoute("kyc_bank_otp")
}
