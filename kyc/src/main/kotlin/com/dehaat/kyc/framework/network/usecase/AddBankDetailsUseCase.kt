package com.dehaat.kyc.framework.network.usecase

import com.dehaat.kyc.KycRepository
import com.dehaat.kyc.feature.bankkyc.model.RequestBankDetailsEntity
import com.dehaat.kyc.framework.model.BankDetailsRequest
import com.dehaat.kyc.framework.model.BankDetailsUploadData
import javax.inject.Inject

class AddBankDetailsUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {
	suspend operator fun invoke(
		farmerId: String,
		id: String,
		requestBankDetailsEntity: RequestBankDetailsEntity
	) = kycRepository.addBankDetails(
		farmerId, id, requestBankDetailsEntity
	)

}

class UpdateBankDetailsUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {
	suspend operator fun invoke(
		farmerId: String,
		bankAccountDetailsId: String,
		requestUpdateBankDetailsEntity: RequestBankDetailsEntity
	) = kycRepository.updateBankDetails(
		farmerId,
		bankAccountDetailsId,
		requestUpdateBankDetailsEntity
	)

}

class AddBankDocumentsUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {

	suspend operator fun invoke(
		farmerId: Long,
		bankDetails: BankDetailsUploadData
	) = kycRepository.addBankDocuments(
		farmerId, bankDetails
	)
}

class UpdateBankDocumentsUseCase @Inject constructor(
	private val kycRepository: KycRepository
) {

	suspend operator fun invoke(
		farmerId: Long,
		bankDetails: BankDetailsRequest
	) = kycRepository.updateBankDocuments(
		farmerId,
		bankDetails
	)
}
