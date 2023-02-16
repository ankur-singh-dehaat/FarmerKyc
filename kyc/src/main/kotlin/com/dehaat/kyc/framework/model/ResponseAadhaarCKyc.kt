package com.dehaat.kyc.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseAadhaarCKyc(
	@Json(name = "id")
	val id: Long?,
	@Json(name = "identity_proof_number")
	val identityProofNumber: String?,
	@Json(name = "identity_proof_type")
	val identityProofType: Int,
	@Json(name = "kyc_name")
	val kycName: String?,
	@Json(name = "meta_data")
	val metaData: MetaData?,
) {
	@JsonClass(generateAdapter = true)
	data class MetaData(
		@Json(name = "identity_master")
		val identityMaster: IdentityMaster?,
		@Json(name = "source")
		val source: Source?,
		@Json(name = "identity_master_error")
		val errorMessage: String?
	) {
		@JsonClass(generateAdapter = true)
		data class IdentityMaster(
			@Json(name = "ckyc")
			val ckyc: Ckyc?
		) {
			@JsonClass(generateAdapter = true)
			data class Ckyc(
				@Json(name = "details")
				val details: Details?,
				@Json(name = "images")
				val images: Images?
			) {
				@JsonClass(generateAdapter = true)
				data class Details(
					@Json(name = "aadhaarNumber")
					val aadhaarNumber: String?,
					@Json(name = "ckycNo")
					val ckycNo: String?,
					@Json(name = "correspondenceAddressLine1")
					val correspondenceAddressLine1: String?,
					@Json(name = "correspondenceAddressLine2")
					val correspondenceAddressLine2: String?,
					@Json(name = "correspondenceAddressLine3")
					val correspondenceAddressLine3: String?,
					@Json(name = "correspondenceCity")
					val correspondenceCity: String?,
					@Json(name = "correspondenceCountry")
					val correspondenceCountry: String?,
					@Json(name = "correspondenceDistrict")
					val correspondenceDistrict: String?,
					@Json(name = "correspondencePin")
					val correspondencePin: String?,
					@Json(name = "correspondenceState")
					val correspondenceState: String?,
					@Json(name = "decDate")
					val decDate: String?,
					@Json(name = "dob")
					val dob: String?,
					@Json(name = "fathersFirstName")
					val fathersFirstName: String?,
					@Json(name = "fathersFullName")
					val fathersFullName: String?,
					@Json(name = "fathersSecondName")
					val fathersSecondName: String?,
					@Json(name = "firstName")
					val firstName: String?,
					@Json(name = "fullName")
					val fullName: String?,
					@Json(name = "gender")
					val gender: String?,
					@Json(name = "kycStatus")
					val kycStatus: String?,
					@Json(name = "lastName")
					val lastName: String?,
					@Json(name = "mobileNumber")
					val mobileNumber: String?,
					@Json(name = "mothersFirstName")
					val mothersFirstName: String?,
					@Json(name = "mothersFullName")
					val mothersFullName: String?,
					@Json(name = "mothersSecondName")
					val mothersSecondName: String?,
					@Json(name = "panNo")
					val panNo: String?,
					@Json(name = "panType")
					val panType: String?,
					@Json(name = "permanentAddressLine1")
					val permanentAddressLine1: String?,
					@Json(name = "permanentAddressLine2")
					val permanentAddressLine2: String?,
					@Json(name = "permanentAddressLine3")
					val permanentAddressLine3: Any?,
					@Json(name = "permanentCity")
					val permanentCity: String?,
					@Json(name = "permanentCountry")
					val permanentCountry: String?,
					@Json(name = "permanentDistrict")
					val permanentDistrict: String?,
					@Json(name = "permanentPin")
					val permanentPin: String?,
					@Json(name = "permanentState")
					val permanentState: String?,
					@Json(name = "prefix")
					val prefix: String?
				)

				@JsonClass(generateAdapter = true)
				data class Images(
					@Json(name = "Other_url")
					val otherUrl: String?,
					@Json(name = "Photograph_url")
					val photographUrl: String?,
					@Json(name = "ProofofPossessionofAadhaar_url")
					val proofofPossessionofAadhaarUrl: String?
				)
			}
		}

		@JsonClass(generateAdapter = true)
		data class Source(
			@Json(name = "app_code")
			val appCode: String?,
			@Json(name = "dehaat_centre_id")
			val dehaatCentreId: Int?
		)
	}
}
