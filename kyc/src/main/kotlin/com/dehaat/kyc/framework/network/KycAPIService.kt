package com.dehaat.kyc.framework.network

import com.dehaat.kyc.feature.bankkyc.model.BankDetails
import com.dehaat.kyc.feature.bankkyc.model.RequestBankDetails
import com.dehaat.kyc.feature.otp.SendOtpRequest
import com.dehaat.kyc.feature.otp.SendOtpViaCallRequest
import com.dehaat.kyc.feature.otp.ValidateOtpRequest
import com.dehaat.kyc.feature.otp.ValidateOtpResponse
import com.dehaat.kyc.framework.model.AadhaarCKycBody
import com.dehaat.kyc.framework.model.AadharOCRBody
import com.dehaat.kyc.framework.model.AvailablePaymentMode
import com.dehaat.kyc.framework.model.BankDetailsRequest
import com.dehaat.kyc.framework.model.BankDetailsUploadData
import com.dehaat.kyc.framework.model.FarmerIdentityProofDetailItem
import com.dehaat.kyc.framework.model.IdProofRequest
import com.dehaat.kyc.framework.model.PANOCRBody
import com.dehaat.kyc.framework.model.PanCKycBody
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.framework.model.ResponseAadhaarCKyc
import com.dehaat.kyc.framework.model.ResponseAadhaarOCR
import com.dehaat.kyc.framework.model.ResponseAllDigitalDocument
import com.dehaat.kyc.framework.model.ResponseBankBranchDetails
import com.dehaat.kyc.framework.model.ResponseMasterData
import com.dehaat.kyc.framework.model.ResponsePanKycVerification
import com.dehaat.kyc.framework.model.ResponsePanOCR
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface KycAPIService {

	@GET("/digilocker/v2/farmer/{farmerId}/details")
	suspend fun getAllDigitalDocsDetails(
		@Path("farmerId") farmerId: Long
	): Response<ResponseAllDigitalDocument?>

	@GET("/common/v1/master-data")
	suspend fun getMasterData(
		@Query("data_for") data: String
	): Response<ResponseMasterData?>

	@POST("/digilocker/v1/farmer/{farmer_id}/identity-proof")
	suspend fun addIdProof(
		@Path("farmer_id") id: Long,
		@Body body: IdProofRequest
	): Response<FarmerIdentityProofDetailItem>

	@POST("/digilocker/v1/farmer/{farmer_id}/ocr/aadhar")
	suspend fun getAadhaarOcr(
		@Path("farmer_id") farmerId: Long,
		@Body body: AadharOCRBody
	): Response<ResponseAadhaarOCR?>

	@POST("/digilocker/v1/farmer/{farmer_id}/ocr/pan-card")
	suspend fun getPanOcr(
		@Path("farmer_id") farmerId: Long,
		@Body body: PANOCRBody
	): Response<ResponsePanOCR?>

	@POST("/digilocker/v1/identity-proof/{id}/ckyc")
	suspend fun sendAadhaarCKyc(
		@Path("id") id: Long,
		@Body body: AadhaarCKycBody
	): Response<ResponseAadhaarCKyc?>

	@POST("/digilocker/v1/identity-proof/{id}/pan-ekyc")
	suspend fun sendPanCKyc(
		@Path("id") id: Long,
		@Body body: PanCKycBody
	): Response<ResponsePanKycVerification?>

	@POST("/digilocker/v1/kyc-otp/action/generate")
	suspend fun sendKycOtpViaSms(@Body sendOtpRequest: SendOtpRequest): Response<Void>

	@POST("/pos/v1/trigger-otp-ivr")
	suspend fun sendOtpViaCall(@Body request: SendOtpViaCallRequest): Response<Unit>

	@POST("/digilocker/v1/kyc-otp/action/validate")
	suspend fun validateKycOtp(@Body validateOtpRequest: ValidateOtpRequest): Response<ValidateOtpResponse>

	@POST("/pos/v1/farmer/{farmer_id}/validate-otp")
	suspend fun validateOtp(
		@Path("farmer_id") farmerId: Long,
		@Body validateOtpRequest: ValidateOtpRequest
	): Response<ValidateOtpResponse>

	@GET("/pos/v1/payment-mode")
	suspend fun getPaymentModes(): Response<List<AvailablePaymentMode>>

	@POST("/pos/{version}/farmer/{farmer_id}/order")
	suspend fun registerSale(
		@Path("version") apiVersion: String,
		@Path("farmer_id") farmerId: Long,
		@Body request: RegisterSaleRequest?,
		@Query("send_otp") validateFor: String?
	): Response<Unit>

	@GET("https://ifsc.razorpay.com/{ifsc}")
	@Headers("Accept-Encoding: identity")
	suspend fun getBankBranchDetails(@Path("ifsc") ifsc: String): Response<ResponseBankBranchDetails>

	@POST("/digilocker/v1/farmer/{farmerId}/user-bank-account/{id}/details")
	suspend fun addBankDetails(
		@Path("farmerId") farmerId: String,
		@Path("id") id: String,
		@Body requestBankDetails: RequestBankDetails
	): Response<BankDocumentId>

	@PATCH("/digilocker/v1/farmer/{farmerId}/user-bank-account-details/{bank_kyc_id}")
	suspend fun updateBankDetails(
		@Path("farmerId") farmerId: String,
		@Path("bank_kyc_id") bankAccountDetailsId: String,
		@Body requestBankDetails: RequestBankDetails
	): Response<BankDocumentId>

	@POST("/digilocker/v1/farmer/{id}/user-bank-account")
	suspend fun addBankDocuments(
		@Path("id") id: Long,
		@Body body: BankDetailsUploadData,
	): Response<BankDocumentId>

	@POST("/digilocker/v1/farmer/{id}/user-bank-account")
	suspend fun updateBankDocuments(
		@Path("id") id: Long,
		@Body body: BankDetailsRequest,
	): Response<BankDetails>
}

@JsonClass(generateAdapter = true)
data class BankDocumentId(
	@Json(name = "id")
	val id: Long
)
