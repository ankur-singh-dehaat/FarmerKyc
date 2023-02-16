package com.dehaat.kyc.feature.ckycverification

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.ckycverification.model.Status
import com.dehaat.kyc.feature.otp.ContinueWithoutInsurance
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.ui.theme.DehaatGreen
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.ErrorRed
import com.dehaat.kyc.ui.theme.Neutral10
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.Neutral60
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.TextBlack
import com.dehaat.kyc.ui.theme.textButtonB1
import com.dehaat.kyc.ui.theme.textHeadingH5
import com.dehaat.kyc.ui.theme.textParagraphT1
import com.dehaat.kyc.ui.theme.textParagraphT3
import com.dehaat.kyc.utils.ui.HorizontalSpacer
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.onClick

@Composable
fun KycVerificationScreen(
	registerSale: Boolean,
	viewModel: CKycViewModel = hiltViewModel(),
	context: Context = LocalContext.current,
	continueWithoutInsurance: onClick = {},
	recordSalesSuccess: onClick = {},
	retryCKyc: (IdProofType) -> Unit,
	cKycSuccess: onClick = {}
) = Column(
	modifier = Modifier
		.fillMaxSize()
		.padding(horizontal = Dimens.dp_24)
) {
	val uiState by viewModel.uiState.collectAsState()

	VerticalSpacer(height = Dimens.dp_40)

	Text(
		text = stringResource(R.string.kyc_status_of_kyc_verification),
		style = textHeadingH5(TextBlack)
	)

	VerticalSpacer(height = Dimens.dp_24)

	if (uiState.isLoading) {
		if (registerSale) {
			SuccessBox {
				CircularProgressIndicator(
					modifier = Modifier.size(Dimens.dp_40),
					color = DehaatGreen
				)

				HorizontalSpacer(width = Dimens.dp_16)

				Text(
					text = stringResource(R.string.kyc_record_sale_in_progress),
					style = textParagraphT1(TextBlack)
				)
			}

			VerticalSpacer(height = Dimens.dp_16)
		}
		SuccessBox {
			CircularProgressIndicator(
				modifier = Modifier.size(Dimens.dp_40),
				color = DehaatGreen
			)

			HorizontalSpacer(width = Dimens.dp_16)

			Text(
				text = stringResource(R.string.kyc_verification_in_progress),
				style = textParagraphT1(TextBlack)
			)
		}
	} else {
		if (registerSale) {
			when (uiState.saleStatus) {
				Status.Success -> {
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.border(
								width = Dimens.dp_1,
								color = Neutral10,
								shape = RoundedCornerShape(Dimens.dp_8)
							)
							.padding(Dimens.dp_16),
						verticalAlignment = Alignment.CenterVertically
					) {
						Image(
							modifier = Modifier.size(Dimens.dp_40),
							painter = painterResource(id = R.drawable.ic_success),
							contentDescription = "Success"
						)

						HorizontalSpacer(width = Dimens.dp_16)

						Text(
							text = stringResource(R.string.kyc_otp_verification_successfull),
							style = textParagraphT1(TextBlack)
						)
					}
				}
				Status.Failed -> {
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.border(
								width = Dimens.dp_1,
								color = ErrorRed,
								shape = RoundedCornerShape(Dimens.dp_8)
							)
							.padding(Dimens.dp_16), verticalAlignment = Alignment.CenterVertically
					) {
						Image(
							modifier = Modifier.size(Dimens.dp_40),
							painter = painterResource(id = R.drawable.kyc_error),
							contentDescription = "Success"
						)

						HorizontalSpacer(width = Dimens.dp_16)

						Text(
							text = stringResource(R.string.kyc_record_sale_failed),
							style = textParagraphT1(Neutral100)
						)
					}
				}
			}

			VerticalSpacer(height = Dimens.dp_16)
		}

		when (uiState.cKycStatus) {
			Status.Success -> SuccessBox {
				Image(
					modifier = Modifier.size(Dimens.dp_40),
					painter = painterResource(id = R.drawable.ic_success),
					contentDescription = "Success"
				)

				HorizontalSpacer(width = Dimens.dp_16)

				Text(
					text = stringResource(R.string.kyc_verification_successful),
					style = textParagraphT1(TextBlack)
				)
			}
			Status.Failed -> {
				RetryCKyc(
					idProofType = viewModel.idProofType,
					retryCKyc = { retryCKyc(it) }
				)
			}
		}

		if (registerSale && uiState.saleStatus is Status.Failed) {
			VerticalSpacer(height = Dimens.dp_24)
			ContinueWithoutInsurance(continueWithoutInsurance)
		}
	}

	LaunchedEffect(Unit) {
		viewModel.recordSaleStatus.collect {
			if (it is Status.Success) recordSalesSuccess()
		}
	}

	LaunchedEffect(Unit) {
		viewModel.cKycComplete.collect {
			if (it) cKycSuccess()
		}
	}

	LaunchedEffect(Unit) {
		viewModel.errorMessage.collect {
			Toast.makeText(context, it, Toast.LENGTH_LONG).show()
		}
	}
}

@Composable
fun SuccessBox(
	content: @Composable () -> Unit
) = Row(
	modifier = Modifier
		.fillMaxWidth()
		.border(width = Dimens.dp_1, color = Neutral10, shape = RoundedCornerShape(Dimens.dp_8))
		.padding(Dimens.dp_16),
	verticalAlignment = Alignment.CenterVertically
) {
	content()
}

@Composable
fun RetryCKyc(
	idProofType: IdProofType,
	retryCKyc: (IdProofType) -> Unit
) = Column(
	modifier = Modifier
		.fillMaxWidth()
		.border(width = Dimens.dp_1, color = ErrorRed, shape = RoundedCornerShape(Dimens.dp_8))
		.padding(Dimens.dp_16),
) {
	Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
		Image(
			modifier = Modifier.size(Dimens.dp_40),
			painter = painterResource(id = R.drawable.kyc_error),
			contentDescription = "Success"
		)

		HorizontalSpacer(width = Dimens.dp_16)

		Text(
			text = stringResource(R.string.kyc_verification_failed),
			style = textParagraphT1(Neutral100)
		)
	}

	VerticalSpacer(height = Dimens.dp_8)

	Text(
		text = stringResource(R.string.kyc_retry_cKyc),
		style = textParagraphT3(Neutral60)
	)

	VerticalSpacer(height = Dimens.dp_16)

	Text(
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				retryCKyc(IdProofType.Aadhaar)
			}
			.background(
				color = Primary100,
				shape = RoundedCornerShape(Dimens.dp_10)
			)
			.padding(vertical = Dimens.dp_12),
		text = stringResource(R.string.kyc_try_again_with_aadhaar),
		style = textButtonB1(Color.White),
		textAlign = TextAlign.Center
	)

	VerticalSpacer(height = Dimens.dp_16)

	Text(
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				retryCKyc(IdProofType.Pan)
			}
			.border(
				width = Dimens.dp_1,
				color = Primary100,
				shape = RoundedCornerShape(Dimens.dp_10)
			)
			.padding(vertical = Dimens.dp_12),
		text = stringResource(R.string.kyc_or_try_with_pan), style = textButtonB1(Primary100),
		textAlign = TextAlign.Center
	)
}
