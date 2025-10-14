package org.impactindiafoundation.iifllemeddocket.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CurrentInventoryLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImagePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.InventoryUnitLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel.FinalPrescriptionDrugServer
import org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel.SendFinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.Model.ImageModel.ImageUploadParams
import org.impactindiafoundation.iifllemeddocket.Model.ImagePrescriptionModel.UploadImagePrescriptionRequest
import org.impactindiafoundation.iifllemeddocket.Model.NewPrescriptionDataModel.NewPrescriptionModel
import org.impactindiafoundation.iifllemeddocket.Model.NewPrescriptionDataModel.sendNewPrescriptionData
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataLive
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataModel

import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SharedPrefUtil
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.ViewModel.ResourceApp
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OpdSyncDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.OpdSyncTable
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OpdPrescriptionFormViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.PharmaMainViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPharmaMainBinding
import org.impactindiafoundation.iifllemeddocket.services.OpdFormService
import org.impactindiafoundation.iifllemeddocket.services.PharmaFormSyncService
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.NewMedicineReportActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.OpdSyncTableActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.OrthosisMainActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.PatientReportActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.QrCodeActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

class PharmaMainActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityPharmaMainBinding
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    private lateinit var popupWindow: PopupWindow
    private lateinit var popupWindow1: PopupWindow
    private lateinit var popupWindow5: PopupWindow
    private var isSyned = false
    private var currentImageIndex = 0
    private lateinit var imagePrescriptionList: List<ImagePrescriptionModel>
    private val viewModel3: OpdPrescriptionFormViewModel by viewModels()
    private var isLogin = false
    lateinit var appUpdateManager: AppUpdateManager

    companion object {
        private const val REQUEST_CODE_UPDATE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmaMainBinding.inflate(layoutInflater)
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

        getViewModel()
        createRoomDatabase()
        isLogin = intent.getBooleanExtra("isLogin", false)
        if (isLogin) {
            if (isInternetAvailable(this@PharmaMainActivity)) {
                GetLocalCurrentInventoryItems()
                GetCurrentInventoryItems()
                GetInventoryUnits()
            }
        }

        binding.CardViewScanQR.setOnClickListener(this)
        binding.fab.setOnClickListener(this)
        binding.CardViewLogout.setOnClickListener(this)
        binding.tvViewReportMedicine.setOnClickListener(this)

        checkForAppUpdate()

        viewModel3.unsyncedFormsCount.observe(this) { count ->
            binding.tvUnsyncedForms.text = "Unsynced Forms: $count"
        }
        binding.aboutUsFab.setOnClickListener {
            openAboutUsDailogueBox()
        }

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
            val versionName = packageManager.getPackageInfo(packageName, 0).versionName
            val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(this, R.style.CustomBottomSheetDialog)
            val view = layoutInflater.inflate(R.layout.layout_whats_new_bottomsheet, null)
            bottomSheetDialog.setContentView(view)
            view.findViewById<TextView>(R.id.titleText).text = "What's New in version $versionName"
            view.findViewById<TextView>(R.id.messageText).text = """
            ✨ Latest Updates:

            • Faster performance
            • Bug fixes
            • New dashboard UI
        """.trimIndent()

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
            val versionName = packageManager
                .getPackageInfo(packageName, 0).versionName
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

    private fun GetInventoryUnits() {
        viewModel.GetInventoryUnits(progressDialog)
        getInventoryUnitsResponse()
    }

    private fun getInventoryUnitsResponse() {
        viewModel.getInventoryUnits.observe(this, Observer { response ->
            when (response) {
                is ResourceApp.Success -> {
                    progressDialog.dismiss()
                    Log.d(ConstantsApp.TAG, "inventoryUnits=>" + response.data!!.inventoryUnits)
                    viewModel1.insertInventoryUnit(response.data.inventoryUnits.map { item ->
                        InventoryUnitLocal(
                            0,
                            inventoryBrand_id = item.inventoryBrand_id,
                            ratio_of = item.ratio_of,
                            unit_id = item.unit_id,
                            unit_name = item.unit_name,
                            unit_symbol = item.unit_symbol
                        )
                    })
                }
                is ResourceApp.Error -> {
                    progressDialog.dismiss()
                }
                is ResourceApp.Loading -> {
                    progressDialog.show()
                }
                else -> {
                    progressDialog.show()
                }
            }
        })
    }

    private fun GetLocalCurrentInventoryItems() {
        viewModel1.deleteAllCurrentInventory()
        viewModel1.deleteAllInventoryUnit()
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

    private fun GetCurrentInventoryItems() {
        viewModel.GetCurrentInventoryItems(progressDialog)
        GetCurrentInventoryItemsResponse()
    }

    private fun GetCurrentInventoryItemsResponse() {
        viewModel.getCurrentInventoryItems.observe(this, Observer { response ->
            when (response) {
                is ResourceApp.Success -> {
                    progressDialog.dismiss()
                    Log.d(ConstantsApp.TAG, "GetCurrentInventoryItemsResponse=>" + response.data!!.currentInventory)
                    val currentInventoryData = ArrayList<CurrentInventoryLocal>()
                    for (item in response.data.currentInventory) {
                        val data = CurrentInventoryLocal(
                            0,
                            ITEM_CATEGORY = item.ITEM_CATEGORY,
                            batch_no = item.batch_no,
                            brand_id = item.brand_id,
                            brand_name = item.brand_name,
                            inventoryItem_id = item.inventoryItem_id,
                            item_name = item.item_name,
                            manufactured_by = item.manufactured_by,
                            procurementItem_id = item.procurementItem_id,
                            purchaseItem_id = item.purchaseItem_id,
                            unit_name = item.unit_name,
                            unit_ratio_of = item.unit_ratio_of,
                            unit_symbol = item.unit_symbol
                        )
                        currentInventoryData.add(data)
                    }
                    viewModel1.insertCurrentInventoryItemNew(currentInventoryData)
                }
                is ResourceApp.Error -> {
                    progressDialog.dismiss()
                }
                is ResourceApp.Loading -> {
                    progressDialog.show()
                }
                else -> {
                    progressDialog.dismiss()
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.CardViewScanQR -> {
                val intent = Intent(this@PharmaMainActivity, QrCodeActivity::class.java)
                intent.putExtra("screen", Constants.SCREEN_OPD)
                startActivity(intent)
            }

            binding.fab -> {
                if (ConstantsApp.checkInternetConenction(applicationContext)) {
                    showPopupSyn()
                } else {
                    Toast.makeText(applicationContext, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                }
            }

            binding.CardViewLogout -> {
                showPopup()
            }

            binding.tvViewReportMedicine -> {
                reportDialog()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        try {
            if (resultCode == RESULT_OK) {
                try {
                    val decodedBytes: ByteArray = Base64.getDecoder().decode(result.contents)
                    val decodedText: String = String(decodedBytes, Charsets.UTF_8)
                    sessionManager.setPatientData(decodedText)
                    val decodedText1 = sessionManager.getPatientData()
                    val gson = Gson()
                    val patientData2 = gson.fromJson(decodedText1, PatientDataLocal::class.java)
                    insertPatientDataToLocal(patientData2)
                    val intent = Intent(this, PharmaFormActivity::class.java)
                    intent.putExtra("result", decodedText)
                    startActivity(intent)
                } catch (e: IllegalArgumentException) {
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

    }

    @SuppressLint("MissingInflatedId")
    private fun showPopup() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_popup_layout, null)
        val closeButton: Button = popupView.findViewById(R.id.popupButton)
        val popupCancel: Button = popupView.findViewById(R.id.popupCancel)
        closeButton.setOnClickListener {
            Utility.successToast(this@PharmaMainActivity,"Logged out successfully!")
            sessionManager.clearCache(this@PharmaMainActivity)
            popupWindow.dismiss()
            val intent = Intent(this@PharmaMainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            this.finish()
        }
        popupCancel.setOnClickListener {
            sessionManager.clearCache(this@PharmaMainActivity)
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
    private fun showPopupSyn() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_syn_layout1, null)
        val closeButton: Button = popupView.findViewById(R.id.popupButton1)
        val popupButtonCancel: Button = popupView.findViewById(R.id.popupButtonCancel)
        closeButton.setOnClickListener {
            progress.show()
            syncPharmaForm()
            getAllPrescriptionImageData()
            Handler(Looper.getMainLooper()).postDelayed({
                progress.dismiss()
                SynCompleted()
            }, 5000)
            popupWindow1.dismiss()
        }

        popupButtonCancel.setOnClickListener {
            popupWindow1.dismiss()
        }

        popupWindow1 = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow1.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        popupWindow1.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    private fun getAllPrescriptionImageData() {
        viewModel1.getImagePrescriptionsIsSyn0.observe(this, Observer { ImagePrescriptionList ->
            imagePrescriptionList = ImagePrescriptionList
            if (!imagePrescriptionList.isNullOrEmpty()) {
                uploadNextImage()
            } else {
                Log.d("Error", "Empty Image")
            }
        })
    }

    private fun UpdateImage(id: String) {
        viewModel1.updateImagePrescription(id.toInt(), 1)
    }

    private fun SynCompleted() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_syn_completed_layout, null)
        val closeButton: Button = popupView.findViewById(R.id.popupButton_synCompleted)
        val popupButtonCancel: Button = popupView.findViewById(R.id.popupCancel_synCompleted)
        val popupCounts: TextView = popupView.findViewById(R.id.popupCounts)
        val popupTitle: TextView = popupView.findViewById(R.id.popupTitle)
        popupTitle.text = "All data have been synced successfully."
        popupCounts.visibility = View.GONE
        closeButton.setOnClickListener {
            if (!isSyned) {
                SynedData()
                isSyned = true
            }
            popupWindow5.dismiss()
        }
        popupButtonCancel.setOnClickListener {
            popupWindow5.dismiss()
        }

        popupWindow5 = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow5.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))

        popupWindow5.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    private fun SynedData() {
        val (campId, userId) = sessionManager.getCampUserID()
        val SynTableRequest = SynTable(
            0, "PRESC",
            campId?.toInt() ?: 0,
            userId ?: "0",
            ConstantsApp.getCurrentOnlyDate(),
            ConstantsApp.getCurrentTime()
        )
        viewModel1.insertSynedData(SynTableRequest)
        { success ->
            when (success) {
                1 -> {
                    getAllSynTableHistory()
                    Log.d(ConstantsApp.TAG, "insertSynedData=" + success)
                }
                0 -> {
                    Log.d(ConstantsApp.TAG, "insertSynedData=" + success)
                }
            }
        }
    }

    private fun getAllSynTableHistory() {
        viewModel1.allSynData.observe(this, Observer { synedDataList ->
            val size_data = synedDataList.size
            if (size_data > 0) {
                val SynedDataList = mutableListOf<SynedDataLive>()
                for (i in 0 until synedDataList.size) {
                    val data = synedDataList[i]
                    val _id = data._id
                    val syn_type = data.syn_type
                    val camp_id = data.camp_id
                    val user_id = data.user_id
                    val date = data.date
                    val time = data.time
                    val isSyn = data.isSyn
                    if (isSyn == 0) {
                        val SynedDataLive = SynedDataLive(
                            _id, syn_type, camp_id, user_id, date, time
                        )
                        SynedDataList.add(SynedDataLive)
                    }
                }
                if (SynedDataList.isNotEmpty()) {
                    val data = SynedDataModel(SynedDataList)
                    viewModel.insertSynedData(progressDialog, data)
                    Insert_SynedData_Response()
                }
            }
        })
    }

    private fun Insert_SynedData_Response() {
        viewModel.uploadSynedDataResponse.observe(this, Observer { response ->
            when (response) {
                is ResourceApp.Success -> {
                    progressDialog.dismiss()
                    try {
                        when (response.data!!.ErrorMessage) {
                            "Success" -> {
                                val data = response.data
                                for (i in 0 until data.lleSyncReport.size) {
                                    val data = data.lleSyncReport[i]
                                    val id = data._id
                                    Log.d(ConstantsApp.TAG, "id=>" + id)
                                    val isSyn = 1
                                    updateSynedData(id.toInt(), isSyn)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                is ResourceApp.Error -> {
                    progressDialog.dismiss()
                }
                is ResourceApp.Loading -> {
                    progressDialog.show()
                }
                else -> {
                    progressDialog.dismiss()
                }
            }
        })
    }

    private fun updateSynedData(toInt: Int, syn: Int) {
        viewModel1.updateSynedData(toInt, syn)
    }

    private fun uploadNextImage() {
        if (currentImageIndex < imagePrescriptionList.size) {
            val currentImage = imagePrescriptionList[currentImageIndex]
            val file = File(currentImage.file_name)
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file_name", file.name, requestFile)
            val patientIdRequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentImage.patient_id.toString()
            )
            val campIdRequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentImage.camp_id.toString()
            )
            val userIdRequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentImage.user_id.toString()
            )
            val idRequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), currentImage._id.toString())
            val imageUploadParams = UploadImagePrescriptionRequest(
                body,
                patientIdRequestBody,
                campIdRequestBody,
                userIdRequestBody,
                idRequestBody
            )
            viewModel.uploadFileImagePrescription(progressDialog, imageUploadParams)
            observeImageUploadResponse()
        } else {
            Log.d(ConstantsApp.TAG, "All images uploaded successfully")
        }
    }

    private fun observeImageUploadResponse() {
        viewModel.getImagePrescriptionUploadResponse.observe(this, Observer { response ->
            val data = response.data
            if (data != null) {
                val errorMessage = data.ErrorMessage
                val id = data._id
                val errorCode = data.ErrorCode
                Log.d(ConstantsApp.TAG, "ErrorMessage => $errorMessage")
                Log.d(ConstantsApp.TAG, "ErrorCode => $errorCode")
                Log.d(ConstantsApp.TAG, "image prescription id => $id")
                when (errorMessage) {
                    "Success" -> {
                        UpdateImage(id)
                        currentImageIndex++
                        uploadNextImage()
                    }
                }
            } else {
                Log.e(ConstantsApp.TAG, "Response data is null")
            }
        })
    }

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
    }

    private fun isInternetAvailable(context: Context): Boolean {
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
                        Log.d(ConstantsApp.TAG, "Update is available. Priority: ${appUpdateInfo.updatePriority()}")
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

    private fun reportDialog() {
        val layoutResId = R.layout.opd_reports_dialog
        val alertCustomDialog = layoutInflater.inflate(layoutResId, null)
        val messageDialog = AlertDialog.Builder(this@PharmaMainActivity)
        messageDialog.setView(alertCustomDialog)
        val tvMedicineReport: TextView = alertCustomDialog.findViewById(R.id.tvMedicineReport)
        val tvSyncReport: TextView = alertCustomDialog.findViewById(R.id.tvSyncReport)
        val finalDialog = messageDialog.create()
        tvMedicineReport.setOnClickListener {
            finalDialog.dismiss()
            gotoScreen(this, NewMedicineReportActivity::class.java)
        }
        tvSyncReport.setOnClickListener {
            finalDialog.dismiss()
            gotoScreen(this, OpdSyncTableActivity::class.java)
        }
        finalDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        finalDialog.show()
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun syncPharmaForm() {
        if (isInternetAvailable(this)) {
            val data = HashMap<String, Any>()
            data["patient"] = ""
            val intent = Intent(this, PharmaFormSyncService::class.java).apply {
                putExtra("QUERY_PARAMS", data)
            }
            startService(intent)
        } else {
            Utility.infoToast(this@PharmaMainActivity, "Internet Not Available")
        }
    }

}


