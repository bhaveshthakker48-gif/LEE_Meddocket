package org.impactindiafoundation.iifllemeddocket.Activity.entui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPostOpNotesViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityEntPostOpNotesBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import java.util.Calendar
import java.util.Locale

class EntPostOpNotesActivity : BaseActivity() {

    private lateinit var binding: ActivityEntPostOpNotesBinding
    private lateinit var sessionManager: SessionManager
    private val viewModel: EntPostOpNotesViewModel by viewModels()
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

    private var currentDateField: EditText? = null
    private var currentTimeField: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntPostOpNotesBinding.inflate(LayoutInflater.from(this))
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

        binding.toolbarEntPostOpNote.toolbar.title = "ENT Post Op Notes"
        sessionManager = SessionManager(this)

        initUi()
        initObserver()
        setPatientData()
    }

    private fun initUi(){
        intentFormId = intent.getIntExtra("localFormId",0)
        campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId",0)

        binding.EditTextDateOfAdmission.setOnClickListener {
            currentDateField = binding.EditTextDateOfAdmission
            showDatePickerDialog()
        }
        binding.sutureRemovalDate.setOnClickListener {
            currentDateField = binding.sutureRemovalDate
            showDatePickerDialog()
        }
        binding.postoperativeAudiogramDate.setOnClickListener {
            currentDateField = binding.postoperativeAudiogramDate
            showDatePickerDialog()
        }
        binding.hearingThresholdDate.setOnClickListener {
            currentDateField = binding.hearingThresholdDate
            showDatePickerDialog()
        }

        binding.EditTextTimeOfAdmission.setOnClickListener {
            showTimePicker(binding.EditTextTimeOfAdmission)
        }

        handleVisibility()

        binding.submitButton.setOnClickListener {
            if (!validateInvestigationInputs()){
                return@setOnClickListener
            }
            savePostOpNotesLocally()
        }

        binding.btnEdit.setOnClickListener {
            if (!canEdit){
                onFormEditClick()
                allowClickableEditText(true)
            }
        }
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
                            formType = Constants.ENT_POST_OP_NOTES,
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
                    Utility.errorToast(this@EntPostOpNotesActivity, "Unexpected error")

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
                            this@EntPostOpNotesActivity,
                            "Form Submitted Successfully"
                        )
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@EntPostOpNotesActivity, "Unexpected error")
                }
            }
        }

        viewModel.postOPDetailsListById.observe(this) {
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
                    Utility.errorToast(this@EntPostOpNotesActivity, "Unexpected error")
                }
            }
        }
    }

    private fun setUpFormData(formData: EntPostOpNotesEntity) {
        binding.ivHydrationGiven.isChecked = formData.ivHydrationGiven
        binding.ivHyderationDetail.setText(formData.ivHyderationDetail ?: "")
        binding.EditTextDateOfAdmission.setText(formData.npoTill ?: "")
        binding.EditTextTimeOfAdmission.setText(formData.clearFluidsStartTime ?: "")
        binding.paracetamolGiven.isChecked = formData.paracetamolGiven
        binding.antibioticClavulanateGiven.isChecked = formData.antibioticClavulanateGiven
        binding.ofloxacinGiven.isChecked = formData.ofloxacinGiven
        binding.otherMedicationsNote.setText(formData.otherMedicationsNote ?: "")
        binding.wickDrainUsed.isChecked = formData.wickDrainUsed
        binding.redivacUsed.isChecked = formData.redivacUsed
        binding.watchForSoakage.isChecked = formData.watchForSoakage
        binding.watchForHematoma.isChecked = formData.watchForHematoma
        binding.watchForMiddleEarInfection.isChecked = formData.watchForMiddleEarInfection
        binding.watchForWoundInfection.isChecked = formData.watchForWoundInfection
        binding.watchForWoundDehiscence.isChecked = formData.watchForWoundDehiscence
        binding.watchForFacialPalsy.isChecked = formData.watchForFacialPalsy
        binding.sutureRemovalDate.setText(formData.sutureRemovalDate ?: "")
        binding.audiogramResult.setText(formData.audiogramResult ?: "")
        binding.postoperativeAudiogramDate.setText(formData.audiogramDate ?: "")
        binding.hasComplicationInfection.isChecked = formData.hasComplicationInfection
        binding.otherComplicationsNote.setText(formData.otherComplicationsNote ?: "")
        binding.tympanicMembraneStatus.setText(formData.tympanicMembraneStatus ?: "")
        binding.otorrhoeaPresent.isChecked = formData.otorrhoeaPresent
        binding.airConductionThresholdDb.setText(formData.airConductionThresholdDb ?: "")
        binding.hearingThresholdDate.setText(formData.hearingThresholdDate ?: "")
    }

    private fun allowClickableEditText(canEdit: Boolean) {
        binding.EditTextDateOfAdmission.isEnabled = canEdit
        binding.EditTextTimeOfAdmission.isEnabled = canEdit
        binding.otherMedicationsNote.isEnabled = canEdit
        binding.sutureRemovalDate.isEnabled = canEdit
        binding.audiogramResult.isEnabled = canEdit
        binding.postoperativeAudiogramDate.isEnabled = canEdit
        binding.otherComplicationsNote.isEnabled = canEdit
        binding.tympanicMembraneStatus.isEnabled = canEdit
        binding.airConductionThresholdDb.isEnabled = canEdit
        binding.hearingThresholdDate.isEnabled = canEdit
        binding.ivHyderationDetail.isEnabled = canEdit

        binding.ivHydrationGiven.isEnabled = canEdit
        binding.paracetamolGiven.isEnabled = canEdit
        binding.antibioticClavulanateGiven.isEnabled = canEdit
        binding.ofloxacinGiven.isEnabled = canEdit
        binding.wickDrainUsed.isEnabled = canEdit
        binding.redivacUsed.isEnabled = canEdit
        binding.watchForSoakage.isEnabled = canEdit
        binding.watchForHematoma.isEnabled = canEdit
        binding.watchForMiddleEarInfection.isEnabled = canEdit
        binding.watchForWoundInfection.isEnabled = canEdit
        binding.watchForWoundDehiscence.isEnabled = canEdit
        binding.watchForFacialPalsy.isEnabled = canEdit
        binding.hasComplicationInfection.isEnabled = canEdit
        binding.otorrhoeaPresent.isEnabled = canEdit
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

        viewModel.getPostOpDetailsById(intentFormId, patientID)

    }


    private fun savePostOpNotesLocally() {
        val userId = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        Log.d("PatientData", "ID: $patientID, CampID: $campID, userId: $userId, Name: $patientFname $patientLname")

        val postOpNotes = EntPostOpNotesEntity(
            uniqueId = localFormId,
            patientId = patientID,
            campId = campID,
            userId = userId?.toIntOrNull() ?: 0,
            appCreatedDate = date,
            ivHydrationGiven = binding.ivHydrationGiven.isChecked,
            npoTill = if (binding.EditTextDateOfAdmission.text.toString().isBlank()) null else binding.EditTextDateOfAdmission.text.toString(),
            clearFluidsStartTime = if (binding.EditTextTimeOfAdmission.text.toString().isBlank()) null else binding.EditTextTimeOfAdmission.text.toString(),
            paracetamolGiven = binding.paracetamolGiven.isChecked,
            antibioticClavulanateGiven = binding.antibioticClavulanateGiven.isChecked,
            ofloxacinGiven = binding.ofloxacinGiven.isChecked,
            otherMedicationsNote = if (binding.otherMedicationsNote.text.toString().isBlank()) null else binding.otherMedicationsNote.text.toString(),
            wickDrainUsed = binding.wickDrainUsed.isChecked,
            redivacUsed = binding.redivacUsed.isChecked,
            watchForSoakage = binding.watchForSoakage.isChecked,
            watchForHematoma = binding.watchForHematoma.isChecked,
            watchForMiddleEarInfection = binding.watchForMiddleEarInfection.isChecked,
            watchForWoundInfection = binding.watchForWoundInfection.isChecked,
            watchForWoundDehiscence = binding.watchForWoundDehiscence.isChecked,
            watchForFacialPalsy = binding.watchForFacialPalsy.isChecked,
            sutureRemovalDate = if (binding.sutureRemovalDate.text.toString().isBlank()) null else binding.sutureRemovalDate.text.toString(),
            audiogramResult = if (binding.audiogramResult.text.toString().isBlank()) null else binding.audiogramResult.text.toString(),
            audiogramDate = if (binding.postoperativeAudiogramDate.text.toString().isBlank()) null else binding.postoperativeAudiogramDate.text.toString(),
            hasComplicationInfection = binding.hasComplicationInfection.isChecked,
            otherComplicationsNote = if (binding.otherComplicationsNote.text.toString().isBlank()) null else binding.otherComplicationsNote.text.toString(),
            tympanicMembraneStatus = if (binding.tympanicMembraneStatus.text.toString().isBlank()) null else binding.tympanicMembraneStatus.text.toString(),
            otorrhoeaPresent = binding.otorrhoeaPresent.isChecked,
            airConductionThresholdDb = if (binding.airConductionThresholdDb.text.toString().isBlank()) null else binding.airConductionThresholdDb.text.toString(),
            hearingThresholdDate = if (binding.hearingThresholdDate.text.toString().isBlank()) null else binding.hearingThresholdDate.text.toString(),
            ivHyderationDetail = if (binding.ivHyderationDetail.text.toString().isBlank()) null else binding.ivHyderationDetail.text.toString(),
            )

        Log.d("PatientData", "SurgicalNotesEntity: ${Gson().toJson(postOpNotes)}")

        viewModel.insertPostOpNotes(postOpNotes)

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format(
                    Locale.getDefault(),
                    "%02d-%02d-%04d",
                    selectedDay, selectedMonth + 1, selectedYear
                )
                currentDateField?.setText(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        val maxYear = calendar.get(Calendar.YEAR) + 5
        val maxCalendar = Calendar.getInstance()
        maxCalendar.set(maxYear, 11, 31)
        datePickerDialog.datePicker.maxDate = maxCalendar.timeInMillis

        datePickerDialog.show()
    }

    private fun showTimePicker(targetEditText: EditText) {
        val dialogView = layoutInflater.inflate(R.layout.custom_time_picker_dialog, null)

        val hourPicker = dialogView.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minutePicker)

        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        hourPicker.value = currentHour
        hourPicker.wrapSelectorWheel = true

        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.value = currentMinute
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

    private fun validateInvestigationInputs() : Boolean{
        if (binding.ivHydrationGiven.isChecked && binding.ivHyderationDetail.text.toString().isBlank()) {
            showToast("Please enter ivHyderation etail")
            return false
        }
        val isAnyFieldSelectedOrFilled =
            binding.ivHydrationGiven.isChecked ||
                    binding.paracetamolGiven.isChecked ||
                    binding.antibioticClavulanateGiven.isChecked ||
                    binding.ofloxacinGiven.isChecked ||
                    binding.otherMedicationsNote.text.toString().isNotBlank() ||
                    binding.wickDrainUsed.isChecked ||
                    binding.redivacUsed.isChecked ||
                    binding.watchForSoakage.isChecked ||
                    binding.watchForHematoma.isChecked ||
                    binding.watchForMiddleEarInfection.isChecked ||
                    binding.watchForWoundInfection.isChecked ||
                    binding.watchForWoundDehiscence.isChecked ||
                    binding.watchForFacialPalsy.isChecked ||
                    binding.sutureRemovalDate.text.toString().isNotBlank() ||
                    binding.audiogramResult.text.toString().isNotBlank() ||
                    binding.postoperativeAudiogramDate.text.toString().isNotBlank() ||
                    binding.hasComplicationInfection.isChecked ||
                    binding.otherComplicationsNote.text.toString().isNotBlank() ||
                    binding.tympanicMembraneStatus.text.toString().isNotBlank() ||
                    binding.otorrhoeaPresent.isChecked ||
                    binding.airConductionThresholdDb.text.toString().isNotBlank() ||
                    binding.hearingThresholdDate.text.toString().isNotBlank()

        if (!isAnyFieldSelectedOrFilled) {
            showToast("Please fill at least one field before submitting.")
            return false
        }
        return true
    }

    private fun handleVisibility() {
        binding.ivHydrationGiven.setOnCheckedChangeListener { _, isChecked ->
            binding.ivHyderationDetail.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.ivHyderationDetail.setText("")
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
