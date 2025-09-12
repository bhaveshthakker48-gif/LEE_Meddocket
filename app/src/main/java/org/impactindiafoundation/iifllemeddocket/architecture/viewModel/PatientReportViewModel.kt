package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject



@HiltViewModel
class PatientReportViewModel @Inject constructor(val mainRepository: NewMainRepository): ViewModel(){

    private var _insertPatientReportResponse = MutableLiveData<Resource<Long>>()
    val insertPatientReportResponse: LiveData<Resource<Long>> get() = _insertPatientReportResponse

    private var _patientReportList = MutableLiveData<Resource<List<PatientReport>>>()
    val patientReportList: LiveData<Resource<List<PatientReport>>> get() = _patientReportList

    private var _opdFormListById =
        MutableLiveData<Resource<List<PatientReport>>>()
    val opdFormListById: LiveData<Resource<List<PatientReport>>> get() = _opdFormListById

    fun insertPatientReport(patientReport: PatientReport) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = mainRepository.insertPatientReport(patientReport)
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
                mainRepository.getPatientReport().let {
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
}