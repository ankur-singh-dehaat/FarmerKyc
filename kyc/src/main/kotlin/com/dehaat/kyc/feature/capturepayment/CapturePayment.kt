package com.dehaat.kyc.feature.capturepayment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehaat.kyc.R
import com.dehaat.kyc.framework.model.RegisterSalePaymentModeRequest
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.ErrorRed
import com.dehaat.kyc.ui.theme.LightGrey
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.TextBlack
import com.dehaat.kyc.ui.theme.TextDimens
import com.dehaat.kyc.ui.theme.TextGreen
import com.dehaat.kyc.ui.theme.textParagraphT2
import com.dehaat.kyc.utils.ui.Button
import com.dehaat.kyc.utils.ui.HorizontalSpacer
import com.dehaat.kyc.utils.ui.PrimaryAppBar
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.onClick

@Composable
fun CapturePaymentScreen(
	onBackPress: onClick,
	viewModel: CapturePaymentViewModel = hiltViewModel(),
	onOtpValidation: (List<RegisterSalePaymentModeRequest>) -> Unit
) {
	val uiState by viewModel.uiState.collectAsState()

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			PrimaryAppBar(
				title = stringResource(R.string.kyc_payment_capture),
				onBackPress = onBackPress
			)
		},
		bottomBar = {
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.padding(all = Dimens.dp_16),
				title = stringResource(R.string.kyc_verify_with_otp),
				isEnabled = uiState.validAmount,
				onClick = {
					onOtpValidation(uiState.selectedPaymentModes)
				}
			)
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.padding(paddingValues)
				.background(LightGrey)
				.fillMaxWidth()
		) {

			VerticalSpacer(height = Dimens.dp_20)

			Row(
				modifier = Modifier.padding(horizontal = Dimens.dp_12),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = stringResource(R.string.kyc_payable_amount),
					style = textParagraphT2(TextBlack)
				)

				HorizontalSpacer(width = Dimens.dp_16)

				Text(
					text = "₹ ${viewModel.paymentAmount}",
					style = TextStyle(
						color = TextGreen,
						fontSize = TextDimens.sp_20,
						fontWeight = FontWeight.Bold,
						lineHeight = TextDimens.sp_24,
						fontFamily = FontFamily(Font(R.font.notosans))
					)
				)
			}

			VerticalSpacer(height = Dimens.dp_8)

			Row(
				modifier = Modifier.padding(horizontal = Dimens.dp_12),
				verticalAlignment = Alignment.CenterVertically
			) {

				Text(
					text = stringResource(R.string.kyc_pending_amount),
					style = textParagraphT2(TextBlack)
				)

				HorizontalSpacer(width = Dimens.dp_16)

				Text(
					text = "₹ ${uiState.pendingAmount}",
					style = TextStyle(
						color = ErrorRed,
						fontSize = TextDimens.sp_20,
						fontWeight = FontWeight.Bold,
						lineHeight = TextDimens.sp_24,
						fontFamily = FontFamily(Font(R.font.notosans))
					)
				)
			}

			VerticalSpacer(height = Dimens.dp_26)

			Divider(modifier = Modifier.height(Dimens.dp_4))

			VerticalSpacer(height = Dimens.dp_22)

			Text(
				modifier = Modifier.padding(Dimens.dp_18),
				text = stringResource(R.string.kyc_select_payment_method),
				style = TextStyle(
					color = TextBlack,
					fontSize = TextDimens.sp_20,
					fontWeight = FontWeight.Normal,
					lineHeight = TextDimens.sp_22,
					fontFamily = FontFamily(Font(R.font.notosans))
				)
			)

			VerticalSpacer(height = Dimens.dp_20)

			LazyColumn {
				items(uiState.availablePaymentMode) {
					PaymentMode(
						title = it.displayName,
						amount = it.amount
					) { amount ->
						viewModel.updateAmount(amount, it.id)
					}

					VerticalSpacer(height = Dimens.dp_20)
				}
			}
		}
	}
}

@Composable
fun PaymentMode(
	title: String,
	amount: String,
	addAmount: (String) -> Unit
) = Column(
	modifier = Modifier
		.fillMaxWidth()
		.padding(horizontal = Dimens.dp_20)
		.background(
			shape = RoundedCornerShape(Dimens.dp_10),
			color = Color.White
		)
		.padding(vertical = Dimens.dp_10, horizontal = Dimens.dp_26)
) {
	var isChecked by remember { mutableStateOf(false) }
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				isChecked = isChecked.not()
			},
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			Image(
				painter = painterResource(id = R.drawable.ic_payment),
				contentDescription = "",
				modifier = Modifier.size(Dimens.dp_40),
			)

			HorizontalSpacer(width = Dimens.dp_26)

			Text(
				text = title, style = TextStyle(
					color = TextBlack,
					fontSize = TextDimens.sp_16,
					fontWeight = FontWeight.Normal,
					lineHeight = TextDimens.sp_18,
					fontFamily = FontFamily(Font(R.font.notosans))
				)
			)
		}

		Checkbox(
			checked = isChecked,
			onCheckedChange = {
				isChecked = isChecked.not()
			}
		)
	}

	if (isChecked) {
		OutlinedTextField(
			value = amount,
			onValueChange = {
				addAmount(it)
			},
			modifier = Modifier
				.fillMaxWidth(),
			singleLine = true,
			colors = TextFieldDefaults.textFieldColors(
				textColor = Neutral100,
				backgroundColor = Color.White
			),
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
		)
	}
}
