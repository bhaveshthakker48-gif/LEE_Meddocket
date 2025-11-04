package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.OPD_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.VISUAL_ACUITY_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OpdFormViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.PatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.VitalsFormViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityNewOpdInvestigationsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewOpdInvestigationsActivity : BaseActivity() {

    private lateinit var binding: ActivityNewOpdInvestigationsBinding
    private val opdFormViewModel: OpdFormViewModel by viewModels()
    private val patientReportVM: PatientReportViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    private var intentFormId = 0
    private var patientReportFormId = 0
    private var localFormId = 0
    private var canEdit = true
    private var isFormLocal = false
    private var intentDecodeText = ""
    var patientFname = ""
    var patientLname = ""
    var patientAge = 0
    var patientID = 0
    private var campID = 0
    var patientGender = ""
    var camp = ""
    var ageUnit = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewOpdInvestigationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars =
            true
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
        setO2Hint()
    }

    private fun initUi() {
        intentFormId = intent.getIntExtra("localFormId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId", 0)
        campID = intent.getIntExtra("campId", 0)

        opdFormViewModel.getOpdFormById(intentFormId)
        binding.opdToolBar.toolbar.title = "OPD Investigations"

        sessionManager = SessionManager(this)
        val decodedText = sessionManager.getPatientData()
        val gson = Gson()
        val patientData = gson.fromJson(decodedText, PatientData::class.java)
        patientAge = patientData.patientAge
        patientGender = patientData.patientGen

        binding.etBloodSugar.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                if (binding.cbPatientRefused.isChecked) {
                    binding.cbPatientRefused.isChecked = false
                }
                interpretBloodGlucose(text.toString())
            } else {
                binding.tvSugarResults.setText("")
                binding.tvSugarResults.visibility = View.INVISIBLE
            }
        }

        binding.etHaemoglobin.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                newinterpretHemoglobin(text.toString(), patientGender!!, patientAge!!.toString())
            } else {
                binding.tvHaemoglobinResults.visibility = View.INVISIBLE
                binding.tvHaemoglobinResults.text = ""
            }
        }

        binding.etO2Saturation.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                interpretOxygenSaturation(text.toString())
            } else {
                binding.tvO2SaturationResults.visibility = View.INVISIBLE
                binding.tvO2SaturationResults.text = ""
            }
        }

        binding.btnSubmitForm.setOnClickListener {
            if (validateForm()) {
                saveOpdForm()
            }
        }

        binding.cbPatientRefused.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etBloodSugar.setText("")
            }
        }

        binding.btnEdit.setOnClickListener {
            if (!canEdit) {
                onFormEditClick()
                allowClickableEditText(true)
            }
        }
    }

    private fun initObserver() {
        opdFormViewModel.insertOpdResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        val patientReport = PatientReport(
                            id = patientReportFormId,
                            formType = OPD_FORM,
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
                        Log.d("pawan", "recyclerView form ${patientReport}")
                        patientReportVM.insertPatientReport(patientReport)

                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@NewOpdInvestigationsActivity, "Unexpected error")
                }
            }
        }

        patientReportVM.insertPatientReportResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        Utility.successToast(
                            this@NewOpdInvestigationsActivity,
                            "Form Submitted Successfully"
                        )
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@NewOpdInvestigationsActivity, "Unexpected error")
                }
            }
        }

        opdFormViewModel.opdFormListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val opdForm = it.data[0]
                            localFormId = opdForm._id
                            isFormLocal = true
                            canEdit = false
                            binding.btnEdit.visibility = View.VISIBLE
                            binding.btnSubmitForm.visibility = View.GONE
                            setUpFormData(opdForm)
                            allowClickableEditText(false)
                        } else {
                            allowClickableEditText(true)
                            localFormId = 0
                            isFormLocal = false
                            canEdit = true
                            binding.btnEdit.visibility = View.GONE
                            binding.btnSubmitForm.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@NewOpdInvestigationsActivity, "Unexpected error")
                }
            }
        }
    }

    private fun saveOpdForm() {
        var camp_id: Int? = 0
        val current_Date = ConstantsApp.getCurrentDate()
        val haemoglobin = binding.etHaemoglobin.text.toString()
        val haemoglobin_interpretation: String = binding.tvHaemoglobinResults.text.toString()
        val has_refused: Boolean = binding.cbPatientRefused.isChecked
        val o2_saturation: String = binding.etO2Saturation.text.toString()
        val o2s_interpretation: String = binding.tvO2SaturationResults.text.toString()
        val patient_id: Int
        val random_blood_sugar: String = binding.etBloodSugar.text.toString()
        val rbs_interpretation: String = binding.tvSugarResults.text.toString()
        var userId: String? = null

        val decodedText = sessionManager.getPatientData()
        val gson = Gson()
        val patientData = gson.fromJson(decodedText, PatientData::class.java)

        patient_id = patientData.patientId

        val loginData = sessionManager.getLoginData()
        camp_id = campID
        for (i in 0 until loginData!!.size) {
            val data = loginData[i]
            userId = data.Userid.toString()
        }
        val opdInvestigations = OPD_Investigations(
            intentFormId, camp_id, current_Date,
            haemoglobin, haemoglobin_interpretation, has_refused, o2_saturation,
            o2s_interpretation, patient_id, random_blood_sugar, rbs_interpretation, userId!!
        )
        Log.d("Pawan", "investigationform ${opdInvestigations}")
        opdFormViewModel.insertOpdForm(opdInvestigations)
    }

    private fun interpretBloodGlucose(input: String) {
        val glucoseLevel = input.toDoubleOrNull()
        glucoseLevel?.let {
            val interpretation = when {
                it < 79 -> "Below Normal"
                it in 79.0..160.0 -> "Normal"
                it in 160.0..200.0 -> "Pre-Diabetes"
                else -> "Diabetes"
            }
            binding.tvSugarResults.visibility = View.VISIBLE
            binding.tvSugarResults.setText(interpretation)
            setColorBasedOnInterpretation(interpretation)
        } ?: run {
            // If input is empty, clear the TextView
            binding.tvSugarResults.text = ""
        }
    }

    private fun setColorBasedOnInterpretation(interpretation: String) {
        val color = when (interpretation) {
            "Below Normal" -> Color.BLUE
            "Normal" -> Color.BLACK
            "Pre-Diabetes" -> Color.RED
            "Diabetes" -> Color.RED
            else -> Color.RED
        }
        binding.tvSugarResults.setTextColor(color)
    }

    private fun newinterpretHemoglobin(input: String, gender: String, ageInput: String) {
        val hbLevel = input.toDoubleOrNull()
        val age = ageInput.toIntOrNull()
        if (ageUnit == "Years") {
            hbLevel?.let {
                age?.let { a ->
                    val interpretation = when {
                        gender == "Male" && age in 5..11 -> interpretMaleHemoglobin5to11(it)
                        gender == "Male" && age in 12..14 -> interpretMaleHemoglobin12to14(it)
                        gender == "Female" && age in 5..11 -> interpretFeMaleHemoglobin5to11(it)
                        gender == "Female" && age in 12..14 -> interpretFeMaleHemoglobin12to14(it)
                        gender == "Male" && age > 14 -> interpretGeneralHemoglobin(it)
                        gender == "Female" && age > 14 -> interpretGeneralHemoglobin(it)
                        else -> "Not Defined"
                    }
                    binding.tvHaemoglobinResults.visibility = View.VISIBLE
                    binding.tvHaemoglobinResults.text = interpretation
                    setColorBasedOnHBInterpretation(interpretation)
                } ?: run {
                    binding.tvHaemoglobinResults.text = ""
                }
            } ?: run {
                binding.tvHaemoglobinResults.text = ""
            }
        } else if (ageUnit == "Months") {
            hbLevel?.let {
                age?.let { a ->
                    val interpretation = when {
                        gender == "Male" -> interpretHemoglobinMonths(it)
                        gender == "Female" -> interpretHemoglobinMonths(it)
                        else -> "Not Defined"
                    }
                    binding.tvHaemoglobinResults.visibility = View.VISIBLE
                    binding.tvHaemoglobinResults.text = interpretation
                    setColorBasedOnHBInterpretation(interpretation)
                } ?: run {
                    binding.tvHaemoglobinResults.text = ""
                }
            } ?: run {
                binding.tvHaemoglobinResults.text = ""
            }
        }
    }

    private fun interpretGeneralHemoglobin(hbLevel: Double): String {
        return when {
            hbLevel < 8 -> "Severe Anemia"
            hbLevel in 8.0..10.9 -> "Moderate Anemia"
            hbLevel in 11.0..11.9 -> "Mild Anemia"
            hbLevel >= 12.0 -> "Normal"
            else -> "Not Defined"
        }
    }

    private fun interpretHemoglobinMonths(hbLevel: Double): String {
        return when {
            hbLevel < 7 -> "Severe Anemia"
            hbLevel in 7.0..9.9 -> "Moderate Anemia"
            hbLevel in 10.0..10.9 -> "Mild Anemia"
            else -> "Normal"
        }
    }

    private fun interpretMaleHemoglobin5to11(hbLevel: Double): String {
        return when {
            hbLevel < 8 -> "Severe Anemia (Male)"
            hbLevel in 8.1..10.9 -> "Moderate Anemia (Male)"
            hbLevel in 11.0..11.4 -> "Mild Anemia (Male)"
            else -> "Normal"
        }
    }

    private fun interpretMaleHemoglobin12to14(hbLevel: Double): String {
        return when {
            hbLevel < 8 -> "Severe Anemia (Male)"
            hbLevel in 8.1..10.9 -> "Moderate Anemia (Male)"
            hbLevel in 11.0..11.9 -> "Mild Anemia (Male)"
            else -> "Normal"
        }
    }

    private fun interpretFeMaleHemoglobin5to11(hbLevel: Double): String {
        return when {
            hbLevel < 8 -> "Severe Anemia (Male)"
            hbLevel in 8.1..10.9 -> "Moderate Anemia (Male)"
            hbLevel in 11.0..11.4 -> "Mild Anemia (Male)"
            else -> "Normal"
        }
    }

    private fun interpretFeMaleHemoglobin12to14(hbLevel: Double): String {
        return when {
            hbLevel < 8 -> "Severe Anemia (Male)"
            hbLevel in 8.1..10.9 -> "Moderate Anemia (Male)"
            hbLevel in 11.0..11.9 -> "Mild Anemia (Male)"
            else -> "Normal"
        }
    }

    private fun setColorBasedOnHBInterpretation(interpretation: String) {
        val color = when {
            interpretation.contains("Severe Anemia") -> Color.BLUE
            interpretation.contains("Moderate Anemia") -> Color.RED
            interpretation.contains("Mild Anemia", ignoreCase = true)
                    || interpretation.contains("Normal", ignoreCase = true) -> Color.BLACK

            else -> Color.BLACK
        }
        binding.tvHaemoglobinResults.setTextColor(color)
    }

    private fun interpretOxygenSaturation(input: String) {
        val saturationLevel = input.toDoubleOrNull()
        saturationLevel?.let {
            val interpretation = when {
                it >= 98.0 -> "Normal"
                it in 95.0..97.9 -> "Insufficient but tolerable"
                it in 90.0..94.9 -> "Hypoxia, requiring immediate intervention"
                it < 90.0 -> "Critical Hypoxia"
                else -> "Not Defined"
            }
            binding.tvO2SaturationResults.visibility = View.VISIBLE
            binding.tvO2SaturationResults.text = interpretation
            setColorBasedOnOxygenSaturation(interpretation)
        } ?: run {
            binding.tvO2SaturationResults.text = ""
        }
    }

    private fun setColorBasedOnOxygenSaturation(interpretation: String) {
        val color = when {
            interpretation.contains("Normal") -> Color.BLACK
            interpretation.contains("Insufficient but tolerable") -> Color.RED
            interpretation.contains("Hypoxia") || interpretation.contains("Critical Hypoxia") -> Color.BLUE
            else -> Color.BLACK
        }
        binding.tvO2SaturationResults.setTextColor(color)
    }

    private fun validateForm(): Boolean {
        var isValid = true
        if (binding.etHaemoglobin.text.isNullOrEmpty()) {
            if (binding.etO2Saturation.text.isNullOrEmpty()) {
                if (!binding.cbPatientRefused.isChecked) {
                    if (binding.etBloodSugar.text.isNullOrEmpty()) {
                        isValid = false
                        Utility.errorToast(
                            this@NewOpdInvestigationsActivity,
                            "Enter At least one field"
                        )
                    }
                }
            }
        }
        return isValid
    }


    private fun setUpFormData(form: OPD_Investigations) {
        binding.etHaemoglobin.setText(form.haemoglobin)
        binding.etO2Saturation.setText(form.o2_saturation)
        if (form.has_refused) {
            binding.cbPatientRefused.isChecked = form.has_refused
            binding.etBloodSugar.setText("")
        } else {
            binding.cbPatientRefused.isChecked = form.has_refused
            binding.etBloodSugar.setText(form.random_blood_sugar)
        }
        binding.tvO2SaturationResults.text = form.o2s_interpretation
        binding.tvSugarResults.text = form.rbs_interpretation
        binding.tvHaemoglobinResults.text = form.haemoglobin_interpretation
    }

    private fun onFormEditClick() {
        canEdit = true
        binding.btnSubmitForm.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE
        binding.tvSubmit.text = "Update"
    }

    private fun allowClickableEditText(isEditable: Boolean) {
        binding.cbPatientRefused.setOnCheckedChangeListener { _, isChecked ->
            if (isEditable) {
                binding.cbPatientRefused.isChecked = isChecked
                binding.etBloodSugar.setText("")
            } else {
                binding.cbPatientRefused.isChecked = !isChecked
                Utility.warningToast(this@NewOpdInvestigationsActivity, "cannot Edit")
            }
        }

        binding.etBloodSugar.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewOpdInvestigationsActivity, "Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etHaemoglobin.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewOpdInvestigationsActivity, "Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etO2Saturation.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewOpdInvestigationsActivity, "Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }
    }

    private fun setPatientData() {
        intentDecodeText = sessionManager.getPatientData().toString()
        val gson = Gson()
        val patientData = gson.fromJson(intentDecodeText, PatientData::class.java)
        patientFname = patientData.patientFname
        patientLname = patientData.patientLname
        patientAge = patientData.patientAge
        patientID = patientData.patientId
        patientGender = patientData.patientGen
        camp = patientData.location
        ageUnit = patientData.AgeUnit
        campID = patientData.camp_id
    }

    private fun setO2Hint() {
        val hintText = SpannableStringBuilder("O2Saturation")
        hintText.setSpan(
            SubscriptSpan(), // Make "2" subscript
            1, 2,            // Index range for "2"
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        hintText.setSpan(
            RelativeSizeSpan(0.7f), // Reduce size of "2" to 70%
            1, 2,                  // Index range for "2"
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.etlO2Saturation.hint = hintText
    }
}