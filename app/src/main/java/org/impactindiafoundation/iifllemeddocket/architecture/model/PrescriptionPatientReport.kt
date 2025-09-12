package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "prescription_patient_report")
data class PrescriptionPatientReport(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val formId:Int,
    val patientFname: String,
    val patientLname: String,
    val patientId: Int,
    val patientGen: String,
    val patientAge: Int,
    val camp_id: Int,
    val AgeUnit: String,
    val location: String,
    val isSyn:Int=0
)
