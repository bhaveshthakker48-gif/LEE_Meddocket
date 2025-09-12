package org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pathology_table")
data class PathologyEntity(
    @PrimaryKey(autoGenerate = true) var uniqueId: Int = 0,
    val patientId : Int,
    val campId : Int,
    val userId : Int,
    val cbc : Boolean,
    val cbcValue : String?,
    val bt : Boolean,
    val btValue : String?,
    val ct : Boolean,
    val ctValue : String?,
    val hiv : Boolean,
    val hivValue : String?,
    val hbsag : Boolean,
    val hbsagValue : String?,
    val pta : Boolean,
    val ptaValue : String?,
    val impedanceAudiometry : Boolean,
    val impedanceAudiometryValue : String?,
    val appCreatedDate : String?,
    var app_id: String? = null
    )
