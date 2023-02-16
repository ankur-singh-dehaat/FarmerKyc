package com.dehaat.kyc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class IdProofType : Parcelable {
	@Parcelize
	object Aadhaar : IdProofType()

	@Parcelize
	object Pan : IdProofType()

	@Parcelize
	object Bank: IdProofType()
}
