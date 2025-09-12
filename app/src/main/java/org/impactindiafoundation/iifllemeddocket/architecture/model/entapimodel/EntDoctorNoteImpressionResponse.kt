package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntDoctorNoteImpressionResponse(
    val data: EntDoctorNoteImpressionResponseData?,
    val message: String,
    val success: Boolean
)

data class EntDoctorNoteImpressionResponseData(
    val success_count: Int,
    val results: List<EntDoctorNoteImpressionResult>
)

data class EntDoctorNoteImpressionResult(
    val message: String,
    val app_id: String
)
