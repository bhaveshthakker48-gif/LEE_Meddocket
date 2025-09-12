package org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pathology_image_table")
data class PathologyImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val formId: Int,
    val patientId : Int,
    val campId : Int,
    val userId : Int,
    val appCreatedDate: String?,
    val filename: String,
    val reportType: String,
    val app_id: String? = null
)
