package com.dehaat.kyc.framework.repo

import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.kyc.framework.entity.model.DigitalDocumentEntity
import com.dehaat.kyc.framework.entity.model.MasterDataEntity

interface FarmerKycRepository {

	suspend fun getDocument(
		dataType: String
	): APIResultEntity<MasterDataEntity?>

	suspend fun getAllDigitalDocument(
		farmerId: Long
	): APIResultEntity<DigitalDocumentEntity?>
}
