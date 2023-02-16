package com.dehaat.kyc.model

import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.androidbase.utils.DateFormat.YYYY_D_MM_D_DD
import com.dehaat.androidbase.utils.DateUtils
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.feature.bankkyc.model.BankDetailsState
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.feature.capturepayment.model.PaymentType
import com.dehaat.kyc.feature.ckyc.model.CKycCompleted
import com.dehaat.kyc.framework.model.AadhaarCKycBody
import com.dehaat.kyc.framework.model.AllDigitalDocumentEntity
import com.dehaat.kyc.framework.model.AvailablePaymentMode
import com.dehaat.kyc.framework.model.BankAccountDetailEntity
import com.dehaat.kyc.framework.model.BankAccountEntity
import com.dehaat.kyc.framework.model.IdentityProofEntity
import com.dehaat.kyc.framework.model.IdentityProofVerifiedData
import com.dehaat.kyc.framework.model.MasterDataEntity
import com.dehaat.kyc.framework.model.MetaDataEntity
import com.dehaat.kyc.framework.model.ResponseAadhaarOCR
import com.dehaat.kyc.framework.model.ResponseAllDigitalDocument
import com.dehaat.kyc.framework.model.ResponseMasterData
import com.dehaat.kyc.framework.model.ResponsePanOCR
import com.dehaat.kyc.utils.ui.maskedIdProofNumber
import com.dehaat.kyc.utils.ui.removeWhiteSpace
import javax.inject.Inject

class KycMapper @Inject constructor() {

	fun toAadhaarOcrDetails(
		aadhaarOcr: ResponseAadhaarOCR.Result,
		imagePath: String
	) = with(aadhaarOcr) {
		OcrDetails.AadhaarOcrDetails(
			imageUrl = imagePath,
			aadhaarNumber = aadhaarNumber.orEmpty().removeWhiteSpace(),
			name = fullName.orEmpty(),
			dateOfBirth = dob.orEmpty(),
			gender = gender.orEmpty()
		)
	}

	fun toPanOcrDetails(panOcr: ResponsePanOCR?, imagePath: String) = panOcr?.let {
		with(it) {
			OcrDetails.PanOcrDetails(
				imageUrl = imagePath,
				panNumber = panNo.orEmpty().removeWhiteSpace(),
				name = name.orEmpty()
			)
		}
	}

	fun toMasterDataEntity(masterData: ResponseMasterData?) = masterData?.let {
		MasterDataEntity(
			identityDocumentProofType = getDocumentTypeEntity(it.identityDocumentProofType),
			bankDocumentTypeDocument = getDocumentTypeEntity(it.bankDocumentTypeDocument)
		)
	}

	private fun getDocumentTypeEntity(bankDocumentTypeDocument: List<ResponseMasterData.ResponseDocumentProofType>?) =
		bankDocumentTypeDocument?.map {
			MasterDataEntity.DocumentProofTypeEntity(
				id = it.id,
				name = it.name,
				sampleImage = it.sampleImage,
				imageUrl = it.imageUrl,
				preview = it.preview,
				identityProofNumber = it.identityProofNumber,
			)
		}


	fun toAllDigitalDocumentEntity(
		response: ResponseAllDigitalDocument?
	) = response?.let {
		AllDigitalDocumentEntity(
			userResponseIdentityProof = toIdentityProofEntityList(it.userResponseIdentityProof),
			userResponseBankAccount = toUserResponseBankAccountList(it.userResponseBankAccount)
		)
	}

	private fun toIdentityProofEntityList(
		userResponseIdentityProofList: List<ResponseAllDigitalDocument.ResponseIdentityProof>
	) = userResponseIdentityProofList.map {
		IdentityProofEntity(
			id = it.id,
			identityProofNumber = it.identityProofNumber,
			backUrl = it.backUrl,
			frontUrl = it.frontUrl,
			verificationStatus = it.verificationStatus,
			identityProofType = it.identityProofType,
			metaData = toMetaDataEntity(
				it.metaData,
			),
			kycName = it.kycName,
			lastUpdatedOn = it.updatedAt
		)
	}

	private fun toMetaDataEntity(metaData: ResponseAllDigitalDocument.MetaData?) = metaData?.let {
		MetaDataEntity(
			agentNotes = it.agentNotes,
			idMasterCKycResponse = toIdMasterCKycResponse(it.idMasterCKycResponse),
			idProofType = if (it.idMasterCKycResponse?.panEKyc.isNotNull()) IdProofType.Pan else IdProofType.Aadhaar
		)
	}

