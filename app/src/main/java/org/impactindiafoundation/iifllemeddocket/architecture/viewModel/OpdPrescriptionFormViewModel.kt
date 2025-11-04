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
import org.impactindiafoundation.iifllemeddocket.architecture.model.AgeGroupCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.BrandUsage
import org.impactindiafoundation.iifllemeddocket.architecture.model.GenericUsage
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.architecture.model.SpecialityCount
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

    val totalPatientCount: LiveData<Int> = mainRepository.totalPatientCount

    val totalFormCount: LiveData<Int> = mainRepository.totalFormCount

    val malePatientCount: LiveData<Int> = mainRepository.getMalePharmaPatientCount
    val femalePatientCount: LiveData<Int> = mainRepository.getFemalePharmaPatientCount
    val otherPatientCount: LiveData<Int> = mainRepository.getOtherPharmaPatientCount

    private val _specialityCountList = MutableLiveData<List<SpecialityCount>>()
    val specialityCountList: LiveData<List<SpecialityCount>> get() = _specialityCountList

    fun fetchSpecialityCountData() {
        viewModelScope.launch {
            val list = mainRepository.getSpecialityCountList()
            _specialityCountList.postValue(list)
        }
    }

    private val _ageGroupCounts = MutableLiveData<List<AgeGroupCount>>()
    val ageGroupCounts: LiveData<List<AgeGroupCount>> get() = _ageGroupCounts

    fun fetchAgeGroupCounts() {
        viewModelScope.launch {
            val list = mainRepository.getPharmaAgeGroupCounts()
            _ageGroupCounts.postValue(list)
        }
    }

    private val _genericUsageList = MutableLiveData<List<GenericUsage>>()
    val genericUsageList: LiveData<List<GenericUsage>> get() = _genericUsageList

    fun fetchGenericUsageData() {
        viewModelScope.launch {
            val list = mainRepository.getGenericUsageList()
            _genericUsageList.postValue(list)
        }
    }

    private val _brandUsageList = MutableLiveData<List<BrandUsage>>()
    val brandUsageList: LiveData<List<BrandUsage>> get() = _brandUsageList

    fun fetchBrandUsageData() {
        viewModelScope.launch {
            val list = mainRepository.getBrandUsageList()
            _brandUsageList.postValue(list)
        }
    }


}