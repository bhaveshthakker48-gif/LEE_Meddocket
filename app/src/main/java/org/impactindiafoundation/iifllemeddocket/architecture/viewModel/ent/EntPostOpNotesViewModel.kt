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
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntPostOpInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntSurgicalInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.repository.EntRepository
import javax.inject.Inject

@HiltViewModel
class EntPostOpNotesViewModel @Inject constructor(private val entRepository: EntRepository) : ViewModel() {

    val All_Ent_Post_Follow_ups:LiveData<List<EntPostOpNotesEntity>> = entRepository.All_Ent_Post_Follow_ups

    private var _insertionStatus = MutableLiveData<Resource<Long>>()
    val insertionStatus: LiveData<Resource<Long>> get() = _insertionStatus

    private var _PostOPDetailsListById = MutableLiveData<Resource<List<EntPostOpNotesEntity>>>()
    val postOPDetailsListById: LiveData<Resource<List<EntPostOpNotesEntity>>> get() = _PostOPDetailsListById

    fun insertPostOpNotes(postOpNotesEntity: EntPostOpNotesEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val message =  entRepository.insertPostOpNotes(postOpNotesEntity)
            if (message == null || message == -1L) {
                _insertionStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertionStatus.postValue(Resource.success(message))
            }
        }
    }

    fun getPostOpDetailsById(localPatientId: Int, patientId : Int) = CoroutineScope(Dispatchers.IO).launch {
        _PostOPDetailsListById.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getPostListById(localPatientId, patientId).let {
                    _PostOPDetailsListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _PostOPDetailsListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            _PostOPDetailsListById.postValue(Resource.error(e.message.toString(), null))
        }
    }



    suspend fun getUnsyncedPostOpNotes(): List<EntPostOpNotesEntity> {
        return entRepository.getUnsyncedPostOpNotes()
    }


    fun sendDoctorPostOpNotesToServer(symptoms: List<EntPostOpNotesEntity>, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("SyncCheck PostOpNotes", "Sending to server: ${symptoms.map { it.uniqueId }}")

                val response = entRepository.sendDoctorPostOpNotesToServer(symptoms)

                if (response.success && response.data != null) {
                    Log.d("SyncCheck PostOpNotes", "Received response: ${response.data.results.size} results")
                    Log.d("SyncCheck PostOpNotes", "Received response: ${response.success} success")
                    Log.d("SyncCheck PostOpNotes", "Received response: ${response.message} message")


                    val matchedItems = symptoms.filter { it.app_id.isNullOrBlank() }

                    response.data.results.forEachIndexed { index, result ->
                        if (index < matchedItems.size) {
                            val item = matchedItems[index]
                            Log.d("SyncCheck PostOpNotes", "Updating app_id for local id: ${item.uniqueId} → server app_id: ${result.app_id}")
                            entRepository.updatePostOpNotesAppId(item.uniqueId, result.app_id)


                            Log.d("SyncCheck PatientReport", "Updating app_id in ent_patient_report for patientId=${item.patientId} → app_id=${result.app_id}")
                            entRepository.updatePatientReportAppId(item.patientId, item.uniqueId, result.app_id)

                        } else {
                            Log.w("SyncCheck PostOpNotes", "Result index exceeds matched item list size")
                        }
                    }
                } else {
                    Log.w("SyncCheck PostOpNotes", "Server returned failure: ${response.message}")
                }

                onResult(response.success, response.message)
            } catch (e: Exception) {
                Log.e("SyncCheck PostOpNotes", "Error while sending to server: ${e.localizedMessage}")
                onResult(false, e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun clearSyncedPostOpNotes() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedPostOpNotes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Get Updated Data From Server
    private val _entPostOpNotesFromServer = MutableLiveData<Resource<List<EntPostOpInstruction>>>()

    val entPostOpNotesFromServer: LiveData<Resource<List<EntPostOpInstruction>>> = _entPostOpNotesFromServer

    fun getUpdatePostOpNotesFromServer() = viewModelScope.launch {
        _entPostOpNotesFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdatePostOpNotesFromServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("SurgicalNotesResponse", "Item #$index: $item")
                }

                _entPostOpNotesFromServer.postValue(Resource.success(items))

                syncServerPostOpNotes(items)
            } else {
                Log.e("SurgicalNotesResponse", "API Error: ${response.code()} ${response.message()}")
                _entPostOpNotesFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("SurgicalNotesResponse", "Exception: ${e.message}", e)
            _entPostOpNotesFromServer.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun syncServerPostOpNotes(apiList: List<EntPostOpInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (serverItem in apiList) {
                val localItem = entRepository.getPostOpNotesByPatientAndCamp(serverItem.patientId, serverItem.campId, serverItem.uniqueId, serverItem.userId)

                if (localItem != null) {
                    if (!serverItem.isSameAsLocal(localItem)) {
                        Log.d("PreOpSync", "Updating local data for patientId=${serverItem.patientId}, campId=${serverItem.campId}")
                        val updatedEntity = serverItem.toEntity()
                        updatedEntity.uniqueId = localItem.uniqueId
                        updatedEntity.app_id = localItem.app_id
                        entRepository.updatePostOpNotesDetails(updatedEntity)
                    }
                } else {
                    Log.d("PreOpSync", "Inserting new data for patientId=${serverItem.patientId}, campId=${serverItem.campId}")
                    val newEntity = serverItem.toEntity()
                    entRepository.insertPostOpNotes(newEntity)
                }
            }
        }
    }

    fun EntPostOpInstruction.isSameAsLocal(local: EntPostOpNotesEntity): Boolean {
        return this.ivHydrationGiven == local.ivHydrationGiven &&
                this.npoTill == local.npoTill &&
                this.clearFluidsStartTime == local.clearFluidsStartTime &&
                this.paracetamolGiven == local.paracetamolGiven &&
                this.antibioticClavulanateGiven == local.antibioticClavulanateGiven &&
                this.ofloxacinGiven == local.ofloxacinGiven &&
                this.otherMedicationsNote == local.otherMedicationsNote &&
                this.wickDrainUsed == local.wickDrainUsed &&
                this.redivacUsed == local.redivacUsed &&
                this.watchForSoakage == local.watchForSoakage &&
                this.watchForHematoma == local.watchForHematoma &&
                this.watchForMiddleEarInfection == local.watchForMiddleEarInfection &&
                this.watchForWoundInfection == local.watchForWoundInfection &&
                this.watchForWoundDehiscence == local.watchForWoundDehiscence &&
                this.watchForFacialPalsy == local.watchForFacialPalsy &&
                this.sutureRemovalDate == local.sutureRemovalDate &&
                this.audiogramResult == local.audiogramResult &&
                this.audiogramDate == local.audiogramDate &&
                this.hasComplicationInfection == local.hasComplicationInfection &&
                this.otherComplicationsNote == local.otherComplicationsNote &&
                this.tympanicMembraneStatus == local.tympanicMembraneStatus &&
                this.otorrhoeaPresent == local.otorrhoeaPresent &&
                this.airConductionThresholdDb == local.airConductionThresholdDb &&
                this.hearingThresholdDate == local.hearingThresholdDate &&
                this.ivHyderationDetail == local.ivHyderationDetail
    }


    fun EntPostOpInstruction.toEntity(): EntPostOpNotesEntity {
        return EntPostOpNotesEntity(
            uniqueId = 0,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            ivHydrationGiven = this.ivHydrationGiven,
            npoTill = this.npoTill,
            clearFluidsStartTime = this.clearFluidsStartTime,
            paracetamolGiven = this.paracetamolGiven,
            antibioticClavulanateGiven = this.antibioticClavulanateGiven,
            ofloxacinGiven = this.ofloxacinGiven,
            otherMedicationsNote = this.otherMedicationsNote,
            wickDrainUsed = this.wickDrainUsed,
            redivacUsed = this.redivacUsed,
            watchForSoakage = this.watchForSoakage,
            watchForHematoma = this.watchForHematoma,
            watchForMiddleEarInfection = this.watchForMiddleEarInfection,
            watchForWoundInfection = this.watchForWoundInfection,
            watchForWoundDehiscence = this.watchForWoundDehiscence,
            watchForFacialPalsy = this.watchForFacialPalsy,
            sutureRemovalDate = this.sutureRemovalDate,
            audiogramResult = this.audiogramResult,
            audiogramDate = this.audiogramDate,
            hasComplicationInfection = this.hasComplicationInfection,
            otherComplicationsNote = this.otherComplicationsNote,
            tympanicMembraneStatus = this.tympanicMembraneStatus,
            otorrhoeaPresent = this.otorrhoeaPresent,
            airConductionThresholdDb = this.airConductionThresholdDb,
            hearingThresholdDate = this.hearingThresholdDate,
            ivHyderationDetail = this.ivHyderationDetail,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            app_id = this.appId
        )
    }


}