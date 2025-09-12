package org.impactindiafoundation.iifllemeddocket.Model.OPD_InvestigationsModel

data class getOPD_Investigations_Response(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val opd_Investigationses: List<OpdInvestigationse>,
    val success_count: Int
) {
    data class OpdInvestigationse(
        val _id: Int
    )
}