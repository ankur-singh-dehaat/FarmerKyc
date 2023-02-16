package com.dehaat.kyc.utils.ui

const val DD_MM_YYYY_SLASH = "dd/MM/yyyy"

fun String.removeWhiteSpace() = this.trim().replace(" ", "")

fun String?.toDoubleOrZero() = this?.toDoubleOrNull() ?: 0.0

fun String.uppercaseLettersOrDigits() = filter { it.isLetterOrDigit() }.uppercase()

fun String.maskedIdProofNumber() = this.mapIndexed { index, c ->
	when {
		index.plus(1) % 4 == 0 && index != length.minus(1) -> "X "
		length - index > 4 -> "X"
		else -> c
	}
}.joinToString("")
