package com.dehaat.kyc.framework.model

data class MasterDataEntity(
	val identityDocumentProofType: List<DocumentProofTypeEntity>?,
	val bankDocumentTypeDocument: List<DocumentProofTypeEntity>?,
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
