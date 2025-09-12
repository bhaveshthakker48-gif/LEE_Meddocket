package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "impression_type")
data class ImpressionType(
    val impression: String,
    @PrimaryKey val id: Int
)
