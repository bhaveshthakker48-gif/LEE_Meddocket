package org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel

import com.google.gson.annotations.SerializedName
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PrescriptionGlassesFinal

data class AddPrecriptionFinal(
    @SerializedName("prescGlasses")
    val prescGlasses:List<PrescriptionGlasse>
)
