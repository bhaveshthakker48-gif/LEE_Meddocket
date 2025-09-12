package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntAudiometryRequest(
    val data: List<EntAudiometryItem>
)

data class EntAudiometryItem(
    val uniqueId: Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val appCreatedDate: String,
    val conductiveLeft : Boolean,
    val conductiveRight : Boolean,
    val conductiveBilateral : Boolean,
    val sensorineuralLeft : Boolean,
    val sensorineuralRight : Boolean,
    val sensorineuralBilateral : Boolean,
    val hearingAidGiven : Boolean,
    val hearingAidType : String,
    val app_id: String = "1"
)
