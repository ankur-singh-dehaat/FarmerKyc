package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import javax.inject.Inject

class SendKycOtpUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {
	suspend operator fun invoke(
		farmerId: Long
	) = kycRepository.sendKycOtp(farmerId = farmerId)

	suspend fun sendOtpViaCall(
		farmerAuthId: String
	) = kycRepository.sendOtpViaCall(farmerAuthId)
}
