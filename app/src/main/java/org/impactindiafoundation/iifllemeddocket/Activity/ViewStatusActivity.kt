package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.Adapter.CombinedDataAdapter
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.Adapter.PrescriptionGridAdapter
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CombinedDataResult
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientPrescriptionRegistrationCombined
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Patient_RegistrationModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Prescription_Model
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleCountModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleDisdributionStatusModel
import org.impactindiafoundation.iifllemeddocket.Model.ViewStatusModel.PrescriptionSpectacleCount
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityViewStatusBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ViewStatusActivity:AppCompatActivity(), TextWatcher {

    lateinit var binding:ActivityViewStatusBinding
    lateinit var customDropDownAdapter: CustomDropDownAdapter
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    var spectacle_given: Boolean=false
    var spectacle_not_matching: Boolean=false
    var spectacle_not_received: Boolean=false
    var patient_call_again: Boolean=false
    var patient_not_come: Boolean=false
    var selectedCondition:String=""
    var selectedDuration:String=""
    var selectedSpectacleType:String=""
    var selectedDates:String=""
    var PrescriptionSpectacleCountArrayList:ArrayList<PrescriptionSpectacleCount>?=null
    var ReportArrayList:ArrayList<String>?=null
    var StatusArrayList:ArrayList<String>?=null
    var registrationData: List<Patient_RegistrationModel>?=null
    var SpectacleTypeArrayList:ArrayList<String>?=null
    var ReportDatesArrayList:ArrayList<String>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityViewStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        getViewModel()
        createRoomDatabase()

        binding.SpinnerCondition1.visibility=View.GONE
        binding.SpinnerDuration1.visibility=View.GONE
        binding.TextViewNoDataAvailable.visibility=View.GONE

        ReportArrayList= ArrayList()
        ReportArrayList!!.add("Today`s")
        ReportArrayList!!.add("All")
        ReportArrayList!!.add("Day Wise")

        SpectacleTypeArrayList= ArrayList()
        SpectacleTypeArrayList!!.add("Select")
        SpectacleTypeArrayList!!.add("Single Vision")
        SpectacleTypeArrayList!!.add("Single Vision (HP)")
        SpectacleTypeArrayList!!.add("Bifocal")
        SpectacleTypeArrayList!!.add("Bifocal (HP)")

        customDropDownAdapter = CustomDropDownAdapter(this, SpectacleTypeArrayList!!)
        binding.SpinnerCondition1!!.adapter=customDropDownAdapter
        customDropDownAdapter = CustomDropDownAdapter(this, ReportArrayList!!)
        binding.SpinnerDuration!!.adapter=customDropDownAdapter

        ReportDatesArrayList= ArrayList()

        StatusArrayList=ArrayList()
        StatusArrayList!!.add("Select")
        StatusArrayList!!.add("Prescription spectacles GIVEN to the patient of Type DD")
        StatusArrayList!!.add("Spectacles NOT YET RECEIVED hence patient CALLED AGAIN")
        StatusArrayList!!.add("Prescription spectacles NOT GIVEN as received spectacles were NOT MATCHING prescription")
        StatusArrayList!!.add("Prescription spectacles POSTED as PATIENT DID NOT COME")
        StatusArrayList!!.add("Prescription spectacles NOT GIVEN as spectacles ordered but NOT RECEIVED")

        customDropDownAdapter = CustomDropDownAdapter(this, StatusArrayList!!)
        binding.SpinnerCondition!!.adapter=customDropDownAdapter

        combineAndLogData1("", "All", "","","")
       setDates()

        binding.SpinnerDuration.onItemSelectedListener=object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as? String
                hideSpinner(position,binding.SpinnerDuration,binding.SpinnerDuration1)
                selectedDuration= (parent?.getItemAtPosition(position) as? String)!!
                combineAndLogData(selectedCondition, selectedDuration, selectedDates, selectedSpectacleType,"")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerDuration1.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 selectedDates= (parent?.getItemAtPosition(position) as? String)!!
                combineAndLogData(
                    selectedCondition,
                    selectedDuration,
                    selectedDates,
                    selectedSpectacleType,
                    ""
                )
                Log.d(ConstantsApp.TAG,"selectedDates=>"+selectedDates)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerCondition.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as? String
                hideSpinner(position,binding.SpinnerCondition,binding.SpinnerCondition1)
                when(position) {
                    1-> {
                         selectedCondition="Spectacle Given"
                        combineAndLogData(
                            selectedCondition,
                            selectedDuration,
                            selectedDates,
                            selectedSpectacleType,""
                        )
                    }2-> {
                        selectedCondition="Patient Call Again"
                        combineAndLogData(
                            selectedCondition,
                            selectedDuration,
                            selectedDates,
                            selectedSpectacleType,""
                        )
                    }3-> {
                        selectedCondition="Not Matching"
                        combineAndLogData(
                            selectedCondition,
                            selectedDuration,
                            selectedDates,
                            selectedSpectacleType,""
                        )
                    }4-> {
                        selectedCondition="Patient Not Come"
                        combineAndLogData(
                            selectedCondition,
                            selectedDuration,
                            selectedDates,
                            selectedSpectacleType,""
                        )
                    }5-> {
                        selectedCondition="Spectacle Not Received"
                        combineAndLogData(
                            selectedCondition,
                            selectedDuration,
                            selectedDates,
                            selectedSpectacleType,""
                        )
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.SpinnerCondition1.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 selectedSpectacleType = (parent?.getItemAtPosition(position) as? String).toString()
                Log.d(ConstantsApp.TAG,"selectedItem in SpinnerCondition1=>"+selectedSpectacleType)
                combineAndLogData(selectedCondition, selectedDuration, selectedDates,selectedSpectacleType,"")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.EditTextSearch.addTextChangedListener(this)
    }

    private fun hideSpinner(position: Int, spinnerCondition: Spinner, spinnerCondition1: Spinner) {
        when(spinnerCondition) {
            binding.SpinnerCondition-> {
                when(position) {
                    1-> {
                        spinnerCondition1.visibility=View.VISIBLE
                    }
                    else-> {
                        spinnerCondition1.visibility=View.GONE
                    }
                }
            }

            binding.SpinnerDuration-> {
                when(position) {
                    2-> {
                        spinnerCondition1.visibility=View.VISIBLE
                        val layoutParams = binding.SpinnerCondition1.layoutParams as LinearLayout.LayoutParams
                        layoutParams.weight = 1f // Set the weight to 1 for equal division
                        layoutParams.width = 0 // Set width to 0 to use weight
                        binding.SpinnerCondition1.layoutParams = layoutParams

                        val layoutParams1 = binding.SpinnerDuration1.layoutParams as LinearLayout.LayoutParams
                        layoutParams1.weight = 1f // Set the weight to 1 for equal division
                        layoutParams1.width = 0 // Set width to 0 to use weight
                        binding.SpinnerDuration1.layoutParams = layoutParams1
                    }else-> {
                        spinnerCondition1.visibility=View.GONE
                    }
                }
            }
        }
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

    private fun combineAndLogData(
        selectedCondition: String,
        selectedDuration: String?,
        selectedDates: String?,
        selectedSpectacleType: String,
        searchText:String
    ) {
        viewModel1.allRegistration.observe(this, Observer { registrationData ->
            viewModel1.allSpectacleDisdributionStatus.observe(this, Observer { spectacleData ->
                viewModel1.allPrescription.observe(this, Observer { prescription->
                    val selectedItem = binding.SpinnerCondition.selectedItem as? String
                    val filteredData = when (selectedCondition) {
                        "Spectacle Given" -> combineAndFilterData(registrationData, spectacleData,prescription) { it.SpectacleDisdributionStatusModel!!.spectacle_given }
                        "Patient Call Again" -> combineAndFilterData(registrationData, spectacleData,prescription) { it.SpectacleDisdributionStatusModel!!.patient_call_again }
                        "Patient Not Come" -> combineAndFilterData(registrationData, spectacleData,prescription) { it.SpectacleDisdributionStatusModel!!.patient_not_come }
                        "Not Matching" -> combineAndFilterData(registrationData, spectacleData,prescription) { it.SpectacleDisdributionStatusModel!!.spectacle_not_matching }
                        "Spectacle Not Received" -> combineAndFilterData(registrationData, spectacleData,prescription) { it.SpectacleDisdributionStatusModel!!.spectacle_not_received }
                        // Add more cases for other spinner items if needed
                        else -> emptyList()
                        // Default to showing all data
                    }.filter {
                        it.registrationData.patient_id.toString().contains(searchText, ignoreCase = true) ||
                                it.registrationData.aadharno!!.contains(searchText, ignoreCase = true) ||
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
                    if (selectedSpectacleTypeData.isEmpty()) {
                        binding.TextViewNoDataAvailable.visibility = View.VISIBLE
                    } else {
                        binding.TextViewNoDataAvailable.visibility = View.GONE
                    }
                    Log.d(ConstantsApp.TAG,"filteredData=>"+filteredData)

                    when(selectedDuration) {
                        "All"-> {
                            val sortedCombinedData = selectedSpectacleTypeData.sortedByDescending { it.SpectacleDisdributionStatusModel.app_createdDate }
                            setDataToRecycleView(sortedCombinedData)
                        }
                        "Today`s"-> {
                            val sortedCombinedData = selectedSpectacleTypeData.sortedByDescending { it.SpectacleDisdributionStatusModel.app_createdDate }
                            val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords: List<PatientPrescriptionRegistrationCombined> = sortedCombinedData.filter { it.SpectacleDisdributionStatusModel.app_createdDate?.startsWith(todayDate) == true }
                            setDataToRecycleView(todayRecords)
                        }
                        "Day Wise"-> {
                            when(selectedDates) {
                                ""-> {
                                    val sortedCombinedData = selectedSpectacleTypeData.sortedByDescending { it.SpectacleDisdributionStatusModel.app_createdDate }
                                    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                    val todayRecords: List<PatientPrescriptionRegistrationCombined> = sortedCombinedData.filter { it.SpectacleDisdributionStatusModel.app_createdDate?.startsWith(todayDate) == true }
                                    setDataToRecycleView(todayRecords)
                                }
                                selectedDates-> {
                                    val sortedCombinedData = selectedSpectacleTypeData.sortedByDescending { it.SpectacleDisdributionStatusModel.app_createdDate }
                                    val selectedDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    val parsedSelectedDate = selectedDateFormat.parse(selectedDates)
                                    val formattedSelectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedSelectedDate)
                                    val todayRecords: List<PatientPrescriptionRegistrationCombined> = sortedCombinedData.filter { it.SpectacleDisdributionStatusModel.app_createdDate?.startsWith(formattedSelectedDate) == true
                                    }
                                    setDataToRecycleView(todayRecords)
                                }
                            }
                        }
                    }
                })
            })
        })
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
                viewModel1.allPrescription.observe(this, Observer { prescription->
                    val selectedItem = binding.SpinnerCondition.selectedItem as? String
                    val filteredData = when (selectedCondition) {
                         "Spectacle Given" -> combineAndFilterData(registrationData, spectacleData,prescription) { it.SpectacleDisdributionStatusModel.spectacle_given }
                         "Patient Call Again" -> combineAndFilterData(registrationData, spectacleData,prescription) { it.SpectacleDisdributionStatusModel.patient_call_again }
                         // Add more cases for other spinner items if needed
                         else -> combineData(registrationData, spectacleData,prescription)
                     // Default to showing all data
                     }.filter {
                         it.registrationData.patient_id.toString().contains(searchText, ignoreCase = true) ||
                                 it.registrationData.aadharno!!.contains(searchText, ignoreCase = true) ||
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
                    if (selectedSpectacleTypeData.isEmpty()) {
                        binding.TextViewNoDataAvailable.visibility = View.VISIBLE
                    } else {
                        binding.TextViewNoDataAvailable.visibility = View.GONE
                    }
                    Log.d(ConstantsApp.TAG,"filteredData=>"+filteredData)
                    when(selectedDuration) {
                        "All"-> {
                            val sortedCombinedData = selectedSpectacleTypeData.sortedByDescending { it.SpectacleDisdributionStatusModel.app_createdDate }
                            setDataToRecycleView(sortedCombinedData)
                        }
                        "Today`s"-> {
                            val sortedCombinedData = selectedSpectacleTypeData.sortedByDescending { it.SpectacleDisdributionStatusModel.app_createdDate }
                            val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val todayRecords: List<PatientPrescriptionRegistrationCombined> = sortedCombinedData.filter { it.SpectacleDisdributionStatusModel.app_createdDate?.startsWith(todayDate) == true }
                            setDataToRecycleView(todayRecords)
                        }
                        "Day Wise"-> {
                            when(selectedDates) {
                                ""-> {
                                    val sortedCombinedData = selectedSpectacleTypeData.sortedByDescending { it.SpectacleDisdributionStatusModel.app_createdDate }
                                    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                    val todayRecords: List<PatientPrescriptionRegistrationCombined> = sortedCombinedData.filter { it.SpectacleDisdributionStatusModel.app_createdDate?.startsWith(todayDate) == true }
                                    setDataToRecycleView(todayRecords)
                                }
                                selectedDates-> {
                                    val sortedCombinedData = selectedSpectacleTypeData.sortedByDescending { it.SpectacleDisdributionStatusModel.app_createdDate }
                                    val selectedDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    val parsedSelectedDate = selectedDateFormat.parse(selectedDates)
                                    val formattedSelectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedSelectedDate)
                                    val todayRecords: List<PatientPrescriptionRegistrationCombined> = sortedCombinedData.filter { it.SpectacleDisdributionStatusModel.app_createdDate?.startsWith(formattedSelectedDate) == true }
                                    setDataToRecycleView(todayRecords)
                                }
                            }
                        }
                    }
                })
            })
        })
    }

    private fun setDataToRecycleView(sortedCombinedData: List<PatientPrescriptionRegistrationCombined>) {
        val adapter = CombinedDataAdapter(this, sortedCombinedData)
        binding.RecyclerViewStatus.adapter = adapter
        binding.RecyclerViewStatus.layoutManager = LinearLayoutManager(this)
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

        PrescriptionSpectacleCountArrayList= ArrayList()
        PrescriptionSpectacleCountArrayList!!.add(PrescriptionSpectacleCount("","Total Prescription Spectacles Ordered",counts.spectacleGivenCount+counts.spectacleNotReceivedCount+counts.spectacleNotMatchingCount+counts.patientNotComeCount,0,0,0,0))
        PrescriptionSpectacleCountArrayList!!.add(PrescriptionSpectacleCount("","Given",counts.spectacleGivenCount,counts.singleVisionCount,counts.singleVisionHPCount,counts.bifocalCount,counts.bifocalHPCount))
        PrescriptionSpectacleCountArrayList!!.add(PrescriptionSpectacleCount("","Spectacles not arrived",counts.spectacleNotReceivedCount,0,0,0,0))
        PrescriptionSpectacleCountArrayList!!.add(PrescriptionSpectacleCount("","Incorrect spectacles received",counts.spectacleNotMatchingCount,0,0,0,0))
        PrescriptionSpectacleCountArrayList!!.add(PrescriptionSpectacleCount("","Patient did not come",counts.patientNotComeCount,0,0,0,0))

        val adapter = PrescriptionGridAdapter(this, PrescriptionSpectacleCountArrayList!!)
        binding.GridViewCount.adapter = adapter
        return combinedList
    }

    private fun setDates() {
        viewModel1.allSpectacleDisdributionStatus.observe(this, Observer { vitalList->
            for (data in vitalList) {
                    Log.d(ConstantsApp.TAG,"dates=>"+data.app_createdDate)
                    val datesInOriginalFormat = mutableListOf(data.app_createdDate)
                 val uniqueDatesSet = HashSet<String>(ReportDatesArrayList!!)
                    uniqueDatesSet.addAll(convertDatesToDesiredFormat(datesInOriginalFormat))
                 ReportDatesArrayList!!.clear()
                    ReportDatesArrayList!!.addAll(uniqueDatesSet)
                    customDropDownAdapter = CustomDropDownAdapter(this, ReportDatesArrayList!!)
                    binding.SpinnerDuration1!!.adapter=customDropDownAdapter
            }
        })
    }

    private fun convertDatesToDesiredFormat(originalDates: List<String>): ArrayList<String> {
        val desiredDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val convertedDates = ArrayList<String>()
        for (originalDate in originalDates) {
            try {
                val parsedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(originalDate)
                convertedDates.add(desiredDateFormat.format(parsedDate!!))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return convertedDates
    }

    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
        val searchText = charSequence.toString().trim()
        combineAndLogData(selectedCondition, selectedDuration, selectedDates, selectedSpectacleType,searchText)
    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        val searchText = charSequence.toString().trim()
        combineAndLogData(selectedCondition, selectedDuration, selectedDates, selectedSpectacleType,searchText)
    }

    override fun afterTextChanged(s: Editable?) {
        val searchText = s.toString().trim()
        combineAndLogData(selectedCondition, selectedDuration, selectedDates, selectedSpectacleType,searchText)
    }
}