@file:OptIn(ExperimentalMaterialApi::class)

package com.dehaat.kyc.feature.idprooftypeselection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.addkyc.AddKycScreen
import com.dehaat.kyc.features.FarmerKycViewModel
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.ui.theme.Color3C3C3B
import com.dehaat.kyc.ui.theme.ColorEAFBF1
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Neutral90
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.Primary110
import com.dehaat.kyc.ui.theme.text14Sp
import com.dehaat.kyc.ui.theme.textButtonB1
import com.dehaat.kyc.ui.theme.textSemiBold16sp
import com.dehaat.kyc.utils.ui.HorizontalSpacer
import com.dehaat.kyc.utils.ui.PrimaryAppBar
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.closeSheet
import com.dehaat.kyc.utils.ui.onClick
import com.dehaat.kyc.utils.ui.openSheet
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun IdProofTypeSelectionScreen(
	finish: onClick,
	viewModel: FarmerKycViewModel,
	captureIdProofPhoto: onClick
) {
	val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
	val scope = rememberCoroutineScope()
	val uiState by viewModel.uiState.collectAsState()

	BackHandler {
		if (sheetState.isVisible) {
			sheetState.closeSheet(scope)
		} else {
			finish()
		}
	}

	ModalBottomSheetLayout(
		modifier = Modifier,
		sheetContent = {
			viewModel.sampleImageMap[uiState.idProofType]?.let {
				Column(
					modifier = Modifier.fillMaxWidth()
				) {
					ViewSampleBottomSheetContent(
						uiState.idProofType,
						it.sampleUrl
					) {
						sheetState.closeSheet(scope)
					}
				}
			}
		},
		sheetBackgroundColor = Color.White,
		sheetState = sheetState,
		sheetShape = RoundedCornerShape(topStart = Dimens.dp_16, topEnd = Dimens.dp_16)
	) {
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			topBar = {
				PrimaryAppBar(title = stringResource(R.string.add_kyc), onBackPress = finish)
			}
		) { paddingValues ->
			AddKycScreen(
				modifier = Modifier
					.padding(paddingValues),
				idProofType = uiState.idProofType,
				isClickable = true,
				captureIdProofPhoto = captureIdProofPhoto,
				updateIdProofType = {
					viewModel.updateIdProofType(it)
				}
			) {
				VerticalSpacer(height = Dimens.dp_40)

				Row(
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							sheetState.openSheet(scope)
						},
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = stringResource(R.string.view_sample),
						style = textButtonB1(Primary100)
					)

					HorizontalSpacer(width = Dimens.dp_4)

					Icon(
						modifier = Modifier.size(Dimens.dp_20),
						painter = painterResource(id = R.drawable.ic_right_arrow_green),
						contentDescription = "",
						tint = Primary100
					)
				}

				VerticalSpacer(height = Dimens.dp_48)
			}
		}
	}
}

@Composable
fun ViewSampleBottomSheetContent(
	idProofType: IdProofType,
	sampleImage: String?,
	close: () -> Unit
) {
	Column(
		Modifier
			.padding(vertical = Dimens.dp_24, horizontal = Dimens.dp_32)
			.verticalScroll(rememberScrollState())
	) {
		Row {
			Text(
				text = stringResource(
					id = R.string.s_sample, when (idProofType) {
						IdProofType.Bank -> stringResource(id = R.string.kyc_passbook)
						IdProofType.Aadhaar -> stringResource(id = R.string.kyc_aadhaar_card)
						IdProofType.Pan -> stringResource(id = R.string.kyc_pan_card)
					}
				),
				style = textSemiBold16sp(Neutral90),
				modifier = Modifier.weight(1f)
			)
			Icon(
				painterResource(id = R.drawable.ic_close),
				contentDescription = "close",
				modifier = Modifier.clickable(onClick = close)
			)
		}
		Text(
			stringResource(id = R.string.card_should_be_clearly_visible),
			style = text14Sp(Color3C3C3B),
			modifier = Modifier.padding(top = Dimens.dp_24)
		)
		Text(
			text = stringResource(id = R.string.this_is_an_sample),
			style = textSemiBold16sp(Color3C3C3B),
			modifier = Modifier.padding(top = Dimens.dp_24)
		)
		if (sampleImage?.isNotBlank() == true)
			GlideImage(
				{ sampleImage },
				modifier = Modifier
					.padding(top = Dimens.dp_4)
					.height(Dimens.dp_150)
					.fillMaxWidth(),
				imageOptions = ImageOptions(contentScale = ContentScale.Inside)
			)
		OutlinedButton(
			onClick = close,
			modifier = Modifier
				.padding(top = Dimens.dp_24)
				.fillMaxWidth(),
			border = BorderStroke(Dimens.dp_1, Primary110),
			shape = RoundedCornerShape(Dimens.dp_8),
			colors = ButtonDefaults.outlinedButtonColors(
				backgroundColor = ColorEAFBF1,
			)
		) {
			Text(stringResource(id = R.string.close), color = Primary110)
		}
	}
}
