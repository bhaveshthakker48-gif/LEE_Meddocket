package org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.repository.EntRepository
import javax.inject.Inject

@HiltViewModel
class EntPatientReportViewModel @Inject constructor(val entRepository: EntRepository): ViewModel(){

    private var _insertPatientReportResponse = MutableLiveData<Resource<Long>>()
    val insertPatientReportResponse: LiveData<Resource<Long>> get() = _insertPatientReportResponse

    private var _patientReportList = MutableLiveData<Resource<List<EntPatientReport>>>()
    val patientReportList: LiveData<Resource<List<EntPatientReport>>> get() = _patientReportList

    private var _opdFormListById =
        MutableLiveData<Resource<List<EntPatientReport>>>()
    val opdFormListById: LiveData<Resource<List<EntPatientReport>>> get() = _opdFormListById

    fun insertPatientReport(patientReport: EntPatientReport) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.insertPatientReport(patientReport)
            _insertPatientReportResponse.postValue(Resource.success(message))

            if (message.equals(null)) {
                _insertPatientReportResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertPatientReportResponse.postValue(Resource.success(message))
            }
        }
    }

    fun getPatientReportList() = CoroutineScope(Dispatchers.IO).launch {
        _patientReportList.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getPatientReport().let {
                    _patientReportList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _patientReportList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _patientReportList.postValue(Resource.error(e.message.toString(), null))
        }
    }

    fun getPatientReportById(localPatientId: Int) = CoroutineScope(Dispatchers.IO).launch {
        _opdFormListById.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getPatientReportById(localPatientId).let {
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

    fun clearSyncedEntPatientReportList() {
        viewModelScope.launch {
            try {
                entRepository.clearSyncedEntPatientReportList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertPrescriptionPatientReport(patientReport: PrescriptionPatientReport) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = entRepository.insertPrescriptionPatientReport(patientReport)
            _insertPatientReportResponse.postValue(Resource.success(message))

            if (message.equals(null)) {
                _insertPatientReportResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertPatientReportResponse.postValue(Resource.success(message))
            }
        }
    }

    private var _prescriptionPatientReportList = MutableLiveData<Resource<List<PrescriptionPatientReport>>>()
    val prescriptionPatientReportList: LiveData<Resource<List<PrescriptionPatientReport>>> get() = _prescriptionPatientReportList


    fun getPrescriptionPatientReportList() = CoroutineScope(Dispatchers.IO).launch {
        _prescriptionPatientReportList.postValue(Resource.loading(null))
        try {
            try {
                entRepository.getPrescriptionPatientReport().let {
                    _prescriptionPatientReportList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _prescriptionPatientReportList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _prescriptionPatientReportList.postValue(Resource.error(e.message.toString(), null))
        }
    }
}