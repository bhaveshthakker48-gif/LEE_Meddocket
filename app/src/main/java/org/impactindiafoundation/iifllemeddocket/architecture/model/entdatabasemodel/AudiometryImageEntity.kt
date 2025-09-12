package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audiometry_image_table")
data class AudiometryImageEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val patientId : Int,
    val campId : Int,
    val userId : Int,
    val appCreatedDate: String?,
    val filename: String,
    val app_id: String? = null
)
