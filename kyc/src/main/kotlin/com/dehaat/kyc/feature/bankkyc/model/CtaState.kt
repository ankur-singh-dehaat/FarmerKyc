package com.dehaat.kyc.feature.bankkyc.model

sealed class CtaState {
	object Enabled : CtaState()
	object Disabled : CtaState()
}
