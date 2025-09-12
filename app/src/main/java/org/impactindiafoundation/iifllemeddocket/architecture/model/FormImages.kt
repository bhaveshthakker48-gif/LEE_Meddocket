package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "form_images")
data class FormImages(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    var formId:Int,
    val images:String,
    val temp_patient_id:String,
    val camp_id:String,
    var isSynced:Int=0
)
