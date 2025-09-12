package org.impactindiafoundation.iifllemeddocket.Model.EyePreOPNotes

import com.google.gson.annotations.SerializedName

data class AddEyePreOpNotesRequest(
    @SerializedName("eye_Pre_Op_Notes")
    val eye_Pre_Op_Notes: List<EyePreOpNote>
)