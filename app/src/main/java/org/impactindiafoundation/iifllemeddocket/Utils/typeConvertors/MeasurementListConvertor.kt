package org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.impactindiafoundation.iifllemeddocket.architecture.model.Measurement

class MeasurementListConvertor {

    @TypeConverter
    fun fromMeasurementList(value: List<Measurement>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Measurement>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMeasurementList(value: String): List<Measurement> {
        val gson = Gson()
        val type = object : TypeToken<List<Measurement>>() {}.type
        return gson.fromJson(value, type)
    }
}