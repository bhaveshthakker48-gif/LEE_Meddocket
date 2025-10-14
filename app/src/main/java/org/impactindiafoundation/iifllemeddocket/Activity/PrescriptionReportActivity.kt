package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import org.impactindiafoundation.iifllemeddocket.Adapter.FormCountAdapter
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientPrescriptionRegistrationCombined
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Patient_RegistrationModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Prescription_Model
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleCountModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleDisdributionStatusModel
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPrescriptionReportBinding

class PrescriptionReportActivity:AppCompatActivity() {

    lateinit var binding:ActivityPrescriptionReportBinding
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPrescriptionReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.toolbarEyePreOpNotes.toolbar.title="Reports"
        getViewModel()
        createRoomDatabase()
        combineAndLogData1("", "All", "","","")
    }

    private fun getViewModel() {
        val LLE_MedDocketRespository= LLE_MedDocketRespository()
        val LLE_MedDocketProviderFactory= LLE_MedDocketProviderFactory(LLE_MedDocketRespository,application)
        viewModel= ViewModelProvider(this,LLE_MedDocketProviderFactory).get(LLE_MedDocketViewModel::class.java)
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage(getString(R.string.please_wait))
        }
        sessionManager=SessionManager(this)
    }

    private fun createRoomDatabase() {
        val database = LLE_MedDocket_Room_Database.getDatabase(this)
        val Vital_DAO : Vital_DAO =database.Vital_DAO()
        val VisualAcuity_DAO : VisualAcuity_DAO =database.VisualAcuity_DAO()
        val Refractive_Error_DAO: Refractive_Error_DAO =database.Refractive_Error_DAO()
        val OPD_Investigations_DAO: OPD_Investigations_DAO =database.OPD_Investigations_DAO()
        val Eye_Pre_Op_Notes_DAO : Eye_Pre_Op_Notes_DAO =database.Eye_Pre_Op_Notes_DAO()
        val Eye_Pre_Op_Investigation_DAO : Eye_Pre_Op_Investigation_DAO =database.Eye_Pre_Op_Investigation_DAO()
        val Eye_Post_Op_AND_Follow_ups_DAO : Eye_Post_Op_AND_Follow_ups_DAO =database.Eye_Post_Op_AND_Follow_ups_DAO()
        val Eye_OPD_Doctors_Note_DAO : Eye_OPD_Doctors_Note_DAO =database.Eye_OPD_Doctors_Note_DAO()
        val Cataract_Surgery_Notes_DAO: Cataract_Surgery_Notes_DAO =database.Cataract_Surgery_Notes_DAO()
        val Patient_DAO: PatientDao =database.PatientDao()
        val Image_Upload_DAO: Image_Upload_DAO =database.Image_Upload_DAO()
        val Registration_DAO: Registration_DAO = database.Registration_DAO()
        val Prescription_DAO: Prescription_DAO = database.Prescription_DAO()
        val SynTable_DAO: SynTable_DAO = database.SynTable_DAO()
        val Final_Prescription_DAO: Final_Prescription_DAO =database.Final_Prescription_DAO()
        val SpectacleDisdributionStatus_DAO: SpectacleDisdributionStatus_DAO =database.SpectacleDisdributionStatus_DAO()
        val CurrentInventory_DAO: CurrentInventory_DAO =database.CurrentInventory_DAO()
        val InventoryUnit_DAO: InventoryUnit_DAO =database.InventoryUnit_DAO()
        val CreatePrescriptionDAO: CreatePrescriptionDAO =database.CreatePrescriptionDAO()
        val Image_Prescription_DAO: Image_Prescription_DAO =database.Image_Prescription_DAO()
        val FinalPrescriptionDrugDAO: FinalPrescriptionDrugDAO =database.FinalPrescriptionDrugDAO()
        val repository = LLE_MedDocket_Repository(Vital_DAO, VisualAcuity_DAO, Refractive_Error_DAO, OPD_Investigations_DAO, Eye_Pre_Op_Notes_DAO, Eye_Pre_Op_Investigation_DAO, Eye_Post_Op_AND_Follow_ups_DAO, Eye_OPD_Doctors_Note_DAO, Cataract_Surgery_Notes_DAO, Patient_DAO,Image_Upload_DAO,Registration_DAO,Prescription_DAO,Final_Prescription_DAO,SpectacleDisdributionStatus_DAO,SynTable_DAO,CurrentInventory_DAO,InventoryUnit_DAO,CreatePrescriptionDAO,Image_Prescription_DAO,FinalPrescriptionDrugDAO,database)
        viewModel1 = ViewModelProvider(this, LLE_MedDocket_ViewModelFactory(repository)).get(LLE_MedDocket_ViewModel::class.java)
    }

    private fun combineAndLogData1(
        selectedCondition: String,
        selectedDuration: String?,
        selectedDates: String?,
        selectedSpectacleType: String,
        searchText:String
    ) {
        viewModel1.allRegistration.observe(this, Observer { registrationData ->
            viewModel1.allSpectacleDisdributionStatus.observe(this, Observer { spectacleData ->
                viewModel1.allPrescription.observe(this, Observer {
                        prescription->
                    val filteredData = when (selectedCondition) {
                        "Spectacle Given" -> combineAndFilterData(registrationData, spectacleData,prescription) { it.SpectacleDisdributionStatusModel.spectacle_given }
                        "Patient Call Again" -> combineAndFilterData(registrationData, spectacleData,prescription) { it.SpectacleDisdributionStatusModel.patient_call_again }
                        else -> combineData(registrationData, spectacleData,prescription)
                    }.filter {
                        it.registrationData.patient_id.toString().contains(searchText, ignoreCase = true) ||
                                it.registrationData.aadharno.contains(searchText, ignoreCase = true) ||
                                it.registrationData.fname.contains(searchText, ignoreCase = true) ||
                                it.registrationData.lname.contains(searchText, ignoreCase = true)
                    }
                    val selectedSpectacleTypeData = when (selectedSpectacleType) {
                        "Single Vision" -> filteredData.filter { it.prescription?.presc_type == "Single Vision" }
                        "Single Vision (HP)" -> filteredData.filter { it.prescription?.presc_type == "Single Vision (HP)" }
                        "Bifocal" -> filteredData.filter { it.prescription?.presc_type == "Bifocal" }
                        "Bifocal (HP)" -> filteredData.filter { it.prescription?.presc_type == "Bifocal (HP)" }
                        // Add more cases for other spectacle types if needed
                        else -> filteredData
                    }
                    Log.d(ConstantsApp.TAG,"filteredData=>"+filteredData)
                })
            })
        })
    }

    private inline fun combineAndFilterData(
        registrationData: List<Patient_RegistrationModel>,
        spectacleData: List<SpectacleDisdributionStatusModel>,
        prescription:List<Prescription_Model>,
        predicate: (PatientPrescriptionRegistrationCombined) -> Boolean
    ): List<PatientPrescriptionRegistrationCombined> {
        val combinedData = combineData(registrationData, spectacleData,prescription)
        return combinedData.filter(predicate)
    }

    private fun combineData(
        registrationData: List<Patient_RegistrationModel>,
        spectacleData: List<SpectacleDisdributionStatusModel>,
        prescriptionData: List<Prescription_Model>
    ): List<PatientPrescriptionRegistrationCombined> {
        val recentSpectacleMap = mutableMapOf<Int, SpectacleDisdributionStatusModel>()
        val prescriptionMap = mutableMapOf<Int, Prescription_Model>()
        for (spectacle in spectacleData) {
            if (!recentSpectacleMap.containsKey(spectacle.patient_id) ||
                spectacle.app_createdDate > recentSpectacleMap[spectacle.patient_id]?.app_createdDate ?: ""
            ) {
                recentSpectacleMap[spectacle.patient_id] = spectacle
            }
        }
        for (prescription in prescriptionData) {
            prescriptionMap[prescription.patient_id] = prescription
        }
        val combinedList = mutableListOf<PatientPrescriptionRegistrationCombined>()

        for (registration in registrationData) {
            val mostRecentSpectacle = recentSpectacleMap.remove(registration.patient_id)
            val prescription = prescriptionMap[registration.patient_id]
            if (mostRecentSpectacle != null) {
                combinedList.add(
                    PatientPrescriptionRegistrationCombined(
                        registration,
                        mostRecentSpectacle,
                        prescription
                    )
                )
            }
        }

        var counts = SpectacleCountModel(
            patientNotComeCount = combinedList.count { it.SpectacleDisdributionStatusModel?.patient_not_come == true },
            patientCallAgainCount = combinedList.count { it.SpectacleDisdributionStatusModel?.patient_call_again == true },
            spectacleGivenCount = combinedList.count { it.SpectacleDisdributionStatusModel?.spectacle_given == true },
            spectacleNotMatchingCount = combinedList.count { it.SpectacleDisdributionStatusModel?.spectacle_not_matching == true },
            spectacleNotReceivedCount = combinedList.count { it.SpectacleDisdributionStatusModel?.spectacle_not_received == true },
            singleVisionCount = combinedList.count { it.SpectacleDisdributionStatusModel?.spectacle_given == true && it.prescription?.presc_type == "Single Vision" },
            singleVisionHPCount = combinedList.count { it.SpectacleDisdributionStatusModel?.spectacle_given == true && it.prescription?.presc_type == "Single Vision (HP)" },
            bifocalCount = combinedList.count { it.SpectacleDisdributionStatusModel?.spectacle_given == true && it.prescription?.presc_type == "Bifocal" },
            bifocalHPCount = combinedList.count { it.SpectacleDisdributionStatusModel?.spectacle_given == true && it.prescription?.presc_type == "Bifocal (HP)" }
        )
        combinedList.forEach { it.counts = counts }
        setBarChart(counts.spectacleGivenCount,counts.spectacleNotReceivedCount,counts.spectacleNotMatchingCount,counts.patientNotComeCount,counts.patientCallAgainCount)
        setPieChart(counts.singleVisionCount,counts.singleVisionHPCount,counts.bifocalCount,counts.bifocalHPCount)
        return combinedList
    }

    private fun setPieChart(singleVisionCount: Int, singleVisionHPCount: Int, bifocalCount: Int, bifocalHPCount: Int) {
        val singleVisionCount = singleVisionCount ?: 0
        val singleVisionHPCount = singleVisionHPCount ?: 0
        val bifocalCount = bifocalCount ?: 0
        val bifocalHPCount = bifocalHPCount ?: 0
        val data1 = listOf(singleVisionCount, singleVisionHPCount, bifocalCount, bifocalHPCount)
        val totalSum = data1.sum()
        val percentages = data1.map { it.toFloat() / totalSum * 100 }
        val colors1 = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GRAY, Color.DKGRAY, Color.LTGRAY,Color.BLACK)
        val labels1 = arrayOf("Single Vision", "Single Vision(HP)", "Bifocal", "Bifocal (HP)")
        val adapter = FormCountAdapter(this,labels1, data1, percentages, colors1,0)
        binding.RecyclerViewBloodPressure.adapter = adapter
        binding.RecyclerViewBloodPressure.layoutManager = LinearLayoutManager(this)
        if (singleVisionCount == 0 && singleVisionHPCount == 0 && bifocalCount == 0 && bifocalHPCount == 0) {
                binding.PieChart.visibility= View.VISIBLE
                val entries1 = ArrayList<PieEntry>()
                entries1.add(PieEntry(singleVisionCount.toFloat(), ""))
                entries1.add(PieEntry(1f, ""))
                entries1.add(PieEntry(bifocalCount.toFloat(), ""))
                entries1.add(PieEntry(bifocalHPCount.toFloat(), ""))

                val colors = mutableListOf<Int>(
                    Color.rgb(255, 102, 102), // Hypotension - Red
                    Color.rgb(102, 255, 102), // Normal - Green
                    Color.rgb(255, 255, 102), // Prehypertension - Yellow
                    Color.rgb(255, 204, 102), // Stage 1 Hypertension - Orange
                    Color.rgb(102, 102, 255)  // Stage 2 Hypertension - Blue
                )
                val dataSet = PieDataSet(entries1, "Pie Chart")
                dataSet.colors = colors
                dataSet.setDrawValues(false)
                dataSet.setValueTextColor(Color.rgb(102, 255, 102))

                val data = PieData(dataSet)
                binding.PieChart.data = data
                binding.PieChart.description.isEnabled = false
                binding.PieChart.setUsePercentValues(true)
                binding.PieChart.isDrawHoleEnabled = true
                binding.PieChart.setHoleColor(android.R.color.transparent)
                binding.PieChart.setTransparentCircleColor(android.R.color.transparent)
                binding.PieChart.setTransparentCircleAlpha(0)
                binding.PieChart.holeRadius = 40f
                binding.PieChart.transparentCircleRadius = 45f
                binding.PieChart.legend.isEnabled = false
                binding.PieChart.animateY(1000)
            } else {
                binding.PieChart.visibility= View.VISIBLE
                val entries1 = ArrayList<PieEntry>()
                entries1.add(PieEntry(singleVisionCount.toFloat(), "Single Vision"))
                entries1.add(PieEntry(singleVisionHPCount.toFloat(), "Single Vision (HP)"))
                entries1.add(PieEntry(bifocalCount.toFloat(), "Bifocal"))
                entries1.add(PieEntry(bifocalHPCount.toFloat(), "Bifocal (HP)"))
                val colors = mutableListOf<Int>(
                    Color.rgb(255, 102, 102), // Hypotension - Red
                    Color.rgb(102, 255, 102), // Normal - Green
                    Color.rgb(255, 255, 102), // Prehypertension - Yellow
                    Color.rgb(255, 204, 102), // Stage 1 Hypertension - Orange
                    Color.rgb(102, 102, 255)  // Stage 2 Hypertension - Blue
                )
                val dataSet = PieDataSet(entries1, "Pie Chart")
                dataSet.colors = colors
                val data = PieData(dataSet)
                binding.PieChart.data = data
                binding.PieChart.description.isEnabled = false
                binding.PieChart.setUsePercentValues(true)
                binding.PieChart.isDrawHoleEnabled = true
                binding.PieChart.setHoleColor(android.R.color.transparent)
                binding.PieChart.setTransparentCircleColor(android.R.color.transparent)
                binding.PieChart.setTransparentCircleAlpha(0)
                binding.PieChart.holeRadius = 40f
                binding.PieChart.transparentCircleRadius = 45f
                binding.PieChart.legend.isEnabled = false
                binding.PieChart.animateY(1000)
            }
    }

    private fun setBarChart(
        spectacleGivenCount: Int,
        spectacleNotReceivedCount: Int,
        spectacleNotMatchingCount: Int,
        patientNotComeCount: Int,
        patientCallAgainCount: Int
    ) {
        val Total_forms=spectacleGivenCount+spectacleNotReceivedCount+spectacleNotMatchingCount+patientNotComeCount+patientCallAgainCount
        val data1 = listOf(spectacleGivenCount, spectacleNotReceivedCount, spectacleNotMatchingCount, patientNotComeCount,
            patientCallAgainCount)
        val entries1 = ArrayList<BarEntry>()
        for ((index, value) in data1.withIndex()) {
            val xValue = (index + 1).toFloat()
            val yValue = value.toFloat()
            entries1.add(BarEntry(xValue, yValue))
        }
        val entries = ArrayList<BarEntry>()
        for ((index, value) in data1.withIndex()) {
            val xValue = (index + 1).toFloat()
            val yValue = value.toFloat()
            entries.add(BarEntry(xValue, yValue))
        }
        val totalSum = data1.sum()
        val percentages = data1.map { it.toFloat() / totalSum * 100 }
        val colors1 = listOf(
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GRAY, Color.DKGRAY, Color.LTGRAY,
            Color.BLACK)
        val labels1 = arrayOf("Spectacle Given", "Spectacles not arrived", "Incorrect spectacles received", "Patient did not come", "Patient call again")
        val adapter = FormCountAdapter(this,labels1, data1, percentages, colors1,Total_forms)
        binding.RecyclerViewFormBarChart.adapter = adapter
        binding.RecyclerViewFormBarChart.layoutManager = LinearLayoutManager(this)
        val colors = ArrayList<Int>()
        colors.add(Color.RED)
        colors.add(Color.GREEN)
        colors.add(Color.BLUE)
        colors.add(Color.YELLOW)
        colors.add(Color.MAGENTA)
        colors.add(Color.CYAN)
        colors.add(Color.GRAY)
        colors.add(Color.DKGRAY)
        colors.add(Color.LTGRAY)
        val barDataSet = BarDataSet(entries1, "")
        barDataSet.colors = colors
        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f
        val xAxis: XAxis = binding.barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        binding.barChart.isScaleXEnabled = true
        binding.barChart.isScaleYEnabled = false // Optional, depending on your preference
        binding.barChart.data = barData
        binding.barChart.setFitBars(true)
        binding.barChart.animateY(3000);
        binding.barChart.invalidate()  // Refresh the char
    }
}