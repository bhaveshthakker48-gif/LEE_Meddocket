package org.impactindiafoundation.iifllemeddocket.Activity.pathologyui

import android.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyEntity
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.pathology.PathologyViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPathologyBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import java.io.File
import java.io.FileOutputStream

class PathologyActivity : BaseActivity() {

    private lateinit var binding: ActivityPathologyBinding
    private lateinit var sessionManager: SessionManager

    private val viewModel: PathologyViewModel by viewModels()
    private val patientReportVM: EntPatientReportViewModel by viewModels()
    private val GALLERY: Int = 1
    private var CAMERA: Int = 2
    private var currentImagePath: String? = null
    private var currentReportType: String? = null
    private val imagePathMap = mutableMapOf<String, String>()

    //Edit Form
    private var intentFormId = 0
    private var localFormId = 0
    private var patientReportFormId = 0
    private var canEdit = true
    private var isFormLocal = false
    private var intentDecodeText = ""
    var patientFname = ""
    var patientLname = ""
    var patientAge = 0
    var patientID = 0
    var campID = 0
    var patientGender = ""
    var camp = ""
    var ageUnit = ""
    private var campId = 0
    private var isDataInserted = false
    private var isImageInserted = false
    private var insertedFormId = 0

    lateinit var customDropDownAdapter: CustomDropDownAdapter
    private val hivList = listOf("Select","Positive","Negative")
    private val hbsagList = listOf("Select","Positive","Negative")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathologyBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

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

        binding.toolbarPathologyNote.toolbar.title = "Pathology Reports"
        sessionManager = SessionManager(this)

