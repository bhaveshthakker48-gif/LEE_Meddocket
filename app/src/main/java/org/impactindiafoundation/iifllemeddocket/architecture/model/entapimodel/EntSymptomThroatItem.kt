package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntSymptomThroatType(
    val data: EntSymptomThroatData,
    val message: String,
    val success: Boolean
)

data class EntSymptomThroatData(
    val results: List<EntSymptomThroatItem>
)

data class EntSymptomThroatItem(
    val symptom: String,
    val id: Int
)

