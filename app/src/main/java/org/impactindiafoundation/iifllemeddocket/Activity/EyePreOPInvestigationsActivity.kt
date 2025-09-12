package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Adapter.Add_AddSymptomsArrayList_Adapter
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Investigation
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.AddSymptomsModel
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityEyePreOpInvestigationsBinding

class EyePreOPInvestigationsActivity:AppCompatActivity(), View.OnClickListener {

    lateinit var binding:ActivityEyePreOpInvestigationsBinding
    lateinit var customDropDownAdapter: CustomDropDownAdapter

    var ConclusionArrayList:ArrayList<String>?=null
    var UnitArrayList:ArrayList<String>?=null
    var IOLPowerArrayList:ArrayList<String>?=null
    var SlitLampEyeArrayList:ArrayList<String>?=null
    var SlitLampLocationArrayList:ArrayList<String>?=null
    var AddSliteLampArrayList:ArrayList<AddSymptomsModel>?=null

    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager

    var patientAge:String?=null
    var patientGender:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEyePreOpInvestigationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply padding to the activity content (this handles all root layouts properly)
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

        binding.toolbarEyePreOPInvestigation.toolbar.title="Eye Pre-Op Investigations"
        getViewModel()
        createRoomDatabase()

        initView()

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

    private fun init()
    {
        ConclusionArrayList= ArrayList()
        ConclusionArrayList!!.add("Select")
        ConclusionArrayList!!.add("Negative")
        ConclusionArrayList!!.add("Positive")

        UnitArrayList= ArrayList()
        UnitArrayList!!.add("Select")
        UnitArrayList!!.add("dp")
        UnitArrayList!!.add("mm")

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

        SlitLampEyeArrayList= ArrayList()
        SlitLampEyeArrayList!!.add("Select Eye")
        SlitLampEyeArrayList!!.add("Right")
        SlitLampEyeArrayList!!.add("Left")
        SlitLampEyeArrayList!!.add("Both")

        SlitLampLocationArrayList= ArrayList()
        SlitLampLocationArrayList!!.add("Select Location")
        SlitLampLocationArrayList!!.add("Eye Lid")
        SlitLampLocationArrayList!!.add("Lacrimal Gland")
        SlitLampLocationArrayList!!.add("Lacrimal Duct")
        SlitLampLocationArrayList!!.add("Cornea")
        SlitLampLocationArrayList!!.add("Conjuntiva")
        SlitLampLocationArrayList!!.add("Fundus")
        SlitLampLocationArrayList!!.add("Lens")
        SlitLampLocationArrayList!!.add("Iris")
        SlitLampLocationArrayList!!.add("Others")

        AddSliteLampArrayList= ArrayList()

        binding.LinearLayoutSlitLampOther.visibility=View.GONE
        binding.RecyclerViewSliteLamp.visibility=View.GONE
        binding.LinearLayoutSlitLampRecycleview.visibility=View.GONE


        customDropDownAdapter=CustomDropDownAdapter(this,ConclusionArrayList!!)
        binding.SpinnerHIV.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,ConclusionArrayList!!)
        binding.SpinnerHBsAg.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,ConclusionArrayList!!)
        binding.SpinnerHCV.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,UnitArrayList!!)
        binding.SpinnerHorizontalAxisRightEye.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,UnitArrayList!!)
        binding.SpinnerHorizontalAxisLeftEye.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,UnitArrayList!!)
        binding.SpinnerVerticalAxisRightEye.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,UnitArrayList!!)
        binding.SpinnerVerticalAxisLeftEye.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,UnitArrayList!!)
        binding.SpinnerAverageValueRightEye.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,UnitArrayList!!)
        binding.SpinnerAverageValueLeftEye.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,IOLPowerArrayList!!)
        binding.spinnerIOLPower.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,SlitLampEyeArrayList!!)
        binding.SpinnerSliteLampEye.adapter=customDropDownAdapter

        customDropDownAdapter=CustomDropDownAdapter(this,SlitLampLocationArrayList!!)
        binding.SpinnerSliteLampLocation.adapter=customDropDownAdapter

        binding.cardViewAdd.setOnClickListener(this)
        binding.cardViewEyePreOpInvestigationsSubmit.setOnClickListener(this)


        binding.EditTextHaemoglobin.addTextChangedListener(createTextWatcher(binding.EditTextHaemoglobin))
        binding.EditTextBPDiastolic.addTextChangedListener(createTextWatcher(binding.EditTextBPDiastolic))

        binding.SpinnerSliteLampLocation.onItemSelectedListener=object:AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                handleSpinnerSelection(binding.SpinnerSliteLampLocation, binding.LinearLayoutSlitLampOther,binding.EditTextSliteLampLocationDetails)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    override fun onClick(v: View?) {
        when(v)
        {
            binding.cardViewAdd->
            {
                var selected_eye=binding.SpinnerSliteLampEye.selectedItem.toString()
                var selected_eye_location=binding.SpinnerSliteLampLocation.selectedItem.toString()
                var selected_location_details=binding.EditTextSliteLampLocationDetails.text.toString()
                var other_selected_location_details=binding.EditTextOtherDetails.text.toString()+""+binding.EditTextLocationOtherDetails.text.toString()

                val resultDetails: String = when (selected_eye_location) {
                    "Others" -> other_selected_location_details
                    else -> selected_location_details
                }

                add_SliteLamp(selected_eye!!,selected_eye_location,resultDetails)
            }
            binding.cardViewEyePreOpInvestigationsSubmit->
            {
                val (patientId, campId, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
                val current_Date= ConstantsApp.getCurrentDate()
                val opd_eye_blood_pressure_diastolic=binding.EditTextBPDiastolic.text.toString()
                val opd_eye_blood_pressure_systolic=binding.EditTextBPSystolic.text.toString()
                val opd_eye_blood_pressure_interpretation=binding.TextViewBPInterpretation.text.toString()

                val opd_eye_blood_sugar_fasting=binding.EditTextBSF.text.toString()
                val opd_eye_blood_sugar_interpretation=binding.TextViewSugarInterpretation.text.toString()
                val opd_eye_blood_sugar_pp=binding.EditTextPP.text.toString()

                val opd_eye_haemoglobin=binding.EditTextHaemoglobin.text.toString()
                val opd_eye_haemoglobin_interpretation=binding.textViewHBInterpretation.text.toString()

                val opd_eye_cbc=binding.EditTextCBC.text.toString()
                val opd_eye_pt=binding.EditTextProthrombinTime.text.toString()
                val opd_eye_bt=binding.EditTextBleedingTime.text.toString()

                val opd_eye_hiv=binding.SpinnerHIV.selectedItem.toString()
                val opd_eye_hbsag=binding.SpinnerHBsAg.selectedItem.toString()
                val opd_eye_hcv=binding.SpinnerHCV.selectedItem.toString()
                val opd_eye_ecg=binding.EditTextECG.text.toString()
                val opd_eye_iop_left=binding.EditTextIOPLeftEye.text.toString()
                val opd_eye_iop_right=binding.EditTextIOPRightEye.text.toString()

                val opd_eye_ha_left=binding.EditTextHorizontalAxisRightEye.text.toString()
                val opd_eye_ha_left_unit=binding.SpinnerHorizontalAxisRightEye.selectedItem.toString()
                val opd_eye_ha_right=binding.EditTextHorizontalAxisLeftEye.text.toString()
                val opd_eye_ha_right_unit=binding.SpinnerHorizontalAxisLeftEye.selectedItem.toString()

                val opd_eye_va_left=binding.EditTextVerticalAxisLeftEye.text.toString()
                val opd_eye_va_left_unit=binding.SpinnerVerticalAxisLeftEye.selectedItem.toString()
                val opd_eye_va_right=binding.EditTextVerticalAxisRightEye.text.toString()
                val opd_eye_va_right_unit=binding.SpinnerVerticalAxisRightEye.selectedItem.toString()

                val opd_eye_av_left=binding.EditTextAverageValueLeftEye.text.toString()
                val opd_eye_av_left_unit=binding.SpinnerAverageValueLeftEye.selectedItem.toString()
                val opd_eye_av_right=binding.EditTextAverageValueRightEye.text.toString()
                val opd_eye_av_right_unit=binding.SpinnerAverageValueRightEye.selectedItem.toString()


                //
                val opd_eye_fa_left=binding.EditTextAScan1ValueLeftEye.text.toString()
                val opd_eye_fa_right=binding.EditTextAScan1ValueRightEye.text.toString()

                val opd_eye_sv_left=binding.EditTextAScan2ValueLeftEye.text.toString()
                val opd_eye_sv_right=binding.EditTextAScan2ValueRightEye.text.toString()

                val opd_eye_tv_left=binding.EditTextAScan3ValueLeftEye.text.toString()
                val opd_eye_tv_right=binding.EditTextAScan3ValueRightEye.text.toString()

                val opd_eye_mv_left=binding.EditTextAScanMedianValueLeftEye.text.toString()
                val opd_eye_mv_right=binding.EditTextAScanMedianValueRightEye.text.toString()
                val opd_eye_iol_power=binding.spinnerIOLPower.selectedItem.toString()
                val opd_eye_ct=binding.EditTextClottingTime.text.toString()

                val formattedStringSymptoms = AddSliteLampArrayList!!.joinToString { model ->
                    "${model.selected_symptoms} = ${model.selected_eye}"
                }

                val opd_eye_slit_location = "{$formattedStringSymptoms}"

                val formattedStringSymptomsDetails = AddSliteLampArrayList!!.joinToString { model ->
                    "${model.selected_symptoms} = ${model.selected_eye_symptoms_details}"
                }

                val opd_eye_slit_location_description = "{$formattedStringSymptomsDetails}"





                SubmitEyePreOPInvestigations(
                    campId!!,
                    current_Date,
                    opd_eye_av_left,
                    opd_eye_av_left_unit,
                    opd_eye_av_right,
                    opd_eye_av_right_unit,
                    opd_eye_blood_pressure_diastolic,
                    opd_eye_blood_pressure_interpretation,
                    opd_eye_blood_pressure_systolic,
                    opd_eye_blood_sugar_fasting,
                    opd_eye_blood_sugar_interpretation,
                    opd_eye_blood_sugar_pp,
                    opd_eye_bt,
                    opd_eye_ct,
                    opd_eye_cbc,
                    opd_eye_ecg,
                    opd_eye_fa_left,
                    opd_eye_fa_right,
                    opd_eye_ha_left,
                    opd_eye_ha_left_unit,
                    opd_eye_ha_right,
                    opd_eye_ha_right_unit,
                    opd_eye_haemoglobin,
                    opd_eye_haemoglobin_interpretation,
                    opd_eye_hbsag,
                    opd_eye_hcv,
                    opd_eye_hiv,
                    opd_eye_iol_power,
                    opd_eye_iop_left,
                    opd_eye_iop_right,
                    opd_eye_mv_left,
                    opd_eye_mv_right,
                    opd_eye_pt,
                    opd_eye_slit_location,
                    opd_eye_slit_location_description,
                    opd_eye_sv_left,
                    opd_eye_sv_right,
                    opd_eye_tv_left,
                    opd_eye_tv_right,
                    opd_eye_va_left,
                    opd_eye_va_left_unit,
                    opd_eye_va_right,
                    opd_eye_va_right_unit,
                    patientId!!,
                    userId!!
                )

            }
        }
    }

    private fun SubmitEyePreOPInvestigations(

        camp_id: Int,
        createdDate: String,
        opd_eye_av_left: String,
        opd_eye_av_left_unit: String,
        opd_eye_av_right: String,
        opd_eye_av_right_unit: String,

        opd_eye_blood_pressure_diastolic: String,
        opd_eye_blood_pressure_interpretation: String,
        opd_eye_blood_pressure_systolic: String,

        opd_eye_blood_sugar_fasting: String,
        opd_eye_blood_sugar_interpretation: String,
        opd_eye_blood_sugar_pp: String,

        opd_eye_bt: String,
        opd_eye_ct: String,
        opd_eye_cbc: String,
        opd_eye_ecg: String,
        opd_eye_fa_left: String,
        opd_eye_fa_right: String,
        opd_eye_ha_left: String,
        opd_eye_ha_left_unit: String,
        opd_eye_ha_right: String,
        opd_eye_ha_right_unit: String,

        opd_eye_haemoglobin: String,
        opd_eye_haemoglobin_interpretation: String,

        opd_eye_hbsag: String,
        opd_eye_hcv: String,
        opd_eye_hiv: String,
        opd_eye_iol_power: String,
        opd_eye_iop_left: String,
        opd_eye_iop_right: String,
        opd_eye_mv_left: String,
        opd_eye_mv_right: String,
        opd_eye_pt: String,
        opd_eye_slit_location: String,
        opd_eye_slit_location_description: String,
        opd_eye_sv_left: String,
        opd_eye_sv_right: String,
        opd_eye_tv_left: String,
        opd_eye_tv_right: String,
        opd_eye_va_left: String,
        opd_eye_va_left_unit: String,
        opd_eye_va_right: String,
        opd_eye_va_right_unit: String,
        patient_id: Int,
        user_id: String,
    ) {
        val eyePreOpInvestigation=Eye_Pre_Op_Investigation(0, camp_id!!,
            createdDate,
            opd_eye_av_left,
            opd_eye_av_left_unit,
            opd_eye_av_right,
            opd_eye_av_right_unit,
            opd_eye_blood_pressure_diastolic,
            opd_eye_blood_pressure_interpretation,
            opd_eye_blood_pressure_systolic,
            opd_eye_blood_sugar_fasting,
            opd_eye_blood_sugar_interpretation,
            opd_eye_blood_sugar_pp,
            opd_eye_bt,
            opd_eye_ct,
            opd_eye_cbc,
            opd_eye_ecg,
            opd_eye_fa_left,
            opd_eye_fa_right,
            opd_eye_ha_left,
            opd_eye_ha_left_unit,
            opd_eye_ha_right,
            opd_eye_ha_right_unit,
            opd_eye_haemoglobin,
            opd_eye_haemoglobin_interpretation,
            opd_eye_hbsag,
            opd_eye_hcv,
            opd_eye_hiv,
            opd_eye_iol_power,
            opd_eye_iop_left,
            opd_eye_iop_right,
            opd_eye_mv_left,
            opd_eye_mv_right,
            opd_eye_pt,
            opd_eye_slit_location,
            opd_eye_slit_location_description,
            opd_eye_sv_left,
            opd_eye_sv_right,
            opd_eye_tv_left,
            opd_eye_tv_right,
            opd_eye_va_left,
            opd_eye_va_left_unit,
            opd_eye_va_right,
            opd_eye_va_right_unit,
           patient_id,
            user_id
        )

        Log.d("pawan", "insert pre op investigation ${eyePreOpInvestigation}")

        viewModel1.insertEyePre_OP_Investigations(eyePreOpInvestigation)
        InsertEyePre_OP_InvestigationsResponse()

    }

    private fun InsertEyePre_OP_InvestigationsResponse() {
        viewModel1.toastMessage.observe(this
            , Observer { message ->
                showToast(message)
                gotoScreen(this,PatientForms::class.java)
            })
    }

    private fun add_SliteLamp(selectedEye: String, selectedEyeLocation: String, selectedLocationDetails: String) {

        if (selectedEye == "Select Eye") {
            showToast("Please select eye")
        }
        else if (selectedEyeLocation == "Select Location") {
            showToast("Please select Location")
        } else {
            val isSymptomExists = AddSliteLampArrayList?.any {
                it.selected_symptoms == selectedEyeLocation
            } ?: false

            if (isSymptomExists) {
                //showToast("Symptom already selected")
                val existingIndex = AddSliteLampArrayList?.indexOfFirst {
                    it.selected_symptoms == selectedEyeLocation
                } ?: -1

                if (existingIndex != -1) {
                    updateSliteLamp(existingIndex, selectedEye, selectedEyeLocation, selectedLocationDetails,binding.RecyclerViewSliteLamp,AddSliteLampArrayList!!,binding.LinearLayoutSlitLampRecycleview)
                } else {
                    addNewSliteLamp(selectedEye, selectedEyeLocation, selectedLocationDetails,binding.RecyclerViewSliteLamp,AddSliteLampArrayList!!,binding.LinearLayoutSlitLampRecycleview)
                }
            } else {

                addNewSliteLamp(selectedEye, selectedEyeLocation, selectedLocationDetails,binding.RecyclerViewSliteLamp,AddSliteLampArrayList!!,binding.LinearLayoutSlitLampRecycleview)

            }
        }

    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun addNewSliteLamp(selected_eye: String, selected_symptoms: String, selected_eye_symptoms_details: String, recyclerView: RecyclerView, arrayList:ArrayList<AddSymptomsModel>, layout: LinearLayout) {

        recyclerView.visibility=View.VISIBLE
        layout.visibility=View.VISIBLE
        arrayList?.add(
            AddSymptomsModel(
                selected_eye,
                selected_symptoms,
                selected_eye_symptoms_details
            )
        )
        recyclerView.adapter =
            Add_AddSymptomsArrayList_Adapter(this, arrayList!!)

        val dayWisePosition = ( binding.SpinnerSliteLampEye.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Eye")
        binding.SpinnerSliteLampEye.setSelection(dayWisePosition!!)

        val dayWisePosition1 = ( binding.SpinnerSliteLampLocation.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Location")
        binding.SpinnerSliteLampLocation.setSelection(dayWisePosition1!!)
        binding.EditTextSliteLampLocationDetails.setText(null)
    }

    private fun updateSliteLamp(index: Int, selected_eye: String, selected_symptoms: String, selected_eye_symptoms_details: String,recyclerView: RecyclerView,arrayList:ArrayList<AddSymptomsModel>,layout: LinearLayout) {
        recyclerView.visibility=View.VISIBLE
        layout.visibility=View.VISIBLE

        arrayList?.get(index)?.let { existingItem ->
            val updatedItem = existingItem.copy(
                selected_eye = selected_eye,
                selected_symptoms = selected_symptoms,
                selected_eye_symptoms_details = selected_eye_symptoms_details
            )

            // Replace the old instance with the updated one
            arrayList?.set(index, updatedItem)

            // Notify the adapter that the data has changed
            recyclerView.adapter?.notifyItemChanged(index)


            val dayWisePosition = ( binding.SpinnerSliteLampEye.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Eye")
            binding.SpinnerSliteLampEye.setSelection(dayWisePosition!!)

            val dayWisePosition1 = ( binding.SpinnerSliteLampLocation.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Location")
            binding.SpinnerSliteLampLocation.setSelection(dayWisePosition1!!)
            binding.EditTextSliteLampLocationDetails.setText(null)
            //showToast("Symptom data updated")
        }
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.RecyclerViewSliteLamp!!.layoutManager = layoutManager
        binding.RecyclerViewSliteLamp!!.setHasFixedSize(true)
    }

    private fun createTextWatcher(editText: EditText): TextWatcher? {

        return object : TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val text = s.toString()
                // Here, you can identify which EditText has changed by checking the ID or some other criteria
                when (editText) {

                    binding.EditTextHaemoglobin -> {

                        val decodedText=sessionManager.getPatientData()
                        val gson = Gson()
                        val patientData = gson.fromJson(decodedText, PatientData::class.java)
                        patientAge= patientData.patientAge.toString()
                        patientGender=patientData.patientGen

                        val hb=binding.EditTextHaemoglobin.text.toString()
                        interpretHemoglobin(hb, patientGender!!,patientAge!!)

                    }
                    binding.EditTextBPDiastolic->
                    {
                        setBloodPressure()
                    }
                    // Add more cases if needed
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                // Here, you can identify which EditText has changed by checking the ID or some other criteria
                when (editText) {

                    binding.EditTextHaemoglobin -> {

                        val hb=binding.EditTextHaemoglobin.text.toString()
                        interpretHemoglobin(hb, patientGender!!,patientAge!!)

                    }
                    binding.EditTextBPDiastolic->
                    {
                        setBloodPressure()
                    }
                    // Add more cases if needed
                }
            }

            override fun afterTextChanged(s: Editable?) {


                val text = s.toString()
                // Here, you can identify which EditText has changed by checking the ID or some other criteria
                when (editText) {

                    binding.EditTextHaemoglobin -> {
                        val hb=binding.EditTextHaemoglobin.text.toString()
                        interpretHemoglobin(hb, patientGender!!,patientAge!!)

                    }
                    binding.EditTextBPDiastolic->
                    {
                        setBloodPressure()
                    }
                    // Add more cases if needed
                }
            }

        }

    }

    private fun interpretHemoglobin(input: String, gender: String, ageInput: String) {
        val hbLevel = input.toDoubleOrNull()
        val age = ageInput.toIntOrNull()

        hbLevel?.let {
            age?.let { a ->
                val interpretation = when {
                    gender == "Male" -> interpretMaleHemoglobin(it, a)
                    gender == "Female" -> interpretFemaleHemoglobin(it, a)
                    else -> "Not Defined"
                }

                // Set interpretation text and color in the TextView
                binding.textViewHBInterpretation.text = interpretation
                setColorBasedOnHBInterpretation(interpretation)

            } ?: run {
                // If age input is empty, clear the TextView
                binding.textViewHBInterpretation.text = ""
            }
        } ?: run {
            // If hemoglobin input is empty, clear the TextView
            binding.textViewHBInterpretation.text = ""
        }
    }

    private fun interpretMaleHemoglobin(hbLevel: Double, age: Int): String {
        return when {
            age in 6..59 && hbLevel in 10.0..10.9 -> "Mild Anemia (Male)"
            age in 5..11 && hbLevel in 11.0..11.4 -> "Mild Anemia (Male)"
            age in 12..14 && hbLevel in 11.0..11.9 -> "Mild Anemia (Male)"
            age > 14 && hbLevel in 11.0..12.9 -> "Mild Anemia (Male)"
            else -> interpretGeneralHemoglobin(hbLevel)
        }
    }

    private fun interpretFemaleHemoglobin(hbLevel: Double, age: Int): String {
        return when {
            age in 6..59 && hbLevel in 10.0..10.9 -> "Mild Anemia (Female)"
            age in 5..11 && hbLevel in 11.0..11.4 -> "Mild Anemia (Female)"
            age in 12..14 && hbLevel in 11.0..11.9 -> "Mild Anemia (Female)"
            age > 14 && hbLevel in 11.0..11.9 -> "Mild Anemia (Female)"
            else -> interpretGeneralHemoglobin(hbLevel)
        }
    }

    private fun interpretGeneralHemoglobin(hbLevel: Double): String {
        return when {
            hbLevel < 8 -> "Severe Anemia"
            hbLevel in 8.0..10.9 -> "Moderate Anemia"
            hbLevel in 11.0..11.9 -> "Mild Anemia"
            hbLevel >= 12.0 -> "Normal"
            else -> "Not Defined"
        }
    }

    private fun setColorBasedOnHBInterpretation(interpretation: String) {
        val color = when {
            interpretation.contains("Severe Anemia") -> Color.BLUE
            interpretation.contains("Moderate Anemia") -> Color.RED
            interpretation.contains("Mild Anemia", ignoreCase = true)
                    || interpretation.contains("Normal", ignoreCase = true) -> Color.BLACK
            else -> Color.BLACK
        }

        binding.textViewHBInterpretation.setTextColor(color)

    }

    private fun handleSpinnerSelection(
        spinner: Spinner,
        layout: LinearLayout,
        editText:EditText
    ) {

        val selectedValue = spinner.selectedItem.toString()

        // Check if the selected value is "Others"
        if (selectedValue == "Others") {
            layout.visibility = View.VISIBLE
            editText.visibility=View.GONE
        } else {
            layout.visibility = View.GONE
            editText.visibility=View.VISIBLE
        }
    }

    private fun setBloodPressure()
    {
        val systolic = binding.EditTextBPSystolic.text.toString().toIntOrNull() ?: 0
        val diastolic = binding.EditTextBPDiastolic.text.toString().toIntOrNull() ?: 0

        val bloodPressureInfo = getBloodPressureType(systolic, diastolic)
        // binding.tvInterpretationBP.text = "Blood Pressure Type: ${bloodPressureInfo.first}"
        binding.TextViewBPInterpretation.text = "${bloodPressureInfo.first}"
        binding.TextViewBPInterpretation.setTextColor(ContextCompat.getColor(this, bloodPressureInfo.second))
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


    override fun onBackPressed() {
        super.onBackPressed()
        gotoScreen(this,PatientForms::class.java)
    }
    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }
}