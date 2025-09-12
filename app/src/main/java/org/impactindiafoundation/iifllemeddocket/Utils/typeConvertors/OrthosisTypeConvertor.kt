package org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType

class OrthosisTypeConvertor {

    @TypeConverter
    fun fromOrthosisType(value: OrthosisType): String {
        val gson = Gson()
        val type = object : TypeToken<OrthosisType>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toOrthosisType(value: String): OrthosisType {
        val gson = Gson()
        val type = object : TypeToken<OrthosisType>() {}.type
        return gson.fromJson(value, type)
    }
}