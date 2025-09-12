package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntSurgicalInstructionsResponse(
    @SerializedName("data")
    val data: List<EntSurgicalInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class EntSurgicalInstruction(
    @SerializedName("adrenalinInfiltrationDone")
    val adrenalinInfiltrationDone: Boolean,

    @SerializedName("antibioticGiven")
    val antibioticGiven: Boolean,

    @SerializedName("app_id")
    val appId: String,

    @SerializedName("bpDiastolic")
    val bpDiastolic: String,

    @SerializedName("bpMonitored")
    val bpMonitored: Boolean,

    @SerializedName("bpSystolic")
    val bpSystolic: String,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("earWaxRemovalDone")
    val earWaxRemovalDone: Boolean,

    @SerializedName("ecgMonitored")
    val ecgMonitored: Boolean,

    @SerializedName("ethamsylateGiven")
    val ethamsylateGiven: Boolean,

    @SerializedName("excisionBiopsyDone")
    val excisionBiopsyDone: Boolean,

    @SerializedName("foreignBodyRemovalDone")
    val foreignBodyRemovalDone: Boolean,

    @SerializedName("generalEndotrachealUsed")
    val generalEndotrachealUsed: Boolean,

    @SerializedName("grommentInsertionDone")
    val grommentInsertionDone: Boolean,

    @SerializedName("uniqueId")
    val uniqueId: Int,

    @SerializedName("lignocaineSensitive")
    val lignocaineSensitive: Boolean,

    @SerializedName("localApplicationDone")
    val localApplicationDone: Boolean,

    @SerializedName("localInfiltrationDone")
    val localInfiltrationDone: Boolean,

    @SerializedName("mastoidectomyDone")
    val mastoidectomyDone: Boolean,

    @SerializedName("nerveBlock")
    val nerveBlock: Boolean,

    @SerializedName("other")
    val other: String,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("pulseMonitored")
    val pulseMonitored: Boolean,

    @SerializedName("pulseValue")
    val pulseValue: String,

    @SerializedName("respirationMonitored")
    val respirationMonitored: Boolean,

    @SerializedName("respirationValue")
    val respirationValue: String,

    @SerializedName("temperatureMonitored")
    val temperatureMonitored: Boolean,

    @SerializedName("temperatureUnit")
    val temperatureUnit: String,

    @SerializedName("temperatureValue")
    val temperatureValue: String,

    @SerializedName("tympanoplastyDone")
    val tympanoplastyDone: Boolean,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("xylocaineSensitive")
    val xylocaineSensitive: Boolean,

    @SerializedName("antibioticDetail")
    val antibioticDetail: String,

    @SerializedName("ecgDetail")
    val ecgDetail: String
)
