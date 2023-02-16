package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import javax.inject.Inject

class AddIdProofUseCase @Inject constructor(
	private val repository: KycRepository
) {

	suspend operator fun invoke(
		farmerId: Long,
		documentId: Long,
		idProofNumber: String
	) = repository.addIdProof(
		farmerId = farmerId,
		documentId = documentId,
		idProofNumber = idProofNumber
	)
}
