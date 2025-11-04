package org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_OPD_Doctors_Note
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntEarType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntImpressionEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntNoseType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntSymptomsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntThroatType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.ImpressionType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.AudiometryDetailsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.DoctorInvestigationInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.ImpressionInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.SymptomsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.repository.EntRepository
import javax.inject.Inject

@HiltViewModel
class EntOpdDoctorsNoteViewModel @Inject constructor(private val entRepository: EntRepository): ViewModel() {
    val All_Ent_Opd_Doctor_Follow_ups:LiveData<List<DoctorNoteInvestigationEntity>> = entRepository.All_Ent_Opd_Doctor_Follow_ups

    val getEntOpdDoctorsNoteSyncData: LiveData<List<DoctorNoteInvestigationEntity>> =
        entRepository.getSyncedDoctorNoteInvestigations("1")

    val allSymptoms:LiveData<List<EntSymptomsEntity>> = entRepository.allSymptoms

    val allImpression:LiveData<List<EntImpressionEntity>> = entRepository.allImpression


    private val _earSymptoms = MutableLiveData<List<EntEarType>>()
    val earSymptoms: LiveData<List<EntEarType>> get() = _earSymptoms

    private val _noseSymptoms = MutableLiveData<List<EntNoseType>>()
    val noseSymptoms: LiveData<List<EntNoseType>> get() = _noseSymptoms

    private val _throatSymptoms = MutableLiveData<List<EntThroatType>>()
    val throatSymptoms: LiveData<List<EntThroatType>> get() = _throatSymptoms

    private val _entImpression = MutableLiveData<List<ImpressionType>>()
    val entImpression: LiveData<List<ImpressionType>> get() = _entImpression


    fun fetchEarSymptoms() {
        viewModelScope.launch {
            val earSymptoms = entRepository.getEarSymptoms()
            _earSymptoms.postValue(earSymptoms)
        }
    }

    fun fetchNoseSymptoms() {
        viewModelScope.launch {
            val noseSymptoms = entRepository.getNoseSymptoms()
            _noseSymptoms.postValue(noseSymptoms)
        }
    }

    fun fetchThroatSymptoms() {
        viewModelScope.launch {
            val throatSymptoms = entRepository.getThroatSymptoms()
            _throatSymptoms.postValue(throatSymptoms)
        }
    }

    fun fetchEntImpression() {
        viewModelScope.launch {
            val entImpression = entRepository.getEntImpression()
            _entImpression.postValue(entImpression)
        }
    }

    //Symptoms
    private var _insertionSymptomsStatus = MutableLiveData<Resource<Long>>()
    val insertionSymptomsStatus: LiveData<Resource<Long>> get() = _insertionSymptomsStatus

    private var _symptomsListListById = MutableLiveData<Resource<List<EntSymptomsEntity>>>()
    val symptomsListListById: LiveData<Resource<List<EntSymptomsEntity>>> get() = _symptomsListListById

