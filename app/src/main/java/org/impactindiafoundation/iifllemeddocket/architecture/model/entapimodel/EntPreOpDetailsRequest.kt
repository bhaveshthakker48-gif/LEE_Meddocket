package org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel

data class EntPreOpDetailsRequest(
    val data: List<EntPreOpDetailsItem>
)

data class EntPreOpDetailsItem(
    val uniqueId : Int,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
    val appCreatedDate: String,
    val injtt: Boolean,
    val adulttab: Boolean,
    val childrensolidorliquid: Boolean,
    val childrenbreastfed: Boolean,
    val childrenwaterorliquid: Boolean,
    val adultsolidorliquid: Boolean,
    val adultwaterorliquid: Boolean,
    val currentedicine: String,
    val childrensolidorliquidTime: String,
    val childrenbreastfedTime: String,
    val childrenwaterorliquidTime: String,
    val adultsolidorliquidTime: String,
    val adultwaterorliquidTime: String,
    val otherInstructions: Boolean,
    val otherInstructionsDetail: String,
    val surgeryCancel: Boolean,
    val surgeryCancelReason: String,
    val app_id: String = "1"
)
