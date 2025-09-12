package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntSurgicalNotesRequest(
    val data: List<EntSurgicalNotesItem>
)

data class EntSurgicalNotesItem(
    val uniqueId: Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val appCreatedDate: String,
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
    val other: String,
    val bpInterpretation : String,
    val pulseValue: String,
    val bpSystolic: String,
    val bpDiastolic: String,
    val respirationValue: String,
    val temperatureValue: String,
    val temperatureUnit: String,
    val ecgDetail: String,
    val antibioticDetail: String,
    val app_id: String = "1"
)
