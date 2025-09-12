package org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel

import com.google.gson.annotations.SerializedName


data class sendEyeOPDDoctorsNoteData(
    @SerializedName("eyeopddocnotes")
    val EyeopddocnoteList: List<Eyeopddocnote>
)