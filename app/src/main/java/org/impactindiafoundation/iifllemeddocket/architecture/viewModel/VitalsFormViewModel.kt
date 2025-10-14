package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject

@HiltViewModel
class VitalsFormViewModel @Inject constructor(val mainRepository: NewMainRepository):ViewModel(){

    val allVitals:LiveData<List<Vitals>> = mainRepository.allVitals

    private var _insertVitalsResponse = MutableLiveData<Resource<Long>>()
    val insertVitalsResponse: LiveData<Resource<Long>> get() = _insertVitalsResponse

    private var _vitalsFormListById = MutableLiveData<Resource<List<Vitals>>>()
    val vitalsFormListById: LiveData<Resource<List<Vitals>>> get() = _vitalsFormListById

    fun insertVitalsForm(vitalsForm:Vitals) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = mainRepository.insertVitalsForm(vitalsForm)
            _insertVitalsResponse.postValue(Resource.success(message))
            if (message.equals(null)) {
                _insertVitalsResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertVitalsResponse.postValue(Resource.success(message))
            }
        }
    }

    fun getVitalsFormById(localPatientId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _vitalsFormListById.postValue(Resource.loading(null))
        try {
            try {
                mainRepository.getVitalsFormById(localPatientId).let {
                    _vitalsFormListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _vitalsFormListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _vitalsFormListById.postValue(Resource.error(e.message.toString(), null))
        }
    }
}