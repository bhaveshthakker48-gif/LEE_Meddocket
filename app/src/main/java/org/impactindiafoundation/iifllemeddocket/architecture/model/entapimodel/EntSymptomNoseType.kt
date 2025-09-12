package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntSymptomNoseType(
    val data: EntSymptomNoseData,
    val message: String,
    val success: Boolean
)

data class EntSymptomNoseData(
    val results: List<EntSymptomNoseItem>
)

data class EntSymptomNoseItem(
    val symptom: String,
    val id: Int
)
