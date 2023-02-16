package com.dehaat.kyc.feature.captureimage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehaat.kyc.R
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.model.UiState
import com.dehaat.kyc.ui.theme.Dimens
import com.dehaat.kyc.ui.theme.Error100
import com.dehaat.kyc.ui.theme.Neutral90
import com.dehaat.kyc.ui.theme.Primary100
import com.dehaat.kyc.ui.theme.textButtonB1
import com.dehaat.kyc.ui.theme.textParagraphT2
import com.dehaat.kyc.ui.theme.textSubHeadingS2
import com.dehaat.kyc.utils.ui.HorizontalSpacer
import com.dehaat.kyc.utils.ui.VerticalSpacer

@Composable
fun CaptureIdProofPhoto(
	onBackPress: () -> Unit = {},
	context: Context = LocalContext.current,
	viewModel: OcrViewModel = hiltViewModel(),
	onPassBookPhoto: (String) -> Unit = {},
	onImageFile: (OcrDetails) -> Unit = {}
) = Column {
	val uiState by viewModel.uiState.collectAsState()
	LaunchedEffect(Unit) {
		viewModel.ocrDetails.collect { ocrUiState ->
			ocrUiState?.let(onImageFile)
		}
	}

	LaunchedEffect(Unit) {
		viewModel.bankPassBookPhoto.collect(onPassBookPhoto)
	}

	val executor = remember(context) { ContextCompat.getMainExecutor(context) }
	val lifecycleOwner = LocalLifecycleOwner.current
	var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
	var hasCamPermission by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.CAMERA
			) == PackageManager.PERMISSION_GRANTED
		)
	}
	var isFlashEnabled by remember { mutableStateOf(false) }
	var cameraControl: CameraControl? by remember { mutableStateOf(null) }
	val previewView = remember { PreviewView(context) }

	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
		onResult = { granted ->
			hasCamPermission = granted
		}
	)

	val galleryLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent()
	) { imageUri: Uri? ->
		imageUri?.let {
			URIPathHelper().getPath(context, it)?.let { image ->
				if (uiState.idProofType is IdProofType.Bank) {
					onPassBookPhoto(image)
				} else {
					viewModel.getIdProofDetails(image)
				}
			}
		}
	}

	val galleryPermissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
		onResult = { granted ->
			if (granted)
				galleryLauncher.launch("image/*")
			else {
				Toast.makeText(context, "Camera Permission Required", Toast.LENGTH_LONG).show()
			}
		}
	)

	val imageCapturedCallback = object : ImageCapture.OnImageCapturedCallback() {
		override fun onCaptureSuccess(image: ImageProxy) {
			super.onCaptureSuccess(image)
			val file = convertImageProxyToFile(context, image)
			Uri.fromFile(file).path?.let { imagePath ->
				if (uiState.idProofType is IdProofType.Bank) {
					onPassBookPhoto(imagePath)
				} else {
					viewModel.getIdProofDetails(imagePath)
				}
			}
			image.close()
		}

		override fun onError(exception: ImageCaptureException) {
			super.onError(exception)
			Toast.makeText(context, "Image capture failed: ${exception.message}", Toast.LENGTH_LONG)
				.show()
		}
	}

	LaunchedEffect(Unit) {
		launcher.launch(Manifest.permission.CAMERA)

		val preview = Preview.Builder().build().also {
			it.setSurfaceProvider(previewView.surfaceProvider)
		}

		val selector = CameraSelector.Builder().requireLensFacing(
			CameraSelector.LENS_FACING_BACK
		).build()

		imageCapture = ImageCapture.Builder().build()

		val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

		cameraProviderFuture.addListener(
			{
				try {
					cameraProviderFuture.get().unbindAll()
					cameraControl = cameraProviderFuture.get().bindToLifecycle(
						lifecycleOwner, selector, preview, imageCapture
					).cameraControl
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}, ContextCompat.getMainExecutor(context)
		)
	}

	if (hasCamPermission) {
		Box(modifier = Modifier.fillMaxSize()) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(Color.Black)
					.verticalScroll(rememberScrollState()),
				verticalArrangement = Arrangement.SpaceBetween,
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Column(horizontalAlignment = Alignment.CenterHorizontally) {
					Icon(
						painter = painterResource(id = R.drawable.ic_back),
						contentDescription = "",
						tint = Color.White,
						modifier = Modifier
							.padding(top = 20.dp)
							.clickable { onBackPress() }
							.align(Alignment.Start)
					)
					Text(
						stringResource(R.string.kyc_keep_id_proof_inside_box),
						color = Color.White,
						modifier = Modifier.padding(bottom = Dimens.dp_20, top = Dimens.dp_40)
					)
					AndroidView(
						factory = { previewView },
						modifier = Modifier
							.size(360.dp, 210.dp),
						update = {
							cameraControl?.enableTorch(isFlashEnabled)
						}
					)
				}
				Row(
					modifier = Modifier
						.padding(top = 10.dp, bottom = 40.dp)
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceEvenly,
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						modifier = Modifier
							.size(48.dp)
							.clickable {
								galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
							},
						painter = painterResource(id = R.drawable.ic_gallery_photo),
						contentDescription = "Click Photo",
						tint = Color.White
					)

					Icon(
						modifier = Modifier
							.size(Dimens.dp_64)
							.clickable {
								imageCapture?.takePicture(
									executor, imageCapturedCallback
								)
							},
						painter = painterResource(id = R.drawable.ic_take_photo),
						contentDescription = "Click Photo",
						tint = Color.White
					)

					Icon(
						modifier = Modifier
							.size(48.dp)
							.clickable { isFlashEnabled = !isFlashEnabled },
						painter = painterResource(id = if (isFlashEnabled) R.drawable.ic_flash else R.drawable.ic_flash),
						contentDescription = "",
						tint = Color.White
					)
				}
			}

			if (uiState.isBlurredImage)
				Dialog(onDismissRequest = {}) {
					Column(
						modifier = Modifier
							.align(Alignment.Center)
							.fillMaxWidth()
							.padding(horizontal = Dimens.dp_16)
							.background(
								Color.White,
								shape = RoundedCornerShape(Dimens.dp_16)
							)
							.padding(Dimens.dp_16)
					) {
						Row(
							modifier = Modifier
								.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically
						) {
							Image(
								modifier = Modifier.size(Dimens.dp_20),
								painter = painterResource(id = R.drawable.kyc_error),
								contentDescription = ""
							)
							HorizontalSpacer(width = Dimens.dp_8)
							Text(
								text = stringResource(R.string.kyc_blurred_image),
								style = textSubHeadingS2(Error100)
							)
						}

						VerticalSpacer(height = Dimens.dp_12)

						Text(
							text = stringResource(R.string.kyc_image_couldnt_captured),
							style = textParagraphT2(Neutral90)
						)

						VerticalSpacer(height = Dimens.dp_40)

						Row(
							modifier = Modifier
								.fillMaxWidth()
								.background(
									color = Primary100,
									shape = RoundedCornerShape(Dimens.dp_16)
								)
								.clickable { viewModel.retakePhoto() }
								.padding(vertical = Dimens.dp_14),
							horizontalArrangement = Arrangement.Center,
							verticalAlignment = Alignment.CenterVertically
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_kyc_retry),
								contentDescription = "",
								tint = Color.White
							)
							HorizontalSpacer(width = Dimens.dp_4)
							Text(
								text = stringResource(R.string.kyc_retake),
								style = textButtonB1(Color.White)
							)
						}
					}
				}
			if (uiState.uiState is UiState.Loading) {
				Dialog(onDismissRequest = {}) {
					Column(
						modifier = Modifier
							.align(Alignment.Center)
							.background(Color.White, RoundedCornerShape(8.dp))
							.padding(36.dp)
					) {
						CircularProgressIndicator(modifier = Modifier.size(60.dp))

						VerticalSpacer(height = 17.dp)

						Text(text = stringResource(R.string.kyc_loading))
					}
				}
			}
		}
	} else {
		Box(Modifier.fillMaxSize()) {
			Text(
				stringResource(R.string.kyc_permission_required),
				Modifier.align(Alignment.Center)
			)
		}
	}
}
