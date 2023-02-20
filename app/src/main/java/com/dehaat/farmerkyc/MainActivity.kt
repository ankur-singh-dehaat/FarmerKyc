package com.dehaat.farmerkyc

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.dehaat.farmerkyc.ui.theme.FarmerKycTheme
import com.dehaat.kyc.framework.model.RegisterSalePaymentModeRequest
import com.dehaat.kyc.navigation.KycExecutor
import com.dehaat.kyc.navigation.KycInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			OpenKycModule(
				farmerName = "",
				farmerNumber = "",
				farmerAuthId = "",
				farmerId = 89L,
				awsBucket = " "
			)
		}
	}
}

@Composable
fun Greeting(name: String) {
	Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	FarmerKycTheme {
		Greeting("Android")
	}
}

@Composable
fun OpenKycModule(
	farmerName: String,
	farmerNumber: String,
	farmerAuthId: String,
	farmerId: Long,
	awsBucket: String,
	context: Context = LocalContext.current
) {
	KycExecutor.openIdProofKyc(
		context,
		farmerName,
		farmerNumber,
		farmerAuthId,
		farmerId,
		awsBucket,
		kycInterface = object : KycInterface {
			override suspend fun sendScreenAnalytics(route: String) = Unit

			override fun getRegisterSaleRequest(
				paymentModes: List<RegisterSalePaymentModeRequest>
			) = null

			override fun continueWithoutInsurance() = Unit

			override fun saleRecordSuccess() = Unit

			override suspend fun getPreSignedUrl(imagePath: String?) = null
		}
	)
}
