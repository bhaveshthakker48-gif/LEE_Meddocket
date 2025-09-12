package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcel
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Adapter.AdapterPrescriptionData
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.CallBack.OnDeleteClickListener
import org.impactindiafoundation.iifllemeddocket.CallBack.OnEditClickListener
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.MyValidator
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SharedPrefUtil
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OpdPrescriptionFormViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPharmaFormBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class PharmaFormActivity : BaseActivity(), View.OnClickListener, OnDeleteClickListener,
    View.OnTouchListener, OnEditClickListener {

    lateinit var binding: ActivityPharmaFormBinding

    lateinit var adapter: AdapterPrescriptionData

    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager

    lateinit var customDropDownAdapter: CustomDropDownAdapter

    var PrescriptionDataArrayList: ArrayList<CreatePrescriptionModel>? = null

    var FinalPrescriptionDataArrayList: ArrayList<FinalPrescriptionDrug.PrescriptionItem>? = null

    var brandNamesWithPrompt = mutableListOf<String>()

    var batchWithPrompt = mutableListOf<String>()

    var quantityWithPrompt = mutableListOf<String>()
    var FrequencyMutableList = mutableListOf<String>()
    var DurationMutableList = mutableListOf<String>()
    var DoseMutableList = mutableListOf<String>()

    var action_flag = 0
    var updated_position = 0

    var SpecialityMutableList = mutableListOf<String>()

    var selectedBrand = ""
    var selectedBatch = ""
    var selectedQuantity = ""
    var selectedFrequency = ""
    var selectedFrequency1 = ""
    var selectedDuration = ""
    var selectedSpeciality = ""
    var procurementItem_id = ""
    var brand_id = ""
    var patient_id = ""
    var patient_name = ""
    var unit_id1: Long = 0
    private var campID = 0
    private var existingPrescriptionId: Long = 0
    var campfrom = ""

    private val opdPrescriptionViewModel: OpdPrescriptionFormViewModel by viewModels()

    private var isEditMode = false
    private var editPosition: Int = -1
    private var opdFormId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmaFormBinding.inflate(layoutInflater)
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

        getViewModel()
        createRoomDatabase()
        binding.toolbarPrescriptionForm.toolbar.title = "Prescription Details"
        PrescriptionDataArrayList = ArrayList()
        FinalPrescriptionDataArrayList = ArrayList()
        viewModel1.getAllCurrentInventory()
        val decodedText = sessionManager.getPatientData()
        val gson = Gson()
        val patientData = gson.fromJson(decodedText, PatientData::class.java)
        val patientData1 = gson.fromJson(decodedText, PatientDataLocal::class.java)

        val patientFname = patientData.patientFname
        val patientLname = patientData.patientLname
        val patientAge = patientData.patientAge
        val patientID = patientData.patientId
        val patientGender = patientData.patientGen
        val camp = patientData.location
        val ageUnit = patientData.AgeUnit
        patient_id = patientData.patientId.toString()
        patient_name = patientFname + " " + patientLname
        campID = patientData.camp_id

        binding.edtPatientName.setText("Name :- " + patientFname + " " + patientLname)
        binding.edtAge.setText("Age :- " + patientAge.toString() + " " + ageUnit)
        binding.edtId.text = "Patient ID :- " + patientID.toString()
        binding.edtGend.setText("Gender :- " + patientGender)
        binding.edtCampLoc.setText("Camp :- " + camp)

        val loginData = sessionManager.getLoginData()

        opdFormId = intent.getIntExtra("formId",0)
        isEditMode = intent.getBooleanExtra("editMode", false)
        editPosition = intent.getIntExtra("position", -1)
        if (opdFormId != 0){
            opdPrescriptionViewModel.getFinalPrescriptionByFormId(opdFormId)
        }
        quantityWithPrompt.clear()
        FrequencyMutableList.clear()
        DurationMutableList.clear()
        SpecialityMutableList.clear()

        binding.TextViewDate.text = ConstantsApp.getCurrentDate()

        GetGenericItem()

        brandNamesWithPrompt = mutableListOf<String>()

        // Add "Select Brand" as the first item
        brandNamesWithPrompt.add("Select Brand")

        customDropDownAdapter = CustomDropDownAdapter(this, brandNamesWithPrompt!!)
        binding.SpinnerBrand!!.adapter = customDropDownAdapter

        batchWithPrompt = mutableListOf<String>()

        // Add "Select Batch" as the first item
        batchWithPrompt.add("Select Batch")

        customDropDownAdapter = CustomDropDownAdapter(this, batchWithPrompt!!)
        binding.SpinnerBatch!!.adapter = customDropDownAdapter

        quantityWithPrompt = mutableListOf()
        quantityWithPrompt.add("Select Unit")

        customDropDownAdapter = CustomDropDownAdapter(this, quantityWithPrompt!!)
        binding.SpinnerQuantity!!.adapter = customDropDownAdapter

        DoseMutableList = mutableListOf()
        DoseMutableList.add("Select Unit")

        customDropDownAdapter = CustomDropDownAdapter(this, DoseMutableList!!)
        binding.SpinnerFrequency!!.adapter = customDropDownAdapter

        FrequencyMutableList.add("Select Unit")
        FrequencyMutableList.add("SOS")
        FrequencyMutableList.add("Once a day")
        FrequencyMutableList.add("Twice a day")
        FrequencyMutableList.add("Thrice a day")
        FrequencyMutableList.add("Four times a day")
        FrequencyMutableList.add("Stat")
        FrequencyMutableList.add("Every 1 hour")
        FrequencyMutableList.add("Every 2 hour")
        FrequencyMutableList.add("Every 3 hour")
        FrequencyMutableList.add("Every 4 hour")
        FrequencyMutableList.add("Once a week")
        FrequencyMutableList.add("Alternate Day")

        customDropDownAdapter = CustomDropDownAdapter(this, FrequencyMutableList!!)
        binding.SpinnerFrequency1!!.adapter = customDropDownAdapter

        DurationMutableList.add("Select Duration")
        DurationMutableList.add("Day(s)")
        DurationMutableList.add("Week(s)")
        DurationMutableList.add("Month(s)")
        DurationMutableList.add("Year(s)")

        customDropDownAdapter = CustomDropDownAdapter(this, DurationMutableList!!)
        binding.SpinnerDuration!!.adapter = customDropDownAdapter

        SpecialityMutableList.add("Select Speciality")
        SpecialityMutableList.add("Ophthalmologist")
        SpecialityMutableList.add("ENT Surgeon")
        SpecialityMutableList.add("Plastic Surgeon")
        SpecialityMutableList.add("Dentist")
        SpecialityMutableList.add("Physician")
        SpecialityMutableList.add("Gynaecologist")
        SpecialityMutableList.add("Oncologist")
        SpecialityMutableList.add("Orthopaedic Surgeon")

        customDropDownAdapter = CustomDropDownAdapter(this, SpecialityMutableList!!)
        binding.SpinnerSpeciality!!.adapter = customDropDownAdapter

        binding.TextViewDate.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.autoCompleteTextView.setOnTouchListener(this)

        binding.SpinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Update the selectedValue variable with the selected item
                selectedBrand = parent?.getItemAtPosition(position).toString()

                getBrand_id(selectedBrand)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected (optional)
            }
        }

        binding.SpinnerSpeciality.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Update the selectedValue variable with the selected item
                    selectedSpeciality = parent?.getItemAtPosition(position).toString()


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected (optional)
                }
            }

        binding.SpinnerFrequency1.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Update the selectedValue variable with the selected item
                    selectedFrequency1 = parent?.getItemAtPosition(position).toString()


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected (optional)
                }
            }

        binding.SpinnerBatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Update the selectedValue variable with the selected item
                selectedBatch = parent?.getItemAtPosition(position).toString()

                getProcurementItem_id(selectedBatch)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected (optional)
            }
        }

        binding.SpinnerQuantity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Update the selectedValue variable with the selected item
                    selectedQuantity = parent?.getItemAtPosition(position).toString()

                    getQuantityID(selectedQuantity)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected (optional)
                }
            }

        binding.SpinnerFrequency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Update the selectedValue variable with the selected item
                    selectedFrequency = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected (optional)
                }
            }

        binding.SpinnerDuration.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Update the selectedValue variable with the selected item
                    selectedDuration = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected (optional)
                }
            }

        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            GetSelectedItemData(selectedItem)
            GetDoseUnitData(selectedItem)
            binding.autoCompleteTextView.isFocusable = false
            binding.autoCompleteTextView.isFocusableInTouchMode = false
        }

        updateList()

        PrescriptionDataArrayList = SharedPrefUtil.loadPrescriptions(this)

        if (PrescriptionDataArrayList!!.isNotEmpty()) {
            binding.linearLayoutPrescriptionList.visibility = View.VISIBLE
        }

        adapter = AdapterPrescriptionData(this, PrescriptionDataArrayList!!, this, this)
        binding.RecyclerViewPrescription.adapter = adapter
        binding.RecyclerViewPrescription.layoutManager = LinearLayoutManager(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isKeyboardOpen(this@PharmaFormActivity)) {
                    hideKeyboard(this@PharmaFormActivity)
                } else {
                    AlertDialog.Builder(this@PharmaFormActivity)
                        .setTitle("Exit Form")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes") { _, _ -> finish() }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        })

       initObserver()
    }


    override fun onResume() {
        super.onResume()

    }

    private fun GetDoseUnitData(selectedItem: String) {
        DoseMutableList.clear()
        viewModel1.currentInventoryLocal.observe(this, Observer { response ->
            Log.d(ConstantsApp.TAG, "GetDoseUnitData=>" + response)

            val selectedItemName =
                selectedItem.toString() // Assuming autoCompleteTextView is your AutoCompleteTextView
            val unitNamesList = response
                .filter { it.item_name == selectedItem }
                .map { it.unit_name }
                .distinct()

            DoseMutableList.clear()
            DoseMutableList.add("tabs")
            DoseMutableList.addAll(unitNamesList)

            customDropDownAdapter = CustomDropDownAdapter(this, DoseMutableList!!)
            binding.SpinnerFrequency!!.adapter = customDropDownAdapter
            if (DoseMutableList.size == 2) {
                binding.SpinnerFrequency!!.setSelection(1)
            }
        })
    }

    private fun GetSelectedItemData(selectedItem: String) {
        viewModel1.allCurrentInventory.observe(this, Observer { InventoryList ->
            val selectedItemList = InventoryList.filter { it.item_name == selectedItem }
            selectedItemList.forEach { item ->
                val selectedBrandIds = selectedItemList.map { it.brand_id }
                getUnitByBrandID(selectedBrandIds)

                val brandNames = mutableListOf<String>()
                val batchNumbers = mutableListOf<String>()
                brandNames.clear()
                batchNumbers.clear()
                selectedItemList.forEach { item ->
                    brandNames.add(item.brand_name)
                    batchNumbers.add(item.batch_no)
                }
                brandNames.forEach { brandName ->
                    Log.d(ConstantsApp.TAG, "Brand Name: $brandName")
                }
                batchNumbers.forEach { batchNo ->
                    Log.d(ConstantsApp.TAG, "Batch Number: $batchNo")
                }

                brandNamesWithPrompt.clear()
                batchWithPrompt.clear()
                brandNamesWithPrompt.add("Select Brand")
                batchWithPrompt.add("Select Batch")
                brandNamesWithPrompt.addAll(brandNames)
                batchWithPrompt.addAll(batchNumbers)

                brandNamesWithPrompt.clear()
                batchWithPrompt.clear()
                brandNamesWithPrompt.add("Select Brand")
                batchWithPrompt.add("Select Batch")

                val uniqueBrandNames = (brandNamesWithPrompt + brandNames).toSet()
                val uniqueBatchNumbers = (batchWithPrompt + batchNumbers).toSet()

                brandNamesWithPrompt.clear()
                batchWithPrompt.clear()
                brandNamesWithPrompt.addAll(uniqueBrandNames)
                batchWithPrompt.addAll(uniqueBatchNumbers)

                customDropDownAdapter = CustomDropDownAdapter(this, brandNamesWithPrompt!!)
                binding.SpinnerBrand!!.adapter = customDropDownAdapter
                try {
                    if (brandNamesWithPrompt.size == 2) {
                        binding.SpinnerBrand!!.setSelection(1)
                    }
                } catch (e: Exception) {
                    Log.d("Error", e.message.toString())
                }

                customDropDownAdapter = CustomDropDownAdapter(this, batchWithPrompt!!)
                binding.SpinnerBatch!!.adapter = customDropDownAdapter
                try {
                    if (batchWithPrompt.size == 2) {
                        binding.SpinnerBatch!!.setSelection(1)
                    }
                } catch (e: Exception) {
                    Log.d("Error", e.message.toString())
                }
            }
        })
    }

    private fun getQuantityID(selectedQuantity: String) {
        viewModel1.allInventoryUnit.observe(this, Observer { inventoryList ->
            inventoryList?.let { inventory ->
                val selectedItem = inventory.find { it.unit_name == selectedQuantity }
                var unit_id = selectedItem?.unit_id
                if (unit_id != null) {
                    unit_id1 = unit_id.toLong()
                    Log.d(ConstantsApp.TAG, "unitID1=>" + unit_id1)
                }
            }
        })
    }

    private fun getBrand_id(selectedBrand: String) {
        viewModel1.allCurrentInventory.observe(this, Observer { inventoryList ->
            inventoryList?.let { inventory ->
                val selectedItem = inventory.find { it.brand_name == selectedBrand }
                val brandID = selectedItem?.brand_id
                if (brandID != null) {
                    brand_id = brandID.toString()
                    Log.d(ConstantsApp.TAG, "brand_id=>" + brand_id)
                }
            }
        })
    }

    private fun getProcurementItem_id(selectedBatch: String) {
        viewModel1.allCurrentInventory.observe(this, Observer { inventoryList ->
            inventoryList?.let { inventory ->
                val selectedItem = inventory.find { it.batch_no == selectedBatch }
                val procurementItemId = selectedItem?.procurementItem_id
                if (procurementItemId != null) {
                    procurementItem_id = procurementItemId.toString()
                }
            }
        })
    }

    private fun getViewModel() {
        val LLE_MedDocketRespository = LLE_MedDocketRespository()
        val LLE_MedDocketProviderFactory =
            LLE_MedDocketProviderFactory(LLE_MedDocketRespository, application)
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
        val Eye_Pre_Op_Investigation_DAO: Eye_Pre_Op_Investigation_DAO =
            database.Eye_Pre_Op_Investigation_DAO()
        val Eye_Post_Op_AND_Follow_ups_DAO: Eye_Post_Op_AND_Follow_ups_DAO =
            database.Eye_Post_Op_AND_Follow_ups_DAO()
        val Eye_OPD_Doctors_Note_DAO: Eye_OPD_Doctors_Note_DAO = database.Eye_OPD_Doctors_Note_DAO()
        val Cataract_Surgery_Notes_DAO: Cataract_Surgery_Notes_DAO =
            database.Cataract_Surgery_Notes_DAO()
        val Patient_DAO: PatientDao = database.PatientDao()
        val Image_Upload_DAO: Image_Upload_DAO = database.Image_Upload_DAO()
        val Registration_DAO: Registration_DAO = database.Registration_DAO()
        val Prescription_DAO: Prescription_DAO = database.Prescription_DAO()
        val SynTable_DAO: SynTable_DAO = database.SynTable_DAO()
        val Final_Prescription_DAO: Final_Prescription_DAO = database.Final_Prescription_DAO()
        val SpectacleDisdributionStatus_DAO: SpectacleDisdributionStatus_DAO =
            database.SpectacleDisdributionStatus_DAO()
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

    private fun GetGenericItem() {
        viewModel1.allCurrentInventory.observe(this, Observer { inventoryList ->
            val itemNameList = mutableListOf<String>()
            inventoryList.filter { it.ITEM_CATEGORY == "DRUG" }.forEach { currentItem ->
                itemNameList.add(currentItem.item_name)
            }

            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, itemNameList)
            binding.autoCompleteTextView.setAdapter(adapter)
            binding.autoCompleteTextView.threshold =
                1 // Set minimum number of characters before suggestions start appearing
        })
    }


    private fun getUnitByBrandID(selectedBrandIds: List<Int>) {

        quantityWithPrompt.clear()
        viewModel1.allInventoryUnit.observe(this, Observer { unitList ->
            val filteredUnits = unitList.filter { selectedBrandIds.contains(it.inventoryBrand_id) }
            val unitSymbols = HashSet<String>()
            filteredUnits.forEach { unit ->
                unitSymbols.add(unit.unit_name)
            }

            val uniqueUnitSymbolsList = unitSymbols.toList()

            quantityWithPrompt.clear()
            quantityWithPrompt.add("Select Quantity")
            quantityWithPrompt.addAll(uniqueUnitSymbolsList)

            customDropDownAdapter = CustomDropDownAdapter(this, quantityWithPrompt!!)
            binding.SpinnerQuantity!!.adapter = customDropDownAdapter

            try {
                if (quantityWithPrompt.size == 2) {
                    binding.SpinnerQuantity!!.setSelection(1)
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        })
    }

    private fun UpdateData() {
        PrescriptionDataArrayList?.let { list ->
            if (updated_position in list.indices) {
                val item = list[updated_position]

                item.generic_name = binding.autoCompleteTextView.text.toString()
                item.brand_name = selectedBrand
                item.batch_no = selectedBatch
                item.quantity = binding.EditTextQuantity.text.toString()
                item.quantity_unit = selectedQuantity
                item.dose = binding.EditTextFrequency.text.toString()
                item.dose_frequency = selectedFrequency
                item.frequency = selectedFrequency1
                item.duration = binding.EditTextDuration.text.toString()
                item.selected_duration = binding.SpinnerDuration.selectedItem.toString()
            }
        }

        adapter.notifyItemChanged(updated_position)
    }


    private fun showDatePickerDialog() {

        var campFromDate = ""

        var loginData = sessionManager.getLoginData()

        for (data in loginData!!) {
            campFromDate = data.campfrom.split(" ")[0] // Extract only the date part, ignoring time
            Log.d(ConstantsApp.TAG, "campfromdate=> $campFromDate")
        }
        val dateParts = campFromDate.split("-")
        val year = dateParts[0].toInt()
        val month = dateParts[1].toInt() - 1 // Months are 0-based in Calendar, so subtract 1
        val day = dateParts[2].toInt()

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val today = Calendar.getInstance()


        val datePickerDialog = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(today.timeInMillis) // Highlight today's date
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setStart(calendar.timeInMillis)
                    .setEnd(today.timeInMillis)
                    .setValidator(object : CalendarConstraints.DateValidator {
                        override fun isValid(date: Long): Boolean {
                            // Dates before campFromDate or after today are not valid
                            return date in calendar.timeInMillis..today.timeInMillis
                        }

                        override fun writeToParcel(dest: Parcel, flags: Int) {}

                        override fun describeContents(): Int {
                            return 0
                        }
                    })
                    .build()
            )
            .build()

        datePickerDialog.addOnPositiveButtonClickListener { selection ->
            val selectedDate = selection?.let {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it
                String.format(
                    Locale.getDefault(),
                    "%02d-%02d-%04d",
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.YEAR)
                )
            }
            selectedDate?.let { binding.TextViewDate.setText(it) }
        }

        datePickerDialog.show(supportFragmentManager, "DatePickerDialog")
    }


    fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to add this data?")
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                binding.linearLayoutPrescriptionList.visibility = View.VISIBLE

                InsertPrescriptionDataLocal()
                ClearAllData()

                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun ClearAllData() {
        binding.EditTextDuration.setText(null)
        binding.EditTextFrequency.setText(null)
        binding.EditTextQuantity.setText(null)

        binding.SpinnerBrand.setSelection(0)
        binding.SpinnerBatch.setSelection(0)
        binding.SpinnerQuantity.setSelection(0)
        binding.SpinnerFrequency.setSelection(0)
        binding.SpinnerFrequency1.setSelection(0)
        binding.SpinnerDuration.setSelection(0)
        // binding.SpinnerSpeciality.setSelection(0)
        binding.autoCompleteTextView.setText("")
        // binding.TextViewDate.setText("")
        binding.TextViewPrescriptionAdd.setText("Add")

        binding.autoCompleteTextView.isFocusable = true
        binding.autoCompleteTextView.isFocusableInTouchMode = true

        action_flag = 0

    }

    override fun onDeleteClick(position: Int) {
        if (PrescriptionDataArrayList.isNullOrEmpty()) {
            // If the list is empty or null, hide the linear layout
            binding.linearLayoutPrescriptionList.visibility = View.GONE
        } else {
            // If the list is not empty, show the linear layout and show confirmation dialog
            binding.linearLayoutPrescriptionList.visibility = View.VISIBLE
            showConfirmationDialog1(position)
        }
    }

    private fun updateList() {
        if (PrescriptionDataArrayList.isNullOrEmpty()) {
            // If the list is empty or null, hide the linear layout
            binding.linearLayoutPrescriptionList.visibility = View.GONE
        } else {
            // If the list is not empty, show the linear layout and show confirmation dialog
            binding.linearLayoutPrescriptionList.visibility = View.VISIBLE

        }
    }

    fun validation(): Boolean {
        var flag = true

        if (!MyValidator.isValidSpinner17(binding.SpinnerBrand, "Select Brand", this)) {
            flag = false
        }
        if (!MyValidator.isValidSpinner17(binding.SpinnerBatch, "Select Batch", this)) {
            flag = false
        }
        if (!MyValidator.isValidSpinner17(binding.SpinnerQuantity, "Select Quantity Unit", this)) {
            flag = false
        }
        if (!MyValidator.isValidSpinner17(binding.SpinnerFrequency, "Select Dose Unit", this)) {
            flag = false
        }
        if (!MyValidator.isValidSpinner17(
                binding.SpinnerFrequency1,
                "Select Frequency Unit",
                this
            )
        ) {
            flag = false
        }
        if (!MyValidator.isValidSpinner17(binding.SpinnerDuration, "Select Duration", this)) {
            flag = false
        }
        if (!MyValidator.isValidSpinner17(binding.SpinnerSpeciality, "Select Speciality", this)) {
            flag = false
        }
        if (!MyValidator.isValidField(binding.EditTextQuantity)) {
            flag = false
        }
        if (!MyValidator.isValidField(binding.EditTextFrequency)) {
            flag = false
        }
        if (!MyValidator.isValidField(binding.EditTextDuration)) {
            flag = false
        }
        if (!MyValidator.isValidFieldAutoCompleteTextView(binding.autoCompleteTextView)) {
            flag = false
        }
        if (!MyValidator.isValidTextView(binding.TextViewDate)) {
            flag = false
        }

        return flag
    }


    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {


        if (event!!.action == MotionEvent.ACTION_UP) {
            // Check if the touch event is inside the drawable bounds
            if (event.rawX >= binding.autoCompleteTextView.right - binding.autoCompleteTextView.compoundDrawables[2].bounds.width()) {
                // Clear the text
                binding.autoCompleteTextView.setText("")
                binding.autoCompleteTextView.isFocusable = true
                binding.autoCompleteTextView.isFocusableInTouchMode = true
                return true
            }
        }

        return false
    }


    fun showConfirmationDialog1(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to delete this data?")
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                // User confirmed, insert data

                Log.d(ConstantsApp.TAG, "position=>" + position)
                PrescriptionDataArrayList!!.removeAt(position)
                adapter.notifyItemRemoved(position)
                updateList()


                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onEditClick(data: CreatePrescriptionModel, position: Int) {
        Log.d(ConstantsApp.TAG, "click data for edit" + data)

        showConfirmationDialog2(position, data)
    }

    fun showConfirmationDialog2(position: Int, data: CreatePrescriptionModel) {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to update this data?")
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                // User confirmed, insert data

                action_flag = 1

                SetDataTOUpdateData(position, data)



                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun SetDataTOUpdateData(position: Int, data: CreatePrescriptionModel) {

        updated_position = position
        binding.TextViewPrescriptionAdd.setText("Update")
        binding.autoCompleteTextView.setText(data.generic_name)

        GetSelectedItemData(data.generic_name)
        GetDoseUnitData(data.generic_name)
        binding.EditTextQuantity.setText(data.quantity)
        binding.EditTextFrequency.setText(data.dose)
        binding.EditTextDuration.setText(data.duration)

        val position_brand = brandNamesWithPrompt.indexOf(data.brand_name)

        if (position_brand != -1) {
            binding.SpinnerBrand.setSelection(position_brand)
        }

        val position_batch = batchWithPrompt.indexOf(data.batch_no)

        if (position_batch != -1) {
            binding.SpinnerBatch.setSelection(position_batch)
        }

        val position_quality = quantityWithPrompt.indexOf(data.quantity_unit)

        if (position_quality != -1) {
            binding.SpinnerQuantity.setSelection(position_quality)
        }

        val position_quality1 = DoseMutableList.indexOf(data.dose_frequency)

        if (position_quality1 != -1) {
            binding.SpinnerFrequency.setSelection(position_quality1)
        }


        val position_frequency = FrequencyMutableList.indexOf(data.frequency)

        if (position_frequency != -1) {
            binding.SpinnerFrequency1.setSelection(position_frequency)
        }

        val position_duration = DurationMutableList.indexOf(data.selected_duration)

        if (position_duration != -1) {
            binding.SpinnerDuration.setSelection(position_duration)
        }


    }

    fun SubmitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to submit this data?")
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                binding.linearLayoutPrescriptionList.visibility = View.VISIBLE
                UpdateData()
                ClearAllData()
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun isKeyboardOpen(activity: AppCompatActivity): Boolean {
        val rootView = activity.window.decorView.rootView
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = rootView.height
        val keypadHeight = screenHeight - rect.bottom
        return keypadHeight > screenHeight * 0.15 // Adjust threshold as needed
    }

    fun hideKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = (context as Activity).currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun initObserver(){
        opdPrescriptionViewModel.insertOpdPrescriptionResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        Utility.successToast(
                            this@PharmaFormActivity,
                            "Form Submitted Successfully"
                        )

                        val intent = Intent(this, PrescriptionPreviewActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("prescriptionDataList", PrescriptionDataArrayList)
                        intent.putExtras(bundle)
                        startActivity(intent)


                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@PharmaFormActivity, "Unexpected error")

                }
            }
        }

        opdPrescriptionViewModel.patientMedicineListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {

                        val opdPatient = it.data?.get(0)!!
                        existingPrescriptionId = opdPatient._id.toLong()
                        val medicineList = ArrayList<CreatePrescriptionModel>()

                        for (medicineData in opdPatient.prescriptionItems){
                            val CreatePrescriptionModel = CreatePrescriptionModel(
                                0,
                                opdPatient.patient_temp_id,
                                opdPatient.patient_name,
                                medicineData.item_name,
                                medicineData.brand_name,
                                "0",
                                medicineData.batch_no,
                                medicineData.procurementItem_id.toString(),
                                medicineData.qty.toString(),
                                medicineData.qty_unit_id.toLong(),
                                medicineData.qty_name,
                                medicineData.dose,
                                "",
                                medicineData.frequency,
                                medicineData.duration,
                                medicineData.duration_unit,
                                opdPatient.createdDate,
                                opdPatient.camp_id!!,
                                opdPatient.camp_id.toLong(),
                                "",
                                ""
                            )
                            medicineList.add(CreatePrescriptionModel)
                        }

                        PrescriptionDataArrayList!!.clear()
                        PrescriptionDataArrayList!!.addAll(medicineList)
                        adapter = AdapterPrescriptionData(this, PrescriptionDataArrayList!!, this, this)
                        binding.RecyclerViewPrescription.adapter = adapter
                        binding.RecyclerViewPrescription.layoutManager = LinearLayoutManager(this)
                        binding.linearLayoutPrescriptionList.visibility = View.VISIBLE
                        adapter = AdapterPrescriptionData(this, PrescriptionDataArrayList!!, this, this)

                        binding.RecyclerViewPrescription.adapter = adapter
                        binding.RecyclerViewPrescription.layoutManager = LinearLayoutManager(this)

                        binding.linearLayoutPrescriptionList.visibility = View.VISIBLE
                        val speciality = opdPatient.doctor_specialty
                        val adapter = binding.SpinnerSpeciality.adapter

                        if (adapter != null) {
                            val position = (0 until adapter.count).firstOrNull {
                                adapter.getItem(it).toString().equals(speciality, ignoreCase = true)
                            } ?: -1

                            if (position >= 0) {
                                binding.SpinnerSpeciality.setSelection(position)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@PharmaFormActivity, "Unexpected error")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.TextViewDate -> {
                showDatePickerDialog()
            }

            binding.btnAdd -> {
                if (validation()) {
                    if (action_flag == 0) {
                        showConfirmationDialog()
                    } else {
                        SubmitDialog()
                    }
                }
            }

            binding.btnSubmit -> {
                val size = PrescriptionDataArrayList?.size
                    ?: 0

                if (size > 0) {
                    PrescriptionDataArrayList?.let { prescriptionList ->
                        if (FinalPrescriptionDataArrayList == null) {
                            FinalPrescriptionDataArrayList = ArrayList()
                        }

                        for (createPrescriptionModel in prescriptionList) {
                            val prescriptionItem = FinalPrescriptionDrug.PrescriptionItem(
                                batch_no = createPrescriptionModel.batch_no,
                                brand_name = createPrescriptionModel.brand_name,
                                dose = createPrescriptionModel.dose,
                                duration = createPrescriptionModel.duration,
                                duration_unit = createPrescriptionModel.selected_duration,
                                frequency = createPrescriptionModel.frequency,
                                given = false,
                                item_name = createPrescriptionModel.generic_name,
                                procurementItem_id = createPrescriptionModel.procurementItem_id.toInt(), // Assuming it's Int
                                qty = createPrescriptionModel.quantity.toDouble(), // Assuming it's Double
                                qty_name = createPrescriptionModel.quantity_unit, // Assuming quantity_unit is qty_name
                                qty_unit_id = createPrescriptionModel.quantity_id.toInt(), // Assuming it's Int
                                route = "" // You need to provide route from somewhere
                            )

                            FinalPrescriptionDataArrayList?.add(prescriptionItem)
                        }
                    }

                    val (camp_id, user_id) = sessionManager.getCampUserID()

                    if (FinalPrescriptionDataArrayList?.isNotEmpty() == true) {
                        val prescriptionItems =
                            mutableListOf<PatientMedicine.PrescriptionItem>()
                        for (createPrescriptionModel in FinalPrescriptionDataArrayList!!) {
                            val prescriptionItem = PatientMedicine.PrescriptionItem(
                                batch_no = createPrescriptionModel.batch_no,
                                brand_name = createPrescriptionModel.brand_name,
                                dose = createPrescriptionModel.dose,
                                duration = createPrescriptionModel.duration,
                                duration_unit = createPrescriptionModel.duration_unit, // You need to provide duration_unit from somewhere
                                frequency = createPrescriptionModel.frequency,
                                given = false, // You need to decide the value for this field
                                item_name = createPrescriptionModel.item_name, // Assuming generic_name is the item name
                                procurementItem_id = createPrescriptionModel.procurementItem_id.toInt(), // Assuming it's Int
                                qty = createPrescriptionModel.qty, // Assuming it's Double
                                qty_name = createPrescriptionModel.qty_name, // Assuming quantity_unit is qty_name
                                qty_unit_id = createPrescriptionModel.qty_unit_id.toInt(), // Assuming it's Int
                                route = "" // You need to provide route from somewhere
                            )

                            prescriptionItems.add(prescriptionItem)
                        }

                        val prescription = PatientMedicine(
                            _id = existingPrescriptionId.toInt(),
                            camp_id = camp_id,
                            created_by = user_id!!,
                            department = " ",
                            doctor_name = " ",
                            doctor_specialty = selectedSpeciality,
                            patient_name = patient_name,
                            patient_temp_id = patient_id,
                            createdDate = getCurrentDate(),
                            prescriptionItems = prescriptionItems
                        )

                        opdPrescriptionViewModel.savePrescription(prescription)

                    } else {
                        // Data is not available, show a message
                        Toast.makeText(this, "Data not available", Toast.LENGTH_SHORT).show()
                    }

                    SharedPrefUtil.savePrescriptions(this, arrayListOf())


                    val intent = Intent(this, PrescriptionPreviewActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable("prescriptionDataList", PrescriptionDataArrayList)
                    intent.putExtras(bundle)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Data not available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun InsertPrescriptionDataLocal() {
        val generic_name = binding.autoCompleteTextView.text.toString()
        val app_createdDate = ConstantsApp.getCurrentDate()
        val (patientId, campId, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val CreatePrescriptionModel = CreatePrescriptionModel(
            0,
            patient_id,
            patient_name,
            generic_name,
            selectedBrand,
            brand_id,
            selectedBatch,
            procurementItem_id?:"",//is getting null ofr ear drops
            binding.EditTextQuantity.text.toString().toString(),
            unit_id1, selectedQuantity,
            binding.EditTextFrequency.text.toString(),
            selectedFrequency,
            selectedFrequency1,
            binding.EditTextDuration.text.toString(),
            selectedDuration, app_createdDate, userId!!,
            campID.toLong(),
            androidId,
            "" )

        PrescriptionDataArrayList!!.add(CreatePrescriptionModel)
        SharedPrefUtil.savePrescriptions(this, PrescriptionDataArrayList!!)
        adapter = AdapterPrescriptionData(this, PrescriptionDataArrayList!!, this, this)
        binding.RecyclerViewPrescription.adapter = adapter
        binding.RecyclerViewPrescription.layoutManager = LinearLayoutManager(this)

    }




fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    override fun onBackPressed() {
        SharedPrefUtil.clearPrescriptions(this)
        super.onBackPressed()
    }

}