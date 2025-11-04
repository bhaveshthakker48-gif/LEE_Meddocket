package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import android.media.audiofx.DynamicsProcessing.Eq
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.AgeGroupCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.Equipment
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImage
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideos
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisStatusCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisSynTable
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModelItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject

@HiltViewModel
class OrthosisViewModel @Inject constructor(private val newMainRepository: NewMainRepository) :
    ViewModel() {

    private var _insertOrthosisResponse = MutableLiveData<Resource<Long>>()
    val insertOrthosisResponse: LiveData<Resource<Long>> get() = _insertOrthosisResponse

    private var _orthosisMasterList = MutableLiveData<Resource<List<OrthosisType>>>()
    val orthosisMasterList: LiveData<Resource<List<OrthosisType>>> get() = _orthosisMasterList

    private var _orthosisPatientFormList = MutableLiveData<Resource<List<OrthosisPatientForm>>>()
    val orthosisPatientFormList: LiveData<Resource<List<OrthosisPatientForm>>> get() = _orthosisPatientFormList

    private var _insertOrthosisFormResponse = MutableLiveData<Resource<Long>>()
    val insertOrthosisFormResponse: LiveData<Resource<Long>> get() = _insertOrthosisFormResponse

    private var _orthosisPatientFormListById =
        MutableLiveData<Resource<List<OrthosisPatientForm>>>()
    val orthosisPatientFormListById: LiveData<Resource<List<OrthosisPatientForm>>> get() = _orthosisPatientFormListById

    //orthosis file datas
    private val _insertedFormImageIds = MutableLiveData<Resource<List<Long>>>()
    val insertedFormImageIds: LiveData<Resource<List<Long>>> get() = _insertedFormImageIds

    private var _userList = MutableLiveData<Resource<List<UserModel>>>()
    val userList: LiveData<Resource<List<UserModel>>> get() = _userList

    private var _formImageListFormId = MutableLiveData<Resource<List<FormImages>>>()
    val formImageListFormId: LiveData<Resource<List<FormImages>>> get() = _formImageListFormId

    private var _formVideosListById = MutableLiveData<Resource<List<FormVideos>>>()
    val formVideosListById: LiveData<Resource<List<FormVideos>>> get() = _formVideosListById

    private var _orthosisImagesList = MutableLiveData<Resource<List<OrthosisImages>>>()
    val orthosisImagesList: LiveData<Resource<List<OrthosisImages>>> get() = _orthosisImagesList

    private var _diagnosisMasterList = MutableLiveData<Resource<List<DiagnosisType>>>()
    val diagnosisMasterList: LiveData<Resource<List<DiagnosisType>>> get() = _diagnosisMasterList

    private var _orthosisEquipmentMasterList = MutableLiveData<Resource<List<Equipment>>>()
    val orthosisEquipmentMasterList: LiveData<Resource<List<Equipment>>> get() = _orthosisEquipmentMasterList

    //equipment file datas
    private var _equipmentImagesList = MutableLiveData<Resource<List<EquipmentImage>>>()
    val equipmentImagesList: LiveData<Resource<List<EquipmentImage>>> get() = _equipmentImagesList

    val patientCount: LiveData<Int> = newMainRepository.getPatientCount()
    val formCount: LiveData<Int> = newMainRepository.getFormCount()
    val malePatientCount: LiveData<Int> = newMainRepository.getMalePatientCount()
    val femalePatientCount: LiveData<Int> = newMainRepository.getFemalePatientCount()
    val otherPatientCount: LiveData<Int> = newMainRepository.getOtherPatientCount()


    private val _diagnosisCounts = MutableLiveData<List<DiagnosisCount>>()
    val diagnosisCounts: LiveData<List<DiagnosisCount>> = _diagnosisCounts

    fun fetchDiagnosisCounts() {
        viewModelScope.launch {
            val list = newMainRepository.getDiagnosisCounts()
            _diagnosisCounts.postValue(list)
        }
    }

    private val _orthosisStatusCounts = MutableLiveData<List<OrthosisStatusCount>>()
    val orthosisStatusCounts: LiveData<List<OrthosisStatusCount>> = _orthosisStatusCounts

    // Function to fetch status counts
    fun fetchOrthosisStatusCounts() {
        viewModelScope.launch {
            val counts = newMainRepository.getOrthosisStatusCounts()
            _orthosisStatusCounts.postValue(counts)
        }
    }


    private val _orthosisTypeCounts = MutableLiveData<List<OrthosisTypeCount>>()
    val orthosisTypeCounts: LiveData<List<OrthosisTypeCount>> get() = _orthosisTypeCounts

    fun fetchOrthosisTypeCounts() {
        viewModelScope.launch {
            val counts = newMainRepository.getOrthosisTypeCounts()
            _orthosisTypeCounts.postValue(counts)
        }
    }

    private val _ageGroupCounts = MutableLiveData<List<AgeGroupCount>>()
    val ageGroupCounts: LiveData<List<AgeGroupCount>> get() = _ageGroupCounts

    fun fetchAgeGroupCounts() {
        viewModelScope.launch {
            val list = newMainRepository.getAgeGroupCounts()
            _ageGroupCounts.postValue(list)
        }
    }

    fun getFormVideos(formId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _formVideosListById.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getFormVideosById(formId).let {
                    _formVideosListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _formVideosListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _formVideosListById.postValue(Resource.error(e.message.toString(), null))
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

    fun getOrthosisMasterLocal() = CoroutineScope(Dispatchers.IO).launch {
        _orthosisMasterList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOrthosisMaster().let {
                    _orthosisMasterList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _orthosisMasterList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _orthosisMasterList.postValue(Resource.error(e.message.toString(), null))
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

    fun insertOrthosisPatientForm(orthosisPatientForm: OrthosisPatientForm) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val message = newMainRepository.insertOrthosisForm(orthosisPatientForm)
                if (message == null) {
                    _insertOrthosisFormResponse.postValue(Resource.error("Local Db Error", null))
                } else {
                    _insertOrthosisFormResponse.postValue(Resource.success(message))
                }
            } catch (e: Exception) {
                _insertOrthosisFormResponse.postValue(Resource.error("Exception: ${e.localizedMessage}", null))
            }
        }
    }


    fun getOrthosisPatientFormById(localPatientId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _orthosisPatientFormListById.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOrthosisPatientFormById(localPatientId).let {
                    _orthosisPatientFormListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _orthosisPatientFormListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _orthosisPatientFormListById.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun insertOrthosisImageList(orthosisImageList: List<OrthosisImages>) {
        viewModelScope.launch {
            newMainRepository.insertOrthosisImageList(orthosisImageList)
        }
    }

    fun deleteOrthosisImageByPath(imagePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            newMainRepository.deleteOrthosisImageByPath(imagePath)
        }
    }

    fun deleteOrthosisImages(images: List<OrthosisImages>) {
        viewModelScope.launch(Dispatchers.IO) {
            newMainRepository.deleteOrthosisImages(images)
        }
    }


    fun getOrthosisImageByFormId(formId: String) = CoroutineScope(Dispatchers.IO).launch {
        _orthosisImagesList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOrthosisImageByFormId(formId).let {
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

    fun insertFormImageList(formImageList: List<FormImages>) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = newMainRepository.insertFormImageList(formImageList)
            _insertedFormImageIds.postValue(Resource.success(message))
            if (message.equals(null)) {
                _insertedFormImageIds.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertedFormImageIds.postValue(Resource.success(message))
            }
        }
    }

    fun deleteFormImages(formImageList: List<FormImages>) {
        viewModelScope.launch {
            newMainRepository.deleteFormImages(formImageList)
        }
    }

    fun deleteFormImagesById(formImageList: List<Int>) {
        viewModelScope.launch {
            newMainRepository.deleteFormImagesById(formImageList)
        }
    }

    fun getFormImageListByFormId(formId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                try {
                    newMainRepository.getFormImageListByFormId(formId).let {
                        _formImageListFormId.postValue(Resource.success(it))
                    }
                } catch (e: Exception) {
                    _formImageListFormId.postValue(
                        Resource.error(
                            e.toString(),
                            null
                        )
                    )
                }
            } catch (e: Exception) {
                _formImageListFormId.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun insertFormVideoList(formVideoList: List<FormVideos>) {
        viewModelScope.launch {
            newMainRepository.insertFormVideoList(formVideoList)
        }
    }

    fun getLocalUserList() = CoroutineScope(Dispatchers.IO).launch {
        _userList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getAllUsers().let {
                    _userList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _userList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _userList.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun deleteFormVideosById(formVideoList: List<Int>) {
        viewModelScope.launch {
            newMainRepository.deleteFormVideosById(formVideoList)
        }
    }

    fun insertEquipmentImageList(equipmentImageList: List<EquipmentImage>) {
        viewModelScope.launch {
            newMainRepository.insertEquipmentImageList(equipmentImageList)
        }
    }

    fun deleteEquipmentImage(image: String) {
        viewModelScope.launch {
            newMainRepository.deleteEquipmentImage(image)
        }
    }

    fun getEquipmentImageByFormId(formId: String) = CoroutineScope(Dispatchers.IO).launch {
        _equipmentImagesList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getEquipmentImageByFormId(formId).let {
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

    fun getDiagnosisMasterLocal() = CoroutineScope(Dispatchers.IO).launch {
        _diagnosisMasterList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getDiagnosisMasterLocal().let {
                    _diagnosisMasterList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _diagnosisMasterList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _diagnosisMasterList.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun getOrthosisEquipmentMasterLocal() = CoroutineScope(Dispatchers.IO).launch {
        _orthosisEquipmentMasterList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOrthosisEquipmentMasterLocal().let {
                    _orthosisEquipmentMasterList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _orthosisEquipmentMasterList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _orthosisEquipmentMasterList.postValue(Resource.error(e.message.toString(), null))
        }
    }

    val allSynData:LiveData<List<OrthosisSynTable>> = newMainRepository.allSynData

}