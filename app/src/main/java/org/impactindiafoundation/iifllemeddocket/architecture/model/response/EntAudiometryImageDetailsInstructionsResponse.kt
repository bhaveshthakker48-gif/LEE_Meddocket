package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntAudiometryImageDetailsInstructionsResponse(
    @SerializedName("data")
    val data: List<AudiometryImageDetailsInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class AudiometryImageDetailsInstruction(

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
