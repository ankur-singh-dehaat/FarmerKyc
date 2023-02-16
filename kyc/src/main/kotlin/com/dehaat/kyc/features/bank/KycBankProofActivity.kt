package com.dehaat.kyc.features.bank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dehaat.kyc.features.FarmerKycViewModel
import com.dehaat.kyc.features.bank.navigation.KycBankProofNavigation
import com.dehaat.kyc.ui.theme.KycAppTheme
import com.dehaat.kyc.utils.ui.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KycBankProofActivity : ComponentActivity() {

	private val viewModel by viewModels<FarmerKycViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			KycAppTheme {
				KycBankProofNavigation(
					finish = ::finish,
					farmerKycViewModel = viewModel
				)
			}
		}
	}

	companion object {
		const val BANK_VERIFIED_DETAILS = "BANK_VERIFIED_DETAILS"
		internal data class Args(
			val name: String,
			val number: String,
			val farmerAuthId: String,
			val farmerId: Long,
		) {
			fun build(context: Context) = Intent(
				context,
				KycBankProofActivity::class.java
			).apply {
				putExtra(Constants.NAME, name)
				putExtra(Constants.NUMBER, number)
				putExtra(Constants.FARMER_AUTH_ID, farmerAuthId)
				putExtra(Constants.FARMER_ID, farmerId)
				putExtra(Constants.MASTER_DATA_TYPE, Constants.BankDocumentType)
			}
		}
	}
}
