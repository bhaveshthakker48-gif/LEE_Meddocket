package org.impactindiafoundation.iifllemeddocket.architecture.viewModel.pathology

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntAudiometryImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.PreOpImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathologyapimodel.PathologyImageResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.AudiometryDetailsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.PathologyDetailsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.PathologyImageDetailsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.model.response.PreOpImageDetailsInstruction
import org.impactindiafoundation.iifllemeddocket.architecture.repository.EntRepository
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class PathologyViewModel @Inject constructor(private val entRepository: EntRepository, application: Application): AndroidViewModel(application) {

    val All_PATHOLOGY_Follow_ups:LiveData<List<PathologyEntity>> = entRepository.All_PATHOLOGY_Follow_ups

    val getAllPathologyImages:LiveData<List<PathologyImageEntity>> =entRepository.getAllPathologyImages

    private var _insertionStatus = MutableLiveData<Resource<Long>>()
    val insertionStatus: LiveData<Resource<Long>> get() = _insertionStatus

    private var _insertionImageStatus = MutableLiveData<Resource<Long>>()
    val insertionImageStatus: LiveData<Resource<Long>> get() = _insertionImageStatus

    private var _pathologyListById = MutableLiveData<Resource<List<PathologyEntity>>>()
    val pathologyListById: LiveData<Resource<List<PathologyEntity>>> get() = _pathologyListById

    fun insertPathologyDetails(pathologyEntity: PathologyEntity) {
        Log.d("ImageDebug", "Inserting pathology data into Room: $pathologyEntity")
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.insertPathologyDetails(pathologyEntity)
            if (message == null || message == -1L) {
                Log.e("ImageDebug", "Insert failed for pathology data")
                _insertionStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                Log.d("ImageDebug", "Inserted successfully with ID: $message")
                _insertionStatus.postValue(Resource.success(message))
            }
        }
    }

    fun getPathologyByFormId( localFormId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _pathologyListById.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getPathologyById(localFormId).let {
                    _pathologyListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _pathologyListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            _pathologyListById.postValue(Resource.error(e.message.toString(), null))
        }
    }

    suspend fun getUnsyncedPathologyDetails(): List<PathologyEntity> {
        return entRepository.getUnSyncedPathologyDetailsNow()
    }

    fun sendDoctorPathologyDetailsToServer(symptoms: List<PathologyEntity>, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("SyncCheck Pathology", "Sending to server: ${symptoms.map { it.uniqueId }}")

                val response = entRepository.sendDoctorPathologyDetailsToServer(symptoms)

                if (response.success && response.data != null) {
                    Log.d("SyncCheck Pathology", "Received response: ${response.data.results.size} results")
                    Log.d("SyncCheck Pathology", "Received response: ${response.success} success")
                    Log.d("SyncCheck Pathology", "Received response: ${response.message} message")


                    val matchedItems = symptoms.filter { it.app_id.isNullOrBlank() }

                    response.data.results.forEachIndexed { index, result ->
                        if (index < matchedItems.size) {
                            val item = matchedItems[index]
                            Log.d("SyncCheck PatientReport", "Updating app_id for local id: ${item.uniqueId} → server app_id: ${result.app_id}")
                            entRepository.updatePathologyDetailsAppId(item.uniqueId, result.app_id)

                            Log.d(
                                "SyncCheck PatientReport",
                                "Calling updatePatientReportAppId with patientId=${item.patientId}, localId=${item.uniqueId}, app_id=${result.app_id}"
                            )
                            entRepository.updatePatientReportAppId(item.patientId, item.uniqueId, result.app_id)
                            Log.d("SyncCheck PatientReport", "Finished updatePatientReportAppId for localId=${item.uniqueId}")
                        } else {
                            Log.w("SyncCheck Pathology", "Result index exceeds matched item list size")
                        }
                    }
                } else {
                    Log.w("SyncCheck Pathology", "Server returned failure: ${response.message}")
                }

                onResult(response.success, response.message)
            } catch (e: Exception) {
                Log.e("SyncCheck Pathology", "Error while sending to server: ${e.localizedMessage}")
                onResult(false, e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun clearSyncedPathologyReports() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedPathologyReports()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _entPathologyDetailsFromServer = MutableLiveData<Resource<List<PathologyDetailsInstruction>>>()

    val entPathologyDetailsFromServer: LiveData<Resource<List<PathologyDetailsInstruction>>> = _entPathologyDetailsFromServer

    fun getUpdatePathologyDetailsServer() = viewModelScope.launch {
        _entPathologyDetailsFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdatePathologyDetailsServer()

            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data

                items.forEachIndexed { index, item ->
                    Log.d("SurgicalNotesResponse", "Item #$index: $item")
                }

                _entPathologyDetailsFromServer.postValue(Resource.success(items))

                syncServerPathologyDetails(items)
            } else {
                Log.e("SurgicalNotesResponse", "API Error: ${response.code()} ${response.message()}")
                _entPathologyDetailsFromServer.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            Log.e("SurgicalNotesResponse", "Exception: ${e.message}", e)
            _entPathologyDetailsFromServer.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun syncServerPathologyDetails(apiList: List<PathologyDetailsInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("SurgicalNotesResponse", "syncServerPathologyDetails called with ${apiList.size} items")

            for ((index, serverItem) in apiList.withIndex()) {
                Log.d("SurgicalNotesResponse", "Processing item #$index : patientId=${serverItem.patientId}, campId=${serverItem.campId}")

                try {
                    val localItem = entRepository.getPathologyDetailsByPatientAndCamp(
                        serverItem.patientId,
                        serverItem.campId,
                        serverItem.uniqueId
                    )
                    Log.d("SurgicalNotesResponse", "Local item: $localItem")

                    if (localItem != null) {
                        if (!serverItem.isSameAsLocal(localItem)) {
                            Log.d("SurgicalNotesResponse", "Updating local data for patientId=${serverItem.patientId}, campId=${serverItem.campId}")
                            val updatedEntity = serverItem.toEntity()
                            updatedEntity.uniqueId = localItem.uniqueId
                            updatedEntity.app_id = localItem.app_id
                            entRepository.updatePathologyDetails(updatedEntity)
                        }
                    } else {
                        Log.d("SurgicalNotesResponse", "Inserting new data for patientId=${serverItem.patientId}, campId=${serverItem.campId}")
                        val newEntity = serverItem.toEntity()
                        entRepository.insertPathologyDetails(newEntity)
                    }
                } catch (e: Exception) {
                    Log.e("SurgicalNotesResponse", "Exception inside loop: ${e.message}", e)
                }
            }

            Log.d("SurgicalNotesResponse", "Finished syncing pathology details")
        }
    }


    fun PathologyDetailsInstruction.isSameAsLocal(local: PathologyEntity): Boolean {
        return this.cbc == local.cbc &&
                (if (this.cbc) this.cbcValue else "") == local.cbcValue &&
                this.bt == local.bt &&
                (if (this.bt) this.btValue else "") == local.btValue &&
                this.ct == local.ct &&
                (if (this.ct) this.ctValue else "") == local.ctValue &&
                this.hiv == local.hiv &&
                (if (this.hiv) this.hivValue else "") == local.hivValue &&
                this.hbsag == local.hbsag &&
                (if (this.hbsag) this.hbsagValue else "") == local.hbsagValue &&
                this.pta == local.pta &&
                (if (this.pta) this.ptaValue else "") == local.ptaValue &&
                this.impedanceAudiometry == local.impedanceAudiometry &&
                (if (this.impedanceAudiometry) this.impedanceAudiometryValue else "") == local.impedanceAudiometryValue
    }



    fun PathologyDetailsInstruction.toEntity(): PathologyEntity {
        return PathologyEntity(
            uniqueId = 0,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            cbc = this.cbc,
            cbcValue = if (this.cbc) this.cbcValue else "",
            bt = this.bt,
            btValue = if (this.bt) this.btValue else "",
            ct = this.ct,
            ctValue = if (this.ct) this.ctValue else "",
            hiv = this.hiv,
            hivValue = if (this.hiv) this.hivValue else "",
            hbsag = this.hbsag,
            hbsagValue = if (this.hbsag) this.hbsagValue else "",
            pta = this.pta,
            ptaValue = if (this.pta) this.ptaValue else "",
            impedanceAudiometry = this.impedanceAudiometry,
            impedanceAudiometryValue = if (this.impedanceAudiometry) this.impedanceAudiometryValue else "",
            appCreatedDate = null,
            app_id = this.appId
        )
    }


    // Pathology Image
    fun insertPathologyImage(imageEntity: PathologyImageEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("ImageDebug", "Inserting to DB: ${Gson().toJson(imageEntity)}")
            val message = entRepository.insertPathologyImage(imageEntity)
            Log.d("ImageDebug", "Insert successful for: ${imageEntity.reportType}")
            if (message == null || message == -1L) {
                _insertionImageStatus.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertionImageStatus.postValue(Resource.success(message))
            }
        }
    }

    fun imageAlreadyExists(patientId: Int, reportType: String): Boolean {
        val existingImages = pathologyImageListById.value?.data ?: return false
        return existingImages.any { it.patientId == patientId && it.reportType == reportType }
    }

    fun updateImageInRoom(patientId: Int, reportType: String, newPath: String) {
        val date = ConstantsApp.getCurrentDate()
        viewModelScope.launch {
            try {
                entRepository.updateImage(patientId, reportType, newPath, date)
            } catch (e: Exception) {
                Log.e("ImageDebug", "Failed to update image: ${e.message}")
            }
        }
    }



    val pathologyImageListById = MutableLiveData<Resource<List<PathologyImageEntity>>>()

    fun getImagesByFormId( localFormId: Int) {
        pathologyImageListById.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                entRepository.getImagesByPatientId( localFormId).observeForever {
                    pathologyImageListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                pathologyImageListById.postValue(Resource.error("Error fetching images", null))
            }
        }
    }

    suspend fun syncPathologyImagesNew(
        fileName: MultipartBody.Part,
        patientId: RequestBody,
        campId: RequestBody,
        userId: RequestBody,
        appCreatedDate: RequestBody,
        reportType : RequestBody,
        app_id: RequestBody,
        uniqueId : RequestBody
    ): Response<PathologyImageResponse> {
        return entRepository.syncPathologyImagesNew(
            fileName, patientId, campId, userId, appCreatedDate, reportType, app_id, uniqueId
        )
    }

    fun updatePathologyImageAppId(id: Int, appId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Pathology Upload", "ViewModel: Updating app_id in DB for id=$id")
            entRepository.updatePathologyImageAppId(id, appId)
        }
    }

    suspend fun getUnSyncedPathologyImageDetailsNow(): List<PathologyImageEntity> {
        return entRepository.getUnSyncedPathologyImageDetailsNow()
    }

    fun clearSyncedPathologyImage() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedPathologyImage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _entPreOpImageDetailsFromServer =
        MutableLiveData<Resource<List<PathologyImageDetailsInstruction>>>()

    val entPreOpImageDetailsFromServer: LiveData<Resource<List<PathologyImageDetailsInstruction>>> =
        _entPreOpImageDetailsFromServer

    private val _audiometryImageBitmap = MutableLiveData<Bitmap>()

    val audiometryImageBitmap: LiveData<Bitmap> = _audiometryImageBitmap
    fun getUpdatedPathologyImageDetailsFromServer() = viewModelScope.launch {
        _entPreOpImageDetailsFromServer.postValue(Resource.loading(null))
        try {
            val response = entRepository.getUpdatePathologyImageDetailsFromServer()

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

    fun syncServerAudiometryImages(apiList: List<PathologyImageDetailsInstruction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (serverItem in apiList) {
                try {
                    val localItem = entRepository.getPathologyImageByPatientCampUser(
                        serverItem.patientId,
                        serverItem.campId,
                        serverItem.userId,
                        serverItem.reportType,
                        serverItem.uniqueId
                    )

                    val response = entRepository.downloadPathologyImage(serverItem.imageName ?: "")
                    if (response.isSuccessful && response.body() != null) {
                        val inputStream = response.body()!!.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        if (bitmap != null) {
                            val localFilePath = saveBitmapToFile(bitmap, serverItem.imageName ?: "default.jpg")

                            if (localItem != null) {
                                if (!serverItem.isSameAsLocal(localItem)) {
                                    val updatedEntity = serverItem.toEntity(localFilePath).copy(id = localItem.id)
                                    entRepository.updatePathologyImageDetails(updatedEntity)
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


    fun PathologyImageDetailsInstruction.toEntity(localPath: String): PathologyImageEntity {
        return PathologyImageEntity(
            id = 0,
            patientId = this.patientId,
            campId = this.campId,
            userId = this.userId,
            filename = localPath,
            appCreatedDate = ConstantsApp.getCurrentDate(),
            reportType = this.reportType,
            formId = this.uniqueId,
            app_id = "1"
        )
    }

    fun PathologyImageDetailsInstruction.isSameAsLocal(local: PathologyImageEntity): Boolean {
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