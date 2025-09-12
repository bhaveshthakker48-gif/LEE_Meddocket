package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.util.Util
import com.google.gson.Gson
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.impactindiafoundation.iifllemeddocket.Activity.PatientForms
import org.impactindiafoundation.iifllemeddocket.Activity.PharmaFormActivity
import org.impactindiafoundation.iifllemeddocket.Activity.PresciptionMainActivity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.IS_CAMP_COMPLETE
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.LEAST_CAMP_ID
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.SCREEN_OPD
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.SCREEN_PSD
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.SCREEN_QR
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.SCREEN_VOLUNTEER
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityQrCodeBinding
import java.util.Base64

class QrCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityQrCodeBinding
    lateinit var sessionManager: SessionManager

    private var leastCampId = 0
    private var isCampComplete = false
    private var screen = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQrCodeBinding.inflate(layoutInflater)
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

        startScanning()
        initUi()
        initObserver()

    }

    private fun initUi() {
        screen = intent.getStringExtra("screen").toString()
        leastCampId = intent.getIntExtra(LEAST_CAMP_ID, 0)
        isCampComplete = intent.getBooleanExtra(IS_CAMP_COMPLETE, false)
        sessionManager = SessionManager(this)

    }

    private fun initObserver() {

    }

    private fun startScanning() {
        binding.scannerView.setResultHandler(this)  // Set handler to receive results
        binding.scannerView.startCamera()  // Start camera preview
    }

    override fun handleResult(rawResult: Result) {
        binding.infoText.text = "QR Code Scanned!"
        try {
            println("Raw Result: ${rawResult.text}")
            var decodedBytes: ByteArray
            var decodedText = ""
            try {
                if (isNumeric(rawResult.text)) {
                    Utility.infoToast(this@QrCodeActivity, "QR is not Proper")
                    onBackPressed()
                } else {
                    decodedBytes = Base64.getDecoder().decode(rawResult.text)
                    decodedText = String(decodedBytes, Charsets.UTF_8)
                }

            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
                Utility.infoToast(this@QrCodeActivity, "QR is not Proper")
                onBackPressed()

            }
            if (!decodedText.isNullOrEmpty()) {
                Log.d(ConstantsApp.TAG, "result.contents => $decodedText")

                sessionManager.setPatientData(decodedText)
                val gson = Gson()
                val patientData = gson.fromJson(decodedText, PatientData::class.java)

                if (screen == SCREEN_PSD) {
                    val intent = Intent(
                        this@QrCodeActivity,
                        PresciptionMainActivity::class.java
                    )
                    intent.putExtra("result", decodedText)
                    startActivity(intent)
                    finish()
                }
                else if (screen == SCREEN_VOLUNTEER) {
                    val intent = Intent(
                        this@QrCodeActivity,
                        PatientForms::class.java
                    )
                    intent.putExtra("result", decodedText)
                    startActivity(intent)
                    finish()
                }
                else if (screen == SCREEN_OPD){
                    val intent = Intent(
                        this@QrCodeActivity,
                        PharmaFormActivity::class.java
                    )
                    intent.putExtra("result", decodedText)
                    startActivity(intent)
                    finish()
                }
                else {
                    if (leastCampId == 0) {

                        val intent = Intent(
                            this@QrCodeActivity, OrthosisFittingActivity::class.java
                        )
                        intent.putExtra("result", decodedText)
                        intent.putExtra("screen", SCREEN_QR)
                        intent.putExtra("temp_id", patientData.patientId.toString())

                        startActivity(intent)
                        finish()


                    } else {
                        if (patientData.camp_id == leastCampId && isCampComplete) {
                            Utility.infoToast(this@QrCodeActivity, "Camp Completed")
                            onBackPressed()
                        } else {
                            if (patientData.camp_id > leastCampId && isCampComplete) {
                                val intent = Intent(
                                    this@QrCodeActivity, OrthosisFittingActivity::class.java
                                )
                                intent.putExtra("result", decodedText)
                                intent.putExtra("temp_id", patientData.patientId.toString())
                                intent.putExtra("screen", SCREEN_QR)

                                startActivity(intent)
                                finish()
                            } else {
                                if (patientData.camp_id > leastCampId || patientData.camp_id != leastCampId) {
                                    Utility.infoToast(this@QrCodeActivity, "Complete Previous Camp")
                                    onBackPressed()
                                } else {
                                    val intent = Intent(
                                        this@QrCodeActivity, OrthosisFittingActivity::class.java
                                    )
                                    intent.putExtra("result", decodedText)
                                    intent.putExtra("temp_id", patientData.patientId.toString())
                                    intent.putExtra("screen", SCREEN_QR)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                        }
                    }
                }
            } else {
                onBackPressed()
            }


        } catch (e: IllegalArgumentException) {
            // Handle the case where the Base64 content is invalid
            Log.e(ConstantsApp.TAG, "Illegal base64 character", e)
            Toast.makeText(this, "Invalid QR code", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startScanning()
        } else {
            //Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show()
            Utility.errorToast(this, "Camera permission is required")
        }
    }

    override fun onResume() {
        super.onResume()
        binding.scannerView.startCamera()  // Restart camera on resume
    }

    override fun onPause() {
        super.onPause()
        binding.scannerView.stopCamera()  // Stop camera to save resources
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.scannerView.stopCamera()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }

    private fun isNumeric(input: String): Boolean {
        // Check if the string contains only digits
        return input.matches(Regex("^[0-9]+$"))
    }
}