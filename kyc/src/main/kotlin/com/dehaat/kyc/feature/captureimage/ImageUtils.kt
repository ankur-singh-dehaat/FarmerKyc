package com.dehaat.kyc.feature.captureimage

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import androidx.camera.core.ImageProxy
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

const val STORAGE_DOCUMENT = "com.android.externalstorage.documents"
const val DOWNLOAD_DOCUMENT = "com.android.providers.downloads.documents"
const val MEDIA_DOCUMENT = "com.android.providers.media.documents"
const val IMAGE = "image"
const val VIDEO = "video"
const val AUDIO = "audio"

class URIPathHelper {

	fun getPath(context: Context, uri: Uri): String? {

		// DocumentProvider
		if (DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				val docId = DocumentsContract.getDocumentId(uri)
				val split = docId.split(":".toRegex()).toTypedArray()
				val type = split[0]
				if ("primary".equals(type, ignoreCase = true)) {
					return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
				}

			} else if (isDownloadsDocument(uri)) {
				val fileName = getFilePath(context, uri)
				if (fileName != null) {
					return Environment.getExternalStorageDirectory()
						.toString() + "/Download/" + fileName
				}
				val id = DocumentsContract.getDocumentId(uri)
				val contentUri = ContentUris.withAppendedId(
					Uri.parse("content://downloads/public_downloads"),
					java.lang.Long.valueOf(id)
				)
				return getDataColumn(context, contentUri, null, null)
			} else if (isMediaDocument(uri)) {
				val docId = DocumentsContract.getDocumentId(uri)
				val split = docId.split(":".toRegex()).toTypedArray()
				val type = split[0]
				var contentUri: Uri? = null
				when (type) {
					IMAGE -> {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
					}
					VIDEO -> {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
					}
					AUDIO -> {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
					}
				}
				val selection = "_id=?"
				val selectionArgs = arrayOf(split[1])
				return getDataColumn(context, contentUri, selection, selectionArgs)
			}
		} else if ("content".equals(uri.scheme, ignoreCase = true)) {
			return getDataColumn(context, uri, null, null)
		} else if ("file".equals(uri.scheme, ignoreCase = true)) {
			return uri.path
		}
		return null
	}

	private fun getDataColumn(
		context: Context,
		uri: Uri?,
		selection: String?,
		selectionArgs: Array<String>?
	): String? {
		var cursor: Cursor? = null
		val column = "_data"
		val projection = arrayOf(column)
		try {
			cursor =
				uri?.let {
					context.contentResolver.query(
						it,
						projection,
						selection,
						selectionArgs,
						null
					)
				}
			if (cursor != null && cursor.moveToFirst()) {
				val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
				return cursor.getString(columnIndex)
			}
		} finally {
			cursor?.close()
		}
		return null
	}

	private fun getFilePath(context: Context, uri: Uri?): String? {
		var cursor: Cursor? = null
		val projection = arrayOf(
			MediaStore.MediaColumns.DISPLAY_NAME
		)
		try {
			cursor = context.contentResolver.query(
				uri!!, projection, null, null,
				null
			)
			if (cursor != null && cursor.moveToFirst()) {
				val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
				return cursor.getString(index)
			}
		} finally {
			cursor?.close()
		}
		return null
	}

	private fun isExternalStorageDocument(uri: Uri): Boolean {
		return STORAGE_DOCUMENT == uri.authority
	}

	private fun isDownloadsDocument(uri: Uri): Boolean {
		return DOWNLOAD_DOCUMENT == uri.authority
	}

	private fun isMediaDocument(uri: Uri): Boolean {
		return MEDIA_DOCUMENT == uri.authority
	}

}

fun convertImageProxyToFile(context: Context, image: ImageProxy): File? {
	val planeProxy = image.planes[0]
	val buffer = planeProxy.buffer
	val bytes = ByteArray(buffer.remaining())
	buffer.get(bytes)
	return saveImage(context, bytes)
}

