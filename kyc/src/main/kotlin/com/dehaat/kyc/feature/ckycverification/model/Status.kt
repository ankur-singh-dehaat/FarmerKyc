package com.dehaat.kyc.feature.ckycverification.model

sealed class Status {
	object Success : Status()
	object Failed : Status()
}
