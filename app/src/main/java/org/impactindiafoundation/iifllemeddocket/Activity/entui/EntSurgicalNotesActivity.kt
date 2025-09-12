package org.impactindiafoundation.iifllemeddocket.Activity.entui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Activity.PatientForms
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.PatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntSurgicalNotesViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityEntSurgicalNotesBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity

class EntSurgicalNotesActivity : BaseActivity() {

    private lateinit var binding: ActivityEntSurgicalNotesBinding

    private val viewModel: EntSurgicalNotesViewModel by viewModels()
    private val patientReportVM: EntPatientReportViewModel by viewModels()

    private lateinit var sessionManager: SessionManager

    lateinit var customDropDownAdapter: CustomDropDownAdapter
    private val UnitArrayList = listOf("Select Unit", 0x00B0.toChar()+"C", 0x00B0.toChar()+"F")

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntSurgicalNotesBinding.inflate(LayoutInflater.from(this))
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
        handleVisibility()
        initObserver()
        setPatientData()
    }

    private fun initUi() {
        intentFormId = intent.getIntExtra("localFormId",0)
        campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId",0)

        binding.toolbarEntSurgicalNote.toolbar.title = "ENT Surgical Notes"
        sessionManager = SessionManager(this)

        customDropDownAdapter= CustomDropDownAdapter(this,UnitArrayList!!)
        binding.spinnerTemperatureUnit!!.adapter=customDropDownAdapter

        binding.enterbloodPressureSystolic.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setBloodPressure()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.enterbloodPressureDiastolic.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setBloodPressure()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.enterTemperature.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

                binding.enterbloodPressureSystolic.requestFocus()

                // Optionally show keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.enterbloodPressureSystolic, InputMethodManager.SHOW_IMPLICIT)

                true
            } else {
                false
            }
        }

        binding.enterbloodPressureSystolic.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

                // Focus on ecgDetail, skipping Diastolic
                binding.enterbloodPressureDiastolic.requestFocus()

                // Show keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.enterbloodPressureDiastolic, InputMethodManager.SHOW_IMPLICIT)

                true
            } else {
                false
            }
        }


        binding.submitButton.setOnClickListener {
            if (!validateInvestigationInputs()){
                return@setOnClickListener
            }
            saveSurgicalNotesLocally()
        }

        binding.btnEdit.setOnClickListener {
            if (!canEdit){
                onFormEditClick()
                allowClickableEditText(true)
            }
        }
    }

    private fun setBloodPressure() {
        val systolic = binding.enterbloodPressureSystolic.text?.toString()?.toIntOrNull() ?: 0
        val diastolic = binding.enterbloodPressureDiastolic.text?.toString()?.toIntOrNull() ?: 0

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


    private fun saveSurgicalNotesLocally() {
        val userId = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        Log.d("PatientData", "ID: $patientID, CampID: $campID, userId: $userId, Name: $patientFname $patientLname")

        val surgicalNotes = SurgicalNotesEntity(
            uniqueId = localFormId,
            patientId = patientID,
            campId = campID,
            userId = userId?.toIntOrNull() ?: 0,
            appCreatedDate = date,
            lignocaineSensitive = binding.lignocaine.isChecked,
            xylocaineSensitive = binding.xylocaineSensitivity.isChecked,
            localApplicationDone = binding.localApplication.isChecked,
            localInfiltrationDone = binding.localInfiltration.isChecked,
            nerveBlock = binding.nerveBlock.isChecked,
            generalEndotrachealUsed = binding.generalEndotrachealAnaesthesia.isChecked,
            pulseMonitored = binding.monitorPulse.isChecked,
            respirationMonitored = binding.respiration.isChecked,
            bpMonitored = binding.bloodPressure.isChecked,
            ecgMonitored = binding.ecg.isChecked,
            temperatureMonitored = binding.temperature.isChecked,
            antibioticGiven = binding.antibiotic.isChecked,
            ethamsylateGiven = binding.ethamsylate.isChecked,
            adrenalinInfiltrationDone = binding.localAdrenalinInfiltration.isChecked,
            earWaxRemovalDone = binding.removalOfImpactedEarwax.isChecked,
            tympanoplastyDone = binding.tympanoplasty.isChecked,
            mastoidectomyDone = binding.mastoidectomy.isChecked,
            foreignBodyRemovalDone = binding.foreignBodyRemoval.isChecked,
            grommentInsertionDone = binding.grommetInsertion.isChecked,
            excisionBiopsyDone = binding.excisionBiopsy.isChecked,
            other = binding.enterOther.text.toString(),
            pulseValue = binding.enterMonitorPulse.text.toString(),
            bpSystolic = binding.enterbloodPressureSystolic.text.toString(),
            bpDiastolic = binding.enterbloodPressureDiastolic.text.toString(),
            bpInterpretation = binding.tvBpResult.text.toString(),
            respirationValue = binding.enterRespiration.text.toString(),
            temperatureValue = binding.enterTemperature.text.toString(),
            temperatureUnit = when (binding.spinnerTemperatureUnit.selectedItem?.toString()) {
                "${0x00B0.toChar()}C" -> "Celsius"
                "${0x00B0.toChar()}F" -> "Fahrenheit"
                else -> ""
            },
            ecgDetail = binding.ecgDetail.text.toString(),
            antibioticDetail = binding.antibioticDetail.text.toString(),
        )
        Log.d("PatientData", "SurgicalNotesEntity: ${Gson().toJson(surgicalNotes)}")
        viewModel.insertSurgicalNotes(surgicalNotes)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun gotoScreen(context: Context, cls: Class<*>) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }


    private fun initObserver() {
        viewModel.insertionStatus.observe(this) {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    try {
                        val patientReport = EntPatientReport(
                            id = patientReportFormId,
                            formType = Constants.ENT_SURGICAL_NOTES,
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
                    Utility.errorToast(this@EntSurgicalNotesActivity, "Unexpected error")

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
                            this@EntSurgicalNotesActivity,
                            "Form Submitted Successfully"
                        )
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntSurgicalNotesActivity, "Unexpected error")
                }
            }
        }

        viewModel.surgucalNotesListByIdById.observe(this) {
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
                    Utility.errorToast(this@EntSurgicalNotesActivity, "Unexpected error")

                }
            }
        }
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

        viewModel.getSurgicalNotesById(intentFormId, patientID)

    }

    private fun setUpFormData(formData: SurgicalNotesEntity) {
        binding.lignocaine.isChecked = formData.lignocaineSensitive
        binding.xylocaineSensitivity.isChecked = formData.xylocaineSensitive
        binding.localApplication.isChecked = formData.localApplicationDone
        binding.localInfiltration.isChecked = formData.localInfiltrationDone
        binding.nerveBlock.isChecked = formData.nerveBlock
        binding.generalEndotrachealAnaesthesia.isChecked = formData.generalEndotrachealUsed

        binding.monitorPulse.isChecked = formData.pulseMonitored
        binding.enterMonitorPulse.setText(formData.pulseValue)

        binding.respiration.isChecked = formData.respirationMonitored
        binding.enterRespiration.setText(formData.respirationValue)

        binding.bloodPressure.isChecked = formData.bpMonitored
        binding.enterbloodPressureSystolic.setText(formData.bpSystolic)
        binding.enterbloodPressureDiastolic.setText(formData.bpDiastolic)

        binding.ecg.isChecked = formData.ecgMonitored
        binding.ecgDetail.setText(formData.ecgDetail)

        binding.temperature.isChecked = formData.temperatureMonitored
        binding.enterTemperature.setText(formData.temperatureValue)

        val tempUnit = when (formData.temperatureUnit) {
            "Celsius" -> 0x00B0.toChar() + "C"
            "Fahrenheit" -> 0x00B0.toChar() + "F"
            else -> "Select Unit"
        }
        val index = UnitArrayList.indexOf(tempUnit)
        if (index >= 0) binding.spinnerTemperatureUnit.setSelection(index)

        binding.antibiotic.isChecked = formData.antibioticGiven
        binding.antibioticDetail.setText(formData.antibioticDetail)

        binding.ethamsylate.isChecked = formData.ethamsylateGiven
        binding.localAdrenalinInfiltration.isChecked = formData.adrenalinInfiltrationDone

        binding.removalOfImpactedEarwax.isChecked = formData.earWaxRemovalDone
        binding.tympanoplasty.isChecked = formData.tympanoplastyDone
        binding.mastoidectomy.isChecked = formData.mastoidectomyDone
        binding.foreignBodyRemoval.isChecked = formData.foreignBodyRemovalDone
        binding.grommetInsertion.isChecked = formData.grommentInsertionDone
        binding.excisionBiopsy.isChecked = formData.excisionBiopsyDone

        binding.enterOther.setText(formData.other)
    }

    private fun allowClickableEditText(canEdit: Boolean) {
        // EditTexts
        binding.enterMonitorPulse.isEnabled = canEdit
        binding.enterRespiration.isEnabled = canEdit
        binding.enterbloodPressureSystolic.isEnabled = canEdit
        binding.enterbloodPressureDiastolic.isEnabled = canEdit
        binding.enterTemperature.isEnabled = canEdit
        binding.enterOther.isEnabled = canEdit
        binding.antibioticDetail.isEnabled = canEdit
        binding.ecgDetail.isEnabled = canEdit


        // CheckBoxes
        binding.lignocaine.isEnabled = canEdit
        binding.xylocaineSensitivity.isEnabled = canEdit
        binding.localApplication.isEnabled = canEdit
        binding.localInfiltration.isEnabled = canEdit
        binding.nerveBlock.isEnabled = canEdit
        binding.generalEndotrachealAnaesthesia.isEnabled = canEdit

        binding.monitorPulse.isEnabled = canEdit
        binding.respiration.isEnabled = canEdit
        binding.bloodPressure.isEnabled = canEdit
        binding.ecg.isEnabled = canEdit
        binding.temperature.isEnabled = canEdit

        binding.antibiotic.isEnabled = canEdit
        binding.ethamsylate.isEnabled = canEdit
        binding.localAdrenalinInfiltration.isEnabled = canEdit
        binding.removalOfImpactedEarwax.isEnabled = canEdit
        binding.tympanoplasty.isEnabled = canEdit
        binding.mastoidectomy.isEnabled = canEdit
        binding.foreignBodyRemoval.isEnabled = canEdit
        binding.grommetInsertion.isEnabled = canEdit
        binding.excisionBiopsy.isEnabled = canEdit

        // Spinner
        binding.spinnerTemperatureUnit.isEnabled = canEdit
    }

    private fun onFormEditClick() {
        canEdit = true
        binding.submitButton.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE
        binding.tvSubmit.text = "Update"
    }

    private fun validateInvestigationInputs() : Boolean{
        val isAnyFieldSelected = binding.lignocaine.isChecked ||
                binding.xylocaineSensitivity.isChecked ||
                binding.localApplication.isChecked ||
                binding.localInfiltration.isChecked ||
                binding.nerveBlock.isChecked ||
                binding.generalEndotrachealAnaesthesia.isChecked ||
                binding.monitorPulse.isChecked ||
                binding.respiration.isChecked ||
                binding.bloodPressure.isChecked ||
                binding.ecg.isChecked ||
                binding.temperature.isChecked ||
                binding.antibiotic.isChecked ||
                binding.ethamsylate.isChecked ||
                binding.localAdrenalinInfiltration.isChecked ||
                binding.removalOfImpactedEarwax.isChecked ||
                binding.tympanoplasty.isChecked ||
                binding.mastoidectomy.isChecked ||
                binding.foreignBodyRemoval.isChecked ||
                binding.grommetInsertion.isChecked ||
                binding.excisionBiopsy.isChecked ||
                binding.enterOther.text.toString().isNotBlank()

        if (!isAnyFieldSelected) {
            showToast("Please select at least one field before submitting")
            return false
        }

        if (binding.temperature.isChecked && (binding.spinnerTemperatureUnit.selectedItem == null || binding.spinnerTemperatureUnit.selectedItem.toString() == "Select Unit")) {
            showToast("Please select a valid Temperature Unit")
            return false
        }
        if (binding.monitorPulse.isChecked && binding.enterMonitorPulse.text.toString().isBlank()) {
            showToast("Please enter pulse value")
            return false
        }
        if (binding.respiration.isChecked && binding.enterRespiration.text.toString().isBlank()) {
            showToast("Please enter respiration value")
            return false
        }
        if (binding.bloodPressure.isChecked && binding.enterbloodPressureDiastolic.text.toString().isBlank() && binding.enterbloodPressureSystolic.text.toString().isBlank()) {
            showToast("Please enter blood pressure value")
            return false
        }
        if (binding.ecg.isChecked && binding.ecgDetail.text.toString().isBlank()) {
            showToast("Please enter ecg details")
            return false
        }
        if (binding.antibiotic.isChecked && binding.antibioticDetail.text.toString().isBlank()) {
            showToast("Please enter antibiotic details")
            return false
        }
        return true
    }

    private fun handleVisibility() {
        binding.monitorPulse.setOnCheckedChangeListener { _, isChecked ->
            binding.enterMonitorPulse.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.monitorPulseUnit.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.enterMonitorPulse.setText("")
        }

        binding.respiration.setOnCheckedChangeListener { _, isChecked ->
            binding.enterRespiration.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.respirationUnit.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.enterRespiration.setText("")
        }

        binding.temperature.setOnCheckedChangeListener { _, isChecked ->
            binding.enterTemperature.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.spinnerTemperatureUnit.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                binding.enterTemperature.setText("")
                binding.spinnerTemperatureUnit.setSelection(0)
            }
        }

        binding.bloodPressure.setOnCheckedChangeListener { _, isChecked ->
            binding.bloodPressureLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.tvBpResult.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                binding.enterbloodPressureSystolic.setText("")
                binding.enterbloodPressureDiastolic.setText("")
            }
        }

        binding.ecg.setOnCheckedChangeListener { _, isChecked ->
            binding.ecgDetail.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.ecgDetail.setText("")
        }

        binding.antibiotic.setOnCheckedChangeListener { _, isChecked ->
            binding.antibioticDetail.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.antibioticDetail.setText("")
        }
    }

}