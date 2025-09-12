package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Vitals")
data class Vitals(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val bmi: String,
    val bmiInterpretation: String,
    val bpInterpretation: String,
    val camp_id: Int,
    val createdDate: String,
    val diastolic: String,
    val height: String,
    val heightUnit: String,
    val patient_id: Int,
    val prInterpretation: String,
    val pulseRate: Int,
    val systolic: String,
    val userId: String,
    val weight: String,
    val weightUnit: String,
    val isSyn: Int =0
)