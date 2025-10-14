package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.InventoryUnitLocal
import org.impactindiafoundation.iifllemeddocket.Model.Login.LoginData
import org.impactindiafoundation.iifllemeddocket.Model.Login.LoginRequest
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.MyValidator
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SharedPrefUtil
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.ViewModel.ResourceApp
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ORTHOSIS_LOGIN
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.PSD_LOGIN
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.LoginViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityLoginBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.OrthosisActivity
import org.impactindiafoundation.iifllemeddocket.ui.activity.OrthosisMainActivity


class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    private val loginViewModel: LoginViewModel by viewModels()
    lateinit var appUpdateManager : AppUpdateManager
    lateinit var appUpdateInfoTask: Task<AppUpdateInfo>

    companion object {
        private const val REQUEST_CODE_UPDATE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        checkForAppUpdate()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        getViewModel()
        createRoomDatabase()

        binding.chkKeepMeLogIn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.chkKeepMeLogIn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.checkbox_checked,
                    0,
                    0,
                    0
                )
            } else {
                binding.chkKeepMeLogIn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.checkbox_unchecked,
                    0,
                    0,
                    0
                )
            }
        }

        binding.btnLogin.setOnClickListener(this)
    }

    private fun initObserver() {
        loginViewModel.insertUserResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try { }
                    catch (e: Exception) { }
                }
                Status.ERROR -> {}
            }
        }
    }

    private fun createRoomDatabase() {
        val database = LLE_MedDocket_Room_Database.getDatabase(this)

        if (!database.isOpen) {
            Log.d(ConstantsApp.TAG, "Database is created")
        } else {
            Log.d(ConstantsApp.TAG, "Database is not created")
        }

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

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> {
                if (validation()) {
                    val user_name = binding.editTextUsername?.text.toString()
                    val password = binding.editTextPassword?.text.toString()

                    val sessionData = sessionManager.getLoginData()
                    if (sessionData != null) {
                        for (data in sessionData) {
                            val email_id = data?.Emailid
                            val password1 = data?.Password
                            if (email_id == user_name && password == password1) {
                                Log.d(ConstantsApp.TAG, "camp_id=>" + data.camp_id)
                                handleSuccessfulLogin(data)
                                return // Exiting the loop if a match is found
                            }
                        }
                    }
                    userLogin(user_name, password)
                }
            }
        }
    }

    private fun handleSuccessfulLogin(data: LoginData) {
        when (data.Designation_name) {
            "Data Entry Volunteer(LLE)" -> {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            "PSD Volunteer(LLE)" -> {
                sessionManager.setCampUserID(data.camp_id.toString(), data.Userid.toString())
                val intent = Intent(this@LoginActivity, PresciptionMainActivity::class.java)
                intent.putExtra(PSD_LOGIN, true)
                startActivity(intent)
                finish()
            }

            "Indent User" -> {
                sessionManager.setCampUserID(data.camp_id.toString(), data.Userid.toString())
                val intent = Intent(this@LoginActivity, PharmaMainActivity::class.java)
                intent.putExtra("isLogin",true)
                startActivity(intent)
                finish()
            }

            "Orthotist" -> {
                sessionManager.setCampUserID(data.camp_id.toString(), data.Userid.toString())
                val intent = Intent(this@LoginActivity, OrthosisMainActivity::class.java)
                intent.putExtra(ORTHOSIS_LOGIN, true)
                startActivity(intent)
                finish()
            }
        }
        finish()
    }

    private fun userLogin(user_name: String, password: String) {
        val loginRequest = LoginRequest(user_name, password)
        viewModel.login(loginRequest, progressDialog)
        loginResponse()
    }

    private fun loginResponse() {
        viewModel.userLoginLive.observe(this, Observer { response ->
            when (response) {
                is ResourceApp.Success -> {
                    progressDialog.dismiss()
                    when (response.data!!.ErrorMessage) {
                        "success" -> {
                            if (binding.chkKeepMeLogIn.isChecked) {
                                val loginData = response.data.LoginData

                                sessionManager.saveLoginData(response.data.LoginData)

                                var prefix = ""
                                if (loginData[0].Prefix == null) {
                                    prefix = ""
                                }
                                val data = response.data.LoginData[0]
                                SharedPrefUtil.savePrefInt(
                                    this@LoginActivity,
                                    SharedPrefUtil.USER_CAMP,
                                    data.camp_id
                                )
                                val user = UserModel(
                                    userId = data.Userid,
                                    address = "",
                                    contactNo = "",
                                    designationName = data.Designation_name,
                                    designationNo = data.Designation_no,
                                    emailId = "",
                                    firstName = data.FirstName,
                                    lastName = data.LastName,
                                    mobileNumber = "",
                                    password = data.Password,
                                    prefix = prefix,
                                    title = data.Title,
                                    userName = data.UserName,
                                    campId = data.camp_id,
                                    campName = data.camp_name,
                                    campNo = data.camp_no,
                                    campTo = data.campto,
                                    campFrom = data.campfrom
                                )
                                loginViewModel.insertUserList(user)

                                when (data.Designation_name) {
                                    "Data Entry Volunteer(LLE)" -> {
                                        gotoScreen(this, MainActivity::class.java)
                                    }
                                    "PSD Volunteer(LLE)" -> {
                                        sessionManager.setCampUserID(
                                            data.camp_id.toString(),
                                            data.Userid.toString()
                                        )
                                        val intent = Intent(this@LoginActivity, PresciptionMainActivity::class.java)
                                        intent.putExtra(PSD_LOGIN, true)
                                        startActivity(intent)
                                        finish()
                                    }
                                    "Indent User" -> {
                                        sessionManager.setCampUserID(
                                            data.camp_id.toString(),
                                            data.Userid.toString()
                                        )
                                        val intent = Intent(this@LoginActivity, PharmaMainActivity::class.java)
                                        intent.putExtra("isLogin",true)
                                        startActivity(intent)
                                        finish()
                                    }
                                    "Orthotist" -> {
                                        sessionManager.setCampUserID(
                                            data.camp_id.toString(),
                                            data.Userid.toString()
                                        )
                                        val intent = Intent(
                                            this@LoginActivity,
                                            OrthosisMainActivity::class.java
                                        )
                                        intent.putExtra(ORTHOSIS_LOGIN, true)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            } else {
                                gotoScreen(this, LoginActivity::class.java)
                            }
                        }
                        "Data not available" -> {
                            Toast.makeText(this, "Camp is not active", Toast.LENGTH_SHORT).show()
                        }
                        "Invalid" -> {
                            Toast.makeText(this, "Credentials Wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is ResourceApp.Error -> {
                    progressDialog.dismiss()
                }
                is ResourceApp.Loading -> {
                    progressDialog.show()
                }
            }
        })
    }


    private fun validation(): Boolean {
        var flag = true
        if (!MyValidator.isValidField(binding.editTextUsername)) {
            flag = false
        }
        if (!MyValidator.isValidField(binding.editTextPassword)) {
            flag = false
        }
        return flag
    }


    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("Update", "Update flow failed! Result code: $resultCode")
            }
        }
    }
}