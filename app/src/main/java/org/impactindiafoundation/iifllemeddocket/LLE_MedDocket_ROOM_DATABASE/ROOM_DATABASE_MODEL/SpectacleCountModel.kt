package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

data class SpectacleCountModel(

    val patientNotComeCount: Int,
    val patientCallAgainCount: Int,
    val spectacleGivenCount: Int,
    val spectacleNotMatchingCount: Int,
    val spectacleNotReceivedCount: Int,
    val singleVisionCount:Int,
    val singleVisionHPCount:Int,
    val bifocalCount:Int,
    val bifocalHPCount:Int
)
