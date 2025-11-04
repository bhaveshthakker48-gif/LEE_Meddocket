package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "CreatePrescriptionModel")
data class CreatePrescriptionModel(

    @PrimaryKey(autoGenerate = true) var _id: Int,
    var patient_id:String,
    var patient_name:String,
    var doctor_specialty:String,
    var generic_name:String,
    var brand_name:String,
    var brand_id:String,
    var batch_no:String,
    var procurementItem_id:String,
    var quantity:String,
    var quantity_id: Long,
    var quantity_unit: String,
    var dose:String,
    var dose_frequency:String,
    var frequency:String,
    var duration:String,
    var selected_duration:String,
    var current_date:String,
    var user_id:String,
    var camp_id:Long,
    var device_id:String,
    var device_name:String,
    var isSyn:Int=0,
    ): Serializable
