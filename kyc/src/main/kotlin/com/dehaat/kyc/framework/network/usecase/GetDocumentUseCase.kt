package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import com.dehaat.kyc.framework.repo.FarmerKycRepository
import javax.inject.Inject

class GetDocumentUseCase @Inject constructor(
	private val kycRepository: KycRepository,
	private val farmerKycRepository: FarmerKycRepository
) {
	suspend operator fun invoke(dataType: String) = kycRepository.getDocument(dataType)

	suspend fun getDocuments(dataType: String) = farmerKycRepository.getDocument(dataType)
}
