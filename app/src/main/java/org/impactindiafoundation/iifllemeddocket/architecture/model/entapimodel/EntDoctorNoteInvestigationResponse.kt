package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntDoctorNoteInvestigationResponse(
    val data: EntDoctorNoteInvestigationResponseData?,
    val message: String,
    val success: Boolean
)

data class EntDoctorNoteInvestigationResponseData(
    val success_count: Int,
    val results: List<EntDoctorNoteInvestigationResult>
)

data class EntDoctorNoteInvestigationResult(
    val message: String,
    val app_id: String
)
