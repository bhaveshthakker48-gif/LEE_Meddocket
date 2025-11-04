package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_summary_table")
data class SyncSummaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,                  // Auto-generated primary key
    val totalSynced: Int,
    val totalUnsynced: Int,
    val dateTime: String,
    val formType: String
)
