package com.dehaat.kyc.features

import com.dehaat.androidbase.utils.DateFormat
import com.dehaat.androidbase.utils.DateUtils
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.features.composables.kycsuccess.model.KycStatusUiState
import com.dehaat.kyc.features.model.DocumentDetails
import com.dehaat.kyc.framework.entity.model.DigitalDocumentEntity
import com.dehaat.kyc.framework.entity.model.MasterDataEntity
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.utils.ui.maskedIdProofNumber
import javax.inject.Inject

class FarmerKycMapper @Inject constructor() {

	fun toBottomSheetMap(
		documentEntity: List<MasterDataEntity.DocumentProofTypeEntity>?
	): Map<IdProofType, DocumentDetails> {
		val map = mutableMapOf<IdProofType, DocumentDetails>()
		documentEntity?.map {
			when (it.name) {
				AADHAAR_CARD -> map[IdProofType.Aadhaar] = DocumentDetails(
					documentId = it.id.toLongOrNull(),
					cardName = AADHAAR_CARD,
					sampleUrl = it.sampleImage
				)
				PAN_CARD -> map[IdProofType.Pan] = DocumentDetails(
					documentId = it.id.toLongOrNull(),
					cardName = PAN_CARD,
					sampleUrl = it.sampleImage
				)
				PASSBOOK -> map[IdProofType.Bank] = DocumentDetails(
					documentId = it.id.toLongOrNull(),
					cardName = PASSBOOK,
					sampleUrl = it.sampleImage
				)
			}
		}
		return map
	}

	fun toKycSuccessUiState(
		response: DigitalDocumentEntity.IdentityProofEntity
	) = with(response) {
		KycStatusUiState.VerifiedIdProof(
			name = kycName,
			cardType = idProofType,
			cardNumber = identityProofNumber.maskedIdProofNumber(),
			lastUpdatedOn = lastUpdatedOn?.let {
				DateUtils.fromToDateFormat(
					DateFormat.YYYY_D_MM_D_DD,
					DD_MMM_YYYY,
					it
				)
			}
		)
	}

	fun toBankDetails(
		response: List<DigitalDocumentEntity.BankProofEntity>?
	) = response?.map {
		KycStatusUiState.BankUiState.BankDetails(
			ifscCode = it.ifscCode,
			bankAccountNumber = it.accountNumber,
			kycId = it.kycId,
			accountHolderName = it.accountHolderName,
			verificationStatus = toVerificationStatus(it.verificationStatus),
			passBookPhoto = it.passbookPhoto
		)
	}.orEmpty()

	fun toVerificationStatus(
		verificationStatus: String?
	) = when (verificationStatus) {
		VerificationStatus.Pending.value -> VerificationStatus.Pending
		VerificationStatus.Approved.value -> VerificationStatus.Approved
		VerificationStatus.Rejected.value -> VerificationStatus.Rejected
		VerificationStatus.Verified.value -> VerificationStatus.Verified
		VerificationStatus.Submitted.value -> VerificationStatus.Submitted
		else -> VerificationStatus.Pending
	}

	companion object {
		private const val AADHAAR_CARD = "Aadhar Card"
		private const val PAN_CARD = "PAN Card"
		private const val PASSBOOK = "Passbook"
		private const val DD_MMM_YYYY = "dd-MMM-yyyy"
	}
}
