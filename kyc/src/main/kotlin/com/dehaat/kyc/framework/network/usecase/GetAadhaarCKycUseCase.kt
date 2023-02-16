package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import javax.inject.Inject

class GetAadhaarCKycUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {
	suspend operator fun invoke(
		id: Long,
		dateOfBirth: String,
		gender: Char?,
		name: String,
		aadhaarLastFourDigits: String
	) = kycRepository.sendAadhaarCKyc(
		id = id,
		dateOfBirth = dateOfBirth,
		gender = gender,
		name = name,
		aadhaarLastFourDigits = aadhaarLastFourDigits
	)
}
