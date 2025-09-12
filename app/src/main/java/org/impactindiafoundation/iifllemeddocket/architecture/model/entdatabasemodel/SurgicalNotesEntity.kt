package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surgical_note_table")
data class SurgicalNotesEntity(
    @PrimaryKey(autoGenerate = true) var uniqueId: Int = 0,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val lignocaineSensitive: Boolean,
    val xylocaineSensitive: Boolean,
    val localApplicationDone: Boolean,
    val localInfiltrationDone: Boolean,
    val nerveBlock: Boolean,
    val generalEndotrachealUsed: Boolean,
    val pulseMonitored: Boolean,
    val respirationMonitored: Boolean,
    val bpMonitored: Boolean,
    val ecgMonitored: Boolean,
    val temperatureMonitored: Boolean,
    val antibioticGiven: Boolean,
    val ethamsylateGiven: Boolean,
    val adrenalinInfiltrationDone: Boolean,
    val earWaxRemovalDone: Boolean,
    val tympanoplastyDone: Boolean,
    val mastoidectomyDone: Boolean,
    val foreignBodyRemovalDone: Boolean,
    val grommentInsertionDone: Boolean,
    val excisionBiopsyDone: Boolean,
    val other: String? = null,
    val bpInterpretation: String? = null,
    val pulseValue: String? = null,
    val bpSystolic: String? = null,
    val bpDiastolic: String? = null,
    val respirationValue: String? = null,
    val temperatureValue: String? = null,
    val temperatureUnit: String? = null,
    val ecgDetail: String? = null,
    val antibioticDetail: String? = null,
    val appCreatedDate: String,
    var app_id: String? = null
)

