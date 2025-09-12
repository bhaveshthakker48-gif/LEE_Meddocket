package org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preop_details_table")
data class EntPreOpDetailsEntity(
    @PrimaryKey(autoGenerate = true) var uniqueId: Int = 0,
    val patientId: Int,
    val campId: Int,
    val userId: Int,
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
    val otherInstructionsDetail: String? = null,
    val surgeryCancel: Boolean? = null,
    val surgeryCancelReason: String? = null,
    val appCreatedDate: String,
    var app_id: String? = null
)
