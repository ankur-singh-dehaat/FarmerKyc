package com.dehaat.kyc.framework.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottomSheetData(
	val cardType: String = "",
	val sampleUrl: String? = null,
	val id: Long? = null,
) : Parcelable
