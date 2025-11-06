package org.impactindiafoundation.iifllemeddocket.architecture.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisTypeModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.Equipment
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImage
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImageSyncResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideoRquest
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideos
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisFormSyncResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisSynTable
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModelItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OrthosisMainViewModel @Inject constructor(private val newMainRepository: NewMainRepository) :
    ViewModel() {

    private var _othosisMasterResponse = MutableLiveData<Resource<List<OrthosisTypeModelItem>>>()
    val othosisMasterResponse: LiveData<Resource<List<OrthosisTypeModelItem>>> get() = _othosisMasterResponse

    private var _insertOrthosisResponse = MutableLiveData<Resource<Long>>()
    val insertOrthosisResponse: LiveData<Resource<Long>> get() = _insertOrthosisResponse

    private var _campPatienDetailsResponse = MutableLiveData<Resource<CampPatientData>>()
    val campPatienDetailsResponse: LiveData<Resource<CampPatientData>> get() = _campPatienDetailsResponse

    private var _formImagesList = MutableLiveData<Resource<List<FormImages>>>()
    val formImagesList: LiveData<Resource<List<FormImages>>> get() = _formImagesList

    private var _formImagesListForSync = MutableLiveData<Resource<List<FormImages>>>()
    val formImagesListForSync: LiveData<Resource<List<FormImages>>> get() = _formImagesListForSync


    private var _orthosisImagesList = MutableLiveData<Resource<List<OrthosisImages>>>()
    val orthosisImagesList: LiveData<Resource<List<OrthosisImages>>> get() = _orthosisImagesList

    private var _orthosisImagesListForSync = MutableLiveData<Resource<List<OrthosisImages>>>()
    val orthosisImagesListForSync: LiveData<Resource<List<OrthosisImages>>> get() = _orthosisImagesListForSync

    private var _equipmentImagesListForSync = MutableLiveData<Resource<List<EquipmentImage>>>()
    val equipmentImagesListForSync: LiveData<Resource<List<EquipmentImage>>> get() = _equipmentImagesListForSync

    private var _equipmentImagesList = MutableLiveData<Resource<List<EquipmentImage>>>()
    val equipmentImagesList: LiveData<Resource<List<EquipmentImage>>> get() = _equipmentImagesList

    private var _formVideosList = MutableLiveData<Resource<List<FormVideos>>>()
    val formVideosList: LiveData<Resource<List<FormVideos>>> get() = _formVideosList

    private var _formVideosListForSync = MutableLiveData<Resource<List<FormVideos>>>()
    val formVideosListForSync: LiveData<Resource<List<FormVideos>>> get() = _formVideosListForSync

    private var _campPatientList = MutableLiveData<Resource<List<CampPatientDataItem>>>()
    val campPatientList: LiveData<Resource<List<CampPatientDataItem>>> get() = _campPatientList

    private var _orthosisPatientFormList = MutableLiveData<Resource<List<OrthosisPatientForm>>>()
    val orthosisPatientFormList: LiveData<Resource<List<OrthosisPatientForm>>> get() = _orthosisPatientFormList

    private var _campDetailsResponse = MutableLiveData<Resource<List<CampModel>>>()
    val campDetailsResponse: LiveData<Resource<List<CampModel>>> get() = _campDetailsResponse

    private var _campForViewReport = MutableLiveData<Resource<List<CampModel>>>()
    val campForViewReport: LiveData<Resource<List<CampModel>>> get() = _campForViewReport

    private var _userList = MutableLiveData<Resource<List<UserModel>>>()
    val userList: LiveData<Resource<List<UserModel>>> get() = _userList

    private var _diagnosisMasterResponse = MutableLiveData<Resource<List<DiagnosisType>>>()
    val diagnosisMasterResponse: LiveData<Resource<List<DiagnosisType>>> get() = _diagnosisMasterResponse

    private var _insertDiagnosisResponse = MutableLiveData<Resource<Long>>()
    val insertDiagnosisResponse: LiveData<Resource<Long>> get() = _insertDiagnosisResponse

    private var _othosisEquipmentMasterResponse = MutableLiveData<Resource<List<Equipment>>>()
    val othosisEquipmentMasterResponse: LiveData<Resource<List<Equipment>>> get() = _othosisEquipmentMasterResponse

    private var _formSyncResponse = MutableLiveData<Resource<FormImageSyncResponse>>()
    val formSyncResponse: LiveData<Resource<FormImageSyncResponse>> get() = _formSyncResponse

    private var _orthosisImageSyncResponse = MutableLiveData<Resource<FormImageSyncResponse>>()
    val orthosisImageSyncResponse: LiveData<Resource<FormImageSyncResponse>> get() = _orthosisImageSyncResponse

    private var _equipmentImageSyncResponse = MutableLiveData<Resource<FormImageSyncResponse>>()
    val equipmentImageSyncResponse: LiveData<Resource<FormImageSyncResponse>> get() = _equipmentImageSyncResponse

    private var _formVideoSyncResponse = MutableLiveData<Resource<FormImageSyncResponse>>()
    val formVideoSyncResponse: LiveData<Resource<FormImageSyncResponse>> get() = _formVideoSyncResponse

    fun getOrthosisMasterApi() = CoroutineScope(Dispatchers.IO).launch {
        _othosisMasterResponse.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOrthosisType().let {
                    _othosisMasterResponse.postValue(Resource.success(it.body()!!.orthosisList))
                }
            } catch (e: Exception) {
                _othosisMasterResponse.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _othosisMasterResponse.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun insertOrthosisMasterLocal(orthosisMaster: OrthosisType) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = newMainRepository.insertOrthosisMasterr(orthosisMaster)
            _insertOrthosisResponse.postValue(Resource.success(message))
            if (message.equals(null)) {
                _insertOrthosisResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertOrthosisResponse.postValue(Resource.success(message))
            }
        }
    }

    fun getCampPatientDetails() = CoroutineScope(Dispatchers.IO).launch {
        _campPatienDetailsResponse.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getCampPatientDetailsApi().let {
                    _campPatienDetailsResponse.postValue(Resource.success(it.body()))
                }
            } catch (e: Exception) {
                _campPatienDetailsResponse.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _campPatienDetailsResponse.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun insertCampPatientDetails(campPatientList: List<CampPatientDataItem>) {
        viewModelScope.launch {
            newMainRepository.insertCampPatientDetails(campPatientList)
        }
    }

    fun getFormImages() = CoroutineScope(Dispatchers.IO).launch {
        _formImagesList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getFormImages().let {
                    _formImagesList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _formImagesList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _formImagesList.postValue(Resource.error(e.message.toString(), null))
        }
    }


    fun insertImageSyncSummary(syncType: String, syncCount: Int, notSyncCount: Int) {
        viewModelScope.launch {
            newMainRepository.insertSyncData(
                synType = syncType,
                syncItemCount = syncCount,
                notSyncItemCount = notSyncCount
            )
        }
    }

//    fun getFormImagesForSync() = CoroutineScope(Dispatchers.IO).launch {
//        _formImagesListForSync.postValue(Resource.loading(null))
//        try {
//            try {
//                newMainRepository.getFormImagesForSync().let {
//                    _formImagesListForSync.postValue(Resource.success(it))
//                }
//            } catch (e: Exception) {
//                _formImagesListForSync.postValue(
//                    Resource.error(
//                        e.toString(),
//                        null
//                    )
//                )
//            }
//        } catch (e: Exception) {
//            _formImagesListForSync.postValue(Resource.error(e.message.toString(), null))
//        }
//    }

    fun getUnsyncedFormImages() {
        viewModelScope.launch {
            _formImagesListForSync.postValue(Resource.loading(null))
            try {
                val list = newMainRepository.getUnsyncedFormImages()
                _formImagesListForSync.postValue(Resource.success(list))
            } catch (e: Exception) {
                _formImagesListForSync.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }


    fun getFormOrthosisImages() = CoroutineScope(Dispatchers.IO).launch {
        _orthosisImagesList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getFormOrthosisImages().let {
                    _orthosisImagesList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _orthosisImagesList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _orthosisImagesList.postValue(Resource.error(e.message.toString(), null))
        }
    }

//    fun getOrthosisImagesForSync() = CoroutineScope(Dispatchers.IO).launch {
//        _orthosisImagesListForSync.postValue(Resource.loading(null))
//        try {
//            try {
//                newMainRepository.getFormOrthosisImages().let {
//                    _orthosisImagesListForSync.postValue(Resource.success(it))
//                }
//            } catch (e: Exception) {
//                _orthosisImagesListForSync.postValue(
//                    Resource.error(
//                        e.toString(),
//                        null
//                    )
//                )
//            }
//        } catch (e: Exception) {
//            _orthosisImagesListForSync.postValue(Resource.error(e.message.toString(), null))
//        }
//    }

    fun getUnsyncedOrthosisImages() = viewModelScope.launch {
        _orthosisImagesListForSync.postValue(Resource.loading(null))
        try {
            val list = newMainRepository.getUnsyncedOrthosisImages()
            _orthosisImagesListForSync.postValue(Resource.success(list))
        } catch (e: Exception) {
            _orthosisImagesListForSync.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun getUnsyncedEquipmentImages() = viewModelScope.launch {
        _equipmentImagesListForSync.postValue(Resource.loading(null))
        try {
            val list = newMainRepository.getUnsyncedEquipmentImages()
            _equipmentImagesListForSync.postValue(Resource.success(list))
        } catch (e: Exception) {
            _equipmentImagesListForSync.postValue(Resource.error(e.message.toString(), null))
        }
    }



    fun getEquipmentImages() = CoroutineScope(Dispatchers.IO).launch {
        _equipmentImagesList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getEquipmentImage().let {
                    _equipmentImagesList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _equipmentImagesList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _equipmentImagesList.postValue(Resource.error(e.message.toString(), null))
        }
    }

//    fun getEquipmentImagesForSync() = CoroutineScope(Dispatchers.IO).launch {
//        _equipmentImagesListForSync.postValue(Resource.loading(null))
//        try {
//            try {
//                newMainRepository.getEquipmentImage().let {
//                    _equipmentImagesListForSync.postValue(Resource.success(it))
//                }
//            } catch (e: Exception) {
//                _equipmentImagesListForSync.postValue(
//                    Resource.error(
//                        e.toString(),
//                        null
//                    )
//                )
//            }
//        } catch (e: Exception) {
//            _equipmentImagesListForSync.postValue(Resource.error(e.message.toString(), null))
//        }
//    }

    fun getFormVideos() = CoroutineScope(Dispatchers.IO).launch {
        _formVideosList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getFormVideos().let {
                    _formVideosList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _formVideosList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _formVideosList.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun getFormVideosForSync() = CoroutineScope(Dispatchers.IO).launch {
        _formVideosListForSync.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getFormVideos().let {
                    _formVideosListForSync.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _formVideosListForSync.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _formVideosListForSync.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun getUnsyncedFormVideos() {
        _formVideosListForSync.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val unsyncedVideos = newMainRepository.getUnsyncedFormVideos()
                _formVideosListForSync.postValue(Resource.success(unsyncedVideos))
            } catch (e: Exception) {
                _formVideosListForSync.postValue(Resource.error("Error fetching unsynced form videos", null))
            }
        }
    }


    fun getCampPatientList() = CoroutineScope(Dispatchers.IO).launch {
        _campPatientList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getCampPatientDetailsDb().let {
                    _campPatientList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _campPatientList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _campPatientList.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun getOrthosisPatientForm() = CoroutineScope(Dispatchers.IO).launch {
        _orthosisPatientFormList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOrthosisPatientForm().let {
                    _orthosisPatientFormList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _orthosisPatientFormList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _orthosisPatientFormList.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun insertCampDetails(campList: List<CampModel>) {
        viewModelScope.launch {
            newMainRepository.insertCampDetails(campList)
        }
    }

    fun updateSingleCamp(camp: CampModel) {
        viewModelScope.launch {
            newMainRepository.updateSingleCamp(camp)
        }
    }

    fun getCampDetails() = CoroutineScope(Dispatchers.IO).launch {
        _campDetailsResponse.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getCampList().let {
                    _campDetailsResponse.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _campDetailsResponse.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _campDetailsResponse.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun getCampForViewReport() = CoroutineScope(Dispatchers.IO).launch {
        _campForViewReport.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getCampList().let {
                    _campForViewReport.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _campForViewReport.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _campForViewReport.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun getDiagnosisMaster() = CoroutineScope(Dispatchers.IO).launch {
        _diagnosisMasterResponse.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getDiagnosisMaster().let {
                    _diagnosisMasterResponse.postValue(Resource.success(it.body()!!.diagnosisList))
                }
            } catch (e: Exception) {
                _diagnosisMasterResponse.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _diagnosisMasterResponse.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun insertDiagnosisMasterLocal(diagnosisMaster: DiagnosisType) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = newMainRepository.insertDiagnosisMaster(diagnosisMaster)
            _insertDiagnosisResponse.postValue(Resource.success(message))
            if (message.equals(null)) {
                _insertDiagnosisResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertDiagnosisResponse.postValue(Resource.success(message))
            }
        }
    }

    fun getOrthosisEquipmentMasterApi() = CoroutineScope(Dispatchers.IO).launch {
        _othosisEquipmentMasterResponse.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOrthosisEquipmentMaster().let {
                    _othosisEquipmentMasterResponse.postValue(Resource.success(it.body()!!.equipmentList))
                }
            } catch (e: Exception) {
                _othosisEquipmentMasterResponse.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _othosisEquipmentMasterResponse.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun insertOrthosisEquipmentMasterLocal(equipmentMaster: Equipment) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = newMainRepository.insertOrthosisEquipmentMaster(equipmentMaster)
            _insertOrthosisResponse.postValue(Resource.success(message))
            if (message.equals(null)) {
                _insertOrthosisResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertOrthosisResponse.postValue(Resource.success(message))
            }
        }
    }

    //Images and Videos Sync Operation
    fun syncFormImages(formImageRequest: FormImageRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            _formSyncResponse.postValue(Resource.loading(null))
            try {
                try {
                    newMainRepository.syncFormImagesNew(formImageRequest).let {
                        _formSyncResponse.postValue(Resource.success(it.body()))
                    }
                } catch (e: Exception) {
                    _formSyncResponse.postValue(
                        Resource.error(
                            e.toString(),
                            null
                        )
                    )
                }
            } catch (e: Exception) {
                _formSyncResponse.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun syncFormVideos(formImageRequest: FormVideoRquest) {
        CoroutineScope(Dispatchers.IO).launch {
            _formVideoSyncResponse.postValue(Resource.loading(null))
            try {
                try {
                    newMainRepository.syncFormVideosNew(formImageRequest).let {
                        _formVideoSyncResponse.postValue(Resource.success(it.body()))
                    }
                } catch (e: Exception) {
                    _formVideoSyncResponse.postValue(
                        Resource.error(
                            e.toString(),
                            null
                        )
                    )
                }
            } catch (e: Exception) {
                _formVideoSyncResponse.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun syncOrthosisImages(formImageRequest: OrthosisImageRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            _orthosisImageSyncResponse.postValue(Resource.loading(null))
            try {
                try {
                    newMainRepository.syncOrthosisImagesNew(formImageRequest).let {
                        _orthosisImageSyncResponse.postValue(Resource.success(it.body()))
                    }
                } catch (e: Exception) {
                    _orthosisImageSyncResponse.postValue(
                        Resource.error(
                            e.toString(),
                            null
                        )
                    )
                }
            } catch (e: Exception) {
                _orthosisImageSyncResponse.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun syncEquipmentImages(formImageRequest: EquipmentImageRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            _equipmentImageSyncResponse.postValue(Resource.loading(null))
            try {
                try {
                    newMainRepository.syncEquipmentImagesNew(formImageRequest).let {
                        _equipmentImageSyncResponse.postValue(Resource.success(it.body()))
                    }
                } catch (e: Exception) {
                    _equipmentImageSyncResponse.postValue(
                        Resource.error(
                            e.toString(),
                            null
                        )
                    )
                }
            } catch (e: Exception) {
                _equipmentImageSyncResponse.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun updateFormImageSynced(imageId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            newMainRepository.updateSyncedImage(imageId)
        }
    }

    fun updateOrthosisImageSynced(imageId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            newMainRepository.updateSyncedOrthoImage(imageId)
        }
    }

    fun updateEquipmentImageSynced(imageId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            newMainRepository.updateSyncedEquipmentImage(imageId)
        }
    }

    fun updateFormVideoSynced(videoId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            newMainRepository.updateSyncedVideo(videoId)
        }
    }


}