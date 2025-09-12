package org.impactindiafoundation.iifllemeddocket.Activity.entui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Activity.PatientForms
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.RealPathUtil1
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntAudiometryViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityEntAudiometryBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EntAudiometryActivity : BaseActivity() {

    private lateinit var binding: ActivityEntAudiometryBinding
    private val GALLERY: Int = 1
    private var CAMERA: Int = 2
    var filePath: String? = null
    var UpdatedfilePath: String = ""
    private lateinit var sessionManager: SessionManager

    private val viewModel: EntAudiometryViewModel by viewModels()
    private val patientReportVM: EntPatientReportViewModel by viewModels()

    //Edit Form
    private var intentFormId = 0
    private var localFormId = 0
    private var patientReportFormId = 0
    private var canEdit = true
    private var isFormLocal = false
    private var intentDecodeText =""
    var patientFname = ""
    var patientLname = ""
    var patientAge = 0
    var patientID = 0
    var campID = 0
    var patientGender = ""
    var camp = ""
    var ageUnit=""
    private var campId = 0
    private var isDataInserted = false
    private var isImageInserted = false
    private var insertedFormId = 0

    lateinit var customDropDownAdapter: CustomDropDownAdapter
    private val hearingAidList = listOf("Select", "Behind-the-Ear (BTE)", "In-the-Ear (ITE)", " In-the-Canal (ITC)", "Completely-in-Canal (CIC)", "Receiver-in-Canal (RIC)")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntAudiometryBinding.inflate(LayoutInflater.from(this))
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

        binding.toolbarEntAudiometryNote.toolbar.title = "ENT Audiometry"
        sessionManager = SessionManager(this)
        initUi()
        initObserver()
        setPatientData()
    }

    private fun initUi(){
        intentFormId = intent.getIntExtra("localFormId",0)
        campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId",0)
        viewModel.getAudiometryImageById(intentFormId)

        customDropDownAdapter = CustomDropDownAdapter(this, hearingAidList)
        binding.spinnerHearingAid.adapter = customDropDownAdapter

        binding.CardViewUploadImage.setOnClickListener {
            showPictureDialog()
        }

        binding.submitButton.setOnClickListener {
            if (UpdatedfilePath.isEmpty()) {
                showToast("Please upload an image")
                return@setOnClickListener
            }

            if (binding.givenHearingAid.isChecked) {
                val selectedItem = binding.spinnerHearingAid.selectedItem?.toString()?.trim()
                if (selectedItem.isNullOrEmpty() || selectedItem == "Select") {
                    showToast("Please select a hearing aid type")
                    return@setOnClickListener
                }
            }
            saveAudiometryImageLocally()
            saveAudiometryDataLocally()
        }

        binding.btnEdit.setOnClickListener {
            if (!canEdit){
                onFormEditClick()
                allowClickableEditText(true)
            }
        }

        binding.btnDeleteImage.setOnClickListener {
            if (UpdatedfilePath.isNotEmpty()) {
                val file = File(UpdatedfilePath)
                if (file.exists()) {
                    file.delete()
                    Log.d("pawan", "Image deleted: $UpdatedfilePath")
                }
                UpdatedfilePath = ""
            }

            binding.ImageViewEyePreOpNotes.setImageDrawable(null)
            binding.ImageViewEyePreOpNotes.visibility = View.GONE
            binding.btnDeleteImage.visibility = View.GONE
            binding.tvUploadText.text = "Upload Image"
        }

        handleVisibility()
    }

    private fun handleVisibility() {
        binding.givenHearingAid.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.spinnerHearingAid.visibility = View.VISIBLE
            } else {
                binding.spinnerHearingAid.visibility = View.GONE
                binding.spinnerHearingAid.setSelection(0)
            }
        }
    }


    private fun initObserver() {
        viewModel.insertionStatus.observe(this) {
            Log.d("pawan", "Audiometry data insertion status: ${it.status}")
            when (it.status) {

                Status.LOADING -> {
                    // Show progress if needed
                }
                Status.SUCCESS -> {
                    isDataInserted = true
                    insertedFormId = it.data?.toInt() ?: 0
                    checkAndInsertPatientReport()
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntAudiometryActivity, "Error inserting audiometry data")
                }
            }
        }

        viewModel.insertionImageStatus.observe(this) {
            Log.d("pawan", "Audiometry image insertion status: ${it.status}")
            when (it.status) {
                Status.LOADING -> {
                    // Show progress if needed
                }
                Status.SUCCESS -> {
                    isImageInserted = true
                    checkAndInsertPatientReport()
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntAudiometryActivity, "Error inserting audiometry image")
                }
            }
        }


        patientReportVM.insertPatientReportResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    try {
                        Log.d("pawan", "PatientReport insert SUCCESS")
                        Utility.successToast(
                            this@EntAudiometryActivity,
                            "Form Submitted Successfully"
                        )
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntAudiometryActivity, "Unexpected error")
                }
            }
        }

        viewModel.audiometryListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()){
                            val preOpDetails = it.data[0]
                            localFormId = preOpDetails.uniqueId
                            isFormLocal = true
                            canEdit = false

                            if (preOpDetails.app_id == "1") {
                                binding.btnEdit.visibility = View.GONE
                                binding.submitButton.visibility = View.GONE
                            } else {
                                binding.btnEdit.visibility = View.VISIBLE
                                binding.submitButton.visibility = View.GONE
                            }

                            allowClickableEditText(false)
                            setUpFormData(preOpDetails)

                        }
                        else{
                            allowClickableEditText(true)
                            localFormId = 0
                            isFormLocal = false
                            canEdit = true
                            binding.btnEdit.visibility = View.GONE
                            binding.submitButton.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntAudiometryActivity, "Unexpected error")

                }
            }
        }

        viewModel.audiometryImageListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()){
                            val preOpDetails = it.data[0]
                            localFormId = preOpDetails.id
                            isFormLocal = true
                            canEdit = false

                            if (preOpDetails.app_id == "1") {
                                binding.btnEdit.visibility = View.GONE
                                binding.submitButton.visibility = View.GONE
                            } else {
                                binding.btnEdit.visibility = View.VISIBLE
                                binding.submitButton.visibility = View.GONE
                            }

                            allowClickableEditText(false)
                            setUpImageFormData(preOpDetails)

                        }
                        else{
                            allowClickableEditText(true)
                            localFormId = 0
                            isFormLocal = false
                            canEdit = true
                            binding.btnEdit.visibility = View.GONE
                            binding.submitButton.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntAudiometryActivity, "Unexpected error")

                }
            }
        }
    }

    private fun checkAndInsertPatientReport() {
        Log.d("pawan", "checkAndInsertPatientReport() called")
        if (isDataInserted && isImageInserted) {
            try {
                val patientReport = EntPatientReport(
                    id = patientReportFormId,
                    formType = Constants.ENT_AUDIOMETRY,
                    formId = insertedFormId,
                    patientFname = patientFname,
                    patientLname = patientLname,
                    patientId = patientID,
                    patientGen = patientGender,
                    patientAge = patientAge,
                    camp_id = campID,
                    location = camp,
                    AgeUnit = ageUnit,
                    RegNo = ""
                )

                Log.d("pawan", "insertPatientReport() called with: $patientReport")
                patientReportVM.insertPatientReport(patientReport)

                // Reset flags so it doesn't insert again
                isDataInserted = false
                isImageInserted = false
                insertedFormId = 0

            } catch (e: Exception) {
                Log.e("FormSaveError", e.message ?: "Unknown error")
            }
        }
    }

    private fun setUpFormData(formData: AudiometryEntity) {
        binding.conductiveLeft.isChecked = formData.conductiveLeft
        binding.conductiveRight.isChecked = formData.conductiveRight
        binding.conductiveBilateral.isChecked = formData.conductiveBilateral

        binding.sensorineuralLeft.isChecked = formData.sensorineuralLeft
        binding.sensorineuralRight.isChecked = formData.sensorineuralRight
        binding.sensorineuralBilateral.isChecked = formData.sensorineuralBilateral

        binding.givenHearingAid.isChecked = formData.hearingAidGiven

        if (formData.hearingAidGiven) {
            binding.spinnerHearingAid.visibility = View.VISIBLE
            val index = hearingAidList.indexOfFirst {
                it.equals(formData.hearingAidType, ignoreCase = true)
            }
            if (index >= 0) {
                binding.spinnerHearingAid.setSelection(index)
            }
        } else {
            binding.spinnerHearingAid.visibility = View.GONE
        }
    }

    private fun setUpImageFormData(formData: AudiometryImageEntity) {
        val file = File(formData.filename)
        if (file.exists()) {
            UpdatedfilePath = formData.filename
            val resizedBitmap = decodeSampledBitmapFromFile(file, 1024, 1024)
            if (resizedBitmap != null) {
                binding.ImageViewEyePreOpNotes.setImageBitmap(resizedBitmap)
                binding.ImageViewEyePreOpNotes.visibility = View.VISIBLE
                binding.btnDeleteImage.visibility = View.VISIBLE
                binding.tvUploadText.text = "Change Image"
            } else {
                Log.e(ConstantsApp.TAG, "Failed to decode bitmap from file")
            }
        } else {
            Log.e(ConstantsApp.TAG, "Image file not found: ${formData.filename}")
        }
    }



    private fun allowClickableEditText(canEdit: Boolean) {
        binding.conductiveLeft.isEnabled = canEdit
        binding.conductiveRight.isEnabled = canEdit
        binding.conductiveBilateral.isEnabled = canEdit

        binding.sensorineuralLeft.isEnabled = canEdit
        binding.sensorineuralRight.isEnabled = canEdit
        binding.sensorineuralBilateral.isEnabled = canEdit

        binding.givenHearingAid.isEnabled = canEdit
        binding.spinnerHearingAid.isEnabled = canEdit

        binding.CardViewUploadImage.isEnabled = canEdit
        binding.btnDeleteImage.isEnabled = canEdit

    }


    private fun onFormEditClick() {
        canEdit = true
        binding.submitButton.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE
        binding.tvSubmit.text = "Update"
    }

    private fun setPatientData() {
        intentDecodeText = sessionManager.getPatientData().toString()
        val gson = Gson()

        if (patientReportFormId != 0) {
            patientFname = intent.getStringExtra("patientFname") ?: ""
            patientLname = intent.getStringExtra("patientLname") ?: ""
            patientID = intent.getIntExtra("patientId", 0)
            patientAge = intent.getIntExtra("patientAge", 0)
            patientGender = intent.getStringExtra("patientGen") ?: ""
            campID = intent.getIntExtra("campId", 0)
            camp = intent.getStringExtra("location") ?: ""
            ageUnit = intent.getStringExtra("AgeUnit") ?: ""
        } else {
            val patientData = gson.fromJson(intentDecodeText, EntPatientData::class.java)
            patientFname = patientData.patientFname
            patientLname = patientData.patientLname
            patientID = patientData.patientId
            patientAge = patientData.patientAge
            patientGender = patientData.patientGen
            campID = patientData.camp_id
            camp = patientData.location
            ageUnit = patientData.AgeUnit
        }

        viewModel.getAudiometryById(intentFormId, patientID)

    }


    private fun showPictureDialog() {
        val pictureDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera"
        )
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            val imageUri = data?.data ?: return

            filePath = RealPathUtil1.getRealPath(this, imageUri)
            Log.d(ConstantsApp.TAG, "filePath=>$filePath")
            Log.d(ConstantsApp.TAG, "imageUri=>$imageUri")

            val inputStream = contentResolver.openInputStream(imageUri)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            options.inSampleSize = calculateInSampleSize(options, 1024, 1024)
            options.inJustDecodeBounds = false

            val resizedInputStream = contentResolver.openInputStream(imageUri)
            val resizedBitmap = BitmapFactory.decodeStream(resizedInputStream, null, options)
            resizedInputStream?.close()

            resizedBitmap?.let {
                binding.ImageViewEyePreOpNotes.setImageBitmap(it)
                binding.ImageViewEyePreOpNotes.visibility = View.VISIBLE
                binding.btnDeleteImage.visibility = View.VISIBLE
            }

            binding.tvUploadText.text = "Change Image"

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "surgical_notes_image_$timestamp.jpg"

            val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri, fileName)
            UpdatedfilePath = updatedPath ?: ""
        }

        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap ?: return

            filePath = ConstantsApp.saveBitmapToFile(imageBitmap, this)
            Log.d(ConstantsApp.TAG, "filepath on camera click=>$filePath")

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "surgical_notes_image_$timestamp.jpg"

            val tempFile = ConstantsApp.saveBitmapToFile1(imageBitmap, fileName, this)

            val imageUri = FileProvider.getUriForFile(
                this,
                "org.impactindiafoundation.iifllemeddocket.provider",
                tempFile
            )

            val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri, fileName)

            UpdatedfilePath = updatedPath ?: ""
            val resizedBitmap = decodeSampledBitmapFromFile(File(UpdatedfilePath), 1024, 1024)
            resizedBitmap?.let {
                binding.ImageViewEyePreOpNotes.setImageBitmap(it)
                binding.ImageViewEyePreOpNotes.visibility = View.VISIBLE
                binding.btnDeleteImage.visibility = View.VISIBLE
            }
            binding.tvUploadText.text = "Change Image"
        }
    }

    private fun decodeSampledBitmapFromFile(file: File, reqWidth: Int, reqHeight: Int): Bitmap? {
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(file.absolutePath, options)

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false

            BitmapFactory.decodeFile(file.absolutePath, options)
        } catch (e: Exception) {
            Log.e(ConstantsApp.TAG, "decodeSampledBitmapFromFile failed: ${e.localizedMessage}")
            null
        }
    }


    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun saveAudiometryDataLocally() {
        val userId = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        val selectedHearingAidType = if (binding.givenHearingAid.isChecked) {
            binding.spinnerHearingAid.selectedItem?.toString()?.takeIf { it != "Select" } ?: ""
        } else {
            ""
        }

        val audiometryData = AudiometryEntity(
            uniqueId = localFormId,
            patientId = patientID,
            campId = campID,
            userId = userId?.toIntOrNull() ?: 0,
            appCreatedDate = date,
            conductiveLeft = binding.conductiveLeft.isChecked,
            conductiveRight = binding.conductiveRight.isChecked,
            conductiveBilateral = binding.conductiveBilateral.isChecked,
            sensorineuralLeft = binding.sensorineuralLeft.isChecked,
            sensorineuralRight = binding.sensorineuralRight.isChecked,
            sensorineuralBilateral = binding.sensorineuralBilateral.isChecked,
            hearingAidGiven = binding.givenHearingAid.isChecked,
            hearingAidType = selectedHearingAidType
        )

        Log.d("pawan","AudiometryEntity ${Gson().toJson(audiometryData)}")

        viewModel.insertAudiometryDetails(audiometryData)
    }


    private fun saveAudiometryImageLocally() {
        val userId = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        if (UpdatedfilePath.isEmpty()) {
            Log.e(ConstantsApp.TAG, "Image path is empty. Cannot save.")
            return
        }

        val sourceFile = File(UpdatedfilePath)
        if (!sourceFile.exists()) {
            Log.e(ConstantsApp.TAG, "File does not exist: $UpdatedfilePath")
            return
        }

        val targetDir = File(getExternalFilesDir(null), "AudiometryImages")
        if (!targetDir.exists()) targetDir.mkdirs()

        val newFileName = "audiometry_image_${System.currentTimeMillis()}.jpg"
        val targetFile = File(targetDir, newFileName)

        sourceFile.copyTo(targetFile, overwrite = true)

        Log.d("PatientData", "ID: $patientID, CampID: $campID, userId: $userId, Name: $patientFname $patientLname")

        val entity = AudiometryImageEntity(
            id = localFormId,
            patientId = patientID,
            campId = campID,
            userId = userId?.toIntOrNull() ?: 0,
            appCreatedDate = date,
            filename = targetFile.absolutePath,
        )

        Log.d("pawan", "AudiometryImageEntity: ${Gson().toJson(entity)}")

        viewModel.insertAudiometryImageDetails(entity)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
