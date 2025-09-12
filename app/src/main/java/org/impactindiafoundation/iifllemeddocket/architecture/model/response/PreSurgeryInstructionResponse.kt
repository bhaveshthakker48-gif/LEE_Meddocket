package org.impactindiafoundation.iifllemeddocket.architecture.model.response

import com.google.gson.annotations.SerializedName

data class PreSurgeryInstructionResponse(
    @SerializedName("data")
    val data: List<PreSurgeryInstruction>,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class PreSurgeryInstruction(
    @SerializedName("adultsolidorliquid")
    val adultSolidOrLiquid: Boolean,

    @SerializedName("adultsolidorliquidTime")
    val adultSolidOrLiquidTime: String,

    @SerializedName("adulttab")
    val adultTab: Boolean,

    @SerializedName("adultwaterorliquid")
    val adultWaterOrLiquid: Boolean,

    @SerializedName("adultwaterorliquidTime")
    val adultWaterOrLiquidTime: String,

    @SerializedName("app_id")
    val appId: String,

    @SerializedName("campId")
    val campId: Int,

    @SerializedName("childrenbreastfed")
    val childrenBreastfed: Boolean,

    @SerializedName("childrenbreastfedTime")
    val childrenBreastfedTime: String,

    @SerializedName("childrensolidorliquid")
    val childrenSolidOrLiquid: Boolean,

    @SerializedName("childrensolidorliquidTime")
    val childrenSolidOrLiquidTime: String,

    @SerializedName("childrenwaterorliquid")
    val childrenWaterOrLiquid: Boolean,

    @SerializedName("childrenwaterorliquidTime")
    val childrenWaterOrLiquidTime: String,

    @SerializedName("currentedicine")
    val currentMedicine: String,

    @SerializedName("uniqueId")
    val uniqueId: Int,

    @SerializedName("injtt")
    val injTT: Boolean,

    @SerializedName("otherInstructions")
    val otherInstructions: Boolean,

    @SerializedName("otherInstructionsDetail")
    val otherInstructionsDetail: String,

    @SerializedName("patientId")
    val patientId: Int,

    @SerializedName("surgeryCancel")
    val surgeryCancel: Boolean,

    @SerializedName("surgeryCancelReason")
    val surgeryCancelReason: String,

    @SerializedName("userId")
    val userId: Int
)
