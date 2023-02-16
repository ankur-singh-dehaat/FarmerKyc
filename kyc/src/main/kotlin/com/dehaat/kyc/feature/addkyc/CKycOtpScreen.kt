@file:OptIn(ExperimentalComposeUiApi::class)

package com.dehaat.kyc.feature.addkyc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.otp.OtpNotReceivedText
import com.dehaat.kyc.feature.otp.OtpPopUp
import com.dehaat.kyc.feature.otp.OtpView
import com.dehaat.kyc.feature.otp.ResendOtp
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Error110
import com.dehaat.kyc.ui.theme.Error30
import com.dehaat.kyc.ui.theme.LightGrey
import com.dehaat.kyc.ui.theme.Neutral60
import com.dehaat.kyc.ui.theme.TextBlack
import com.dehaat.kyc.ui.theme.TextDimens
import com.dehaat.kyc.ui.theme.TextGreen
import com.dehaat.kyc.ui.theme.textParagraphT2
import com.dehaat.kyc.utils.ui.Button
import com.dehaat.kyc.utils.ui.ShowError
import com.dehaat.kyc.utils.ui.ShowProgress
import com.dehaat.kyc.utils.ui.VerticalSpacer

@Composable
fun CKycOtpScreen(
	viewModel: CKycOtpViewModel = hiltViewModel(),
	navigateTocKyc: (String) -> Unit
) {
	val uiState by viewModel.uiState.collectAsState()

	Box(
		modifier = Modifier.fillMaxSize()
	) {
		ShowProgress(uiState.isLoading)

		if (uiState.isError) {
			uiState.errorMessage?.let {
				ShowError(it)
			}
		}

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
				text = stringResource(R.string.kyc_otp_validation),
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
				text = stringResource(R.string.otp_has_been_set_to_farmer, uiState.name, uiState.phone),
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

				OtpView(
					text = uiState.otp,
					maskedOtp = uiState.otp,
					isValidOtp = uiState.isValidOtp,
					onTextChanged = viewModel::enterOtp
				)

				Spacer(modifier = Modifier.height(Dimens.dp_24))

				OtpNotReceivedText(name = uiState.name)

				Spacer(modifier = Modifier.height(Dimens.dp_8))

				ResendOtp(
					uiState = uiState,
					sendOtpViaSms = viewModel::sendOtpViaSms,
					sendOtpViaCall = viewModel::sendOtpViaCall
				)

				Spacer(modifier = Modifier.height(Dimens.dp_24))

				Button(
					modifier = Modifier.fillMaxWidth(),
					title = stringResource(R.string.kyc_confirm),
					isEnabled = uiState.hashCode.isNotNull(),
					onClick = {
						uiState.hashCode?.let(navigateTocKyc)
					}
				)
			}

			VerticalSpacer(height = Dimens.dp_24)

		}

		OtpPopUp(
			modifier = Modifier.align(Alignment.TopCenter),
			isVisible = uiState.isInvalidOtp,
			title = stringResource(R.string.kyc_wrong_otp),
			textColor = Error110,
			backGroundColor = Error30
		)

		OtpPopUp(
			modifier = Modifier.align(Alignment.TopCenter),
			isVisible = uiState.reSendOtpViaSms,
			title = stringResource(R.string.kyc_otp_resent),
			textColor = LightGrey,
			backGroundColor = TextGreen
		)

		OtpPopUp(
			modifier = Modifier.align(Alignment.TopCenter),
			isVisible = uiState.reSendOtpViaCall,
			title = stringResource(R.string.kyc_otp_sent_over_call),
			textColor = LightGrey,
			backGroundColor = TextGreen
		)
	}
}
