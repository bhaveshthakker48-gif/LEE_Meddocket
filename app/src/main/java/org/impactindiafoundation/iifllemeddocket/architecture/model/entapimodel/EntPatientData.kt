package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel


data class EntPatientData(
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
