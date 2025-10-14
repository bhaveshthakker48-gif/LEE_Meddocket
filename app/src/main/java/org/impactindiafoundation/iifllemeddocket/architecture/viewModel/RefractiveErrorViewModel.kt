package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject


@HiltViewModel
class RefractiveErrorViewModel @Inject constructor(private val mainRepository: NewMainRepository):ViewModel() {

    val allRefractive_Error:LiveData<List<RefractiveError>> = mainRepository.allRefractive_Error

    private var _insertRefractiveResponse = MutableLiveData<Resource<Long>>()
    val insertRefractiveResponse: LiveData<Resource<Long>> get() = _insertRefractiveResponse

    private var _refractiveFormListById = MutableLiveData<Resource<List<RefractiveError>>>()
    val refractiveFormListById: LiveData<Resource<List<RefractiveError>>> get() = _refractiveFormListById

    fun insertRefractiveForm(refractiveForm: RefractiveError) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = mainRepository.insertRefractiveForm(refractiveForm)
            _insertRefractiveResponse.postValue(Resource.success(message))
            if (message.equals(null)) {
                _insertRefractiveResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertRefractiveResponse.postValue(Resource.success(message))
            }
        }
    }

    fun getRefractiveFormById(localPatientId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _refractiveFormListById.postValue(Resource.loading(null))
        try {
            try {
                mainRepository.getRefractiveFormById(localPatientId).let {
                    _refractiveFormListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _refractiveFormListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _refractiveFormListById.postValue(Resource.error(e.message.toString(), null))
        }
    }
}