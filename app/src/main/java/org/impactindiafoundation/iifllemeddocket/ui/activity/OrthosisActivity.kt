package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.ThumbnailUtils
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.BoolRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_FILE
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_PATH
import com.colourmoon.imagepicker.utils.ResultImage
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.SearchAbleList
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SingleSelectBottomSheetDialogFragment
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.Utils.imageUtils.ImagePickerDialog
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.VIDEO_URL
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.Equipment
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImage
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideos
import org.impactindiafoundation.iifllemeddocket.architecture.model.Measurement
import org.impactindiafoundation.iifllemeddocket.architecture.model.MeasurementPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModelItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OrthosisViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityOrthosisBinding
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.EquipmentImageAdapter
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.ImageAdapter
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.OrthosisFormAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrthosisActivity : BaseActivity() {

    private lateinit var binding: ActivityOrthosisBinding
    private lateinit var orthosisFormAdapter: OrthosisFormAdapter
    private var orthosisFormList = ArrayList<Int>()
    private var orthosisTypeList = ArrayList<OrthosisType>()
    private var patientOrthosisList = ArrayList<OrthosisPatientData>()
    private val orthosisViewModel: OrthosisViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    private var isStatusGiven = false
    private var imagePosition = 0
    private var imageList = ArrayList<FormImages>()
    private var removeImageList = ArrayList<FormImages>()
    private var localFormImages = ArrayList<FormImages>()
    private var videoList = ArrayList<FormVideos>()
    private lateinit var imageAdapter: ImageAdapter
    private var equipmentImages = ArrayList<EquipmentImage>()
    private lateinit var equipmentImageAdapter: EquipmentImageAdapter

    //data for orthosis files
    private var tempPatientId = ""
    private var patientId = ""
    private var campId = ""
    private var localPatientId = ""
    private var screen = ""
    private lateinit var patientData: PatientData
    private lateinit var localPatientData: OrthosisPatientForm
    private var canEdit = true
    private var isFormImageLocal = false
    private var isFormVideoLocal = false
    private var isOrthoImageLocal = false
    private var isImageEdited = false
    private var isFormEdited = true
    private var isSelectedOtherDiagnosis = false
    private var isEquipmentImageLocal = false

    private val orthosisImageList = ArrayList<OrthosisImages>()
    private val removeOrthosisImageList = ArrayList<OrthosisImages>()
    private val orthosisLocalImageList = ArrayList<OrthosisImages>()

    //patient data
    private lateinit var localUser: UserModel

    private lateinit var popupWindow: PopupWindow

    private var diagnosisList = ArrayList<DiagnosisType>()
    private var equipmentList = ArrayList<Equipment>()
    private var deleteEquipmentList = ArrayList<EquipmentImage>()
    private var isMeasurementOther = false
    private var isAmputee = false
    private var selectedDiagnosis: DiagnosisType? = null
    private var selectedEquipment: Equipment? = null
    private var isPrescribedStatusNeeded = false
    private var isFormEditable = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrthosisBinding.inflate(layoutInflater)
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
        //getPatientData()
        initUi()
        orthosisViewModel.getOrthosisMasterLocal()
        orthosisViewModel.getDiagnosisMasterLocal()
        orthosisViewModel.getOrthosisEquipmentMasterLocal()
        //getIntentData()
        getLocalUserData()
        //setUpOrthosisFormRecyclerView()
        setUpImagesRecyclerView()
        setUpEquipmentImagesRecyclerView()
        initObserver()

    }

    private fun initUi() {
        val tempPath =
            "/data/data/org.impactindiafoundation.iifllemeddocket/files/recorded_video.mp4"

        screen = intent.getStringExtra("screen") ?: "QrCode"
        canEdit = intent.getBooleanExtra("edit", true)

        if (canEdit) {
            binding.llBottomOptions.visibility = View.VISIBLE
        } else {
            binding.llBottomOptions.visibility = View.GONE
        }
        testOrthosisData()
        binding.orthosisToolBar.toolbar.title = "Patient Form"


        var isArrowDown = true

        binding.llPatientDetailTab.setOnClickListener {
            val fromDegree = if (isArrowDown) 0f else 180f
            val toDegree = if (isArrowDown) 180f else 360f

            val rotateAnimator =
                ObjectAnimator.ofFloat(binding.ivPatientArrowDown, "rotation", fromDegree, toDegree)
            rotateAnimator.duration = 300 // duration of the animation in milliseconds
            rotateAnimator.start()

            if (isArrowDown) {
                binding.llPatientSection.visibility = View.GONE
            } else {
                binding.llPatientSection.visibility = View.VISIBLE
            }
            isArrowDown = !isArrowDown

        }

        binding.etExaminationDate.setOnClickListener {
            Utility.openDatePicker(
                this,
                Utility.HIDE_PREVIOUS_DATES,
                binding.etExaminationDate,
                object : Utility.DateListener {
                    override fun onDateSelected(date: String) {
                        binding.etExaminationDate.setText(date)
                    }

                })
        }

        binding.btnSavePatientOrthosis.setOnClickListener {
            saveOrthosisPatientForm()
        }

        binding.tvImages.setOnClickListener {
            if (isFormEditable) {
                if (canEdit) {
                    if (imageList.size >= 4) {
                        if (screen != "QrCode") {

                            val dialog = ImagePickerDialog(
                                this@OrthosisActivity,
                                formImageLauncher,
                                "Upload Orthosis Image"
                            )
                            dialog.show(supportFragmentManager, "ImagePickerDialog")
                        } else {
                            Utility.warningToast(this, "Only 4 images are allowed!")

                        }
                    } else {

                        if (screen != "QrCode") {
                            val dialog = ImagePickerDialog(
                                this@OrthosisActivity,
                                formImageLauncher,
                                "Upload Orthosis Image"
                            )
                            dialog.show(supportFragmentManager, "ImagePickerDialog")
                        } else {
                            val dialog = ImagePickerDialog(
                                this@OrthosisActivity,
                                formImageLauncher,
                                "Upload Orthosis Image"
                            )
                            dialog.show(supportFragmentManager, "ImagePickerDialog")
                        }

                    }
                } else {
                    Utility.warningToast(this@OrthosisActivity, "Cannot Edit Data")
                }
            } else {
                Utility.warningToast(this@OrthosisActivity, "Not Editable")
            }


        }

        binding.tvVideos.setOnClickListener {
            //  dispatchTakeVideoIntent()
            if (isFormEditable) {
                val intent = Intent(this@OrthosisActivity, VideoRecordActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE)
            } else {
                Utility.warningToast(this@OrthosisActivity, "Not Editable")

            }
        }

        binding.etPatientHeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Move focus to the next EditText
                binding.etPatientWeight.requestFocus()

                true  // Indicate that we've handled the action
            } else {
                false  // Otherwise, do nothing
            }
        }
        binding.etPatientWeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etPatientWeight.clearFocus()  // Clear the focus from this EditText
                val imm =
                    binding.etPatientWeight.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etPatientWeight.windowToken, 0)
                true  // Return true to indicate that we've handled the action
            } else {
                false  // Otherwise, do nothing
            }
        }

        binding.etDiagnosisType.setOnClickListener {
            if (isFormEditable) {
                inflateDiagnosisBottomSheet()
            } else {
                Utility.warningToast(this@OrthosisActivity, "Not Editable")
            }

        }
        binding.etPrescribed.setOnClickListener {
            if (isFormEditable) {
                inflateEquipmentBottomSheet()
            } else {
                Utility.warningToast(this@OrthosisActivity, "Not Editable")
            }
        }

        val statusOptions = listOf("Pending", "Given")
        val statusadapter =
            ArrayAdapter(
                this@OrthosisActivity,
                android.R.layout.simple_dropdown_item_1line,
                statusOptions
            )
        binding.etPrescribedStatus.setAdapter(statusadapter)
        binding.etPrescribedStatus.setOnClickListener {
            if (isFormEditable) {
                binding.etPrescribedStatus.showDropDown()

            } else {
                Utility.warningToast(this@OrthosisActivity, "Not Editable")
            }
        }

        binding.etPrescribedStatus.setOnItemClickListener { _, _, position, _ ->
            val selectedValue = statusOptions[position]
            if (selectedValue == "Given") {
                binding.cvEquipmentPics.visibility = View.VISIBLE
            } else {
                binding.cvEquipmentPics.visibility = View.GONE
            }
        }
        val diagnosisTypeHint = this@OrthosisActivity.getString(R.string.txt_diagnosis_type)
        setAsteriskColor(binding.etlDiagnosisType, diagnosisTypeHint)

        val enterDiagnosisHint =
            this@OrthosisActivity.getString(R.string.txt_enter_diagnosis_type)
        setAsteriskColor(binding.etlOtherDiagnosis, enterDiagnosisHint)

        val prescribedStatusHint =
            this@OrthosisActivity.getString(R.string.txt_prescribed_status)
        setAsteriskColor(binding.etlPrescribedStatus, prescribedStatusHint)

        binding.llEquipmentPicsTab.setOnClickListener {
            if (isFormEditable){
                if (equipmentImages.size < 4) {
                    val dialog = ImagePickerDialog(
                        this@OrthosisActivity,
                        equipmentImageLauncher,
                        "Upload Equipment Image"
                    )
                    dialog.show(supportFragmentManager, "ImagePickerDialog")
                } else {
                    Utility.warningToast(this@OrthosisActivity, "Only 4 images are allowed")
                }
            }
            else{
                Utility.warningToast(this@OrthosisActivity,"Not Editable")
            }
        }

        binding.btnEditPatientOrthosis.setOnClickListener {
            onFormEditClick()
        }
    }

    private fun initObserver() {
        for (i in dummyOrthosisForm()) {
            orthosisViewModel.insertOrthosisMasterLocal(i)
        }

        orthosisViewModel.insertOrthosisFormResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        //  Utility.successToast(this@OrthosisActivity,"")
                        saveFormImages(imageList, it.data?.toInt() ?: 0)
                        imageList.clear()
                        saveFormVideos(videoList, it.data?.toInt() ?: 0)
                        videoList.clear()
                        saveOrthosisImage(patientOrthosisList, it.data.toString() ?: "")
                        saveEquipmentImages(it.data.toString() ?: "")


                        //for duplicate image
                        Utility.successToast(
                            this@OrthosisActivity,
                            "Successfully Added Patient Data"
                        )
                        val intent =
                            Intent(this@OrthosisActivity, OrthosisMainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                        Log.d("FormSuccess", "Form Id:${it.data}")
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisActivity, "Unexpected error")

                }
            }
        }

        orthosisViewModel.insertedFormImageIds.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisActivity, e.message.toString())

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisActivity, "Unexpected error")

                }
            }
        }

        orthosisViewModel.orthosisMasterList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            orthosisTypeList.clear()
                            orthosisTypeList.addAll(it.data)
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
                                //orthosisTypeList.add(orthosisTypeForm1)
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
                                patientOrthosisList.add(data)
                                orthosisFormAdapter.notifyDataSetChanged()
                                binding.rvOrthosisForm.smoothScrollToPosition(
                                    patientOrthosisList.size - 1
                                )

                            }
                        }

                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisActivity, e.message.toString())

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisActivity, "Unexpected error")

                }
            }
        }
        orthosisViewModel.diagnosisMasterList.observe(this) {
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
                                patientOrthosisList.add(data)
                                orthosisFormAdapter.notifyDataSetChanged()
                                binding.rvOrthosisForm.scrollToPosition(patientOrthosisList.size - 1)

                            }
                        }

                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisActivity, e.message.toString())

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisActivity, "Unexpected error")

                }
            }
        }

        orthosisViewModel.orthosisEquipmentMasterList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            equipmentList.clear()
                            equipmentList.addAll(it.data)
                        }

                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisActivity, e.message.toString())

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisActivity, "Unexpected error")

                }
            }
        }

        orthosisViewModel.orthosisPatientFormListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            isFormEditable = false
                            binding.btnSavePatientOrthosis.visibility = View.GONE
                            binding.tvAddMoreOrthosisType.visibility = View.GONE
                            binding.btnEditPatientOrthosis.visibility = View.VISIBLE


                            localPatientData = it.data[0]
                            if (localPatientData.isSynced == 0) {
                                binding.llBottomOptions.visibility = View.VISIBLE
                            } else {
                                binding.llBottomOptions.visibility = View.GONE
                            }
                            patientId = localPatientData.id.toString()
