package org.impactindiafoundation.iifllemeddocket.architecture.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class OrthosisImageRequest(
    val filePart: MultipartBody.Part,
    val tempPatientIdRequestBody: RequestBody,
    val campIdRequestBody: RequestBody,
    val orthosisIdRequestBody: RequestBody,
    val amputationsSideRequestBody: RequestBody,
    val patientIdRequestBody: RequestBody,
    val idRequestBody: RequestBody
)
