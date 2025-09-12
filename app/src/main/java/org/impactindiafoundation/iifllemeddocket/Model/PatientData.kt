package org.impactindiafoundation.iifllemeddocket.Model


data class PatientData(
    val patientFname: String,
    val patientLname: String,
    val patientId: Int,
    val patientGen: String,
    val patientAge: Int,
    val camp_id: Int,
    val location: String,
    val AgeUnit: String,
    val RegNo:String
)
