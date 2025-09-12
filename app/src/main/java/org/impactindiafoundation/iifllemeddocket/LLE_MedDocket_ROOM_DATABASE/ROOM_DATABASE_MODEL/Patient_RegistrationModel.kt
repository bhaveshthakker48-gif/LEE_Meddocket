package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Patient_RegistrationModel")
data class Patient_RegistrationModel(
    @PrimaryKey(autoGenerate = true) val _id: Int,
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
