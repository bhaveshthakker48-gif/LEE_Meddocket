package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.PrimaryKey

data class PrescriptionSynTable(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val syn_type: String,
    val date: String,
    val time: String,
    val isSyn: Int = 0,
    val syncItemCount: Int,
    val notSyncItemCount: Int
)
