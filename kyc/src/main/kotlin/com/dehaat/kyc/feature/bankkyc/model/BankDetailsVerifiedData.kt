package com.dehaat.kyc.feature.bankkyc.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BankDetailsVerifiedData(
	val accountNumber: String,
	var passbookPhoto: String?,
	val ifsc: String,
	val bankAccountHolderName: String? = null,
	val id: Long? = null
) : Parcelable