    fun insertSymptom(symptom: EntSymptomsEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.insertSymptom(symptom)
            if (message == null || message == -1L) {
                _insertionSymptomsStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertionSymptomsStatus.postValue(Resource.success(message))
            }
        }
    }

    fun deleteSymptom(symptom: EntSymptomsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            entRepository.deleteSymptom(symptom)
        }
    }


    fun getSymptomsById(intentFormId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _symptomsListListById.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getSymptomsListById(intentFormId).let {
                    _symptomsListListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _symptomsListListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            _symptomsListListById.postValue(Resource.error(e.message.toString(), null))
        }
    }

    suspend fun getSymptomsByFormId(formId: Int): List<EntSymptomsEntity> {
        return entRepository.getSymptomsByFormId(formId)
    }

    suspend fun getUnsyncedSymptomsOnce(): List<EntSymptomsEntity> {
        return entRepository.getUnsyncedSymptomsNow()
    }

    fun sendDoctorSymptomsNotesToServer(symptoms: List<EntSymptomsEntity>, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("SyncCheck", "Sending to server: ${symptoms.map { it.uniqueId }}")

                val response = entRepository.sendDoctorNoteSymptomsToServer(symptoms)

                if (response.success && response.data != null) {
                    Log.d("SyncCheck", "Received response: ${response.data.results.size} results")

                    val matchedItems = symptoms.filter { it.app_id.isNullOrBlank() }

                    response.data.results.forEachIndexed { index, result ->
                        if (index < matchedItems.size) {
                            val item = matchedItems[index]
                            Log.d("SyncCheck", "Updating app_id for local id: ${item.uniqueId} → server app_id: ${result.app_id}")
                            entRepository.updateSymptomsAppId(item.uniqueId, result.app_id)

                            Log.d("SyncCheck PatientReport", "Updating app_id in ent_patient_report for patientId=${item.patientId} → app_id=${result.app_id}")
                            entRepository.updatePatientReportAppId(item.patientId, item.uniqueId, result.app_id)
                        } else {
                            Log.w("SyncCheck", "Result index exceeds matched item list size")
                        }
                    }
                } else {
                    Log.w("SyncCheck", "Server returned failure: ${response.message}")
                }

                onResult(response.success, response.message)
            } catch (e: Exception) {
                Log.e("SyncCheck", "Error while sending to server: ${e.localizedMessage}")
                onResult(false, e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun clearSyncedSymptoms() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedSymptoms()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    //Impression
    private var _insertionImpressionStatus = MutableLiveData<Resource<Long>>()
    val insertionImpressionStatus: LiveData<Resource<Long>> get() = _insertionImpressionStatus

    private var _impressionListListById = MutableLiveData<Resource<List<EntImpressionEntity>>>()
    val impressionListListById: LiveData<Resource<List<EntImpressionEntity>>> get() = _impressionListListById

    fun insertImpression(impression: EntImpressionEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.insertImpression(impression)
            if (message == null || message == -1L) {
                _insertionImpressionStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertionImpressionStatus.postValue(Resource.success(message))
            }
        }
    }

    suspend fun getImpressionsByFormId(formId: Int): List<EntImpressionEntity> {
        return entRepository.getImpressionsByFormId(formId)
    }



    fun deleteImpression(impression: EntImpressionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            entRepository.deleteImpression(impression)
        }
    }
    fun getImpressionById(intentFormId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _impressionListListById.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getImpressionListById(intentFormId).let {
                    _impressionListListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _impressionListListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            _impressionListListById.postValue(Resource.error(e.message.toString(), null))
        }
    }

    suspend fun getUnsyncedImpressionOnce(): List<EntImpressionEntity> {
        return entRepository.getUnsyncedImpressionNow()
    }


    fun sendDoctorImpressionNotesToServer(symptoms: List<EntImpressionEntity>, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("SyncCheck Impression", "Sending to server: ${symptoms.map { it.uniqueId }}")

                val response = entRepository.sendDoctorImpressionToServer(symptoms)

                if (response.success && response.data != null) {
                    Log.d("SyncCheck Impression", "Received response: ${response.data.results.size} results")

                    val matchedItems = symptoms.filter { it.app_id.isNullOrBlank() }

                    response.data.results.forEachIndexed { index, result ->
                        if (index < matchedItems.size) {
                            val item = matchedItems[index]
                            Log.d("SyncCheck Impression", "Updating app_id for local id: ${item.uniqueId} → server app_id: ${result.app_id}")
                            entRepository.updateImpressionAppId(item.uniqueId, result.app_id)

                            Log.d("SyncCheck PatientReport", "Updating app_id in ent_patient_report for patientId=${item.patientId} → app_id=${result.app_id}")
                            entRepository.updatePatientReportAppId(item.patientId, item.uniqueId, result.app_id)

                        } else {
                            Log.w("SyncCheck Impression", "Result index exceeds matched item list size")
                        }
                    }
                } else {
                    Log.w("SyncCheck Impression", "Server returned failure: ${response.message}")
                }

                onResult(response.success, response.message)
            } catch (e: Exception) {
                Log.e("SyncCheck Impression", "Error while sending to server: ${e.localizedMessage}")
                onResult(false, e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun clearSyncedImpression() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedImpression()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Investigation, Plan, Advice
    private var _insertionIvestigationStatus = MutableLiveData<Resource<Long>>()
    val insertionInvestigationStatus: LiveData<Resource<Long>> get() = _insertionIvestigationStatus

    private var _investigationListListById = MutableLiveData<Resource<List<DoctorNoteInvestigationEntity>>>()
    val investigationListListById: LiveData<Resource<List<DoctorNoteInvestigationEntity>>> get() = _investigationListListById


    fun insertOrUpdateDoctorInvestigation(entity: DoctorNoteInvestigationEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val id = entRepository.insertInvestigation(entity)
                _insertionIvestigationStatus.postValue(Resource.success(id))
            } catch (e: Exception) {
                _insertionIvestigationStatus.postValue(Resource.error("Insert failed", null))
            }
        }
    }


    fun getInvestigationById(intentFormId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _investigationListListById.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getDoctorNoteInvestigationListById(intentFormId).let {
                    _investigationListListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _investigationListListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            _investigationListListById.postValue(Resource.error(e.message.toString(), null))
        }
    }

    suspend fun getUnsyncedInvestigationOnce(): List<DoctorNoteInvestigationEntity> {
        return entRepository.getUnsyncedInvestigationNow()
    }

    fun sendDoctorInvestigationNotesToServer(
        symptoms: List<DoctorNoteInvestigationEntity>,
        onSyncCompleted: (syncedCount: Int, unsyncedCount: Int) -> Unit
    ) {
        viewModelScope.launch {
            var syncedCount = 0
            var unsyncedCount = 0

            try {
                Log.d("SyncCheck Investigation", "Sending to server: ${symptoms.map { it.uniqueId }}")

                val response = entRepository.sendDoctorInvestigationToServer(symptoms)

                if (response.success && response.data != null) {
                    Log.d("SyncCheck Investigation", "Received response: ${response.data.results.size} results")

                    val matchedItems = symptoms.filter { it.app_id.isNullOrBlank() }

                    response.data.results.forEachIndexed { index, result ->
                        if (index < matchedItems.size) {
                            val item = matchedItems[index]
                            entRepository.updateInvestigationAppId(item.uniqueId, result.app_id)
                            entRepository.updatePatientReportAppId(item.patientId, item.uniqueId, result.app_id)
                            syncedCount++
                        } else {
                            unsyncedCount++
                        }
                    }
                } else {
                    Log.w("SyncCheck Investigation", "Server returned failure: ${response.message}")
                    unsyncedCount = symptoms.size // mark all unsynced if failed
                }

                Log.d("SyncCheck Investigation", "✅ Synced: $syncedCount | ❌ Unsynced: $unsyncedCount")

                onSyncCompleted(syncedCount, unsyncedCount)
            } catch (e: Exception) {
                unsyncedCount = symptoms.size
                Log.e("SyncCheck Investigation", "Error while sending to server: ${e.localizedMessage}")
                onSyncCompleted(syncedCount, unsyncedCount)
            }
        }
    }


    fun clearSyncedDoctorInvestigationNotes() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedDoctorInvestigationNotes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _entSymptomsFromServer = MutableLiveData<Resource<List<SymptomsInstruction>>>()

    val entSymptomsFromServer: LiveData<Resource<List<SymptomsInstruction>>> = _entSymptomsFromServer

    fun getUpdateSymptomsFromServer() = viewModelScope.launch {
        _entSymptomsFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdateSymptomsFromServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("DoctorDetailsResponse", "Item #$index: $item")
                }

                _entSymptomsFromServer.postValue(Resource.success(items))

                syncServerSymptomsDetails(items)
            } else {
                Log.e("DoctorDetailsResponse", "API Error: ${response.code()} ${response.message()}")
                _entSymptomsFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("DoctorDetailsResponse", "Exception: ${e.message}", e)
            _entSymptomsFromServer.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun syncServerSymptomsDetails(apiList: List<SymptomsInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {

            // Group server items by patientId+campId+userId+formId
            val groupedServerData = apiList.groupBy { item ->
                "${item.patientId}_${item.campId}_${item.userId}_${item.formId}"
            }

            groupedServerData.forEach { (groupKey, serverItems) ->
                if (serverItems.isNotEmpty()) {
                    val patientId = serverItems.first().patientId
                    val campId = serverItems.first().campId
                    val userId = serverItems.first().userId
                    val formId = serverItems.first().formId

                    // 1️⃣ Delete all local data for this group
                    Log.d("DoctorDetailsResponse", "Deleting all local symptoms for group: $groupKey")
                    entRepository.deleteSymptomsByPatientCampUserForm(
                        patientId.toString(),
                        campId.toString(),
                        userId.toString(),
                        formId.toString()
                    )


                    // 2️⃣ Insert all updated items from server
                    Log.d("DoctorDetailsResponse", "Inserting ${serverItems.size} items for group: $groupKey")
                    serverItems.forEach { serverItem ->
                        entRepository.insertSymptom(serverItem.toEntity())
                    }
                }
            }
        }
    }


    fun SymptomsInstruction.isSameAsLocal(local: EntSymptomsEntity): Boolean {
        return this.organ == local.organ &&
                this.part == local.part &&
                this.symptom == local.symptom &&
                this.symptomId == local.symptomId &&
                this.appId == (local.app_id ?: "")
    }

    fun SymptomsInstruction.toEntity(): EntSymptomsEntity {
        return EntSymptomsEntity(
            uniqueId = this.uniqueId,
            formId = this.formId,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            organ = this.organ,
            part = this.part,
            symptom = this.symptom,
            symptomId = this.symptomId,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            app_id = this.appId
        )
    }

    private val _entImpressionFromServer = MutableLiveData<Resource<List<ImpressionInstruction>>>()

    val entImpressionFromServer: LiveData<Resource<List<ImpressionInstruction>>> = _entImpressionFromServer

    fun getUpdateImpressionFromServer() = viewModelScope.launch {
        _entImpressionFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdateImpressionFromServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("DoctorDetailsResponse", "Item #$index: $item")
                }

                _entImpressionFromServer.postValue(Resource.success(items))

                syncImpressionDetails(items)
            } else {
                Log.e("DoctorDetailsResponse", "API Error: ${response.code()} ${response.message()}")
                _entImpressionFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("DoctorDetailsResponse", "Exception: ${e.message}", e)
            _entImpressionFromServer.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun syncImpressionDetails(apiList: List<ImpressionInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {

            // Group server items by patientId+campId+userId+formId
            val groupedServerData = apiList.groupBy { item ->
                "${item.patientId}_${item.campId}_${item.userId}_${item.formId}"
            }

            groupedServerData.forEach { (groupKey, serverItems) ->
                if (serverItems.isNotEmpty()) {
                    val patientId = serverItems.first().patientId
                    val campId = serverItems.first().campId
                    val userId = serverItems.first().userId
                    val formId = serverItems.first().formId

                    // 1️⃣ Delete all local data for this group
                    Log.d("DoctorDetailsResponse", "Deleting all local impressions for group: $groupKey")
                    entRepository.deleteImpressionsByPatientCampUserForm(
                        patientId.toString(),
                        campId.toString(),
                        userId.toString(),
                        formId.toString()
                    )

                    // 2️⃣ Insert all updated items from server
                    Log.d("DoctorDetailsResponse", "Inserting ${serverItems.size} impressions for group: $groupKey")
                    serverItems.forEach { serverItem ->
                        entRepository.insertImpression(serverItem.toEntity())
                    }
                }
            }
        }
    }

    fun ImpressionInstruction.toEntity(): EntImpressionEntity {
        return EntImpressionEntity(
            uniqueId= this.uniqueId,
            formId = this.formId,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            part = this.part,
            impression = this.impression,
            impressionId = this.impressionId,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            app_id = this.appId
        )
    }



    private val _entDoctorInvestigationFromServer = MutableLiveData<Resource<List<DoctorInvestigationInstruction>>>()

    val entDoctorInvestigationFromServer: LiveData<Resource<List<DoctorInvestigationInstruction>>> = _entDoctorInvestigationFromServer

    fun getUpdateDoctorInvestigationServer() = viewModelScope.launch {
        _entDoctorInvestigationFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdateDoctorInvestigationFromServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("DoctorDetailsSync", "Item #$index: $item")
                }

                _entDoctorInvestigationFromServer.postValue(Resource.success(items))

                syncDoctorInvestigationDetails(items)
            } else {
                Log.e("DoctorDetailsSync", "API Error: ${response.code()} ${response.message()}")
                _entDoctorInvestigationFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("DoctorDetailsSync", "Exception: ${e.message}", e)
            _entDoctorInvestigationFromServer.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }


    fun syncDoctorInvestigationDetails(apiList: List<DoctorInvestigationInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (serverItem in apiList) {
                val localItem = entRepository.getDoctorInvestigationByPatientAndCamp(
                    serverItem.patientId,
                    serverItem.campId,
                    serverItem.uniqueId,
                    serverItem.userId
                )
                Log.d("DoctorDetailsSync", "🔍 Searching local DB for patientId=${serverItem.patientId}, campId=${serverItem.campId}, uniqueId=${serverItem.uniqueId}, userId=${serverItem.userId}")
                Log.d("DoctorDetailsSync", "📦 Loaded from DB: $localItem")


                if (localItem != null) {
                    if (!serverItem.isSameAsLocal(localItem)) {
                        Log.d("DoctorDetailsSync", "👀 Comparing serverItem and localItem for patientId=${serverItem.patientId}, campId=${serverItem.campId}, uniqueId=${serverItem.uniqueId}, userId=${serverItem.userId}")
                        Log.d("DoctorDetailsSync", "Server: $serverItem")
                        Log.d("DoctorDetailsSync", "Local : $localItem")
                        Log.d("DoctorDetailsSync", "Equality Result: ${serverItem.isSameAsLocal(localItem)}")


                        val updatedEntity = serverItem.toEntity()
                        updatedEntity.uniqueId = localItem.uniqueId
                        updatedEntity.app_id = localItem.app_id
                        entRepository.updateDoctorInvestigationDetails(updatedEntity)

                        Log.d("DoctorDetailsSync", "✅ Update done for patientId=${serverItem.patientId}")
                    } else {
                        Log.d("DoctorDetailsSync", "⏩ No update needed for patientId=${serverItem.patientId}, campId=${serverItem.campId}")
                    }
                }
            }
        }
    }


    fun DoctorInvestigationInstruction.isSameAsLocal(local: DoctorNoteInvestigationEntity): Boolean {
        return this.cbc == local.cbc &&
                this.bt == local.bt &&
                this.ct == local.ct &&
                this.hiv == local.hiv &&
                this.hbsag == local.hbsag &&
                this.pureToneAudiometry == local.puretoneaudiometry &&
                this.impedanceAudiometry == local.impedanceaudiometry &&
                this.xray == local.xray &&
                this.xrayValue == (local.xray_value ?: "") &&
                this.removalOfImpactedWaxRight == local.removalOfimpactedwax_right &&
                this.removalOfImpactedWaxLeft == local.removalOfimpactedwax_left &&
                this.tympanoplastyRight == local.tympanoplasty_right &&
                this.tympanoplastyLeft == local.tympanoplasty_left &&
                this.exploratoryTympanoplastyRight == local.exploratoryTympanoplasty_right &&
                this.exploratoryTympanoplastyLeft == local.exploratoryTympanoplasty_left &&
                this.exploratoryMastoidectomyRight == local.exploratoryMastoidectomy_right &&
                this.exploratoryMastoidectomyLeft == local.exploratoryMastoidectomy_left &&
                this.fbRemovalRight == local.fbremoval_right &&
                this.fbRemovalLeft == local.fbremoval_left &&
                this.grommetInsertionRight == local.grommetInsertion_right &&
                this.grommetInsertionLeft == local.grommetInsertion_left &&
                this.excisionBiospyRight == local.excisionBiospy_right &&
                this.excisionBiospyLeft == local.excisionBiospy_left &&
                this.other == (local.other ?: "") &&
                this.otherRight == local.other_right &&
                this.otherLeft == local.other_left &&
                this.lineUpForSurgery == local.lineUpForSurgery &&
                this.medication == local.medication &&
                this.audiometry == local.audiometry
    }


    fun DoctorInvestigationInstruction.toEntity(): DoctorNoteInvestigationEntity {
        return DoctorNoteInvestigationEntity(
            uniqueId = this.uniqueId,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            cbc = this.cbc,
            bt = this.bt,
            ct = this.ct,
            hiv = this.hiv,
            hbsag = this.hbsag,
            puretoneaudiometry = this.pureToneAudiometry,
            impedanceaudiometry = this.impedanceAudiometry,
            xray = this.xray,
            xray_value = this.xrayValue,
            removalOfimpactedwax_right = this.removalOfImpactedWaxRight,
            removalOfimpactedwax_left = this.removalOfImpactedWaxLeft,
            tympanoplasty_right = this.tympanoplastyRight,
            tympanoplasty_left = this.tympanoplastyLeft,
            exploratoryTympanoplasty_right = this.exploratoryTympanoplastyRight,
            exploratoryTympanoplasty_left = this.exploratoryTympanoplastyLeft,
            exploratoryMastoidectomy_right = this.exploratoryMastoidectomyRight,
            exploratoryMastoidectomy_left = this.exploratoryMastoidectomyLeft,
            fbremoval_right = this.fbRemovalRight,
            fbremoval_left = this.fbRemovalLeft,
            grommetInsertion_right = this.grommetInsertionRight,
            grommetInsertion_left = this.grommetInsertionLeft,
            excisionBiospy_right = this.excisionBiospyRight,
            excisionBiospy_left = this.excisionBiospyLeft,
            other = this.other,
            other_right = this.otherRight,
            other_left = this.otherLeft,
            lineUpForSurgery = this.lineUpForSurgery,
            medication = this.medication,
            audiometry = this.audiometry,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            app_id = this.appId
        )
    }

}