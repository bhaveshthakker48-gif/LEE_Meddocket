package org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel

data class Eyeopddocnote(
    val _id: Int,
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
    val user_id: String
)
