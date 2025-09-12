package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "SynTable")
data class SynTable(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val syn_type:String,
    val camp_id:Int,
    val user_id:String,
    val date:String,
    val time:String,
    val isSyn:Int=0
)
