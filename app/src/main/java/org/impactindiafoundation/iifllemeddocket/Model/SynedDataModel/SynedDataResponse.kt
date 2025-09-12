package org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel

data class SynedDataResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val lleSyncReport: List<LleSyncReport>,
    val success_count: Int
) {
    data class LleSyncReport(
        val _id: String
    )
}