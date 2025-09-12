package org.impactindiafoundation.iifllemeddocket.architecture.apiCall

import NewVisualAcuityRequest
import NewVitalsRequest
import OpdFormRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel.SendFinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel.sendFinalPrescriptionDrugResponse
import org.impactindiafoundation.iifllemeddocket.Model.OPD_InvestigationsModel.getOPD_Investigations_Response
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.NewRefractiveErrorRequest
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.getRefractiveErrorResponse
import org.impactindiafoundation.iifllemeddocket.Model.VisualAcuityModel.getVisualAcuityResponse
import org.impactindiafoundation.iifllemeddocket.Model.VitalsModel.getVitalsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisTypeModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImageSyncResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisFormSyncResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientFormMap
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteImpressionRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteImpressionResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteInvestigationRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteInvestigationResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteSymptomRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntImpressionType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPostOpNotesRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPostOpNotesResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpDetailsRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpDetailsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSurgicalNotesRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSurgicalNotesResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomEarType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomNoseType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomThroatType
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel.PathologyImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel.PathologyRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel.PathologyResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntAudiometryDetailsInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntAudiometryImageDetailsInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntDoctorInvestigationInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntImpressionInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntPathologyDetailsInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntPathologyImageDetailsInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntPostOpInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntPreOpImageDetailsInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntSurgicalInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntSymptomsInstructionsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.PreSurgeryInstructionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface APIClient {


    @GET("orthosis/for-app")
    suspend fun getOrthosisType(): Response<OrthosisTypeModel>

    @GET("patient_orthosis/lastActiveCampPatient")
    suspend fun getCampPatientDataApi(): Response<CampPatientData>

    @POST("patient_orthosis/addList3")
    suspend fun syncOrthosisPatientForNew(@Body formData: PatientFormMap): Response<OrthosisFormSyncResponse>

    @POST("patient_orthosis/updateOrthosisType")
    suspend fun syncCampPatientForNew(@Body formData: PatientFormMap): Response<OrthosisFormSyncResponse>

    @Multipart
    @POST("patient_orthosis_images/uploadPatientOrthosis")
    suspend fun syncFormImagesNew(
        @Part filePart: MultipartBody.Part,
        @Part("temp_patient_id")tempPatientIdRequestBody: RequestBody,
        @Part("camp_id")campIdRequestBody: RequestBody,
        @Part("patient_id")patientIdRequestBody: RequestBody,
        @Part("id")idRequestBody: RequestBody
    ): Response<FormImageSyncResponse>

    @Multipart
    @POST("patient_orthosis_images/uploadPatientOrthosisType")
    suspend fun syncOrthosisImagesNew(
        @Part filePart: MultipartBody.Part,
        @Part("temp_patient_id")tempPatientIdRequestBody: RequestBody,
        @Part("camp_id")campIdRequestBody: RequestBody,
        @Part("orthosis_id")orthosisIdRequestBody: RequestBody,
        @Part("amputation_side")amputationSideRequestBody: RequestBody,
        @Part("patient_id")patientIdRequestBody: RequestBody,
        @Part("id")idRequestBody: RequestBody
    ): Response<FormImageSyncResponse>

    @Multipart
    @POST("patient_orthosis_images/uploadPatientOrthosis")
    suspend fun syncEquipmentImagesNew(
        @Part filePart: MultipartBody.Part,
        @Part("temp_patient_id")tempPatientIdRequestBody: RequestBody,
        @Part("camp_id")campIdRequestBody: RequestBody,
        @Part("patient_id")patientIdRequestBody: RequestBody,
        @Part("patient_image_type")imageTypeRequestBody: RequestBody,
        @Part("id")idRequestBody: RequestBody
    ): Response<FormImageSyncResponse>

    @Multipart
    @POST("patient_orthosis_videos/uploadPatientOrthosis")
    suspend fun syncFormVideosNew(
        @Part filePart: MultipartBody.Part,
        @Part("temp_patient_id")tempPatientIdRequestBody: RequestBody,
        @Part("camp_id")campIdRequestBody: RequestBody,
        @Part("patient_id")patientIdRequestBody: RequestBody,
        @Part("id")idRequestBody: RequestBody
    ): Response<FormImageSyncResponse>

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddRefractiveError")
    suspend fun sendRefractiveToServer(@Body data: NewRefractiveErrorRequest):Response<getRefractiveErrorResponse>

    @GET("diagnosis/for-app")
    suspend fun getDiagnosisMaster(): Response<DiagnosisTypeModel>

    @GET("orthosis_equipment/for-app")
    suspend fun getOrthosisEquipmentMaster(): Response<EquipmentResponse>

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddVitals")
    suspend fun syncNewVitalsForm(@Body data: NewVitalsRequest):Response<getVitalsResponse>

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddOpdInvestigation")
    suspend fun syncNewOpdForm(@Body data: OpdFormRequest):Response<getOPD_Investigations_Response>

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddVisualActivity")
    suspend fun syncNewVisualAcuityForm(@Body data: NewVisualAcuityRequest):Response<getVisualAcuityResponse>

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddPrescription")
    suspend fun InsertFinalPrescriptionDrug(@Body data: SendFinalPrescriptionDrug):Response<sendFinalPrescriptionDrugResponse>

    companion object {
        const val FILE_UPLOAD = "${Constants.NEW_BASE_URL}upload-file"
    }

    // ENT
    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntSymptomEye")
    suspend fun getEntSymptomEar(): Response<EntSymptomEarType>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntSymptomNose")
    suspend fun getEntSymptomNose(): Response<EntSymptomNoseType>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntSymptomThroat")
    suspend fun getEntSymptomThroat(): Response<EntSymptomThroatType>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntImpression")
    suspend fun getEntImpression(): Response<EntImpressionType>

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddEntDoctorNoteSymptom")
    suspend fun sendEntDoctorNoteSymptoms(@Body request: EntDoctorNoteSymptomRequest): EntDoctorNoteResponse

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddEntDoctorNoteImpression")
    suspend fun sendEntDoctorNoteImpression(@Body request: EntDoctorNoteImpressionRequest): EntDoctorNoteImpressionResponse

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddEntDoctorNoteInvestigation")
    suspend fun sendEntDoctorNoteInvestigation(@Body request: EntDoctorNoteInvestigationRequest): EntDoctorNoteInvestigationResponse

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddEntPreOpDetail")
    suspend fun sendEntPreOpDetails(@Body request: EntPreOpDetailsRequest): EntPreOpDetailsResponse

    @Multipart
    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/UploadEntConsentForm")
    suspend fun uploadPreOpImage(
        @Part file: MultipartBody.Part,
        @Part("patientId") patientId: RequestBody,
        @Part("campId") campId: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("appCreatedDate") appCreatedDate: RequestBody,
        @Part("app_id") appId: RequestBody,
        @Part("uniqueId") uniqueId: RequestBody
    ): Response<EntPreOpImageResponse>


    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddEntSurgicalNote")
    suspend fun sendEntSurgicalNotes(@Body request: EntSurgicalNotesRequest): EntSurgicalNotesResponse

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddEntPostOpDetail")
    suspend fun sendEntPostOpNotes(@Body request: EntPostOpNotesRequest): EntPostOpNotesResponse

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddEntAudiometry")
    suspend fun sendEntAudiometry(@Body request: EntAudiometryRequest): EntAudiometryResponse

    @Multipart
    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/UploadEntAudiometryImage")
    suspend fun uploadAudiometryImage(
        @Part file: MultipartBody.Part,
        @Part("patientId") patientId: RequestBody,
        @Part("campId") campId: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("appCreatedDate") appCreatedDate: RequestBody,
        @Part("app_id") appId: RequestBody,
        @Part("uniqueId") uniqueId: RequestBody
    ): Response<EntAudiometryImageResponse>

    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/AddEntPathology")
    suspend fun sendPathology(@Body request: PathologyRequest): PathologyResponse

    @Multipart
    @POST("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/UploadEntPathologyImage")
    suspend fun uploadPathologyImage(
        @Part file: MultipartBody.Part,
        @Part("patientId") patientId: RequestBody,
        @Part("campId") campId: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("appCreatedDate") appCreatedDate: RequestBody,
        @Part("reportType") reportType: RequestBody,
        @Part("app_id") appId: RequestBody,
        @Part("uniqueId") uniqueId: RequestBody
    ): Response<PathologyImageResponse>

    //Get Ent Updated Data From Server
    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntPreOpDetails")
    suspend fun getUpdatePreOpDetailsFromServer(): Response<PreSurgeryInstructionResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntConsentForm")
    suspend fun getUpdatePreOpImageDetailsFromServer(): Response<EntPreOpImageDetailsInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/DownloadEntConsentForm")
    suspend fun downloadPreOpImage(
        @Query("fileName") fileName: String
    ): Response<ResponseBody>


    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntSurgicalNote")
    suspend fun getUpdateSurgicalNotesFromServer(): Response<EntSurgicalInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntPostOpNote")
    suspend fun getUpdatePostOpNotesFromServer(): Response<EntPostOpInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntAudiometry")
    suspend fun getUpdateAudiometryFromServer(): Response<EntAudiometryDetailsInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntAudiometryImages")
    suspend fun getUpdateAudiometryImageDetailsFromServer(): Response<EntAudiometryImageDetailsInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/DownloadEntAudiometryImage")
    suspend fun downloadAudiometryImage(
        @Query("fileName") fileName: String
    ): Response<ResponseBody>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntDoctorNoteInvestigations")
    suspend fun getUpdateDoctorInvestigationFromServer(): Response<EntDoctorInvestigationInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntDoctorNoteImpressions")
    suspend fun getUpdateImpressionFromServer(): Response<EntImpressionInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntDoctorNoteSymptoms")
    suspend fun getUpdateSymptomsFromServer(): Response<EntSymptomsInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntPathology")
    suspend fun getUpdatePathologyDetailsServer(): Response<EntPathologyDetailsInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/GetEntPathologyImages")
    suspend fun getUpdatePathologyImageDetailsFromServer(): Response<EntPathologyImageDetailsInstructionsResponse>

    @GET("https://impactindiafoundation.org/ImpactWebService/rest/LLEwebcall/DownloadEntPathologyImage")
    suspend fun downloadPathologyImage(
        @Query("fileName") fileName: String
    ): Response<ResponseBody>

}