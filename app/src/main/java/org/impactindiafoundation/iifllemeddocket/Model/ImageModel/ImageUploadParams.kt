package org.impactindiafoundation.iifllemeddocket.Model.ImageModel

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class ImageUploadParams(
    val filePart: MultipartBody.Part,
    val imageTypeRequestBody: RequestBody,
    val patientIdRequestBody: RequestBody,
    val campIdRequestBody: RequestBody,
    val userIdRequestBody: RequestBody,
    val idRequestBody: RequestBody
)

