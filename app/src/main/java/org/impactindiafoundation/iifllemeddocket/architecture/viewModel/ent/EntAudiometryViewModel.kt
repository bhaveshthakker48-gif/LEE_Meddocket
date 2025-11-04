package org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.repository.EntRepository
import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.impactindiafoundation.iifllemeddocket.BaseApplication
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImageModel
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImageSyncResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.AudiometryDetailsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.AudiometryImageDetailsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.EntPostOpInstruction
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@HiltViewModel
class EntAudiometryViewModel @Inject constructor(
    private val entRepository: EntRepository,
    application: Application
) : AndroidViewModel(application) {
    val All_Ent_Audiometry_Follow_ups: LiveData<List<AudiometryEntity>> =
        entRepository.All_Ent_Audiometry_Follow_ups

    val getEntAudiometrySyncData: LiveData<List<AudiometryEntity>> =
        entRepository.getEntAudiometrySyncData("1")

    val getAllAudiometryImages: LiveData<List<AudiometryImageEntity>> =
        entRepository.getAllAudiometryImages

    private var _insertionStatus = MutableLiveData<Resource<Long>>()
    val insertionStatus: LiveData<Resource<Long>> get() = _insertionStatus

    private var _insertionImageStatus = MutableLiveData<Resource<Long>>()
    val insertionImageStatus: LiveData<Resource<Long>> get() = _insertionImageStatus

    private var _audiometryListById = MutableLiveData<Resource<List<AudiometryEntity>>>()
    val audiometryListById: LiveData<Resource<List<AudiometryEntity>>> get() = _audiometryListById

    private var _audiometryImageListById = MutableLiveData<Resource<List<AudiometryImageEntity>>>()
    val audiometryImageListById: LiveData<Resource<List<AudiometryImageEntity>>> get() = _audiometryImageListById

    fun insertAudiometryDetails(audiometryEntity: AudiometryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.insertAudiometryDetails(audiometryEntity)
            if (message == null || message == -1L) {
                _insertionStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertionStatus.postValue(Resource.success(message))
            }
        }
    }

    fun getAudiometryById(localPatientId: Int, patientId: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            _audiometryListById.postValue(Resource.loading(null))
            try {
                try {
                    entRepository.getAudiometryListById(localPatientId, patientId).let {
                        _audiometryListById.postValue(Resource.success(it))
                    }
                } catch (e: Exception) {
                    _audiometryListById.postValue(
                        Resource.error(
                            e.toString(),
                            null
                        )
                    )
                }

            } catch (e: Exception) {
                _audiometryListById.postValue(Resource.error(e.message.toString(), null))
            }
        }

    suspend fun getUnsyncedAudiometryDetails(): List<AudiometryEntity> {
        return entRepository.getUnsyncedAudiometryDetails()
    }

    fun sendDoctorAudiometryDetailsToServer(
        list: List<AudiometryEntity>,
        onSyncCompleted: (syncedCount: Int, unsyncedCount: Int) -> Unit
    ) {
        viewModelScope.launch {
            var syncedCount = 0
            var unsyncedCount = 0

            try {
                Log.d("SyncCheck Audiometry", "Sending to server: ${list.map { it.uniqueId }}")

                val response = entRepository.sendDoctorAudiometryDetailsToServer(list)

                if (response.success && response.data != null) {
                    Log.d("SyncCheck Audiometry", "Received response: ${response.data.results.size} results")
                    Log.d("SyncCheck Audiometry", "Response success: ${response.success} | message: ${response.message}")

                    val matchedItems = list.filter { it.app_id.isNullOrBlank() }

                    response.data.results.forEachIndexed { index, result ->
                        if (index < matchedItems.size) {
                            val item = matchedItems[index]
                            Log.d(
                                "SyncCheck Audiometry",
                                "Updating app_id for local id: ${item.uniqueId} → server app_id: ${result.app_id}"
                            )

                            entRepository.updateAudiometryDetailsAppId(item.uniqueId, result.app_id)
                            entRepository.updatePatientReportAppId(item.patientId, item.uniqueId, result.app_id)

                            syncedCount++
                        } else {
                            unsyncedCount++
                            Log.w("SyncCheck Audiometry", "Result index exceeds matched item list size")
                        }
                    }
                } else {
                    Log.w("SyncCheck Audiometry", "Server returned failure: ${response.message}")
                    unsyncedCount = list.size
                }

                Log.d("SyncCheck Audiometry", "✅ Synced: $syncedCount | ❌ Unsynced: $unsyncedCount")
                onSyncCompleted(syncedCount, unsyncedCount)

            } catch (e: Exception) {
                unsyncedCount = list.size
                Log.e("SyncCheck Audiometry", "Error while sending to server: ${e.localizedMessage}")
                onSyncCompleted(syncedCount, unsyncedCount)
            }
        }
    }


    fun clearSyncedAudiometryDetails() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedAudiometryDetails()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _entAudiometryDetailsFromServer =
        MutableLiveData<Resource<List<AudiometryDetailsInstruction>>>()

    val entAudiometryDetailsFromServer: LiveData<Resource<List<AudiometryDetailsInstruction>>> =
        _entAudiometryDetailsFromServer

    fun getUpdateAudiometryDetailsFromServer() = viewModelScope.launch {
        _entAudiometryDetailsFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdateAudiometryDetailsFromServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("SurgicalNotesResponse", "Item #$index: $item")
                }

                _entAudiometryDetailsFromServer.postValue(Resource.success(items))

                syncServerAudiometryDetails(items)
            } else {
                Log.e(
                    "SurgicalNotesResponse",
                    "API Error: ${response.code()} ${response.message()}"
                )
                _entAudiometryDetailsFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("SurgicalNotesResponse", "Exception: ${e.message}", e)
            _entAudiometryDetailsFromServer.postValue(
                Resource.error(
                    e.message ?: "Unknown Error",
                    null
                )
            )
        }
    }

    fun syncServerAudiometryDetails(apiList: List<AudiometryDetailsInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (serverItem in apiList) {
                val localItem = entRepository.getAudiometryDetailsByPatientAndCamp(
                    serverItem.patientId,
                    serverItem.campId,
                    serverItem.uniqueId,
                    serverItem.userId
                )

                if (localItem != null) {
                    if (!serverItem.isSameAsLocal(localItem)) {
                        Log.d(
                            "PreOpSync",
                            "Updating local data for patientId=${serverItem.patientId}, campId=${serverItem.campId}, userId=${serverItem.userId}"
                        )
                        val updatedEntity = serverItem.toEntity()
                        updatedEntity.uniqueId = localItem.uniqueId
                        updatedEntity.app_id = localItem.app_id
                        entRepository.updateAudiometryDetails(updatedEntity)
                    }
                } else {
                    Log.d(
                        "PreOpSync",
                        "Inserting new data for patientId=${serverItem.patientId}, campId=${serverItem.campId}"
                    )
                    val newEntity = serverItem.toEntity()
                    entRepository.insertAudiometryDetails(newEntity)
                }
            }
        }
    }

    fun AudiometryDetailsInstruction.isSameAsLocal(local: AudiometryEntity): Boolean {
        return this.conductiveLeft == local.conductiveLeft &&
                this.conductiveRight == local.conductiveRight &&
                this.conductiveBilateral == local.conductiveBilateral &&
                this.sensorineuralLeft == local.sensorineuralLeft &&
                this.sensorineuralRight == local.sensorineuralRight &&
                this.sensorineuralBilateral == local.sensorineuralBilateral &&
                this.hearingAidGiven == local.hearingAidGiven &&
                this.hearingAidType == (local.hearingAidType ?: "")
    }

    fun AudiometryDetailsInstruction.toEntity(): AudiometryEntity {
        return AudiometryEntity(
            uniqueId = 0,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            conductiveLeft = this.conductiveLeft,
            conductiveRight = this.conductiveRight,
            conductiveBilateral = this.conductiveBilateral,
            sensorineuralLeft = this.sensorineuralLeft,
            sensorineuralRight = this.sensorineuralRight,
            sensorineuralBilateral = this.sensorineuralBilateral,
            hearingAidGiven = this.hearingAidGiven,
            hearingAidType = this.hearingAidType,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            app_id = this.appId
        )
    }

    //Audiometry Image
    fun insertAudiometryImageDetails(imageEntity: AudiometryImageEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.insertAudiometryImageDetails(imageEntity)
            if (message == null || message == -1L) {
                _insertionImageStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertionImageStatus.postValue(Resource.success(message))
            }
        }
    }

    fun getAudiometryImageById(localPatientId: Int) = CoroutineScope(Dispatchers.IO).launch {
        Log.d("pawan", "getAudiometryImageById() called with localPatientId=$localPatientId")

        _audiometryImageListById.postValue(Resource.loading(null))
        Log.d("pawan", "Loading state posted to _audiometryImageListById")

        try {
            try {
                Log.d("pawan", "Calling repository.getAudiometryImageListById()")
                val result = entRepository.getAudiometryImageListById(localPatientId)
                Log.d("pawan", "Data received from repository: ${Gson().toJson(result)}")

                _audiometryImageListById.postValue(Resource.success(result))
                Log.d("pawan", "Success state posted to _audiometryImageListById")
            } catch (e: Exception) {
                Log.e("pawan", "Inner catch block triggered: ${e.message}", e)
                _audiometryImageListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            Log.e("pawan", "Outer catch block triggered: ${e.message}", e)
            _audiometryImageListById.postValue(Resource.error(e.message.toString(), null))
        }
    }



    private var _audiometryImageSyncResponse =
        MutableLiveData<Resource<EntAudiometryImageResponse>>()
    val audiometryImageSyncResponse: LiveData<Resource<EntAudiometryImageResponse>> get() = _audiometryImageSyncResponse

    suspend fun syncAudiometryImagesNew(
        fileName: MultipartBody.Part,
        patientId: RequestBody,
        campId: RequestBody,
        userId: RequestBody,
        appCreatedDate: RequestBody,
        app_id: RequestBody,
        uniqueId: RequestBody
    ): Response<EntAudiometryImageResponse> {
        return entRepository.syncAudiometryImagesNew(
            fileName, patientId, campId, userId, appCreatedDate, app_id, uniqueId
        )
    }

    fun updateAudiometryImageAppId(id: Int, appId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Audiometry Upload", "ViewModel: Updating app_id in DB for id=$id")
            entRepository.updateAudiometryImageDetailsAppId(id, appId)
        }
    }

    suspend fun getUnSyncedAudiometryImageDetailsNow(): List<AudiometryImageEntity> {
        return entRepository.getUnSyncedAudiometryImageDetailsNow()
    }


    fun clearSyncedAudiometryImage() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedAudiometryImage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private val _entAudiometryImageDetailsFromServer =
        MutableLiveData<Resource<List<AudiometryImageDetailsInstruction>>>()

    val entAudiometryImageDetailsFromServer: LiveData<Resource<List<AudiometryImageDetailsInstruction>>> =
        _entAudiometryImageDetailsFromServer

    private val _audiometryImageBitmap = MutableLiveData<Bitmap>()

    val audiometryImageBitmap: LiveData<Bitmap> = _audiometryImageBitmap
    fun getUpdateAudiometryImageDetailsFromServer() = viewModelScope.launch {
        _entAudiometryImageDetailsFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdateAudiometryImageDetailsFromServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("ImageSync", "Item #$index: $item")
                    item.imageName?.let {
                    }
                }
                syncServerAudiometryImages(items)

                _entAudiometryImageDetailsFromServer.postValue(Resource.success(items))

            } else {
                Log.e(
                    "pawan",
                    "API Error: ${response.code()} ${response.message()}"
                )
                _entAudiometryImageDetailsFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("pawan", "Exception: ${e.message}", e)
            _entAudiometryImageDetailsFromServer.postValue(
                Resource.error(
                    e.message ?: "Unknown Error", null
                )
            )
        }
    }

    fun syncServerAudiometryImages(apiList: List<AudiometryImageDetailsInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (serverItem in apiList) {
                try {
                    val localItem = entRepository.getImageByPatientCampUser(
                        serverItem.patientId,
                        serverItem.campId,
                        serverItem.userId,
                        serverItem.uniqueId
                    )

                    val response = entRepository.downloadAudiometryImage(serverItem.imageName ?: "")
                    if (response.isSuccessful && response.body() != null) {
                        val inputStream = response.body()!!.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        if (bitmap != null) {
                            val localFilePath = saveBitmapToFile(bitmap, serverItem.imageName ?: "default.jpg")

                            if (localItem != null) {
                                if (!serverItem.isSameAsLocal(localItem)) {
                                    val updatedEntity = serverItem.toEntity(localFilePath).copy(id = localItem.id)
                                    entRepository.updateAudiometryImageDetails(updatedEntity)
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


    fun AudiometryImageDetailsInstruction.toEntity(localPath: String): AudiometryImageEntity {
        return AudiometryImageEntity(
            id = 0,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            filename = localPath,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            app_id = "1"
        )
    }

    fun AudiometryImageDetailsInstruction.isSameAsLocal(local: AudiometryImageEntity): Boolean {
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
