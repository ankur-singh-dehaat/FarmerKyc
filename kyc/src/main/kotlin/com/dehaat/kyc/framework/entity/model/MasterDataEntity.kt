package com.dehaat.kyc.framework.entity.model

class MasterDataEntity(
	val documentEntity: List<DocumentProofTypeEntity>
) {
	data class DocumentProofTypeEntity(
		val id: String,
		val name: String,
		val sampleImage: String?,
		var imageUrl: String?,
		var preview: String?,
		var identityProofNumber: String?,
	)
}
