package org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel

data class PrescriptionDetailsModel(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val patientPrescription: List<PatientPrescription>
) {
    data class PatientPrescription(
        val camp_id: Int,
        val fundus_notes: String,
        val is_given: String,
        val is_ordered: String,
        val patient_id: Int,
        val presc_type: String,
        val re_bvd: String,
        val re_distant_vision_axis_left: String,
        val re_distant_vision_axis_right: String,
        val re_distant_vision_cylinder_left: String,
        val re_distant_vision_cylinder_right: String,
        val re_distant_vision_left: String,
        val re_distant_vision_right: String,
        val re_distant_vision_sphere_left: String,
        val re_distant_vision_sphere_right: String,
        val re_distant_vision_unit_left: String,
        val re_distant_vision_unit_right: String,
        val re_near_vision_axis_left: String,
        val re_near_vision_axis_right: String,
        val re_near_vision_cylinder_left: String,
        val re_near_vision_cylinder_right: String,
        val re_near_vision_left: String,
        val re_near_vision_right: String,
        val re_near_vision_sphere_left: String,
        val re_near_vision_sphere_right: String,
        val re_prism_left: String,
        val re_prism_right: String,
        val re_prism_unit_left: String,
        val re_prism_unit_right: String,
        val re_pupipllary_distance: String,
        val re_reading_addition_left: String,
        val re_reading_addition_left_details: String,
        val re_reading_addition_right: String,
        val re_reading_addition_right_details: String,
        val re_remark_left: String,
        val re_remark_right: String,
        val re_remarks: String,
        val reading_glass: String,
        val user_id: Int
    )
}