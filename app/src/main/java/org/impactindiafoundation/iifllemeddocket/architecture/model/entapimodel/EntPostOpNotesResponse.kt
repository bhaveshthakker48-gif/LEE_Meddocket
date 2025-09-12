package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntPostOpNotesResponse(
    val data: EntPostOpNotesResponseData?,
    val message: String,
    val success: Boolean
)

data class EntPostOpNotesResponseData(
    val success_count: Int,
    val results: List<EntPostOpNotesResult>
)

data class EntPostOpNotesResult(
    val message: String,
    val app_id: String
)
