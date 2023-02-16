package com.dehaat.kyc.utils.ui

import com.cleanarch.base.common.ApiExtraInfo
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.coroutine.IDispatchers
import com.dehaat.androidbase.network.api.makeAPICall
import retrofit2.Response

suspend fun <D, C> callAPI(
	dispatchers: IDispatchers,
	apiCall: suspend () -> Response<D>,
	parse: (D?) -> C
): APIResultEntity<C> {
	return makeAPICall(dispatchers.io, {
		apiCall.invoke()
	}, parse){ request, response ->
		ApiExtraInfo().apply {
			put("api-request-trace-id", request?.header("api-request-trace-id"))
			put("IB-Request-Identifier", response?.headers()?.get("IB-Request-Identifier"))
		}
	}
}
