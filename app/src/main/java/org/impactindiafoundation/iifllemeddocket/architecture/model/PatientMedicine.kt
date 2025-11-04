package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "PatientMedicine")
data class PatientMedicine(
    @PrimaryKey(autoGenerate = true)
    val _id: Int,
    val isSyn: Int = 0, // Default value is 0
    val camp_id: String?,
    val created_by: String,
    val department: String,
    val doctor_name: String,
    val doctor_specialty: String,
    val patient_name: String,
    val patient_geneder: String,
    val patient_age: Int,
    val patient_temp_id: String,
    val createdDate:String,
    val prescriptionItems: List<PrescriptionItem>,

    ) {
    data class PrescriptionItem(
        val formid : Int,
        val batch_no: String,
        val brand_name: String,
        val doctor_specialty: String,
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
    ) : Serializable
}