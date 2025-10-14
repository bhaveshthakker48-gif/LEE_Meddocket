package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject


@HiltViewModel
class VisualAcuityViewModel @Inject constructor(val mainRepository: NewMainRepository):ViewModel() {

    val allVisualAcuity:LiveData<List<VisualAcuity>> = mainRepository.allVisualAcuity

    private var _insertVisualAcuityResponse = MutableLiveData<Resource<Long>>()
    val insertVisualAcuityResponse: LiveData<Resource<Long>> get() = _insertVisualAcuityResponse

    private var _visualAcuityFormListById = MutableLiveData<Resource<List<VisualAcuity>>>()
    val visualAcuityFormListById: LiveData<Resource<List<VisualAcuity>>> get() = _visualAcuityFormListById

    fun insertVisualAcuityForm(visualAcuityForm: VisualAcuity) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = mainRepository.insertVisualAcuityForm(visualAcuityForm)
            _insertVisualAcuityResponse.postValue(Resource.success(message))
            if (message.equals(null)) {
                _insertVisualAcuityResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertVisualAcuityResponse.postValue(Resource.success(message))
            }
        }
    }

    fun getVisualAcuityFormById(localPatientId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _visualAcuityFormListById.postValue(Resource.loading(null))
        try {
            try {
                mainRepository.getVisualAcuityFormById(localPatientId).let {
                    _visualAcuityFormListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _visualAcuityFormListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _visualAcuityFormListById.postValue(Resource.error(e.message.toString(), null))
        }
    }
}