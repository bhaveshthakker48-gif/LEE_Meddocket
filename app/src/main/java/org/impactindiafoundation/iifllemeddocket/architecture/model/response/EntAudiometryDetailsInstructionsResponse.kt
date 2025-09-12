package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntAudiometryDetailsInstructionsResponse(
    @SerializedName("data")
    val data: List<AudiometryDetailsInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class AudiometryDetailsInstruction(

    @SerializedName("app_id")
    val appId: String,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("conductiveBilateral")
    val conductiveBilateral: Boolean,

    @SerializedName("conductiveLeft")
    val conductiveLeft: Boolean,

    @SerializedName("conductiveRight")
    val conductiveRight: Boolean,

    @SerializedName("hearingAidGiven")
    val hearingAidGiven: Boolean,

    @SerializedName("hearingAidType")
    val hearingAidType: String,

    @SerializedName("uniqueId")
    val uniqueId: Int,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("sensorineuralBilateral")
    val sensorineuralBilateral: Boolean,

    @SerializedName("sensorineuralLeft")
    val sensorineuralLeft: Boolean,

    @SerializedName("sensorineuralRight")
    val sensorineuralRight: Boolean,

    @SerializedName("userId")
    val userId: Int
)
