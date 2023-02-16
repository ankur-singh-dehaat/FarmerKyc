package com.dehaat.kyc

import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.kyc.feature.bankkyc.model.BankDetails
import com.dehaat.kyc.feature.bankkyc.model.RequestBankDetailsEntity
import com.dehaat.kyc.feature.otp.ValidateOtpResponse
import com.dehaat.kyc.framework.model.AllDigitalDocumentEntity
import com.dehaat.kyc.framework.model.AvailablePaymentMode
import com.dehaat.kyc.framework.model.BankDetailsRequest
import com.dehaat.kyc.framework.model.BankDetailsUploadData
import com.dehaat.kyc.framework.model.FarmerIdentityProofDetailItem
import com.dehaat.kyc.framework.model.MasterDataEntity
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.framework.model.ResponseAadhaarCKyc
import com.dehaat.kyc.framework.model.ResponseBankBranchDetails
import com.dehaat.kyc.framework.model.ResponsePanKycVerification
import com.dehaat.kyc.framework.network.BankDocumentId

interface KycRepository {

	suspend fun getDocument(dataType: String): APIResultEntity<MasterDataEntity?>

	suspend fun getAllDigitalDocument(farmerId: Long): APIResultEntity<AllDigitalDocumentEntity?>

	suspend fun addIdProof(
		farmerId: Long,
		documentId: Long,
		idProofNumber: String
	): APIResultEntity<FarmerIdentityProofDetailItem?>

	suspend fun sendAadhaarCKyc(
		id: Long,
		dateOfBirth: String,
		gender: Char?,
		name: String,
		aadhaarLastFourDigits: String
	): APIResultEntity<ResponseAadhaarCKyc?>

	suspend fun sendPanCKyc(
		id: Long,
		panNumber: String
	): APIResultEntity<ResponsePanKycVerification?>

	suspend fun validateOtp(
		otp: String,
		farmerId: Long
	): APIResultEntity<ValidateOtpResponse?>

	suspend fun sendKycOtp(
		farmerId: Long
	): APIResultEntity<Void?>

	suspend fun sendOtpViaCall(
		farmerAuthId: String
	): APIResultEntity<Unit?>

	suspend fun validateKycOtp(
		otp: String, farmerId: Long
	): APIResultEntity<ValidateOtpResponse?>

	suspend fun getPaymentModes(): APIResultEntity<List<AvailablePaymentMode>?>

	suspend fun registerSale(
		farmerId: Long,
		request: RegisterSaleRequest?,
		requestOtp: Boolean
	): APIResultEntity<Unit?>

	suspend fun getBankBranchDetails(ifsc: String): APIResultEntity<ResponseBankBranchDetails?>

	suspend fun addBankDetails(
		farmerId: String,
		id: String,
		requestBankDetailsEntity: RequestBankDetailsEntity
	): APIResultEntity<BankDocumentId?>

	suspend fun updateBankDetails(
		farmerId: String,
		bankAccountDetailsId: String,
		requestUpdateBankDetailsEntity: RequestBankDetailsEntity
	): APIResultEntity<BankDocumentId?>

	suspend fun addBankDocuments(
		farmerId: Long,
		bankDetails: BankDetailsUploadData
	): APIResultEntity<BankDocumentId?>

	suspend fun updateBankDocuments(
		farmerId: Long,
		bankDetails: BankDetailsRequest
	): APIResultEntity<BankDetails?>
}
