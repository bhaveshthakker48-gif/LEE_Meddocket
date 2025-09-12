package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "camp_patient_details")
data class CampPatientDataItem(
    val camp_id: String, // d
    val camp_name: String,// d
    @PrimaryKey(autoGenerate = false)
    val id: Int,//orthosis form id // d
    var orthosis_date: String, //form date // d
    var patientOrthosisTypes: List<OrthosisPatientData>, // PatientOrthosisType// d
    val patient_age_years: String, // d
    val patient_contact_no: String, // d
    val patient_gender: String,// d
    var patient_height_cm: String, // d
    val patient_id: String,//server generated// d
    val patient_name: String, // d
    var patient_weight_kg: String, // d
    var diagnosis:String, // d
    var diagnosisId:Int,// d
    var equipment_support:String,// d
    var equipment_category:String,// d
    var equipmentId:Int,
    var equipment_status:String,// d
    var equipment_status_notes:String,// d
    val temp_patient_id: String,// temp id - check for second time fitting form // d
    var isLocal : Boolean = false,
    var isSynced:Int=1,
    var isEdited:Boolean,
    var fromDevice:Boolean = false
)