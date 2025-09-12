package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.media.ThumbnailUtils
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_FILE
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_PATH
import com.colourmoon.imagepicker.utils.ResultImage
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.zxing.qrcode.encoder.QRCode
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.SearchAbleList
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SingleSelectBottomSheetDialogFragment
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.Utils.imageUtils.ImagePickerDialog
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.SCREEN_QR
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.Equipment
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideos
import org.impactindiafoundation.iifllemeddocket.architecture.model.Measurement
import org.impactindiafoundation.iifllemeddocket.architecture.model.MeasurementPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OrthosisFittingViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityOrthosisFittingBinding
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.ImageAdapter
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.OrthosisFittingFormAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrthosisFittingActivity : BaseActivity() {

    private lateinit var binding: ActivityOrthosisFittingBinding
    private val orthosisFittingVM: OrthosisFittingViewModel by viewModels()
    private val orthosisFittingFormList = ArrayList<OrthosisPatientData>()
    private lateinit var orthosisFittingAdapter: OrthosisFittingFormAdapter
    lateinit var sessionManager: SessionManager

    private var fittingList = ArrayList<OrthosisPatientData>()
    private var isStatusGiven = false
    private var screen = ""

    private var imageList = ArrayList<FormImages>()
    private var videoList = ArrayList<FormVideos>()

    private lateinit var imageAdapter: ImageAdapter
    private var campPatientData: CampPatientDataItem? = null
    private var formPatientData: OrthosisPatientForm? = null
    private var isEdited = false
    private var formId = 0
    private var tempPatientId = 0
    private var isFormImageLocal = false
    private var isFormVideoLocal = false
    private var campId = 0
    private var canEdit = true
    private var diagnosisList = ArrayList<DiagnosisType>()
    private var isSelectedOtherDiagnosis = false

    private var selectedDiagnosis: DiagnosisType? = null
    private var selectedEquipment: Equipment? = null
    private var tempId = 0

    companion object {
        const val REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrthosisFittingBinding.inflate(layoutInflater)
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

        sessionManager = SessionManager(this)


        initUi()
        setUpImagesRecyclerView()
        setUpFittingFormRecyclerView(true)
        orthosisFittingVM.getDiagnosisMasterLocal()
        initObserver()
        //for commit once again


    }

    val formImageLauncher = object : ResultImage {

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
                if (screen != SCREEN_QR) {
                    if (imageList.size == 4) {
                        val formImageIds = ArrayList<Int>()
                        for (i in imageList) {
                            formImageIds.add(i.id)
                        }
                        orthosisFittingVM.deleteFormImagesById(formImageIds)
                        imageList.clear()
                    }
                    else{
                        Utility.warningToast(this@OrthosisFittingActivity, "Only 4 images are allowed!")
                    }

                }
                val formImage = FormImages(
                    formId = 0,
                    images = imagePath!!,
                    temp_patient_id = tempPatientId.toString(),
                    camp_id = campId.toString()
                )
                isFormImageLocal = false
                imageList.add(formImage)
                imageAdapter.notifyDataSetChanged()

            }
        }
    }

    private fun initUi() {
        screen = intent.getStringExtra("screen") ?: ""
        isEdited = intent.getBooleanExtra("is_edited", false)
        formId = intent.getIntExtra("form_id", 0)
        canEdit = intent.getBooleanExtra("edit", true)

        if (!intent.getStringExtra("temp_id").isNullOrEmpty()) {
            tempId = intent.getStringExtra("temp_id").toString().toInt()
        } else {
            tempId = 0
        }
        if (screen == "Camp_List" || screen == SCREEN_QR) {
            if (isEdited) {
                if (screen == SCREEN_QR){
                    //else call from camp patient detail
                    orthosisFittingVM.getCampPatientListById(tempId?.toInt() ?: 0)
                }
                else{
                    //call from local patient form
                    orthosisFittingVM.getOrthosisPatientFormById(formId)
                }

            } else {
                //else call from camp patient detail
                orthosisFittingVM.getCampPatientListById(tempId?.toInt() ?: 0)
            }
        } else {
            if (screen == "PatientID") {
                if (tempId != 0) {
                    orthosisFittingVM.getCampPatientListById(tempId?.toInt() ?: 0)
                }
            } else{
                getPatientData()
            }

        }
        binding.orthosisToolBar.toolbar.title = "Fitting"

        binding.tvImages.setOnClickListener {
            if (canEdit) {
                if (imageList.size >= 4) {
                    if (screen != SCREEN_QR) {
                        val dialog = ImagePickerDialog(
                            this@OrthosisFittingActivity,
                            formImageLauncher,
                            "Upload Orthosis Image"
                        )
                        dialog.show(supportFragmentManager, "ImagePickerDialog")
                    } else {
                        Utility.warningToast(this, "Only 4 images are allowed!")

                    }
                } else {
                    if (screen != SCREEN_QR) {
                        val dialog = ImagePickerDialog(
                            this@OrthosisFittingActivity,
                            formImageLauncher,
                            "Upload Orthosis Image"
                        )
                        dialog.show(supportFragmentManager, "ImagePickerDialog")
                    } else {
                        val dialog = ImagePickerDialog(
                            this@OrthosisFittingActivity,
                            formImageLauncher,
                            "Upload Orthosis Image"
                        )
                        dialog.show(supportFragmentManager, "ImagePickerDialog")
                    }

                }
            } else {
                Utility.warningToast(this@OrthosisFittingActivity, "Cannot Edit Data")
            }
        }

        binding.tvVideos.setOnClickListener {
            //  dispatchTakeVideoIntent()
            val intent = Intent(this@OrthosisFittingActivity, VideoRecordActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.btnSavePatientOrthosis.setOnClickListener {
            if (validateForm()) {
                updateOrthosis()
            }
        }

        binding.etDiagnosisType.setOnClickListener {

            inflateDiagnosisBottomSheet()
        }

        val diagnosisTypeHint = this@OrthosisFittingActivity.getString(R.string.txt_diagnosis_type)
        setAsteriskColor(binding.etlDiagnosisType, diagnosisTypeHint)

        val enterDiagnosisHint =
            this@OrthosisFittingActivity.getString(R.string.txt_enter_diagnosis_type)
        setAsteriskColor(binding.etlOtherDiagnosis, enterDiagnosisHint)

        val prescribedStatusHint =
            this@OrthosisFittingActivity.getString(R.string.txt_prescribed_status)
        setAsteriskColor(binding.etlPrescribedStatus, prescribedStatusHint)

    }

    private fun initObserver() {
        orthosisFittingVM.campPatientListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val patientData = it.data[0]
                            campPatientData = it.data[0]
                            campId = patientData.camp_id.toInt()
                            if (patientData.isEdited) {
                                orthosisFittingVM.getOrthosisPatientFormByTempId(tempPatientId)
                            } else {
                                if (screen == "Camp_List") {
                                    binding.llBottomOptions.visibility = View.GONE
                                    binding.llImages.visibility = View.GONE
                                    binding.llVideos.visibility = View.GONE

                                    binding.etPatientHeight.isEnabled = false
                                    binding.etPatientHeight.isEnabled = false
                                    binding.etDiagnosisType.isEnabled = false
                                    binding.etPrescribed.isEnabled = false
                                    binding.etPrescribedStatus.isEnabled = false
                                    binding.etOtherDiagnosis.isEnabled = false

                                } else {

                                    binding.etPatientHeight.isEnabled = true
                                    binding.etPatientHeight.isEnabled = true
                                    binding.etDiagnosisType.isEnabled = true
                                    binding.etPrescribed.isEnabled = true
                                    binding.etPrescribedStatus.isEnabled = true
                                    binding.etOtherDiagnosis.isEnabled = true

                                    binding.llImages.visibility = View.GONE
                                    binding.llVideos.visibility = View.GONE
                                    binding.llBottomOptions.visibility = View.VISIBLE
                                }
                                fittingList.clear()

                                binding.tvPatientDetails.text =
                                    "Patient Details - ${patientData.temp_patient_id}"
                                binding.tvCampDetails.text = "Camp : ${patientData.camp_name}"
                                binding.tvPatientName.text = "Name : ${patientData.patient_name}"
                                binding.tvPatientGender.text =
                                    "Gender : ${patientData.patient_gender}"
                                binding.tvPatientAge.text = "Age : ${patientData.patient_age_years}"
                                binding.etPatientHeight.setText(patientData.patient_height_cm)
                                binding.etPatientWeight.setText(patientData.patient_weight_kg)


                                binding.etDiagnosisType.setText(patientData.diagnosis)
                                binding.etlOtherDiagnosis.visibility = View.GONE
                                if (!patientData.equipment_category.isNullOrEmpty()) {
                                    binding.etlPrescribed.visibility = View.VISIBLE
                                    binding.etPrescribed.setText(patientData.equipment_category)
                                } else {
                                    binding.etlPrescribed.visibility = View.GONE

                                }

                                if (!patientData.equipment_status.isNullOrEmpty()) {
                                    binding.etPrescribedStatus.setText(patientData.equipment_status)
                                } else {
                                    binding.etlPrescribedStatus.visibility = View.GONE

                                }
                                for (i in patientData.patientOrthosisTypes) {
                                    fittingList.add(i)
                                }

                                if (screen == "Camp_List") {
                                    setUpFittingFormRecyclerView(false)

                                } else {
                                    setUpFittingFormRecyclerView(true)

                                }
                            }

                        } else {
                            if (screen == "PatientID") {
                                orthosisFittingVM.getOrthosisPatientFormByTempId(tempId)

                            }
                            if (screen == SCREEN_QR) {
                                val intent =
                                    Intent(
                                        this@OrthosisFittingActivity,
                                        OrthosisActivity::class.java
                                    )
                                startActivity(intent)
                                finish()
                            }
                        }


                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisFittingActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    progress.dismiss()

                    Utility.errorToast(this@OrthosisFittingActivity, "Unexpected error")

                }
            }
        }

        orthosisFittingVM.insertOrthosisFormResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    try {
                        Handler(Looper.getMainLooper()).postDelayed({
                            saveFormImages(imageList, it.data?.toInt() ?: 0)
                            saveFormVideos(videoList, it.data?.toInt() ?: 0)
                            Utility.successToast(
                                this@OrthosisFittingActivity,
                                "Successfully Added Patient Data"
                            )
                            val intent =
                                Intent(this@OrthosisFittingActivity, OrthosisMainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                            Log.d("FormSuccess", "Form Id:${it.data}")
                        },200)


                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisFittingActivity, "Unexpected error")

                }
            }
        }

        orthosisFittingVM.orthosisPatientFormListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val patientData = it.data[0]
                            formPatientData = it.data[0]
                            campId = patientData.campId
                            orthosisFittingVM.getFormImageListByFormId(patientData.id)
                            orthosisFittingVM.getFormVideos(patientData.id)
                            if (patientData.isEdited) {
                                fittingList.clear()
                                // campPatientData = it.data[0]
                                binding.tvPatientDetails.text =
                                    "Patient Details - ${patientData.tempPatientId}"
                                binding.tvCampDetails.text = "Camp : ${patientData.location}"
                                binding.tvPatientName.text = "Name : ${patientData.patientName}"
                                binding.tvPatientGender.text =
                                    "Gender : ${patientData.patientGender}"
                                binding.tvPatientAge.text = "Age : ${patientData.patientAge}"
                                binding.etPatientHeight.setText(patientData.patientHeightCm)
                                binding.etPatientWeight.setText(patientData.patientWeightKg)
                                for (i in patientData.orthosisList) {
                                    fittingList.add(i)
                                }
                                orthosisFittingAdapter.notifyDataSetChanged()
                            } else {
                                val intent =
                                    Intent(
                                        this@OrthosisFittingActivity,
                                        OrthosisActivity::class.java
                                    )
                                startActivity(intent)
                                finish()
                            }

                        } else {
                            if (screen == "PatientID") {
                                orthosisFittingVM.getCampPatientListById(tempId?.toInt() ?: 0)
                            } else {
                                val intent =
                                    Intent(
                                        this@OrthosisFittingActivity,
                                        OrthosisActivity::class.java
                                    )
                                startActivity(intent)
                                finish()
                            }
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisFittingActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@OrthosisFittingActivity, "Unexpected error")
                }
            }
        }
        orthosisFittingVM.orthosisPatientFormListByTempId.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val patientData = it.data[0]
                            if (patientData.isEdited) {
                                fittingList.clear()
                                // campPatientData = it.data[0]
                                binding.tvPatientDetails.text =
                                    "Patient Details - ${patientData.tempPatientId}"
                                binding.tvCampDetails.text = "Camp : ${patientData.location}"
                                binding.tvPatientName.text = "Name : ${patientData.patientName}"
                                binding.tvPatientGender.text =
                                    "Gender : ${patientData.patientGender}"
                                binding.tvPatientAge.text = "Age : ${patientData.patientAge}"
                                binding.etPatientHeight.setText(patientData.patientHeightCm)
                                binding.etPatientWeight.setText(patientData.patientWeightKg)
                                binding.etDiagnosisType.setText(patientData.diagnosis)
                                binding.etlOtherDiagnosis.visibility = View.GONE
                                if (!patientData.equipmentCategory.isNullOrEmpty()) {
                                    binding.etPrescribed.setText(patientData.equipmentCategory)
                                } else {
                                    binding.etlPrescribed.visibility = View.VISIBLE
                                }
                                if (!patientData.equipmentStatus.isNullOrEmpty()) {
                                    binding.etPrescribedStatus.setText(patientData.equipmentStatus)
                                } else {
                                    binding.etlPrescribedStatus.visibility = View.VISIBLE
                                }
                                for (i in patientData.orthosisList) {
                                    fittingList.add(i)
                                }
                                if (screen == "Camp_List") {
                                    setUpFittingFormRecyclerView(false)

                                } else {
                                    setUpFittingFormRecyclerView(true)

                                }
                            } else {
                                val intent =
                                    Intent(
                                        this@OrthosisFittingActivity,
                                        OrthosisActivity::class.java
                                    )
                                intent.putExtra("local_patient_id", patientData.id.toString())
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            if (screen == "PatientID") {
                                Utility.infoToast(this@OrthosisFittingActivity, "No Data Found")
                                onBackPressed()
                            } else {
                                val intent =
                                    Intent(
                                        this@OrthosisFittingActivity,
                                        OrthosisActivity::class.java
                                    )
                                startActivity(intent)
                                finish()
                            }
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisFittingActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@OrthosisFittingActivity, "Unexpected error")
                }
            }
        }

        orthosisFittingVM.formImageListFormId.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            imageList.clear()
                            imageList.addAll(it.data)
                            isFormImageLocal = true
                            imageAdapter.notifyDataSetChanged()
                        }

                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisFittingActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisFittingActivity, "Unexpected error")
                }
            }
        }
        orthosisFittingVM.formVideosListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            isFormVideoLocal = true
                            videoList.clear()
                            videoList.addAll(it.data)
                            val thumbnail = ThumbnailUtils.createVideoThumbnail(
                                it.data[0].video,
                                MediaStore.Video.Thumbnails.MINI_KIND
                            )
                            binding.cvFile.visibility = View.VISIBLE

                            binding.ivFile.setImageBitmap(thumbnail)
                        }

                    } catch (e: Exception) {

                        Utility.errorToast(this@OrthosisFittingActivity, e.message.toString())

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisFittingActivity, "Unexpected error")

                }
            }
        }


        orthosisFittingVM.diagnosisMasterList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            diagnosisList.clear()
                            diagnosisList.addAll(it.data)
                            binding.tvAddMoreOrthosisType.setOnClickListener {
                                val measurementList1 =
                                    listOf(
                                        Measurement(false, "", "", 1),
                                    )
                                val orthosisTypeForm1 = OrthosisType(
                                    1,
                                    measurementList1,
                                    "ANKEL FOOT ORTHOSIS(PP) WITH FIXED 90 DEGREE ANKEL"
                                )
                                val data =
                                    OrthosisPatientData(
                                        0,//requested by server to add 0 - first time 0
                                        "",
                                        "",
                                        "",
                                        "",
                                        OrthosisType(1, listOf(), ""),
                                        "",
                                        "",
                                        "",
                                        examinationDate = getCurrentDate(),//added manually as per requirement
                                        "",
                                        "",
                                        orthoFormId = 0,
                                        listOf(),
                                        image = "",
                                        orthosisImageList = mutableListOf()
                                    )
                            }
                        }

                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisFittingActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisFittingActivity, "Unexpected error")

                }
            }
        }
    }

    private fun setUpFittingFormRecyclerView(isEditable: Boolean) {
        if (fittingList.isNullOrEmpty()){
            fittingList = provideOrthosisTypeData()
            
        }
        orthosisFittingAdapter = OrthosisFittingFormAdapter(
            isEditable,
            this,
            fittingList,
            object : OrthosisFittingFormAdapter.OrthosisFormClickListener {
                override fun onMeasurementData(parentPosition: Int, childPosition: Int) {
//
                }

                override fun setOrthosisEditTextData(
                    position: Int,
                    fieldName: String,
                    fieldValue: String
                ) {
                    when (fieldName) {
                        "fittingStatus" -> fittingList[position].fit_properly =
                            fieldValue

                        "fittingReason" -> fittingList[position].fit_properly_reason =
                            fieldValue

                        "status" -> {
                            fittingList[position].status = fieldValue
                            if (fittingList.any { it.status == "Given" }) {
                                isStatusGiven = true
                                binding.llImages.visibility = View.VISIBLE
                                binding.llVideos.visibility = View.VISIBLE
                            } else {
                                isStatusGiven = false
                                binding.llImages.visibility = View.GONE
                                binding.llVideos.visibility = View.GONE
                            }
                        }

                        "statusNotes" -> fittingList[position].statusNotes =
                            fieldValue
                    }
                }

            })

        binding.rvOrthosisFittingForm.apply {
            adapter = orthosisFittingAdapter
            layoutManager = LinearLayoutManager(this@OrthosisFittingActivity)
        }
    }

    private fun provideOrthosisTypeData(): ArrayList<OrthosisPatientData> {
        val data = ArrayList<OrthosisPatientData>()
        val mData = ArrayList<MeasurementPatientData>()

        for (i in dummyOrthosisForm()) {
            for (j in i.measurements) {
                mData.add(
                    MeasurementPatientData(
                        1,
                        j,
                        value = 0.0,
                        unit = "",
                        otherMeasurement = ""
                    )
                )
            }
        }

        for (i in dummyOrthosisForm()) {
            data.add(
                OrthosisPatientData(
                    1,
                    "",
                    "",
                    "",
                    "",
                    i,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    orthoFormId = 0,
                    mData,
                    image = "",
                    orthosisImageList = mutableListOf()
                )
            )
        }

        return data
    }

    private fun dummyOrthosisForm(): List<OrthosisType> {
        val measurementList1 =
            listOf(
                Measurement(false, "", "Length HEEL TO 1st TOE", 1),
                Measurement(false, "", "FORE FOOT CIRCUMFERENCE at metatarsal level", 2),
                Measurement(false, "", "ANKEL CIRCUMFERENCE 1 inch bellow the head of fibula", 3)
            )

        val measurementList2 =
            listOf(
                Measurement(false, "", "HEEL TO TOE", 1),
                Measurement(false, "", "FOREFOOT", 2),
                Measurement(false, "", "ANKEL", 3)
            )
        val orthosisTypeForm1 =
            OrthosisType(1, measurementList1, "ANKEL FOOT ORTHOSIS(PP) WITH FIXED 90 DEGREE ANKEL")
        val orthosisTypeForm2 = OrthosisType(2, measurementList2, "DYNAMIC ANKEL FOOT ORTHOSIS(PP)")

        // return listOf(orthosisTypeForm1, orthosisTypeForm2)
        return listOf(orthosisTypeForm1)
    }

    private fun getPatientData() {
        sessionManager = SessionManager(this)
        try {
            val decodedText = sessionManager.getPatientData()
            val gson = Gson()
            val patientData = gson.fromJson(decodedText, PatientData::class.java)

            // Access the values
            tempPatientId = patientData.patientId
            val patientFname = patientData.patientFname
            val patientLname = patientData.patientLname
            val patientAge = patientData.patientAge
            val patientID = patientData.patientId
            val patientGender = patientData.patientGen
            val camp = patientData.location
            val ageUnit = patientData.AgeUnit
            campId = patientData.camp_id


            binding.tvPatientDetails.text = "Patient Details - ${patientID}"
            binding.tvCampDetails.text = "Camp : ${camp}"
            binding.tvPatientName.text = "Name : ${patientFname} ${patientLname}"
            binding.tvPatientGender.text = "Gender : ${patientGender}"
            binding.tvPatientAge.text = "Age : ${patientAge}"


            if (screen == SCREEN_QR){
                orthosisFittingVM.getOrthosisPatientFormByTempId(tempPatientId)
            }
            else{
                orthosisFittingVM.getCampPatientListById(patientID)
            }


        } catch (e: Exception) {
            Utility.warningToast(this@OrthosisFittingActivity, "Please try again")
            e.printStackTrace()
            Log.d("Error", e.message.toString())
            onBackPressed()
        }

    }

    private fun setUpImagesRecyclerView() {
        imageAdapter =
            ImageAdapter(
                this@OrthosisFittingActivity,
                imageList,
                object : ImageAdapter.ImageAdapterEvent {
                    override fun onImageClick(position: Int) {
                        //
                    }

                    override fun onImageRemove(position: Int) {
                        //
                    }

                })

        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(this@OrthosisFittingActivity, 4)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == OrthosisActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Retrieve the data sent from SecondActivity
            val videoUrl = data?.getStringExtra(Constants.VIDEO_URL)
            if (videoUrl != "") {
                val videosId = ArrayList<Int>()
                for (i in videoList) {
                    videosId.add(i.id)
                }
                isFormVideoLocal = false
                orthosisFittingVM.deleteFormVideosById(videosId)
                videoList.clear()
                val videoItem = FormVideos(
                    video = videoUrl!!,
                    temp_patient_id = "",
                    camp_id = "",
                    patient_id = "",
                    formId = 0
                )
                videoList.add(videoItem)
                val thumbnail = ThumbnailUtils.createVideoThumbnail(
                    videoUrl,
                    MediaStore.Video.Thumbnails.MINI_KIND
                )
                binding.cvFile.visibility = View.VISIBLE
                binding.ivFile.setImageBitmap(thumbnail)
            }
        }
    }

    private fun updateOrthosis() {

        var patientOrthosisForm: OrthosisPatientForm? = null
//        if (screen == "Camp_List") {
        if (campPatientData != null) {
            patientOrthosisForm = OrthosisPatientForm(//id not added will get auto generated
                patientName = campPatientData!!.patient_name,
                patientId = campPatientData!!.patient_id.toInt(),
                patientGender = campPatientData!!.patient_gender,
                patientAge = campPatientData!!.patient_age_years.toInt(),
                campId = campPatientData!!.camp_id.toInt(),
                location = campPatientData!!.camp_name,
                ageUnit = "",
                regNo = "",
                examinationDate = getCurrentDate(),//orthosis date for form only not orthosis type date(examination date)
                orthosisList = fittingList,
                orthosisDate = getCurrentDate(),
                tempPatientId = campPatientData!!.temp_patient_id,
                patientContactNo = "123456789",
                campName = campPatientData!!.camp_name,
                patientAgeYears = campPatientData!!.patient_age_years,
                patientHeightCm = binding.etPatientHeight.text?.toString() ?: "",
                patientWeightKg = binding.etPatientWeight.text?.toString() ?: "",
                diagnosis = if (binding.etDiagnosisType.text.toString() == "Other") binding.etOtherDiagnosis.text.toString() else binding.etDiagnosisType.text.toString(),
                diagnosisId = selectedDiagnosis?.id ?: 0,
                equipmentSupport = selectedEquipment?.equipment_support ?: "",
                equipmentCategory = selectedEquipment?.equipment_category ?: "",
                equipmentId = selectedEquipment?.id ?: 0,
                equipmentStatus = binding.etPrescribedStatus.text?.toString() ?: "",
                equipmentStatusNotes = "",
                isEdited = true,
                isLocal = false
            )
        } else {
            patientOrthosisForm = OrthosisPatientForm(//id not added will get auto generated
                patientName = formPatientData!!.patientName,
                patientId = formPatientData!!.patientId,
                patientGender = formPatientData!!.patientGender,
                patientAge = formPatientData!!.patientAgeYears.toInt(),
                campId = formPatientData!!.campId.toInt(),
                location = formPatientData!!.campName,
                ageUnit = "",
                regNo = "",
                examinationDate = getCurrentDate(),//orthosis date for form only not orthosis type date(examination date)
                orthosisList = fittingList,
                orthosisDate = getCurrentDate(),
                tempPatientId = formPatientData!!.tempPatientId,
                patientContactNo = "123456789",
                campName = formPatientData!!.campName,
                patientAgeYears = formPatientData!!.patientAgeYears,
                patientHeightCm = binding.etPatientHeight.text?.toString() ?: "",
                patientWeightKg = binding.etPatientWeight.text?.toString() ?: "",
                diagnosis = if (binding.etDiagnosisType.text.toString() == "Other") binding.etOtherDiagnosis.text.toString() else binding.etDiagnosisType.text.toString(),
                diagnosisId = selectedDiagnosis?.id ?: 0,
                equipmentSupport = selectedEquipment?.equipment_support ?: "",
                equipmentCategory = selectedEquipment?.equipment_category ?: "",
                equipmentId = selectedEquipment?.id ?: 0,
                equipmentStatus = binding.etPrescribedStatus.text?.toString() ?: "",
                equipmentStatusNotes = "",
                isEdited = true,
                isLocal = false
            )
        }

//        }
        orthosisFittingVM.insertOrthosisPatientForm(patientOrthosisForm)

        //Camp Patient Logic
        if (campPatientData != null) {
            campPatientData?.let {
                it.orthosis_date = getCurrentDate()
                it.patientOrthosisTypes = fittingList
                it.patient_height_cm = binding.etPatientHeight.text?.toString() ?: ""
                it.patient_weight_kg = binding.etPatientWeight.text?.toString() ?: ""
                it.isEdited = true
            }

            orthosisFittingVM.insertSingleCampPatient(campPatientData!!)
        }


    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun saveFormImages(imageList: ArrayList<FormImages>, formId: Int) {
        val formImageList = ArrayList<FormImages>()
        if (isFormImageLocal) {

            orthosisFittingVM.insertFormImageList(imageList)
        } else {
            val filteredImages = imageList.filter { it.id == 0 }
            for (i in filteredImages) {
                formImageList.add(
                    FormImages(
                        images = i.images,
                        temp_patient_id = tempPatientId.toString(),
                        formId = formId,
                        camp_id = campId.toString()
                    )
                )
            }
            //add a viewmodel and save in local db all the images

            orthosisFittingVM.insertFormImageList(formImageList)
        }
    }

    private fun saveFormVideos(videos: List<FormVideos>, formId: Int) {
        val formVideoList = ArrayList<FormVideos>()
        if (!isFormVideoLocal) {
            for (i in videos) {
                formVideoList.add(
                    FormVideos(
                        video = i.video,
                        temp_patient_id = tempPatientId.toString(),
                        camp_id = campId.toString(),
                        patient_id = formId.toString(),
                        formId = formId
                    )
                )
            }
            //add a viewmodel and save in local db all the images
            orthosisFittingVM.insertFormVideoList(formVideoList)
        } else {
            orthosisFittingVM.insertFormVideoList(videoList)
        }

    }

    private fun validateForm(): Boolean {
        var isValid = true

        for ((index, data) in fittingList.withIndex()) {
            if (data.status == "Given") {
                if (data.fit_properly.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Select Fitting Status")
                    isValid = false
                    return isValid
                }
            }

            if (data.status == "Given") {
                if (data.fit_properly_reason.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Enter Fitting Reason")
                    isValid = false
                    return isValid
                }
            }

            if (data.status != "Given" && data.status != "Pending") {
                if (data.statusNotes.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Enter Comments")
                    isValid = false
                    return isValid
                }
            }
        }
        if (isStatusGiven) {
            //if image is empty show error to
            if (imageList.isNullOrEmpty()) {
                Utility.errorToast(this@OrthosisFittingActivity, "Select Images")
                isValid = false
                return isValid
            }

        }

        if (isStatusGiven) {
            //if video is empty show error to
            if (videoList.isNullOrEmpty()) {
                Utility.errorToast(this@OrthosisFittingActivity, "Select Videos")
                isValid = false
                return isValid
            }
        }
        return isValid
    }

    private fun inflateDiagnosisBottomSheet() {
        val items = ArrayList<SearchAbleList>()
        // diagnosisList.add(DiagnosisType(0,"Other"))
        for (i in diagnosisList.indices) {
            items.add(SearchAbleList(i, diagnosisList[i].name))
        }
        val dialog = SingleSelectBottomSheetDialogFragment(
            this@OrthosisFittingActivity,
            items,
            "Select Diagnosis",
            0,
            true
        ) { selectedValue ->
            val diagnosisType = diagnosisList[selectedValue.position]
            binding.etDiagnosisType.setText(diagnosisType.name)
            //condition and flag to check if the selected diagnosis is OTHER
            //isSelectedOtherDiagnosis = diagnosisType.name == "Other"
            if (diagnosisType.name == "Other") {
                isSelectedOtherDiagnosis = true
                binding.etlOtherDiagnosis.visibility = View.VISIBLE
            } else {
                isSelectedOtherDiagnosis = false
                binding.etlOtherDiagnosis.visibility = View.GONE
                binding.etOtherDiagnosis.setText("")
            }
        }
        dialog.show(supportFragmentManager, "SingleSelectBottomSheetDialogFragment")
    }

    private fun setAsteriskColor(etlField: TextInputLayout, hintText: String) {
        val spannableString = SpannableString(hintText)

        val redColor =
            ForegroundColorSpan(this@OrthosisFittingActivity.resources.getColor(R.color.dark_red))
        spannableString.setSpan(
            redColor,
            hintText.length - 1,
            hintText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        etlField.hint = spannableString

    }
}