package org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class PathologyImageRequest(
    val filename: MultipartBody.Part,
    val patientId: RequestBody,
    val campId: RequestBody,
    val userId: RequestBody,
    val appCreatedDate: RequestBody,
    val reportType : RequestBody,
    val app_id: RequestBody
)