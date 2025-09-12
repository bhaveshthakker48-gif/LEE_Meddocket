package org.impactindiafoundation.iifllemeddocket.architecture.model

data class Diagnosis(
    val id: Int,
    val name: String,
    val parentDiagnosis: Any
)