package com.dehaat.kyc.feature.updatekyc

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.addkyc.AddKycScreen
import com.dehaat.kyc.feature.addkyc.AddKycViewModel
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.model.UiState
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.utils.ui.Button
import com.dehaat.kyc.utils.ui.ComposeOnResumeCallback
import com.dehaat.kyc.utils.ui.PrimaryAppBar
import com.dehaat.kyc.utils.ui.ShowError
import com.dehaat.kyc.utils.ui.ShowProgress
import com.dehaat.kyc.utils.ui.TextUserInput
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.onClick

@Composable
fun AddKycDetailsScreen(
	onBackPress: onClick,
	viewModel: AddKycViewModel = hiltViewModel(),
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	captureIdProofPhoto: onClick,
	navigateToCapturePayment: (Long, OcrDetails?) -> Unit
) {
	val uiState by viewModel.uiState.collectAsState()

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			PrimaryAppBar(title = stringResource(R.string.add_kyc), onBackPress = onBackPress)
		},
		bottomBar = {
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.padding(all = Dimens.dp_16),
				title = stringResource(R.string.add_and_continue),
				onClick = {
					viewModel.submitIdProofDetails()
				},
				isEnabled = uiState.submitEnabled
			)
		}
	) { paddingValues ->
		when (uiState.uiState) {
			UiState.Loading -> ShowProgress()
			is UiState.Error -> ShowError()
			else -> Unit
		}
		AddKycScreen(
			modifier = Modifier
				.padding(paddingValues),
			isClickable = false,
			imagePath = uiState.imagePath,
			idProofType = uiState.idProofType,
			removeIdProof = viewModel::removeIdProof,
			captureIdProofPhoto = captureIdProofPhoto
		) {
			when (val ocr = uiState.ocrDetails) {
				is OcrDetails.AadhaarOcrDetails -> AadhaarInputFields(
					aadhaarOcrDetails = ocr,
					updateAadhaarNumber = viewModel::updateAadhaarNumber,
					updateName = viewModel::updateAadhaarName,
					updateDOB = viewModel::updateAadhaarDOB,
					updateGender = viewModel::updateGender
				)
				is OcrDetails.PanOcrDetails -> PanInputFields(
					panOcrDetails = ocr,
					updatePanNumber = viewModel::updatePanNumber,
					updateName = viewModel::updatePanName
				)
				else -> Unit
			}
		}
	}

	LaunchedEffect(Unit) {
		viewModel.idProofSubmitted.collect {
			navigateToCapturePayment(it, viewModel.ocrDetails)
		}
	}

	ComposeOnResumeCallback(lifecycleOwner = lifecycleOwner) {
		viewModel.updateOcrDetails()
		viewModel.updateSubmitButtonCtaState()
	}
}

@Composable
fun AadhaarInputFields(
	aadhaarOcrDetails: OcrDetails.AadhaarOcrDetails,
	updateAadhaarNumber: (String) -> Unit,
	updateName: (String) -> Unit,
	updateDOB: (String) -> Unit,
	updateGender: (String) -> Unit
) {
	TextUserInput(
		label = stringResource(R.string.kyc_aadhaar_card_number),
		value = aadhaarOcrDetails.aadhaarNumber,
		onInputChanged = updateAadhaarNumber,
		errorMessage = if (aadhaarOcrDetails.isAadhaarNumberInValid)
			stringResource(R.string.kyc_please_enter_valid_aadhaar_number)
		else null,
		keyboardType = KeyboardType.Number
	)

	VerticalSpacer(height = Dimens.dp_16)

	TextUserInput(
		label = stringResource(R.string.kyc_farmer_name),
		value = aadhaarOcrDetails.name,
		onInputChanged = updateName,
		errorMessage = if (aadhaarOcrDetails.isNameInValid) {
			stringResource(R.string.kyc_couldnt_capture_name_from_photo)
		} else {
			null
		},
		keyboardType = KeyboardType.Text
	)

	VerticalSpacer(height = Dimens.dp_16)

	TextUserInput(
		label = stringResource(R.string.kyc_dob),
		value = aadhaarOcrDetails.dateOfBirth,
		onInputChanged = updateDOB,
		errorMessage = if (aadhaarOcrDetails.isDateOfBirthInValid) {
			stringResource(R.string.kyc_please_enter_valid_dob)
		} else {
			null
		},
		keyboardType = KeyboardType.Text
	)

	VerticalSpacer(height = Dimens.dp_16)


	var expanded by remember {
		mutableStateOf(false)
	}
	var size by remember {
		mutableStateOf(Size.Zero)
	}
	TextUserInput(
		modifier = Modifier
			.clickable {
				expanded = expanded.not()
			}
			.onGloballyPositioned {
				size = it.size.toSize()
			},
		enabled = aadhaarOcrDetails.gender.isNotEmpty(),
		readOnly = true,
		label = stringResource(R.string.kyc_gender),
		value = aadhaarOcrDetails.gender,
		onInputChanged = {},
		errorMessage = if (aadhaarOcrDetails.isGenderInValid) {
			stringResource(R.string.kyc_couldnt_capture_gender)
		} else {
			null
		},
		keyboardType = KeyboardType.Text
	)
	DropdownMenu(
		modifier = Modifier.width(with(LocalDensity.current) { size.width.toDp() }),
		expanded = expanded, onDismissRequest = { expanded = false }) {
		listOf("Male", "Female").forEach {
			DropdownMenuItem(onClick = {
				updateGender(it)
				expanded = false
			}) {
				Text(text = it)
			}
		}
	}
}

@Composable
fun PanInputFields(
	panOcrDetails: OcrDetails.PanOcrDetails,
	updatePanNumber: (String) -> Unit,
	updateName: (String) -> Unit
) {
	TextUserInput(
		label = stringResource(R.string.kyc_pan_number),
		value = panOcrDetails.panNumber,
		onInputChanged = updatePanNumber,
		errorMessage = when {
			panOcrDetails.panNumber.isEmpty() -> stringResource(R.string.kyc_couldnt_capture_pan_number)
			panOcrDetails.isPanNumberInValid -> stringResource(R.string.kyc_please_enter_valid_pan_number)
			else -> null
		}
	)

	VerticalSpacer(height = Dimens.dp_16)

	TextUserInput(
		label = stringResource(R.string.kyc_farmer_name),
		value = panOcrDetails.name,
		onInputChanged = updateName,
		errorMessage = if (panOcrDetails.isNameInValid) {
			stringResource(R.string.kyc_couldnt_capture_name_from_photo)
		} else {
			null
		}
	)
}
