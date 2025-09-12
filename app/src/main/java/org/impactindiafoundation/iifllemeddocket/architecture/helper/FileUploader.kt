package org.impactindiafoundation.iifllemeddocket.architecture.helper

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * Created by JOSUS PRAISER on 29-10-2024.
 */

object FileUploader {

    suspend fun uploadFiles(
        context: Context,
        url: String,
        uris: Map<String, Uri>,
        files: Map<String, File>,
        parameters: Map<String, String>,
        type: String,
        callback: UploadCallback
    ) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(60, TimeUnit.SECONDS)    // Reading timeout
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor(context))
            .build()

        //patient_orthosis_images/uploadPatientOrthosisType
        var urlFormImage =
            Constants.NEW_BASE_URL + "patient_orthosis_images/uploadPatientOrthosis"
        var urlOrthosisImage =
            Constants.NEW_BASE_URL + "patient_orthosis_images/uploadPatientOrthosisType"

        var urlOrthosisVideo =
            Constants.NEW_BASE_URL + "patient_orthosis_videos/uploadPatientOrthosis"

        var responseBody: String? = null

        try {
            val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            parameters.forEach { (key, value) ->
                multipartBuilder.addFormDataPart(key, value)
            }
            uris.forEach { (key, uri) ->
                val file = File(uriToFilePath(context, uri))
                val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                multipartBuilder.addFormDataPart(key, file.name, requestBody)
            }
            files.forEach { (key, file) ->
                val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                multipartBuilder.addFormDataPart(key, file.name, requestBody)
            }
            val requestBody = multipartBuilder.build()
            val request: Request
            if (type == "FormImage") {
                request = Request.Builder()
                .url(urlFormImage)
                    .post(requestBody)
                    .build()
            }
            else if (type == "FormVideo"){
                request = Request.Builder()
                    .url(urlOrthosisVideo)
                    .post(requestBody)
                    .build()
            }
            else if (type == "EquipmentImage"){
                request = Request.Builder()
                    .url(urlFormImage)
                    .post(requestBody)
                    .build()
            }
            else{
                request = Request.Builder()
                    .url(urlOrthosisImage)
                    .post(requestBody)
                    .build()
            }
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
            responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    callback.onAllFilesUploaded(responseBody)
                }
            } else {
                withContext(Dispatchers.Main) {
                    callback.onFileUploadError("Upload failed: ${response.code}")
                }
            }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                callback.onFileUploadError(e.message)
            }
        }
    }

    fun uriToFilePath(context: Context, uri: Uri): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available() ?: 0
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream?.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)

        } catch (e: java.lang.Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path

    }


    interface UploadCallback {
        fun onFileUploadError(errorMessage: String?)
        fun onAllFilesUploaded(string: String)
    }
}