//                            getOrthosisImagesByFormId(patientId)
                            binding.tvPatientDetails.text =
                                "Patient Details - ${localPatientData.tempPatientId}"
                            binding.tvCampDetails.text = "Camp : ${localPatientData.campName}"
                            binding.tvPatientName.text =
                                "Name : ${localPatientData.patientName}"
                            binding.tvPatientGender.text =
                                "Gender : ${localPatientData.patientGender}"
                            binding.tvPatientAge.text =
                                "Age : ${localPatientData.patientAgeYears}"

                            binding.etPatientHeight.setText(localPatientData.patientHeightCm)
                            binding.etPatientWeight.setText(localPatientData.patientWeightKg)
                            binding.etPatientWeight.isFocusable = isFormEditable
                            binding.etPatientHeight.isFocusable = isFormEditable

                            binding.etPatientWeight.setOnClickListener {

                                if (!isFormEditable) {
                                    Utility.warningToast(this@OrthosisActivity, "Not Editable")
                                }


                            }
                            binding.etPatientHeight.setOnClickListener {
                                if (!isFormEditable) {
                                    Utility.warningToast(this@OrthosisActivity, "Not Editable")
                                }
                            }


                            //logic for checking if selected diagnosis is OTHER
                            if (localPatientData.diagnosisId == 1){
                                binding.etlOtherDiagnosis.visibility = View.VISIBLE
                                binding.etOtherDiagnosis.setText(localPatientData.diagnosis)
                                binding.etDiagnosisType.setText("Other")
                            }
                            else{
                                binding.etlOtherDiagnosis.visibility = View.GONE
                                binding.etDiagnosisType.setText(localPatientData.diagnosis)
                                binding.etOtherDiagnosis.setText("")

                            }

                            if (!localPatientData.equipmentCategory.isNullOrEmpty()) {
                                binding.etlPrescribed.visibility = View.VISIBLE
                                binding.etPrescribed.setText(localPatientData.equipmentCategory)
                            } else {
                                binding.etlPrescribed.visibility = View.GONE
                                binding.etPrescribed.setText("")


                            }

                            if (!localPatientData.equipmentStatus.isNullOrEmpty()) {
                                binding.etlPrescribedStatus.visibility = View.VISIBLE
                                binding.etPrescribedStatus.setText(localPatientData.equipmentStatus)
                                val statusOptions = listOf("Pending", "Given")
                                val statusadapter =
                                    ArrayAdapter(
                                        this@OrthosisActivity,
                                        android.R.layout.simple_dropdown_item_1line,
                                        statusOptions
                                    )
                                binding.etPrescribedStatus.setAdapter(statusadapter)
                                binding.etPrescribedStatus.setOnClickListener {
                                    if (isFormEditable) {
                                        binding.etPrescribedStatus.showDropDown()

                                    } else {
                                        Utility.warningToast(
                                            this@OrthosisActivity,
                                            "Not Editable"
                                        )
                                    }
                                }
                            } else {
                                binding.etlPrescribedStatus.visibility = View.GONE
                                binding.etPrescribedStatus.setText("")
                                val statusOptions = listOf("Pending", "Given")
                                val statusadapter =
                                    ArrayAdapter(
                                        this@OrthosisActivity,
                                        android.R.layout.simple_dropdown_item_1line,
                                        statusOptions
                                    )
                                binding.etPrescribedStatus.setAdapter(statusadapter)
                                binding.etPrescribedStatus.setOnClickListener {
                                    if (isFormEditable) {
                                        binding.etPrescribedStatus.showDropDown()

                                    } else {
                                        Utility.warningToast(
                                            this@OrthosisActivity,
                                            "Not Editable"
                                        )
                                    }
                                }


                            }

                            if (localPatientData.orthosisList.any { it.status == "Given" }) {
                                binding.llImages.visibility = View.VISIBLE
                                binding.llVideos.visibility = View.VISIBLE
                            } else {
                                binding.llImages.visibility = View.GONE
                                binding.llVideos.visibility = View.GONE
                            }


                            patientOrthosisList.clear()
                            patientOrthosisList.addAll(localPatientData.orthosisList)
                            setUpOrthosisFormRecyclerView(isFormEditable)
                        } else {
                            getPatientData()
                            setUpOrthosisFormRecyclerView(true)
                            binding.btnEditPatientOrthosis.visibility = View.GONE
                            binding.btnSavePatientOrthosis.visibility = View.VISIBLE
                            binding.tvAddMoreOrthosisType.visibility = View.VISIBLE
                            binding.tvSaveOrthosis.text = "Submit"
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@OrthosisActivity, "Unexpected error")
                }
            }
        }

        orthosisViewModel.userList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            localUser = it.data[0]
                            screen = intent.getStringExtra("screen") ?: "QrCode"
                            canEdit = intent.getBooleanExtra("edit", true)
                            tempPatientId = intent.getStringExtra("temp_id") ?: ""
                            localPatientId = intent.getStringExtra("local_patient_id") ?: "0"
