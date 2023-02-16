package com.dehaat.kyc.feature.bankkyc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.dehaat.kyc.feature.bankkyc.model.BankVerifiedDetails
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.LightGreyText
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.Primary110
import com.dehaat.kyc.ui.theme.TextDimens
import com.dehaat.kyc.ui.theme.mediumShape
import com.dehaat.kyc.ui.theme.text14Sp
import com.dehaat.kyc.ui.theme.text16Sp

@Composable
fun BankOtpScreen(
	viewModel: BankOtpViewModel,
	navigateToPayments: (BankVerifiedDetails) -> Unit
) = Unit

@Composable
private fun OtpInfoText(
	phone: String,
	name: String
) {
	Text(
		text = buildAnnotatedString {
			append("OTP has been sent to")
			withStyle(
				style = SpanStyle(
					fontSize = TextDimens.sp_14,
					color = Neutral100
				)
			) {
				append(" $name ")
			}
			append("on phone number")
			withStyle(
				style = SpanStyle(
					fontSize = TextDimens.sp_14,
					color = Primary110
				)
			) {
				append(" $phone")
			}
		},
		style = text14Sp(LightGreyText, lineHeight = TextDimens.sp_20)
	)
}

@Composable
private fun ConfirmButton(isActive: Boolean, onClick: () -> Unit) {
	TextButton(
		modifier = Modifier
			.fillMaxWidth()
			.background(color = Primary100, shape = mediumShape()),
		onClick = onClick,
		enabled = isActive
	) {
		Text(
			text = "Confirm",
			style = text16Sp(textColor = Color.White, fontWeight = FontWeight.SemiBold),
			modifier = Modifier.padding(Dimens.dp_10)
		)
	}
}
