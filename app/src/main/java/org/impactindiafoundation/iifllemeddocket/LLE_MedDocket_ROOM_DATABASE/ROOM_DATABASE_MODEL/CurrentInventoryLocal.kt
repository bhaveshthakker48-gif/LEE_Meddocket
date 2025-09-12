package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "CurrentInventoryLocal")
data class CurrentInventoryLocal(
    @PrimaryKey(autoGenerate = true) val _id: Int=0,
    val ITEM_CATEGORY: String,
    val batch_no: String,
    val brand_id: Int,
    val brand_name: String,
    val inventoryItem_id: Int,
    val item_name: String,
    val manufactured_by: String,
    val procurementItem_id: Int,
    val purchaseItem_id: Int,
    val unit_name: String,
    val unit_ratio_of: String,
    val unit_symbol: String
)