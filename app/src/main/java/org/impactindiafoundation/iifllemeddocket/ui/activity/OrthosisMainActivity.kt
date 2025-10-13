package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
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
import org.impactindiafoundation.iifllemeddocket.Activity.LoginActivity
import org.impactindiafoundation.iifllemeddocket.Activity.SplashScreenActivity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SharedPrefUtil
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.IS_CAMP_COMPLETE
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.LEAST_CAMP_ID
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ORTHOSIS_LOGIN
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.Equipment
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImage
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideoRquest
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideos
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImageRequest
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OrthosisMainViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityOrthosisMainBinding
import org.impactindiafoundation.iifllemeddocket.services.CampPatientFormService
import org.impactindiafoundation.iifllemeddocket.services.EquipmentImageSyncService
import org.impactindiafoundation.iifllemeddocket.services.FormImageSyncService
import org.impactindiafoundation.iifllemeddocket.services.FormVideoSyncService
import org.impactindiafoundation.iifllemeddocket.services.OrthosisImageSyncService
import org.impactindiafoundation.iifllemeddocket.services.OrthosisPatientFormService
import java.io.File
import java.util.Base64

class OrthosisMainActivity : BaseActivity() {

    private lateinit var binding: ActivityOrthosisMainBinding
    lateinit var sessionManager: SessionManager
    var flag = 0
    private val orthosisMainVM: OrthosisMainViewModel by viewModels()
    private var totalImageCount = 0
    private var formImageCount = 0
    private var orthosisImageCount = 0
    private var equipmentImageCount = 0
    private var campPatientCount = 0
    private var localFormCount = 0
    private lateinit var popupWindow: PopupWindow
    private var isLogin = false
    private var unsycnedVideosCount = 0
    private var leastCampData: CampModel? = null
    lateinit var progressDialog: ProgressDialog
    private lateinit var localUser: UserModel
    private var isSycnComplete = true
    var formImageIndexCheck = 0
    var formImageListForSync = ArrayList<FormImages>()
    var orthosisImageIndexCheck = 0
    var orthosisImageListForSync = ArrayList<OrthosisImages>()
    var formVideoIndexCheck = 0
    var formVideoListForSync = ArrayList<FormVideos>()
    var equipmentImageIndexCheck = 0
    var equipmentImageListForSync = ArrayList<EquipmentImage>()

    private var progressForThis = doctorProgress
    lateinit var appUpdateManager : AppUpdateManager

    companion object {
        private const val REQUEST_CODE_UPDATE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrthosisMainBinding.inflate(layoutInflater)
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
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage("Syncing : Orthosis Form")
        }
        checkForAppUpdate()
        initUi()
        orthosisMainVM.getFormImages()
        orthosisMainVM.getEquipmentImages()
        orthosisMainVM.getFormVideos()
        orthosisMainVM.getCampPatientDetails()// server data
        getCampPatientList()
        getLocalPatientList()
        initObserver()
        refreshCount()

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
            // Show AlertDialog with custom view
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("About Us")
                .setView(view)
                .setPositiveButton("OK", null)
                .show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initUi() {
        isLogin = intent.getBooleanExtra(ORTHOSIS_LOGIN, false)
        sessionManager = SessionManager(this)
        binding.fab.visibility = View.GONE
        binding.addFab.setOnClickListener {
            if (flag == 0) {
                flag = 1
                binding.fab.visibility = View.VISIBLE
                binding.addFab.visibility = View.GONE
                binding.addAnalyticsText.visibility = View.GONE
                binding.syncFabOrthisys.visibility = View.GONE
                binding.patientsFab.visibility = View.GONE
                binding.addSyncText.visibility = View.GONE
                binding.addPersonActionText.visibility = View.GONE
                binding.analyticsFab.visibility = View.GONE
                binding.addPersonActionText.visibility = View.GONE
                Log.d(ConstantsApp.TAG, "0=>" + flag)
            } else if (flag == 1) {
                flag = 0
                Log.d(ConstantsApp.TAG, "1=>" + flag)
            }

            binding.fab.setOnClickListener {
                if (flag == 1) {
                    flag = 0
                    binding.fab.visibility = View.GONE
                    binding.patientsFab.visibility = View.VISIBLE
                    binding.addFab.visibility = View.VISIBLE
                    binding.syncFabOrthisys.visibility = View.VISIBLE
                    binding.addSyncText.visibility = View.VISIBLE
                    binding.addPersonActionText.visibility = View.VISIBLE
                    Log.d(ConstantsApp.TAG, "0=>" + flag)
                } else if (flag == 0) {
                    flag = 1
                    Log.d(ConstantsApp.TAG, "1=>" + flag)
                }
            }
        }
        binding.patientsFab.setOnClickListener {
            getCampAndMoveToViewReport()
        }
        binding.btnQrScan.setOnClickListener {
            getCampDataAndMoveToQrScan()
        }
        binding.btnViewReport.setOnClickListener {
            enterPatientIdDialog()
        }
        binding.syncFabOrthisys.setOnClickListener {
            showSyncDialog()
        }
        binding.btnLogout.setOnClickListener {
            showPopup()
        }
        binding.CardViewCampCompleted.setOnClickListener {
            if (isSycnComplete) {
                if (leastCampData != null) {
                    //  binding.tvCampCompleted.text = "Camp ${leastCampData!!.campId} Completed"
                    progress.show()
                    leastCampData!!.isComplete = true
                    orthosisMainVM.updateSingleCamp(leastCampData!!)
                    Utility.successToast(this@OrthosisMainActivity, "Camp Completed")
                    progress.dismiss()
                }
            } else {
                Utility.infoToast(this@OrthosisMainActivity, "Please Sync Data")
            }
        }
        binding.ivRefreshSync.setOnClickListener {
            refreshCount()
        }
    }

