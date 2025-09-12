package org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel

data class getEyePreOpInvestigationResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val eye_Pre_Op_Investigations: List<EyePreOpInvestigation>,
    val success_count: Int
) {
    data class EyePreOpInvestigation(
        val _id: String
    )
}