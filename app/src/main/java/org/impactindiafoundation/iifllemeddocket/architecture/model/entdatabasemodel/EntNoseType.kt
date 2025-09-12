package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nose_type")
data class EntNoseType(
    val symptom: String,
    @PrimaryKey val id: Int
)
