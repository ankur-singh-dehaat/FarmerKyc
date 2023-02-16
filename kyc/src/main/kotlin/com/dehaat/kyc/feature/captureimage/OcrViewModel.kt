package com.dehaat.kyc.feature.captureimage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.androidbase.helper.orZero
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.feature.captureimage.model.OcrViewModelState
import com.dehaat.kyc.framework.network.usecase.AadhaarOcrUseCase
import com.dehaat.kyc.framework.network.usecase.PanOcrUseCase
import com.dehaat.kyc.model.IdProofType
import com.dehaat.kyc.model.KycMapper
import com.dehaat.kyc.utils.ui.Constants
import com.dehaat.kyc.utils.ui.getIdProofType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class OcrViewModel @Inject constructor(
	private val aadhaarOcrUseCase: AadhaarOcrUseCase,
	private val panOcrUseCase: PanOcrUseCase,
	private val mapper: KycMapper,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val viewModelState = MutableStateFlow(OcrViewModelState())
	val uiState = viewModelState.map {
		it.toUiState()
	}.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
	)

	private val _ocrDetails = MutableSharedFlow<OcrDetails?>()
	val ocrDetails: SharedFlow<OcrDetails?> = _ocrDetails

	private val _bankPassBookPhoto = MutableSharedFlow<String>()
	val bankPassBookPhoto: SharedFlow<String> = _bankPassBookPhoto

	init {
		viewModelState.update {
			it.copy(
				idProofType = savedStateHandle.getIdProofType()
			)
		}
	}

	private val farmerId by lazy { savedStateHandle.get<Long>(Constants.FARMER_ID).orZero() }

	fun getIdProofDetails(
		photo: String
	) = callInViewModelScope {
		updateLoader(true)
		ImageUtils.getCompressedBase64String(photo)?.let {
			getOcrDetails(it, photo)
		} ?: updateLoader(false)
	}

	private suspend fun getOcrDetails(base64Image: String, imagePath: String) =
		when (viewModelState.value.idProofType) {
			is IdProofType.Pan -> {
				when (val result = panOcrUseCase(frontUrl = base64Image, farmerId = farmerId)) {
					is APIResultEntity.Success -> {
						viewModelState.update {
							it.copy(
								isLoading = false,
								isSuccess = true
							)
						}
						_ocrDetails.emit(
							mapper.toPanOcrDetails(
								result.data,
								imagePath
							)
						)
					}
					is APIResultEntity.Failure -> updateLoader(false)
				}
			}
			is IdProofType.Aadhaar -> {
				when (val result = aadhaarOcrUseCase(frontUrl = base64Image, farmerId = farmerId)) {
					is APIResultEntity.Success -> {
						viewModelState.update {
							it.copy(
								isLoading = false,
								isSuccess = true,
								isBlurredImage = result.data?.error.isNotNull()
							)
						}
						result.data?.result?.let {
							_ocrDetails.emit(
								mapper.toAadhaarOcrDetails(
									it,
									imagePath
								)
							)
						}
					}
					is APIResultEntity.Failure -> updateLoader(false)
				}
			}
			is IdProofType.Bank -> {
				_bankPassBookPhoto.emit(imagePath)
			}
		}

	private fun updateLoader(isLoading: Boolean) = viewModelState.update {
		it.copy(isLoading = isLoading, isSuccess = isLoading.not())
	}

	fun retakePhoto() = viewModelState.update {
		it.copy(isBlurredImage = false)
	}
}
