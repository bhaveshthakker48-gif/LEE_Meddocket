package org.impactindiafoundation.iifllemeddocket.architecture.model

data class MeasurementPatientData(
    val id: Int,//need to send 0
    var orthosisMeasurement: Measurement,
    var value: Double,
    var unit:String,
    var otherMeasurement:String,
    val deleted: Boolean = false
)