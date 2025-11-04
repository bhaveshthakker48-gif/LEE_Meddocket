package org.impactindiafoundation.iifllemeddocket.architecture.model

data class SyncResult(
    val type: String,
    val total: Int,
    val synced: Int,
    val unsynced: Int,
    val success: Boolean,
    val message: String
)

