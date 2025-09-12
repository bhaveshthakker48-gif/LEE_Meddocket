package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntPreOpDetailsResponse(
    val data: EntPreOpDetailsResponseData?,
    val message: String,
    val success: Boolean
)

data class EntPreOpDetailsResponseData(
    val success_count: Int,
    val results: List<EntPreOpDetailsResult>
)

data class EntPreOpDetailsResult(
    val message: String,
    val app_id: String
)
