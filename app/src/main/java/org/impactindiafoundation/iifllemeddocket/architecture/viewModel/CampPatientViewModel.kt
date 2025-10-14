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
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModelItem
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject


@HiltViewModel
class CampPatientViewModel @Inject constructor(private val newMainRepository: NewMainRepository):
    ViewModel() {

    private var _campPatientList = MutableLiveData<Resource<List<CampPatientDataItem>>>()
    val campPatientList: LiveData<Resource<List<CampPatientDataItem>>> get() = _campPatientList

    private var _orthosisPatientFormList = MutableLiveData<Resource<List<OrthosisPatientForm>>>()
    val orthosisPatientFormList: LiveData<Resource<List<OrthosisPatientForm>>> get() = _orthosisPatientFormList

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
}