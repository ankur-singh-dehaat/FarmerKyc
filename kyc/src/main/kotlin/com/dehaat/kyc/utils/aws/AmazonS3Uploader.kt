package com.dehaat.kyc.utils.aws

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.dehaat.kyc.navigation.KycExecutor
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AmazonS3Uploader @Inject constructor(
	@ApplicationContext private val applicationContext: Context,
) {
	private var transferUtility: TransferUtility? = null
	private var s3Client: AmazonS3Client? = null

	fun uploadImageToKYCs3Bucket(
		file: File,
		farmerId: Long,
		isPreSignedUrl: Boolean?,
		uploadSuccess: (String) -> Unit,
		uploadFailure: () -> Unit,
	) {
		val key = "KYC_DBA" + "/" + farmerId + "/" + file.name
		if (isPreSignedUrl == true) {
			uploadSuccess(key)
			return
		}
		uploadFile(file, key,
			object : AmazonS3FileUploadStatus {
				override fun onUploadSuccess(uploadFilePath: String) {
					uploadSuccess(uploadFilePath)
				}

				override fun onUploadError(response: String?) {
					Log.d("AWS", "onUploadError: $response")
					uploadFailure()
				}
			}
		)
	}

	private fun uploadFile(
		file: File,
		key: String,
		amazonS3FileUploadStatus: AmazonS3FileUploadStatus?
	) {
		getS3Client() ?: return
		transferUtility = TransferUtility.builder().s3Client(s3Client)
			.context(applicationContext).build()
		val observer = transferUtility?.upload(
			KycExecutor.awsBucket, key, file
		)
		observer?.setTransferListener(object : TransferListener {

			override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
			}

			override fun onStateChanged(id: Int, state: TransferState?) {
				if (state == TransferState.COMPLETED) {
					amazonS3FileUploadStatus?.onUploadSuccess(key)
				}
			}

			override fun onError(id: Int, e: java.lang.Exception?) {
				amazonS3FileUploadStatus?.onUploadError(e.toString())
			}
		})
	}

	private fun getS3Client(): AmazonS3Client? {
		try {
			s3Client = AmazonS3Client(AWSMobileClient.getInstance().credentialsProvider)
		} catch (_: Exception) {
		}
		return s3Client
	}

}

