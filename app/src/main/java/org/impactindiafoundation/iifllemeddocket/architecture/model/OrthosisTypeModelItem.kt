package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class OrthosisTypeModelItem(
    val deleted: Boolean,
    val id: Int,
    val measurements: List<Measurement>,
    val name: String
)