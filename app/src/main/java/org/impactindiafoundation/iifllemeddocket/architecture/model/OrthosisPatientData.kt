package org.impactindiafoundation.iifllemeddocket.architecture.model

data class OrthosisPatientData(
    val id: Int,
    var amputationDate: String,
    var amputationSide: String, // Right/Left
    var amputationLevel: String,
    var amputationCause: String,
    var orthosis: OrthosisType,//object
    var otherOrthosis:String,
    var status: String,
    var statusNotes:String,
    var examinationDate:String,
    var fit_properly:String,
    var fit_properly_reason:String,
    var orthoFormId:Int,
    var patientOrthosisMeasurements: List<MeasurementPatientData>,
    var deleted: Boolean = false,
    var image:String,
    var orthosisImageList:MutableList<OrthosisImages>
)
