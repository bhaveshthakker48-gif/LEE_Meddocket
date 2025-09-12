package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntPreOpImageDetailsInstructionsResponse(
    @SerializedName("data")
    val data: List<PreOpImageDetailsInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class PreOpImageDetailsInstruction(

    @SerializedName("imageName")
    val imageName: String,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("uniqueId")
    val uniqueId: Int,

)
