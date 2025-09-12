package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FinalPrescriptionDrug")

data class FinalPrescriptionDrug(
    @PrimaryKey(autoGenerate = true)
    val _id: Int,
    val isSyn: Int = 0, // Default value is 0
    val camp_id: String?,
    val created_by: String,
    val department: String,
    val doctor_name: String,
    val doctor_specialty: String,
    val patient_name: String,
    val patient_temp_id: String,
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
        val procurementItem_id: Int,
        val qty: Double,
        val qty_name: String,
        val qty_unit_id: Int,
        val route: String
    )
}
