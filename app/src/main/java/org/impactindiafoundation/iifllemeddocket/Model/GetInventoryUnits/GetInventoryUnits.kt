package org.impactindiafoundation.iifllemeddocket.Model.GetInventoryUnits

data class GetInventoryUnits(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val inventoryUnits: List<InventoryUnit>
) {
    data class InventoryUnit(
        val inventoryBrand_id: Int,
        val ratio_of: Double,
        val unit_id: Int,
        val unit_name: String,
        val unit_symbol: String
    )
}