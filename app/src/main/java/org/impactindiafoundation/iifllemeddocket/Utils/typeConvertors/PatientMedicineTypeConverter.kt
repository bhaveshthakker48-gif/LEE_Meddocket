package org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine

class PatientMedicineTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<PatientMedicine.PrescriptionItem> {
        val listType = object : TypeToken<List<PatientMedicine.PrescriptionItem>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<PatientMedicine.PrescriptionItem>): String {
        return gson.toJson(list)
    }
}