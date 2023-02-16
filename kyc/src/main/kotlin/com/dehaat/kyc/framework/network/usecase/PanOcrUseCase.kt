package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.OcrRepository
import javax.inject.Inject

class PanOcrUseCase @Inject constructor(
	private val ocrRepository: OcrRepository
) {

	suspend operator fun invoke(
		frontUrl: String?,
		farmerId: Long
	) = ocrRepository.getPanOcr(frontUrl, farmerId)
}
