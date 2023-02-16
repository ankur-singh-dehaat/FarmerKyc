package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseAllDigitalDocument(
	@Json(name = "user_identity_proof") val userResponseIdentityProof: List<ResponseIdentityProof>,
	@Json(name = "user_bank_account") val userResponseBankAccount: List<ResponseBankAccount>,
	@Json(name = "user_identity_proof_status") val idProofVerificationStatus: String?,
	@Json(name = "user_bank_account_status") val bankVerificationStatus: String?
) {
	@JsonClass(generateAdapter = true)
	data class ResponseIdentityProof(
		@Json(name = "id") val id: Long,
		@Json(name = "identity_proof_number") val identityProofNumber: String,
		@Json(name = "back_url") val backUrl: String?,
		@Json(name = "front_url") val frontUrl: String?,
		@Json(name = "verification_status") val verificationStatus: String,
		@Json(name = "identity_proof_type") val identityProofType: Int,
		@Json(name = "meta_data") val metaData: MetaData?,
		@Json(name = "kyc_name") val kycName: String?,
		@Json(name = "updated_at") val updatedAt: String?
	)

	@JsonClass(generateAdapter = true)
	data class MetaData(
		@Json(name = "agent_notes") val agentNotes: List<String>?,
		@Json(name = "identity_master") val idMasterCKycResponse: IdMasterCKycResponse?
	) {
		@JsonClass(generateAdapter = true)
		data class IdMasterCKycResponse(
			@Json(name = "ckyc")
			val cKyc: CKyc?,
			@Json(name = "pan-ekyc")
			val panEKyc: PanEKyc?
		) {
			@JsonClass(generateAdapter = true)
			data class CKyc(
				@Json(name = "details")
				val details: Details?
			) {
				@JsonClass(generateAdapter = true)
				data class Details(
					@Json(name = "ckycNo") val cKycNo: String?
				)
			}

			@JsonClass(generateAdapter = true)
			data class PanEKyc(
				@Json(name = "panNumber")
				val panNumber: String?
			)
		}
	}

	@JsonClass(generateAdapter = true)
	data class ResponseBankAccount(

		@Json(name = "id") val id: String,
		@Json(name = "user_bank_account_details") val userBankAccountDetails: List<BankAccountDetail>,
		@Json(name = "account_number") val accountNumber: String,
		@Json(name = "ifsc_code") val ifscCode: String,
		@Json(name = "verification_status") val verificationStatus: String,
		@Json(name = "account_holder_name") val accountHolderName: String?,
		@Json(name = "meta_data") val metaData: MetaData?,
	) {
		@JsonClass(generateAdapter = true)
		data class BankAccountDetail(
			@Json(name = "id") val id: String,
			@Json(name = "front_url") val frontUrl: String,
			@Json(name = "verification_status") val verificationStatus: String,
			@Json(name = "user_bank_account") val userBankAccount: Int,
			@Json(name = "bank_document_type") val bankDocumentType: Int
		)
	}
}
