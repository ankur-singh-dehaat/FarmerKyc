package com.dehaat.kyc.utils.ui

import androidx.lifecycle.SavedStateHandle
import com.dehaat.kyc.model.IdProofType

object Constants {
	const val ID_PROOF_TYPE = "ID_PROOF_TYPE"
	const val IS_AADHAAR_C_KYC = "IS_AADHAAR_C_KYC"
	const val OTP_HASH_CODE = "OTP_HASH_CODE"
	const val NAME = "NAME"
	const val NUMBER = "NUMBER"
	const val FARMER_ID = "FARMER_ID"
	const val FARMER_AUTH_ID = "FARMER_AUTH_ID"
	const val DOCUMENT_TYPE = "DOCUMENT_TYPE"
	const val MASTER_DATA_TYPE = "MASTER_DATA_TYPE"
	const val BankDocumentType = "bank_document_type"
	const val IdentityProofType = "identity_proof_type"
	const val DOCUMENT_ID = "DOCUMENT_ID"
	const val KYC_ID = "KYC_ID"


	const val BANK_NUMBER_REGEX = "^\\d{9,18}$"
	const val IFSC_CODE_REGEX = "[A-Z]{4}0[A-Z0-9]{6}$"
	const val AADHAAR_REGEX = "^([0-9]){4}([0-9]){4}([0-9]){4}$"
	const val PAN_REGEX = "[A-Z]{5}[0-9]{4}[A-Z]{1}"
	const val DATE_OF_BIRTH_REGEX = "^(0?[1-9]|[12][0-9]|3[01])[/](0?[1-9]|1[012])[/]\\d{4}\$"
}

fun SavedStateHandle.getIdProofType() = this.get<IdProofType>(Constants.ID_PROOF_TYPE) ?: IdProofType.Aadhaar
