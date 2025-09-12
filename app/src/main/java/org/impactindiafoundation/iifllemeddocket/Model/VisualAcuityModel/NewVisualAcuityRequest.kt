

import com.google.gson.annotations.SerializedName
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity

data class NewVisualAcuityRequest(
    @SerializedName("visualActivities")
    val visualAcuityList: List<VisualAcuity>
)
