package com.dehaat.kyc.navigation

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.KycViewModel
import com.dehaat.kyc.feature.addkyc.CKycOtpScreen
import com.dehaat.kyc.feature.bankkyc.AddBankDetailsScreen
import com.dehaat.kyc.feature.bankkyc.BankKycScreen
import com.dehaat.kyc.feature.bankkyc.BankKycSuccessScreen
import com.dehaat.kyc.feature.bankkyc.BankOtpScreen
import com.dehaat.kyc.feature.bankkyc.BankVerificationViewModel
import com.dehaat.kyc.feature.bankkyc.model.BankVerifiedDetails
import com.dehaat.kyc.feature.bankkyc.model.VerificationStatus
import com.dehaat.kyc.feature.captureimage.CaptureIdProofPhoto
import com.dehaat.kyc.feature.captureimage.ImageUtils
import com.dehaat.kyc.feature.capturepayment.CapturePaymentScreen
import com.dehaat.kyc.feature.ckyc.CKycCompleteScreen
import com.dehaat.kyc.feature.ckycverification.KycVerificationScreen
import com.dehaat.kyc.feature.otp.OtpScreen
import com.dehaat.kyc.feature.otp.OtpViewModel
import com.dehaat.kyc.feature.updatekyc.AddKycDetailsScreen
import com.dehaat.kyc.framework.model.RegisterSalePaymentModeRequest
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.PrimaryTextColor
import com.dehaat.kyc.ui.theme.TextDimens
import com.dehaat.kyc.utils.ui.navBaseComposable
import com.dehaat.kyc.utils.ui.navigateTo
import com.dehaat.kyc.utils.ui.onClick
import kotlinx.coroutines.delay

