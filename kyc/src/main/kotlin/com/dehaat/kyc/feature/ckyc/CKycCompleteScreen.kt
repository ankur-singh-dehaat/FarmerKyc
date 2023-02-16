package com.dehaat.kyc.feature.ckyc

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleOwner
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.KycViewModel
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.model.UiState
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.Neutral70
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
fun CKycCompleteScreen(
	viewModel: KycViewModel,
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	finish: onClick,
	completeKyc: onClick
) {
	BackHandler { finish() }
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			PrimaryAppBar(title = stringResource(R.string.kyc_kyc_details), onBackPress = finish)
		}
	) { paddingValues ->
		val uiState by viewModel.uiState.collectAsState()

		when (uiState.uiState) {
			UiState.Loading -> ShowProgress()
			is UiState.Error -> ShowError()
			UiState.Success -> Column(
				modifier = Modifier
					.padding(paddingValues)
					.padding(horizontal = Dimens.dp_32)
			) {
				uiState.completedKycState?.let {
					VerticalSpacer(height = Dimens.dp_24)
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(text = stringResource(R.string.kyc_farmer_id_proof_verified), style = textSubHeadingS3())

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

					it.cardType?.let {
						Text(text = stringResource(R.string.kyc_card_type), style = textCaptionCP1(Neutral70))

						when (it) {
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
		}

		LaunchedEffect(Unit) {
			viewModel.cKycPending.collect {
				if (it) completeKyc()
			}
		}

		ComposeOnResumeCallback(lifecycleOwner = lifecycleOwner) {
			viewModel.getInitialData()
		}
	}
}
