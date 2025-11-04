package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orthosis_sync_table")
data class OrthosisSynTable(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val syn_type:String,
    val date:String,
    val time:String,
    val isSyn:Int=0,
    val syncItemCount: Int,
    val notSyncItemCount: Int
)
