package org.impactindiafoundation.iifllemeddocket.Model.NewPrescriptionDataModel

import com.google.gson.annotations.SerializedName
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataLive

data class NewPrescriptionModel (
    @SerializedName("givenMedicines")
    val newPrescriptionList: List<sendNewPrescriptionData>
)


