package org.impactindiafoundation.iifllemeddocket.Model.EyePreOPNotes

data class AddEyePreOpNotesResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val eye_Pre_Op_Notes: List<EyePreOpNote>,
    val success_count: Int
) {
    data class EyePreOpNote(
        val _id: String
    )
}