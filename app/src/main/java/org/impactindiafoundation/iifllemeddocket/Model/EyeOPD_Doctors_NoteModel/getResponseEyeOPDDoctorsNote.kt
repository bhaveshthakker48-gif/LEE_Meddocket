package org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel

data class getResponseEyeOPDDoctorsNote(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val eyeopddocnotes: List<Eyeopddocnote>,
    val success_count: Int
) {
    data class Eyeopddocnote(
        val _id: String
    )
}