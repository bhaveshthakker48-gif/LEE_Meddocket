package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.print.PrintManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.PrimaryKey
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Adapter.AdapterPrescriptionData1
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImagePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.CustomPrintDocumentAdapter7
import org.impactindiafoundation.iifllemeddocket.Utils.FooterData
import org.impactindiafoundation.iifllemeddocket.Utils.HeaderData
import org.impactindiafoundation.iifllemeddocket.Utils.RealPathUtil1
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityRecycleviewOnlyBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrintPrescriptionDataActivity1 : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityRecycleviewOnlyBinding

    var prescriptionDataList: ArrayList<CreatePrescriptionModel>? = null

    lateinit var adapter: AdapterPrescriptionData1

    private lateinit var printManager: PrintManager

    lateinit var sessionManager: SessionManager
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    var patient_id = ""

    private val GALLERY: Int = 1
    private var CAMERA: Int = 2

    var filePath: String? = null

    var UpdatedfilePath: String = ""
    private var patientData: PatientData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecycleviewOnlyBinding.inflate(layoutInflater)

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

        getViewModel()
        createRoomDatabase()
        binding.toolbarPrescriptionFormDrugList.toolbar.title = "Prescription Drug List"
        prescriptionDataList = ArrayList()
        printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        sessionManager = SessionManager(this)
        val decodedText = sessionManager.getPatientData()

        val gson = Gson()
        patientData = gson.fromJson(decodedText, PatientData::class.java)
        patient_id = patientData!!.patientId.toString()

        val extras = intent.extras
        if (extras != null) {
            prescriptionDataList =
                extras.getSerializable("prescriptionDataList") as ArrayList<CreatePrescriptionModel>?
            if (prescriptionDataList != null) {
                // Use the prescriptionDataList as needed
                Log.d(ConstantsApp.TAG, "prescriptionDataList=>" + prescriptionDataList)
                adapter = AdapterPrescriptionData1(this, prescriptionDataList!!)
                binding.RecyclerViewPrescription.adapter = adapter
                binding.RecyclerViewPrescription.layoutManager = LinearLayoutManager(this)
                printDocument()
            }
        }

        binding.submitPrescription.setOnClickListener(this)
    }


    private fun getViewModel() {
        val LLE_MedDocketRespository = LLE_MedDocketRespository()
        val LLE_MedDocketProviderFactory =
            LLE_MedDocketProviderFactory(LLE_MedDocketRespository, application)
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
        val Eye_Pre_Op_Investigation_DAO: Eye_Pre_Op_Investigation_DAO =
            database.Eye_Pre_Op_Investigation_DAO()
        val Eye_Post_Op_AND_Follow_ups_DAO: Eye_Post_Op_AND_Follow_ups_DAO =
            database.Eye_Post_Op_AND_Follow_ups_DAO()
        val Eye_OPD_Doctors_Note_DAO: Eye_OPD_Doctors_Note_DAO = database.Eye_OPD_Doctors_Note_DAO()
        val Cataract_Surgery_Notes_DAO: Cataract_Surgery_Notes_DAO =
            database.Cataract_Surgery_Notes_DAO()
        val Patient_DAO: PatientDao = database.PatientDao()
        val Image_Upload_DAO: Image_Upload_DAO = database.Image_Upload_DAO()
        val Registration_DAO: Registration_DAO = database.Registration_DAO()
        val Prescription_DAO: Prescription_DAO = database.Prescription_DAO()
        val SynTable_DAO: SynTable_DAO = database.SynTable_DAO()
        val Final_Prescription_DAO: Final_Prescription_DAO = database.Final_Prescription_DAO()
        val SpectacleDisdributionStatus_DAO: SpectacleDisdributionStatus_DAO =
            database.SpectacleDisdributionStatus_DAO()
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

    private fun printDocument() {
        val decodedText = sessionManager.getPatientData()

        val gson = Gson()
        val patientData = gson.fromJson(decodedText, PatientData::class.java)
        val patientData1 = gson.fromJson(decodedText, PatientDataLocal::class.java)

        // Access the values
        val patientFname = patientData.patientFname
        val patientLname = patientData.patientLname
        val patientAge = patientData.patientAge
        val patientID = patientData.patientId
        val patientGender = patientData.patientGen
        val camp = patientData.location
        val ageUnit = patientData.AgeUnit
        val patient_id = patientData.patientId.toString()

        val patientName = patientFname + "" + patientLname
        val genderAge = patientGender + "/" + patientAge + " " + ageUnit

        val jobName = getString(R.string.app_name) + " Document"

        val printAdapter = CustomPrintDocumentAdapter7(
            this,
            binding.RecyclerViewPrescription,
            camp,
            patientName,
            genderAge
        )
        printManager.print(jobName, printAdapter, null)

        val headerData = HeaderData(camp, patientName, genderAge)
        val footer = FooterData(ConstantsApp.getCurrentDate())

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.submitPrescription -> {
                val intent = Intent(
                    this,
                    PrescriptionDrugScreenShotActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            val imageUri = data?.data
            filePath = RealPathUtil1.getRealPath(this, imageUri!!)
            Log.d(ConstantsApp.TAG, "filePath=>" + filePath)
            Log.d(ConstantsApp.TAG, "imageUri=>" + imageUri)
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "surgical_notes_image_$timestamp.jpg"
            val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!, fileName)
            UpdatedfilePath = updatedPath!!
            Log.d(ConstantsApp.TAG, "UpdatedfilePath=>" + UpdatedfilePath)
            val (camp_id, user_id) = sessionManager.getCampUserID()
            val imagePrescription = ImagePrescriptionModel(
                0,
                UpdatedfilePath,
                patient_id.toInt(),
                camp_id!!.toInt(),
                user_id!!.toInt()
            )
            viewModel1.insertImagePrescriptionModel(imagePrescription)
        }

        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            filePath = ConstantsApp.saveBitmapToFile(imageBitmap, this)
            Log.d(ConstantsApp.TAG, "filepath on camera click=>" + filePath)
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "surgical_notes_image_$timestamp.jpg"
            val tempFile = ConstantsApp.saveBitmapToFile1(imageBitmap, fileName, this)
            val imageUri = FileProvider.getUriForFile(
                this,
                "org.impactindiafoundation.iifllemeddocket.fileprovider",  // Replace with your app's package name
                tempFile
            )
            val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!, fileName)
            UpdatedfilePath = updatedPath!!
            Log.d(ConstantsApp.TAG, "UpdatedfilePath=>" + UpdatedfilePath)
            val (camp_id, user_id) = sessionManager.getCampUserID()
            val imagePrescription = ImagePrescriptionModel(
                0,
                UpdatedfilePath,
                patient_id.toInt(),
                camp_id!!.toInt(),
                user_id!!.toInt()
            )
            viewModel1.insertImagePrescriptionModel(imagePrescription)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(
            this,
            PharmaMainActivity::class.java
        )

        startActivity(intent)
        finish()
    }
}