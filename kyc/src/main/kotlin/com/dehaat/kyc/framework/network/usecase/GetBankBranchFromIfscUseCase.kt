package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import javax.inject.Inject

class GetBankBranchFromIfscUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {

	suspend operator fun invoke(ifsc: String) = kycRepository.getBankBranchDetails(ifsc)
}
