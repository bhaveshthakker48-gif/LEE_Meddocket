package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "symptoms_table")
data class EntSymptomsEntity(
    @PrimaryKey(autoGenerate = true) var uniqueId: Int = 0,
    val formId: Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val organ: String,
    val part: String,
    val symptom: String?,
    val symptomId: Int,
    val appCreatedDate: String,
    var app_id: String? = null
)
