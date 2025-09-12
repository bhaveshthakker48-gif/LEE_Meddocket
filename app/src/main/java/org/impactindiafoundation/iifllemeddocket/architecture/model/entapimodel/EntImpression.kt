package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntImpressionType(
    val data: EntImpressionData,
    val message: String,
    val success: Boolean
)

data class EntImpressionData(
    val results: List<EntImpressionItem>
)

data class EntImpressionItem(
    val impression: String,
    val id: Int
)

