package org.impactindiafoundation.iifllemeddocket.Model.SurgicalNotesModel

data class getSurgicalNotesResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val cataract_Surgeries: List<CataractSurgery>,
    val success_count: Int
) {
    data class CataractSurgery(
        val _id: Int
    )
}