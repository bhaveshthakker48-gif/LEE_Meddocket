package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntImpressionInstructionsResponse(
    @SerializedName("data")
    val data: List<ImpressionInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class ImpressionInstruction(

    @SerializedName("app_id")
    val appId: String,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("uniqueId")
    val uniqueId: Int,

    @SerializedName("formId")
    val formId: Int,

    @SerializedName("impression")
    val impression: String,

    @SerializedName("impressionId")
    val impressionId: Int,

    @SerializedName("part")
    val part: String,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("userId")
    val userId: Int
)
