package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserModel(
    @PrimaryKey(autoGenerate = false)
    val userId:Int,
    val address: String?,
    val contactNo: String,
    val designationName: String,
    val designationNo: String,
    val emailId: String,
    val firstName: String,
    val lastName: String,
    val mobileNumber: String,
    val password: String,
    val prefix: String,
    val title: String,
    val userName: String,
    val campId: Int,
    val campName: String,
    val campNo: Int,
    val campFrom: String,
    val campTo: String
)
