package com.dehaat.kyc.framework.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IdentityProofVerifiedData(
	val cardType: String,
	val cardNumber: String,
	var ipPhoto: String?,
	val identityProofId: Long
) : Parcelable