	private fun toIdMasterCKycResponse(
		idMasterCKycResponse: ResponseAllDigitalDocument.MetaData.IdMasterCKycResponse?
	) = idMasterCKycResponse?.let {
		MetaDataEntity.IdMasterCKycResponse(
			details = it.cKyc?.details?.let {
				MetaDataEntity.IdMasterCKycResponse.Details(
					cKycNo = it.cKycNo
				)
			} ?: it.panEKyc?.let {
				MetaDataEntity.IdMasterCKycResponse.Details(
					cKycNo = it.panNumber
				)
			}
		)
	}

	private fun toUserResponseBankAccountList(
		userResponseBankAccount: List<ResponseAllDigitalDocument.ResponseBankAccount>
	) = userResponseBankAccount.map {
		BankAccountEntity(
			id = it.id,
			userBankAccountDetails = toBankAccountDetailEntityList(it.userBankAccountDetails),
			accountNumber = it.accountNumber,
			ifscCode = it.ifscCode,
			verificationStatus = it.verificationStatus,
			accountHolderName = it.accountHolderName,
			metaData = toMetaDataEntity(it.metaData)
		)
	}

	private fun toBankAccountDetailEntityList(
		userBankAccountDetails: List<ResponseAllDigitalDocument.ResponseBankAccount.BankAccountDetail>
	) = userBankAccountDetails.map {
		BankAccountDetailEntity(
			id = it.id,
			frontUrl = it.frontUrl,
			verificationStatus = it.verificationStatus,
			userBankAccount = it.userBankAccount,
			bankDocumentType = it.bankDocumentType
		)
	}

	fun toVerifiedData(idProofDetails: IdentityProofEntity?) = idProofDetails?.let {
		IdentityProofVerifiedData(
			it.identityProofType.toString(),
			it.identityProofNumber,
			it.frontUrl,
			it.id
		)
	}

	fun toAadhaarCKycBody(
		dateOfBirth: String,
		gender: String?,
		name: String,
		aadhaarLastFourDigits: String
	) = AadhaarCKycBody(
		docType = AADHAAR,
		dateOfBirth = dateOfBirth,
		gender = gender,
		name = name,
		aadhaarLastFourDigits = aadhaarLastFourDigits
	)

	fun toPaymentType(data: List<AvailablePaymentMode>?) = data?.map {
		when (it.name) {
			"UPI" -> PaymentType.UPI(
				icon = it.imageUrl,
				paymentAmount = "",
				paymentId = it.id
			)
			"Cash" -> PaymentType.Cash(
				icon = it.imageUrl,
				paymentAmount = "",
				paymentId = it.id
			)
			"Credit" -> PaymentType.Credit(
				icon = it.imageUrl,
				paymentAmount = "",
				paymentId = it.id
			)
			"Debit/Credit Card" -> PaymentType.Card(
				icon = it.imageUrl,
				paymentAmount = "",
				paymentId = it.id
			)
			"Credit by lender" -> PaymentType.CreditByLender(
				icon = it.imageUrl,
				paymentAmount = "",
				paymentId = it.id
			)
			else -> null
		}
	}.orEmpty()

	fun toCompletedKycState(details: IdentityProofEntity?) = details?.let {
		CKycCompleted(
			name = it.kycName,
			cardType = it.metaData?.idProofType,
			cardNumber = it.identityProofNumber.maskedIdProofNumber(),
			lastUpdatedOn = it.lastUpdatedOn?.let {
				DateUtils.fromToDateFormat(
					YYYY_D_MM_D_DD,
					"dd-MMM-yyyy",
					it
				)
			}
		)
	}

	fun toBankDetails(
		bankDetails: List<BankAccountEntity>?
	) = bankDetails?.map {
		BankDetailsState(
			bankName = null,
			ifscCode = it.ifscCode,
			accountNumber = it.accountNumber,
			isPassbookAttached = it.userBankAccountDetails.firstOrNull()?.frontUrl.isNotNull(),
			verificationStatus = getVerificationStatus(it.verificationStatus),
			kycId = it.id,
			accountHolderName = it.accountHolderName,
			passbookPhoto = it.userBankAccountDetails.firstOrNull()?.frontUrl
		)
	}.orEmpty()

	fun getVerificationStatus(status: String) = when (status) {
		VerificationStatus.Rejected.value -> VerificationStatus.Rejected
		VerificationStatus.Approved.value -> VerificationStatus.Approved
		VerificationStatus.Submitted.value -> VerificationStatus.Submitted
		VerificationStatus.Verified.value -> VerificationStatus.Verified
		else -> VerificationStatus.Pending
	}


	companion object {
		const val AADHAAR = "AADHAAR"
	}
}
