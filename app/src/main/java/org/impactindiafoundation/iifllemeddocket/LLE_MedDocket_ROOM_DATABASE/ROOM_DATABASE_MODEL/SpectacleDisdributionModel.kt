package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName ="SpectacleDisdributionStatusModel")
data class SpectacleDisdributionStatusModel(

    @PrimaryKey(autoGenerate = true)
    val index:Int,
    val alternate_mobile: String,
    val app_createdDate: String,
    val call_again_date: String,
    val camp_id: String,
    val id: Int,
    val patient_call_again: Boolean,
    val patient_id: Int,
    val patient_not_come: Boolean,
    val postal_addressline1: String,
    val postal_addressline2: String,
    val postal_city: String,
    val postal_country: String,
    val postal_pincode: String,
    val postal_state: String,
    val sameAddress: Boolean,
    val spectacle_given: Boolean,
    val spectacle_not_matching: Boolean,
    val spectacle_not_received: Boolean,
    val spectacle_not_matching_details:String,
    val given_presc_type:String,
    val user_id: String,
    val isSyn:Int=0

)
