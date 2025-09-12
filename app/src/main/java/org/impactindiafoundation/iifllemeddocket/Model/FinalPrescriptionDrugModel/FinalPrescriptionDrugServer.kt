package org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel

import androidx.room.PrimaryKey

data class FinalPrescriptionDrugServer(
    @PrimaryKey(autoGenerate = true)
    val _id: Long,
    val camp_id: Long?,
    val created_by: Long,
    val department: String,
    val doctor_name: String,
    val doctor_specialty: String,
    val patient_name: String,
    val patient_temp_id: Long,
    val prescriptionItems: List<PrescriptionItem>,

    ) {
    data class PrescriptionItem(
        val batch_no: String,
        val brand_name: String,
        val dose: String,
        val duration: String,
        val duration_unit: String,
        val frequency: String,
        val given: Boolean,
        val item_name: String,
        val procurementItem_id: Long,
        val qty: Double,
        val qty_name: String,
        val qty_unit_id: Long,
        val route: String
    )
}