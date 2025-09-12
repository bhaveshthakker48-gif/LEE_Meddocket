package org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientData

class OrthosisTypeListConvertor {

    @TypeConverter
    fun fromOrthosisTypeList(value: List<OrthosisPatientData>): String {
        val gson = Gson()
        val type = object : TypeToken<List<OrthosisPatientData>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toOrthosisTypeList(value: String): List<OrthosisPatientData> {
        val gson = Gson()
        val type = object : TypeToken<List<OrthosisPatientData>>() {}.type
        return gson.fromJson(value, type)
    }
}