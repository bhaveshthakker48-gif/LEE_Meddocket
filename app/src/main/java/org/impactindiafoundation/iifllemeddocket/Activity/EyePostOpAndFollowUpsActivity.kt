package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.DatePickerDialog
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
import android.widget.CompoundButton
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
import org.impactindiafoundation.iifllemeddocket.Adapter.BloodPressureAdapter
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.Adapter.FollowUpsAdapter
import org.impactindiafoundation.iifllemeddocket.Adapter.FollowUpsAdapter1
import org.impactindiafoundation.iifllemeddocket.Adapter.TwoValueAdapter
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Post_Op_AND_Follow_ups
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.BloodPressureData
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.FollowUpsData
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.TwoValueModel
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityEyePostOpAndFollowUpsBinding
import java.util.Calendar
import java.util.Locale

class EyePostOpAndFollowUpsActivity:AppCompatActivity(), CompoundButton.OnCheckedChangeListener,
    View.OnClickListener {

    lateinit var binding:ActivityEyePostOpAndFollowUpsBinding
    lateinit var customDropDownAdapter: CustomDropDownAdapter
    var UnitArrayList:ArrayList<String>?=null
    var FollowUpsArrayList:ArrayList<String>?=null
    var NearVisionArrayList:ArrayList<String>?=null
    var RightVisionMetersArrayList:ArrayList<String>?=null
    var RightVisionArrayList:ArrayList<String>?=null
    var PinHoleArrayList:ArrayList<String>?=null
    var logMARArrayList:ArrayList<String>?=null
    var FollowUpEarly:ArrayList<FollowUpsData>?=null
    var FollowUpFundus:ArrayList<FollowUpsData>?=null
    var AccessCataractWoundArrayList:ArrayList<String>?=null
    var CheckedPupilArrayList:ArrayList<String>?=null
    var BloodPressureDataArrayList:ArrayList<BloodPressureData>?=null
    var PulseRateArrayList:ArrayList<TwoValueModel>?=null
    var RespiratoryRateArrayList:ArrayList<TwoValueModel>?=null
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    var camp_id:Int=0
    var createdDate:String=""
    var eye_post_op_2nd_date:String=""
    var eye_post_op_3rd_date:String=""
    var eye_post_op_addi_detail_left:String=""
    var eye_post_op_addi_detail_right:String=""
    var eye_post_op_asses_imedi:String=""
    var eye_post_op_assess_catract:String=""
    var eye_post_op_assess_catract_detail:String=""
    var eye_post_op_bp:String=""
    var eye_post_op_check_pupil:String=""
    var eye_post_op_check_pupil_detail:String=""
    var eye_post_op_cifloxacin:String=""
    var eye_post_op_cifloxacin_detail:String=""
    var eye_post_op_counseling:String=""
    var eye_post_op_diclofenac:String=""
    var eye_post_op_diclofenac_detail:String=""
    var eye_post_op_dimox:String=""
    var eye_post_op_dimox_detail:String=""
    var eye_post_op_discharge_check:String=""
    var eye_post_op_distant_vision_left:String=""
    var eye_post_op_distant_vision_right:String=""
    var eye_post_op_distant_vision_unit_left:String=""
    var eye_post_op_distant_vision_unit_right:String=""
    var eye_post_op_early_post_op:String=""
    var eye_post_op_ed_homide:String=""
    var eye_post_op_ed_homide_detail:String=""
    var eye_post_op_eye_1:String=""
    var eye_post_op_eye_1_detail:String=""
    var eye_post_op_eye_2:String=""
    var eye_post_op_eye_2_detail:String=""
    var eye_post_op_eye_3:String=""
    var eye_post_op_eye_3_detail:String=""
    var eye_post_op_eye_4:String=""
    var eye_post_op_eye_4_detail:String=""
    var eye_post_op_eye_5:String=""
    var eye_post_op_eye_5_detail:String=""
    var eye_post_op_fundus_exam:String=""
    var eye_post_op_fundus_pathology:String=""
    var eye_post_op_head_position:String=""
    var eye_post_op_homide:String=""
    var eye_post_op_homide_detail:String=""
    var eye_post_op_hypersol:String=""
    var eye_post_op_hypersol_detail:String=""
    var eye_post_op_last_date:String=""
    var eye_post_op_location_centration:String=""
    var eye_post_op_lubricant:String=""
    var eye_post_op_lubricant_detail:String=""
    var eye_post_op_moxifloxacin:String=""
    var eye_post_op_moxifloxacin_detail:String=""
    var eye_post_op_near_vision_left:String=""
    var eye_post_op_near_vision_right:String=""
    var eye_post_op_other:String=""
    var eye_post_op_other_detail:String=""
    var eye_post_op_pantaprezol:String=""
    var eye_post_op_pantaprezol_detail:String=""
    var eye_post_op_pr:String=""
    var eye_post_op_pressure_regular:String=""
    var eye_post_op_rr:String=""
    var eye_post_op_slit_lamp_exam:String=""
    var eye_post_op_temp:String=""
    var eye_post_op_temp_unit:String=""
    var eye_post_op_timolol:String=""
    var eye_post_op_timolol_detail:String=""
    var eye_post_op_w_addi_detail_left:String=""
    var eye_post_op_w_addi_detail_right:String=""
    var eye_post_op_w_distant_vision_left:String=""
    var eye_post_op_w_distant_vision_right:String=""
    var eye_post_op_w_distant_vision_unit_left:String=""
    var eye_post_op_w_distant_vision_unit_right:String=""
    var eye_post_op_w_near_vision_left:String=""
    var eye_post_op_w_near_vision_right:String=""
    var eye_post_op_w_pinhole_improve_left:String=""
    var eye_post_op_w_pinhole_improve_right:String=""
    var eye_post_op_w_pinhole_improve_unit_left:String=""
    var eye_post_op_w_pinhole_improve_unit_right:String=""
    var eye_post_op_w_pinhole_left:String=""
    var eye_post_op_w_pinhole_right:String=""
    var patient_id:Int=0
    var user_id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEyePostOpAndFollowUpsBinding.inflate(layoutInflater)
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
        initView()
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

    private fun initView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.RecyclerViewEarlyPostoperative!!.layoutManager = layoutManager
        binding.RecyclerViewEarlyPostoperative!!.setHasFixedSize(true)

        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = RecyclerView.VERTICAL
        binding.RecyclerViewFundusPathology!!.layoutManager = layoutManager1
        binding.RecyclerViewFundusPathology!!.setHasFixedSize(true)

        val layoutManager2 = LinearLayoutManager(this)
        layoutManager2.orientation = RecyclerView.VERTICAL
        binding.RecyclerViewBp!!.layoutManager = layoutManager2
        binding.RecyclerViewBp!!.setHasFixedSize(true)

        val layoutManager3 = LinearLayoutManager(this)
        layoutManager3.orientation = RecyclerView.VERTICAL
        binding.RecyclerViewPulseRate!!.layoutManager = layoutManager3
        binding.RecyclerViewPulseRate!!.setHasFixedSize(true)

        val layoutManager4 = LinearLayoutManager(this)
        layoutManager4.orientation = RecyclerView.VERTICAL
        binding.RecyclerViewRespirationRate!!.layoutManager = layoutManager4
        binding.RecyclerViewRespirationRate!!.setHasFixedSize(true)
    }

    private fun init() {
        binding.toolbarEyePostOpFollowUps.toolbar.title="Eye Post-Op and Follow Ups"

        BloodPressureDataArrayList= ArrayList()
        PulseRateArrayList= ArrayList()
        RespiratoryRateArrayList= ArrayList()

        FollowUpEarly= ArrayList()
        FollowUpFundus= ArrayList()
        UnitArrayList=ArrayList()
        UnitArrayList!!.add("Select Unit")
        UnitArrayList!!.add(0x00B0.toChar()+"C")
        UnitArrayList!!.add(0x00B0.toChar()+"F")

        FollowUpsArrayList= ArrayList()
        FollowUpsArrayList!!.add("Select")
        FollowUpsArrayList!!.add("Present")
        FollowUpsArrayList!!.add("Absent")

        AccessCataractWoundArrayList= ArrayList()
        AccessCataractWoundArrayList!!.add("Select")
        AccessCataractWoundArrayList!!.add("Opposed Well")
        AccessCataractWoundArrayList!!.add("Not Opposed Well")

        CheckedPupilArrayList= ArrayList()
        CheckedPupilArrayList!!.add("Select")
        CheckedPupilArrayList!!.add("Dilated")
        CheckedPupilArrayList!!.add("Not Dilated")

        RightVisionMetersArrayList=ArrayList()
        PinHoleArrayList=ArrayList()
        logMARArrayList= ArrayList()
        RightVisionArrayList=ArrayList()

        NearVisionArrayList= ArrayList()
        NearVisionArrayList!!.add("Select")
        NearVisionArrayList!!.add("N5")
        NearVisionArrayList!!.add("N6")
        NearVisionArrayList!!.add("N8")
        NearVisionArrayList!!.add("N10")
        NearVisionArrayList!!.add("N12")
        NearVisionArrayList!!.add("N14")
        NearVisionArrayList!!.add("N18")
        NearVisionArrayList!!.add("N24")
        NearVisionArrayList!!.add("N36")
        NearVisionArrayList!!.add("N48")

        RightVisionArrayList!!.add("Select")
        RightVisionArrayList!!.add("6/60")
        RightVisionArrayList!!.add("6/48")
        RightVisionArrayList!!.add("6/38")
        RightVisionArrayList!!.add("6/30")
        RightVisionArrayList!!.add("6/24")
        RightVisionArrayList!!.add("6/19")
        RightVisionArrayList!!.add("6/15")
        RightVisionArrayList!!.add("6/12")
        RightVisionArrayList!!.add("6/9.5")
        RightVisionArrayList!!.add("6/7.5")
        RightVisionArrayList!!.add("6/6.0")
        RightVisionArrayList!!.add("6/4.8")
        RightVisionArrayList!!.add("6/3.8")
        RightVisionArrayList!!.add("6/3.0")
        RightVisionArrayList!!.add("Counting fingers")
        RightVisionArrayList!!.add("Hand motion")
        RightVisionArrayList!!.add("Light perception")
        RightVisionArrayList!!.add("No Light perception")

        RightVisionMetersArrayList!!.add("Meters")
        RightVisionMetersArrayList!!.add("logMAR")

        PinHoleArrayList!!.add("Select")
        PinHoleArrayList!!.add("Improved")
        PinHoleArrayList!!.add("Not Improved")

        logMARArrayList!!.add("Select")
        logMARArrayList!!.add("0.00")
        logMARArrayList!!.add("0.02")
        logMARArrayList!!.add("0.04")
        logMARArrayList!!.add("0.06")
        logMARArrayList!!.add("0.08")
        logMARArrayList!!.add("0.10")
        logMARArrayList!!.add("0.12")
        logMARArrayList!!.add("0.14")
        logMARArrayList!!.add("0.16")
        logMARArrayList!!.add("0.18")
        logMARArrayList!!.add("0.20")
        logMARArrayList!!.add("0.22")
        logMARArrayList!!.add("0.24")
        logMARArrayList!!.add("0.26")
        logMARArrayList!!.add("0.28")
        logMARArrayList!!.add("0.30")
        logMARArrayList!!.add("0.32")
        logMARArrayList!!.add("0.34")
        logMARArrayList!!.add("0.36")
        logMARArrayList!!.add("0.38")
        logMARArrayList!!.add("0.40")
        logMARArrayList!!.add("0.42")
        logMARArrayList!!.add("0.44")
        logMARArrayList!!.add("0.46")
        logMARArrayList!!.add("0.48")
        logMARArrayList!!.add("0.50")
        logMARArrayList!!.add("0.52")
        logMARArrayList!!.add("0.54")
        logMARArrayList!!.add("0.56")
        logMARArrayList!!.add("0.58")
        logMARArrayList!!.add("0.60")
        logMARArrayList!!.add("0.62")
        logMARArrayList!!.add("0.64")
        logMARArrayList!!.add("0.66")
        logMARArrayList!!.add("0.68")
        logMARArrayList!!.add("0.70")
        logMARArrayList!!.add("0.72")
        logMARArrayList!!.add("0.74")
        logMARArrayList!!.add("0.76")
        logMARArrayList!!.add("0.78")
        logMARArrayList!!.add("0.80")
        logMARArrayList!!.add("0.82")
        logMARArrayList!!.add("0.84")
        logMARArrayList!!.add("0.86")
        logMARArrayList!!.add("0.88")
        logMARArrayList!!.add("0.90")
        logMARArrayList!!.add("0.92")
        logMARArrayList!!.add("0.94")
        logMARArrayList!!.add("0.96")
        logMARArrayList!!.add("0.98")
        logMARArrayList!!.add("1.00")

        customDropDownAdapter= CustomDropDownAdapter(this,AccessCataractWoundArrayList!!)
        binding.SpinnerAssessTheCataractWound!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,CheckedPupilArrayList!!)
        binding.SpinnerCheckPupil!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,NearVisionArrayList!!)
        binding.SpinnerWithoutPinHoleNVRightEye!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,NearVisionArrayList!!)
        binding.SpinnerWithoutPinHoleNVLeftEye!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,RightVisionMetersArrayList!!)
        binding.SpinnerWithoutPinHoleDVRightEye!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,RightVisionMetersArrayList!!)
        binding.SpinnerWithoutPinHoleDVLeftEye!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,RightVisionMetersArrayList!!)
        binding.SpinnerWithPinHolePinHoleRightEyeUnit!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,RightVisionMetersArrayList!!)
        binding.SpinnerWithPinHolePinHoleLeftEyeUnit!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,PinHoleArrayList!!)
        binding.SpinnerWithPinHolePinHoleRightEye!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,PinHoleArrayList!!)
        binding.SpinnerWithPinHolePinHoleLeftEye!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,NearVisionArrayList!!)
        binding.SpinnerWithPinHoleNVRightEye!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,NearVisionArrayList!!)
        binding.SpinnerWithPinHoleNVLeftEye!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,UnitArrayList!!)
        binding.SpinnerMonitorTemperature!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,FollowUpsArrayList!!)
        binding.SpinnerEarlyPostoperativeComplications!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,FollowUpsArrayList!!)
        binding.SpinnerFundusPathology!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,RightVisionMetersArrayList!!)
        binding.SpinnerWithPinHoleDVRightEye!!.adapter=customDropDownAdapter
        customDropDownAdapter= CustomDropDownAdapter(this,RightVisionMetersArrayList!!)
        binding.SpinnerWithPinHoleDVLeftEye!!.adapter=customDropDownAdapter

        binding.edittextTabCifloxacin.visibility=View.GONE
        binding.EditTextTabDiclofenac.visibility=View.GONE
        binding.EditTextTabPantaprezol.visibility=View.GONE
        binding.EditTextTabDimox.visibility=View.GONE
        binding.EditTextEyeDropMoxifloxacin8.visibility=View.GONE
        binding.EditTextEyeDropMoxifloxacin6.visibility=View.GONE
        binding.EditTextEyeDropMoxifloxacin4.visibility=View.GONE
        binding.EditTextEyeDropMoxifloxacin2.visibility=View.GONE
        binding.EditTextEyeDropMoxifloxacin5thweek.visibility=View.GONE
        binding.EditTextEyeDropMoxifloxacinP.visibility=View.GONE
        binding.EditTextEyeDropHomide.visibility=View.GONE
        binding.EditTextEyeDropTimololSos.visibility=View.GONE
        binding.EditTextEyeDropHypersolSos.visibility=View.GONE
        binding.EditTextLubricantDropRefresh.visibility=View.GONE
        binding.EditTextOnTheDayOfDischargeED.visibility=View.GONE
        binding.EditTextOtherMedication.visibility=View.GONE
        binding.linearEarlyPostoperative.visibility=View.GONE
        binding.texViewInstructionToPatients.visibility=View.GONE
        binding.RecyclerViewEarlyPostoperative.visibility=View.GONE
        binding.LinearLayoutAssessTheCataractWound.visibility=View.GONE
        binding.LinearLayoutCheckThePupil.visibility=View.GONE
        binding.RecyclerViewBp.visibility=View.GONE
        binding.LinearLayoutBloodPressure.visibility=View.GONE
        binding.RecyclerViewPulseRate.visibility=View.GONE
        binding.LinearLayoutPulseRate.visibility=View.GONE
        binding.LinearLayoutRespirationRate.visibility=View.GONE

        binding.checkboxTabCifloxacin.setOnCheckedChangeListener(this)
        binding.checkboxTabDiclofenac.setOnCheckedChangeListener(this)
        binding.checkboxTabPantaprezol.setOnCheckedChangeListener(this)
        binding.checkboxTabDimox.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin8.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin6.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin4.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin2.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacinP.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropMoxifloxacin5thweek.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropHomide.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropTimololSos.setOnCheckedChangeListener(this)
        binding.checkboxEyeDropHypersolSos.setOnCheckedChangeListener(this)
        binding.checkboxLubricantDropRefresh.setOnCheckedChangeListener(this)
        binding.checkboxOnTheDayOfDischarge.setOnCheckedChangeListener(this)
        binding.checkboxOtherMedication.setOnCheckedChangeListener(this)
        binding.cardViewSumbitEyePostFollow.setOnClickListener(this)
        binding.textViewInstructionsToPatientsReadMore.setOnClickListener(this)
        binding.textViewCounselingAndHealthEducationReadMore.setOnClickListener(this)
        binding.cardViewEarlyPostoperativeComplications.setOnClickListener(this)
        binding.cardViewEarlyPostoperativeComplications1.setOnClickListener(this)
        binding.cardViewFundusPathology.setOnClickListener(this)
        binding.cardViewAddMonitorBp.setOnClickListener(this)
        binding.cardViewFundusPathologyComplications1.setOnClickListener(this)
        binding.EditText2ndFollowUp.setOnClickListener(this)
        binding.EditText3ndFollowUp.setOnClickListener(this)
        binding.EditText4weeksFollowUp.setOnClickListener(this)
        binding.cardViewAddMonitorPulseRate.setOnClickListener(this)
        binding.cardViewAddMonitorRespirationRate.setOnClickListener(this)

        //binding.editTextMonitorSystolic.addTextChangedListener(createTextWatcher(binding.editTextMonitorSystolic))
        binding.editTextMonitorDiastolic.addTextChangedListener(createTextWatcher(binding.editTextMonitorDiastolic))
        binding.editTextMonitorPulseRate.addTextChangedListener(createTextWatcher(binding.editTextMonitorPulseRate))
        binding.editTextMonitorRespirationRate.addTextChangedListener(createTextWatcher(binding.editTextMonitorRespirationRate))

        binding.SpinnerEarlyPostoperativeComplications.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem == "Present") {
                    binding.linearEarlyPostoperative.visibility = View.VISIBLE
                    binding.cardViewEarlyPostoperativeComplications.visibility=View.GONE
                } else {
                    binding.linearEarlyPostoperative.visibility = View.GONE
                    binding.cardViewEarlyPostoperativeComplications.visibility=View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerFundusPathology.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem == "Present") {
                    binding.linearFundusPathology.visibility = View.VISIBLE
                    binding.cardViewFundusPathology.visibility=View.GONE
                } else {
                    binding.linearFundusPathology.visibility = View.GONE
                    binding.cardViewFundusPathology.visibility=View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHoleDVRightEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_distant_vision_unit_right=binding.SpinnerWithPinHoleDVRightEye.selectedItem.toString()
                val selectedItem=binding.SpinnerWithPinHoleDVRightEye.selectedItem.toString()

                Log.d(ConstantsApp.TAG,"selectedItem=>"+selectedItem)
                val selectedOption = RightVisionMetersArrayList!![position]

                Log.d(ConstantsApp.TAG,"selectedOption=>"+selectedOption)
                updateVisualAcuityDetailsSpinner(selectedOption,
                    binding.SpinnerWithPinHoleDVRightEyeDetails!!
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHoleDVLeftEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_distant_vision_unit_left=binding.SpinnerWithPinHoleDVLeftEye.selectedItem.toString()
                val selectedOption = RightVisionMetersArrayList!![position]
                updateVisualAcuityDetailsSpinner(selectedOption,
                    binding.SpinnerWithPinHoleDVLeftEyeDetails!!
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHolePinHoleRightEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_pinhole_right=binding.SpinnerWithPinHolePinHoleRightEye.selectedItem.toString()

                val selectedItem=binding.SpinnerWithPinHolePinHoleRightEye.selectedItem.toString()
                Log.d(ConstantsApp.TAG,""+binding.SpinnerWithPinHolePinHoleRightEye.selectedItem.toString())
                updateSpinnerVisibility(binding.SpinnerWithPinHolePinHoleRightEyeUnit,
                    binding.SpinnerWithPinHolePinHoleRightEyeDetails,selectedItem)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHolePinHoleLeftEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_pinhole_left=binding.SpinnerWithPinHolePinHoleLeftEye.selectedItem.toString()
                val selectedItem=binding.SpinnerWithPinHolePinHoleLeftEye.selectedItem.toString()
                Log.d(ConstantsApp.TAG,""+binding.SpinnerWithPinHolePinHoleLeftEye.selectedItem.toString())
                updateSpinnerVisibility(binding.SpinnerWithPinHolePinHoleLeftEyeUnit, binding.SpinnerWithPinHolePinHoleLeftEyeDetails,selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.SpinnerWithPinHolePinHoleRightEyeUnit.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_pinhole_improve_unit_right=binding.SpinnerWithPinHolePinHoleRightEyeUnit.selectedItem.toString()
                val selectedOption = RightVisionMetersArrayList!![position]
                updateVisualAcuityDetailsSpinner(selectedOption,
                    binding.SpinnerWithPinHolePinHoleRightEyeDetails!!
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHolePinHoleLeftEyeUnit.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_pinhole_improve_unit_left=binding.SpinnerWithPinHolePinHoleLeftEyeUnit.selectedItem.toString()
                val selectedOption = RightVisionMetersArrayList!![position]
                updateVisualAcuityDetailsSpinner(selectedOption,
                    binding.SpinnerWithPinHolePinHoleLeftEyeDetails!!
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithoutPinHoleDVRightEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_distant_vision_unit_right=binding.SpinnerWithoutPinHoleDVRightEye.selectedItem.toString()
                val selectedOption = RightVisionMetersArrayList!![position]
                updateVisualAcuityDetailsSpinner(selectedOption,
                    binding.SpinnerWithoutPinHoleDVRightEyeDetails!!
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithoutPinHoleDVLeftEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_distant_vision_unit_left=binding.SpinnerWithoutPinHoleDVLeftEye.selectedItem.toString()
                val selectedOption = RightVisionMetersArrayList!![position]
                updateVisualAcuityDetailsSpinner(selectedOption,
                    binding.SpinnerWithoutPinHoleDVLeftEyeDetails!!
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerAssessTheCataractWound.onItemSelectedListener=object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = binding.SpinnerAssessTheCataractWound.selectedItem.toString()
                eye_post_op_assess_catract=binding.SpinnerAssessTheCataractWound.selectedItem.toString()
                when (selectedItem) {
                    "Not Opposed Well", "Opposed Well" -> {
                        binding.LinearLayoutAssessTheCataractWound.visibility = View.VISIBLE
                    }
                    "Select" -> {
                        binding.LinearLayoutAssessTheCataractWound.visibility = View.GONE
                    }
                }
                Log.d(ConstantsApp.TAG, "Selected Item: $selectedItem")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerCheckPupil.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem=binding.SpinnerCheckPupil.selectedItem.toString()
                eye_post_op_check_pupil=binding.SpinnerCheckPupil.selectedItem.toString()
                HideShowLayout1(selectedItem,binding.LinearLayoutCheckThePupil)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerMonitorTemperature.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_temp_unit=binding.SpinnerMonitorTemperature.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHoleNVRightEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_near_vision_right=binding.SpinnerWithPinHoleNVRightEye.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHoleNVLeftEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_near_vision_left=binding.SpinnerWithPinHoleNVLeftEye.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHoleDVRightEyeDetails.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_distant_vision_right=binding.SpinnerWithPinHoleDVRightEyeDetails.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHoleDVLeftEyeDetails.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_distant_vision_left=binding.SpinnerWithPinHoleDVLeftEyeDetails.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHolePinHoleRightEyeDetails.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_pinhole_improve_right=binding.SpinnerWithPinHolePinHoleRightEyeDetails.selectedItem.toString()            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithPinHolePinHoleLeftEyeDetails.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_w_pinhole_improve_left=binding.SpinnerWithPinHolePinHoleLeftEyeDetails.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithoutPinHoleNVRightEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_near_vision_right=binding.SpinnerWithoutPinHoleNVRightEye.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithoutPinHoleNVLeftEye.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_near_vision_left=binding.SpinnerWithoutPinHoleNVLeftEye.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithoutPinHoleDVRightEyeDetails.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_distant_vision_right=binding.SpinnerWithoutPinHoleDVRightEyeDetails.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerWithoutPinHoleDVLeftEyeDetails.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                eye_post_op_distant_vision_left=binding.SpinnerWithoutPinHoleDVLeftEyeDetails.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun HideShowLayout1(selectedItem: String, layout: LinearLayout?) {
        when(selectedItem) {
            "Not Dilated"-> {
                layout!!.visibility=View.VISIBLE
            }
            "Dilated"-> {
                layout!!.visibility=View.VISIBLE
            }
            "Select"-> {
                layout!!.visibility=View.GONE
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView) {
            binding.checkboxTabCifloxacin-> {
                if (isChecked) {
                    eye_post_op_cifloxacin=binding.checkboxTabCifloxacin.text.toString()
                    binding.edittextTabCifloxacin.visibility = View.VISIBLE
                } else {
                    binding.edittextTabCifloxacin.visibility = View.GONE
                }
            }

            binding.checkboxTabDiclofenac-> {
                if (isChecked) {
                    eye_post_op_diclofenac=binding.checkboxTabDiclofenac.text.toString()
                    binding.EditTextTabDiclofenac.visibility = View.VISIBLE
                } else {
                    binding.EditTextTabDiclofenac.visibility = View.GONE
                }
            }

            binding.checkboxTabPantaprezol-> {
                if (isChecked) {
                    eye_post_op_pantaprezol=binding.checkboxTabPantaprezol.text.toString()
                    binding.EditTextTabPantaprezol.visibility = View.VISIBLE
                } else {
                    binding.EditTextTabPantaprezol.visibility = View.GONE
                }
            }

            binding.checkboxTabDimox-> {
                if (isChecked) {
                    eye_post_op_dimox=binding.checkboxTabDimox.text.toString()
                    binding.EditTextTabDimox.visibility = View.VISIBLE
                } else {
                    binding.EditTextTabDimox.visibility = View.GONE
                }
            }

            binding.checkboxEyeDropMoxifloxacin8->
            {
                if (isChecked) {
                    eye_post_op_eye_1=binding.checkboxEyeDropMoxifloxacin8.text.toString()
                    binding.EditTextEyeDropMoxifloxacin8.visibility = View.VISIBLE
                } else {
                    binding.EditTextEyeDropMoxifloxacin8.visibility = View.GONE
                }
            }

            binding.checkboxEyeDropMoxifloxacin6-> {
                if (isChecked) {
                    eye_post_op_eye_2=binding.checkboxEyeDropMoxifloxacin6.text.toString()
                    binding.EditTextEyeDropMoxifloxacin6.visibility = View.VISIBLE
                } else {
                    binding.EditTextEyeDropMoxifloxacin6.visibility = View.GONE
                }
            }

            binding.checkboxEyeDropMoxifloxacin4-> {
                if (isChecked) {
                    eye_post_op_eye_3=binding.checkboxEyeDropMoxifloxacin4.text.toString()
                    binding.EditTextEyeDropMoxifloxacin4.visibility = View.VISIBLE
                } else {
                    binding.EditTextEyeDropMoxifloxacin4.visibility = View.GONE
                }
            }

            binding.checkboxEyeDropMoxifloxacin2-> {
                if (isChecked) {
                    eye_post_op_eye_4=binding.checkboxEyeDropMoxifloxacin2.text.toString()
                    binding.EditTextEyeDropMoxifloxacin2.visibility = View.VISIBLE
                } else {
                    binding.EditTextEyeDropMoxifloxacin2.visibility = View.GONE
                }
            }

            binding.checkboxEyeDropMoxifloxacin5thweek-> {
                if (isChecked) {
                    eye_post_op_eye_5=binding.checkboxEyeDropMoxifloxacin5thweek.text.toString()
                    binding.EditTextEyeDropMoxifloxacin5thweek.visibility = View.VISIBLE
                } else {
                    binding.EditTextEyeDropMoxifloxacin5thweek.visibility = View.GONE
                }
            }

            binding.checkboxEyeDropMoxifloxacinP-> {
                if (isChecked) {
                    eye_post_op_moxifloxacin=binding.checkboxEyeDropMoxifloxacinP.text.toString()
                    binding.EditTextEyeDropMoxifloxacinP.visibility = View.VISIBLE
                } else {
                    binding.EditTextEyeDropMoxifloxacinP.visibility = View.GONE
                }
            }

            binding.checkboxEyeDropHomide-> {
                if (isChecked) {
                    eye_post_op_homide=binding.checkboxEyeDropHomide.text.toString()
                    binding.EditTextEyeDropHomide.visibility = View.VISIBLE
                } else {
                    binding.EditTextEyeDropHomide.visibility = View.GONE
                }
            }

            binding.checkboxEyeDropTimololSos-> {
                if (isChecked) {
                    eye_post_op_timolol=binding.checkboxEyeDropTimololSos.text.toString()
                    binding.EditTextEyeDropTimololSos.visibility = View.VISIBLE
                } else {
                    binding.EditTextEyeDropTimololSos.visibility = View.GONE
                }
            }

            binding.checkboxEyeDropHypersolSos-> {
                if (isChecked) {
                    eye_post_op_hypersol=binding.checkboxEyeDropHypersolSos.text.toString()
                    binding.EditTextEyeDropHypersolSos.visibility = View.VISIBLE
                } else {
                    binding.EditTextEyeDropHypersolSos.visibility = View.GONE
                }
            }

            binding.checkboxLubricantDropRefresh-> {
                if (isChecked) {
                    eye_post_op_lubricant=binding.checkboxLubricantDropRefresh.text.toString()
                    binding.EditTextLubricantDropRefresh.visibility = View.VISIBLE
                } else {
                    binding.EditTextLubricantDropRefresh.visibility = View.GONE
                }
            }

            binding.checkboxOnTheDayOfDischarge-> {
                if (isChecked) {
                    eye_post_op_ed_homide=binding.checkboxOnTheDayOfDischarge.text.toString()
                    binding.EditTextOnTheDayOfDischargeED.visibility = View.VISIBLE
                } else {
                    binding.EditTextOnTheDayOfDischargeED.visibility = View.GONE
                }
            }

            binding.checkboxOtherMedication-> {
                if (isChecked) {
                    eye_post_op_other=binding.checkboxOtherMedication.text.toString()
                    Log.d(ConstantsApp.TAG,"eye_post_op_other=>"+eye_post_op_other)
                    binding.EditTextOtherMedication.visibility = View.VISIBLE
                } else {
                    binding.EditTextOtherMedication.visibility = View.GONE
                }
            }
        }
    }

    override fun onClick(v: View?) {
      when(v) {
          binding.textViewInstructionsToPatientsReadMore-> {
              val text=binding.textViewInstructionsToPatientsReadMore.text.toString()
              when(text) {
                  "Read More..."-> {
                      binding.texViewInstructionToPatients.visibility=View.VISIBLE
                      binding.textViewInstructionsToPatientsReadMore.text="Read Less..."
                  }
                  "Read Less..."-> {
                      binding.texViewInstructionToPatients.visibility=View.GONE
                      binding.textViewInstructionsToPatientsReadMore.text="Read More..."
                  }
              }
          }

          binding.textViewCounselingAndHealthEducationReadMore-> {
              val text=binding.textViewCounselingAndHealthEducationReadMore.text.toString()
              when(text) {
                  "Read More..."-> {
                      binding.texViewCounselingAndHealthEducation.visibility=View.VISIBLE
                      binding.textViewCounselingAndHealthEducationReadMore.text="Read Less..."
                  }
                  "Read Less..."-> {
                      binding.texViewCounselingAndHealthEducation.visibility=View.GONE
                      binding.textViewCounselingAndHealthEducationReadMore.text="Read More..."
                  }
              }
          }

          binding.cardViewSumbitEyePostFollow-> {
              val (patientId, campId, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
              val current_Date= ConstantsApp.getCurrentDate()

              eye_post_op_temp=binding.editTextMonitorTemperature.text.toString()
              eye_post_op_pressure_regular=binding.editTextMonitorEyePressure.text.toString()
              eye_post_op_head_position=binding.editTextMonitorHeadPosition.text.toString()
              eye_post_op_cifloxacin_detail=binding.edittextTabCifloxacin.text.toString()
              eye_post_op_diclofenac_detail=binding.EditTextTabDiclofenac.text.toString()
              eye_post_op_pantaprezol_detail=binding.EditTextTabPantaprezol.text.toString()
              eye_post_op_dimox_detail=binding.EditTextTabDimox.text.toString()
              eye_post_op_eye_1_detail=binding.EditTextEyeDropMoxifloxacin8.text.toString()
              eye_post_op_eye_2_detail=binding.EditTextEyeDropMoxifloxacin6.text.toString()
              eye_post_op_eye_3_detail=binding.EditTextEyeDropMoxifloxacin4.text.toString()
              eye_post_op_eye_4_detail=binding.EditTextEyeDropMoxifloxacin2.text.toString()
              eye_post_op_eye_5_detail=binding.EditTextEyeDropMoxifloxacin5thweek.text.toString()
              eye_post_op_moxifloxacin_detail=binding.EditTextEyeDropMoxifloxacinP.text.toString()
              eye_post_op_homide_detail=binding.EditTextEyeDropHomide.text.toString()
              eye_post_op_timolol_detail=binding.EditTextEyeDropTimololSos.text.toString()
              eye_post_op_hypersol_detail=binding.EditTextEyeDropHypersolSos.text.toString()
              eye_post_op_lubricant_detail=binding.EditTextLubricantDropRefresh.text.toString()
              eye_post_op_ed_homide_detail=binding.EditTextOnTheDayOfDischargeED.text.toString()
              eye_post_op_other_detail=binding.EditTextOtherMedication.text.toString()
              eye_post_op_discharge_check=binding.EditTextCounselingAndHealthEducation.text.toString()
              eye_post_op_2nd_date=binding.EditText2ndFollowUp.text.toString()
              eye_post_op_3rd_date=binding.EditText3ndFollowUp.text.toString()
              eye_post_op_last_date=binding.EditText4weeksFollowUp.text.toString()
              eye_post_op_asses_imedi=binding.EditTextAssessTheImmediatePostoperative.text.toString()
              eye_post_op_w_addi_detail_right=binding.EditTextAdditionalDetailsRightEye.text.toString()
              eye_post_op_w_addi_detail_left=binding.EditTextAdditionalDetailsLeftEye.text.toString()
              eye_post_op_addi_detail_right=binding.EditTextWithoutPinHoleAdditionalDetailsRightEye.text.toString()
              eye_post_op_addi_detail_left=binding.EditTextWithoutPinHoleAdditionalDetailsLeftEye.text.toString()
              eye_post_op_slit_lamp_exam=binding.EditTextSlitLampExamination.text.toString()
              eye_post_op_fundus_exam=binding.EditTextFundusExamination.text.toString()
              eye_post_op_assess_catract_detail=binding.EditTextAssessTheCataractWound.text.toString()
              eye_post_op_location_centration=binding.EditTextTheLocationAndCentrationOfTheIOL.text.toString()
              eye_post_op_check_pupil_detail=binding.EditTextCheckThePupil.text.toString()
              eye_post_op_counseling=binding.EditTextCounseling.text.toString()

              if (BloodPressureDataArrayList!!.isNotEmpty()) {
                  val formattedStringSymptoms = BloodPressureDataArrayList!!.joinToString { model ->
                      "{${model.systolic} = ${model.diastolic}}"+"="+model.bp_interpretation
                  }
                  val finalStringSymptomes = "{$formattedStringSymptoms}"
                  eye_post_op_bp=finalStringSymptomes
              }

              if (PulseRateArrayList!!.isNotEmpty()) {
                  val formattedStringSymptoms = PulseRateArrayList!!.joinToString { model ->
                      "${model.text} = ${model.interpretation}"
                  }
                  val finalStringSymptomes = "{$formattedStringSymptoms}"
                  eye_post_op_pr=finalStringSymptomes
              }

              if (RespiratoryRateArrayList!!.isNotEmpty()) {
                  val formattedStringSymptoms = RespiratoryRateArrayList!!.joinToString { model ->
                      "${model.text} = ${model.interpretation}"
                  }
                  val finalStringSymptomes = "{$formattedStringSymptoms}"
                  eye_post_op_rr=finalStringSymptomes
              }

              if (FollowUpEarly!!.isNotEmpty()) {
                  val formattedStringSymptoms = FollowUpEarly!!.joinToString { model ->
                      "${model.complication_details} = ${model.complication}"
                  }
                  val finalStringSymptomes = "{$formattedStringSymptoms}"
                  eye_post_op_early_post_op=finalStringSymptomes
              } else {
                  eye_post_op_early_post_op=""
              }

              if (FollowUpFundus!!.isNotEmpty()) {
                  val formattedStringSymptoms = FollowUpFundus!!.joinToString { model ->
                      "${model.complication_details} = ${model.complication}"
                  }
                  val finalStringSymptomes = "{$formattedStringSymptoms}"
                  eye_post_op_fundus_pathology=finalStringSymptomes
              } else {
                  eye_post_op_fundus_pathology=""
              }

              SubmitEyePostOpAndFollw(campId!!,
                  current_Date!!,
                  eye_post_op_2nd_date!!,
                  eye_post_op_3rd_date!!,
                  eye_post_op_addi_detail_left!!,
                  eye_post_op_addi_detail_right!!,
                  eye_post_op_asses_imedi!!,
                  eye_post_op_assess_catract!!,
                  eye_post_op_assess_catract_detail!!,
                  eye_post_op_bp!!,
                  eye_post_op_check_pupil!!,
                  eye_post_op_check_pupil_detail!!,
                  eye_post_op_cifloxacin!!,
                  eye_post_op_cifloxacin_detail!!,
                  eye_post_op_counseling!!,
                  eye_post_op_diclofenac!!,
                  eye_post_op_diclofenac_detail!!,
                  eye_post_op_dimox!!,
                  eye_post_op_dimox_detail!!,
                  eye_post_op_discharge_check!!,
                  eye_post_op_distant_vision_left!!,
                  eye_post_op_distant_vision_right!!,
                  eye_post_op_distant_vision_unit_left!!,
                  eye_post_op_distant_vision_unit_right!!,
                  eye_post_op_early_post_op!!,
                  eye_post_op_ed_homide!!,
                  eye_post_op_ed_homide_detail!!,
                  eye_post_op_eye_1!!,
                  eye_post_op_eye_1_detail!!,
                  eye_post_op_eye_2!!,
                  eye_post_op_eye_2_detail!!,
                  eye_post_op_eye_3!!,
                  eye_post_op_eye_3_detail!!,
                  eye_post_op_eye_4!!,
                  eye_post_op_eye_4_detail!!,
                  eye_post_op_eye_5!!,
                  eye_post_op_eye_5_detail!!,
                  eye_post_op_fundus_exam!!,
                  eye_post_op_fundus_pathology!!!!,
                  eye_post_op_head_position!!,
                  eye_post_op_homide!!,
                  eye_post_op_homide_detail!!,
                  eye_post_op_hypersol!!,
                  eye_post_op_hypersol_detail!!,
                  eye_post_op_last_date!!,
                  eye_post_op_location_centration!!,
                  eye_post_op_lubricant!!,
                  eye_post_op_lubricant_detail!!,
                  eye_post_op_moxifloxacin!!,
                  eye_post_op_moxifloxacin_detail!!,
                  eye_post_op_near_vision_left!!,
                  eye_post_op_near_vision_right!!,
                  eye_post_op_other!!,
                  eye_post_op_other_detail!!,
                  eye_post_op_pantaprezol!!,
                  eye_post_op_pantaprezol_detail!!,
                  eye_post_op_pr!!,
                  eye_post_op_pressure_regular!!,
                  eye_post_op_rr!!,
                  eye_post_op_slit_lamp_exam!!,
                  eye_post_op_temp!!,
                  eye_post_op_temp_unit!!,
                  eye_post_op_timolol!!,
                  eye_post_op_timolol_detail!!,
                  eye_post_op_w_addi_detail_left!!,
                  eye_post_op_w_addi_detail_right!!,
                  eye_post_op_w_distant_vision_left!!,
                  eye_post_op_w_distant_vision_right!!,
                  eye_post_op_w_distant_vision_unit_left!!,
                  eye_post_op_w_distant_vision_unit_right!!,
                  eye_post_op_w_near_vision_left!!,
                  eye_post_op_w_near_vision_right!!,
                  eye_post_op_w_pinhole_improve_left!!,
                  eye_post_op_w_pinhole_improve_right!!,
                  eye_post_op_w_pinhole_improve_unit_left!!,
                  eye_post_op_w_pinhole_improve_unit_right!!,
                  eye_post_op_w_pinhole_left!!,
                  eye_post_op_w_pinhole_right!!,
                  patientId!!,
                  userId!!)
          }

          binding.cardViewEarlyPostoperativeComplications1-> {
              val selected_complications=binding.SpinnerEarlyPostoperativeComplications.selectedItem.toString()
              when(selected_complications) {
                  "Absent"-> {
                      val selected_complications_details="-"
                      addComplications(selected_complications,selected_complications_details,FollowUpEarly)
                  }
                  "Present"-> {
                      val selected_complications_details=binding.EditTextEarlyPostoperative.text.toString()
                      addComplications(selected_complications,selected_complications_details,FollowUpEarly)
                  }
                  else-> {
                      val selected_complications_details="-"
                      addComplications(selected_complications,selected_complications_details,FollowUpEarly)
                  }
              }
          }

          binding.cardViewEarlyPostoperativeComplications-> {
              val selected_complications=binding.SpinnerEarlyPostoperativeComplications.selectedItem.toString()
              when(selected_complications) {
                  "Absent"-> {
                      val selected_complications_details="-"
                      addComplications(selected_complications,selected_complications_details,FollowUpEarly)
                  }
                  "Present"-> {
                      val selected_complications_details=binding.EditTextEarlyPostoperative.text.toString()
                      addComplications(selected_complications,selected_complications_details,FollowUpEarly)
                  }
                  else-> {
                      val selected_complications_details="-"
                      addComplications(selected_complications,selected_complications_details,FollowUpEarly)
                  }
              }
          }

          binding.cardViewFundusPathology-> {
              val selected_complications=binding.SpinnerFundusPathology.selectedItem.toString()
              when(selected_complications) {
                  "Absent"-> {
                      val selected_complications_details="-"
                      addFundusComplications(selected_complications,selected_complications_details,FollowUpFundus)
                  }
                 "Present"-> { val selected_complications_details=binding.EditTextFundusPathology.text.toString()
                      addFundusComplications(selected_complications,selected_complications_details,FollowUpFundus)
                  }
                  else-> {
                      val selected_complications_details="-"
                      addFundusComplications(selected_complications,selected_complications_details,FollowUpFundus)
                  }
              }
          }

          binding.cardViewFundusPathologyComplications1-> {
              val selected_complications=binding.SpinnerFundusPathology.selectedItem.toString()
              when(selected_complications) {
                  "Absent"-> {
                      val selected_complications_details="-"
                      addFundusComplications(selected_complications,selected_complications_details,FollowUpFundus)
                  }
                  "Present"-> {
                      val selected_complications_details=binding.EditTextFundusPathology.text.toString()
                      addFundusComplications(selected_complications,selected_complications_details,FollowUpFundus)
                  }
                  else-> {
                      val selected_complications_details="-"
                      addFundusComplications(selected_complications,selected_complications_details,FollowUpFundus)
                  }
              }
          }

          binding.EditText2ndFollowUp-> {
              showDatePickerDialog(binding.EditText2ndFollowUp)
          }

          binding.EditText3ndFollowUp-> {
              showDatePickerDialog(binding.EditText3ndFollowUp)
          }

          binding.EditText4weeksFollowUp-> {
              showDatePickerDialog(binding.EditText4weeksFollowUp)
          }

          binding.cardViewAddMonitorBp-> {
             val systolic=binding.editTextMonitorSystolic.text.toString()
              val diastolic=binding.editTextMonitorDiastolic.text.toString()
              val bp_interpretation=binding.textViewMonitorBpInterpretation.text.toString()
              AddBloodPressure(systolic,diastolic,bp_interpretation)
          }

          binding.cardViewAddMonitorPulseRate-> {
              val text=binding.editTextMonitorPulseRate.text.toString()
              val text1=binding.textViewMonitorPulseRateInterpretation.text.toString()
              AddPulseRate(text,text1,binding.RecyclerViewPulseRate,PulseRateArrayList,binding.LinearLayoutPulseRate)
          }

          binding.cardViewAddMonitorRespirationRate-> {
              val text=binding.editTextMonitorRespirationRate.text.toString()
              val text1=binding.textViewMonitorRespirationRateInterpretation.text.toString()
              AddPulseRate(
                  text,
                  text1,
                  binding.RecyclerViewRespirationRate,
                  RespiratoryRateArrayList,
                  binding.LinearLayoutRespirationRate
              )
          }
      }
    }

    private fun AddPulseRate(
        text: String,
        text1: String,
        recyclerView: RecyclerView?,
        arrayList: ArrayList<TwoValueModel>?,
        linearLayoutPulseRate: LinearLayout?,
        ) {
        recyclerView!!.visibility=View.VISIBLE
        linearLayoutPulseRate!!.visibility=View.VISIBLE
        arrayList!!.add(TwoValueModel(text,text1))
        recyclerView.adapter=TwoValueAdapter(this,arrayList)
    }

    private fun AddBloodPressure(systolic: String, diastolic: String, bpInterpretation: String) {
        binding.RecyclerViewBp.visibility=View.VISIBLE
        binding.LinearLayoutBloodPressure.visibility=View.VISIBLE

        BloodPressureDataArrayList!!.add(BloodPressureData(systolic,diastolic,bpInterpretation))
        binding.RecyclerViewBp.adapter = BloodPressureAdapter(this, BloodPressureDataArrayList!!)
    }

    private fun addComplications(
        selectedComplications: String,
        selectedComplicationsDetails: String,
        arrayList: ArrayList<FollowUpsData>?
    ) {
        if (selectedComplications == "Select") {
            showToast("Please select")
        } else {
            val isComplicationExists = FollowUpEarly?.any {
                it.complication == selectedComplications
            } ?: false

            if (isComplicationExists) {
                val existingIndex = arrayList?.indexOfFirst {
                    it.complication == selectedComplications
                } ?: -1

                if (existingIndex != -1) {
                    updateComplicationData(existingIndex,selectedComplications,selectedComplicationsDetails,binding.RecyclerViewEarlyPostoperative,FollowUpEarly)
                } else {
                    addComplicationData(selectedComplications,selectedComplicationsDetails,binding.RecyclerViewEarlyPostoperative,FollowUpEarly)
                }
            } else {
                addComplicationData(selectedComplications,selectedComplicationsDetails,binding.RecyclerViewEarlyPostoperative,FollowUpEarly)
            }
        }
    }

    private fun addComplicationData(selectedComplications: String, selectedComplicationsDetails: String, recyclerView: RecyclerView, arrayList: ArrayList<FollowUpsData>?) {
        recyclerView.visibility=View.VISIBLE
        arrayList?.add(
            FollowUpsData(
                selectedComplications,
                selectedComplicationsDetails,
            )
        )
        recyclerView.adapter = FollowUpsAdapter(this, arrayList!!)

        val dayWisePosition = ( binding.SpinnerEarlyPostoperativeComplications.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select")
        binding.SpinnerEarlyPostoperativeComplications.setSelection(dayWisePosition!!)
        binding.EditTextEarlyPostoperative.setText(null)
    }

    private fun updateComplicationData(
        index: Int,
        selectedComplications: String,
        selectedComplicationsDetails: String,
        recyclerView: RecyclerView,
        arrayList: ArrayList<FollowUpsData>?
    ) {
        recyclerView.visibility=View.VISIBLE

        arrayList?.get(index)?.let { existingItem ->
            val updatedItem = existingItem.copy(
                complication = selectedComplications,
                complication_details = selectedComplicationsDetails
            )

            arrayList?.set(index, updatedItem)

            recyclerView.adapter?.notifyItemChanged(index)

            val dayWisePosition = ( binding.SpinnerEarlyPostoperativeComplications.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select")
            binding.SpinnerEarlyPostoperativeComplications.setSelection(dayWisePosition!!)
            binding.EditTextEarlyPostoperative.setText(null)
        }
    }


    private fun addFundusComplications(
        selectedComplications: String,
        selectedComplicationsDetails: String,
        arrayList: ArrayList<FollowUpsData>?
    ) {
        if (selectedComplications == "Select") {
            showToast("Please select")
        } else {
            val isComplicationExists = FollowUpFundus?.any {
                it.complication == selectedComplications
            } ?: false

            if (isComplicationExists) {
                val existingIndex = arrayList?.indexOfFirst {
                    it.complication == selectedComplications
                } ?: -1

                if (existingIndex != -1) {
                    updateFundusComplicationData(existingIndex,selectedComplications,selectedComplicationsDetails,binding.RecyclerViewFundusPathology,FollowUpFundus)
                } else {
                    addFundusComplicationData(selectedComplications,selectedComplicationsDetails,binding.RecyclerViewFundusPathology,FollowUpFundus)
                }
            }
            else {
                addFundusComplicationData(selectedComplications,selectedComplicationsDetails,binding.RecyclerViewFundusPathology,FollowUpFundus)
            }
        }
    }

    private fun addFundusComplicationData(selectedComplications: String, selectedComplicationsDetails: String, recyclerView: RecyclerView, arrayList: ArrayList<FollowUpsData>?) {
        recyclerView.visibility=View.VISIBLE
        arrayList?.add(
            FollowUpsData(
                selectedComplications,
                selectedComplicationsDetails,
                )
        )
        recyclerView.adapter = FollowUpsAdapter1(this, arrayList!!)

        val dayWisePosition = ( binding.SpinnerEarlyPostoperativeComplications.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select")
        binding.SpinnerFundusPathology.setSelection(dayWisePosition!!)
        binding.EditTextFundusPathology.setText(null)
    }

    private fun updateFundusComplicationData(
        index: Int,
        selectedComplications: String,
        selectedComplicationsDetails: String,
        recyclerView: RecyclerView,
        arrayList: ArrayList<FollowUpsData>?
    ) {
        recyclerView.visibility=View.VISIBLE
        arrayList?.get(index)?.let { existingItem ->
            val updatedItem = existingItem.copy(
                complication = selectedComplications,
                complication_details = selectedComplicationsDetails
            )

            arrayList?.set(index, updatedItem)

            recyclerView.adapter?.notifyItemChanged(index)
            val dayWisePosition = ( binding.SpinnerEarlyPostoperativeComplications.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select")
            binding.SpinnerFundusPathology.setSelection(dayWisePosition!!)
            binding.EditTextFundusPathology.setText(null)
        }
    }


    private fun SubmitEyePostOpAndFollw(
        camp_id: Int,
        createdDate: String,
        eye_post_op_2nd_date: String,
        eye_post_op_3rd_date: String,
        eye_post_op_addi_detail_left: String,
        eye_post_op_addi_detail_right: String,
        eye_post_op_asses_imedi: String,
        eye_post_op_assess_catract: String,
        eye_post_op_assess_catract_detail: String,
        eye_post_op_bp: String,
        eye_post_op_check_pupil: String,
        eye_post_op_check_pupil_detail: String,
        eye_post_op_cifloxacin: String,
        eye_post_op_cifloxacin_detail: String,
        eye_post_op_counseling: String,
        eye_post_op_diclofenac: String,
        eye_post_op_diclofenac_detail: String,
        eye_post_op_dimox: String,
        eye_post_op_dimox_detail: String,
        eye_post_op_discharge_check: String,
        eye_post_op_distant_vision_left: String,
        eye_post_op_distant_vision_right: String,
        eye_post_op_distant_vision_unit_left: String,
        eye_post_op_distant_vision_unit_right: String,
        eye_post_op_early_post_op: String,
        eye_post_op_ed_homide: String,
        eye_post_op_ed_homide_detail: String,
        eye_post_op_eye_1: String,
        eye_post_op_eye_1_detail: String,
        eye_post_op_eye_2: String,
        eye_post_op_eye_2_detail: String,
        eye_post_op_eye_3: String,
        eye_post_op_eye_3_detail: String,
        eye_post_op_eye_4: String,
        eye_post_op_eye_4_detail: String,
        eye_post_op_eye_5: String,
        eye_post_op_eye_5_detail: String,
        eye_post_op_fundus_exam: String,
        eye_post_op_fundus_pathology: String,
        eye_post_op_head_position: String,
        eye_post_op_homide: String,
        eye_post_op_homide_detail: String,
        eye_post_op_hypersol: String,
        eye_post_op_hypersol_detail: String,
        eye_post_op_last_date: String,
        eye_post_op_location_centration: String,
        eye_post_op_lubricant: String,
        eye_post_op_lubricant_detail: String,
        eye_post_op_moxifloxacin: String,
        eye_post_op_moxifloxacin_detail: String,
        eye_post_op_near_vision_left: String,
        eye_post_op_near_vision_right: String,
        eye_post_op_other: String,
        eye_post_op_other_detail: String,
        eye_post_op_pantaprezol: String,
        eye_post_op_pantaprezol_detail: String,
        eye_post_op_pr: String,
        eye_post_op_pressure_regular: String,
        eye_post_op_rr: String,
        eye_post_op_slit_lamp_exam: String,
        eye_post_op_temp: String,
        eye_post_op_temp_unit: String,
        eye_post_op_timolol: String,
        eye_post_op_timolol_detail: String,
        eye_post_op_w_addi_detail_left: String,
        eye_post_op_w_addi_detail_right: String,
        eye_post_op_w_distant_vision_left: String,
        eye_post_op_w_distant_vision_right: String,
        eye_post_op_w_distant_vision_unit_left: String,
        eye_post_op_w_distant_vision_unit_right: String,
        eye_post_op_w_near_vision_left: String,
        eye_post_op_w_near_vision_right: String,
        eye_post_op_w_pinhole_improve_left: String,
        eye_post_op_w_pinhole_improve_right: String,
        eye_post_op_w_pinhole_improve_unit_left: String,
        eye_post_op_w_pinhole_improve_unit_right: String,
        eye_post_op_w_pinhole_left: String,
        eye_post_op_w_pinhole_right: String,
        patient_id: Int,
        user_id:String
    ) {
        val Eye_Post_Op_AND_Follow_ups= Eye_Post_Op_AND_Follow_ups(0,camp_id,
            createdDate,
            eye_post_op_2nd_date,
            eye_post_op_3rd_date,
            eye_post_op_addi_detail_left,
            eye_post_op_addi_detail_right,
            eye_post_op_asses_imedi,
            eye_post_op_assess_catract,
            eye_post_op_assess_catract_detail,
            eye_post_op_bp,
            eye_post_op_check_pupil,
            eye_post_op_check_pupil_detail,
            eye_post_op_cifloxacin,
            eye_post_op_cifloxacin_detail,
            eye_post_op_counseling,
            eye_post_op_diclofenac,
            eye_post_op_diclofenac_detail,
            eye_post_op_dimox,
            eye_post_op_dimox_detail,
            eye_post_op_discharge_check,
            eye_post_op_distant_vision_left,
            eye_post_op_distant_vision_right,
            eye_post_op_distant_vision_unit_left,
            eye_post_op_distant_vision_unit_right,
            eye_post_op_early_post_op,
            eye_post_op_ed_homide,
            eye_post_op_ed_homide_detail,
            eye_post_op_eye_1,
            eye_post_op_eye_1_detail,
            eye_post_op_eye_2,
            eye_post_op_eye_2_detail,
            eye_post_op_eye_3,
            eye_post_op_eye_3_detail,
            eye_post_op_eye_4,
            eye_post_op_eye_4_detail,
            eye_post_op_eye_5,
            eye_post_op_eye_5_detail,
            eye_post_op_fundus_exam,
            eye_post_op_fundus_pathology,
            eye_post_op_head_position,
            eye_post_op_homide,
            eye_post_op_homide_detail,
            eye_post_op_hypersol,
            eye_post_op_hypersol_detail,
            eye_post_op_last_date,
            eye_post_op_location_centration,
            eye_post_op_lubricant,
            eye_post_op_lubricant_detail,
            eye_post_op_moxifloxacin,
            eye_post_op_moxifloxacin_detail,
            eye_post_op_near_vision_left,
            eye_post_op_near_vision_right,
            eye_post_op_other,
            eye_post_op_other_detail,
            eye_post_op_pantaprezol,
            eye_post_op_pantaprezol_detail,
            eye_post_op_pr,
            eye_post_op_pressure_regular,
            eye_post_op_rr,
            eye_post_op_slit_lamp_exam,
            eye_post_op_temp,
            eye_post_op_temp_unit,
            eye_post_op_timolol,
            eye_post_op_timolol_detail,
            eye_post_op_w_addi_detail_left,
            eye_post_op_w_addi_detail_right,
            eye_post_op_w_distant_vision_left,
            eye_post_op_w_distant_vision_right,
            eye_post_op_w_distant_vision_unit_left,
            eye_post_op_w_distant_vision_unit_right,
            eye_post_op_w_near_vision_left,
            eye_post_op_w_near_vision_right,
            eye_post_op_w_pinhole_improve_left,
            eye_post_op_w_pinhole_improve_right,
            eye_post_op_w_pinhole_improve_unit_left,
            eye_post_op_w_pinhole_improve_unit_right,
            eye_post_op_w_pinhole_left,
            eye_post_op_w_pinhole_right,
            patient_id,
            user_id)

        viewModel1.insertEyePostOpAndFollowUps(Eye_Post_Op_AND_Follow_ups)
        EyePostOpAndFollowUpsResponse()
    }

    private fun EyePostOpAndFollowUpsResponse() {
        viewModel1.toastMessage.observe(this
            , Observer { message ->
                showToast(message)
                gotoScreen(this,PatientForms::class.java)
            })
    }


    private fun createTextWatcher(editText: EditText): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val text = s.toString()
                when (editText) {
                    binding.editTextMonitorSystolic-> {
                        setBloodPressure()
                    }
                    binding.editTextMonitorDiastolic-> {
                        setBloodPressure()
                    }
                    binding.editTextMonitorPulseRate-> {
                        setPulseRateInfo()
                    }
                    binding.editTextMonitorRespirationRate-> {
                        interpretRespirationRate();
                    }
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                // Here, you can identify which EditText has changed by checking the ID or some other criteria
                when (editText) {
                    binding.editTextMonitorSystolic-> {
                        setBloodPressure()
                    }
                    binding.editTextMonitorDiastolic-> {
                        setBloodPressure()
                    }
                    binding.editTextMonitorPulseRate-> {
                        setPulseRateInfo()
                    }
                    binding.editTextMonitorRespirationRate-> {
                        interpretRespirationRate();
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                when (editText) {
                    binding.editTextMonitorSystolic-> {
                        setBloodPressure()
                    }
                    binding.editTextMonitorDiastolic-> {
                        setBloodPressure()
                    }
                    binding.editTextMonitorPulseRate-> {
                        setPulseRateInfo()
                    }
                    binding.editTextMonitorRespirationRate-> {
                        interpretRespirationRate();
                    }
                }
            }
        }
    }

    private fun setBloodPressure() {
        val systolic = binding.editTextMonitorSystolic.text.toString().toIntOrNull() ?: 0
        val diastolic = binding.editTextMonitorDiastolic.text.toString().toIntOrNull() ?: 0

        val bloodPressureInfo = getBloodPressureType(systolic, diastolic)
        binding.textViewMonitorBpInterpretation.text = "${bloodPressureInfo.first}"
        binding.textViewMonitorBpInterpretation.setTextColor(ContextCompat.getColor(this, bloodPressureInfo.second))
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

    private fun setPulseRateInfo() {
        val pulseRate = binding.editTextMonitorPulseRate.text.toString().toIntOrNull() ?: 0
        val pulseRateInfo = getPulseRateInfo(pulseRate)
        binding.textViewMonitorPulseRateInterpretation.text = "${pulseRateInfo.first}"
        binding.textViewMonitorPulseRateInterpretation.setTextColor(ContextCompat.getColor(this, pulseRateInfo.second))
    }

    private fun getPulseRateInfo(pulseRate: Int): Pair<String, Int> {
        return when {
            pulseRate in 60..100 -> Pair("Normal", R.color.black)
            pulseRate < 60 -> Pair("Bradycardia", R.color.blue)
            pulseRate > 100 -> Pair("Tachycardia", R.color.red)
            else -> Pair("Unknown", android.R.color.black) // Set a default color for unknown
        }
    }

    private fun interpretRespirationRate() {
        val input: String = binding.editTextMonitorRespirationRate.getText().toString().trim()
        if (!input.isEmpty()) {
            try {
                val respirationRate = input.toInt()
                val result = getInterpretation(respirationRate)
                binding.textViewMonitorRespirationRateInterpretation.setText(result.message)
                binding.textViewMonitorRespirationRateInterpretation.setTextColor(result.color)
            } catch (e: NumberFormatException) {
            }
        } else {
            binding.textViewMonitorRespirationRateInterpretation.setTextColor(Color.WHITE)
            binding.textViewMonitorRespirationRateInterpretation.hint="Interpretation"
        }
    }

    private fun getInterpretation(respirationRate: Int): InterpretationResult {
        return if (respirationRate >= 1 && respirationRate <= 11) {
            InterpretationResult(
                "Bradypnea",
                Color.BLUE
            )
        } else if (respirationRate >= 12 && respirationRate <= 20) {
            InterpretationResult(
                "Normal",
                Color.BLACK
            )
        } else if (respirationRate > 21) {
            InterpretationResult(
                "Tachypnea",
                Color.RED
            )
        } else {
            InterpretationResult("Normal",Color.BLACK)
        }
    }

    private class InterpretationResult internal constructor(var message: String, var color: Int)

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateVisualAcuityDetailsSpinner(
        selectedOption: String,
        spinner: Spinner
    ) {
        val detailsArrayList = when (selectedOption) {
            "Meters" -> RightVisionArrayList
            "logMAR" -> logMARArrayList
            else -> arrayListOf("Select") // Handle other cases if needed
        }
        val detailsAdapter = CustomDropDownAdapter(this, detailsArrayList!!)
        spinner!!.adapter = detailsAdapter
    }

    fun updateSpinnerVisibility(spinner: Spinner, spinner1: Spinner, selectedItem: String?) {
        if (selectedItem.equals("Select")||selectedItem.equals("Not Improved")) {
            spinner.visibility = View.GONE
            spinner1.visibility = View.GONE
        } else {
            spinner.visibility = View.VISIBLE
            spinner1.visibility = View.VISIBLE
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
                val selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
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
