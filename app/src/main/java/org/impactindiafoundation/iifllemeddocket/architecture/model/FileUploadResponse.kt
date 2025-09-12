package org.impactindiafoundation.iifllemeddocket.architecture.model

data class FileUploadResponse(
    val error: Boolean,
    val statusCode: Int,
    val message: String,
    val file_url: String
)