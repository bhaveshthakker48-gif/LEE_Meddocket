package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class EntPostOpInstructionsResponse(
    @SerializedName("data")
    val data: List<EntPostOpInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class EntPostOpInstruction(
    @SerializedName("airConductionThresholdDb")
    val airConductionThresholdDb: String? = null,

    @SerializedName("antibioticClavulanateGiven")
    val antibioticClavulanateGiven: Boolean,

    @SerializedName("app_id")
    val appId: String,

    @SerializedName("audiogramDate")
    val audiogramDate: String? = null,

    @SerializedName("audiogramResult")
    val audiogramResult: String? = null,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("clearFluidsStartTime")
    val clearFluidsStartTime: String? = null,

    @SerializedName("hasComplicationInfection")
    val hasComplicationInfection: Boolean,

    @SerializedName("hearingThresholdDate")
    val hearingThresholdDate: String? = null,

    @SerializedName("uniqueId")
    val uniqueId: Int,

    @SerializedName("ivHyderationDetail")
    val ivHyderationDetail: String? = null,

    @SerializedName("ivHydrationGiven")
    val ivHydrationGiven: Boolean,

    @SerializedName("npoTill")
    val npoTill: String? = null,

    @SerializedName("ofloxacinGiven")
    val ofloxacinGiven: Boolean,

    @SerializedName("otherComplicationsNote")
    val otherComplicationsNote: String? = null,

    @SerializedName("otherMedicationsNote")
    val otherMedicationsNote: String? = null,

    @SerializedName("otorrhoeaPresent")
    val otorrhoeaPresent: Boolean,

    @SerializedName("paracetamolGiven")
    val paracetamolGiven: Boolean,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("redivacUsed")
    val redivacUsed: Boolean,

    @SerializedName("sutureRemovalDate")
    val sutureRemovalDate: String? = null,

    @SerializedName("tympanicMembraneStatus")
    val tympanicMembraneStatus: String? = null,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("watchForFacialPalsy")
    val watchForFacialPalsy: Boolean,

    @SerializedName("watchForHematoma")
    val watchForHematoma: Boolean,

    @SerializedName("watchForMiddleEarInfection")
    val watchForMiddleEarInfection: Boolean,

    @SerializedName("watchForSoakage")
    val watchForSoakage: Boolean,

    @SerializedName("watchForWoundDehiscence")
    val watchForWoundDehiscence: Boolean,

    @SerializedName("watchForWoundInfection")
    val watchForWoundInfection: Boolean,

    @SerializedName("wickDrainUsed")
    val wickDrainUsed: Boolean
)
