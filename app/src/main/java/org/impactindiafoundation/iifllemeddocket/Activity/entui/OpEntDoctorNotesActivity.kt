package org.impactindiafoundation.iifllemeddocket.Activity.entui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.Adapter.entadapter.EntImpressionAdapter
import org.impactindiafoundation.iifllemeddocket.Adapter.entadapter.EntSymptomAdapter
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.entapimodel.EntPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntEarType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntImpressionEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntNoseType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntSymptomsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntThroatType
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntOpdDoctorsNoteViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityOpEntDoctorNotesBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity

class OpEntDoctorNotesActivity : BaseActivity() {

    private lateinit var binding: ActivityOpEntDoctorNotesBinding

    private val organList = listOf("Select Organ", "Ear", "Nose", "Throat")
    private val partList = listOf("Select Part", "Right", "Left", "Both")

    lateinit var customDropDownAdapter: CustomDropDownAdapter
    lateinit var symptomAdapter: EntSymptomAdapter
    lateinit var impressionAdapter: EntImpressionAdapter

    lateinit var sessionManager: SessionManager

    private var selectedSymptomId: Int? = null
    private var selectedImpressionId: Int? = null
    private lateinit var currentSymptomList: List<SymptomWrapper>

    private val viewModel: EntOpdDoctorsNoteViewModel by viewModels()
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
    private var isSymptomsInserted = false
    private var isImpressionInserted = false
    private var isInvestigationInserted = false
    private var insertedFormId = 0


    private val temporarySymptomList = mutableListOf<EntSymptomsEntity>()
    private val temporaryImpressionList = mutableListOf<EntImpressionEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpEntDoctorNotesBinding.inflate(layoutInflater)
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

        binding.toolbarEyeOPDDoctorsNote.toolbar.title = "ENT OPD Doctors Note"

        sessionManager = SessionManager(this)

