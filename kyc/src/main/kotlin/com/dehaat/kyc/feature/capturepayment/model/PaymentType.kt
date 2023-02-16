package com.dehaat.kyc.feature.capturepayment.model

sealed class PaymentType(
	val imageUrl: String?,
	var amount: String,
	val id: Long
) {
	data class UPI(
		val icon: String?,
		var paymentAmount: String,
		val paymentId: Long
	) : PaymentType(icon, paymentAmount, paymentId)

	data class Cash(
		val icon: String?,
		var paymentAmount: String,
		val paymentId: Long
	) : PaymentType(icon, paymentAmount, paymentId)

	data class Credit(
		val icon: String?,
		var paymentAmount: String,
		val paymentId: Long
	) : PaymentType(icon, paymentAmount, paymentId)

	data class Card(
		val icon: String?,
		var paymentAmount: String,
		val paymentId: Long
	) : PaymentType(icon, paymentAmount, paymentId)

	data class CreditByLender(
		val icon: String?,
		var paymentAmount: String,
		val paymentId: Long
	) : PaymentType(icon, paymentAmount, paymentId)
}
