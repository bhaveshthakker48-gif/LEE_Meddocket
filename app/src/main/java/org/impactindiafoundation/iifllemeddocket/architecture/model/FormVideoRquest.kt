package org.impactindiafoundation.iifllemeddocket.architecture.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class FormVideoRquest(
    val filePart: MultipartBody.Part,
    val tempPatientIdRequestBody: RequestBody,
    val campIdRequestBody: RequestBody,
    val patientIdRequestBody: RequestBody,
    val idRequestBody: RequestBody
)
