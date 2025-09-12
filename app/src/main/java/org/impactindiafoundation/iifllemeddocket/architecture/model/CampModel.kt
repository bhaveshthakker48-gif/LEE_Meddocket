package org.impactindiafoundation.iifllemeddocket.architecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "camp_table")
data class CampModel(
    @PrimaryKey(autoGenerate = false)
    val campId:Int,
    val campName:String,
    var isComplete:Boolean
)
