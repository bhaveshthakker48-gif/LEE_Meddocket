package org.impactindiafoundation.iifllemeddocket.Model.OPD_InvestigationsModel

data class OPD_Investigation(
    val _id: Int,
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
    val userId: String
)
