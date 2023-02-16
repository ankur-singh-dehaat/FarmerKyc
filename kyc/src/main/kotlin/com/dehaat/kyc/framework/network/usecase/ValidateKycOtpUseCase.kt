package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import javax.inject.Inject

class ValidateKycOtpUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {
	suspend operator fun invoke(otp: String, farmerId: Long) = kycRepository.validateKycOtp(
		otp = otp,
		farmerId = farmerId
	)
}
