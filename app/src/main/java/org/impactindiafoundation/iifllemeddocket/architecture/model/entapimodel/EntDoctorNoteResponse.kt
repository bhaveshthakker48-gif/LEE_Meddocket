package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntDoctorNoteResponse(
    val data: EntDoctorNoteResponseData?,
    val message: String,
    val success: Boolean
)

data class EntDoctorNoteResponseData(
    val success_count: Int,
    val results: List<EntDoctorNoteResult>
)

data class EntDoctorNoteResult(
    val message: String,
    val app_id: String
)
