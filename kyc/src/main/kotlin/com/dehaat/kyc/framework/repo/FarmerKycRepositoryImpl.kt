package com.dehaat.kyc.framework.repo

import com.dehaat.androidbase.coroutine.IDispatchers
import com.dehaat.kyc.framework.entity.mapper.FarmerKycEntityMapper
import com.dehaat.kyc.framework.network.KycAPIService
import com.dehaat.kyc.utils.ui.callAPI
import javax.inject.Inject

class FarmerKycRepositoryImpl @Inject constructor(
	private val dispatchers: IDispatchers,
	private val service: KycAPIService,
	private val mapper: FarmerKycEntityMapper
) : FarmerKycRepository {
	override suspend fun getDocument(
		dataType: String
	) = callAPI(
		dispatchers = dispatchers,
		apiCall = { service.getMasterData(dataType) }
	) { mapper.toMasterDataEntity(it) }

	override suspend fun getAllDigitalDocument(
		farmerId: Long
	) = callAPI(
		dispatchers = dispatchers,
		apiCall = { service.getAllDigitalDocsDetails(farmerId) }
	) { mapper.toDigitalDocumentEntity(it) }
}
