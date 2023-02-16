package com.dehaat.kyc.features.composables.otp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehaat.kyc.R
import com.dehaat.kyc.features.recordsale.composable.otp.RecordSaleOtpViewModel
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Neutral60
import com.dehaat.kyc.ui.theme.TextBlack
import com.dehaat.kyc.ui.theme.TextDimens
import com.dehaat.kyc.ui.theme.textParagraphT2
import com.dehaat.kyc.utils.ui.Button
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.onClick

@Composable
fun RecordSaleOtpScreen(
	viewModel: RecordSaleOtpViewModel = hiltViewModel(),
	continueWithoutInsurance: onClick,
	updateOtpHash: (String) -> Unit,
	onSubmitOtpClick: (String) -> Unit
) {

	val uiState by viewModel.uiState.collectAsState()
	OtpScreen(
		uiState = uiState,
		isRecordSale = true,
		continueWithoutInsurance = continueWithoutInsurance,
		sendOtpViaCall = viewModel::sendOtpViaCall,
		sendOtpViaSms = viewModel::sendOtpViaSms,
		onSubmitOtpClick = { uiState.hashCode?.let(onSubmitOtpClick) },
		enterOtp = viewModel::enterOtp
	)

	LaunchedEffect(Unit) {
		viewModel.otpHashCode.collect(updateOtpHash)
	}
}

@Composable
fun OtpView(
	farmerName: String,
	phoneNumber: String,
	sendOtpViaSms: () -> Unit,
	sendOtpViaCall: () -> Unit,
	isSubmitEnabled: Boolean,
	onSubmitOtpClick: onClick,
	continueWithoutInsurance: onClick,
	otpBox: @Composable () -> Unit,
	errorPopUp: @Composable (BoxScope) -> Unit
) = Box(modifier = Modifier.fillMaxSize()) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
	) {

		VerticalSpacer(height = Dimens.dp_48)

		Image(
			modifier = Modifier.align(Alignment.CenterHorizontally),
			painter = painterResource(id = R.drawable.ic_otp_validate),
			contentDescription = ""
		)

		VerticalSpacer(height = Dimens.dp_28)

		Text(
			modifier = Modifier.align(Alignment.CenterHorizontally),
			text = stringResource(id = R.string.kyc_otp_validation),
			style = TextStyle(
				color = TextBlack,
				fontSize = TextDimens.sp_20,
				fontWeight = FontWeight.Medium,
				lineHeight = TextDimens.sp_22,
				fontFamily = FontFamily(Font(R.font.notosans))
			)
		)

		VerticalSpacer(height = Dimens.dp_24)

		Text(
			modifier = Modifier
				.padding(horizontal = Dimens.dp_28)
				.align(Alignment.CenterHorizontally),
			text = buildString {
				append(stringResource(id = R.string.otp_sent_to))
				append(" $farmerName ")
				append(stringResource(id = R.string.on_phone_number))
				append("(${phoneNumber})")
			},
			style = textParagraphT2(Neutral60),
			textAlign = TextAlign.Center
		)

		VerticalSpacer(height = Dimens.dp_24)

		Column(
			modifier = Modifier.padding(
				horizontal = Dimens.dp_32,
				vertical = Dimens.dp_20
			)
		) {
			Spacer(modifier = Modifier.height(Dimens.dp_24))

			otpBox()

			Spacer(modifier = Modifier.height(Dimens.dp_24))

			OtpNotReceivedText(name = farmerName)

			Spacer(modifier = Modifier.height(Dimens.dp_8))

			ResendOtp(
				sendOtpViaSms = sendOtpViaSms,
				sendOtpViaCall = sendOtpViaCall
			)

			Spacer(modifier = Modifier.height(Dimens.dp_24))

			Button(
				modifier = Modifier.fillMaxWidth(),
				title = stringResource(id = R.string.kyc_confirm),
				isEnabled = isSubmitEnabled,
				onClick = onSubmitOtpClick
			)
		}

		VerticalSpacer(height = Dimens.dp_24)

		ContinueWithoutInsurance(continueWithoutInsurance)
	}
	errorPopUp(this)
}
