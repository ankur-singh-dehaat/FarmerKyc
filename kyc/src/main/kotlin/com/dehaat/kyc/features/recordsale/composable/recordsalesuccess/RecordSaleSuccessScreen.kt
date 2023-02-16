package com.dehaat.kyc.features.recordsale.composable.recordsalesuccess

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dehaat.kyc.R
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.PrimaryTextColor
import com.dehaat.kyc.ui.theme.TextDimens
import com.dehaat.kyc.utils.ui.onClick
import kotlinx.coroutines.delay

@Composable
fun RecordSaleSuccessScreen(
	saleRecordSuccess: onClick
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(Color.White),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(id = R.drawable.ic_kyc_success_check),
			contentDescription = "",
			modifier = Modifier
				.width(64.dp)
				.height(64.dp)
				.clip(CircleShape)
				.background(Color(0xFFE5F3DE))
				.padding(12.dp)
		)
		Text(
			text = stringResource(R.string.kyc_sale_recorded_successfully),
			modifier = Modifier.padding(Dimens.dp_20),
			fontSize = TextDimens.sp_16,
			color = PrimaryTextColor
		)
	}

	LaunchedEffect(Unit) {
		delay(2000L)
		saleRecordSuccess()
	}
}
