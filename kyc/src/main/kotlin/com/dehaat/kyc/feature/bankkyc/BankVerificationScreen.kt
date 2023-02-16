@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.dehaat.kyc.feature.bankkyc

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dehaat.androidbase.helper.orTrue
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.bankkyc.model.BankDetailsUiState
import com.dehaat.kyc.feature.bankkyc.model.BankDetailsValidationState
import com.dehaat.kyc.feature.bankkyc.model.BankVerifiedDetails
import com.dehaat.kyc.feature.bankkyc.model.CtaState
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.feature.idprooftypeselection.ViewSampleBottomSheetContent
import com.dehaat.kyc.model.UiState
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Error100
import com.dehaat.kyc.ui.theme.Neutral100
import com.dehaat.kyc.ui.theme.Neutral70
import com.dehaat.kyc.ui.theme.Neutral90
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.TextDimens
import com.dehaat.kyc.ui.theme.textButtonB1
import com.dehaat.kyc.ui.theme.textSubHeadingS3
import com.dehaat.kyc.utils.ui.Button
import com.dehaat.kyc.utils.ui.PrimaryAppBar
import com.dehaat.kyc.utils.ui.ShowError
import com.dehaat.kyc.utils.ui.TextUserInput
import com.dehaat.kyc.utils.ui.VerticalSpacer
import com.dehaat.kyc.utils.ui.closeSheet
import com.dehaat.kyc.utils.ui.openSheet
import com.dehaat.kyc.utils.ui.uppercaseLettersOrDigits
import com.skydoves.landscapist.glide.GlideImage
import java.io.File

@Composable
fun AddBankDetailsScreen(
	viewModel: BankVerificationViewModel,
	onSelectPhotoClick: () -> Unit,
	navigateToOtp: (String?) -> Unit,
	onBankKycSuccess: (BankVerifiedDetails) -> Unit,
	onBackPress: () -> Unit
) {
	val uiState by viewModel.uiState.collectAsState()
	val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
	val scope = rememberCoroutineScope()

	val bankDetails = uiState.bankDetailsValidationState
	val ctaState by remember(uiState) {
		mutableStateOf(
			when {
				bankDetails.isValid -> CtaState.Enabled
				else -> CtaState.Disabled
			}
		)
	}

	ModalBottomSheetLayout(
		modifier = Modifier,
		sheetContent = {
			Column(
				modifier = Modifier.fillMaxWidth()
			) {
				/*ViewSampleBottomSheetContent(
					"Passbook",
					uiState.bottomSheetData.sampleUrl
				) {
					sheetState.closeSheet(scope)
				}*/
			}
		},
		sheetBackgroundColor = Color.White,
		sheetState = sheetState,
		sheetShape = RoundedCornerShape(topStart = Dimens.dp_16, topEnd = Dimens.dp_16)
	) {
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			topBar = {
				PrimaryAppBar(
					title = "Bank Verification",
					onBackPress = onBackPress,
					content = {
						BankVerificationStatus(
							verificationStatus = uiState.verificationStatus
						)
					}
				)
			},
			bottomBar = {
				Button(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = Dimens.dp_32)
						.padding(bottom = Dimens.dp_24, top = Dimens.dp_8),
					title = "Submit",
					isEnabled = ctaState == CtaState.Enabled
				) {
					if (uiState.verificationStatus == VerificationStatus.Rejected) {
						viewModel.navigateToOtpScreen()
					} else {
						viewModel.submitBankDetails()
					}
				}
			}
		) { paddingValues ->
			when (uiState.uiState) {
				UiState.Loading -> {
					Dialog(onDismissRequest = {}) {
						Box(Modifier.fillMaxSize()) {
							Column(
								modifier = Modifier
									.align(Alignment.Center)
									.background(Color.White, RoundedCornerShape(8.dp))
									.padding(36.dp)
							) {
								CircularProgressIndicator(modifier = Modifier.size(60.dp))

								VerticalSpacer(height = 17.dp)

								Text(text = stringResource(id = R.string.loading))
							}
						}
					}
				}
				is UiState.Error -> {
					ShowError()
				}
				UiState.Success -> {
					AddBankDetails(
						paddingValues = paddingValues,
						viewModel = viewModel,
						bankDetails = bankDetails,
						uiState = uiState,
						onSelectPhotoClick = onSelectPhotoClick
					) {
						sheetState.openSheet(scope)
					}
				}
			}
		}
	}

	LaunchedEffect(Unit) {
		viewModel.navigateSuccessBankVerification.collect { event ->
			onBankKycSuccess(event)
		}
	}

	LaunchedEffect(Unit) {
		viewModel.navigateToOtp.collect {
			if (it) navigateToOtp(viewModel.uiState.value.previewImagePath)
		}
	}
}

