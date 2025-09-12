package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preop_image_table")
data class PreOpImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientId : Int,
    val campId : Int,
    val userId : Int,
    val appCreatedDate: String?,
    val filename: String,
    val app_id: String? = null
)
