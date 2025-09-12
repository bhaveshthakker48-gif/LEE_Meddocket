package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntDoctorNoteImpressionRequest(
    val data: List<EntDoctorNoteImpressionItem>
)

data class EntDoctorNoteImpressionItem(
    val uniqueId : Int,
    val formId : Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val part: String,
    val impression: String,
    val impressionId: Int,
    val appCreatedDate: String,
    val app_id: String = "1"
)
