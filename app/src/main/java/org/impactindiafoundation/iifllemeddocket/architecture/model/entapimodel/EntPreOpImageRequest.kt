package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class EntPreOpImageRequest(
    val filename: MultipartBody.Part,
    val patientId: RequestBody,
    val campId: RequestBody,
    val userId: RequestBody,
    val appCreatedDate: RequestBody,
    val app_id: RequestBody
)