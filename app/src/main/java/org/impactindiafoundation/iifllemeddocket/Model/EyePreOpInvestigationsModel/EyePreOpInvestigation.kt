package org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel

data class EyePreOpInvestigation(
    val _id: Int,
    val camp_id: Int,
    val createdDate: String,
    val opd_eye_av_left: String,
    val opd_eye_av_left_unit: String,
    val opd_eye_av_right: String,
    val opd_eye_av_right_unit: String,

    val opd_eye_blood_pressure_diastolic: String,
    val opd_eye_blood_pressure_interpretation: String,
    val opd_eye_blood_pressure_systolic: String,

    val opd_eye_blood_sugar_fasting: String,
    val opd_eye_blood_sugar_interpretation: String,
    val opd_eye_blood_sugar_pp: String,

    val opd_eye_bt: String,
    val opd_eye_ct: String,
    val opd_eye_cbc: String,
    val opd_eye_ecg: String,
    val opd_eye_fa_left: String,
    val opd_eye_fa_right: String,
//    val opd_eye_fv_left: String,
//    val opd_eye_fv_right: String,
    val opd_eye_ha_left: String,
    val opd_eye_ha_left_unit: String,
    val opd_eye_ha_right: String,
    val opd_eye_ha_right_unit: String,

    val opd_eye_haemoglobin: String,
    val opd_eye_haemoglobin_interpretation: String,

    val opd_eye_hbsag: String,
    val opd_eye_hcv: String,
    val opd_eye_hiv: String,
    val opd_eye_iol_power: String,
    val opd_eye_iop_left: String,
    val opd_eye_iop_right: String,
    val opd_eye_mv_left: String,
    val opd_eye_mv_right: String,
    val opd_eye_pt: String,
    val opd_eye_slit_location: String,
    val opd_eye_slit_location_description: String,
    val opd_eye_sv_left: String,
    val opd_eye_sv_right: String,
    val opd_eye_tv_left: String,
    val opd_eye_tv_right: String,
    val opd_eye_va_left: String,
    val opd_eye_va_left_unit: String,
    val opd_eye_va_right: String,
    val opd_eye_va_right_unit: String,
    val patient_id: Int,
    val user_id: String,


)
