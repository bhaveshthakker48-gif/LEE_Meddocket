package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_prescription_record_table")
data class SyncPrescriptionRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val time: String,
    val syncCount: Int,
    val unsyncCount: Int,
    val formType: String
)
