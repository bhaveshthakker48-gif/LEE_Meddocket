package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ent_patient_report")
data class EntPatientReport(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val formType:String,
    val formId:Int,
    val patientFname: String,
    val patientLname: String,
    val patientId: Int,
    val patientGen: String,
    val patientAge: Int,
    val camp_id: Int,
    val location: String,
    val AgeUnit: String,
    val RegNo:String,
    val app_id: String? = null
)
