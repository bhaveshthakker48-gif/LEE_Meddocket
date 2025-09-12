package org.impactindiafoundation.iifllemeddocket.Model.NewPrescriptionDataModel

data class NewPrescriptionDataResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val givenMedicine: List<GivenMedicine>,
    val success_count: Int
) {
    data class GivenMedicine(
        val ErrorCode: String,
        val ErrorMessage: String,
        val _id: Int
    )
}
