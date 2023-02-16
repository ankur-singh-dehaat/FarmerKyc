package com.dehaat.kyc.features.recordsale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dehaat.kyc.features.FarmerKycViewModel
import com.dehaat.kyc.features.recordsale.navigation.KycRecordSaleNavigation
import com.dehaat.kyc.navigation.KycExecutor
import com.dehaat.kyc.ui.theme.KycAppTheme
import com.dehaat.kyc.utils.ui.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KycRecordSaleActivity : ComponentActivity() {

	private val viewModel by viewModels<FarmerKycViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			KycAppTheme {
				KycRecordSaleNavigation(
					finish = ::finish,
					farmerKycViewModel = viewModel,
					recordSaleSuccess = {
						KycExecutor.saleRecordSuccess()
						finish()
					}
				) {
					KycExecutor.continueWithoutInsurance()
					finish()
				}
			}
		}
	}

	companion object {
		const val PAYABLE_AMOUNT = "PAYABLE_AMOUNT"

		internal data class Args(
			val name: String,
			val number: String,
			val farmerAuthId: String,
			val farmerId: Long,
			val payableAmount: Double
		) {
			fun build(context: Context) = Intent(
				context,
				KycRecordSaleActivity::class.java
			).apply {
				putExtra(Constants.NAME, name)
				putExtra(Constants.NUMBER, number)
				putExtra(Constants.FARMER_AUTH_ID, farmerAuthId)
				putExtra(Constants.FARMER_ID, farmerId)
				putExtra(PAYABLE_AMOUNT, payableAmount)
				putExtra(Constants.MASTER_DATA_TYPE, Constants.IdentityProofType)
			}
		}
	}
}
