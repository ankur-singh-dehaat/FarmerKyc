package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import javax.inject.Inject

class GetPanCKycUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {
	suspend operator fun invoke(
		id: Long,
		panNumber: String
	) = kycRepository.sendPanCKyc(
		id = id,
		panNumber = panNumber
	)
}