@Composable
fun KycNavigation(
	finish: onClick,
	kycViewModel: KycViewModel,
	registerSale: Boolean,
	bankVerification: Boolean,
	continueWithoutInsurance: onClick,
	saleRecordSuccess: onClick,
	context: Context = LocalContext.current
) {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = if (bankVerification) KycRoute.BankKyc.route else KycRoute.CKycComplete.route
	) {
		navBaseComposable(route = KycRoute.CKycComplete.route) {
			CKycCompleteScreen(
				viewModel = kycViewModel,
				finish = finish
			) {
				navController.navigateToIdProofTypeSelectionScreen()
			}
		}

		navBaseComposable(route = KycRoute.BankKyc.route) {
			BankKycScreen(
				finish = finish,
				kycViewModel = kycViewModel,
				addBankAccount = {
					navController.navigateToUpdateBankKycScreen(
						farmerId = kycViewModel.farmerId,
						farmerName = kycViewModel.name
					)
				},
				updateBankAccount = { bankAccountsEntity ->
					if (bankAccountsEntity.verificationStatus == VerificationStatus.Approved) {
						navController.navigateTo(
							KycRoute.BankVerifiedScreen.route,
							args = bundleOf("BANK_VERIFIED_DETAILS" to bankAccountsEntity.toVerifiedBankDetails())
						)
					} else {
						navController.navigateToUpdateBankKycScreen(
							farmerId = kycViewModel.farmerId,
							farmerName = kycViewModel.name,
							kycId = bankAccountsEntity.kycId,
							verificationStatus = bankAccountsEntity.verificationStatus.value
						)
					}
				}
			)
		}

		navBaseComposable(route = KycRoute.BankVerification.route) {
			val viewModel = hiltViewModel<BankVerificationViewModel>()
			val passbookPhoto =
				it.savedStateHandle.getLiveData<String>(BankVerificationViewModel.PASSBOOK_PHOTO)
			passbookPhoto.observeForever {
				if (it.isNotNull()) {
					val compressedFile = ImageUtils.getCompressedFile(context, it)
					viewModel.updatePreviewPhoto(compressedFile)
				}
			}
			AddBankDetailsScreen(
				viewModel = viewModel,
				onSelectPhotoClick = { navController.navigateToCapturePhotoScreen(idProofType = IdProofType.Bank) },
				navigateToOtp = { navController.navigateTo(KycRoute.BankOtp.route) },
				onBackPress = { navController.navigateUp() },
				onBankKycSuccess = {
					navController.navigateTo(
						KycRoute.BankVerifiedScreen.route,
						args = bundleOf("BANK_VERIFIED_DETAILS" to it)
					)
				}
			)
		}

		navBaseComposable(KycRoute.BankVerifiedScreen.route) {
			val bankVerifiedDetails =
				it.arguments?.getParcelable<BankVerifiedDetails>("BANK_VERIFIED_DETAILS")
			BankKycSuccessScreen(finish, bankVerifiedDetails)
		}

		navBaseComposable(KycRoute.BankOtp.route) {
			BankOtpScreen(
				viewModel = hiltViewModel(), navigateToPayments = {
					navController.navigateTo(
						KycRoute.BankVerifiedScreen.route,
						args = bundleOf("BANK_VERIFIED_DETAILS" to it)
					)
				}
			)
		}

		navBaseComposable(route = KycRoute.IdProofTypeSelection.route) {
			/*IdProofTypeSelectionScreen(
				finish = finish,
				captureIdProofPhoto = { proofType ->
					kycViewModel.updateIdProofType(proofType)
					navController.navigateToCapturePhotoScreen(proofType)
				},
				idProofType = kycViewModel.idProofType,
				updateIdProofType = {
					kycViewModel.updateIdProofType(it)
				},
				sampleImageMap = kycViewModel.bottomSheetMap
			)*/
		}

		navBaseComposable(route = KycRoute.CapturePhoto.route) {
			CaptureIdProofPhoto(
				onBackPress = { navController.navigateUp() },
				onPassBookPhoto = {
					with(navController) {
						previousBackStackEntry?.savedStateHandle?.set<String>(
							BankVerificationViewModel.PASSBOOK_PHOTO, it
						)
						popBackStack()
					}
				}
			) { ocrDetails ->
				navController.navigateToUpdateKycScreen(
					ocrDetails = ocrDetails,
					farmerId = kycViewModel.farmerId,
					proofType = kycViewModel.proofTypeId,
					idProofType = kycViewModel.idProofType
				)
			}
		}

		navBaseComposable(route = KycRoute.UpdateKyc.route) {
			AddKycDetailsScreen(
				onBackPress = { navController.navigateUp() },
				captureIdProofPhoto = {
					navController.navigateToCapturePhotoScreen(kycViewModel.idProofType)
				}
			) { proofId, ocrDetails ->
				kycViewModel.proofId = proofId
				kycViewModel.updateOcrDetails(ocrDetails)
				if (registerSale) {
					navController.navigateTo(
						KycRoute.CapturePayment.route,
						KycRoute.CapturePayment.getArgs(kycViewModel.payableAmount)
					)
				} else {
					navController.navigateToKycOtpScreen(
						farmerId = kycViewModel.farmerId,
						name = kycViewModel.name,
						phoneNumber = kycViewModel.phoneNumber,
						proofTypeId = kycViewModel.proofTypeId,
						farmerAuthId = kycViewModel.farmerAuthId
					)
				}
			}
		}

		navBaseComposable(route = KycRoute.KycVerificationOtp.route) {
			CKycOtpScreen {
				kycViewModel.updateOtpHashCode(it)
				navController.navigateToKycVerificationScreen(
					ocrDetails = kycViewModel.ocrDetails,
					farmerId = kycViewModel.farmerId,
					name = kycViewModel.name,
					phoneNumber = kycViewModel.phoneNumber,
					idProofType = kycViewModel.idProofType,
					proofId = kycViewModel.proofId,
					registerSale = false,
					isKycSuccessful = false,
					registerSaleRequest = null
				)
			}
		}

		navBaseComposable(route = KycRoute.CapturePayment.route) {
			CapturePaymentScreen(onBackPress = { navController.navigateUp() }) {
				if (kycViewModel.hashCode.isNotNull()) {
					navController.navigateToKycVerificationScreen(
						ocrDetails = kycViewModel.ocrDetails,
						farmerId = kycViewModel.farmerId,
						name = kycViewModel.name,
						phoneNumber = kycViewModel.phoneNumber,
						idProofType = kycViewModel.idProofType,
						proofId = kycViewModel.proofId,
						registerSale = true,
						isKycSuccessful = kycViewModel.isCKycComplete,
						registerSaleRequest = kycViewModel.registerSaleRequest
					)
				} else {
					navController.navigateTo(
						KycRoute.OtpValidation.route,
						KycRoute.OtpValidation.getArgs(
							it,
							kycViewModel.farmerId,
							kycViewModel.name,
							kycViewModel.phoneNumber
						)
					)
				}
			}
		}

		navBaseComposable(route = KycRoute.OtpValidation.route) {
			val viewModel: OtpViewModel = hiltViewModel()
			val modes = it.arguments?.getParcelableArrayList<RegisterSalePaymentModeRequest>(
				OtpViewModel.PAYMENT_MODES
			).orEmpty()

			with(viewModel) {
				kycViewModel.registerSaleRequest?.let { saleReq ->
					updateRegisterSaleRequest(saleReq)
					sendOtpViaSms(false)
				} ?: KycExecutor.getRegisterSaleRequest(modes)?.let { saleReq ->
					updateRegisterSaleRequest(saleReq)
					kycViewModel.updateSaleRequest(saleReq)
				}
			}

			OtpScreen(
				viewModel = viewModel,
				continueWithoutInsurance = continueWithoutInsurance
			) {
				navController.navigateToKycVerificationScreen(
					ocrDetails = kycViewModel.ocrDetails,
					farmerId = kycViewModel.farmerId,
					name = kycViewModel.name,
					phoneNumber = kycViewModel.phoneNumber,
					idProofType = kycViewModel.idProofType,
					proofId = kycViewModel.proofId,
					registerSale = true,
					isKycSuccessful = kycViewModel.isCKycComplete,
					registerSaleRequest = kycViewModel.registerSaleRequest
				)
			}
			LaunchedEffect(Unit) {
				viewModel.otpHashCode.collect {
					kycViewModel.updateOtpHashCode(it)
				}
			}
		}

		navBaseComposable(route = KycRoute.KycVerification.route) {
			KycVerificationScreen(
				registerSale = registerSale,
				continueWithoutInsurance = continueWithoutInsurance,
				recordSalesSuccess = {
					navController.navigateTo(KycRoute.RecordSaleComplete.route)
				},
				retryCKyc = {
					kycViewModel.updateIdProofType(it)
					navController.navigateToIdProofTypeSelectionScreen(it)
				}
			) {
				navController.navigateTo(KycRoute.CKycComplete.route)
			}
		}

		navBaseComposable(route = KycRoute.RecordSaleComplete.route) {
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
	}
}
