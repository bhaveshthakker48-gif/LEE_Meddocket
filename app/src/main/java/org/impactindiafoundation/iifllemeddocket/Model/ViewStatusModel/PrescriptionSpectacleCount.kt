package org.impactindiafoundation.iifllemeddocket.Model.ViewStatusModel

data class PrescriptionSpectacleCount(
    val label:String,
    val status:String,
    val count:Int,
    val singleVisionCount:Int,
    val singleVisionHPCount:Int,
    val bifocalCount:Int,
    val bifocalHPCount:Int
)
