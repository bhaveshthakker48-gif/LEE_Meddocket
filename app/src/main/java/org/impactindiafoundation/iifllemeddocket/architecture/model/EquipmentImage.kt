package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment_image")
data class EquipmentImage(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val temp_patient_id:String,
    val camp_id:String,
    var patient_id:String,
    val patient_image_type:String,
    val images:String,
    var isSynced:Int=0
)
