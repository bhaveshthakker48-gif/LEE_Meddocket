package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "patient_report")
data class PatientReport(
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
    var isSynced:Int=0

)
