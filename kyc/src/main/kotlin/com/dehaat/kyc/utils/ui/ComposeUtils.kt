@file:OptIn(ExperimentalMaterialApi::class)

package com.dehaat.kyc.utils.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.navigation.KycExecutor
import com.dehaat.kyc.ui.theme.DehaatGreen
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.DividerColor
import com.dehaat.kyc.ui.theme.Error100
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.TextDimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun HorizontalSpacer(width: Dp) = Spacer(modifier = Modifier.width(width))

@Composable
internal fun VerticalSpacer(height: Dp) = Spacer(modifier = Modifier.height(height))

internal typealias onClick = () -> Unit

fun ModalBottomSheetState.openSheet(scope: CoroutineScope) {
	scope.launch { this@openSheet.show() }
}

fun ModalBottomSheetState.closeSheet(scope: CoroutineScope) {
	scope.launch { this@closeSheet.hide() }
}

fun NavGraphBuilder.navBaseComposable(
	route: String,
	arguments: List<NamedNavArgument> = emptyList(),
	deepLinks: List<NavDeepLink> = emptyList(),
	content: @Composable (NavBackStackEntry) -> Unit
) {
	composable(route, arguments, deepLinks) {
		LaunchedEffect(Unit) {
			KycExecutor.sendNavigationAnalytics(route)
		}
		content(it)
	}
}

fun booleanNavArgument(
	name: String,
	default: Boolean = false
) = navArgument(name) {
	type = NavType.BoolType
	defaultValue = default
}

fun NavController.navigateTo(
	route: String,
	args: Bundle = bundleOf(),
	navOptions: NavOptions? = null,
	navigatorExtras: Navigator.Extras? = null
) {
	val routeLink = NavDeepLinkRequest
		.Builder
		.fromUri(NavDestination.createRoute(route).toUri())
		.build()

	val deepLinkMatch = graph.matchDeepLink(routeLink)
	if (deepLinkMatch != null) {
		val destination = deepLinkMatch.destination
		val id = destination.id
		navigate(id, args, navOptions, navigatorExtras)
	} else {
		navigate(route, navOptions, navigatorExtras)
	}
}

@Composable
fun TextUserInput(
	modifier: Modifier = Modifier,
	label: String,
	value: String,
	onInputChanged: (String) -> Unit = {},
	enabled: Boolean = true,
	readOnly: Boolean = false,
	errorMessage: String? = null,
	keyboardType: KeyboardType = KeyboardType.Text,
	keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = keyboardType),
	keyboardActions: KeyboardActions = KeyboardActions()
) {
	TextField(
		value = value,
		onValueChange = onInputChanged,
		modifier = modifier
			.padding(start = Dimens.dp_16, end = Dimens.dp_16, top = Dimens.dp_24)
			.fillMaxWidth(),
		enabled = enabled,
		readOnly = readOnly,
		isError = errorMessage.isNullOrEmpty().not(),
		label = { Text(label) },
		singleLine = true,
		colors = TextFieldDefaults.textFieldColors(
			textColor = Neutral100,
			backgroundColor = DividerColor,
		),
		keyboardOptions = keyboardOptions,
		keyboardActions = keyboardActions
	)

	if (errorMessage.isNullOrEmpty().not())
		Text(
			errorMessage.toString(),
			color = Error100,
			fontSize = TextDimens.sp_12,
			modifier = Modifier.padding(start = Dimens.dp_32, top = Dimens.dp_4)
		)
}

@Composable
fun ShowProgress(showProgress: Boolean = true, modifier: Modifier = Modifier.fillMaxSize()) {
	if (showProgress) {
		Box(modifier = modifier
			.fillMaxSize()
			.clickable(
				indication = null,
				interactionSource = remember { MutableInteractionSource() }) {},
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator(color = DehaatGreen)
		}
	}
}

@Composable
fun ShowError(
	errorMessage: String = "Something went wrong, please try again",
	context: Context = LocalContext.current
) = Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()

@Composable
inline fun ComposeOnResumeCallback(lifecycleOwner: LifecycleOwner, crossinline onResume: () -> Unit) {
	DisposableEffect(lifecycleOwner) {
		val observer = LifecycleEventObserver { source, event ->
			if (event == Lifecycle.Event.ON_RESUME) {
				onResume()
			}
		}
		// Add the observer to the lifecycle
		lifecycleOwner.lifecycle.addObserver(observer)
		// When the effect leaves the Composition, remove the observer
		onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
	}
}

@Composable
fun VerificationStatus.toLabel() = when(this) {
	VerificationStatus.Submitted -> stringResource(id = R.string.kyc_document_submitted)
	VerificationStatus.Pending -> stringResource(id = R.string.kyc_document_pending)
	VerificationStatus.Approved -> stringResource(id = R.string.kyc_document_approved)
	VerificationStatus.Rejected -> stringResource(id = R.string.kyc_document_rejected)
	VerificationStatus.Verified -> stringResource(id = R.string.kyc_document_verified)
}
