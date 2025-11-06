package org.impactindiafoundation.iifllemeddocket.Activity

import android.Manifest
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
import android.provider.Settings
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import org.impactindiafoundation.iifllemeddocket.Adapter.Adapter_Eye_Pre_Op_Notes_History
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImageModel
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp.Companion.saveBitmapToFile
import org.impactindiafoundation.iifllemeddocket.Utils.EmptyPermissionListener
import org.impactindiafoundation.iifllemeddocket.Utils.RealPathUtil1
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityEyePreOpNotesBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class EyePreOpNotesActivity:AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    lateinit var binding:ActivityEyePreOpNotesBinding
    lateinit var customDropDownAdapter: CustomDropDownAdapter
    var UpdatedfilePath:String=""
    var UnitArrayList:ArrayList<String>?=null
    var InvestigationArrayList:ArrayList<String>?=null
    var InvestigationArrayList1:ArrayList<String>?=null
    var DiscussedWithArrayList:ArrayList<String>?=null
    var AllergyTestArrayList:ArrayList<String>?=null
    var AllergyTestResultArrayList:ArrayList<String>?=null
    var EyeDropsArrayList:ArrayList<String>?=null
    var HistoryOfArrayList:ArrayList<String>?=null
    var HistoryOfArrayList1:ArrayList<String>?=null
    var IOLPowerArrayList:ArrayList<String>?=null
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    var eye_pre_op_recommendation=""
    var eye_pre_op_recommendation_detail=""
    var eye_pre_op_identify_eye:String=""
    var eye_pre_op_wash_face=""
    var eye_pre_op_nil_mouth=""
    var eye_pre_op_head_bath=""
    var eye_pre_op_antihyp=""
    var eye_pre_op_antihyp_detail=""
    var eye_pre_op_heart=""
    var eye_pre_op_heart_detail=""
    var eye_pre_op_dia=""
    var eye_pre_op_dia_detail=""
    var eye_pre_op_other=""
    var eye_pre_op_other_detail=""
    var eye_pre_op_diamox=""
    var eye_pre_op_alprax=""
    var eye_pre_op_ciplox=""
    var eye_pre_op_tropical_drop=""
    var eye_pre_op_plain_tropical=""
    var eye_pre_op_ciplox_drop=""
    var eye_pre_op_flur_eye=""
    var eye_pre_op_amlodipine=""
    var eye_pre_op_notes=""
    var eye_pre_op_antibiotic=""
    var eye_pre_op_xylocaine=""
    private val GALLERY:Int = 1
    private  var CAMERA:Int = 2
    var filePath:String?=null
    var eye_pre_op_bs_f=""
    var eye_pre_op_haemoglobin=""
    var eye_pre_op_bs_pp=""
    var eye_pre_op_pt=""
    var eye_pre_op_cbc=""
    var eye_pre_op_bt=""
    var eye_pre_op_hiv=""
    var eye_pre_op_ct=""
    var eye_pre_op_hbsag=""
    var eye_pre_op_hcv=""
    var eye_pre_op_ecg=""
    var eye_pre_op_xylocaine_detail=""
    var eye_pre_op_xylocaine_result=""
    var eye_pre_op_tropicamide=""
    var eye_pre_op_betadine=""
    var eye_pre_op_iol_power=""
    var eye_pre_op_temp_unit=""
    var eye_pre_op_antibiotic_result=""
    var eye_pre_op_antibiotic_detail=""
    var eye_pre_op_discussed_with=""
    private var isLineUpSurgeryVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEyePreOpNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Choose whichever bottom inset is larger (IME or system bars)
            val bottom = maxOf(systemBarsInsets.bottom, imeInsets.bottom)

            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                bottom
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
        binding.toolbarEyePreOpNotes.toolbar.title="Eye Pre-Op Notes"

        initView()
        getPermission()
        UnitArrayList=ArrayList()
        UnitArrayList!!.add("Select Unit")
        UnitArrayList!!.add(0x00B0.toChar()+"C")
        UnitArrayList!!.add(0x00B0.toChar()+"F")
        HistoryOfArrayList1=ArrayList()

        IOLPowerArrayList= ArrayList()
        IOLPowerArrayList!!.add("Select")
        IOLPowerArrayList!!.add("+0.00")
        IOLPowerArrayList!!.add("+0.50")
        IOLPowerArrayList!!.add("+1.00")
        IOLPowerArrayList!!.add("+1.50")
        IOLPowerArrayList!!.add("+2.00")
        IOLPowerArrayList!!.add("+2.50")
        IOLPowerArrayList!!.add("+3.00")
        IOLPowerArrayList!!.add("+3.50")
        IOLPowerArrayList!!.add("+4.00")
        IOLPowerArrayList!!.add("+4.50")
        IOLPowerArrayList!!.add("+5.00")
        IOLPowerArrayList!!.add("+5.50")
        IOLPowerArrayList!!.add("+6.00")
        IOLPowerArrayList!!.add("+6.50")
        IOLPowerArrayList!!.add("+7.00")
        IOLPowerArrayList!!.add("+7.50")
        IOLPowerArrayList!!.add("+8.00")
        IOLPowerArrayList!!.add("+8.50")
        IOLPowerArrayList!!.add("+9.00")
        IOLPowerArrayList!!.add("+9.50")
        IOLPowerArrayList!!.add("+10.00")
        IOLPowerArrayList!!.add("+10.50")
        IOLPowerArrayList!!.add("+11.00")
        IOLPowerArrayList!!.add("+11.50")
        IOLPowerArrayList!!.add("+12.00")
        IOLPowerArrayList!!.add("+12.50")
        IOLPowerArrayList!!.add("+13.00")
        IOLPowerArrayList!!.add("+13.50")
        IOLPowerArrayList!!.add("+14.00")
        IOLPowerArrayList!!.add("+14.50")
        IOLPowerArrayList!!.add("+15.00")
        IOLPowerArrayList!!.add("+15.50")
        IOLPowerArrayList!!.add("+16.00")
        IOLPowerArrayList!!.add("+16.50")
        IOLPowerArrayList!!.add("+17.00")
        IOLPowerArrayList!!.add("+17.50")
        IOLPowerArrayList!!.add("+18.00")
        IOLPowerArrayList!!.add("+18.50")
        IOLPowerArrayList!!.add("+19.00")
        IOLPowerArrayList!!.add("+19.50")
        IOLPowerArrayList!!.add("+20.00")
        IOLPowerArrayList!!.add("+20.50")
        IOLPowerArrayList!!.add("+21.00")
        IOLPowerArrayList!!.add("+21.50")
        IOLPowerArrayList!!.add("+22.00")
        IOLPowerArrayList!!.add("+22.50")
        IOLPowerArrayList!!.add("+23.00")
        IOLPowerArrayList!!.add("+23.50")
        IOLPowerArrayList!!.add("+24.00")
        IOLPowerArrayList!!.add("+24.50")
        IOLPowerArrayList!!.add("+25.00")
        IOLPowerArrayList!!.add("+25.50")
        IOLPowerArrayList!!.add("+26.00")
        IOLPowerArrayList!!.add("+26.50")
        IOLPowerArrayList!!.add("+27.00")
        IOLPowerArrayList!!.add("+27.50")
        IOLPowerArrayList!!.add("+28.00")
        IOLPowerArrayList!!.add("+28.50")
        IOLPowerArrayList!!.add("+29.00")
        IOLPowerArrayList!!.add("+29.50")
        IOLPowerArrayList!!.add("+30.00")
        IOLPowerArrayList!!.add("+30.50")
        IOLPowerArrayList!!.add("+31.00")
        IOLPowerArrayList!!.add("+31.50")
        IOLPowerArrayList!!.add("+32.00")
        IOLPowerArrayList!!.add("+32.50")
        IOLPowerArrayList!!.add("+33.00")
        IOLPowerArrayList!!.add("+33.50")
        IOLPowerArrayList!!.add("+34.00")
        IOLPowerArrayList!!.add("+34.50")
        IOLPowerArrayList!!.add("+35.00")

        InvestigationArrayList= ArrayList()
        InvestigationArrayList!!.add("Select")
        InvestigationArrayList!!.add("Normal")
        InvestigationArrayList!!.add("Abnormal")
        InvestigationArrayList!!.add("Not done")

        InvestigationArrayList1= ArrayList()
        InvestigationArrayList1!!.add("Select")
        InvestigationArrayList1!!.add("Negative")
        InvestigationArrayList1!!.add("Positive")
        InvestigationArrayList1!!.add("Not done")

        DiscussedWithArrayList= ArrayList()
        DiscussedWithArrayList!!.add("Select")
        DiscussedWithArrayList!!.add("Matron")
        DiscussedWithArrayList!!.add("Surgeon")
        DiscussedWithArrayList!!.add("Matron and Surgeon")

        AllergyTestArrayList= ArrayList()
        AllergyTestArrayList!!.add("Select")
        AllergyTestArrayList!!.add("Redness (Erythema)")
        AllergyTestArrayList!!.add("Raised bump (Papule)")
        AllergyTestArrayList!!.add("Itching")

        AllergyTestResultArrayList= ArrayList()
        AllergyTestResultArrayList!!.add("Select")
        AllergyTestResultArrayList!!.add("Negative")
        AllergyTestResultArrayList!!.add("Positive")

        EyeDropsArrayList= ArrayList()
        EyeDropsArrayList!!.add("Select")
        EyeDropsArrayList!!.add("Instilled")
        EyeDropsArrayList!!.add("Not Instilled")

        HistoryOfArrayList= ArrayList()
        HistoryOfArrayList!!.add("Select")
        HistoryOfArrayList!!.add("Trauma to the eye")
        HistoryOfArrayList!!.add("Skin infection")
        HistoryOfArrayList!!.add("Major illness")
        HistoryOfArrayList!!.add("Bleeding disorder")
        HistoryOfArrayList!!.add("Anticoagulant therapy")

        customDropDownAdapter= CustomDropDownAdapter(this,HistoryOfArrayList!!)
        binding.SpinnerHistoryOf!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,IOLPowerArrayList!!)
        binding.spinnerIOLPower!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,UnitArrayList!!)
        binding.spinnerTemperatureUnit!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,EyeDropsArrayList!!)
        binding.Spinner2DropsOfTropicamide!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,EyeDropsArrayList!!)
        binding.Spinner2DropsOfBetadine!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,AllergyTestResultArrayList!!)
        binding.SpinnerAnitiBioticTestResult!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,AllergyTestResultArrayList!!)
        binding.SpinnerXylocaineTestResult!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,AllergyTestArrayList!!)
        binding.SpinnerCheckedSkinAfter20Minutes!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,AllergyTestArrayList!!)
        binding.SpinnerXylocaine!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,DiscussedWithArrayList!!)
        binding.SpinnerDiscussedWith!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList!!)
        binding.spinnerBloodSugarFasting!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList!!)
        binding.spinnerBloodSugarPp!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList!!)
        binding.spinnerHaemoglobin!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList!!)
        binding.spinnerProthrombinTime!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList!!)
        binding.spinnerCbc!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList!!)
        binding.spinnerBleedingTime!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList1!!)
        binding.spinnerHiv!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList!!)
        binding.spinnerClottingTime!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList1!!)
        binding.spinnerHbsag!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList!!)
        binding.SpinnerECG!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,InvestigationArrayList1!!)
        binding.spinnerHcv!!.adapter=customDropDownAdapter

        binding.EditTextDateOfAdmission.setOnClickListener(this)
        binding.CheckBoxDiscussedWith.setOnClickListener(this)
        binding.radioButonLineUpForSurgery.setOnClickListener(this)
        binding.radioButonUnfitForSurgery.setOnClickListener(this)
        binding.radioButonCanNotOperate.setOnClickListener(this)
        binding.CardViewAddHistoryOf.setOnClickListener(this)
        binding.CardViewUploadImage.setOnClickListener(this)
        binding.cardViewSubmitEyePreOpNotes.setOnClickListener(this)
        binding.CheckBoxIdentifyCorrectEye.setOnCheckedChangeListener(this)
        binding.CheckBoxWashTheFace.setOnCheckedChangeListener(this)
        binding.CheckBoxNilByMouth4Hours.setOnCheckedChangeListener(this)
        binding.CheckBoxHeadBath.setOnCheckedChangeListener(this)
        binding.CheckBoxAntihypertensiveMedication.setOnCheckedChangeListener(this)
        binding.CheckBoxHeartMedicationTaken.setOnCheckedChangeListener(this)
        binding.CheckBoxDiabetesMedicationTaken.setOnCheckedChangeListener(this)
        binding.CheckBoxAnyOtherInstruction.setOnCheckedChangeListener(this)
        binding.CheckBoxDiamox.setOnCheckedChangeListener(this)
        binding.CheckBoxAlprax.setOnCheckedChangeListener(this)
        binding.CheckBoxCiplox.setOnCheckedChangeListener(this)
        binding.CheckBoxDiscussedWith.setOnCheckedChangeListener(this)
        binding.CheckBoxAlleryTestAntibiotic.setOnCheckedChangeListener(this)
        binding.CheckBoxCheckBoxAlleryTestXylocaine.setOnCheckedChangeListener(this)
        binding.CheckBoxTropicacylPlusEyeDrop.setOnCheckedChangeListener(this)
        binding.CheckBoxPlainTropicacylIfMyopia.setOnCheckedChangeListener(this)
        binding.CheckBoxCiploxAntibioticDrops.setOnCheckedChangeListener(this)
        binding.CheckBoxFlurEyeDrop.setOnCheckedChangeListener(this)
        binding.CheckBoxAmlodipine.setOnCheckedChangeListener(this)

        binding.spinnerBloodSugarFasting.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_bs_f=binding.spinnerBloodSugarFasting.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerHaemoglobin.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 eye_pre_op_haemoglobin=binding.spinnerHaemoglobin.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerBloodSugarPp.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_bs_pp=binding.spinnerBloodSugarPp.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerProthrombinTime.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_pt=binding.spinnerProthrombinTime.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerCbc.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_cbc=binding.spinnerCbc.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerBleedingTime.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_bt=binding.spinnerBleedingTime.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerHiv.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 eye_pre_op_hiv=binding.spinnerHiv.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerClottingTime.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 eye_pre_op_ct=binding.spinnerClottingTime.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerHbsag.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_hbsag=binding.spinnerHbsag.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerHcv.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_hcv=binding.spinnerHcv.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerECG.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 eye_pre_op_ecg=binding.SpinnerECG.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerXylocaine.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_xylocaine_detail=binding.SpinnerXylocaine.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerXylocaineTestResult.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_xylocaine_result=binding.SpinnerXylocaineTestResult.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.Spinner2DropsOfTropicamide.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_tropicamide=binding.Spinner2DropsOfTropicamide.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.Spinner2DropsOfBetadine.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_betadine=binding.Spinner2DropsOfBetadine.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerIOLPower.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 eye_pre_op_iol_power=binding.spinnerIOLPower.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerTemperatureUnit.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_temp_unit=binding.spinnerTemperatureUnit.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerAnitiBioticTestResult.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 eye_pre_op_antibiotic_result=binding.SpinnerAnitiBioticTestResult.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerCheckedSkinAfter20Minutes.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_antibiotic_detail=binding.SpinnerCheckedSkinAfter20Minutes.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerDiscussedWith.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_pre_op_discussed_with=binding.SpinnerDiscussedWith.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerDiscussedWith.visibility=View.GONE
        binding.EditTextDiscussedWith.visibility=View.GONE
        binding.EditTextUnfitForSurgery.visibility=View.GONE
        binding.EditTextCanNotOperate.visibility=View.GONE
        binding.EditTextAntihypertensiveMedication.visibility=View.GONE
        binding.EditTextHeartMedicationTaken.visibility=View.GONE
        binding.EditTextDiabetesMedicationTaken.visibility=View.GONE
        binding.EditTextAnyOtherInstruction.visibility=View.GONE
        binding.LinearLayoutLineUpSurgery!!.visibility = if (isLineUpSurgeryVisible) View.VISIBLE else View.GONE
        binding.LinearLayoutAntibiotic.visibility=View.GONE
        binding.LinearLayoutXylocaine.visibility=View.GONE
        binding.RecyclerViewHistoryOf.visibility=View.GONE
        binding.EditTextDiastolic.addTextChangedListener(createTextWatcher(binding.EditTextDiastolic))
        binding.EditTextO2Saturation.addTextChangedListener(createTextWatcher(binding.EditTextO2Saturation))
    }

    private fun getPermission() {
        val cameraPermissionListener: PermissionListener = object : EmptyPermissionListener() {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {}

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                showAppSettings()
            }
        }

        val multiplePermissionsListener: MultiplePermissionsListener =
            object : CompositeMultiplePermissionsListener() {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // Handle multiple permissions checks if needed
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                }
            }

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(cameraPermissionListener)
            .onSameThread()
            .check()
    }

    private fun initView() {
        val layoutManager= LinearLayoutManager(this)
        layoutManager.orientation= RecyclerView.VERTICAL
        binding.RecyclerViewHistoryOf!!.layoutManager=layoutManager
        binding.RecyclerViewHistoryOf!!.setHasFixedSize(true)
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
                binding.EditTextDateOfAdmission.setText(selectedDate)
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

    private fun AddHistory(selected_history: String, recyclerView: RecyclerView) {
        binding.RecyclerViewHistoryOf.visibility=View.VISIBLE
        HistoryOfArrayList1!!.add(selected_history)
        recyclerView.adapter = Adapter_Eye_Pre_Op_Notes_History(this, HistoryOfArrayList1!!)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView) {
            binding.CheckBoxDiscussedWith-> {
                binding.EditTextDiscussedWith.visibility = if (isChecked) View.VISIBLE else View.GONE
                binding.SpinnerDiscussedWith.visibility = if (isChecked) View.VISIBLE else View.GONE
            }

            binding.CheckBoxAlleryTestAntibiotic-> {
                binding.LinearLayoutAntibiotic.visibility = if (isChecked) View.VISIBLE else View.GONE
                if (isChecked) {
                    eye_pre_op_antibiotic = binding.CheckBoxAlleryTestAntibiotic.text.toString()
                    Log.d(ConstantsApp.TAG, "eye_pre_op_antibiotic => $eye_pre_op_antibiotic")
                } else {
                    eye_pre_op_antibiotic = ""
                    Log.d(ConstantsApp.TAG, "eye_pre_op_antibiotic => $eye_pre_op_antibiotic")
                }
            }

            binding.CheckBoxCheckBoxAlleryTestXylocaine-> {
                binding.LinearLayoutXylocaine.visibility = if (isChecked) View.VISIBLE else View.GONE
                if (isChecked) {
                    eye_pre_op_xylocaine=binding.CheckBoxCheckBoxAlleryTestXylocaine.text.toString()
                } else {
                    eye_pre_op_xylocaine=""
                }
            }

            binding.CheckBoxIdentifyCorrectEye-> {
                if(isChecked) {
                    eye_pre_op_identify_eye=binding.CheckBoxIdentifyCorrectEye.text.toString()
                } else {
                    eye_pre_op_identify_eye=" "
                }
            }

            binding.CheckBoxWashTheFace-> {
                if(isChecked) {
                    eye_pre_op_wash_face=binding.CheckBoxWashTheFace.text.toString()
                } else {
                    eye_pre_op_wash_face=""
                }
            }

            binding.CheckBoxNilByMouth4Hours-> {
                if(isChecked) {
                    eye_pre_op_nil_mouth=binding.CheckBoxNilByMouth4Hours.text.toString()
                } else {
                    eye_pre_op_nil_mouth=""
                }
            }

            binding.CheckBoxHeadBath-> {
                if(isChecked) {
                    eye_pre_op_head_bath=binding.CheckBoxHeadBath.text.toString()
                } else {
                    eye_pre_op_head_bath=""
                }
            }

            binding.CheckBoxAntihypertensiveMedication-> {
                if(isChecked) {
                    binding.EditTextAntihypertensiveMedication.visibility=View.VISIBLE
                    eye_pre_op_antihyp=binding.CheckBoxAntihypertensiveMedication.text.toString()
                } else {
                    eye_pre_op_antihyp=""
                }
            }

            binding.CheckBoxHeartMedicationTaken-> {
                if(isChecked) {
                    binding.EditTextHeartMedicationTaken.visibility=View.VISIBLE
                    eye_pre_op_heart=binding.CheckBoxHeartMedicationTaken.text.toString()
                } else {
                    eye_pre_op_heart=""
                }
            }

            binding.CheckBoxDiabetesMedicationTaken-> {
                if (isChecked) {
                    binding.EditTextDiabetesMedicationTaken.visibility=View.VISIBLE
                    eye_pre_op_dia=binding.CheckBoxDiabetesMedicationTaken.text.toString()
                } else {
                    eye_pre_op_dia=""
                }
            }

            binding.CheckBoxAnyOtherInstruction-> {
                if (isChecked) {
                    binding.EditTextAnyOtherInstruction.visibility=View.VISIBLE
                    eye_pre_op_other=binding.CheckBoxAnyOtherInstruction.text.toString()
                } else {
                    eye_pre_op_other=""
                }
            }

            binding.CheckBoxDiamox-> {
                if (isChecked) {
                    eye_pre_op_diamox=binding.CheckBoxDiamox.text.toString()
                } else {
                    eye_pre_op_diamox=""
                }
            }

            binding.CheckBoxAlprax-> {
                if (isChecked) {
                    eye_pre_op_alprax=binding.CheckBoxAlprax.text.toString()
                } else {
                    eye_pre_op_alprax=""
                }
            }

            binding.CheckBoxCiplox-> {
                if (isChecked) {
                    eye_pre_op_ciplox=binding.CheckBoxCiplox.text.toString()
                } else {
                    eye_pre_op_ciplox=""
                }
            }

            binding.CheckBoxTropicacylPlusEyeDrop-> {
                if (isChecked) {
                    eye_pre_op_tropical_drop=binding.CheckBoxTropicacylPlusEyeDrop.text.toString()
                } else {
                    eye_pre_op_tropical_drop=""
                }
            }

            binding.CheckBoxPlainTropicacylIfMyopia-> {
                if (isChecked) {
                   eye_pre_op_plain_tropical=binding.CheckBoxPlainTropicacylIfMyopia.text.toString()
                } else {
                    eye_pre_op_plain_tropical=""
                }
            }

            binding.CheckBoxCiploxAntibioticDrops-> {
                if (isChecked) {
                   eye_pre_op_ciplox_drop=binding.CheckBoxCiploxAntibioticDrops.text.toString()
                } else {
                    eye_pre_op_ciplox_drop=""
                }
            }

            binding.CheckBoxFlurEyeDrop-> {
                if (isChecked) {
                  eye_pre_op_flur_eye=binding.CheckBoxFlurEyeDrop.text.toString()
                } else {
                    eye_pre_op_flur_eye=""
                }
            }

            binding.CheckBoxAmlodipine-> {
                if (isChecked) {
                  eye_pre_op_amlodipine=binding.CheckBoxAmlodipine.text.toString()
                } else {
                    eye_pre_op_amlodipine=""
                }
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
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
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
            binding.ImageViewEyePreOpNotes?.setImageURI(imageUri)
            binding.TextViewEyePreOpNotes.text=filePath
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "surgical_notes_image_$timestamp.jpg"
            val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!,fileName)
            UpdatedfilePath= updatedPath!!
            binding.ImageViewEyePreOpNotes?.setImageURI(Uri.fromFile(File(updatedPath)))
            binding.TextViewEyePreOpNotes.text = updatedPath
        }
        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            filePath = saveBitmapToFile(imageBitmap, this)
            Log.d(ConstantsApp.TAG,"filepath on camera click=>"+filePath)

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "pre_op_notes_image_$timestamp.jpg"

            val tempFile = ConstantsApp.saveBitmapToFile1(imageBitmap, fileName, this)
            val imageUri = FileProvider.getUriForFile(
                this,
                "org.impactindiafoundation.iifllemeddocket.provider", // ✅ matches manifest
                tempFile
            )

            val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!,fileName)
            UpdatedfilePath= updatedPath!!
            binding.ImageViewEyePreOpNotes?.setImageURI(Uri.fromFile(File(updatedPath)))
            binding.TextViewEyePreOpNotes.text = updatedPath
        }
    }

    private fun showAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
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
                when (editText) {
                    binding.EditTextDiastolic -> {
                        setBloodPressure()
                    }
                    binding.EditTextO2Saturation -> {
                        interpretOxygenSaturation(s.toString())
                    }
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when (editText) {
                    binding.EditTextDiastolic -> {
                        setBloodPressure()
                    }
                    binding.EditTextO2Saturation -> {
                        interpretOxygenSaturation(s.toString())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                when (editText) {
                    binding.EditTextDiastolic -> {
                        setBloodPressure()
                    }
                    binding.EditTextO2Saturation -> {
                        interpretOxygenSaturation(s.toString())
                    }
                }
            }
        }
    }

    private fun setBloodPressure() {
        val systolic = binding.EditTextSystolic.text.toString().toIntOrNull() ?: 0
        val diastolic = binding.EditTextDiastolic.text.toString().toIntOrNull() ?: 0
        val bloodPressureInfo = getBloodPressureType(systolic, diastolic)
        binding.TextViewBPInterpretation.text = "${bloodPressureInfo.first}"
        binding.TextViewBPInterpretation.setTextColor(ContextCompat.getColor(this, bloodPressureInfo.second))
    }

    private fun getBloodPressureType(systolic: Int, diastolic: Int): Pair<String, Int> {
        return when {
            systolic == 0 || diastolic == 0 -> Pair("", android.R.color.black)
            systolic < 90 || (diastolic in 0..60) -> Pair("Hypotension", R.color.blue)
            systolic < 120 && diastolic < 80 -> Pair("Normal", R.color.black)
            systolic in 120..139 || diastolic in 80..89 -> Pair("Prehypertension", R.color.red)
            systolic in 140..159 || diastolic in 90..99 -> Pair("Stage 1 Hypertension", R.color.red)
            systolic >= 160 || diastolic >= 100 -> Pair("Stage 2 Hypertension", R.color.red)
            else -> Pair("Unknown", android.R.color.black)
        }
    }

    private fun interpretOxygenSaturation(input: String) {
        val saturationLevel = input.toDoubleOrNull()
        saturationLevel?.let {
            val interpretation = when {
                it >= 98.0 -> "Normal"
                it in 95.0..97.9 -> "Insufficient but tolerable"
                it in 90.0..94.9 -> "Hypoxia, requiring immediate intervention"
                it < 90.0 -> "Critical Hypoxia"
                else -> "Not Defined"
            }
            binding.TextViewO2SaturationInterpretation.text = interpretation
            setColorBasedOnOxygenSaturation(interpretation)
        } ?: run {
            binding.TextViewO2SaturationInterpretation.text = ""
        }
    }

    private fun setColorBasedOnOxygenSaturation(interpretation: String) {
        val color = when {
            interpretation.contains("Normal") -> Color.BLACK
            interpretation.contains("Insufficient but tolerable") -> Color.RED
            interpretation.contains("Hypoxia") || interpretation.contains("Critical Hypoxia") -> Color.BLUE
            else -> Color.BLACK
        }
        binding.TextViewO2SaturationInterpretation.setTextColor(color)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.EditTextDateOfAdmission-> {
                showDatePickerDialog()
            }

            binding.radioButonLineUpForSurgery-> {
                binding.radioButonLineUpForSurgery.isChecked=true
                binding.radioButonUnfitForSurgery.isChecked=false
                binding.radioButonCanNotOperate.isChecked=false
                binding.EditTextUnfitForSurgery.visibility=View.GONE
                binding.EditTextCanNotOperate.visibility=View.GONE
                binding.LinearLayoutLineUpSurgery!!.visibility=View.VISIBLE
                isLineUpSurgeryVisible = true
                eye_pre_op_recommendation=binding.radioButonLineUpForSurgery.text.toString()
                eye_pre_op_recommendation_detail=""
            }

            binding.radioButonUnfitForSurgery-> {
                binding.radioButonUnfitForSurgery.isChecked=true
                binding.radioButonLineUpForSurgery.isChecked=false
                binding.radioButonCanNotOperate.isChecked=false
                binding.EditTextUnfitForSurgery.visibility=View.VISIBLE
                binding.EditTextCanNotOperate.visibility=View.GONE
                binding.LinearLayoutLineUpSurgery!!.visibility=View.GONE
                eye_pre_op_recommendation=binding.radioButonUnfitForSurgery.text.toString()
                eye_pre_op_recommendation_detail=binding.EditTextUnfitForSurgery.text.toString()
            }

            binding.radioButonCanNotOperate-> {
                binding.radioButonCanNotOperate.isChecked=true
                binding.radioButonUnfitForSurgery.isChecked=false
                binding.radioButonLineUpForSurgery.isChecked=false
                binding.EditTextUnfitForSurgery.visibility=View.GONE
                binding.EditTextCanNotOperate.visibility=View.VISIBLE
                binding.LinearLayoutLineUpSurgery!!.visibility=View.GONE
                eye_pre_op_recommendation=binding.radioButonCanNotOperate.text.toString()
                eye_pre_op_recommendation_detail=binding.EditTextCanNotOperate.text.toString()
            }

            binding.CardViewAddHistoryOf-> {
                val selected_history=binding.SpinnerHistoryOf.selectedItem.toString()
                Log.d(ConstantsApp.TAG,"selected_history=>"+selected_history)
                AddHistory(selected_history,binding.RecyclerViewHistoryOf)
            }

            binding.CardViewUploadImage-> {
                showPictureDialog()
            }

            binding.cardViewSubmitEyePreOpNotes -> {

                // ✅ Step 1: Ensure at least one radio button is selected
                if (!binding.radioButonLineUpForSurgery.isChecked &&
                    !binding.radioButonUnfitForSurgery.isChecked &&
                    !binding.radioButonCanNotOperate.isChecked) {

                    Toast.makeText(this, "Please select a recommendation option.", Toast.LENGTH_SHORT).show()
                }

                // ✅ Step 2: Handle validations based on selected radio button
                when {
                    // 🟢 Line Up for Surgery → Image required
                    binding.radioButonLineUpForSurgery.isChecked -> {
                        if (filePath.isNullOrEmpty()) {
                            Toast.makeText(
                                this,
                                "Please upload or capture an image before submitting.",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Log.d(ConstantsApp.TAG, "Line Up selected, image OK → submitting data")
                            SubmitDataOnly()
                        }
                    }

                    // 🟡 Unfit for Surgery → EditText required
                    binding.radioButonUnfitForSurgery.isChecked -> {
                        val reason = binding.EditTextUnfitForSurgery.text.toString().trim()
                        if (reason.isEmpty()) {
                            Toast.makeText(
                                this,
                                "Please enter reason for unfit for surgery.",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Log.d(ConstantsApp.TAG, "Unfit for surgery reason entered → submitting data")
                            SubmitDataOnly()
                        }
                    }

                    // 🔴 Cannot Operate → EditText required
                    binding.radioButonCanNotOperate.isChecked -> {
                        val reason = binding.EditTextCanNotOperate.text.toString().trim()
                        if (reason.isEmpty()) {
                            Toast.makeText(
                                this,
                                "Please enter reason for cannot operate.",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Log.d(ConstantsApp.TAG, "Cannot operate reason entered → submitting data")
                            SubmitDataOnly()
                        }
                    }
                }
            }


//                    binding.cardViewSubmitEyePreOpNotes-> {
//                if (filePath.isNullOrEmpty()) {
//                    Log.d(ConstantsApp.TAG,"Image no")
//                    SubmitDataOnly()
//                } else {
//                    Log.d(ConstantsApp.TAG,"Image yes")
//                    SubmitDataOnly()
//                }
//            }
        }
    }

    private fun SubmitDataOnly() {
        val (patientId, campId, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
        val current_Date= ConstantsApp.getCurrentDate()
        val eye_pre_op_admission_date=binding.EditTextDateOfAdmission.text.toString()
        val eye_pre_op_symptoms=binding.EditTextSymptoms.text.toString()
        val eye_pre_op_temp=binding.EditTextTemperature.text.toString()
        val eye_pre_op_heart_rate=binding.EditTextHeartRate.text.toString()
        val eye_pre_op_bp_systolic=binding.EditTextSystolic.text.toString()
        val eye_pre_op_bp_diastolic=binding.EditTextDiastolic.text.toString()
        val eye_pre_op_bp_interpretation=binding.TextViewBPInterpretation.text.toString()
        val eye_pre_op_o2_saturation=binding.EditTextO2Saturation.text.toString()
        val eye_pre_op_o2_saturation_interpretation=binding.TextViewO2SaturationInterpretation.text.toString()
        val eye_pre_op_discussed_with_detail=binding.EditTextDiscussedWith.text.toString()
        val eye_pre_op_notes=binding.EditTextNotes.text.toString()
        eye_pre_op_antihyp_detail=binding.EditTextAntihypertensiveMedication.text.toString()
        eye_pre_op_heart_detail=binding.EditTextHeartMedicationTaken.text.toString()
        eye_pre_op_dia_detail=binding.EditTextDiabetesMedicationTaken.text.toString()
        eye_pre_op_other_detail=binding.EditTextAnyOtherInstruction.text.toString()
        val eye_pre_op_antibiotic_other=binding.EditTextAntiBioticOther.text.toString()
        val eye_pre_op_xylocaine_other=binding.EditTextXylocaineOther.text.toString()
        val eye_pre_op_historyof: String = HistoryOfArrayList1?.joinToString(",") ?: ""
        Log.d(ConstantsApp.TAG,"eye_pre_op_recommendation=>"+eye_pre_op_recommendation)

        SubmitEyePreOPNotes(
            campId,
            userId,
            patientId,
            current_Date,
            eye_pre_op_admission_date,
            eye_pre_op_symptoms,
            eye_pre_op_temp,
            eye_pre_op_temp_unit,
            eye_pre_op_heart_rate,
            eye_pre_op_bp_systolic,
            eye_pre_op_bp_diastolic,
            eye_pre_op_bp_interpretation,
            eye_pre_op_recommendation,
            eye_pre_op_recommendation_detail!!,
            eye_pre_op_identify_eye!!,
            eye_pre_op_wash_face,
            eye_pre_op_nil_mouth,
            eye_pre_op_head_bath,
            eye_pre_op_antihyp,
            eye_pre_op_antihyp_detail,
            eye_pre_op_heart,
            eye_pre_op_heart_detail,
            eye_pre_op_dia,
            eye_pre_op_dia_detail,
            eye_pre_op_other,
            eye_pre_op_other_detail,
            eye_pre_op_diamox,
            eye_pre_op_alprax,
            eye_pre_op_ciplox,
            eye_pre_op_o2_saturation,
            eye_pre_op_o2_saturation_interpretation,
            eye_pre_op_bs_f,
            eye_pre_op_bs_pp,
            eye_pre_op_haemoglobin,
            eye_pre_op_pt,
            eye_pre_op_cbc,
            eye_pre_op_bt,
            eye_pre_op_hiv,
            eye_pre_op_ct,
            eye_pre_op_hbsag,
            eye_pre_op_hcv,
            eye_pre_op_ecg,
            eye_pre_op_discussed_with,
            eye_pre_op_discussed_with_detail,
            eye_pre_op_tropicamide,
            eye_pre_op_betadine,
            eye_pre_op_notes,
            eye_pre_op_iol_power,
            eye_pre_op_tropical_drop,
            eye_pre_op_plain_tropical,
            eye_pre_op_ciplox_drop,
            eye_pre_op_flur_eye,
            eye_pre_op_amlodipine,
            eye_pre_op_antibiotic,
            eye_pre_op_antibiotic_detail,
            eye_pre_op_antibiotic_other,
            eye_pre_op_antibiotic_result,
            eye_pre_op_xylocaine,
            eye_pre_op_xylocaine_detail,
            eye_pre_op_xylocaine_other,
            eye_pre_op_xylocaine_result,
            eye_pre_op_historyof
        )
    }

    private fun SubmitEyePreOPNotes(
        camp_id: Int?,
        user_id: String?,
        patient_id: Int?,
        current_Date: String,
        eye_pre_op_admission_date: String,
        eye_pre_op_symptoms: String,
        eye_pre_op_temp: String,
        eye_pre_op_temp_unit: String,
        eye_pre_op_heart_rate: String,
        eye_pre_op_bp_systolic: String,
        eye_pre_op_bp_diastolic: String,
        eye_pre_op_bp_interpretation: String,
        eye_pre_op_recommendation: String?,
        eye_pre_op_recommendation_detail: String,
        eye_pre_op_identify_eye: String,
        eye_pre_op_wash_face: String,
        eye_pre_op_nil_mouth: String,
        eye_pre_op_head_bath: String,
        eye_pre_op_antihyp: String,
        eye_pre_op_antihyp_detail: String,
        eye_pre_op_heart: String,
        eye_pre_op_heart_detail: String,
        eye_pre_op_dia: String,
        eye_pre_op_dia_detail: String,
        eye_pre_op_other: String,
        eye_pre_op_other_detail: String,
        eye_pre_op_diamox: String,
        eye_pre_op_alprax: String,
        eye_pre_op_ciplox: String,
        eye_pre_op_o2_saturation: String,
        eye_pre_op_o2_saturation_interpretation: String,
        eye_pre_op_bs_f: String,
        eye_pre_op_bs_pp: String,
        eye_pre_op_haemoglobin: String,
        eye_pre_op_pt: String,
        eye_pre_op_cbc: String,
        eye_pre_op_bt: String,
        eye_pre_op_hiv: String,
        eye_pre_op_ct: String,
        eye_pre_op_hbsag: String,
        eye_pre_op_hcv: String,
        eye_pre_op_ecg: String,
        eye_pre_op_discussed_with: String,
        eye_pre_op_discussed_with_detail: String,
        eye_pre_op_tropicamide: String,
        eye_pre_op_betadine: String,
        eye_pre_op_notes: String,
        eye_pre_op_iol_power: String,
        eye_pre_op_tropical_drop: String,
        eye_pre_op_plain_tropical: String,
        eye_pre_op_ciplox_drop: String,
        eye_pre_op_flur_eye: String,
        eye_pre_op_amlodipine: String,
        eye_pre_op_antibiotic: String,
        eye_pre_op_antibiotic_detail: String,
        eye_pre_op_antibiotic_other: String,
        eye_pre_op_antibiotic_result: String,
        eye_pre_op_xylocaine: String,
        eye_pre_op_xylocaine_detail: String,
        eye_pre_op_xylocaine_other: String,
        eye_pre_op_xylocaine_result: String,
        eye_pre_op_historyof: String
    ) {
        if (UpdatedfilePath=="") {
            val eyePreNotes=Eye_Pre_Op_Notes(0,
                camp_id!!,
                current_Date,
                eye_pre_op_admission_date,
                eye_pre_op_alprax,
                eye_pre_op_amlodipine,
                eye_pre_op_antibiotic,
                eye_pre_op_antibiotic_detail,
                eye_pre_op_antibiotic_other,
                eye_pre_op_antibiotic_result,
                eye_pre_op_antihyp,
                eye_pre_op_antihyp_detail,
                eye_pre_op_betadine,
                eye_pre_op_bp_diastolic,
                eye_pre_op_bp_interpretation,
                eye_pre_op_bp_systolic,
                eye_pre_op_bs_f,
                eye_pre_op_bs_pp,
                eye_pre_op_bt,
                eye_pre_op_cbc,
                eye_pre_op_ciplox,
                eye_pre_op_ciplox_drop,
                eye_pre_op_ct,
                eye_pre_op_dia,
                eye_pre_op_dia_detail,
                eye_pre_op_diamox,
                eye_pre_op_discussed_with,
                eye_pre_op_discussed_with_detail,
                eye_pre_op_ecg,
                eye_pre_op_flur_eye,
                eye_pre_op_haemoglobin,
                eye_pre_op_hbsag,
                eye_pre_op_hcv,
                eye_pre_op_head_bath,
                eye_pre_op_heart,
                eye_pre_op_heart_detail,
                eye_pre_op_heart_rate,
                eye_pre_op_historyof,
                eye_pre_op_hiv,
                eye_pre_op_identify_eye!!,
                eye_pre_op_iol_power,
                eye_pre_op_nil_mouth,
                eye_pre_op_notes,
                eye_pre_op_o2_saturation,
                eye_pre_op_o2_saturation_interpretation,
                eye_pre_op_other,
                eye_pre_op_other_detail,
                eye_pre_op_plain_tropical,
                eye_pre_op_pt,
                eye_pre_op_recommendation!!,
                eye_pre_op_recommendation_detail!!,
                eye_pre_op_symptoms,
                eye_pre_op_temp,
                eye_pre_op_temp_unit,
                eye_pre_op_tropical_drop,
                eye_pre_op_tropicamide,
                eye_pre_op_wash_face,
                eye_pre_op_xylocaine,
                eye_pre_op_xylocaine_detail,
                eye_pre_op_xylocaine_other,
                eye_pre_op_xylocaine_result,
                patient_id!!,
                user_id!!,
                "",
                "111"
            )
            Log.d(ConstantsApp.TAG,"eyePreNotes=>"+eyePreNotes)
            viewModel1.insertEye_Pre_Op_Notes1(eyePreNotes)
            InsertEye_Pre_Op_Notes_Localresponse()
        } else {
            val eyePreNotes=Eye_Pre_Op_Notes(0,
                camp_id!!,
                current_Date,
                eye_pre_op_admission_date,
                eye_pre_op_alprax,
                eye_pre_op_amlodipine,
                eye_pre_op_antibiotic,
                eye_pre_op_antibiotic_detail,
                eye_pre_op_antibiotic_other,
                eye_pre_op_antibiotic_result,
                eye_pre_op_antihyp,
                eye_pre_op_antihyp_detail,
                eye_pre_op_betadine,
                eye_pre_op_bp_diastolic,
                eye_pre_op_bp_interpretation,
                eye_pre_op_bp_systolic,
                eye_pre_op_bs_f,
                eye_pre_op_bs_pp,
                eye_pre_op_bt,
                eye_pre_op_cbc,
                eye_pre_op_ciplox,
                eye_pre_op_ciplox_drop,
                eye_pre_op_ct,
                eye_pre_op_dia,
                eye_pre_op_dia_detail,
                eye_pre_op_diamox,
                eye_pre_op_discussed_with,
                eye_pre_op_discussed_with_detail,
                eye_pre_op_ecg,
                eye_pre_op_flur_eye,
                eye_pre_op_haemoglobin,
                eye_pre_op_hbsag,
                eye_pre_op_hcv,
                eye_pre_op_head_bath,
                eye_pre_op_heart,
                eye_pre_op_heart_detail,
                eye_pre_op_heart_rate,
                eye_pre_op_historyof,
                eye_pre_op_hiv,
                eye_pre_op_identify_eye!!,
                eye_pre_op_iol_power,
                eye_pre_op_nil_mouth,
                eye_pre_op_notes,
                eye_pre_op_o2_saturation,
                eye_pre_op_o2_saturation_interpretation,
                eye_pre_op_other,
                eye_pre_op_other_detail,
                eye_pre_op_plain_tropical,
                eye_pre_op_pt,
                eye_pre_op_recommendation!!,
                eye_pre_op_recommendation_detail!!,
                eye_pre_op_symptoms,
                eye_pre_op_temp,
                eye_pre_op_temp_unit,
                eye_pre_op_tropical_drop,
                eye_pre_op_tropicamide,
                eye_pre_op_wash_face,
                eye_pre_op_xylocaine,
                eye_pre_op_xylocaine_detail,
                eye_pre_op_xylocaine_other,
                eye_pre_op_xylocaine_result,
                patient_id!!,
                user_id!!,
                UpdatedfilePath,
                "111"
            )
            Log.d(ConstantsApp.TAG,"eyePreNotes=>"+eyePreNotes)
            viewModel1.insertEye_Pre_Op_Notes1(eyePreNotes)
            InsertEye_Pre_Op_Notes_Localresponse()
        }
    }

    private fun InsertEye_Pre_Op_Notes_Localresponse() {
        viewModel1.toastMessage.observe(this) { message ->
            Log.d(ConstantsApp.TAG, "Toast message received => $message")
            showToast(message)
        }

        viewModel1.insertResponse1.observe(this) { response ->
            response?.let {
                val _id = it.first
                val camp_id = it.second
                val patient_id = it.third
                val userId = it.fourth
                val filePath = it.fifth

                Log.d("pawan", "Insert Response => _id=$_id, camp_id=$camp_id, patient_id=$patient_id, userId=$userId, filePath=$filePath")

                if (_id != null && camp_id != null && patient_id != null && userId != null && filePath != null) {
                    if (filePath.isNotEmpty()) {
                        val fileName = ConstantsApp.getFileNameFromPath(filePath)
                        Log.d("pawan", "Image found. fileName=$fileName, fullPath=$filePath")

                        val imageModel = ImageModel(
                            0,
                            _id,
                            fileName,
                            "111",
                            patient_id,
                            camp_id,
                            userId,
                            filePath
                        )

                        Log.d("pawan", "Inserting ImageModel => $imageModel")
                        viewModel1.InsertImageLocal(imageModel)
                    } else {
                        Log.d("pawan", "No image uploaded (filePath is empty). Skipping image insert.")
                    }

                    gotoScreen(this, PatientForms::class.java)
                } else {
                    Log.e("pawan", "Some response properties are null: _id=$_id, camp_id=$camp_id, patient_id=$patient_id, userId=$userId, filePath=$filePath")
                    showToast("Some properties are null in the response")
                    gotoScreen(this, PatientForms::class.java)
                }
            } ?: run {
                Log.e("pawan", "InsertEye_Pre_Op_Notes_Localresponse: response is null")
            }
        }
    }


    private fun showToast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}