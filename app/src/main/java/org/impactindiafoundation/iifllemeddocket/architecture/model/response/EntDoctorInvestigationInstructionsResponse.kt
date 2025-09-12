package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntDoctorInvestigationInstructionsResponse(
    @SerializedName("data")
    val data: List<DoctorInvestigationInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class DoctorInvestigationInstruction(

    @SerializedName("app_id")
    val appId: String,

    @SerializedName("audiometry")
    val audiometry: Boolean,

    @SerializedName("bt")
    val bt: Boolean,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("cbc")
    val cbc: Boolean,

    @SerializedName("ct")
    val ct: Boolean,

    @SerializedName("excisionBiospy_left")
    val excisionBiospyLeft: Boolean,

    @SerializedName("excisionBiospy_right")
    val excisionBiospyRight: Boolean,

    @SerializedName("exploratoryMastoidectomy_left")
    val exploratoryMastoidectomyLeft: Boolean,

    @SerializedName("exploratoryMastoidectomy_right")
    val exploratoryMastoidectomyRight: Boolean,

    @SerializedName("exploratoryTympanoplasty_left")
    val exploratoryTympanoplastyLeft: Boolean,

    @SerializedName("exploratoryTympanoplasty_right")
    val exploratoryTympanoplastyRight: Boolean,

    @SerializedName("fbremoval_left")
    val fbRemovalLeft: Boolean,

    @SerializedName("fbremoval_right")
    val fbRemovalRight: Boolean,

    @SerializedName("grommetInsertion_left")
    val grommetInsertionLeft: Boolean,

    @SerializedName("grommetInsertion_right")
    val grommetInsertionRight: Boolean,

    @SerializedName("hbsag")
    val hbsag: Boolean,

    @SerializedName("hiv")
    val hiv: Boolean,

    @SerializedName("id")
    val id: Int,

    @SerializedName("impedanceaudiometry")
    val impedanceAudiometry: Boolean,

    @SerializedName("lineUpForSurgery")
    val lineUpForSurgery: Boolean,

    @SerializedName("medication")
    val medication: Boolean,

    @SerializedName("other")
    val other: String,

    @SerializedName("other_left")
    val otherLeft: Boolean,

    @SerializedName("other_right")
    val otherRight: Boolean,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("puretoneaudiometry")
    val pureToneAudiometry: Boolean,

    @SerializedName("removalOfimpactedwax_left")
    val removalOfImpactedWaxLeft: Boolean,

    @SerializedName("removalOfimpactedwax_right")
    val removalOfImpactedWaxRight: Boolean,

    @SerializedName("tympanoplasty_left")
    val tympanoplastyLeft: Boolean,

    @SerializedName("tympanoplasty_right")
    val tympanoplastyRight: Boolean,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("xray")
    val xray: Boolean,

    @SerializedName("xray_value")
    val xrayValue: String,

    @SerializedName("uniqueId")
    val uniqueId: Int,
)
