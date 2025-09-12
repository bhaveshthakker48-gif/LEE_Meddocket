package org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel

data class PathologyResponse(
    val data: PathologyResponseData?,
    val message: String,
    val success: Boolean
)

data class PathologyResponseData(
    val success_count: Int,
    val results: List<PathologyResult>
)

data class PathologyResult(
    val message: String,
    val app_id: String
)
