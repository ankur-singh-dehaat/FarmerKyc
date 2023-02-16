package com.dehaat.kyc.feature.addkyc

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.kyc.R
import com.dehaat.kyc.composable.IdProofButton
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Error110
import com.dehaat.kyc.ui.theme.Neutral20
import com.dehaat.kyc.ui.theme.Neutral40
import com.dehaat.kyc.ui.theme.Neutral80
import com.dehaat.kyc.ui.theme.Neutral90
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.Primary70
import com.dehaat.kyc.ui.theme.Verdigris20
import com.dehaat.kyc.ui.theme.Warning110
import com.dehaat.kyc.ui.theme.textParagraphT1
import com.dehaat.kyc.ui.theme.textParagraphT2
import com.dehaat.kyc.ui.theme.textParagraphT3
import com.dehaat.kyc.ui.theme.textSubHeadingS3
import com.dehaat.kyc.utils.ui.HorizontalSpacer
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.onClick
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun AddKycScreen(
	modifier: Modifier,
	captureIdProofPhoto: onClick = {},
	removeIdProof: onClick = {},
	isClickable: Boolean,
	imagePath: String? = null,
	idProofType: IdProofType,
	updateIdProofType: (IdProofType) -> Unit = {},
	content: @Composable () -> Unit
) = Column(
	modifier = modifier.verticalScroll(
		state = rememberScrollState(),
		reverseScrolling = true
	)
) {
	val paddingModifier = Modifier
		.fillMaxWidth()
		.padding(horizontal = Dimens.dp_24)

	VerticalSpacer(height = Dimens.dp_16)

	Row(
		modifier = paddingModifier
			.background(shape = RoundedCornerShape(Dimens.dp_8), color = Verdigris20)
			.padding(horizontal = Dimens.dp_16, vertical = Dimens.dp_12),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			modifier = Modifier.weight(1f),
			text = stringResource(R.string.bank_and_id_should_be_of_same_person),
			style = textParagraphT3(Neutral80)
		)

		HorizontalSpacer(Dimens.dp_8)

		Image(
			modifier = Modifier.size(Dimens.dp_28),
			painter = painterResource(id = R.drawable.ic_kyc_idea),
			contentDescription = "Idea"
		)
	}

	VerticalSpacer(height = Dimens.dp_26)

	Text(
		modifier = paddingModifier,
		text = stringResource(R.string.kyc_choose_only_one_id_proof_mandatory),
		style = textSubHeadingS3(Neutral90)
	)

	VerticalSpacer(Dimens.dp_8)

	var selectedIdProofType by remember(idProofType) { mutableStateOf(idProofType) }

	SelectIdProof(
		modifier = paddingModifier,
		selected = selectedIdProofType,
		isClickable = isClickable
	) {
		selectedIdProofType = it
		updateIdProofType(selectedIdProofType)
	}

	VerticalSpacer(Dimens.dp_40)

	Text(
		modifier = paddingModifier,
		text = stringResource(R.string.upload_photo_of_id_proof),
		style = textSubHeadingS3(Neutral90)
	)

	VerticalSpacer(height = Dimens.dp_4)

	Text(
		modifier = paddingModifier,
		text = stringResource(R.string.card_should_be_clearly_visible),
		style = textParagraphT3(Neutral90)
	)

	VerticalSpacer(height = Dimens.dp_8)

	CaptureIdProof(
		imagePath = imagePath,
		selectedIdProofType = selectedIdProofType,
		captureIdProofPhoto = captureIdProofPhoto,
		removeIdProof = removeIdProof
	)

	content()
}

@Composable
fun CaptureIdProof(
	imagePath: String?,
	selectedIdProofType: IdProofType,
	removeIdProof: onClick,
	captureIdProofPhoto: onClick
) = Column(
	modifier = Modifier
		.padding(Dimens.dp_8)
		.background(
			color = Neutral20,
			shape = RoundedCornerShape(Dimens.dp_24)
		)
) {
	Box(modifier = Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(Dimens.dp_8)
				.border(
					color = Neutral40,
					shape = RoundedCornerShape(Dimens.dp_24),
					width = Dimens.dp_1
				)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(Dimens.dp_8)
					.border(
						color = Primary100,
						shape = RoundedCornerShape(Dimens.dp_24),
						width = Dimens.dp_2
					)
					.clip(RoundedCornerShape(Dimens.dp_24))
					.clickable(onClick = captureIdProofPhoto)
					.background(Color.White)
					.height(Dimens.dp_200)
			) {
				if (imagePath != null) {
					Box(modifier = Modifier.fillMaxSize()) {
						GlideImage(
							modifier = Modifier.fillMaxSize(),
							imageModel = { imagePath }
						)
					}
				} else {
					Box(
						modifier = Modifier
							.fillMaxSize()
							.padding(Dimens.dp_16)
					) {
						if (selectedIdProofType is IdProofType.Aadhaar) {
							Image(
								modifier = Modifier.align(Alignment.TopEnd),
								painter = painterResource(id = R.drawable.ic_aadhaar),
								contentDescription = ""
							)
						}

						Image(
							modifier = Modifier.align(Alignment.Center),
							painter = painterResource(id = R.drawable.ic_capture_id_proof),
							contentDescription = ""
						)

						Column(
							modifier = Modifier
								.fillMaxWidth()
								.align(
									Alignment.BottomCenter
								),
							horizontalAlignment = Alignment.CenterHorizontally
						) {
							when (selectedIdProofType) {
								is IdProofType.Aadhaar -> Text(
									text = "Upload Aadhaar card photo",
									style = textParagraphT1(Primary70)
								)
								is IdProofType.Pan -> Text(
									text = "Upload Pan card photo",
									style = textParagraphT1(Primary70)
								)
								else -> Unit
							}

							Text(text = "Front Side Only", style = textParagraphT2(Warning110))
						}
					}
				}
			}
		}

		if (imagePath.isNotNull()) {
			Icon(
				modifier = Modifier
					.clickable(onClick = removeIdProof)
					.size(Dimens.dp_24)
					.align(Alignment.TopEnd),
				painter = painterResource(id = R.drawable.ic_cross),
				contentDescription = "",
				tint = Error110
			)
		}
	}
}

@Composable
fun SelectIdProof(
	modifier: Modifier,
	selected: IdProofType,
	isClickable: Boolean,
	onClick: (IdProofType) -> Unit
) = Row(modifier = modifier) {
	IdProofButton(
		title = stringResource(id = R.string.adhaar_card),
		isClickable = isClickable,
		isSelected = selected is IdProofType.Aadhaar
	) {
		onClick(IdProofType.Aadhaar)
	}

	HorizontalSpacer(width = Dimens.dp_8)

	IdProofButton(
		title = stringResource(R.string.pan_card),
		isClickable = isClickable,
		isSelected = selected is IdProofType.Pan
	) {
		onClick(IdProofType.Pan)
	}
}
