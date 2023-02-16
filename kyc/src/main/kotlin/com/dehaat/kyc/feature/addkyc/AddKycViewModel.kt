package com.dehaat.kyc.feature.addkyc

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.androidbase.helper.orFalse
import com.dehaat.androidbase.helper.orZero
import com.dehaat.kyc.feature.addkyc.model.AddKycViewModelState
import com.dehaat.kyc.feature.addkyc.model.OcrDetails
import com.dehaat.kyc.framework.network.usecase.AddIdProofUseCase
import com.dehaat.kyc.utils.ui.Constants
import com.dehaat.kyc.utils.ui.getIdProofType
import com.dehaat.kyc.utils.ui.removeWhiteSpace
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
class AddKycViewModel @Inject constructor(
	private val addIdProofUseCase: AddIdProofUseCase,
	private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val viewModelState = MutableStateFlow(AddKycViewModelState())
	val uiState = viewModelState.map {
		it.toUiState()
	}.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
	)

	private val _idProofSubmitted = MutableSharedFlow<Long>()
	val idProofSubmitted: SharedFlow<Long> = _idProofSubmitted

	val ocrDetails: OcrDetails?
		get() = viewModelState.value.ocrDetails

	private val farmerId by lazy { savedStateHandle.get<Long>(Constants.FARMER_ID).orZero() }
	private val documentId by lazy { savedStateHandle.get<Long>(Constants.DOCUMENT_ID).orZero() }

	fun updateOcrDetails() {
		val ocr = savedStateHandle.get<OcrDetails>(OCR_DETAILS)
		when (ocr) {
			is OcrDetails.AadhaarOcrDetails -> with(ocr) {
				isAadhaarNumberInValid = checkAadhaarNumberValidation(aadhaarNumber)
				isGenderInValid = gender.isEmpty()
				isNameInValid = name.isEmpty()
				isDateOfBirthInValid = dateOfBirth.isEmpty()
			}

			is OcrDetails.PanOcrDetails -> with(ocr) {
				isPanNumberInValid = checkPanNumberValidation(panNumber)
				isNameInValid = name.isEmpty()
			}
			else -> Unit
		}
		viewModelState.update {
			it.copy(
				idProofType = savedStateHandle.getIdProofType(),
				imagePath = when (ocr) {
					is OcrDetails.AadhaarOcrDetails -> ocr.imageUrl
					is OcrDetails.PanOcrDetails -> ocr.imageUrl
					else -> ""
				},
				ocrDetails = ocr
			)
		}
	}

	fun submitIdProofDetails() = callInViewModelScope {
		viewModelState.update { it.copy(isLoading = true) }
		val state = viewModelState.value
		val response = addIdProofUseCase(
			farmerId = farmerId,
			documentId = documentId,
			idProofNumber = when (state.ocrDetails) {
				is OcrDetails.AadhaarOcrDetails -> state.ocrDetails.aadhaarNumber.removeWhiteSpace()
				is OcrDetails.PanOcrDetails -> state.ocrDetails.panNumber.removeWhiteSpace()
				else -> ""
			}
		)

		when (response) {
			is APIResultEntity.Success -> {
				viewModelState.update {
					it.copy(
						isLoading = false,
						isSuccess = true
					)
				}
				_idProofSubmitted.emit(response.data?.id.orZero())
			}
			is APIResultEntity.Failure -> viewModelState.update {
				it.copy(
					isLoading = false,
					isError = true
				)
			}
		}
	}

	fun removeIdProof() = viewModelState.update {
		it.copy(
			imagePath = null,
			ocrDetails = null,
			isSubmitEnabled = false
		)
	}

	fun updateSubmitButtonCtaState() = viewModelState.update {
		when (ocrDetails) {
			is OcrDetails.AadhaarOcrDetails -> {
				it.copy(
					isSubmitEnabled = (ocrDetails as? OcrDetails.AadhaarOcrDetails)?.let {
						validateAadhaarOcrDetails(it)
					}.orFalse()
				)
			}
			is OcrDetails.PanOcrDetails -> {
				it.copy(
					isSubmitEnabled = (ocrDetails as? OcrDetails.PanOcrDetails)?.let {
						validatePanOcrDetails(it)
					}.orFalse()
				)
			}
			else -> it.copy()
		}
	}

	fun updateAadhaarNumber(aadhaarNumber: String) {
		val ocrDetails = viewModelState.value.ocrDetails as? OcrDetails.AadhaarOcrDetails
		viewModelState.update {
			val aadhaarDetails = ocrDetails?.copy(
				aadhaarNumber = aadhaarNumber,
				isAadhaarNumberInValid = checkAadhaarNumberValidation(aadhaarNumber)
			) ?: OcrDetails.AadhaarOcrDetails(
				aadhaarNumber = aadhaarNumber,
				isAadhaarNumberInValid = checkAadhaarNumberValidation(aadhaarNumber = aadhaarNumber)
			)
			it.copy(ocrDetails = aadhaarDetails)
		}
		updateSubmitButtonCtaState()
	}

	private fun checkAadhaarNumberValidation(
		aadhaarNumber: String
	) = aadhaarNumber.matches(
		Regex(
			Constants.AADHAAR_REGEX
		)
	).not()

	fun updateAadhaarName(name: String) {
		val ocrDetails = viewModelState.value.ocrDetails as? OcrDetails.AadhaarOcrDetails
		viewModelState.update {
			val aadhaarDetails = ocrDetails?.copy(
				name = name,
				isNameInValid = name.isEmpty()
			) ?: OcrDetails.AadhaarOcrDetails(
				name = name,
				isNameInValid = name.isEmpty()
			)
			it.copy(ocrDetails = aadhaarDetails)
		}
		updateSubmitButtonCtaState()
	}

	fun updateAadhaarDOB(dob: String) {
		val ocrDetails = viewModelState.value.ocrDetails as? OcrDetails.AadhaarOcrDetails
		viewModelState.update {
			val aadhaarDetails = ocrDetails?.copy(
				dateOfBirth = dob,
				isDateOfBirthInValid = checkDateOfBirthValidation(dob)
			) ?: OcrDetails.AadhaarOcrDetails(
				dateOfBirth = dob,
				isDateOfBirthInValid = checkDateOfBirthValidation(dob)
			)
			it.copy(ocrDetails = aadhaarDetails)
		}
		updateSubmitButtonCtaState()
	}

	fun checkDateOfBirthValidation(
		dateOfBirth: String
	) = dateOfBirth.matches(
		Regex(Constants.DATE_OF_BIRTH_REGEX)
	).not()

	fun updateGender(gender: String) {
		val ocrDetails = viewModelState.value.ocrDetails as? OcrDetails.AadhaarOcrDetails
		viewModelState.update {
			val aadhaarDetails = ocrDetails?.copy(
				gender = gender,
				isGenderInValid = gender.isEmpty()
			) ?: OcrDetails.AadhaarOcrDetails(
				gender = gender,
				isGenderInValid = gender.isEmpty()
			)
			it.copy(ocrDetails = aadhaarDetails)
		}
		updateSubmitButtonCtaState()
	}

	fun updatePanNumber(panNumber: String) {
		val ocrDetails = viewModelState.value.ocrDetails as? OcrDetails.PanOcrDetails
		viewModelState.update {
			val panDetails = ocrDetails?.copy(
				panNumber = panNumber,
				isPanNumberInValid = checkPanNumberValidation(panNumber)
			) ?: OcrDetails.PanOcrDetails(
				panNumber = panNumber,
				isPanNumberInValid = checkPanNumberValidation(panNumber)
			)
			it.copy(ocrDetails = panDetails)
		}
		updateSubmitButtonCtaState()
	}

	private fun checkPanNumberValidation(panNumber: String) =
		panNumber.matches(Regex(Constants.PAN_REGEX)).not()

	fun updatePanName(name: String) {
		val ocrDetails = viewModelState.value.ocrDetails as? OcrDetails.PanOcrDetails
		viewModelState.update {
			val panDetails = ocrDetails?.copy(
				name = name,
				isNameInValid = name.isEmpty()
			) ?: OcrDetails.PanOcrDetails(
				name = name,
				isNameInValid = name.isEmpty()
			)
			it.copy(ocrDetails = panDetails)
		}
		updateSubmitButtonCtaState()
	}

	private fun validateAadhaarOcrDetails(aadhaarOcrDetails: OcrDetails.AadhaarOcrDetails) =
		with(aadhaarOcrDetails) {
			isAadhaarNumberInValid.not() &&
					isDateOfBirthInValid.not() &&
					gender.isNotNull() &&
					name.isNotEmpty()
		}

	private fun validatePanOcrDetails(panOcrDetails: OcrDetails.PanOcrDetails) =
		with(panOcrDetails) {
			name.isNotEmpty() && isPanNumberInValid.not()
		}

	companion object {
		const val OCR_DETAILS = "OCR_DETAILS"
	}
}
