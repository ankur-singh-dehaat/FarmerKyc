package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.OcrRepository
import javax.inject.Inject

class AadhaarOcrUseCase @Inject constructor(
	private val ocrRepository: OcrRepository
) {

	suspend operator fun invoke(
		frontUrl: String?,
		farmerId: Long,
		backUrl: String? = null
	) = ocrRepository.getAadhaarOcr(frontUrl, backUrl, farmerId)
}
