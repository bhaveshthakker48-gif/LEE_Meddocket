package org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel

data class PathologyRequest(
    val data: List<PathologyItem>
)

data class PathologyItem(
    val uniqueId : Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val appCreatedDate: String,
    val cbc : Boolean,
    val cbcValue : String,
    val bt : Boolean,
    val btValue : String,
    val ct : Boolean,
    val ctValue : String,
    val hiv : Boolean,
    val hivValue : String,
    val hbsag : Boolean,
    val hbsagValue : String,
    val pta : Boolean,
    val ptaValue : String,
    val impedanceAudiometry : Boolean,
    val impedanceAudiometryValue : String?,
    val app_id: String = "1"
)
