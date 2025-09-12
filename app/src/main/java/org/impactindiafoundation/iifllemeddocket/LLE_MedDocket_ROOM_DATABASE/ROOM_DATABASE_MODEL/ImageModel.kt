package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Image")
data class ImageModel(
    @PrimaryKey(autoGenerate = true) val index:Int,
    val _id:Int?=null,
    val file_name:String?=null,
    val image_type:String?=null,
    val patient_id:Int?=null,
    val camp_id:Int?=null,
    val user_id:Int?=null,
    val file_path:String?=null,
    val isSyn: Int =0

)