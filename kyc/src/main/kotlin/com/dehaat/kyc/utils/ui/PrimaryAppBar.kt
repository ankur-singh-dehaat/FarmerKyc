package com.dehaat.kyc.utils.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.dehaat.kyc.R
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Neutral80
import com.dehaat.kyc.ui.theme.TextDimens

@Composable
fun PrimaryAppBar(
	title: String,
	onBackPress: () -> Unit,
	content: @Composable () -> Unit = {},
	extendedContent: @Composable () -> Unit = {}
) {
	Surface(
		modifier = Modifier.fillMaxWidth(),
		elevation = Dimens.dp_4,
		color = Color.White
	) {
		Column(modifier = Modifier.fillMaxWidth()) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.height(Dimens.dp_56),
				verticalAlignment = Alignment.CenterVertically
			) {

				IconButton(onClick = { onBackPress() }) {
					Icon(
						painterResource(id = R.drawable.ic_back),
						contentDescription = "Back",
						tint = Neutral80
					)
				}

				Column(modifier = Modifier.weight(1f)) {
					Text(
						text = title,
						style = TextStyle(
							lineHeight = TextDimens.sp_20,
							fontWeight = FontWeight.SemiBold,
							color = Neutral80,
							fontFamily = FontFamily(Font(resId = R.font.notosans)),
							fontSize = TextDimens.sp_18
						),
						maxLines = 1,
						overflow = TextOverflow.Ellipsis
					)
				}

				Box(Modifier.padding(end = Dimens.dp_12)) {
					content()
				}
			}
			extendedContent()
		}
	}
}
