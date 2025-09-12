package org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel

data class addPrecriptionGlassesResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val prescriptionGlasses: List<PrescriptionGlasse>,
    val success_count: Int
) {
    data class PrescriptionGlasse(
        val _id: String
    )
}