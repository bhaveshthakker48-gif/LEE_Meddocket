package org.impactindiafoundation.iifllemeddocket.Model.VitalsModel

data class getVitalsResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val success_count: Int,
    val vital: List<Vital>
) {
    data class Vital(
        val _id: String
    )
}