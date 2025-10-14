package org.impactindiafoundation.iifllemeddocket.ViewModel


import org.impactindiafoundation.iifllemeddocket.Data.RetrofitInstance
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.sendEyeOPDDoctorsNoteData
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOPNotes.AddEyePreOpNotesRequest
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel.AddEyePreOpInvestigationsRequest
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.EyePostAndFollowrequest
import org.impactindiafoundation.iifllemeddocket.Model.ImageModel.ImageUploadParams
import org.impactindiafoundation.iifllemeddocket.Model.ImagePrescriptionModel.UploadImagePrescriptionRequest
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.AddPrecriptionFinal
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.AddRefractiveErrorRequest
import org.impactindiafoundation.iifllemeddocket.Model.SurgicalNotesModel.SurgicalNotesRequest
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataModel

class LLE_MedDocketRespository() {

    suspend fun userLogin(loginRequest: String)=RetrofitInstance.localApi.login(loginRequest)
    suspend fun insertEyeOPDDoctorsNote(data: sendEyeOPDDoctorsNoteData)=RetrofitInstance.localApi.insertEyeOPDDoctorsNote(data)
    suspend fun insertEyePreOpInvestigation(addEyePreOpInvestigationsRequest: AddEyePreOpInvestigationsRequest)=RetrofitInstance.localApi.insertEyePreOpInvestigation(addEyePreOpInvestigationsRequest)
    suspend fun insertEyePreOpNotes(data: AddEyePreOpNotesRequest)=RetrofitInstance.localApi.insertEyePreOpNotes(data)
   suspend fun insertSurgicalNotes(data: SurgicalNotesRequest)=RetrofitInstance.localApi.insertSurgicalNotes(data)
   suspend fun insertEyePostOpAndFollowUps(data: EyePostAndFollowrequest)= RetrofitInstance.localApi.insertEyePostOpAndFollowUps(data)

  suspend  fun imageUpload1(imageUploadParams: ImageUploadParams)=RetrofitInstance.localApi.imageUpload(imageUploadParams.filePart,
      imageUploadParams.imageTypeRequestBody,imageUploadParams.patientIdRequestBody,imageUploadParams.campIdRequestBody,
      imageUploadParams.userIdRequestBody,imageUploadParams.idRequestBody)

    suspend fun imagePrescriptionUpload(imageUploadParams: UploadImagePrescriptionRequest)=RetrofitInstance.localApi.imagePrescriptionUpload(imageUploadParams.filePart,imageUploadParams.patientIdRequestBody,imageUploadParams.campIdRequestBody,
        imageUploadParams.userIdRequestBody,imageUploadParams.idRequestBody)

    suspend fun getPriscriptionDetails()=RetrofitInstance.localApi.getPriscriptionDetails()
    suspend fun getRegistrationDetails()=RetrofitInstance.localApi.getRegistrationDetails()
    suspend fun getPriscriptionGlassStatusDetails()=RetrofitInstance.localApi.getPriscriptionGlassStatusDetails()
   suspend fun insertAddPrecriptionGlasses(data: AddPrecriptionFinal) =RetrofitInstance.localApi.addPrescriptionGlassesDetail(data)
   suspend fun insertSynedData(data: SynedDataModel)=RetrofitInstance.localApi.insertSynedData(data)
   suspend fun getCurrentInventoryItems()=RetrofitInstance.localApi.getCurrentInventoryItems()
    suspend fun getInventoryUnits()=RetrofitInstance.localApi.getInventoryUnits()
}