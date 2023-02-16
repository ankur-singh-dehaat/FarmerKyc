package com.dehaat.kyc.feature.bankkyc.model

import com.dehaat.androidbase.helper.orFalse

data class BankDetailsValidationState(
	var isAccountNumberValid: Boolean? = null,
	var isConfirmAccountNumberValid: Boolean? = null,
	var isIfscCodeValid: Boolean? = null,
	var isAccountHolderNameValid: Boolean? = null,
	var isPassBookPhotoValid: Boolean? = null,
	var bankAndBranchName: String? = null,

	var accountNumber: String = "",
	var confirmAccountNumber: String = "",
	var ifscCode: String = "",
	var passbookPhoto: String? = null,
	val bankAccountHolderName: String = ""
) {
	val isValid: Boolean
		get() = isAccountNumberValid.orFalse() &&
				isConfirmAccountNumberValid.orFalse() &&
				isIfscCodeValid.orFalse() &&
				isAccountHolderNameValid.orFalse() &&
				isPassBookPhotoValid.orFalse()
}
