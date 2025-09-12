package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orthosis_master")
data class OrthosisType(
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val measurements: List<Measurement>,
    var name: String
)
