package org.impactindiafoundation.iifllemeddocket.Model.OPD_InvestigationsModel

import com.google.gson.annotations.SerializedName

data class sendOPD_InvestigationData(
    @SerializedName("opd_Investigationses")
    val opdInvestigationsList: List<OPD_Investigation>
)
