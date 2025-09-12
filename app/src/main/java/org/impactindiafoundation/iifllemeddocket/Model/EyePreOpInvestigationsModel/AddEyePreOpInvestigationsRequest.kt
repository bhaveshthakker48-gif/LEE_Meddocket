package org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel

import com.google.gson.annotations.SerializedName

data class AddEyePreOpInvestigationsRequest(
    @SerializedName("eye_Pre_Op_Investigations")
    val eye_Pre_Op_Investigations: List<EyePreOpInvestigation>
)