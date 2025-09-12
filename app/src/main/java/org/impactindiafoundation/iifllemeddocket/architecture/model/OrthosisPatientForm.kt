package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orthosis_patient_form")
data class OrthosisPatientForm (
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    val patientName: String,
    val patientId: Int,
    val patientGender: String,
    val patientAge: Int,
    val campId: Int,
    val location: String,
    val ageUnit: String,
    val regNo:String,
    val examinationDate:String,//form date
    val orthosisList:List<OrthosisPatientData>,
    val orthosisDate: String,
    val tempPatientId: String,
    val patientContactNo: String,
    val campName: String,
    val patientAgeYears: String,
    val patientHeightCm: String,
    val patientWeightKg: String,
    var diagnosis:String,
    var diagnosisId:Int,
    var equipmentSupport:String,
    var equipmentCategory:String,
    var equipmentId:Int,
    var equipmentStatus:String,
    var equipmentStatusNotes:String,
    var isSynced:Int=0,
    var isEdited:Boolean = false,
    var isLocal : Boolean = true
)