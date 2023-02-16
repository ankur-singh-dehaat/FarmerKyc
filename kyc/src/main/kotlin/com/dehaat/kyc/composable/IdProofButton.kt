package com.dehaat.kyc.composable

import android.text.style.ClickableSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dehaat.kyc.ui.theme.Neutral90
import com.dehaat.kyc.ui.theme.Primary10
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.textParagraphT1

@Composable
fun IdProofButton(
	title: String,
	isClickable: Boolean,
	isSelected: Boolean,
	onClick: () -> Unit
) = Text(
	modifier = Modifier
		.background(
			shape = RoundedCornerShape(20.dp), color = if (isSelected) {
				Primary100
			} else {
				Primary10
			}
		)
		.clip(RoundedCornerShape(20.dp))
		.clickable(onClick = onClick, enabled = isClickable)
		.padding(vertical = 8.dp, horizontal = 20.dp),
	text = title,
	textAlign = TextAlign.Center,
	style = textParagraphT1(
		textColor = if (isSelected) {
			Color.White
		} else {
			Neutral90
		}
	),
	maxLines = 1,
	overflow = TextOverflow.Ellipsis
)