@Throws(IOException::class)
private fun saveImage(context: Context?, byteArray: ByteArray): File? {
	val file = File.createTempFile("image", ".jpg", context?.cacheDir)
	val fileOutputStream = try {
		FileOutputStream(file)
	} catch (e: FileNotFoundException) {
		e.printStackTrace()
		return null
	}
	val bufferedOutputStream = BufferedOutputStream(
		fileOutputStream
	)
	try {
		bufferedOutputStream.write(byteArray)
	} catch (e: IOException) {
		e.printStackTrace()
		return null
	} finally {
		try {
			bufferedOutputStream.close()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}
	return file
}

object ImageUtils {
	private const val MAX_IMAGE_HEIGHT = 1000F
	private const val MAX_IMAGE_WIDTH = 1000F

	fun getCompressedBase64String(
		imagePath: String?
	): String? {
		if (imagePath == null) return null
		val decodedBitmap = BitmapFactory.decodeFile(imagePath)
		val bitmap = if (decodedBitmap == null) {
			try {
				getScaledBitmap(
					imagePath
				)
			} catch (e: Exception) {
				e.printStackTrace()
				return null
			}
		} else {
			getResizedBitmap(
				decodedBitmap
			)
		}
		val baos = ByteArrayOutputStream()
		bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
		val byteArray = baos.toByteArray()
		return Base64.encodeToString(byteArray, Base64.DEFAULT)
	}

	private fun getScaledBitmap(imagePath: String): Bitmap {
		val options = BitmapFactory.Options()
		options.inJustDecodeBounds = true
		val actualHeight = options.outHeight
		val actualWidth = options.outWidth
		if (MAX_IMAGE_HEIGHT != actualHeight.toFloat() && MAX_IMAGE_WIDTH != actualWidth.toFloat()) {
			val scaleFactor =
				(actualWidth / MAX_IMAGE_WIDTH).coerceAtMost(actualHeight / MAX_IMAGE_HEIGHT)
			options.inJustDecodeBounds = false
			options.inSampleSize = scaleFactor.toInt()
		}
		return BitmapFactory.decodeFile(imagePath, options)
	}

	private fun getResizedBitmap(image: Bitmap): Bitmap {
		var width = image.width
		var height = image.height
		if (MAX_IMAGE_HEIGHT < height || MAX_IMAGE_WIDTH < width) {
			val bitmapRatio = (MAX_IMAGE_WIDTH / width).coerceAtMost(MAX_IMAGE_HEIGHT / height)
			width = (width * bitmapRatio).toInt()
			height = (height * bitmapRatio).toInt()
			return Bitmap.createScaledBitmap(image, width, height, true)
		}
		return image
	}

	fun getCompressedFile(
		context: Context?,
		imagePath: String?,
		showMaxHeight: Boolean = false,
		maxHeight: Float? = null,
		maxWidth: Float? = null
	): File? {
		if (imagePath == null) return null
		val decodedBitmap = BitmapFactory.decodeFile(imagePath)
		val bitmap = if (decodedBitmap == null) {
			try {
				getScaledBitmap(
					imagePath,
					maxHeight ?: MAX_IMAGE_HEIGHT,
					maxWidth ?: MAX_IMAGE_WIDTH
				)
			} catch (e: Exception) {
				e.printStackTrace()
				return null
			}
		} else {
			if (showMaxHeight) getMaxResizedBitmap(decodedBitmap)
			else getResizedBitmap(
				decodedBitmap,
				maxHeight ?: MAX_IMAGE_HEIGHT,
				maxWidth ?: MAX_IMAGE_WIDTH
			)
		}
		val baos = ByteArrayOutputStream()
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
		val byteArray = baos.toByteArray()
		var fileOfImage: File? = null
		try {
			fileOfImage = saveImage(context, byteArray)
		} catch (e: IOException) {
			e.printStackTrace()
		}
		return fileOfImage
	}

	private fun getScaledBitmap(imagePath: String, maxHeight: Float, maxWidth: Float): Bitmap {
		val options = BitmapFactory.Options()
		options.inJustDecodeBounds = true
		val bmp = BitmapFactory.decodeFile(imagePath, options)
		val actualHeight = options.outHeight
		val actualWidth = options.outWidth
		if (maxHeight != actualHeight.toFloat() && maxWidth != actualWidth.toFloat()) {
			val scaleFactor = (actualWidth / maxWidth).coerceAtMost(actualHeight / maxHeight)
			options.inJustDecodeBounds = false
			options.inSampleSize = scaleFactor.toInt()
		}
		return BitmapFactory.decodeFile(imagePath, options)
	}

	private fun getMaxResizedBitmap(image: Bitmap): Bitmap {
		var width = image.width.toFloat()
		var height = image.height.toFloat()
		val maxWidth = MAX_IMAGE_WIDTH
		val maxHeight = MAX_IMAGE_HEIGHT
		val maxBitmapRatio = maxWidth / maxHeight
		val bitmapRatio = width / height
		if (bitmapRatio >= maxBitmapRatio) {
			if (width > maxWidth) {
				width = maxWidth
				height = width / bitmapRatio
			}
		} else {
			if (height > maxHeight) {
				height = maxHeight
				width = height * bitmapRatio
			}
		}
		return Bitmap.createScaledBitmap(image, width.toInt(), height.toInt(), true)
	}

	private fun getResizedBitmap(image: Bitmap, maxHeight: Float, maxWidth: Float): Bitmap {
		var width = image.width
		var height = image.height
		if (maxHeight < height || maxWidth < width) {
			val bitmapRatio = (maxWidth / width).coerceAtMost(maxHeight / height)
			width = (width * bitmapRatio).toInt()
			height = (height * bitmapRatio).toInt()
			return Bitmap.createScaledBitmap(image, width, height, true)
		}
		return image
	}
}
