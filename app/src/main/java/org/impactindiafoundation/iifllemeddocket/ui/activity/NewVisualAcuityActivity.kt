package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SearchAbleList
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SingleSelectBottomSheetDialogFragment
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.VISUAL_ACUITY_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.PatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.VisualAcuityViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityNewVisualAcuityBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewVisualAcuityActivity : BaseActivity() {

    private lateinit var binding: ActivityNewVisualAcuityBinding
    private val visualAcuityViewModel: VisualAcuityViewModel by viewModels()
    private val patientReportVM: PatientReportViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    private var intentFormId = 0
    private var patientReportFormId = 0
    private var localFormId = 0
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewVisualAcuityBinding.inflate(layoutInflater)
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

        initUi()
        initObserver()
        setPatientData()
    }

    private fun initUi() {
        intentFormId = intent.getIntExtra("localFormId",0)
        campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId",0)
        visualAcuityViewModel.getVisualAcuityFormById(intentFormId)
        sessionManager = SessionManager(this)
        binding.visualAcuityToolBar.toolbar.title = "Visual Acuity"
        binding.btnSubmitForm.setOnClickListener {
            if (validateForm()) {
                saveForm()
            }
        }

        val rightUnitAdapter = ArrayAdapter(
                this@NewVisualAcuityActivity,
                android.R.layout.simple_dropdown_item_1line,
                provideDistantUnit()
            )
        binding.etRDVUnit.setAdapter(rightUnitAdapter)
        binding.etRDVUnit.setOnClickListener {
            if (canEdit){
                binding.etRDVUnit.showDropDown()
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        val leftUnitAdapter = ArrayAdapter(
                this@NewVisualAcuityActivity,
                android.R.layout.simple_dropdown_item_1line,
                provideDistantUnit()
            )
        binding.etLDVUnit.setAdapter(leftUnitAdapter)
        binding.etLDVUnit.setOnClickListener {
            if (canEdit){
                binding.etLDVUnit.showDropDown()
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        binding.etRDVSphere.setOnClickListener {
            if (canEdit){
                if (!binding.etRDVUnit.text.isNullOrEmpty()) {
                    val unitData = binding.etRDVUnit.text.toString()
                    if (unitData == "Distant Unit") {
                        Utility.warningToast(this@NewVisualAcuityActivity, "Please Choose Unit")
                    } else if (unitData == "Meters") {
                        inflateBottomSheet(
                            "Select Visual Activity",
                            provideDistantData(),
                            binding.etRDVSphere
                        )
                    } else if (unitData == "logMAR") {
                        inflateBottomSheet(
                            "Select Visual Activity",
                            provideLogMarData(),
                            binding.etRDVSphere
                        )
                    }
                } else {
                    Utility.warningToast(this@NewVisualAcuityActivity, "Please Choose Unit")
                }
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        binding.etLDVSphere.setOnClickListener {
            if (canEdit){
                if (!binding.etLDVUnit.text.isNullOrEmpty()) {
                    val unitData = binding.etLDVUnit.text.toString()
                    if (unitData == "Distant Unit") {
                        Utility.warningToast(this@NewVisualAcuityActivity, "Please Choose Unit")
                    }
                    if (unitData == "Meters") {
                        inflateBottomSheet(
                            "Select",
                            provideDistantData(),
                            binding.etLDVSphere
                        )
                    } else if (unitData == "logMAR") {
                        inflateBottomSheet(
                            "Select",
                            provideLogMarData(),
                            binding.etLDVSphere
                        )
                    }
                } else {
                    Utility.warningToast(this@NewVisualAcuityActivity, "Please Choose Unit")
                }
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        binding.etRNVSphere.setOnClickListener {
            if (canEdit){
                inflateBottomSheet("Select Sphere", provideSphereData(), binding.etRNVSphere)
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        binding.etLNVSphere.setOnClickListener {
            if (canEdit){
                inflateBottomSheet("Select Sphere", provideSphereData(), binding.etLNVSphere)
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        binding.etLeftPinHole.setOnClickListener {
            if (canEdit){
                inflateBottomSheet("Select PinHole", providePinHoleData(), binding.etLeftPinHole)
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        binding.etRightPinHole.setOnClickListener {
            if (canEdit){
                inflateBottomSheet("Select PinHole", providePinHoleData(), binding.etRightPinHole)
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        val rightPHUnitAdapter = ArrayAdapter(
                this@NewVisualAcuityActivity,
                android.R.layout.simple_dropdown_item_1line,
                providePinHoleDataUnit()
            )
        binding.etRightPinHoleUnit.setAdapter(rightPHUnitAdapter)
        binding.etRightPinHoleUnit.setOnClickListener {
            if (canEdit){
                if (!binding.etRightPinHole.text.isNullOrEmpty()) {
                    if ( binding.etRightPinHole.text.toString() == "PinHole"){
                        Utility.warningToast(this@NewVisualAcuityActivity, "Select Pin Hole")
                    } else if (binding.etRightPinHole.text.toString() == "Not Improved") {
                        Utility.warningToast(this@NewVisualAcuityActivity, "Pin Hole is Not Improved")
                    } else {
                        binding.etRightPinHoleUnit.showDropDown()
                    }
                } else {
                    Utility.warningToast(this@NewVisualAcuityActivity, "Select Pin Hole")

                }
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        val leftPHUnitAdapter = ArrayAdapter(
                this@NewVisualAcuityActivity,
                android.R.layout.simple_dropdown_item_1line,
                providePinHoleDataUnit()
            )
        binding.etLeftPinHoleUnit.setAdapter(leftPHUnitAdapter)
        binding.etLeftPinHoleUnit.setOnClickListener {
            if (canEdit){
                if (!binding.etLeftPinHole.text.isNullOrEmpty()) {
                    if ( binding.etLeftPinHole.text.toString() == "PinHole"){
                        Utility.warningToast(this@NewVisualAcuityActivity, "Select Pin Hole")
                    } else if (binding.etLeftPinHole.text.toString() == "Not Improved") {
                        Utility.warningToast(this@NewVisualAcuityActivity, "Pin Hole is Not Improved")
                    } else {
                        binding.etLeftPinHoleUnit.showDropDown()
                    }
                } else {
                    Utility.warningToast(this@NewVisualAcuityActivity, "Select Pin Hole")

                }
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        binding.etRPHValue.setOnClickListener {
            if (canEdit) {
                if (!binding.etRightPinHoleUnit.text.isNullOrEmpty()) {
                    when (binding.etRightPinHoleUnit.text.toString()) {
                        "PinHole Unit" -> {
                            Utility.warningToast(this@NewVisualAcuityActivity, "Please Choose Unit")
                        }
                        "Meters" -> {
                            inflateBottomSheet(
                                "Select",
                                provideDistantData(),
                                binding.etRPHValue
                            )
                        }
                        "logMAR" -> {
                            inflateBottomSheet(
                                "Select",
                                provideLogMarData(),
                                binding.etRPHValue
                            )
                        }
                    }
                } else {
                    Utility.warningToast(this@NewVisualAcuityActivity, "Please Choose Unit")
                }
            }
            else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        binding.etLPHValue.setOnClickListener {
            if (canEdit){
                if (!binding.etLeftPinHoleUnit.text.isNullOrEmpty()) {
                    when (binding.etLeftPinHoleUnit.text.toString()) {
                        "PinHole Unit" -> {
                            Utility.warningToast(this@NewVisualAcuityActivity, "Please Choose Unit")
                        }
                        "Meters" -> {
                            inflateBottomSheet(
                                "Select",
                                provideDistantData(),
                                binding.etLPHValue
                            )
                        }
                        "logMAR" -> {
                            inflateBottomSheet(
                                "Select",
                                provideLogMarData(),
                                binding.etLPHValue
                            )
                        }
                    }
                } else {
                    Utility.warningToast(this@NewVisualAcuityActivity, "Please Choose Unit")
                }
            }
            else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        binding.etRightPinHole.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                if (text.toString() == "Not Improved") {
                    binding.etRightPinHoleUnit.setText("")
                    binding.etRPHValue.setText("")
                }
            }
        }

        binding.etLeftPinHole.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                if (text.toString() == "Not Improved") {
                    binding.etLeftPinHoleUnit.setText("")
                    binding.etLPHValue.setText("")
                }
            }
        }

        binding.btnEdit.setOnClickListener {
            if (!canEdit){
                onFormEditClick()
            }
        }
    }

    private fun initObserver() {
        visualAcuityViewModel.insertVisualAcuityResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        val patientReport = PatientReport(
                            id = patientReportFormId,
                            formType = VISUAL_ACUITY_FORM,
                            formId = it.data?.toInt() ?: 0,
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
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@NewVisualAcuityActivity, "Unexpected error")
                }
            }
        }

        patientReportVM.insertPatientReportResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        Utility.successToast(this@NewVisualAcuityActivity, "Form Submitted Successfully")
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@NewVisualAcuityActivity, "Unexpected error")
                }
            }
        }

        visualAcuityViewModel.visualAcuityFormListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()){
                            localFormId = it.data[0]._id
                            isFormLocal = true
                            val visualAcuity = it.data[0]
                            canEdit = false
                            allowClickableEditText(canEdit)
                            binding.btnEdit.visibility = View.VISIBLE
                            binding.btnSubmitForm.visibility = View.GONE
                            setUpFormData(visualAcuity)
                        } else{
                            localFormId = 0
                            isFormLocal = false
                            canEdit = true
                            allowClickableEditText(canEdit)
                            binding.btnEdit.visibility = View.GONE
                            binding.btnSubmitForm.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@NewVisualAcuityActivity, "Unexpected error")
                }
            }
        }
    }

    private fun saveForm() {
        val (patientId, campIdLocal, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
        val currentDate = ConstantsApp.getCurrentDate()
        val vaPinholeLeft = binding.etLeftPinHole.text?.toString().takeIf { it != "PinHole" } ?: ""
        val vaPinholeRight = binding.etRightPinHole.text?.toString().takeIf { it != "PinHole" } ?: ""
        val vaPinholeImproveLeft = binding.etLPHValue.text?.toString().takeIf { it != "Value" } ?: ""
        val vaPinholeImproveRight = binding.etRPHValue.text?.toString().takeIf { it != "Value" } ?: ""
        val vaPinholeUnitLeft = binding.etLeftPinHoleUnit.text?.toString().takeIf { it != "PinHole Unit" } ?: ""
        val vaPinholeUnitRight = binding.etRightPinHoleUnit.text?.toString().takeIf { it != "PinHole Unit" } ?: ""
        val vaAddiDetailsLeft = binding.etLeftNotes.text?.toString() ?: ""
        val vaAddiDetailsRight = binding.etRightNotes.text?.toString() ?: ""
        val vaDistantLeft = binding.etLDVSphere.text?.toString().takeIf { it != "Value" } ?: ""
        val vaDistantRight = binding.etRDVSphere.text?.toString().takeIf { it != "Value" } ?: ""
        val vaDistantUnitLeft = binding.etLDVUnit.text?.toString().takeIf { it != "Distant Unit" } ?: ""
        val vaDistantUnitRight = binding.etRDVUnit.text?.toString().takeIf { it != "Distant Unit" } ?: ""
        val vaNearLeft = binding.etRNVSphere.text?.toString() ?: ""
        val vaNearRight = binding.etLNVSphere.text?.toString() ?: ""

        val visualAcuity = VisualAcuity(
            _id = localFormId,
            camp_id = campIdLocal!!,
            createdDate = currentDate,
            patient_id = patientId!!,
            userId = userId ?: "",
            va_addi_details_left = vaAddiDetailsLeft,
            va_addi_details_right = vaAddiDetailsRight,
            va_distant_vision_left = vaDistantLeft,
            va_distant_vision_right = vaDistantRight,
            va_distant_vision_unit_left = vaDistantUnitLeft,
            va_distant_vision_unit_right = vaDistantUnitRight,
            va_near_vision_left = vaNearLeft,
            va_near_vision_right = vaNearRight,
            va_pinhole_improve_left = vaPinholeImproveLeft,
            va_pinhole_improve_right = vaPinholeImproveRight,
            va_pinhole_improve_unit_left = vaPinholeUnitLeft,
            va_pinhole_improve_unit_right = vaPinholeUnitRight,
            va_pinhole_left = vaPinholeLeft,
            va_pinhole_right = vaPinholeRight
        )
        Log.d("pawan", "✅ Saving VisualAcuity with date=${visualAcuity.createdDate}")
        visualAcuityViewModel.insertVisualAcuityForm(visualAcuity)
    }

    private fun setUpFormData(form:VisualAcuity){
        binding.etRNVSphere.setText(form.va_near_vision_right)
        binding.etLNVSphere.setText(form.va_near_vision_left)
        binding.etRDVUnit.setText(form.va_distant_vision_unit_right)
        binding.etLDVUnit.setText(form.va_distant_vision_unit_left)
        binding.etRDVSphere.setText(form.va_distant_vision_right)
        binding.etLDVSphere.setText(form.va_distant_vision_left)
        binding.etRightPinHole.setText(form.va_pinhole_right)
        binding.etLeftPinHole.setText(form.va_pinhole_left)
        binding.etRightPinHoleUnit.setText(form.va_pinhole_improve_unit_right)
        binding.etLeftPinHoleUnit.setText(form.va_pinhole_improve_unit_left)
        binding.etRPHValue.setText(form.va_pinhole_improve_right)
        binding.etLPHValue.setText(form.va_pinhole_improve_left)
        binding.etRightNotes.setText(form.va_addi_details_right)
        binding.etLeftNotes.setText(form.va_addi_details_left)
    }

    private fun onFormEditClick() {
        canEdit = true
        allowClickableEditText(canEdit)
        binding.btnSubmitForm.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE
        binding.tvSubmit.text = "Update"

        val rightUnitAdapter = ArrayAdapter(
                this@NewVisualAcuityActivity,
                android.R.layout.simple_dropdown_item_1line,
                provideDistantUnit()
            )
        binding.etRDVUnit.setAdapter(rightUnitAdapter)
        binding.etRDVUnit.setOnClickListener {
            if (canEdit){
                binding.etRDVUnit.showDropDown()
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        val leftUnitAdapter = ArrayAdapter(
                this@NewVisualAcuityActivity,
                android.R.layout.simple_dropdown_item_1line,
                provideDistantUnit()
            )
        binding.etLDVUnit.setAdapter(leftUnitAdapter)
        binding.etLDVUnit.setOnClickListener {
            if (canEdit){
                binding.etLDVUnit.showDropDown()
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        val rightPHUnitAdapter = ArrayAdapter(
                this@NewVisualAcuityActivity,
                android.R.layout.simple_dropdown_item_1line,
                provideDistantUnit()
            )
        binding.etRightPinHoleUnit.setAdapter(rightPHUnitAdapter)
        binding.etRightPinHoleUnit.setOnClickListener {
            if (canEdit){
                if (!binding.etRightPinHole.text.isNullOrEmpty()) {
                    if (binding.etRightPinHole.text.toString() == "Not Improved") {
                        Utility.warningToast(this@NewVisualAcuityActivity, "Pin Hole is Not Improved")
                    } else {
                        binding.etRightPinHoleUnit.showDropDown()
                    }
                } else {
                    Utility.warningToast(this@NewVisualAcuityActivity, "Select Pin Hole")

                }
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }

        val leftPHUnitAdapter = ArrayAdapter(
                this@NewVisualAcuityActivity,
                android.R.layout.simple_dropdown_item_1line,
                provideDistantUnit()
            )
        binding.etLeftPinHoleUnit.setAdapter(leftPHUnitAdapter)
        binding.etLeftPinHoleUnit.setOnClickListener {
            if (canEdit){
                if (!binding.etLeftPinHole.text.isNullOrEmpty()) {
                    if (binding.etLeftPinHole.text.toString() == "Not Improved") {
                        Utility.warningToast(this@NewVisualAcuityActivity, "Pin Hole is Not Improved")
                    } else {
                        binding.etLeftPinHoleUnit.showDropDown()
                    }
                } else {
                    Utility.warningToast(this@NewVisualAcuityActivity, "Select Pin Hole")

                }
            } else{
                Utility.warningToast(this@NewVisualAcuityActivity, "Cannot Edit")
            }
        }
    }

    private fun setPatientData(){
        intentDecodeText = sessionManager.getPatientData().toString()
        val gson = Gson()
        val patientData = gson.fromJson(intentDecodeText, PatientData::class.java)
        patientFname = patientData.patientFname
        patientLname = patientData.patientLname
        patientAge = patientData.patientAge
        patientID = patientData.patientId
        campID = patientData.camp_id
        patientGender = patientData.patientGen
        camp = patientData.location
        ageUnit=patientData.AgeUnit
    }

    private fun provideSphereData(): List<String> {
        val NearVisionArrayList = ArrayList<String>()
        NearVisionArrayList!!.add("N5")
        NearVisionArrayList!!.add("N6")
        NearVisionArrayList!!.add("N8")
        NearVisionArrayList!!.add("N10")
        NearVisionArrayList!!.add("N12")
        NearVisionArrayList!!.add("N14")
        NearVisionArrayList!!.add("N18")
        NearVisionArrayList!!.add("N24")
        NearVisionArrayList!!.add("N36")
        NearVisionArrayList!!.add("N48")
        return NearVisionArrayList
    }

    private fun provideDistantData(): List<String> {
        val RightVisionArrayList = ArrayList<String>()
        RightVisionArrayList!!.add("Value")
        RightVisionArrayList!!.add("6/60")
        RightVisionArrayList!!.add("6/48")
        RightVisionArrayList!!.add("6/38")
        RightVisionArrayList!!.add("6/30")
        RightVisionArrayList!!.add("6/24")
        RightVisionArrayList!!.add("6/19")
        RightVisionArrayList!!.add("6/15")
        RightVisionArrayList!!.add("6/12")
        RightVisionArrayList!!.add("6/9.5")
        RightVisionArrayList!!.add("6/7.5")
        RightVisionArrayList!!.add("6/6.0")
        RightVisionArrayList!!.add("6/4.8")
        RightVisionArrayList!!.add("6/3.8")
        RightVisionArrayList!!.add("6/3.0")
        RightVisionArrayList!!.add("Counting fingers")
        RightVisionArrayList!!.add("Hand motion")
        RightVisionArrayList!!.add("Light perception")
        RightVisionArrayList!!.add("No Light perception")
        return RightVisionArrayList
    }

    private fun provideDistantUnit(): List<String> {
        val RightVisionMetersArrayList = ArrayList<String>()
        RightVisionMetersArrayList!!.add("Distant Unit")
        RightVisionMetersArrayList!!.add("Meters")
        RightVisionMetersArrayList!!.add("logMAR")
        return RightVisionMetersArrayList
    }

    private fun providePinHoleDataUnit(): List<String> {
        val RightVisionMetersArrayList = ArrayList<String>()
        RightVisionMetersArrayList!!.add("PinHole Unit")
        RightVisionMetersArrayList!!.add("Meters")
        RightVisionMetersArrayList!!.add("logMAR")
        return RightVisionMetersArrayList
    }

    private fun providePinHoleData(): List<String> {
        val PinHoleArrayList = ArrayList<String>()
        PinHoleArrayList!!.add("PinHole")
        PinHoleArrayList!!.add("Improved")
        PinHoleArrayList!!.add("Not Improved")
        return PinHoleArrayList
    }

    private fun provideLogMarData(): List<String> {
        val logMARArrayList = ArrayList<String>()
        logMARArrayList!!.add("Value")
        logMARArrayList!!.add("0.00")
        logMARArrayList!!.add("0.02")
        logMARArrayList!!.add("0.04")
        logMARArrayList!!.add("0.06")
        logMARArrayList!!.add("0.08")
        logMARArrayList!!.add("0.10")
        logMARArrayList!!.add("0.12")
        logMARArrayList!!.add("0.14")
        logMARArrayList!!.add("0.16")
        logMARArrayList!!.add("0.18")
        logMARArrayList!!.add("0.20")
        logMARArrayList!!.add("0.22")
        logMARArrayList!!.add("0.24")
        logMARArrayList!!.add("0.26")
        logMARArrayList!!.add("0.28")
        logMARArrayList!!.add("0.30")
        logMARArrayList!!.add("0.32")
        logMARArrayList!!.add("0.34")
        logMARArrayList!!.add("0.36")
        logMARArrayList!!.add("0.38")
        logMARArrayList!!.add("0.40")
        logMARArrayList!!.add("0.42")
        logMARArrayList!!.add("0.44")
        logMARArrayList!!.add("0.46")
        logMARArrayList!!.add("0.48")
        logMARArrayList!!.add("0.50")
        logMARArrayList!!.add("0.52")
        logMARArrayList!!.add("0.54")
        logMARArrayList!!.add("0.56")
        logMARArrayList!!.add("0.58")
        logMARArrayList!!.add("0.60")
        logMARArrayList!!.add("0.62")
        logMARArrayList!!.add("0.64")
        logMARArrayList!!.add("0.66")
        logMARArrayList!!.add("0.68")
        logMARArrayList!!.add("0.70")
        logMARArrayList!!.add("0.72")
        logMARArrayList!!.add("0.74")
        logMARArrayList!!.add("0.76")
        logMARArrayList!!.add("0.78")
        logMARArrayList!!.add("0.80")
        logMARArrayList!!.add("0.82")
        logMARArrayList!!.add("0.84")
        logMARArrayList!!.add("0.86")
        logMARArrayList!!.add("0.88")
        logMARArrayList!!.add("0.90")
        logMARArrayList!!.add("0.92")
        logMARArrayList!!.add("0.94")
        logMARArrayList!!.add("0.96")
        logMARArrayList!!.add("0.98")
        logMARArrayList!!.add("1.00")
        return logMARArrayList
    }

    private fun inflateBottomSheet(
        title: String,
        commonList: List<String>,
        etEditText: TextInputEditText
    ) {
        val items = ArrayList<SearchAbleList>()
        for (i in commonList.indices) {
            items.add(SearchAbleList(i, commonList[i]))
        }
        val dialog = SingleSelectBottomSheetDialogFragment(
            this@NewVisualAcuityActivity,
            items,
            title,
            0,
            true
        ) { selectedValue ->
            val selectedValue = commonList[selectedValue.position]
            etEditText.setText(selectedValue)
        }
        dialog.show(supportFragmentManager, "SingleSelectBottomSheetDialogFragment")
    }

    private fun validateForm(): Boolean {
        var isValid = true
        if (binding.etRNVSphere.text.isNullOrEmpty()) {
            isValid = false
            Utility.errorToast(this@NewVisualAcuityActivity, "Right Near Vision Required")
        } else if (binding.etLNVSphere.text.isNullOrEmpty()) {
            isValid = false
            Utility.errorToast(this@NewVisualAcuityActivity, "Left Near Vision Required")
        }
        return isValid
    }


    private fun allowClickableEditText(isEditable:Boolean){
        binding.etRightNotes.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewVisualAcuityActivity,"Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etLeftNotes.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewVisualAcuityActivity,"Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }
    }
}