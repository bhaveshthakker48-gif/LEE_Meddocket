package org.impactindiafoundation.iifllemeddocket.Model.SurgicalNotesModel

import com.google.gson.annotations.SerializedName

data class SurgicalNotesRequest(
    @SerializedName("cataract_Surgeries")
    val cataract_Surgeries: List<CataractSurgery>
)