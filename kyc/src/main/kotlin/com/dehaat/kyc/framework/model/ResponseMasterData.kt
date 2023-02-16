package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseMasterData(
	@Json(name = "identity_proof_type")
	val identityDocumentProofType: List<ResponseDocumentProofType>?,
	@Json(name = "bank_document_type")
	val bankDocumentTypeDocument: List<ResponseDocumentProofType>?,
) {
	@JsonClass(generateAdapter = true)
	data class ResponseDocumentProofType(
		@Json(name = "id") val id: String,
		@Json(name = "name") val name: String,
		@Json(name = "sample_image") val sampleImage: String?,
		@Json(name = "data") val metadata: MetaData? = null,
		@Json(name = "imageUrl") var imageUrl: String?,
		@Json(name = "preview") var preview: String?,
		@Json(name = "identityProofNumber") var identityProofNumber: String?,
	) {

		@JsonClass(generateAdapter = true)
		data class MetaData(
			@Json(name = "translations") val translations: Translations
		)

		@JsonClass(generateAdapter = true)
		data class Translations(
			@Json(name = "name") val translatedTitle: List<TranslatedTitle>
		)

		@JsonClass(generateAdapter = true)
		data class TranslatedTitle(
			@Json(name = "language") val language: String,
			@Json(name = "translation") val title: String
		)
	}
}
