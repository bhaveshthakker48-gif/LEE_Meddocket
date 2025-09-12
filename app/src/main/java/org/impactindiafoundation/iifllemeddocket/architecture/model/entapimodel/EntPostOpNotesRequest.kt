package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntPostOpNotesRequest(
    val data: List<EntPostOpNotesItem>
)

data class EntPostOpNotesItem(
    val uniqueId : Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val appCreatedDate: String,
    val ivHydrationGiven: Boolean,
    val npoTill: String,
    val clearFluidsStartTime: String,
    val paracetamolGiven: Boolean,
    val antibioticClavulanateGiven: Boolean,
    val ofloxacinGiven: Boolean,
    val otherMedicationsNote: String,
    val wickDrainUsed: Boolean,
    val redivacUsed: Boolean,
    val watchForSoakage: Boolean,
    val watchForHematoma: Boolean,
    val watchForMiddleEarInfection: Boolean,
    val watchForWoundInfection: Boolean,
    val watchForWoundDehiscence: Boolean,
    val watchForFacialPalsy: Boolean,
    val sutureRemovalDate: String,
    val audiogramResult: String,
    val audiogramDate: String,
    val hasComplicationInfection: Boolean,
    val otherComplicationsNote: String,
    val tympanicMembraneStatus: String,
    val otorrhoeaPresent: Boolean,
    val airConductionThresholdDb: String,
    val hearingThresholdDate: String,
    val ivHyderationDetail: String,
    val app_id: String = "1"
)