//                            if (screen == "Camp_List") {
                            orthosisViewModel.getOrthosisPatientFormById(
                                localPatientId?.toInt() ?: 0
                            )
                            orthosisViewModel.getFormImageListByFormId(
                                localPatientId?.toInt() ?: 0
                            )
                            orthosisViewModel.getEquipmentImageByFormId(localPatientId ?: "0")
                            orthosisViewModel.getFormVideos(localPatientId?.toInt() ?: 0)
                             orthosisViewModel.getOrthosisImageByFormId(localPatientId)
                        }

                    } catch (e: Exception) {

                    }
                }

                Status.ERROR -> {
                }
            }
        }
        orthosisViewModel.formImageListFormId.observe(this) { formImages ->
            when (formImages.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!formImages.data.isNullOrEmpty()) {
                            imageList.clear()
                            for ((index, data) in formImages.data.withIndex()) {
                                if (index == 4) {
                                    break
                                } else {
                                    imageList.add(data)
                                }
                            }
                            isFormImageLocal = true
                            imageAdapter.notifyDataSetChanged()
                        }

                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisActivity, "Unexpected error")
                }
            }
        }
        orthosisViewModel.equipmentImagesList.observe(this) { equipmentLocalImages ->
            when (equipmentLocalImages.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!equipmentLocalImages.data.isNullOrEmpty()) {
                            isEquipmentImageLocal = true
                            equipmentImages.clear()
                            for ((index, data) in equipmentLocalImages.data.withIndex()) {
                                if (index == 4) {
                                    break
                                } else {
                                    equipmentImages.add(data)
                                }
                            }

                            binding.cvEquipmentPics.visibility = View.VISIBLE
                            imageAdapter.notifyDataSetChanged()
                        } else {
                            isEquipmentImageLocal = false
                            binding.cvEquipmentPics.visibility = View.GONE
                        }

                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisActivity, e.message.toString())

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisActivity, "Unexpected error")

                }
            }
        }
        orthosisViewModel.formVideosListById.observe(this) {
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
                    }
                }

                Status.ERROR -> {
                }
            }
        }

        orthosisViewModel.orthosisImagesList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
