package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eye_type")
data class EntEarType(
    val symptom: String,
    @PrimaryKey val id: Int
)
