package org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model

data class getEyePostAndFollowUpResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val eye_Post_Ops: List<EyePostOp>,
    val success_count: Int
) {
    data class EyePostOp(
        val _id: String
    )
}