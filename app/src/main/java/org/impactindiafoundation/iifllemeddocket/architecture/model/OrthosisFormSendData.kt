package org.impactindiafoundation.iifllemeddocket.architecture.model

import com.google.gson.annotations.SerializedName
import org.impactindiafoundation.iifllemeddocket.Model.VitalsModel.Vitals

data class OrthosisFormSendData(
    @SerializedName("orthosis_patient_form")
    val orthosis_patient_form: List<Vitals>
)


