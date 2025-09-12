package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "form_videos")
data class FormVideos(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val video:String,
    val temp_patient_id:String,
    val camp_id:String,
    val patient_id:String,
    var formId:Int,
    var isSynced:Int=0
)
