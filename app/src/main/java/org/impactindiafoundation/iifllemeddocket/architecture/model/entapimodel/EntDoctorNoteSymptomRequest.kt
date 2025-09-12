package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntDoctorNoteSymptomRequest(
    val data: List<EntDoctorNoteSymptomItem>
)

data class EntDoctorNoteSymptomItem(
    val uniqueId : Int,
    val formId : Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val organ: String,
    val part: String,
    val symptom: String,
    val symptomId: Int,
    val appCreatedDate: String,
    val app_id: String = "1"
)
