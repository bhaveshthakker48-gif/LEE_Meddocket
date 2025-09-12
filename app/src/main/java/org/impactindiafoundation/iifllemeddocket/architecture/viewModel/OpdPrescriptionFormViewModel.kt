package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject


@HiltViewModel
class OpdPrescriptionFormViewModel @Inject constructor(val mainRepository: NewMainRepository): ViewModel(){

    private var _insertOpdPrescriptionResponse = MutableLiveData<Resource<Long>>()
    val insertOpdPrescriptionResponse: LiveData<Resource<Long>> get() = _insertOpdPrescriptionResponse

    private var _patientMedicineList = MutableLiveData<Resource<List<PatientMedicine>>>()
    val patientMedicineList: LiveData<Resource<List<PatientMedicine>>> get() = _patientMedicineList

    private var _patientMedicineListById = MutableLiveData<Resource<List<PatientMedicine>>>()
    val patientMedicineListById: LiveData<Resource<List<PatientMedicine>>> get() = _patientMedicineListById

    private var _opdFormListById =
        MutableLiveData<Resource<List<OPD_Investigations>>>()
    val opdFormListById: LiveData<Resource<List<OPD_Investigations>>> get() = _opdFormListById

    fun insertOpdFinalPrescription(prescription: PatientMedicine) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = mainRepository.insertFinalPrescriptionDrug(prescription)
            _insertOpdPrescriptionResponse.postValue(Resource.success(message))

            if (message.equals(null)) {
                _insertOpdPrescriptionResponse.postValue(Resource.error("Local Db Error", null))
            } else {
                _insertOpdPrescriptionResponse.postValue(Resource.success(message))
            }
        }
    }

    fun savePrescription(prescription: PatientMedicine) {
        viewModelScope.launch(Dispatchers.IO) {
            if (prescription._id > 0) {
                mainRepository.updatePrescription(prescription)  // Update if exists
            } else {
                insertOpdFinalPrescription(prescription)  // Insert new
            }
        }
    }

    fun getPatientMedicineList() = CoroutineScope(Dispatchers.IO).launch {
        _patientMedicineList.postValue(Resource.loading(null))
        try {
            try {
                mainRepository.getPatientMedicineReport().let {
                    _patientMedicineList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _patientMedicineList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )

            }

        } catch (e: Exception) {
            _patientMedicineList.postValue(Resource.error(e.message.toString(), null))
        }
    }

    val prescriptionList = MutableLiveData<ArrayList<CreatePrescriptionModel>>(arrayListOf())


    fun getFinalPrescriptionByFormId(opdFormId:Int) = CoroutineScope(Dispatchers.IO).launch {
        _patientMedicineListById.postValue(Resource.loading(null))
        try {
            try {
                mainRepository.getFinalPrescriptionByFormId(opdFormId).let {
                    _patientMedicineListById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _patientMedicineListById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )

            }

        } catch (e: Exception) {
            _patientMedicineListById.postValue(Resource.error(e.message.toString(), null))
        }
    }

    val unsyncedFormsCount: LiveData<Int> = mainRepository.unsyncedFormsCount

}