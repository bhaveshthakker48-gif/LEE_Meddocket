package org.impactindiafoundation.iifllemeddocket.Model.RegistrationModel

data class RegistrationDetailsModel(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val RegistrationDetails: List<RegistrationDetail>
) {
    data class RegistrationDetail(
        val aadharno: String,
        val age: String,
        val ageunit: String,
        val campid: Int,
        val citytownvillage: String,
        val district: String,
        val dob: String,
        val emailid: String,
        val fname: String,
        val gender: String,
        val houseno: String,
        val lname: String,
        val localityareapada: String,
        val mname: String,
        val mobileno: String,
        val patient_id: Int,
        val pincode: String,
        val regno: String,
        val statename: String,
        val streetname: String,
        val taluka: String,
        val voteridno: String
    )
}