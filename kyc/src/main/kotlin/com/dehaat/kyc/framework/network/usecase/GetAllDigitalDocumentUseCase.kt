package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import com.dehaat.kyc.framework.repo.FarmerKycRepository
import javax.inject.Inject

class GetAllDigitalDocumentUseCase @Inject constructor(
	private val kycRepository: KycRepository,
	private val farmerKycRepository: FarmerKycRepository
) {
	suspend operator fun invoke(farmerId: Long) = kycRepository.getAllDigitalDocument(farmerId)

	suspend fun getAllDigitalDocuments(
		farmerId: Long
	) = farmerKycRepository.getAllDigitalDocument(
		farmerId = farmerId
	)
}
