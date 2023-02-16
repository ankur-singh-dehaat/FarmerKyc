package com.dehaat.kyc.features.composables.kycsuccess

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.LifecycleOwner
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.bankkyc.BankVerificationStatus
import com.dehaat.kyc.feature.bankkyc.UploadedBankDetails
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.features.FarmerKycViewModel
import com.dehaat.kyc.features.composables.kycsuccess.model.KycStatusUiState
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.Neutral20
import com.dehaat.kyc.ui.theme.Neutral70
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.textButtonB1
import com.dehaat.kyc.ui.theme.textCaptionCP1
import com.dehaat.kyc.ui.theme.textParagraphT1
import com.dehaat.kyc.ui.theme.textSubHeadingS3
import com.dehaat.kyc.utils.ui.ComposeOnResumeCallback
import com.dehaat.kyc.utils.ui.PrimaryAppBar
import com.dehaat.kyc.utils.ui.ShowError
import com.dehaat.kyc.utils.ui.ShowProgress
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.onClick

@Composable
fun KycEntryScreen(
	finish: onClick,
	viewModel: FarmerKycViewModel,
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	onBankDetailsClick: (Long?, VerificationStatus) -> Unit = { _, _ -> },
	onAddBankDetails: onClick = {},
	completeKyc: onClick = {},
) {
	BackHandler { finish() }
	val uiState by viewModel.uiState.collectAsState()
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			if (viewModel.isBankVerification) {
				PrimaryAppBar(
					title = stringResource(R.string.kyc_bank_account_details),
					onBackPress = finish,
					content = {
						uiState.bankVerificationStatus?.let {
							BankVerificationStatus(verificationStatus = it)
						}
					}
				)
			} else {
				PrimaryAppBar(
					title = stringResource(R.string.kyc_kyc_details),
					onBackPress = finish
				)
			}
		},
		bottomBar = {
			if (viewModel.isBankVerification) {
				Text(
					modifier = Modifier
						.fillMaxWidth()
						.padding(Dimens.dp_24)
						.border(
							width = Dimens.dp_1,
							color = Neutral20,
							shape = RoundedCornerShape(Dimens.dp_8)
						)
						.clickable(onClick = onAddBankDetails)
						.background(Color.White)
						.padding(Dimens.dp_18),
					text = if ((uiState.kycStatusUiState as? KycStatusUiState.BankUiState)?.bankDetails.orEmpty()
							.isEmpty()
					) stringResource(R.string.kyc_add_bank_details) else stringResource(
						R.string.kyc_add_another_bank_details
					),
					style = textButtonB1(Primary100),
					textAlign = TextAlign.Center
				)
			}
		}
	) {
		Column(modifier = Modifier.padding(it)) {
			when (uiState.kycStatusUiState) {
				KycStatusUiState.Loading -> ShowProgress()
				is KycStatusUiState.Error -> ShowError()
				is KycStatusUiState.BankUiState -> UploadedBankDetails(
					modifier = Modifier,
					bankDetails = (uiState.kycStatusUiState as? KycStatusUiState.BankUiState)?.bankDetails.orEmpty()
				) { bankDetails ->
					onBankDetailsClick(bankDetails.kycId, bankDetails.verificationStatus)
				}
				is KycStatusUiState.VerifiedIdProof -> with(uiState.kycStatusUiState as? KycStatusUiState.VerifiedIdProof) {
					VerifiedIdProofDetailsScreen(this)
				}
			}
		}
	}

	ComposeOnResumeCallback(lifecycleOwner = lifecycleOwner) {
		viewModel.getInitialData()
	}

	LaunchedEffect(Unit) {
		viewModel.kycPending.collect {
			if (it) completeKyc()
		}
	}
}

@Composable
private fun VerifiedIdProofDetailsScreen(
	verifiedIdProof: KycStatusUiState.VerifiedIdProof?
) = Column(modifier = Modifier.padding(horizontal = Dimens.dp_32)) {
	verifiedIdProof?.let {
		VerticalSpacer(height = Dimens.dp_24)
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = stringResource(R.string.kyc_farmer_id_proof_verified),
				style = textSubHeadingS3()
			)

			Image(
				modifier = Modifier.size(Dimens.dp_24),
				painter = painterResource(id = R.drawable.ic_kyc_success_check),
				contentDescription = ""
			)
		}

		VerticalSpacer(height = Dimens.dp_16)

		it.name?.let {
			Text(text = stringResource(R.string.kyc_farmer_name), style = textCaptionCP1(Neutral70))

			Text(text = it, style = textParagraphT1(Neutral100))

			VerticalSpacer(height = Dimens.dp_24)

		}

		it.cardType?.let { idProofType ->
			Text(text = stringResource(R.string.kyc_card_type), style = textCaptionCP1(Neutral70))

			when (idProofType) {
				IdProofType.Aadhaar -> Text(
					text = stringResource(R.string.kyc_aadhaar_card),
					style = textParagraphT1(Neutral100)
				)
				IdProofType.Pan -> Text(
					text = stringResource(R.string.kyc_pan_card),
					style = textParagraphT1(Neutral100)
				)
				else -> Unit
			}

			VerticalSpacer(height = Dimens.dp_24)
		}

		it.cardNumber?.let {
			Text(text = stringResource(R.string.kyc_card_number), style = textCaptionCP1(Neutral70))

			Text(text = it, style = textParagraphT1(Neutral100))

			VerticalSpacer(height = Dimens.dp_24)
		}

		it.lastUpdatedOn?.let {
			Text(text = stringResource(R.string.kyc_updated_on), style = textCaptionCP1(Neutral70))

			Text(text = it, style = textParagraphT1(Neutral100))
		}
	}
}
