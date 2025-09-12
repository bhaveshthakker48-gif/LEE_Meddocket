package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Eye_OPD_Doctors_Note")
data class Eye_OPD_Doctors_Note(
   @PrimaryKey(autoGenerate = true) val _id: Int,
    val camp_id: Int,
    val createdDate: String,
    val opd_eye_diagnosis: String,
    val opd_eye_diagnosis_description: String,
    val opd_eye_examination: String,
    val opd_eye_examination_description: String,
    val opd_eye_notes: String,
    val opd_eye_recommended: String,
    val opd_eye_symptoms: String,
    val opd_eye_symptoms_description: String,
    val patient_id: Int,
    val user_id: String,
   val isSyn: Int =0
)
