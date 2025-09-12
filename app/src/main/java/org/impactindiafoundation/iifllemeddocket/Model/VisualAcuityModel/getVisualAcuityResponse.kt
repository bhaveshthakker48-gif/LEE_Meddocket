package org.impactindiafoundation.iifllemeddocket.Model.VisualAcuityModel

data class getVisualAcuityResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val success_count: Int,
    val visualActivities: List<VisualActivity>
) {
    data class VisualActivity(
        val _id: String
    )
}