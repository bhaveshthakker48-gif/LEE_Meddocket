package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OPD_Investigations")
data class OPD_Investigations(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val camp_id: Int,
    val createdDate: String,
    val haemoglobin: String,
    val haemoglobin_interpretation: String,
    val has_refused: Boolean,
    val o2_saturation: String,
    val o2s_interpretation: String,
    val patient_id: Int,
    val random_blood_sugar: String,
    val rbs_interpretation: String,
    val userId: String,
    val isSyn: Int =0
)