@Composable
fun AddBankDetails(
	paddingValues: PaddingValues,
	viewModel: BankVerificationViewModel,
	bankDetails: BankDetailsValidationState,
	uiState: BankDetailsUiState,
	onSelectPhotoClick: () -> Unit,
	openSheet: () -> Unit
) = Column(
	modifier = Modifier
		.padding(paddingValues)
		.verticalScroll(rememberScrollState())
) {
	VerticalSpacer(height = 20.dp)

	Text(
		modifier = Modifier.padding(horizontal = 24.dp),
		text = "Add Bank details",
		style = textSubHeadingS3(Neutral100)
	)

	VerticalSpacer(height = 4.dp)

	TextUserInput(
		label = "Account Number*",
		value = bankDetails.accountNumber,
		onInputChanged = {
			viewModel.checkAccountNumberValidation(it)
		},
		errorMessage = if (bankDetails.isAccountNumberValid.orTrue()) {
			null
		} else {
			"Enter Correct Account Number"
		},
		keyboardType = KeyboardType.Number
	)

	TextUserInput(
		label = "Enter Account Number again*",
		value = bankDetails.confirmAccountNumber,
		onInputChanged = {
			viewModel.checkConfirmAccountNumberValidation(it)
		},
		errorMessage = if (bankDetails.isConfirmAccountNumberValid.orTrue()) {
			null
		} else {
			"Confirm Account no does not match"
		},
		keyboardType = KeyboardType.Number
	)

	TextUserInput(
		label = "Account holder Name*",
		value = bankDetails.bankAccountHolderName,
		onInputChanged = {
			viewModel.checkAccountHolderNameValidation(it)
		},
		errorMessage = if (bankDetails.isAccountHolderNameValid.orTrue()) {
			null
		} else {
			"Cannot be empty"
		}
	)

	TextUserInput(
		label = "Bank IFSC*",
		value = bankDetails.ifscCode,
		onInputChanged = {
			viewModel.checkIfscCodeValidation(it.uppercaseLettersOrDigits())
		},
		errorMessage = if (bankDetails.isIfscCodeValid.orTrue()) {
			null
		} else {
			"Invalid IFSC"
		}
	)

	bankDetails.bankAndBranchName?.let {
		Text(
			it,
			color = Neutral70,
			fontSize = TextDimens.sp_12,
			modifier = Modifier.padding(start = Dimens.dp_32, top = Dimens.dp_4)
		)
	}

	VerticalSpacer(height = 20.dp)

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 32.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			modifier = Modifier.weight(1F),
			text = "Add passbook photo",
			style = textSubHeadingS3(Neutral90)
		)

		Text(
			modifier = Modifier.clickable(onClick = openSheet),
			text = stringResource(id = R.string.view_sample),
			style = textButtonB1(Primary100)
		)
	}

	VerticalSpacer(height = 24.dp)

	PhotoUploadBucket(
		preview = uiState.previewImage,
		selectedPhoto = uiState.verifiedData?.passbookPhoto,
		openCamera = onSelectPhotoClick,
		updateAllowed = true
	) {
		viewModel.updatePreviewPhoto()
	}

	VerticalSpacer(height = 40.dp)
}

private fun String.uppercaseLettersOrDigits() = filter { it.isLetterOrDigit() }.uppercase()

@Composable
fun PhotoUploadBucket(
	preview: File?,
	selectedPhoto: String?,
	openCamera: () -> Unit,
	updateAllowed: Boolean,
	updatePhoto: () -> Unit
) = Box(modifier = Modifier.fillMaxWidth()) {
	Icon(
		modifier = Modifier
			.padding(start = 32.dp)
			.align(Alignment.TopStart),
		painter = painterResource(id = R.drawable.ic_top_left_arc),
		contentDescription = "Top Left Icon"
	)

	Icon(
		modifier = Modifier
			.padding(end = 32.dp)
			.align(Alignment.TopEnd),
		painter = painterResource(id = R.drawable.ic_top_right_arc),
		contentDescription = "Top Right Icon"
	)

	Surface(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp)
			.padding(horizontal = 40.dp)
			.height(200.dp),
		onClick = openCamera,
		enabled = selectedPhoto.isNullOrEmpty() || updateAllowed,
		shape = RoundedCornerShape(16.dp),
		elevation = 12.dp
	) {
		when {
			preview != null -> {
				Box {
					GlideImage(
						imageModel = { preview },
					)
				}
			}
			selectedPhoto != null -> {
				GlideImage(
					imageModel = { selectedPhoto },
				)
			}
			else -> {
				Column(
					modifier = Modifier
						.padding(vertical = 60.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Icon(
						painter = painterResource(R.drawable.ic_upload_icon),
						contentDescription = "Upload Photo Button",
						tint = Neutral70
					)

					VerticalSpacer(height = 8.dp)

					Text(
						text = "Add photo of passbook",
						style = textSubHeadingS3(Neutral90)
					)
				}
			}
		}
	}
	if (preview != null) {
		Surface(
			modifier = Modifier
				.padding(end = 32.dp)
				.align(Alignment.TopEnd)
				.background(Color.White, shape = CircleShape)
				.clip(CircleShape)
				.clickable(onClick = updatePhoto),
			elevation = 14.dp
		) {
			Icon(
				modifier = Modifier
					.background(Color.White, shape = CircleShape)
					.clip(CircleShape)
					.padding(6.dp),
				painter = painterResource(id = R.drawable.ic_cross),
				contentDescription = "",
				tint = Error100
			)
		}
	}
	Icon(
		modifier = Modifier
			.padding(start = 32.dp)
			.align(Alignment.BottomStart),
		painter = painterResource(id = R.drawable.ic_bottom_left_arc),
		contentDescription = "Bottom Left Icon"
	)

	Icon(
		modifier = Modifier
			.padding(end = 32.dp)
			.align(Alignment.BottomEnd),
		painter = painterResource(id = R.drawable.ic_bottom_right_arc),
		contentDescription = "Bottom Right Icon"
	)
}
