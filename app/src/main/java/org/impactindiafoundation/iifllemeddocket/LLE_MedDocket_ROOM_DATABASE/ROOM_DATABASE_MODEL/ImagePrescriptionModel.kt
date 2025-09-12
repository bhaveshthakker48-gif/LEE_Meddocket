package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ImagePrescriptionModel")
data class ImagePrescriptionModel(

    @PrimaryKey(autoGenerate = true)
    val _id:Int=0,
    val file_name:String?=null,
    val patient_id:Int?=null,
    val camp_id:Int?=null,
    val user_id: Int? =null,
    val isSyn: Int =0


)
