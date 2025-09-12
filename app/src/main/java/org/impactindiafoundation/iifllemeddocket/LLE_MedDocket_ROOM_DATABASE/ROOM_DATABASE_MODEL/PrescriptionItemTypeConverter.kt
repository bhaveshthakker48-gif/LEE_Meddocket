package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrescriptionItemTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<FinalPrescriptionDrug.PrescriptionItem> {
        val listType = object : TypeToken<List<FinalPrescriptionDrug.PrescriptionItem>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<FinalPrescriptionDrug.PrescriptionItem>): String {
        return gson.toJson(list)
    }
}
