package org.impactindiafoundation.iifllemeddocket.architecture.model

data class OrthosisFormSyncResponse(
    val failedSyncId: List<Int>,
    val message: String,
    val successSyncId: List<Int>
)