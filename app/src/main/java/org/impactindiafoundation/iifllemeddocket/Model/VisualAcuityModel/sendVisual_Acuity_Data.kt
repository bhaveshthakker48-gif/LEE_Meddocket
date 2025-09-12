package org.impactindiafoundation.iifllemeddocket.Model.VisualAcuityModel

import com.google.gson.annotations.SerializedName


data class sendVisual_Acuity_Data (
    @SerializedName("visualActivities")
    val visualAcuityList: List<VisualAcuity>
)

