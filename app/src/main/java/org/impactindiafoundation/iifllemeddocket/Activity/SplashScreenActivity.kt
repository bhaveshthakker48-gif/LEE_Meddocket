package org.impactindiafoundation.iifllemeddocket.Activity

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.LLE_MedDocket_ViewModel
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivitySplashScreenBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.OrthosisMainActivity

class SplashScreenActivity:AppCompatActivity() {


lateinit var binding:ActivitySplashScreenBinding

    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager

    private val MY_REQUEST_CODE = 1000  // Choose any code you prefer

    lateinit var appUpdateInfoTask: Task<AppUpdateInfo>
    private val appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(this) }


    companion object {
        private const val REQUEST_CODE_UPDATE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
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

        binding.textViewAboveBottom.text="Version "+getAppVersion()

        checkForUpdate()

    }

    private fun checkForUpdate() {
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
                        requestPermission()
                    }
                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                        Log.d(ConstantsApp.TAG, "Developer triggered update in progress")
                        onResume()
                    }
                    UpdateAvailability.UNKNOWN -> Log.d(ConstantsApp.TAG, "Update status unknown")
                }
            }.addOnFailureListener { exception ->
                Log.d(ConstantsApp.TAG, "Error checking for updates: ${exception.message}")
                requestPermission()
            }
        } catch (e: Exception) {
            Log.d("Error", "Exception: ${e.message}")
        }
    }

    private fun requestUpdate(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.IMMEDIATE,
            this,
            MY_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                // If the update is not completed, you can handle it here
                Toast.makeText(this,"Update Incomplete",Toast.LENGTH_SHORT).show()
            } else {
                // Update completed successfully, open the main activity
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // Check if all permissions are granted
                    if (report != null && report.areAllPermissionsGranted()) {
                        Log.d(ConstantsApp.TAG,"Check if all permissions are granted")
                        loadMainActivity()
                    } else {
                        Log.d(ConstantsApp.TAG," At least one permission is denied")
                        loadMainActivity()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                }
            })
            .check()
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

    private fun loadMainActivity() {
        try {
            val loginData=sessionManager.getLoginData()
            if (loginData!!.size>0)
            {
                for (login in loginData)
                {
                    handleSuccessfulLogin(login.Designation_name)
                }
            }
            else
            {
                Handler().postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish() // Close the splash activity so that it's not accessible with the back button
                }, 3000)
            }
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Close the splash activity so that it's not accessible with the back button
            }, 3000)
        }
    }

    private fun handleSuccessfulLogin(designation: String?) {
        when (designation) {
            "Data Entry Volunteer(LLE)" -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            "PSD Volunteer(LLE)" -> {
                startActivity(Intent(this, PresciptionMainActivity::class.java))
            }

            "Indent User"-> {
                startActivity(Intent(this, PharmaMainActivity::class.java))
            }

            "Orthotist"-> {
                val intent = Intent(this, OrthosisMainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            else -> {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }

        }
        finish()
    }

    private fun getAppVersion(): String? {
        try {
            val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Unknown"
    }

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE_UPDATE
                )
            }
        }
    }
}