//                            orthosisImageList.clear()
//                            orthosisImageList.addAll(it.data)
                            orthosisLocalImageList.clear()
                            orthosisLocalImageList.addAll(it.data)
                        }
                    } catch (e: Exception) {

                    }
                }

                Status.ERROR -> {
                    progress.dismiss()

                }
            }
        }

    }

    val orthosisImageLauncher = object : ResultImage {

        override val result: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imagePath = result.data?.getStringExtra(RESULT_IMAGE_PATH)
                var imageFile: File? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getSerializableExtra(
                            RESULT_IMAGE_FILE, File::class.java
                        )
                    } else {
                        result.data?.getSerializableExtra(RESULT_IMAGE_FILE) as File
                    }

                val formOrthosis = patientOrthosisList[imagePosition]

                val orthosisImage = OrthosisImages(
                    images = imagePath!!,
                    temp_patient_id = tempPatientId,
                    camp_id = campId,
                    patient_id = "",
                    orthosis_id = formOrthosis.orthosis.id.toString(),
                    orthosisFormId = formOrthosis.orthoFormId,
                    amputation_side = formOrthosis.amputationSide
                )
                patientOrthosisList[imagePosition].orthosisImageList.add(orthosisImage)
                orthosisFormAdapter.notifyDataSetChanged()

            }
        }
    }

    val formImageLauncher = object : ResultImage {

        override val result: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imagePath = result.data?.getStringExtra(RESULT_IMAGE_PATH)
                var imageFile: File? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getSerializableExtra(
                            RESULT_IMAGE_FILE, File::class.java
                        )
                    } else {
                        result.data?.getSerializableExtra(RESULT_IMAGE_FILE) as File
                    }

                if (screen != "QrCode") {
                    if (imageList.size == 4) {
                        // orthosisViewModel.deleteFormImages(imageList)
                        val formImageIds = ArrayList<Int>()
                        for (i in imageList) {
                            formImageIds.add(i.id)
                        }
                        imageList.clear()
                        orthosisViewModel.deleteFormImagesById(formImageIds)

                    }
                }
                val formImage = FormImages(
                    formId = 0,
                    images = imagePath!!,
                    temp_patient_id = tempPatientId,
                    camp_id = campId
                )
                isFormImageLocal = false
                imageList.add(formImage)
                imageAdapter.notifyDataSetChanged()

            }
        }
    }

    val equipmentImageLauncher = object : ResultImage {

        override val result: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imagePath = result.data?.getStringExtra(RESULT_IMAGE_PATH)
                var imageFile: File? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getSerializableExtra(
                            RESULT_IMAGE_FILE, File::class.java
                        )
                    } else {
                        result.data?.getSerializableExtra(RESULT_IMAGE_FILE) as File
                    }
                val equiomentImage = EquipmentImage(
                    images = imagePath!!,
                    temp_patient_id = tempPatientId,
                    camp_id = campId,
                    patient_image_type = "prescribed",
                    patient_id = ""
                )
                isEquipmentImageLocal = false
                equipmentImages.add(equiomentImage)
                equipmentImageAdapter.notifyDataSetChanged()
            }
        }
    }


    private fun setUpOrthosisFormRecyclerView(isEditable: Boolean) {
        //for patient data
        if (patientOrthosisList.isNullOrEmpty()) {
            patientOrthosisList.addAll(provideOrthosisTypeData())
        }

        orthosisFormAdapter = OrthosisFormAdapter(
            isEditable,
            this,
            patientOrthosisList,
            layoutInflater,
            object : OrthosisFormAdapter.OrthosisFormClickListener {
                override fun updateMeasurementList(
                    parentPosition: Int,
                    measurementList: List<MeasurementPatientData>,
                    orthosisType: OrthosisType
                ) {

                    patientOrthosisList[parentPosition].orthosis = orthosisType
                    patientOrthosisList[parentPosition].orthoFormId =
                        generateOrthosisFormId(patientOrthosisList, orthosisType.id).toInt()
                    patientOrthosisList[parentPosition].patientOrthosisMeasurements =
                        measurementList
                }

                override fun onMeasurementData(
                    parentPosition: Int,
                    childPosition: Int,
                    measurement: Measurement,
                    measurementName: String,
                    measurementValue: String,
                    measurementUnit: String,
                    isOtherMeasurement: Boolean
                ) {
                    // orthosisTypeList[parentPosition].measurements[childPosition]
                    this@OrthosisActivity.isMeasurementOther = isOtherMeasurement
                    val orthosisMeasurementObject =
                        patientOrthosisList[parentPosition].patientOrthosisMeasurements[childPosition]
                    orthosisMeasurementObject.orthosisMeasurement = measurement
                    if (measurementValue != "") {
                        orthosisMeasurementObject.value = measurementValue.toDouble()
                    } else {
                        orthosisMeasurementObject.value = 0.0
                    }
                    orthosisMeasurementObject.otherMeasurement = measurementName
                    orthosisMeasurementObject.unit = measurementUnit

                }

                override fun setOrthosisEditTextData(
                    position: Int,
                    fieldName: String,
                    fieldValue: String
                ) {
                    when (fieldName) {
                        "amputationDate" -> patientOrthosisList[position].amputationDate =
                            fieldValue

                        "amputationSide" -> patientOrthosisList[position].amputationSide =
                            fieldValue

                        "amputationLevel" -> patientOrthosisList[position].amputationLevel =
                            fieldValue

                        "amputationCause" -> patientOrthosisList[position].amputationCause =
                            fieldValue

                        "fittingStatus" -> patientOrthosisList[position].fit_properly =
                            fieldValue

                        "fittingReason" -> patientOrthosisList[position].fit_properly_reason =
                            fieldValue

                        "otherOrthosis" -> patientOrthosisList[position].otherOrthosis =
                            fieldValue

                        "status" -> {
                            patientOrthosisList[position].status = fieldValue
                            if (patientOrthosisList.any { it.status == "Given" }) {
                                isStatusGiven = true
                                binding.llImages.visibility = View.VISIBLE
                                binding.llVideos.visibility = View.VISIBLE
                            } else {
                                isStatusGiven = false
                                //delete form images and videos

                                val formImageIds = ArrayList<Int>()
                                for (i in imageList) {
                                    if (i.id != 0) {
                                        formImageIds.add(i.id)
                                    }
                                }
                                if (!formImageIds.isNullOrEmpty()) {
                                    orthosisViewModel.deleteFormImagesById(formImageIds)
                                }
                                imageList.clear()
                                imageAdapter.notifyDataSetChanged()

                                val videosId = ArrayList<Int>()
                                for (i in videoList) {
                                    if (i.id != 0) {
                                        videosId.add(i.id)
                                    }
                                }
                                if (!videosId.isNullOrEmpty()) {
                                    orthosisViewModel.deleteFormVideosById(videosId)
                                }
                                videoList.clear()
                                val thumbnail = ThumbnailUtils.createVideoThumbnail(
                                    "videoUrl",
                                    MediaStore.Video.Thumbnails.MINI_KIND
                                )
                                binding.ivFile.setImageBitmap(thumbnail)

                                binding.llImages.visibility = View.GONE
                                binding.llVideos.visibility = View.GONE
                            }
                        }
                    }
                }

                override fun setOrthosisTypeData(position: Int, orthosisType: OrthosisType) {
                    patientOrthosisList[position].orthosis = orthosisType
                    patientOrthosisList[position].orthoFormId =
                        generateOrthosisFormId(patientOrthosisList, orthosisType.id).toInt()
                }

                override fun onDeleteForm(position: Int) {
                    deleteFormDialog(position)
                }

                override fun onImageClick(position: Int) {
                    imagePosition = position

                    val dialog = ImagePickerDialog(
                        this@OrthosisActivity,
                        orthosisImageLauncher,
                        "Upload Orthosis Photo"
                    )
                    dialog.show(supportFragmentManager, "ImagePickerDialog")
                }

                override fun onOrthosisImageRemove(
                    parentPosition: Int,
                    childPosition: Int,
                    image: String
                ) {
                    if (isFormEditable){
                        removeOrthosisImageDialog(parentPosition, childPosition, image)
                    }
                    else{
                        Utility.warningToast(this@OrthosisActivity,"Not Editable")
                    }
                }

            },
            supportFragmentManager,
            orthosisTypeList,
            localUser
        )
        binding.rvOrthosisForm.apply {
            adapter = orthosisFormAdapter
            layoutManager = LinearLayoutManager(this@OrthosisActivity)
        }
    }

    private fun dummyOrthosisForm(): List<OrthosisType> {
        val measurementList1 =
            listOf(
                Measurement(false, "", "Length HEEL TO 1st TOE", 1),
                Measurement(false, "", "FORE FOOT CIRCUMFERENCE at metatarsal level", 2),
                Measurement(
                    false,
                    "",
                    "ANKEL CIRCUMFERENCE 1 inch bellow the head of fibula",
                    3
                )
            )

        val measurementList2 =
            listOf(
                Measurement(false, "", "HEEL TO TOE", 1),
                Measurement(false, "", "FOREFOOT", 2),
                Measurement(false, "", "ANKEL", 3)
            )
        val orthosisTypeForm1 =
            OrthosisType(1, measurementList1, "")
        val orthosisTypeForm2 =
            OrthosisType(2, measurementList2, "DYNAMIC ANKEL FOOT ORTHOSIS(PP)")

        return listOf(orthosisTypeForm1)
    }


    private fun provideOrthosisTypeData(): List<OrthosisPatientData> {
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
                    0,//requested by serve to add 0 - first time 0
                    "",
                    "",
                    "",
                    "",
                    i,
                    "",
                    "",
                    "",
                    examinationDate = getCurrentDate(),
                    "",
                    "",
                    orthoFormId = 0,
                    listOf(),
                    image = "",
                    orthosisImageList = mutableListOf()
                )
            )
        }

        return data
    }

    private fun saveOrthosisPatientForm() {

        if (validateOrthosisForm()) {
            if (patientOrthosisList.size == 0) {
                Utility.errorToast(this@OrthosisActivity, "At least One Orthosis is needed!")
            } else {
                // Utility.successToast(this@OrthosisActivity, "Dummy success")


                //Logic to delete equipment images on prescribed status Pending or Given
                if (binding.etPrescribedStatus.text.isNullOrEmpty()) {
                    if (binding.etPrescribedStatus.text.toString() == "Pending") {
                        for (i in equipmentImages) {
                            orthosisViewModel.deleteEquipmentImage(i.images)
                        }
                    } else {
                        for (i in deleteEquipmentList) {
                            orthosisViewModel.deleteEquipmentImage(i.images)
                        }
                    }
                }

                //Logic to remove form images
                if (!removeImageList.isNullOrEmpty()){
                    orthosisViewModel.deleteFormImages(removeImageList)
                }

                //Logic to remove Orthosis Image
                if (!removeOrthosisImageList.isNullOrEmpty()){
                    orthosisViewModel.deleteOrthosisImages(removeOrthosisImageList)
                }

                var patientOrthosisForm: OrthosisPatientForm? = null
                if (screen == "Camp_List") {
                    patientOrthosisForm = OrthosisPatientForm(
                        id = localPatientId.toInt(),
                        patientName = localPatientData.patientName,
                        patientId = localPatientData.patientId,
                        patientGender = localPatientData.patientGender,
                        patientAge = localPatientData.patientAge,
                        campId = localPatientData.campId,
                        location = localPatientData.location,
                        ageUnit = "",
                        regNo = localPatientData.regNo,
                        examinationDate = getCurrentDate(),//orthosis date for form only not orthosis type date(examination date)
                        orthosisList = patientOrthosisList,
                        orthosisDate = getCurrentDate(),
                        tempPatientId = localPatientData.tempPatientId,
                        patientContactNo = "123456789",
                        campName = localPatientData.location,
                        patientAgeYears = localPatientData.patientAgeYears,
                        patientHeightCm = binding.etPatientHeight.text?.toString() ?: "",
                        patientWeightKg = binding.etPatientWeight.text?.toString() ?: "",
                        diagnosis = if (binding.etDiagnosisType.text.toString() == "Other") binding.etOtherDiagnosis.text.toString() else binding.etDiagnosisType.text.toString(),
                        diagnosisId = selectedDiagnosis?.id ?: 0,
                        equipmentSupport = selectedEquipment?.equipment_support ?: "",
                        equipmentCategory = selectedEquipment?.equipment_category ?: "",
                        equipmentId = selectedEquipment?.id ?: 0,
                        equipmentStatus = binding.etPrescribedStatus.text?.toString() ?: "",
                        equipmentStatusNotes = ""
                    )
                } else if (screen == "QrCode"){
                    patientOrthosisForm = OrthosisPatientForm(
                        id = localPatientId.toInt(),
                        patientName = "${patientData.patientFname} ${patientData.patientLname}",
                        patientId = patientData.patientId,
                        patientGender = patientData.patientGen,
                        patientAge = patientData.patientAge,
                        campId = patientData.camp_id,
                        location = patientData.location,
                        ageUnit = "",
                        regNo = patientData.RegNo,
                        examinationDate = getCurrentDate(),//orthosis date for form only not orthosis type date(examination date)
                        orthosisList = patientOrthosisList,
                        orthosisDate = getCurrentDate(),
                        tempPatientId = patientData.patientId.toString(),
                        patientContactNo = "123456789",
                        campName = patientData.location,
                        patientAgeYears = patientData.patientAge.toString(),
                        patientHeightCm = binding.etPatientHeight.text?.toString() ?: "",
                        patientWeightKg = binding.etPatientWeight.text?.toString() ?: "",
                        diagnosis = if (binding.etDiagnosisType.text.toString() == "Other") binding.etOtherDiagnosis.text.toString() else binding.etDiagnosisType.text.toString(),
                        diagnosisId = selectedDiagnosis?.id ?: 0,
                        equipmentSupport = selectedEquipment?.equipment_support ?: "",
                        equipmentCategory = selectedEquipment?.equipment_category ?: "",
                        equipmentId = selectedEquipment?.id ?: 0,
                        equipmentStatus = binding.etPrescribedStatus.text?.toString() ?: "",
                        equipmentStatusNotes = ""
                    )
                }
                else{
                    patientOrthosisForm = OrthosisPatientForm(
                        id = localPatientId.toInt(),
                        patientName = "${patientData.patientFname} ${patientData.patientLname}",
                        patientId = patientData.patientId,
                        patientGender = patientData.patientGen,
                        patientAge = patientData.patientAge,
                        campId = patientData.camp_id,
                        location = patientData.location,
                        ageUnit = "",
                        regNo = patientData.RegNo,
                        examinationDate = getCurrentDate(),//orthosis date for form only not orthosis type date(examination date)
                        orthosisList = patientOrthosisList,
                        orthosisDate = getCurrentDate(),
                        tempPatientId = patientData.patientId.toString(),
                        patientContactNo = "123456789",
                        campName = patientData.location,
                        patientAgeYears = patientData.patientAge.toString(),
                        patientHeightCm = binding.etPatientHeight.text?.toString() ?: "",
                        patientWeightKg = binding.etPatientWeight.text?.toString() ?: "",
                        diagnosis = if (binding.etDiagnosisType.text.toString() == "Other") binding.etOtherDiagnosis.text.toString() else binding.etDiagnosisType.text.toString(),
                        diagnosisId = selectedDiagnosis?.id ?: 0,
                        equipmentSupport = selectedEquipment?.equipment_support ?: "",
                        equipmentCategory = selectedEquipment?.equipment_category ?: "",
                        equipmentId = selectedEquipment?.id ?: 0,
                        equipmentStatus = binding.etPrescribedStatus.text?.toString() ?: "",
                        equipmentStatusNotes = ""
                    )
                }
                orthosisViewModel.insertOrthosisPatientForm(patientOrthosisForm)
            }
        }
    }

    private fun getPatientData() {
        val decodedText = sessionManager.getPatientData()
        val gson = Gson()
        patientData = gson.fromJson(decodedText, PatientData::class.java)

        campId = patientData.camp_id.toString()
        tempPatientId = patientData.patientId.toString()
        // Access the values
        val patientFname = patientData.patientFname
        val patientLname = patientData.patientLname
        val patientAge = patientData.patientAge
        val patientID = patientData.patientId
        val patientGender = patientData.patientGen
        val camp = patientData.location
        val ageUnit = patientData.AgeUnit


        binding.tvPatientDetails.text = "Patient Details - ${patientID}"
        binding.tvCampDetails.text = "Camp : ${camp}"
        binding.tvPatientName.text = "Name : ${patientFname} ${patientLname}"
        binding.tvPatientGender.text = "Gender : ${patientGender}"
        binding.tvPatientAge.text = "Age : ${patientAge}"
    }

    private fun validateOrthosisForm(): Boolean {
        var isValid = true
        //validates Diagnosis Type
        if (binding.etDiagnosisType.text.isNullOrEmpty()) {
            Utility.errorToast(this@OrthosisActivity, "Select Diagnosis")
            isValid = false
            return isValid
        }

        //validates Manual Enter Diagnosis if selected Diagnosis is Other
        if (isSelectedOtherDiagnosis) {
            if (binding.etOtherDiagnosis.text.isNullOrEmpty()) {
                Utility.errorToast(this@OrthosisActivity, "Enter Diagnosis")
                isValid = false
                return isValid
            }
        }

        if (!binding.etPrescribed.text.isNullOrEmpty()) {
            if (!binding.etPrescribedStatus.text.isNullOrEmpty()) {
                if (binding.etPrescribedStatus.text.toString() == "Given") {
                    if (equipmentImages.isNullOrEmpty()) {
                        Utility.warningToast(
                            this@OrthosisActivity,
                            "Please Upload Equipment Images"
                        )
                        isValid = false
                        return isValid
                    }
                }
            }
        }

        orthosisLoop@ for ((index, data) in patientOrthosisList.withIndex()) {

            //validates Orthosis Type
            if (data.orthosis.name.isNullOrEmpty()) {
                Utility.errorToast(this, "(${index + 1}) Select Orthosis Type")
                isValid = false
                break@orthosisLoop
            }

            if (isMeasurementOther) {
                if (data.otherOrthosis.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Enter Orthosis Type")
                    isValid = false
                    break@orthosisLoop
                }
            }

            //validates Orthosis Status
            if (data.status.isNullOrEmpty()) {
                Utility.errorToast(this, "(${index + 1}) Select Orthosis Status")
                isValid = false
                break@orthosisLoop
            }

            if (isAmputee) {
                //validates Amputation Date
                if (data.amputationDate.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Select Amputation Date")
                    isValid = false
                    break@orthosisLoop
                }

                //Validates Amputation Side
                if (data.amputationSide.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Select Amputation Side")
                    isValid = false
                    break@orthosisLoop
                }

                //Validates Amputation Level
                if (data.amputationLevel.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Enter Amputation Level")
                    isValid = false
                    break@orthosisLoop
                }

                //Validates Amputation Cause
                if (data.amputationCause.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Enter Amputation Cause")
                    isValid = false
                    break@orthosisLoop
                }
            }

            if (data.status == "Given") {
                if (data.fit_properly.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Select Fitting Status")
                    isValid = false
                    break@orthosisLoop
                }
            }


            //Validates Fitting Reason
            if (data.status == "Given") {
                if (data.fit_properly_reason.isNullOrEmpty()) {
                    Utility.errorToast(this, "(${index + 1}) Enter Fitting Feedback")
                    isValid = false
                    break@orthosisLoop
                }
            }

            //Validates Orthosis Image
            if (data.orthosisImageList.isNullOrEmpty()) {
                Utility.errorToast(this, "(${index + 1}) Click an Image!")
                isValid = false
                break@orthosisLoop
            }

            //validates measurement fields
            measurementLoop@ for (measurement in data.patientOrthosisMeasurements) {
                if (isMeasurementOther) {
                    if (measurement.value == 0.0) {
                        Utility.errorToast(
                            this@OrthosisActivity,
                            "(${index + 1}) Measurement is incomplete"
                        )
                        isValid = false
                        break@measurementLoop
                    } else if (measurement.unit.isNullOrEmpty()) {
                        Utility.errorToast(
                            this@OrthosisActivity,
                            "(${index + 1}) Measurement is incomplete"
                        )
                        isValid = false
                        break@measurementLoop
                    } else if (measurement.otherMeasurement.isNullOrEmpty()) {
                        Utility.errorToast(
                            this@OrthosisActivity,
                            "(${index + 1}) Measurement is incomplete"
                        )
                        isValid = false
                        break@measurementLoop
                    }
                }
                if (measurement.value == 0.0) {
                    Utility.errorToast(
                        this@OrthosisActivity,
                        "(${index + 1}) Enter ${measurement.orthosisMeasurement.fieldName}"
                    )
                    isValid = false
                    break@measurementLoop
                } else if (measurement.unit.isNullOrEmpty()) {
                    Utility.errorToast(
                        this@OrthosisActivity,
                        "(${index + 1}) Select unit"
                    )
                    isValid = false
                    break@measurementLoop
                }
            }
        }


//        Image validation if orthosis status == given
        if (isStatusGiven) {
            //if image is empty show error to
            if (imageList.isNullOrEmpty()) {
                Utility.errorToast(this@OrthosisActivity, "Select Images")
                isValid = false
            }

        }

        if (isStatusGiven) {
            //if video is empty show error to
            if (videoList.isNullOrEmpty()) {
                Utility.errorToast(this@OrthosisActivity, "Select Videos")
                isValid = false
            }

        }

        return isValid
    }

    private fun testOrthosisData() {
        orthosisViewModel.getOrthosisPatientForm()
        orthosisViewModel.orthosisPatientFormList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val dataList = it.data
                            dataList.get(0).ageUnit
                        }

                    } catch (e: Exception) {
                        Utility.errorToast(this@OrthosisActivity, e.message.toString())

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@OrthosisActivity, "Unexpected error")

                }
            }
        }
    }

    private fun deleteFormDialog(position: Int) {
        val layoutResId = R.layout.orthosis_delete_dialog
        val alertCustomDialog = layoutInflater.inflate(layoutResId, null)
        val messageDialog = AlertDialog.Builder(this)
        messageDialog.setView(alertCustomDialog)


        val tvCancel: TextView = alertCustomDialog.findViewById(R.id.tvCancel)
        val tvLogout: TextView = alertCustomDialog.findViewById(R.id.tvDelete)

        val finalDialog = messageDialog.create()

        tvCancel.setOnClickListener {
            finalDialog.dismiss()
        }
        tvLogout.setOnClickListener {
            finalDialog.dismiss()
            //perform operation
            patientOrthosisList.removeAt(position)
            orthosisFormAdapter.notifyDataSetChanged()
        }

        finalDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        finalDialog.show()
    }

    private fun removeImageDialog(position: Int) {
        val layoutResId = R.layout.image_remove_dialog
        val alertCustomDialog = layoutInflater.inflate(layoutResId, null)
        val messageDialog = AlertDialog.Builder(this)
        messageDialog.setView(alertCustomDialog)


        val tvCancel: TextView = alertCustomDialog.findViewById(R.id.tvCancel)
        val tvYes: TextView = alertCustomDialog.findViewById(R.id.tvDelete)

        val finalDialog = messageDialog.create()

        tvCancel.setOnClickListener {
            finalDialog.dismiss()
        }
        tvYes.setOnClickListener {
            finalDialog.dismiss()
            //perform operation
            val imageToRemove = imageList[position]
            removeImageList.add(imageToRemove)
            imageList.removeAt(position)
            imageAdapter.notifyDataSetChanged()
        }

        finalDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        finalDialog.show()
    }

    private fun saveOrthosisImage(
        patientOrthosisList: ArrayList<OrthosisPatientData>,
        formId: String
    ) {
        val imageList = ArrayList<OrthosisImages>()

        //new
        patientOrthosisList[imagePosition].orthosisImageList.forEach { orthosis ->
            if (!orthosisLocalImageList.any { it.images == orthosis.images }) {
                imageList.add(
                    OrthosisImages(
                        images = orthosis.images,
                        temp_patient_id = tempPatientId,
                        camp_id = campId,
                        patient_id = formId,//for getting unique orthosis images for preventing from duplicate entries in local db
                        orthosis_id = orthosis.orthosis_id,
                        orthosisFormId = 1,
                        amputation_side = orthosis.amputation_side
                    )
                )
            }

        }
        orthosisViewModel.insertOrthosisImageList(imageList)
    }


    private fun setUpImagesRecyclerView() {
        imageAdapter =
            ImageAdapter(
                this@OrthosisActivity,
                imageList,
                object : ImageAdapter.ImageAdapterEvent {
                    override fun onImageClick(position: Int) {
                        //
                    }

                    override fun onImageRemove(position: Int) {
                        if (isFormEditable){
                            removeImageDialog(position)
                        }
                        else{
                            Utility.warningToast(this@OrthosisActivity,"Cannot Edit")
                        }
                    }

                })

        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(this@OrthosisActivity, 4)
        }
    }

    private fun setUpEquipmentImagesRecyclerView() {
        equipmentImageAdapter = EquipmentImageAdapter(
            this@OrthosisActivity,
            equipmentImages,
            object : EquipmentImageAdapter.EquipmnetImageAdapterEvent {
                override fun onImageClick(position: Int) {
                    //
                }

                override fun onImageRemove(position: Int) {
                    if (isFormEditable){
                        removeEquipmentImageDialog(position)
                    }
                   else{
                       Utility.warningToast(this@OrthosisActivity,"Not Editable")
                    }
                }

            }
        )
        binding.rvEquipmentImages.apply {
            adapter = equipmentImageAdapter
            layoutManager = GridLayoutManager(this@OrthosisActivity, 4)
        }
    }


    private fun saveFormImages(imageList: ArrayList<FormImages>, formId: Int) {
        val formImageList = ArrayList<FormImages>()
        if (!imageList.isNullOrEmpty()) {
            if (isFormImageLocal) {
                orthosisViewModel.insertFormImageList(imageList)
            } else {
                val filteredImages = imageList.filter { it.id == 0 }
                for (i in filteredImages) {
                    formImageList.add(
                        FormImages(
                            images = i.images,
                            temp_patient_id = tempPatientId,
                            formId = formId,
                            camp_id = campId
                        )
                    )
                }
                orthosisViewModel.insertFormImageList(formImageList)
            }
        }

    }

    private fun saveFormVideos(videos: List<FormVideos>, formId: Int) {
        val formVideoList = ArrayList<FormVideos>()
        if (!isFormVideoLocal) {
            for (i in videos) {
                formVideoList.add(
                    FormVideos(
                        video = i.video,
                        temp_patient_id = tempPatientId,
                        camp_id = campId,
                        patient_id = formId.toString(),
                        formId = formId
                    )
                )
            }
            //add a viewmodel and save in local db all the images
            orthosisViewModel.insertFormVideoList(formVideoList)
        } else {
            orthosisViewModel.insertFormVideoList(videoList)
        }

    }

    private fun getLocalUserData() {
        orthosisViewModel.getLocalUserList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val videoUrl = data?.getStringExtra(VIDEO_URL)

            if (videoUrl != "") {
                val videosId = ArrayList<Int>()
                for (i in videoList) {
                    videosId.add(i.id)
                }
                isFormVideoLocal = false
                orthosisViewModel.deleteFormVideosById(videosId)
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
            } else {
                binding.cvFile.visibility = View.GONE
            }

        }
    }

    companion object {
        const val REQUEST_CODE = 1001
    }


    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getOrthosisImagesByFormId(formId: String) {
        orthosisViewModel.getOrthosisImageByFormId(formId)
    }

    private fun inflateDiagnosisBottomSheet() {
        val items = ArrayList<SearchAbleList>()
        // diagnosisList.add(DiagnosisType(0,"Other"))
        for (i in diagnosisList.indices) {
            items.add(SearchAbleList(i, diagnosisList[i].name))
        }
        val dialog = SingleSelectBottomSheetDialogFragment(
            this@OrthosisActivity,
            items,
            "Select Diagnosis",
            0,
            true
        ) { selectedValue ->
            val diagnosisType = diagnosisList[selectedValue.position]
            selectedDiagnosis = diagnosisType
            binding.etDiagnosisType.setText(diagnosisType.name)
            if (diagnosisType.name == "Other") {
                isSelectedOtherDiagnosis = true

                binding.etlPrescribed.visibility = View.GONE
                binding.etlPrescribedStatus.visibility = View.GONE
                binding.etPrescribed.setText("")
                binding.etPrescribedStatus.setText("")

                binding.etlOtherDiagnosis.visibility = View.VISIBLE
            } else if (diagnosisType.name == "Cerebral Palsy") {

                binding.etlOtherDiagnosis.visibility = View.GONE
                binding.etOtherDiagnosis.setText("")


                binding.etlPrescribed.visibility = View.VISIBLE
            } else {
                binding.etlPrescribed.visibility = View.GONE
                binding.etlPrescribedStatus.visibility = View.GONE
                binding.etPrescribed.setText("")
                binding.etPrescribedStatus.setText("")

                isSelectedOtherDiagnosis = false
                binding.etlOtherDiagnosis.visibility = View.GONE
                binding.etOtherDiagnosis.setText("")
                if (diagnosisType.name.contains("Amputee", true)) {
                    isAmputee = true
                    orthosisFormAdapter.onDiagnosisClick(true)
                } else {
                    isAmputee = false
                    orthosisFormAdapter.onDiagnosisClick(false)
                }
            }


        }

        dialog.show(supportFragmentManager, "SingleSelectBottomSheetDialogFragment")
    }

    private fun inflateEquipmentBottomSheet() {
        val items = ArrayList<SearchAbleList>()
        // diagnosisList.add(DiagnosisType(0,"Other"))
        for (i in equipmentList.indices) {
            items.add(SearchAbleList(i, equipmentList[i].name))
        }
        val dialog = SingleSelectBottomSheetDialogFragment(
            this@OrthosisActivity,
            items,
            "Select Equipments",
            0,
            true
        ) { selectedValue ->
            val equipmentType = equipmentList[selectedValue.position]
            selectedEquipment = equipmentType
            binding.etPrescribed.setText(equipmentType.name)
            if (!binding.etPrescribed.text.isNullOrEmpty()) {
                isPrescribedStatusNeeded = true
                binding.cvEquipmentPics.visibility = View.VISIBLE
                binding.etlPrescribedStatus.visibility = View.VISIBLE
            }


        }

        dialog.show(supportFragmentManager, "SingleSelectBottomSheetDialogFragment")
    }

    private fun setAsteriskColor(etlField: TextInputLayout, hintText: String) {
        val spannableString = SpannableString(hintText)

        val redColor =
            ForegroundColorSpan(this@OrthosisActivity.resources.getColor(R.color.dark_red))
        spannableString.setSpan(
            redColor,
            hintText.length - 1,
            hintText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        etlField.hint = spannableString

    }

    private fun removeOrthosisImageDialog(parentPosition: Int, position: Int, image: String) {
        val layoutResId = R.layout.image_remove_dialog
        val alertCustomDialog = layoutInflater.inflate(layoutResId, null)
        val messageDialog = AlertDialog.Builder(this)
        messageDialog.setView(alertCustomDialog)


        val tvCancel: TextView = alertCustomDialog.findViewById(R.id.tvCancel)
        val tvYes: TextView = alertCustomDialog.findViewById(R.id.tvDelete)

        val finalDialog = messageDialog.create()

        tvCancel.setOnClickListener {
            finalDialog.dismiss()
        }
        tvYes.setOnClickListener {
            finalDialog.dismiss()
            //perform operation
            val itemToRemove = patientOrthosisList[parentPosition].orthosisImageList.find { it.images == image }
            if (itemToRemove != null){
                removeOrthosisImageList.add(itemToRemove)
            }
            patientOrthosisList[parentPosition].orthosisImageList.removeIf { it.images == image }
            orthosisFormAdapter.notifyDataSetChanged()
        }

        finalDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        finalDialog.show()
    }

    private fun removeEquipmentImageDialog(position: Int) {
        val layoutResId = R.layout.image_remove_dialog
        val alertCustomDialog = layoutInflater.inflate(layoutResId, null)
        val messageDialog = AlertDialog.Builder(this)
        messageDialog.setView(alertCustomDialog)


        val tvCancel: TextView = alertCustomDialog.findViewById(R.id.tvCancel)
        val tvYes: TextView = alertCustomDialog.findViewById(R.id.tvDelete)

        val finalDialog = messageDialog.create()

        tvCancel.setOnClickListener {
            finalDialog.dismiss()
        }
        tvYes.setOnClickListener {
            finalDialog.dismiss()
            //perform operation

            if (!equipmentImages[position].patient_id.isNullOrEmpty()) {
                deleteEquipmentList.add(equipmentImages[position])
            }
            equipmentImages.removeAt(position)
            equipmentImageAdapter.notifyDataSetChanged()
        }

        finalDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        finalDialog.show()
    }

    private fun saveEquipmentImages(formId: String) {
        val imageList = ArrayList<EquipmentImage>()
        for (i in equipmentImages) {
            i.patient_id = formId
        }
        orthosisViewModel.insertEquipmentImageList(equipmentImages)
    }

    private fun generateOrthosisFormId(
        patientOrthosisList: ArrayList<OrthosisPatientData>,
        orthosisId: Int
    ): String {
        val orthoFormId = mutableMapOf<Int, Int>()

        val solutions = mutableListOf<String>()
        var specificSolution = ""
        for (i in patientOrthosisList) {
            val formCount = orthoFormId.getOrDefault(i.orthosis.id, 0) + 1
            orthoFormId[i.orthosis.id] = formCount
            if (i.orthosis.id == orthosisId) {
                specificSolution = "${i.orthosis.id}${formCount}"
            }
        }

        return specificSolution
    }



    private fun onFormEditClick() {
        isFormEditable = true
        binding.etPatientWeight.isFocusable = true
        binding.etPatientHeight.isFocusable = true

        binding.etPatientWeight.setOnClickListener {

            if (!isFormEditable) {
                Utility.warningToast(this@OrthosisActivity, "Not Editable")
            }


        }
        binding.etPatientHeight.setOnClickListener {
            if (!isFormEditable) {
                Utility.warningToast(this@OrthosisActivity, "Not Editable")
            }
        }
        binding.btnEditPatientOrthosis.visibility = View.GONE
        binding.btnSavePatientOrthosis.visibility = View.VISIBLE
        binding.tvAddMoreOrthosisType.visibility = View.VISIBLE
        binding.tvSaveOrthosis.text = "Update"
        setUpOrthosisFormRecyclerView(true)

    }
}