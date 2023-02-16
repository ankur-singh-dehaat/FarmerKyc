package com.dehaat.kyc.framework.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class RegisterSalePaymentModeRequest(
	@Json(name = "payment_mode")
	val id: Long,

	@Json(name = "amount")
	val amount: String
) : Parcelable
