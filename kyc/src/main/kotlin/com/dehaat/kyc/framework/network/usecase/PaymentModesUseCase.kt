package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import javax.inject.Inject

class PaymentModesUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {

	suspend operator fun invoke() = kycRepository.getPaymentModes()
}
