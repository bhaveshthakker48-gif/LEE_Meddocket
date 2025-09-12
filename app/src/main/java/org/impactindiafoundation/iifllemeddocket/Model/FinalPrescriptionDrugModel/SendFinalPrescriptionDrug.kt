package org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel

import com.google.gson.annotations.SerializedName
import org.impactindiafoundation.iifllemeddocket.Model.NewPrescriptionDataModel.sendNewPrescriptionData

data class SendFinalPrescriptionDrug(
    @SerializedName("prescriptions")
    val newPrescriptionList: List<FinalPrescriptionDrugServer>

)