package org.impactindiafoundation.iifllemeddocket.Model.NewPrescriptionDataModel

data class PrescriptionItem(
    val batch_no: String,
    val brand_name: String,
    val dose: String,
    val duration: String,
    val duration_unit: String,
    val frequeny: String,
    val given: Boolean,
    val item_name: String,
    val procurementItem_id: Int,
    val qty: Double,
    val qty_name: String,
    val qty_unit_id: Int,
    val route: String
)
