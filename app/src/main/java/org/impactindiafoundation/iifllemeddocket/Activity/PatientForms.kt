package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Activity.entui.EntAudiometryActivity
import org.impactindiafoundation.iifllemeddocket.Activity.entui.EntPostOpNotesActivity
import org.impactindiafoundation.iifllemeddocket.Activity.entui.EntPreOpDetailsActivity
import org.impactindiafoundation.iifllemeddocket.Activity.entui.EntSurgicalNotesActivity
import org.impactindiafoundation.iifllemeddocket.Activity.entui.OpEntDoctorNotesActivity
import org.impactindiafoundation.iifllemeddocket.Activity.pathologyui.PathologyActivity
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPatientFormBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.NewOpdInvestigationsActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.NewVisualAcuityActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.NewVitalsActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.RefractiveErrorFormActivity

class PatientForms:AppCompatActivity(), View.OnClickListener {

    lateinit var binding:ActivityPatientFormBinding
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    private var intentDecodeText =""
    private var campId =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityPatientFormBinding.inflate(layoutInflater)
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

        binding.cvVital.setOnClickListener(this)
        binding.cvInvestigations.setOnClickListener(this)
        binding.cvVision.setOnClickListener(this)
        binding.cvRefractiveError.setOnClickListener(this)
        binding.cvEyeOPDDoctorsNote.setOnClickListener(this)
        binding.cvEyePreOpInvestigation.setOnClickListener(this)
        binding.cvEyePreOpNotes.setOnClickListener(this)
        binding.cvSurgicalNotes.setOnClickListener(this)
        binding.cvEyePostOpAndFollowUps.setOnClickListener(this)
        binding.opdEntDoctorNotes?.setOnClickListener(this)
        binding.entAudiometry?.setOnClickListener(this)
        binding.entPreOpDetails?.setOnClickListener(this)
        binding.entSugicalNotes?.setOnClickListener(this)
        binding.entPostOpNotes?.setOnClickListener(this)
        binding.pathologyReports?.setOnClickListener(this)


        binding.general?.setOnClickListener {
            val isVisible = binding.generalLinearLayout?.visibility == View.VISIBLE
            binding.generalLinearLayout?.visibility = if (isVisible) View.GONE else View.VISIBLE
            val arrowDrawable = if (isVisible) R.drawable.ic_arrow_down_form else R.drawable.ic_arrow_up_form
            binding.general?.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowDrawable, 0)
        }

        // Eye section toggle
        binding.eye?.setOnClickListener {
            val isVisible = binding.mainEyeLinearLayout?.visibility == View.VISIBLE
            binding.mainEyeLinearLayout?.visibility = if (isVisible) View.GONE else View.VISIBLE
            val arrowDrawable = if (isVisible) R.drawable.ic_arrow_down_form else R.drawable.ic_arrow_up_form
            binding.eye?.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowDrawable, 0)
        }

        // Ear section toggle
        binding.ear?.setOnClickListener {
            val isVisible = binding.mainEar?.visibility == View.VISIBLE
            binding.mainEar?.visibility = if (isVisible) View.GONE else View.VISIBLE
            val arrowDrawable = if (isVisible) R.drawable.ic_arrow_down_form else R.drawable.ic_arrow_up_form
            binding.ear?.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowDrawable, 0)
        }

        //Pathology selection toggle
        binding.pathology?.setOnClickListener {
            val isVisible = binding.mainPathology?.visibility == View.VISIBLE
            binding.mainPathology?.visibility = if (isVisible) View.GONE else View.VISIBLE
            val arrowDrawable = if (isVisible) R.drawable.ic_arrow_down_form else R.drawable.ic_arrow_up_form
            binding.pathology?.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowDrawable, 0)
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

    private fun init()
    {

        binding.toolbarForm.toolbar.title="Patient Form"
        val decodedText=sessionManager.getPatientData()
        intentDecodeText = sessionManager.getPatientData().toString()
        val gson = Gson()
        val patientData = gson.fromJson(decodedText, PatientData::class.java)
        val patientData1 = gson.fromJson(decodedText, PatientDataLocal::class.java)

        val patientFname = patientData.patientFname
        val patientLname = patientData.patientLname
        val patientAge = patientData.patientAge
        val patientID = patientData.patientId
        val patientGender = patientData.patientGen
        val camp = patientData.location
        val ageUnit=patientData.AgeUnit
        campId = patientData.camp_id


        binding.edtPatientName.setText("Name :- "+patientFname+" "+patientLname)
        binding.edtAge.setText("Age :- " +patientAge.toString()+" "+ageUnit)
        binding.edtId.text="Patient ID :- "+patientID.toString()
        binding.edtGend.setText("Gender :- "+patientGender)
        binding.edtCampLoc.setText("Camp :- "+camp)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v)
        {
            binding.cvVital->
            {
                val intent = Intent(this@PatientForms,NewVitalsActivity::class.java)
                intent.putExtra("result", intentDecodeText)
                intent.putExtra("campId", campId)
                startActivity(intent)
            }
            binding.cvInvestigations->
            {
                val intent = Intent(this@PatientForms, NewOpdInvestigationsActivity::class.java)
                intent.putExtra("result", intentDecodeText)
                intent.putExtra("campId", campId)
                startActivity(intent)
            }
            binding.cvVision->
            {
                val intent = Intent(this@PatientForms,NewVisualAcuityActivity::class.java)
                intent.putExtra("result", intentDecodeText)
                intent.putExtra("campId", campId)
                startActivity(intent)
            }
            binding.cvEyePostOpAndFollowUps->
            {
                gotoScreen(this,EyePostOpAndFollowUpsActivity::class.java)
            }
            binding.cvSurgicalNotes->
            {
                gotoScreen(this,SurgicalNotesActivity::class.java)
            }
            binding.cvEyePreOpNotes->
            {
                gotoScreen(this,EyePreOpNotesActivity::class.java)
            }
            binding.cvEyePreOpInvestigation->
            {
                gotoScreen(this,EyePreOPInvestigationsActivity::class.java)
            }
            binding.cvEyeOPDDoctorsNote->
            {
                gotoScreen(this,EyeOPDDoctorsNoteActivity::class.java)
            }
            binding.cvRefractiveError->
            {
                val intent = Intent(this@PatientForms,RefractiveErrorFormActivity::class.java)
                intent.putExtra("result", intentDecodeText)
                intent.putExtra("campId", campId)
                startActivity(intent)
            }

            // ENT
            // Ent Doctor Notes
            binding.opdEntDoctorNotes ->{
                gotoScreen(this, OpEntDoctorNotesActivity::class.java)
            }

            //Ent Audiometry
            binding.entAudiometry ->{
                gotoScreen(this, EntAudiometryActivity::class.java)
            }

            //Ent Pre Op Details
            binding.entPreOpDetails ->{
                gotoScreen(this, EntPreOpDetailsActivity::class.java)
            }

            //Ent Surgical Notes
            binding.entSugicalNotes ->{
                gotoScreen(this, EntSurgicalNotesActivity::class.java)
            }

            //Ent Post Op Notes
            binding.entPostOpNotes ->{
                gotoScreen(this, EntPostOpNotesActivity::class.java)
            }

            //Pathology
            //Pathology Reports
            binding.pathologyReports ->{
                gotoScreen(this, PathologyActivity::class.java)
            }
        }
    }

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
    }
}