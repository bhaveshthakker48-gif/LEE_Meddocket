package org.impactindiafoundation.iifllemeddocket.architecture.model

import com.google.gson.annotations.SerializedName
import org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel.FinalPrescriptionDrugServer

data class PrescriptionRequest(
    @SerializedName("prescriptions")
    val newPrescriptionList: List<PatientMedicine>

)