package com.dehaat.kyc.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

fun Typography(textDimensions: TextDimensions) = Typography(
	h4 = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = textDimensions.sp_16
	),
	h5 = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = textDimensions.sp_14
	),
	h6 = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = textDimensions.sp_12
	),
	button = TextStyle(
		fontWeight = FontWeight.W500,
		fontSize = textDimensions.sp_16,
		color = Color.White
	),
	caption = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = textDimensions.sp_16,
		lineHeight = textDimensions.sp_18
	),
	body1 = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = textDimensions.sp_16,
		lineHeight = textDimensions.sp_18
	),
	body2 = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = textDimensions.sp_16,
		color = Color.White,
		lineHeight = textDimensions.sp_16
	),
	subtitle1 = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = textDimensions.sp_14,
		lineHeight = textDimensions.sp_16
	),
	subtitle2 = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = textDimensions.sp_14,
		lineHeight = textDimensions.sp_16
	)
)
