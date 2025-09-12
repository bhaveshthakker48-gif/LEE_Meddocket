package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audiometry_table")
data class AudiometryEntity(
    @PrimaryKey(autoGenerate = true) var uniqueId: Int = 0,
    val patientId : Int,
    val campId : Int,
    val userId : Int,
    val conductiveLeft : Boolean,
    val conductiveRight : Boolean,
    val conductiveBilateral : Boolean,
    val sensorineuralLeft : Boolean,
    val sensorineuralRight : Boolean,
    val sensorineuralBilateral : Boolean,
    val hearingAidGiven : Boolean,
    val hearingAidType : String?,
    val appCreatedDate : String?,
    var app_id: String? = null
    )
