package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "throat_type")
data class EntThroatType(
    val symptom: String,
    @PrimaryKey val id: Int
)

