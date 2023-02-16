package com.dehaat.kyc.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.textButtonB1

@Composable
fun Button(
	modifier: Modifier,
	title: String,
	isEnabled: Boolean = true,
	onClick: onClick
) = Row(
	modifier = modifier
		.background(
			color = if (isEnabled) Primary100 else Color.Gray,
			shape = RoundedCornerShape(Dimens.dp_10)
		)
		.clickable(onClick = onClick, enabled = isEnabled)
		.padding(vertical = Dimens.dp_20),
	verticalAlignment = Alignment.CenterVertically,
	horizontalArrangement = Arrangement.Center
) {
	Text(
		text = title, style = textButtonB1(Color.White)
	)
}
