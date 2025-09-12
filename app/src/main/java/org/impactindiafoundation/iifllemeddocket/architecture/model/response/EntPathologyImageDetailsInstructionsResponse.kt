package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntPathologyImageDetailsInstructionsResponse(
    @SerializedName("data")
    val data: List<PathologyImageDetailsInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class PathologyImageDetailsInstruction(

    @SerializedName("imageName")
    val imageName: String,

    @SerializedName("reportType")
    val reportType: String,

    @SerializedName("formId")
    val formId: Int,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("uniqueId")
    val uniqueId: Int,

)
