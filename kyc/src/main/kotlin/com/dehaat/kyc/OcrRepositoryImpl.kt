package com.dehaat.kyc

import com.dehaat.androidbase.coroutine.IDispatchers
import com.dehaat.kyc.framework.model.AadharOCRBody
import com.dehaat.kyc.framework.model.PANOCRBody
import com.dehaat.kyc.framework.network.KycAPIService
import com.dehaat.kyc.utils.ui.callAPI
import javax.inject.Inject

class OcrRepositoryImpl @Inject constructor(
	private val service: KycAPIService,
	private val dispatcher: IDispatchers
) : OcrRepository {
	override suspend fun getAadhaarOcr(
		frontUrl: String?,
		backUrl: String?,
		farmerId: Long
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.getAadhaarOcr(
				farmerId = farmerId,
				AadharOCRBody(
					aadharFront = frontUrl,
					aadharBack = backUrl
				)
			)
		}
	) {
		it
	}

	override suspend fun getPanOcr(
		frontUrl: String?,
		farmerId: Long
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.getPanOcr(
				farmerId = farmerId,
				PANOCRBody(
					panCardImage = frontUrl
				)
			)
		}
	) {
		it
	}
}
