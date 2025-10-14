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
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideos
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject
@HiltViewModel
class OrthosisFittingViewModel @Inject constructor(private val newMainRepository: NewMainRepository):
    ViewModel() {
    private var _campPatientListById = MutableLiveData<Resource<List<CampPatientDataItem>>>()
    val campPatientListById: LiveData<Resource<List<CampPatientDataItem>>> get() = _campPatientListById

    private var _orthosisPatientFormListById = MutableLiveData<Resource<List<OrthosisPatientForm>>>()
    val orthosisPatientFormListById: LiveData<Resource<List<OrthosisPatientForm>>> get() = _orthosisPatientFormListById

    private var _orthosisPatientFormListByTempId = MutableLiveData<Resource<List<OrthosisPatientForm>>>()
    val orthosisPatientFormListByTempId: LiveData<Resource<List<OrthosisPatientForm>>> get() = _orthosisPatientFormListByTempId

    private var _orthosisPatientFormList = MutableLiveData<Resource<List<OrthosisPatientForm>>>()
    val orthosisPatientFormList: LiveData<Resource<List<OrthosisPatientForm>>> get() = _orthosisPatientFormList

    private var _insertCampFormResponse = MutableLiveData<Resource<Long>>()
    val insertCampFormResponse: LiveData<Resource<Long>> get() = _insertCampFormResponse

    private var _insertOrthosisFormResponse = MutableLiveData<Resource<Long>>()
    val insertOrthosisFormResponse: LiveData<Resource<Long>> get() = _insertOrthosisFormResponse

    //orthosis file datas
    private val _insertedFormImageIds = MutableLiveData<Resource<List<Long>>>()
    val insertedFormImageIds: LiveData<Resource<List<Long>>> get() = _insertedFormImageIds

    private var _formImageListFormId = MutableLiveData<Resource<List<FormImages>>>()
    val formImageListFormId: LiveData<Resource<List<FormImages>>> get() = _formImageListFormId

    private var _formVideosListById = MutableLiveData<Resource<List<FormVideos>>>()
    val formVideosListById: LiveData<Resource<List<FormVideos>>> get() = _formVideosListById

    private var _diagnosisMasterList = MutableLiveData<Resource<List<DiagnosisType>>>()
    val diagnosisMasterList: LiveData<Resource<List<DiagnosisType>>> get() = _diagnosisMasterList

    fun getCampPatientListById(tempId:Int) = CoroutineScope(Dispatchers.IO).launch {
        _campPatientListById.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getCampPatientListById(tempId).let {
                    _campPatientListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _campPatientListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _campPatientListById.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun getOrthosisPatientFormById(formId:Int) = CoroutineScope(Dispatchers.IO).launch {
        _orthosisPatientFormListById.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOrthosisPatientFormById(formId).let {
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

    fun getOrthosisPatientFormByTempId(tempId:Int) = CoroutineScope(Dispatchers.IO).launch {
        _orthosisPatientFormListByTempId.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOrthosisPatientFormByTempId(tempId).let {
                    _orthosisPatientFormListByTempId.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _orthosisPatientFormListByTempId.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _orthosisPatientFormListByTempId.postValue(Resource.error(e.message.toString(), null))
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

    fun insertSingleCampPatient(campPatient:CampPatientDataItem) = CoroutineScope(Dispatchers.IO).launch {
        _insertCampFormResponse.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.insertSingleCampPatient(campPatient).let {
                    _insertCampFormResponse.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _insertCampFormResponse.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _insertCampFormResponse.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun insertOrthosisPatientForm(orthosisPatientForm: OrthosisPatientForm) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = newMainRepository.insertOrthosisForm(orthosisPatientForm)
            _insertOrthosisFormResponse.postValue(Resource.success(message))
            if (message.equals(null)) {
                _insertOrthosisFormResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertOrthosisFormResponse.postValue(Resource.success(message))
            }
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

    //3 form videos operation
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

    fun insertFormVideoList(formVideoList: List<FormVideos>) {
        viewModelScope.launch {
            newMainRepository.insertFormVideoList(formVideoList)
        }
    }

    fun deleteFormVideosById(formVideoList: List<Int>) {
        viewModelScope.launch {
            newMainRepository.deleteFormVideosById(formVideoList)
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
}