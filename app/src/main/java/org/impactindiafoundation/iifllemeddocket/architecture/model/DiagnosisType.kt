package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnosis_master")
data class DiagnosisType(
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val name:String
)
