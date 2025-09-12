package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.util.Util
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Activity.PrintActivity1
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.Model.VitalsModel.Interpretation
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.PatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.VitalsFormViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityNewVitalsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewVitalsActivity : BaseActivity() {
    private lateinit var binding: ActivityNewVitalsBinding
    private val vitalsVM: VitalsFormViewModel by viewModels()
    private val patientReportVM: PatientReportViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewVitalsBinding.inflate(layoutInflater)
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

        initUi()
        initObserver()
        setPatientData()
    }

    private fun initUi() {
        intentFormId = intent.getIntExtra("localFormId",0)
        campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId",0)
        vitalsVM.getVitalsFormById(intentFormId)
        sessionManager = SessionManager(this)
        binding.vitalsToolBar.toolbar.title = "Vitals"

        binding.etHeightUnit.setText("cms")
        val heightAdapter =
            ArrayAdapter(
                this@NewVitalsActivity,
                android.R.layout.simple_dropdown_item_1line,
                provideHeightData()
            )
        binding.etHeightUnit.setAdapter(heightAdapter)
        binding.etHeightUnit.setOnClickListener {
            if (canEdit){
                binding.etHeightUnit.showDropDown()
            }
            else{
                Utility.warningToast(this@NewVitalsActivity,"Cannot Edit")
            }

        }
        binding.etWeightUnit.setText("kg(s)")
        val weightData = ArrayList<String>()
        weightData.add("kg(s)")
        weightData.add("lb(s)")
        val weightAdapter =
            ArrayAdapter(
                this@NewVitalsActivity,
                android.R.layout.simple_dropdown_item_1line,
                weightData
            )
        binding.etWeightUnit.setAdapter(weightAdapter)
        binding.etWeightUnit.setOnClickListener {
            if (canEdit){
                binding.etWeightUnit.showDropDown()
            }
            else{
                Utility.warningToast(this@NewVitalsActivity,"cannot Edit")
            }
        }
        binding.btnSubmitForm.setOnClickListener {
            if (validateForm()){
            saveVitalsForm()
            }
        }
        binding.etHeight.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                setBmiCalculation()
            }
            else{
                if (!binding.etHeight.text.isNullOrEmpty() && !binding.etWeight.text.isNullOrEmpty()){
                    setBmiCalculation()
                }
                else{
                    binding.etBMI.setText("")
                    binding.tvBmiResult.text = ""
                }
            }
        }
        binding.etWeight.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                    setBmiCalculation()
            }
            else{
                if (!binding.etHeight.text.isNullOrEmpty() && !binding.etWeight.text.isNullOrEmpty()){
                    setBmiCalculation()
                }
                else{
                    binding.etBMI.setText("")
                    binding.tvBmiResult.text = ""
                }
            }
        }
        binding.etHeightUnit.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                if (!binding.etHeight.text.isNullOrEmpty() && !binding.etWeight.text.isNullOrEmpty()){
                    setBmiCalculation()
                }
                else{
                    binding.etBMI.setText("")
                    binding.tvBmiResult.text = ""
                }

            }
        }
        binding.etWeightUnit.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                if (!binding.etHeight.text.isNullOrEmpty() && !binding.etWeight.text.isNullOrEmpty()){
                    setBmiCalculation()
                }
                else{
                    binding.etBMI.setText("")
                    binding.tvBmiResult.text = ""
                }
            }
        }
        binding.etBpSystolic.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                setBloodPressure()
            }
        }
        binding.etBpDiastolic.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                setBloodPressure()
            }
        }
        binding.etPulseRate.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                setPulseRateInfo()
            }
        }
        binding.btnEdit.setOnClickListener {
            if (!canEdit){
                onFormEditClick()
                allowClickableEditText(true)
            }
        }

    }

    private fun initObserver() {
        vitalsVM.insertVitalsResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    try {
                        val patientReport = PatientReport(
                            id = patientReportFormId,
                            formType = Constants.VITALS_FORM,
                            formId = it.data?.toInt() ?: 0,
                            patientFname = patientFname,
                            patientLname = patientLname,
                            patientId = patientID,
                            patientGen = patientGender,
                            patientAge = patientAge,
                            camp_id = campID,
                            location = camp,
                            AgeUnit =ageUnit,
                            RegNo = ""
                        )
                        patientReportVM.insertPatientReport(patientReport)
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@NewVitalsActivity, "Unexpected error")

                }
            }
        }

        patientReportVM.insertPatientReportResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }
                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        Utility.successToast(
                            this@NewVitalsActivity,
                            "Form Submitted Successfully"
                        )
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@NewVitalsActivity, "Unexpected error")

                }
            }
        }

        vitalsVM.vitalsFormListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()){
                            localFormId = it.data[0]._id
                            isFormLocal = true
                            val vitals = it.data[0]
                            canEdit = false
                            binding.btnEdit.visibility = View.VISIBLE
                            binding.btnSubmitForm.visibility = View.GONE
                            allowClickableEditText(false)
                            setUpFormData(vitals)
                        }
                        else{
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
                    Utility.errorToast(this@NewVitalsActivity, "Unexpected error")

                }
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        if (binding.etBpSystolic.text.isNullOrEmpty()){
            isValid = false
            Utility.errorToast(this@NewVitalsActivity,"Enter Systolic")
        }
        else if (binding.etBpDiastolic.text.isNullOrEmpty()){
            isValid = false
            Utility.errorToast(this@NewVitalsActivity,"Enter Diastolic")
        }
        return isValid
    }

    private fun saveVitalsForm() {
        val (patientId, campIdLocal, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)

        val bmi = binding.etBMI.text?.toString() ?: ""
        val bp_diastolic = binding.etBpDiastolic.text?.toString() ?: ""
        val height = binding.etHeight.text?.toString() ?: ""
        val height_unit = binding.etHeightUnit.text?.toString() ?: ""
        val pulseRate = if (binding.etPulseRate.text.isNullOrEmpty()) 0 else  binding.etPulseRate.text.toString().toInt()
        val bp_systolic = binding.etBpSystolic.text?.toString() ?: ""
        val weight = binding.etWeight.text?.toString() ?: ""
        val weight_unit = binding.etWeightUnit.text?.toString() ?: ""
        val bmi_interpretation = binding.tvBmiResult.text.toString()
        val bp_interpretation = binding.tvBpResult.text.toString()
        val pulse_rate_interpretation = binding.tvPulseRateResult.text.toString()


        val vitals = Vitals(
            localFormId, bmi, bmi_interpretation,
            bp_interpretation, campId,
            ConstantsApp.getCurrentDate(), bp_diastolic,
            height, height_unit,
            patientId!!, pulse_rate_interpretation,
            pulseRate,
            bp_systolic,
            userId.toString(), weight, weight_unit
        )

            Log.d("Eric", "data inserted ${vitals.toString()}")

        vitalsVM.insertVitalsForm(vitals)
    }

    private fun setUpFormData(form: Vitals){
        binding.etBpSystolic.setText(form.systolic)
        binding.etBpDiastolic.setText(form.diastolic)
        binding.etPulseRate.setText(form.pulseRate.toString())
        binding.etHeight.setText(form.height)
        binding.etWeight.setText(form.weight)
        binding.etWeightUnit.setText(form.weightUnit)
        binding.etHeightUnit.setText(form.heightUnit)
        val heightAdapter =
            ArrayAdapter(
                this@NewVitalsActivity,
                android.R.layout.simple_dropdown_item_1line,
                provideHeightData()
            )
        binding.etHeightUnit.setAdapter(heightAdapter)
        val weightData = ArrayList<String>()
        weightData.add("kg(s)")
        weightData.add("lb(s)")
        val weightAdapter =
            ArrayAdapter(
                this@NewVitalsActivity,
                android.R.layout.simple_dropdown_item_1line,
                weightData
            )
        binding.etWeightUnit.setAdapter(weightAdapter)
        binding.etBMI.setText(form.bmi)
        binding.tvBpResult.text = form.bpInterpretation
        binding.tvPulseRateResult.text = form.prInterpretation
        binding.tvBmiResult.text = form.bmiInterpretation
    }

    private fun onFormEditClick() {
        canEdit = true
        binding.btnSubmitForm.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE
        binding.tvSubmit.text = "Update"
    }

    private fun allowClickableEditText(isEditable:Boolean){
        binding.etBpSystolic.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewVitalsActivity,"Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etBpDiastolic.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewVitalsActivity,"Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etPulseRate.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewVitalsActivity,"Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etHeight.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewVitalsActivity,"Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etWeight.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewVitalsActivity,"Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }
        binding.etBMI.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@NewVitalsActivity,"Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }
    }

    private fun setPatientData(){
        // Use Gson to parse the JSON string
        intentDecodeText = sessionManager.getPatientData().toString()
        val gson = Gson()
        val patientData = gson.fromJson(intentDecodeText, PatientData::class.java)


        // Access the values
        patientFname = patientData.patientFname
         patientLname = patientData.patientLname
         patientAge = patientData.patientAge
         patientID = patientData.patientId
         campID = patientData.camp_id
         patientGender = patientData.patientGen
         camp = patientData.location
         ageUnit=patientData.AgeUnit
    }


    private fun provideHeightData(): List<String> {
        val heightList = ArrayList<String>()

        heightList.add("cms")
        heightList.add("inches")
        heightList.add("m")
        heightList.add("feet")

        return heightList
    }


    private fun setBmiCalculation() {


        if (!binding.etHeight.text.isNullOrEmpty() && !binding.etWeight.text.isNullOrEmpty()) {
            val height = binding.etHeight.text?.toString()?.toDouble() ?: 0.0
            val weight = binding.etWeight.text?.toString()?.toDouble() ?: 0.0
            val heightUnit = binding.etHeightUnit.text.toString()
            val weightUnit = binding.etWeightUnit.text.toString()

            val bmi = calculateBMI(weight, height, weightUnit, heightUnit)
            setBMIInterpretation(bmi)
        }


    }

    private fun calculateBMI(
        weight: Double,
        height: Double,
        weightUnit: String,
        heightUnit: String
    ): Double {
        val weightInKg = if (weightUnit == "lb(s)") weight * 0.453592 else weight
        val heightInMeters = when (heightUnit) {
            "inches" -> height * 0.0254
            "feet" -> height * 0.3048
            "cms" -> height / 100.0
            "m" -> height
            else -> height // assume meters
        }

        val bmi = weightInKg / (heightInMeters * heightInMeters)

        return String.format("%.2f", bmi).toDouble()
    }

    fun setBMIInterpretation(bmi: Double) {
        val interpretation = when {
            bmi < 18.5 -> Interpretation("Under Weight", R.color.red)
            bmi in 18.5..22.9 -> Interpretation("Normal", R.color.black)
            bmi in 23.0..29.9 -> Interpretation("Overweight, Grade-I Obesity", R.color.red)
            bmi in 30.0..34.9 -> Interpretation("Obese, Grade-II Obesity", R.color.red)
            bmi in 35.0..39.0 -> Interpretation("Moderately Obese, Grade-III Obesity", R.color.red)
            bmi >= 40.0 -> Interpretation("Morbid Obesity, Grade-IV Obesity", R.color.red)
            else -> Interpretation("Unknown", android.R.color.black)
        }

        Log.d(ConstantsApp.TAG, "bmi=>" + bmi)

        binding.etBMI.text = Editable.Factory.getInstance().newEditable(String.format("%.4f", bmi))

        binding.tvBmiResult.text = interpretation.text
        binding.tvBmiResult.setTextColor(ContextCompat.getColor(this, interpretation.colorResId))
    }

    private fun setBloodPressure() {
        val systolic = binding.etBpSystolic.text?.toString()?.toIntOrNull() ?: 0
        val diastolic = binding.etBpDiastolic.text?.toString()?.toIntOrNull() ?: 0

        val bloodPressureInfo = getBloodPressureType(systolic, diastolic)
        // binding.tvInterpretationBP.text = "Blood Pressure Type: ${bloodPressureInfo.first}"
        binding.tvBpResult.text = "${bloodPressureInfo.first}"
        binding.tvBpResult.setTextColor(ContextCompat.getColor(this, bloodPressureInfo.second))
    }

    private fun getBloodPressureType(systolic: Int, diastolic: Int): Pair<String, Int> {
        return when {
            systolic == 0 || diastolic == 0 -> Pair(
                "",
                android.R.color.black
            ) // Set a default color for zero values
            systolic < 90 || (diastolic in 0..60) -> Pair("Hypotension", R.color.blue)
            systolic < 120 && diastolic < 80 -> Pair("Normal", R.color.black)
            systolic in 120..139 || diastolic in 80..89 -> Pair("Prehypertension", R.color.red)
            systolic in 140..159 || diastolic in 90..99 -> Pair("Stage 1 Hypertension", R.color.red)
            systolic >= 160 || diastolic >= 100 -> Pair("Stage 2 Hypertension", R.color.red)
            else -> Pair("Unknown", android.R.color.black) // Set a default color for unknown
        }
    }

    private fun setPulseRateInfo() {
        val pulseRateText = binding.etPulseRate.text?.toString()?.trim() ?: ""

        if (pulseRateText.isNotEmpty()) {
            val pulseRate = pulseRateText.toIntOrNull() ?: 0
            val pulseRateInfo = getPulseRateInfo(pulseRate)

            Log.d(ConstantsApp.TAG, "pulseRateInfo=>$pulseRateInfo")
            binding.tvPulseRateResult.text = "${pulseRateInfo.first}"
            binding.tvPulseRateResult.setTextColor(
                ContextCompat.getColor(
                    this,
                    pulseRateInfo.second
                )
            )
        } else {
            Log.d(ConstantsApp.TAG, "Pulse rate input is empty")
        }
    }

    private fun getPulseRateInfo(pulseRate: Int): Pair<String, Int> {
        return when {
            pulseRate in 60..100 -> Pair("Normal", R.color.black)
            pulseRate < 60 -> Pair("Bradycardia", R.color.blue)
            pulseRate > 100 -> Pair("Tachycardia", R.color.red)
            else -> Pair("Unknown", android.R.color.black) // Set a default color for unknown
        }
    }

}