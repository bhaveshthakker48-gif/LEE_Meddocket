package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntPathologyDetailsInstructionsResponse(
    @SerializedName("data")
    val data: List<PathologyDetailsInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class PathologyDetailsInstruction(

    @SerializedName("app_id")
    val appId: String,

    @SerializedName("bt")
    val bt: Boolean,

    @SerializedName("btValue")
    val btValue: String,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("cbc")
    val cbc: Boolean,

    @SerializedName("cbcValue")
    val cbcValue: String,

    @SerializedName("ct")
    val ct: Boolean,

    @SerializedName("ctValue")
    val ctValue: String,

    @SerializedName("hbsag")
    val hbsag: Boolean,

    @SerializedName("hbsagValue")
    val hbsagValue: String,

    @SerializedName("hiv")
    val hiv: Boolean,

    @SerializedName("hivValue")
    val hivValue: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("impedanceAudiometry")
    val impedanceAudiometry: Boolean,

    @SerializedName("impedanceAudiometryValue")
    val impedanceAudiometryValue: String,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("pta")
    val pta: Boolean,

    @SerializedName("ptaValue")
    val ptaValue: String,

    @SerializedName("uniqueId")
    val uniqueId: Int,

    @SerializedName("userId")
    val userId: Int
)
