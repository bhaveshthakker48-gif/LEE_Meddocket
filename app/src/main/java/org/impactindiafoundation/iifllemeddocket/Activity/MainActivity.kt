package org.impactindiafoundation.iifllemeddocket.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.impactindiafoundation.iifllemeddocket.Activity.entui.EntPatientReportActivity
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Decuple
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_OPD_Doctors_Note
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Post_Op_AND_Follow_ups
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Investigation
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImageModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.Eyeopddocnote
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.sendEyeOPDDoctorsNoteData
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOPNotes.AddEyePreOpNotesRequest
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOPNotes.EyePreOpNote
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel.AddEyePreOpInvestigationsRequest
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel.EyePreOpInvestigation
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.EyePostAndFollowrequest
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.EyePostOp
import org.impactindiafoundation.iifllemeddocket.Model.ImageModel.ImageUploadParams
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.RefractiveError
import org.impactindiafoundation.iifllemeddocket.Model.SurgicalNotesModel.CataractSurgery
import org.impactindiafoundation.iifllemeddocket.Model.SurgicalNotesModel.SurgicalNotesRequest
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataLive
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataModel
import org.impactindiafoundation.iifllemeddocket.Model.TotalCountDataModel
import org.impactindiafoundation.iifllemeddocket.Model.VisualAcuityModel.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.Model.VitalsModel.Vitals
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SharedPrefUtil
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.ViewModel.ResourceApp
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_AUDIOMETRY
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_OPD_DOCTOR_NOTES
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_POST_OP_NOTES
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_PRE_OP_DETAILS
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_SURGICAL_NOTES
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.OPD_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.PATHOLOGY_REPORTS
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.REFRACTIVE_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.SCREEN_VOLUNTEER
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.VISUAL_ACUITY_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.VITALS_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.Measurement
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModelItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntEarType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntNoseType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntThroatType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.ImpressionType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.PreOpImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.MainViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OpdFormViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.RefractiveErrorViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.VisualAcuityViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.VitalsFormViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntAudiometryViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntOpdDoctorsNoteViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPostOpNotesViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntProOpDetailsViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntSurgicalNotesViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.pathology.PathologyViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityMainBinding
import org.impactindiafoundation.iifllemeddocket.services.OpdFormService
import org.impactindiafoundation.iifllemeddocket.services.RefractiveFormService
import org.impactindiafoundation.iifllemeddocket.services.VisualAcuityFormService
import org.impactindiafoundation.iifllemeddocket.services.VitalsFormService
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.CampPatientListActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.PatientReportActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.QrCodeActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Base64
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityMainBinding
    var vitalsUploaded: Boolean = false
    private var isSyned = false
    private lateinit var popupWindow: PopupWindow
    private lateinit var popupWindow2: PopupWindow
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    private val STORAGE_PERMISSION_REQUEST_CODE = 123
    var flag = 0
    var currentImageIndex = 0
    private val mainViewModel: MainViewModel by viewModels()
    private val REQUEST_READ_EXTERNAL_STORAGE = 1
    lateinit var appUpdateManager: AppUpdateManager

    companion object {
        private const val REQUEST_CODE_UPDATE = 100
    }

    //EYE
    private val eyeVisualAcuityViewModel: VisualAcuityViewModel by viewModels()
    private val eyeVitalsFormViewModel: VitalsFormViewModel by viewModels()
    private val eyeOpdFormViewModel: OpdFormViewModel by viewModels()
    private val eyeRefractiveErrorViewModel: RefractiveErrorViewModel by viewModels()

    //ENT
    private val entOpdDoctorsNoteViewModel: EntOpdDoctorsNoteViewModel by viewModels()
    private val entPreOpDetailsViewModel: EntProOpDetailsViewModel by viewModels()
    private val entSurgicalNotesViewModel: EntSurgicalNotesViewModel by viewModels()
    private val entPostOpNotesViewModel : EntPostOpNotesViewModel by viewModels()
    private val entAudiometryViewModel : EntAudiometryViewModel by viewModels()
    private val entPatientReportViewModel : EntPatientReportViewModel by viewModels()

    //Pathology
    private val pathologyViewModel: PathologyViewModel by viewModels()
    private var showWarningAfterSync: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        binding.fab.visibility = View.GONE
        binding.tvScanner.setOnClickListener(this)
        binding.syncFab.setOnClickListener(this)
        binding.tvReport.setOnClickListener(this)
        binding.addFab.setOnClickListener(this)
        binding.fab.setOnClickListener(this)
        binding.analyticsFab.setOnClickListener(this)
        binding.CardViewViewReport.setOnClickListener(this)
        binding.CardViewScanQR.setOnClickListener(this)
        binding.CardViewCampCompleted.setOnClickListener(this)
        binding.tvLogout.setOnClickListener(this)
        binding.CardViewLogout.setOnClickListener(this)
        binding.aboutUsFab.setOnClickListener {
            openAboutUsDailogueBox()
        }
        getViewModel()
        createRoomDatabase()
        checkForAppUpdate()
        getDataFromApiForEnt()
        checkWhatsNew(this)
    }

    private fun getAppVersion(context: Context): String {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName ?: "0.0"
        } catch (e: Exception) {
            "0.0"
        }
    }

    private fun checkWhatsNew(context: Context) {
        val currentVersion = getAppVersion(context)
        val lastVersion = SharedPrefUtil.getPrfString(context, SharedPrefUtil.LAST_SEEN_VERSION)
        if (lastVersion.isEmpty() || currentVersion != lastVersion) {
            showWhatsNewBottomSheet()
            SharedPrefUtil.savePrefString(context, SharedPrefUtil.LAST_SEEN_VERSION, currentVersion)
        }
    }


    private fun showWhatsNewBottomSheet() {
        try {
            val versionName = packageManager
                .getPackageInfo(packageName, 0).versionName

            val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(this, R.style.CustomBottomSheetDialog)
            val view = layoutInflater.inflate(R.layout.layout_whats_new_bottomsheet, null)
            bottomSheetDialog.setContentView(view)

            // Set title + message
            view.findViewById<TextView>(R.id.titleText).text = "What's New in version $versionName"
            view.findViewById<TextView>(R.id.messageText).text = """
            ✨ Latest Updates:

            • Faster performance
            • Bug fixes
            • New dashboard UI
        """.trimIndent()

            // Button click
            view.findViewById<Button>(R.id.btnGotIt).setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            Handler(Looper.getMainLooper()).postDelayed({
                bottomSheetDialog.show()
            }, 600) // 600ms delay
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @Suppress("DEPRECATION")
    private fun openAboutUsDailogueBox() {
        try {
            val appName = getString(R.string.app_name)
            val versionName = packageManager.getPackageInfo(packageName, 0).versionName

            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.dialog_about_us, null)

            val aboutText = view.findViewById<TextView>(R.id.about_text)

            val message = """
            <b>$appName</b><br><br>
            
            Impact India Foundation’s LLE MedDocket App is meant for the use of 
            <b>Lifeline Express team</b> and <b>volunteers</b> associated with each camp of Impact India Foundation’s Lifeline Express. <br><br>
            
            Volunteers, Prescription Spectacles Distribution users, Pharmacy users and Orthosis & Prosthetics users can login to the app by scanning the QR code on the registration form.<br><br>
            
            This app is an effort of Impact India Foundation to maintain complete data of all beneficiaries of Lifeline Express camps digitally, 
            for the sake of transparency, analytics and improving quality of healthcare delivery to the served community.<br><br>
            
            You are currently using <b>version $versionName</b> of the LLE MedDocket App.
        """.trimIndent()

            aboutText.text = Html.fromHtml(message)

            val logoVideo = view.findViewById<VideoView>(R.id.logo_video)

            val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.logo_animation}")

            logoVideo.setVideoURI(videoUri)
            logoVideo.setOnPreparedListener { mp ->
                mp.isLooping = true   // loop forever
                logoVideo.start()
            }

            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("About Us")
                .setView(view)
                .setPositiveButton("OK", null)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        showWarningAfterSync = false
        Handler(Looper.getMainLooper()).postDelayed({
            GetUserData()
            GetUnSyneddata()
        }, 300)
    }

    private fun getDataFromApiForEnt() {
        mainViewModel.getEntSymptomEarApi()
        mainViewModel.entSymptomEarItemResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> progress.show()
                Status.SUCCESS -> {
                    progress.dismiss()
                    response.data?.forEach {
                        val entEarType = EntEarType(it.symptom, it.id)
                        mainViewModel.insertEntSymptomEar(entEarType)
                    }
                    Log.d("details" ,"ear : ${response.data}")
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Toast.makeText(this, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    Log.d("details" ,"ear : ${response.message}" )
                }
            }
        }

        mainViewModel.getEntSymptomNoseApi()
        mainViewModel.entSymptomNoseItemResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> progress.show()
                Status.SUCCESS -> {
                    progress.dismiss()
                    response.data?.forEach {
                        val entEarType = EntNoseType(it.symptom, it.id)
                        mainViewModel.insertEntSymptomNose(entEarType)
                    }
                    Log.d("details" ,"nose : ${response.data}")
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Toast.makeText(this, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    Log.d("details" ,"nose : ${response.message}" )
                }
            }
        }

        mainViewModel.getEntSymptomThroatApi()
        mainViewModel.entSymptomThroatItemResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> progress.show()
                Status.SUCCESS -> {
                    progress.dismiss()
                    response.data?.forEach {
                        val entEarType = EntThroatType(it.symptom, it.id)
                        mainViewModel.insertEntSymptomThroat(entEarType)
                    }
                    Log.d("details" ,"Throat : ${response.data}")
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Toast.makeText(this, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    Log.d("details" ,"Throat : ${response.message}" )
                }
            }
        }

        mainViewModel.getEntImpression()
        mainViewModel.entImpressionResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> progress.show()
                Status.SUCCESS -> {
                    progress.dismiss()
                    response.data?.forEach {
                        val entImpression = ImpressionType(it.impression, it.id)
                        mainViewModel.insertEntImpression(entImpression)
                    }
                    Log.d("details" ,"Impression : ${response.data}")
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Toast.makeText(this, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    Log.d("details" ,"Impression : ${response.message}" )
                }
            }
        }
    }

    private fun GetUnSyneddata() {
        getTotalCountOfAllForm(viewModel1) { unsyncedAllformData ->
            val totalForms = unsyncedAllformData.Total_Cataract_Surgery_Notes +
                    unsyncedAllformData.Total_Eye_OPD_Doctors_Note +
                    unsyncedAllformData.Total_Eye_Post_Op_AND_Follow_ups +
                    unsyncedAllformData.Total_Eye_Pre_Op_Investigation +
                    unsyncedAllformData.Total_Eye_Pre_Op_Notes +
                    unsyncedAllformData.Total_OPD_Investigations +
                    unsyncedAllformData.Total_OPD_Investigations2 +
                    unsyncedAllformData.Total_Refractive +
                    unsyncedAllformData.Total_Refractive2 +
                    unsyncedAllformData.Total_Visual +
                    unsyncedAllformData.Total_Visual2 +
                    unsyncedAllformData.Total_Vital +
                    unsyncedAllformData.Total_Vital2 +
                    unsyncedAllformData.Total_Ent_Pro_Op_Follow_ups +
                    unsyncedAllformData.Total_Ent_Post_Op_Follow_ups +
                    unsyncedAllformData.Total_Ent_Audiometry_Follow_ups +
                    unsyncedAllformData.Total_Ent_Surgical_Follow_ups +
                    unsyncedAllformData.Total_Ent_Doctor_Notes_Follow_ups +
                    unsyncedAllformData.Pathology_Report

            val totalImages = unsyncedAllformData.Total_Image +
                    unsyncedAllformData.Total_Audiometry_Image +
                    unsyncedAllformData.Total_Pathology_Image +
                    unsyncedAllformData.Total_Preop_Image

            Log.d("UnsyncedForms", "Total unsynced forms: $totalForms")
            Log.d("UnsyncedForms", "Total unsynced images: $totalImages")
            binding.tvUnsyncedForms.text = "UnSynced Forms :- $totalForms"
            binding.tvUnsyncedImages.text = "UnSynced Images :- $totalImages"
        }
    }

    private fun getViewModel() {
        val LLE_MedDocketRespository = LLE_MedDocketRespository()
        val LLE_MedDocketProviderFactory = LLE_MedDocketProviderFactory(LLE_MedDocketRespository, application)
        viewModel = ViewModelProvider(
            this, LLE_MedDocketProviderFactory
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

    private fun GetUserData() {
        Log.d(ConstantsApp.TAG, "Login Data in Main " + sessionManager.getLoginData())
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.CardViewCampCompleted -> {
                showPopupCampCompleted()
            }

            binding.CardViewLogout -> {
                showPopup()
            }

            binding.tvLogout -> {
                showPopup()
            }

            binding.tvScanner -> {
                val intent = Intent(this@MainActivity, QrCodeActivity::class.java)
                intent.putExtra("screen", SCREEN_VOLUNTEER)
                startActivity(intent)
            }

            binding.CardViewScanQR -> {
                val intent = Intent(this@MainActivity, QrCodeActivity::class.java)
                intent.putExtra("screen", SCREEN_VOLUNTEER)
                startActivity(intent)
            }

            binding.syncFab -> {
                if (isInternetAvailable(this@MainActivity)) {
                    showPopupSync()
                } else {
                    Utility.infoToast(this@MainActivity, "Internet Not Available")
                }
            }

            binding.CardViewViewReport -> {
                reportDialog()
            }

            binding.tvReport -> {
                reportDialog()
            }

            binding.addFab -> {
                if (flag == 0) {
                    flag = 1
                    binding.fab.visibility = View.VISIBLE
                    binding.addFab.visibility = View.GONE
                    binding.addAnalyticsText.visibility = View.GONE
                    binding.syncFab.visibility = View.GONE
                    binding.addSyncText.visibility = View.GONE
                    binding.analyticsFab.visibility = View.GONE
                    binding.aboutUsFab.visibility = View.GONE
                    binding.aboutUsFabText.visibility = View.GONE
                    binding.addPersonActionText.visibility = View.GONE
                    binding.patientsFab.visibility = View.GONE
                    Log.d(ConstantsApp.TAG, "0=>" + flag)
                } else if (flag == 1) {
                    flag = 0
                    Log.d(ConstantsApp.TAG, "1=>" + flag)
                }
            }

            binding.fab -> {
                if (flag == 1) {
                    flag = 0
                    binding.fab.visibility = View.GONE
                    binding.addFab.visibility = View.VISIBLE
                    binding.addAnalyticsText.visibility = View.VISIBLE
                    binding.syncFab.visibility = View.VISIBLE
                    binding.addSyncText.visibility = View.VISIBLE
                    binding.analyticsFab.visibility = View.VISIBLE
                    binding.aboutUsFab.visibility = View.VISIBLE
                    binding.aboutUsFabText.visibility = View.VISIBLE
                    binding.addPersonActionText.visibility = View.VISIBLE
                    binding.patientsFab.visibility = View.VISIBLE
                    Log.d(ConstantsApp.TAG, "0=>" + flag)
                } else if (flag == 0) {
                    flag = 1
                    Log.d(ConstantsApp.TAG, "1=>" + flag)
                }
            }

            binding.analyticsFab -> {
                val intent = Intent(this@MainActivity, AnalyticActivity::class.java)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopup() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_popup_layout, null)

        val btnLogout: Button = popupView.findViewById(R.id.popupButton)
        val btnClose: Button = popupView.findViewById(R.id.popupCancel)
        btnLogout.setOnClickListener {
            Utility.successToast(this@MainActivity, "Logged out successfully!")
            sessionManager.clearCache(this@MainActivity)
            popupWindow.dismiss()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            this.finish()
        }
        btnClose.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopupSync() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupViewSync: View = inflater.inflate(R.layout.custom_syn_layout_data_image, null)
        val popupWindow7 = PopupWindow(
            popupViewSync,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        ).apply {
            isOutsideTouchable = false
            isFocusable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val layoutContent: View = popupViewSync.findViewById(R.id.layoutContent)
        val syncDataButton: Button = popupViewSync.findViewById(R.id.syncData)
        val syncImageButton: Button = popupViewSync.findViewById(R.id.syncImage)
        val progressOverlay: View = popupViewSync.findViewById(R.id.layoutProgressOverlay)

        popupViewSync.isFocusableInTouchMode = true
        popupViewSync.requestFocus()
        popupViewSync.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && progressOverlay.visibility == View.VISIBLE) {
                true // Consume back press
            } else {
                false
            }
        }

        syncDataButton.setOnClickListener {
            syncDataButton.isEnabled = false
            syncImageButton.isEnabled = false
            layoutContent.visibility = View.GONE
            progressOverlay.visibility = View.VISIBLE

            lifecycleScope.launch {
                var networkFailed = false
                try {
                    if (!isInternetAvailable(this@MainActivity)) {
                        throw IOException("Internet not available")
                    }
                    Send_Local_Data_To_Server()
                    entSendDataToServer()
                    getUpdateEntDataFromServer()
                    GetUnSyneddata()
                } catch (e: Exception) {
                    Log.e("SyncPopup", "Error: ${e.message}", e)
                    networkFailed = true
                    showNetworkErrorDialog()
                } finally {
                    progressOverlay.visibility = View.GONE
                    layoutContent.visibility = View.VISIBLE
                    popupWindow7.dismiss()
                    if (!networkFailed) {
                        getTotalCountOfAllForm(viewModel1) { unsyncedAllformData ->
                            val totalForms = unsyncedAllformData.Total_Cataract_Surgery_Notes +
                                    unsyncedAllformData.Total_Eye_OPD_Doctors_Note +
                                    unsyncedAllformData.Total_Eye_Post_Op_AND_Follow_ups +
                                    unsyncedAllformData.Total_Eye_Pre_Op_Investigation +
                                    unsyncedAllformData.Total_Eye_Pre_Op_Notes +
                                    unsyncedAllformData.Total_OPD_Investigations +
                                    unsyncedAllformData.Total_OPD_Investigations2 +
                                    unsyncedAllformData.Total_Refractive +
                                    unsyncedAllformData.Total_Refractive2 +
                                    unsyncedAllformData.Total_Visual +
                                    unsyncedAllformData.Total_Visual2 +
                                    unsyncedAllformData.Total_Vital +
                                    unsyncedAllformData.Total_Vital2 +
                                    unsyncedAllformData.Total_Ent_Pro_Op_Follow_ups +
                                    unsyncedAllformData.Total_Ent_Post_Op_Follow_ups +
                                    unsyncedAllformData.Total_Ent_Audiometry_Follow_ups +
                                    unsyncedAllformData.Total_Ent_Surgical_Follow_ups +
                                    unsyncedAllformData.Total_Ent_Doctor_Notes_Follow_ups +
                                    unsyncedAllformData.Pathology_Report

                            val totalImages = unsyncedAllformData.Total_Image +
                                    unsyncedAllformData.Total_Audiometry_Image +
                                    unsyncedAllformData.Total_Pathology_Image +
                                    unsyncedAllformData.Total_Preop_Image

                            val totalEntImages = unsyncedAllformData.Total_Audiometry_Image +
                                    unsyncedAllformData.Total_Pathology_Image +
                                    unsyncedAllformData.Total_Preop_Image

                            binding.tvUnsyncedForms.text = "UnSynced Forms :- $totalForms"
                            binding.tvUnsyncedImages.text = "UnSynced Images :- $totalImages"
                            showRemainingDataWarningDialog(totalForms, totalEntImages, "DATA")
                        }
                    }
                }
            }
        }

        syncImageButton.setOnClickListener {
            syncDataButton.isEnabled = false
            syncImageButton.isEnabled = false
            layoutContent.visibility = View.GONE
            progressOverlay.visibility = View.VISIBLE
            lifecycleScope.launch {
                var networkFailed = false
                try {
                    if (!isInternetAvailable(this@MainActivity)) {
                        throw IOException("Internet not available")
                    }
                    entSendImageToServer()
                } catch (e: Exception) {
                    Log.e("ImageSyncPopup", "Error: ${e.message}", e)
                    networkFailed = true
                    showNetworkErrorDialog()
                } finally {
                    progressOverlay.visibility = View.GONE
                    layoutContent.visibility = View.VISIBLE
                    popupWindow7.dismiss()
                    if (!networkFailed) {
                        getTotalCountOfAllForm(viewModel1) { unsyncedAllformData ->
                            val totalForms = unsyncedAllformData.Total_Cataract_Surgery_Notes +
                                    unsyncedAllformData.Total_Eye_OPD_Doctors_Note +
                                    unsyncedAllformData.Total_Eye_Post_Op_AND_Follow_ups +
                                    unsyncedAllformData.Total_Eye_Pre_Op_Investigation +
                                    unsyncedAllformData.Total_Eye_Pre_Op_Notes +
                                    unsyncedAllformData.Total_OPD_Investigations +
                                    unsyncedAllformData.Total_OPD_Investigations2 +
                                    unsyncedAllformData.Total_Refractive +
                                    unsyncedAllformData.Total_Refractive2 +
                                    unsyncedAllformData.Total_Visual +
                                    unsyncedAllformData.Total_Visual2 +
                                    unsyncedAllformData.Total_Vital +
                                    unsyncedAllformData.Total_Vital2 +
                                    unsyncedAllformData.Total_Ent_Pro_Op_Follow_ups +
                                    unsyncedAllformData.Total_Ent_Post_Op_Follow_ups +
                                    unsyncedAllformData.Total_Ent_Audiometry_Follow_ups +
                                    unsyncedAllformData.Total_Ent_Surgical_Follow_ups +
                                    unsyncedAllformData.Total_Ent_Doctor_Notes_Follow_ups +
                                    unsyncedAllformData.Pathology_Report

                            val totalImages = unsyncedAllformData.Total_Image +
                                    unsyncedAllformData.Total_Audiometry_Image +
                                    unsyncedAllformData.Total_Pathology_Image +
                                    unsyncedAllformData.Total_Preop_Image

                            val totalEntImages = unsyncedAllformData.Total_Audiometry_Image +
                                    unsyncedAllformData.Total_Pathology_Image +
                                    unsyncedAllformData.Total_Preop_Image

                            binding.tvUnsyncedForms.text = "UnSynced Forms :- $totalForms"
                            binding.tvUnsyncedImages.text = "UnSynced Images :- $totalImages"
                            showRemainingDataWarningDialog(totalForms, totalEntImages, "BOTH")
                        }
                    }
                }
            }
        }

        popupWindow7.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        popupWindow7.showAtLocation(popupViewSync, Gravity.CENTER, 0, 0)
    }

    private fun showNetworkErrorDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_network_error, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnRetry = dialogView.findViewById<Button>(R.id.btnRetry)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        btnRetry.setOnClickListener {
            dialog.dismiss()
            if (isInternetAvailable(this@MainActivity)) {
                showPopupSync()
            } else {
                Utility.infoToast(this@MainActivity, "Check your internet connection")
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRemainingDataWarningDialog(formsCount: Int, imagesCount: Int, source: String) {
        val message = when {
            formsCount == 0 && imagesCount == 0 && source == "DATA" ->
                "✅ All form data has been synced successfully.\n\nNo images found to sync."
            formsCount == 0 && imagesCount == 0 && source == "BOTH" ->
                "🎉 All data and images have been synced successfully."
            formsCount == 0 && imagesCount > 0 ->
                "All forms synced successfully.\n\n$imagesCount image(s) are still pending. Please sync them."
            formsCount > 0 && imagesCount > 0 ->
                "There are still $formsCount unsynced form(s) and $imagesCount unsynced image(s).\n\nPlease sync again."
            else -> "There are still some unsynced data."
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_remaining_data, null)

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val btnPositive = dialogView.findViewById<Button>(R.id.btnPositive)
        val btnNegative = dialogView.findViewById<Button>(R.id.btnNegative)

        tvTitle.text = if (formsCount == 0 && imagesCount == 0) "Sync Complete" else "Unsynced Data Remaining"
        tvMessage.text = message

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnPositive.text = if (formsCount == 0 && imagesCount == 0) "OK" else "Sync Again"
        btnPositive.setOnClickListener {
            dialog.dismiss()
            if (formsCount > 0 || imagesCount > 0) {
                showPopupSync()
            }
        }

        if (formsCount == 0 && imagesCount == 0) {
            btnNegative.visibility = View.GONE
        } else {
            btnNegative.text = "Cancel"
            btnNegative.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun ImageUpload1() {
        viewModel1.allImages.observe(this, Observer { imageList ->
            for (currentImage in imageList) {
                if (currentImage.isSyn == 0) {
                    val file = File(currentImage.file_path)
                    val requestFile =
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    val body =
                        MultipartBody.Part.createFormData("file_name", file.name, requestFile)
                    val imageTypeRequestBody = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(), currentImage.image_type!!
                    )
                    val patientIdRequestBody = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(), currentImage.patient_id.toString()
                    )
                    val campIdRequestBody = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(), currentImage.camp_id.toString()
                    )
                    val userIdRequestBody = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(), currentImage.user_id.toString()
                    )
                    val idRequestBody = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(), currentImage._id.toString()
                    )
                    val imageUploadParams = ImageUploadParams(
                        body,
                        imageTypeRequestBody,
                        patientIdRequestBody,
                        campIdRequestBody,
                        userIdRequestBody,
                        idRequestBody
                    )
                    viewModel.uploadFile1(progressDialog, imageUploadParams)
                    ImageUploadResponseSequentially()
                }
            }
        })
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopupCampCompleted() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_layout_camp_completed, null)
        val closeButton: Button = popupView.findViewById(R.id.popupButtoncancel)
        val synButton: Button = popupView.findViewById(R.id.popupButtonSyn)

        closeButton.setOnClickListener {
            popupWindow2.dismiss()
        }

        synButton.setOnClickListener {
            progress.show()
            lifecycleScope.launch {
                val unsyncedAllformData = suspendCancellableCoroutine<TotalCountDataModel> { continuation ->
                    getTotalCountOfAllForm(viewModel1) { result ->
                        continuation.resume(result, onCancellation = null)
                    }
                }
                withContext(Dispatchers.IO) {
                    val unsyncedtotalformData =
                        unsyncedAllformData.Total_Cataract_Surgery_Notes +
                                unsyncedAllformData.Total_Eye_OPD_Doctors_Note +
                                unsyncedAllformData.Total_Eye_Post_Op_AND_Follow_ups +
                                unsyncedAllformData.Total_Eye_Pre_Op_Investigation +
                                unsyncedAllformData.Total_Eye_Pre_Op_Notes +
                                unsyncedAllformData.Total_OPD_Investigations +
                                unsyncedAllformData.Total_OPD_Investigations2 +
                                unsyncedAllformData.Total_Refractive +
                                unsyncedAllformData.Total_Refractive2 +
                                unsyncedAllformData.Total_Visual +
                                unsyncedAllformData.Total_Visual2 +
                                unsyncedAllformData.Total_Vital +
                                unsyncedAllformData.Total_Vital2 +
                                unsyncedAllformData.Total_Ent_Pro_Op_Follow_ups +
                                unsyncedAllformData.Total_Ent_Post_Op_Follow_ups +
                                unsyncedAllformData.Total_Ent_Audiometry_Follow_ups +
                                unsyncedAllformData.Total_Ent_Surgical_Follow_ups +
                                unsyncedAllformData.Total_Ent_Doctor_Notes_Follow_ups +
                                unsyncedAllformData.Pathology_Report

                    val unsyncedtotalImageData = unsyncedAllformData.Total_Image +
                                unsyncedAllformData.Total_Audiometry_Image +
                                unsyncedAllformData.Total_Pathology_Image +
                                unsyncedAllformData.Total_Preop_Image

                    Log.d("UnsyncedForms", "Forms: $unsyncedtotalformData, Images: $unsyncedtotalImageData")

                    withContext(Dispatchers.Main) {
                        if (unsyncedtotalformData > 0 || unsyncedtotalImageData > 0) {
                            Send_Local_Data_To_Server()
                            entSendDataToServer()
                        } else {
                            ClearAllData()
                        }

                        popupWindow2.dismiss()
                        progress.dismiss()
                    }
                }
            }
        }
        popupWindow2 = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow2.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        popupWindow2.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    private fun ClearAllData() {
        Clear_Vitals()
        Clear_OPDInvestigation_Data()
        ClearRefractiveError()
        Clear_Eye_OPD_Doctors_Note()
        Clear_Eye_Pre_Op_Investigation()
        Clear_Eye_Pre_Op_Notes()
        Clear_SurgicalNotes()
        Clear_Eye_Post_OP_And_Follow_Ups()
        Clear_Insert_VisualAcuity()
        clearPreOpDetails()
        clearSurgicalNotes()
        clearPostOpNotes()
        clearAudiometryDetails()
        clearPathologyReports()
        clearImpression()
        clearSymptoms()
        clearDoctorInvestigationNotes()
        clearAudiometryImage()
        clearPathologyImage()
        clearPreOpImage()
        clearEntPatientReportList()
    }

    private fun clearPreOpDetails() {
        entPreOpDetailsViewModel.clearSyncedPreOpDetails()
    }

    private fun clearSurgicalNotes() {
        entSurgicalNotesViewModel.clearSyncedSurgicalNotes()
    }

    private fun clearPostOpNotes() {
        entPostOpNotesViewModel.clearSyncedPostOpNotes()
    }

    private fun clearAudiometryDetails() {
        entAudiometryViewModel.clearSyncedAudiometryDetails()
    }

    private fun clearAudiometryImage() {
        entAudiometryViewModel.clearSyncedAudiometryImage()
    }

    private fun clearPathologyReports() {
        pathologyViewModel.clearSyncedPathologyReports()
    }

    private fun clearPathologyImage() {
        pathologyViewModel.clearSyncedPathologyImage()
    }

    private fun clearPreOpImage() {
        entPreOpDetailsViewModel.clearSyncedPreOpImage()
    }

    private fun clearSymptoms(){
        entOpdDoctorsNoteViewModel.clearSyncedSymptoms()
    }

    private fun clearImpression(){
        entOpdDoctorsNoteViewModel.clearSyncedImpression()
    }

    private fun clearDoctorInvestigationNotes(){
        entOpdDoctorsNoteViewModel.clearSyncedDoctorInvestigationNotes()
    }

    private fun clearEntPatientReportList(){
        entPatientReportViewModel.clearSyncedEntPatientReportList()
    }

    private fun Clear_Insert_VisualAcuity() {
        viewModel1.allVisualAcuity.observe(this, Observer { response ->
            val visualAcuityList = mutableListOf<VisualAcuity>()
            for (i in 0 until response.size) {
                val data = response[i]
                val _id = data._id
                val camp_id = data.camp_id
                val createdDate = data.createdDate
                val patient_id = data.patient_id
                val userId = data.userId
                val va_addi_details_left = data.va_addi_details_left
                val va_addi_details_right = data.va_addi_details_right
                val va_distant_vision_left = data.va_distant_vision_left
                val va_distant_vision_right = data.va_distant_vision_right
                val va_distant_vision_unit_left = data.va_distant_vision_unit_left
                val va_distant_vision_unit_right = data.va_distant_vision_unit_right
                val va_near_vision_left = data.va_near_vision_left
                val va_near_vision_right = data.va_near_vision_right
                val va_pinhole_improve_left = data.va_pinhole_improve_left
                val va_pinhole_improve_right = data.va_pinhole_improve_right
                val va_pinhole_improve_unit_left = data.va_pinhole_improve_unit_left
                val va_pinhole_improve_unit_right = data.va_pinhole_improve_unit_right
                val va_pinhole_left = data.va_pinhole_left
                val va_pinhole_right = data.va_pinhole_right
                val isSyn = data.isSyn

                val visualAcuity =
                    org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity(
                        _id,
                        camp_id,
                        createdDate,
                        patient_id,
                        userId,
                        va_addi_details_left,
                        va_addi_details_right,
                        va_distant_vision_left,
                        va_distant_vision_right,
                        va_distant_vision_unit_left,
                        va_distant_vision_unit_right,
                        va_near_vision_left,
                        va_near_vision_right,
                        va_pinhole_improve_left,
                        va_pinhole_improve_right,
                        va_pinhole_improve_unit_left,
                        va_pinhole_improve_unit_right,
                        va_pinhole_left,
                        va_pinhole_right,
                        isSyn
                    )
                viewModel1.deleteVisualAcuity(visualAcuity)
            }
        })
    }

    private fun Clear_Eye_Post_OP_And_Follow_Ups() {
        viewModel1.allEye_Post_Op_AND_Follow_ups.observe(this, Observer { response ->
            val sizeData = response.size
            if (sizeData > 0) {
                val Eye_Post_Op_AND_Follow_upsList = mutableListOf<EyePostOp>()
                for (i in 0 until response.size) {
                    val data = response[i]
                    Log.d(ConstantsApp.TAG, "data of eye post op=>" + data)
                    val _id = data._id
                    val camp_id = data.camp_id
                    val createdDate = data.createdDate
                    val eye_post_op_2nd_date = data.eye_post_op_2nd_date
                    val eye_post_op_3rd_date = data.eye_post_op_3rd_date
                    val eye_post_op_addi_detail_left = data.eye_post_op_addi_detail_left
                    val eye_post_op_addi_detail_right = data.eye_post_op_addi_detail_right
                    val eye_post_op_asses_imedi = data.eye_post_op_asses_imedi
                    val eye_post_op_assess_catract = data.eye_post_op_assess_catract
                    val eye_post_op_assess_catract_detail = data.eye_post_op_assess_catract_detail
                    val eye_post_op_bp = data.eye_post_op_bp
                    val eye_post_op_check_pupil = data.eye_post_op_check_pupil
                    val eye_post_op_check_pupil_detail = data.eye_post_op_check_pupil_detail
                    val eye_post_op_cifloxacin = data.eye_post_op_cifloxacin
                    val eye_post_op_cifloxacin_detail = data.eye_post_op_cifloxacin_detail
                    val eye_post_op_counseling = data.eye_post_op_counseling
                    val eye_post_op_diclofenac = data.eye_post_op_diclofenac
                    val eye_post_op_diclofenac_detail = data.eye_post_op_diclofenac_detail
                    val eye_post_op_dimox = data.eye_post_op_dimox
                    val eye_post_op_dimox_detail = data.eye_post_op_dimox_detail
                    val eye_post_op_discharge_check = data.eye_post_op_discharge_check
                    val eye_post_op_distant_vision_left = data.eye_post_op_distant_vision_left
                    val eye_post_op_distant_vision_right = data.eye_post_op_distant_vision_right
                    val eye_post_op_distant_vision_unit_left = data.eye_post_op_distant_vision_unit_left
                    val eye_post_op_distant_vision_unit_right = data.eye_post_op_distant_vision_unit_right
                    val eye_post_op_early_post_op = data.eye_post_op_early_post_op
                    val eye_post_op_ed_homide = data.eye_post_op_ed_homide
                    val eye_post_op_ed_homide_detail = data.eye_post_op_ed_homide_detail
                    val eye_post_op_eye_1 = data.eye_post_op_eye_1
                    val eye_post_op_eye_1_detail = data.eye_post_op_eye_1_detail
                    val eye_post_op_eye_2 = data.eye_post_op_eye_2
                    val eye_post_op_eye_2_detail = data.eye_post_op_eye_2_detail
                    val eye_post_op_eye_3 = data.eye_post_op_eye_3
                    val eye_post_op_eye_3_detail = data.eye_post_op_eye_3_detail
                    val eye_post_op_eye_4 = data.eye_post_op_eye_4
                    val eye_post_op_eye_4_detail = data.eye_post_op_eye_4_detail
                    val eye_post_op_eye_5 = data.eye_post_op_eye_5
                    val eye_post_op_eye_5_detail = data.eye_post_op_eye_5_detail
                    val eye_post_op_fundus_exam = data.eye_post_op_fundus_exam
                    val eye_post_op_fundus_pathology = data.eye_post_op_fundus_pathology
                    val eye_post_op_head_position = data.eye_post_op_head_position
                    val eye_post_op_homide = data.eye_post_op_homide
                    val eye_post_op_homide_detail = data.eye_post_op_homide_detail
                    val eye_post_op_hypersol = data.eye_post_op_hypersol
                    val eye_post_op_hypersol_detail = data.eye_post_op_hypersol_detail
                    val eye_post_op_last_date = data.eye_post_op_last_date
                    val eye_post_op_location_centration = data.eye_post_op_location_centration
                    val eye_post_op_lubricant = data.eye_post_op_lubricant
                    val eye_post_op_lubricant_detail = data.eye_post_op_lubricant_detail
                    val eye_post_op_moxifloxacin = data.eye_post_op_moxifloxacin
                    val eye_post_op_moxifloxacin_detail = data.eye_post_op_moxifloxacin_detail
                    val eye_post_op_near_vision_left = data.eye_post_op_near_vision_left
                    val eye_post_op_near_vision_right = data.eye_post_op_near_vision_right
                    val eye_post_op_other = data.eye_post_op_other
                    val eye_post_op_other_detail = data.eye_post_op_other_detail
                    val eye_post_op_pantaprezol = data.eye_post_op_pantaprezol
                    val eye_post_op_pantaprezol_detail = data.eye_post_op_pantaprezol_detail
                    val eye_post_op_pr = data.eye_post_op_pr
                    val eye_post_op_pressure_regular = data.eye_post_op_pressure_regular
                    val eye_post_op_rr = data.eye_post_op_rr
                    val eye_post_op_slit_lamp_exam = data.eye_post_op_slit_lamp_exam
                    val eye_post_op_temp = data.eye_post_op_temp
                    val eye_post_op_temp_unit = data.eye_post_op_temp_unit
                    val eye_post_op_timolol = data.eye_post_op_timolol
                    val eye_post_op_timolol_detail = data.eye_post_op_timolol_detail
                    val eye_post_op_w_addi_detail_left = data.eye_post_op_w_addi_detail_left
                    val eye_post_op_w_addi_detail_right = data.eye_post_op_w_addi_detail_right
                    val eye_post_op_w_distant_vision_left = data.eye_post_op_w_distant_vision_left
                    val eye_post_op_w_distant_vision_right = data.eye_post_op_w_distant_vision_right
                    val eye_post_op_w_distant_vision_unit_left = data.eye_post_op_w_distant_vision_unit_left
                    val eye_post_op_w_distant_vision_unit_right = data.eye_post_op_w_distant_vision_unit_right
                    val eye_post_op_w_near_vision_left = data.eye_post_op_w_near_vision_left
                    val eye_post_op_w_near_vision_right = data.eye_post_op_w_near_vision_right
                    val eye_post_op_w_pinhole_improve_left = data.eye_post_op_w_pinhole_improve_left
                    val eye_post_op_w_pinhole_improve_right = data.eye_post_op_w_pinhole_improve_right
                    val eye_post_op_w_pinhole_improve_unit_left = data.eye_post_op_w_pinhole_improve_unit_left
                    val eye_post_op_w_pinhole_improve_unit_right = data.eye_post_op_w_pinhole_improve_unit_right
                    val eye_post_op_w_pinhole_left = data.eye_post_op_w_pinhole_left
                    val eye_post_op_w_pinhole_right = data.eye_post_op_w_pinhole_right
                    val patient_id = data.patient_id
                    val user_id = data.userId
                    val isSyn = data.isSyn

                    val eyePostOpAndFollowUps = Eye_Post_Op_AND_Follow_ups(
                        _id,
                        camp_id,
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
                        user_id,
                        isSyn
                    )
                    viewModel1.deleteEyePostOpAndFollowUps(eyePostOpAndFollowUps)
                }
            }
        })
    }

    private fun Clear_SurgicalNotes() {
        viewModel1.allCataract_Surgery_Notes.observe(this, Observer { response ->
            val sizeData = response.size
            if (sizeData > 0) {
                val CataractSurgeryList = mutableListOf<CataractSurgery>()
                for (i in 0 until response.size) {
                    val data = response[i]
                    val _id = data._id
                    val camp_id = data.camp_id
                    val createdDate = data.createdDate
                    val patient_id = data.patient_id
                    val sn_airway = data.sn_airway
                    val sn_airway_detail = data.sn_airway_detail
                    val sn_anaesthetist_concern = data.sn_anaesthetist_concern
                    val sn_anticoagulant = data.sn_anticoagulant
                    val sn_anticoagulant_detail = data.sn_anticoagulant_detail
                    val sn_before_incision_all_team = data.sn_before_incision_all_team
                    val sn_before_or_instrument = data.sn_before_or_instrument
                    val sn_before_or_key = data.sn_before_or_key
                    val sn_before_or_key_detail = data.sn_before_or_key_detail
                    val sn_before_or_specimen = data.sn_before_or_specimen
                    val sn_before_or_weather = data.sn_before_or_weather
                    val sn_before_or_weather_detail = data.sn_before_or_weather_detail
                    val sn_cataract_capsulotomy = data.sn_cataract_capsulotomy
                    val sn_cataract_capsulotomy_detail = data.sn_cataract_capsulotomy_detail
                    val sn_cataract_castroviejo = data.sn_cataract_castroviejo
                    val sn_cataract_castroviejo_detail = data.sn_cataract_castroviejo_detail
                    val sn_cataract_colibri = data.sn_cataract_colibri
                    val sn_cataract_colibri_detail = data.sn_cataract_colibri_detail
                    val sn_cataract_formed = data.sn_cataract_formed
                    val sn_cataract_formed_detail = data.sn_cataract_formed_detail
                    val sn_cataract_hydrodissectiirs = data.sn_cataract_hydrodissectiirs
                    val sn_cataract_hydrodissectiirs_detail = data.sn_cataract_hydrodissectiirs_detail
                    val sn_cataract_irrigation = data.sn_cataract_irrigation
                    val sn_cataract_irrigation_detail = data.sn_cataract_irrigation_detail
                    val sn_cataract_keretome = data.sn_cataract_keretome
                    val sn_cataract_keretome_detail = data.sn_cataract_keretome_detail
                    val sn_cataract_keretome_phaco = data.sn_cataract_keretome_phaco
                    val sn_cataract_keretome_phaco_detail = data.sn_cataract_keretome_phaco_detail
                    val sn_cataract_knife = data.sn_cataract_knife
                    val sn_cataract_knife_detail = data.sn_cataract_knife_detail
                    val sn_cataract_lieberman = data.sn_cataract_lieberman
                    val sn_cataract_lieberman_detail = data.sn_cataract_lieberman_detail
                    val sn_cataract_limb = data.sn_cataract_limb
                    val sn_cataract_limb_detail = data.sn_cataract_limb_detail
                    val sn_cataract_mac = data.sn_cataract_mac
                    val sn_cataract_mac_detail = data.sn_cataract_mac_detail
                    val sn_cataract_nucleus = data.sn_cataract_nucleus
                    val sn_cataract_nucleus_detail = data.sn_cataract_nucleus_detail
                    val sn_cataract_sinsky = data.sn_cataract_sinsky
                    val sn_cataract_sinsky_detail = data.sn_cataract_sinsky_detail
                    val sn_cataract_universal = data.sn_cataract_universal
                    val sn_cataract_universal_detail = data.sn_cataract_universal_detail
                    val sn_cataract_viscoelastic = data.sn_cataract_viscoelastic
                    val sn_cataract_viscoelastic_detail = data.sn_cataract_viscoelastic_detail
                    val sn_common_dislocation = data.sn_common_dislocation
                    val sn_common_dislocation_detail = data.sn_common_dislocation_detail
                    val sn_common_endophthalmitis = data.sn_common_endophthalmitis
                    val sn_common_endophthalmitis_detail = data.sn_common_endophthalmitis_detail
                    val sn_common_endothelial = data.sn_common_endothelial
                    val sn_common_endothelial_detail = data.sn_common_endothelial_detail
                    val sn_common_fluid = data.sn_common_fluid
                    val sn_common_fluid_detail = data.sn_common_fluid_detail
                    val sn_common_hyphema = data.sn_common_hyphema
                    val sn_common_hyphema_detail = data.sn_common_hyphema_detail
                    val sn_common_light = data.sn_common_light
                    val sn_common_light_detail = data.sn_common_light_detail
                    val sn_common_macular = data.sn_common_macular
                    val sn_common_macular_detail = data.sn_common_macular_detail
                    val sn_common_ocular = data.sn_common_ocular
                    val sn_common_ocular_detail = data.sn_common_ocular_detail
                    val sn_common_posterior_opacification = data.sn_common_posterior_opacification
                    val sn_common_posterior_opacification_detail = data.sn_common_posterior_opacification_detail
                    val sn_common_posterior_rent = data.sn_common_posterior_rent
                    val sn_common_posterior_rent_detail = data.sn_common_posterior_rent_detail
                    val sn_common_retinal = data.sn_common_retinal
                    val sn_common_retinal_detail = data.sn_common_retinal_detail
                    val sn_common_vitreous = data.sn_common_vitreous
                    val sn_common_vitreous_detail = data.sn_common_vitreous_detail
                    val sn_date_of_surgery = data.sn_date_of_surgery
                    val sn_flomax = data.sn_flomax
                    val sn_has_confirmed_allergies = data.sn_has_confirmed_allergies
                    val sn_has_confirmed_consent = data.sn_has_confirmed_consent
                    val sn_has_confirmed_identity = data.sn_has_confirmed_identity
                    val sn_has_confirmed_procedure = data.sn_has_confirmed_procedure
                    val sn_has_confirmed_site = data.sn_has_confirmed_site
                    val sn_incision_cornea = data.sn_incision_cornea
                    val sn_incision_sclera_1 = data.sn_incision_sclera_1
                    val sn_incision_sclera_2 = data.sn_incision_sclera_2
                    val sn_intra_adrenaline = data.sn_intra_adrenaline
                    val sn_intra_adrenaline_detail = data.sn_intra_adrenaline_detail
                    val sn_intra_combination = data.sn_intra_combination
                    val sn_intra_combination_detail = data.sn_intra_combination_detail
                    val sn_intra_gentamycin = data.sn_intra_gentamycin
                    val sn_intra_gentamycin_detail = data.sn_intra_gentamycin_detail
                    val sn_intra_intasol = data.sn_intra_intasol
                    val sn_intra_intasol_detail = data.sn_intra_intasol_detail
                    val sn_intra_mannitol = data.sn_intra_mannitol
                    val sn_intra_mannitol_detail = data.sn_intra_mannitol_detail
                    val sn_intra_moxifloxacin = data.sn_intra_moxifloxacin
                    val sn_intra_moxifloxacin_detail = data.sn_intra_moxifloxacin_detail
                    val sn_intra_occular_lens = data.sn_intra_occular_lens
                    val sn_intra_prednisolone = data.sn_intra_prednisolone
                    val sn_intra_prednisolone_detail = data.sn_intra_prednisolone_detail
                    val sn_intra_vigamox = data.sn_intra_vigamox
                    val sn_intra_vigamox_detail = data.sn_intra_vigamox_detail
                    val sn_intra_visco = data.sn_intra_visco
                    val sn_intra_visco_detail = data.sn_intra_visco_detail
                    val sn_local_anaesthesia = data.sn_local_anaesthesia
                    val sn_nurse_age = data.sn_nurse_age
                    val sn_nurse_age_unit = data.sn_nurse_age_unit
                    val sn_nurse_anaesthesia = data.sn_nurse_anaesthesia
                    val sn_nurse_anaesthetist = data.sn_nurse_anaesthetist
                    val sn_nurse_bp_diastolic = data.sn_nurse_bp_diastolic
                    val sn_nurse_bp_interpretation = data.sn_nurse_bp_interpretation
                    val sn_nurse_bp_sistolic = data.sn_nurse_bp_sistolic
                    val sn_nurse_concern = data.sn_nurse_concern
                    val sn_nurse_diagnosis = data.sn_nurse_diagnosis
                    val sn_nurse_duration = data.sn_nurse_duration
                    val sn_nurse_equipment_issue = data.sn_nurse_equipment_issue
                    val sn_nurse_implant_detail = data.sn_nurse_implant_detail
                    val sn_nurse_name = data.sn_nurse_name
                    val sn_nurse_orally_confirm = data.sn_nurse_orally_confirm
                    val sn_nurse_rbs = data.sn_nurse_rbs
                    val sn_nurse_rbs_interpretation = data.sn_nurse_rbs_interpretation
                    val sn_nurse_registered = data.sn_nurse_registered
                    val sn_nurse_scrub = data.sn_nurse_scrub
                    val sn_nurse_serial = data.sn_nurse_serial
                    val sn_nurse_sex = data.sn_nurse_sex
                    val sn_nurse_specimen_biopsy = data.sn_nurse_specimen_biopsy
                    val sn_nurse_specimen_detail = data.sn_nurse_specimen_detail
                    val sn_nurse_sterility = data.sn_nurse_sterility
                    val sn_nurse_surgeon = data.sn_nurse_surgeon
                    val sn_nurse_surgery_name = data.sn_nurse_surgery_name
                    val sn_nurse_viral_serology = data.sn_nurse_viral_serology
                    val sn_post_cifloxacin = data.sn_post_cifloxacin
                    val sn_post_cifloxacin_detail = data.sn_post_cifloxacin_detail
                    val sn_post_diclofenac = data.sn_post_diclofenac
                    val sn_post_diclofenac_detail = data.sn_post_diclofenac_detail
                    val sn_post_dimox = data.sn_post_dimox
                    val sn_post_dimox_detail = data.sn_post_dimox_detail
                    val sn_post_eye_1 = data.sn_post_eye_1
                    val sn_post_eye_1_detail = data.sn_post_eye_1_detail
                    val sn_post_eye_2 = data.sn_post_eye_2
                    val sn_post_eye_2_detail = data.sn_post_eye_2_detail
                    val sn_post_eye_3 = data.sn_post_eye_3
                    val sn_post_eye_3_detail = data.sn_post_eye_3_detail
                    val sn_post_eye_4 = data.sn_post_eye_4
                    val sn_post_eye_4_detail = data.sn_post_eye_4_detail
                    val sn_post_eye_5 = data.sn_post_eye_5
                    val sn_post_eye_5_detail = data.sn_post_eye_5_detail
                    val sn_post_eye_homide = data.sn_post_eye_homide
                    val sn_post_eye_homide_detail = data.sn_post_eye_homide_detail
                    val sn_post_eye_hypersol = data.sn_post_eye_hypersol
                    val sn_post_eye_hypersol_detail = data.sn_post_eye_hypersol_detail
                    val sn_post_eye_lubricant = data.sn_post_eye_lubricant
                    val sn_post_eye_lubricant_detail = data.sn_post_eye_lubricant_detail
                    val sn_post_eye_moxifloxacin = data.sn_post_eye_moxifloxacin
                    val sn_post_eye_moxifloxacin_detail = data.sn_post_eye_moxifloxacin_detail
                    val sn_post_eye_timolol = data.sn_post_eye_timolol
                    val sn_post_eye_timolol_detail = data.sn_post_eye_timolol_detail
                    val sn_post_pantaprezol = data.sn_post_pantaprezol
                    val sn_post_pantaprezol_detail = data.sn_post_pantaprezol_detail
                    val sn_site_marked_history = data.sn_site_marked_history
                    val sn_site_marked_pre_anaesthesia = data.sn_site_marked_pre_anaesthesia
                    val sn_site_marked_pre_surgical = data.sn_site_marked_pre_surgical
                    val sn_type_of_surgery = data.sn_type_of_surgery
                    val sn_type_of_surgery_other = data.sn_type_of_surgery_other
                    val sn_unexpected_step = data.sn_unexpected_step
                    val sn_unexpected_step_detail = data.sn_unexpected_step_detail
                    val userId = data.userId
                    val isSyn = data.isSyn
                    val filepath = data.filepath

                    val surgicaldata = Cataract_Surgery_Notes(
                        _id,
                        camp_id,
                        createdDate,
                        patient_id,
                        sn_airway,
                        sn_airway_detail,
                        sn_anaesthetist_concern,
                        sn_anticoagulant,
                        sn_anticoagulant_detail,
                        sn_before_incision_all_team,
                        sn_before_or_instrument,
                        sn_before_or_key,
                        sn_before_or_key_detail,
                        sn_before_or_specimen,
                        sn_before_or_weather,
                        sn_before_or_weather_detail,
                        sn_cataract_capsulotomy,
                        sn_cataract_capsulotomy_detail,
                        sn_cataract_castroviejo,
                        sn_cataract_castroviejo_detail,
                        sn_cataract_colibri,
                        sn_cataract_colibri_detail,
                        sn_cataract_formed,
                        sn_cataract_formed_detail,
                        sn_cataract_hydrodissectiirs,
                        sn_cataract_hydrodissectiirs_detail,
                        sn_cataract_irrigation,
                        sn_cataract_irrigation_detail,
                        sn_cataract_keretome,
                        sn_cataract_keretome_detail,
                        sn_cataract_keretome_phaco,
                        sn_cataract_keretome_phaco_detail,
                        sn_cataract_knife,
                        sn_cataract_knife_detail,
                        sn_cataract_lieberman,
                        sn_cataract_lieberman_detail,
                        sn_cataract_limb,
                        sn_cataract_limb_detail,
                        sn_cataract_mac,
                        sn_cataract_mac_detail,
                        sn_cataract_nucleus,
                        sn_cataract_nucleus_detail,
                        sn_cataract_sinsky,
                        sn_cataract_sinsky_detail,
                        sn_cataract_universal,
                        sn_cataract_universal_detail,
                        sn_cataract_viscoelastic,
                        sn_cataract_viscoelastic_detail,
                        sn_common_dislocation,
                        sn_common_dislocation_detail,
                        sn_common_endophthalmitis,
                        sn_common_endophthalmitis_detail,
                        sn_common_endothelial,
                        sn_common_endothelial_detail,
                        sn_common_fluid,
                        sn_common_fluid_detail,
                        sn_common_hyphema,
                        sn_common_hyphema_detail,
                        sn_common_light,
                        sn_common_light_detail,
                        sn_common_macular,
                        sn_common_macular_detail,
                        sn_common_ocular,
                        sn_common_ocular_detail,
                        sn_common_posterior_opacification,
                        sn_common_posterior_opacification_detail,
                        sn_common_posterior_rent,
                        sn_common_posterior_rent_detail,
                        sn_common_retinal,
                        sn_common_retinal_detail,
                        sn_common_vitreous,
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
                        sn_intra_adrenaline,
                        sn_intra_adrenaline_detail,
                        sn_intra_combination,
                        sn_intra_combination_detail,
                        sn_intra_gentamycin,
                        sn_intra_gentamycin_detail,
                        sn_intra_intasol,
                        sn_intra_intasol_detail,
                        sn_intra_mannitol,
                        sn_intra_mannitol_detail,
                        sn_intra_moxifloxacin,
                        sn_intra_moxifloxacin_detail,
                        sn_intra_occular_lens,
                        sn_intra_prednisolone,
                        sn_intra_prednisolone_detail,
                        sn_intra_vigamox,
                        sn_intra_vigamox_detail,
                        sn_intra_visco,
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
                        sn_post_cifloxacin,
                        sn_post_cifloxacin_detail,
                        sn_post_diclofenac,
                        sn_post_diclofenac_detail,
                        sn_post_dimox,
                        sn_post_dimox_detail,
                        sn_post_eye_1,
                        sn_post_eye_1_detail,
                        sn_post_eye_2,
                        sn_post_eye_2_detail,
                        sn_post_eye_3,
                        sn_post_eye_3_detail,
                        sn_post_eye_4,
                        sn_post_eye_4_detail,
                        sn_post_eye_5,
                        sn_post_eye_5_detail,
                        sn_post_eye_homide,
                        sn_post_eye_homide_detail,
                        sn_post_eye_hypersol,
                        sn_post_eye_hypersol_detail,
                        sn_post_eye_lubricant,
                        sn_post_eye_lubricant_detail,
                        sn_post_eye_moxifloxacin,
                        sn_post_eye_moxifloxacin_detail,
                        sn_post_eye_timolol,
                        sn_post_eye_timolol_detail,
                        sn_post_pantaprezol,
                        sn_post_pantaprezol_detail,
                        sn_site_marked_history,
                        sn_site_marked_pre_anaesthesia,
                        sn_site_marked_pre_surgical,
                        sn_type_of_surgery,
                        sn_type_of_surgery_other,
                        sn_unexpected_step,
                        sn_unexpected_step_detail,
                        userId,
                        filepath,
                        isSyn
                    )
                    viewModel1.deleteSurgicalData(surgicaldata)
                }
            }
        })
    }

    private fun Clear_Eye_Pre_Op_Notes() {
        viewModel1.allEye_Pre_Op_Notes.observe(this, Observer { response ->
            val sizeData = response.size
            if (sizeData > 0) {
                val eyePreOpNotesList = mutableListOf<EyePreOpNote>()
                for (i in 0 until response.size) {
                    val data = response[i]
                    val _id = data._id
                    val camp_id = data.camp_id
                    val createdDate = data.createdDate
                    val eye_pre_op_admission_date = data.eye_pre_op_admission_date
                    val eye_pre_op_alprax = data.eye_pre_op_alprax
                    val eye_pre_op_amlodipine = data.eye_pre_op_amlodipine
                    val eye_pre_op_antibiotic = data.eye_pre_op_antibiotic
                    val eye_pre_op_antibiotic_detail = data.eye_pre_op_antibiotic_detail
                    val eye_pre_op_antibiotic_other = data.eye_pre_op_antibiotic_other
                    val eye_pre_op_antibiotic_result = data.eye_pre_op_antibiotic_result
                    val eye_pre_op_antihyp = data.eye_pre_op_antihyp
                    val eye_pre_op_antihyp_detail = data.eye_pre_op_antihyp_detail
                    val eye_pre_op_betadine = data.eye_pre_op_betadine
                    val eye_pre_op_bp_diastolic = data.eye_pre_op_bp_diastolic
                    val eye_pre_op_bp_interpretation = data.eye_pre_op_bp_interpretation
                    val eye_pre_op_bp_systolic = data.eye_pre_op_bp_systolic
                    val eye_pre_op_bs_f = data.eye_pre_op_bs_f
                    val eye_pre_op_bs_pp = data.eye_pre_op_bs_pp
                    val eye_pre_op_bt = data.eye_pre_op_bt
                    val eye_pre_op_cbc = data.eye_pre_op_cbc
                    val eye_pre_op_ciplox = data.eye_pre_op_ciplox
                    val eye_pre_op_ciplox_drop = data.eye_pre_op_ciplox_drop
                    val eye_pre_op_ct = data.eye_pre_op_ct
                    val eye_pre_op_dia = data.eye_pre_op_dia
                    val eye_pre_op_dia_detail = data.eye_pre_op_dia_detail
                    val eye_pre_op_diamox = data.eye_pre_op_diamox
                    val eye_pre_op_discussed_with = data.eye_pre_op_discussed_with
                    val eye_pre_op_discussed_with_detail = data.eye_pre_op_discussed_with_detail
                    val eye_pre_op_ecg = data.eye_pre_op_ecg
                    val eye_pre_op_flur_eye = data.eye_pre_op_flur_eye
                    val eye_pre_op_haemoglobin = data.eye_pre_op_haemoglobin
                    val eye_pre_op_hbsag = data.eye_pre_op_hbsag
                    val eye_pre_op_hcv = data.eye_pre_op_hcv
                    val eye_pre_op_head_bath = data.eye_pre_op_head_bath
                    val eye_pre_op_heart = data.eye_pre_op_heart
                    val eye_pre_op_heart_detail = data.eye_pre_op_heart_detail
                    val eye_pre_op_heart_rate = data.eye_pre_op_heart_rate
                    val eye_pre_op_historyof = data.eye_pre_op_historyof
                    val eye_pre_op_hiv = data.eye_pre_op_hiv
                    val eye_pre_op_identify_eye = data.eye_pre_op_identify_eye
                    val eye_pre_op_iol_power = data.eye_pre_op_iol_power
                    val eye_pre_op_nil_mouth = data.eye_pre_op_nil_mouth
                    val eye_pre_op_notes = data.eye_pre_op_notes
                    val eye_pre_op_o2_saturation = data.eye_pre_op_o2_saturation
                    val eye_pre_op_o2_saturation_interpretation = data.eye_pre_op_o2_saturation_interpretation
                    val eye_pre_op_other = data.eye_pre_op_other
                    val eye_pre_op_other_detail = data.eye_pre_op_other_detail
                    val eye_pre_op_plain_tropical = data.eye_pre_op_plain_tropical
                    val eye_pre_op_pt = data.eye_pre_op_pt
                    val eye_pre_op_recommendation = data.eye_pre_op_recommendation
                    val eye_pre_op_recommendation_detail = data.eye_pre_op_recommendation_detail
                    val eye_pre_op_symptoms = data.eye_pre_op_symptoms
                    val eye_pre_op_temp = data.eye_pre_op_temp
                    val eye_pre_op_temp_unit = data.eye_pre_op_temp_unit
                    val eye_pre_op_tropical_drop = data.eye_pre_op_tropical_drop
                    val eye_pre_op_tropicamide = data.eye_pre_op_tropicamide
                    val eye_pre_op_wash_face = data.eye_pre_op_wash_face
                    val eye_pre_op_xylocaine = data.eye_pre_op_xylocaine
                    val eye_pre_op_xylocaine_detail = data.eye_pre_op_xylocaine_detail
                    val eye_pre_op_xylocaine_other = data.eye_pre_op_xylocaine_other
                    val eye_pre_op_xylocaine_result = data.eye_pre_op_xylocaine_result
                    val patient_id = data.patient_id
                    val user_id = data.user_id
                    val isSyn = data.isSyn
                    val imageType = data.image_type
                    val eyePreOpNotesImagepath = data.eyePreOpNotesImagepath

                    val eyePreOpNote = Eye_Pre_Op_Notes(
                        _id,
                        camp_id,
                        createdDate,
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
                        eye_pre_op_identify_eye,
                        eye_pre_op_iol_power,
                        eye_pre_op_nil_mouth,
                        eye_pre_op_notes,
                        eye_pre_op_o2_saturation,
                        eye_pre_op_o2_saturation_interpretation,
                        eye_pre_op_other,
                        eye_pre_op_other_detail,
                        eye_pre_op_plain_tropical,
                        eye_pre_op_pt,
                        eye_pre_op_recommendation,
                        eye_pre_op_recommendation_detail,
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
                        patient_id,
                        user_id,
                        eyePreOpNotesImagepath,
                        imageType,
                        isSyn
                    )
                    viewModel1.deleteEyePreOpNotes(eyePreOpNote)
                }
            }
        })
    }

    private fun Clear_Eye_Pre_Op_Investigation() {
        viewModel1.allEye_Pre_Op_Investigation.observe(this, Observer { response ->
            val sizeData = response.size
            if (sizeData > 0) {
                val eyePreOpInvestiogation = mutableListOf<EyePreOpInvestigation>()
                for (i in 0 until response.size) {
                    val data = response[i]
                    val _id = data._id
                    val camp_id = data.camp_id
                    val createdDate = data.createdDate
                    val opd_eye_av_left = data.opd_eye_av_left
                    val opd_eye_av_left_unit = data.opd_eye_av_left_unit
                    val opd_eye_av_right = data.opd_eye_av_right
                    val opd_eye_av_right_unit = data.opd_eye_av_right_unit
                    val opd_eye_blood_pressure_diastolic = data.opd_eye_blood_pressure_diastolic
                    val opd_eye_blood_pressure_interpretation = data.opd_eye_blood_pressure_interpretation
                    val opd_eye_blood_pressure_systolic = data.opd_eye_blood_pressure_systolic
                    val opd_eye_blood_sugar_fasting = data.opd_eye_blood_sugar_fasting
                    val opd_eye_blood_sugar_interpretation = data.opd_eye_blood_sugar_interpretation
                    val opd_eye_blood_sugar_pp = data.opd_eye_blood_sugar_pp
                    val opd_eye_bt = data.opd_eye_bt
                    val opd_eye_cbc = data.opd_eye_cbc
                    val opd_eye_ecg = data.opd_eye_ecg
                    val opd_eye_fa_left = data.opd_eye_fa_left
                    val opd_eye_fa_right = data.opd_eye_fa_right
                    val opd_eye_ha_left = data.opd_eye_ha_left
                    val opd_eye_ha_left_unit = data.opd_eye_ha_left_unit
                    val opd_eye_ha_right = data.opd_eye_ha_right
                    val opd_eye_ha_right_unit = data.opd_eye_ha_right_unit
                    val opd_eye_haemoglobin = data.opd_eye_haemoglobin
                    val opd_eye_haemoglobin_interpretation = data.opd_eye_haemoglobin_interpretation
                    val opd_eye_hbsag = data.opd_eye_hbsag
                    val opd_eye_hcv = data.opd_eye_hcv
                    val opd_eye_hiv = data.opd_eye_hiv
                    val opd_eye_iol_power = data.opd_eye_iol_power
                    val opd_eye_iop_left = data.opd_eye_iop_left
                    val opd_eye_iop_right = data.opd_eye_iop_right
                    val opd_eye_mv_left = data.opd_eye_mv_left
                    val opd_eye_mv_right = data.opd_eye_mv_right
                    val opd_eye_pt = data.opd_eye_pt
                    val opd_eye_slit_location = data.opd_eye_slit_location
                    val opd_eye_slit_location_description = data.opd_eye_slit_location_description
                    val opd_eye_sv_left = data.opd_eye_sv_left
                    val opd_eye_sv_right = data.opd_eye_sv_right
                    val opd_eye_tv_left = data.opd_eye_tv_left
                    val opd_eye_tv_right = data.opd_eye_tv_right
                    val opd_eye_va_left = data.opd_eye_va_left
                    val opd_eye_va_left_unit = data.opd_eye_va_left_unit
                    val opd_eye_va_right = data.opd_eye_va_right
                    val opd_eye_va_right_unit = data.opd_eye_va_right_unit
                    val patient_id = data.patient_id
                    val user_id = data.user_id
                    val opd_eye_ct = data.opd_eye_ct
                    val isSyn = data.isSyn

                    val eyePreOpInvestigation = Eye_Pre_Op_Investigation(
                        _id,
                        camp_id!!,
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
                        patient_id!!,
                        user_id!!,
                        isSyn
                    )
                    viewModel1.deleteEyePreOPInvestigation(eyePreOpInvestigation)
                }
            }
        })
    }

    private fun Clear_Eye_OPD_Doctors_Note() {
        viewModel1.allEye_OPD_Doctors_Note.observe(this, Observer { response ->
            val sizeData = response.size
            if (sizeData > 0) {
                val eyeOPDDoctorNoteList = mutableListOf<Eyeopddocnote>()
                for (i in 0 until response.size) {
                    val data = response[i]
                    val _id = data._id
                    val camp_id = data.camp_id
                    val createdDate = data.createdDate
                    val opd_eye_diagnosis = data.opd_eye_diagnosis
                    val opd_eye_diagnosis_description = data.opd_eye_diagnosis_description
                    val opd_eye_examination = data.opd_eye_examination
                    val opd_eye_examination_description = data.opd_eye_examination_description
                    val opd_eye_notes = data.opd_eye_notes
                    val opd_eye_recommended = data.opd_eye_recommended
                    val opd_eye_symptoms = data.opd_eye_symptoms
                    val opd_eye_symptoms_description = data.opd_eye_symptoms_description
                    val patient_id = data.patient_id
                    val userId = data.user_id
                    val isSyn = data.isSyn

                    val eyeOpdDoctorsNote = Eye_OPD_Doctors_Note(
                        _id,
                        camp_id,
                        createdDate,
                        opd_eye_diagnosis,
                        opd_eye_diagnosis_description,
                        opd_eye_examination,
                        opd_eye_examination_description,
                        opd_eye_notes,
                        opd_eye_recommended,
                        opd_eye_symptoms,
                        opd_eye_symptoms_description,
                        patient_id,
                        userId,
                        isSyn
                    )
                    viewModel1.deleteEyeOPDDoctorNote(eyeOpdDoctorsNote)
                }
            }
        })
    }

    private fun ClearRefractiveError() {
        viewModel1.allRefractive_Error.observe(this, Observer { response ->
            val sizeData = response.size
            if (sizeData > 0) {
                val Refractive_ErrorList = mutableListOf<RefractiveError>()
                for (i in 0 until response.size) {
                    val data = response[i]
                    val _id = data._id
                    val camp_id = data.camp_id
                    val createdDate = data.createdDate
                    val fundus_notes = data.fundus_notes
                    val is_given = data.is_given
                    val is_ordered = data.is_ordered
                    val patient_id = data.patient_id
                    val presc_type = data.presc_type
                    val re_bvd = data.re_bvd
                    val re_distant_vision_axis_left = data.re_distant_vision_axis_left
                    val re_distant_vision_axis_right = data.re_distant_vision_axis_right
                    val re_distant_vision_cylinder_left = data.re_distant_vision_cylinder_left
                    val re_distant_vision_cylinder_right = data.re_distant_vision_cylinder_right
                    val re_distant_vision_left = data.re_distant_vision_left
                    val re_distant_vision_right = data.re_distant_vision_right
                    val re_distant_vision_sphere_left = data.re_distant_vision_sphere_left
                    val re_distant_vision_sphere_right = data.re_distant_vision_sphere_right
                    val re_distant_vision_unit_left = data.re_distant_vision_unit_left
                    val re_distant_vision_unit_right = data.re_distant_vision_unit_right
                    val re_near_vision_axis_left = data.re_near_vision_axis_left
                    val re_near_vision_axis_right = data.re_near_vision_axis_right
                    val re_near_vision_cylinder_left = data.re_near_vision_cylinder_left
                    val re_near_vision_cylinder_right = data.re_near_vision_cylinder_right
                    val re_near_vision_left = data.re_near_vision_left
                    val re_near_vision_right = data.re_near_vision_right
                    val re_near_vision_sphere_left = data.re_near_vision_sphere_left
                    val re_near_vision_sphere_right = data.re_near_vision_sphere_right
                    val re_prism_left = data.re_prism_left
                    val re_prism_right = data.re_prism_right
                    val re_prism_unit_left = data.re_prism_unit_left
                    val re_prism_unit_right = data.re_prism_unit_right
                    val re_pupipllary_distance = data.re_pupipllary_distance
                    val re_reading_addition_left = data.re_reading_addition_left
                    val re_reading_addition_left_details = data.re_reading_addition_left_details
                    val re_reading_addition_right = data.re_reading_addition_right
                    val re_reading_addition_right_details = data.re_reading_addition_right_details
                    val re_remark_left = data.re_remark_left
                    val re_remark_right = data.re_remark_right
                    val re_remarks = data.re_remarks
                    val reading_glass = data.reading_glass
                    val frameCode = data.frameCode
                    val frameColor = data.frameColor
                    val userId = data.userId
                    val isSyn = data.isSyn

                    val RefractiveError =
                        org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError(
                            _id,
                            camp_id,
                            createdDate,
                            fundus_notes,
                            is_given,
                            is_ordered,
                            patient_id,
                            presc_type,
                            re_bvd,
                            re_distant_vision_axis_left,
                            re_distant_vision_axis_right,
                            re_distant_vision_cylinder_left,
                            re_distant_vision_cylinder_right,
                            re_distant_vision_left,
                            re_distant_vision_right,
                            re_distant_vision_sphere_left,
                            re_distant_vision_sphere_right,
                            re_distant_vision_unit_left,
                            re_distant_vision_unit_right,
                            re_near_vision_axis_left,
                            re_near_vision_axis_right,
                            re_near_vision_cylinder_left,
                            re_near_vision_cylinder_right,
                            re_near_vision_left,
                            re_near_vision_right,
                            re_near_vision_sphere_left,
                            re_near_vision_sphere_right,
                            re_prism_left,
                            re_prism_right,
                            re_prism_unit_left,
                            re_prism_unit_right,
                            re_pupipllary_distance,
                            re_reading_addition_left,
                            re_reading_addition_left_details,
                            re_reading_addition_right,
                            re_reading_addition_right_details,
                            re_remark_left,
                            re_remark_right,
                            re_remarks,
                            reading_glass,
                            frameCode,
                            frameColor,
                            userId,
                            isSyn
                        )
                    viewModel1.deleteRefractiveError(RefractiveError)
                }
            }
        })
    }

    private fun getTotalCountOfAllForm(viewModel1: LLE_MedDocket_ViewModel, onComplete: (TotalCountDataModel) -> Unit) {
        val totalCountDataModel = TotalCountDataModel()
        var completedObservers = 0
        val totalObservers = 23
        fun checkAndReturn() {
            completedObservers++
            if (completedObservers == totalObservers) {
                onComplete(totalCountDataModel)
            }
        }
        viewModel1.allVitals.observe(this) {
            totalCountDataModel.Total_Vital = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        eyeVitalsFormViewModel.allVitals.observe(this) {
            totalCountDataModel.Total_Vital2 = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        eyeVisualAcuityViewModel.allVisualAcuity.observe(this) {
            totalCountDataModel.Total_Visual2 = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        viewModel1.allVisualAcuity.observe(this) {
            totalCountDataModel.Total_Visual = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        eyeOpdFormViewModel.allOPD_Investigations.observe(this) {
            totalCountDataModel.Total_OPD_Investigations2 = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        viewModel1.allOPD_Investigations.observe(this) {
            totalCountDataModel.Total_OPD_Investigations = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        eyeRefractiveErrorViewModel.allRefractive_Error.observe(this) {
            totalCountDataModel.Total_Refractive2 = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        viewModel1.allRefractive_Error.observe(this) {
            totalCountDataModel.Total_Refractive = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        viewModel1.allEye_OPD_Doctors_Note.observe(this) {
            totalCountDataModel.Total_Eye_OPD_Doctors_Note = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        viewModel1.allEye_Pre_Op_Investigation.observe(this) {
            totalCountDataModel.Total_Eye_Pre_Op_Investigation = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        viewModel1.allEye_Pre_Op_Notes.observe(this) {
            totalCountDataModel.Total_Eye_Pre_Op_Notes = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        viewModel1.allCataract_Surgery_Notes.observe(this) {
            totalCountDataModel.Total_Cataract_Surgery_Notes = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        viewModel1.allEye_Post_Op_AND_Follow_ups.observe(this) {
            totalCountDataModel.Total_Eye_Post_Op_AND_Follow_ups = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        entPreOpDetailsViewModel.All_Ent_Pro_Op_Follow_ups.observe(this) {
            totalCountDataModel.Total_Ent_Pro_Op_Follow_ups = it.count { it.app_id.isNullOrBlank() }
            checkAndReturn()
        }
        entPostOpNotesViewModel.All_Ent_Post_Follow_ups.observe(this) {
            totalCountDataModel.Total_Ent_Post_Op_Follow_ups = it.count { it.app_id.isNullOrBlank() }
            checkAndReturn()
        }
        entSurgicalNotesViewModel.All_Ent_Surgical_Follow_ups.observe(this) {
            totalCountDataModel.Total_Ent_Audiometry_Follow_ups = it.count { it.app_id.isNullOrBlank() }
            checkAndReturn()
        }
        entOpdDoctorsNoteViewModel.All_Ent_Opd_Doctor_Follow_ups.observe(this) {
            totalCountDataModel.Total_Ent_Surgical_Follow_ups = it.count { it.app_id.isNullOrBlank() }
            checkAndReturn()
        }
        entAudiometryViewModel.All_Ent_Audiometry_Follow_ups.observe(this) {
            totalCountDataModel.Total_Ent_Doctor_Notes_Follow_ups = it.count { it.app_id.isNullOrBlank() }
            checkAndReturn()
        }
        pathologyViewModel.All_PATHOLOGY_Follow_ups.observe(this) {
            totalCountDataModel.Pathology_Report = it.count { it.app_id.isNullOrBlank() }
            checkAndReturn()
        }

        // IMAGE DATA STARTS HERE
        viewModel1.allImages.observe(this) {
            totalCountDataModel.Total_Image = it.count { it.isSyn == 0 }
            checkAndReturn()
        }
        entAudiometryViewModel.getAllAudiometryImages.observe(this) {
            Log.d("ImageLog", "Audiometry images: ${it.size}")
            totalCountDataModel.Total_Audiometry_Image = it.count { it.app_id.isNullOrBlank() }
            checkAndReturn()
        }
        pathologyViewModel.getAllPathologyImages.observe(this) {
            Log.d("ImageLog", "Pathology images: ${it.size}")
            totalCountDataModel.Total_Pathology_Image = it.count { it.app_id.isNullOrBlank() }
            checkAndReturn()
        }
        entPreOpDetailsViewModel.getAllPreOpImages.observe(this) {
            Log.d("ImageLog", "Preop images: ${it.size}")
            totalCountDataModel.Total_Preop_Image = it.count { it.app_id.isNullOrBlank() }
            checkAndReturn()
        }
    }

    private suspend fun entSendImageToServer() {
        loadAndUploadAudiometryImage()
        loadAndUploadPathologyImage()
        loadAndUploadPreOpImage()
        getUpdateAudiometryImageDetailsFromServer()
        getUpdatePreOPImageDetailsFromServer()
        getUpdatedPathologyImageDetailsFromServer()
    }

    private fun getUpdateEntDataFromServer(){
        getUpdatePreOpDetailsFromServer()
        getUpdateSurgicalNotesFromServer()
        getUpdatePostOpNotesFromServer()
        getUpdateAudiometryDetailsFromServer()
        getUpdateSymptomsFromServer()
        getUpdateImpressionFromServer()
        getUpdateDoctorInvestigationServer()
        getUpdatePathologyDetailsServer()
    }

    private suspend fun entSendDataToServer() {
        insertEntOpdDoctorSymptomsNote()
        insertEntOpdDoctorImpressionNote()
        insertEntOpdDoctorInvestigationNote()
        insertPreOpDetails()
        insertSurgicalNotes()
        insertPostOpNotes()
        insertAudiometryDetails()
        insertPythologyDetails()
        syncAllReportData()
    }

    private fun Send_Local_Data_To_Server() {
        lifecycleScope.launch {
            syncNewVitals()
            syncNewOpdForm()
            syncNewVisualAcuity()
            newRefractiveUpdateData()
            Insert_Eye_OPD_Doctors_Note()
            Insert_Eye_Pre_Op_Investigation()
            Insert_Eye_Pre_Op_Notes()
            SurgicalNotes()
            Eye_Post_OP_And_Follow_Ups()
            ImageUpload1()
            syncAllReportData()
        }
    }

    private fun syncAllReportData() {
        try {
            if (!isSyned) {
                SynedData()
                isSyned = true
            } else {
                Log.d("ABCDEF", "syncAllReportData() -> Already synced, skipping...")
            }
        } catch (e: Exception) {
            Log.d("ABCDEF", "syncAllReportData() -> Error: ${e.message}")
        }
    }

    private fun SynedData() {
        Log.d("ABCDEF", "SynedData() -> Called")
        Log.d("ABCDEF", "vitalsUploaded => $vitalsUploaded")
        val (patientId, campIdLocal, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
        Log.d("ABCDEF", "campIdLocal => $campIdLocal , userId => $userId")
        if (campIdLocal == null || userId.isNullOrBlank()) {
            Log.d("ABCDEF", "SynedData() -> campId or userId is null/blank. Skipping sync.")
            return
        }
        val synTableRequest = SynTable(
            0,
            "Form",
            campIdLocal,  // already safe Int
            userId,
            ConstantsApp.getCurrentOnlyDate(),
            ConstantsApp.getCurrentTime()
        )
        Log.d("ABCDEF", "SynedData() -> Request Created: $synTableRequest")
        viewModel1.insertSynedData(synTableRequest) { success ->
            when (success) {
                1 -> {
                    Log.d("ABCDEF", "insertSynedData() -> Success")
                    getAllSynTableHistory()
                }
                0 -> {
                    Log.d("ABCDEF", "insertSynedData() -> Failed")
                }
            }
        }
    }

    private fun getAllSynTableHistory() {
        Log.d("ABCDEF", "getAllSynTableHistory() -> Fetching data...")
        viewModel1.allSynData.observe(this, Observer { synedDataList ->
            Log.d("ABCDEF", "getAllSynTableHistory() -> Data size: ${synedDataList.size}")
            val SynedDataList = mutableListOf<SynedDataLive>()
            for (data in synedDataList) {
                Log.d("ABCDEF", "Loop item -> $data")
                if (data.isSyn == 0) {
                    SynedDataList.add(
                        SynedDataLive(data._id, data.syn_type, data.camp_id, data.user_id, data.date, data.time)
                    )
                }
            }
            if (SynedDataList.isNotEmpty()) {
                val data = SynedDataModel(SynedDataList)
                Log.d("ABCDEF", "getAllSynTableHistory() -> Unsynced Data: $data")
                viewModel.insertSynedData(progressDialog, data)
                Insert_SynedData_Response()
            } else {
                Log.d("ABCDEF", "getAllSynTableHistory() -> No unsynced data found")
            }
        })
    }

    private fun Insert_SynedData_Response() {
        Log.d("ABCDEF", "Insert_SynedData_Response() -> Observing response...")
        viewModel.uploadSynedDataResponse.observe(this, Observer { response ->
            when (response) {
                is ResourceApp.Success -> {
                    Log.d("ABCDEF", "Insert_SynedData_Response() -> Success response")
                    progressDialog.dismiss()
                    try {
                        if (response.data!!.ErrorMessage == "Success") {
                            Log.d("ABCDEF", "Insert_SynedData_Response() -> Server Success")
                            val data = response.data
                            for (item in data.lleSyncReport) {
                                Log.d("ABCDEF", "Insert_SynedData_Response() -> Updating id=${item._id}")
                                updateSynedData(item._id.toInt(), 1)
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("ABCDEF", "Insert_SynedData_Response() -> Exception: ${e.message}")
                    }
                }
                is ResourceApp.Error -> {
                    Log.d("ABCDEF", "Insert_SynedData_Response() -> Error: ${response.data}")
                    progressDialog.dismiss()
                }
                is ResourceApp.Loading -> {
                    Log.d("ABCDEF", "Insert_SynedData_Response() -> Loading...")
                    progressDialog.show()
                }
            }
        })
    }

    private fun updateSynedData(toInt: Int, syn: Int) {
        Log.d("ABCDEF", "updateSynedData() -> Updating id=$toInt with isSyn=$syn")
        viewModel1.updateSynedData(toInt, syn)
    }

    private fun getAllLocalImages() {
        viewModel1.allImages.observe(this, Observer { response ->
            if (currentImageIndex < response.size) {
                val currentImage = response[currentImageIndex]
                Log.d(ConstantsApp.TAG, "file_path=>" + currentImage.file_path)
                val internalFile = File(this.filesDir, "your_internal_image.jpg")
                val filePath = currentImage.file_path
                val file = File(filePath)
                val file_exits = file.exists()
                Log.d(ConstantsApp.TAG, "file_exits=>" + file_exits)
                NewCode()
                if (filePath != null) {
                    Log.d(ConstantsApp.TAG, "filename=>" + getFileNameFromPath(filePath))
                    val internalFilePath = getInternalFilePath(getFileNameFromPath(filePath))
                    if (doesFileExist(filePath)) {
                        Log.d(ConstantsApp.TAG, "internalFilePath=>" + internalFilePath)
                        uploadImageData(internalFilePath, currentImage)
                    } else {
                        val externalFilePath = getExternalFilePath(getFileNameFromPath(filePath))
                        Log.d(ConstantsApp.TAG, "externalFilePath=>" + externalFilePath)
                    }
                }
            } else {
                Log.d(ConstantsApp.TAG, "All images uploaded successfully")
            }
        })
    }

    private fun NewCode() {
        val newFolderName = "lle_images"
        val destinationDirectory = File(Environment.getExternalStorageDirectory(), newFolderName)
        if (!destinationDirectory.exists()) {
            val directoryCreated = destinationDirectory.mkdirs()
            if (!directoryCreated) {
                return
            }
        }
        val originalFilePath =
            "/path/to/your/original/image.jpg" // Replace with the actual file path
        val newFileName = "new_image.jpg"
        val originalFile = File(originalFilePath)
        val destinationFile = File(destinationDirectory, newFileName)
        try {
            FileInputStream(originalFile).use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun uploadImageData(FilePath: String, currentImage: ImageModel) {
        val file = File(FilePath)
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file_name", file.name, requestFile)
        val imageTypeRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentImage.image_type!!)
        val patientIdRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentImage.patient_id.toString())
        val campIdRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentImage.camp_id.toString())
        val userIdRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentImage.user_id.toString())
        val idRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentImage._id.toString())
        val imageUploadParams = ImageUploadParams(
            body,
            imageTypeRequestBody,
            patientIdRequestBody,
            campIdRequestBody,
            userIdRequestBody,
            idRequestBody
        )
        viewModel.uploadFile1(progressDialog, imageUploadParams)
        ImageUploadResponseSequentially()
    }

    private fun ImageUploadResponseSequentially() {
        viewModel.getImagePrescriptionUploadResponse.observe(this, Observer { response ->
            val data = response.data
            if (data != null) {
                val ErrorMessage = data.ErrorMessage
                val id = data._id
                val ErrorCode = data.ErrorCode
                Log.d(ConstantsApp.TAG, "ErrorMessage => $ErrorMessage")
                Log.d(ConstantsApp.TAG, "ErrorCode => $ErrorCode")
                Log.d(ConstantsApp.TAG, "id => $id")
                when (ErrorMessage) {
                    "Success" -> {
                        UpdateImage(id)
                    }
                }
            } else {
                Log.e(ConstantsApp.TAG, "Response data is null")
            }
        })
    }

    private fun UpdateImage(id: String) {
        viewModel1.updateImage(id, 1)
    }

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
    }

    private fun newRefractiveUpdateData() {
        if (isInternetAvailable(this)) {
            val data = HashMap<String, Any>()
            data["patient"] = ""
            val intent = Intent(this, RefractiveFormService::class.java).apply {
                putExtra("QUERY_PARAMS", data)
            }
            startService(intent)
        } else {
            Utility.infoToast(this@MainActivity, "Internet Not Available")
        }
    }

    private fun Eye_Post_OP_And_Follow_Ups() {
        viewModel1.fetchUnsyncedPostOPNotes { unsyncedList ->
            Log.d("pawan_sync", "Unsynced records count: ${unsyncedList.size}")
            if (unsyncedList.isNotEmpty()) {
                val requestList = unsyncedList.map { data ->
                    EyePostOp(
                        _id = data._id,
                        camp_id = data.camp_id,
                        createdDate = data.createdDate,
                        eye_post_op_2nd_date = data.eye_post_op_2nd_date,
                        eye_post_op_3rd_date = data.eye_post_op_3rd_date,
                        eye_post_op_addi_detail_left = data.eye_post_op_addi_detail_left,
                        eye_post_op_addi_detail_right = data.eye_post_op_addi_detail_right,
                        eye_post_op_asses_imedi = data.eye_post_op_asses_imedi,
                        eye_post_op_assess_catract = data.eye_post_op_assess_catract,
                        eye_post_op_assess_catract_detail = data.eye_post_op_assess_catract_detail,
                        eye_post_op_bp = data.eye_post_op_bp,
                        eye_post_op_check_pupil = data.eye_post_op_check_pupil,
                        eye_post_op_check_pupil_detail = data.eye_post_op_check_pupil_detail,
                        eye_post_op_cifloxacin = data.eye_post_op_cifloxacin,
                        eye_post_op_cifloxacin_detail = data.eye_post_op_cifloxacin_detail,
                        eye_post_op_counseling = data.eye_post_op_counseling,
                        eye_post_op_diclofenac = data.eye_post_op_diclofenac,
                        eye_post_op_diclofenac_detail = data.eye_post_op_diclofenac_detail,
                        eye_post_op_dimox = data.eye_post_op_dimox,
                        eye_post_op_dimox_detail = data.eye_post_op_dimox_detail,
                        eye_post_op_discharge_check = data.eye_post_op_discharge_check,
                        eye_post_op_distant_vision_left = data.eye_post_op_distant_vision_left,
                        eye_post_op_distant_vision_right = data.eye_post_op_distant_vision_right,
                        eye_post_op_distant_vision_unit_left = data.eye_post_op_distant_vision_unit_left,
                        eye_post_op_distant_vision_unit_right = data.eye_post_op_distant_vision_unit_right,
                        eye_post_op_early_post_op = data.eye_post_op_early_post_op,
                        eye_post_op_ed_homide = data.eye_post_op_ed_homide,
                        eye_post_op_ed_homide_detail = data.eye_post_op_ed_homide_detail,
                        eye_post_op_eye_1 = data.eye_post_op_eye_1,
                        eye_post_op_eye_1_detail = data.eye_post_op_eye_1_detail,
                        eye_post_op_eye_2 = data.eye_post_op_eye_2,
                        eye_post_op_eye_2_detail = data.eye_post_op_eye_2_detail,
                        eye_post_op_eye_3 = data.eye_post_op_eye_3,
                        eye_post_op_eye_3_detail = data.eye_post_op_eye_3_detail,
                        eye_post_op_eye_4 = data.eye_post_op_eye_4,
                        eye_post_op_eye_4_detail = data.eye_post_op_eye_4_detail,
                        eye_post_op_eye_5 = data.eye_post_op_eye_5,
                        eye_post_op_eye_5_detail = data.eye_post_op_eye_5_detail,
                        eye_post_op_fundus_exam = data.eye_post_op_fundus_exam,
                        eye_post_op_fundus_pathology = data.eye_post_op_fundus_pathology,
                        eye_post_op_head_position = data.eye_post_op_head_position,
                        eye_post_op_homide = data.eye_post_op_homide,
                        eye_post_op_homide_detail = data.eye_post_op_homide_detail,
                        eye_post_op_hypersol = data.eye_post_op_hypersol,
                        eye_post_op_hypersol_detail = data.eye_post_op_hypersol_detail,
                        eye_post_op_last_date = data.eye_post_op_last_date,
                        eye_post_op_location_centration = data.eye_post_op_location_centration,
                        eye_post_op_lubricant = data.eye_post_op_lubricant,
                        eye_post_op_lubricant_detail = data.eye_post_op_lubricant_detail,
                        eye_post_op_moxifloxacin = data.eye_post_op_moxifloxacin,
                        eye_post_op_moxifloxacin_detail = data.eye_post_op_moxifloxacin_detail,
                        eye_post_op_near_vision_left = data.eye_post_op_near_vision_left,
                        eye_post_op_near_vision_right = data.eye_post_op_near_vision_right,
                        eye_post_op_other = data.eye_post_op_other,
                        eye_post_op_other_detail = data.eye_post_op_other_detail,
                        eye_post_op_pantaprezol = data.eye_post_op_pantaprezol,
                        eye_post_op_pantaprezol_detail = data.eye_post_op_pantaprezol_detail,
                        eye_post_op_pr = data.eye_post_op_pr,
                        eye_post_op_pressure_regular = data.eye_post_op_pressure_regular,
                        eye_post_op_rr = data.eye_post_op_rr,
                        eye_post_op_slit_lamp_exam = data.eye_post_op_slit_lamp_exam,
                        eye_post_op_temp = data.eye_post_op_temp,
                        eye_post_op_temp_unit = data.eye_post_op_temp_unit,
                        eye_post_op_timolol = data.eye_post_op_timolol,
                        eye_post_op_timolol_detail = data.eye_post_op_timolol_detail,
                        eye_post_op_w_addi_detail_left = data.eye_post_op_w_addi_detail_left,
                        eye_post_op_w_addi_detail_right = data.eye_post_op_w_addi_detail_right,
                        eye_post_op_w_distant_vision_left = data.eye_post_op_w_distant_vision_left,
                        eye_post_op_w_distant_vision_right = data.eye_post_op_w_distant_vision_right,
                        eye_post_op_w_distant_vision_unit_left = data.eye_post_op_w_distant_vision_unit_left,
                        eye_post_op_w_distant_vision_unit_right = data.eye_post_op_w_distant_vision_unit_right,
                        eye_post_op_w_near_vision_left = data.eye_post_op_w_near_vision_left,
                        eye_post_op_w_near_vision_right = data.eye_post_op_w_near_vision_right,
                        eye_post_op_w_pinhole_improve_left = data.eye_post_op_w_pinhole_improve_left,
                        eye_post_op_w_pinhole_improve_right = data.eye_post_op_w_pinhole_improve_right,
                        eye_post_op_w_pinhole_improve_unit_left = data.eye_post_op_w_pinhole_improve_unit_left,
                        eye_post_op_w_pinhole_improve_unit_right = data.eye_post_op_w_pinhole_improve_unit_right,
                        eye_post_op_w_pinhole_left = data.eye_post_op_w_pinhole_left,
                        eye_post_op_w_pinhole_right = data.eye_post_op_w_pinhole_right,
                        patient_id = data.patient_id,
                        user_id = data.userId
                    )
                }
                val request = EyePostAndFollowrequest(requestList)
                viewModel.insertEyePostOpAndFollowUps(progressDialog, request)
                viewModel.getEyePostAndFollowUpResponse.observe(this) { response ->
                    if (response is ResourceApp.Success && response.data?.ErrorMessage == "Success") {
                        unsyncedList.forEach {
                            viewModel1.UpdatePostOpAndFollowUpsResponse(it._id.toString(), 1)
                        }
                    }
                }
            }
        }
    }

    private fun SurgicalNotes() {
        viewModel1.fetchUnsyncedSurgicalNotes { unsyncedList ->
            Log.d("pawan_sync", "Unsynced records count: ${unsyncedList.size}")

            if (unsyncedList.isNotEmpty()) {
                val requestList = unsyncedList.map { data ->
                    CataractSurgery(
                        _id = data._id,
                        camp_id = data.camp_id,
                        createdDate = data.createdDate,
                        patient_id = data.patient_id,
                        sn_airway = data.sn_airway,
                        sn_airway_detail = data.sn_airway_detail,
                        sn_anaesthetist_concern = data.sn_anaesthetist_concern,
                        sn_anticoagulant = data.sn_anticoagulant,
                        sn_anticoagulant_detail = data.sn_anticoagulant_detail,
                        sn_before_incision_all_team = data.sn_before_incision_all_team,
                        sn_before_or_instrument = data.sn_before_or_instrument,
                        sn_before_or_key = data.sn_before_or_key,
                        sn_before_or_key_detail = data.sn_before_or_key_detail,
                        sn_before_or_specimen = data.sn_before_or_specimen,
                        sn_before_or_weather = data.sn_before_or_weather,
                        sn_before_or_weather_detail = data.sn_before_or_weather_detail,
                        sn_cataract_capsulotomy = data.sn_cataract_capsulotomy,
                        sn_cataract_capsulotomy_detail = data.sn_cataract_capsulotomy_detail,
                        sn_cataract_castroviejo = data.sn_cataract_castroviejo,
                        sn_cataract_castroviejo_detail = data.sn_cataract_castroviejo_detail,
                        sn_cataract_colibri = data.sn_cataract_colibri,
                        sn_cataract_colibri_detail = data.sn_cataract_colibri_detail,
                        sn_cataract_formed = data.sn_cataract_formed,
                        sn_cataract_formed_detail = data.sn_cataract_formed_detail,
                        sn_cataract_hydrodissectiirs = data.sn_cataract_hydrodissectiirs,
                        sn_cataract_hydrodissectiirs_detail = data.sn_cataract_hydrodissectiirs_detail,
                        sn_cataract_irrigation = data.sn_cataract_irrigation,
                        sn_cataract_irrigation_detail = data.sn_cataract_irrigation_detail,
                        sn_cataract_keretome = data.sn_cataract_keretome,
                        sn_cataract_keretome_detail = data.sn_cataract_keretome_detail,
                        sn_cataract_keretome_phaco = data.sn_cataract_keretome_phaco,
                        sn_cataract_keretome_phaco_detail = data.sn_cataract_keretome_phaco_detail,
                        sn_cataract_knife = data.sn_cataract_knife,
                        sn_cataract_knife_detail = data.sn_cataract_knife_detail,
                        sn_cataract_lieberman = data.sn_cataract_lieberman,
                        sn_cataract_lieberman_detail = data.sn_cataract_lieberman_detail,
                        sn_cataract_limb = data.sn_cataract_limb,
                        sn_cataract_limb_detail = data.sn_cataract_limb_detail,
                        sn_cataract_mac = data.sn_cataract_mac,
                        sn_cataract_mac_detail = data.sn_cataract_mac_detail,
                        sn_cataract_nucleus = data.sn_cataract_nucleus,
                        sn_cataract_nucleus_detail = data.sn_cataract_nucleus_detail,
                        sn_cataract_sinsky = data.sn_cataract_sinsky,
                        sn_cataract_sinsky_detail = data.sn_cataract_sinsky_detail,
                        sn_cataract_universal = data.sn_cataract_universal,
                        sn_cataract_universal_detail = data.sn_cataract_universal_detail,
                        sn_cataract_viscoelastic = data.sn_cataract_viscoelastic,
                        sn_cataract_viscoelastic_detail = data.sn_cataract_viscoelastic_detail,
                        sn_common_dislocation = data.sn_common_dislocation,
                        sn_common_dislocation_detail = data.sn_common_dislocation_detail,
                        sn_common_endophthalmitis = data.sn_common_endophthalmitis,
                        sn_common_endophthalmitis_detail = data.sn_common_endophthalmitis_detail,
                        sn_common_endothelial = data.sn_common_endothelial,
                        sn_common_endothelial_detail = data.sn_common_endothelial_detail,
                        sn_common_fluid = data.sn_common_fluid,
                        sn_common_fluid_detail = data.sn_common_fluid_detail,
                        sn_common_hyphema = data.sn_common_hyphema,
                        sn_common_hyphema_detail = data.sn_common_hyphema_detail,
                        sn_common_light = data.sn_common_light,
                        sn_common_light_detail = data.sn_common_light_detail,
                        sn_common_macular = data.sn_common_macular,
                        sn_common_macular_detail = data.sn_common_macular_detail,
                        sn_common_ocular = data.sn_common_ocular,
                        sn_common_ocular_detail = data.sn_common_ocular_detail,
                        sn_common_posterior_opacification = data.sn_common_posterior_opacification,
                        sn_common_posterior_opacification_detail = data.sn_common_posterior_opacification_detail,
                        sn_common_posterior_rent = data.sn_common_posterior_rent,
                        sn_common_posterior_rent_detail = data.sn_common_posterior_rent_detail,
                        sn_common_retinal = data.sn_common_retinal,
                        sn_common_retinal_detail = data.sn_common_retinal_detail,
                        sn_common_vitreous = data.sn_common_vitreous,
                        sn_common_vitreous_detail = data.sn_common_vitreous_detail,
                        sn_date_of_surgery = data.sn_date_of_surgery,
                        sn_flomax = data.sn_flomax,
                        sn_has_confirmed_allergies = data.sn_has_confirmed_allergies,
                        sn_has_confirmed_consent = data.sn_has_confirmed_consent,
                        sn_has_confirmed_identity = data.sn_has_confirmed_identity,
                        sn_has_confirmed_procedure = data.sn_has_confirmed_procedure,
                        sn_has_confirmed_site = data.sn_has_confirmed_site,
                        sn_incision_cornea = data.sn_incision_cornea,
                        sn_incision_sclera_1 = data.sn_incision_sclera_1,
                        sn_incision_sclera_2 = data.sn_incision_sclera_2,
                        sn_intra_adrenaline = data.sn_intra_adrenaline,
                        sn_intra_adrenaline_detail = data.sn_intra_adrenaline_detail,
                        sn_intra_combination = data.sn_intra_combination,
                        sn_intra_combination_detail = data.sn_intra_combination_detail,
                        sn_intra_gentamycin = data.sn_intra_gentamycin,
                        sn_intra_gentamycin_detail = data.sn_intra_gentamycin_detail,
                        sn_intra_intasol = data.sn_intra_intasol,
                        sn_intra_intasol_detail = data.sn_intra_intasol_detail,
                        sn_intra_mannitol = data.sn_intra_mannitol,
                        sn_intra_mannitol_detail = data.sn_intra_mannitol_detail,
                        sn_intra_moxifloxacin = data.sn_intra_moxifloxacin,
                        sn_intra_moxifloxacin_detail = data.sn_intra_moxifloxacin_detail,
                        sn_intra_occular_lens = data.sn_intra_occular_lens,
                        sn_intra_prednisolone = data.sn_intra_prednisolone,
                        sn_intra_prednisolone_detail = data.sn_intra_prednisolone_detail,
                        sn_intra_vigamox = data.sn_intra_vigamox,
                        sn_intra_vigamox_detail = data.sn_intra_vigamox_detail,
                        sn_intra_visco = data.sn_intra_visco,
                        sn_intra_visco_detail = data.sn_intra_visco_detail,
                        sn_local_anaesthesia = data.sn_local_anaesthesia,
                        sn_nurse_age = data.sn_nurse_age,
                        sn_nurse_age_unit = data.sn_nurse_age_unit,
                        sn_nurse_anaesthesia = data.sn_nurse_anaesthesia,
                        sn_nurse_anaesthetist = data.sn_nurse_anaesthetist,
                        sn_nurse_bp_diastolic = data.sn_nurse_bp_diastolic,
                        sn_nurse_bp_interpretation = data.sn_nurse_bp_interpretation,
                        sn_nurse_bp_sistolic = data.sn_nurse_bp_sistolic,
                        sn_nurse_concern = data.sn_nurse_concern,
                        sn_nurse_diagnosis = data.sn_nurse_diagnosis,
                        sn_nurse_duration = data.sn_nurse_duration,
                        sn_nurse_equipment_issue = data.sn_nurse_equipment_issue,
                        sn_nurse_implant_detail = data.sn_nurse_implant_detail,
                        sn_nurse_name = data.sn_nurse_name,
                        sn_nurse_orally_confirm = data.sn_nurse_orally_confirm,
                        sn_nurse_rbs = data.sn_nurse_rbs,
                        sn_nurse_rbs_interpretation = data.sn_nurse_rbs_interpretation,
                        sn_nurse_registered = data.sn_nurse_registered,
                        sn_nurse_scrub = data.sn_nurse_scrub,
                        sn_nurse_serial = data.sn_nurse_serial,
                        sn_nurse_sex = data.sn_nurse_sex,
                        sn_nurse_specimen_biopsy = data.sn_nurse_specimen_biopsy,
                        sn_nurse_specimen_detail = data.sn_nurse_specimen_detail,
                        sn_nurse_sterility = data.sn_nurse_sterility,
                        sn_nurse_surgeon = data.sn_nurse_surgeon,
                        sn_nurse_surgery_name = data.sn_nurse_surgery_name,
                        sn_nurse_viral_serology = data.sn_nurse_viral_serology,
                        sn_post_cifloxacin = data.sn_post_cifloxacin,
                        sn_post_cifloxacin_detail = data.sn_post_cifloxacin_detail,
                        sn_post_diclofenac = data.sn_post_diclofenac,
                        sn_post_diclofenac_detail = data.sn_post_diclofenac_detail,
                        sn_post_dimox = data.sn_post_dimox,
                        sn_post_dimox_detail = data.sn_post_dimox_detail,
                        sn_post_eye_1 = data.sn_post_eye_1,
                        sn_post_eye_1_detail = data.sn_post_eye_1_detail,
                        sn_post_eye_2 = data.sn_post_eye_2,
                        sn_post_eye_2_detail = data.sn_post_eye_2_detail,
                        sn_post_eye_3 = data.sn_post_eye_3,
                        sn_post_eye_3_detail = data.sn_post_eye_3_detail,
                        sn_post_eye_4 = data.sn_post_eye_4,
                        sn_post_eye_4_detail = data.sn_post_eye_4_detail,
                        sn_post_eye_5 = data.sn_post_eye_5,
                        sn_post_eye_5_detail = data.sn_post_eye_5_detail,
                        sn_post_eye_homide = data.sn_post_eye_homide,
                        sn_post_eye_homide_detail = data.sn_post_eye_homide_detail,
                        sn_post_eye_hypersol = data.sn_post_eye_hypersol,
                        sn_post_eye_hypersol_detail = data.sn_post_eye_hypersol_detail,
                        sn_post_eye_lubricant = data.sn_post_eye_lubricant,
                        sn_post_eye_lubricant_detail = data.sn_post_eye_lubricant_detail,
                        sn_post_eye_moxifloxacin = data.sn_post_eye_moxifloxacin,
                        sn_post_eye_moxifloxacin_detail = data.sn_post_eye_moxifloxacin_detail,
                        sn_post_eye_timolol = data.sn_post_eye_timolol,
                        sn_post_eye_timolol_detail = data.sn_post_eye_timolol_detail,
                        sn_post_pantaprezol = data.sn_post_pantaprezol,
                        sn_post_pantaprezol_detail = data.sn_post_pantaprezol_detail,
                        sn_site_marked_history = data.sn_site_marked_history,
                        sn_site_marked_pre_anaesthesia = data.sn_site_marked_pre_anaesthesia,
                        sn_site_marked_pre_surgical = data.sn_site_marked_pre_surgical,
                        sn_type_of_surgery = data.sn_type_of_surgery,
                        sn_type_of_surgery_other = data.sn_type_of_surgery_other,
                        sn_unexpected_step = data.sn_unexpected_step,
                        sn_unexpected_step_detail = data.sn_unexpected_step_detail,
                        userId = data.userId
                    )
                }

                val request = SurgicalNotesRequest(requestList)

                viewModel.insertSurgicalData(progressDialog, request)

                viewModel.getSurgicalNotesResponse.observe(this) { response ->
                    if (response is ResourceApp.Success && response.data?.ErrorMessage == "Success") {
                        unsyncedList.forEach {
                            viewModel1.updateCataract_Surgeries(it._id, 1)
                        }
                    }
                }
            }
        }
    }

    private fun Insert_Eye_Pre_Op_Notes() {
        viewModel1.fetchUnsyncedPreOpNotes { unsyncedList ->
            Log.d("pawan_sync", "Unsynced records count: ${unsyncedList.size}")

            if (unsyncedList.isNotEmpty()) {
                val requestList = unsyncedList.map { data ->
                    EyePreOpNote(
                        _id = data._id,
                        camp_id = data.camp_id,
                        createdDate = data.createdDate,
                        eye_pre_op_admission_date = data.eye_pre_op_admission_date,
                        eye_pre_op_alprax = data.eye_pre_op_alprax,
                        eye_pre_op_amlodipine = data.eye_pre_op_amlodipine,
                        eye_pre_op_antibiotic = data.eye_pre_op_antibiotic,
                        eye_pre_op_antibiotic_detail = data.eye_pre_op_antibiotic_detail,
                        eye_pre_op_antibiotic_other = data.eye_pre_op_antibiotic_other,
                        eye_pre_op_antibiotic_result = data.eye_pre_op_antibiotic_result,
                        eye_pre_op_antihyp = data.eye_pre_op_antihyp,
                        eye_pre_op_antihyp_detail = data.eye_pre_op_antihyp_detail,
                        eye_pre_op_betadine = data.eye_pre_op_betadine,
                        eye_pre_op_bp_diastolic = data.eye_pre_op_bp_diastolic,
                        eye_pre_op_bp_interpretation = data.eye_pre_op_bp_interpretation,
                        eye_pre_op_bp_systolic = data.eye_pre_op_bp_systolic,
                        eye_pre_op_bs_f = data.eye_pre_op_bs_f,
                        eye_pre_op_bs_pp = data.eye_pre_op_bs_pp,
                        eye_pre_op_bt = data.eye_pre_op_bt,
                        eye_pre_op_cbc = data.eye_pre_op_cbc,
                        eye_pre_op_ciplox = data.eye_pre_op_ciplox,
                        eye_pre_op_ciplox_drop = data.eye_pre_op_ciplox_drop,
                        eye_pre_op_ct = data.eye_pre_op_ct,
                        eye_pre_op_dia = data.eye_pre_op_dia,
                        eye_pre_op_dia_detail = data.eye_pre_op_dia_detail,
                        eye_pre_op_diamox = data.eye_pre_op_diamox,
                        eye_pre_op_discussed_with = data.eye_pre_op_discussed_with,
                        eye_pre_op_discussed_with_detail = data.eye_pre_op_discussed_with_detail,
                        eye_pre_op_ecg = data.eye_pre_op_ecg,
                        eye_pre_op_flur_eye = data.eye_pre_op_flur_eye,
                        eye_pre_op_haemoglobin = data.eye_pre_op_haemoglobin,
                        eye_pre_op_hbsag = data.eye_pre_op_hbsag,
                        eye_pre_op_hcv = data.eye_pre_op_hcv,
                        eye_pre_op_head_bath = data.eye_pre_op_head_bath,
                        eye_pre_op_heart = data.eye_pre_op_heart,
                        eye_pre_op_heart_detail = data.eye_pre_op_heart_detail,
                        eye_pre_op_heart_rate = data.eye_pre_op_heart_rate,
                        eye_pre_op_historyof = data.eye_pre_op_historyof,
                        eye_pre_op_hiv = data.eye_pre_op_hiv,
                        eye_pre_op_identify_eye = data.eye_pre_op_identify_eye,
                        eye_pre_op_iol_power = data.eye_pre_op_iol_power,
                        eye_pre_op_nil_mouth = data.eye_pre_op_nil_mouth,
                        eye_pre_op_notes = data.eye_pre_op_notes,
                        eye_pre_op_o2_saturation = data.eye_pre_op_o2_saturation,
                        eye_pre_op_o2_saturation_interpretation = data.eye_pre_op_o2_saturation_interpretation,
                        eye_pre_op_other = data.eye_pre_op_other,
                        eye_pre_op_other_detail = data.eye_pre_op_other_detail,
                        eye_pre_op_plain_tropical = data.eye_pre_op_plain_tropical,
                        eye_pre_op_pt = data.eye_pre_op_pt,
                        eye_pre_op_recommendation = data.eye_pre_op_recommendation,
                        eye_pre_op_recommendation_detail = data.eye_pre_op_recommendation_detail,
                        eye_pre_op_symptoms = data.eye_pre_op_symptoms,
                        eye_pre_op_temp = data.eye_pre_op_temp,
                        eye_pre_op_temp_unit = data.eye_pre_op_temp_unit,
                        eye_pre_op_tropical_drop = data.eye_pre_op_tropical_drop,
                        eye_pre_op_tropicamide = data.eye_pre_op_tropicamide,
                        eye_pre_op_wash_face = data.eye_pre_op_wash_face,
                        eye_pre_op_xylocaine = data.eye_pre_op_xylocaine,
                        eye_pre_op_xylocaine_detail = data.eye_pre_op_xylocaine_detail,
                        eye_pre_op_xylocaine_other = data.eye_pre_op_xylocaine_other,
                        eye_pre_op_xylocaine_result = data.eye_pre_op_xylocaine_result,
                        patient_id = data.patient_id,
                        user_id = data.user_id
                    )
                }

                val request = AddEyePreOpNotesRequest(requestList)

                viewModel.insertEyePreOpNotes(request, progressDialog)

                viewModel.getEyePreOpNotesResponse.observe(this) { response ->
                    if (response is ResourceApp.Success && response.data?.ErrorMessage == "Success") {
                        unsyncedList.forEach {
                            viewModel1.updateEye_Pre_Op_Notes(it._id.toString(), 1)
                        }
                    }
                }
            }
        }
    }

    private fun Insert_Eye_Pre_Op_Investigation() {
        viewModel1.fetchUnsyncedInvestigations  { unsyncedList ->
            Log.d("pawan_sync", "Unsynced records count: ${unsyncedList.size}")

            if (unsyncedList.isNotEmpty()) {
                val requestList = unsyncedList.map { data ->
                    EyePreOpInvestigation(
                        _id = data._id,
                        camp_id = data.camp_id!!,
                        createdDate = data.createdDate,
                        opd_eye_av_left = data.opd_eye_av_left,
                        opd_eye_av_left_unit = data.opd_eye_av_left_unit,
                        opd_eye_av_right = data.opd_eye_av_right,
                        opd_eye_av_right_unit = data.opd_eye_av_right_unit,
                        opd_eye_blood_pressure_diastolic = data.opd_eye_blood_pressure_diastolic,
                        opd_eye_blood_pressure_interpretation = data.opd_eye_blood_pressure_interpretation,
                        opd_eye_blood_pressure_systolic = data.opd_eye_blood_pressure_systolic,
                        opd_eye_blood_sugar_fasting = data.opd_eye_blood_sugar_fasting,
                        opd_eye_blood_sugar_interpretation = data.opd_eye_blood_sugar_interpretation,
                        opd_eye_blood_sugar_pp = data.opd_eye_blood_sugar_pp,
                        opd_eye_bt = data.opd_eye_bt,
                        opd_eye_ct = data.opd_eye_ct,
                        opd_eye_cbc = data.opd_eye_cbc,
                        opd_eye_ecg = data.opd_eye_ecg,
                        opd_eye_fa_left = data.opd_eye_fa_left,
                        opd_eye_fa_right = data.opd_eye_fa_right,
                        opd_eye_ha_left = data.opd_eye_ha_left,
                        opd_eye_ha_left_unit = data.opd_eye_ha_left_unit,
                        opd_eye_ha_right = data.opd_eye_ha_right,
                        opd_eye_ha_right_unit = data.opd_eye_ha_right_unit,
                        opd_eye_haemoglobin = data.opd_eye_haemoglobin,
                        opd_eye_haemoglobin_interpretation = data.opd_eye_haemoglobin_interpretation,
                        opd_eye_hbsag = data.opd_eye_hbsag,
                        opd_eye_hcv = data.opd_eye_hcv,
                        opd_eye_hiv = data.opd_eye_hiv,
                        opd_eye_iol_power = data.opd_eye_iol_power,
                        opd_eye_iop_left = data.opd_eye_iop_left,
                        opd_eye_iop_right = data.opd_eye_iop_right,
                        opd_eye_mv_left = data.opd_eye_mv_left,
                        opd_eye_mv_right = data.opd_eye_mv_right,
                        opd_eye_pt = data.opd_eye_pt,
                        opd_eye_slit_location = data.opd_eye_slit_location,
                        opd_eye_slit_location_description = data.opd_eye_slit_location_description,
                        opd_eye_sv_left = data.opd_eye_sv_left,
                        opd_eye_sv_right = data.opd_eye_sv_right,
                        opd_eye_tv_left = data.opd_eye_tv_left,
                        opd_eye_tv_right = data.opd_eye_tv_right,
                        opd_eye_va_left = data.opd_eye_va_left,
                        opd_eye_va_left_unit = data.opd_eye_va_left_unit,
                        opd_eye_va_right = data.opd_eye_va_right,
                        opd_eye_va_right_unit = data.opd_eye_va_right_unit,
                        patient_id = data.patient_id!!,
                        user_id = data.user_id!!
                    )
                }

                val request = AddEyePreOpInvestigationsRequest(requestList)

                viewModel.insertEyePreOPInvestigation(progressDialog, request)

                viewModel.getEyePreOpInvestigationData.observe(this) { response ->
                    if (response is ResourceApp.Success && response.data?.ErrorMessage == "Success") {
                        unsyncedList.forEach {
                            viewModel1.updateEye_Pre_Op_Investigations(it._id.toString(), 1)
                        }
                    }
                }
            }
        }
    }

    private suspend fun insertEntOpdDoctorSymptomsNote() {
        val unsyncedList = entOpdDoctorsNoteViewModel.getUnsyncedSymptomsOnce()
        Log.d("SyncCheck", "Fetched ${unsyncedList.size} unsynced symptom records")
        if (!unsyncedList.isNullOrEmpty()) {
            suspendCoroutine<Unit> { cont ->
                entOpdDoctorsNoteViewModel.sendDoctorSymptomsNotesToServer(unsyncedList) { success, message ->
                    logResult("Symptoms", success, message, unsyncedList.size)
                    cont.resume(Unit)
                }
            }
        }
    }

    private suspend fun insertEntOpdDoctorImpressionNote() {
        val unsyncedList = entOpdDoctorsNoteViewModel.getUnsyncedImpressionOnce()
        Log.d("SyncCheck", "Fetched ${unsyncedList.size} unsynced impression records")
        if (!unsyncedList.isNullOrEmpty()) {
            suspendCoroutine<Unit> { cont ->
                entOpdDoctorsNoteViewModel.sendDoctorImpressionNotesToServer(unsyncedList) { success, message ->
                    logResult("Impression", success, message, unsyncedList.size)
                    cont.resume(Unit)
                }
            }
        }
    }

    private suspend fun insertEntOpdDoctorInvestigationNote() {
        val unsyncedList = entOpdDoctorsNoteViewModel.getUnsyncedInvestigationOnce()
        Log.d("SyncCheck", "Fetched ${unsyncedList.size} unsynced investigation records")
        if (!unsyncedList.isNullOrEmpty()) {
            suspendCoroutine<Unit> { cont ->
                entOpdDoctorsNoteViewModel.sendDoctorInvestigationNotesToServer(unsyncedList) { success, message ->
                    logResult("Investigation", success, message, unsyncedList.size)
                    cont.resume(Unit)
                }
            }
        }
    }

    private suspend fun insertPreOpDetails() {
        val unsyncedList = entPreOpDetailsViewModel.getUnsyncedPreOpDetails()
        Log.d("SyncCheck", "Fetched ${unsyncedList.size} unsynced pre-op records")
        if (!unsyncedList.isNullOrEmpty()) {
            suspendCoroutine<Unit> { cont ->
                entPreOpDetailsViewModel.sendDoctorPreOpDetailsToServer(unsyncedList) { success, message ->
                    logResult("PreOp", success, message, unsyncedList.size)
                    cont.resume(Unit)
                }
            }
        }
    }

    private suspend fun insertSurgicalNotes() {
        val unsyncedList = entSurgicalNotesViewModel.getUnsyncedSurgicalNotes()
        Log.d("SyncCheck", "Fetched ${unsyncedList.size} unsynced surgical notes")
        if (!unsyncedList.isNullOrEmpty()) {
            suspendCoroutine<Unit> { cont ->
                entSurgicalNotesViewModel.sendDoctorSurgicalNotesToServer(unsyncedList) { success, message ->
                    logResult("SurgicalNotes", success, message, unsyncedList.size)
                    cont.resume(Unit)
                }
            }
        }
    }

    private suspend fun insertPostOpNotes() {
        val unsyncedList = entPostOpNotesViewModel.getUnsyncedPostOpNotes()
        Log.d("SyncCheck", "Fetched ${unsyncedList.size} unsynced post-op notes")
        if (!unsyncedList.isNullOrEmpty()) {
            suspendCoroutine<Unit> { cont ->
                entPostOpNotesViewModel.sendDoctorPostOpNotesToServer(unsyncedList) { success, message ->
                    logResult("PostOp", success, message, unsyncedList.size)
                    cont.resume(Unit)
                }
            }
        }
    }

    private suspend fun insertAudiometryDetails() {
        val unsyncedList = entAudiometryViewModel.getUnsyncedAudiometryDetails()
        Log.d("SyncCheck", "Fetched ${unsyncedList.size} unsynced audiometry records")
        if (!unsyncedList.isNullOrEmpty()) {
            suspendCoroutine<Unit> { cont ->
                entAudiometryViewModel.sendDoctorAudiometryDetailsToServer(unsyncedList) { success, message ->
                    logResult("Audiometry", success, message, unsyncedList.size)
                    cont.resume(Unit)
                }
            }
        }
    }

    private suspend fun insertPythologyDetails() {
        val unsyncedList = pathologyViewModel.getUnsyncedPathologyDetails()
        Log.d("SyncCheck", "Fetched ${unsyncedList.size} unsynced pathology records")
        if (!unsyncedList.isNullOrEmpty()) {
            suspendCoroutine<Unit> { cont ->
                pathologyViewModel.sendDoctorPathologyDetailsToServer(unsyncedList) { success, message ->
                    logResult("Pathology", success, message, unsyncedList.size)
                    cont.resume(Unit)
                }
            }
        }
    }


    private fun logResult(tag: String, success: Boolean, message: String?, count: Int) {
        if (success) {
            Toast.makeText(this, "$tag: Successfully sent $count records", Toast.LENGTH_SHORT).show()
            Log.d("SyncCheck $tag", "Successfully sent $count records.")
        } else {
//            Toast.makeText(this, "$tag: Failed - $message", Toast.LENGTH_LONG).show()
            Log.e("SyncCheck $tag", "Sync failed: $message")
        }
    }


    private var equipmentImageListForSync = ArrayList<AudiometryImageEntity>()

    private suspend fun loadAndUploadAudiometryImage() {
        val unsyncedList = withContext(Dispatchers.IO) {
            entAudiometryViewModel.getUnSyncedAudiometryImageDetailsNow()
        }

        if (unsyncedList.isEmpty()) return

        for (imageEntity in unsyncedList) {
            val imageFile = File(imageEntity.filename)
            if (!imageFile.exists()) continue

            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("fileName", imageFile.name, requestFile)

            val patientId = imageEntity.patientId.toString().toRequestBody()
            val campId = imageEntity.campId.toString().toRequestBody()
            val userId = imageEntity.userId.toString().toRequestBody()
            val appCreatedDate = (imageEntity.appCreatedDate ?: "").toRequestBody()
            val appId = (imageEntity.app_id ?: "1").toRequestBody()
            val uniqueId = imageEntity.id.toString().toRequestBody("text/plain".toMediaTypeOrNull())


            try {
                val response = withContext(Dispatchers.IO) {
                    entAudiometryViewModel.syncAudiometryImagesNew(
                        multipartBody, patientId, campId, userId, appCreatedDate, appId, uniqueId
                    )
                }

                if (response.isSuccessful) {
                    withContext(Dispatchers.IO) {
                        entAudiometryViewModel.updateAudiometryImageAppId(imageEntity.id, "1")
                    }
                }

            } catch (e: Exception) {
                Log.e("Audiometry Upload", "Exception: ${e.message}")
            }
        }
    }


    private var pathologyImageListForSync = ArrayList<PathologyImageEntity>()

    private suspend fun loadAndUploadPathologyImage() {
        val unsyncedList = withContext(Dispatchers.IO) {
            pathologyViewModel.getUnSyncedPathologyImageDetailsNow()
        }

        if (unsyncedList.isEmpty()) return

        for (imageEntity in unsyncedList) {
            val imageFile = File(imageEntity.filename)
            if (!imageFile.exists()) continue

            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("fileName", imageFile.name, requestFile)

            val patientId = imageEntity.patientId.toString().toRequestBody()
            val campId = imageEntity.campId.toString().toRequestBody()
            val userId = imageEntity.userId.toString().toRequestBody()
            val appCreatedDate = (imageEntity.appCreatedDate ?: "").toRequestBody()
            val reportType = (imageEntity.reportType ?: "").toRequestBody()
            val appId = (imageEntity.app_id ?: "1").toRequestBody()
            val uniqueId = imageEntity.formId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            try {
                val response = withContext(Dispatchers.IO) {
                    pathologyViewModel.syncPathologyImagesNew(
                        multipartBody, patientId, campId, userId, appCreatedDate, reportType, appId, uniqueId
                    )
                }

                if (response.isSuccessful) {
                    withContext(Dispatchers.IO) {
                        pathologyViewModel.updatePathologyImageAppId(imageEntity.id, "1")
                    }
                }

            } catch (e: Exception) {
                Log.e("Pathology Upload", "Exception: ${e.message}")
            }
        }
    }


    private var preOpImageListForSync = ArrayList<PreOpImageEntity>()

    private suspend fun loadAndUploadPreOpImage() {
        val unsyncedList = withContext(Dispatchers.IO) {
            entPreOpDetailsViewModel.getUnSyncedPreOpImageDetailsNow()
        }

        if (unsyncedList.isEmpty()) return

        for (imageEntity in unsyncedList) {
            val imageFile = File(imageEntity.filename)
            if (!imageFile.exists()) continue

            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("fileName", imageFile.name, requestFile)

            val patientId = imageEntity.patientId.toString().toRequestBody()
            val campId = imageEntity.campId.toString().toRequestBody()
            val userId = imageEntity.userId.toString().toRequestBody()
            val appCreatedDate = (imageEntity.appCreatedDate ?: "").toRequestBody()
            val appId = (imageEntity.app_id ?: "1").toRequestBody()
            val uniqueId = imageEntity.id.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            try {
                val response = withContext(Dispatchers.IO) {
                    entPreOpDetailsViewModel.syncPreOpImagesNew(
                        multipartBody, patientId, campId, userId, appCreatedDate, appId, uniqueId
                    )
                }

                if (response.isSuccessful) {
                    withContext(Dispatchers.IO) {
                        entPreOpDetailsViewModel.updatePreOpImageDetailsAppId(imageEntity.id, "1")
                    }
                }

            } catch (e: Exception) {
                Log.e("PreOp Upload", "Exception: ${e.message}")
            }
        }
    }


    // Get Ent Updated Data From Server
    private fun getUpdatePreOpDetailsFromServer(){
        entPreOpDetailsViewModel.getUpdatePreOpDetailsFromServer()
    }

    private fun getUpdatePreOPImageDetailsFromServer(){
        entPreOpDetailsViewModel.getUpdatePreOpImageDetailsFromServer()
    }

    private fun getUpdateSurgicalNotesFromServer(){
        entSurgicalNotesViewModel.getUpdateSurgicalNotesFromServer()
    }

    private fun getUpdatePostOpNotesFromServer(){
        entPostOpNotesViewModel.getUpdatePostOpNotesFromServer()
    }

    private fun getUpdateAudiometryDetailsFromServer(){
        entAudiometryViewModel.getUpdateAudiometryDetailsFromServer()
    }

    private fun getUpdateAudiometryImageDetailsFromServer(){
        entAudiometryViewModel.getUpdateAudiometryImageDetailsFromServer()
    }


    private fun getUpdateSymptomsFromServer(){
        entOpdDoctorsNoteViewModel.getUpdateSymptomsFromServer()
    }

    private fun getUpdateImpressionFromServer(){
        entOpdDoctorsNoteViewModel.getUpdateImpressionFromServer()
    }

    private fun getUpdateDoctorInvestigationServer(){
        entOpdDoctorsNoteViewModel.getUpdateDoctorInvestigationServer()
    }

    private fun getUpdatePathologyDetailsServer(){
        pathologyViewModel.getUpdatePathologyDetailsServer()
    }

    private fun getUpdatedPathologyImageDetailsFromServer(){
        pathologyViewModel.getUpdatedPathologyImageDetailsFromServer()
    }



    private fun Insert_Eye_OPD_Doctors_Note() {
        viewModel1.fetchUnsyncedOpDoctorNotes { unsyncedList ->
            Log.d("pawan_sync", "Unsynced records count: ${unsyncedList.size}")

            if (unsyncedList.isNotEmpty()) {
                val requestList = unsyncedList.map { data ->
                    Eyeopddocnote(
                        _id = data._id,
                        camp_id = data.camp_id,
                        createdDate = data.createdDate,
                        opd_eye_diagnosis = data.opd_eye_diagnosis,
                        opd_eye_diagnosis_description = data.opd_eye_diagnosis_description,
                        opd_eye_examination = data.opd_eye_examination,
                        opd_eye_examination_description = data.opd_eye_examination_description,
                        opd_eye_notes = data.opd_eye_notes,
                        opd_eye_recommended = data.opd_eye_recommended,
                        opd_eye_symptoms = data.opd_eye_symptoms,
                        opd_eye_symptoms_description = data.opd_eye_symptoms_description,
                        patient_id = data.patient_id,
                        user_id = data.user_id
                    )
                }

                val request = sendEyeOPDDoctorsNoteData(requestList)

                viewModel.insertEyeOPDDoctorNote(progressDialog, request)

                viewModel.getEyeOPDDoctorsNoteData.observe(this) { response ->
                    if (response is ResourceApp.Success && response.data?.ErrorMessage == "Success") {
                        unsyncedList.forEach {
                            viewModel1.updateEyeopddocnotes(it._id.toString(), 1)
                        }
                    }
                }
            } else {
                Log.d("pawan_sync", "No unsynced records found.")
            }
        }
    }



    private fun Clear_Vitals() {
        viewModel1.allVitals.observe(this, Observer { response ->
            Log.d(ConstantsApp.TAG, "" + response)
            val size_data = response.size
            if (size_data > 0) {
                val vitalsList = mutableListOf<Vitals>()
                for (i in 0 until response.size) {
                    val data = response[i]
                    val _id = data._id
                    val bmi = data.bmi
                    val bmiInterpretation = data.bmiInterpretation
                    val bpInterpretation = data.bpInterpretation
                    val camp_id = data.camp_id
                    val createdDate = data.createdDate
                    val diastolic = data.diastolic
                    val height = data.height
                    val heightUnit = data.heightUnit
                    val patient_id = data.patient_id
                    val prInterpretation = data.prInterpretation
                    val pulseRate = data.pulseRate
                    val systolic = data.systolic
                    val userId = data.userId
                    val weight = data.weight
                    val weightUnit = data.weightUnit
                    val isSyn = data.isSyn

                    val vitals =
                        org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals(
                            _id,
                            bmi,
                            bmiInterpretation,
                            bpInterpretation,
                            camp_id.toInt(),
                            createdDate,
                            diastolic,
                            height,
                            heightUnit,
                            patient_id.toInt(),
                            prInterpretation,
                            pulseRate.toInt(),
                            systolic,
                            userId,
                            weight,
                            weightUnit,
                            isSyn
                        )
                    viewModel1.deleteVitals(vitals)
                }
            } else {
                // The response is empty, handle it accordingly
            }
        })
    }

    private fun Clear_OPDInvestigation_Data() {
        viewModel1.allOPD_Investigations.observe(this, Observer { response ->
            val sizeData = response.size
            if (sizeData > 0) {
                Log.d(ConstantsApp.TAG, "allOPD_Investigations size=>" + response.size)
                for (i in 0 until response.size) {
                    val data = response[i]
                    val _id = data._id
                    val camp_id = data.camp_id
                    val createdDate = data.createdDate
                    val haemoglobin = data.haemoglobin
                    val haemoglobin_interpretation = data.haemoglobin_interpretation
                    val has_refused = data.has_refused
                    val o2_saturation = data.o2_saturation
                    val o2s_interpretation = data.o2s_interpretation
                    val patient_id = data.patient_id
                    val random_blood_sugar = data.random_blood_sugar
                    val rbs_interpretation = data.rbs_interpretation
                    val userId = data.userId
                    val isSyn = data.isSyn

                    val opdInvestigations = OPD_Investigations(
                        _id,
                        camp_id!!,
                        createdDate,
                        haemoglobin,
                        haemoglobin_interpretation,
                        has_refused,
                        o2_saturation,
                        o2s_interpretation,
                        patient_id,
                        random_blood_sugar,
                        rbs_interpretation,
                        userId!!,
                        isSyn
                    )
                    viewModel1.deleteOPDInvestigations(opdInvestigations)
                }
            } else {
                // The response is empty, handle it accordingly
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        try {
            if (resultCode == RESULT_OK) {
                try {
                    val decodedBytes: ByteArray = Base64.getDecoder().decode(result.contents)
                    val decodedText: String = String(decodedBytes, Charsets.UTF_8)
                    Log.d(ConstantsApp.TAG, "result.contents => $decodedText")
                    sessionManager.setPatientData(decodedText)
                    val decodedText1 = sessionManager.getPatientData()
                    val gson = Gson()
                    val patientData2 = gson.fromJson(decodedText1, PatientDataLocal::class.java)
                    insertPatientDataToLocal(patientData2)
                    val designation = sessionManager.getLoginData()!!.get(0).Designation_name
                    if (designation.equals("Orthotist", true)) {
                        val intent = Intent(
                            this@MainActivity, CampPatientListActivity::class.java
                        )
                        intent.putExtra("result", decodedText)
                        startActivity(intent)
                    } else {
                        val intent = Intent(
                            this@MainActivity,
                            PatientForms::class.java
                        )
                        intent.putExtra("result", decodedText)
                        startActivity(intent)
                    }
                } catch (e: IllegalArgumentException) {
                    Log.e(ConstantsApp.TAG, "Illegal base64 character", e)
                    Toast.makeText(this, "Invalid QR code", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.canceled_by_user, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun insertPatientDataToLocal(patientData: PatientDataLocal) {
        viewModel1.insertPatient(patientData)
        InsertPatientToLocal()
    }

    private fun InsertPatientToLocal() {
        viewModel1.toastMessage.observe(this, Observer { message ->
            showToast(message)
        })
    }

    private fun showToast(message: String) {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Log.d(ConstantsApp.TAG, "Read external storage permission denied")
            }
        }

        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllLocalImages()
                } else {
                    Log.e(ConstantsApp.TAG, "Storage permission denied")
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun observeImageUploadResponse() {
        viewModel.getImageUploadResponse.observe(this, Observer { response ->
            val data = response.data
            if (data != null) {
                val errorMessage = data.ErrorMessage
                Log.d(ConstantsApp.TAG, "ErrorMessage => $errorMessage")
            } else {
                Log.e(ConstantsApp.TAG, "Response data is null")
            }
        })
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    fun getFileNameFromPath(filePath: String): String {
        val file = File(filePath)
        return file.name
    }


    private fun getInternalFilePath(fileName: String): String {
        return File(this.filesDir, fileName).absolutePath
    }

    // Function to get the file path for a given file name in external storage
    private fun getExternalFilePath(fileName: String): String {
        return File(this.getExternalFilesDir(null), fileName).absolutePath
    }

    private fun doesFileExist(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    private fun dummyOrthosis(): List<OrthosisTypeModelItem> {
        val measurementList1 = listOf(
            Measurement(false, "", "Length HEEL TO 1st TOE", 1),
            Measurement(false, "", "FORE FOOT CIRCUMFERENCE at metatarsal level", 2),
            Measurement(false, "", "ANKEL CIRCUMFERENCE 1 inch bellow the head of fibula", 3)
        )
        val measurementList2 = listOf(
            Measurement(false, "", "HEEL TO TOE", 1),
            Measurement(false, "", "FOREFOOT", 2),
            Measurement(false, "", "ANKEL", 3)
        )
        return listOf(
            OrthosisTypeModelItem(
                false, 1, measurementList1, "ANKEL FOOT ORTHOSIS(PP) WITH FIXED 90 DEGREE ANKEL"
            ), OrthosisTypeModelItem(false, 2, measurementList2, "DYNAMIC ANKEL FOOT ORTHOSIS(PP)")
        )
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun checkForAppUpdate() {
        try {
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                when (appUpdateInfo.updateAvailability()) {
                    UpdateAvailability.UPDATE_AVAILABLE -> {
                        Log.d(
                            ConstantsApp.TAG,
                            "Update is available. Priority: ${appUpdateInfo.updatePriority()}"
                        )
                        requestUpdate(appUpdateInfo)
                    }

                    UpdateAvailability.UPDATE_NOT_AVAILABLE -> {
                        Log.d(ConstantsApp.TAG, "No updates available")
                    }

                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                        Log.d(ConstantsApp.TAG, "Developer triggered update in progress")
                        onResume()
                    }

                    UpdateAvailability.UNKNOWN -> Log.d(ConstantsApp.TAG, "Update status unknown")
                }
            }.addOnFailureListener { exception ->
                Log.d(ConstantsApp.TAG, "Error checking for updates: ${exception.message}")
            }
        } catch (e: Exception) {
            Log.d("Error", "Exception: ${e.message}")
        }
    }

    private fun requestUpdate(appUpdateInfo: AppUpdateInfo) {
        try {
            val appUpdateType = AppUpdateType.IMMEDIATE // Or AppUpdateType.FLEXIBLE
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                appUpdateType,
                this,
                REQUEST_CODE_UPDATE
            )
        } catch (e: Exception) {
            Log.d("Error", "Error starting update flow: ${e.message}")
        }
    }

    private fun startUpdate(appUpdateInfo: AppUpdateInfo, updateType: Int) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            updateType,
            this,
            REQUEST_CODE_UPDATE
        )
    }

    private fun showUpdateCompleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Update Completed")
            .setMessage("Restart the app to apply the update.")
            .setPositiveButton("Restart") { _, _ ->
                appUpdateManager.completeUpdate()
            }
            .setCancelable(false)
            .show()
    }

    private fun syncNewVitals() {
        if (isInternetAvailable(this)) {
            val data = HashMap<String, Any>()
            data["patient"] = ""
            val intent = Intent(this, VitalsFormService::class.java).apply {
                putExtra("QUERY_PARAMS", data)
            }
            startService(intent)
        } else {
            Utility.infoToast(this@MainActivity, "Internet Not Available")
        }

    }

    private fun syncNewOpdForm() {
        if (isInternetAvailable(this)) {
            //sync service for Orthosis Patient Form
            val data = HashMap<String, Any>()
            data["patient"] = ""
            val intent = Intent(this, OpdFormService::class.java).apply {
                putExtra("QUERY_PARAMS", data)
            }
            startService(intent)
        } else {
            Utility.infoToast(this@MainActivity, "Internet Not Available")
        }

    }

    private fun syncNewVisualAcuity() {
        if (isInternetAvailable(this)) {
            //sync service for Orthosis Patient Form
            val data = HashMap<String, Any>()
            data["patient"] = ""
            val intent = Intent(this, VisualAcuityFormService::class.java).apply {
                putExtra("QUERY_PARAMS", data)
            }
            startService(intent)
        } else {
            Utility.infoToast(this@MainActivity, "Internet Not Available")
        }

    }

    private fun reportDialog() {
        val layoutResId = R.layout.reports_dialog
        val alertCustomDialog = layoutInflater.inflate(layoutResId, null)
        val messageDialog = AlertDialog.Builder(this@MainActivity)
        messageDialog.setView(alertCustomDialog)

        val tvGeneral: TextView = alertCustomDialog.findViewById(R.id.tvGeneral)
        val tvEye: TextView = alertCustomDialog.findViewById(R.id.tvEye)
        val tvEnt: TextView = alertCustomDialog.findViewById(R.id.tvEnt)
        val tvPathology: TextView = alertCustomDialog.findViewById(R.id.tvPathology)

        val generalLayout: View = alertCustomDialog.findViewById(R.id.generalLayout)
        val eyeLayout: View = alertCustomDialog.findViewById(R.id.eyeLayout)
        val entLayout: View = alertCustomDialog.findViewById(R.id.entLayout)
        val pathologyLayout: View = alertCustomDialog.findViewById(R.id.pathologyLayout)


        val tvVitalsReport: TextView = alertCustomDialog.findViewById(R.id.tvVitalsReport)
        val tvOpdReport: TextView = alertCustomDialog.findViewById(R.id.tvOpdReport)
        val tvVisualAcuityReport: TextView = alertCustomDialog.findViewById(R.id.tvVisualAcuityReport)
        val tvRefractiveReport: TextView = alertCustomDialog.findViewById(R.id.tvRefractiveErrorReport)
        val tvOtherReport: TextView = alertCustomDialog.findViewById(R.id.tvOtherReports)


        val tvOpDoctorNotes: TextView = alertCustomDialog.findViewById(R.id.tvEntOpdDcNotes)
        val tvAudiometry: TextView = alertCustomDialog.findViewById(R.id.tvEntAudiometry)
        val tvPreOpNotes: TextView = alertCustomDialog.findViewById(R.id.tvEntPreOpDetails)
        val tvSurgicalNotes: TextView = alertCustomDialog.findViewById(R.id.tvEntSurgicalNotes)
        val tvPostOpDetails: TextView = alertCustomDialog.findViewById(R.id.tvEntPostOpNotes)

        val pathologyReports: TextView = alertCustomDialog.findViewById(R.id.tvPathologyReport)

        val finalDialog = messageDialog.create()

        fun setSelectedTab(selected: TextView, others: List<TextView>) {
            selected.isSelected = true
            others.forEach { it.isSelected = false }
        }

        tvGeneral.setOnClickListener {
            generalLayout.visibility = View.VISIBLE
            eyeLayout.visibility = View.GONE
            entLayout.visibility = View.GONE
            pathologyLayout.visibility = View.GONE
            setSelectedTab(tvGeneral, listOf(tvEye, tvEnt, tvPathology))
        }

        tvEye.setOnClickListener {
            generalLayout.visibility = View.GONE
            eyeLayout.visibility = View.VISIBLE
            entLayout.visibility = View.GONE
            pathologyLayout.visibility = View.GONE
            setSelectedTab(tvEye, listOf(tvGeneral, tvEnt, tvPathology))
        }

        tvEnt.setOnClickListener {
            generalLayout.visibility = View.GONE
            eyeLayout.visibility = View.GONE
            entLayout.visibility = View.VISIBLE
            pathologyLayout.visibility = View.GONE
            setSelectedTab(tvEnt, listOf(tvGeneral, tvEye, tvPathology))
        }

        tvPathology.setOnClickListener {
            generalLayout.visibility = View.GONE
            eyeLayout.visibility = View.GONE
            entLayout.visibility = View.GONE
            pathologyLayout.visibility = View.VISIBLE
            setSelectedTab(tvPathology, listOf(tvGeneral, tvEye, tvEnt))
        }

        tvGeneral.performClick()

        tvVitalsReport.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, PatientReportActivity::class.java).putExtra("report", VITALS_FORM))
        }

        tvOpdReport.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, PatientReportActivity::class.java).putExtra("report", OPD_FORM))
        }

        tvVisualAcuityReport.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, PatientReportActivity::class.java).putExtra("report", VISUAL_ACUITY_FORM))
        }

        tvRefractiveReport.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, PatientReportActivity::class.java).putExtra("report", REFRACTIVE_FORM))
        }

        tvOtherReport.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, PatientListActivity::class.java))
        }

        tvOpDoctorNotes.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, EntPatientReportActivity::class.java).putExtra("report", ENT_OPD_DOCTOR_NOTES))
        }

        tvAudiometry.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, EntPatientReportActivity::class.java).putExtra("report", ENT_AUDIOMETRY))
        }

        tvPreOpNotes.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, EntPatientReportActivity::class.java).putExtra("report", ENT_PRE_OP_DETAILS))
        }

        tvSurgicalNotes.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, EntPatientReportActivity::class.java).putExtra("report", ENT_SURGICAL_NOTES))
        }

        tvPostOpDetails.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, EntPatientReportActivity::class.java).putExtra("report", ENT_POST_OP_NOTES))
        }

        pathologyReports.setOnClickListener {
            finalDialog.dismiss()
            startActivity(Intent(this, EntPatientReportActivity::class.java).putExtra("report", PATHOLOGY_REPORTS))
        }

        finalDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        finalDialog.show()
    }
}


