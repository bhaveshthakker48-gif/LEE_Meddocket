

import com.google.gson.annotations.SerializedName
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals

data class NewVitalsRequest(
    @SerializedName("vital")
    val vitalList: List<Vitals>
)