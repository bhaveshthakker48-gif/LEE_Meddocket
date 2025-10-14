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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject


@HiltViewModel
class OpdFormViewModel @Inject constructor(val mainRepository: NewMainRepository):ViewModel(){

    val allOPD_Investigations:LiveData<List<OPD_Investigations>> = mainRepository.allOPD_Investigations

    private var _insertOpdResponse = MutableLiveData<Resource<Long>>()
    val insertOpdResponse: LiveData<Resource<Long>> get() = _insertOpdResponse

    private var _opdFormListById = MutableLiveData<Resource<List<OPD_Investigations>>>()
    val opdFormListById: LiveData<Resource<List<OPD_Investigations>>> get() = _opdFormListById

    fun insertOpdForm(opdForm: OPD_Investigations) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = mainRepository.insertOpdForm(opdForm)
            _insertOpdResponse.postValue(Resource.success(message))

            if (message.equals(null)) {
                _insertOpdResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertOpdResponse.postValue(Resource.success(message))
            }
        }
    }

    fun getOpdFormById(localPatientId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _opdFormListById.postValue(Resource.loading(null))
        try {
            try {
                mainRepository.getOpdFormById(localPatientId).let {
                    _opdFormListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _opdFormListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _opdFormListById.postValue(Resource.error(e.message.toString(), null))
        }
    }
}