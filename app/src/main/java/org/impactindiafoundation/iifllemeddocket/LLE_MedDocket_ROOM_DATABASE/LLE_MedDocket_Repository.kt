package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import org.impactindiafoundation.iifllemeddocket.Data.RetrofitInstance

import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Cataract_Surgery_Notes_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.CreatePrescriptionDAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.CurrentInventory_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Eye_OPD_Doctors_Note_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Eye_Post_Op_AND_Follow_ups_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Eye_Pre_Op_Investigation_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Eye_Pre_Op_Notes_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.FinalPrescriptionDrugDAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Final_Prescription_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Image_Prescription_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Image_Upload_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.InventoryUnit_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.OPD_Investigations_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.PatientDao
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Prescription_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Refractive_Error_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Registration_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.SpectacleDisdributionStatus_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.SynTable_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.VisualAcuity_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Vital_DAO
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Patient_RegistrationModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PrescriptionGlassesFinal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Prescription_Model
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Quadruple
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Quintuple
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleDisdributionStatusModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel.AddEyePreOpInvestigationsRequest
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel.EyePreOpInvestigation
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionSynTable
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpDetailsItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpDetailsRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPreOpDetailsResponse
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity


class LLE_MedDocket_Repository(private val Vital_DAO: Vital_DAO,
                               private val VisualAcuity_DAO: VisualAcuity_DAO,
                               private val Refractive_Error_DAO: Refractive_Error_DAO,
                               private val OPD_Investigations_DAO: OPD_Investigations_DAO,
                               private val Eye_Pre_Op_Notes_DAO: Eye_Pre_Op_Notes_DAO,
                               private val Eye_Pre_Op_Investigation_DAO: Eye_Pre_Op_Investigation_DAO,
                               private val Eye_Post_Op_AND_Follow_ups_DAO: Eye_Post_Op_AND_Follow_ups_DAO,
                               private val Eye_OPD_Doctors_Note_DAO: Eye_OPD_Doctors_Note_DAO,
                               private val Cataract_Surgery_Notes_DAO: Cataract_Surgery_Notes_DAO,
                               private val Patient_DAO:PatientDao,
                               private val Image_Upload_DAO:Image_Upload_DAO,
                               private val Registration_DAO:Registration_DAO,
                               private val Prescription_DAO: Prescription_DAO,
                               private val Final_Prescription_DAO: Final_Prescription_DAO,
                               private val SpectacleDisdributionStatus_DAO: SpectacleDisdributionStatus_DAO,
                               private val SynTable_DAO: SynTable_DAO,
                               private val CurrentInventory_DAO: CurrentInventory_DAO,
                               private val InventoryUnit_DAO: InventoryUnit_DAO,
                               private val CreatePrescriptionDAO: CreatePrescriptionDAO,
                               private val Image_Prescription_DAO: Image_Prescription_DAO,
                               private val FinalPrescriptionDrugDAO: FinalPrescriptionDrugDAO,
                               private val database: LLE_MedDocket_Room_Database
) {

    val allVitals: LiveData<List<Vitals>> = Vital_DAO.getAllVitailsData()

    val allVisualAcuity: LiveData<List<VisualAcuity>> = VisualAcuity_DAO.getAllVisualAcuityData()
    val allRefractive_Error: LiveData<List<RefractiveError>> =
        Refractive_Error_DAO.getAllRefractiveErrorData()
    val allOPD_Investigations: LiveData<List<OPD_Investigations>> =
        OPD_Investigations_DAO.getAllOPD_InvestigationsData()
    val allEye_Pre_Op_Notes: LiveData<List<Eye_Pre_Op_Notes>> =
        Eye_Pre_Op_Notes_DAO.getAllEye_Pre_Op_Notes()
    val allEye_Pre_Op_Investigation: LiveData<List<Eye_Pre_Op_Investigation>> =
        Eye_Pre_Op_Investigation_DAO.getAllEye_Pre_Op_Investigation()

    //get unsync investigation
    suspend fun getUnsyncedInvestigations(): List<Eye_Pre_Op_Investigation> {
        return Eye_Pre_Op_Investigation_DAO.getUnsyncedInvestigationsOnce()
    }

    suspend fun fetchUnsyncedOpDoctorNotes(): List<Eye_OPD_Doctors_Note> {
        return Eye_OPD_Doctors_Note_DAO.fetchUnsyncedOpDoctorNotes()
    }

    suspend fun fetchUnsyncedPreOpNotes(): List<Eye_Pre_Op_Notes> {
        return Eye_Pre_Op_Notes_DAO.fetchUnsyncedPreOpNotes()
    }

    suspend fun fetchUnsyncedSurgicalNotes(): List<Cataract_Surgery_Notes> {
        return Cataract_Surgery_Notes_DAO.fetchUnsyncedSurgicalNotes()
    }

    suspend fun fetchUnsyncedPostOPNotes(): List<Eye_Post_Op_AND_Follow_ups> {
        return Eye_Post_Op_AND_Follow_ups_DAO.fetchUnsyncedPostOPNotes()
    }

    val allEye_Post_Op_AND_Follow_ups: LiveData<List<Eye_Post_Op_AND_Follow_ups>> =
        Eye_Post_Op_AND_Follow_ups_DAO.getAllEye_Post_Op_AND_Follow_upsData()
    val allEye_OPD_Doctors_Note: LiveData<List<Eye_OPD_Doctors_Note>> =
        Eye_OPD_Doctors_Note_DAO.getAllEye_OPD_Doctors_Note()
    val allCataract_Surgery_Notes: LiveData<List<Cataract_Surgery_Notes>> =
        Cataract_Surgery_Notes_DAO.getAllCataract_Surgery_Notes()
    val allPatient: LiveData<List<PatientDataLocal>> = Patient_DAO.getAllpatientFromLocal()

    suspend fun getPatientById(id: String): PatientDataLocal? {
        return Patient_DAO.getPatientById(id)
    }

    val allImages: LiveData<List<ImageModel>> = Image_Upload_DAO.getAllImage()

    val getAllValidImages: LiveData<List<ImageModel>> = Image_Upload_DAO.getAllValidImages()


    val allRegistartion: LiveData<List<Patient_RegistrationModel>> =
        Registration_DAO.getAllRegistrationData()

    val allPrescriptionData: LiveData<List<Prescription_Model>> = Prescription_DAO.getAllPrescriptionData()

    val allFinalPrescriptionData:LiveData<List<PrescriptionGlassesFinal>> = Final_Prescription_DAO.getAll_PrescriptionGlassesFinal()

    val allSpectacleDisdributionStatus:LiveData<List<SpectacleDisdributionStatusModel>> = SpectacleDisdributionStatus_DAO.getAllSpectacleDisdributionStatusModel()

    val allSynData:LiveData<List<SynTable>> = SynTable_DAO.getAllSynData()

    fun getSynDataByType(type: String): LiveData<List<SynTable>> {
        return SynTable_DAO.getSynDataByType(type)
    }

    val allCurrentInventory:LiveData<List<CurrentInventoryLocal>> = CurrentInventory_DAO.getAllCurrentInventory()

    val allInventoryUnit:LiveData<List<InventoryUnitLocal>> = InventoryUnit_DAO.getAllInventoryUnit()

    val allnewPrescription:LiveData<List<CreatePrescriptionModel>> = CreatePrescriptionDAO.getAllCreatePrescription()

    val getImagePrescriptionsIsSyn0:LiveData<List<ImagePrescriptionModel>> = Image_Prescription_DAO.getImagePrescriptionsIsSyn0()

    private var cachedData: List<SpectacleDisdributionStatusModel>? = null

    suspend fun deleteVitals(vitals: Vitals) {
        database.performDatabaseOperation {

            Vital_DAO.deleteVitals(vitals)

        }
    }

    suspend fun deleteVisualAcuity(visualAcuity: VisualAcuity) {
        database.performDatabaseOperation {

            VisualAcuity_DAO.deleteVisualAcuity(visualAcuity)

        }
    }

    suspend fun deleteRefractiveError(refractiveError: RefractiveError) {
        database.performDatabaseOperation {

            Refractive_Error_DAO.deleteRefractiveError(refractiveError)

        }
    }

    suspend fun deleteOPD_Investigations(opdInvestigations: OPD_Investigations) {
        database.performDatabaseOperation {

            OPD_Investigations_DAO.deleteOPD_Investigations(opdInvestigations)

        }
    }

    suspend fun insertEyeOPD_DoctorsNote(eyeopdDoctorsNote: Eye_OPD_Doctors_Note) {

        return database.performDatabaseOperation {
            Eye_OPD_Doctors_Note_DAO.insertEye_OPD_Doctors_Note(eyeopdDoctorsNote)
            true // Indicate success
        }

    }

    suspend fun insertEyePreOPInvestigations(eyePreOpInvestigation: Eye_Pre_Op_Investigation) {

        return database.performDatabaseOperation {
            Eye_Pre_Op_Investigation_DAO.insertEye_Pre_Op_Investigation(eyePreOpInvestigation)
            true // Indicate success
        }

    }

    suspend fun insertEyePreOPNotes1(eyePreNotes: Eye_Pre_Op_Notes): Quintuple<Int, Int, Int, Int, String> {
        return database.performDatabaseOperation3 {
            val insertedId = Eye_Pre_Op_Notes_DAO.insertEye_Pre_Op_Notes(eyePreNotes)
            if (insertedId != -1L) {
                Quintuple(
                    insertedId.toInt(),
                    eyePreNotes.camp_id,
                    eyePreNotes.patient_id,
                    eyePreNotes.user_id.toInt(),
                    eyePreNotes.eyePreOpNotesImagepath
                )
            } else {
                throw RuntimeException("Insert failed") // Indicate failure
            }
        }
    }


    suspend fun insertSurgicalNotes(surgicalNote: Cataract_Surgery_Notes) {
        return database.performDatabaseOperation {
            Cataract_Surgery_Notes_DAO.insertCataract_Surgery_Notes(surgicalNote)
            true // Indicate success
        }
    }

    suspend fun insertSurgicalNotes1(surgicalNote: Cataract_Surgery_Notes): Quintuple<Int, Int, Int, Int, String> {
        return database.performDatabaseOperation3 {
            val insertedId = Cataract_Surgery_Notes_DAO.insertCataract_Surgery_Notes1(surgicalNote)
            if (insertedId != -1L) {
                Quintuple(
                    insertedId.toInt(),
                    surgicalNote.camp_id,
                    surgicalNote.patient_id,
                    surgicalNote.userId.toInt(),
                    surgicalNote.filepath
                )
            } else {
                throw RuntimeException("Insert failed") // Indicate failure
            }
        }
    }

    suspend fun insertEyePostOpAndFollowUps(eyePostOpAndFollowUps: Eye_Post_Op_AND_Follow_ups) {
        return database.performDatabaseOperation {
            Eye_Post_Op_AND_Follow_ups_DAO.insertEye_Post_Op_AND_Follow_ups(eyePostOpAndFollowUps)
            true // Indicate success
        }
    }

    suspend fun insertPatient(patientData: PatientDataLocal?) {

        return database.performDatabaseOperation {
            if (patientData != null) {
                Patient_DAO.insertPatient(patientData)
            }
            true // Indicate success
        }

    }

    suspend fun insertImage(imageModel: ImageModel) {
        return database.performDatabaseOperation {
            if (imageModel != null) {
                Image_Upload_DAO.insertImage(imageModel)
            }
            true // Indicate success
        }
    }

    suspend fun updateEyeopddocnotes(_id: String, syn: Int) {
        return database.performDatabaseOperation {
            Eye_OPD_Doctors_Note_DAO.updateEyeopddocnotes(_id.toInt(), syn)
            true // Indicate success
        }

    }

    suspend fun updateEye_Pre_Op_Investigations(_id: String, syn: Int) {

        return database.performDatabaseOperation {
            Eye_Pre_Op_Investigation_DAO.updateEye_Pre_Op_Investigations(_id.toInt(), syn)
            true // Indicate success
        }

    }

    suspend fun updateEye_Pre_Op_Notes(_id: String, syn: Int) {

        return database.performDatabaseOperation {
            Eye_Pre_Op_Notes_DAO.updateEye_Pre_Op_Notes(_id.toInt(), syn)
            true // Indicate success
        }
    }

    suspend fun updateCataract_Surgeries(_id: Int, syn: Int) {

        return database.performDatabaseOperation {
            Cataract_Surgery_Notes_DAO.updateCataract_Surgeries(_id.toInt(), syn)
            true // Indicate success
        }

    }

    suspend fun UpdatePostOpAndFollowUps(_id: String, syn: Int) {

        return database.performDatabaseOperation {
            Eye_Post_Op_AND_Follow_ups_DAO.UpdatePostOpAndFollowUps(_id.toInt(), syn)
            true // Indicate success
        }

    }

    suspend fun updateImage(_id: String, syn: Int) {
        return database.performDatabaseOperation {
            Image_Upload_DAO.updateImage(_id.toInt(), syn)
            true // Indicate success
        }

    }

    suspend fun deleteEyePostOpAndFollowUps(eyePostOpAndFollowUps: Eye_Post_Op_AND_Follow_ups) {
        database.performDatabaseOperation {
            Eye_Post_Op_AND_Follow_ups_DAO.deleteEye_Post_Op_AND_Follow_ups(eyePostOpAndFollowUps)
            true

        }
    }

    suspend fun deleteEyeOPDDoctorNote(eyeOpdDoctorsNote: Eye_OPD_Doctors_Note) {
        database.performDatabaseOperation {
            Eye_OPD_Doctors_Note_DAO.deleteEye_OPD_Doctors_Note(eyeOpdDoctorsNote)
            true

        }
    }

    suspend fun deleteEyePreOpInvestigation(eyePreOpInvestigation: Eye_Pre_Op_Investigation) {
        database.performDatabaseOperation {
            Eye_Pre_Op_Investigation_DAO.deleteEye_Pre_Op_Investigation(eyePreOpInvestigation)
        }
    }

    suspend fun deleteEyePreOpNote(eyePreOpNote: Eye_Pre_Op_Notes) {
        database.performDatabaseOperation {
            Eye_Pre_Op_Notes_DAO.deleteEye_Pre_Op_Notes(eyePreOpNote)
        }
    }

    suspend fun deleteSurgicaldata(surgicaldata: Cataract_Surgery_Notes) {

        database.performDatabaseOperation {
            Cataract_Surgery_Notes_DAO.deleteCataract_Surgery_Notes(surgicaldata)
        }

    }

    suspend fun insertRegistration(registrationDetail: Patient_RegistrationModel) {

        database.performDatabaseOperation {

            Registration_DAO.insertRegistrations(registrationDetail)

        }

    }

    suspend fun insertPrescription(prescriptionModel: Prescription_Model) {

        database.performDatabaseOperation {
            Log.d("pawan", "Prescription_Model => ${prescriptionModel}")
            Prescription_DAO.insertPrescription(prescriptionModel)

        }

    }

    suspend fun getPrescriptionDataPatientID(patientId: Int): List<Prescription_Model> {
        return Prescription_DAO.getPrescriptionsByPatientId(patientId)
    }

    suspend fun getRegistrationByPatientId(patientID: Int): List<Patient_RegistrationModel>? {

        return Registration_DAO.getRegistrationByPatientId(patientID)

    }

    suspend fun insertPrescriptionGlassesFinal(
        prescriptionGlassesFinal: PrescriptionGlassesFinal
    ): Long {
        return database.performDatabaseOperations {
            Final_Prescription_DAO.insertPrescription_Glasses_Final(prescriptionGlassesFinal)
        }
    }

    fun getPostListById(localPatientId: Int, patientId: Int) =
        Final_Prescription_DAO.getPostOpDetailsById(localPatientId, patientId)


    suspend fun insertSpectacleDisdributionStatusModel(spectacleDisdributionStatusModel: SpectacleDisdributionStatusModel) {


        return database.performDatabaseOperation {
            SpectacleDisdributionStatus_DAO.insertSpectacleDisdributionStatusModel(spectacleDisdributionStatusModel)
        }
    }

    suspend fun getPatientID_By_AadharNo(identity: String): List<Patient_RegistrationModel>? {

        return Registration_DAO.getPatientIdAndAadharNoByAadharNo(identity)



    }

    suspend fun GetPrescriptionStatusDetails(patientId: Int, forceRefresh: Boolean): List<SpectacleDisdributionStatusModel>? {
        // Check if forceRefresh is true or if the cache is empty
        if (forceRefresh || cachedData == null) {
            // Invalidate the cache before fetching the data
            invalidateDataSource()

            // Fetch the data from the database and update the cache
            cachedData = database.performDatabaseOperation6 {
                SpectacleDisdributionStatus_DAO.GetPrescriptionStatusDetails(patientId)
            }
        }

        return cachedData.orEmpty()
    }

    suspend fun getPrescriptionStatusDetailsNew(patientId: Int, pageSize: Int, page: Int, forceRefresh: Boolean) =  SpectacleDisdributionStatus_DAO.getPrescriptionStatusDetailsWithPaginationNew(patientId)

    suspend fun UpdatePrescriptionGlassesResponse(_id: String, syn: Int) {

        return database.performDatabaseOperation {
            Final_Prescription_DAO.updatePrescription_Glasses_Final(_id.toInt(), syn)
            true // Indicate success
        }

    }

    suspend fun invalidateDataSource() {
        cachedData = null
    }

    suspend fun insertSynedData(synTableRequest: SynTable): Int {
        return try {
            Log.d("ABCDEF", "Repository.insertSynedData() -> Inserting: $synTableRequest")
            database.performDatabaseOperation {
                SynTable_DAO.insertSynData(synTableRequest)
            }
            Log.d("ABCDEF", "Repository.insertSynedData() -> Success")
            1
        } catch (e: Exception) {
            Log.d("ABCDEF", "Repository.insertSynedData() -> Exception: ${e.message}")
            0
        }
    }

    suspend fun updateSynedData(toInt: Int, syn: Int) {

        return database.performDatabaseOperation {
            SynTable_DAO.updateSynData(toInt.toInt(), syn)
            true // Indicate success
        }

    }

    suspend fun insertCurrentInventoryNew(inventoryList: List<CurrentInventoryLocal>) {
            CurrentInventory_DAO.insertCurrentInventory(inventoryList)
    }

    suspend fun deleteAllCurrentInventory() {
        CurrentInventory_DAO.deleteAllCurrentInventory()
    }

  suspend  fun deleteAllInventoryUnit() {

        InventoryUnit_DAO.deleteAllInventoryUnit()
    }

    suspend fun insertInventoryUnit(map: List<InventoryUnitLocal>) {
        database.performDatabaseOperation {
            InventoryUnit_DAO.insertInventoryUnit(map)
        }

    }

   suspend fun updateNewPrescriptionData(toInt: Int, syn: Int) {

        return database.performDatabaseOperation {
            CreatePrescriptionDAO.updateCreatePrescriptionModel(toInt.toInt(), syn)
            true // Indicate success
        }

    }

    suspend fun insertImagePrescriptionModel(imagePrescription: ImagePrescriptionModel) {

        return database.performDatabaseOperation {
            if (imagePrescription != null) {
                Image_Prescription_DAO.insertImagePrescriptionModel(imagePrescription)
            }
            true // Indicate success
        }

    }

   suspend fun updateImagePrescriptionModel(_id: Int, syn: Int) {

        return database.performDatabaseOperation {
            Image_Prescription_DAO.updateImagePrescriptionModel(_id.toInt(), syn)
            true // Indicate success
        }

    }
    suspend fun getAllCurrentInventory() = CurrentInventory_DAO.getAllCurrentInventory()



}





