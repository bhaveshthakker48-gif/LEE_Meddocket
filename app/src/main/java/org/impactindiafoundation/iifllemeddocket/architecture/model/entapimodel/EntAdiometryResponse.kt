package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntAudiometryResponse(
    val data: EntAudiometryResponseData?,
    val message: String,
    val success: Boolean
)

data class EntAudiometryResponseData(
    val success_count: Int,
    val results: List<EntAudiometryResult>
)

data class EntAudiometryResult(
    val message: String,
    val app_id: String
)
