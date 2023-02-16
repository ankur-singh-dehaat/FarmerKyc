package com.dehaat.kyc.feature.bankkyc

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.LifecycleOwner
import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.KycViewModel
import com.dehaat.kyc.feature.bankkyc.model.BankDetailsState
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.features.composables.kycsuccess.model.KycStatusUiState
import com.dehaat.kyc.model.UiState
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Error100
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.Neutral20
import com.dehaat.kyc.ui.theme.Olivine20Color
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.Success110Color
import com.dehaat.kyc.ui.theme.TextDimens
import com.dehaat.kyc.ui.theme.YellowP100Color
import com.dehaat.kyc.ui.theme.textButtonB1
import com.dehaat.kyc.utils.ui.ComposeOnResumeCallback
import com.dehaat.kyc.utils.ui.HorizontalSpacer
import com.dehaat.kyc.utils.ui.PrimaryAppBar
import com.dehaat.kyc.utils.ui.ShowError
import com.dehaat.kyc.utils.ui.ShowProgress
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.onClick
import com.dehaat.kyc.utils.ui.toLabel

@Composable
fun BankKycScreen(
	finish: onClick,
	kycViewModel: KycViewModel,
	addBankAccount: onClick,
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	updateBankAccount: (BankDetailsState) -> Unit
) {
	val uiState by kycViewModel.uiState.collectAsState()
	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.background(Color(0XFFECECEC)),
		topBar = {
			PrimaryAppBar(
				title = stringResource(R.string.kyc_bank_account_details),
				onBackPress = finish
			)
		},
		bottomBar = {
			Text(
				modifier = Modifier
					.fillMaxWidth()
					.padding(Dimens.dp_24)
					.border(
						width = Dimens.dp_1,
						color = Neutral20,
						shape = RoundedCornerShape(Dimens.dp_8)
					)
					.clickable(onClick = addBankAccount)
					.background(Color.White)
					.padding(Dimens.dp_18),
				text = if (uiState.bankDetails.isEmpty()) stringResource(R.string.kyc_add_bank_details) else stringResource(
					R.string.kyc_add_another_bank_details
				),
				style = textButtonB1(Primary100),
				textAlign = TextAlign.Center
			)
		}
	) { paddingValues ->
		when (uiState.uiState) {
			UiState.Loading -> ShowProgress()
			is UiState.Error -> ShowError()
			is UiState.Success -> {

			}
		}
	}

	ComposeOnResumeCallback(lifecycleOwner = lifecycleOwner) {
		kycViewModel.getInitialData()
	}
}

@Composable
fun UploadedBankDetails(
	modifier: Modifier,
	bankDetails: List<KycStatusUiState.BankUiState.BankDetails>,
	updateBankAccount: (KycStatusUiState.BankUiState.BankDetails) -> Unit
) = Column(
	modifier = modifier
		.fillMaxSize()
		.verticalScroll(
			rememberScrollState()
		)
) {
	bankDetails.forEach {
		BankDetailsScreen(bankDetails = it) {
			updateBankAccount(it)
		}
		VerticalSpacer(height = Dimens.dp_2)
	}
}

@Composable
private fun BankDetailsScreen(
	bankDetails: KycStatusUiState.BankUiState.BankDetails,
	onClick: onClick
) = Column(
	modifier = Modifier
		.fillMaxWidth()
		.clickable(onClick = onClick)
		.padding(
			horizontal = Dimens.dp_24,
			vertical = Dimens.dp_20
		)
) {

	bankDetails.bankName?.let {
		Text(
			text = it,
			style = TextStyle(
				color = Color(0xFF3c3c3b),
				fontWeight = FontWeight.SemiBold,
				fontSize = TextDimens.sp_16
			)
		)

		VerticalSpacer(height = Dimens.dp_8)
	}

	bankDetails.bankAccountNumber?.let {
		Row {
			Text(
				text = stringResource(R.string.kyc_bank_account),
				style = TextStyle(
					color = Color(0xFF646363),
					fontWeight = FontWeight.Normal,
					fontSize = TextDimens.sp_16
				)
			)
			HorizontalSpacer(width = Dimens.dp_8)

			Text(
				text = it,
				style = TextStyle(
					color = Color(0xFF3C3C3B),
					fontWeight = FontWeight.Normal,
					fontSize = TextDimens.sp_16
				)
			)
		}

		VerticalSpacer(height = Dimens.dp_8)
	}

	bankDetails.ifscCode?.let {
		Row {
			Text(
				text = stringResource(R.string.kyc_ifsc_code),
				style = TextStyle(
					color = Color(0xFF646363),
					fontWeight = FontWeight.Normal,
					fontSize = TextDimens.sp_16
				)
			)
			HorizontalSpacer(width = Dimens.dp_8)

			Text(
				text = it,
				style = TextStyle(
					color = Color(0xFF3C3C3B),
					fontWeight = FontWeight.Normal,
					fontSize = TextDimens.sp_16
				)
			)
		}

		VerticalSpacer(height = Dimens.dp_8)
	}

	if (bankDetails.passBookPhoto.isNotNull()) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			Image(
				modifier = Modifier.size(Dimens.dp_16),
				painter = painterResource(id = R.drawable.ic_success), contentDescription = ""
			)
			HorizontalSpacer(width = Dimens.dp_4)
			Text(
				text = stringResource(R.string.kyc_passbook_is_attached), style = TextStyle(
					color = Color(0xFF646363),
					fontWeight = FontWeight.Medium,
					fontSize = TextDimens.sp_16
				)
			)
		}
	}

	VerticalSpacer(height = Dimens.dp_8)

	BankVerificationStatus(bankDetails.verificationStatus)
}

@Composable
fun BankVerificationStatus(verificationStatus: VerificationStatus) = when (verificationStatus) {
	VerificationStatus.Rejected -> VerificationLabel(
		backgroundColor = Error100,
		text = verificationStatus.toLabel()
	)
	VerificationStatus.Verified -> VerificationLabel(
		backgroundColor = Success110Color,
		text = verificationStatus.toLabel()
	)
	VerificationStatus.Submitted -> VerificationLabel(
		backgroundColor = Olivine20Color,
		text = verificationStatus.toLabel()
	)
	VerificationStatus.Approved -> VerificationLabel(
		backgroundColor = Success110Color,
		text = verificationStatus.toLabel()
	)
	VerificationStatus.Pending -> VerificationLabel(
		backgroundColor = YellowP100Color,
		text = verificationStatus.toLabel()
	)
}

@Composable
fun VerificationLabel(
	backgroundColor: Color,
	text: String
) = Text(
	text = text, modifier = Modifier
		.background(
			color = backgroundColor,
			shape = RoundedCornerShape(Dimens.dp_12)
		)
		.padding(vertical = Dimens.dp_4, horizontal = Dimens.dp_16),
	color = Neutral100
)
