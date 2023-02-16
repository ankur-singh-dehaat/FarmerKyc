package com.dehaat.kyc.feature.bankkyc.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BankBranchDetailsViewData(
	val bank: String?,
	val branch: String?,
) : Parcelable
