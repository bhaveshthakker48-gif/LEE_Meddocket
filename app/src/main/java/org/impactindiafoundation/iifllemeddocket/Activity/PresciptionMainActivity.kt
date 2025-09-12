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
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import org.impactindiafoundation.iifllemeddocket.Dialog.MyProgressDialog
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Patient_RegistrationModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Prescription_Model
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleDisdributionStatusModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.AddPrecriptionFinal
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.PrescriptionGlasse
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataLive
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataModel
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp.Companion.formatAadharNumber
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SharedPrefUtil
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.ViewModel.ResourceApp
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.PSD_LOGIN
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPrescriptionMainBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.QrCodeActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Base64
import java.util.Date
import java.util.Locale


class PresciptionMainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager

    private lateinit var popupWindow5: PopupWindow

    private var isCallInProgress = false

    private var progressDialog1: ProgressDialog? = null

    private var isSyned = false

    private var myProgressDialog: MyProgressDialog? = null


    private lateinit var popupWindow: PopupWindow
    private lateinit var popupWindow1: PopupWindow
    private lateinit var popupWindow2: PopupWindow
    private lateinit var popupWindow3: PopupWindow
    private lateinit var popupWindow4: PopupWindow
    private var patientId = 0
    private var campId = 0
    private var intentDecodeText = ""


    lateinit var binding: ActivityPrescriptionMainBinding

    lateinit var appUpdateManager: AppUpdateManager
    lateinit var appUpdateInfoTask: Task<AppUpdateInfo>

    companion object {
        private const val REQUEST_CODE_UPDATE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars =
            true
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

        binding.CardViewLogout.setOnClickListener(this)
        binding.CardViewViewReport!!.setOnClickListener(this)
        binding.CardViewScanQR.setOnClickListener(this)
        binding.CardViewViewPatientID.setOnClickListener(this)
        binding.tvAdharNo.setOnClickListener(this)
        binding.tvPatientID.setOnClickListener(this)
        binding.fab.setOnClickListener(this)
        binding.tvUnsyncedForms.setOnClickListener(this)
        binding.tvUnsyncedImages.setOnClickListener(this)

        binding.ivRefreshSync.setOnClickListener(this)

        binding.aboutUsContainer.setOnClickListener {
            openAboutUsDailogueBox()
        }

        intentDecodeText = intent.getStringExtra("result") ?: ""
        Log.d("eric", "Raw intent result: '$intentDecodeText'")
        if (!intentDecodeText.isNullOrEmpty()) {
            Log.d("eric", "Inside intentDecodeText block")
            // Use Gson to parse the JSON string
            val gson = Gson()
            val patientData = gson.fromJson(intentDecodeText, PatientData::class.java)
            val patientData1 = gson.fromJson(intentDecodeText, PatientDataLocal::class.java)
            val patientID = patientData.patientId
            patientId = patientData.patientId
            campId = patientData.camp_id
            Log.d("eric", "Assigned patientId: $patientId, campId: $campId")
            GetPrescriptionStatusDetails(patientID)
        } else {
            Log.d("eric", "intentDecodeText is null or empty — going to else block")
            val isLogin = intent.getBooleanExtra(PSD_LOGIN, false)
            GetPatientData()
            checkForAppUpdate()
        }

        binding.viewPatientData.setOnClickListener {
            val intent = Intent(this, PrescriptionPatientDataActivity::class.java)
            startActivity(intent)
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
        try {
            // Get version name from PackageManager
            val versionName = packageManager
                .getPackageInfo(packageName, 0).versionName

            // Build and show dialog
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("What's New in version $versionName")
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun openAboutUsDailogueBox() {
        try {
            // Get app name from resources
            val appName = getString(R.string.app_name)

            // Get version name from PackageManager
            val versionName = packageManager
                .getPackageInfo(packageName, 0).versionName

            // Build About Us message
            val message = """
            $appName

            Impact India Foundation’s LLE MedDocket App is meant for the use of Lifeline Express team and volunteers associated with each camp of Impact India Foundation’s Lifeline Express. 

            Volunteers, Prescription Spectacles Distribution users, Pharmacy users and Orthosis & Prosthetics users can login to the app by scanning the QR code on the registration form.

            This app is an effort of Impact India Foundation to maintain complete data of all beneficiaries of Lifeline Express camps digitally, for the sake of transparency, analytics and improving quality of healthcare delivery to the served community.

            You are currently using version $versionName of the LLE MedDocket App.
        """.trimIndent()

            // Show AlertDialog
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("About Us")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun GetPatientData() {
        GetPriscriptionData()
        GetResgistrationData()
        GetPriscriptionStatusData()
    }

    private fun GetPriscriptionStatusData() {
        viewModel.getPriscriptionStatusDetails(progressDialog)

        GetPriscriptionStatusDetailsResponse()
    }

    private fun GetPriscriptionStatusDetailsResponse() {
        viewModel.getPriscriptionGlassStatusDetailsResponse.observe(this, Observer { response ->


            when (response) {
                is ResourceApp.Success -> {
                    progressDialog.dismiss()
                    val message = response.data!!.ErrorMessage

                    Log.d(
                        "pawan",
                        "GetPriscriptionStatusDetailsResponse=>" + response.data.ErrorMessage
                    )
                    Log.d(
                        "pawan",
                        "GetPriscriptionStatusDetailsResponse=>" + response.data.PrescriptionGlasses
                    )

                    when (message) {
                        "Success" -> {
                            val PrescriptionGlasses = response.data.PrescriptionGlasses

                            for (data in PrescriptionGlasses) {
                                val alternate_mobile = data.alternate_mobile
                                val app_createdDate = data.app_createdDate
                                val call_again_date = data.call_again_date
                                val camp_id = data.camp_id
                                val id = data.id
                                val patient_call_again = data.patient_call_again
                                val patient_id = data.patient_id
                                val patient_not_come = data.patient_not_come
                                val postal_addressline1 = data.postal_addressline1
                                val postal_addressline2 = data.postal_addressline2
                                val postal_city = data.postal_city
                                val postal_country = data.postal_country
                                val postal_pincode = data.postal_pincode
                                val postal_state = data.postal_state
                                val sameAddress = data.sameAddress
                                val spectacle_given = data.spectacle_given
                                val spectacle_not_matching = data.spectacle_not_matching
                                val ordered_not_received = data.ordered_not_received
                                val user_id = data.user_id
                                val spectacle_not_matching_details =
                                    data.spectacle_not_matching_details

                                val SpectacleDisdributionStatusModel =
                                    SpectacleDisdributionStatusModel(
                                        0, alternate_mobile,
                                        app_createdDate,
                                        call_again_date,
                                        camp_id,
                                        id,
                                        patient_call_again,
                                        patient_id.toInt(),
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
                                        spectacle_not_matching_details,
                                        "",
                                        user_id
                                    )

                                InsertPrescriptionGlassStatus(SpectacleDisdributionStatusModel)


                            }
                        }
                    }

                }

                is ResourceApp.Error -> {
                    progressDialog.dismiss()
                }

                is ResourceApp.Loading -> {
                    progressDialog.show()
                }

                else -> {

                }
            }

        })
    }

    private fun InsertPrescriptionGlassStatus(spectacleDisdributionStatusModel: SpectacleDisdributionStatusModel) {

        Log.d(
            "pawan",
            "spectacleDisdributionStatusModel Insert=> ${spectacleDisdributionStatusModel}"
        )

        viewModel1.insertSpectacleDisdributionStatusModel(spectacleDisdributionStatusModel)
    }

    private fun GetResgistrationData() {
        viewModel.getRegistrationDetails(progressDialog)

        GetRegistrationDataResponse()
    }

    private fun GetRegistrationDataResponse() {
        viewModel.getRegistrationDetailsResponse.observe(this, Observer { response ->

            Log.d("pawan", "getRegistrationDetailsResponse" + response.data.toString())

            response.data?.let { data ->
                data.RegistrationDetails?.let { registration ->

                    for (data in registration) {
                        val aadharno = data.aadharno
                        val age = data.age
                        val ageunit = data.ageunit
                        val campid = data.campid
                        val citytownvillage = data.citytownvillage
                        val district = data.district
                        val dob = data.dob
                        val emailid = data.emailid
                        val fname = data.fname
                        val gender = data.gender
                        val houseno = data.houseno
                        val lname = data.lname
                        val localityareapada = data.localityareapada
                        val mname = data.mname
                        val mobileno = data.mobileno
                        val patient_id = data.patient_id
                        val pincode = data.pincode
                        val regno = data.regno
                        val statename = data.statename
                        val streetname = data.streetname
                        val taluka = data.taluka
                        val voteridno = data.voteridno

                        val RegistrationDetail = Patient_RegistrationModel(
                            0,
                            aadharno ?: "",
                            age ?: "",
                            ageunit ?: "NA", // 👈 default if null
                            campid ?: 0,
                            citytownvillage ?: "",
                            district ?: "",
                            dob ?: "",
                            emailid ?: "",
                            fname ?: "",
                            gender ?: "",
                            houseno ?: "",
                            lname ?: "",
                            localityareapada ?: "",
                            mname ?: "",
                            mobileno ?: "",
                            patient_id ?: 0,
                            pincode ?: "",
                            regno ?: "",
                            statename ?: "",
                            streetname ?: "",
                            taluka ?: "",
                            voteridno ?: ""
                        )
                        InsertRegistration(RegistrationDetail)
                    }
                }
            }
        })
    }

    private fun InsertRegistration(registrationDetail: Patient_RegistrationModel) {

        viewModel1.insertRegistraion(registrationDetail)

    }

    private fun GetPriscriptionData() {
        viewModel.getPriscriptionDetails(progressDialog)

        GetPriscriptionDataResponse()
    }

    private fun GetPriscriptionDataResponse() {
        viewModel.getPrescriptiondetailsResponse.observe(this, Observer { response ->

            Log.d("pawan", "GetPriscriptionDataResponse" + response.data.toString())


            response.data?.let { data ->
                data.patientPrescription?.let { prescriptions ->

                    for (data in prescriptions) {

                        val camp_id = data.camp_id
                        val fundus_notes = data.fundus_notes
                        val is_given = data.is_given
                        val is_ordered = data.is_ordered
                        val patient_id = data.patient_id

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
                        val re_reading_addition_left = data.re_reading_addition_left ?: ""
                        val re_reading_addition_left_details = data.re_reading_addition_left_details
                        val re_reading_addition_right = data.re_reading_addition_right
                        val re_reading_addition_right_details =
                            data.re_reading_addition_right_details
                        val re_remark_left = data.re_remark_left
                        val re_remark_right = data.re_remark_right
                        val re_remarks = data.re_remarks
                        val reading_glass = data.reading_glass
                        val user_id = data.user_id

                        val presc_type = data.presc_type ?: "default_value"


                        val Prescription_Model = Prescription_Model(
                            0,
                            camp_id,
                            fundus_notes.toString(),
                            is_given.toString(),
                            is_ordered.toString(),
                            patient_id,
                            presc_type,
                            re_bvd.toString(),
                            re_distant_vision_axis_left.toString(),
                            re_distant_vision_axis_right.toString(),
                            re_distant_vision_cylinder_left.toString(),
                            re_distant_vision_cylinder_right.toString(),
                            re_distant_vision_left.toString(),
                            re_distant_vision_right.toString(),
                            re_distant_vision_sphere_left.toString(),
                            re_distant_vision_sphere_right.toString(),
                            re_distant_vision_unit_left.toString(),
                            re_distant_vision_unit_right.toString(),
                            re_near_vision_axis_left.toString(),
                            re_near_vision_axis_right.toString(),
                            re_near_vision_cylinder_left.toString(),
                            re_near_vision_cylinder_right.toString(),
                            re_near_vision_left.toString(),
                            re_near_vision_right.toString(),
                            re_near_vision_sphere_left.toString(),
                            re_near_vision_sphere_right.toString(),
                            re_prism_left.toString(),
                            re_prism_right.toString(),
                            re_prism_unit_left.toString(),
                            re_prism_unit_right.toString(),
                            re_pupipllary_distance.toString(),
                            re_reading_addition_left.toString(),
                            re_reading_addition_left_details ?: "",
                            re_reading_addition_right ?: "",
                            re_reading_addition_right_details ?: "",
                            re_remark_left.toString(),
                            re_remark_right.toString(),
                            re_remarks.toString(),
                            reading_glass ?: "",
                            user_id
                        )
                        InsertPrescription(Prescription_Model)
                    }
                }
            }


        })
    }

    private fun InsertPrescription(prescriptionModel: Prescription_Model) {
        viewModel1.insertPrescription(prescriptionModel)
    }

    override fun onClick(v: View?) {

        when (v) {
            binding.tvUnsyncedForms -> {
                startActivity(Intent(this, ViewStatusActivity::class.java))
            }

            binding.tvUnsyncedImages -> {
                startActivity(Intent(this, PrescriptionReportActivity::class.java))
            }

            binding.CardViewLogout -> {
                showPopup()
            }

            binding.ivRefreshSync -> {
                checkSyncedData()
            }

            binding.CardViewScanQR -> {
                val intent = Intent(this@PresciptionMainActivity, QrCodeActivity::class.java)
                intent.putExtra("screen", Constants.SCREEN_PSD)
                startActivity(intent)
            }

            binding.tvScanner -> {
                val intent = Intent(this@PresciptionMainActivity, QrCodeActivity::class.java)
                intent.putExtra("screen", Constants.SCREEN_PSD)
                startActivity(intent)
                finish()
            }

            binding.CardViewViewReport -> {
                showPopupAadhar()
            }

            binding.tvAdharNo -> {
                showPopupAadhar()
            }

            binding.CardViewViewPatientID -> {
                showPopupPatientID()
            }

            binding.tvPatientID -> {
                showPopupPatientID()
            }

            binding.fab -> {
                if (isInternetAvailable(this@PresciptionMainActivity)) {
                    Log.d(ConstantsApp.TAG, "fab clicked")
                    showPopupSyn()
                } else {
                    Utility.infoToast(this@PresciptionMainActivity, "Internet Not Available")
                }
            }
        }

    }

    private fun Upload_Data_Local_Server() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Data...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        viewModel1.allFinalPrescriptionData.observe(this, Observer { response ->
            Log.d(ConstantsApp.TAG, "allFinalPrescriptionData=>" + response.toString())

            if (!isCallInProgress && response.isNotEmpty()) {
                isCallInProgress = true  // Set the flag to true to indicate call in progress

                val prescriptionGlassesFinalList = mutableListOf<PrescriptionGlasse>()

                for (data in response) {
                    if (data.isSyn == 0) {
                        val _id = data._id
                        val alternate_mobile = data.alternate_mobile
                        val app_createdDate = data.app_createdDate
                        val call_again_date = data.call_again_date
                        val camp_id = data.camp_id
                        val patient_call_again = data.patient_call_again
                        val patient_id = data.patient_id
                        val patient_not_come = data.patient_not_come
                        val postal_addressline1 = data.postal_addressline1
                        val postal_addressline2 = data.postal_addressline2
                        val postal_city = data.postal_city
                        val postal_country = data.postal_country
                        val postal_pincode = data.postal_pincode
                        val postal_state = data.postal_state
                        val sameAddress = data.sameAddress
                        val spectacle_given = data.spectacle_given
                        val spectacle_not_matching = data.spectacle_not_matching
                        val ordered_not_received = data.ordered_not_received
                        val user_id = data.user_id
                        val id = data.server_id
                        val spectacle_not_matching_details = data.spectacle_not_matching_details
                        val given_presc_type = data.given_presc_type

                        val prescriptionGlasse = PrescriptionGlasse(
                            _id,
                            alternate_mobile,
                            app_createdDate,
                            call_again_date,
                            camp_id,
                            id,
                            ordered_not_received,
                            patient_call_again,
                            patient_id,
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
                            spectacle_not_matching_details,
                            given_presc_type,
                            user_id
                        )
                        prescriptionGlassesFinalList.add(prescriptionGlasse)
                    }
                }

                if (prescriptionGlassesFinalList.isNotEmpty()) {
                    Log.d(
                        ConstantsApp.TAG,
                        "PrescriptionGlassesFinalList=>" + prescriptionGlassesFinalList
                    )

                    val addPrescriptionFinal = AddPrecriptionFinal(prescriptionGlassesFinalList)

                    viewModel.addPrecriptionGlassesResponse(progressDialog, addPrescriptionFinal)
                    AddPrecriptionGlassesResponse(progressDialog)

                    isCallInProgress = false

                } else {
                    progressDialog.dismiss()
                    isCallInProgress = false
                }
            }
        })
    }

    private fun checkSyncedData() {
        // Show progress dialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Refreshing Synced Data...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        viewModel1.allFinalPrescriptionDataForSync.observe(this, Observer { response ->
            Log.d(ConstantsApp.TAG, "allFinalPrescriptionData=>" + response.toString())

            if (!isCallInProgress && response.isNotEmpty()) {
                isCallInProgress = true  // Set the flag to true to indicate call in progress

                val unsyncedData = ArrayList<Int>()
                var index = 0
                for (data in response) {
                    if (data.isSyn == 0) {
                        unsyncedData.add(index)
                        index++
                    }
                }

                if (!unsyncedData.isNullOrEmpty()) {
                    Log.d(
                        "Unsynced Data",
                        "Unsynced Count=>" + unsyncedData
                    )
                    isCallInProgress = false
                    progressDialog.dismiss()
                    binding.tvSyncStatusDetail!!.text = "Sync Not Complete"
                    binding.tvSyncStatusDetail!!.setTextColor(resources.getColor(R.color.dark_red))

                } else {
                    // If no data to upload, dismiss dialog and reset flag
                    binding.tvSyncStatusDetail!!.text = "Sync Complete"
                    binding.tvSyncStatusDetail!!.setTextColor(resources.getColor(R.color.dark_green))

                    progressDialog.dismiss()
                    isCallInProgress = false
                }
            } else {
                binding.tvSyncStatusDetail!!.text = "Sync Complete"
                binding.tvSyncStatusDetail!!.setTextColor(resources.getColor(R.color.dark_green))

                progressDialog.dismiss()
                isCallInProgress = false
            }
        })
    }

    private fun AddPrecriptionGlassesResponse(progressDialog: ProgressDialog) {
        viewModel.addPrecriptionGlassesResponse.observe(this, Observer { response ->
            progressDialog.dismiss()
            when (response) {
                is ResourceApp.Success -> {
                    Log.d(ConstantsApp.TAG, "ErrorMessage=>" + response.data!!.ErrorMessage)
                    Log.d(ConstantsApp.TAG, "ErrorCode=>" + response.data!!.ErrorCode)
                    Log.d(
                        ConstantsApp.TAG,
                        "prescriptionGlasses=>" + response.data!!.prescriptionGlasses
                    )
                    response.data.prescriptionGlasses?.let { data ->
                        for (prescriptionGlasses in data) {
                            val isSyn = 1
                            UpdatePrescriptionGlassesResponse(prescriptionGlasses._id, isSyn)
                        }
                    }
                    this.progressDialog.dismiss()
                }

                is ResourceApp.Error -> {
                    this.progressDialog.dismiss()
                    // Handle the error case if needed
                }

                is ResourceApp.Loading -> {
                    this.progressDialog.show()
                }

                else -> {

                }

            }

        })
    }


    private fun UpdatePrescriptionGlassesResponse(_id: String, syn: Int) {
        viewModel1.UpdatePrescriptionGlassesResponse(_id, syn)
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopupAadhar() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_popup_aadhar_layout, null)
        val closeButton: Button = popupView.findViewById(R.id.popupButton)
        val popupEditText_Aadhar: EditText = popupView.findViewById(R.id.popupEditText_Aadhar)
        closeButton.setOnClickListener {

            val aadhar_no = popupEditText_Aadhar.text.toString()
            CheckStatus(aadhar_no)
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

    private fun CheckStatus(identity: String) {
        val data = formatAadharNumber(identity)
        viewModel1.getPatientID_By_AadharNo(data)
        getPatientID_By_AadharNoResponse()
    }

    private fun getPatientID_By_AadharNoResponse() {
        viewModel1.registration.observe(this, Observer { response ->
            for (registration in response) {
                val aadhar_no = registration.aadharno ?: ""
                val patient_id = registration.patient_id
                sessionManager.setPatientIdentity(aadhar_no, patient_id.toString())
                GetPrescriptionStatusDetails(patient_id)
            }
        })
    }

    private fun GetPrescriptionStatusDetails(patientId: Int) {
        val forceRefresh = true
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Waiting for data...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val patientId = patientId // Provide patient ID
        val pageSize =
            20 // Specify page size (e.g., 20, 50, or any value based on your requirement)
        val firstPage = 0 // or 1 if pagination starts from 1
        viewModel1.getPrescriptionStatusDetailsWithPaginationNew(
            patientId,
            pageSize,
            firstPage,
            false
        )
        GetPrescriptionStatusDetailsResponse(progressDialog)
    }

    private fun GetPrescriptionStatusDetailsResponse(progressDialog: ProgressDialog) {
        val (pid, camp_id, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
        viewModel1.prescriptionsStatus.observe(this, Observer { response ->
            progressDialog.dismiss()
            if (response != null && response.isNotEmpty()) {
                var mostRecentData: SpectacleDisdributionStatusModel? = null
                var mostRecentDate: LocalDateTime? = null

                for (data in response) {
                    if (!data.app_createdDate.isNullOrBlank()) {
                        if (mostRecentDate == null) {
                            mostRecentData = data
                        }
                    }
                }

                if (mostRecentData != null) {
                    Log.d("pawan", "Most recent data:")
                    Log.d("pawan", "app_createdDate: " + mostRecentData.app_createdDate)
                    Log.d("pawan", "patient_id: " + mostRecentData.patient_id)
                    Log.d("pawan", "spectacle_given: " + mostRecentData.spectacle_given)

                    val spectacle_given = mostRecentData.spectacle_given
                    val spectacle_not_matching = mostRecentData.spectacle_not_matching
                    val spectacle_not_received = mostRecentData.spectacle_not_received
                    val patient_call_again = mostRecentData.patient_call_again
                    val patient_not_come = mostRecentData.patient_not_come

                    when {
                        !spectacle_given && !spectacle_not_matching && !spectacle_not_received && !patient_call_again && !patient_not_come -> {
                            Log.d(ConstantsApp.TAG, "condition 1")
                            val intent = Intent(
                                this@PresciptionMainActivity,
                                PrescriptionDisbributionActivity::class.java
                            )
                            intent.putExtra("patient_id", patientId)
                            intent.putExtra("camp_id", camp_id)
                            intent.putExtra("result", intentDecodeText)
                            startActivity(intent)
                        }

                        spectacle_given -> {
                            Log.d(ConstantsApp.TAG, "condition 2")
                            val message = "Message: Spectacle is already given."
                            showPopupCheckStatus(mostRecentData, message)
                        }

                        spectacle_not_matching -> {
                            val message = "Message: Spectacle not matching. Patient called again."
                            showPopupCheckStatus(mostRecentData, message)
                        }

                        spectacle_not_received -> {
                            val message = "Message: Spectacle not received. Patient called again."
                            showPopupCheckStatus(mostRecentData, message)
                        }
                    }
                } else {
                    Log.d("pawan", "No valid data in the response.")
                }
            } else {
                Log.d("pawan", "Response is null or empty=>" + response)
                val intent = Intent(
                    this@PresciptionMainActivity,
                    PrescriptionDisbributionActivity::class.java
                )
                Log.d("pawan", "patientId" + patientId)
                Log.d("pawan", "campId" + camp_id)
                Log.d("pawan", "intentDecodeText" + intentDecodeText)
                intent.putExtra("patient_id", patientId)
                intent.putExtra("camp_id", camp_id)
                intent.putExtra("result", intentDecodeText)
                startActivity(intent)
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
                    GetPrescriptionStatusDetails(patientData2.patientId)
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


    @SuppressLint("MissingInflatedId")
    private fun showPopupCheckStatus(
        mostRecentData: SpectacleDisdributionStatusModel,
        message: String
    ) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_popup_check_status_layout, null)
        val closeButton: Button = popupView.findViewById(R.id.popupButtonPatientID)
        val registrationData = GetPatientRegistrationDetails(mostRecentData.patient_id)
        val TextView_status: TextView = popupView.findViewById(R.id.TextView_status)
        TextView_status.text = message
        closeButton.setOnClickListener {
            val spectacle_given = mostRecentData.spectacle_given
            val spectacle_not_matching = mostRecentData.spectacle_not_matching
            val spectacle_not_received = mostRecentData.spectacle_not_received
            val patient_call_again = mostRecentData.patient_call_again
            val patient_not_come = mostRecentData.patient_not_come

            when {
                !spectacle_given && !spectacle_not_matching && !spectacle_not_received && !patient_call_again && !patient_not_come -> {
                    val intent = Intent(
                        this@PresciptionMainActivity,
                        PrescriptionDisbributionActivity::class.java
                    )
                    intent.putExtra("patient_id", patientId)
                    intent.putExtra("camp_id", campId)
                    intent.putExtra("result", intentDecodeText)
                    startActivity(intent)
                }

                spectacle_given -> {
                    popupWindow3.dismiss()
                }

                spectacle_not_matching -> {
                    popupWindow3.dismiss()
                    val intent = Intent(
                        this@PresciptionMainActivity,
                        PrescriptionDisbributionActivity::class.java
                    )
                    intent.putExtra("patient_id", patientId)
                    intent.putExtra("camp_id", campId)
                    intent.putExtra("result", intentDecodeText)
                    startActivity(intent)
                }

                spectacle_not_received -> {
                    popupWindow3.dismiss()
                    val intent = Intent(
                        this@PresciptionMainActivity,
                        PrescriptionDisbributionActivity::class.java
                    )
                    intent.putExtra("patient_id", patientId)
                    intent.putExtra("camp_id", campId)
                    intent.putExtra("result", intentDecodeText)
                    startActivity(intent)
                }
            }

            popupWindow3.dismiss()
        }

        popupWindow3 = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow3.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        popupWindow3.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }


    private fun GetPatientRegistrationDetails(patientId: Int): LiveData<List<Patient_RegistrationModel>> {
        var resultLiveData = MutableLiveData<List<Patient_RegistrationModel>>()
        viewModel1.getRegistrationByPatientId(patientId)
        viewModel1.registration.observe(this, Observer { response ->
            resultLiveData = response as MutableLiveData<List<Patient_RegistrationModel>>
        })
        return resultLiveData
    }


    @SuppressLint("MissingInflatedId")
    private fun showPopupPatientID() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_popup_patient_layout, null)
        val closeButton: Button = popupView.findViewById(R.id.popupButtonPatientID)
        val EditText_patientID: EditText = popupView.findViewById(R.id.EditText_patientID)
        closeButton.setOnClickListener {
            if (!EditText_patientID.text.isNullOrEmpty()) {
                val patientID = EditText_patientID.text.toString()
                patientId = EditText_patientID.text.toString().toInt()
                sessionManager.setPatientIdentity("", patientID)

                GetPrescriptionStatusDetails(patientID.toInt())
                popupWindow2.dismiss()
            } else {
                popupWindow2.dismiss()
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


    @SuppressLint("MissingInflatedId")
    private fun showPopup() {

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_popup_layout, null)
        val closeButton: Button = popupView.findViewById(R.id.popupButton)
        val popupCancel: Button = popupView.findViewById(R.id.popupCancel)
        closeButton.setOnClickListener {
            Utility.successToast(this@PresciptionMainActivity, "Logged out successfully!")
            sessionManager.logoutClear(this@PresciptionMainActivity)
            popupWindow.dismiss()
            val intent = Intent(this@PresciptionMainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            this.finish()

        }
        popupCancel.setOnClickListener {
            sessionManager.clearCache(this@PresciptionMainActivity)
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

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }


    @SuppressLint("MissingInflatedId")
    private fun showPopupSyn() {

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_syn_layout1, null)

        val closeButton: Button = popupView.findViewById(R.id.popupButton1)
        val popupButtonCancel: Button = popupView.findViewById(R.id.popupButtonCancel)
        closeButton.setOnClickListener {
            Upload_Data_Local_Server()
            SynCompleted()
            popupWindow1.dismiss()
        }

        popupButtonCancel.setOnClickListener {
            popupWindow1.dismiss()
        }

        // Create the PopupWindow
        popupWindow1 = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Set background to allow outside clicks to dismiss the popup
        popupWindow1.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))

        // Show the popup at a specific location or anchor it to a view
        popupWindow1.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    private fun SynCompleted() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_syn_completed_layout, null)

        val okayButton: Button = popupView.findViewById(R.id.popupButton_synCompleted)
        val popupButtonCancel: Button = popupView.findViewById(R.id.popupCancel_synCompleted)
        val popupCounts: TextView = popupView.findViewById(R.id.popupCounts)
        val popupTitle: TextView = popupView.findViewById(R.id.popupTitle)
        popupTitle.text = "All data have been synced successfully."
        popupCounts.visibility = View.GONE
        okayButton.setOnClickListener {

            if (!isSyned) {
                SynedData()
                isSyned = true
            }

            popupWindow5.dismiss()
            checkSyncedData()
        }

        popupButtonCancel.setOnClickListener {
            popupWindow5.dismiss()
            checkSyncedData()

        }

        popupWindow5 = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Set background to allow outside clicks to dismiss the popup
        popupWindow5.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))

        // Show the popup at a specific location or anchor it to a view
        popupWindow5.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    private fun SynedData() {
        val (campId, userId) = sessionManager.getCampUserID()
        var synTableRequest: SynTable? = null
        if (campId != null) {
            Log.d(ConstantsApp.TAG, "campId=>" + campId)
            synTableRequest = SynTable(
                0, "PSD",
                campId!!.toInt(),
                userId!!,
                ConstantsApp.getCurrentOnlyDate(),
                ConstantsApp.getCurrentTime()
            )
        } else {
            synTableRequest = SynTable(
                0, "PSD",
                0,
                "0",
                ConstantsApp.getCurrentOnlyDate(),
                ConstantsApp.getCurrentTime()
            )
        }

        viewModel1.insertSynedData(synTableRequest)
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
                    Log.d(ConstantsApp.TAG, "SynedDataList=>" + data)
                    viewModel.insertSynedData(progressDialog, data)
                    Insert_SynedData_Response()
                } else {
                    // Handle the case where there are no items with isSyn = 0
                }
            } else {
                // The response is empty, handle it accordingly
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

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
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
}




