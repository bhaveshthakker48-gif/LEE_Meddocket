package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModelItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntImpressionItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomEarItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomNoseItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntSymptomThroatItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntEarType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntNoseType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntThroatType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.ImpressionType
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val newMainRepository: NewMainRepository) :
    ViewModel() {
    //Ent
    //Ear
    private val _entSymptomEarItemResponse = MutableLiveData<Resource<List<EntSymptomEarItem>>>()
    val entSymptomEarItemResponse: LiveData<Resource<List<EntSymptomEarItem>>> = _entSymptomEarItemResponse

    private val _insertEntSymptomEarResponse = MutableLiveData<Resource<Long>>()
    val insertEntSymptomEarResponse: LiveData<Resource<Long>> = _insertEntSymptomEarResponse

    //Nose
    private val _entSymptomNoseItemResponse = MutableLiveData<Resource<List<EntSymptomNoseItem>>>()
    val entSymptomNoseItemResponse: LiveData<Resource<List<EntSymptomNoseItem>>> = _entSymptomNoseItemResponse

    private val _insertEntSymptomNoseResponse = MutableLiveData<Resource<Long>>()
    val insertEntSymptomNoseResponse: LiveData<Resource<Long>> = _insertEntSymptomNoseResponse

    //Throat
    private val _entSymptomThroatItemResponse = MutableLiveData<Resource<List<EntSymptomThroatItem>>>()
    val entSymptomThroatItemResponse: LiveData<Resource<List<EntSymptomThroatItem>>> = _entSymptomThroatItemResponse

    private val _insertEntSymptomThroatResponse = MutableLiveData<Resource<Long>>()
    val insertEntSymptomThroatResponse: LiveData<Resource<Long>> = _insertEntSymptomThroatResponse

    //Impression
    private val _entImpressionResponse = MutableLiveData<Resource<List<EntImpressionItem>>>()
    val entImpressionResponse: LiveData<Resource<List<EntImpressionItem>>> = _entImpressionResponse

    private val _insertEntImpressionResponse = MutableLiveData<Resource<Long>>()
    val insertEntImpressionResponse: LiveData<Resource<Long>> = _insertEntImpressionResponse

    fun getEntSymptomEarApi() = viewModelScope.launch {
        _entSymptomEarItemResponse.postValue(Resource.loading(null))
        try {
            val response = newMainRepository.getEntSymptomEar()
            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data.results
                _entSymptomEarItemResponse.postValue(Resource.success(items))
            } else {
                _entSymptomEarItemResponse.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            _entSymptomEarItemResponse.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun insertEntSymptomEar(entEarType: EntEarType) = viewModelScope.launch {
        try {
            val result = newMainRepository.insertEntSymptomEar(entEarType)
            Log.d("details", "Ear symptom inserted successfully: $entEarType, result: $result")
            _insertEntSymptomEarResponse.postValue(Resource.success(result))
        } catch (e: Exception) {
            Log.d("details", "Failed to insert ear symptom: ${e.message}", e)
            _insertEntSymptomEarResponse.postValue(Resource.error("Local DB Error", null))
        }
    }

    fun getEntSymptomNoseApi() = viewModelScope.launch {
        _entSymptomNoseItemResponse.postValue(Resource.loading(null))
        try {
            val response = newMainRepository.getEntSymptomNose()
            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data.results
                _entSymptomNoseItemResponse.postValue(Resource.success(items))
            } else {
                _entSymptomNoseItemResponse.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            _entSymptomNoseItemResponse.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun insertEntSymptomNose(entNoseType: EntNoseType) = viewModelScope.launch {
        try {
            val result = newMainRepository.insertEntSymptomNose(entNoseType)
            Log.d("details", "Nose symptom inserted successfully: $entNoseType, result: $result")
            _insertEntSymptomNoseResponse.postValue(Resource.success(result))
        } catch (e: Exception) {
            Log.d("details", "Failed to insert nose symptom: ${e.message}", e)
            _insertEntSymptomNoseResponse.postValue(Resource.error("Local DB Error", null))
        }
    }

    fun getEntSymptomThroatApi() = viewModelScope.launch {
        _entSymptomThroatItemResponse.postValue(Resource.loading(null))
        try {
            val response = newMainRepository.getEntSymptomThroat()
            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data.results
                _entSymptomThroatItemResponse.postValue(Resource.success(items))
            } else {
                _entSymptomThroatItemResponse.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            _entSymptomThroatItemResponse.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun insertEntSymptomThroat(entThroatType: EntThroatType) = viewModelScope.launch {
        try {
            val result = newMainRepository.insertEntSymptomThroat(entThroatType)
            Log.d("details", "Throat symptom inserted successfully: $entThroatType, result: $result")
            _insertEntSymptomThroatResponse.postValue(Resource.success(result))
        } catch (e: Exception) {
            Log.d("details", "Failed to insert throat symptom: ${e.message}", e)
            _insertEntSymptomThroatResponse.postValue(Resource.error("Local DB Error", null))
        }
    }

    fun getEntImpression() = viewModelScope.launch {
        _entImpressionResponse.postValue(Resource.loading(null))
        try {
            val response = newMainRepository.getEntImpression()
            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.data.results
                _entImpressionResponse.postValue(Resource.success(items))
            } else {
                _entImpressionResponse.postValue(Resource.error("API Error", null))
            }
        } catch (e: Exception) {
            _entImpressionResponse.postValue(Resource.error(e.message ?: "Unknown Error", null))
        }
    }

    fun insertEntImpression(impressionType: ImpressionType) = viewModelScope.launch {
        try {
            val result = newMainRepository.insertEntImpression(impressionType)
            Log.d("details", "Impression symptom inserted successfully: $impressionType, result: $result")
            _insertEntSymptomThroatResponse.postValue(Resource.success(result))
        } catch (e: Exception) {
            Log.d("details", "Failed to insert throat symptom: ${e.message}", e)
            _insertEntSymptomThroatResponse.postValue(Resource.error("Local DB Error", null))
        }
    }

}