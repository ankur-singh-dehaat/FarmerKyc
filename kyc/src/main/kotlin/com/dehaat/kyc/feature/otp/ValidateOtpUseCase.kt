package com.dehaat.kyc.feature.otp

import com.dehaat.kyc.KycRepository
import javax.inject.Inject

class ValidateOtpUseCase @Inject constructor(private val repository: KycRepository) {

	suspend operator fun invoke(
		otp: String,
		farmerId: Long
	) = repository.validateOtp(otp, farmerId)
}
