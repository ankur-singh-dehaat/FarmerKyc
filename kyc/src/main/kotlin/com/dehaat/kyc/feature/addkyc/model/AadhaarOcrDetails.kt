package com.dehaat.kyc.feature.addkyc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class OcrDetails : Parcelable {
	@Parcelize
	data class AadhaarOcrDetails(
		val imageUrl: String = "",
		val aadhaarNumber: String = "",
		val name: String = "",
		val dateOfBirth: String = "",
		val gender: String = "",
		var isAadhaarNumberInValid: Boolean = false,
		var isNameInValid: Boolean = false,
		var isGenderInValid: Boolean = false,
		var isDateOfBirthInValid: Boolean = false,
	) : OcrDetails()

	@Parcelize
	data class PanOcrDetails(
		val imageUrl: String = "",
		val panNumber: String = "",
		val name: String = "",
		var isPanNumberInValid: Boolean = false,
		var isNameInValid: Boolean = false
	) : OcrDetails()
}
