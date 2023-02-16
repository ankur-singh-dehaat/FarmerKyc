@file:OptIn(ExperimentalComposeUiApi::class)

package com.dehaat.kyc.feature.otp

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.kyc.R
import com.dehaat.kyc.features.composables.otp.model.OtpUiState
import com.dehaat.kyc.model.InsuranceErrorResponse
import com.dehaat.kyc.ui.theme.Color0B8040
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Error110
import com.dehaat.kyc.ui.theme.Error30
import com.dehaat.kyc.ui.theme.LightGrey
import com.dehaat.kyc.ui.theme.LightGreyText
import com.dehaat.kyc.ui.theme.LightRed
import com.dehaat.kyc.ui.theme.LineColor
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.Neutral60
import com.dehaat.kyc.ui.theme.TextBlack
import com.dehaat.kyc.ui.theme.TextDimens
import com.dehaat.kyc.ui.theme.TextGreen
import com.dehaat.kyc.ui.theme.mediumShape
import com.dehaat.kyc.ui.theme.text14Sp
import com.dehaat.kyc.ui.theme.text16Sp
import com.dehaat.kyc.ui.theme.textMedium12Sp
import com.dehaat.kyc.ui.theme.textParagraphT2
import com.dehaat.kyc.utils.ui.Button
import com.dehaat.kyc.utils.ui.HorizontalSpacer
import com.dehaat.kyc.utils.ui.ShowProgress
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.onClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(
	viewModel: OtpViewModel,
	continueWithoutInsurance: onClick,
	navigateToPayments: onClick
) {
	val uiState by viewModel.uiState.collectAsState()

	Box(
		modifier = Modifier.fillMaxSize()
	) {
		ShowProgress(uiState.isLoading)

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
				text = "OTP has been sent to ${uiState.name} on phone number (${uiState.phone}).",
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
					title = stringResource(id = R.string.kyc_confirm),
					isEnabled = uiState.hashCode.isNotNull(),
					onClick = navigateToPayments
				)
			}

			VerticalSpacer(height = Dimens.dp_24)

			ContinueWithoutInsurance(continueWithoutInsurance)

		}

		OtpPopUp(
			modifier = Modifier.align(Alignment.TopCenter),
			isVisible = uiState.isInvalidOtp,
			title = stringResource(id = R.string.kyc_wrong_otp),
			textColor = Error110,
			backGroundColor = Error30
		)

		OtpPopUp(
			modifier = Modifier.align(Alignment.TopCenter),
			isVisible = uiState.reSendOtpViaSms,
			title = stringResource(id = R.string.kyc_otp_resent),
			textColor = LightGrey,
			backGroundColor = TextGreen
		)

		OtpPopUp(
			modifier = Modifier.align(Alignment.TopCenter),
			isVisible = uiState.reSendOtpViaCall,
			title = stringResource(id = R.string.kyc_otp_sent_over_call),
			textColor = LightGrey,
			backGroundColor = TextGreen
		)

		/*uiState.errorResponse?.let {
			val message = getInsuranceErrorMessage(it)
			OtpPopUp(
				modifier = Modifier
					.align(Alignment.TopCenter)
					.padding(horizontal = Dimens.dp_8),
				isVisible = uiState.isError,
				title = message,
				textColor = Error110,
				backGroundColor = Error30
			)
		}*/
	}
}

private const val INSURANCE_TYPE_FREE = "free"
private const val INSURANCE_TYPE_PAID = "paid"
private const val INSURED_CART_VALUE_EXCEEDED = "insured_cart_value_exceeded"
private const val INSUFFICIENT_INSURED_CART_VALUE = "insufficient_insured_cart_value"

@Composable
private fun getInsuranceErrorMessage(
	insuranceError: InsuranceErrorResponse
) = when (insuranceError.insuranceType) {
	INSURANCE_TYPE_FREE -> {
		when (insuranceError.reason) {
			INSURED_CART_VALUE_EXCEEDED -> stringResource(
				R.string.insured_card_value_exceeded_free_insurance,
				insuranceError.cartThresholdData.maximumCartValue
			)
			INSUFFICIENT_INSURED_CART_VALUE -> stringResource(
				R.string.insufficient_insured_cart_value_free_insurance,
				insuranceError.cartThresholdData.minimumCartValue
			)
			else -> ""
		}
	}
	INSURANCE_TYPE_PAID -> {
		when (insuranceError.reason) {
			INSURED_CART_VALUE_EXCEEDED -> stringResource(
				R.string.insured_card_value_exceeded_paid_insurance,
				insuranceError.cartThresholdData.maximumCartValue
			)
			INSUFFICIENT_INSURED_CART_VALUE -> stringResource(
				R.string.insufficient_insured_cart_value_paid_insurance,
				insuranceError.cartThresholdData.minimumCartValue
			)
			else -> ""
		}
	}
	else -> ""
}

@Composable
fun OtpPopUp(
	modifier: Modifier,
	isVisible: Boolean,
	title: String,
	textColor: Color,
	backGroundColor: Color
) = AnimatedVisibility(
	visible = isVisible,
	enter = slideInVertically(),
	exit = slideOutVertically()
) {
	Text(
		modifier = modifier
			.fillMaxWidth()
			.background(color = backGroundColor)
			.padding(vertical = Dimens.dp_10),
		text = title,
		style = textParagraphT2(textColor),
		textAlign = TextAlign.Center
	)
}

