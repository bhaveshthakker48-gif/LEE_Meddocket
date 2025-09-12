package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "InventoryUnitLocal")
data class InventoryUnitLocal(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val inventoryBrand_id: Int,
    val ratio_of: Double,
    val unit_id: Int,
    val unit_name: String,
    val unit_symbol: String
)
