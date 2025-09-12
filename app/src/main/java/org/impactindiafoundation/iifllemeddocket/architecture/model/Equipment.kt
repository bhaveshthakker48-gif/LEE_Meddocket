package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "orthosis_equipment")
data class Equipment(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val diagnosis: DiagnosisType,
    val equipment_category: String,
    val equipment_support: String,
    val given_when_case: String,
    val name: String
)