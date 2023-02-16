package com.dehaat.kyc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dehaat.androidbase.helper.orFalse
import com.dehaat.androidbase.helper.orZero
import com.dehaat.kyc.feature.KycViewModel
import com.dehaat.kyc.navigation.KycExecutor
import com.dehaat.kyc.navigation.KycNavigation
import com.dehaat.kyc.ui.theme.KycAppTheme
import com.dehaat.kyc.utils.ui.Constants.FARMER_AUTH_ID
import com.dehaat.kyc.utils.ui.Constants.FARMER_ID
import com.dehaat.kyc.utils.ui.Constants.NAME
import com.dehaat.kyc.utils.ui.Constants.NUMBER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KycActivity : ComponentActivity() {

	private val viewModel by viewModels<KycViewModel>()

	private val args by lazy { getArgs(intent.extras) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			KycAppTheme {
				KycNavigation(
					finish = ::finish,
					kycViewModel = viewModel,
					registerSale = args.registerSale,
					bankVerification = args.bankVerification,
					continueWithoutInsurance = {
						KycExecutor.continueWithoutInsurance()
						finish()
					},
					saleRecordSuccess = {
						KycExecutor.saleRecordSuccess()
						finish()
					}
				)
			}
		}
	}

	companion object {
		const val PAYABLE_AMOUNT = "PAYABLE_AMOUNT"
		const val REGISTER_SALE = "REGISTER_SALE"
		const val C_KYC = "C_KYC"
		const val BANK_VERIFICATION = "BANK_VERIFICATION"

		private fun getArgs(bundle: Bundle?) = with(bundle) {
			Args(
				name = this?.getString(NAME, "").orEmpty(),
				number = this?.getString(NUMBER, "").orEmpty(),
				farmerAuthId = this?.getString(FARMER_AUTH_ID, "").orEmpty(),
				farmerId = this?.getLong(FARMER_ID, 0).orZero(),
				payableAmount = this?.getDouble(PAYABLE_AMOUNT, 0.0) ?: 0.0,
				registerSale = this?.getBoolean(REGISTER_SALE, false).orFalse(),
				cKyc = this?.getBoolean(C_KYC, false).orFalse(),
				bankVerification = this?.getBoolean(BANK_VERIFICATION, false).orFalse(),
			)
		}

		internal data class Args(
			val name: String,
			val number: String,
			val farmerAuthId: String,
			val farmerId: Long,
			val payableAmount: Double,
			val registerSale: Boolean,
			val cKyc: Boolean = false,
			val bankVerification: Boolean = false
		) {
			fun build(context: Context) = Intent(
				context,
				KycActivity::class.java
			).apply {
				putExtra(NAME, name)
				putExtra(NUMBER, number)
				putExtra(FARMER_AUTH_ID, farmerAuthId)
				putExtra(FARMER_ID, farmerId)
				putExtra(PAYABLE_AMOUNT, payableAmount)
				putExtra(REGISTER_SALE, registerSale)
				putExtra(C_KYC, cKyc)
				putExtra(BANK_VERIFICATION, bankVerification)
			}
		}
	}
}
