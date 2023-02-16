package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import javax.inject.Inject

class RegisterSaleUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {

	suspend operator fun invoke(
		farmerId: Long,
		request: RegisterSaleRequest?,
		requestOtp: Boolean
	) = kycRepository.registerSale(
		farmerId = farmerId, request = request, requestOtp = requestOtp
	)
}
