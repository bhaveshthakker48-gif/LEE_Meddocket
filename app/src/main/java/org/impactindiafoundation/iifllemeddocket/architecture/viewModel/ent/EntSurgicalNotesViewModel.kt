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
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntSurgicalInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.PreSurgeryInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.repository.EntRepository
import javax.inject.Inject

@HiltViewModel
class EntSurgicalNotesViewModel @Inject constructor(private val entRepository: EntRepository) : ViewModel() {

    val All_Ent_Surgical_Follow_ups:LiveData<List<SurgicalNotesEntity>> = entRepository.All_Ent_Surgical_Follow_ups


    private var _insertionStatus = MutableLiveData<Resource<Long>>()
    val insertionStatus: LiveData<Resource<Long>> get() = _insertionStatus

    private var _SurgucalNotesListById = MutableLiveData<Resource<List<SurgicalNotesEntity>>>()
    val surgucalNotesListByIdById: LiveData<Resource<List<SurgicalNotesEntity>>> get() = _SurgucalNotesListById

    fun insertSurgicalNotes(surgicalNotesEntity: SurgicalNotesEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.insertSurgicalNotesDetails(surgicalNotesEntity)
            if (message == null || message == -1L) {
                _insertionStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertionStatus.postValue(Resource.success(message))
            }
        }
    }

    fun getSurgicalNotesById(localPatientId: Int, patientId :Int) = CoroutineScope(Dispatchers.IO).launch {
        _SurgucalNotesListById.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getSurgicalNotesListById(localPatientId, patientId).let {
                    _SurgucalNotesListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _SurgucalNotesListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            _SurgucalNotesListById.postValue(Resource.error(e.message.toString(), null))
        }
    }


    suspend fun getUnsyncedSurgicalNotes(): List<SurgicalNotesEntity> {
        return entRepository.getUnsyncedSurgicalNotes()
    }

    fun sendDoctorSurgicalNotesToServer(
        notes: List<SurgicalNotesEntity>,
        onSyncCompleted: (syncedCount: Int, unsyncedCount: Int) -> Unit
    ) {
        viewModelScope.launch {
            var syncedCount = 0
            var unsyncedCount = 0

            try {
                Log.d("SyncCheck SurgicalNotes", "Sending to server: ${notes.map { it.uniqueId }}")

                val response = entRepository.sendDoctorSurgicalNotesToServer(notes)

                if (response.success && response.data != null) {
                    Log.d("SyncCheck SurgicalNotes", "Received response: ${response.data.results.size} results")
                    Log.d("SyncCheck SurgicalNotes", "Response success: ${response.success} | message: ${response.message}")

                    val matchedItems = notes.filter { it.app_id.isNullOrBlank() }

                    response.data.results.forEachIndexed { index, result ->
                        if (index < matchedItems.size) {
                            val item = matchedItems[index]

                            Log.d(
                                "SyncCheck SurgicalNotes",
                                "Updating app_id for local id: ${item.uniqueId} → server app_id: ${result.app_id}"
                            )

                            entRepository.updateSurgicalNotesAppId(item.uniqueId, result.app_id)
                            entRepository.updatePatientReportAppId(item.patientId, item.uniqueId, result.app_id)

                            syncedCount++
                        } else {
                            unsyncedCount++
                            Log.w("SyncCheck SurgicalNotes", "Result index exceeds matched item list size")
                        }
                    }
                } else {
                    Log.w("SyncCheck SurgicalNotes", "Server returned failure: ${response.message}")
                    unsyncedCount = notes.size
                }

                Log.d("SyncCheck SurgicalNotes", "✅ Synced: $syncedCount | ❌ Unsynced: $unsyncedCount")
                onSyncCompleted(syncedCount, unsyncedCount)

            } catch (e: Exception) {
                unsyncedCount = notes.size
                Log.e("SyncCheck SurgicalNotes", "Error while sending to server: ${e.localizedMessage}")
                onSyncCompleted(syncedCount, unsyncedCount)
            }
        }
    }


    fun clearSyncedSurgicalNotes() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedSurgicalNotes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Get Updated Data From Server
    private val _entSurgicalNotesFromServer = MutableLiveData<Resource<List<EntSurgicalInstruction>>>()

    val entSurgicalNotesFromServer: LiveData<Resource<List<EntSurgicalInstruction>>> = _entSurgicalNotesFromServer

    fun getUpdateSurgicalNotesFromServer() = viewModelScope.launch {
        _entSurgicalNotesFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdateSurgicalNotesFromServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("SurgicalNotesResponse", "Item #$index: $item")
                }

                _entSurgicalNotesFromServer.postValue(Resource.success(items))

                syncServerSurgicalNotes(items)
            } else {
                Log.e("SurgicalNotesResponse", "API Error: ${response.code()} ${response.message()}")
                _entSurgicalNotesFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("SurgicalNotesResponse", "Exception: ${e.message}", e)
            _entSurgicalNotesFromServer.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun syncServerSurgicalNotes(apiList: List<EntSurgicalInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (serverItem in apiList) {
                val localItem = entRepository.getSurgicalNotesByPatientAndCamp(serverItem.patientId, serverItem.campId, serverItem.uniqueId, serverItem.userId)

                if (localItem != null) {
                    if (!serverItem.isSameAsLocal(localItem)) {
                        Log.d("PreOpSync", "Updating local data for patientId=${serverItem.patientId}, campId=${serverItem.campId}")
                        val updatedEntity = serverItem.toEntity()
                        updatedEntity.uniqueId = localItem.uniqueId
                        updatedEntity.app_id = localItem.app_id
                        entRepository.updateSurgicalNotesDetails(updatedEntity)
                    }
                } else {
                    Log.d("PreOpSync", "Inserting new data for patientId=${serverItem.patientId}, campId=${serverItem.campId}")
                    val newEntity = serverItem.toEntity()
                    entRepository.insertSurgicalNotesDetails(newEntity)
                }
            }
        }
    }

    fun EntSurgicalInstruction.isSameAsLocal(local: SurgicalNotesEntity): Boolean {
        return this.lignocaineSensitive == local.lignocaineSensitive &&
                this.xylocaineSensitive == local.xylocaineSensitive &&
                this.localApplicationDone == local.localApplicationDone &&
                this.localInfiltrationDone == local.localInfiltrationDone &&
                this.nerveBlock == local.nerveBlock &&
                this.generalEndotrachealUsed == local.generalEndotrachealUsed &&
                this.pulseMonitored == local.pulseMonitored &&
                this.respirationMonitored == local.respirationMonitored &&
                this.bpMonitored == local.bpMonitored &&
                this.ecgMonitored == local.ecgMonitored &&
                this.temperatureMonitored == local.temperatureMonitored &&
                this.antibioticGiven == local.antibioticGiven &&
                this.ethamsylateGiven == local.ethamsylateGiven &&
                this.adrenalinInfiltrationDone == local.adrenalinInfiltrationDone &&
                this.earWaxRemovalDone == local.earWaxRemovalDone &&
                this.tympanoplastyDone == local.tympanoplastyDone &&
                this.mastoidectomyDone == local.mastoidectomyDone &&
                this.foreignBodyRemovalDone == local.foreignBodyRemovalDone &&
                this.grommentInsertionDone == local.grommentInsertionDone &&
                this.excisionBiopsyDone == local.excisionBiopsyDone &&
                this.other == local.other &&
                this.pulseValue == local.pulseValue &&
                this.bpSystolic == local.bpSystolic &&
                this.bpDiastolic == local.bpDiastolic &&
                this.respirationValue == local.respirationValue &&
                this.temperatureValue == local.temperatureValue &&
                this.temperatureUnit == local.temperatureUnit &&
                this.ecgDetail == local.ecgDetail &&
                this.antibioticDetail == local.antibioticDetail
    }

    fun EntSurgicalInstruction.toEntity(): SurgicalNotesEntity {
        return SurgicalNotesEntity(
            uniqueId = 0,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            lignocaineSensitive = this.lignocaineSensitive,
            xylocaineSensitive = this.xylocaineSensitive,
            localApplicationDone = this.localApplicationDone,
            localInfiltrationDone = this.localInfiltrationDone,
            nerveBlock = this.nerveBlock,
            generalEndotrachealUsed = this.generalEndotrachealUsed,
            pulseMonitored = this.pulseMonitored,
            respirationMonitored = this.respirationMonitored,
            bpMonitored = this.bpMonitored,
            ecgMonitored = this.ecgMonitored,
            temperatureMonitored = this.temperatureMonitored,
            antibioticGiven = this.antibioticGiven,
            ethamsylateGiven = this.ethamsylateGiven,
            adrenalinInfiltrationDone = this.adrenalinInfiltrationDone,
            earWaxRemovalDone = this.earWaxRemovalDone,
            tympanoplastyDone = this.tympanoplastyDone,
            mastoidectomyDone = this.mastoidectomyDone,
            foreignBodyRemovalDone = this.foreignBodyRemovalDone,
            grommentInsertionDone = this.grommentInsertionDone,
            excisionBiopsyDone = this.excisionBiopsyDone,
            other = this.other,
            pulseValue = this.pulseValue,
            bpSystolic = this.bpSystolic,
            bpDiastolic = this.bpDiastolic,
            respirationValue = this.respirationValue,
            temperatureValue = this.temperatureValue,
            temperatureUnit = this.temperatureUnit,
            ecgDetail = this.ecgDetail,
            antibioticDetail = this.antibioticDetail,
            app_id = this.appId
        )
    }



}