@Composable
fun ContinueWithoutInsurance(
	onClick: onClick
) = Row(
	modifier = Modifier
		.fillMaxWidth()
		.clickable(onClick = onClick),
	verticalAlignment = Alignment.CenterVertically,
	horizontalArrangement = Arrangement.Center
) {
	Text(
		text = stringResource(R.string.kyc_unable_to_verify_otp),
		style = TextStyle(
			color = Color(0xff333333),
			fontWeight = FontWeight.Medium,
			fontSize = TextDimens.sp_14,
			lineHeight = TextDimens.sp_20
		)
	)
	HorizontalSpacer(width = Dimens.dp_4)
	Text(
		text = stringResource(R.string.kyc_continue_without_insurance), style = TextStyle(
			color = Color(0xff27AE60),
			fontWeight = FontWeight.Medium,
			fontSize = TextDimens.sp_12,
			lineHeight = TextDimens.sp_20
		)
	)
}

@Composable
fun ResendOtp(
	uiState: OtpUiState,
	sendOtpViaSms: () -> Unit,
	sendOtpViaCall: () -> Unit
) {
	Text(
		text = stringResource(id = R.string.resend_otp),
		style = text14Sp(fontWeight = FontWeight.SemiBold)
	)

	AnimatedVisibility(visible = uiState.timer.isNullOrBlank().not()) {
		Spacer(modifier = Modifier.height(15.dp))
		Text(
			text = uiState.timer.toString(),
			style = textMedium12Sp(color = TextBlack)
		)
	}

	Spacer(modifier = Modifier.height(15.dp))

	Row {
		ResendOtpButton(R.string.by_sms, uiState.timer.isNullOrBlank(), sendOtpViaSms)

		HorizontalSpacer(width = 16.dp)

		ResendOtpButton(R.string.by_call, uiState.timer.isNullOrBlank(), sendOtpViaCall)
	}
}

@Composable
fun ResendOtpButton(@StringRes title: Int, enabled: Boolean, resendOtp: () -> Unit) {
	val color by animateColorAsState(
		targetValue = if (enabled) Color0B8040 else LineColor
	)

	OutlinedButton(
		enabled = enabled,
		modifier = Modifier.width(100.dp),
		onClick = { resendOtp() },
		border = BorderStroke(1.dp, color),
		shape = RoundedCornerShape(Dimens.dp_8),
	) {
		Text(
			stringResource(id = title),
			style = text16Sp(color)
		)
	}
}

@ExperimentalComposeUiApi
@Composable
fun OtpView(
	text: String,
	maskedOtp: String,
	isValidOtp: Boolean,
	onTextChanged: (String) -> Unit,
	length: Int = 4
) {
	val focusRequester = remember { FocusRequester() }
	val keyboard = LocalSoftwareKeyboardController.current
	val interactionSource = remember { MutableInteractionSource() }

	val borderColor = when {
		text.length < length -> {
			LineColor
		}
		isValidOtp -> {
			LightGrey
		}
		else -> {
			LightRed
		}
	}

	val textColor = when {
		isValidOtp || text.length < length -> {
			TextGreen
		}
		else -> {
			LightRed
		}
	}

	Row(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		(0 until length).map { index ->
			OtpCell(
				modifier = Modifier
					.padding(horizontal = Dimens.dp_12)
					.weight(1f)
					.aspectRatio(1f, false)
					.clickable(
						indication = null,
						interactionSource = interactionSource
					) {
						focusRequester.requestFocus()
						keyboard?.show()
					},
				value = maskedOtp.getOrElse(index) { Character.MIN_VALUE }.toString(),
				isCursorVisible = text.length == index,
				textColor = textColor,
				borderColor = borderColor
			)
		}
	}

	TextField(
		value = text,
		onValueChange = {
			if (it.length <= length) {
				onTextChanged(it)
			}
			if (it.length == length) {
				keyboard?.hide()
			}
		},
		modifier = Modifier
			.size(0.dp)
			.focusRequester(focusRequester),
		keyboardOptions = KeyboardOptions.Default.copy(
			keyboardType = KeyboardType.NumberPassword,
			imeAction = ImeAction.Next
		),
		maxLines = 1
	)
}

@Composable
fun OtpNotReceivedText(name: String) {
	Text(
		text = buildAnnotatedString {
			withStyle(
				style = SpanStyle(
					fontSize = TextDimens.sp_14,
					color = Neutral100
				)
			) {
				append("$name ")
			}
			append(stringResource(R.string.could_not_receive))
		},
		style = text14Sp(LightGreyText, lineHeight = TextDimens.sp_20)
	)
}

@Composable
fun OtpCell(
	modifier: Modifier,
	value: String,
	textColor: Color,
	borderColor: Color,
	isCursorVisible: Boolean = false
) {

	val scope = rememberCoroutineScope()
	val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

	LaunchedEffect(key1 = cursorSymbol, isCursorVisible) {
		if (isCursorVisible) {
			scope.launch {
				delay(350)
				setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else "")
			}
		}
	}

	Box(
		modifier = modifier.border(
			color = borderColor,
			width = Dimens.dp_1,
			shape = mediumShape()
		),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = if (isCursorVisible) cursorSymbol else value,
			style = TextStyle(
				color = textColor,
				fontWeight = FontWeight.Bold,
				fontSize = 20.sp,
				lineHeight = 23.sp
			),
		)
	}
}
