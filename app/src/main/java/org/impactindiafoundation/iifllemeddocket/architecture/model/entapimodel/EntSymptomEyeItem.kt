package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntSymptomEarType(
    val data: EntSymptomEarData,
    val message: String,
    val success: Boolean
)

data class EntSymptomEarData(
    val results: List<EntSymptomEarItem>
)

data class EntSymptomEarItem(
    val symptom: String,
    val id: Int
)

