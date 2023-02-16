package com.dehaat.kyc.features.idproof

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dehaat.kyc.features.FarmerKycViewModel
import com.dehaat.kyc.features.idproof.navigation.KycIdProofNavigation
import com.dehaat.kyc.ui.theme.KycAppTheme
import com.dehaat.kyc.utils.ui.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KycIdProofActivity : ComponentActivity() {

	private val viewModel by viewModels<FarmerKycViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			KycAppTheme {
				KycIdProofNavigation(
					finish = ::finish,
					farmerKycViewModel = viewModel
				)
			}
		}
	}

	companion object {
		internal data class Args(
			val name: String,
			val number: String,
			val farmerAuthId: String,
			val farmerId: Long,
		) {
			fun build(context: Context) = Intent(
				context,
				KycIdProofActivity::class.java
			).apply {
				putExtra(Constants.NAME, name)
				putExtra(Constants.NUMBER, number)
				putExtra(Constants.FARMER_AUTH_ID, farmerAuthId)
				putExtra(Constants.FARMER_ID, farmerId)
				putExtra(Constants.MASTER_DATA_TYPE, Constants.IdentityProofType)
			}
		}
	}
}
