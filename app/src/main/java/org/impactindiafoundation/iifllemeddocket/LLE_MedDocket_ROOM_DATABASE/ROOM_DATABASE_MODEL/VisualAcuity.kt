package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "VisualAcuity")
data class VisualAcuity(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val camp_id: Int,
    val createdDate: String,
    val patient_id: Int,
    val userId: String,
    val va_addi_details_left: String,
    val va_addi_details_right: String,

    val va_distant_vision_left: String,
    val va_distant_vision_right: String,
    val va_distant_vision_unit_left: String,
    val va_distant_vision_unit_right: String,

    val va_near_vision_left: String,
    val va_near_vision_right: String,

    val va_pinhole_improve_left: String,
    val va_pinhole_improve_right: String,
    val va_pinhole_improve_unit_left: String,
    val va_pinhole_improve_unit_right: String,
    val va_pinhole_left: String,
    val va_pinhole_right: String,
    val isSyn: Int =0
)
