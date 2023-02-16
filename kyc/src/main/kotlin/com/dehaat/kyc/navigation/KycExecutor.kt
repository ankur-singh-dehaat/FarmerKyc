package com.dehaat.kyc.navigation

import android.content.Context
import com.dehaat.kyc.KycActivity.Companion.Args
import com.dehaat.kyc.features.bank.KycBankProofActivity
import com.dehaat.kyc.features.idproof.KycIdProofActivity
import com.dehaat.kyc.features.recordsale.KycRecordSaleActivity
import com.dehaat.kyc.framework.model.RegisterSalePaymentModeRequest
import com.dehaat.kyc.framework.model.RegisterSaleRequest

object KycExecutor {

	private var kycInterface: KycInterface? = null
	internal var awsBucket: String = ""

	fun openKyc(
		context: Context,
		name: String,
		number: String,
		farmerAuthId: String,
		farmerId: Long,
		payableAmount: Double = 0.0,
		registerSale: Boolean = true,
		bankVerification: Boolean = false,
		awsBucket: String,
		kycInterface: KycInterface
	) {
		this.kycInterface = kycInterface
		this.awsBucket = awsBucket
		context.startActivity(
			Args(
				name = name,
				number = number,
				farmerAuthId = farmerAuthId,
				farmerId = farmerId,
				payableAmount = payableAmount,
				registerSale = registerSale,
				bankVerification = bankVerification
			).build(context)
		)
	}

	fun openBankKyc(
		context: Context,
		name: String,
		number: String,
		farmerAuthId: String,
		farmerId: Long,
		awsBucket: String,
		kycInterface: KycInterface
	) {
		this.awsBucket = awsBucket
		this.kycInterface = kycInterface
		context.startActivity(
			KycBankProofActivity.Companion.Args(
				name = name,
				number = number,
				farmerAuthId = farmerAuthId,
				farmerId = farmerId
			).build(context)
		)
	}

	fun openIdProofKyc(
		context: Context,
		name: String,
		number: String,
		farmerAuthId: String,
		farmerId: Long,
		awsBucket: String,
		kycInterface: KycInterface
	) {
		this.awsBucket = awsBucket
		this.kycInterface = kycInterface
		context.startActivity(
			KycIdProofActivity.Companion.Args(
				name = name,
				number = number,
				farmerAuthId = farmerAuthId,
				farmerId = farmerId
			).build(context)
		)
	}

	fun openRecordSaleKyc(
		context: Context,
		name: String,
		number: String,
		farmerAuthId: String,
		farmerId: Long,
		payableAmount: Double,
		awsBucket: String,
		kycInterface: KycInterface
	) {
		this.awsBucket = awsBucket
		this.kycInterface = kycInterface
		context.startActivity(
			KycRecordSaleActivity.Companion.Args(
				name = name,
				number = number,
				farmerAuthId = farmerAuthId,
				farmerId = farmerId,
				payableAmount = payableAmount
			).build(context)
		)
	}

	internal suspend fun sendNavigationAnalytics(
		route: String
	) = kycInterface?.sendScreenAnalytics(route)

	internal fun getRegisterSaleRequest(
		paymentModes: List<RegisterSalePaymentModeRequest>
	) = kycInterface?.getRegisterSaleRequest(paymentModes)

	internal fun continueWithoutInsurance() = kycInterface?.continueWithoutInsurance()

	internal suspend fun getPreSignedUrl(imagePath: String?) =
		kycInterface?.getPreSignedUrl(imagePath)

	internal fun saleRecordSuccess() = kycInterface?.saleRecordSuccess()
}

interface KycInterface {
	suspend fun sendScreenAnalytics(route: String)

	fun getRegisterSaleRequest(paymentModes: List<RegisterSalePaymentModeRequest>): RegisterSaleRequest?

	fun continueWithoutInsurance()

	fun saleRecordSuccess()

	suspend fun getPreSignedUrl(imagePath: String?): String?
}
