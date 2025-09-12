package org.impactindiafoundation.iifllemeddocket.Model.NewPrescriptionDataModel

data class sendNewPrescriptionData(
    val _id:Long,
    val user_id:Long,
    val procurementItem_id: Int,
    val qty: Long,
    val qty_unit_id: Int,
    val patient_id:  Long,
    val camp_id:Long,
)

