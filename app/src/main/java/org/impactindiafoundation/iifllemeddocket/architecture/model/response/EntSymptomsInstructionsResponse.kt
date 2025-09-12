package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntSymptomsInstructionsResponse(
    @SerializedName("data")
    val data: List<SymptomsInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class SymptomsInstruction(

    @SerializedName("app_id")
    val appId: String,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("uniqueId")
    val uniqueId: Int,

    @SerializedName("formId")
    val formId: Int,

    @SerializedName("organ")
    val organ: String,

    @SerializedName("part")
    val part: String,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("symptom")
    val symptom: String,

    @SerializedName("symptomId")
    val symptomId: Int,

    @SerializedName("userId")
    val userId: Int
)
