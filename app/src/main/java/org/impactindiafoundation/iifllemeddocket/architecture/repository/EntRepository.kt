package org.impactindiafoundation.iifllemeddocket.architecture.repository

import android.util.Log
import androidx.lifecycle.LiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Post_Op_AND_Follow_ups
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImageModel
import org.impactindiafoundation.iifllemeddocket.architecture.apiCall.APIClient
import org.impactindiafoundation.iifllemeddocket.architecture.database.EntDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.UserDatabase
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteImpressionItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteImpressionRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteImpressionResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteInvestigationItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteInvestigationRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteInvestigationResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteSymptomItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntDoctorNoteSymptomRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntImpressionType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPostOpNotesItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPostOpNotesRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPostOpNotesResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpDetailsItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpDetailsRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpDetailsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSurgicalNotesItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSurgicalNotesRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSurgicalNotesResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntEarType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntImpressionEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntNoseType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntSymptomsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntThroatType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.ImpressionType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.PreOpImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel.PathologyItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel.PathologyRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel.PathologyImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel.PathologyResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.DoctorInvestigationInstruction
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
import javax.inject.Inject

class EntRepository @Inject constructor(
    private val apiClient: APIClient,
    private val dataBase: UserDatabase,
    private val entDataBase: EntDataBase
) {
    suspend fun getEarSymptoms(): List<EntEarType> = dataBase.entEarSymptomsDao().getEntEarType()

    suspend fun getNoseSymptoms(): List<EntNoseType> =
        dataBase.entNoseSymptomsDao().getEntNoseType()

    suspend fun getThroatSymptoms(): List<EntThroatType> =
        dataBase.entThroatSymptomsDao().getEntThroatType()

    suspend fun getEntImpression(): List<ImpressionType> =
        dataBase.entImpressionDao().getEntImpressionType()

    //EntOpdDoctorsNoteViewModel
    //Symptoms

    val allSymptoms: LiveData<List<EntSymptomsEntity>> =
        entDataBase.entSymptomsDao().allSymptoms()

    fun insertSymptom(symptom: EntSymptomsEntity)  =
        entDataBase.entSymptomsDao().insert(symptom)

    suspend fun getSymptomsListById(intentFormId: Int) =
        entDataBase.entSymptomsDao().getSymptomsListById(intentFormId)

    suspend fun getSymptomsByFormId(formId: Int): List<EntSymptomsEntity> {
        return entDataBase.entSymptomsDao().getSymptomsByFormId(formId)
    }

    suspend fun deleteSymptom(symptom: EntSymptomsEntity) {
        entDataBase.entSymptomsDao().deleteSymptom(symptom)
    }
    suspend fun getUnsyncedSymptomsNow(): List<EntSymptomsEntity> {
        return entDataBase.entSymptomsDao().getUnSyncedSymptomsNow()
    }

    suspend fun updateSymptomsAppId(id: Int, appId: String) {
        Log.d("SyncCheck", "Calling DAO to update appId=$appId for id=$id")
        entDataBase.entSymptomsDao().updateSymptomsAppId(id, appId)
    }

    suspend fun sendDoctorNoteSymptomsToServer(symptoms: List<EntSymptomsEntity>): EntDoctorNoteResponse {
        val requestItems = symptoms.map {
            EntDoctorNoteSymptomItem(
                uniqueId = it.uniqueId,
                formId = it.formId,
                patientId = it.patientId,
                campId = it.campId,
                userId = it.userId,
                organ = it.organ,
                part = it.part,
                symptom = it.symptom ?: "",
                symptomId = it.symptomId,
                appCreatedDate = it.appCreatedDate
            )
        }
        val request = EntDoctorNoteSymptomRequest(requestItems)
        return apiClient.sendEntDoctorNoteSymptoms(request)
    }

    suspend fun getUpdateSymptomsFromServer(): Response<EntSymptomsInstructionsResponse> = apiClient.getUpdateSymptomsFromServer()

    suspend fun getAllSymptoms(): List<EntSymptomsEntity> {
        return entDataBase.entSymptomsDao().getAllSymptoms()
    }

    suspend fun getSymptomsByPatientAndCamp(
        patientId: Int,
        campId: Int,
        uniqueId: Int,
        formId : Int,
        userId: Int
    ): EntSymptomsEntity? {
        return entDataBase.entSymptomsDao().getSymptomsByPatientAndCamp(patientId, campId, uniqueId, formId, userId)
    }

    suspend fun updateSymptomsDetails(entity: EntSymptomsEntity) {
        entDataBase.entSymptomsDao().updateSymptomsDetails(entity)
    }

    suspend fun deleteSymptomsByPatientCampUserForm(
        patientId: String,
        campId: String,
        userId: String,
        formId: String
    ) {
        entDataBase.entSymptomsDao().deleteSymptomsByPatientCampUserForm(patientId, campId, userId, formId)
    }

    suspend fun clearSyncedSymptoms() {
        entDataBase.entSymptomsDao().clearSyncedSymptoms()
    }

    //Impression
    fun insertImpression(impression: EntImpressionEntity)  =
        entDataBase.entImpressionListDao().insert(impression)

    val allImpression: LiveData<List<EntImpressionEntity>> =
        entDataBase.entImpressionListDao().allSymptoms()
    suspend fun getImpressionListById(intentFormId: Int) =
        entDataBase.entImpressionListDao().getImpressionListById(intentFormId)

    suspend fun getImpressionsByFormId(formId: Int): List<EntImpressionEntity> {
        return entDataBase.entImpressionListDao().getImpressionsByFormId(formId)
    }

    suspend fun deleteImpression(impression: EntImpressionEntity) {
        entDataBase.entImpressionListDao().deleteImpression(impression)
    }

    suspend fun getUnsyncedImpressionNow(): List<EntImpressionEntity> {
        return entDataBase.entImpressionListDao().getUnSyncedInvestigationNow()
    }

    suspend fun deleteImpressionsByPatientCampUserForm(
        patientId: String,
        campId: String,
        userId: String,
        formId: String
    ) {
        entDataBase.entImpressionListDao().deleteImpressionsByPatientCampUserForm(patientId, campId, userId, formId)
    }


    suspend fun updateImpressionAppId(id: Int, appId: String) {
        Log.d("SyncCheck Impression", "Calling DAO to update appId=$appId for id=$id")
        entDataBase.entImpressionListDao().updateImpressionAppId(id, appId)
    }

    suspend fun sendDoctorImpressionToServer(impression: List<EntImpressionEntity>): EntDoctorNoteImpressionResponse {
        val requestItems = impression.map {
            EntDoctorNoteImpressionItem(
                uniqueId = it.uniqueId,
                formId = it.formId,
                patientId = it.patientId,
                campId = it.campId,
                userId = it.userId,
                part = it.part,
                impression = it.impression ?: "",
                impressionId = it.impressionId,
                appCreatedDate = it.appCreatedDate
            )
        }
        val request = EntDoctorNoteImpressionRequest(requestItems)
        return apiClient.sendEntDoctorNoteImpression(request)
    }

    suspend fun getUpdateImpressionFromServer(): Response<EntImpressionInstructionsResponse> = apiClient.getUpdateImpressionFromServer()

    suspend fun getAllImpressions(): List<EntImpressionEntity> {
        return entDataBase.entImpressionListDao().getAllImpressions()
    }


    suspend fun getImpressionByPatientCampAndServerId(
        patientId: Int,
        campId: Int,
        uniqueId: Int,
        formId : Int,
        userId: Int
    ): EntImpressionEntity? {
        return entDataBase.entImpressionListDao().getImpressionByPatientCampAndServerId(patientId, campId, uniqueId, formId, userId)
    }


    suspend fun updateImpressionDetails(entity: EntImpressionEntity) {
        entDataBase.entImpressionListDao().updateImpressionDetails(entity)
    }

    suspend fun clearSyncedImpression() {
        entDataBase.entImpressionListDao().clearSyncedImpression()
    }

    //Investigation, Plan, Advice

    suspend fun insertInvestigation(entity: DoctorNoteInvestigationEntity): Long {
        return entDataBase.doctorNoteInvestigationDao().insertDoctorNoteInvestigation(entity)
    }

//
//    suspend fun insertOrUpdateInvestigationByPatientId(entity: DoctorNoteInvestigationEntity): Long {
//        val existing = entDataBase.doctorNoteInvestigationDao().getDoctorNoteInvestigationByPatientId(entity.patientId)
//
//        return if (existing != null) {
//            entity.uniqueId = existing.uniqueId
//            entDataBase.doctorNoteInvestigationDao().updateInvestigation(entity)
//            existing.uniqueId.toLong()
//        } else {
//            entDataBase.doctorNoteInvestigationDao().insertDoctorNoteInvestigation(entity)
//        }
//    }


    fun getDoctorNoteInvestigationListById(intentFormId: Int) =
        entDataBase.doctorNoteInvestigationDao().getDoctorNoteInvestigationListById(intentFormId)


    suspend fun getUnsyncedInvestigationNow(): List<DoctorNoteInvestigationEntity> {
        return entDataBase.doctorNoteInvestigationDao().getUnSyncedInvestigationNow()
    }


    suspend fun sendDoctorInvestigationToServer(doctorInvestigationNotes: List<DoctorNoteInvestigationEntity>): EntDoctorNoteInvestigationResponse {
        val requestItems = doctorInvestigationNotes.map {
            EntDoctorNoteInvestigationItem(
                uniqueId = it.uniqueId,
                patientId = it.patientId,
                campId = it.campId,
                userId = it.userId,
                cbc = it.cbc,
                bt = it.bt,
                ct = it.ct,
                hiv = it.hiv,
                hbsag = it.hbsag,
                puretoneaudiometry = it.puretoneaudiometry,
                impedanceaudiometry = it.impedanceaudiometry,
                xray = it.xray,
                xray_value = it.xray_value ?: "",
                removalOfimpactedwax_right = it.removalOfimpactedwax_right,
                removalOfimpactedwax_left = it.removalOfimpactedwax_left,
                tympanoplasty_right = it.tympanoplasty_right,
                tympanoplasty_left = it.tympanoplasty_left,
                exploratoryTympanoplasty_right = it.exploratoryTympanoplasty_right,
                exploratoryTympanoplasty_left = it.exploratoryTympanoplasty_left,
                exploratoryMastoidectomy_right = it.exploratoryMastoidectomy_right,
                exploratoryMastoidectomy_left = it.exploratoryMastoidectomy_left,
                fbremoval_right = it.fbremoval_right,
                fbremoval_left = it.fbremoval_left,
                grommetInsertion_right = it.grommetInsertion_right,
                grommetInsertion_left = it.grommetInsertion_left,
                excisionBiospy_right = it.excisionBiospy_right,
                excisionBiospy_left = it.excisionBiospy_left,
                other = it.other ?: "",
                other_right = it.other_right,
                other_left = it.other_left,
                lineUpForSurgery = it.lineUpForSurgery,
                medication = it.medication,
                audiometry = it.audiometry,
                appCreatedDate = it.appCreatedDate ?: "",
            )
        }

        val request = EntDoctorNoteInvestigationRequest(requestItems)
        return apiClient.sendEntDoctorNoteInvestigation(request)
    }

    suspend fun updateInvestigationAppId(id: Int, appId: String) {
        Log.d("SyncCheck Investigation", "Calling DAO to update appId=$appId for id=$id")
        entDataBase.doctorNoteInvestigationDao().updateInvestigationAppId(id, appId)
    }

    suspend fun getUpdateDoctorInvestigationFromServer(): Response<EntDoctorInvestigationInstructionsResponse> = apiClient.getUpdateDoctorInvestigationFromServer()

    suspend fun getDoctorInvestigationByPatientAndCamp(patientId: Int, campId: Int, uniqueId: Int, userId: Int): DoctorNoteInvestigationEntity? {
        return entDataBase.doctorNoteInvestigationDao().getDoctorInvestigationByPatientAndCamp(patientId, campId, uniqueId, userId)
    }

    suspend fun updateDoctorInvestigationDetails(entity: DoctorNoteInvestigationEntity) {
        entDataBase.doctorNoteInvestigationDao().updateDoctorInvestigationDetails(entity)
    }

    val All_Ent_Opd_Doctor_Follow_ups: LiveData<List<DoctorNoteInvestigationEntity>> =
        entDataBase.doctorNoteInvestigationDao().getAll_Ent_Opd_Doctor_Follow_ups()

    fun getSyncedDoctorNoteInvestigations(appId: String = "1"): LiveData<List<DoctorNoteInvestigationEntity>> {
        return entDataBase.doctorNoteInvestigationDao().getSyncedDoctorInvestigationByAppId(appId)
    }
    suspend fun clearSyncedDoctorInvestigationNotes() {
        entDataBase.doctorNoteInvestigationDao().clearSyncedDoctorInvestigationNotes()
    }


    //EntProOpDetailsViewModel

    fun insertPreOpDetails(preOpDetailsEntity: EntPreOpDetailsEntity) =
        entDataBase.preOpDetailsDao().insertPreOpDetails(preOpDetailsEntity)

    fun getPreOpListById(localFormId: Int, patientId: Int) =
        entDataBase.preOpDetailsDao().getPreOpDetailsById(localFormId, patientId)


    suspend fun getUnsyncedPreOpDetails(): List<EntPreOpDetailsEntity> {
        return entDataBase.preOpDetailsDao().getUnSyncedPreOpDetails()
    }

    suspend fun sendDoctorPreOpDetailsToServer(preOpDetails: List<EntPreOpDetailsEntity>): EntPreOpDetailsResponse {
        val requestItems = preOpDetails.map {
            EntPreOpDetailsItem(
                uniqueId = it.uniqueId,
                patientId = it.patientId,
                campId = it.campId,
                userId = it.userId,
                appCreatedDate = it.appCreatedDate,
                injtt = it.injtt,
                adulttab = it.adulttab,
                childrensolidorliquid = it.childrensolidorliquid,
                childrenbreastfed = it.childrenbreastfed,
                childrenwaterorliquid = it.childrenwaterorliquid,
                adultsolidorliquid = it.adultsolidorliquid,
                adultwaterorliquid = it.adultwaterorliquid,
                currentedicine = it.currentedicine,
                childrensolidorliquidTime = it.childrensolidorliquidTime,
                childrenbreastfedTime = it.childrenbreastfedTime,
                childrenwaterorliquidTime = it.childrenwaterorliquidTime,
                adultsolidorliquidTime = it.adultsolidorliquidTime,
                adultwaterorliquidTime = it.adultwaterorliquidTime,
                otherInstructions = it.otherInstructions,
                otherInstructionsDetail = it.otherInstructionsDetail ?: "",
                surgeryCancel = it.surgeryCancel ?: false,
                surgeryCancelReason = it.surgeryCancelReason ?: ""
            )
        }
        val request = EntPreOpDetailsRequest(requestItems)
        return apiClient.sendEntPreOpDetails(request)
    }


    suspend fun updatePreOpDetailsAppId(id: Int, appId: String) {
        Log.d("SyncCheck PreOpDetails", "Calling DAO to update appId=$appId for id=$id")
        entDataBase.preOpDetailsDao().updatePreOpDetailsAppId(id, appId)
    }

    suspend fun updatePatientReportAppId(patientId: Int, id: Int, appId: String) {
        entDataBase.entPatientReportDao().updatePatientReportAppId(patientId, id, appId)
    }


    val All_Ent_Pro_Op_Follow_ups: LiveData<List<EntPreOpDetailsEntity>> =
        entDataBase.preOpDetailsDao().getAll_Ent_Pro_Op_Follow_ups()

    suspend fun deleteSyncedPreOpDetails() {
        entDataBase.preOpDetailsDao().deleteSyncedPreOpDetails()
    }

    //Image
    val getAllPreOpImages: LiveData<List<PreOpImageEntity>> = entDataBase.preOpImageDao().getAllPreOpImages()

    fun saveConcentFormImageLocally(preOpImageEntity: PreOpImageEntity) =
        entDataBase.preOpImageDao().saveConcentFormImageLocally(preOpImageEntity)

    fun getPreOpImageById(localPatientId: Int) =
        entDataBase.preOpImageDao().getPreOpImageById(localPatientId)

    suspend fun syncPreOpImagesNew(
        fileName: MultipartBody.Part,
        patientId: RequestBody,
        campId: RequestBody,
        userId: RequestBody,
        appCreatedDate: RequestBody,
        app_id: RequestBody,
        uniqueId: RequestBody
    ): Response<EntPreOpImageResponse> {
        Log.d("Upload", "Repository: Uploading image to server")
        return apiClient.uploadPreOpImage(fileName, patientId, campId, userId, appCreatedDate, app_id, uniqueId)
    }

    suspend fun getUnSyncedPreOpImageDetailsNow(): List<PreOpImageEntity> {
        return entDataBase.preOpImageDao().getUnSyncedPreOpImageDetailsNow()
    }

    suspend fun updatePreOpImageDetailsAppId(id: Int, appId: String) {
        entDataBase.preOpImageDao().updatePreOpImageDetailsAppId(id, appId)
    }

    suspend fun clearSyncedPreOpImage() {
        entDataBase.preOpImageDao().clearSyncedPreOpImage()
    }

    suspend fun getUpdatePreOpImageDetailsFromServer(): Response<EntPreOpImageDetailsInstructionsResponse> = apiClient.getUpdatePreOpImageDetailsFromServer()

    suspend fun downloadPreOpImage(fileName: String): Response<ResponseBody> {
        return apiClient.downloadPreOpImage(fileName)
    }

    suspend fun getPreOpImageByPatientCampUser(
        patientId: Int,
        campId: Int,
        userId: Int,
        uniqueId: Int,
    ): PreOpImageEntity? {
        return  entDataBase.preOpImageDao().getImageByPatientCampUser(patientId, campId, userId, uniqueId)
    }

    suspend fun updatePreOpImageDetails(entity: PreOpImageEntity) {
        entDataBase.preOpImageDao().updatePreOpImageDetails(entity)
    }


    //Get update pre op details from server
    suspend fun getUpdatePreOpDetailsFromServer(): Response<PreSurgeryInstructionResponse> = apiClient.getUpdatePreOpDetailsFromServer()

    suspend fun getPreOpByPatientAndCamp(patientId: Int, campId: Int, uniqueId: Int, userId: Int): EntPreOpDetailsEntity? {
        return entDataBase.preOpDetailsDao().getByPatientIdAndCampId(patientId, campId, uniqueId, userId)
    }

    suspend fun updatePreOpDetails(entity: EntPreOpDetailsEntity) {
        entDataBase.preOpDetailsDao().updatePreOpDetails(entity)
    }

    //SurgicalNotesViewModel

    fun insertSurgicalNotesDetails(surgicalNotesEntity: SurgicalNotesEntity) =
        entDataBase.surgicalNotesDao().insertSurgicalNotes(surgicalNotesEntity)

    fun getSurgicalNotesListById(localPatientId: Int, patientId: Int) =
        entDataBase.surgicalNotesDao().getSurgicalNotesById(localPatientId, patientId)

    suspend fun getUnsyncedSurgicalNotes(): List<SurgicalNotesEntity> {
        return entDataBase.surgicalNotesDao().getUnSyncedSurgicalNotes()
    }

    suspend fun sendDoctorSurgicalNotesToServer(surgicalNotes: List<SurgicalNotesEntity>): EntSurgicalNotesResponse {
        val requestItems = surgicalNotes.map {
            EntSurgicalNotesItem(
                uniqueId = it.uniqueId,
                patientId = it.patientId,
                campId = it.campId,
                userId = it.userId,
                appCreatedDate = it.appCreatedDate,
                lignocaineSensitive = it.lignocaineSensitive,
                xylocaineSensitive = it.xylocaineSensitive,
                localApplicationDone = it.localApplicationDone,
                localInfiltrationDone = it.localInfiltrationDone,
                nerveBlock = it.nerveBlock,
                generalEndotrachealUsed = it.generalEndotrachealUsed,
                pulseMonitored = it.pulseMonitored,
                respirationMonitored = it.respirationMonitored,
                bpMonitored = it.bpMonitored,
                ecgMonitored = it.ecgMonitored,
                temperatureMonitored = it.temperatureMonitored,
                antibioticGiven = it.antibioticGiven,
                ethamsylateGiven = it.ethamsylateGiven,
                adrenalinInfiltrationDone = it.adrenalinInfiltrationDone,
                earWaxRemovalDone = it.earWaxRemovalDone,
                tympanoplastyDone = it.tympanoplastyDone,
                mastoidectomyDone = it.mastoidectomyDone,
                foreignBodyRemovalDone = it.foreignBodyRemovalDone,
                grommentInsertionDone = it.grommentInsertionDone,
                excisionBiopsyDone = it.excisionBiopsyDone,
                other = it.other ?: "",
                pulseValue = it.pulseValue ?: "",
                bpSystolic = it.bpSystolic ?: "",
                bpDiastolic = it.bpDiastolic ?: "",
                bpInterpretation = it.bpInterpretation ?: "",
                respirationValue = it.respirationValue ?: "",
                temperatureValue = it.temperatureValue ?: "",
                temperatureUnit = it.temperatureUnit ?: "",
                ecgDetail = it.ecgDetail ?: "",
                antibioticDetail = it.antibioticDetail ?: ""
            )
        }
        val request = EntSurgicalNotesRequest(requestItems)
        return apiClient.sendEntSurgicalNotes(request)
    }

    suspend fun updateSurgicalNotesAppId(id: Int, appId: String) {
        Log.d("SyncCheck SurgicalNotes", "Calling DAO to update appId=$appId for id=$id")
        entDataBase.surgicalNotesDao().updateSurgicalNotesAppId(id, appId)
    }

    val All_Ent_Surgical_Follow_ups: LiveData<List<SurgicalNotesEntity>> =
        entDataBase.surgicalNotesDao().getAll_Ent_Surgical_Follow_ups()

    suspend fun clearSyncedSurgicalNotes() {
        entDataBase.surgicalNotesDao().clearSyncedSurgicalNotes()
    }

    //Get update surgical notes from server
    suspend fun getUpdateSurgicalNotesFromServer(): Response<EntSurgicalInstructionsResponse> = apiClient.getUpdateSurgicalNotesFromServer()

    suspend fun getSurgicalNotesByPatientAndCamp(patientId: Int, campId: Int, uniqueId: Int, userId: Int): SurgicalNotesEntity? {
        return entDataBase.surgicalNotesDao().getSurgicalNotesByPatientAndCamp(patientId, campId, uniqueId, userId)
    }

    suspend fun updateSurgicalNotesDetails(entity: SurgicalNotesEntity) {
        entDataBase.surgicalNotesDao().updateSurgicalNotesDetails(entity)
    }

    //EntPostOpNotesViewModel
    fun insertPostOpNotes(postOpNotesEntity: EntPostOpNotesEntity) =
        entDataBase.entPostOPNotesDao().insertPostOpNotes(postOpNotesEntity)

    fun getPostListById(localPatientId: Int, patientId: Int) =
        entDataBase.entPostOPNotesDao().getPostOpDetailsById(localPatientId, patientId)

    suspend fun getUnsyncedPostOpNotes(): List<EntPostOpNotesEntity> {
        return entDataBase.entPostOPNotesDao().getUnSyncedPostOpNotes()
    }

    suspend fun sendDoctorPostOpNotesToServer(postOpNotes: List<EntPostOpNotesEntity>): EntPostOpNotesResponse {
        val requestItems = postOpNotes.map {
            EntPostOpNotesItem(
                uniqueId = it.uniqueId,
                patientId = it.patientId,
                campId = it.campId,
                userId = it.userId,
                appCreatedDate = it.appCreatedDate,
                ivHydrationGiven = it.ivHydrationGiven,
                npoTill = it.npoTill ?: "",
                clearFluidsStartTime = it.clearFluidsStartTime ?: "",
                paracetamolGiven = it.paracetamolGiven,
                antibioticClavulanateGiven = it.antibioticClavulanateGiven,
                ofloxacinGiven = it.ofloxacinGiven,
                otherMedicationsNote = it.otherMedicationsNote ?: "",
                wickDrainUsed = it.wickDrainUsed,
                redivacUsed = it.redivacUsed,
                watchForSoakage = it.watchForSoakage,
                watchForHematoma = it.watchForHematoma,
                watchForMiddleEarInfection = it.watchForMiddleEarInfection,
                watchForWoundInfection = it.watchForWoundInfection,
                watchForWoundDehiscence = it.watchForWoundDehiscence,
                watchForFacialPalsy = it.watchForFacialPalsy,
                sutureRemovalDate = it.sutureRemovalDate ?: "",
                audiogramResult = it.audiogramResult ?: "",
                audiogramDate = it.audiogramDate ?: "",
                hasComplicationInfection = it.hasComplicationInfection,
                otherComplicationsNote = it.otherComplicationsNote ?: "",
                tympanicMembraneStatus = it.tympanicMembraneStatus ?: "",
                otorrhoeaPresent = it.otorrhoeaPresent,
                airConductionThresholdDb = it.airConductionThresholdDb ?: "",
                hearingThresholdDate = it.hearingThresholdDate ?: "",
                ivHyderationDetail = it.ivHyderationDetail ?: "",
            )
        }
        val request = EntPostOpNotesRequest(requestItems)
        return apiClient.sendEntPostOpNotes(request)

    }

    suspend fun updatePostOpNotesAppId(id: Int, appId: String) {
        Log.d("SyncCheck PostOpNotes", "Calling DAO to update appId=$appId for id=$id")
        entDataBase.entPostOPNotesDao().updatePostOpNotesAppId(id, appId)
    }


    val All_Ent_Post_Follow_ups: LiveData<List<EntPostOpNotesEntity>> =
        entDataBase.entPostOPNotesDao().getAll_Ent_Post_Follow_ups()

    suspend fun clearSyncedPostOpNotes() {
        entDataBase.entPostOPNotesDao().clearSyncedPostOpNotes()
    }

    //Get update post op details from server
    suspend fun getUpdatePostOpNotesFromServer(): Response<EntPostOpInstructionsResponse> = apiClient.getUpdatePostOpNotesFromServer()

    suspend fun getPostOpNotesByPatientAndCamp(patientId: Int, campId: Int, uniqueId: Int, userId: Int): EntPostOpNotesEntity? {
        return entDataBase.entPostOPNotesDao().getPostOpNotesByPatientAndCamp(patientId, campId, uniqueId, userId)
    }

    suspend fun updatePostOpNotesDetails(entity: EntPostOpNotesEntity) {
        entDataBase.entPostOPNotesDao().updatePostOpNotesDetails(entity)
    }

    //AudiometryViewModel
    //Audiometry Data
    fun insertAudiometryDetails(audiometryEntity: AudiometryEntity) =
        entDataBase.audimetryDao().insertAudiometryDetails(audiometryEntity)

    fun getAudiometryListById(localPatientId: Int, patientId: Int) =
        entDataBase.audimetryDao().getAudiometrysById(localPatientId, patientId)

    suspend fun getUnsyncedAudiometryDetails(): List<AudiometryEntity> {
        return entDataBase.audimetryDao().getUnSyncedAudiometryDetailsNow()
    }

    suspend fun sendDoctorAudiometryDetailsToServer(audiometryEntity: List<AudiometryEntity>): EntAudiometryResponse {
        val requestItems = audiometryEntity.map {
            EntAudiometryItem(
                uniqueId = it.uniqueId,
                patientId = it.patientId,
                campId = it.campId,
                userId = it.userId,
                conductiveLeft = it.conductiveLeft,
                conductiveRight = it.conductiveRight,
                conductiveBilateral = it.conductiveBilateral,
                sensorineuralLeft = it.sensorineuralLeft,
                sensorineuralRight = it.sensorineuralRight,
                sensorineuralBilateral = it.sensorineuralBilateral,
                hearingAidGiven = it.hearingAidGiven,
                appCreatedDate = it.appCreatedDate ?: "",
                hearingAidType = it.hearingAidType ?: "",

                )
        }
        val request = EntAudiometryRequest(requestItems)
        return apiClient.sendEntAudiometry(request)
    }

    suspend fun getUpdateAudiometryDetailsFromServer(): Response<EntAudiometryDetailsInstructionsResponse> = apiClient.getUpdateAudiometryFromServer()

    suspend fun getAudiometryDetailsByPatientAndCamp(patientId: Int, campId: Int, uniqueId: Int, userId: Int): AudiometryEntity? {
        return entDataBase.audimetryDao().getAudiometryDetailsByPatientAndCamp(patientId, campId, uniqueId, userId)
    }

    suspend fun updateAudiometryDetails(entity: AudiometryEntity) {
        entDataBase.audimetryDao().updatePostOpNotesDetails(entity)
    }

    suspend fun updateAudiometryDetailsAppId(id: Int, appId: String) {
        Log.d("SyncCheck SurgicalNotes", "Calling DAO to update appId=$appId for id=$id")
        entDataBase.audimetryDao().updateAudiometryDetailsAppId(id, appId)
    }

    val All_Ent_Audiometry_Follow_ups: LiveData<List<AudiometryEntity>> =
        entDataBase.audimetryDao().getAll_Ent_Audiometry_Follow_ups()

    fun getEntAudiometrySyncData(appId: String = "1"): LiveData<List<AudiometryEntity>> {
        return entDataBase.audimetryDao().getEntAudiometrySyncData(appId)
    }

    fun clearSyncedAudiometryDetails() {
        entDataBase.audimetryDao().clearSyncedAudiometryDetails()
    }

    //Audiometry Image

    val getAllAudiometryImages: LiveData<List<AudiometryImageEntity>> = entDataBase.audimetryImageDao().getAllAudiometryImages()

    fun insertAudiometryImageDetails(audiometryImageEntity: AudiometryImageEntity) =
        entDataBase.audimetryImageDao().insertAudiometryImageDetails(audiometryImageEntity)

    fun getAudiometryImageListById(localPatientId: Int) =
        entDataBase.audimetryImageDao().getAudiometryImageById(localPatientId)

    suspend fun syncAudiometryImagesNew(
        fileName: MultipartBody.Part,
        patientId: RequestBody,
        campId: RequestBody,
        userId: RequestBody,
        appCreatedDate: RequestBody,
        app_id: RequestBody,
        uniqueId: RequestBody
    ): Response<EntAudiometryImageResponse> {
        Log.d("pawan", "Repository: Uploading image to server")
        return apiClient.uploadAudiometryImage(fileName, patientId, campId, userId, appCreatedDate, app_id, uniqueId)
    }

    suspend fun getUnSyncedAudiometryImageDetailsNow(): List<AudiometryImageEntity> {
        return entDataBase.audimetryImageDao().getUnSyncedAudiometryImageDetailsNow()
    }

    suspend fun updateAudiometryImageDetailsAppId(id: Int, appId: String) {
        entDataBase.audimetryImageDao().updateAudiometryImageDetailsAppId(id, appId)
    }


    suspend fun clearSyncedAudiometryImage() {
        entDataBase.audimetryImageDao().clearSyncedAudiometryImage()
    }

    suspend fun getImageByPatientCampUser(
        patientId: Int,
        campId: Int,
        userId: Int,
        uniqueId: Int
    ): AudiometryImageEntity? {
        return  entDataBase.audimetryImageDao().getImageByPatientCampUser(patientId, campId, userId, uniqueId)
    }

    suspend fun updateAudiometryImageDetails(entity: AudiometryImageEntity) {
        entDataBase.audimetryImageDao().updateAudiometryImageDetails(entity)
    }

    suspend fun getUpdateAudiometryImageDetailsFromServer(): Response<EntAudiometryImageDetailsInstructionsResponse> = apiClient.getUpdateAudiometryImageDetailsFromServer()

    suspend fun downloadAudiometryImage(fileName: String): Response<ResponseBody> {
        return apiClient.downloadAudiometryImage(fileName)
    }

    //Pathology ViewModel
    val All_PATHOLOGY_Follow_ups: LiveData<List<PathologyEntity>> =
        entDataBase.pathology().getAll_PATHOLOGY_Follow_ups()

    fun insertPathologyDetails(pathologyEntity: PathologyEntity) =
        entDataBase.pathology().insertPathologyDetails(pathologyEntity)

    fun getPathologyById(localFormId: Int) =
        entDataBase.pathology().getPathologyById(localFormId)

    suspend fun getUnSyncedPathologyDetailsNow(): List<PathologyEntity> {
        return entDataBase.pathology().getUnSyncedPathologyDetailsNow()
    }

    suspend fun sendDoctorPathologyDetailsToServer(audiometryEntity: List<PathologyEntity>): PathologyResponse {
        val requestItems = audiometryEntity.map {
            PathologyItem(
                uniqueId = it.uniqueId,
                patientId = it.patientId,
                campId = it.campId,
                userId = it.userId,
                cbc = it.cbc,
                cbcValue = it.cbcValue ?: "",
                bt = it.bt,
                btValue = it.btValue ?: "",
                ct = it.ct,
                ctValue = it.ctValue ?: "",
                hiv = it.hiv,
                hivValue = it.hivValue ?: "",
                hbsag = it.hbsag,
                hbsagValue = it.hbsagValue ?: "",
                pta = it.pta,
                ptaValue = it.ptaValue ?: "",
                impedanceAudiometry = it.impedanceAudiometry,
                impedanceAudiometryValue = it.impedanceAudiometryValue ?: "",
                appCreatedDate = it.appCreatedDate ?: "",
                )
        }
        val request = PathologyRequest(requestItems)
        return apiClient.sendPathology(request)
    }

    suspend fun updatePathologyDetailsAppId(id: Int, appId: String) {
        Log.d("SyncCheck Pathology", "Calling DAO to update appId=$appId for id=$id")
        entDataBase.pathology().updatePathologyDetailsAppId(id, appId)
    }

    suspend fun clearSyncedPathologyReports() {
        entDataBase.pathology().clearSyncedPathologyReports()
    }

    // Get Updated pathology data from server
    suspend fun getUpdatePathologyDetailsServer(): Response<EntPathologyDetailsInstructionsResponse> = apiClient.getUpdatePathologyDetailsServer()

    suspend fun getPathologyDetailsByPatientAndCamp(patientId: Int, campId: Int, uniqueId: Int): PathologyEntity? {
        return entDataBase.pathology().getPathologyDetailsByPatientAndCamp(patientId, campId, uniqueId)
    }

    suspend fun updatePathologyDetails(entity: PathologyEntity) {
        entDataBase.pathology().updatePathologyDetails(entity)
    }


//    Image
    val getAllPathologyImages: LiveData<List<PathologyImageEntity>> = entDataBase.pathologyImageDao().getAllPathologyImages()

    fun insertPathologyImage(audiometryImageEntity: PathologyImageEntity) =
        entDataBase.pathologyImageDao().insertPathologyImageDetails(audiometryImageEntity)

    fun getImagesByPatientId(localFormId: Int): LiveData<List<PathologyImageEntity>> {
        return  entDataBase.pathologyImageDao().getImagesByPatientId(localFormId)
    }

    suspend fun updateImage(patientId: Int, reportType: String, newPath: String, date: String) {
        entDataBase.pathologyImageDao().updateImageInRoom(patientId, reportType, newPath, date)
    }

    suspend fun syncPathologyImagesNew(
        fileName: MultipartBody.Part,
        patientId: RequestBody,
        campId: RequestBody,
        userId: RequestBody,
        appCreatedDate: RequestBody,
        reportType : RequestBody,
        app_id: RequestBody,
        uniqueId: RequestBody
    ): Response<PathologyImageResponse> {
        Log.d("Pathology Upload", "Repository: Uploading image to server")
        return apiClient.uploadPathologyImage(fileName, patientId, campId, userId, appCreatedDate, reportType, app_id, uniqueId)
    }

    suspend fun getUnSyncedPathologyImageDetailsNow(): List<PathologyImageEntity> {
        return entDataBase.pathologyImageDao().getUnSyncedPathologyImageDetailsNow()
    }

    suspend fun updatePathologyImageAppId(id: Int, appId: String) {
        entDataBase.pathologyImageDao().updateAudiometryImageDetailsAppId(id, appId)
    }


    suspend fun getUpdatePathologyImageDetailsFromServer(): Response<EntPathologyImageDetailsInstructionsResponse> = apiClient.getUpdatePathologyImageDetailsFromServer()

    suspend fun downloadPathologyImage(fileName: String): Response<ResponseBody> {
        return apiClient.downloadPathologyImage(fileName)
    }

    suspend fun getPathologyImageByPatientCampUser(
        patientId: Int,
        campId: Int,
        userId: Int,
        reportType: String,
        uniqueId: Int
    ): PathologyImageEntity? {
        return  entDataBase.pathologyImageDao().getPathologyImageByPatientCampUser(patientId, campId, userId, reportType, uniqueId)
    }

    suspend fun updatePathologyImageDetails(entity: PathologyImageEntity) {
        entDataBase.pathologyImageDao().updatePreOpImageDetails(entity)
    }


    suspend fun clearSyncedPathologyImage() {
        entDataBase.pathologyImageDao().clearSyncedPathologyImage()
    }

    //Ent Patient Report
    fun insertPatientReport(patientReport: EntPatientReport) =
        entDataBase.entPatientReportDao().insertEntPatientReport(patientReport)

    suspend fun getPatientReport() = entDataBase.entPatientReportDao().getEntPatientReport()

    fun getPatientReportById(localPatientId: Int) =
        entDataBase.entPatientReportDao().getEntPatientReportById(localPatientId)

    suspend fun clearSyncedEntPatientReportList() {
        entDataBase.entPatientReportDao().clearSyncedEntPatientReportList()
    }

//    fun updatePatientForms(syncedForms: List<Int>, formType: String) =
//        entDataBase.entPatientReportDao().updatePatientForms(syncedForms, formType)
    fun insertPrescriptionPatientReport(patientReport: PrescriptionPatientReport) =
        entDataBase.prescriptionPatientReportDao().insertPrescriptionPatientReport(patientReport)

    suspend fun getPrescriptionPatientReport() = entDataBase.prescriptionPatientReportDao().getPrescriptionPatientReport()

}