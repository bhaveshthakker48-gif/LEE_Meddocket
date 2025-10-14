package org.impactindiafoundation.iifllemeddocket.Utils

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ConstantsApp {

    companion object {
        const val BASE_URL="https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/"

        const val LOCAL_URL="http://192.168.0.160:8095/ImpactWebService/rest/LLEwebcall/"

        const val TAG="mytag"

        fun checkInternetConenction(mContext: Context): Boolean {
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            return info != null && info.isConnected
        }

        fun getCurrentDate(): String {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Choose your desired date and time format
            return currentDateTime.format(formatter)
        }

        fun extractPatientAndLoginData(sessionManager: SessionManager): Triple<Int?, Int?, String?> {
            val decodedText = sessionManager.getPatientData()
            val gson = Gson()
            val patientData = gson.fromJson(decodedText, PatientData::class.java)
            val loginData = sessionManager.getLoginData()
            var patientId: String? = null
            var campId = "0"
            var userId = "0"
            patientId = patientData?.patientId?.toString() ?: "0"
            for (data in loginData.orEmpty()) {
                campId = data.camp_id.toString()
                userId = data.Userid.toString()
                break
            }
            return Triple(patientId.toInt(), campId.toInt(), userId)
        }

        fun saveBitmapToFile(bitmap: Bitmap, context: Context): String {
            val filesDir = context.filesDir
            val imageFile = File(filesDir, "image.png")

            val outputStream: FileOutputStream
            try {
                outputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return imageFile.absolutePath
        }

        fun saveBitmapToFile1(bitmap: Bitmap, fileName: String, context: Context): File {
            val picturesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File(picturesDirectory, fileName)

            try {
                FileOutputStream(file).use { outStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                    outStream.flush()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return file
        }

        fun moveImageToLLEFolder(context: Context, sourceUri: Uri, fileName: String): String? {
            val lleFolder = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "LLE")

            if (!lleFolder.exists()) {
                lleFolder.mkdirs()
            }
            val destinationFile = File(lleFolder, fileName)

            try {
                copyFile(context, sourceUri, destinationFile)
                return destinationFile.absolutePath
            } catch (e: IOException) {
                Log.e(ConstantsApp.TAG, "Error moving image to LLE folder: ${e.message}")
                return null
            }
        }

        private fun copyFile(context: Context, sourceUri: Uri, destinationFile: File) {
            context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    val buf = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buf).also { bytesRead = it } > 0) {
                        outputStream.write(buf, 0, bytesRead)
                    }
                }
            }
        }

        fun getFileNameFromPath(filePath: String): String {
            val file = File(filePath)
            return file.name
        }

        fun formatAadharNumber(input: String): String {
            return input.chunked(4).joinToString("-")
        }

        fun getCurrentOnlyDate(): String {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // Month index starts from 0
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            return "$year-$month-$day"
        }

        fun getCurrentTime(): String {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY) // 24-hour format
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)
            return "$hour:$minute:$second"
        }

        fun convertDateFormat(inputDate: String, inputFormat: String, outputFormat: String): String {
            val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())

            return try {
                val date = inputDateFormat.parse(inputDate)
                outputDateFormat.format(date!!)
            } catch (e: Exception) {
                e.printStackTrace()
                "" // Return empty string in case of any error
            }
        }
    }
}