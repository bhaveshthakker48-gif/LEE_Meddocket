package org.impactindiafoundation.iifllemeddocket.architecture.model

data class BrandUsage(
    val brandName: String,
    val genericName: String,
    val patientCount: Int,
    val totalQuantity: Double,
    val qtyName: String
)
