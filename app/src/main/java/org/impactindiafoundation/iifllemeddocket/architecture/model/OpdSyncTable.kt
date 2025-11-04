package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "OpdSyncTable")
data class OpdSyncTable(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var dateTime:String,
    var syncedCount:Int,
    var unsyncFormCount: Int,
)
