package com.dehaat.kyc.framework.model;

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RegisterSaleRequest(

	@Json(name = "order_details")
	val orderDetails: List<SaleItemRequest>,

	@Json(name = "total_price")
	val totalPrice: String,

	@Json(name = "payment_details")
	val paymentDetails: List<RegisterSalePaymentModeRequest>,

	@Json(name = "insurance")
	var insurance: CropInsuranceRequest? = null,

	@Json(name = "otp_hash_for_credit_by_lender")
	val otpHashForCreditByLender: String? = null,

	@Json(name = "is_from_pinelabs")
	val isFromPineLabs: Boolean = false,

	@Json(name = "coupon")
	val coupon: Coupon? = null,

	@Json(name = "net_price")
	val netPrice: Double? = null,
) : Parcelable {

	@Parcelize
	@JsonClass(generateAdapter = true)
	data class SaleItemRequest(
		@Json(name = "price")
		val price: String,

		@Json(name = "quantity")
		val quantity: Int,

		@Json(name = "product_variant")
		val variantId: Long
	) : Parcelable

	@Parcelize
	@JsonClass(generateAdapter = true)
	data class Coupon(
		@Json(name = "code") var code: String? = null,
		@Json(name = "discount") var discount: Double? = null
	) : Parcelable

	@Parcelize
	@JsonClass(generateAdapter = true)
	data class CropInsuranceRequest(
		@Json(name = "otp_hash")
		var otpHash: String? = null,

		@Json(name = "cost_to_farmer")
		val costToFarmer: String,

		@Json(name = "policy_order_details")
		val insuranceOrderDetails: List<InsuranceOrderRequest>,

		@Json(name = "farmer_identity_proof")
		var identityProofId: Long? = null,

		@Json(name = "farmer_bank_account")
		val farmerBankProofId: Long? = null
	) : Parcelable {

		@Parcelize
		@JsonClass(generateAdapter = true)
		data class InsuranceOrderRequest(
			@Json(name = "product_variant")
			val variantId: Long,

			@Json(name = "group_insurance_product_id")
			val insuranceId: Long,

			@Json(name = "units_purchased")
			val insuranceQuantity: Int,

			@Json(name = "cost_to_farmer")
			val costToFarmer: String,

			@Json(name = "qr_codes")
			val qrCodes: List<String>
		) : Parcelable
	}
}
