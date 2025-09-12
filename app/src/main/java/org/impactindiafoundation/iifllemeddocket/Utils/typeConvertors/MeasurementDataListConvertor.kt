package org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.impactindiafoundation.iifllemeddocket.architecture.model.MeasurementPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientData

class MeasurementDataListConvertor {

    @TypeConverter
    fun fromOrthosisTypeList(value: List<MeasurementPatientData>): String {
        val gson = Gson()
        val type = object : TypeToken<List<MeasurementPatientData>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toOrthosisTypeList(value: String): List<MeasurementPatientData> {
        val gson = Gson()
        val type = object : TypeToken<List<MeasurementPatientData>>() {}.type
        return gson.fromJson(value, type)
    }
}