        initUi()
        initObserver()
        setPatientData()
    }

    private fun initUi(){
        intentFormId = intent.getIntExtra("localFormId",0)
        campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId",0)

        if (intentFormId != 0) {
            viewModel.getSymptomsById(intentFormId)
            viewModel.getImpressionById(intentFormId)
            viewModel.getInvestigationById(intentFormId)
        }

        // Initialize dropdowns
        binding.selectOrgan.adapter = CustomDropDownAdapter(this, organList)
        binding.selectPart.adapter = CustomDropDownAdapter(this, partList)
        binding.selectImpressionPart.adapter = CustomDropDownAdapter(this, partList)

        // Initialize Adapters early to prevent crash
        symptomAdapter = EntSymptomAdapter(
            temporarySymptomList,
            canEdit
        ) { selectedItem ->
            temporarySymptomList.remove(selectedItem)
            symptomAdapter.notifyDataSetChanged()
        }
        binding.RecyclerViewEntSymptoms.adapter = symptomAdapter
        binding.RecyclerViewEntSymptoms.layoutManager = LinearLayoutManager(this)


        impressionAdapter = EntImpressionAdapter(temporaryImpressionList, canEdit) { selectedItem ->
            temporaryImpressionList.remove(selectedItem)
            impressionAdapter.notifyDataSetChanged()
        }
        binding.RecyclerViewEntImpression.layoutManager = LinearLayoutManager(this)
        binding.RecyclerViewEntImpression.adapter = impressionAdapter

        // Fetch data
        viewModel.fetchEarSymptoms()
        viewModel.fetchNoseSymptoms()
        viewModel.fetchThroatSymptoms()
        viewModel.fetchEntImpression()

        // Organ Selection
        binding.selectOrgan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOrgan = organList[position]
                when (selectedOrgan) {
                    "Ear" -> viewModel.earSymptoms.observe(this@OpEntDoctorNotesActivity) {
                        setupGenericSymptomsDropdown(it.map { e -> SymptomWrapper.EarSymptom(e) })
                    }
                    "Nose" -> viewModel.noseSymptoms.observe(this@OpEntDoctorNotesActivity) {
                        setupGenericSymptomsDropdown(it.map { n -> SymptomWrapper.NoseSymptom(n) })
                    }
                    "Throat" -> viewModel.throatSymptoms.observe(this@OpEntDoctorNotesActivity) {
                        setupGenericSymptomsDropdown(it.map { t -> SymptomWrapper.ThroatSymptom(t) })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Impression List
        viewModel.entImpression.observe(this) { list ->
            if (!list.isNullOrEmpty()) {
                val adapter = CustomDropDownAdapter(this, list.map { it.impression })
                binding.selectImpression.adapter = adapter

                binding.selectImpression.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selected = list[position]
                        selectedImpressionId = selected.id

                        binding.EditTextImpressionOtherDetails.visibility =
                            if (selected.impression.equals("Other", true)) View.VISIBLE else View.GONE
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }

        binding.addSymptoms.setOnClickListener { saveSymptomToRoom() }
        binding.addImpression.setOnClickListener { saveImpressionToRoom() }

        updateSymptomsHeaderWeightsBasedOnCanEdit()
        updateImpressionHeaderWeightsBasedOnCanEdit()

        binding.submitButton.setOnClickListener {
            if (!validateInvestigationInputs()) return@setOnClickListener

            if (temporarySymptomList.isEmpty()) {
                Utility.errorToast(this@OpEntDoctorNotesActivity, "Please add at least one Symptom before submitting.")
                return@setOnClickListener
            }

            if (temporaryImpressionList.isEmpty()) {
                Utility.errorToast(this@OpEntDoctorNotesActivity, "Please add at least one Impression before submitting.")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                Log.d("SubmitFlow", "Submit button clicked. Symptom size = ${temporarySymptomList.size}, Impression size = ${temporaryImpressionList.size}")


                storeInvestigationDataLocally()

                // Clear UI lists

            }
        }


        binding.btnEdit.setOnClickListener {
            if (!canEdit){
                onFormEditClick()
                allowClickableEditText(true)
            }
        }
        visibilityInvestigation()
    }

    private fun initObserver() {
        viewModel.insertionInvestigationStatus.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    // Show progress if needed
                }
                Status.SUCCESS -> {
                    isInvestigationInserted = true
                    insertedFormId = it.data?.toInt() ?: 0

                    lifecycleScope.launch {


                        val existingSymptoms = viewModel.getSymptomsByFormId(intentFormId)
                        val existingImpressions = viewModel.getImpressionsByFormId(intentFormId)

                        existingSymptoms?.forEach { existing ->
                            if (temporarySymptomList.none { it.uniqueId == existing.uniqueId }) {
                                Log.d("DeleteCheck", "Deleting symptom with id=${existing.uniqueId}")
                                viewModel.deleteSymptom(existing)
                            }
                        }

                        existingImpressions?.forEach { existing ->
                            if (temporaryImpressionList.none { it.uniqueId == existing.uniqueId }) {
                                Log.d("DeleteCheck", "Deleting Impression with id=${existing.uniqueId}")
                                viewModel.deleteImpression(existing)
                            }
                        }


                        temporarySymptomList.forEach { symptom ->
                            val updatedSymptom = symptom.copy(formId = insertedFormId)
                            Log.d("insertedFormId", "Inserting symptom: $updatedSymptom")
                            Log.d("insertedFormId", "Inserting symptoms for formId=$insertedFormId")
                            viewModel.insertSymptom(updatedSymptom)
                        }

                        temporaryImpressionList.forEach { impression ->
                            val updatedImpression = impression.copy(formId = insertedFormId)
                            Log.d("insertedFormId", "Inserting impression: $updatedImpression")
                            Log.d("insertedFormId", "Inserting impression for formId=$insertedFormId")
                            viewModel.insertImpression(updatedImpression)
                        }

                        viewModel.getSymptomsById(insertedFormId)
                        viewModel.getImpressionById(insertedFormId)


                        temporarySymptomList.clear()
                        symptomAdapter.notifyDataSetChanged()
                        binding.LinearLayoutSymptomsList.visibility = View.GONE

                        temporaryImpressionList.clear()
                        impressionAdapter.notifyDataSetChanged()
                        binding.LinearLayoutImpressionList.visibility = View.GONE

                    }

                    checkAndInsertPatientReport()
                }
                Status.ERROR -> {
                    Utility.errorToast(this@OpEntDoctorNotesActivity, "Error inserting audiometry image")
                }
            }
        }

        viewModel.insertionSymptomsStatus.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    // Show progress if needed
                }
                Status.SUCCESS -> {
                    isSymptomsInserted = true

                    Log.d("insertedFormId", "symptoms form inserted with ID = $insertedFormId")

                    checkAndInsertPatientReport()
                }
                Status.ERROR -> {
                    Utility.errorToast(this@OpEntDoctorNotesActivity, "Error inserting audiometry data")
                }
            }
        }

        viewModel.insertionImpressionStatus.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    // Show progress if needed
                }
                Status.SUCCESS -> {
                    isImpressionInserted = true

                    Log.d("insertedFormId", "impression form inserted with ID = $insertedFormId")

                    checkAndInsertPatientReport()
                }
                Status.ERROR -> {
                    Utility.errorToast(this@OpEntDoctorNotesActivity, "Error inserting audiometry image")
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
                            this@OpEntDoctorNotesActivity,
                            "Form Submitted Successfully"
                        )
                        onBackPressed()
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@OpEntDoctorNotesActivity, "Unexpected error")
                }
            }
        }

        viewModel.symptomsListListById.observe(this) {
            Log.d("insertedFormId", "Fetched symptoms count: ${it.data?.size}")

            when (it.status) {
                Status.LOADING -> {
                    // Optional: Show loading indicator
                }
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val symptomsList = it.data

                            localFormId = symptomsList[0].formId
                            isFormLocal = true
                            canEdit = false
                            updateSymptomsHeaderWeightsBasedOnCanEdit()
                            symptomAdapter.updateEditState(false)

                            temporarySymptomList.clear()
                            temporarySymptomList.addAll(symptomsList)

                            symptomAdapter = EntSymptomAdapter(temporarySymptomList, false) { selectedItem ->
                                temporarySymptomList.remove(selectedItem)
                                symptomAdapter.notifyDataSetChanged()
                            }
                            binding.RecyclerViewEntSymptoms.adapter = symptomAdapter
                            binding.LinearLayoutSymptomsList.visibility = View.VISIBLE

                            if (symptomsList[0].app_id == "1") {
                                binding.btnEdit.visibility = View.GONE
                                binding.submitButton.visibility = View.GONE
                            } else {
                                binding.btnEdit.visibility = View.VISIBLE
                                binding.submitButton.visibility = View.GONE
                            }

                            allowClickableEditText(false)
                            setUpSymptomsFormData(symptomsList)
                        } else {
                            allowClickableEditText(true)
                            localFormId = 0
                            isFormLocal = false
                            canEdit = true
                            updateSymptomsHeaderWeightsBasedOnCanEdit()
                            binding.btnEdit.visibility = View.GONE
                            binding.submitButton.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message ?: "Unknown error")
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@OpEntDoctorNotesActivity, "Unexpected error")
                }
            }
        }

        viewModel.impressionListListById.observe(this) {
            Log.d("insertedFormId", "Fetched impressions count: ${it.data?.size}")


            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            val impressionList = it.data

                            localFormId = impressionList[0].formId
                            isFormLocal = true
                            canEdit = false
                            updateImpressionHeaderWeightsBasedOnCanEdit()
                            impressionAdapter.updateEditState(false)

                            temporaryImpressionList.clear()
                            temporaryImpressionList.addAll(impressionList)

                            impressionAdapter = EntImpressionAdapter(temporaryImpressionList, canEdit) { selectedItem ->
                                temporaryImpressionList.remove(selectedItem)
                                impressionAdapter.notifyDataSetChanged()
                            }
                            binding.RecyclerViewEntImpression.adapter = impressionAdapter
                            binding.LinearLayoutImpressionList.visibility = View.VISIBLE

                            if (impressionList[0].app_id == "1") {
                                binding.btnEdit.visibility = View.GONE
                                binding.submitButton.visibility = View.GONE
                            } else {
                                binding.btnEdit.visibility = View.VISIBLE
                                binding.submitButton.visibility = View.GONE
                            }

                            allowClickableEditText(false)
                            setUpImpressionFormData(impressionList)
                        } else {
                            allowClickableEditText(true)
                            localFormId = 0
                            isFormLocal = false
                            canEdit = true
                            updateImpressionHeaderWeightsBasedOnCanEdit()
                            binding.btnEdit.visibility = View.GONE
                            binding.submitButton.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message ?: "Unknown error")
                    }
                }
                Status.ERROR -> {
                    Utility.errorToast(this@OpEntDoctorNotesActivity, "Unexpected error")
                }
            }
        }


        viewModel.investigationListListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    try {
                        if (!it.data.isNullOrEmpty()){
                            val investigationDetails = it.data[0]
                            localFormId = investigationDetails.uniqueId
                            isFormLocal = true
                            canEdit = false

                            if (investigationDetails.app_id == "1") {
                                binding.btnEdit.visibility = View.GONE
                                binding.submitButton.visibility = View.GONE
                            } else {
                                binding.btnEdit.visibility = View.VISIBLE
                                binding.submitButton.visibility = View.GONE
                            }

                            allowClickableEditText(false)
                            setUpInvestigationFormData(investigationDetails)

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
                    Utility.errorToast(this@OpEntDoctorNotesActivity, "Unexpected error")

                }
            }
        }
    }

    private fun checkAndInsertPatientReport() {
        Log.d("OPDDOCTORNOTES", "Checking insert: S=$isSymptomsInserted, I=$isImpressionInserted, V=$isInvestigationInserted")

        if (isSymptomsInserted && isImpressionInserted && isInvestigationInserted) {
            try {
                val patientReport = EntPatientReport(
                    id = patientReportFormId,
                    formType = Constants.ENT_OPD_DOCTOR_NOTES,
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

                isSymptomsInserted = false
                isImpressionInserted = false
                isInvestigationInserted = false
                insertedFormId = 0

            } catch (e: Exception) {
                Log.e("FormSaveError", e.message ?: "Unknown error")
            }
        }
    }

    private fun updateSymptomsHeaderWeightsBasedOnCanEdit() {
        val organParams = binding.tvOrgan.layoutParams as LinearLayout.LayoutParams
        val partParams = binding.tvPart.layoutParams as LinearLayout.LayoutParams
        val symptomParams = binding.tvSymptom.layoutParams as LinearLayout.LayoutParams
        val actionParams = binding.actionSymptoms.layoutParams as LinearLayout.LayoutParams

        if (canEdit) {
            organParams.weight = 1f
            partParams.weight = 1f
            symptomParams.weight = 1f
            actionParams.weight = 1f
            binding.actionSymptoms.visibility = View.VISIBLE
        } else {
            organParams.weight = 1.3f
            partParams.weight = 1.3f
            symptomParams.weight = 1.4f
            actionParams.weight = 0f
            binding.actionSymptoms.visibility = View.GONE
        }

        binding.tvOrgan.layoutParams = organParams
        binding.tvPart.layoutParams = partParams
        binding.tvSymptom.layoutParams = symptomParams
        binding.actionSymptoms.layoutParams = actionParams
    }



    private fun updateImpressionHeaderWeightsBasedOnCanEdit() {
        val impressionPart = binding.impressionPart.layoutParams as LinearLayout.LayoutParams
        val impresionType = binding.impresionType.layoutParams as LinearLayout.LayoutParams
        val actionParams = binding.actionImpression.layoutParams as LinearLayout.LayoutParams

        if (canEdit) {
            impressionPart.weight = 1f
            impresionType.weight = 1f
            actionParams.weight = 1f
            binding.actionImpression.visibility = View.VISIBLE
        } else {
            impressionPart.weight = 1.5f
            impresionType.weight = 1.5f
            actionParams.weight = 0f
            binding.actionImpression.visibility = View.GONE
        }

        binding.impressionPart.layoutParams = impressionPart
        binding.impresionType.layoutParams = impresionType
        binding.actionImpression.layoutParams = actionParams
    }


    private fun saveSymptomToRoom() {
        val organ = binding.selectOrgan.selectedItem.toString()
        val part = binding.selectPart.selectedItem.toString()
        val symptomName = binding.SelectSymptoms.selectedItem?.toString()
        val otherDetail = binding.EditTextSymptomOtherDetails.text.toString()

        if (organ == "Select Organ" || part == "Select Part" || symptomName == "Select") {
            showToast("Please select valid organ, part, and symptom")
            return
        }

        val finalSymptom = if (symptomName.equals("Other", true)) {
            if (otherDetail.isBlank()) {
                showToast("Please enter other symptom detail")
                return
            }
            otherDetail
        } else symptomName

        val uid = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        val entity = EntSymptomsEntity(
            formId = insertedFormId,
            patientId = patientID,
            campId = campID,
            userId = uid?.toIntOrNull() ?: 0,
            organ = organ,
            part = part,
            symptom = finalSymptom,
            symptomId = selectedSymptomId ?: 0,
            appCreatedDate = date,
        )

        Log.d("insertedFormId", "Added symptoms with formId=${entity.formId}")


        temporarySymptomList.add(entity)
        symptomAdapter.notifyDataSetChanged()
        binding.LinearLayoutSymptomsList.visibility = View.VISIBLE
        resetSymptomInputFields()

    }

    private fun saveImpressionToRoom() {
        val part = binding.selectImpressionPart.selectedItem.toString()
        val impression = binding.selectImpression.selectedItem?.toString()
        val otherDetail = binding.EditTextImpressionOtherDetails.text.toString()

        if (part == "Select Part" || impression == "Select") {
            showToast("Please select valid impression and part")
            return
        }

        val finalImpression = if (impression.equals("Other", true)) {
            if (otherDetail.isBlank()) {
                showToast("Please enter other impression detail")
                return
            }
            otherDetail
        } else impression

        val uid = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        val entity = EntImpressionEntity(
            formId = insertedFormId,
            patientId = patientID,
            campId = campID,
            userId = uid?.toIntOrNull() ?: 0,
            part = part,
            impression = finalImpression,
            impressionId = selectedImpressionId ?: 0,
            appCreatedDate = date,
        )

        Log.d("insertedFormId", "Added impression with formId=${entity.formId}")


        temporaryImpressionList.add(entity)
        impressionAdapter.notifyDataSetChanged()
        binding.LinearLayoutImpressionList.visibility = View.VISIBLE
        resetImpressionInputFields()
    }

    private fun storeInvestigationDataLocally() {
        val uid = ConstantsApp.extractPatientAndLoginData(sessionManager).third
        val date = ConstantsApp.getCurrentDate()

        val doctorNoteInvestigation = DoctorNoteInvestigationEntity(
            uniqueId = localFormId,
            patientId = patientID,
            campId = campID,
            userId = uid?.toIntOrNull() ?: 0,
            appCreatedDate = date,
            cbc = binding.checkboxCBC.isChecked,
            bt = binding.checkboxBT.isChecked,
            ct = binding.checkboxCT.isChecked,
            hiv = binding.checkboxHIV.isChecked,
            hbsag = binding.checkboxHBsAg.isChecked,
            puretoneaudiometry = binding.checkboxPTA.isChecked,
            impedanceaudiometry = binding.checkboxImpedanceAudiometry.isChecked,
            xray = binding.checkboxXRay.isChecked,
            xray_value = if (binding.checkboxXRay.isChecked) binding.enterXRay.text.toString() else null,
            removalOfimpactedwax_right = binding.checkboxRemovalWaxRight.isChecked,
            removalOfimpactedwax_left = binding.checkboxRemovalWaxLeft.isChecked,
            tympanoplasty_right = binding.checkboxTympanoplastyRight.isChecked,
            tympanoplasty_left = binding.checkboxTympanoplastyLeft.isChecked,
            exploratoryTympanoplasty_right = binding.checkboxExploratoryTympanoplastyRight.isChecked,
            exploratoryTympanoplasty_left = binding.checkboxExploratoryTympanoplastyLeft.isChecked,
            exploratoryMastoidectomy_right = binding.checkboxExploratoryMastoidectomyRight.isChecked,
            exploratoryMastoidectomy_left = binding.checkboxExploratoryMastoidectomyLeft.isChecked,
            fbremoval_right = binding.checkboxFbRemovalRight.isChecked,
            fbremoval_left = binding.checkboxFbRemovalLeft.isChecked,
            grommetInsertion_right = binding.checkboxGrommetInsertionRight.isChecked,
            grommetInsertion_left = binding.checkboxGrommetInsertionLeft.isChecked,
            excisionBiospy_right = binding.checkboxExcisionBiopsyRight.isChecked,
            excisionBiospy_left = binding.checkboxExcisionBiopsyLeft.isChecked,
            other = binding.enterOther.text.toString(),
            other_right = binding.checkboxOtherRight.isChecked,
            other_left = binding.checkboxOtherLeft.isChecked,
            lineUpForSurgery = binding.lineUpForSurgery.isChecked,
            medication = binding.medication.isChecked,
            audiometry = binding.audiometry.isChecked,
        )

        viewModel.insertOrUpdateDoctorInvestigation(doctorNoteInvestigation)
    }

    private fun resetSymptomInputFields() {
        binding.selectOrgan.setSelection(0)
        binding.selectPart.setSelection(0)
        binding.SelectSymptoms.setSelection(0)
        binding.EditTextSymptomOtherDetails.text?.clear()
        selectedSymptomId = null
    }

    private fun resetImpressionInputFields() {
        binding.selectImpressionPart.setSelection(0)
        binding.selectImpression.setSelection(0)
        binding.EditTextImpressionOtherDetails.text?.clear()
        selectedImpressionId = null
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

        Log.d("insertedFormId", "get form in patient data with intentFormId  $intentFormId")



    }


    private fun setUpSymptomsFormData(symptomsList: List<EntSymptomsEntity>) {
        if (symptomsList.isEmpty()) return

            temporarySymptomList.clear()
            temporarySymptomList.addAll(symptomsList)
            symptomAdapter.notifyDataSetChanged()
            binding.LinearLayoutSymptomsList.visibility = View.VISIBLE

    }


    private fun setUpImpressionFormData(impressionList: List<EntImpressionEntity>) {
        if (impressionList.isEmpty()) return

        temporaryImpressionList.clear()
        temporaryImpressionList.addAll(impressionList)
        impressionAdapter.notifyDataSetChanged()
        binding.LinearLayoutImpressionList.visibility = View.VISIBLE
    }

    private fun allowClickableEditText(canEdit: Boolean) {
        binding.selectOrgan.isEnabled = canEdit
        binding.selectPart.isEnabled = canEdit
        binding.SelectSymptoms.isEnabled = canEdit
        binding.EditTextSymptomOtherDetails.isEnabled = canEdit
        binding.selectImpressionPart.isEnabled = canEdit
        binding.selectImpression.isEnabled = canEdit
        binding.EditTextImpressionOtherDetails.isEnabled = canEdit

        binding.checkboxCBC.isEnabled = canEdit
        binding.checkboxBT.isEnabled = canEdit
        binding.checkboxCT.isEnabled = canEdit
        binding.checkboxHIV.isEnabled = canEdit
        binding.checkboxHBsAg.isEnabled = canEdit
        binding.checkboxPTA.isEnabled = canEdit
        binding.checkboxImpedanceAudiometry.isEnabled = canEdit
        binding.checkboxXRay.isEnabled = canEdit
        binding.enterXRay.isEnabled = canEdit

        binding.checkboxRemovalWaxRight.isEnabled = canEdit
        binding.checkboxRemovalWaxLeft.isEnabled = canEdit
        binding.checkboxTympanoplastyRight.isEnabled = canEdit
        binding.checkboxTympanoplastyLeft.isEnabled = canEdit
        binding.checkboxExploratoryTympanoplastyRight.isEnabled = canEdit
        binding.checkboxExploratoryTympanoplastyLeft.isEnabled = canEdit
        binding.checkboxExploratoryMastoidectomyRight.isEnabled = canEdit
        binding.checkboxExploratoryMastoidectomyLeft.isEnabled = canEdit
        binding.checkboxFbRemovalRight.isEnabled = canEdit
        binding.checkboxFbRemovalLeft.isEnabled = canEdit
        binding.checkboxGrommetInsertionRight.isEnabled = canEdit
        binding.checkboxGrommetInsertionLeft.isEnabled = canEdit
        binding.checkboxExcisionBiopsyRight.isEnabled = canEdit
        binding.checkboxExcisionBiopsyLeft.isEnabled = canEdit
        binding.checkboxOtherRight.isEnabled = canEdit
        binding.checkboxOtherLeft.isEnabled = canEdit
        binding.enterOther.isEnabled = canEdit

        binding.lineUpForSurgery.isEnabled = canEdit
        binding.medication.isEnabled = canEdit
        binding.audiometry.isEnabled = canEdit

        binding.addSymptoms.isEnabled = canEdit
        binding.addImpression.isEnabled = canEdit
    }

    private fun onFormEditClick() {
        canEdit = true
        symptomAdapter.updateEditState(true)
        impressionAdapter.updateEditState(true)
        binding.actionImpression.visibility = View.VISIBLE
        binding.actionSymptoms.visibility = View.VISIBLE
        binding.submitButton.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE
        binding.tvSubmit.text = "Update"
        updateSymptomsHeaderWeightsBasedOnCanEdit()
        updateImpressionHeaderWeightsBasedOnCanEdit()
    }

    private fun setUpInvestigationFormData(formData: DoctorNoteInvestigationEntity) {
        binding.checkboxCBC.isChecked = formData.cbc
        binding.checkboxBT.isChecked = formData.bt
        binding.checkboxCT.isChecked = formData.ct
        binding.checkboxHIV.isChecked = formData.hiv
        binding.checkboxHBsAg.isChecked = formData.hbsag
        binding.checkboxPTA.isChecked = formData.puretoneaudiometry
        binding.checkboxImpedanceAudiometry.isChecked = formData.impedanceaudiometry
        binding.checkboxXRay.isChecked = formData.xray
        binding.enterXRay.setText(formData.xray_value ?: "")

        binding.checkboxRemovalWaxRight.isChecked = formData.removalOfimpactedwax_right
        binding.checkboxRemovalWaxLeft.isChecked = formData.removalOfimpactedwax_left
        binding.checkboxTympanoplastyRight.isChecked = formData.tympanoplasty_right
        binding.checkboxTympanoplastyLeft.isChecked = formData.tympanoplasty_left
        binding.checkboxExploratoryTympanoplastyRight.isChecked = formData.exploratoryTympanoplasty_right
        binding.checkboxExploratoryTympanoplastyLeft.isChecked = formData.exploratoryTympanoplasty_left
        binding.checkboxExploratoryMastoidectomyRight.isChecked = formData.exploratoryMastoidectomy_right
        binding.checkboxExploratoryMastoidectomyLeft.isChecked = formData.exploratoryMastoidectomy_left
        binding.checkboxFbRemovalRight.isChecked = formData.fbremoval_right
        binding.checkboxFbRemovalLeft.isChecked = formData.fbremoval_left
        binding.checkboxGrommetInsertionRight.isChecked = formData.grommetInsertion_right
        binding.checkboxGrommetInsertionLeft.isChecked = formData.grommetInsertion_left
        binding.checkboxExcisionBiopsyRight.isChecked = formData.excisionBiospy_right
        binding.checkboxExcisionBiopsyLeft.isChecked = formData.excisionBiospy_left
        binding.checkboxOtherRight.isChecked = formData.other_right
        binding.checkboxOtherLeft.isChecked = formData.other_left
        binding.enterOther.setText(formData.other ?: "")

        binding.lineUpForSurgery.isChecked = formData.lineUpForSurgery
        binding.medication.isChecked = formData.medication
        binding.audiometry.isChecked = formData.audiometry
    }





    private fun setupGenericSymptomsDropdown(symptoms: List<SymptomWrapper>) {
        currentSymptomList = symptoms
        val adapter = CustomDropDownAdapter(this, symptoms.map { it.symptom })
        binding.SelectSymptoms.adapter = adapter

        binding.SelectSymptoms.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = currentSymptomList[position]
                selectedSymptomId = selected.id
                binding.EditTextSymptomOtherDetails.visibility =
                    if (selected.symptom.equals("Other", true)) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    private fun visibilityInvestigation(){
        binding.checkboxXRay.setOnCheckedChangeListener { _, isChecked ->
            binding.enterXRay.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) binding.enterXRay.setText("")
        }

    }

    private fun validateInvestigationInputs(): Boolean {
        if (binding.checkboxXRay.isChecked && binding.enterXRay.text.toString().isBlank()) {
            showToast("Please enter X-Ray value")
            return false
        }
        if ((binding.checkboxOtherRight.isChecked || binding.checkboxOtherLeft.isChecked) && binding.enterOther.text.toString().isBlank()) {
            showToast("Please enter details in Other field")
            return false
        }
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {

        super.onBackPressed()

    }



}

sealed class SymptomWrapper(val id: Int, val symptom: String) {
    class EarSymptom(e: EntEarType) : SymptomWrapper(e.id, e.symptom)
    class NoseSymptom(n: EntNoseType) : SymptomWrapper(n.id, n.symptom)
    class ThroatSymptom(t: EntThroatType) : SymptomWrapper(t.id, t.symptom)
}
