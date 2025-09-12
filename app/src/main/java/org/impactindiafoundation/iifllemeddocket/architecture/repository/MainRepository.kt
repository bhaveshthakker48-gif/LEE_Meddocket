package org.impactindiafoundation.iifllemeddocket.architecture.repository

import NewVisualAcuityRequest
import NewVitalsRequest
import OpdFormRequest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.VisualAcuity_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CurrentInventoryLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel.SendFinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.NewRefractiveErrorRequest
import org.impactindiafoundation.iifllemeddocket.architecture.apiCall.APIClient
import org.impactindiafoundation.iifllemeddocket.architecture.dao.CurrentInventoryDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OPDFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OpdSyncDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.PatientReportDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.PrescriptionsDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.RefractiveErrorFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.VisualAcuityFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.VitalsFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.database.CampDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.CampPatientDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.OrthosisFileDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.OrthosisFormDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.UserDatabase
import org.impactindiafoundation.iifllemeddocket.architecture.helper.FileUploader
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.Equipment
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImage
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideoRquest
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideos
import org.impactindiafoundation.iifllemeddocket.architecture.model.ImageSyncResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.OpdSyncTable
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientFormMap
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntImpressionType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomEarType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomNoseType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomThroatType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntEarType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntNoseType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntThroatType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.ImpressionType
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class NewMainRepository @Inject constructor(
    private val apiHelper: APIClient,
    private val userDatabase: UserDatabase,
    private val orthosisFormDataBase: OrthosisFormDataBase,
    private val campPatientDataBase: CampPatientDataBase,
    private val orthosisFileDataBase: OrthosisFileDataBase,
    private val campDataBase: CampDataBase,
    private val currentInventoryDao: CurrentInventoryDao,
    private val refractiveFormDao: RefractiveErrorFormDao,
    private val vitalsFormDao: VitalsFormDao,
    private val opdFormDao: OPDFormDao,
    private val visualAcuityFormDao: VisualAcuityFormDao,
    private val patietnReportDao: PatientReportDao,
    private val opdPrescriptionsDao: PrescriptionsDao,
    private val opdSyncDao: OpdSyncDao,
) {

    suspend fun getAllUsers() = userDatabase.userDao().getAll()
    fun insertAllUser(userList: UserModel) = userDatabase.userDao().insertAll(userList)

    //orthosis master operations
    suspend fun getOrthosisType() = apiHelper.getOrthosisType()

    suspend fun getOrthosisMaster() = userDatabase.orthosisMasterDao().getOrthosisMaster()
    fun insertOrthosisMasterr(orthosisMaster: OrthosisType) =
        userDatabase.orthosisMasterDao().insertOrthosisMaster(orthosisMaster)

    //orthosis patient form operations
    fun insertOrthosisForm(orthosisPatientForm: OrthosisPatientForm) =
        orthosisFormDataBase.orthosisFormDao().insertOrthosisForm(orthosisPatientForm)

    suspend fun getOrthosisPatientForm() =
        orthosisFormDataBase.orthosisFormDao().getOrthosisPatientForm()

    fun updateSyncedForms(syncedForms: List<Int>) =
        orthosisFormDataBase.orthosisFormDao().updateSyncedForms(syncedForms)

    fun getOrthosisPatientFormById(localPatientId: Int) =
        orthosisFormDataBase.orthosisFormDao().getOrthosisPatientFormById(localPatientId)

    fun getOrthosisPatientFormByTempId(tempId: Int) =
        orthosisFormDataBase.orthosisFormDao().getOrthosisPatientFormByTempId(tempId)

    suspend fun syncOrthosisPatientForNew(formData: PatientFormMap) =
        apiHelper.syncOrthosisPatientForNew(formData)

    suspend fun syncCampPatientForNew(formData: PatientFormMap) =
        apiHelper.syncCampPatientForNew(formData)

    suspend fun syncFormImagesNew(formImageRequest: FormImageRequest) = apiHelper.syncFormImagesNew(
        formImageRequest.filePart,
        formImageRequest.tempPatientIdRequestBody,
        formImageRequest.campIdRequestBody,
        formImageRequest.patientIdRequestBody,
        formImageRequest.idRequestBody
    )

    suspend fun syncOrthosisImagesNew(formImageRequest: OrthosisImageRequest) =
        apiHelper.syncOrthosisImagesNew(
            formImageRequest.filePart,
            formImageRequest.tempPatientIdRequestBody,
            formImageRequest.campIdRequestBody,
            formImageRequest.orthosisIdRequestBody,
            formImageRequest.amputationsSideRequestBody,
            formImageRequest.patientIdRequestBody,
            formImageRequest.idRequestBody
        )

    suspend fun syncEquipmentImagesNew(formImageRequest: EquipmentImageRequest) =
        apiHelper.syncEquipmentImagesNew(
            formImageRequest.filePart,
            formImageRequest.tempPatientIdRequestBody,
            formImageRequest.campIdRequestBody,
            formImageRequest.patientIdRequestBody,
            formImageRequest.imageTypeRequestBody,
            formImageRequest.idRequestBody
        )

    suspend fun syncFormVideosNew(formImageRequest: FormVideoRquest) = apiHelper.syncFormVideosNew(
        formImageRequest.filePart,
        formImageRequest.tempPatientIdRequestBody,
        formImageRequest.campIdRequestBody,
        formImageRequest.patientIdRequestBody,
        formImageRequest.idRequestBody
    )

    suspend fun getCampPatientDetailsApi() = apiHelper.getCampPatientDataApi()

    //camp patient details operations
    suspend fun insertCampPatientDetails(campPatientList: List<CampPatientDataItem>) =
        campPatientDataBase.campPatientDao().insertCampPatient(campPatientList)

    suspend fun insertSingleCampPatient(campPatient: CampPatientDataItem) =
        campPatientDataBase.campPatientDao().insertSingleCampPatient(campPatient)

    suspend fun getCampPatientDetailsDb() =
        campPatientDataBase.campPatientDao().getCampPatientList()

    suspend fun getCampPatientListById(tempId: Int) =
        campPatientDataBase.campPatientDao().getCampPatientListById(tempId)

    //orthosis files operations


    //form image
    suspend fun getFormImages() = orthosisFileDataBase.orthosisFileDao().getFormImage()
    suspend fun getFormImagesForSync() = orthosisFileDataBase.orthosisFileDao().getFormImage()
    suspend fun insertFormImage(formImage: FormImages) =
        orthosisFileDataBase.orthosisFileDao().insertFormImage(formImage)

    fun updateSyncedImages(syncedImages: List<Int>) =
        orthosisFileDataBase.orthosisFileDao().updateSyncedImages(syncedImages)

    suspend fun updateSyncedImage(syncedImage: Int) =
        orthosisFileDataBase.orthosisFileDao().updateSyncedImage(syncedImage)

    suspend fun insertFormImageList(formImageList: List<FormImages>) =
        orthosisFileDataBase.orthosisFileDao().insertFormImageList(formImageList)

    suspend fun deleteFormImages(formImageList: List<FormImages>) =
        orthosisFileDataBase.orthosisFileDao().deleteFormImages(formImageList)

    suspend fun deleteFormImagesById(formImageList: List<Int>) =
        orthosisFileDataBase.orthosisFileDao().deleteFormImagesById(formImageList)

    suspend fun getFormImageListByFormId(formId: Int) =
        orthosisFileDataBase.orthosisFileDao().getFormImageList(formId)


    //orthosis image
    suspend fun insertOrthosisImageList(imageList: List<OrthosisImages>) =
        orthosisFileDataBase.orthosisFileDao().insertOrthosisImageList(imageList)

    suspend fun getFormOrthosisImages() = orthosisFileDataBase.orthosisFileDao().getOrthosisImage()
    suspend fun getOrthosisImageByFormId(formId: String) =
        orthosisFileDataBase.orthosisFileDao().getOrthosisImageByFormId(formId)

    suspend fun updateSyncedOrthoImage(syncedImage: Int) =
        orthosisFileDataBase.orthosisFileDao().updateSyncedOrthoImage(syncedImage)

    suspend fun deleteOrthosisImages(orthosisImagesList: List<OrthosisImages>) =
        orthosisFileDataBase.orthosisFileDao().deleteOrthosisImages(orthosisImagesList)

    //form videos

    suspend fun insertFormVideoList(formVideoList: List<FormVideos>) =
        orthosisFileDataBase.orthosisFileDao().insertFormVideoList(formVideoList)

    suspend fun getFormVideos() = orthosisFileDataBase.orthosisFileDao().getFormVideo()
    suspend fun getFormVideosById(formId: Int) =
        orthosisFileDataBase.orthosisFileDao().getFormVideoById(formId)

    suspend fun updateSyncedVideo(syncedVideo: Int) =
        orthosisFileDataBase.orthosisFileDao().updateSyncedVideo(syncedVideo)

    suspend fun deleteFormVideosById(formVideosId: List<Int>) =
        orthosisFileDataBase.orthosisFileDao().deleteFormVideosById(formVideosId)


    fun uploadFile(
        context: Context,
        uriMap: Map<String, Uri>,
        fileMap: Map<String, File>,
        dataMap: Map<String, String>,
        url: String,
        type: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        // uploadFileResponse.postValue(Resource.loading(null))
        FileUploader.uploadFiles(context,
            url,
            uriMap,
            fileMap,
            dataMap,
            type,
            object : FileUploader.UploadCallback {
                override fun onFileUploadError(errorMessage: String?) {
                    // uploadFileResponse.postValue(Resource.error(errorMessage!!, null))
                }

                override fun onAllFilesUploaded(string: String) {
                    Log.d("File upload", string)
                    string?.let {
                        try {
                            // Parse the JSON response
                            val gson = Gson()
                            val data = gson.fromJson(it, ImageSyncResponse::class.java)

                            if (data.success_id != 0) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    updateSyncedImage(data.success_id)
                                }

                                //uploadFileResponse.postValue(Resource.success(data))
                            } else {
                                // uploadFileResponse.postValue(Resource.error("Upload failed with success_id: ${data.success_id}", null))
                            }

                        } catch (e: Exception) {
                            Log.e("ERROR", e.message!!)
                            //   uploadFileResponse.postValue(Resource.error("Failed to parse response", null))
                        }
                    } ?: run {
                        //  uploadFileResponse.postValue(Resource.error("Server Error", null))
                    }
                }
            })
    }

    fun uploadVideoFile(
        context: Context,
        uriMap: Map<String, Uri>,
        fileMap: Map<String, File>,
        dataMap: Map<String, String>,
        url: String,
        type: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        // uploadFileResponse.postValue(Resource.loading(null))
        FileUploader.uploadFiles(context,
            url,
            uriMap,
            fileMap,
            dataMap,
            type,
            object : FileUploader.UploadCallback {
                override fun onFileUploadError(errorMessage: String?) {
                    // uploadFileResponse.postValue(Resource.error(errorMessage!!, null))
                    Log.e("ERROR", errorMessage ?: "Unknown network error")
                }

                override fun onAllFilesUploaded(string: String) {
                    Log.d("File upload", string)
                    string?.let {
                        try {
                            // Parse the JSON response
                            val gson = Gson()
                            val data = gson.fromJson(it, ImageSyncResponse::class.java)

                            if (data.success_id != 0) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    updateSyncedVideo(data.success_id)
                                }

                            } else {

                            }

                        } catch (e: Exception) {
                            Log.e("ERROR", e.message!!)
                        }
                    } ?: run {
                        Log.e("ERROR", "Server Error")
                    }
                }
            })
    }

    fun uploadOrthoImageFile(
        context: Context,
        uriMap: Map<String, Uri>,
        fileMap: Map<String, File>,
        dataMap: Map<String, String>,
        url: String,
        type: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        // uploadFileResponse.postValue(Resource.loading(null))
        FileUploader.uploadFiles(context,
            url,
            uriMap,
            fileMap,
            dataMap,
            type,
            object : FileUploader.UploadCallback {
                override fun onFileUploadError(errorMessage: String?) {
                    // uploadFileResponse.postValue(Resource.error(errorMessage!!, null))
                    Log.e("ERROR", errorMessage ?: "Unknown network error")
                }

                override fun onAllFilesUploaded(string: String) {
                    Log.d("File upload", string)
                    string?.let {
                        try {
                            // Parse the JSON response
                            val gson = Gson()
                            val data = gson.fromJson(it, ImageSyncResponse::class.java)

                            if (data.success_id != 0) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    updateSyncedOrthoImage(data.success_id)
                                }

                            } else {

                            }

                        } catch (e: Exception) {
                            Log.e("ERROR", e.message!!)
                        }
                    } ?: run {
                        Log.e("ERROR", "Server Error")
                    }
                }
            })
    }


    fun uploadEquipmentImageFile(
        context: Context,
        uriMap: Map<String, Uri>,
        fileMap: Map<String, File>,
        dataMap: Map<String, String>,
        url: String,
        type: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        // uploadFileResponse.postValue(Resource.loading(null))
        FileUploader.uploadFiles(context,
            url,
            uriMap,
            fileMap,
            dataMap,
            type,
            object : FileUploader.UploadCallback {
                override fun onFileUploadError(errorMessage: String?) {
                    // uploadFileResponse.postValue(Resource.error(errorMessage!!, null))
                    Log.e("ERROR", errorMessage ?: "Unknown network error")
                }

                override fun onAllFilesUploaded(string: String) {
                    Log.d("File upload", string)
                    string?.let {
                        try {
                            // Parse the JSON response
                            val gson = Gson()
                            val data = gson.fromJson(it, ImageSyncResponse::class.java)

                            if (data.success_id != 0) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    updateSyncedEquipmentImage(data.success_id)
                                }

                            } else {

                            }

                        } catch (e: Exception) {
                            Log.e("ERROR", e.message!!)
                        }
                    } ?: run {
                        Log.e("ERROR", "Server Error")
                    }
                }
            })
    }

    //camp db details operations
    suspend fun insertCampDetails(campList: List<CampModel>) =
        campDataBase.campDao().insertCampDetail(campList)

    suspend fun updateSingleCamp(camp: CampModel) = campDataBase.campDao().updateSingleCamp(camp)

    suspend fun getCampList() = campDataBase.campDao().getCampList()

    //Diagnosis Master
    suspend fun getDiagnosisMaster() = apiHelper.getDiagnosisMaster()

    fun insertDiagnosisMaster(diagnosisMaster: DiagnosisType) =
        userDatabase.diagnosisMasterDao().insertDiagnosisMaster(diagnosisMaster)

    suspend fun getDiagnosisMasterLocal() = userDatabase.diagnosisMasterDao().getDiagnosisMaster()


    //Inventory Master


    //refractive error form operations

    val allRefractive_Error: LiveData<List<RefractiveError>> =
        refractiveFormDao.getAllRefractiveErrorData()

    val uniqueRefractivePatientCount: LiveData<Int> = refractiveFormDao.getUniquePatientCount()


    fun insertRefractiveForm(refractiveForm: RefractiveError) =
        refractiveFormDao.insertRefractiveForm(refractiveForm)

    fun getRefractiveFormById(localPatientId: Int) =
        refractiveFormDao.getRefractiveFormById(localPatientId)

    suspend fun getRefractiveForm() = refractiveFormDao.getRefractiveForm()
    fun updateSyncedRefractiveForms(syncedForms: List<Int>) =
        refractiveFormDao.updateRefractiveForms(syncedForms)

    suspend fun syncRefractiveErrorForm(data: NewRefractiveErrorRequest) =
        apiHelper.sendRefractiveToServer(data)

    //orthosis equipment master
    suspend fun getOrthosisEquipmentMaster() = apiHelper.getOrthosisEquipmentMaster()

    suspend fun getOrthosisEquipmentMasterLocal() =
        userDatabase.orthosisEquipmentMasterDao().getEquipmentMaster()

    fun insertOrthosisEquipmentMaster(equipmentMaster: Equipment) =
        userDatabase.orthosisEquipmentMasterDao().insertEquipmentMaster(equipmentMaster)


