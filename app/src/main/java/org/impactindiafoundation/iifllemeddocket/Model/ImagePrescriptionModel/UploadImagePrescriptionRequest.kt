package org.impactindiafoundation.iifllemeddocket.Model.ImagePrescriptionModel

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UploadImagePrescriptionRequest(
    val filePart: MultipartBody.Part,
    val patientIdRequestBody: RequestBody,
    val campIdRequestBody: RequestBody,
    val userIdRequestBody: RequestBody,
    val idRequestBody: RequestBody
)
