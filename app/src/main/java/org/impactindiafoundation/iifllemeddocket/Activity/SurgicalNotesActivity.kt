package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.LLE_MedDocket_Repository
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.LLE_MedDocket_Room_Database
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.LLE_MedDocket_ViewModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.LLE_MedDocket_ViewModelFactory
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImageModel
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.RealPathUtil1
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivitySurgicalNotesBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SurgicalNotesActivity:AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    lateinit var binding:ActivitySurgicalNotesBinding
    lateinit var customDropDownAdapter: CustomDropDownAdapter
    var PatientConfirmArrayList:ArrayList<String>?=null
    private val GALLERY:Int = 1
    private  var CAMERA:Int = 2
    var filePath:String?=null
    var UpdatedfilePath:String=""
    var PatientHaveArrayList:ArrayList<String>?=null
    var SurgenReviewArrayList:ArrayList<String>?=null
    var BeforeleavingOpRoomArrayList:ArrayList<String>?=null
    var GenderArrayList:ArrayList<String>?=null
    var Sclera_Cornea_ArrayList:ArrayList<String>?=null
    var ScleraArrayList:ArrayList<String>?=null
    var selectedSurgenAndNurseOrallyConfirmList = mutableListOf<String>()
    val sn_intra_occular_lensArrayList= mutableListOf<String>()
    val sn_type_of_surgery_list= mutableListOf<String>()
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    var sn_anticoagulant_detail:String=""
    var sn_before_incision_all_team:String=""
    var sn_cataract_lieberman:String=""
     var sn_cataract_limb :String=""
     var sn_cataract_formed :String=""
     var sn_cataract_hydrodissectiirs :String=""
     var sn_cataract_mac :String=""
     var sn_cataract_viscoelastic :String=""
     var sn_cataract_universal :String=""
     var sn_cataract_keretome :String=""
     var sn_cataract_knife :String=""
     var sn_cataract_nucleus :String=""
     var sn_cataract_keretome_phaco :String=""
     var sn_cataract_sinsky :String=""
     var sn_cataract_irrigation :String=""
    var sn_cataract_capsulotomy :String=""
    var sn_cataract_colibri :String=""
    var sn_cataract_castroviejo :String=""
    var sn_common_dislocation :String=""
    var sn_common_endophthalmitis :String=""
    var sn_common_endothelial :String=""
    var sn_common_fluid :String=""
    var sn_common_hyphema :String=""
    var sn_common_light :String=""
    var sn_common_macular :String=""
    var sn_common_ocular :String=""
    var sn_common_posterior_opacification :String=""
    var sn_common_posterior_rent :String=""
    var sn_common_retinal :String=""
    var sn_common_vitreous :String=""
    var sn_intra_adrenaline:String=""
    var sn_intra_combination:String=""
    var sn_intra_gentamycin:String=""
    var sn_intra_intasol:String=""
    var sn_intra_mannitol:String=""
    var sn_intra_moxifloxacin:String=""
    var sn_intra_prednisolone:String=""
    var sn_intra_vigamox:String=""
    var sn_intra_visco:String=""
    var sn_post_cifloxacin:String=""
    var sn_post_diclofenac:String=""
    var sn_post_dimox:String=""
    var sn_post_eye_1:String=""
    var sn_post_eye_2:String=""
    var sn_post_eye_3:String=""
    var sn_post_eye_4:String=""
    var sn_post_eye_5:String=""
    var sn_post_eye_homide:String=""
    var sn_post_eye_hypersol:String=""
    var sn_post_eye_lubricant:String=""
    var sn_post_eye_moxifloxacin:String=""
    var sn_post_eye_timolol:String=""
    var sn_post_pantaprezol:String=""
    var sn_before_or_key:String=""
    var sn_before_or_weather:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySurgicalNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        getViewModel()
        createRoomDatabase()
        init()
    }

    private fun getViewModel() {
        val LLE_MedDocketRespository= LLE_MedDocketRespository()
        val LLE_MedDocketProviderFactory= LLE_MedDocketProviderFactory(LLE_MedDocketRespository,application)
        viewModel= ViewModelProvider(this,LLE_MedDocketProviderFactory).get(LLE_MedDocketViewModel::class.java)
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage(getString(R.string.please_wait))
        }
        sessionManager= SessionManager(this)
    }

    private fun createRoomDatabase() {
        val database = LLE_MedDocket_Room_Database.getDatabase(this)

        val Vital_DAO : Vital_DAO =database.Vital_DAO()
        val VisualAcuity_DAO : VisualAcuity_DAO =database.VisualAcuity_DAO()
        val Refractive_Error_DAO: Refractive_Error_DAO =database.Refractive_Error_DAO()
        val OPD_Investigations_DAO: OPD_Investigations_DAO =database.OPD_Investigations_DAO()
        val Eye_Pre_Op_Notes_DAO : Eye_Pre_Op_Notes_DAO =database.Eye_Pre_Op_Notes_DAO()
        val Eye_Pre_Op_Investigation_DAO : Eye_Pre_Op_Investigation_DAO =database.Eye_Pre_Op_Investigation_DAO()
        val Eye_Post_Op_AND_Follow_ups_DAO : Eye_Post_Op_AND_Follow_ups_DAO =database.Eye_Post_Op_AND_Follow_ups_DAO()
        val Eye_OPD_Doctors_Note_DAO : Eye_OPD_Doctors_Note_DAO =database.Eye_OPD_Doctors_Note_DAO()
        val Cataract_Surgery_Notes_DAO: Cataract_Surgery_Notes_DAO =database.Cataract_Surgery_Notes_DAO()
        val Patient_DAO: PatientDao =database.PatientDao()
        val Image_Upload_DAO: Image_Upload_DAO =database.Image_Upload_DAO()
        val Registration_DAO: Registration_DAO = database.Registration_DAO()
        val Prescription_DAO: Prescription_DAO = database.Prescription_DAO()
        val SynTable_DAO: SynTable_DAO = database.SynTable_DAO()
        val Final_Prescription_DAO: Final_Prescription_DAO =database.Final_Prescription_DAO()
        val SpectacleDisdributionStatus_DAO: SpectacleDisdributionStatus_DAO =database.SpectacleDisdributionStatus_DAO()
        val CurrentInventory_DAO: CurrentInventory_DAO =database.CurrentInventory_DAO()
        val InventoryUnit_DAO: InventoryUnit_DAO =database.InventoryUnit_DAO()
        val CreatePrescriptionDAO: CreatePrescriptionDAO =database.CreatePrescriptionDAO()
        val Image_Prescription_DAO: Image_Prescription_DAO =database.Image_Prescription_DAO()
        val FinalPrescriptionDrugDAO: FinalPrescriptionDrugDAO =database.FinalPrescriptionDrugDAO()

        val repository = LLE_MedDocket_Repository(Vital_DAO, VisualAcuity_DAO, Refractive_Error_DAO, OPD_Investigations_DAO, Eye_Pre_Op_Notes_DAO, Eye_Pre_Op_Investigation_DAO, Eye_Post_Op_AND_Follow_ups_DAO, Eye_OPD_Doctors_Note_DAO, Cataract_Surgery_Notes_DAO, Patient_DAO,Image_Upload_DAO,Registration_DAO,Prescription_DAO,Final_Prescription_DAO,SpectacleDisdributionStatus_DAO,SynTable_DAO,CurrentInventory_DAO,InventoryUnit_DAO,CreatePrescriptionDAO,Image_Prescription_DAO,FinalPrescriptionDrugDAO,database)
        viewModel1 = ViewModelProvider(this, LLE_MedDocket_ViewModelFactory(repository)).get(LLE_MedDocket_ViewModel::class.java)
    }

    private fun init() {
        binding.toolbarSurgicalNotes.toolbar.title="Surgical Notes"
        PatientConfirmArrayList= ArrayList()
        PatientConfirmArrayList!!.add("Select")
        PatientConfirmArrayList!!.add("Yes")
        PatientConfirmArrayList!!.add("No")

        PatientHaveArrayList= ArrayList()
        PatientHaveArrayList!!.add("Select")
        PatientHaveArrayList!!.add("Not Applicable")
        PatientHaveArrayList!!.add("No")
        PatientHaveArrayList!!.add("Yes")

        SurgenReviewArrayList= ArrayList()
        SurgenReviewArrayList!!.add("Select")
        SurgenReviewArrayList!!.add("Non anticipated")
        SurgenReviewArrayList!!.add("Reviewed")
        SurgenReviewArrayList!!.add("Operative duration")

        BeforeleavingOpRoomArrayList= ArrayList()
        BeforeleavingOpRoomArrayList!!.add("Select")
        BeforeleavingOpRoomArrayList!!.add("Not Applicable")
        BeforeleavingOpRoomArrayList!!.add("Yes")

        GenderArrayList= ArrayList()
        GenderArrayList!!.add("Select")
        GenderArrayList!!.add("Male")
        GenderArrayList!!.add("Female")
        GenderArrayList!!.add("Other")

        selectedSurgenAndNurseOrallyConfirmList=ArrayList()

        Sclera_Cornea_ArrayList= ArrayList()
        Sclera_Cornea_ArrayList!!.add("Select")
        Sclera_Cornea_ArrayList!!.add("Superior")
        Sclera_Cornea_ArrayList!!.add("Temporal")

        ScleraArrayList= ArrayList()
        ScleraArrayList!!.add("Select")
        ScleraArrayList!!.add("With IOL")
        ScleraArrayList!!.add("Without IOL")

        customDropDownAdapter=CustomDropDownAdapter(this,Sclera_Cornea_ArrayList!!)
        binding.SpinnerSclera1.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,ScleraArrayList!!)
        binding.SpinnerSclera2.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,Sclera_Cornea_ArrayList!!)
        binding.SpinnerCornea.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerAllergies.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerConsent.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerIdentity.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerProcedure.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerSiteMarked.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerHistoryPhysical.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerPreSurgicalAssessment.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerPreAnaesthesiaAssessment.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientHaveArrayList!!)
        binding.spinnerDifficultAirwayAspirationRisk!!.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerHistoryFlomax!!.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,PatientConfirmArrayList!!)
        binding.spinnerHistoryAnticoagulants!!.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,SurgenReviewArrayList!!)
        binding.spinnerCriticalOrUnexpectedSteps!!.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,BeforeleavingOpRoomArrayList!!)
        binding.spinnerInstrumentSponge!!.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,BeforeleavingOpRoomArrayList!!)
        binding.spinnerSpecimenLabelled!!.adapter=customDropDownAdapter
        customDropDownAdapter=CustomDropDownAdapter(this,GenderArrayList!!)
        binding.spinnerGender!!.adapter=customDropDownAdapter

        binding.edittextDateOfSurgery.setOnClickListener(this)
        binding.textViewReadMore.setOnClickListener(this)
        binding.carddviewUploadImplantLabelDetails.setOnClickListener(this)

        binding.textViewAnaesthesia!!.visibility=View.GONE
        binding.editTextTypesOfSurgeryOther.visibility=View.GONE
        binding.editTextLieberman.visibility = View.GONE
        binding.editTextFormedIrrigatingCystotomes.visibility=View.GONE
        binding.editTextColibri.visibility=View.GONE
        binding.editTextCastroviejo.visibility=View.GONE
        binding.editTextCapsulotomy.visibility=View.GONE
        binding.editTextLimb.visibility=View.GONE
        binding.editTextHydrodissectiirs.visibility=View.GONE
        binding.editTextMacPhersonForcep.visibility=View.GONE
        binding.editTextViscoelasticCannula.visibility=View.GONE
        binding.editTextUniversalStSidePort.visibility=View.GONE
        binding.editTextIrrigation.visibility=View.GONE
        binding.editTextSinsky.visibility=View.GONE
        binding.editTextKeretome3.visibility=View.GONE
        binding.editTextKeretome2.visibility=View.GONE
        binding.editTextKnife.visibility=View.GONE
        binding.editTextNucleus.visibility=View.GONE
        binding.editTextPosterior.visibility=View.GONE
        binding.editIextLightSensitivity.visibility=View.GONE
        binding.editTextFluidCollection.visibility=View.GONE
        binding.editTextMacularOdema.visibility=View.GONE
        binding.editTextPosteriorCapsularOpacification.visibility=View.GONE
        binding.editTextEndothelialDecompermation.visibility=View.GONE
        binding.editTextHyphema.visibility=View.GONE
        binding.editTextRentinalTear.visibility=View.GONE
        binding.editTextVitreousDechatments.visibility=View.GONE
        binding.editTextDislocation.visibility=View.GONE
        binding.edittextOcularHypertension.visibility=View.GONE
        binding.edittextEndophthalmitis.visibility=View.GONE
        binding.edittextVigamox.visibility=View.GONE
        binding.editTextPrednisolone.visibility=View.GONE
        binding.editTextCombinationOfInjGentamycin.visibility=View.GONE
        binding.editTextVisco.visibility=View.GONE
        binding.editTextIntasol500.visibility=View.GONE
        binding.editTextInjMannitol.visibility=View.GONE
        binding.editTextInjGentamycin.visibility=View.GONE
        binding.editTextInjMoxifloxacin.visibility=View.GONE
        binding.edittextInjAdrenaline.visibility=View.GONE
        binding.edittextTabCifloxacin.visibility=View.GONE
        binding.edittextTabDiclofenacSodium.visibility=View.GONE
        binding.editTextTabPantaprezol.visibility=View.GONE
        binding.editTextTabDimox.visibility=View.GONE
        binding.edittextEyeDropMoxifloxacin.visibility=View.GONE
        binding.edittextEyeDropMoxifloxacin6.visibility=View.GONE
        binding.edittextEyeDropMoxifloxacin4.visibility=View.GONE
        binding.edittextEyeDropMoxifloxacin2times.visibility=View.GONE
        binding.edittextEyeDropMoxifloxacin5w.visibility=View.GONE
        binding.edittextEyeDropMoxifloxacinP.visibility=View.GONE
        binding.edittextEyeDropHomide.visibility=View.GONE
        binding.edittextEyeDropTimololSos.visibility=View.GONE
        binding.editTextEyeDropHypersolSos.visibility=View.GONE
        binding.editTextLubricantDropRefresh.visibility=View.GONE
        binding.editTextWheatherEuipmentAddressed.visibility=View.GONE
        binding.editTextKeyConcerns.visibility=View.GONE
        binding.LinearLayoutHistoryAnticoagulants.visibility=View.GONE
        binding.EditTextDifficultAirwayAspirationRisk.visibility=View.GONE
        binding.LinearLayoutCriticalOrUnexpected.visibility=View.GONE

        binding.checkboxOtherTypesOfSurgery.setOnCheckedChangeListener(this)
        binding.checkboxSmallIncisionCataractSurgeryWithIOL.setOnCheckedChangeListener(this)
        binding.checkboxSmallIncisionCataractSurgeryWithoutIOL.setOnCheckedChangeListener(this)
        binding.checkboxExtracapsularCataractExtraction.setOnCheckedChangeListener(this)
        binding.checkboxIntracapsularCataractExcisionr.setOnCheckedChangeListener(this)
        binding.checkboxPterygium.setOnCheckedChangeListener(this)
        binding.checkboxSurgery.setOnCheckedChangeListener(this)
        binding.checkboxStyExcision.setOnCheckedChangeListener(this)
        binding.checkboxPtosisCorrection.setOnCheckedChangeListener(this)
        binding.checkboxAcIol.setOnCheckedChangeListener(this)
        binding.checkboxPcIol.setOnCheckedChangeListener(this)
        binding.checkboxIRIS.setOnCheckedChangeListener(this)
        binding.checkboxLieberman.setOnCheckedChangeListener(this)
        binding.checkboxCastroviejo.setOnCheckedChangeListener(this)
        binding.checkboxColibri.setOnCheckedChangeListener(this)
        binding.checkboxCapsulotomy.setOnCheckedChangeListener(this)
        binding.checkboxLimb.setOnCheckedChangeListener(this)
        binding.checkboxFormedIrrigatingCystotomes.setOnCheckedChangeListener(this)
        binding.checkboxHydrodissectiirs.setOnCheckedChangeListener(this)
        binding.checkboxMacPhersonForcep.setOnCheckedChangeListener(this)
        binding.checkboxViscoelasticCannula.setOnCheckedChangeListener(this)
        binding.checkboxUniversalStSidePort.setOnCheckedChangeListener(this)
        binding.checkboxIrrigation.setOnCheckedChangeListener(this)
        binding.checkboxSinsky.setOnCheckedChangeListener(this)
        binding.checkboxKeretome3.setOnCheckedChangeListener(this)
        binding.checkboxKeretome2.setOnCheckedChangeListener(this)
        binding.checkboxKnife.setOnCheckedChangeListener(this)
        binding.checkboxNucleus.setOnCheckedChangeListener(this)
        binding.checkboxPosterior.setOnCheckedChangeListener(this)
        binding.checkboxLightSensitivity.setOnCheckedChangeListener(this)
        binding.checkboxFluidCollection.setOnCheckedChangeListener(this)
        binding.checkboxMacularOdema.setOnCheckedChangeListener(this)
        binding.checkboxPosteriorCapsularOpacification.setOnCheckedChangeListener(this)
        binding.checkboxEndothelialDecompermation.setOnCheckedChangeListener(this)
        binding.checkBoxHyphema.setOnCheckedChangeListener(this)
        binding.checkboxRentinalTear.setOnCheckedChangeListener(this)
        binding.checkboxVitreousDechatments.setOnCheckedChangeListener(this)
        binding.checkboxDislocation.setOnCheckedChangeListener(this)
        binding.checkboxOcularHypertension.setOnCheckedChangeListener(this)
        binding.checkboxEndophthalmitis.setOnCheckedChangeListener(this)
        binding.checkboxVigamox.setOnCheckedChangeListener(this)
        binding.checkboxPrednisolone.setOnCheckedChangeListener(this)
        binding.checkboxCombinationOfInjGentamycin.setOnCheckedChangeListener(this)
        binding.checkboxVisco.setOnCheckedChangeListener(this)
        binding.checkboxIntasol500.setOnCheckedChangeListener(this)
        binding.checkboxInjMannitol.setOnCheckedChangeListener(this)
        binding.checkboxInjGentamycin.setOnCheckedChangeListener(this)
        binding.checkboxInjMoxifloxacin.setOnCheckedChangeListener(this)
        binding.checkboxInjAdrenaline.setOnCheckedChangeListener(this)
        binding.checkboxTabCifloxacin.setOnCheckedChangeListener(this)
        binding.checkboxTabDiclofenacSodium.setOnCheckedChangeListener(this)
        binding.checkboxTabPantaprezol.setOnCheckedChangeListener(this)
        binding.checkboxTabDimox.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin6.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin4.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin2times.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin5w.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacinP.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropHomide.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropTimololSos.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropHypersolSos.setOnCheckedChangeListener(this)
        binding.checkboxLubricantDropRefresh.setOnCheckedChangeListener(this)
        binding.checkboxWheatherEuipmentAddressed.setOnCheckedChangeListener(this)
        binding.checkboxKeyConcerns.setOnCheckedChangeListener(this)
        binding.CheckBoxContinued.setOnCheckedChangeListener(this)
        binding.CheckBoxStoppedAsInstructed.setOnCheckedChangeListener(this)
        binding.checkboxBeforeIncision.setOnCheckedChangeListener(this)
        binding.checkboxSNOrallyConfirmAntibiotic.setOnCheckedChangeListener(this)
        binding.checkboxSNOrallyConfirmImplants.setOnCheckedChangeListener(this)
        binding.checkboxSNOrallyConfirmDyes.setOnCheckedChangeListener(this)
        binding.checkboxSNOrallyConfirmGas.setOnCheckedChangeListener(this)
        binding.checkboxSNOrallyConfirmImplantsStyle.setOnCheckedChangeListener(this)
        binding.checkboxSNOrallyConfirmMitomycin.setOnCheckedChangeListener(this)
        binding.cardViewSumbitSurgicalNotes.setOnClickListener(this)

        binding.editTextNRDiastolic.addTextChangedListener(createTextWatcher(binding.editTextNRDiastolic))
        binding.EditTextRandomBloodSugar.addTextChangedListener(createTextWatcher(binding.EditTextRandomBloodSugar))

        binding.spinnerDifficultAirwayAspirationRisk.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = PatientHaveArrayList!![position]
                if (selectedItem == "Yes") {
                    binding.EditTextDifficultAirwayAspirationRisk.visibility = View.VISIBLE
                } else {
                    binding.EditTextDifficultAirwayAspirationRisk.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerHistoryAnticoagulants.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = PatientConfirmArrayList!![position]
                if (selectedItem == "Yes") {
                    binding.LinearLayoutHistoryAnticoagulants.visibility = View.VISIBLE
                } else {
                    binding.LinearLayoutHistoryAnticoagulants.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerCriticalOrUnexpectedSteps.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = SurgenReviewArrayList!![position]
                if (selectedItem == "Operative duration") {
                    binding.LinearLayoutCriticalOrUnexpected.visibility = View.VISIBLE
                } else {
                    binding.LinearLayoutCriticalOrUnexpected.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.edittextDateOfSurgery-> {
                showDatePickerDialog()
            }

            binding.textViewReadMore-> {
                val text=binding.textViewReadMore.text
                when(text) {
                    "Read More..."-> {
                        binding.textViewAnaesthesia!!.visibility=View.VISIBLE
                        binding.textViewReadMore.text="Read Less..."
                    }
                    "Read Less..."-> {
                        binding.textViewAnaesthesia!!.visibility=View.GONE
                        binding.textViewReadMore.text="Read More..."
                    }
                }
            }

            binding.carddviewUploadImplantLabelDetails-> {
                showPictureDialog()
            }

            binding.cardViewSumbitSurgicalNotes-> {
                Log.d(ConstantsApp.TAG,"Submit click")
                val (patientId, campId, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
                val current_Date= ConstantsApp.getCurrentDate()
                val camp_id=campId
                val createdDate=current_Date
                val patient_id=patientId
                val sn_airway=binding.spinnerDifficultAirwayAspirationRisk.selectedItem.toString()
                val sn_airway_detail=binding.EditTextDifficultAirwayAspirationRisk.text.toString()
                val sn_anaesthetist_concern=binding.edittextAnaesthetistReviews.text.toString()
                val sn_anticoagulant=binding.spinnerHistoryAnticoagulants.selectedItem.toString()
                val sn_anticoagulant_detail=sn_anticoagulant_detail
                val sn_before_or_instrument=binding.spinnerInstrumentSponge.selectedItem.toString()
                val sn_before_or_key_detail=binding.editTextKeyConcerns.text.toString()
                val sn_before_or_specimen=binding.spinnerSpecimenLabelled.selectedItem.toString()
                val sn_before_or_weather_detail=binding.editTextWheatherEuipmentAddressed.text.toString()
                val sn_cataract_capsulotomy_detail=binding.editTextCapsulotomy.text.toString()
                val sn_cataract_castroviejo_detail=binding.editTextCastroviejo.text.toString()
                val sn_cataract_colibri_detail=binding.editTextColibri.text.toString()
                val sn_cataract_formed_detail=binding.editTextFormedIrrigatingCystotomes.text.toString()
                val sn_cataract_hydrodissectiirs_detail=binding.editTextHydrodissectiirs.text.toString()
                val sn_cataract_irrigation_detail=binding.editTextIrrigation.text.toString()
                val sn_cataract_keretome_detail=binding.editTextKeretome3.text.toString()
                val sn_cataract_keretome_phaco_detail=binding.editTextKeretome2.text.toString()
                val sn_cataract_knife_detail=binding.editTextKnife.text.toString()
                val sn_cataract_lieberman_detail=binding.editTextLieberman.text.toString()
                val sn_cataract_limb_detail=binding.editTextLimb.text.toString()
                val sn_cataract_mac_detail=binding.editTextMacPhersonForcep.text.toString()
                val sn_cataract_nucleus_detail=binding.editTextNucleus.text.toString()
                val sn_cataract_sinsky_detail=binding.editTextSinsky.text.toString()
                val sn_cataract_universal_detail=binding.editTextUniversalStSidePort.text.toString()
                val sn_cataract_viscoelastic_detail=binding.editTextViscoelasticCannula.text.toString()
                val sn_common_dislocation_detail=binding.editTextDislocation.text.toString()
                val sn_common_endophthalmitis_detail=binding.edittextEndophthalmitis.text.toString()
                val sn_common_endothelial_detail=binding.editTextEndothelialDecompermation.text.toString()
                val sn_common_fluid_detail=binding.editTextFluidCollection.text.toString()
                val sn_common_hyphema_detail=binding.editTextHyphema.text.toString()
                val sn_common_light_detail=binding.editIextLightSensitivity.text.toString()
                val sn_common_macular_detail=binding.editTextMacularOdema.text.toString()
                val sn_common_ocular_detail=binding.edittextOcularHypertension.text.toString()
                val sn_common_posterior_opacification_detail=binding.editTextPosteriorCapsularOpacification.text.toString()
                val sn_common_posterior_rent_detail=binding.editTextPosterior.text.toString()
                val sn_common_retinal_detail=binding.editTextRentinalTear.text.toString()
                val sn_common_vitreous_detail=binding.editTextVitreousDechatments.text.toString()
                val sn_date_of_surgery=binding.edittextDateOfSurgery.text.toString()
                val sn_flomax=binding.spinnerHistoryFlomax.selectedItem.toString()
                val sn_has_confirmed_allergies=binding.spinnerAllergies.selectedItem.toString()
                val sn_has_confirmed_consent=binding.spinnerConsent.selectedItem.toString()
                val sn_has_confirmed_identity=binding.spinnerIdentity.selectedItem.toString()
                val sn_has_confirmed_procedure=binding.spinnerProcedure.selectedItem.toString()
                val sn_has_confirmed_site=binding.spinnerSiteMarked.selectedItem.toString()
                val sn_incision_cornea=binding.SpinnerCornea.selectedItem.toString()
                val sn_incision_sclera_1=binding.SpinnerSclera1.selectedItem.toString()
                val sn_incision_sclera_2=binding.SpinnerSclera2.selectedItem.toString()
                val sn_intra_adrenaline_detail=binding.edittextInjAdrenaline.text.toString()
                val sn_intra_combination_detail=binding.editTextCombinationOfInjGentamycin.text.toString()
                val sn_intra_gentamycin_detail=binding.editTextInjGentamycin.text.toString()
                val sn_intra_intasol_detail=binding.editTextIntasol500.text.toString()
                val sn_intra_mannitol_detail=binding.editTextInjMannitol.text.toString()
                val sn_intra_moxifloxacin_detail=binding.editTextInjMoxifloxacin.text.toString()
                val resultsn_intra_occular_lens=sn_intra_occular_lensArrayList.joinToString(",")
                Log.d(ConstantsApp.TAG,"resultsn_intra_occular_lens=>"+resultsn_intra_occular_lens)
                val sn_intra_occular_lens=resultsn_intra_occular_lens
                val sn_intra_prednisolone_detail=binding.editTextPrednisolone.text.toString()
                val sn_intra_vigamox_detail=binding.edittextVigamox.text.toString()
                val sn_intra_visco_detail=binding.editTextVisco.text.toString()
                val sn_local_anaesthesia=" "
                val sn_nurse_age=binding.editTextAge.text.toString()
                val sn_nurse_age_unit=" "
                val sn_nurse_anaesthesia=binding.editTextAnaesthesiaGiven.text.toString()
                val sn_nurse_anaesthetist=binding.editTextAnaesthetist.text.toString()
                val sn_nurse_bp_diastolic=binding.editTextNRDiastolic.text.toString()
                val sn_nurse_bp_interpretation=binding.TextViewNRBPInterpretation.text.toString()
                val sn_nurse_bp_sistolic=binding.editTextNRSystolic.text.toString()
                val sn_nurse_concern=binding.edittextConcerns.text.toString()
                val sn_nurse_diagnosis=binding.editTextDiagnosis.text.toString()
                val sn_nurse_duration=binding.editTextDurationSurgery.text.toString()
                val sn_nurse_equipment_issue=binding.edittextEquipmentIssues.text.toString()
                val sn_nurse_implant_detail=binding.editTextImplantDetails.text.toString()
                val sn_nurse_name=binding.editTextSurgeryName.text.toString()
                val resultString = selectedSurgenAndNurseOrallyConfirmList.joinToString(",")
                Log.d(ConstantsApp.TAG,"resultString=>"+resultString)
                val sn_nurse_orally_confirm=resultString
                val sn_nurse_rbs=binding.EditTextRandomBloodSugar.text.toString()
                val sn_nurse_rbs_interpretation=binding.TextViewNRRBSInterpretation.text.toString()
                val sn_nurse_registered=binding.editTextRegistered.text.toString()
                val sn_nurse_scrub=binding.editTextScrubNurse.text.toString()
                val sn_nurse_serial=binding.editTextSerialNo.text.toString()
                val sn_nurse_sex=binding.spinnerGender.selectedItem.toString()
                val sn_nurse_specimen_biopsy=binding.editTextBiopsySite.text.toString()
                val sn_nurse_specimen_detail=binding.editTextBiopsySiteDetails.text.toString()
                val sn_nurse_sterility=binding.edittextSterility.text.toString()
                val sn_nurse_surgeon=binding.editTextSurgeon.text.toString()
                val sn_nurse_surgery_name=binding.editTextSurgeryName.text.toString()
                val sn_nurse_viral_serology=binding.editTextViralSerology.text.toString()
                val sn_post_cifloxacin_detail=binding.edittextTabCifloxacin.text.toString()
                val sn_post_diclofenac_detail=binding.edittextTabDiclofenacSodium.text.toString()
                val sn_post_dimox_detail=binding.editTextTabDimox.text.toString()
                val sn_post_eye_1_detail=binding.edittextEyeDropMoxifloxacin.text.toString()
                val sn_post_eye_2_detail=binding.edittextEyeDropMoxifloxacin6.text.toString()
                val sn_post_eye_3_detail=binding.edittextEyeDropMoxifloxacin4.text.toString()
                val sn_post_eye_4_detail=binding.edittextEyeDropMoxifloxacin2times.text.toString()
                val sn_post_eye_5_detail=binding.edittextEyeDropMoxifloxacin5w.text.toString()
                val sn_post_eye_homide_detail=binding.edittextEyeDropHomide.text.toString()
                val sn_post_eye_hypersol_detail=binding.editTextEyeDropHypersolSos.text.toString()
                val sn_post_eye_lubricant_detail=binding.editTextLubricantDropRefresh.text.toString()
                val sn_post_eye_moxifloxacin_detail=binding.edittextEyeDropMoxifloxacinP.text.toString()
                val sn_post_eye_timolol_detail=binding.edittextEyeDropTimololSos.text.toString()
                val sn_post_pantaprezol_detail=binding.editTextTabPantaprezol.text.toString()
                val sn_site_marked_history=binding.spinnerHistoryPhysical.selectedItem.toString()
                val sn_site_marked_pre_anaesthesia=binding.spinnerPreAnaesthesiaAssessment.selectedItem.toString()
                val sn_site_marked_pre_surgical=binding.spinnerPreSurgicalAssessment.selectedItem.toString()
                val sn_type_of_surgery=sn_type_of_surgery_list.joinToString(",")
                Log.d(ConstantsApp.TAG,"sn_type_of_surgery=>"+sn_type_of_surgery)
                val sn_type_of_surgery_other=binding.editTextTypesOfSurgeryOther.text.toString()
                val sn_unexpected_step=binding.spinnerCriticalOrUnexpectedSteps.selectedItem.toString()
                val sn_unexpected_step_detail=binding.EditTextCriticalOrUnexpected.text.toString()

                SubmitSurgicalNotes(
                    camp_id.toString(),
                    createdDate,
                    patient_id.toString(),
                    sn_airway,
                    sn_airway_detail,
                    sn_anaesthetist_concern,
                    sn_anticoagulant,
                    sn_anticoagulant_detail!!,
                    sn_before_incision_all_team!!,
                    sn_before_or_instrument,
                    sn_before_or_key!!,
                    sn_before_or_key_detail,
                    sn_before_or_specimen,
                    sn_before_or_weather!!,
                    sn_before_or_weather_detail,
                    sn_cataract_capsulotomy!!,
                    sn_cataract_capsulotomy_detail,
                    sn_cataract_castroviejo!!,
                    sn_cataract_castroviejo_detail,
                    sn_cataract_colibri!!,
                    sn_cataract_colibri_detail,
                    sn_cataract_formed!!,
                    sn_cataract_formed_detail,
                    sn_cataract_hydrodissectiirs!!,
                    sn_cataract_hydrodissectiirs_detail,
                    sn_cataract_irrigation!!,
                    sn_cataract_irrigation_detail,
                    sn_cataract_keretome!!,
                    sn_cataract_keretome_detail,
                    sn_cataract_keretome_phaco!!,
                    sn_cataract_keretome_phaco_detail,
                    sn_cataract_knife!!,
                    sn_cataract_knife_detail,
                    sn_cataract_lieberman!!,
                    sn_cataract_lieberman_detail,
                    sn_cataract_limb!!,
                    sn_cataract_limb_detail,
                    sn_cataract_mac!!,
                    sn_cataract_mac_detail,
                    sn_cataract_nucleus!!,
                    sn_cataract_nucleus_detail,
                    sn_cataract_sinsky!!,
                    sn_cataract_sinsky_detail,
                    sn_cataract_universal!!,
                    sn_cataract_universal_detail,
                    sn_cataract_viscoelastic!!,
                    sn_cataract_viscoelastic_detail,
                    sn_common_dislocation!!,
                    sn_common_dislocation_detail,
                    sn_common_endophthalmitis!!,
                    sn_common_endophthalmitis_detail,
                    sn_common_endothelial!!,
                    sn_common_endothelial_detail.toString(),
                    sn_common_fluid!!,
                    sn_common_fluid_detail!!.toString(),
                    sn_common_hyphema!!,
                    sn_common_hyphema_detail.toString(),
                    sn_common_light!!,
                    sn_common_light_detail,
                    sn_common_macular!!,
                    sn_common_macular_detail,
                    sn_common_ocular!!,
                    sn_common_ocular_detail,
                    sn_common_posterior_opacification!!,
                    sn_common_posterior_opacification_detail,
                    sn_common_posterior_rent!!,
                    sn_common_posterior_rent_detail,
                    sn_common_retinal!!,
                    sn_common_retinal_detail,
                    sn_common_vitreous!!,
                    sn_common_vitreous_detail,
                    sn_date_of_surgery,
                    sn_flomax,
                    sn_has_confirmed_allergies,
                    sn_has_confirmed_consent,
                    sn_has_confirmed_identity,
                    sn_has_confirmed_procedure,
                    sn_has_confirmed_site,
                    sn_incision_cornea,
                    sn_incision_sclera_1,
                    sn_incision_sclera_2,
                    sn_intra_adrenaline!!,
                    sn_intra_adrenaline_detail,
                    sn_intra_combination!!,
                    sn_intra_combination_detail,
                    sn_intra_gentamycin!!,
                    sn_intra_gentamycin_detail,
                    sn_intra_intasol!!,
                    sn_intra_intasol_detail,
                    sn_intra_mannitol!!,
                    sn_intra_mannitol_detail,
                    sn_intra_moxifloxacin!!,
                    sn_intra_moxifloxacin_detail,
                    sn_intra_occular_lens,
                    sn_intra_prednisolone!!,
                    sn_intra_prednisolone_detail,
                    sn_intra_vigamox!!,
                    sn_intra_vigamox_detail,
                    sn_intra_visco!!,
                    sn_intra_visco_detail,
                    sn_local_anaesthesia,
                    sn_nurse_age,
                    sn_nurse_age_unit,
                    sn_nurse_anaesthesia,
                    sn_nurse_anaesthetist,
                    sn_nurse_bp_diastolic,
                    sn_nurse_bp_interpretation,
                    sn_nurse_bp_sistolic,
                    sn_nurse_concern,
                    sn_nurse_diagnosis,
                    sn_nurse_duration,
                    sn_nurse_equipment_issue,
                    sn_nurse_implant_detail,
                    sn_nurse_name,
                    sn_nurse_orally_confirm,
                    sn_nurse_rbs,
                    sn_nurse_rbs_interpretation,
                    sn_nurse_registered,
                    sn_nurse_scrub,
                    sn_nurse_serial,
                    sn_nurse_sex,
                    sn_nurse_specimen_biopsy,
                    sn_nurse_specimen_detail,
                    sn_nurse_sterility,
                    sn_nurse_surgeon,
                    sn_nurse_surgery_name,
                    sn_nurse_viral_serology,
                    sn_post_cifloxacin!!,
                    sn_post_cifloxacin_detail,
                    sn_post_diclofenac!!,
                    sn_post_diclofenac_detail,
                    sn_post_dimox!!,
                    sn_post_dimox_detail,
                    sn_post_eye_1!!,
                    sn_post_eye_1_detail,
                    sn_post_eye_2!!,
                    sn_post_eye_2_detail,
                    sn_post_eye_3!!,
                    sn_post_eye_3_detail,
                    sn_post_eye_4!!,
                    sn_post_eye_4_detail,
                    sn_post_eye_5!!,
                    sn_post_eye_5_detail,
                    sn_post_eye_homide!!,
                    sn_post_eye_homide_detail,
                    sn_post_eye_hypersol!!,
                    sn_post_eye_hypersol_detail,
                    sn_post_eye_lubricant!!,
                    sn_post_eye_lubricant_detail,
                    sn_post_eye_moxifloxacin!!,
                    sn_post_eye_moxifloxacin_detail,
                    sn_post_eye_timolol!!,
                    sn_post_eye_timolol_detail,
                    sn_post_pantaprezol!!,
                    sn_post_pantaprezol_detail,
                    sn_site_marked_history,
                    sn_site_marked_pre_anaesthesia,
                    sn_site_marked_pre_surgical,
                    sn_type_of_surgery,
                    sn_type_of_surgery_other,
                    sn_unexpected_step,
                    sn_unexpected_step_detail,
                    userId
                )
            }
        }
    }

    private fun showPictureDialog() {
        val pictureDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera"
        )
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> choosePhotoFromGallary1()
                    1 -> takePhotoFromCamera()
                }
            })
        pictureDialog.show()
    }

    private fun choosePhotoFromGallary1() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            val imageUri = data?.data
            filePath = RealPathUtil1.getRealPath(this, imageUri!!)
            Log.d(ConstantsApp.TAG,"filePath=>"+filePath)
            Log.d(ConstantsApp.TAG,"imageUri=>"+imageUri)
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "surgical_notes_image_$timestamp.jpg"
            val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!,fileName)

            UpdatedfilePath= updatedPath!!
            binding.ImageViewSurgicalNotes?.setImageURI(Uri.fromFile(File(updatedPath)))
            binding.TextViewSurgicalNotes.text = updatedPath
        }
        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            filePath = ConstantsApp.saveBitmapToFile(imageBitmap, this)
            Log.d(ConstantsApp.TAG,"filepath on camera click=>"+filePath)
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "surgical_notes_image_$timestamp.jpg"
            val tempFile = ConstantsApp.saveBitmapToFile1(imageBitmap, fileName, this)
            val imageUri = FileProvider.getUriForFile(
                this,
                "org.impactindiafoundation.iifllemeddocket.provider", // ✅ matches manifest
                tempFile
            )

            val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!,fileName)

            UpdatedfilePath= updatedPath!!
            binding.ImageViewSurgicalNotes?.setImageURI(Uri.fromFile(File(updatedPath)))
            binding.TextViewSurgicalNotes.text = updatedPath
        }
    }

    private fun SubmitSurgicalNotes(
        camp_id  :String,
        createdDate  :String,
        patient_id  :String,
        sn_airway  :String,
        sn_airway_detail  :String,
        sn_anaesthetist_concern  :String,
        sn_anticoagulant  :String,
        sn_anticoagulant_detail  :String,
        sn_before_incision_all_team  :String,
        sn_before_or_instrument  :String,
        sn_before_or_key  :String,
        sn_before_or_key_detail  :String,
        sn_before_or_specimen  :String,
        sn_before_or_weather  :String,
        sn_before_or_weather_detail  :String,
        sn_cataract_capsulotomy  :String,
        sn_cataract_capsulotomy_detail  :String,
        sn_cataract_castroviejo  :String,
        sn_cataract_castroviejo_detail  :String,
        sn_cataract_colibri  :String,
        sn_cataract_colibri_detail  :String,
        sn_cataract_formed  :String,
        sn_cataract_formed_detail  :String,
        sn_cataract_hydrodissectiirs  :String,
        sn_cataract_hydrodissectiirs_detail  :String,
        sn_cataract_irrigation  :String,
        sn_cataract_irrigation_detail  :String,
        sn_cataract_keretome  :String,
        sn_cataract_keretome_detail  :String,
        sn_cataract_keretome_phaco  :String,
        sn_cataract_keretome_phaco_detail  :String,
        sn_cataract_knife  :String,
        sn_cataract_knife_detail  :String,
        sn_cataract_lieberman  :String,
        sn_cataract_lieberman_detail  :String,
        sn_cataract_limb  :String,
        sn_cataract_limb_detail  :String,
        sn_cataract_mac  :String,
        sn_cataract_mac_detail  :String,
        sn_cataract_nucleus  :String,
        sn_cataract_nucleus_detail  :String,
        sn_cataract_sinsky  :String,
        sn_cataract_sinsky_detail  :String,
        sn_cataract_universal  :String,
        sn_cataract_universal_detail  :String,
        sn_cataract_viscoelastic  :String,
        sn_cataract_viscoelastic_detail  :String,
        sn_common_dislocation  :String,
        sn_common_dislocation_detail  :String,
        sn_common_endophthalmitis  :String,
        sn_common_endophthalmitis_detail  :String,
        sn_common_endothelial  :String,
        sn_common_endothelial_detail  :String,
        sn_common_fluid  :String,
        sn_common_fluid_detail  :String,
        sn_common_hyphema  :String,
        sn_common_hyphema_detail  :String,
        sn_common_light  :String,
        sn_common_light_detail  :String,
        sn_common_macular  :String,
        sn_common_macular_detail  :String,
        sn_common_ocular  :String,
        sn_common_ocular_detail  :String,
        sn_common_posterior_opacification  :String,
        sn_common_posterior_opacification_detail  :String,
        sn_common_posterior_rent  :String,
        sn_common_posterior_rent_detail  :String,
        sn_common_retinal  :String,
        sn_common_retinal_detail  :String,
        sn_common_vitreous  :String,
        sn_common_vitreous_detail  :String,
        sn_date_of_surgery  :String,
        sn_flomax  :String,
        sn_has_confirmed_allergies  :String,
        sn_has_confirmed_consent  :String,
        sn_has_confirmed_identity  :String,
        sn_has_confirmed_procedure  :String,
        sn_has_confirmed_site  :String,
        sn_incision_cornea  :String,
        sn_incision_sclera_1  :String,
        sn_incision_sclera_2  :String,
        sn_intra_adrenaline  :String,
        sn_intra_adrenaline_detail  :String,
        sn_intra_combination  :String,
        sn_intra_combination_detail  :String,
        sn_intra_gentamycin  :String,
        sn_intra_gentamycin_detail  :String,
        sn_intra_intasol  :String,
        sn_intra_intasol_detail  :String,
        sn_intra_mannitol  :String,
        sn_intra_mannitol_detail  :String,
        sn_intra_moxifloxacin  :String,
        sn_intra_moxifloxacin_detail  :String,
        sn_intra_occular_lens  :String,
        sn_intra_prednisolone  :String,
        sn_intra_prednisolone_detail  :String,
        sn_intra_vigamox  :String,
        sn_intra_vigamox_detail  :String,
        sn_intra_visco  :String,
        sn_intra_visco_detail  :String,
        sn_local_anaesthesia  :String,
        sn_nurse_age  :String,
        sn_nurse_age_unit  :String,
        sn_nurse_anaesthesia  :String,
        sn_nurse_anaesthetist  :String,
        sn_nurse_bp_diastolic  :String,
        sn_nurse_bp_interpretation  :String,
        sn_nurse_bp_sistolic  :String,
        sn_nurse_concern  :String,
        sn_nurse_diagnosis  :String,
        sn_nurse_duration  :String,
        sn_nurse_equipment_issue  :String,
        sn_nurse_implant_detail  :String,
        sn_nurse_name  :String,
        sn_nurse_orally_confirm  :String,
        sn_nurse_rbs  :String,
        sn_nurse_rbs_interpretation  :String,
        sn_nurse_registered  :String,
        sn_nurse_scrub  :String,
        sn_nurse_serial  :String,
        sn_nurse_sex  :String,
        sn_nurse_specimen_biopsy  :String,
        sn_nurse_specimen_detail  :String,
        sn_nurse_sterility  :String,
        sn_nurse_surgeon  :String,
        sn_nurse_surgery_name  :String,
        sn_nurse_viral_serology  :String,
        sn_post_cifloxacin  :String,
        sn_post_cifloxacin_detail  :String,
        sn_post_diclofenac  :String,
        sn_post_diclofenac_detail  :String,
        sn_post_dimox  :String,
        sn_post_dimox_detail  :String,
        sn_post_eye_1  :String,
        sn_post_eye_1_detail  :String,
        sn_post_eye_2  :String,
        sn_post_eye_2_detail  :String,
        sn_post_eye_3  :String,
        sn_post_eye_3_detail  :String,
        sn_post_eye_4  :String,
        sn_post_eye_4_detail  :String,
        sn_post_eye_5  :String,
        sn_post_eye_5_detail  :String,
        sn_post_eye_homide  :String,
        sn_post_eye_homide_detail  :String,
        sn_post_eye_hypersol  :String,
        sn_post_eye_hypersol_detail  :String,
        sn_post_eye_lubricant  :String,
        sn_post_eye_lubricant_detail  :String,
        sn_post_eye_moxifloxacin  :String,
        sn_post_eye_moxifloxacin_detail  :String,
        sn_post_eye_timolol  :String,
        sn_post_eye_timolol_detail  :String,
        sn_post_pantaprezol  :String,
        sn_post_pantaprezol_detail  :String,
        sn_site_marked_history  :String,
        sn_site_marked_pre_anaesthesia  :String,
        sn_site_marked_pre_surgical  :String,
        sn_type_of_surgery  :String,
        sn_type_of_surgery_other  :String,
        sn_unexpected_step  :String,
        sn_unexpected_step_detail  :String,
        userId: String?
    ) {
        if (UpdatedfilePath=="") {
            val surgicalNote= Cataract_Surgery_Notes(0,
                camp_id.toInt() ,
                createdDate ,
                patient_id.toInt() ,
                sn_airway ,
                sn_airway_detail ,
                sn_anaesthetist_concern ,
                sn_anticoagulant ,
                sn_anticoagulant_detail ,
                sn_before_incision_all_team ,
                sn_before_or_instrument ,
                sn_before_or_key ,
                sn_before_or_key_detail ,
                sn_before_or_specimen ,
                sn_before_or_weather ,
                sn_before_or_weather_detail ,
                sn_cataract_capsulotomy ,
                sn_cataract_capsulotomy_detail ,
                sn_cataract_castroviejo ,
                sn_cataract_castroviejo_detail ,
                sn_cataract_colibri ,
                sn_cataract_colibri_detail ,
                sn_cataract_formed ,
                sn_cataract_formed_detail ,
                sn_cataract_hydrodissectiirs ,
                sn_cataract_hydrodissectiirs_detail ,
                sn_cataract_irrigation ,
                sn_cataract_irrigation_detail ,
                sn_cataract_keretome ,
                sn_cataract_keretome_detail ,
                sn_cataract_keretome_phaco ,
                sn_cataract_keretome_phaco_detail ,
                sn_cataract_knife ,
                sn_cataract_knife_detail ,
                sn_cataract_lieberman ,
                sn_cataract_lieberman_detail ,
                sn_cataract_limb ,
                sn_cataract_limb_detail ,
                sn_cataract_mac ,
                sn_cataract_mac_detail ,
                sn_cataract_nucleus ,
                sn_cataract_nucleus_detail ,
                sn_cataract_sinsky ,
                sn_cataract_sinsky_detail ,
                sn_cataract_universal ,
                sn_cataract_universal_detail ,
                sn_cataract_viscoelastic ,
                sn_cataract_viscoelastic_detail ,
                sn_common_dislocation ,
                sn_common_dislocation_detail ,
                sn_common_endophthalmitis ,
                sn_common_endophthalmitis_detail ,
                sn_common_endothelial ,
                sn_common_endothelial_detail ,
                sn_common_fluid ,
                sn_common_fluid_detail ,
                sn_common_hyphema ,
                sn_common_hyphema_detail ,
                sn_common_light ,
                sn_common_light_detail ,
                sn_common_macular ,
                sn_common_macular_detail ,
                sn_common_ocular ,
                sn_common_ocular_detail ,
                sn_common_posterior_opacification ,
                sn_common_posterior_opacification_detail ,
                sn_common_posterior_rent ,
                sn_common_posterior_rent_detail ,
                sn_common_retinal ,
                sn_common_retinal_detail ,
                sn_common_vitreous ,
                sn_common_vitreous_detail ,
                sn_date_of_surgery ,
                sn_flomax ,
                sn_has_confirmed_allergies ,
                sn_has_confirmed_consent ,
                sn_has_confirmed_identity ,
                sn_has_confirmed_procedure ,
                sn_has_confirmed_site ,
                sn_incision_cornea ,
                sn_incision_sclera_1 ,
                sn_incision_sclera_2 ,
                sn_intra_adrenaline ,
                sn_intra_adrenaline_detail ,
                sn_intra_combination ,
                sn_intra_combination_detail ,
                sn_intra_gentamycin ,
                sn_intra_gentamycin_detail ,
                sn_intra_intasol ,
                sn_intra_intasol_detail ,
                sn_intra_mannitol ,
                sn_intra_mannitol_detail ,
                sn_intra_moxifloxacin ,
                sn_intra_moxifloxacin_detail ,
                sn_intra_occular_lens ,
                sn_intra_prednisolone ,
                sn_intra_prednisolone_detail ,
                sn_intra_vigamox ,
                sn_intra_vigamox_detail ,
                sn_intra_visco ,
                sn_intra_visco_detail ,
                sn_local_anaesthesia ,
                sn_nurse_age ,
                sn_nurse_age_unit ,
                sn_nurse_anaesthesia ,
                sn_nurse_anaesthetist ,
                sn_nurse_bp_diastolic ,
                sn_nurse_bp_interpretation ,
                sn_nurse_bp_sistolic ,
                sn_nurse_concern ,
                sn_nurse_diagnosis ,
                sn_nurse_duration ,
                sn_nurse_equipment_issue ,
                sn_nurse_implant_detail ,
                sn_nurse_name ,
                sn_nurse_orally_confirm ,
                sn_nurse_rbs ,
                sn_nurse_rbs_interpretation ,
                sn_nurse_registered ,
                sn_nurse_scrub ,
                sn_nurse_serial ,
                sn_nurse_sex ,
                sn_nurse_specimen_biopsy ,
                sn_nurse_specimen_detail ,
                sn_nurse_sterility ,
                sn_nurse_surgeon ,
                sn_nurse_surgery_name ,
                sn_nurse_viral_serology ,
                sn_post_cifloxacin ,
                sn_post_cifloxacin_detail ,
                sn_post_diclofenac ,
                sn_post_diclofenac_detail ,
                sn_post_dimox ,
                sn_post_dimox_detail ,
                sn_post_eye_1 ,
                sn_post_eye_1_detail ,
                sn_post_eye_2 ,
                sn_post_eye_2_detail ,
                sn_post_eye_3 ,
                sn_post_eye_3_detail ,
                sn_post_eye_4 ,
                sn_post_eye_4_detail ,
                sn_post_eye_5 ,
                sn_post_eye_5_detail ,
                sn_post_eye_homide ,
                sn_post_eye_homide_detail ,
                sn_post_eye_hypersol ,
                sn_post_eye_hypersol_detail ,
                sn_post_eye_lubricant ,
                sn_post_eye_lubricant_detail ,
                sn_post_eye_moxifloxacin ,
                sn_post_eye_moxifloxacin_detail ,
                sn_post_eye_timolol ,
                sn_post_eye_timolol_detail ,
                sn_post_pantaprezol ,
                sn_post_pantaprezol_detail ,
                sn_site_marked_history ,
                sn_site_marked_pre_anaesthesia ,
                sn_site_marked_pre_surgical ,
                sn_type_of_surgery ,
                sn_type_of_surgery_other ,
                sn_unexpected_step ,
                sn_unexpected_step_detail ,
                userId.toString(),
                ""
            )
            viewModel1.insert_Surgical_Notes1(surgicalNote)
            InsertSurgicalNotesLocalResponse()
        } else {
            val surgicalNote= Cataract_Surgery_Notes(0,
                camp_id.toInt() ,
                createdDate ,
                patient_id.toInt() ,
                sn_airway ,
                sn_airway_detail ,
                sn_anaesthetist_concern ,
                sn_anticoagulant ,
                sn_anticoagulant_detail ,
                sn_before_incision_all_team ,
                sn_before_or_instrument ,
                sn_before_or_key ,
                sn_before_or_key_detail ,
                sn_before_or_specimen ,
                sn_before_or_weather ,
                sn_before_or_weather_detail ,
                sn_cataract_capsulotomy ,
                sn_cataract_capsulotomy_detail ,
                sn_cataract_castroviejo ,
                sn_cataract_castroviejo_detail ,
                sn_cataract_colibri ,
                sn_cataract_colibri_detail ,
                sn_cataract_formed ,
                sn_cataract_formed_detail ,
                sn_cataract_hydrodissectiirs ,
                sn_cataract_hydrodissectiirs_detail ,
                sn_cataract_irrigation ,
                sn_cataract_irrigation_detail ,
                sn_cataract_keretome ,
                sn_cataract_keretome_detail ,
                sn_cataract_keretome_phaco ,
                sn_cataract_keretome_phaco_detail ,
                sn_cataract_knife ,
                sn_cataract_knife_detail ,
                sn_cataract_lieberman ,
                sn_cataract_lieberman_detail ,
                sn_cataract_limb ,
                sn_cataract_limb_detail ,
                sn_cataract_mac ,
                sn_cataract_mac_detail ,
                sn_cataract_nucleus ,
                sn_cataract_nucleus_detail ,
                sn_cataract_sinsky ,
                sn_cataract_sinsky_detail ,
                sn_cataract_universal ,
                sn_cataract_universal_detail ,
                sn_cataract_viscoelastic ,
                sn_cataract_viscoelastic_detail ,
                sn_common_dislocation ,
                sn_common_dislocation_detail ,
                sn_common_endophthalmitis ,
                sn_common_endophthalmitis_detail ,
                sn_common_endothelial ,
                sn_common_endothelial_detail ,
                sn_common_fluid ,
                sn_common_fluid_detail ,
                sn_common_hyphema ,
                sn_common_hyphema_detail ,
                sn_common_light ,
                sn_common_light_detail ,
                sn_common_macular ,
                sn_common_macular_detail ,
                sn_common_ocular ,
                sn_common_ocular_detail ,
                sn_common_posterior_opacification ,
                sn_common_posterior_opacification_detail ,
                sn_common_posterior_rent ,
                sn_common_posterior_rent_detail ,
                sn_common_retinal ,
                sn_common_retinal_detail ,
                sn_common_vitreous ,
                sn_common_vitreous_detail ,
                sn_date_of_surgery ,
                sn_flomax ,
                sn_has_confirmed_allergies ,
                sn_has_confirmed_consent ,
                sn_has_confirmed_identity ,
                sn_has_confirmed_procedure ,
                sn_has_confirmed_site ,
                sn_incision_cornea ,
                sn_incision_sclera_1 ,
                sn_incision_sclera_2 ,
                sn_intra_adrenaline ,
                sn_intra_adrenaline_detail ,
                sn_intra_combination ,
                sn_intra_combination_detail ,
                sn_intra_gentamycin ,
                sn_intra_gentamycin_detail ,
                sn_intra_intasol ,
                sn_intra_intasol_detail ,
                sn_intra_mannitol ,
                sn_intra_mannitol_detail ,
                sn_intra_moxifloxacin ,
                sn_intra_moxifloxacin_detail ,
                sn_intra_occular_lens ,
                sn_intra_prednisolone ,
                sn_intra_prednisolone_detail ,
                sn_intra_vigamox ,
                sn_intra_vigamox_detail ,
                sn_intra_visco ,
                sn_intra_visco_detail ,
                sn_local_anaesthesia ,
                sn_nurse_age ,
                sn_nurse_age_unit ,
                sn_nurse_anaesthesia ,
                sn_nurse_anaesthetist ,
                sn_nurse_bp_diastolic ,
                sn_nurse_bp_interpretation ,
                sn_nurse_bp_sistolic ,
                sn_nurse_concern ,
                sn_nurse_diagnosis ,
                sn_nurse_duration ,
                sn_nurse_equipment_issue ,
                sn_nurse_implant_detail ,
                sn_nurse_name ,
                sn_nurse_orally_confirm ,
                sn_nurse_rbs ,
                sn_nurse_rbs_interpretation ,
                sn_nurse_registered ,
                sn_nurse_scrub ,
                sn_nurse_serial ,
                sn_nurse_sex ,
                sn_nurse_specimen_biopsy ,
                sn_nurse_specimen_detail ,
                sn_nurse_sterility ,
                sn_nurse_surgeon ,
                sn_nurse_surgery_name ,
                sn_nurse_viral_serology ,
                sn_post_cifloxacin ,
                sn_post_cifloxacin_detail ,
                sn_post_diclofenac ,
                sn_post_diclofenac_detail ,
                sn_post_dimox ,
                sn_post_dimox_detail ,
                sn_post_eye_1 ,
                sn_post_eye_1_detail ,
                sn_post_eye_2 ,
                sn_post_eye_2_detail ,
                sn_post_eye_3 ,
                sn_post_eye_3_detail ,
                sn_post_eye_4 ,
                sn_post_eye_4_detail ,
                sn_post_eye_5 ,
                sn_post_eye_5_detail ,
                sn_post_eye_homide ,
                sn_post_eye_homide_detail ,
                sn_post_eye_hypersol ,
                sn_post_eye_hypersol_detail ,
                sn_post_eye_lubricant ,
                sn_post_eye_lubricant_detail ,
                sn_post_eye_moxifloxacin ,
                sn_post_eye_moxifloxacin_detail ,
                sn_post_eye_timolol ,
                sn_post_eye_timolol_detail ,
                sn_post_pantaprezol ,
                sn_post_pantaprezol_detail ,
                sn_site_marked_history ,
                sn_site_marked_pre_anaesthesia ,
                sn_site_marked_pre_surgical ,
                sn_type_of_surgery ,
                sn_type_of_surgery_other ,
                sn_unexpected_step ,
                sn_unexpected_step_detail ,
                userId.toString(),
                UpdatedfilePath
            )
            viewModel1.insert_Surgical_Notes1(surgicalNote)
            InsertSurgicalNotesLocalResponse()
        }
    }

    private fun InsertSurgicalNotesLocalResponse() {
        viewModel1.toastMessage.observe(this, Observer { message ->
                showToast(message)
            })
        viewModel1.insertResponse1.observe(this, Observer { response ->
            val _id = response?.first
            val camp_id = response?.second
            val patient_id = response?.third
            val userId = response?.fourth
            val filePath = response?.fifth

            if (_id != null && camp_id != null && patient_id != null && userId != null && filePath != null) {
                val fileName = ConstantsApp.getFileNameFromPath(filePath)
                val imageModel = ImageModel(0, _id, fileName, "222", patient_id, camp_id, userId, filePath)
                viewModel1.InsertImageLocal(imageModel)
                gotoScreen(this, PatientForms::class.java)
            } else {
                showToast("Some properties are null in the response")
                gotoScreen(this, PatientForms::class.java)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.edittextDateOfSurgery.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        val maxYear = calendar.get(Calendar.YEAR) + 5
        val endOfYearCalendar = Calendar.getInstance()
        endOfYearCalendar.set(maxYear, 11, 31, 23, 59, 59)
        datePickerDialog.datePicker.maxDate = endOfYearCalendar.timeInMillis
        datePickerDialog.show()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
       when(buttonView) {
           binding.checkboxOtherTypesOfSurgery-> {
               if (isChecked) {
                   binding.editTextTypesOfSurgeryOther.visibility = View.VISIBLE
                   val text=binding.checkboxOtherTypesOfSurgery.text.toString()
                   sn_type_of_surgery_list.add(text)
               } else {
                   binding.editTextTypesOfSurgeryOther.visibility = View.GONE
               }
           }

           binding.checkboxLieberman-> {
               if (isChecked) {
                   val text= binding.checkboxLieberman.text.toString()
                   sn_cataract_lieberman=text
                   binding.editTextLieberman.visibility = View.VISIBLE
               } else {
                   binding.editTextLieberman.visibility = View.GONE
                   sn_cataract_lieberman="null"
               }
           }

           binding.checkboxCastroviejo-> {
               if (isChecked) {
                   val text= binding.checkboxCastroviejo.text.toString()
                   sn_cataract_castroviejo=text
                   binding.editTextCastroviejo.visibility = View.VISIBLE
               } else {
                   binding.editTextCastroviejo.visibility = View.GONE
                   sn_cataract_castroviejo="null"
               }
           }

           binding.checkboxColibri-> {
               if (isChecked) {
                   val text= binding.checkboxColibri.text.toString()
                   sn_cataract_colibri=text
                   binding.editTextColibri.visibility = View.VISIBLE
               } else {
                   binding.editTextColibri.visibility = View.GONE
                   sn_cataract_colibri="null"
               }
           }

           binding.checkboxCapsulotomy-> {
               if (isChecked) {
                   binding.editTextCapsulotomy.visibility = View.VISIBLE
                   val text= binding.checkboxCapsulotomy.text.toString()
                   sn_cataract_capsulotomy=text
               } else {
                   binding.editTextCapsulotomy.visibility = View.GONE
                   sn_cataract_capsulotomy="null"
               }
           }

           binding.checkboxLimb-> {
               if (isChecked) {
                   binding.editTextLimb.visibility = View.VISIBLE
                   val text= binding.checkboxLimb.text.toString()
                   sn_cataract_limb=text
               } else {
                   binding.editTextLimb.visibility = View.GONE
                   sn_cataract_limb="null"
               }
           }

           binding.checkboxFormedIrrigatingCystotomes-> {
               if (isChecked) {
                   val text= binding.checkboxFormedIrrigatingCystotomes.text.toString()
                   sn_cataract_formed=text
                   binding.editTextFormedIrrigatingCystotomes.visibility = View.VISIBLE
               } else {
                   binding.editTextFormedIrrigatingCystotomes.visibility = View.GONE
                   sn_cataract_formed="null"
               }
           }

           binding.checkboxHydrodissectiirs-> {
               if (isChecked) {
                   binding.editTextHydrodissectiirs.visibility = View.VISIBLE
                   val text= binding.checkboxHydrodissectiirs.text.toString()
                   sn_cataract_hydrodissectiirs=text
               } else {
                   binding.editTextHydrodissectiirs.visibility = View.GONE
                   sn_cataract_hydrodissectiirs="null"
               }
           }

           binding.checkboxMacPhersonForcep-> {
               if (isChecked) {
                   binding.editTextMacPhersonForcep.visibility = View.VISIBLE
                   val text= binding.checkboxMacPhersonForcep.text.toString()
                   sn_cataract_mac=text
               } else {
                   binding.editTextMacPhersonForcep.visibility = View.GONE
                   sn_cataract_mac="null"
               }
           }

           binding.checkboxViscoelasticCannula-> {
               if (isChecked) {
                   binding.editTextViscoelasticCannula.visibility = View.VISIBLE
                   val text= binding.checkboxViscoelasticCannula.text.toString()
                   sn_cataract_viscoelastic=text
               } else {
                   binding.editTextViscoelasticCannula.visibility = View.GONE
               }
           }

           binding.checkboxUniversalStSidePort-> {
               if (isChecked) {
                   binding.editTextUniversalStSidePort.visibility = View.VISIBLE
                   val text= binding.checkboxUniversalStSidePort.text.toString()
                   sn_cataract_universal=text
               } else {
                   binding.editTextUniversalStSidePort.visibility = View.GONE
               }
           }

           binding.checkboxIrrigation-> {
               if (isChecked) {
                   binding.editTextIrrigation.visibility = View.VISIBLE
                   val text= binding.checkboxIrrigation.text.toString()
                   sn_cataract_irrigation=text
               } else {
                   binding.editTextIrrigation.visibility = View.GONE
               }
           }

           binding.checkboxSinsky-> {
               if (isChecked) {
                   binding.editTextSinsky.visibility = View.VISIBLE
                   val text= binding.checkboxSinsky.text.toString()
                   sn_cataract_sinsky=text
               } else {
                   binding.editTextSinsky.visibility = View.GONE
               }
           }

           binding.checkboxKeretome3-> {
               if (isChecked) {
                   binding.editTextKeretome3.visibility = View.VISIBLE
                   val text= binding.checkboxKeretome3.text.toString()
                   sn_cataract_keretome=text
               } else {
                   binding.editTextKeretome3.visibility = View.GONE
               }
           }

           binding.checkboxKeretome2-> {
               if (isChecked) {
                   binding.editTextKeretome2.visibility = View.VISIBLE
                   val text= binding.checkboxKeretome2.text.toString()
                   sn_cataract_keretome_phaco=text
               } else {
                   binding.editTextKeretome2.visibility = View.GONE
               }
           }

           binding.checkboxKnife-> {
               if (isChecked) {
                   binding.editTextKnife.visibility = View.VISIBLE
                   val text= binding.checkboxKnife.text.toString()
                   sn_cataract_knife=text
               } else {
                   binding.editTextKnife.visibility = View.GONE
               }
           }

           binding.checkboxNucleus-> {
               if (isChecked) {
                   binding.editTextNucleus.visibility = View.VISIBLE
                   val text= binding.checkboxNucleus.text.toString()
                   sn_cataract_nucleus=text
               } else {
                   binding.editTextNucleus.visibility = View.GONE
               }
           }

           binding.checkboxPosterior-> {
               if (isChecked) {
                   binding.editTextPosterior.visibility = View.VISIBLE
                   val text= binding.checkboxPosterior.text.toString()
                   sn_common_posterior_rent=text
               } else {
                   binding.editTextPosterior.visibility = View.GONE
               }
           }

           binding.checkboxLightSensitivity-> {
               if (isChecked) {
                   binding.editIextLightSensitivity.visibility = View.VISIBLE
                   val text= binding.checkboxLightSensitivity.text.toString()
                   sn_common_light=text
               } else {
                   binding.editIextLightSensitivity.visibility = View.GONE
               }
           }

           binding.checkboxFluidCollection-> {
               if (isChecked) {
                   binding.editTextFluidCollection.visibility = View.VISIBLE
                   val text= binding.checkboxFluidCollection.text.toString()
                   sn_common_fluid=text
               } else {
                   binding.editTextFluidCollection.visibility = View.GONE
               }
           }

           binding.checkboxMacularOdema-> {
               if (isChecked) {
                   binding.editTextMacularOdema.visibility = View.VISIBLE
                   val text= binding.checkboxMacularOdema.text.toString()
                   sn_common_macular=text
               } else {
                   binding.editTextMacularOdema.visibility = View.GONE
               }
           }

           binding.checkboxPosteriorCapsularOpacification-> {
               if (isChecked) {
                   binding.editTextPosteriorCapsularOpacification.visibility = View.VISIBLE
                   val text= binding.checkboxPosteriorCapsularOpacification.text.toString()
                   sn_common_posterior_opacification=text
               } else {
                   binding.editTextPosteriorCapsularOpacification.visibility = View.GONE
               }
           }

           binding.checkboxEndothelialDecompermation-> {
               if (isChecked) {
                   binding.editTextEndothelialDecompermation.visibility = View.VISIBLE
                   val text= binding.checkboxEndothelialDecompermation.text.toString()
                   sn_common_endothelial=text
               } else {
                   binding.editTextEndothelialDecompermation.visibility = View.GONE
               }
           }

           binding.checkBoxHyphema-> {
               if (isChecked) {
                   binding.editTextHyphema.visibility = View.VISIBLE
                   val text= binding.checkBoxHyphema.text.toString()
                   sn_common_hyphema=text
               } else {
                   binding.editTextHyphema.visibility = View.GONE
               }
           }

           binding.checkboxRentinalTear-> {
               if (isChecked) {
                   binding.editTextRentinalTear.visibility = View.VISIBLE
                   val text= binding.checkboxRentinalTear.text.toString()
                   sn_common_retinal=text
               } else {
                   binding.editTextRentinalTear.visibility = View.GONE
               }
           }

           binding.checkboxVitreousDechatments-> {
               if (isChecked) {
                   binding.editTextVitreousDechatments.visibility = View.VISIBLE
                   val text= binding.checkboxVitreousDechatments.text.toString()
                   sn_common_vitreous=text
               } else {
                   binding.editTextVitreousDechatments.visibility = View.GONE
               }
           }

           binding.checkboxDislocation-> {
               if (isChecked) {
                   binding.editTextDislocation.visibility = View.VISIBLE
                   val text= binding.checkboxDislocation.text.toString()
                   sn_common_dislocation=text
               } else {
                   binding.editTextDislocation.visibility = View.GONE
               }
           }

           binding.checkboxOcularHypertension-> {
               if (isChecked) {
                   binding.edittextOcularHypertension.visibility = View.VISIBLE
                   val text= binding.checkboxOcularHypertension.text.toString()
                   sn_common_ocular=text
               } else {
                   binding.edittextOcularHypertension.visibility = View.GONE
               }
           }

           binding.checkboxEndophthalmitis-> {
               if (isChecked) {
                   binding.edittextEndophthalmitis.visibility = View.VISIBLE
                   val text= binding.checkboxEndophthalmitis.text.toString()
                   sn_common_endophthalmitis=text
               } else {
                   binding.edittextEndophthalmitis.visibility = View.GONE
               }
           }

           binding.checkboxVigamox-> {
               if (isChecked) {
                   binding.edittextVigamox.visibility = View.VISIBLE
                   val text= binding.checkboxVigamox.text.toString()
                   sn_intra_vigamox=text
               } else {
                   binding.edittextVigamox.visibility = View.GONE
               }
           }

           binding.checkboxPrednisolone-> {
               if (isChecked) {
                   binding.editTextPrednisolone.visibility = View.VISIBLE
                   val text= binding.checkboxPrednisolone.text.toString()
                   sn_intra_prednisolone=text
               } else {
                   binding.editTextPrednisolone.visibility = View.GONE
               }
           }

           binding.checkboxCombinationOfInjGentamycin-> {
               if (isChecked) {
                   binding.editTextCombinationOfInjGentamycin.visibility = View.VISIBLE
                   val text= binding.checkboxCombinationOfInjGentamycin.text.toString()
                   sn_intra_combination=text
               } else {
                   binding.editTextCombinationOfInjGentamycin.visibility = View.GONE
               }
           }

           binding.checkboxVisco-> {
               if (isChecked) {
                   binding.editTextVisco.visibility = View.VISIBLE
                   val text= binding.checkboxVisco.text.toString()
                   sn_intra_visco=text
               } else {
                   binding.editTextVisco.visibility = View.GONE
               }
           }

           binding.checkboxIntasol500-> {
               if (isChecked) {
                   binding.editTextIntasol500.visibility = View.VISIBLE
                   val text= binding.checkboxIntasol500.text.toString()
                   sn_intra_intasol=text
               } else {
                   binding.editTextIntasol500.visibility = View.GONE
               }
           }

           binding.checkboxInjMannitol-> {
               if (isChecked) {
                   binding.editTextInjMannitol.visibility = View.VISIBLE
                   val text= binding.checkboxInjMannitol.text.toString()
                   sn_intra_mannitol=text
               } else {
                   binding.editTextInjMannitol.visibility = View.GONE
               }
           }

           binding.checkboxInjGentamycin-> {
               if (isChecked) {
                   binding.editTextInjGentamycin.visibility = View.VISIBLE
                   val text= binding.checkboxInjGentamycin.text.toString()
                   sn_intra_gentamycin=text
               } else {
                   binding.editTextInjGentamycin.visibility = View.GONE
               }
           }

           binding.checkboxInjMoxifloxacin-> {
               if (isChecked) {
                   binding.editTextInjMoxifloxacin.visibility = View.VISIBLE
                   val text= binding.checkboxInjMoxifloxacin.text.toString()
                   sn_intra_moxifloxacin=text
               } else {
                   binding.editTextInjMoxifloxacin.visibility = View.GONE
               }
           }

           binding.checkboxInjAdrenaline-> {
               if (isChecked) {
                   binding.edittextInjAdrenaline.visibility = View.VISIBLE
                   val text= binding.checkboxInjAdrenaline.text.toString()
                   sn_intra_adrenaline=text
               } else {
                   binding.edittextInjAdrenaline.visibility = View.GONE
               }
           }

           binding.checkboxTabCifloxacin-> {
               if (isChecked) {
                   sn_post_cifloxacin=binding.checkboxTabCifloxacin.text.toString()
                   binding.edittextTabCifloxacin.visibility = View.VISIBLE
               } else {
                   binding.edittextTabCifloxacin.visibility = View.GONE
               }
           }

           binding.checkboxTabDiclofenacSodium-> {
               if (isChecked) {
                   binding.edittextTabDiclofenacSodium.visibility = View.VISIBLE
                   val text= binding.checkboxTabDiclofenacSodium.text.toString()
                   sn_post_diclofenac=text
               } else {
                   binding.edittextTabDiclofenacSodium.visibility = View.GONE
               }
           }

           binding.checkboxTabPantaprezol-> {
               if (isChecked) {
                   binding.editTextTabPantaprezol.visibility = View.VISIBLE
                   val text= binding.checkboxTabPantaprezol.text.toString()
                   sn_post_pantaprezol=text
               } else {
                   binding.editTextTabPantaprezol.visibility = View.GONE
               }
           }

           binding.checkboxTabDimox-> {
               if (isChecked) {
                   binding.editTextTabDimox.visibility = View.VISIBLE
                   val text= binding.checkboxTabDimox.text.toString()
                   sn_post_dimox=text
               } else {
                   binding.editTextTabDimox.visibility = View.GONE
               }
           }

           binding.checkboxEyeDropMoxifloxacin-> {
               if (isChecked) {
                   binding.edittextEyeDropMoxifloxacin.visibility = View.VISIBLE
                   val text= binding.checkboxEyeDropMoxifloxacin.text.toString()
                   sn_post_eye_1=text
               } else {
                   binding.edittextEyeDropMoxifloxacin.visibility = View.GONE
               }
           }

           binding.checkboxEyeDropMoxifloxacin6-> {
               if (isChecked) {
                   binding.edittextEyeDropMoxifloxacin6.visibility = View.VISIBLE
                   val text= binding.checkboxEyeDropMoxifloxacin6.text.toString()
                   sn_post_eye_2=text
               } else {
                   binding.edittextEyeDropMoxifloxacin6.visibility = View.GONE
               }
           }

           binding.checkboxEyeDropMoxifloxacin4-> {
               if (isChecked) {
                   binding.edittextEyeDropMoxifloxacin4.visibility = View.VISIBLE
                   val text= binding.checkboxEyeDropMoxifloxacin4.text.toString()
                   sn_post_eye_3=text
               } else {
                   binding.edittextEyeDropMoxifloxacin4.visibility = View.GONE
               }
           }

           binding.checkboxEyeDropMoxifloxacin2times-> {
               if (isChecked) {
                   binding.edittextEyeDropMoxifloxacin2times.visibility = View.VISIBLE
                   val text= binding.checkboxEyeDropMoxifloxacin2times.text.toString()
                   sn_post_eye_4=text
               } else {
                   binding.edittextEyeDropMoxifloxacin2times.visibility = View.GONE
               }
           }

           binding.checkboxEyeDropMoxifloxacin5w-> {
               if (isChecked) {
                   binding.edittextEyeDropMoxifloxacin5w.visibility = View.VISIBLE
                   val text= binding.checkboxEyeDropMoxifloxacin5w.text.toString()
                   sn_post_eye_5=text
               } else {
                   binding.edittextEyeDropMoxifloxacin5w.visibility = View.GONE
               }
           }

           binding.checkboxEyeDropMoxifloxacinP-> {
               if (isChecked) {
                   binding.edittextEyeDropMoxifloxacinP.visibility = View.VISIBLE
                   val text= binding.checkboxEyeDropMoxifloxacinP.text.toString()
                   sn_post_eye_moxifloxacin=text
               } else {
                   binding.edittextEyeDropMoxifloxacinP.visibility = View.GONE
               }
           }

           binding.checkboxEyeDropHomide-> {
               if (isChecked) {
                   binding.edittextEyeDropHomide.visibility = View.VISIBLE
                   val text= binding.checkboxEyeDropHomide.text.toString()
                   sn_post_eye_homide=text
               } else {
                   binding.edittextEyeDropHomide.visibility = View.GONE
               }
           }

           binding.checkboxEyeDropTimololSos-> {
               if (isChecked) {
                   binding.edittextEyeDropTimololSos.visibility = View.VISIBLE
                   val text= binding.checkboxEyeDropTimololSos.text.toString()
                   sn_post_eye_timolol=text
               } else {
                   binding.edittextEyeDropTimololSos.visibility = View.GONE
               }
           }

           binding.checkboxEyeDropHypersolSos-> {
               if (isChecked) {
                   binding.editTextEyeDropHypersolSos.visibility = View.VISIBLE
                   val text= binding.checkboxEyeDropHypersolSos.text.toString()
                   sn_post_eye_hypersol=text
               } else {
                   binding.editTextEyeDropHypersolSos.visibility = View.GONE
               }
           }

           binding.checkboxLubricantDropRefresh-> {
               if (isChecked) {
                   binding.editTextLubricantDropRefresh.visibility = View.VISIBLE
                   val text= binding.checkboxLubricantDropRefresh.text.toString()
                   sn_post_eye_lubricant=text
               } else {
                   binding.editTextLubricantDropRefresh.visibility = View.GONE
               }
           }

           binding.checkboxWheatherEuipmentAddressed-> {
               if (isChecked) {
                   binding.editTextWheatherEuipmentAddressed.visibility = View.VISIBLE
                   val text= binding.checkboxWheatherEuipmentAddressed.text.toString()
                   sn_before_or_weather=text
               } else {
                   binding.editTextWheatherEuipmentAddressed.visibility = View.GONE
               }
           }

           binding.checkboxKeyConcerns-> {
               if (isChecked) {
                   binding.editTextKeyConcerns.visibility = View.VISIBLE
                   val text= binding.checkboxKeyConcerns.text.toString()
                   sn_before_or_key=text
               } else {
                   binding.editTextKeyConcerns.visibility = View.GONE
               }
           }

           binding.CheckBoxContinued-> {
               if (isChecked) {
                   updateSelectedOptions()
               }
           }

           binding.CheckBoxStoppedAsInstructed-> {
               if (isChecked) {
                   updateSelectedOptions()
               }
           }

           binding.checkboxBeforeIncision-> {
               if (isChecked) {
                   sn_before_incision_all_team=binding.checkboxBeforeIncision.text.toString()
               } else {
                   sn_before_incision_all_team=""
               }
           }

           binding.checkboxSNOrallyConfirmAntibiotic-> {
               if (isChecked) {
                   selectedSurgenAndNurseOrallyConfirmList.add(binding.checkboxSNOrallyConfirmAntibiotic.text.toString())
               }
           }

           binding.checkboxSNOrallyConfirmImplants-> {
               if (isChecked) {
                   selectedSurgenAndNurseOrallyConfirmList.add(binding.checkboxSNOrallyConfirmImplants.text.toString())
               }
           }

           binding.checkboxSNOrallyConfirmDyes-> {
               if (isChecked) {
                   selectedSurgenAndNurseOrallyConfirmList.add(binding.checkboxSNOrallyConfirmDyes.text.toString())
               }
           }

           binding.checkboxSNOrallyConfirmGas-> {
               if (isChecked) {
                   selectedSurgenAndNurseOrallyConfirmList.add(binding.checkboxSNOrallyConfirmGas.text.toString())
               }
           }

           binding.checkboxSNOrallyConfirmImplantsStyle-> {
               if (isChecked) {
                   selectedSurgenAndNurseOrallyConfirmList.add(binding.checkboxSNOrallyConfirmImplantsStyle.text.toString())
               }
           }

           binding.checkboxSNOrallyConfirmMitomycin-> {
               if (isChecked) {
                   selectedSurgenAndNurseOrallyConfirmList.add(binding.checkboxSNOrallyConfirmMitomycin.text.toString())
               }
           }

           binding.checkboxAcIol-> {
               if(isChecked) {
                   val text=binding.checkboxAcIol.text.toString()
                   Log.d(ConstantsApp.TAG,"checkboxAcIol=>"+text)
                   sn_intra_occular_lensArrayList.add(text)
               }
           }

           binding.checkboxPcIol-> {
               if(isChecked) {
                   val text=binding.checkboxPcIol.text.toString()
                   sn_intra_occular_lensArrayList.add(text)
               }
           }

           binding.checkboxIRIS-> {
               if(isChecked) {
                   val text=binding.checkboxIRIS.text.toString()
                   sn_intra_occular_lensArrayList.add(text)
               }
           }

           binding.checkboxSmallIncisionCataractSurgeryWithIOL-> {
               if (isChecked) {
                   val text=binding.checkboxSmallIncisionCataractSurgeryWithIOL.text.toString()
                   sn_type_of_surgery_list.add(text)
               }
           }

           binding.checkboxSmallIncisionCataractSurgeryWithoutIOL-> {
               if (isChecked) {
                   val text=binding.checkboxSmallIncisionCataractSurgeryWithoutIOL.text.toString()
                   sn_type_of_surgery_list.add(text)
               } else{
                   sn_type_of_surgery_list.add("null")
               }
           }

           binding.checkboxExtracapsularCataractExtraction-> {
               if (isChecked) {
                   val text=binding.checkboxExtracapsularCataractExtraction.text.toString()
                   sn_type_of_surgery_list.add(text)
               }
           }

           binding.checkboxIntracapsularCataractExcisionr-> {
               if (isChecked) {
                   val text=binding.checkboxIntracapsularCataractExcisionr.text.toString()
                   sn_type_of_surgery_list.add(text)
               }
           }

           binding.checkboxPterygium-> {
               if (isChecked) {
                   val text=binding.checkboxPterygium.text.toString()
                   sn_type_of_surgery_list.add(text)
               }
           }

           binding.checkboxStyExcision-> {
               if (isChecked) {
                   val text=binding.checkboxStyExcision.text.toString()
                   sn_type_of_surgery_list.add(text)
               }
           }

           binding.checkboxSurgery-> {
               if (isChecked) {
                   val text=binding.checkboxSurgery.text.toString()
                   sn_type_of_surgery_list.add(text)
               }
           }

           binding.checkboxPtosisCorrection-> {
               if (isChecked) {
                   val text=binding.checkboxPtosisCorrection.text.toString()
                   sn_type_of_surgery_list.add(text)
               }
           }
       }
    }

    private fun updateSelectedOptions() {
        sn_anticoagulant_detail = when {
            binding.CheckBoxContinued.isChecked && binding.CheckBoxStoppedAsInstructed.isChecked ->
                "Continued,Stopped as instructed"
            binding.CheckBoxContinued.isChecked -> "Continued"
            binding.CheckBoxStoppedAsInstructed.isChecked -> "Stopped as Instructed"
            else -> ""
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        gotoScreen(this,PatientForms::class.java)
    }

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }

    private fun createTextWatcher(editText: EditText): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val text = s.toString()
                when (editText) {
                    binding.editTextNRDiastolic -> {
                        setBloodPressure()
                    }
                    binding.EditTextRandomBloodSugar -> {
                        interpretBloodGlucose(s.toString())
                    }
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                when (editText) {
                    binding.editTextNRDiastolic -> {
                        setBloodPressure()
                    }
                    binding.EditTextRandomBloodSugar -> {
                        interpretBloodGlucose(s.toString())

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                when (editText) {
                    binding.editTextNRDiastolic -> {
                        setBloodPressure()
                    }
                    binding.EditTextRandomBloodSugar -> {
                        interpretBloodGlucose(s.toString())

                    }
                }
            }
        }
    }

    private fun setBloodPressure() {
        val systolic = binding.editTextNRSystolic.text.toString().toIntOrNull() ?: 0
        val diastolic = binding.editTextNRDiastolic.text.toString().toIntOrNull() ?: 0
        val bloodPressureInfo = getBloodPressureType(systolic, diastolic)
        binding.TextViewNRBPInterpretation.text = "${bloodPressureInfo.first}"
        binding.TextViewNRBPInterpretation.setTextColor(ContextCompat.getColor(this, bloodPressureInfo.second))
    }

    private fun getBloodPressureType(systolic: Int, diastolic: Int): Pair<String, Int> {
        return when {
            systolic == 0 || diastolic == 0 -> Pair("", android.R.color.black) // Set a default color for zero values
            systolic < 90 || (diastolic in 0..60) -> Pair("Hypotension", R.color.blue)
            systolic < 120 && diastolic < 80 -> Pair("Normal", R.color.black)
            systolic in 120..139 || diastolic in 80..89 -> Pair("Prehypertension", R.color.red)
            systolic in 140..159 || diastolic in 90..99 -> Pair("Stage 1 Hypertension", R.color.red)
            systolic >= 160 || diastolic >= 100 -> Pair("Stage 2 Hypertension", R.color.red)
            else -> Pair("Unknown", android.R.color.black) // Set a default color for unknown
        }
    }

    private fun interpretBloodGlucose(input: String) {
        val glucoseLevel = input.toDoubleOrNull()
        glucoseLevel?.let {
            val interpretation = when {
                it < 79 -> "Below Normal"
                it in 79.0..160.0 -> "Normal"
                it in 160.0..200.0 -> "Pre-Diabetes"
                else -> "Diabetes"
            }
            binding.TextViewNRRBSInterpretation.setText(interpretation)
            setColorBasedOnInterpretation(interpretation)
        } ?: run {
            binding.TextViewNRRBSInterpretation.text = ""
        }
    }

    private fun setColorBasedOnInterpretation(interpretation: String) {
        val color = when (interpretation) {
            "Below Normal" -> Color.BLUE
            "Normal" -> Color.BLACK
            "Pre-Diabetes" -> Color.RED
            "Diabetes" -> Color.RED
            else -> Color.RED
        }

        binding.TextViewNRRBSInterpretation.setTextColor(color)
    }
}