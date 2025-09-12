package org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel

data class PrescriptionGlassStatusDetails(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val PrescriptionGlasses: List<PrescriptionGlasse>
)