package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Cataract_Surgery_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CurrentInventoryLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_OPD_Doctors_Note
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Post_Op_AND_Follow_ups
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Investigation
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImageModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImagePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.InventoryUnitLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Medicine_Report_Model
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientPrescriptionRegistrationCombined
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Patient_RegistrationModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PrescriptionGlassesFinal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Prescription_Model
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Quadruple
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Quintuple
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleDisdributionStatusModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Tuple
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModelItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionSynTable
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LLE_MedDocket_ViewModel(private val repository: LLE_MedDocket_Repository) : ViewModel() {

    val allVitals:LiveData<List<Vitals>> = repository.allVitals
    val allVisualAcuity:LiveData<List<VisualAcuity>> = repository.allVisualAcuity
    val allRefractive_Error:LiveData<List<RefractiveError>> = repository.allRefractive_Error
    val allOPD_Investigations:LiveData<List<OPD_Investigations>> = repository.allOPD_Investigations
    val allEye_Pre_Op_Notes:LiveData<List<Eye_Pre_Op_Notes>> = repository.allEye_Pre_Op_Notes


    val allEye_Pre_Op_Investigation:LiveData<List<Eye_Pre_Op_Investigation>> = repository.allEye_Pre_Op_Investigation

    fun fetchUnsyncedInvestigations(callback: (List<Eye_Pre_Op_Investigation>) -> Unit) {
        viewModelScope.launch {
            val list = repository.getUnsyncedInvestigations()
            callback(list)
        }
    }

    fun fetchUnsyncedOpDoctorNotes(callback: (List<Eye_OPD_Doctors_Note>) -> Unit) {
        viewModelScope.launch {
            val list = repository.fetchUnsyncedOpDoctorNotes()
            callback(list)
        }
    }

    fun fetchUnsyncedPreOpNotes(callback: (List<Eye_Pre_Op_Notes>) -> Unit) {
        viewModelScope.launch {
            val list = repository.fetchUnsyncedPreOpNotes()
            callback(list)
        }
    }

    fun fetchUnsyncedSurgicalNotes(callback: (List<Cataract_Surgery_Notes>) -> Unit) {
        viewModelScope.launch {
            val list = repository.fetchUnsyncedSurgicalNotes()
            callback(list)
        }
    }

    fun fetchUnsyncedPostOPNotes(callback: (List<Eye_Post_Op_AND_Follow_ups>) -> Unit) {
        viewModelScope.launch {
            val list = repository.fetchUnsyncedPostOPNotes()
            callback(list)
        }
    }

    val allEye_Post_Op_AND_Follow_ups:LiveData<List<Eye_Post_Op_AND_Follow_ups>> = repository.allEye_Post_Op_AND_Follow_ups
    val allEye_OPD_Doctors_Note:LiveData<List<Eye_OPD_Doctors_Note>> = repository.allEye_OPD_Doctors_Note
    val allCataract_Surgery_Notes:LiveData<List<Cataract_Surgery_Notes>> = repository.allCataract_Surgery_Notes
    val allPatient:LiveData<List<PatientDataLocal>> =repository.allPatient

    suspend fun getPatientData(id: String): PatientDataLocal? {
        return repository.getPatientById(id)
    }

    val allImages:LiveData<List<ImageModel>> =repository.allImages

    val allRegistration:LiveData<List<Patient_RegistrationModel>> =repository.allRegistartion
    val allPrescription:LiveData<List<Prescription_Model>> =repository.allPrescriptionData

    val allFinalPrescriptionData:LiveData<List<PrescriptionGlassesFinal>> = repository.allFinalPrescriptionData
    val allFinalPrescriptionDataForSync:LiveData<List<PrescriptionGlassesFinal>> = repository.allFinalPrescriptionData

    val allSpectacleDisdributionStatus:LiveData<List<SpectacleDisdributionStatusModel>> = repository.allSpectacleDisdributionStatus

    val allSynData:LiveData<List<SynTable>> = repository.allSynData
    fun getSynDataByType(type: String): LiveData<List<SynTable>> {
        return repository.getSynDataByType(type)
    }


    val allCurrentInventory:LiveData<List<CurrentInventoryLocal>> = repository.allCurrentInventory

    val allInventoryUnit:LiveData<List<InventoryUnitLocal>> = repository.allInventoryUnit

    val allnewPrescription:LiveData<List<CreatePrescriptionModel>> = repository.allnewPrescription

    val getImagePrescriptionsIsSyn0:LiveData<List<ImagePrescriptionModel>> = repository.getImagePrescriptionsIsSyn0

    var currentInventoryLocal: LiveData<List<CurrentInventoryLocal>> = MutableLiveData<List<CurrentInventoryLocal>>()

    private val _toastMessageTriple = MutableLiveData<Quadruple<Int, Int,Int, String>>()

    private val _ImagetoastMessage = MutableLiveData<String>()

    private val _toastMessage = MutableLiveData<String>()

    val toastMessage: LiveData<String>
        get() = _toastMessage

    val toastImageMessage: LiveData<String>
        get() = _ImagetoastMessage


    private val _insertResponse1 = MutableLiveData<Quintuple<Int, Int,Int, Int,String>>()
    val insertResponse1: MutableLiveData<Quintuple<Int, Int,Int, Int ,String>> get() = _insertResponse1

    private val _prescriptionData = MutableLiveData<List<Prescription_Model>>()
    val prescriptionData: LiveData<List<Prescription_Model>> get() = _prescriptionData


    private val _prescriptionsStatus = MutableLiveData<List<SpectacleDisdributionStatusModel>>()
    val prescriptionsStatus: LiveData<List<SpectacleDisdributionStatusModel>> get() = _prescriptionsStatus

    private val _registrations = MutableLiveData<List<Patient_RegistrationModel>>()
    val registration: LiveData<List<Patient_RegistrationModel>> get() = _registrations


    fun showToast(message: String) {
      //  _toastMessage.value = message
        if (_toastMessage.value != message) {
            _toastMessage.value = message
        }
    }

    fun showImageToast(message: String) {

        if (_ImagetoastMessage.value != message) {
            _ImagetoastMessage.value = message
        }
    }


    fun deleteVitals(vitals: Vitals)
    {
        viewModelScope.launch {
            repository.deleteVitals(vitals)
        }
    }

    fun deleteVisualAcuity(visualAcuity: VisualAcuity)
    {
        viewModelScope.launch {
            repository.deleteVisualAcuity(visualAcuity)
        }
    }


    fun deleteRefractiveError(refractiveError: RefractiveError)
    {
        viewModelScope.launch {
            repository.deleteRefractiveError(refractiveError)
        }
    }

    fun insertEyeOPD_Doctors_Note(eyeopdDoctorsNote: Eye_OPD_Doctors_Note) {

        viewModelScope.launch {
            try {
                repository.insertEyeOPD_DoctorsNote(eyeopdDoctorsNote)
                showToast("Eye OPD Doctors Note data is inserted successfully")
            } catch (e: Exception) {
                showToast("Insert Eye OPD Doctors Note failed: ${e.message}")
            }
        }

    }

    fun insertEyePre_OP_Investigations(eyePreOpInvestigation: Eye_Pre_Op_Investigation) {

        viewModelScope.launch {
            try {
                repository.insertEyePreOPInvestigations(eyePreOpInvestigation)
                showToast("Eye Pre-OP Investigation data is inserted successfully")
            } catch (e: Exception) {
                showToast("Insert Eye Pre-OP Investigation failed: ${e.message}")
            }
        }

    }

    fun insertEye_Pre_Op_Notes1(eyePreNotes: Eye_Pre_Op_Notes)
    {
        viewModelScope.launch {
            try {
                val result = repository.insertEyePreOPNotes1(eyePreNotes)

                _insertResponse1.value = result
                showToast("Eye Pre-Op Notes data is inserted successfully")


            } catch (e: Exception) {
                showToast("Insert Eye Pre-OP Notes failed: ${e.message}")
            }
        }
    }


    fun insert_Surgical_Notes1(surgicalNote: Cataract_Surgery_Notes) {
        viewModelScope.launch {
            try {
                val insertedId = repository.insertSurgicalNotes1(surgicalNote)
                _insertResponse1.value = insertedId
                showToast("Surgical Notes data is inserted successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Insert Surgical Notes failed: ${e.message}")
            }
        }
    }


    fun insertEyePostOpAndFollowUps(eyePostOpAndFollowUps: Eye_Post_Op_AND_Follow_ups) {

        viewModelScope.launch {
            try {
                repository.insertEyePostOpAndFollowUps(eyePostOpAndFollowUps)
                showToast("Eye Post Op and Follow Ups data is inserted successfully")
            } catch (e: Exception) {
                showToast("Insert Eye Post Op and Follow Ups failed: ${e.message}")
            }
        }

    }

    fun insertPatient(patientData: PatientDataLocal?) {
        viewModelScope.launch {
            try {
                repository.insertPatient(patientData)
                showToast("Patient is inserted successfully")
            } catch (e: Exception) {
                showToast("Insert Patient failed: ${e.message}")
            }
        }

    }

    fun InsertImageLocal(imageModel: ImageModel)
    {
        viewModelScope.launch {
            try {
                repository.insertImage(imageModel)
                showImageToast("Image is inserted successfully")
            } catch (e: Exception) {
                showImageToast("Insert Image failed: ${e.message}")
            }
        }
    }
    // ✅ Correct suspend versions — no viewModelScope.launch
    suspend fun updateEyeopddocnotes(_id: String, syn: Int) {
        try {
            Log.d("pawan_sync", "Updating EyeOPDDoctorNote isSyn=$syn for local record _id=$_id")
            repository.updateEyeopddocnotes(_id, syn)
            Log.d("pawan_sync", "✅ Successfully updated EyeOPDDoctorNote _id=$_id to isSyn=$syn")
        } catch (e: Exception) {
            Log.e("pawan_sync", "❌ Failed to update EyeOPDDoctorNote _id=$_id: ${e.message}", e)
        }
    }

    suspend fun updateEye_Pre_Op_Investigations(_id: String, syn: Int) {
        try {
            Log.d("pawan_sync", "Updating Eye_Pre_Op_Investigations isSyn=$syn for local record _id=$_id")
            repository.updateEye_Pre_Op_Investigations(_id, syn)
            Log.d("pawan_sync", "✅ Successfully updated Eye_Pre_Op_Investigations _id=$_id to isSyn=$syn")
        } catch (e: Exception) {
            Log.e("pawan_sync", "❌ Failed to update Eye_Pre_Op_Investigations _id=$_id: ${e.message}", e)
        }
    }

    suspend fun updateEye_Pre_Op_Notes(_id: String, syn: Int) {
        try {
            Log.d("pawan_sync", "Updating Eye_Pre_Op_Notes isSyn=$syn for local record _id=$_id")
            repository.updateEye_Pre_Op_Notes(_id, syn)
            Log.d("pawan_sync", "✅ Successfully updated Eye_Pre_Op_Notes _id=$_id to isSyn=$syn")
        } catch (e: Exception) {
            Log.e("pawan_sync", "❌ Failed to update Eye_Pre_Op_Notes _id=$_id: ${e.message}", e)
        }
    }

    suspend fun updateCataract_Surgeries(_id: Int, syn: Int) {
        try {
            Log.d("pawan_sync", "Updating Cataract_Surgeries isSyn=$syn for local record _id=$_id")
            repository.updateCataract_Surgeries(_id, syn)
            Log.d("pawan_sync", "✅ Successfully updated Cataract_Surgeries _id=$_id to isSyn=$syn")
        } catch (e: Exception) {
            Log.e("pawan_sync", "❌ Failed to update Cataract_Surgeries _id=$_id: ${e.message}", e)
        }
    }

    suspend fun UpdatePostOpAndFollowUpsResponse(_id: String, syn: Int) {
        try {
            Log.d("pawan_sync", "Updating PostOpAndFollowUps isSyn=$syn for local record _id=$_id")
            repository.UpdatePostOpAndFollowUps(_id, syn)
            Log.d("pawan_sync", "✅ Successfully updated PostOpAndFollowUps _id=$_id to isSyn=$syn")
        } catch (e: Exception) {
            Log.e("pawan_sync", "❌ Failed to update PostOpAndFollowUps _id=$_id: ${e.message}", e)
        }
    }


    fun updateImage(_id: String, syn: Int) {

        viewModelScope.launch {
            try {
                repository.updateImage(_id,syn)
                //showToast("Vitals data is update successfully")
            } catch (e: Exception) {
                showToast("Insert Vitals failed: ${e.message}")
            }
        }

    }

    fun deleteOPDInvestigations(opdInvestigations: OPD_Investigations) {
        viewModelScope.launch {
            repository.deleteOPD_Investigations(opdInvestigations)
        }

    }

    fun deleteEyePostOpAndFollowUps(eyePostOpAndFollowUps: Eye_Post_Op_AND_Follow_ups) {

        viewModelScope.launch {
            repository.deleteEyePostOpAndFollowUps(eyePostOpAndFollowUps)
        }

    }

    fun deleteEyeOPDDoctorNote(eyeOpdDoctorsNote: Eye_OPD_Doctors_Note) {

        viewModelScope.launch {
            repository.deleteEyeOPDDoctorNote(eyeOpdDoctorsNote)
        }

    }

    fun deleteEyePreOPInvestigation(eyePreOpInvestigation: Eye_Pre_Op_Investigation) {
        viewModelScope.launch {
            repository.deleteEyePreOpInvestigation(eyePreOpInvestigation)
        }
    }

    fun deleteEyePreOpNotes(eyePreOpNote: Eye_Pre_Op_Notes) {

        viewModelScope.launch {
            repository.deleteEyePreOpNote(eyePreOpNote)
        }

    }

    fun deleteSurgicalData(surgicaldata: Cataract_Surgery_Notes) {

        viewModelScope.launch {
            repository.deleteSurgicaldata(surgicaldata)
        }

    }

    fun insertRegistraion(registrationDetail: Patient_RegistrationModel) {

        viewModelScope.launch {
            try {
                repository.insertRegistration(registrationDetail)
                showToast("Registration is inserted successfully")
            } catch (e: Exception) {
                showToast("IRegistration failed: ${e.message}")
            }
        }

    }

    fun insertPrescription(prescriptionModel: Prescription_Model) {

        viewModelScope.launch {
            try {
                Log.d("pawan", "Prescription_Model => ${prescriptionModel}")
                repository.insertPrescription(prescriptionModel)
                showToast("Prescription is inserted successfully")
            } catch (e: Exception) {
                showToast("Prescription failed: ${e.message}")
            }
        }

    }

    fun getPrescriptionDataPatientID(patientId: Int) {
        Log.d("pawan", "getPrescriptionDataPatientID called with => $patientId")
        viewModelScope.launch {
            try {
                val response = repository.getPrescriptionDataPatientID(patientId)
                Log.d("pawan", "repository.getPrescription() => $response")
                _prescriptionData.value = response
            } catch (e: Exception) {
                Log.e("pawan", "Error fetching prescription => ${e.message}")
                _prescriptionData.value = emptyList()
            }
        }
    }

    fun getRegistrationByPatientId(patientID: Int) {

        viewModelScope.launch {
            try {
                _registrations.value = repository.getRegistrationByPatientId(patientID)
            } catch (e: Exception) {
                // Handle the error, log, or show an error message
            }
        }

    }

    private var _insertionprescriptionGlassesStatus = MutableLiveData<Resource<Long>>()
    val insertionprescriptionGlassesStatus: LiveData<Resource<Long>> get() = _insertionprescriptionGlassesStatus

    private var _prescriptionDataById = MutableLiveData<Resource<List<PrescriptionGlassesFinal>>>()
    val prescriptionDataById: LiveData<Resource<List<PrescriptionGlassesFinal>>> get() = _prescriptionDataById

    fun insertFinalPrescriptionData(prescriptionGlassesFinal: PrescriptionGlassesFinal) {
        viewModelScope.launch {
            try {
                // DAO insert returns the new rowId (Long)
                val rowId = repository.insertPrescriptionGlassesFinal(prescriptionGlassesFinal)

                _insertionprescriptionGlassesStatus.postValue(Resource.success(rowId))
            } catch (e: Exception) {
                _insertionprescriptionGlassesStatus.postValue(
                    Resource.error("Insert PrescriptionGlasses failed: ${e.message}", null)
                )
            }
        }
    }

    fun getPostOpDetailsById(localPatientId: Int, patientId : Int) = CoroutineScope(Dispatchers.IO).launch {
        _prescriptionDataById.postValue(Resource.loading(null))
        try {
            try {
                repository.getPostListById(localPatientId, patientId).let {
                    _prescriptionDataById.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _prescriptionDataById.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }

        } catch (e: Exception) {
            _prescriptionDataById.postValue(Resource.error(e.message.toString(), null))
        }
    }


    fun insertSpectacleDisdributionStatusModel(spectacleDisdributionStatusModel: SpectacleDisdributionStatusModel) {

        viewModelScope.launch {
            try {
                repository.insertSpectacleDisdributionStatusModel(spectacleDisdributionStatusModel)
                showToast("SpectacleDisdributionStatusModel data is inserted successfully")
            } catch (e: Exception) {
                showToast("Insert SpectacleDisdributionStatusModel failed: ${e.message}")
            }
        }
    }


    fun getPatientID_By_AadharNo(identity: String) {

        viewModelScope.launch {
            try {
                _registrations.value = repository.getPatientID_By_AadharNo(identity)
            } catch (e: Exception) {
                // Handle the error, log, or show an error message
            }
        }

    }

    fun getPrescriptionStatusDetailsWithPaginationNew(patientId: Int, pageSize: Int, page: Int, forceRefresh: Boolean) {
        viewModelScope.launch {
            try {
                _prescriptionsStatus.value = repository.getPrescriptionStatusDetailsNew(patientId, pageSize, page, forceRefresh)
                Log.d("pawan", "_prescriptionsStatus.value=>" + _prescriptionsStatus.value)
            } catch (e: Exception) {
                // Handle the error, log, or show an error message
            }
        }
    }

    fun UpdatePrescriptionGlassesResponse(_id: String, syn: Int) {

        viewModelScope.launch {
            try {
                repository.UpdatePrescriptionGlassesResponse(_id,syn)
                showToast("PrescriptionGlasses is updated successfully")
            } catch (e: Exception) {
                showToast("Update PrescriptionGlasses failed: ${e.message}")
            }
        }

    }

    fun insertSynedData(synTableRequest: SynTable, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            val success = repository.insertSynedData(synTableRequest)
            onSuccess(success)
        }
    }

    fun updateSynedData(toInt: Int, syn: Int) {

        viewModelScope.launch {
            try {
                repository.updateSynedData(toInt,syn)
               // showToast("PrescriptionGlasses is updated successfully")
            } catch (e: Exception) {
                showToast("Update PrescriptionGlasses failed: ${e.message}")
            }
        }

    }

    fun insertCurrentInventoryItemNew(inventoryList: List<CurrentInventoryLocal>) {
        viewModelScope.launch {
            repository.insertCurrentInventoryNew(inventoryList)
        }
    }

    fun deleteAllCurrentInventory() {
        viewModelScope.launch {
            repository.deleteAllCurrentInventory()
        }
    }

    fun deleteAllInventoryUnit() {
        viewModelScope.launch {
            repository.deleteAllInventoryUnit()
        }
    }

    fun insertInventoryUnit(map: List<InventoryUnitLocal>) {
        viewModelScope.launch {
            repository.insertInventoryUnit(map)
        }

    }

    fun getAllCurrentInventory(){
        viewModelScope.launch {
            currentInventoryLocal = repository.getAllCurrentInventory()
        }
    }


    fun insertImagePrescriptionModel(imagePrescription: ImagePrescriptionModel) {

        viewModelScope.launch {
            try {
                repository.insertImagePrescriptionModel(imagePrescription)
                showImageToast("Image Prescription is inserted successfully")
            } catch (e: Exception) {
                showImageToast("Insert Image failed: ${e.message}")
            }
        }

    }

    fun updateImagePrescription(_id: Int, syn: Int) {

        viewModelScope.launch {
            try {
                repository.updateImagePrescriptionModel(_id,syn)
                //showToast("Vitals data is update successfully")
            } catch (e: Exception) {
                showToast("Insert Vitals failed: ${e.message}")
            }
        }

    }




}

