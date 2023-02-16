package com.dehaat.kyc.framework.entity.mapper

import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.kyc.framework.entity.model.DigitalDocumentEntity
import com.dehaat.kyc.framework.entity.model.MasterDataEntity
import com.dehaat.kyc.framework.model.ResponseAllDigitalDocument
import com.dehaat.kyc.framework.model.ResponseMasterData
import com.dehaat.kyc.model.IdProofType
import javax.inject.Inject

class FarmerKycEntityMapper @Inject constructor() {

	fun toMasterDataEntity(masterData: ResponseMasterData?) = masterData?.let {
		MasterDataEntity(
			documentEntity = getDocumentTypeEntity(
				idProofDocument = it.identityDocumentProofType,
				bankDocument = it.bankDocumentTypeDocument
			)
		)
	}

	private fun getDocumentTypeEntity(
		idProofDocument: List<ResponseMasterData.ResponseDocumentProofType>?,
		bankDocument: List<ResponseMasterData.ResponseDocumentProofType>?
	) = getProofType(
		idProofDocument
	).plus(
		getProofType(
			bankDocument
		)
	)

	private fun getProofType(
		document: List<ResponseMasterData.ResponseDocumentProofType>?
	) = document?.map {
		MasterDataEntity.DocumentProofTypeEntity(
			id = it.id,
			name = it.name,
			sampleImage = it.sampleImage,
			imageUrl = it.imageUrl,
			preview = it.preview,
			identityProofNumber = it.identityProofNumber,
		)
	}.orEmpty()

	fun toDigitalDocumentEntity(
		response: ResponseAllDigitalDocument?
	) = response?.let {
		DigitalDocumentEntity(
			identityProofEntity = toIdentityProofEntity(it.userResponseIdentityProof),
			bankProofEntity = toBankProofEntity(it.userResponseBankAccount),
			bankVerificationStatus = it.bankVerificationStatus,
			idProofVerificationStatus = it.idProofVerificationStatus
		)
	}

	private fun toBankProofEntity(
		bankProof: List<ResponseAllDigitalDocument.ResponseBankAccount>
	) = bankProof.map {
		DigitalDocumentEntity.BankProofEntity(
			kycId = it.id.toLongOrNull(),
			ifscCode = it.ifscCode,
			accountNumber = it.accountNumber,
			accountHolderName = it.accountHolderName,
			passbookPhoto = it.userBankAccountDetails.firstOrNull()?.frontUrl,
			verificationStatus = it.verificationStatus
		)
	}

	private fun toIdentityProofEntity(
		identityProof: List<ResponseAllDigitalDocument.ResponseIdentityProof>
	) = identityProof.firstOrNull()?.let {
		DigitalDocumentEntity.IdentityProofEntity(
			kycId = it.id,
			identityProofNumber = it.identityProofNumber,
			frontUrl = it.frontUrl,
			verificationStatus = it.verificationStatus,
			identityProofType = it.identityProofType,
			kycName = it.kycName,
			lastUpdatedOn = it.updatedAt,
			idProofType = if (it.metaData?.idMasterCKycResponse?.panEKyc.isNotNull()) {
				IdProofType.Pan
			} else {
				IdProofType.Aadhaar
			}
		)
	}
}