        initUi()
        initObserver()
        setPatientData()
    }

    private fun initUi() {
        intentFormId = intent.getIntExtra("localFormId", 0)
        campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId", 0)

        visibilityInvestigation()

        customDropDownAdapter = CustomDropDownAdapter(this, hivList)
        binding.hivTextbox.adapter = customDropDownAdapter

        customDropDownAdapter = CustomDropDownAdapter(this, hbsagList)
        binding.hbsagTextbox.adapter = customDropDownAdapter


        binding.submitButton.setOnClickListener {
            if (!validateInvestigationInputs()) return@setOnClickListener
            savePathalogyReportLocally()

            Toast.makeText(this, "Form submitted successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.uploadCbcReport.setOnClickListener {
            currentReportType = "CBC"
            showPictureDialog()
        }
        binding.uploadBtReport.setOnClickListener {
            currentReportType = "BT"
            showPictureDialog()
        }
        binding.uploadCtReport.setOnClickListener {
            currentReportType = "CT"
            showPictureDialog()
        }
        binding.uploadhivReport.setOnClickListener {
            currentReportType = "HIV"
            showPictureDialog()
        }
        binding.uploadhbsagReport.setOnClickListener {
            currentReportType = "HBsAg"
            showPictureDialog()
        }
        binding.uploadPtaReport.setOnClickListener {
            currentReportType = "PTA"
            showPictureDialog()
        }
        binding.uploadimpedanceAudiometryReport.setOnClickListener {
            currentReportType = "Impedance Audiometry"
            showPictureDialog()
        }
        binding.deleteCbcImage.setOnClickListener {
            deleteImage("CBC")
        }
        binding.deleteBtImage.setOnClickListener {
            deleteImage("BT")
        }
        binding.deletectImage.setOnClickListener {
            deleteImage("CT")
        }
        binding.deletehivImage.setOnClickListener {
            deleteImage("HIV")
        }
        binding.deletehbsagImage.setOnClickListener {
            deleteImage("HBsAg")
        }
        binding.deleteptaImage.setOnClickListener {
            deleteImage("PTA")
        }
        binding.deleteimpedanceAudiometryImage.setOnClickListener {
            deleteImage("Impedance Audiometry")
        }
        binding.viewCbcImage.setOnClickListener {
            showImageFullScreen(
                imagePathMap["CBC"],
            )
        }
        binding.viewBtImage.setOnClickListener {
            showImageFullScreen(
                imagePathMap["BT"],
            )
        }
        binding.viewCtImage.setOnClickListener {
            showImageFullScreen(
                imagePathMap["CT"],
            )
        }
        binding.viewHivImage.setOnClickListener {
            showImageFullScreen(
                imagePathMap["HIV"],
            )
        }
        binding.viewHbsagImage.setOnClickListener {
            showImageFullScreen(
                imagePathMap["HBsAg"],
            )
        }
        binding.viewPtaImage.setOnClickListener {
            showImageFullScreen(
                imagePathMap["PTA"],
            )
        }
        binding.viewImpedanceAudiometryImage.setOnClickListener {
            showImageFullScreen(
                imagePathMap["Impedance Audiometry"],
            )
        }
        binding.btnEdit.setOnClickListener {
            if (!canEdit) {
                onFormEditClick()
                allowClickableEditText(true)
            }
        }
    }


    private fun showImageFullScreen(imagePath: String?) {
        if (imagePath.isNullOrEmpty()) {
            Toast.makeText(this, "No image to preview", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(org.impactindiafoundation.iifllemeddocket.R.layout.image_preview, null)
        val imageView = dialogView.findViewById<ImageView>(org.impactindiafoundation.iifllemeddocket.R.id.fullscreenImageView)
        val closeButton = dialogView.findViewById<ImageButton>(org.impactindiafoundation.iifllemeddocket.R.id.btnClosePreview)

        val bitmap = decodeSampledBitmapFromFile(imagePath, 1000, 1000)
        imageView.setImageBitmap(bitmap)

        val dialog = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
            .setView(dialogView)
            .create()

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initObserver() {
        viewModel.insertionStatus.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    isDataInserted = true
                    insertedFormId = it.data?.toInt() ?: 0

                    Log.d("insertedFormId", "Pathology form inserted with ID = $insertedFormId")

                    // ✅ NOW insert all images with correct formId
                    for ((reportType, imagePath) in imagePathMap) {
                        if (imagePath.isNotEmpty()) {
                            val alreadyExists = viewModel.imageAlreadyExists(patientID, reportType)
                            if (alreadyExists) {
                                Log.d("ImageDebug", "Skipped duplicate image for: $reportType")
                                viewModel.updateImageInRoom(patientID, reportType, imagePath)
                            } else {
                                Log.d("ImageDebug", "Inserting image: $reportType at $imagePath")
                                insertImageInRoom(imagePath, reportType)  // Uses correct insertedFormId
                            }
                        }
                    }

                    checkAndInsertPatientReport()
                }

                Status.ERROR -> {
                    Utility.errorToast(this@PathologyActivity, "Error inserting audiometry data")
                }
            }
        }

        viewModel.insertionImageStatus.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    // optional loading UI
                }

                Status.SUCCESS -> {
                    isImageInserted = true
                    Log.d("insertedFormId", "Image inserted. Current formId = $insertedFormId")
                    checkAndInsertPatientReport()

                    Log.d(
                        "ImageDebug",
                        "Re-fetching images after successful insert for patient ID: $patientID"
                    )
                }

                Status.ERROR -> {
                    Utility.errorToast(this@PathologyActivity, "Error inserting audiometry image")
                }
            }
        }

        patientReportVM.insertPatientReportResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                    try {
                        Utility.successToast(
                            this@PathologyActivity,
                            "Form Submitted Successfully"
                        )
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@PathologyActivity, "Unexpected error")
                }
            }
        }

        viewModel.pathologyListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()) {
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

                        } else {
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
                    Utility.errorToast(this@PathologyActivity, "Unexpected error")
                }
            }
        }


        viewModel.pathologyImageListById.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    try {
                        Log.d(
                            "ImageDebug",
                            "Fetched ${it.data?.size ?: 0} images for patient ID: $patientID"
                        )

                        if (!it.data.isNullOrEmpty()) {
                            val imageList = it.data
                            val shownImages = mutableSetOf<Pair<String, String>>()

                            for (imageEntity in it.data) {
                                val key = Pair(imageEntity.reportType, imageEntity.filename)
                                if (shownImages.contains(key)) continue

                                shownImages.add(key)

                                Log.d(
                                    "ImageDebug",
                                    "Showing image for reportType: ${imageEntity.reportType}, path: ${imageEntity.filename}, patientId: ${imageEntity.patientId}"
                                )
                                viewModel.pathologyListById.value?.data?.firstOrNull()?.let { formData ->
                                    setUpFormWithImages(formData, imageList)  // <-- now valid
                                }
                            }

                            localFormId = it.data[0].formId
                            isFormLocal = true
                            canEdit = false

                            if (it.data[0].app_id == "1") {
                                binding.btnEdit.visibility = View.GONE
                                binding.submitButton.visibility = View.GONE
                            } else {
                                binding.btnEdit.visibility = View.VISIBLE
                                binding.submitButton.visibility = View.GONE
                            }

                            allowClickableEditText(false)
                        } else {
                            Log.d("ImageDebug", "No image data found for patient ID: $patientID")
                            allowClickableEditText(true)
                            localFormId = 0
                            isFormLocal = false
                            canEdit = true
                            binding.btnEdit.visibility = View.GONE
                            binding.submitButton.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message ?: "Unknown error in image setup")
                    }
                }

                Status.ERROR -> {
                    Log.e("ImageDebug", "Error loading image data for patientId: $patientID")
                    Utility.errorToast(this@PathologyActivity, "Unexpected error loading images")
                }

                else -> {}
            }
        }
    }

    private fun checkAndInsertPatientReport() {
        if (isDataInserted && isImageInserted) {
            try {
                val patientReport = EntPatientReport(
                    id = patientReportFormId,
                    formType = Constants.PATHOLOGY_REPORTS,
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
                patientReportVM.insertPatientReport(patientReport)
                isDataInserted = false
                isImageInserted = false
                insertedFormId = 0

            } catch (e: Exception) {
                Log.e("FormSaveError", e.message ?: "Unknown error")
            }
        }
    }

    private fun insertImageInRoom(filePath: String, reportType: String) {
        val userId = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        val imageEntity = PathologyImageEntity(
            patientId = patientID,
            campId = campID,
            userId = userId?.toIntOrNull() ?: 0,
            appCreatedDate = date,
            filename = filePath,
            reportType = reportType,
            formId = insertedFormId
        )
        Log.d("insertedFormId", "Inserting image for reportType=$reportType, formId=$insertedFormId, path=$filePath")


        Log.d("ImageDebug", "Inserting image: $reportType at $filePath")
        Log.d("ImageDebug", "Attempting to insert image: $imageEntity")
        viewModel.insertPathologyImage(imageEntity)
    }

    private fun savePathalogyReportLocally() {
        val userId = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()


        val pathologyData = PathologyEntity(
            uniqueId = localFormId,
            patientId = patientID,
            campId = campID,
            userId = userId?.toIntOrNull() ?: 0,
            appCreatedDate = date,
            cbc = binding.cbc.isChecked,
            cbcValue = binding.cbcTextbox.text.toString(),
            bt = binding.bt.isChecked,
            btValue = binding.btTextbox.text.toString(),
            ct = binding.ct.isChecked,
            ctValue = binding.ctTextbox.text.toString(),
            hiv = binding.hiv.isChecked,
            hivValue = binding.hivTextbox.selectedItem.toString(),
            hbsag = binding.hbsag.isChecked,
            hbsagValue = binding.hbsagTextbox.selectedItem.toString(),
            pta = binding.pta.isChecked,
            ptaValue = binding.ptaTextbox.text.toString(),
            impedanceAudiometry = binding.impedanceAudiometry.isChecked,
            impedanceAudiometryValue = binding.impedanceAudiometryTextbox.text.toString()
        )


        Log.d("ImageDebug", "Prepared pathology data: $pathologyData")

        viewModel.insertPathologyDetails(pathologyData)
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

        Log.d("ImageDebug", "Patient ID set to $patientID in setPatientData")

        Log.d("insertedFormId", "get form in patient data with intentFormId  $intentFormId")


        if (intentFormId != 0) {
            viewModel.getPathologyByFormId(intentFormId)
            viewModel.getImagesByFormId(intentFormId)
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): File? {
        return try {
            val file = File(context.filesDir, "report_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun setUpFormWithImages(
        formData: PathologyEntity,
        imageEntities: List<PathologyImageEntity>
    ) {
        // Map reportType -> filename for quick lookup
        val imageMap = imageEntities.associateBy { it.reportType }

        // CBC
        binding.cbc.isChecked = formData.cbc
        binding.cbcTextbox.setText(formData.cbcValue ?: "")
        if (formData.cbc) {
            binding.cbcTextbox.visibility = View.VISIBLE
            binding.uploadCbcReport.visibility = View.VISIBLE
            imageMap["CBC"]?.let { showImagePreview("CBC", it.filename) }
        } else {
            binding.cbcTextbox.visibility = View.GONE
            binding.uploadCbcReport.visibility = View.GONE
            hideImagePreview("CBC")
        }

        // BT
        binding.bt.isChecked = formData.bt
        binding.btTextbox.setText(formData.btValue ?: "")
        if (formData.bt) {
            binding.btTextbox.visibility = View.VISIBLE
            binding.uploadBtReport.visibility = View.VISIBLE
            imageMap["BT"]?.let { showImagePreview("BT", it.filename) }
        } else {
            binding.btTextbox.visibility = View.GONE
            binding.uploadBtReport.visibility = View.GONE
            hideImagePreview("BT")
        }

        // CT
        binding.ct.isChecked = formData.ct
        binding.ctTextbox.setText(formData.ctValue ?: "")
        if (formData.ct) {
            binding.ctTextbox.visibility = View.VISIBLE
            binding.uploadCtReport.visibility = View.VISIBLE
            imageMap["CT"]?.let { showImagePreview("CT", it.filename) }
        } else {
            binding.ctTextbox.visibility = View.GONE
            binding.uploadCtReport.visibility = View.GONE
            hideImagePreview("CT")
        }

        // HIV
        binding.hiv.isChecked = formData.hiv
        val hivIndex = hivList.indexOf(formData.hivValue)
        if (hivIndex >= 0) binding.hivTextbox.setSelection(hivIndex)
        if (formData.hiv) {
            binding.hivTextbox.visibility = View.VISIBLE
            binding.uploadhivReport.visibility = View.VISIBLE
            imageMap["HIV"]?.let { showImagePreview("HIV", it.filename) }
        } else {
            binding.hivTextbox.visibility = View.GONE
            binding.uploadhivReport.visibility = View.GONE
            hideImagePreview("HIV")
        }

        // HBsAg
        binding.hbsag.isChecked = formData.hbsag
        val hbsagIndex = hbsagList.indexOf(formData.hbsagValue)
        if (hbsagIndex >= 0) binding.hbsagTextbox.setSelection(hbsagIndex)
        if (formData.hbsag) {
            binding.hbsagTextbox.visibility = View.VISIBLE
            binding.uploadhbsagReport.visibility = View.VISIBLE
            imageMap["HBsAg"]?.let { showImagePreview("HBsAg", it.filename) }
        } else {
            binding.hbsagTextbox.visibility = View.GONE
            binding.uploadhbsagReport.visibility = View.GONE
            hideImagePreview("HBsAg")
        }

        // PTA
        binding.pta.isChecked = formData.pta
        binding.ptaTextbox.setText(formData.ptaValue ?: "")
        if (formData.pta) {
            binding.ptaTextbox.visibility = View.VISIBLE
            binding.uploadPtaReport.visibility = View.VISIBLE
            imageMap["PTA"]?.let { showImagePreview("PTA", it.filename) }
        } else {
            binding.ptaTextbox.visibility = View.GONE
            binding.uploadPtaReport.visibility = View.GONE
            hideImagePreview("PTA")
        }

        // Impedance Audiometry
        binding.impedanceAudiometry.isChecked = formData.impedanceAudiometry
        binding.impedanceAudiometryTextbox.setText(formData.impedanceAudiometryValue ?: "")
        if (formData.impedanceAudiometry) {
            binding.impedanceAudiometryTextbox.visibility = View.VISIBLE
            binding.uploadimpedanceAudiometryReport.visibility = View.VISIBLE
            imageMap["Impedance Audiometry"]?.let { showImagePreview("Impedance Audiometry", it.filename) }
        } else {
            binding.impedanceAudiometryTextbox.visibility = View.GONE
            binding.uploadimpedanceAudiometryReport.visibility = View.GONE
            hideImagePreview("Impedance Audiometry")
        }
    }

    private fun hideImagePreview(reportType: String) {
        // Remove stored path
        imagePathMap.remove(reportType)

        // Hide the ImageView/preview that was showing this report type
        when (reportType) {
            "CBC" -> binding.uploadCbcReport.visibility = View.GONE
            "BT" -> binding.uploadBtReport.visibility = View.GONE
            "CT" -> binding.uploadCtReport.visibility = View.GONE
            "HIV" -> binding.uploadhivReport.visibility = View.GONE
            "HBsAg" -> binding.uploadhbsagReport.visibility = View.GONE
            "PTA" -> binding.uploadPtaReport.visibility = View.GONE
            "Impedance Audiometry" -> binding.uploadimpedanceAudiometryReport.visibility = View.GONE
        }
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

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        val file = saveBitmapToFile(applicationContext, it)
                        file?.let {
                            currentImagePath = it.absolutePath
                            showImagePreview(currentReportType, it.absolutePath)
                        }
                    }
                }

                GALLERY -> {
                    val selectedImageUri = data?.data
                    selectedImageUri?.let {
                        try {
                            val inputStream = contentResolver.openInputStream(it)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            inputStream?.close()

                            val file = saveBitmapToFile(applicationContext, bitmap)
                            file?.let { savedFile ->
                                currentImagePath = savedFile.absolutePath
                                showImagePreview(currentReportType, savedFile.absolutePath)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            currentReportType?.let {
                imagePathMap[it] = currentImagePath ?: ""
            }
        }
    }



    private fun showImagePreview(reportType: String?, imagePath: String) {
        val bitmap = decodeSampledBitmapFromFile(imagePath, 800, 800)

        when (reportType) {
            "CBC" -> {
                binding.imageCbcPreview.setImageBitmap(bitmap)
                binding.imageCbcPreview.visibility = View.VISIBLE
                binding.cbcViewDelete.visibility = View.VISIBLE
                binding.uploadCbcReport.visibility = View.VISIBLE
                binding.uploadCbcReportText.text = "Change Image"
            }

            "BT" -> {
                binding.imageBtPreview.setImageBitmap(bitmap)
                binding.imageBtPreview.visibility = View.VISIBLE
                binding.btViewDelete.visibility = View.VISIBLE
                binding.uploadBtReport.visibility = View.VISIBLE
                binding.uploadBtReportText.text = "Change Image"
            }

            "CT" -> {
                binding.imageCtPreview.setImageBitmap(bitmap)
                binding.imageCtPreview.visibility = View.VISIBLE
                binding.ctViewDelete.visibility = View.VISIBLE
                binding.uploadCtReport.visibility = View.VISIBLE
                binding.uploadCtReportText.text = "Change Image"
            }

            "HIV" -> {
                binding.imageHivPreview.setImageBitmap(bitmap)
                binding.imageHivPreview.visibility = View.VISIBLE
                binding.hivViewDelete.visibility = View.VISIBLE
                binding.uploadhivReport.visibility = View.VISIBLE
                binding.uploadhivReportText.text = "Change Image"
            }

            "HBsAg" -> {
                binding.imageHsagsPreview.setImageBitmap(bitmap)
                binding.imageHsagsPreview.visibility = View.VISIBLE
                binding.hbsagViewDelete.visibility = View.VISIBLE
                binding.uploadhbsagReport.visibility = View.VISIBLE
                binding.uploadhbsagReportText.text = "Change Image"
            }

            "PTA" -> {
                binding.imagePtaPreview.setImageBitmap(bitmap)
                binding.imagePtaPreview.visibility = View.VISIBLE
                binding.ptaViewDelete.visibility = View.VISIBLE
                binding.uploadPtaReport.visibility = View.VISIBLE
                binding.uploadPtaReportText.text = "Change Image"
            }

            "Impedance Audiometry" -> {
                binding.imageimpedanceAudiometryPreview.setImageBitmap(bitmap)
                binding.imageimpedanceAudiometryPreview.visibility = View.VISIBLE
                binding.impedanceAudiometryViewDelete.visibility = View.VISIBLE
                binding.uploadimpedanceAudiometryReport.visibility = View.VISIBLE
                binding.uploadImpedanceAudiometryReportText.text = "Change Image"
            }
        }
    }


    fun decodeSampledBitmapFromFile(path: String, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun setUpFormData(formData: PathologyEntity) {
        binding.cbc.isChecked = formData.cbc
        binding.cbcTextbox.setText(formData.cbcValue ?: "")
        binding.cbcTextbox.visibility = if (formData.cbc) View.VISIBLE else View.GONE
        binding.uploadCbcReport.visibility = if (formData.cbc) View.VISIBLE else View.GONE

        binding.bt.isChecked = formData.bt
        binding.btTextbox.setText(formData.btValue ?: "")
        binding.btInputGroup.visibility = if (formData.bt) View.VISIBLE else View.GONE
        binding.uploadBtReport.visibility = if (formData.bt) View.VISIBLE else View.GONE

        binding.ct.isChecked = formData.ct
        binding.ctTextbox.setText(formData.ctValue ?: "")
        binding.ctInputGroup.visibility = if (formData.ct) View.VISIBLE else View.GONE
        binding.uploadCtReport.visibility = if (formData.ct) View.VISIBLE else View.GONE

        binding.hiv.isChecked = formData.hiv
        val hivIndex = hivList.indexOf(formData.hivValue)
        if (hivIndex >= 0) binding.hivTextbox.setSelection(hivIndex)
        binding.hivTextbox.visibility = if (formData.hiv) View.VISIBLE else View.GONE
        binding.uploadhivReport.visibility = if (formData.hiv) View.VISIBLE else View.GONE

        binding.hbsag.isChecked = formData.hbsag
        val hbsagIndex = hbsagList.indexOf(formData.hbsagValue)
        if (hbsagIndex >= 0) binding.hbsagTextbox.setSelection(hbsagIndex)
        binding.hbsagTextbox.visibility = if (formData.hbsag) View.VISIBLE else View.GONE
        binding.uploadhbsagReport.visibility = if (formData.hbsag) View.VISIBLE else View.GONE

        binding.pta.isChecked = formData.pta
        binding.ptaTextbox.setText(formData.ptaValue ?: "")
        binding.ptaInputGroup.visibility = if (formData.pta) View.VISIBLE else View.GONE
        binding.uploadPtaReport.visibility = if (formData.pta) View.VISIBLE else View.GONE

        binding.impedanceAudiometry.isChecked = formData.impedanceAudiometry
        binding.impedanceAudiometryTextbox.setText(formData.impedanceAudiometryValue ?: "")
        binding.impedanceAudiometryTextbox.visibility =
            if (formData.impedanceAudiometry) View.VISIBLE else View.GONE
        binding.uploadimpedanceAudiometryReport.visibility =
            if (formData.impedanceAudiometry) View.VISIBLE else View.GONE
    }


    private fun allowClickableEditText(canEdit: Boolean) {
        binding.cbc.isEnabled = canEdit
        binding.cbcTextbox.isEnabled = canEdit
        binding.uploadCbcReport.isEnabled = canEdit
        binding.deleteCbcImage.isEnabled = canEdit
        binding.viewCbcImage.isEnabled = canEdit

        binding.bt.isEnabled = canEdit
        binding.btTextbox.isEnabled = canEdit
        binding.uploadBtReport.isEnabled = canEdit
        binding.deleteBtImage.isEnabled = canEdit
        binding.viewBtImage.isEnabled = canEdit

        binding.ct.isEnabled = canEdit
        binding.ctTextbox.isEnabled = canEdit
        binding.uploadCtReport.isEnabled = canEdit
        binding.deletectImage.isEnabled = canEdit
        binding.viewCtImage.isEnabled = canEdit

        binding.hiv.isEnabled = canEdit
        binding.hivTextbox.isEnabled = canEdit
        binding.uploadhivReport.isEnabled = canEdit
        binding.deletehivImage.isEnabled = canEdit
        binding.viewHivImage.isEnabled = canEdit

        binding.hbsag.isEnabled = canEdit
        binding.hbsagTextbox.isEnabled = canEdit
        binding.uploadhbsagReport.isEnabled = canEdit
        binding.deletehbsagImage.isEnabled = canEdit
        binding.viewHbsagImage.isEnabled = canEdit

        binding.pta.isEnabled = canEdit
        binding.ptaTextbox.isEnabled = canEdit
        binding.uploadPtaReport.isEnabled = canEdit
        binding.deleteptaImage.isEnabled = canEdit
        binding.viewPtaImage.isEnabled = canEdit

        binding.impedanceAudiometry.isEnabled = canEdit
        binding.impedanceAudiometryTextbox.isEnabled = canEdit
        binding.uploadimpedanceAudiometryReport.isEnabled = canEdit
        binding.deleteimpedanceAudiometryImage.isEnabled = canEdit
        binding.viewImpedanceAudiometryImage.isEnabled = canEdit
    }


    private fun onFormEditClick() {
        canEdit = true
        binding.submitButton.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE
        binding.tvSubmit.text = "Update"
    }

    private fun deleteImage(reportType: String) {
        imagePathMap.remove(reportType)
        when (reportType) {
            "CBC" -> {
                binding.imageCbcPreview.setImageDrawable(null)
                binding.imageCbcPreview.visibility = View.GONE
                binding.cbcViewDelete.visibility = View.GONE
                binding.uploadCbcReportText.text = "Upload Image"
            }
            "BT" -> {
                binding.imageBtPreview.setImageDrawable(null)
                binding.imageBtPreview.visibility = View.GONE
                binding.btViewDelete.visibility = View.GONE
                binding.uploadBtReportText.text = "Upload Image"
            }
            "CT" -> {
                binding.imageCtPreview.setImageDrawable(null)
                binding.imageCtPreview.visibility = View.GONE
                binding.ctViewDelete.visibility = View.GONE
                binding.uploadCtReportText.text = "Upload Image"
            }
            "HIV" -> {
                binding.imageHivPreview.setImageDrawable(null)
                binding.imageHivPreview.visibility = View.GONE
                binding.hivViewDelete.visibility = View.GONE
                binding.uploadhivReportText.text = "Upload Image"
            }
            "HBsAg" -> {
                binding.imageHsagsPreview.setImageDrawable(null)
                binding.imageHsagsPreview.visibility = View.GONE
                binding.hbsagViewDelete.visibility = View.GONE
                binding.uploadhbsagReportText.text = "Upload Image"
            }
            "PTA" -> {
                binding.imagePtaPreview.setImageDrawable(null)
                binding.imagePtaPreview.visibility = View.GONE
                binding.ptaViewDelete.visibility = View.GONE
                binding.uploadPtaReportText.text = "Upload Image"
            }
            "Impedance Audiometry" -> {
                binding.imageimpedanceAudiometryPreview.setImageDrawable(null)
                binding.imageimpedanceAudiometryPreview.visibility = View.GONE
                binding.impedanceAudiometryViewDelete.visibility = View.GONE
                binding.uploadImpedanceAudiometryReportText.text = "Upload Image"
            }
        }
    }


    private fun visibilityInvestigation() {
        binding.cbc.setOnCheckedChangeListener { _, isChecked ->
            binding.cbcTextbox.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.uploadCbcReport.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                binding.cbcTextbox.setText("")
                binding.imageCbcPreview.visibility = View.GONE
                imagePathMap.remove("CBC")
                binding.cbcViewDelete.visibility = View.GONE
                binding.uploadCbcReportText.text = "Upload Image"
            }
        }

        binding.ct.setOnCheckedChangeListener { _, isChecked ->
            binding.ctInputGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.uploadCtReport.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                binding.ctTextbox.setText("")
                binding.imageCtPreview.visibility = View.GONE
                imagePathMap.remove("CT")
                binding.ctViewDelete.visibility = View.GONE
                binding.uploadCtReportText.text = "Upload Image"
            }
        }

        binding.bt.setOnCheckedChangeListener { _, isChecked ->
            binding.btInputGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.uploadBtReport.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                binding.btTextbox.setText("")
                binding.imageBtPreview.visibility = View.GONE
                imagePathMap.remove("BT")
                binding.btViewDelete.visibility = View.GONE
                binding.uploadBtReportText.text = "Upload Image"
            }
        }

        binding.hiv.setOnCheckedChangeListener { _, isChecked ->
            binding.hivTextbox.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.uploadhivReport.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                binding.hivTextbox.setSelection(0)
                binding.imageHivPreview.visibility = View.GONE
                imagePathMap.remove("HIV")
                binding.hivViewDelete.visibility = View.GONE
                binding.uploadhivReportText.text = "Upload Image"
            }
        }

        binding.hbsag.setOnCheckedChangeListener { _, isChecked ->
            binding.hbsagTextbox.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.uploadhbsagReport.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                binding.hbsagTextbox.setSelection(0)
                binding.imageHsagsPreview.visibility = View.GONE
                imagePathMap.remove("HBsAg")
                binding.hbsagViewDelete.visibility = View.GONE
                binding.uploadhbsagReportText.text = "Upload Image"
            }
        }

        binding.pta.setOnCheckedChangeListener { _, isChecked ->
            binding.ptaInputGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.uploadPtaReport.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                binding.ptaTextbox.setText("")
                binding.imagePtaPreview.visibility = View.GONE
                imagePathMap.remove("PTA")
                binding.ptaViewDelete.visibility = View.GONE
                binding.uploadPtaReportText.text = "Upload Image"
            }
        }

        binding.impedanceAudiometry.setOnCheckedChangeListener { _, isChecked ->
            binding.impedanceAudiometryTextbox.visibility =
                if (isChecked) View.VISIBLE else View.GONE
            binding.uploadimpedanceAudiometryReport.visibility =
                if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                binding.impedanceAudiometryTextbox.setText("")
                binding.imageimpedanceAudiometryPreview.visibility = View.GONE
                imagePathMap.remove("Impedance Audiometry")
                binding.impedanceAudiometryViewDelete.visibility = View.GONE
                binding.uploadImpedanceAudiometryReportText.text = "Upload Image"
            }
        }
    }

    private fun validateInvestigationInputs(): Boolean {
        val isAnySelected = binding.cbc.isChecked ||
                binding.bt.isChecked ||
                binding.ct.isChecked ||
                binding.hiv.isChecked ||
                binding.hbsag.isChecked ||
                binding.pta.isChecked ||
                binding.impedanceAudiometry.isChecked

        if (!isAnySelected) {
            showToast("Please select at least one field before submitting")
            return false
        }

        fun check(report: Boolean, value: String?, imageKey: String, label: String): Boolean {
            if (report) {
                if (value.isNullOrBlank()) {
                    showToast("Please enter $label value")
                    return false
                }
                if (imagePathMap[imageKey].isNullOrBlank()) {
                    showToast("Please upload $label image")
                    return false
                }
                // 🛑 Validation for "Select" in dropdowns
                if ((label == "HIV" || label == "HBsAg") && value == "Select") {
                    showToast("Please select a valid $label result")
                    return false
                }
            }
            return true
        }

        return check(binding.cbc.isChecked, binding.cbcTextbox.text.toString(), "CBC", "CBC") &&
                check(binding.bt.isChecked, binding.btTextbox.text.toString(), "BT", "BT") &&
                check(binding.ct.isChecked, binding.ctTextbox.text.toString(), "CT", "CT") &&
                check(binding.hiv.isChecked, binding.hivTextbox.selectedItem.toString(), "HIV", "HIV") &&
                check(binding.hbsag.isChecked, binding.hbsagTextbox.selectedItem.toString(), "HBsAg", "HBsAg") &&
                check(binding.pta.isChecked, binding.ptaTextbox.text.toString(), "PTA", "PTA") &&
                check(binding.impedanceAudiometry.isChecked, binding.impedanceAudiometryTextbox.text.toString(), "Impedance Audiometry", "Impedance Audiometry")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }



}