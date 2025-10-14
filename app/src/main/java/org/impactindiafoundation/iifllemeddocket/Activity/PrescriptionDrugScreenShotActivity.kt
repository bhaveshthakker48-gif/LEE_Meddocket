package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_FILE
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_PATH
import com.colourmoon.imagepicker.utils.ResultImage
import com.google.gson.Gson
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImagePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.RealPathUtil1
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.Utils.imageUtils.ImagePickerDialog
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPrescriptionDrugScreenshotBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrescriptionDrugScreenShotActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityPrescriptionDrugScreenshotBinding
    lateinit var sessionManager: SessionManager
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    private var selectedImageView: ImageView? = null
    var patient_id = ""
    private val GALLERY: Int = 1
    private var CAMERA: Int = 2
    var filePath: String? = null
    var UpdatedfilePathFront: String = ""
    var UpdatedfilePathBack: String = ""
    private var frontImage: String = ""
    private var backImage: String = ""
    private var clickedId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionDrugScreenshotBinding.inflate(layoutInflater)
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

        binding.ImageViewScreenshotFront.setOnClickListener {
            clickedId = it.id
            clickImage()
        }

        binding.ImageViewScreenshotBack.setOnClickListener {
            clickedId = it.id
            clickImage()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.toolbarPrescriptionScreenshot.toolbar.title = "Prescription Screenshot"

        getViewModel()
        createRoomDatabase()

        val decodedText = sessionManager.getPatientData()
        val gson = Gson()
        val patientData = gson.fromJson(decodedText, PatientData::class.java)
        patient_id = patientData.patientId.toString()

        binding.ButtonSubmit.setOnClickListener {
            if (!frontImage.isNullOrEmpty() || !backImage.isNullOrEmpty()) {
                Log.d(ConstantsApp.TAG, "UpdatedfilePathFront=>" + UpdatedfilePathFront)
                Log.d(ConstantsApp.TAG, "UpdatedfilePathBack=>" + UpdatedfilePathBack)
                SaveDataLocal(frontImage, backImage)
                Utility.successToast(this@PrescriptionDrugScreenShotActivity,"Data Submitted Successfully")
                val intent = Intent(this, PharmaMainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please set at least one image", Toast.LENGTH_SHORT).show()
            }
        }
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

    override fun onClick(v: View?) {
        selectedImageView = when (v) {
            else -> null
        }
        selectedImageView?.let {
            showPictureDialog(it)
        }
    }

    private fun showPictureDialog(imageView: ImageView) {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera"
        )
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallery(imageView)
                1 -> takePhotoFromCamera(imageView)
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery(imageView: ImageView) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY)
    }

    private fun takePhotoFromCamera(imageView: ImageView) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                GALLERY -> {
                    val imageUri = data?.data
                    selectedImageView?.let {
                        processAndSetImage(imageUri, null, it)
                    }
                }
                CAMERA -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    selectedImageView?.let {
                        processAndSetImage(null, imageBitmap, it)
                    }
                }
            }
        }
    }

    private fun processAndSetImage(imageUri: Uri?, imageBitmap: Bitmap?, imageView: ImageView) {
        if (imageUri != null) {
            Glide.with(imageView.context)
                .load(imageUri)
                .into(imageView)
            saveLocalByImageUri(imageUri, imageView)
            imageView.setPadding(0, 0, 0, 0)
        } else if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap)
            saveLocalByImageBitmap(imageBitmap, imageView)
            imageView.setPadding(0, 0, 0, 0)
        }
    }

    private fun saveLocalByImageBitmap(imageBitmap: Bitmap, imageView: ImageView) {
        when (imageView) {
            binding.ImageViewScreenshotFront -> {
                filePath = ConstantsApp.saveBitmapToFile(imageBitmap, this)
                Log.d(ConstantsApp.TAG, "filepath on camera click=>" + filePath)
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "prescription_front_page_image_$timestamp.jpg"
                val tempFile = ConstantsApp.saveBitmapToFile1(imageBitmap, fileName, this)
                val imageUri = FileProvider.getUriForFile(
                    this,
                    "org.impactindiafoundation.iifllemeddocket.fileprovider",  // Replace with your app's package name
                    tempFile
                )
                val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!, fileName)
                UpdatedfilePathFront = updatedPath!!
                Log.d(ConstantsApp.TAG, "UpdatedfilePathFront=>" + UpdatedfilePathFront)
            }

            binding.ImageViewScreenshotBack -> {
                filePath = ConstantsApp.saveBitmapToFile(imageBitmap, this)
                Log.d(ConstantsApp.TAG, "filepath on camera click=>" + filePath)
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "prescription_back_page_image_$timestamp.jpg"
                val tempFile = ConstantsApp.saveBitmapToFile1(imageBitmap, fileName, this)
                val imageUri = FileProvider.getUriForFile(
                    this,
                    "org.impactindiafoundation.iifllemeddocket.fileprovider",  // Replace with your app's package name
                    tempFile
                )
                val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!, fileName)
                UpdatedfilePathBack = updatedPath!!
                Log.d(ConstantsApp.TAG, "UpdatedfilePathBack=>" + UpdatedfilePathBack)
            }
        }
    }

    private fun saveLocalByImageUri(imageUri: Uri, imageView: ImageView) {
        when (imageView) {
            binding.ImageViewScreenshotFront -> {
                filePath = RealPathUtil1.getRealPath(this, imageUri!!)
                Log.d(ConstantsApp.TAG, "filePath=>" + filePath)
                Log.d(ConstantsApp.TAG, "imageUri=>" + imageUri)
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "prescription_front_page_image_$timestamp.jpg"
                val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!, fileName)
                UpdatedfilePathFront = updatedPath!!
                Log.d(ConstantsApp.TAG, "UpdatedfilePathFront=>" + UpdatedfilePathFront)
            }

            binding.ImageViewScreenshotBack -> {
                binding.ImageViewScreenshotBack.setPadding(0)
                filePath = RealPathUtil1.getRealPath(this, imageUri!!)
                Log.d(ConstantsApp.TAG, "filePath=>" + filePath)
                Log.d(ConstantsApp.TAG, "imageUri=>" + imageUri)
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "prescription_back_page_image_$timestamp.jpg"
                val updatedPath = ConstantsApp.moveImageToLLEFolder(this, imageUri!!, fileName)
                UpdatedfilePathBack = updatedPath!!
                Log.d(ConstantsApp.TAG, "UpdatedfilePathBack=>" + UpdatedfilePathBack)
            }
        }
    }

    private fun SaveDataLocal(updatedfilePath: String, updatedfilePath1: String) {
        if (updatedfilePath.isNotEmpty() && updatedfilePath1.isNotEmpty()) {
            val (camp_id, user_id) = sessionManager.getCampUserID()
            val imagePrescription = ImagePrescriptionModel(
                0,
                updatedfilePath,
                patient_id.toInt(),
                camp_id!!.toInt(),
                user_id!!.toInt()
            )
            viewModel1.insertImagePrescriptionModel(imagePrescription)

            val imagePrescription1 = ImagePrescriptionModel(
                0,
                updatedfilePath1,
                patient_id.toInt(),
                camp_id!!.toInt(),
                user_id!!.toInt()
            )
            viewModel1.insertImagePrescriptionModel(imagePrescription1)
        } else {
            val (camp_id, user_id) = sessionManager.getCampUserID()

            if (updatedfilePath.isNotEmpty()) {
                val imagePrescription = ImagePrescriptionModel(
                    0,
                    updatedfilePath,
                    patient_id.toInt(),
                    camp_id!!.toInt(),
                    user_id!!.toInt()
                )
                viewModel1.insertImagePrescriptionModel(imagePrescription)
            } else {
                val imagePrescription1 = ImagePrescriptionModel(
                    0,
                    updatedfilePath1,
                    patient_id.toInt(),
                    camp_id!!.toInt(),
                    user_id!!.toInt()
                )
                viewModel1.insertImagePrescriptionModel(imagePrescription1)
            }
        }
    }

    val imageLauncher = object : ResultImage {
        override val result: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imagePath = result.data?.getStringExtra(RESULT_IMAGE_PATH)
                var imageFile: File? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getSerializableExtra(
                        RESULT_IMAGE_FILE, File::class.java
                    )
                } else {
                    result.data?.getSerializableExtra(RESULT_IMAGE_FILE) as File
                }

                when (clickedId) {
                    binding.ImageViewScreenshotFront.id -> {
                        Glide.with(applicationContext)
                            .load(imagePath!!).placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_placeholder)
                            .into(binding.ImageViewScreenshotFront)
                        frontImage = imagePath!!
                    }

                    binding.ImageViewScreenshotBack.id -> {
                        Glide.with(applicationContext)
                            .load(imagePath!!).placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_placeholder)
                            .into(binding.ImageViewScreenshotBack)
                        backImage = imagePath!!
                    }
                }
            }
        }
    }

    private fun clickImage() {
        val dialog = ImagePickerDialog(
            this@PrescriptionDrugScreenShotActivity,
            imageLauncher,
            "Upload Image",
            true
        )
        dialog.show(supportFragmentManager, "ImagePickerDialog")
    }
}
