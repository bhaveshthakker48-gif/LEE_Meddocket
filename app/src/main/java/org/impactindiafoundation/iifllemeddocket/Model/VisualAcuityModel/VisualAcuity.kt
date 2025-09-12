package org.impactindiafoundation.iifllemeddocket.Model.VisualAcuityModel


data class VisualAcuity(
    val _id: Int,
    val camp_id: Int,
    val createdDate: String,
    val patient_id: Int,
    val userId: String,
    val va_addi_details_left: String,
    val va_addi_details_right: String,

    val va_distant_vision_left: String,
    val va_distant_vision_right: String,
    val va_distant_vision_unit_left: String,
    val va_distant_vision_unit_right: String,

    val va_near_vision_left: String,
    val va_near_vision_right: String,

    val va_pinhole_improve_left: String,
    val va_pinhole_improve_right: String,
    val va_pinhole_improve_unit_left: String,
    val va_pinhole_improve_unit_right: String,
    val va_pinhole_left: String,
    val va_pinhole_right: String
)

