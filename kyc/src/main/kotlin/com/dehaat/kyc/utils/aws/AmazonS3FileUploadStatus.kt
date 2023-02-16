package com.dehaat.kyc.utils.aws

interface AmazonS3FileUploadStatus {
    fun onUploadSuccess(uploadFilePath: String)
    fun onUploadError(response: String?)
}
