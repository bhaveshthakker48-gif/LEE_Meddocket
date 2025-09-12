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
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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
    private var videosCount = ArrayList<String>()
    private val campPatientList = ArrayList<CampPatientDataItem>()
    private var campPatientCount = 0
    private var localFormCount = 0
    private lateinit var popupWindow: PopupWindow
    private var isLogin = false
    private var unsycnedVideosCount = 0
    private var leastCampData: CampModel? = null

    lateinit var progressDialog: ProgressDialog
    private lateinit var localUser: UserModel
    private var isSycnComplete = true

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var serviceCheckRunnable: Runnable

    var formImageIndexCheck = 0
    var formImageListForSync = ArrayList<FormImages>()

    var orthosisImageIndexCheck = 0
    var orthosisImageListForSync = ArrayList<OrthosisImages>()

    var formVideoIndexCheck = 0
    var formVideoListForSync = ArrayList<FormVideos>()

    var equipmentImageIndexCheck = 0
    var equipmentImageListForSync = ArrayList<EquipmentImage>()

    private var progressForThis = doctorProgress

    private val progressReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val current = intent.getIntExtra("current", 0)
            val total = intent.getIntExtra("total", 1)
            val progressPercentage = (current * 100) / total
        }
    }

    private val formTaskCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "TASK_COMPLETION") {
                val dataImage = HashMap<String, Any>()
                dataImage["patient"] = ""
                val intentImage =
                    Intent(this@OrthosisMainActivity, FormImageSyncService::class.java).apply {
                        putExtra("QUERY_PARAMS", dataImage)
                    }
                startService(intentImage)
            }
        }
    }


    private val formImageTaskCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                OrthosisPatientFormService.ACTION_SERVICE_RUNNING -> {
                }

                OrthosisPatientFormService.ACTION_PROGRESS_UPDATE -> {
                    val current = intent.getIntExtra("current", 0)
                    val total = intent.getIntExtra("total", 0)
                    updateProgressUI(current, total)
                }

                OrthosisPatientFormService.ACTION_TASK_COMPLETED -> {
                    if (!isMyServiceRunning(FormImageSyncService::class.java)) {
                        val dataVideo = HashMap<String, Any>()
                        dataVideo["patient"] = ""
                        val intentVideo =
                            Intent(
                                this@OrthosisMainActivity,
                                FormVideoSyncService::class.java
                            ).apply {
                                putExtra("QUERY_PARAMS", dataVideo)
                            }
                        startService(intentVideo)
                    }

                }
            }
        }


    }

    private val formVideoTaskCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                OrthosisPatientFormService.ACTION_SERVICE_RUNNING -> {
                }

                OrthosisPatientFormService.ACTION_PROGRESS_UPDATE -> {
                    val current = intent.getIntExtra("current", 0)
                    val total = intent.getIntExtra("total", 0)
                    updateProgressUI(current, total)
                }

                OrthosisPatientFormService.ACTION_TASK_COMPLETED -> {
                    val dataOrthoImage = HashMap<String, Any>()
                    dataOrthoImage["patient"] = ""
                    val intentOrthoImage =
                        Intent(
                            this@OrthosisMainActivity,
                            OrthosisImageSyncService::class.java
                        ).apply {
                            putExtra("QUERY_PARAMS", dataOrthoImage)
                        }
                    startService(intentOrthoImage)
                }
            }
        }


    }

    private val formStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                OrthosisPatientFormService.ACTION_SERVICE_RUNNING -> {
                }

                OrthosisPatientFormService.ACTION_PROGRESS_UPDATE -> {
                    val current = intent.getIntExtra("current", 0)
                    val total = intent.getIntExtra("total", 0)
                    updateProgressUI(current, total)
                }

                OrthosisPatientFormService.ACTION_TASK_COMPLETED -> {
                    if (!isMyServiceRunning(OrthosisPatientFormService::class.java)) {
                        val dataImage = HashMap<String, Any>()
                        dataImage["patient"] = ""
                        val intentImage =
                            Intent(
                                this@OrthosisMainActivity,
                                FormImageSyncService::class.java
                            ).apply {
                                putExtra("QUERY_PARAMS", dataImage)
                            }
                        startService(intentImage)
                    }

                }
            }
        }
    }

    lateinit var appUpdateManager : AppUpdateManager
    lateinit var appUpdateInfoTask: Task<AppUpdateInfo>

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

            // Apply padding to the activity content (this handles all root layouts properly)
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
            showWhatsNewDialog()
            SharedPrefUtil.savePrefString(context, SharedPrefUtil.LAST_SEEN_VERSION, currentVersion)
        }
    }



    private fun showWhatsNewDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("What's New")
            .setMessage(
                """
            ✨ Latest Updates:
            
            • Faster performance
            • Bug fixes
            • New dashboard UI
            
            """.trimIndent()
            )
            .setPositiveButton("Got it", null)
            .show()
    }


    private fun openAboutUsDailogueBox(){
        try {
            // Get app name from resources
            val appName = getString(R.string.app_name)

            // Get version name from PackageManager
            val versionName = packageManager
                .getPackageInfo(packageName, 0).versionName

            // Build About Us message
            val message = "$appName\nVersion: $versionName"

            // Show AlertDialog
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("About Us")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        } catch (e: java.lang.Exception) {
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
//                    showImpactLoader()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
//                    stopImpactLoader()
                    if (it.data != null) {
                        //Utility.successToast(this@MainActivity, it.data[0].name)
                        for (i in it.data) {
                            orthosisMainVM.insertOrthosisMasterLocal(
                                OrthosisType(
                                    i.id, i.measurements, i.name
                                )
                            )
                        }
                        // gotoScreen(this,OrthosisActivity::class.java)
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
//                    showImpactLoader()

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
//                    stopImpactLoader()
                }
            }
        }
        orthosisMainVM.othosisEquipmentMasterResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
