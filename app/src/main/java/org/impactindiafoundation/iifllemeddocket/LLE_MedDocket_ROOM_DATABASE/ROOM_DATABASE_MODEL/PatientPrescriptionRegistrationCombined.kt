package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL

data class PatientPrescriptionRegistrationCombined(
    val registrationData: Patient_RegistrationModel,
    val SpectacleDisdributionStatusModel: SpectacleDisdributionStatusModel,
    val prescription: Prescription_Model?,
    var counts: SpectacleCountModel = SpectacleCountModel(0, 0, 0, 0, 0,0,0,0,0)

)



