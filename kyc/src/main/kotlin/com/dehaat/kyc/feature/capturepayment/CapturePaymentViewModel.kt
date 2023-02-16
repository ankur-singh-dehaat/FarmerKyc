package com.dehaat.kyc.feature.capturepayment

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.kyc.features.recordsale.KycRecordSaleActivity
import com.dehaat.kyc.framework.model.RegisterSalePaymentModeRequest
import com.dehaat.kyc.framework.network.usecase.PaymentModesUseCase
import com.dehaat.kyc.model.KycMapper
import com.dehaat.kyc.navigation.KycExecutor
import com.dehaat.kyc.utils.ui.toDoubleOrZero
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class CapturePaymentViewModel @Inject constructor(
	private val paymentModesUseCase: PaymentModesUseCase,
	private val mapper: KycMapper,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	val paymentAmount by lazy {
		String.format(
			"%.2f",
			savedStateHandle.get<Double>(KycRecordSaleActivity.PAYABLE_AMOUNT)
		)
	}

	private val viewModelState = MutableStateFlow(CapturePaymentViewModelState())
	val uiState = viewModelState.map {
		it.toUiState()
	}.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
	)

	init {
		viewModelState.update { it.copy(isLoading = true, pendingAmount = paymentAmount) }
		getInitialData()
	}

	fun getInitialData() = callInViewModelScope {
		when (val response = paymentModesUseCase()) {
			is APIResultEntity.Success -> {
				val paymentModes = response.data.orEmpty().onEach {
					it.imageUrl = KycExecutor.getPreSignedUrl(it.imageUrl)
				}.toMutableStateList()
				viewModelState.update {
					it.copy(
						isSuccess = true,
						isLoading = false,
						isError = false,
						availablePaymentMode = paymentModes
					)
				}
			}
			is APIResultEntity.Failure -> {
				viewModelState.update {
					it.copy(
						isError = true,
						isLoading = false
					)
				}
			}
		}
	}

	fun updateAmount(amount: String, id: Long) {
		val availablePaymentMode = viewModelState.value.availablePaymentMode
		val sumTotal = availablePaymentMode.sumOf {
			if (it.id == id) 0.0 else it.amount.toDoubleOrZero()
		}
		val finalAmount = sumTotal + amount.toDoubleOrZero()
		if (finalAmount <= paymentAmount.toDoubleOrZero()) {
			val paymentModes = availablePaymentMode.map {
				if (it.id == id) it.amount = amount
				it
			}
			val pendingAmount = String.format(
				"%.2f",
				paymentAmount.toDoubleOrZero() - paymentModes.sumOf {
					it.amount.toDoubleOrZero()
				}
			)
			viewModelState.update {
				it.copy(
					availablePaymentMode = paymentModes.toMutableStateList(),
					selectedPaymentModes = paymentModes.mapNotNull { paymentMode ->
						paymentMode.amount.toDoubleOrNull()?.let {
							RegisterSalePaymentModeRequest(
								id = paymentMode.id,
								amount = it.toString()
							)
						}
					},
					pendingAmount = pendingAmount,
					validAmount = pendingAmount.toDoubleOrZero() == 0.0
				)
			}
		}
	}
}
