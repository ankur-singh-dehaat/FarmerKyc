package com.dehaat.kyc.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.dehaat.kyc.R


class TextDimensions(
	val sp_5: TextUnit,
	val sp_10: TextUnit,
	val sp_12: TextUnit,
	val sp_14: TextUnit,
	val sp_16: TextUnit,
	val sp_18: TextUnit,
	val sp_20: TextUnit,
	val sp_22: TextUnit,
	val sp_24: TextUnit,
	val sp_28: TextUnit,
)

val normalTextDimensions = TextDimensions(
	sp_5 = 5.sp,
	sp_10 = 10.sp,
	sp_12 = 12.sp,
	sp_14 = 14.sp,
	sp_16 = 16.sp,
	sp_18 = 18.sp,
	sp_20 = 20.sp,
	sp_22 = 22.sp,
	sp_24 = 24.sp,
	sp_28 = 28.sp,
)

val smallTextDimensions = TextDimensions(
	sp_5 = 3.sp,
	sp_10 = 8.sp,
	sp_12 = 10.sp,
	sp_14 = 12.sp,
	sp_16 = 14.sp,
	sp_18 = 16.sp,
	sp_20 = 18.sp,
	sp_22 = 20.sp,
	sp_24 = 22.sp,
	sp_28 = 26.sp,
)


@Composable
fun textHeadingH1(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 32.sp,
	fontWeight: FontWeight = FontWeight.SemiBold,
	lineHeight: TextUnit = 40.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	fontFamily = fontFamily,
	textDecoration = textDecoration
)

@Composable
fun textHeadingH2(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 32.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 40.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	fontFamily = fontFamily,
	textDecoration = textDecoration
)

@Composable
fun textHeadingH3(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 24.sp,
	fontWeight: FontWeight = FontWeight.SemiBold,
	lineHeight: TextUnit = 32.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textHeadingH4(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 24.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 32.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textHeadingH5(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 20.sp,
	fontWeight: FontWeight = FontWeight.SemiBold,
	lineHeight: TextUnit = 28.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textHeadingH6(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 20.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 28.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textSubHeadingS1(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 18.sp,
	fontWeight: FontWeight = FontWeight.SemiBold,
	lineHeight: TextUnit = 24.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textSubHeadingS2(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 18.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 24.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textSubHeadingS3(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 16.sp,
	fontWeight: FontWeight = FontWeight.SemiBold,
	lineHeight: TextUnit = 24.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textParagraphT1(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 16.sp,
	fontWeight: FontWeight = FontWeight.Normal,
	lineHeight: TextUnit = 24.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textParagraphT1Highlight(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 16.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 24.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textParagraphT2(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 14.sp,
	fontWeight: FontWeight = FontWeight.Normal,
	lineHeight: TextUnit = 20.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	fontFamily = fontFamily,
	textDecoration = textDecoration
)

@Composable
fun textParagraphT2Highlight(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 14.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 20.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun spanParagraphT2Highlight(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 14.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 20.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = SpanStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textParagraphT3(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 12.sp,
	fontWeight: FontWeight = FontWeight.Normal,
	lineHeight: TextUnit = 16.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textParagraphT3Highlight(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 12.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 16.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textButtonB1(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 16.sp,
	fontWeight: FontWeight = FontWeight.SemiBold,
	lineHeight: TextUnit = 20.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textButtonB2(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 14.sp,
	fontWeight: FontWeight = FontWeight.SemiBold,
	lineHeight: TextUnit = 18.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun spanButtonB2(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 14.sp,
	fontWeight: FontWeight = FontWeight.SemiBold,
	lineHeight: TextUnit = 18.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = SpanStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textCaptionCP1(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 12.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 16.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textCaptionCP2(
	textColor: Color = TextBlack,
	fontSize: TextUnit = 10.sp,
	fontWeight: FontWeight = FontWeight.Medium,
	lineHeight: TextUnit = 14.sp,
	textDecoration: TextDecoration? = null,
	fontFamily: FontFamily = FontFamily(Font(R.font.notosans))
) = TextStyle(
	color = textColor,
	fontSize = fontSize,
	fontWeight = fontWeight,
	lineHeight = lineHeight,
	textDecoration = textDecoration,
	fontFamily = fontFamily
)

@Composable
fun textSemiBold14Sp(
	textColor: Color = TextBlack,
	fontWeight: FontWeight = FontWeight.SemiBold,
	lineHeight: TextUnit = 14.sp
) = TextStyle(
	fontSize = 14.sp,
	lineHeight = lineHeight,
	fontWeight = fontWeight,
	color = textColor,
	fontFamily = FontFamily(Font(R.font.notosans))
)

@Composable
fun textSemiBold16sp(color: Color = TextBlack) = TextStyle(
	lineHeight = TextDimens.sp_16,
	fontWeight = FontWeight.SemiBold,
	color = color,
	fontFamily = FontFamily(Font(resId = R.font.notosans))
)

@Composable
fun text14Sp(
	textColor: Color = TextBlack,
	fontWeight: FontWeight = FontWeight.Normal,
	lineHeight: TextUnit = 14.sp
) = TextStyle(
	fontSize = 14.sp,
	lineHeight = lineHeight,
	fontWeight = fontWeight,
	color = textColor,
	fontFamily = FontFamily(Font(resId = R.font.notosans))
)

@Composable
fun textMedium18Sp(color: Color = TextBlack) = TextStyle(
	fontSize = TextDimens.sp_18,
	lineHeight = TextDimens.sp_20,
	fontWeight = FontWeight.Medium,
	color = color,
	fontFamily = FontFamily(Font(resId = R.font.notosans))
)

@Composable
fun text16Sp(
	textColor: Color = TextBlack,
	fontWeight: FontWeight = FontWeight.Normal,
	lineHeight: TextUnit = 16.sp
) = TextStyle(
	fontSize = 16.sp,
	lineHeight = lineHeight,
	fontWeight = fontWeight,
	color = textColor,
	fontFamily = FontFamily(Font(resId = R.font.notosans))
)

@Composable
fun textMedium12Sp(color: Color = TextBlack) =
	TextStyle(
		fontSize = TextDimens.sp_12,
		lineHeight = TextDimens.sp_14,
		fontWeight = FontWeight.Medium,
		color = color,
		fontFamily = FontFamily(Font(resId = R.font.notosans))
	)
