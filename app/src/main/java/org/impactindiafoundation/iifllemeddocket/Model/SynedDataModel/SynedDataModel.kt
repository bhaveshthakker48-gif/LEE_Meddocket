package org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel

import com.google.gson.annotations.SerializedName
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable


data class SynedDataModel(
    @SerializedName("lleMedSyncReports")
    val synedDataList: List<SynedDataLive>
)