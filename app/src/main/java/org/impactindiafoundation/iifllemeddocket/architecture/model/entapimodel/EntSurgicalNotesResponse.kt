package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntSurgicalNotesResponse(
    val data: EntSurgicalNotesResponseData?,
    val message: String,
    val success: Boolean
)

data class EntSurgicalNotesResponseData(
    val success_count: Int,
    val results: List<EntSurgicalNotesResult>
)

data class EntSurgicalNotesResult(
    val message: String,
    val app_id: String
)
