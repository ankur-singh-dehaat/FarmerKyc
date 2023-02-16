package com.dehaat.kyc.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration

private val LightColorPalette = lightColors(
	primary = ColorPrimary,
	primaryVariant = ColorPrimaryDark,
	secondary = ColorAccent,
	background = Color.White,
	onBackground = SnackBarSurface,
	surface = Color.White,
	onSurface = SnackBarSurface,
	onPrimary = Color.White,
	onSecondary = Color.White
)

@Composable
fun ProvideTextDimens(
	textDimensions: TextDimensions,
	content: @Composable () -> Unit
) {
	val dimensionSet = remember { textDimensions }
	CompositionLocalProvider(LocalAppTextDimens provides dimensionSet, content = content)
}

@Composable
fun ProvideDimens(
	dimensions: Dimensions,
	content: @Composable () -> Unit
) {
	val dimensionSet = remember { dimensions }
	CompositionLocalProvider(LocalAppDimens provides dimensionSet, content = content)
}

private val LocalAppDimens = staticCompositionLocalOf {
	normalDimensions
}

private val LocalAppTextDimens = staticCompositionLocalOf {
	normalTextDimensions
}

object AppTheme {
	val dimens: Dimensions
		@Composable
		get() = LocalAppDimens.current

	val textDimens: TextDimensions
		@Composable
		get() = LocalAppTextDimens.current
}

val Dimens: Dimensions
	@Composable
	get() = AppTheme.dimens

val TextDimens: TextDimensions
	@Composable
	get() = AppTheme.textDimens

@Composable
fun KycAppTheme(
	content: @Composable () -> Unit
) {
	val textDimensions =
		if (LocalConfiguration.current.screenWidthDp <= 360) smallTextDimensions else normalTextDimensions

	CompositionLocalProvider {
		ProvideDimens(dimensions = normalDimensions) {
			ProvideTextDimens(textDimensions = textDimensions) {
				MaterialTheme(
					colors = LightColorPalette,
					typography = Typography(textDimensions),
					shapes = Shapes,
					content = content
				)
			}
		}
	}
}
