package com.dehaat.kyc

import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.kyc.framework.model.ResponseAadhaarOCR
import com.dehaat.kyc.framework.model.ResponsePanOCR

interface OcrRepository {

	suspend fun getAadhaarOcr(
		frontUrl: String?,
		backUrl: String?,
		farmerId: Long
	): APIResultEntity<ResponseAadhaarOCR?>

	suspend fun getPanOcr(
		frontUrl: String?,
		farmerId: Long
	): APIResultEntity<ResponsePanOCR?>
}