    private fun initObserver() {
        orthosisMainVM.getOrthosisMasterApi()
        orthosisMainVM.getDiagnosisMaster()
        orthosisMainVM.getOrthosisEquipmentMasterApi()
        orthosisMainVM.othosisMasterResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    if (it.data != null) {
                        for (i in it.data) {
                            orthosisMainVM.insertOrthosisMasterLocal(
                                OrthosisType(
                                    i.id, i.measurements, i.name
                                )
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                }
            }
        }

        orthosisMainVM.diagnosisMasterResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    if (it.data != null) {
                        for (i in it.data) {
                            orthosisMainVM.insertDiagnosisMasterLocal(i)
                        }
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                }
            }
        }

        orthosisMainVM.othosisEquipmentMasterResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    if (it.data != null) {
                        for (i in it.data) {
                            orthosisMainVM.insertOrthosisEquipmentMasterLocal(
                                Equipment(
                                    id = i.id,
                                    diagnosis = i.diagnosis,
                                    equipment_category = i.equipment_category,
                                    equipment_support = i.equipment_support,
                                    given_when_case = i.given_when_case,
                                    name = i.name
                                )
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                }
            }
        }

        orthosisMainVM.campPatienDetailsResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                }

                Status.SUCCESS -> {
                    if (!response.data!!.patientOrthosisList.isNullOrEmpty()) {
                        val dataList = response.data.patientOrthosisList
                        val uniqueCamp = getUniqueCampIds(dataList)
                        val campList = ArrayList<CampModel>()

                        for (i in uniqueCamp) {
                            campList.add(
                                CampModel(
                                    campId = i.camp_id.toInt(),
                                    campName = i.camp_name,
                                    isComplete = false
                                )
                            )
                        }
                        val campLeastId = campList.minByOrNull { it.campId }
                        if (campLeastId != null) {
                            leastCampData = campLeastId
                            binding.tvCampCompleted.text = "Complete Camp ${campLeastId.campId}"
                        } else {
                            binding.tvCampCompleted.text = "Complete Camp"
                        }
                        if (isLogin) {
                            orthosisMainVM.insertCampDetails(campList)
                        }
                        for (i in dataList) {
                            if (i.equipment_status.isNullOrEmpty()){
                                i.equipment_status = ""
                            }
                            if (i.equipment_status_notes.isNullOrEmpty()){
                                i.equipment_status_notes = ""
                            }
                            if (i.equipment_category.isNullOrEmpty()){
                                i.equipment_category = ""
                            }
                            if (i.equipment_support.isNullOrEmpty()){
                                i.equipment_support = ""
                            }
                            if (i.orthosis_date.isNullOrEmpty()) {
                                i.orthosis_date = "2024-10-11"
                            }
                            i.isLocal = false
                        }
                        orthosisMainVM.insertCampPatientDetails(dataList)
                    }
                }
                Status.ERROR -> {
                }
            }
        }

        orthosisMainVM.formImagesList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val unsyncedFormImages = it.data.filter { it.isSynced == 0 }
                            formImageCount = unsyncedFormImages.size
                            totalImageCount = formImageCount + orthosisImageCount + equipmentImageCount
                            binding.tvUnsyncedImages.text = "Unsynced Images :- ${totalImageCount}"
                        }
                        orthosisMainVM.getFormOrthosisImages()
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }

        orthosisMainVM.orthosisImagesList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val unsyncedFormImages = it.data.filter { it.isSynced == 0 }
                            orthosisImageCount = unsyncedFormImages.size
                            totalImageCount = formImageCount + orthosisImageCount + equipmentImageCount
                            binding.tvUnsyncedImages.text = "Unsynced Images :- ${totalImageCount}"
                        } else {
                            totalImageCount = formImageCount + orthosisImageCount + equipmentImageCount
                            binding.tvUnsyncedImages.text = "Unsynced Images :- ${totalImageCount}"
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }

        orthosisMainVM.equipmentImagesList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val unsyncedEquipmentImages = it.data.filter { it.isSynced == 0 }
                            equipmentImageCount = unsyncedEquipmentImages.size
                            totalImageCount = formImageCount + orthosisImageCount + equipmentImageCount
                            binding.tvUnsyncedImages.text = "Unsynced Images :- ${totalImageCount}"
                        } else {
                            totalImageCount = formImageCount + orthosisImageCount + equipmentImageCount
                            binding.tvUnsyncedImages.text = "Unsynced Images :- ${totalImageCount}"
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }

        orthosisMainVM.formVideosList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val unsycnedVideos = it.data.filter { it.isSynced == 0 }
                            unsycnedVideosCount = unsycnedVideos.size
                            binding.tvUnsyncedVideos.text =
                                "Unsynced Videos :- ${unsycnedVideosCount}"
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }

        orthosisMainVM.campPatientList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    Log.d("SyncForms", "campPatientList: LOADING...")
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val unsyncedCampData = it.data.filter { it.isSynced == 0 }
                            campPatientCount = unsyncedCampData.size
                            val totalCount = localFormCount
                            Log.d("SyncForms", "campPatientList: SUCCESS → Unsynced Camp Count = $campPatientCount, Local Form Count = $localFormCount, Total = $totalCount")
                            binding.tvUnsyncedForms.text = "Unsysnced Forms :- ${totalCount}"
                        } else {
                            val totalCount = localFormCount
                            Log.d("SyncForms", "campPatientList: SUCCESS → Empty data. Using Local Form Count = $totalCount")
                            binding.tvUnsyncedForms.text = "Unsysnced Forms :- ${totalCount}"
                        }
                    } catch (e: Exception) {
                        Log.e("SyncForms", "campPatientList: Exception -> ${e.message}", e)
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Log.e("SyncForms", "campPatientList: ERROR -> Unexpected error")
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }

        orthosisMainVM.orthosisPatientFormList.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    Log.d("SyncForms", "orthosisPatientFormList: LOADING...")
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!response.data.isNullOrEmpty()) {
                            val unsyncedForm = response.data.filter { it.isSynced == 0 }
                            localFormCount = unsyncedForm.size
                            val totalCount = localFormCount
                            Log.d("SyncForms", "orthosisPatientFormList: SUCCESS → Unsynced Form Count = $localFormCount, Total = $totalCount")
                            binding.tvUnsyncedForms.text = "Unsysnced Forms :- ${totalCount}"
                            if (localFormCount == 0 && formImageCount == 0 && orthosisImageCount == 0 && unsycnedVideosCount == 0) {
                                isSycnComplete = true
                                binding.tvSyncStatusDetail.text = "Sync Complete"
                                binding.tvSyncStatusDetail.setTextColor(resources.getColor(R.color.dark_green))
                                Log.d("SyncForms", "orthosisPatientFormList: ✅ Sync Complete")
                            } else {
                                isSycnComplete = false
                                binding.tvSyncStatusDetail.text = "Sync Not Complete"
                                binding.tvSyncStatusDetail.setTextColor(resources.getColor(R.color.dark_red))
                                Log.d("SyncForms", "orthosisPatientFormList: ❌ Sync Not Complete")
                            }
                        } else {
                            val totalCount = localFormCount
                            Log.d("SyncForms", "orthosisPatientFormList: SUCCESS → Empty data, Total Count = $totalCount")
                            binding.tvUnsyncedForms.text = "Unsysnced Forms :- ${totalCount}"
                            if (localFormCount == 0 && formImageCount == 0 && orthosisImageCount == 0 && unsycnedVideosCount == 0) {
                                isSycnComplete = true
                                binding.tvSyncStatusDetail.text = "Sync Complete"
                                binding.tvSyncStatusDetail.setTextColor(resources.getColor(R.color.dark_green))
                                Log.d("SyncForms", "orthosisPatientFormList: ✅ Sync Complete (Empty data)")
                            } else {
                                isSycnComplete = false
                                binding.tvSyncStatusDetail.text = "Sync Not Complete"
                                binding.tvSyncStatusDetail.setTextColor(resources.getColor(R.color.dark_red))
                                Log.d("SyncForms", "orthosisPatientFormList: ❌ Sync Not Complete (Empty data)")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("SyncForms", "orthosisPatientFormList: Exception -> ${e.message}", e)
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    progress.dismiss()
                    Log.e("SyncForms", "orthosisPatientFormList: ERROR -> Unexpected error")
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }


        orthosisMainVM.campDetailsResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        val intent = Intent(this@OrthosisMainActivity, QrCodeActivity::class.java)
                        if (!response.data.isNullOrEmpty()) {
                            val campLeastId = response.data.minByOrNull { it.campId }
                            intent.putExtra(LEAST_CAMP_ID, campLeastId?.campId ?: 0)
                            intent.putExtra(IS_CAMP_COMPLETE, campLeastId?.isComplete ?: false)
                        }
                        startActivity(intent)
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }

        orthosisMainVM.campForViewReport.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        val intent =
                            Intent(this@OrthosisMainActivity, CampPatientListActivity::class.java)
                        if (!response.data.isNullOrEmpty()) {
                            val campLeastId = response.data.minByOrNull { it.campId }
                            intent.putExtra(LEAST_CAMP_ID, campLeastId?.campId ?: 0)
                            intent.putExtra(IS_CAMP_COMPLETE, campLeastId?.isComplete ?: false)
                        }
                        startActivity(intent)
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }

        orthosisMainVM.userList.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            localUser = it.data[0]
                            val campData = CampModel(
                                campId = localUser.campId,
                                campName = localUser.campName,
                                isComplete = false
                            )
                            leastCampData = campData
                            orthosisMainVM.insertCampDetails(listOf(leastCampData!!))
                        }
                    } catch (e: Exception) { }
                }
                Status.ERROR -> {}
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
                    Log.d(ConstantsApp.TAG, "result.contents => $decodedText")
                    sessionManager.setPatientData(decodedText)
                    val intent = Intent(this@OrthosisMainActivity, OrthosisFittingActivity::class.java)
                    intent.putExtra("result", decodedText)
                    startActivity(intent)
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

    private fun getCampPatientList() {
        orthosisMainVM.getCampPatientList()
    }

    private fun getLocalPatientList() {
        orthosisMainVM.getOrthosisPatientForm()
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopup() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_popup_layout, null)
        val logoutButton: Button = popupView.findViewById(R.id.popupButton)
        val closeButton: Button = popupView.findViewById(R.id.popupCancel)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
        logoutButton.setOnClickListener {
            Utility.successToast(this@OrthosisMainActivity,"Logged out successfully!")
            sessionManager.logoutClear(this@OrthosisMainActivity)
            popupWindow.dismiss()
            val intent = Intent(this@OrthosisMainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            this.finish()
        }
        closeButton.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun getUniqueCampIds(camp: List<CampPatientDataItem>): List<CampPatientDataItem> {
        return camp.groupBy { it.camp_id }
            .map { (_, campList) -> campList.first() }
    }

    private fun getCampDataAndMoveToQrScan() {
        orthosisMainVM.getCampDetails()
    }

    private fun getCampAndMoveToViewReport() {
        orthosisMainVM.getCampForViewReport()
    }

    private fun refreshCount() {
        progress.show()
        orthosisMainVM.getFormImages()
        orthosisMainVM.getFormVideos()
        getLocalPatientList()
        Handler(Looper.getMainLooper()).postDelayed({
            getCampPatientList()
        }, 2000)
    }

    private fun showSyncDialog() {
        val layoutResId = R.layout.sync_dialog
        val alertCustomDialog = layoutInflater.inflate(layoutResId, null)
        val messageDialog = AlertDialog.Builder(this@OrthosisMainActivity)
        messageDialog.setView(alertCustomDialog)
        val tvSyncForm: TextView = alertCustomDialog.findViewById(R.id.tvSyncForm)
        val tvSyncImages: TextView = alertCustomDialog.findViewById(R.id.tvSyncImages)
        val tvSyncVideos: TextView = alertCustomDialog.findViewById(R.id.tvSyncVideos)
        val finalDialog = messageDialog.create()
        tvSyncForm.setOnClickListener {
            finalDialog.dismiss()
            if (isInternetAvailable(this@OrthosisMainActivity)){
                syncForms()
            }
            else {
                Utility.infoToast(this@OrthosisMainActivity,"Internet Not Available!")
            }
        }
        tvSyncImages.setOnClickListener {
            finalDialog.dismiss()
            progressForThis.show()
            if (isInternetAvailable(this@OrthosisMainActivity)){
                syncFormImages()
            }
            else {
                Utility.infoToast(this@OrthosisMainActivity,"Internet Not Available!")
            }
        }
        tvSyncVideos.setOnClickListener {
            finalDialog.dismiss()
            if (isInternetAvailable(this@OrthosisMainActivity)){
                progressForThis.show()
                syncFormVideos()
            }
            else {
                Utility.infoToast(this@OrthosisMainActivity,"Internet Not Available!")
            }
        }
        finalDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        finalDialog.show()
    }

    private fun enterPatientIdDialog() {
        val layoutResId = R.layout.patient_id_dialog
        val alertCustomDialog = layoutInflater.inflate(layoutResId, null)
        val messageDialog = AlertDialog.Builder(this@OrthosisMainActivity)
        messageDialog.setView(alertCustomDialog)
        val etPatientId: TextInputEditText = alertCustomDialog.findViewById(R.id.etPatientId)
        val tvSubmit: TextView = alertCustomDialog.findViewById(R.id.tvSubmit)
        val finalDialog = messageDialog.create()
        tvSubmit.setOnClickListener {
            if (etPatientId.text.isNullOrEmpty()){
                Utility.infoToast(this@OrthosisMainActivity,"Enter Patient Id")
            }
            else{
                val intent = Intent(this@OrthosisMainActivity,OrthosisFittingActivity::class.java)
                intent.putExtra("screen","PatientID")
                intent.putExtra("temp_id",etPatientId.text.toString())
                startActivity(intent)
            }
        }
        finalDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        finalDialog.show()
    }

    private fun syncForms() {
        if (isInternetAvailable(this)) {
            doctorProgress.show()
            Log.d("SyncForms", "Starting OrthosisPatientFormService...")
            val data = HashMap<String, Any>()
            data["patient"] = ""
            val intent = Intent(this, OrthosisPatientFormService::class.java).apply {
                putExtra("QUERY_PARAMS", data)
            }
            startService(intent)
            Log.d("SyncForms", "Starting CampPatientFormService...")
            val campData = HashMap<String, Any>()
            campData["patient"] = ""
            val campIntent = Intent(this, CampPatientFormService::class.java).apply {
                putExtra("QUERY_PARAMS", campData)
            }
            startService(campIntent)
        } else {
            Utility.infoToast(this@OrthosisMainActivity, "Internet not available!")
        }
    }

    private val syncCompletedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == OrthosisPatientFormService.ACTION_TASK_COMPLETED) {
                Log.d("SyncForms", "Broadcast received: Sync completed")
                if (doctorProgress.isShowing()) doctorProgress.dismiss()
                Toast.makeText(this@OrthosisMainActivity, "All forms synced successfully!", Toast.LENGTH_LONG).show()
                orthosisMainVM.getOrthosisPatientForm()
                orthosisMainVM.getCampPatientList()
                Log.d("SyncForms", "Requested fresh data from ViewModel after sync")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(syncCompletedReceiver, IntentFilter(OrthosisPatientFormService.ACTION_TASK_COMPLETED))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(syncCompletedReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
//        handler.removeCallbacks(serviceCheckRunnable)
    }

    private fun syncFormImages() {
        Log.d("SyncImages", "Starting syncFormImages")
        doctorProgress.show()
        formImageIndexCheck = 0
        orthosisMainVM.getFormImagesForSync()
        orthosisMainVM.formImagesListForSync.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    Log.d("SyncImages", "Form images: LOADING")
                    doctorProgress.show()
                }
                Status.SUCCESS -> {
                    doctorProgress.dismiss()
                    Log.d("SyncImages", "Form images: SUCCESS, total=${it.data?.size ?: 0}")
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            formImageListForSync.clear()
                            for (i in it.data){
                                if (i.isSynced == 0 ){
                                    formImageListForSync.add(i)
                                    Log.d("SyncImages", "Form image added for sync: ${i.id}, file=${i.images}")
                                }
                            }
                            syncNextFormImage()
                        }
                        else{
                            Log.d("SyncImages", "No unsynced form images, moving to Orthosis images")
                            syncOrthosisImages()
                        }
                    } catch (e: Exception) {
                        Log.e("SyncImages", "Form images exception: ${e.message}", e)
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    Log.e("SyncImages", "Form images: ERROR")
                    doctorProgress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }
    }

    private fun syncNextFormImage() {
        if (formImageListForSync.size != 0){
            if (formImageIndexCheck < formImageListForSync.size) {
                val formImage = formImageListForSync[formImageIndexCheck]
                Log.d("SyncImages", "Uploading form image ${formImage.id}, file=${formImage.images}")
                val file = File(formImage.images)
                val requestFile = RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    file
                )
                val body = MultipartBody.Part.createFormData(
                    "images",
                    file.name,
                    requestFile
                )
                val tempPatientIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    formImage.temp_patient_id
                )
                val campIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    formImage.camp_id
                )
                val patientIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    ""
                )
                val idRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    formImage.id.toString()
                )
                val imageUploadParams = FormImageRequest(
                    body,
                    tempPatientIdRequestBody,
                    campIdRequestBody,
                    patientIdRequestBody,
                    idRequestBody
                )
                orthosisMainVM.syncFormImages(imageUploadParams)
                orthosisMainVM.formSyncResponse.observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            Log.d("SyncImages", "Form image ${formImage.id} uploading...")
                            doctorProgress.show()
                        }
                        Status.SUCCESS -> {
                            Log.d("SyncImages", "Form image ${formImage.id} uploaded successfully, server success_id=${it.data?.success_id}")
                            doctorProgress.dismiss()
                            try {
                                if (it.data != null) {
                                    orthosisMainVM.updateFormImageSynced(it.data.success_id ?: 0)
                                    orthosisMainVM.getFormImages()
                                    orthosisMainVM.getEquipmentImages()
                                    formImageIndexCheck++
                                    syncNextFormImage()
                                }
                            } catch (e: Exception) {
                                Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                            }
                        }
                        Status.ERROR -> {
                           Log.e("SyncImages", "Form image ${formImage.id} upload failed")
                            doctorProgress.dismiss()
                            Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                        }
                    }
                }
            }
            else{
                Log.d("SyncImages", "All form images uploaded, moving to Orthosis images")
                doctorProgress.dismiss()
                syncOrthosisImages()
            }
        }
        else{
            Log.d("SyncImages", "All form images uploaded, moving to Orthosis images")
            doctorProgress.dismiss()
            syncOrthosisImages()
        }
    }

    private fun syncOrthosisImages() {
        Log.d("SyncImages", "Starting syncOrthosisImages")
        orthosisMainVM.getOrthosisImagesForSync()
        orthosisMainVM.orthosisImagesListForSync.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    doctorProgress.show()
                }
                Status.SUCCESS -> {
                    doctorProgress.dismiss()
                    Log.d("SyncImages", "Orthosis images: SUCCESS, total=${it.data?.size ?: 0}")
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            orthosisImageListForSync.clear()
                            for (i in it.data){
                                if (i.isSynced == 0){
                                    orthosisImageListForSync.add(i)
                                    Log.d("SyncImages", "Orthosis image added for sync: ${i.id}, file=${i.images}")
                                }
                            }
                            syncNextOrthosisImage()
                        }
                        else{
                            Log.d("SyncImages", "No unsynced orthosis images, moving to equipment images")
                            syncEquipmentImages()
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    Log.e("SyncImages", "Orthosis images: ERROR")
                    doctorProgress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }
    }

    private fun syncNextOrthosisImage() {
        Log.d("SyncImages", "syncNextOrthosisImage called. Index=$orthosisImageIndexCheck, Total=${orthosisImageListForSync.size}")
        if (orthosisImageListForSync.isNotEmpty()) {
            if (orthosisImageIndexCheck < orthosisImageListForSync.size) {
                val orthosisImage = orthosisImageListForSync[orthosisImageIndexCheck]
                Log.d("SyncImages", "Preparing to upload Orthosis image ID=${orthosisImage.id}, File=${orthosisImage.images}")
                val file = File(orthosisImage.images)
                if (!file.exists()) {
                    Log.e("SyncImages", "File not found: ${orthosisImage.images}")
                    orthosisImageIndexCheck++
                    syncNextOrthosisImage()
                    return
                }
                val requestFile = RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    file
                )
                val body = MultipartBody.Part.createFormData(
                    "images",
                    file.name,
                    requestFile
                )
                val tempPatientIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    orthosisImage.temp_patient_id
                )
                val campIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    orthosisImage.camp_id
                )
                val orthosistIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    orthosisImage.orthosis_id
                )
                val amputationSideRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    orthosisImage.amputation_side
                )
                val patientIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    ""
                )
                val idRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    orthosisImage.id.toString()
                )
                val imageUploadParams = OrthosisImageRequest(
                    body,
                    tempPatientIdRequestBody,
                    campIdRequestBody,
                    orthosistIdRequestBody,
                    amputationSideRequestBody,
                    patientIdRequestBody,
                    idRequestBody
                )
                Log.d("SyncImages", "Uploading Orthosis image ID=${orthosisImage.id} to server")
                orthosisMainVM.syncOrthosisImages(imageUploadParams)
                orthosisMainVM.orthosisImageSyncResponse.observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            Log.d("SyncImages", "Orthosis image ${orthosisImage.id} is uploading...")
                            doctorProgress.show()
                        }
                        Status.SUCCESS -> {
                            doctorProgress.dismiss()
                            Log.d("SyncImages", "Orthosis image ${orthosisImage.id} uploaded successfully. Server success_id=${it.data?.success_id}")
                            try {
                                if (it.data != null) {
                                    orthosisMainVM.updateOrthosisImageSynced(it.data.success_id ?: 0)
                                    Log.d("SyncImages", "Marked Orthosis image ${orthosisImage.id} as synced in local DB")
                                    orthosisMainVM.getFormImages()
                                    orthosisMainVM.getEquipmentImages()
                                    orthosisImageIndexCheck++
                                    syncNextOrthosisImage()
                                }
                            } catch (e: Exception) {
                                Log.e("SyncImages", "Exception while updating Orthosis image ${orthosisImage.id}: ${e.message}", e)
                                Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                            }
                        }
                        Status.ERROR -> {
                            doctorProgress.dismiss()
                            Log.e("SyncImages", "Failed to upload Orthosis image ${orthosisImage.id}")
                            Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                        }
                    }
                }
            } else {
                Log.d("SyncImages", "All Orthosis images uploaded. Moving to Equipment images")
                syncEquipmentImages()
            }
        } else {
            Log.d("SyncImages", "No Orthosis images to sync. Moving to Equipment images")
            syncEquipmentImages()
        }
    }

    private fun syncEquipmentImages() {
        Log.d("SyncImages", "syncEquipmentImages called. Resetting equipmentImageIndexCheck to 0")
        equipmentImageIndexCheck = 0
        Log.d("SyncImages", "Fetching equipment images for sync from ViewModel")
        orthosisMainVM.getEquipmentImagesForSync()
        orthosisMainVM.equipmentImagesListForSync.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    Log.d("SyncImages", "Equipment images: LOADING")
                    doctorProgress.show()
                }
                Status.SUCCESS -> {
                    doctorProgress.dismiss()
                    Log.d("SyncImages", "Equipment images: SUCCESS, total=${response.data?.size ?: 0}")
                    try {
                        if (!response.data.isNullOrEmpty()) {
                            equipmentImageListForSync.clear()
                            Log.d("SyncImages", "Cleared existing equipmentImageListForSync")
                            for (i in response.data) {
                                if (i.isSynced == 0) {
                                    equipmentImageListForSync.add(i)
                                    Log.d("SyncImages", "Equipment image added for sync: ID=${i.id}, File=${i.images}")
                                }
                            }
                            Log.d("SyncImages", "Total unsynced equipment images to sync: ${equipmentImageListForSync.size}")
                            syncNextEquipmentImage()
                        } else {
                            Log.d("SyncImages", "No unsynced equipment images found")
                        }
                    } catch (e: Exception) {
                        Log.e("SyncImages", "Exception while processing equipment images: ${e.message}", e)
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    doctorProgress.dismiss()
                    Log.e("SyncImages", "Equipment images: ERROR while fetching from server")
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }
    }

    private fun syncNextEquipmentImage() {
        Log.d("SyncImages", "syncNextEquipmentImage called. Current index: $equipmentImageIndexCheck, total images: ${equipmentImageListForSync.size}")
        if (equipmentImageListForSync.isNotEmpty()) {
            if (equipmentImageIndexCheck < equipmentImageListForSync.size) {
                val equipmentImage = equipmentImageListForSync[equipmentImageIndexCheck]
                Log.d("SyncImages", "Preparing to upload equipment image ID=${equipmentImage.id}, file=${equipmentImage.images}")
                val file = File(equipmentImage.images)
                if (!file.exists()) {
                    Log.e("SyncImages", "File not found: ${equipmentImage.images}")
                    equipmentImageIndexCheck++
                    syncNextEquipmentImage()
                    return
                }
                val requestFile = RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    file
                )
                val body = MultipartBody.Part.createFormData(
                    "images",
                    file.name,
                    requestFile
                )
                val tempPatientIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    equipmentImage.temp_patient_id
                )
                val campIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    equipmentImage.camp_id
                )
                val patientIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    ""
                )
                val imageTypeRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    "equipment"
                )
                val idRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    equipmentImage.id.toString()
                )
                val imageUploadParams = EquipmentImageRequest(
                    body,
                    tempPatientIdRequestBody,
                    campIdRequestBody,
                    imageTypeRequestBody,
                    patientIdRequestBody,
                    idRequestBody
                )
                Log.d("SyncImages", "Calling ViewModel to sync equipment image ID=${equipmentImage.id}")
                orthosisMainVM.syncEquipmentImages(imageUploadParams)
                orthosisMainVM.equipmentImageSyncResponse.observe(this) { response ->
                    when (response.status) {
                        Status.LOADING -> {
                            Log.d("SyncImages", "Uploading equipment image ID=${equipmentImage.id}...")
                            doctorProgress.show()
                        }
                        Status.SUCCESS -> {
                            doctorProgress.dismiss()
                            Log.d("SyncImages", "Equipment image ID=${equipmentImage.id} uploaded successfully, server success_id=${response.data?.success_id}")
                            try {
                                if (response.data != null) {
                                    orthosisMainVM.updateEquipmentImageSynced(response.data.success_id ?: 0)
                                    orthosisMainVM.getFormImages()
                                    orthosisMainVM.getEquipmentImages()
                                    equipmentImageIndexCheck++
                                    Log.d("SyncImages", "Moving to next equipment image. Next index: $equipmentImageIndexCheck")
                                    syncNextEquipmentImage()
                                }
                            } catch (e: Exception) {
                                Log.e("SyncImages", "Exception while updating synced status: ${e.message}", e)
                                Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                            }
                        }
                        Status.ERROR -> {
                            Log.e("SyncImages", "Failed to upload equipment image ID=${equipmentImage.id}")
                            doctorProgress.dismiss()
                            Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                            equipmentImageIndexCheck++
                            syncNextEquipmentImage() // continue with next image
                        }
                    }
                }
            } else {
                Log.d("SyncImages", "All equipment images uploaded. Sync completed.")
                doctorProgress.dismiss()
                Toast.makeText(this, "All form, orthosis, and equipment images synced successfully!", Toast.LENGTH_LONG).show()
            }
        } else {
            Log.d("SyncImages", "No equipment images to sync. Sync completed.")
            doctorProgress.dismiss()
            Toast.makeText(this, "All form, orthosis, and equipment images synced successfully!", Toast.LENGTH_LONG).show()
        }
    }

    private fun syncFormVideos() {
        Log.d("SyncVideos", "Starting syncFormVideos")
        doctorProgress.show() // Show progress immediately
        orthosisMainVM.getFormVideosForSync()
        orthosisMainVM.formVideosListForSync.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> Log.d("SyncVideos", "Form videos: LOADING")
                Status.SUCCESS -> {
                    Log.d("SyncVideos", "Form videos: SUCCESS, total=${response.data?.size ?: 0}")
                    formVideoListForSync.clear()
                    response.data?.forEach { video ->
                        if (video.isSynced == 0) {
                            formVideoListForSync.add(video)
                            Log.d("SyncVideos", "Form video added for sync: ID=${video.id}, file=${video.video}")
                        }
                    }
                    if (formVideoListForSync.isNotEmpty()) {
                        formVideoIndexCheck = 0
                        observeFormVideoUpload() // observe once
                        syncNextFormVideo()
                    } else {
                        Log.d("SyncVideos", "No unsynced form videos found")
                        doctorProgress.dismiss()
                    }
                }
                Status.ERROR -> {
                    Log.e("SyncVideos", "Error fetching form videos")
                    doctorProgress.dismiss()
                }
            }
        }
    }

    private fun observeFormVideoUpload() {
        orthosisMainVM.formVideoSyncResponse.observe(this) { response ->
            val formVideo = formVideoListForSync.getOrNull(formVideoIndexCheck) ?: return@observe
            when (response.status) {
                Status.LOADING -> Log.d("SyncVideos", "Form video ${formVideo.id} uploading...")
                Status.SUCCESS -> {
                    Log.d("SyncVideos", "Form video ${formVideo.id} uploaded successfully")
                    orthosisMainVM.updateFormVideoSynced(response.data?.success_id ?: 0)
                    orthosisMainVM.getFormVideos()
                    formVideoIndexCheck++
                    if (formVideoIndexCheck < formVideoListForSync.size) {
                        syncNextFormVideo()
                    } else {
                        Log.d("SyncVideos", "All form videos uploaded. Dismissing progress.")
                        doctorProgress.dismiss()
                        Toast.makeText(this, "All form videos synced successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
                Status.ERROR -> {
                    Log.e("SyncVideos", "Failed to upload Form video ${formVideo.id}")
                    Utility.errorToast(this, "Upload failed for video ${formVideo.id}")
                    formVideoIndexCheck++
                    if (formVideoIndexCheck < formVideoListForSync.size) {
                        syncNextFormVideo()
                    } else {
                        doctorProgress.dismiss()
                        Toast.makeText(this, "Form video sync completed with some errors.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun syncNextFormVideo() {
        val formVideo = formVideoListForSync.getOrNull(formVideoIndexCheck) ?: return
        val file = File(formVideo.video)
        if (!file.exists()) {
            Log.e("SyncVideos", "File not found: ${formVideo.video}")
            formVideoIndexCheck++
            syncNextFormVideo()
            return
        }
        val body = MultipartBody.Part.createFormData(
            "video", file.name,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        )
        val imageUploadParams = FormVideoRquest(
            body,
            RequestBody.create("text/plain".toMediaTypeOrNull(), formVideo.temp_patient_id),
            RequestBody.create("text/plain".toMediaTypeOrNull(), formVideo.camp_id),
            RequestBody.create("text/plain".toMediaTypeOrNull(), ""),
            RequestBody.create("text/plain".toMediaTypeOrNull(), formVideo.id.toString())
        )
        Log.d("SyncVideos", "Uploading Form video ID=${formVideo.id} to server")
        orthosisMainVM.syncFormVideos(imageUploadParams)
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
}