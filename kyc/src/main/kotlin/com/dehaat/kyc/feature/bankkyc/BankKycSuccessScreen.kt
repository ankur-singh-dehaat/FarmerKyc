package com.dehaat.kyc.feature.bankkyc

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.bankkyc.model.BankVerifiedDetails
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.Neutral70
import com.dehaat.kyc.ui.theme.text16Sp
import com.dehaat.kyc.utils.ui.PrimaryAppBar
import com.dehaat.kyc.utils.ui.onClick
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun BankKycSuccessScreen(
	finish: onClick,
	bankVerifiedDetails: BankVerifiedDetails?
) {
	BackHandler { finish() }
	Scaffold(
		topBar = {
			PrimaryAppBar(
				title = stringResource(R.string.kyc_bank_verification),
				onBackPress = finish
			)
		}
	) { padding ->
		Column(
			Modifier
				.padding(padding)
				.padding(horizontal = Dimens.dp_32)
		) {
			SubmitDetailsField(
				bankVerifiedDetails?.accountNumber,
				stringResource(R.string.kyc_account_number),
				appendStar = true
			)
			SubmitDetailsField(
				bankVerifiedDetails?.accountHolderName,
				stringResource(R.string.kyc_account_holder_name),
				appendStar = true
			)
			SubmitDetailsField(
				bankVerifiedDetails?.ifscCode,
				stringResource(R.string.kyc_bank_ifsc_code),
				appendStar = true
			)
			SubmitDetailsImageField(
				label = stringResource(id = R.string.kyc_passbook_is_attached),
				bankVerifiedDetails?.passbookPhoto
			)
		}
	}
}

@Composable
fun SubmitDetailsField(
	value: String? = null,
	label: String,
	filePath: String? = null,
	appendStar: Boolean = true
) {
	Text(
		text = "$label${if (appendStar) "*" else ""}",
		color = Neutral70,
		modifier = Modifier.padding(top = Dimens.dp_24)
	)
	if (value?.isNotBlank() == true)
		Text(
			text = value,
			style = text16Sp(Neutral100),
		)

	if (filePath.isNullOrBlank().not()) {
		GlideImage(
			{ filePath },
			modifier = Modifier
				.padding(top = Dimens.dp_4)
				.height(Dimens.dp_200)
				.fillMaxWidth(),
		)
	}
}

@Composable
fun SubmitDetailsImageField(
	label: String,
	fileUrl: String? = null
) {
	fileUrl?.let {
		Text(
			text = label,
			color = Neutral70,
			modifier = Modifier.padding(top = Dimens.dp_24)
		)
		GlideImage(
			imageModel = { fileUrl },
			modifier = Modifier
				.padding(top = Dimens.dp_4)
				.height(Dimens.dp_200)
				.fillMaxWidth(),
		)
	}
}
