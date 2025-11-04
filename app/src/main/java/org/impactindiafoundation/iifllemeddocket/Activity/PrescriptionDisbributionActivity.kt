package org.impactindiafoundation.iifllemeddocket.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PrescriptionGlassesFinal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleDisdributionStatusModel
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.MyValidator
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.MeasurementPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPrescriptionDisbributionBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.OtherMeasurementAdapter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PrescriptionDisbributionActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    lateinit var binding: ActivityPrescriptionDisbributionBinding
    lateinit var customDropDownAdapter: CustomDropDownAdapter
    private lateinit var progressDialog1: ProgressDialog
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    private val patientReportVM: EntPatientReportViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    var camp_id: Int? = 0
    var user_id: Int = 0
    var spectacle_given: Boolean = false
    var patient_not_come: Boolean = false
    var selected_Text = ""
    var selected_given_presc_type = ""
    var spectacle_not_matching: Boolean = false
    var ordered_not_received: Boolean = false
    var patient_call_again: Boolean = false
    var NotMatchingArrayList: ArrayList<String>? = null
    var SpectacleTypeArrayList: ArrayList<String>? = null
    private var intentDecodeText = ""
    private var campId = 0
    private var patientId = 0
    private lateinit var popupWindow3: PopupWindow
    private var intentFormId = 0
    private var localFormId = 0
    private var patientReportFormId = 0
    private var canEdit = true
    private var isFormLocal = false
    var patientFname = ""
    var patientLname = ""
    var patientAge = 0
    var campID = 0
    var patientGender = ""
    var camp = ""
    var ageUnit=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionDisbributionBinding.inflate(layoutInflater)
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

        intentFormId = intent.getIntExtra("localFormId",0)
        campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId",0)
        binding.toolbarEyePreOpNotes.toolbar.title = "Spectacle Distribution"
        getViewModel()
        createRoomDatabase()
        val data = sessionManager.getLoginData()
        Log.d(ConstantsApp.TAG, "login data=>" + data)
        try {
            for (login in data!!) {
                user_id = login.Userid
            }
        } catch (e: Exception) {
            Log.e("LoginError", e.message.toString())
        }

        val decodedText = sessionManager.getPatientData()
        val gson = Gson()
        val patientData = gson.fromJson(decodedText, PatientData::class.java)

        if (patientData != null) {
            patientId = patientData.patientId
            patientId = intent.getIntExtra("patient_id", 0)
            campId = intent.getIntExtra("camp_id", 0)
        } else {
            val patientIdIntent = intent.getIntExtra("patient_id", 0)
            patientId = intent.getIntExtra("patient_id", 0)
            campId = intent.getIntExtra("camp_id", 0)
            intentDecodeText = intent.getStringExtra("result") ?: ""
            if (!intentDecodeText.isNullOrEmpty()) {
                val gsonn = Gson()
                val patientDataN = gsonn.fromJson(intentDecodeText, PatientData::class.java)
                campId = patientDataN.camp_id
                patientId = patientDataN.patientId
            }
        }

        binding.CheckBoxSameAs.setOnCheckedChangeListener(this)
        binding.cardViewSubmit.setOnClickListener(this)
        binding.RadioGroup.setOnCheckedChangeListener(this)
        binding.edittextCallAgain.setOnClickListener(this)

        binding.btnEdit.setOnClickListener {
            if (!canEdit){
                onFormEditClick()
                allowClickableEditText(true)
            }
        }

        initObserver()
        setPatientData()
    }

    private fun initObserver(){
        viewModel1.insertionprescriptionGlassesStatus.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        val patientReport = PrescriptionPatientReport(
                            id = patientReportFormId,
                            formId = it.data?.toInt() ?: 0,
                            patientFname = patientFname,
                            patientLname = patientLname,
                            patientId = patientId,
                            patientGen = patientGender,
                            patientAge = patientAge,
                            camp_id = campId,
                            location = camp,
                            AgeUnit =ageUnit,
                        )
                        Log.d("xyz", "insertData : ${patientReport.toString()}")
                        patientReportVM.insertPrescriptionPatientReport(patientReport)
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@PrescriptionDisbributionActivity, "Unexpected error")
                }
            }
        }

        patientReportVM.insertPatientReportResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        Utility.successToast(
                            this@PrescriptionDisbributionActivity,
                            "Form Submitted Successfully"
                        )
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@PrescriptionDisbributionActivity, "Unexpected error")
                }
            }
        }

        viewModel1.prescriptionDataById.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()){
                            val preOpDetails = it.data[0]
                            localFormId = preOpDetails._id
                            isFormLocal = true
                            canEdit = false
                            if (preOpDetails.isSyn == 1) {
                                binding.btnEdit.visibility = View.GONE
                                binding.cardViewSubmit.visibility = View.GONE
                            } else {
                                binding.btnEdit.visibility = View.VISIBLE
                                binding.cardViewSubmit.visibility = View.GONE
                            }
                            allowClickableEditText(false)
                            setUpFormData(preOpDetails)
                        }
                        else{
                            allowClickableEditText(true)
                            localFormId = 0
                            isFormLocal = false
                            canEdit = true
                            binding.btnEdit.visibility = View.GONE
                            binding.cardViewSubmit.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@PrescriptionDisbributionActivity, "Unexpected error")
                }
            }
        }
    }

    private fun setUpFormData(formData: PrescriptionGlassesFinal) {
        // Text fields
        binding.EditTextPostalAddressLine1.setText(formData.postal_addressline1)
        binding.EditTextPostalAddressLine2.setText(formData.postal_addressline2)
        binding.EditTextPostalCity.setText(formData.postal_city)
        binding.EditTextPostalCountry.setText(formData.postal_country)
        binding.EditTextPostalPincode.setText(formData.postal_pincode)
        binding.EditTextPostalState.setText(formData.postal_state)

        // Checkbox
        binding.CheckBoxSameAs.isChecked = formData.sameAddress

        // Radio buttons
        binding.RadioButtonPrescriptionSpectaclesGIVEN.isChecked = formData.spectacle_given
        binding.RadioButtonSpectaclesMATCHINGPrescription.isChecked = formData.spectacle_not_matching
        binding.RadioButtonOrderedButNOTRECEIVED.isChecked = formData.ordered_not_received
        binding.RadioButtonPatientCALLEDAGAIN.isChecked = formData.patient_call_again
        binding.RadioButtonPATIENTDIDNOTCOME.isChecked = formData.patient_not_come

        formData.spectacle_not_matching_details?.let {
            val adapter = binding.SpinnerNotMatchingDetails.adapter
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i).toString() == it) {
                    binding.SpinnerNotMatchingDetails.setSelection(i)
                    break
                }
            }
        }

        formData.given_presc_type?.let {
            val adapter = binding.SpinnerPrescType.adapter
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i).toString() == it) {
                    binding.SpinnerPrescType.setSelection(i)
                    break
                }
            }
        }

        binding.edittextCallAgain.setText(formData.call_again_date)
    }


    private fun allowClickableEditText(canEdit: Boolean) {
        binding.EditTextPostalAddressLine1.isEnabled = canEdit
        binding.EditTextPostalAddressLine2.isEnabled = canEdit
        binding.EditTextPostalCity.isEnabled = canEdit
        binding.EditTextPostalCountry.isEnabled = canEdit
        binding.EditTextPostalPincode.isEnabled = canEdit
        binding.EditTextPostalState.isEnabled = canEdit
        binding.CheckBoxSameAs.isEnabled = canEdit

        // Radios
        binding.RadioButtonPrescriptionSpectaclesGIVEN.isEnabled = canEdit
        binding.RadioButtonSpectaclesMATCHINGPrescription.isEnabled = canEdit
        binding.RadioButtonOrderedButNOTRECEIVED.isEnabled = canEdit
        binding.RadioButtonPatientCALLEDAGAIN.isEnabled = canEdit
        binding.RadioButtonPATIENTDIDNOTCOME.isEnabled = canEdit

        // Spinners
        binding.SpinnerNotMatchingDetails.isEnabled = canEdit
        binding.SpinnerPrescType.isEnabled = canEdit

        // Date field
        binding.edittextCallAgain.isEnabled = canEdit
    }

    private fun onFormEditClick() {
        canEdit = true
        binding.cardViewSubmit.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE
        binding.TextViewSubmit.text = "Update"
    }

    @SuppressLint("SuspiciousIndentation")
    private fun submitFinalPrecriptionGlass() {
        val alternate_mobile = ""
        val patient_call_again = binding.RadioButtonPatientCALLEDAGAIN.isChecked
        val patient_not_come = binding.RadioButtonPATIENTDIDNOTCOME.isChecked
        val postal_addressline1 = binding.EditTextPostalAddressLine1.text.toString()
        val postal_addressline2 = binding.EditTextPostalAddressLine2.text.toString()
        val postal_city = binding.EditTextPostalCity.text.toString()
        val postal_country = binding.EditTextPostalCountry.text.toString()
        val postal_pincode = binding.EditTextPostalPincode.text.toString()
        val postal_state = binding.EditTextPostalState.text.toString()
        val sameAddress = binding.CheckBoxSameAs.isChecked
        val spectacle_given = binding.RadioButtonPrescriptionSpectaclesGIVEN.isChecked
        val spectacle_not_matching = binding.RadioButtonSpectaclesMATCHINGPrescription.isChecked
        val call_again_date = binding.edittextCallAgain.text.toString()
        val ordered_not_received = binding.RadioButtonOrderedButNOTRECEIVED.isChecked
        val user_id = user_id
        val spectacle_not_matching_details =
            if (binding.SpinnerNotMatchingDetails.selectedItem != null)
                binding.SpinnerNotMatchingDetails.selectedItem.toString()
            else ""

        val PrescriptionGlassesFinal = PrescriptionGlassesFinal(
            localFormId,
            alternate_mobile,
            getCurrentDate(),
            call_again_date,
            campId.toString(),
            patientId.toString(),
            patient_not_come,
            postal_addressline1,
            postal_addressline2,
            postal_city,
            postal_country,
            postal_pincode,
            postal_state,
            sameAddress,
            spectacle_given,
            spectacle_not_matching,
            ordered_not_received,
            patient_call_again,
            spectacle_not_matching_details,
            selected_given_presc_type,
            user_id.toString(),
        )
        Log.d("xyz", "insertData : ${PrescriptionGlassesFinal.toString()}")
        viewModel1.insertFinalPrescriptionData(PrescriptionGlassesFinal)
        InsertFinalPrescriptionResponse()
    }

    private fun setPatientData() {
        intentDecodeText = sessionManager.getPatientData().toString()
        val gson = Gson()
        if (patientReportFormId != 0) {
            patientFname = intent.getStringExtra("patientFname") ?: ""
            patientLname = intent.getStringExtra("patientLname") ?: ""
            patientId = intent.getIntExtra("patientId", 0)
            patientAge = intent.getIntExtra("patientAge", 0)
            patientGender = intent.getStringExtra("patientGen") ?: ""
            campID = intent.getIntExtra("campId", 0)
            camp = intent.getStringExtra("location") ?: ""
            ageUnit = intent.getStringExtra("AgeUnit") ?: ""
        }
        viewModel1.getPostOpDetailsById(intentFormId, patientId)
        GetPatientRegistrationDetails(patientId)
        getPrescriptionDetails(patientId)
    }

    override fun onResume() {
        super.onResume()
        binding.LinearLayoutCallAgain.visibility = View.GONE
        binding.LinearLayoutAddress.visibility = View.GONE
        binding.LinearLayoutSpectacleNotMatching.visibility = View.GONE
        binding.LinearLayoutPrescriptionType.visibility = View.GONE

        NotMatchingArrayList = ArrayList()
        NotMatchingArrayList!!.add("Select")
        NotMatchingArrayList!!.add("Call again")
        NotMatchingArrayList!!.add("Send by post")
        customDropDownAdapter = CustomDropDownAdapter(this, NotMatchingArrayList!!)
        binding.SpinnerNotMatchingDetails!!.adapter = customDropDownAdapter

        SpectacleTypeArrayList = ArrayList()
        SpectacleTypeArrayList!!.add("Select")
        SpectacleTypeArrayList!!.add("Single Vision")
        SpectacleTypeArrayList!!.add("Single Vision (HP)")
        SpectacleTypeArrayList!!.add("Bifocal")
        SpectacleTypeArrayList!!.add("Bifocal (HP)")
        customDropDownAdapter = CustomDropDownAdapter(this, NotMatchingArrayList!!)
        binding.SpinnerNotMatchingDetails!!.adapter = customDropDownAdapter

        customDropDownAdapter = CustomDropDownAdapter(this, SpectacleTypeArrayList!!)
        binding.SpinnerPrescType!!.adapter = customDropDownAdapter

        binding.SpinnerNotMatchingDetails.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem =
                        NotMatchingArrayList?.get(position)

                    when (selectedItem) {
                        "Call again" -> {
                            binding.LinearLayoutCallAgain.visibility = View.VISIBLE
                            binding.LinearLayoutAddress.visibility = View.GONE
                            binding.LinearLayoutPrescriptionType.visibility = View.GONE
                        }

                        "Send by post" -> {
                            binding.LinearLayoutCallAgain.visibility = View.GONE
                            binding.LinearLayoutAddress.visibility = View.VISIBLE
                            binding.LinearLayoutPrescriptionType.visibility = View.VISIBLE
                        }

                        else -> {
                            binding.LinearLayoutCallAgain.visibility = View.GONE
                            binding.LinearLayoutAddress.visibility = View.GONE
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        binding.SpinnerPrescType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position) as? String
                    selected_given_presc_type = selectedItem.toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
    }

    private fun getViewModel() {
        val LLE_MedDocketRespository = LLE_MedDocketRespository()
        val LLE_MedDocketProviderFactory = LLE_MedDocketProviderFactory(LLE_MedDocketRespository, application)
        viewModel = ViewModelProvider(
            this,
            LLE_MedDocketProviderFactory
        ).get(LLE_MedDocketViewModel::class.java)
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage(getString(R.string.please_wait))
        }
        sessionManager = SessionManager(this)
    }

    private fun createRoomDatabase() {
        val database = LLE_MedDocket_Room_Database.getDatabase(this)

        val Vital_DAO: Vital_DAO = database.Vital_DAO()
        val VisualAcuity_DAO: VisualAcuity_DAO = database.VisualAcuity_DAO()
        val Refractive_Error_DAO: Refractive_Error_DAO = database.Refractive_Error_DAO()
        val OPD_Investigations_DAO: OPD_Investigations_DAO = database.OPD_Investigations_DAO()
        val Eye_Pre_Op_Notes_DAO: Eye_Pre_Op_Notes_DAO = database.Eye_Pre_Op_Notes_DAO()
        val Eye_Pre_Op_Investigation_DAO: Eye_Pre_Op_Investigation_DAO = database.Eye_Pre_Op_Investigation_DAO()
        val Eye_Post_Op_AND_Follow_ups_DAO: Eye_Post_Op_AND_Follow_ups_DAO = database.Eye_Post_Op_AND_Follow_ups_DAO()
        val Eye_OPD_Doctors_Note_DAO: Eye_OPD_Doctors_Note_DAO = database.Eye_OPD_Doctors_Note_DAO()
        val Cataract_Surgery_Notes_DAO: Cataract_Surgery_Notes_DAO = database.Cataract_Surgery_Notes_DAO()
        val Patient_DAO: PatientDao = database.PatientDao()
        val Image_Upload_DAO: Image_Upload_DAO = database.Image_Upload_DAO()
        val Registration_DAO: Registration_DAO = database.Registration_DAO()
        val Prescription_DAO: Prescription_DAO = database.Prescription_DAO()
        val SynTable_DAO: SynTable_DAO = database.SynTable_DAO()
        val Final_Prescription_DAO: Final_Prescription_DAO = database.Final_Prescription_DAO()
        val SpectacleDisdributionStatus_DAO: SpectacleDisdributionStatus_DAO = database.SpectacleDisdributionStatus_DAO()
        val CurrentInventory_DAO: CurrentInventory_DAO = database.CurrentInventory_DAO()
        val InventoryUnit_DAO: InventoryUnit_DAO = database.InventoryUnit_DAO()
        val CreatePrescriptionDAO: CreatePrescriptionDAO = database.CreatePrescriptionDAO()
        val Image_Prescription_DAO: Image_Prescription_DAO = database.Image_Prescription_DAO()
        val FinalPrescriptionDrugDAO: FinalPrescriptionDrugDAO = database.FinalPrescriptionDrugDAO()

        val repository = LLE_MedDocket_Repository(
            Vital_DAO,
            VisualAcuity_DAO,
            Refractive_Error_DAO,
            OPD_Investigations_DAO,
            Eye_Pre_Op_Notes_DAO,
            Eye_Pre_Op_Investigation_DAO,
            Eye_Post_Op_AND_Follow_ups_DAO,
            Eye_OPD_Doctors_Note_DAO,
            Cataract_Surgery_Notes_DAO,
            Patient_DAO,
            Image_Upload_DAO,
            Registration_DAO,
            Prescription_DAO,
            Final_Prescription_DAO,
            SpectacleDisdributionStatus_DAO,
            SynTable_DAO,
            CurrentInventory_DAO,
            InventoryUnit_DAO,
            CreatePrescriptionDAO,
            Image_Prescription_DAO,
            FinalPrescriptionDrugDAO,
            database
        )
        viewModel1 = ViewModelProvider(this, LLE_MedDocket_ViewModelFactory(repository)).get(
            LLE_MedDocket_ViewModel::class.java
        )
    }

    private fun GetPatientRegistrationDetails(patientID: Int) {
        progressDialog1 = ProgressDialog(this)
        progressDialog1.setMessage("Fetching Prescription Data...")
        progressDialog1.setCancelable(false)
        progressDialog1.show()
        viewModel1.getRegistrationByPatientId(patientID)
        getRegistrationResponse()
    }

    private fun getPrescriptionDetails(patientID: Int){
        viewModel1.getPrescriptionDataPatientID(patientID)
        getPrescriptionResponse()
    }

    private fun getPrescriptionResponse() {
        viewModel1.prescriptionData.observe(this, Observer { response ->
            Log.d("pawan", "getPrescriptionResponse => $response")
            progressDialog1.dismiss()

            if (response.isNotEmpty()) {
                val data = response[0]
                Log.d("pawan", "Prescription Data Found => PatientID=${data.patient_id}, CampID=${data.camp_id}")

                // DISTANCE (Right Eye)
                binding.TextViewDistanceRightSph.text = data.re_distant_vision_sphere_right
                binding.TextViewDistanceRightCyl.text = data.re_distant_vision_cylinder_right
                binding.TextViewDistanceRightAxis.text = data.re_distant_vision_axis_right

                // DISTANCE (Left Eye)
                binding.TextViewDistanceLeftSph.text = data.re_distant_vision_sphere_left
                binding.TextViewDistanceLeftCyl.text = data.re_distant_vision_cylinder_left
                binding.TextViewDistanceLeftAxis.text = data.re_distant_vision_axis_left

                // ADD AMOUNT (Right Eye)
                binding.TextViewAddAmountRightSph.text = data.re_reading_addition_right
                binding.TextViewAddAmountRightCyl.text = data.re_reading_addition_right_details
                binding.TextViewAddAmountRightAxis.text = data.re_remarks // or replace with correct field if available

                // ADD AMOUNT (Left Eye)
                binding.TextViewAddAmountLeftSph.text = data.re_reading_addition_left
                binding.TextViewAddAmountLeftCyl.text = data.re_reading_addition_left_details
                binding.TextViewAddAmountLeftAxis.text = data.re_remark_left // or correct field

                // TOTAL ADD (Right Eye)
                binding.TextViewTotalAmountRightSph.text = data.re_distant_vision_sphere_right
                binding.TextViewTotalAmountRightCyl.text = data.re_distant_vision_cylinder_right
                binding.TextViewTotalAmountRightAxis.text = data.re_distant_vision_axis_right

                // TOTAL ADD (Left Eye)
                binding.TextViewTotalAmountLeftSph.text = data.re_distant_vision_sphere_left
                binding.TextViewTotalAmountLeftCyl.text = data.re_distant_vision_cylinder_left
                binding.TextViewTotalAmountLeftAxis.text = data.re_distant_vision_axis_left

                // PD and BVD
                binding.TextViewPD.text = data.re_pupipllary_distance
                binding.TextViewBVD.text = data.re_bvd

                // Prism and Base (Right Eye)
                binding.TextViewPrismRight.text = data.re_prism_right
                binding.TextViewBaseRight.text = data.re_prism_unit_right

                // Prism and Base (Left Eye)
                binding.TextViewPrismLeft.text = data.re_prism_left
                binding.TextViewBaseLeft.text = data.re_prism_unit_left

                Log.d("pawan", """
                Prescription Values:
                - Distance Right: SPH=${data.re_distant_vision_sphere_right}, CYL=${data.re_distant_vision_cylinder_right}, AXIS=${data.re_distant_vision_axis_right}
                - Distance Left:  SPH=${data.re_distant_vision_sphere_left}, CYL=${data.re_distant_vision_cylinder_left}, AXIS=${data.re_distant_vision_axis_left}
                - Add Right: SPH=${data.re_reading_addition_right}, CYL=${data.re_reading_addition_right_details}
                - Add Left:  SPH=${data.re_reading_addition_left}, CYL=${data.re_reading_addition_left_details}
                - PD=${data.re_pupipllary_distance}, BVD=${data.re_bvd}
                - Prism Right=${data.re_prism_right} (${data.re_prism_unit_right})
                - Prism Left=${data.re_prism_left} (${data.re_prism_unit_left})
            """.trimIndent())

            } else {
                Log.e("pawan", "getPrescriptionResponse => No prescription data found!")
                finish()
                Toast.makeText(this,"No prescription data found for this patient",Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun getRegistrationResponse() {
        viewModel1.registration.observe(this, Observer { response ->
            progressDialog1.dismiss()
            Log.d("xyz", "getRegistrationResponse => $response")

            if (response != null && response.isNotEmpty()) {
                val data = response[0]
                binding.EditTextResidenceCity.setText(data.citytownvillage)
                binding.EditTextResidenceCountry.setText("India")
                binding.EditTextResidenceState.setText(data.statename)
                binding.EditTextResidencePincode.setText(data.pincode)
                binding.EditTextResidenceAddressLine1.setText(data.houseno)
                binding.EditTextResidenceAddressLine2.setText(data.localityareapada)
                binding.EditTextResidenceTaluka.setText(data.taluka)
                binding.EditTextMobileNumber.setText(data.mobileno)
                binding.TextViewPatientName.text = "${data.fname} ${data.mname} ${data.lname}"
                binding.edtCampLoc.text = "Camp ID :- ${data.campid}"
                binding.edtPatientName.setText("Name :- ${data.fname} ${data.lname}")
                binding.edtAge.setText("Age :- ${data.age} ${data.ageunit}")
                binding.edtId.text = "Patient ID :- ${data.patient_id}"
                binding.edtGend.setText("Gender :- ${data.gender}")

                // Assigning to variables
                campId = data.campid
                patientId = data.patient_id
                patientFname = data.fname
                patientLname = data.lname
                patientGender = data.gender
                patientAge = data.age.toIntOrNull() ?: 0
                ageUnit = data.ageunit
                camp = data.citytownvillage

            } else {
                // ❌ No registration data found
                Log.e("xyz", "getRegistrationResponse => This patient is not registered")
                finish()
                Toast.makeText(this,"This patient is not registered",Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView) {
            binding.CheckBoxSameAs -> {
                if (isChecked) {
                    viewModel1.allRegistration.observe(this, Observer { response ->
                        for (data in response) {
                            if (patientId == data.patient_id) {
                                Log.d(ConstantsApp.TAG, "" + data.citytownvillage)
                                binding.EditTextPostalCity.setText(data.citytownvillage)
                                binding.EditTextPostalCountry.setText("India")
                                binding.EditTextPostalState.setText(data.statename)
                                binding.EditTextPostalPincode.setText(data.pincode)
                                binding.EditTextPostalAddressLine1.setText(data.houseno)
                                binding.EditTextPostalAddressLine2.setText(data.localityareapada)
                            }
                        }
                    })
                } else {
                    binding.EditTextPostalCity.setText("")
                    binding.EditTextPostalCountry.setText("")
                    binding.EditTextPostalState.setText("")
                    binding.EditTextPostalPincode.setText("")
                    binding.EditTextPostalAddressLine1.setText("")
                    binding.EditTextPostalAddressLine2.setText("")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.cardViewSubmit -> {
                val checkedRadioButtonId = binding.RadioGroup.checkedRadioButtonId
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(this, "Please select a Radio Button", Toast.LENGTH_SHORT).show()
                } else {
                    processSelectedText(selected_Text)
                }
            }

            binding.TextViewSubmit -> {
            }

            binding.edittextCallAgain -> {
                showDatePickerDialog(binding.edittextCallAgain)
            }
        }
    }

    private fun InsertFinalPrescriptionResponse() {
        viewModel1.toastMessage.observe(this, Observer { message ->
            showToast(message)
            onBackPressed()
            sessionManager.clearCache(this@PrescriptionDisbributionActivity)
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.RadioButtonPrescription_spectacles_GIVEN -> {
                selected_Text = binding.RadioButtonPrescriptionSpectaclesGIVEN.text.toString()
                spectacle_given = true
                binding.LinearLayoutCallAgain.visibility = View.GONE
                binding.LinearLayoutAddress.visibility = View.GONE
                binding.LinearLayoutSpectacleNotMatching.visibility = View.GONE
                binding.LinearLayoutPrescriptionType.visibility = View.VISIBLE
                binding.CheckBoxSameAs.isChecked = false
                binding.SpinnerNotMatchingDetails.setSelection(0)
                customDropDownAdapter.notifyDataSetChanged()
            }

            R.id.RadioButton_PATIENT_DID_NOT_COME -> {
                selected_Text = binding.RadioButtonPATIENTDIDNOTCOME.text.toString()
                patient_not_come = true
                binding.LinearLayoutCallAgain.visibility = View.GONE
                binding.LinearLayoutAddress.visibility = View.VISIBLE
                binding.LinearLayoutSpectacleNotMatching.visibility = View.GONE
                binding.LinearLayoutPrescriptionType.visibility = View.VISIBLE
                binding.CheckBoxSameAs.isChecked = true
                binding.SpinnerNotMatchingDetails.setSelection(0)
                customDropDownAdapter.notifyDataSetChanged()
            }

            R.id.RadioButton_spectacles_MATCHING_prescription -> {
                selected_Text = binding.RadioButtonSpectaclesMATCHINGPrescription.text.toString()
                spectacle_not_matching = true
                binding.LinearLayoutCallAgain.visibility = View.GONE
                binding.LinearLayoutAddress.visibility = View.GONE
                binding.LinearLayoutSpectacleNotMatching.visibility = View.VISIBLE
                binding.LinearLayoutPrescriptionType.visibility = View.GONE
                binding.SpinnerNotMatchingDetails.setSelection(0)
                customDropDownAdapter.notifyDataSetChanged()
                binding.CheckBoxSameAs.isChecked = false
                binding.TextViewCallAgain.text = "Prescription spectacles NOT GIVEN as received spectacles were NOT MATCHING prescription"
            }

            R.id.RadioButton_ordered_but_NOT_RECEIVED -> {
                selected_Text = binding.RadioButtonOrderedButNOTRECEIVED.text.toString()
                ordered_not_received = true
                binding.LinearLayoutCallAgain.visibility = View.GONE
                binding.LinearLayoutAddress.visibility = View.GONE
                binding.LinearLayoutSpectacleNotMatching.visibility = View.GONE
                binding.LinearLayoutPrescriptionType.visibility = View.GONE
                binding.SpinnerNotMatchingDetails.setSelection(0)
                binding.CheckBoxSameAs.isChecked = false
                customDropDownAdapter.notifyDataSetChanged()
            }

            R.id.RadioButton_patient_CALLED_AGAIN -> {
                selected_Text = binding.RadioButtonPatientCALLEDAGAIN.text.toString()
                patient_call_again = true
                binding.LinearLayoutCallAgain.visibility = View.VISIBLE
                binding.LinearLayoutAddress.visibility = View.GONE
                binding.LinearLayoutSpectacleNotMatching.visibility = View.GONE
                binding.LinearLayoutPrescriptionType.visibility = View.GONE
                binding.TextViewCallAgain.text = "Spectacles NOT YET RECEIVED hence patient CALLED AGAIN on"
                binding.CheckBoxSameAs.isChecked = false
                binding.SpinnerNotMatchingDetails.setSelection(0)
                customDropDownAdapter.notifyDataSetChanged()
            }
        }
    }


    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format(
                    Locale.getDefault(),
                    "%02d-%02d-%04d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )
                editText.setText(selectedDate)
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

    fun processSelectedText(selectedText: String) {
        Log.d(ConstantsApp.TAG, "selectedText in processSelectedText=>" + selectedText)
        when (selectedText.trim()) {
            "Prescription spectacles GIVEN to the patient" -> {
                if (validation3()) {
                    submitFinalPrecriptionGlass()
                }
            }

            "Spectacles NOT YET RECEIVED hence patient CALLED AGAIN" -> {
                if (validation()) {
                    submitFinalPrecriptionGlass()
                }
            }

            "Prescription spectacles NOT GIVEN as received spectacles were NOT MATCHING prescription" -> {
                if (validation1()) {
                    Log.d(ConstantsApp.TAG, "if")
                    val spinnerSelectedText =
                        binding.SpinnerNotMatchingDetails.selectedItem.toString()
                    Log.d(ConstantsApp.TAG, "spinnerSelectedText=>" + spinnerSelectedText)
                    when (spinnerSelectedText) {
                        "Call again" -> {
                            if (validation1()) {
                                submitFinalPrecriptionGlass()
                            }
                        }
                        "Send by post" -> {
                            if (validation2()) {
                                submitFinalPrecriptionGlass()
                            }
                        }
                    }
                } else {
                    Log.d(ConstantsApp.TAG, "else")
                    val spinnerSelectedText =
                        binding.SpinnerNotMatchingDetails.selectedItem.toString()
                    Log.d(ConstantsApp.TAG, "spinnerSelectedText=>" + spinnerSelectedText)
                    when (spinnerSelectedText) {
                        "Call again" -> {
                            if (validation1()) {
                                submitFinalPrecriptionGlass()
                            }
                        }
                        "Send by post" -> {
                            if (validation2()) {
                                submitFinalPrecriptionGlass()
                            }

                        }
                    }
                }
            }

            "Prescription spectacles NOT GIVEN as spectacles ordered but NOT RECEIVED" -> {
                submitFinalPrecriptionGlass()
            }

            "Prescription spectacles POSTED as PATIENT DID NOT COME" -> {
                if (validation3()) {
                    submitFinalPrecriptionGlass()
                }
            }

            else -> {}
        }
    }

    private fun validation(): Boolean {
        var flag = true
        if (!MyValidator.isValidField1(binding.edittextCallAgain)) {
            flag = false
        }
        return flag
    }

    private fun validation1(): Boolean {
        var flag = true
        if (!MyValidator.isValidField1(binding.edittextCallAgain)) {
            flag = false
        }
        if (!MyValidator.isValidSpinner17(
                binding.SpinnerNotMatchingDetails,
                "Select something",
                this
            )
        ) {
            flag = false
        }
        return flag
    }

    private fun validation2(): Boolean {
        var flag = true
        if (!MyValidator.isValidField1(binding.EditTextPostalAddressLine1)) {
            flag = false
        }
        if (!MyValidator.isValidField1(binding.EditTextPostalAddressLine2)) {
            flag = false
        }
        if (!MyValidator.isValidField1(binding.EditTextPostalCity)) {
            flag = false
        }
        if (!MyValidator.isValidField1(binding.EditTextPostalState)) {
            flag = false
        }
        if (!MyValidator.isValidField1(binding.EditTextPostalCountry)) {
            flag = false
        }
        if (!MyValidator.isValidField1(binding.EditTextPostalPincode)) {
            flag = false
        }
        if (!MyValidator.isValidSpinner17(binding.SpinnerPrescType, "Select Prescription Type", this)) {
            flag = false
        }
        if (!MyValidator.isValidSpinner17(binding.SpinnerNotMatchingDetails, "Select something", this)) {
            flag = false
        }
        return flag
    }

    private fun validation3(): Boolean {
        var flag = true
        if (!MyValidator.isValidSpinner17(binding.SpinnerPrescType, "Select Prescription Type", this)) {
            flag = false
        }
        return flag
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}