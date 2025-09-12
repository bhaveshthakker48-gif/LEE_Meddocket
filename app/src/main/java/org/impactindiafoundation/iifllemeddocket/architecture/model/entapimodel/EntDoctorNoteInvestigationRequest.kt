package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntDoctorNoteInvestigationRequest(
    val data: List<EntDoctorNoteInvestigationItem>
)

data class EntDoctorNoteInvestigationItem(
    val uniqueId : Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val cbc: Boolean,
    val bt: Boolean,
    val ct: Boolean,
    val hiv: Boolean,
    val hbsag: Boolean,
    val puretoneaudiometry: Boolean,
    val impedanceaudiometry: Boolean,
    val xray: Boolean,
    val xray_value: String,
    val removalOfimpactedwax_right: Boolean,
    val removalOfimpactedwax_left: Boolean,
    val tympanoplasty_right: Boolean,
    val tympanoplasty_left: Boolean,
    val exploratoryTympanoplasty_right: Boolean,
    val exploratoryTympanoplasty_left: Boolean,
    val exploratoryMastoidectomy_right: Boolean,
    val exploratoryMastoidectomy_left: Boolean,
    val fbremoval_right: Boolean,
    val fbremoval_left: Boolean,
    val grommetInsertion_right: Boolean,
    val grommetInsertion_left: Boolean,
    val excisionBiospy_right: Boolean,
    val excisionBiospy_left: Boolean,
    val other: String,
    val other_right: Boolean,
    val other_left: Boolean,
    val lineUpForSurgery: Boolean,
    val medication: Boolean,
    val audiometry: Boolean,
    val appCreatedDate: String,
    val app_id: String = "1"
)