//    suspend fun insertRefractiveError(refractiveError: RefractiveError) = oldRefractiveDao.insertRefractiveError(refractiveError)

    //orthosis equipment image
    suspend fun insertEquipmentImageList(imageList: List<EquipmentImage>) =
        orthosisFileDataBase.orthosisFileDao().insertEquipmentImageList(imageList)

    suspend fun getEquipmentImage() = orthosisFileDataBase.orthosisFileDao().getEquipmentImage()
    suspend fun getEquipmentImageByFormId(formId: String) =
        orthosisFileDataBase.orthosisFileDao().getEquipmentImageByFormId(formId)

    suspend fun updateSyncedEquipmentImage(syncedVideo: Int) =
        orthosisFileDataBase.orthosisFileDao().updateSyncedEquipmentImage(syncedVideo)

    suspend fun deleteEquipmentImage(image: String) =
        orthosisFileDataBase.orthosisFileDao().deleteEquipmentImage(image)

    //Vitals form operations

    val allVitals: LiveData<List<Vitals>> = vitalsFormDao.getAllVitailsData()

    val uniqueVitalPatientCount: LiveData<Int> = vitalsFormDao.getUniquePatientCount()


    fun insertVitalsForm(vitalsForm: Vitals) =
        vitalsFormDao.insertVitalsForm(vitalsForm)

    suspend fun getVitalsForm() = vitalsFormDao.getVitalsForm()
    fun updateVitalsForms(syncedForms: List<Int>) =
        vitalsFormDao.updateVitalsForms(syncedForms)

    fun getVitalsFormById(localPatientId: Int) =
        vitalsFormDao.getVitalsFormById(localPatientId)

    suspend fun syncNewVitalsForm(data: NewVitalsRequest) =
        apiHelper.syncNewVitalsForm(data)


    //OPD Investigations form operations
    val allOPD_Investigations: LiveData<List<OPD_Investigations>> =
        opdFormDao.getAllOPD_InvestigationsData()

    val uniqueOpdInestigationPatientCount: LiveData<Int> = opdFormDao.getUniquePatientCount()

    fun insertOpdForm(opdForm: OPD_Investigations) =
        opdFormDao.insertOpdForm(opdForm)

    suspend fun getOpdForm() = opdFormDao.getOpdForm()
    fun updateOpdForms(syncedForms: List<Int>) =
        opdFormDao.updateOpdForms(syncedForms)

    fun getOpdFormById(localPatientId: Int) =
        opdFormDao.getOpdFormById(localPatientId)

    suspend fun syncNewOpdForm(data: OpdFormRequest) =
        apiHelper.syncNewOpdForm(data)

    //Visual Acuity form operations
    val allVisualAcuity: LiveData<List<VisualAcuity>> = visualAcuityFormDao.getAllVisualAcuityData()

    val uniqueVisualPatientCount: LiveData<Int> = visualAcuityFormDao.getUniquePatientCount()


    fun insertVisualAcuityForm(visualAcuityForm: VisualAcuity) =
        visualAcuityFormDao.insertVisualAcuityForm(visualAcuityForm)

    suspend fun getVisualAcuityForm() = visualAcuityFormDao.getVisualAcuityForm()
    fun updateVisualAcuityForms(syncedForms: List<Int>) =
        visualAcuityFormDao.updateVisualAcuityForms(syncedForms)

    fun getVisualAcuityFormById(localPatientId: Int) =
        visualAcuityFormDao.getVisualAcuityFormById(localPatientId)

    suspend fun syncNewVisualAcuityForm(data: NewVisualAcuityRequest) =
        apiHelper.syncNewVisualAcuityForm(data)


    //Patient Report operations
    fun insertPatientReport(patientReport: PatientReport) =
        patietnReportDao.insertPatientReport(patientReport)

    suspend fun getPatientReport() = patietnReportDao.getPatientReport()

    fun updatePatientForms(syncedForms: List<Int>, formType: String) =
        patietnReportDao.updatePatientForms(syncedForms, formType)

    //OPD Prescription Form Database Operations
    suspend fun insertFinalPrescriptionDrug(prescription: PatientMedicine) = opdPrescriptionsDao.insertFinalPrescriptionDrug(prescription)

    val unsyncedFormsCount: LiveData<Int> = opdPrescriptionsDao.getUnsyncedFormsCount()


    suspend fun updatePrescription(prescription: PatientMedicine) {
        opdPrescriptionsDao.updatePrescription(prescription)
    }
    suspend fun getPatientMedicineReport() = opdPrescriptionsDao.getAllFinalPrescriptionDrugs()
    suspend fun getFinalPrescriptionByFormId(opdFormId:Int) = opdPrescriptionsDao.getFinalPrescriptionByFormId(opdFormId)

    suspend fun InsertFinalPrescriptionDrug(data: SendFinalPrescriptionDrug)= apiHelper.InsertFinalPrescriptionDrug(data)

    suspend fun updateFinalPrescriptionDrug1(id: Int, newIsSyn: Int) = opdPrescriptionsDao.updateFinalPrescriptionDrug1(id, newIsSyn)

    //OPD Sync Table Database Operations

    suspend fun insertOpdSyncTable(opdSyncItem: OpdSyncTable) = opdSyncDao.insertOpdSyncTable(opdSyncItem)

    suspend fun getOpdSyncTable() = opdSyncDao.getOpdSyncTable()


    // ENT
    // eye symptoms
    suspend fun getEntSymptomEar(): Response<EntSymptomEarType> = apiHelper.getEntSymptomEar()

    suspend fun insertEntSymptomEar(entEarType: EntEarType): Long {
        return userDatabase.entEarSymptomsDao().insertEntEarType(entEarType)
    }

    // nose symptoms
    suspend fun getEntSymptomNose(): Response<EntSymptomNoseType> = apiHelper.getEntSymptomNose()

    suspend fun insertEntSymptomNose(entNoseType: EntNoseType): Long {
        return userDatabase.entNoseSymptomsDao().insertEntNoseType(entNoseType)
    }

    // throat symptoms
    suspend fun getEntSymptomThroat(): Response<EntSymptomThroatType> = apiHelper.getEntSymptomThroat()

    suspend fun insertEntSymptomThroat(entNoseType: EntThroatType): Long {
        return userDatabase.entThroatSymptomsDao().insertEntThroatType(entNoseType)
    }

    suspend fun getEntImpression(): Response<EntImpressionType> = apiHelper.getEntImpression()

    suspend fun insertEntImpression(impressionType: ImpressionType): Long {
        return userDatabase.entImpressionDao().insertEntImpression(impressionType)
    }


}