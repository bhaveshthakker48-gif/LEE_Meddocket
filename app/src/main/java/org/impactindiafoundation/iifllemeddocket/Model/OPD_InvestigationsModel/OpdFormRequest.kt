
import com.google.gson.annotations.SerializedName
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations


data class OpdFormRequest (
    @SerializedName("opd_Investigationses")
    val opdInvestigationsList: List<OPD_Investigations>
)