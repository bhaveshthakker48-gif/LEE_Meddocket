package org.impactindiafoundation.iifllemeddocket.Activity.entui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Activity.PatientForms
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.RealPathUtil1
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.PreOpImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntProOpDetailsViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityEntPreOpDetailsBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EntPreOpDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityEntPreOpDetailsBinding
    private lateinit var sessionManager: SessionManager

    private val viewModel: EntProOpDetailsViewModel by viewModels()
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
    private var insertedDataFormId = 0

    private val GALLERY: Int = 1
    private var CAMERA: Int = 2
    var filePath: String? = null
    var UpdatedfilePath: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntPreOpDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbarEyeOPDDoctorsNote.toolbar.title = "ENT Pre-Op Details"
        sessionManager = SessionManager(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Choose whichever bottom inset is larger (IME or system bars)
            val bottom = maxOf(systemBarsInsets.bottom, imeInsets.bottom)

            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                bottom
            )

            insets
        }
        initUi()
        initObserver()
        setPatientData()
        handleEditTextVisibility()
        setUpTimePickers()
    }

    private fun initUi(){
        intentFormId = intent.getIntExtra("localFormId",0)
        campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId",0)
        viewModel.getPreOpImageById(intentFormId)


        binding.submitButton.setOnClickListener {
            if (!validateInvestigationInputs()){
                return@setOnClickListener
            }
            savePreOpDetailsLocally()
            saveConcentFormImageLocally()
        }

        binding.btnEdit.setOnClickListener {
            if (!canEdit){
                onFormEditClick()
                allowClickableEditText(true)
            }
        }

        binding.viewImage.setOnClickListener {
            showImageFullScreen(UpdatedfilePath)
        }

        binding.deleteImage.setOnClickListener {
            deleteImage()
        }

        binding.uploadImage.setOnClickListener {
            showPictureDialog()
        }
    }

    private fun savePreOpDetailsLocally() {
        val userId = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        Log.d("pawan", "ID: $patientID, CampID: $campID, userId: $userId, Name: $patientFname $patientLname, localFormId: $localFormId")

        val preOpDetails = EntPreOpDetailsEntity(
            uniqueId = localFormId,
            patientId = patientID,
            campId = campID,
            userId = userId?.toIntOrNull() ?: 0,
            appCreatedDate = date,
            injtt = binding.lineUpForSurgery.isChecked,
            adulttab = binding.medication.isChecked,
            childrensolidorliquid = binding.solidFood.isChecked,
            childrenbreastfed = binding.breastfedBabies.isChecked,
            childrenwaterorliquid = binding.waterOrClearLiquird.isChecked,
            adultsolidorliquid = binding.adult1.isChecked,
            adultwaterorliquid = binding.adult2.isChecked,
            currentedicine = binding.currentedicine.text.toString(),
            childrensolidorliquidTime = binding.childrensolidorliquidTime.text.toString(),
            childrenbreastfedTime = binding.childrenbreastfedTime.text.toString(),
            childrenwaterorliquidTime = binding.childrenwaterorliquidTime.text.toString(),
            adultsolidorliquidTime = binding.adultsolidorliquidTime.text.toString(),
            adultwaterorliquidTime = binding.adultwaterorliquidTime.text.toString(),
            otherInstructions = binding.otherInstructions.isChecked,
            otherInstructionsDetail = binding.otherInstructionsDetail.text.toString(),
            surgeryCancel = when (binding.surgeryRadioGroup.checkedRadioButtonId) {
                R.id.surgeryCancel -> true
                R.id.radioLineForSurgery -> false
                else -> null
            },
            surgeryCancelReason = if (binding.surgeryRadioGroup.checkedRadioButtonId == R.id.surgeryCancel)
                binding.surgeryCancelReason.text.toString()
            else
                null,
            )

        Log.d("Pawan", "EntPreOpDetailsEntity: ${Gson().toJson(preOpDetails)}")

        viewModel.insertPreOpDetails(preOpDetails)
    }

    private fun saveConcentFormImageLocally() {
        val userId = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        var finalImagePath: String? = null

        if (UpdatedfilePath.isNotEmpty()) {
            val sourceFile = File(UpdatedfilePath)
            if (sourceFile.exists()) {
                val targetDir = File(getExternalFilesDir(null), "AudiometryImages")
                if (!targetDir.exists()) targetDir.mkdirs()

                val newFileName = "audiometry_image_${System.currentTimeMillis()}.jpg"
                val targetFile = File(targetDir, newFileName)

                sourceFile.copyTo(targetFile, overwrite = true)
                finalImagePath = targetFile.absolutePath
            } else {
                Log.e("pawans", "File does not exist: $UpdatedfilePath")
            }
        } else {
            Log.w("pawans", "Image path is empty. Proceeding to save without image.")
        }

        Log.d("pawan", "ID: $patientID, CampID: $campID, userId: $userId, Name: $patientFname $patientLname")

        val entity = PreOpImageEntity(
            id = localFormId,
            patientId = patientID,
            campId = campID,
            userId = userId?.toIntOrNull() ?: 0,
            appCreatedDate = date,
            filename = finalImagePath ?: ""
        )

        Log.d("Pawan", "AudiometryImageEntity: ${Gson().toJson(entity)}")

        viewModel.saveConcentFormImageLocally(entity)
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
                    insertedDataFormId = it.data?.toInt() ?: 0
                    Log.d(
                        "FormInsert",
                        "Form inserted successfully. insertedDataFormId: $insertedDataFormId (from PreOpDetails)"
                    )
                    checkAndInsertPatientReport()
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntPreOpDetailsActivity, "Error inserting audiometry data")
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
                    if (insertedDataFormId == 0 && it.data != null) {
                        insertedDataFormId = it.data.toInt()
                        Log.d(
                            "FormInsert",
                            "Image inserted successfully. insertedDataFormId updated to: $insertedDataFormId (from Image)"
                        )
                    } else {
                        Log.d(
                            "FormInsert",
                            "Image inserted, but insertedDataFormId already set to: $insertedDataFormId"
                        )
                    }

                    checkAndInsertPatientReport()
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntPreOpDetailsActivity, "Error inserting audiometry image")
                }
            }
        }

        patientReportVM.insertPatientReportResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    try {
                        Log.d("FormInsert", "PatientReport inserted successfully for formId: $insertedDataFormId")

                        Utility.successToast(
                            this@EntPreOpDetailsActivity,
                            "Form Submitted Successfully"
                        )
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntPreOpDetailsActivity, "Unexpected error")
                }
            }
        }

        viewModel.preOPDetailsListById.observe(this) {
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

                            Log.d("App_id","${preOpDetails.app_id}")

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
                    Utility.errorToast(this@EntPreOpDetailsActivity, "Unexpected error")

                }
            }
        }

        viewModel.preOpImageListById.observe(this) {
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
                    Utility.errorToast(this@EntPreOpDetailsActivity, "Unexpected error")

                }
            }
        }


    }

    private fun checkAndInsertPatientReport() {
        Log.d("pawan", "checkAndInsertPatientReport() called")
        if (isDataInserted && isImageInserted) {
            val currentFormId = insertedDataFormId
            Log.d(
                "FormInsert",
                "checkAndInsertPatientReport() called with insertedDataFormId = $currentFormId, isDataInserted = $isDataInserted, isImageInserted = $isImageInserted"
            )

            try {
                val patientReport = EntPatientReport(
                    id = patientReportFormId,
                    formType = Constants.ENT_PRE_OP_DETAILS,
                    formId = currentFormId,
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

                Log.d("FormInsert", "Inserting EntPatientReport with formId = ${patientReport.formId} for patientId = ${patientReport.patientId}")
                patientReportVM.insertPatientReport(patientReport)

                // Reset flags
                isDataInserted = false
                isImageInserted = false
                insertedDataFormId = 0

            } catch (e: Exception) {
                Log.e("FormSaveError", e.message ?: "Unknown error")
            }
        }
    }



    private fun setUpImageFormData(formData: PreOpImageEntity) {
        val file = File(formData.filename)
        if (file.exists()) {
            UpdatedfilePath = formData.filename
            val resizedBitmap = decodeSampledBitmapFromFile(file, 1024, 1024)
            if (resizedBitmap != null) {
                binding.imageLineUpForSurgery.setImageBitmap(resizedBitmap)
                binding.imageLineUpForSurgery.visibility = View.VISIBLE
                binding.viewDelete.visibility = View.VISIBLE
                binding.tvUploadText.text = "Change Image"
            } else {
                Log.e(ConstantsApp.TAG, "Failed to decode bitmap from file")
            }
        } else {
            Log.e(ConstantsApp.TAG, "Image file not found: ${formData.filename}")
        }
    }


    private fun setUpFormData(formData: EntPreOpDetailsEntity) {
        binding.lineUpForSurgery.isChecked = formData.injtt
        binding.medication.isChecked = formData.adulttab

        binding.solidFood.isChecked = formData.childrensolidorliquid
        binding.childrensolidorliquidTime.setText(formData.childrensolidorliquidTime)

        binding.breastfedBabies.isChecked = formData.childrenbreastfed
        binding.childrenbreastfedTime.setText(formData.childrenbreastfedTime)

        binding.waterOrClearLiquird.isChecked = formData.childrenwaterorliquid
        binding.childrenwaterorliquidTime.setText(formData.childrenwaterorliquidTime)

        binding.adult1.isChecked = formData.adultsolidorliquid
        binding.adultsolidorliquidTime.setText(formData.adultsolidorliquidTime)

        binding.adult2.isChecked = formData.adultwaterorliquid
        binding.adultwaterorliquidTime.setText(formData.adultwaterorliquidTime)

        binding.checkboxXRay.isChecked = !formData.currentedicine.isNullOrEmpty()
        binding.currentedicine.setText(formData.currentedicine)

        binding.otherInstructions.isChecked = formData.otherInstructions
        binding.otherInstructionsDetail.setText(formData.otherInstructionsDetail)

        when (formData.surgeryCancel) {
            true -> {
                binding.surgeryRadioGroup.check(R.id.surgeryCancel)
                binding.surgeryCancelReason.visibility = View.VISIBLE
                binding.uploadImage.visibility = View.GONE
                binding.note.visibility = View.GONE
            }
            false -> {
                binding.surgeryRadioGroup.check(R.id.radioLineForSurgery)
                binding.surgeryCancelReason.visibility = View.GONE
                binding.uploadImage.visibility = View.VISIBLE
                binding.note.visibility = View.VISIBLE
            }
            null -> {
                binding.surgeryRadioGroup.clearCheck()
                binding.surgeryCancelReason.visibility = View.GONE
                binding.uploadImage.visibility = View.GONE
                binding.note.visibility = View.GONE
            }
        }


        binding.surgeryCancelReason.setText(formData.surgeryCancelReason)
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

        viewModel.getPreOpDetailsById(intentFormId, patientID)

    }




    private fun showImageFullScreen(imagePath: String?) {
        if (imagePath.isNullOrEmpty()) {
            Toast.makeText(this, "No image to preview", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.image_preview, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.fullscreenImageView)
        val closeButton = dialogView.findViewById<ImageButton>(R.id.btnClosePreview)

        val bitmap = decodeSampledBitmapFromFile(File(imagePath), 1000, 1000)
        imageView.setImageBitmap(bitmap)

        val dialog = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
            .setView(dialogView)
            .create()

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteImage() {
        UpdatedfilePath = ""
        filePath = null

        binding.imageLineUpForSurgery.setImageDrawable(null)
        binding.imageLineUpForSurgery.visibility = View.GONE
        binding.viewDelete.visibility = View.GONE
        binding.tvUploadText.text = "Upload Image"
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
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
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
                binding.imageLineUpForSurgery.setImageBitmap(it)
                binding.imageLineUpForSurgery.visibility = View.VISIBLE
                binding.viewDelete.visibility = View.VISIBLE
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
                binding.imageLineUpForSurgery.setImageBitmap(it)
                binding.imageLineUpForSurgery.visibility = View.VISIBLE
                binding.viewDelete.visibility = View.VISIBLE
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


    private fun allowClickableEditText(canEdit: Boolean) {
        binding.lineUpForSurgery.isEnabled = canEdit
        binding.medication.isEnabled = canEdit

        binding.solidFood.isEnabled = canEdit
        binding.childrensolidorliquidTime.isEnabled = canEdit

        binding.breastfedBabies.isEnabled = canEdit
        binding.childrenbreastfedTime.isEnabled = canEdit

        binding.waterOrClearLiquird.isEnabled = canEdit
        binding.childrenwaterorliquidTime.isEnabled = canEdit

        binding.adult1.isEnabled = canEdit
        binding.adultsolidorliquidTime.isEnabled = canEdit

        binding.adult2.isEnabled = canEdit
        binding.adultwaterorliquidTime.isEnabled = canEdit

        binding.checkboxXRay.isEnabled = canEdit
        binding.currentedicine.isEnabled = canEdit

        binding.otherInstructions.isEnabled = canEdit
        binding.otherInstructionsDetail.isEnabled = canEdit

        binding.surgeryCancel.isEnabled = canEdit
        binding.radioLineForSurgery.isEnabled = canEdit
        binding.surgeryCancelReason.isEnabled = canEdit

        binding.deleteImage.isEnabled = canEdit
        binding.viewImage.isEnabled = canEdit
        binding.uploadImage.isEnabled = canEdit
    }


    private fun onFormEditClick() {
        canEdit = true
        binding.submitButton.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE
        binding.tvSubmit.text = "Update"
    }



    private fun handleEditTextVisibility() {
        binding.checkboxXRay.setOnCheckedChangeListener { _, isChecked ->
            binding.currentedicine.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.currentedicine.text.clear()
        }

        binding.solidFood.setOnCheckedChangeListener { _, isChecked ->
            binding.childrensolidorliquidTime.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.childrensolidorliquidTime.text.clear()
        }

        binding.breastfedBabies.setOnCheckedChangeListener { _, isChecked ->
            binding.childrenbreastfedTime.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.childrenbreastfedTime.text.clear()
        }

        binding.waterOrClearLiquird.setOnCheckedChangeListener { _, isChecked ->
            binding.childrenwaterorliquidTime.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.childrenwaterorliquidTime.text.clear()
        }

        binding.adult1.setOnCheckedChangeListener { _, isChecked ->
            binding.adultsolidorliquidTime.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.adultsolidorliquidTime.text.clear()
        }

        binding.adult2.setOnCheckedChangeListener { _, isChecked ->
            binding.adultwaterorliquidTime.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.adultwaterorliquidTime.text.clear()
        }

        binding.otherInstructions.setOnCheckedChangeListener { _, isChecked ->
            binding.otherInstructionsDetail.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.otherInstructionsDetail.text.clear()
        }

        binding.surgeryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.surgeryCancel -> {
                    binding.surgeryCancelReason.visibility = View.VISIBLE
                    binding.uploadImage.visibility = View.GONE
                    binding.note.visibility = View.GONE
                    binding.imageLineUpForSurgery.visibility = View.GONE
                    binding.viewDelete.visibility = View.GONE
                    UpdatedfilePath = ""
                    binding.tvUploadText.text = "Upload Image"
                }
                R.id.radioLineForSurgery -> {
                    binding.surgeryCancelReason.visibility = View.GONE
                    binding.surgeryCancelReason.text.clear()
                    binding.uploadImage.visibility = View.VISIBLE
                    binding.note.visibility = View.VISIBLE
                    binding.tvUploadText.text = "Upload Image"
                }
            }
        }

    }

    private fun setUpTimePickers() {
        binding.childrensolidorliquidTime.setOnClickListener {
            showTimePicker(binding.childrensolidorliquidTime)
        }

        binding.childrenbreastfedTime.setOnClickListener {
            showTimePicker(binding.childrenbreastfedTime)
        }

        binding.childrenwaterorliquidTime.setOnClickListener {
            showTimePicker(binding.childrenwaterorliquidTime)
        }

        binding.adultsolidorliquidTime.setOnClickListener {
            showTimePicker(binding.adultsolidorliquidTime)
        }

        binding.adultwaterorliquidTime.setOnClickListener {
            showTimePicker(binding.adultwaterorliquidTime)
        }
    }

    private fun showTimePicker(targetEditText: EditText) {
        val dialogView = layoutInflater.inflate(R.layout.custom_time_picker_dialog, null)

        val hourPicker = dialogView.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minutePicker)

        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        hourPicker.wrapSelectorWheel = true

        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.wrapSelectorWheel = true

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Select Time")
            .setPositiveButton("OK") { _, _ ->
                val hour = hourPicker.value
                val minute = minutePicker.value
                val time = String.format("%02d:%02d", hour, minute)
                targetEditText.setText(time)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun validateInvestigationInputs(): Boolean {

        if (
            !binding.lineUpForSurgery.isChecked &&
            !binding.medication.isChecked &&
            !binding.solidFood.isChecked &&
            !binding.breastfedBabies.isChecked &&
            !binding.waterOrClearLiquird.isChecked &&
            !binding.adult1.isChecked &&
            !binding.adult2.isChecked &&
            !binding.otherInstructions.isChecked &&
            !binding.checkboxXRay.isChecked &&
            !(binding.surgeryRadioGroup.checkedRadioButtonId == R.id.surgeryCancel) &&
            !(binding.surgeryRadioGroup.checkedRadioButtonId == R.id.radioLineForSurgery)
        ) {
            Toast.makeText(this, "Please select at least one field before submitting", Toast.LENGTH_SHORT).show()
            return false
        }



        if (binding.solidFood.isChecked && binding.childrensolidorliquidTime.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select time for solid food or thick liquids (children)", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.breastfedBabies.isChecked && binding.childrenbreastfedTime.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select time for breastfed babies", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.waterOrClearLiquird.isChecked && binding.childrenwaterorliquidTime.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select time for water or clear liquids (children)", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.adult1.isChecked && binding.adultsolidorliquidTime.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select time for solid food & dairy (adults)", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.adult2.isChecked && binding.adultwaterorliquidTime.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select time for water or clear liquids (adults)", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.otherInstructions.isChecked && binding.otherInstructionsDetail.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter other instructions detail", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.checkboxXRay.isChecked && binding.currentedicine.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter current medicine", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.surgeryRadioGroup.checkedRadioButtonId == R.id.surgeryCancel &&
            binding.surgeryCancelReason.text.isNullOrEmpty()
        ) {
            Toast.makeText(this, "Please enter surgery cancel reason", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.surgeryRadioGroup.checkedRadioButtonId == R.id.radioLineForSurgery &&
            UpdatedfilePath.isNullOrEmpty()
        ) {
            Toast.makeText(this, "Please upload Image", Toast.LENGTH_SHORT).show()
            return false
        }


        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
