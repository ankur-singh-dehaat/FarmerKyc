package com.dehaat.kyc.feature.bankkyc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankVerifiedDetails(
	val accountNumber: String?,
	val accountHolderName: String?,
	val ifscCode: String?,
	val passbookPhoto: String?
): Parcelable
