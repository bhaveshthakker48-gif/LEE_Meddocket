package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orthosis_image")
data class OrthosisImages(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val images:String,
    val temp_patient_id:String,
    val camp_id:String,
    val patient_id:String,
    val orthosis_id:String,
    var orthosisFormId:Int,
    val amputation_side:String,
    var isSynced:Int=0
)
