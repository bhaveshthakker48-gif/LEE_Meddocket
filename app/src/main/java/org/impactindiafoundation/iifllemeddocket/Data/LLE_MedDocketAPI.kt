package org.impactindiafoundation.iifllemeddocket.Data


import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.impactindiafoundation.iifllemeddocket.Model.CurrentInvetoryItem.getCurrentInvetoryItem
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.getResponseEyeOPDDoctorsNote
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.sendEyeOPDDoctorsNoteData
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOPNotes.AddEyePreOpNotesRequest
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOPNotes.AddEyePreOpNotesResponse
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel.AddEyePreOpInvestigationsRequest
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel.getEyePreOpInvestigationResponse
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.EyePostAndFollowrequest
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.getEyePostAndFollowUpResponse
import org.impactindiafoundation.iifllemeddocket.Model.GetInventoryUnits.GetInventoryUnits
import org.impactindiafoundation.iifllemeddocket.Model.ImageModel.getImageUploadResponse
import org.impactindiafoundation.iifllemeddocket.Model.ImagePrescriptionModel.UploadImagePrescriptionResponse
import org.impactindiafoundation.iifllemeddocket.Model.Login.LoginResponse
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.AddPrecriptionFinal
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.PrescriptionDetailsModel
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.PrescriptionGlassStatusDetails
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.addPrecriptionGlassesResponse
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.AddRefractiveErrorRequest
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.getRefractiveErrorResponse
import org.impactindiafoundation.iifllemeddocket.Model.RegistrationModel.RegistrationDetailsModel
import org.impactindiafoundation.iifllemeddocket.Model.SurgicalNotesModel.SurgicalNotesRequest
import org.impactindiafoundation.iifllemeddocket.Model.SurgicalNotesModel.getSurgicalNotesResponse
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataModel
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface LLE_MedDocketAPI {
    @GET("GetLogindetail")
    suspend fun login(@Query("request_body") requestBody: String): Response<LoginResponse>

    @POST("AddEyeOpdDocNotes")
    suspend fun insertEyeOPDDoctorsNote(@Body data: sendEyeOPDDoctorsNoteData): Response<getResponseEyeOPDDoctorsNote>

    @POST("AddEyePreOpInvestigation")
   suspend fun insertEyePreOpInvestigation(@Body addEyePreOpInvestigationsRequest: AddEyePreOpInvestigationsRequest):Response<getEyePreOpInvestigationResponse>

    @POST("AddEyePreOpNotes")
    suspend fun insertEyePreOpNotes(@Body data: AddEyePreOpNotesRequest):Response<AddEyePreOpNotesResponse>

    @POST("AddSurgicalNotes")
    suspend fun insertSurgicalNotes(@Body data: SurgicalNotesRequest):Response<getSurgicalNotesResponse>

    @POST("AddEyePostOp")
    suspend fun insertEyePostOpAndFollowUps(@Body data: EyePostAndFollowrequest): Response<getEyePostAndFollowUpResponse>

    @POST("AddRefractiveError")
    suspend fun insertRefractiveError(@Body data: AddRefractiveErrorRequest):Response<getRefractiveErrorResponse>

    @Multipart
    @POST("UploadLLEImage")
    suspend fun imageUpload(
        @Part file: MultipartBody.Part,
        @Part("image_type")image_type:RequestBody,
        @Part("patient_id")patient_id:RequestBody,
        @Part("camp_id")camp_id:RequestBody,
        @Part("user_id")user_id:RequestBody,
        @Part("_id")_id:RequestBody,



        ): Response<getImageUploadResponse>

    @GET("GetPrescriptionDetails")
    suspend fun getPriscriptionDetails():Response<PrescriptionDetailsModel>

    @GET("GetLleRegistrationdetail")
    suspend fun getRegistrationDetails():Response<RegistrationDetailsModel>

    @GET("GetPrescriptionGlasses")
    suspend fun getPriscriptionGlassStatusDetails():Response<PrescriptionGlassStatusDetails>

    @POST("addPrescriptionGlassesDetail")
    suspend fun addPrescriptionGlassesDetail(@Body data: AddPrecriptionFinal):Response<addPrecriptionGlassesResponse>

    @POST("addLleMeddocketSyncReport")
    suspend fun insertSynedData(@Body data: SynedDataModel):Response<SynedDataResponse>

    @GET("GetCurrentInventoryItems")
    suspend fun getCurrentInventoryItems():Response<getCurrentInvetoryItem>

    @GET("GetInventoryUnits")
   suspend fun getInventoryUnits():Response<GetInventoryUnits>

    @Multipart
    @POST("UploadPrescriptionImage")
    suspend fun imagePrescriptionUpload(
         @Part filePart: MultipartBody.Part,
                                 @Part("patient_id")patientIdRequestBody: RequestBody,
                                 @Part("camp_id")campIdRequestBody: RequestBody,
                                 @Part("user_id")userIdRequestBody: RequestBody,
                                 @Part("_id")idRequestBody: RequestBody):Response<UploadImagePrescriptionResponse>


}