//                    showImpactLoader()

                }

                Status.SUCCESS -> {
                    progress.dismiss()
//                    stopImpactLoader()
                    if (it.data != null) {
                        //Utility.successToast(this@MainActivity, it.data[0].name)
                        for (i in it.data) {
                            orthosisMainVM.insertOrthosisEquipmentMasterLocal(
                                Equipment(
                                    id = i.id,
                                    diagnosis = i.diagnosis,
                                    equipment_category = i.equipment_category,
                                    equipment_support = i.equipment_support,
                                    given_when_case = i.given_when_case,
                                    name = i.name
//                                    parentEquipment = i.parentEquipment
                                )
                            )
                        }
                    }
                }

                Status.ERROR -> {
                    progress.dismiss()
//                    stopImpactLoader()
                }
            }
        }

        orthosisMainVM.campPatienDetailsResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    // progress.show()

                }

                Status.SUCCESS -> {
                    // progress.dismiss()
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
                                //  Utility.errorToast(this@OrthosisMainActivity,"emoty date")
                            }
                            i.isLocal = false
                        }

                        orthosisMainVM.insertCampPatientDetails(dataList)
                        // CampPatientDataItem
                    }
                }

                Status.ERROR -> {
                    // progress.dismiss()
                }
            }
        }


        //orthosis file operations
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
                        } else {

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
                    progress.show()
//                    showImpactLoader()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
//                    stopImpactLoader()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val unsyncedCampData = it.data.filter { it.isSynced == 0 }
                            campPatientCount = unsyncedCampData.size
//                            val totalCount = campPatientCount + localFormCount
                            val totalCount = localFormCount
                            binding.tvUnsyncedForms.text = "Unsysnced Forms :- ${totalCount}"
                        } else {
                            val totalCount = localFormCount
                            binding.tvUnsyncedForms.text = "Unsysnced Forms :- ${totalCount}"
//
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

        orthosisMainVM.orthosisPatientFormList.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    progress.show()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!response.data.isNullOrEmpty()) {
                            val unsyncedForm = response.data.filter { it.isSynced == 0 }
                            localFormCount = unsyncedForm.size
                            //val totalCount = campPatientCount + localFormCount
                            val totalCount = localFormCount
                            binding.tvUnsyncedForms.text = "Unsysnced Forms :- ${totalCount}"

                            if (localFormCount == 0 && formImageCount == 0 && orthosisImageCount == 0 && unsycnedVideosCount == 0) {
                                isSycnComplete = true
                                binding.tvSyncStatusDetail.text = "Sync Complete"
                                binding.tvSyncStatusDetail.setTextColor(resources.getColor(R.color.dark_green))
                            } else {
                                isSycnComplete = false
                                binding.tvSyncStatusDetail.text = "Sync Not Complete"
                                binding.tvSyncStatusDetail.setTextColor(resources.getColor(R.color.dark_red))

                            }
                        } else {
                            val totalCount = localFormCount
                            binding.tvUnsyncedForms.text = "Unsysnced Forms :- ${totalCount}"

                            if (localFormCount == 0 && formImageCount == 0 && orthosisImageCount == 0 && unsycnedVideosCount == 0) {
                                isSycnComplete = true
                                binding.tvSyncStatusDetail.text = "Sync Complete"
                                binding.tvSyncStatusDetail.setTextColor(resources.getColor(R.color.dark_green))
                            } else {
                                isSycnComplete = false
                                binding.tvSyncStatusDetail.text = "Sync Not Complete"
                                binding.tvSyncStatusDetail.setTextColor(resources.getColor(R.color.dark_red))

                            }
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
        orthosisMainVM.campDetailsResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    progress.show()
//                    showImpactLoader()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
//                    stopImpactLoader()
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
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
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

                    } catch (e: Exception) {

                    }
                }

                Status.ERROR -> {

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        try {
            if (resultCode == RESULT_OK) {
                // Attempt to decode the Base64 content
                try {
                    val decodedBytes: ByteArray = Base64.getDecoder().decode(result.contents)
                    val decodedText: String = String(decodedBytes, Charsets.UTF_8)



                    Log.d(ConstantsApp.TAG, "result.contents => $decodedText")

                    sessionManager.setPatientData(decodedText)

                    val decodedText1 = sessionManager.getPatientData()

                    // Use Gson to parse the JSON string
                    val gson = Gson()

                    val patientData1 = gson.fromJson(decodedText1, PatientData::class.java)
                    val patientData2 = gson.fromJson(decodedText1, PatientDataLocal::class.java)

                    //  insertPatientDataToLocal(patientData2)

                    val intent = Intent(
                        this@OrthosisMainActivity, OrthosisFittingActivity::class.java
                    )
                    intent.putExtra("result", decodedText)
                    startActivity(intent)

                } catch (e: IllegalArgumentException) {
                    // Handle the case where the Base64 content is invalid
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

        // Customize your popup view and set click listeners

        val logoutButton: Button = popupView.findViewById(R.id.popupButton)
        val closeButton: Button = popupView.findViewById(R.id.popupCancel)


        // Create the PopupWindow
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Set background to allow outside clicks to dismiss the popup
        popupWindow.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))

        // Show the popup at a specific location or anchor it to a view
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        logoutButton.setOnClickListener {
            Utility.successToast(this@OrthosisMainActivity,"Logged out successfully!")
            sessionManager.logoutClear(this@OrthosisMainActivity)
            popupWindow.dismiss()
            // exitProcess(0)
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

    private fun updateProgressUI(current: Int, total: Int) {
        // Update your progress bar or text with the progress
        // progressBar.progress = (current * 100) / total
        //progressText.text = "Syncing $current of $total"
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
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
            else
            {
                Utility.infoToast(this@OrthosisMainActivity,"Internet Not Available!")
            }
        }
        tvSyncImages.setOnClickListener {
            finalDialog.dismiss()
            progressForThis.show()
            if (isInternetAvailable(this@OrthosisMainActivity)){
                syncFormImages()
            }
            else
            {
                Utility.infoToast(this@OrthosisMainActivity,"Internet Not Available!")
            }
        }

        tvSyncVideos.setOnClickListener {
            finalDialog.dismiss()
            if (isInternetAvailable(this@OrthosisMainActivity)){
                progressForThis.show()
                syncFormVideos()
            }
            else
            {
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
                //
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
            val data = HashMap<String, Any>()
            data["patient"] = ""
            val intent = Intent(this, OrthosisPatientFormService::class.java).apply {
                putExtra("QUERY_PARAMS", data)
            }
            startService(intent)
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

    override fun onDestroy() {
        super.onDestroy()
//        handler.removeCallbacks(serviceCheckRunnable)
    }

    private fun syncFormImages() {
        doctorProgress.show()

        formImageIndexCheck = 0
        orthosisMainVM.getFormImagesForSync()
        orthosisMainVM.formImagesListForSync.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    doctorProgress.show()
                }

                Status.SUCCESS -> {
                    doctorProgress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            formImageListForSync.clear()
                            for (i in it.data){
                                if (i.isSynced == 0 ){
                                    formImageListForSync.add(i)
                                }

                            }
                            syncNextFormImage()
                        }
                        else{
                            syncOrthosisImages()
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
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
                            doctorProgress.show()
                        }

                        Status.SUCCESS -> {
                            doctorProgress.dismiss()
                            try {
                                if (it.data != null) {
                                    orthosisMainVM.updateFormImageSynced(it.data.success_id ?: 0)
                                    formImageIndexCheck++
                                    syncNextFormImage()
                                }

                            } catch (e: Exception) {

                                Utility.errorToast(this@OrthosisMainActivity, e.message.toString())

                            }
                        }

                        Status.ERROR -> {
                            doctorProgress.dismiss()

                            Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")

                        }
                    }
                }
//        }
            }
            else{
                doctorProgress.dismiss()
//                progressForThis.dismiss()
            syncOrthosisImages()
            }
        }
        else{
            doctorProgress.dismiss()
//            progressForThis.dismiss()
            syncOrthosisImages()
        }
    }

    private fun syncOrthosisImages() {
        orthosisMainVM.getOrthosisImagesForSync()
        orthosisMainVM.orthosisImagesListForSync.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    doctorProgress.show()
                }

                Status.SUCCESS -> {
                    doctorProgress.dismiss()
                    try {

                        if (!it.data.isNullOrEmpty()) {
                            orthosisImageListForSync.clear()
                            for (i in it.data){
                                if (i.isSynced == 0){
                                    orthosisImageListForSync.add(i)
                                }
                            }
                            syncNextOrthosisImage()
                        }
                        else{
                            syncEquipmentImages()
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    doctorProgress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }

    }

    private fun syncNextOrthosisImage(){
        if (orthosisImageListForSync.size!=0){
            if (orthosisImageIndexCheck < orthosisImageListForSync.size) {
                val orthosisImage = orthosisImageListForSync[orthosisImageIndexCheck]
                val file = File(orthosisImage.images)
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

                orthosisMainVM.syncOrthosisImages(imageUploadParams)

                orthosisMainVM.orthosisImageSyncResponse.observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            doctorProgress.show()
                        }

                        Status.SUCCESS -> {
                            doctorProgress.dismiss()
                            try {
                                if (it.data != null) {
                                    orthosisMainVM.updateOrthosisImageSynced(it.data.success_id ?: 0)
                                    orthosisImageIndexCheck++
                                    syncNextOrthosisImage()
                                }

                            } catch (e: Exception) {

                                Utility.errorToast(this@OrthosisMainActivity, e.message.toString())

                            }
                        }

                        Status.ERROR -> {
                            doctorProgress.dismiss()

                            Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")

                        }
                    }
                }

            }
            else{
                syncEquipmentImages()
            }
        }
        else{
            syncEquipmentImages()
        }

    }

    private fun syncEquipmentImages() {
        equipmentImageIndexCheck = 0
        orthosisMainVM.getEquipmentImagesForSync()
        orthosisMainVM.equipmentImagesListForSync.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    doctorProgress.show()
                }

                Status.SUCCESS -> {
                    doctorProgress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            equipmentImageListForSync.clear()
                            for (i in it.data){
                                if (i.isSynced == 0){
                                    equipmentImageListForSync.add(i)
                                }
                            }
                            syncNextEquipmentImage()
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    doctorProgress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }
    }

    private fun syncNextEquipmentImage() {
        if (equipmentImageListForSync.size !=0){
            if (equipmentImageIndexCheck < equipmentImageListForSync.size) {
                val equipmentImage = equipmentImageListForSync[equipmentImageIndexCheck]
                val file = File(equipmentImage.images)
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

                orthosisMainVM.syncEquipmentImages(imageUploadParams)

                orthosisMainVM.equipmentImageSyncResponse.observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            doctorProgress.show()
                        }

                        Status.SUCCESS -> {
                            doctorProgress.dismiss()
                            try {
                                if (it.data != null) {
                                    orthosisMainVM.updateEquipmentImageSynced(it.data.success_id ?: 0)
                                    equipmentImageIndexCheck++
                                    syncNextEquipmentImage()
                                }
                                else{
                                }

                            } catch (e: Exception) {
                                Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                            }
                        }

                        Status.ERROR -> {
                            doctorProgress.dismiss()
                            Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                        }
                    }
                }

            }
            else{
                progressForThis.dismiss()
            }
        }
        else{
            progressForThis.dismiss()
        }

    }

    private fun syncFormVideos() {
        orthosisMainVM.getFormVideosForSync()
        orthosisMainVM.formVideosListForSync.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    doctorProgress.show()
                }

                Status.SUCCESS -> {
                    doctorProgress.dismiss()
                    try {

                        if (!it.data.isNullOrEmpty()) {
                            formVideoListForSync.clear()
                            for (i in it.data){
                                if (i.isSynced == 0){
                                    formVideoListForSync.add(i)
                                }
                            }
                            syncNextFormVideo()
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisMainActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    doctorProgress.dismiss()
                    Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")
                }
            }
        }
    }

    private fun syncNextFormVideo() {
        if (formVideoListForSync.size !=0){
            if (formVideoIndexCheck < formVideoListForSync.size) {
                val formVideo = formVideoListForSync[formVideoIndexCheck]
                val dataMap = HashMap<String, String>()
                val uriMap = HashMap<String, Uri>()
                val fileMap = HashMap<String, File>()
                dataMap["file_type"] = "image"
                dataMap["temp_patient_id"] = formVideo.temp_patient_id
                dataMap["camp_id"] = formVideo.camp_id
                dataMap["patient_id"] = ""
                dataMap["id"] = formVideo.id.toString()
                val file = File(formVideo.video)
                val requestFile = RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    file
                )
                val body = MultipartBody.Part.createFormData(
                    "video",
                    file.name,
                    requestFile
                )

                val tempPatientIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    formVideo.temp_patient_id
                )
                val campIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    formVideo.camp_id
                )
                val patientIdRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    ""
                )
                val idRequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    formVideo.id.toString()
                )



                val imageUploadParams = FormVideoRquest(
                    body,
                    tempPatientIdRequestBody,
                    campIdRequestBody,
                    patientIdRequestBody,
                    idRequestBody
                )

                orthosisMainVM.syncFormVideos(imageUploadParams)

                orthosisMainVM.formVideoSyncResponse.observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            doctorProgress.show()
                        }

                        Status.SUCCESS -> {
                            doctorProgress.dismiss()
                            try {
                                if (it.data != null) {
                                    orthosisMainVM.updateFormVideoSynced(it.data.success_id ?: 0)
                                    formVideoIndexCheck++
                                    syncNextFormVideo()
                                }

                            } catch (e: Exception) {

                                Utility.errorToast(this@OrthosisMainActivity, e.message.toString())

                            }
                        }

                        Status.ERROR -> {
                            doctorProgress.dismiss()

                            Utility.errorToast(this@OrthosisMainActivity, "Unexpected error")

                        }
                    }
                }

            }
            else{
                progressForThis.dismiss()
            }
        }
        else{
            progressForThis.dismiss()
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
}