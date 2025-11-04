package org.impactindiafoundation.iifllemeddocket.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.Adapter.FormCountAdapter
import org.impactindiafoundation.iifllemeddocket.Adapter.SynAdapter
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntImpressionEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntSymptomsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OpdFormViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.RefractiveErrorViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.VisualAcuityViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.VitalsFormViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntAudiometryViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntOpdDoctorsNoteViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPostOpNotesViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntProOpDetailsViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntSurgicalNotesViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.pathology.PathologyViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityAnalyticBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AnalyticActivity : BaseActivity() {

    lateinit var binding: ActivityAnalyticBinding
    private lateinit var synAdapter: SynAdapter
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    lateinit var customDropDownAdapter: CustomDropDownAdapter
    private val entPatientIds = mutableSetOf<Int>()
    private val generalPatientIds = mutableSetOf<Int>()
    private val eyePatientIds = mutableSetOf<Int>()
    private val pathologyPatientIds = mutableSetOf<Int>()
    var totalVitalsBoth = 0
    var totalOpdInvestigationBoth = 0
    var totalVisulaAcuityBoth = 0
    var totalRefractiveErrorBoth = 0
    var Total_Vital: Int = 0
    var Total_Visual: Int = 0
    var Total_Refractive: Int = 0
    var Total_OPD_Investigations: Int = 0
    var Total_Eye_Pre_Op_Notes: Int = 0
    var Total_Eye_Pre_Op_Investigation: Int = 0
    var Total_Eye_Post_Op_AND_Follow_ups: Int = 0
    var Total_Eye_OPD_Doctors_Note: Int = 0
    var Total_Cataract_Surgery_Notes: Int = 0
    var Total_Ent_Pro_Op_Follow_ups: Int = 0
    var Total_Ent_Post_Op_Follow_ups: Int = 0
    var Total_Ent_Audiometry_Follow_ups: Int = 0
    var Total_Ent_Surgical_Follow_ups: Int = 0
    var Total_Ent_Doctor_Notes_Follow_ups: Int = 0
    var Pathology_Report: Int = 0
    var Total_Patient: Int = 0
    var ReportArrayList: ArrayList<String>? = null
    var ReportDatesArrayList: ArrayList<String>? = null
    private val eyeVisualAcuityViewModel: VisualAcuityViewModel by viewModels()
    private val generalVitalsFormViewModel: VitalsFormViewModel by viewModels()
    private val generalOpdFormViewModel: OpdFormViewModel by viewModels()
    private val eyeRefractiveErrorViewModel: RefractiveErrorViewModel by viewModels()

    //Ent
    private val entOpdDoctorsNoteViewModel: EntOpdDoctorsNoteViewModel by viewModels()
    private val entPreOpDetailsViewModel: EntProOpDetailsViewModel by viewModels()
    private val entSurgicalNotesViewModel: EntSurgicalNotesViewModel by viewModels()
    private val entPostOpNotesViewModel: EntPostOpNotesViewModel by viewModels()
    private val entAudiometryViewModel: EntAudiometryViewModel by viewModels()

    //Pathology
    private val pathologyViewModel: PathologyViewModel by viewModels()



    var totalEntPatientCount = 0
    var totalGeneralPatientCount = 0
    var totalEyePatientCount = 0
    var totalPathologyPatientCount = 0
    var totalPatientCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyticBinding.inflate(layoutInflater)
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
        binding.LinearLayout1.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        try {
            getViewModel()
            createRoomDatabase()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.toolbarDataAnalytics.toolbar.title = "Data Analytics"
        ReportArrayList = ArrayList()
        ReportArrayList!!.add("Today`s")
        ReportArrayList!!.add("All")
        ReportArrayList!!.add("Day Wise")
        ReportDatesArrayList = ArrayList()
        customDropDownAdapter = CustomDropDownAdapter(this, ReportArrayList!!)
        binding.spinnerReportType!!.adapter = customDropDownAdapter
        customDropDownAdapter = CustomDropDownAdapter(this, ReportArrayList!!)
        binding.spinnerReportType1!!.adapter = customDropDownAdapter
        binding.LinearLayout1.visibility = View.GONE
        binding.spinnerReportType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = binding.spinnerReportType.selectedItem.toString()
                    updateLinearLayoutVisibility(
                        selectedItem,
                        binding.LinearLayout1,
                        binding.CardView,
                        binding.spinnerReportType,
                        binding.spinnerReportType1
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.spinnerReportType1.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = binding.spinnerReportType1.selectedItem.toString()
                    updateLinearLayoutVisibility(
                        selectedItem,
                        binding.LinearLayout1,
                        binding.CardView,
                        binding.spinnerReportType1,
                        binding.spinnerReportType
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        val calendar = Calendar.getInstance()
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        binding.spinnerReportDayWise.text = todayDate

        GetCountAllLocalTables("Day Wise", todayDate)
        getPatientData("Day Wise", todayDate)

        binding.spinnerReportDayWise.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this@AnalyticActivity,
                { _, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal.set(year, month, dayOfMonth)

                    val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    Log.d(ConstantsApp.TAG, "Date chosen: $selectedDate")

                    binding.spinnerReportDayWise.text = selectedDate

                    GetCountAllLocalTables("Day Wise", selectedDate)
                    getPatientData("Day Wise", selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
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

    fun updateLinearLayoutVisibility(selectedItem: String, linearLayout1: LinearLayout, cardView: CardView, spinner: Spinner, spinner1: Spinner) {
        when (spinner) {
            binding.spinnerReportType -> {
                when (selectedItem) {
                    "All" -> {
                        GetCountAllLocalTables(selectedItem, "")
                    }
                    "Day Wise" -> {
                        linearLayout1.visibility = View.VISIBLE
                        cardView.visibility = View.GONE
                        Log.d(ConstantsApp.TAG, "selectedItem => $selectedItem")
                        val dayWisePosition = (spinner1.adapter as? CustomDropDownAdapter)
                            ?.dataSource
                            ?.indexOf("Day Wise")
                        spinner1.setSelection(dayWisePosition ?: 0)
                        GetCountAllLocalTables(selectedItem, "")
                    }
                    "Today`s" -> {
                        GetCountAllLocalTables(selectedItem, "")
                    }
                }
            }

            binding.spinnerReportType1 -> {
                when (selectedItem) {
                    "All" -> {
                        linearLayout1.visibility = View.GONE
                        cardView.visibility = View.VISIBLE
                        val allPosition = (spinner1.adapter as? CustomDropDownAdapter)
                            ?.dataSource
                            ?.indexOf("All")
                        spinner1.setSelection(allPosition ?: 0)
                    }
                    "Day Wise" -> {
                        linearLayout1.visibility = View.VISIBLE
                        cardView.visibility = View.GONE
                    }
                    "Today`s" -> {
                        linearLayout1.visibility = View.GONE
                        cardView.visibility = View.VISIBLE
                        val todayPosition = (spinner1.adapter as? CustomDropDownAdapter)
                            ?.dataSource
                            ?.indexOf("Today`s")
                        spinner1.setSelection(todayPosition ?: 0)
                    }
                }
            }
        }
    }

    private fun filterRecords(selectedItem: String, selected_date: String, response: List<SynTable>): List<SynTable> {
        return when (selectedItem) {
            "All" -> {
                response.filter { it.isSyn == 1 }
            }
            "Today`s" -> {
                val todayDate = SimpleDateFormat("yyyy-M-dd", Locale.getDefault()).format(Date())
                response.filter { it.isSyn == 1 && it.date == todayDate }
            }
            "Day Wise" -> {
                val formattedSelectedDate = if (selected_date.isEmpty()) {
                    SimpleDateFormat("yyyy-M-dd", Locale.getDefault()).format(Date())
                } else {
                    selected_date
                }
                response.filter { it.isSyn == 1 && it.date == formattedSelectedDate }
            }
            else -> {
                emptyList()
            }
        }
    }

    private fun setSynedData(selectedItem: String, selected_date: String) {
        Log.d(ConstantsApp.TAG, "selected_date in setSynedData => $selected_date")
        Log.d(ConstantsApp.TAG, "selectedItem in setSynedData => $selectedItem")

        // 1️⃣ Initialize adapter before observing LiveData
        synAdapter = SynAdapter(emptyList(), this)
        binding.RecyclerViewSyncedData.layoutManager = LinearLayoutManager(this)
        binding.RecyclerViewSyncedData.adapter = synAdapter

        // 2️⃣ Observe LiveData after adapter is ready
        entPreOpDetailsViewModel.getAllSyncSummaries().observe(this) { summaries ->
            if (!summaries.isNullOrEmpty()) {
                // 🔹 Filter: remove entries with both counts = 0
                val filteredList = summaries.filter {
                    !(it.totalSynced == 0 && it.totalUnsynced == 0)
                }

                if (filteredList.isNotEmpty()) {
                    synAdapter.updateData(filteredList)
                } else {
                    Log.d(ConstantsApp.TAG, "All summaries have 0 sync & unsync count — nothing to show.")
                    synAdapter.updateData(emptyList())
                }
            } else {
                Log.d(ConstantsApp.TAG, "No sync summaries found.")
            }
        }
    }

//    private fun setSynedData(selectedItem: String, selected_date: String) {
//        Log.d(ConstantsApp.TAG, "selected_date in setSynedData=> $selected_date")
//        Log.d(ConstantsApp.TAG, "selectedItem in setSynedData=> $selectedItem")

//        viewModel1.allSynData.observe(this, Observer { response ->
//            Log.d(ConstantsApp.TAG, "allSynData data=> $response")
//
//            val filteredRecords = filterRecords(selectedItem, selected_date, response)
//
//            Log.d(ConstantsApp.TAG, "filteredRecords=>" + filteredRecords)
//
//            if (filteredRecords.isNotEmpty()) {
//                synAdapter = SynAdapter(filteredRecords, this)
//                binding.RecyclerViewSyncedData.layoutManager = LinearLayoutManager(this)
//                binding.RecyclerViewSyncedData.adapter = synAdapter
//                synAdapter.notifyDataSetChanged() // Force RecyclerView refresh
//            } else {
//                Log.d(ConstantsApp.TAG, "Filtered records are empty.")
//            }
//        })
//    }

    private fun GetCountAllLocalTables(selectedItem: String, selected_date: String) {

        //General
        generalVitalsFormViewModel.allVitals.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    totalVitalsBoth += response.size
                    updateVitals(totalVitalsBoth, selectedItem, selected_date)
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.createdDate.startsWith(todayDate) }
                    Total_Vital = todayRecords.size
                    setBarChart()
                    setPieChart(selectedItem, selected_date)
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(todayDate) }
                            Total_Vital = todayRecords.size
                            setBarChart()
                            setPieChart(selectedItem, selected_date)
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(formattedSelectedDate) }
                            Total_Vital = todayRecords.size
                            setBarChart()
                            setPieChart(selectedItem, selected_date)
                        }
                    }
                }
            }

        })

        generalOpdFormViewModel.allOPD_Investigations.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    totalOpdInvestigationBoth += response.size
                    updateOpdInvestigation(totalOpdInvestigationBoth, selectedItem, selected_date)
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.createdDate.startsWith(todayDate) }
                    Total_OPD_Investigations = todayRecords.size
                    setBarChart()
                    setPieChart(selectedItem, selected_date)
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(todayDate) }
                            Total_OPD_Investigations = todayRecords.size
                            setBarChart()
                            setPieChart(selectedItem, selected_date)
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(formattedSelectedDate) }
                            Total_OPD_Investigations = todayRecords.size
                            setBarChart()
                            setPieChart(selectedItem, selected_date)
                        }
                    }
                }
            }

        })

        //Eye
        eyeVisualAcuityViewModel.allVisualAcuity.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    totalVisulaAcuityBoth += response.size
                    updateVisualAcuity(totalVisulaAcuityBoth, selectedItem, selected_date)
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.createdDate.startsWith(todayDate) }
                    Total_Visual = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(todayDate) }
                            Total_Visual = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(formattedSelectedDate) }
                            Total_Visual = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        eyeRefractiveErrorViewModel.allRefractive_Error.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    totalRefractiveErrorBoth += response.size
                    updateRefractiveError(totalRefractiveErrorBoth, selectedItem, selected_date)
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.createdDate.startsWith(todayDate) }
                    Total_Refractive = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(todayDate) }
                            Total_Refractive = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(formattedSelectedDate) }
                            Total_Refractive = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }

        })

        viewModel1.allEye_OPD_Doctors_Note.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Total_Eye_OPD_Doctors_Note = response.size
                    setBarChart()
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.createdDate.startsWith(todayDate) }
                    Total_Eye_OPD_Doctors_Note = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(todayDate) }
                            Total_Eye_OPD_Doctors_Note = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(formattedSelectedDate) }
                            Total_Eye_OPD_Doctors_Note = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        viewModel1.allEye_Pre_Op_Investigation.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Total_Eye_Pre_Op_Investigation = response.size
                    setBarChart()
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.createdDate.startsWith(todayDate) }
                    Total_Eye_Pre_Op_Investigation = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(todayDate) }
                            Total_Eye_Pre_Op_Investigation = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(formattedSelectedDate) }
                            Total_Eye_Pre_Op_Investigation = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        viewModel1.allEye_Pre_Op_Notes.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Total_Eye_Pre_Op_Notes = response.size
                    setBarChart()
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.createdDate.startsWith(todayDate) }
                    Total_Eye_Pre_Op_Notes = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(todayDate) }
                            Total_Eye_Pre_Op_Notes = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(formattedSelectedDate) }
                            Total_Eye_Pre_Op_Notes = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        viewModel1.allCataract_Surgery_Notes.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Total_Cataract_Surgery_Notes = response.size
                    setBarChart()
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.createdDate.startsWith(todayDate) }
                    Total_Cataract_Surgery_Notes = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(todayDate) }
                            Total_Cataract_Surgery_Notes = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(formattedSelectedDate) }
                            Total_Cataract_Surgery_Notes = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        viewModel1.allEye_Post_Op_AND_Follow_ups.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Total_Eye_Post_Op_AND_Follow_ups = response.size
                    setBarChart()
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.createdDate.startsWith(todayDate) }
                    Total_Eye_Post_Op_AND_Follow_ups = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(todayDate) }
                            Total_Eye_Post_Op_AND_Follow_ups = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.createdDate.startsWith(formattedSelectedDate) }
                            Total_Eye_Post_Op_AND_Follow_ups = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        //Ent
        entOpdDoctorsNoteViewModel.All_Ent_Opd_Doctor_Follow_ups.observe(this, Observer { response ->
                when (selectedItem) {
                    "All" -> {
                        Total_Ent_Doctor_Notes_Follow_ups = response.size
                        setBarChart()
                        setPieChart(selectedItem, selected_date)

                    }

                    "Today`s" -> {
                        val todayDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val todayRecords = response.filter {
                            it.appCreatedDate?.startsWith(todayDate) ?: false
                        }
                        Total_Ent_Doctor_Notes_Follow_ups = todayRecords.size
                        setBarChart()
                        setPieChart(selectedItem, selected_date)
                    }

                    "Day Wise" -> {
                        when (selected_date) {
                            "" -> {
                                val todayDate = SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.getDefault()
                                ).format(Date())
                                val todayRecords = response.filter {
                                    it.appCreatedDate?.startsWith(todayDate) ?: false
                                }
                                Total_Ent_Doctor_Notes_Follow_ups = todayRecords.size
                                setBarChart()
                                setPieChart(selectedItem, selected_date)
                            }

                            else -> {
                                val selectedDateFormat =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                                val formattedSelectedDate =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        parsedSelectedDate
                                    )
                                val todayRecords = response.filter {
                                    it.appCreatedDate?.startsWith(formattedSelectedDate) ?: false
                                }
                                Total_Ent_Doctor_Notes_Follow_ups = todayRecords.size
                                setBarChart()
                                setPieChart(selectedItem, selected_date)
                            }
                        }
                    }
                }
            })

        entPreOpDetailsViewModel.All_Ent_Pro_Op_Follow_ups.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Total_Ent_Pro_Op_Follow_ups = response.size
                    setBarChart()
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.appCreatedDate.startsWith(todayDate) }
                    Total_Ent_Pro_Op_Follow_ups = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.appCreatedDate.startsWith(todayDate) }
                            Total_Ent_Pro_Op_Follow_ups = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.appCreatedDate.startsWith(formattedSelectedDate) }
                            Total_Ent_Pro_Op_Follow_ups = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        entSurgicalNotesViewModel.All_Ent_Surgical_Follow_ups.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Total_Ent_Surgical_Follow_ups = response.size
                    setBarChart()
                    setPieChart(selectedItem, selected_date)
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.appCreatedDate.startsWith(todayDate) }
                    Total_Ent_Surgical_Follow_ups = todayRecords.size
                    setBarChart()
                    setPieChart(selectedItem, selected_date)
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.appCreatedDate.startsWith(todayDate) }
                            Total_Ent_Surgical_Follow_ups = todayRecords.size
                            setBarChart()
                            setPieChart(selectedItem, selected_date)
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.appCreatedDate.startsWith(formattedSelectedDate) }
                            Total_Ent_Surgical_Follow_ups = todayRecords.size
                            setBarChart()
                            setPieChart(selectedItem, selected_date)
                        }
                    }
                }
            }
        })

        entAudiometryViewModel.All_Ent_Audiometry_Follow_ups.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Total_Ent_Audiometry_Follow_ups = response.size
                    setBarChart()
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter {
                        it.appCreatedDate?.startsWith(todayDate) ?: false
                    }
                    Total_Ent_Audiometry_Follow_ups = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords = response.filter {
                                it.appCreatedDate?.startsWith(todayDate) ?: false
                            }
                            Total_Ent_Audiometry_Follow_ups = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords = response.filter {
                                it.appCreatedDate?.startsWith(formattedSelectedDate) ?: false
                            }
                            Total_Ent_Audiometry_Follow_ups = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        entPostOpNotesViewModel.All_Ent_Post_Follow_ups.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Total_Ent_Post_Op_Follow_ups = response.size
                    setBarChart()
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter { it.appCreatedDate.startsWith(todayDate) }
                    Total_Ent_Post_Op_Follow_ups = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords =
                                response.filter { it.appCreatedDate.startsWith(todayDate) }
                            Total_Ent_Post_Op_Follow_ups = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords =
                                response.filter { it.appCreatedDate.startsWith(formattedSelectedDate) }
                            Total_Ent_Post_Op_Follow_ups = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        //Pathology
        pathologyViewModel.All_PATHOLOGY_Follow_ups.observe(this, Observer { response ->
            when (selectedItem) {
                "All" -> {
                    Pathology_Report = response.size
                    setBarChart()
                }

                "Today`s" -> {
                    val todayDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val todayRecords = response.filter {
                        it.appCreatedDate?.startsWith(todayDate) ?: false
                    }
                    Pathology_Report = todayRecords.size
                    setBarChart()
                }

                "Day Wise" -> {
                    when (selected_date) {
                        "" -> {
                            val todayDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords = response.filter {
                                it.appCreatedDate?.startsWith(todayDate) ?: false
                            }
                            Pathology_Report = todayRecords.size
                            setBarChart()
                        }

                        else -> {
                            val selectedDateFormat =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                            val formattedSelectedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    parsedSelectedDate
                                )
                            val todayRecords = response.filter {
                                it.appCreatedDate?.startsWith(formattedSelectedDate) ?: false
                            }
                            Pathology_Report = todayRecords.size
                            setBarChart()
                        }
                    }
                }
            }
        })

        viewModel1.allSynData.observe(this, Observer {
            setSynedData(selectedItem, selected_date)
        })

    }

    private fun updateVitals(total: Int, selectedItem: String, selected_date: String) {
        Total_Vital = total
        setBarChart()
        setPieChart(selectedItem, selected_date)
    }

    private fun updateOpdInvestigation(total: Int, selectedItem: String, selected_date: String) {
        Total_OPD_Investigations = total
        setBarChart()
        setPieChart(selectedItem, selected_date)
    }

    private fun updateVisualAcuity(total: Int, selectedItem: String, selected_date: String) {
        Total_Visual = total
        setBarChart()
    }

    private fun updateRefractiveError(total: Int, selectedItem: String, selected_date: String) {
        Total_Refractive = total
        setBarChart()
    }

    private fun setBarChart() {

        val Total_forms = Total_Vital + Total_OPD_Investigations + Total_Visual + Total_Refractive + Total_Eye_OPD_Doctors_Note + Total_Eye_Pre_Op_Investigation +Total_Eye_Pre_Op_Notes + Total_Cataract_Surgery_Notes + Total_Eye_Post_Op_AND_Follow_ups + Total_Ent_Pro_Op_Follow_ups + Total_Ent_Post_Op_Follow_ups + Total_Ent_Audiometry_Follow_ups + Total_Ent_Surgical_Follow_ups + Total_Ent_Doctor_Notes_Follow_ups + Pathology_Report

        val data = listOf(Total_Vital, Total_OPD_Investigations, Total_Visual, Total_Refractive, Total_Eye_OPD_Doctors_Note, Total_Eye_Pre_Op_Investigation, Total_Eye_Pre_Op_Notes, Total_Cataract_Surgery_Notes, Total_Eye_Post_Op_AND_Follow_ups, Total_Ent_Pro_Op_Follow_ups, Total_Ent_Post_Op_Follow_ups, Total_Ent_Audiometry_Follow_ups, Total_Ent_Surgical_Follow_ups, Total_Ent_Doctor_Notes_Follow_ups, Pathology_Report, Total_forms)

        val categoryData = listOf(Total_Vital, Total_OPD_Investigations, Total_Visual, Total_Refractive, Total_Eye_OPD_Doctors_Note, Total_Eye_Pre_Op_Investigation, Total_Eye_Pre_Op_Notes, Total_Cataract_Surgery_Notes, Total_Eye_Post_Op_AND_Follow_ups, Total_Ent_Pro_Op_Follow_ups, Total_Ent_Post_Op_Follow_ups, Total_Ent_Audiometry_Follow_ups, Total_Ent_Surgical_Follow_ups, Total_Ent_Doctor_Notes_Follow_ups, Pathology_Report)

        val entries = ArrayList<BarEntry>()
        for ((index, value) in data.withIndex()) {
            val xValue = (index + 1).toFloat()
            val yValue = value.toFloat()
            entries.add(BarEntry(xValue, yValue))
        }

        val totalSum = categoryData.sum()
        val percentages = if (totalSum > 0) {
            categoryData.map { it.toFloat() / totalSum * 100 } + 100f // or 0f
        } else {
            categoryData.map { 0f } + 0f
        }

        val colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.parseColor("#FF6347"), Color.CYAN, Color.GRAY, Color.MAGENTA, Color.DKGRAY, Color.LTGRAY, Color.BLACK, Color.parseColor("#FFA500"), Color.parseColor("#800080"), Color.parseColor("#008080"), Color.parseColor("#A52A2A"), Color.parseColor("#4682B4"))

        val labels1 = arrayOf("Vital", "OPD Investigation", "Visual Acuity", "Refractive Error", "Eye Opd Doctor Notes", "Eye Pre-Op Investigation", "Eye Pre-Op Notes", "Cataract Surgery Notes", "Eye Post-Op and Follow Ups", "ENT Pre-Op Follow Ups", "ENT Post-Op Follow Ups", "ENT Audiometry Follow Ups", "ENT Surgical Follow Ups", "ENT Doctor Notes Follow Ups", "Pathology Reports", "Total Forms")

        val adapter = FormCountAdapter(this, labels1, data, percentages, colors, Total_forms)
        binding.RecyclerViewFormBarChart.adapter = adapter
        binding.RecyclerViewFormBarChart.layoutManager = LinearLayoutManager(this)

        val barDataSet = BarDataSet(entries, "")
        barDataSet.colors = colors

        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f

        val xAxis: XAxis = binding.barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f

        binding.barChart.isScaleXEnabled = true
        binding.barChart.isScaleYEnabled = false

        binding.barChart.data = barData
        binding.barChart.setFitBars(true)
        binding.barChart.animateY(3000)
        binding.barChart.invalidate()
    }

    private fun setPieChart(selectedItem: String, selected_date: String) {
        setBloodPressure(selectedItem, selected_date)
        setPulseRate(selectedItem, selected_date)
        setBMI(selectedItem, selected_date)
        setBloodSugar(selectedItem, selected_date)
        setSymptoms(selectedItem, selected_date)
        setImpression(selectedItem, selected_date)
        setSurgery(selectedItem, selected_date)
    }

    private fun setBloodPressure(selectedItem: String, selected_date: String) {

        // Helper function to update chart and recycler
        fun updateChartAndRecycler(interpretationCounts: Map<String, Int>) {
            val hypotensionCount = interpretationCounts["Hypotension"] ?: 0
            val normalCount = interpretationCounts["Normal"] ?: 0
            val prehypertensionCount = interpretationCounts["Prehypertension"] ?: 0
            val hypertension1Count = interpretationCounts["Stage 1 Hypertension"] ?: 0
            val hypertension2Count = interpretationCounts["Stage 2 Hypertension"] ?: 0

            val data1 = listOf(
                hypotensionCount,
                normalCount,
                prehypertensionCount,
                hypertension1Count,
                hypertension2Count
            )
            val totalSum = data1.sum()
            val percentages =
                if (totalSum > 0) data1.map { it.toFloat() / totalSum * 100 } else data1.map { 0f }

            val labels1 = arrayOf(
                "Hypotension",
                "Normal",
                "Prehypertension",
                "Stage 1 Hypertension",
                "Stage 2 Hypertension"
            )
            val colors1 = listOf(
                Color.parseColor("#FFF9C4"), // Light Yellow
                Color.parseColor("#C8E6C9"), // Light Green
                Color.parseColor("#BBDEFB"), // Light Blue
                Color.parseColor("#F8BBD0"), // Light Pink
                Color.parseColor("#B2EBF2"), // Light Cyan
            )

            // RecyclerView
            val adapter = FormCountAdapter(this, labels1, data1, percentages, colors1, 0)
            binding.RecyclerViewBloodPressure.adapter = adapter
            binding.RecyclerViewBloodPressure.layoutManager = LinearLayoutManager(this)

            // PieChart
            val entries = ArrayList<PieEntry>()
            for (i in data1.indices) {
                entries.add(PieEntry(data1[i].toFloat(), "")) // empty label for all slices
            }

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors1
            dataSet.setDrawValues(true)
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 14f
            val pieData = PieData(dataSet)

            binding.PieChart.data = pieData
            binding.PieChart.description.isEnabled = false
            binding.PieChart.setUsePercentValues(false) // ✅ show counts, not %
            binding.PieChart.isDrawHoleEnabled = true
            binding.PieChart.holeRadius = 40f
            binding.PieChart.transparentCircleRadius = 45f
            binding.PieChart.legend.isEnabled = false
            binding.PieChart.animateY(1000)
            binding.PieChart.visibility = View.VISIBLE
        }


        var generalData: List<Vitals> = emptyList()
        var viewModelData: List<Vitals> = emptyList()

        fun updateCombined() {
            val combined = generalData + viewModelData

            val filteredData = when (selectedItem) {
                "Today`s" -> {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    combined.filter { it.createdDate.startsWith(today) }
                }

                "Day Wise" -> {
                    if (selected_date.isNotEmpty()) {
                        val parsedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selected_date)
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                        combined.filter { it.createdDate.startsWith(formattedDate) }
                    } else {
                        val today =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        combined.filter { it.createdDate.startsWith(today) }
                    }
                }

                else -> combined
            }

            val interpretationCounts = filteredData
                .groupBy { it.bpInterpretation }
                .mapValues { it.value.size }

            updateChartAndRecycler(interpretationCounts)
        }

        generalVitalsFormViewModel.allVitals.observe(this) {
            generalData = it
            updateCombined()
        }

        viewModel1.allVitals.observe(this) {
            viewModelData = it
            updateCombined()
        }

    }

    private fun setBloodSugar(selectedItem: String, selected_date: String) {

        // Helper function to update chart and recycler
        fun updateChartAndRecycler(interpretationCounts: Map<String, Int>) {
            val preDiabetesCount = interpretationCounts["Pre-Diabetes"] ?: 0
            val normalCount = interpretationCounts["Normal"] ?: 0
            val diabetesCount = interpretationCounts["Diabetes"] ?: 0

            val data1 = listOf(preDiabetesCount, normalCount, diabetesCount)
            val totalSum = data1.sum()
            val percentages =
                if (totalSum > 0) data1.map { it.toFloat() / totalSum * 100 } else data1.map { 0f }

            val labels1 = arrayOf("Pre-Diabetes", "Normal", "Diabetes")
            val colors1 = listOf(
                Color.parseColor("#FFF9C4"), // Light Yellow
                Color.parseColor("#C8E6C9"), // Light Green
                Color.parseColor("#BBDEFB"), // Light Blue
            )

            // RecyclerView
            val adapter = FormCountAdapter(this, labels1, data1, percentages, colors1, 0)
            binding.RecyclerViewBloodSugar.adapter = adapter
            binding.RecyclerViewBloodSugar.layoutManager = LinearLayoutManager(this)

            // PieChart
            val entries = ArrayList<PieEntry>()
            for (i in data1.indices) {
                entries.add(PieEntry(data1[i].toFloat(), ""))
            }

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors1
            dataSet.setDrawValues(true)
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 14f
            val pieData = PieData(dataSet)

            binding.PieChartBloodSugar.data = pieData
            binding.PieChartBloodSugar.description.isEnabled = false
            binding.PieChartBloodSugar.setUsePercentValues(false) // ✅ show counts, not %
            binding.PieChartBloodSugar.isDrawHoleEnabled = true
            binding.PieChartBloodSugar.holeRadius = 40f
            binding.PieChartBloodSugar.transparentCircleRadius = 45f
            binding.PieChartBloodSugar.legend.isEnabled = false
            binding.PieChartBloodSugar.animateY(1000)
            binding.PieChartBloodSugar.visibility = View.VISIBLE

        }

        var generalData: List<OPD_Investigations> = emptyList()
        var viewModelData: List<OPD_Investigations> = emptyList()

        fun updateCombined() {
            val combined = generalData + viewModelData

            val filteredData = when (selectedItem) {
                "Today`s" -> {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    combined.filter { it.createdDate.startsWith(today) }
                }

                "Day Wise" -> {
                    if (selected_date.isNotEmpty()) {
                        val parsedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selected_date)
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                        combined.filter { it.createdDate.startsWith(formattedDate) }
                    } else {
                        val today =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        combined.filter { it.createdDate.startsWith(today) }
                    }
                }

                else -> combined
            }

            val interpretationCounts = filteredData
                .groupBy { it.rbs_interpretation }
                .mapValues { it.value.size }

            updateChartAndRecycler(interpretationCounts)
        }

        generalOpdFormViewModel.allOPD_Investigations.observe(this) {
            generalData = it
            updateCombined()
        }

        viewModel1.allOPD_Investigations.observe(this) {
            viewModelData = it
            updateCombined()
        }
    }

    private fun setBMI(selectedItem: String, selected_date: String) {

        // Helper function to update chart and recycler
        fun updateChartAndRecycler(interpretationCounts: Map<String, Int>) {
            val underWeightCount = interpretationCounts["Under Weight"] ?: 0
            val normalCount = interpretationCounts["Normal"] ?: 0
            val grade1Count = interpretationCounts["Overweight, Grade-I Obesity"] ?: 0
            val grade2Count = interpretationCounts["Obese, Grade-II Obesity"] ?: 0
            val grade3Count = interpretationCounts["Moderately Obese, Grade-III Obesity"] ?: 0
            val grade4Count = interpretationCounts["Morbid Obesity, Grade-IV Obesity"] ?: 0


            val data1 = listOf(
                underWeightCount,
                normalCount,
                grade1Count,
                grade2Count,
                grade3Count,
                grade4Count
            )
            val totalSum = data1.sum()

            val percentages =
                if (totalSum > 0) data1.map { it.toFloat() / totalSum * 100 } else data1.map { 0f }

            val labels1 = arrayOf(
                "Under Weight",
                "Normal",
                "Grade-I Obesity",
                "Grade-II Obesity",
                "Grade-III Obesity",
                "Grade-IV Obesity"
            )
            val colors1 =
                listOf(
                    Color.parseColor("#FFF9C4"), // Light Yellow
                    Color.parseColor("#C8E6C9"), // Light Green
                    Color.parseColor("#BBDEFB"), // Light Blue
                    Color.parseColor("#F8BBD0"), // Light Pink
                    Color.parseColor("#B2EBF2"), // Light Cyan
                    Color.parseColor("#FFCDD2"), // Light Red
                )

            // RecyclerView
            val adapter = FormCountAdapter(this, labels1, data1, percentages, colors1, 0)
            binding.RecyclerViewBMI.adapter = adapter
            binding.RecyclerViewBMI.layoutManager = LinearLayoutManager(this)

            // PieChart
            val entries = ArrayList<PieEntry>()
            for (i in data1.indices) {
                entries.add(PieEntry(data1[i].toFloat(), "")) // empty label for slices
            }

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors1
            dataSet.setDrawValues(true)
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 14f
            val pieData = PieData(dataSet)

            binding.PieChartBMI.data = pieData
            binding.PieChartBMI.description.isEnabled = false
            binding.PieChartBMI.setUsePercentValues(false) // ✅ show counts, not %
            binding.PieChartBMI.isDrawHoleEnabled = true
            binding.PieChartBMI.holeRadius = 40f
            binding.PieChartBMI.transparentCircleRadius = 45f
            binding.PieChartBMI.legend.isEnabled = false
            binding.PieChartBMI.animateY(1000)
            binding.PieChartBMI.visibility = View.VISIBLE

        }

        var generalData: List<Vitals> = emptyList()
        var viewModelData: List<Vitals> = emptyList()

        fun updateCombined() {
            val combined = generalData + viewModelData
            Log.d(
                "Eric",
                "GeneralData=${generalData.size}, ViewModelData=${viewModelData.size}, Combined=${combined.size}"
            )

            val filteredData = when (selectedItem) {
                "Today`s" -> {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    combined.filter { it.createdDate.startsWith(today) }
                }

                "Day Wise" -> {
                    if (selected_date.isNotEmpty()) {
                        val parsedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selected_date)
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                        combined.filter { it.createdDate.startsWith(formattedDate) }
                    } else {
                        val today =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        combined.filter { it.createdDate.startsWith(today) }
                    }
                }

                else -> combined
            }

            Log.d("Eric", "FilteredData=${filteredData.size}")

            val interpretationCounts = filteredData
                .groupBy { it.bmiInterpretation }
                .mapValues { it.value.size }

            Log.d("Eric", "interpretationCounts=$interpretationCounts")

            updateChartAndRecycler(interpretationCounts)
        }

        generalVitalsFormViewModel.allVitals.observe(this) {
            generalData = it
            Log.d("Eric", "generalVitalsFormViewModel emitted → ${it.size} records")
            updateCombined()
        }

        viewModel1.allVitals.observe(this) {
            viewModelData = it
            Log.d("Eric", "viewModel1 emitted → ${it.size} records")
            updateCombined()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setPulseRate(selectedItem: String, selected_date: String) {

        // --- Helper to update chart and recycler ---
        fun updateChartAndRecycler(interpretationCounts: Map<String, Int>) {
            val bradycardiaCount = interpretationCounts["Bradycardia"] ?: 0
            val normalCount = interpretationCounts["Normal"] ?: 0
            val tachycardiaCount = interpretationCounts["Tachycardia"] ?: 0

            val data1 = listOf(bradycardiaCount, normalCount, tachycardiaCount)
            val totalSum = data1.sum()

            val percentages = if (totalSum > 0) {
                data1.map { it.toFloat() / totalSum * 100 }
            } else {
                data1.map { 0f }
            }

            val labels1 = arrayOf("Bradycardia", "Normal", "Tachycardia")
            val colors1 = listOf(
                Color.parseColor("#FFF9C4"), // Light Yellow
                Color.parseColor("#C8E6C9"), // Light Green
                Color.parseColor("#BBDEFB"), // Light Blue
            )

            // RecyclerView
            val adapter = FormCountAdapter(this, labels1, data1, percentages, colors1, 0)
            binding.RecyclerViewPulseRate.adapter = adapter
            binding.RecyclerViewPulseRate.layoutManager = LinearLayoutManager(this)

            // PieChart
            val entries = ArrayList<PieEntry>()
            for (i in data1.indices) {
                entries.add(PieEntry(data1[i].toFloat(), "")) // no label in slice
            }


            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors1
            dataSet.setDrawValues(true)
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 14f
            val pieData = PieData(dataSet)

            binding.PieChartPulseRate.data = pieData
            binding.PieChartPulseRate.description.isEnabled = false
            binding.PieChartPulseRate.setUsePercentValues(false) // ✅ show counts, not %
            binding.PieChartPulseRate.isDrawHoleEnabled = true
            binding.PieChartPulseRate.holeRadius = 40f
            binding.PieChartPulseRate.transparentCircleRadius = 45f
            binding.PieChartPulseRate.legend.isEnabled = false
            binding.PieChartPulseRate.animateY(1000)
            binding.PieChartPulseRate.visibility = View.VISIBLE
        }

        // --- Combine Data from both ViewModels ---
        var generalData: List<Vitals> = emptyList()
        var viewModelData: List<Vitals> = emptyList()

        fun updateCombined() {
            val combined = generalData + viewModelData

            val filteredData = when (selectedItem) {
                "Today`s" -> {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    combined.filter { it.createdDate.startsWith(today) }
                }

                "Day Wise" -> {
                    if (selected_date.isNotEmpty()) {
                        val parsedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selected_date)
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                        combined.filter { it.createdDate.startsWith(formattedDate) }
                    } else {
                        val today =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        combined.filter { it.createdDate.startsWith(today) }
                    }
                }

                else -> combined // "All"
            }

            val interpretationCounts = filteredData
                .groupBy { it.prInterpretation }
                .mapValues { it.value.size }

            updateChartAndRecycler(interpretationCounts)
        }

        // --- Observers for both ViewModels ---
        generalVitalsFormViewModel.allVitals.observe(this) {
            generalData = it
            updateCombined()
        }

        viewModel1.allVitals.observe(this) {
            viewModelData = it
            updateCombined()
        }
    }

    private fun setSymptoms(selectedItem: String, selected_date: String) {
        fun updateChartAndRecycler(interpretationCounts: Map<String, Int>) {
            val underWeightCount = interpretationCounts["Ear"] ?: 0
            val normalCount = interpretationCounts["Nose"] ?: 0
            val grade1Count = interpretationCounts["Throat"] ?: 0

            val data1 = listOf(underWeightCount, normalCount, grade1Count)
            val totalSum = data1.sum()

            val percentages =
                if (totalSum > 0) data1.map { it.toFloat() / totalSum * 100 } else data1.map { 0f }

            val labels1 = arrayOf(
                "Ear",
                "Nose",
                "Throat",
            )
            val colors1 = listOf(
                Color.parseColor("#FFF9C4"), // Light Yellow
                Color.parseColor("#C8E6C9"), // Light Green
                Color.parseColor("#BBDEFB"), // Light Blue
            )

            // RecyclerView
            val adapter = FormCountAdapter(this, labels1, data1, percentages, colors1, 0)
            binding.recyclerViewSymptoms.adapter = adapter
            binding.recyclerViewSymptoms.layoutManager = LinearLayoutManager(this)

            // PieChart
            val entries = ArrayList<PieEntry>()
            for (i in data1.indices) {
                entries.add(PieEntry(data1[i].toFloat(), "")) // keep label empty
            }

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors1
            dataSet.setDrawValues(true)
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 14f
            val pieData = PieData(dataSet)

            binding.pieChartSymptoms.data = pieData
            binding.pieChartSymptoms.description.isEnabled = false
            binding.pieChartSymptoms.setUsePercentValues(false) // ✅ show counts, not %
            binding.pieChartSymptoms.isDrawHoleEnabled = true
            binding.pieChartSymptoms.holeRadius = 40f
            binding.pieChartSymptoms.transparentCircleRadius = 45f
            binding.pieChartSymptoms.legend.isEnabled = false
            binding.pieChartSymptoms.animateY(1000)
            binding.pieChartSymptoms.visibility = View.VISIBLE
        }

        var generalData: List<EntSymptomsEntity> = emptyList()

        fun updateCombined() {
            val combined = generalData

            val filteredData = when (selectedItem) {
                "Today`s" -> {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    combined.filter { it.appCreatedDate.startsWith(today) }
                }

                "Day Wise" -> {
                    if (selected_date.isNotEmpty()) {
                        val parsedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selected_date)
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                        combined.filter { it.appCreatedDate.startsWith(formattedDate) }
                    } else {
                        val today =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        combined.filter { it.appCreatedDate.startsWith(today) }
                    }
                }

                else -> combined
            }

            val interpretationCounts = filteredData
                .groupBy { it.organ }
                .mapValues { it.value.size }

            updateChartAndRecycler(interpretationCounts)
        }

        entOpdDoctorsNoteViewModel.allSymptoms.observe(this) {
            generalData = it
            updateCombined()
        }

    }

    private fun setImpression(selectedItem: String, selected_date: String) {

        // ✅ Normalize impressions into known categories or "Other"
        fun normalizeImpression(impression: String?): String {
            return when (impression) {
                "Otitis media" -> "Otitis media"
                "External otitis" -> "External otitis"
                "Impacted earwax" -> "Impacted earwax"
                "FB in ear" -> "FB in ear"
                "FB in nose" -> "FB in nose"
                "Peritonsillar abscess" -> "Peritonsillar abscess"
                "Facial nerve paralysis" -> "Facial nerve paralysis"
                "Hearing loss" -> "Hearing loss"
                else -> "Other"
            }
        }

        fun updateChartAndRecycler(interpretationCounts: Map<String, Int>) {
            val otitisMedia = interpretationCounts["Otitis media"] ?: 0
            val externalOtisis = interpretationCounts["External otitis"] ?: 0
            val impactedEarwax = interpretationCounts["Impacted earwax"] ?: 0
            val fbEar = interpretationCounts["FB in ear"] ?: 0
            val fbNose = interpretationCounts["FB in nose"] ?: 0
            val peritonsillarAbscess = interpretationCounts["Peritonsillar abscess"] ?: 0
            val facialNerveParalysis = interpretationCounts["Facial nerve paralysis"] ?: 0
            val hearingLoss = interpretationCounts["Hearing loss"] ?: 0
            val other = interpretationCounts["Other"] ?: 0

            val data1 = listOf(
                otitisMedia,
                externalOtisis,
                impactedEarwax,
                fbEar,
                fbNose,
                peritonsillarAbscess,
                facialNerveParalysis,
                hearingLoss,
                other
            )

            val totalSum = data1.sum()
            val percentages = if (totalSum > 0) {
                data1.map { it.toFloat() / totalSum * 100 }
            } else {
                data1.map { 0f }
            }

            val labels1 = arrayOf(
                "Otitis media",
                "External otitis",
                "Impacted earwax",
                "FB in ear",
                "FB in nose",
                "Peritonsillar abscess",
                "Facial nerve paralysis",
                "Hearing loss",
                "Other"
            )

            val colors1 = listOf(
                Color.parseColor("#FFF9C4"), // Light Yellow
                Color.parseColor("#C8E6C9"), // Light Green
                Color.parseColor("#BBDEFB"), // Light Blue
                Color.parseColor("#F8BBD0"), // Light Pink
                Color.parseColor("#B2EBF2"), // Light Cyan
                Color.parseColor("#FFCDD2"), // Light Red
                Color.parseColor("#E1BEE7"), // Light Purple
                Color.parseColor("#D7CCC8"), // Light Brown
                Color.parseColor("#F5F5F5")  // Very Light Gray (for "Other")
            )

            // RecyclerView
            val adapter = FormCountAdapter(this, labels1, data1, percentages, colors1, 0)
            binding.recyclerViewImpression.adapter = adapter
            binding.recyclerViewImpression.layoutManager = LinearLayoutManager(this)

            // PieChart
            val entries = ArrayList<PieEntry>()
            for (i in data1.indices) {
                if (data1[i] > 0) {
                    entries.add(PieEntry(data1[i].toFloat(), ""))
                }
            }

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors1
            dataSet.setDrawValues(true)
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 14f
            val pieData = PieData(dataSet)

            binding.pieChartImpression.data = pieData
            binding.pieChartImpression.description.isEnabled = false
            binding.pieChartImpression.setUsePercentValues(false) // ✅ show counts, not %
            binding.pieChartImpression.isDrawHoleEnabled = true
            binding.pieChartImpression.holeRadius = 40f
            binding.pieChartImpression.transparentCircleRadius = 45f
            binding.pieChartImpression.legend.isEnabled = false
            binding.pieChartImpression.animateY(1000)
            binding.pieChartImpression.visibility = View.VISIBLE

        }

        var generalData: List<EntImpressionEntity> = emptyList()

        fun updateCombined() {
            val combined = generalData

            val filteredData = when (selectedItem) {
                "Today`s" -> {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    combined.filter { it.appCreatedDate.startsWith(today) }
                }

                "Day Wise" -> {
                    if (selected_date.isNotEmpty()) {
                        val parsedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selected_date)
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                        combined.filter { it.appCreatedDate.startsWith(formattedDate) }
                    } else {
                        val today =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        combined.filter { it.appCreatedDate.startsWith(today) }
                    }
                }

                else -> combined
            }

            val interpretationCounts = filteredData
                .groupBy { normalizeImpression(it.impression) }
                .mapValues { it.value.size }

            updateChartAndRecycler(interpretationCounts)
        }

        entOpdDoctorsNoteViewModel.allImpression.observe(this) {
            generalData = it
            updateCombined()
        }
    }

    private fun setSurgery(selectedItem: String, selected_date: String) {

        fun updateChartAndRecycler(filteredData: List<SurgicalNotesEntity>) {
            // Count based on boolean values
            val earWaxRemoval = filteredData.count { it.earWaxRemovalDone }
            val tympanoplasty = filteredData.count { it.tympanoplastyDone }
            val mastoidectomy = filteredData.count { it.mastoidectomyDone }
            val grommetInsertion = filteredData.count { it.grommentInsertionDone }
            val excisionBiopsy = filteredData.count { it.excisionBiopsyDone }
            val foreignBodyRemoval = filteredData.count { it.foreignBodyRemovalDone }
            val other = filteredData.count { !it.other.isNullOrBlank() }

            val data1 = listOf(
                earWaxRemoval,
                tympanoplasty,
                mastoidectomy,
                grommetInsertion,
                excisionBiopsy,
                foreignBodyRemoval,
                other
            )

            val totalSum = data1.sum()
            val percentages = if (totalSum > 0) {
                data1.map { it.toFloat() / totalSum * 100 }
            } else {
                data1.map { 0f }
            }

            val labels1 = arrayOf(
                "Removal of impacted earwax",
                "Tympanoplasty",
                "Mastoidectomy",
                "Grommet insertion",
                "Excision biopsy",
                "Foreign body removal",
                "Other"
            )

            val colors1 = listOf(
                Color.parseColor("#FFF9C4"), // Light Yellow
                Color.parseColor("#C8E6C9"), // Light Green
                Color.parseColor("#BBDEFB"), // Light Blue
                Color.parseColor("#F8BBD0"), // Light Pink
                Color.parseColor("#B2EBF2"), // Light Cyan
                Color.parseColor("#FFCDD2"), // Light Red
                Color.parseColor("#E1BEE7"), // Light Purple
            )

            // RecyclerView
            val adapter = FormCountAdapter(this, labels1, data1, percentages, colors1, 0)
            binding.recyclerViewSurgery.adapter = adapter
            binding.recyclerViewSurgery.layoutManager = LinearLayoutManager(this)

            // PieChart
            val entries = ArrayList<PieEntry>()
            for (i in data1.indices) {
                if (data1[i] > 0) {
                    entries.add(PieEntry(data1[i].toFloat(), ""))
                }
            }

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors1
            dataSet.setDrawValues(true)
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 14f
            val pieData = PieData(dataSet)

            binding.pieChartSurgery.data = pieData
            binding.pieChartSurgery.description.isEnabled = false
            binding.pieChartSurgery.setUsePercentValues(false) // ✅ show counts, not %
            binding.pieChartSurgery.isDrawHoleEnabled = true
            binding.pieChartSurgery.holeRadius = 40f
            binding.pieChartSurgery.transparentCircleRadius = 45f
            binding.pieChartSurgery.legend.isEnabled = false
            binding.pieChartSurgery.animateY(1000)
            binding.pieChartSurgery.visibility = View.VISIBLE

        }

        var generalData: List<SurgicalNotesEntity> = emptyList()

        fun updateCombined() {
            val combined = generalData

            val filteredData = when (selectedItem) {
                "Today`s" -> {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    combined.filter { it.appCreatedDate.startsWith(today) }
                }
                "Day Wise" -> {
                    if (selected_date.isNotEmpty()) {
                        val parsedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selected_date)
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                        combined.filter { it.appCreatedDate.startsWith(formattedDate) }
                    } else {
                        val today =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        combined.filter { it.appCreatedDate.startsWith(today) }
                    }
                }
                else -> combined
            }

            updateChartAndRecycler(filteredData)
        }

        entSurgicalNotesViewModel.All_Ent_Surgical_Follow_ups.observe(this) {
            generalData = it
            updateCombined()
        }
    }

    private fun getPatientData(selectedItem: String, selected_date: String) {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        entPatientIds.clear()
        generalPatientIds.clear()
        eyePatientIds.clear()
        pathologyPatientIds.clear()

        // helper function for filtering by date
        fun <T> filterByDate(response: List<T>, dateExtractor: (T) -> String): List<T> {
            return when (selectedItem) {
                "All" -> response
                "Today`s" -> response.filter { dateExtractor(it).startsWith(todayDate) }
                "Day Wise" -> {
                    if (selected_date.isBlank()) {
                        response.filter { dateExtractor(it).startsWith(todayDate) }
                    } else {
                        val selectedDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val parsedSelectedDate = selectedDateFormat.parse(selected_date)
                        val formattedSelectedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedSelectedDate!!)
                        response.filter { dateExtractor(it).startsWith(formattedSelectedDate) }
                    }
                }
                else -> response
            }
        }

        // ENT
        entOpdDoctorsNoteViewModel.All_Ent_Opd_Doctor_Follow_ups.observe(this) { response ->
            val filtered = filterByDate(response) { it.appCreatedDate.orEmpty() }
            entPatientIds.addAll(filtered.map { it.patientId })
            updateTotalCount()
        }

        entAudiometryViewModel.All_Ent_Audiometry_Follow_ups.observe(this) { response ->
            val filtered = filterByDate(response) { it.appCreatedDate.orEmpty() }
            entPatientIds.addAll(filtered.map { it.patientId })
            updateTotalCount()
        }

        entPreOpDetailsViewModel.All_Ent_Pro_Op_Follow_ups.observe(this) { response ->
            val filtered = filterByDate(response) { it.appCreatedDate }
            entPatientIds.addAll(filtered.map { it.patientId })
            updateTotalCount()
        }

        entSurgicalNotesViewModel.All_Ent_Surgical_Follow_ups.observe(this) { response ->
            val filtered = filterByDate(response) { it.appCreatedDate }
            entPatientIds.addAll(filtered.map { it.patientId })
            updateTotalCount()
        }

        entPostOpNotesViewModel.All_Ent_Post_Follow_ups.observe(this) { response ->
            val filtered = filterByDate(response) { it.appCreatedDate }
            entPatientIds.addAll(filtered.map { it.patientId })
            updateTotalCount()
        }

        // General
        generalVitalsFormViewModel.allVitals.observe(this) { response ->
            val filtered = filterByDate(response) { it.createdDate }
            generalPatientIds.addAll(filtered.map { it.patient_id })
            updateTotalCount()
        }

        generalOpdFormViewModel.allOPD_Investigations.observe(this) { response ->
            val filtered = filterByDate(response) { it.createdDate }
            generalPatientIds.addAll(filtered.map { it.patient_id })
            updateTotalCount()
        }

        // Eye
        eyeVisualAcuityViewModel.allVisualAcuity.observe(this) { response ->
            val filtered = filterByDate(response) { it.createdDate }
            eyePatientIds.addAll(filtered.map { it.patient_id })
            updateTotalCount()
        }

        eyeRefractiveErrorViewModel.allRefractive_Error.observe(this) { response ->
            val filtered = filterByDate(response) { it.createdDate }
            eyePatientIds.addAll(filtered.map { it.patient_id })
            updateTotalCount()
        }

        viewModel1.allEye_OPD_Doctors_Note.observe(this) { response ->
            val filtered = filterByDate(response) { it.createdDate }
            eyePatientIds.addAll(filtered.map { it.patient_id })
            updateTotalCount()
        }

        viewModel1.allEye_Pre_Op_Investigation.observe(this) { response ->
            val filtered = filterByDate(response) { it.createdDate }
            eyePatientIds.addAll(filtered.map { it.patient_id })
            updateTotalCount()
        }

        viewModel1.allEye_Pre_Op_Notes.observe(this) { response ->
            val filtered = filterByDate(response) { it.createdDate }
            eyePatientIds.addAll(filtered.map { it.patient_id })
            updateTotalCount()
        }

        viewModel1.allCataract_Surgery_Notes.observe(this) { response ->
            val filtered = filterByDate(response) { it.createdDate }
            eyePatientIds.addAll(filtered.map { it.patient_id })
            updateTotalCount()
        }

        viewModel1.allEye_Post_Op_AND_Follow_ups.observe(this) { response ->
            val filtered = filterByDate(response) { it.createdDate }
            eyePatientIds.addAll(filtered.map { it.patient_id })
            updateTotalCount()
        }

        // Pathology
        pathologyViewModel.All_PATHOLOGY_Follow_ups.observe(this) { response ->
            val filtered = filterByDate(response) { it.appCreatedDate.orEmpty() }
            pathologyPatientIds.addAll(filtered.map { it.patientId })
            updateTotalCount()
        }
    }

    private fun updateTotalCount() {
        totalEntPatientCount = entPatientIds.size
        totalGeneralPatientCount = generalPatientIds.size
        totalEyePatientCount = eyePatientIds.size
        totalPathologyPatientCount = pathologyPatientIds.size

        // ✅ Pick the biggest one
        totalPatientCount = maxOf(
            totalEntPatientCount,
            totalGeneralPatientCount,
            totalEyePatientCount,
            totalPathologyPatientCount
        )

        binding.TextViewPatientCount.text = totalPatientCount.toString()
        binding.TextViewPatientCount1.text = "Patients Count"

        binding.generalPatientCount.text = totalGeneralPatientCount.toString()
        binding.eyePatientCount.text = totalEyePatientCount.toString()
        binding.entPatientCount.text = totalEntPatientCount.toString()
        binding.pathologyPatientCount.text = totalPathologyPatientCount.toString()

        Log.d("PatientCount", "Unique ENT Patients = $totalEntPatientCount")
        Log.d("PatientCount", "Unique General Patients = $totalGeneralPatientCount")
        Log.d("PatientCount", "Unique Eye Patients = $totalEyePatientCount")
        Log.d("PatientCount", "Unique Pathology Patients = $totalPathologyPatientCount")
        Log.d("PatientCount", "Total (largest group) = $totalPatientCount")
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }
}