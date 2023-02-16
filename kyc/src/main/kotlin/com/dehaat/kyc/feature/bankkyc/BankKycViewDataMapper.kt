package com.dehaat.kyc.feature.bankkyc

import com.dehaat.kyc.feature.bankkyc.model.BankBranchDetailsViewData
import com.dehaat.kyc.feature.bankkyc.model.BankDetailsValidationState
import com.dehaat.kyc.feature.bankkyc.model.BankDetailsVerifiedData
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.framework.model.BankAccountEntity
import com.dehaat.kyc.framework.model.ResponseBankBranchDetails
import javax.inject.Inject

class BankKycViewDataMapper @Inject constructor() {
	fun toBankVerifiedData(data: BankAccountEntity?) = data?.run {
		BankDetailsVerifiedData(
			accountNumber,
			userBankAccountDetails.firstOrNull()?.frontUrl,
			ifscCode,
			accountHolderName,
			id.toLongOrNull()
		)
	}

	fun toBankDetailsValidationState(
		bankDetailsVerifiedData: BankDetailsVerifiedData?
	) = bankDetailsVerifiedData?.let {
		BankDetailsValidationState(
			isAccountNumberValid = it.accountNumber.isNotEmpty(),
			isConfirmAccountNumberValid = it.accountNumber.isNotEmpty(),
			isIfscCodeValid = it.ifsc.isNotEmpty(),
			isAccountHolderNameValid = it.bankAccountHolderName?.let { true },
			isPassBookPhotoValid = it.passbookPhoto?.let { true },
			accountNumber = it.accountNumber,
			confirmAccountNumber = it.accountNumber,
			ifscCode = it.ifsc,
			passbookPhoto = it.passbookPhoto,
			bankAccountHolderName = it.bankAccountHolderName.orEmpty()
		)
	} ?: BankDetailsValidationState()

	fun toBankBranchDetailsViewData(data: ResponseBankBranchDetails?) =
		BankBranchDetailsViewData(data?.bank, data?.branch)

	fun toVerificationStatus(verificationStatus: String?) = when (verificationStatus) {
		VerificationStatus.Approved.value -> VerificationStatus.Approved
		VerificationStatus.Rejected.value -> VerificationStatus.Rejected
		VerificationStatus.Verified.value -> VerificationStatus.Verified
		VerificationStatus.Submitted.value -> VerificationStatus.Submitted
		else -> VerificationStatus.Pending
	}

}
