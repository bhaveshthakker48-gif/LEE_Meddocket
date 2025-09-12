package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "impression_table")
data class EntImpressionEntity(
    @PrimaryKey(autoGenerate = true) var uniqueId: Int = 0,
    val formId: Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val part: String,
    val impression: String?,
    val impressionId: Int,
    val appCreatedDate: String,
    var app_id: String? = null
)