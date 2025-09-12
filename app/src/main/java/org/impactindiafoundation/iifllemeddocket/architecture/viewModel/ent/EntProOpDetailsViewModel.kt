package org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomEarItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.PreOpImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.AudiometryImageDetailsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.PreOpImageDetailsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.PreSurgeryInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.repository.EntRepository
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class EntProOpDetailsViewModel @Inject constructor(private val entRepository: EntRepository, application: Application
) : AndroidViewModel(application){

    val All_Ent_Pro_Op_Follow_ups:LiveData<List<EntPreOpDetailsEntity>> = entRepository.All_Ent_Pro_Op_Follow_ups

    private var _insertionStatus = MutableLiveData<Resource<Long>>()
    val insertionStatus: LiveData<Resource<Long>> get() = _insertionStatus

    private var _PreOPDetailsListById = MutableLiveData<Resource<List<EntPreOpDetailsEntity>>>()
    val preOPDetailsListById: LiveData<Resource<List<EntPreOpDetailsEntity>>> get() = _PreOPDetailsListById

    fun insertPreOpDetails(preOpDetails: EntPreOpDetailsEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.insertPreOpDetails(preOpDetails)
            if (message == null || message == -1L) {
                _insertionStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertionStatus.postValue(Resource.success(message))
            }
        }
    }

    fun getPreOpDetailsById(localPatientId: Int, patientId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _PreOPDetailsListById.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getPreOpListById(localPatientId, patientId).let {
                    _PreOPDetailsListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _PreOPDetailsListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            _PreOPDetailsListById.postValue(Resource.error(e.message.toString(), null))
        }
    }

    suspend fun getUnsyncedPreOpDetails(): List<EntPreOpDetailsEntity> {
        return entRepository.getUnsyncedPreOpDetails()
    }


    fun sendDoctorPreOpDetailsToServer(symptoms: List<EntPreOpDetailsEntity>, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("SyncCheck PreOpDetails", "Sending to server: ${symptoms.map { it.uniqueId }}")

                val response = entRepository.sendDoctorPreOpDetailsToServer(symptoms)

                if (response.success && response.data != null) {
                    Log.d("SyncCheck PreOpDetails", "Received response: ${response.data.results.size} results")
                    Log.d("SyncCheck PreOpDetails", "Received response: ${response.success} success")
                    Log.d("SyncCheck PreOpDetails", "Received response: ${response.message} message")


                    val matchedItems = symptoms.filter { it.app_id.isNullOrBlank() }

                    response.data.results.forEachIndexed { index, result ->
                        if (index < matchedItems.size) {
                            val item = matchedItems[index]
                            Log.d("SyncCheck PreOpDetails", "Updating app_id for local id: ${item.uniqueId} → server app_id: ${result.app_id}")
                            entRepository.updatePreOpDetailsAppId(item.uniqueId, result.app_id)

                            entRepository.updatePatientReportAppId(item.patientId, item.uniqueId, result.app_id)

                        } else {
                            Log.w("SyncCheck PreOpDetails", "Result index exceeds matched item list size")
                        }
                    }
                } else {
                    Log.w("SyncCheck PreOpDetails", "Server returned failure: ${response.message}")
                }

                onResult(response.success, response.message)
            } catch (e: Exception) {
                Log.e("SyncCheck PreOpDetails", "Error while sending to server: ${e.localizedMessage}")
                onResult(false, e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun clearSyncedPreOpDetails() {
        viewModelScope.launch {
            try {
                entRepository.deleteSyncedPreOpDetails()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Get Updated Data From Server
    private val _entPreOpDetailsFromServer = MutableLiveData<Resource<List<PreSurgeryInstruction>>>()

    val entPreOpDetailsFromServer: LiveData<Resource<List<PreSurgeryInstruction>>> = _entPreOpDetailsFromServer

    fun getUpdatePreOpDetailsFromServer() = viewModelScope.launch {
        _entPreOpDetailsFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdatePreOpDetailsFromServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("PreOpResponse", "Item #$index: $item")
                }

                _entPreOpDetailsFromServer.postValue(Resource.success(items))

                syncServerPreOpData(items)
            } else {
                Log.e("PreOpResponse", "API Error: ${response.code()} ${response.message()}")
                _entPreOpDetailsFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("PreOpResponse", "Exception: ${e.message}", e)
            _entPreOpDetailsFromServer.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun syncServerPreOpData(apiList: List<PreSurgeryInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (serverItem in apiList) {
                val localItem = entRepository.getPreOpByPatientAndCamp(serverItem.patientId, serverItem.campId, serverItem.uniqueId, serverItem.userId)

                if (localItem != null) {
                    if (!serverItem.isSameAsLocal(localItem)) {
                        val updatedEntity = serverItem.toEntity()
                        updatedEntity.uniqueId = localItem.uniqueId
                        updatedEntity.app_id = localItem.app_id
                        Log.d("SyncCheck PreOpDetails", "Updating entity in DB: id=${updatedEntity.uniqueId}, appId=${updatedEntity.app_id}")
                        entRepository.updatePreOpDetails(updatedEntity)
                    }
                } else {
                    Log.d("PreOpSync", "Inserting new data for patientId=${serverItem.patientId}, campId=${serverItem.campId}")
                    val newEntity = serverItem.toEntity()
                    entRepository.insertPreOpDetails(newEntity)
                }
            }
        }
    }

    fun PreSurgeryInstruction.isSameAsLocal(local: EntPreOpDetailsEntity): Boolean {
        return this.injTT == local.injtt &&
                this.adultTab == local.adulttab &&
                this.adultSolidOrLiquid == local.adultsolidorliquid &&
                this.adultSolidOrLiquidTime == local.adultsolidorliquidTime &&
                this.adultWaterOrLiquid == local.adultwaterorliquid &&
                this.adultWaterOrLiquidTime == local.adultwaterorliquidTime &&
                this.childrenSolidOrLiquid == local.childrensolidorliquid &&
                this.childrenSolidOrLiquidTime == local.childrensolidorliquidTime &&
                this.childrenBreastfed == local.childrenbreastfed &&
                this.childrenBreastfedTime == local.childrenbreastfedTime &&
                this.childrenWaterOrLiquid == local.childrenwaterorliquid &&
                this.childrenWaterOrLiquidTime == local.childrenwaterorliquidTime &&
                this.currentMedicine == local.currentedicine &&
                this.otherInstructions == local.otherInstructions &&
                this.otherInstructionsDetail == local.otherInstructionsDetail &&
                this.surgeryCancel == local.surgeryCancel &&
                this.surgeryCancelReason == local.surgeryCancelReason
    }

    fun PreSurgeryInstruction.toEntity(): EntPreOpDetailsEntity {
        return EntPreOpDetailsEntity(
            uniqueId = 0,
            app_id = this.appId,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            injtt = this.injTT,
            adulttab = this.adultTab,
            childrensolidorliquid = this.childrenSolidOrLiquid,
            childrenbreastfed = this.childrenBreastfed,
            childrenwaterorliquid = this.childrenWaterOrLiquid,
            adultsolidorliquid = this.adultSolidOrLiquid,
            adultwaterorliquid = this.adultWaterOrLiquid,
            currentedicine = this.currentMedicine,
            childrensolidorliquidTime = this.childrenSolidOrLiquidTime,
            childrenbreastfedTime = this.childrenBreastfedTime,
            childrenwaterorliquidTime = this.childrenWaterOrLiquidTime,
            adultsolidorliquidTime = this.adultSolidOrLiquidTime,
            adultwaterorliquidTime = this.adultWaterOrLiquidTime,
            otherInstructions = this.otherInstructions,
            otherInstructionsDetail = this.otherInstructionsDetail,
            surgeryCancel = this.surgeryCancel,
            surgeryCancelReason = this.surgeryCancelReason
        )
    }

    //Image
    val getAllPreOpImages:LiveData<List<PreOpImageEntity>> =entRepository.getAllPreOpImages

    private var _insertionImageStatus = MutableLiveData<Resource<Long>>()
    val insertionImageStatus: LiveData<Resource<Long>> get() = _insertionImageStatus

    private var _preOpImageListById = MutableLiveData<Resource<List<PreOpImageEntity>>>()
    val preOpImageListById: LiveData<Resource<List<PreOpImageEntity>>> get() = _preOpImageListById
    fun saveConcentFormImageLocally(imageEntity: PreOpImageEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.saveConcentFormImageLocally(imageEntity)
            if (message == null || message == -1L) {
                _insertionImageStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertionImageStatus.postValue(Resource.success(message))
            }
        }
    }

    fun getPreOpImageById(localPatientId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _preOpImageListById.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getPreOpImageById(localPatientId).let {
                    _preOpImageListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _preOpImageListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            _preOpImageListById.postValue(Resource.error(e.message.toString(), null))
        }
    }

    private var _preOpImageSyncResponse = MutableLiveData<Resource<EntPreOpImageResponse>>()
    val preOpImageSyncResponse: LiveData<Resource<EntPreOpImageResponse>> get() = _preOpImageSyncResponse

    suspend fun syncPreOpImagesNew(
        fileName: MultipartBody.Part,
        patientId: RequestBody,
        campId: RequestBody,
        userId: RequestBody,
        appCreatedDate: RequestBody,
        app_id: RequestBody,
        uniqueId : RequestBody
    ): Response<EntPreOpImageResponse> {
        return entRepository.syncPreOpImagesNew(
            fileName, patientId, campId, userId, appCreatedDate, app_id, uniqueId
        )
    }

    fun updatePreOpImageDetailsAppId(id: Int, appId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Audiometry Upload", "ViewModel: Updating app_id in DB for id=$id")
            entRepository.updatePreOpImageDetailsAppId(id, appId)
        }
    }

    suspend fun getUnSyncedPreOpImageDetailsNow(): List<PreOpImageEntity> {
        return entRepository.getUnSyncedPreOpImageDetailsNow()
    }

    fun clearSyncedPreOpImage() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedPreOpImage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _entPreOpImageDetailsFromServer =
        MutableLiveData<Resource<List<PreOpImageDetailsInstruction>>>()

    val entPreOpImageDetailsFromServer: LiveData<Resource<List<PreOpImageDetailsInstruction>>> =
        _entPreOpImageDetailsFromServer

    private val _audiometryImageBitmap = MutableLiveData<Bitmap>()

    val audiometryImageBitmap: LiveData<Bitmap> = _audiometryImageBitmap
    fun getUpdatePreOpImageDetailsFromServer() = viewModelScope.launch {
        _entPreOpImageDetailsFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdatePreOpImageDetailsFromServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("ImageSync", "Item #$index: $item")
                    item.imageName?.let {
                    }
                }
                syncServerAudiometryImages(items)

                _entPreOpImageDetailsFromServer.postValue(Resource.success(items))

            } else {
                Log.e(
                    "pawan",
                    "API Error: ${response.code()} ${response.message()}"
                )
                _entPreOpImageDetailsFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("pawan", "Exception: ${e.message}", e)
            _entPreOpImageDetailsFromServer.postValue(
                Resource.error(
                    e.message ?: "Unknown Error", null
                )
            )
        }
    }

    fun syncServerAudiometryImages(apiList: List<PreOpImageDetailsInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (serverItem in apiList) {
                try {
                    val localItem = entRepository.getPreOpImageByPatientCampUser(
                        serverItem.patientId,
                        serverItem.campId,
                        serverItem.userId,
                        serverItem.uniqueId
                    )

                    val response = entRepository.downloadPreOpImage(serverItem.imageName ?: "")
                    if (response.isSuccessful && response.body() != null) {
                        val inputStream = response.body()!!.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        if (bitmap != null) {
                            val localFilePath = saveBitmapToFile(bitmap, serverItem.imageName ?: "default.jpg")

                            if (localItem != null) {
                                if (!serverItem.isSameAsLocal(localItem)) {
                                    val updatedEntity = serverItem.toEntity(localFilePath).copy(id = localItem.id)
                                    entRepository.updatePreOpImageDetails(updatedEntity)
                                    Log.d("pawan", "Updated image for patientId=${serverItem.patientId}")
                                }
                            }
                        }
                    } else {
                        Log.e("pawan", "Image download failed: ${serverItem.imageName}")
                    }
                } catch (e: Exception) {
                    Log.e("pawan", "Exception syncing image: ${e.message}", e)
                }
            }
        }
    }


    fun PreOpImageDetailsInstruction.toEntity(localPath: String): PreOpImageEntity {
        return PreOpImageEntity(
            id = 0,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            filename = localPath,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            app_id = "1"
        )
    }

    fun PreOpImageDetailsInstruction.isSameAsLocal(local: PreOpImageEntity): Boolean {
        return this.imageName == File(local.filename).name
    }

    private fun saveBitmapToFile(bitmap: Bitmap, imageName: String): String {
        val file = File(getApplication<Application>().filesDir, imageName)
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return file.absolutePath
    }




}