package com.dehaat.kyc

import com.dehaat.androidbase.coroutine.IDispatchers
import com.dehaat.kyc.feature.bankkyc.model.RequestBankDetails
import com.dehaat.kyc.feature.bankkyc.model.RequestBankDetailsEntity
import com.dehaat.kyc.feature.otp.SendOtpRequest
import com.dehaat.kyc.feature.otp.SendOtpViaCallRequest
import com.dehaat.kyc.feature.otp.ValidateOtpRequest
import com.dehaat.kyc.framework.model.BankDetailsRequest
import com.dehaat.kyc.framework.model.BankDetailsUploadData
import com.dehaat.kyc.framework.model.IdProofRequest
import com.dehaat.kyc.framework.model.PanCKycBody
import com.dehaat.kyc.framework.model.RegisterSaleRequest
import com.dehaat.kyc.framework.network.KycAPIService
import com.dehaat.kyc.model.KycMapper
import com.dehaat.kyc.utils.ui.callAPI
import javax.inject.Inject

class KycRepositoryImpl @Inject constructor(
	private val service: KycAPIService,
	private val dispatcher: IDispatchers,
	private val mapper: KycMapper
) : KycRepository {
	override suspend fun getDocument(dataType: String) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.getMasterData(dataType)
		}
	) {
		mapper.toMasterDataEntity(it)
	}

	override suspend fun addIdProof(
		farmerId: Long,
		documentId: Long,
		idProofNumber: String
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.addIdProof(
				farmerId,
				IdProofRequest(documentId, idProofNumber)
			)
		}
	) { it }

	override suspend fun getAllDigitalDocument(farmerId: Long) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.getAllDigitalDocsDetails(farmerId)
		}
	) {
		mapper.toAllDigitalDocumentEntity(it)
	}

	override suspend fun sendAadhaarCKyc(
		id: Long,
		dateOfBirth: String,
		gender: Char?,
		name: String,
		aadhaarLastFourDigits: String
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.sendAadhaarCKyc(
				id = id,
				body = mapper.toAadhaarCKycBody(
					dateOfBirth = dateOfBirth,
					gender = gender?.toString(),
					name = name,
					aadhaarLastFourDigits = aadhaarLastFourDigits
				)
			)
		}
	) {
		it
	}

	override suspend fun sendPanCKyc(
		id: Long,
		panNumber: String
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = { service.sendPanCKyc(id = id, body = PanCKycBody(panNumber)) }
	) { it }

	override suspend fun validateOtp(
		otp: String,
		farmerId: Long
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.validateOtp(farmerId = farmerId, validateOtpRequest = ValidateOtpRequest(otp))
		}
	) { it }

	override suspend fun sendKycOtp(farmerId: Long) = callAPI(
		dispatcher,
		{ service.sendKycOtpViaSms(SendOtpRequest(farmerId, "identity_proof")) }
	) { it }

	override suspend fun sendOtpViaCall(
		farmerAuthId: String
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.sendOtpViaCall(SendOtpViaCallRequest(farmerAuthId, farmerAuthId))
		}
	) { it }

	override suspend fun validateKycOtp(otp: String, farmerId: Long) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.validateKycOtp(
				validateOtpRequest = ValidateOtpRequest(
					otp = otp,
					farmerId =
					farmerId,
					documentType = "identity_proof"
				)
			)
		}
	) { it }

	override suspend fun getPaymentModes() = callAPI(
		dispatchers = dispatcher,
		apiCall = { service.getPaymentModes() }
	) { it }

	override suspend fun registerSale(
		farmerId: Long,
		request: RegisterSaleRequest?,
		requestOtp: Boolean
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.registerSale(
				apiVersion = if (requestOtp) "v1" else "v2",
				farmerId = farmerId,
				request = request,
				validateFor = if (requestOtp) "insurance" else null
			)
		}
	) { it }

	override suspend fun getBankBranchDetails(
		ifsc: String
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.getBankBranchDetails(
				ifsc
			)
		}
	) { it }

	override suspend fun addBankDetails(
		farmerId: String,
		id: String,
		requestBankDetailsEntity: RequestBankDetailsEntity
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.addBankDetails(
				farmerId = farmerId,
				id,
				RequestBankDetails(
					requestBankDetailsEntity.frontUrl,
					requestBankDetailsEntity.bankDocumentType
				)
			)
		}
	) { it }

	override suspend fun updateBankDetails(
		farmerId: String,
		bankAccountDetailsId: String,
		requestUpdateBankDetailsEntity: RequestBankDetailsEntity
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = {
			service.updateBankDetails(
				farmerId, bankAccountDetailsId,
				RequestBankDetails(
					requestUpdateBankDetailsEntity.frontUrl,
					requestUpdateBankDetailsEntity.bankDocumentType
				)
			)
		}
	) { it }

	override suspend fun addBankDocuments(
		farmerId: Long,
		bankDetails: BankDetailsUploadData
	) = callAPI(
		dispatchers = dispatcher,
		apiCall = { service.addBankDocuments(farmerId, bankDetails) }
	) { it }

	override suspend fun updateBankDocuments(
		farmerId: Long,
		bankDetails: BankDetailsRequest
	) = callAPI(
		dispatcher,
		{ service.updateBankDocuments(farmerId, bankDetails) }
	) { it }
}
