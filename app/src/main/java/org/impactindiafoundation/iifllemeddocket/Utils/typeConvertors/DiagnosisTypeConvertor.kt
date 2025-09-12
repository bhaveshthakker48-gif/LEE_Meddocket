package org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType


class DiagnosisTypeConvertor {

    @TypeConverter
    fun fromDiagnosisType(value: DiagnosisType): String {
        val gson = Gson()
        val type = object : TypeToken<DiagnosisType>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toDiagnosisType(value: String): DiagnosisType {
        val gson = Gson()
        val type = object : TypeToken<DiagnosisType>() {}.type
        return gson.fromJson(value, type)
    }
}