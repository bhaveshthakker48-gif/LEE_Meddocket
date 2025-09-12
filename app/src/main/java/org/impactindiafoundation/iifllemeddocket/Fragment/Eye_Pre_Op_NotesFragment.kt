package org.impactindiafoundation.iifllemeddocket.Fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.Adapter.Adapter_Pre_Op_Notes
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel

import org.impactindiafoundation.iifllemeddocket.databinding.FragmentVitalBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Eye_Pre_Op_NotesFragment:Fragment() {

    lateinit var binding: FragmentVitalBinding

    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager

    var ReportArrayList:ArrayList<String>?=null
    var ReportDatesArrayList:ArrayList<String>?=null

    lateinit var customDropDownAdapter: CustomDropDownAdapter

    var patientId=0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentVitalBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getViewModel()
        createRoomDatabase()

        binding.LinearLayout1.visibility=View.GONE

        ReportArrayList= ArrayList()
        ReportArrayList!!.add("Today`s")
        ReportArrayList!!.add("All")
        ReportArrayList!!.add("Day Wise")

        ReportDatesArrayList= ArrayList()



        customDropDownAdapter = CustomDropDownAdapter(requireContext(), ReportArrayList!!)
        binding.spinnerReportType!!.adapter=customDropDownAdapter

        customDropDownAdapter = CustomDropDownAdapter(requireContext(), ReportArrayList!!)
        binding.spinnerReportType1!!.adapter=customDropDownAdapter
        // val patientID = arguments?.getString("patientID")

        binding.spinnerReportType.onItemSelectedListener=object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = binding.spinnerReportType.selectedItem.toString()

                updateLinearLayoutVisibility(selectedItem,binding.LinearLayout1,binding.CardView,binding.spinnerReportType,binding.spinnerReportType1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.spinnerReportType1.onItemSelectedListener=object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = binding.spinnerReportType1.selectedItem.toString()

                updateLinearLayoutVisibility(selectedItem,binding.LinearLayout1,binding.CardView,binding.spinnerReportType1,binding.spinnerReportType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.spinnerReportDayWise.onItemSelectedListener=object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selected_date=binding.spinnerReportDayWise.selectedItem.toString()
                Log.d(ConstantsApp.TAG,"selected_date on spinnerReportDayWise selected"+selected_date)
                GetVitalData("Day Wise",selected_date)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        try {
            val patientData: PatientDataLocal? = arguments?.getParcelable("patientData")



            if (patientData != null) {
                // The patientData is not null, you can safely access its properties
                Log.d(ConstantsApp.TAG, "patientID: ${patientData.patientId}")
                patientId = patientData.patientId

                getAllDataPatientIDWise(patientId)
                setDates(patientId)
                // Use patientId as needed...
            } else {
                // Handle the case where patientData is null
                Log.d(ConstantsApp.TAG, "patientData is null")
            }


        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }






    }

    private fun setDates(patientId: Int) {
        viewModel1.allEye_Pre_Op_Notes.observe(viewLifecycleOwner, Observer {
                vitalList->

            for (data in vitalList)
            {
                if (data.patient_id==patientId)
                {
                    Log.d(ConstantsApp.TAG,"dates=>"+data.createdDate)

                    val datesInOriginalFormat = mutableListOf(data.createdDate)

                    // Convert dates to "dd/MM/yyyy" format and create a Set to filter out duplicates
                    val uniqueDatesSet = HashSet<String>(ReportDatesArrayList!!)
                    uniqueDatesSet.addAll(convertDatesToDesiredFormat(datesInOriginalFormat))

                    // Update ReportDatesArrayList with the unique dates
                    ReportDatesArrayList!!.clear()
                    ReportDatesArrayList!!.addAll(uniqueDatesSet)


                    customDropDownAdapter = CustomDropDownAdapter(requireContext(), ReportDatesArrayList!!)
                    binding.spinnerReportDayWise!!.adapter=customDropDownAdapter
                }
            }
        })
    }

    private fun convertDatesToDesiredFormat(originalDates: List<String>): ArrayList<String> {
        val desiredDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val convertedDates = ArrayList<String>()

        for (originalDate in originalDates) {
            try {
                // Parse the original date
                val parsedDate =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(originalDate)

                // Convert to the desired format and add to the ArrayList
                convertedDates.add(desiredDateFormat.format(parsedDate!!))
            } catch (e: Exception) {
                // Handle parsing exceptions if needed
                e.printStackTrace()
            }
        }

        return convertedDates
    }

    private fun getViewModel() {
        val LLE_MedDocketRespository= LLE_MedDocketRespository()
        val LLE_MedDocketProviderFactory= LLE_MedDocketProviderFactory(LLE_MedDocketRespository,requireActivity().application)
        viewModel= ViewModelProvider(this,LLE_MedDocketProviderFactory).get(LLE_MedDocketViewModel::class.java)

        progressDialog = ProgressDialog(requireContext()).apply {
            setCancelable(false)
            setMessage(getString(R.string.please_wait))
        }

        sessionManager=SessionManager(requireContext())

    }
    private fun createRoomDatabase() {
        val database = LLE_MedDocket_Room_Database.getDatabase(requireContext())

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
        val Final_Prescription_DAO:Final_Prescription_DAO=database.Final_Prescription_DAO()
        val SpectacleDisdributionStatus_DAO:SpectacleDisdributionStatus_DAO=database.SpectacleDisdributionStatus_DAO()
        val CurrentInventory_DAO: CurrentInventory_DAO =database.CurrentInventory_DAO()
        val InventoryUnit_DAO: InventoryUnit_DAO =database.InventoryUnit_DAO()
        val CreatePrescriptionDAO: CreatePrescriptionDAO =database.CreatePrescriptionDAO()
        val Image_Prescription_DAO: Image_Prescription_DAO =database.Image_Prescription_DAO()
        val FinalPrescriptionDrugDAO: FinalPrescriptionDrugDAO =database.FinalPrescriptionDrugDAO()


        val repository = LLE_MedDocket_Repository(Vital_DAO, VisualAcuity_DAO, Refractive_Error_DAO, OPD_Investigations_DAO, Eye_Pre_Op_Notes_DAO, Eye_Pre_Op_Investigation_DAO, Eye_Post_Op_AND_Follow_ups_DAO, Eye_OPD_Doctors_Note_DAO, Cataract_Surgery_Notes_DAO, Patient_DAO,Image_Upload_DAO,Registration_DAO,Prescription_DAO,Final_Prescription_DAO,SpectacleDisdributionStatus_DAO,SynTable_DAO,CurrentInventory_DAO,InventoryUnit_DAO,CreatePrescriptionDAO,Image_Prescription_DAO,FinalPrescriptionDrugDAO,database)
        viewModel1 = ViewModelProvider(this, LLE_MedDocket_ViewModelFactory(repository)).get(LLE_MedDocket_ViewModel::class.java)
    }



    fun updateLinearLayoutVisibility(
        selectedItem: String,
        linearLayout1: LinearLayout,
        cardView: CardView,
        spinner: Spinner,
        spinner1: Spinner
    ) {
        // Check if the selected item is "day wise"
        when(spinner)
        {
            binding.spinnerReportType->
            {

                when(selectedItem)
                {
                    "All"->
                    {
                        GetVitalData(selectedItem,"")
                    }
                    "Day Wise"->
                    {
                        linearLayout1.visibility = View.VISIBLE
                        cardView.visibility=View.GONE

                        Log.d(ConstantsApp.TAG,"selectedItem=>"+selectedItem)

                        val dayWisePosition = (spinner1.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Day Wise")

                        spinner1.setSelection(dayWisePosition!!)
                        // GetCountAllLocalTables(selectedItem, "")
                    }
                    "Today`s"->
                    {


                        GetVitalData(selectedItem,"")
                    }
                }
            }
            binding.spinnerReportType1->
            {
                when(selectedItem)
                {
                    "All"->
                    {
                        linearLayout1.visibility= View.GONE
                        cardView.visibility=View.VISIBLE

                        val dayWisePosition = (spinner1.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("All")
                        spinner1.setSelection(dayWisePosition!!)
                    }
                    "Day Wise"->
                    {
                        linearLayout1.visibility= View.VISIBLE
                        cardView.visibility=View.GONE




                    }
                    "Today`s"->
                    {
                        linearLayout1.visibility= View.GONE
                        cardView.visibility=View.VISIBLE

                        val dayWisePosition = (spinner1.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Today`s")
                        spinner1.setSelection(dayWisePosition!!)
                    }
                }
            }

        }

    }

    private fun GetVitalData(selectedItem: String, selectedDate: String) {

        viewModel1.allEye_Pre_Op_Notes.observe(viewLifecycleOwner, Observer {
                vitalList->

            when(selectedItem)
            {
                "All"->
                {
                    getAllDataPatientIDWise(patientId)
                }
                "Today`s"->
                {

                    for (data in vitalList) {
                        val created_date = data.createdDate
                        Log.d(ConstantsApp.TAG, "" + created_date)
                        Log.d(ConstantsApp.TAG, "Today`s in vital" + created_date)

                        // Inside your code where you retrieve data
                        val todayDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

// Filter the records that have the same createdDate as today's date

                        val todayRecords:MutableList<Eye_Pre_Op_Notes> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()

// Get the total count of records for today
                        val adapter= Adapter_Pre_Op_Notes(requireContext(),todayRecords)
                        binding.RecyclerViewVital.adapter=adapter
                        binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())
                    }
                }
                "Day Wise"->
                {
                    when(selectedDate)
                    {
                        ""->
                        {
                            Log.d(ConstantsApp.TAG,"selected date in Day Wise"+selectedDate)
                            for (data in vitalList)
                            {
                                val created_date=data.createdDate
                                Log.d(ConstantsApp.TAG,""+created_date)
                                Log.d(ConstantsApp.TAG,"Today`s in vital"+created_date)

                                // Inside your code where you retrieve data
                                val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


// Filter the records that have the same createdDate as today's date
                                val todayRecords:MutableList<Eye_Pre_Op_Notes> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()

// Get the total count of records for today
                                val adapter= Adapter_Pre_Op_Notes(requireContext(),todayRecords)
                                binding.RecyclerViewVital.adapter=adapter
                                binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())

// Log the results


                            }


                        }
                        else->
                        {
                            val selectedDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selectedDate)

// Format the parsed date to the "yyyy-MM-dd" format
                            val formattedSelectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedSelectedDate)

                            Log.d(ConstantsApp.TAG, "Formatted selected date: $formattedSelectedDate")
//
//// Filter the records that have the same createdDate as today's date
//
//// Get the total count of records for today

                            val todayRecords:MutableList<Eye_Pre_Op_Notes> = vitalList.filter { it.createdDate.startsWith(formattedSelectedDate) }.toMutableList()

// Get the total count of records for today
                            Log.d(ConstantsApp.TAG,"todayRecords=>"+todayRecords)
                            val adapter= Adapter_Pre_Op_Notes(requireContext(),todayRecords)
                            binding.RecyclerViewVital.adapter=adapter
                            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())


//// Log the results


                        }
                    }
                }
            }
        })

    }


    private fun getAllDataPatientIDWise(patientId: Int) {

        viewModel1.allEye_Pre_Op_Notes.observe(viewLifecycleOwner, Observer {
                vitalList->


            val eyePreOpNotesList= mutableListOf<Eye_Pre_Op_Notes>()

            for (data in vitalList)
            {
                if (patientId==data.patient_id)
                {
                    val _id =data._id
                    val camp_id =data.camp_id
                    val createdDate =data.createdDate
                    val eye_pre_op_admission_date =data.eye_pre_op_admission_date
                    val eye_pre_op_alprax =data.eye_pre_op_alprax
                    val eye_pre_op_amlodipine =data.eye_pre_op_amlodipine
                    val eye_pre_op_antibiotic =data.eye_pre_op_antibiotic
                    val eye_pre_op_antibiotic_detail =data.eye_pre_op_antibiotic_detail
                    val eye_pre_op_antibiotic_other =data.eye_pre_op_antibiotic_other
                    val eye_pre_op_antibiotic_result =data.eye_pre_op_antibiotic_result
                    val eye_pre_op_antihyp =data.eye_pre_op_antihyp
                    val eye_pre_op_antihyp_detail =data.eye_pre_op_antihyp_detail
                    val eye_pre_op_betadine =data.eye_pre_op_betadine
                    val eye_pre_op_bp_diastolic =data.eye_pre_op_bp_diastolic
                    val eye_pre_op_bp_interpretation =data.eye_pre_op_bp_interpretation
                    val eye_pre_op_bp_systolic =data.eye_pre_op_bp_systolic
                    val eye_pre_op_bs_f =data.eye_pre_op_bs_f
                    val eye_pre_op_bs_pp =data.eye_pre_op_bs_pp
                    val eye_pre_op_bt =data.eye_pre_op_bt
                    val eye_pre_op_cbc =data.eye_pre_op_cbc
                    val eye_pre_op_ciplox =data.eye_pre_op_ciplox
                    val eye_pre_op_ciplox_drop =data.eye_pre_op_ciplox_drop
                    val eye_pre_op_ct =data.eye_pre_op_ct
                    val eye_pre_op_dia =data.eye_pre_op_dia
                    val eye_pre_op_dia_detail =data.eye_pre_op_dia_detail
                    val eye_pre_op_diamox =data.eye_pre_op_diamox
                    val eye_pre_op_discussed_with =data.eye_pre_op_discussed_with
                    val eye_pre_op_discussed_with_detail =data.eye_pre_op_discussed_with_detail
                    val eye_pre_op_ecg =data.eye_pre_op_ecg
                    val eye_pre_op_flur_eye =data.eye_pre_op_flur_eye
                    val eye_pre_op_haemoglobin =data.eye_pre_op_haemoglobin
                    val eye_pre_op_hbsag =data.eye_pre_op_hbsag
                    val eye_pre_op_hcv =data.eye_pre_op_hcv
                    val eye_pre_op_head_bath =data.eye_pre_op_head_bath
                    val eye_pre_op_heart =data.eye_pre_op_heart
                    val eye_pre_op_heart_detail =data.eye_pre_op_heart_detail
                    val eye_pre_op_heart_rate =data.eye_pre_op_heart_rate
                    val eye_pre_op_historyof =data.eye_pre_op_historyof
                    val eye_pre_op_hiv =data.eye_pre_op_hiv
                    val eye_pre_op_identify_eye =data.eye_pre_op_identify_eye
                    val eye_pre_op_iol_power =data.eye_pre_op_iol_power
                    val eye_pre_op_nil_mouth =data.eye_pre_op_nil_mouth
                    val eye_pre_op_notes =data.eye_pre_op_notes
                    val eye_pre_op_o2_saturation =data.eye_pre_op_o2_saturation
                    val eye_pre_op_o2_saturation_interpretation =data.eye_pre_op_o2_saturation_interpretation
                    val eye_pre_op_other =data.eye_pre_op_other
                    val eye_pre_op_other_detail =data.eye_pre_op_other_detail
                    val eye_pre_op_plain_tropical =data.eye_pre_op_plain_tropical
                    val eye_pre_op_pt =data.eye_pre_op_pt
                    val eye_pre_op_recommendation =data.eye_pre_op_recommendation
                    val eye_pre_op_recommendation_detail =data.eye_pre_op_recommendation_detail
                    val eye_pre_op_symptoms =data.eye_pre_op_symptoms
                    val eye_pre_op_temp =data.eye_pre_op_temp
                    val eye_pre_op_temp_unit =data.eye_pre_op_temp_unit
                    val eye_pre_op_tropical_drop =data.eye_pre_op_tropical_drop
                    val eye_pre_op_tropicamide =data.eye_pre_op_tropicamide
                    val eye_pre_op_wash_face =data.eye_pre_op_wash_face
                    val eye_pre_op_xylocaine =data.eye_pre_op_xylocaine
                    val eye_pre_op_xylocaine_detail =data.eye_pre_op_xylocaine_detail
                    val eye_pre_op_xylocaine_other =data.eye_pre_op_xylocaine_other
                    val eye_pre_op_xylocaine_result =data.eye_pre_op_xylocaine_result
                    val patient_id =data.patient_id
                    val user_id=data.user_id
                    val eyePreOpNotesImagepath=data.eyePreOpNotesImagepath
                    val image_type=data.image_type
                    val isSyn=data.isSyn


                    val eyePreOpNote= Eye_Pre_Op_Notes(_id, camp_id, createdDate, eye_pre_op_admission_date, eye_pre_op_alprax, eye_pre_op_amlodipine, eye_pre_op_antibiotic, eye_pre_op_antibiotic_detail, eye_pre_op_antibiotic_other, eye_pre_op_antibiotic_result, eye_pre_op_antihyp, eye_pre_op_antihyp_detail, eye_pre_op_betadine, eye_pre_op_bp_diastolic, eye_pre_op_bp_interpretation, eye_pre_op_bp_systolic, eye_pre_op_bs_f, eye_pre_op_bs_pp, eye_pre_op_bt, eye_pre_op_cbc, eye_pre_op_ciplox, eye_pre_op_ciplox_drop, eye_pre_op_ct, eye_pre_op_dia, eye_pre_op_dia_detail, eye_pre_op_diamox, eye_pre_op_discussed_with, eye_pre_op_discussed_with_detail, eye_pre_op_ecg, eye_pre_op_flur_eye, eye_pre_op_haemoglobin, eye_pre_op_hbsag, eye_pre_op_hcv, eye_pre_op_head_bath, eye_pre_op_heart, eye_pre_op_heart_detail, eye_pre_op_heart_rate, eye_pre_op_historyof, eye_pre_op_hiv, eye_pre_op_identify_eye, eye_pre_op_iol_power, eye_pre_op_nil_mouth, eye_pre_op_notes, eye_pre_op_o2_saturation, eye_pre_op_o2_saturation_interpretation, eye_pre_op_other, eye_pre_op_other_detail, eye_pre_op_plain_tropical, eye_pre_op_pt, eye_pre_op_recommendation, eye_pre_op_recommendation_detail, eye_pre_op_symptoms, eye_pre_op_temp, eye_pre_op_temp_unit, eye_pre_op_tropical_drop, eye_pre_op_tropicamide, eye_pre_op_wash_face, eye_pre_op_xylocaine, eye_pre_op_xylocaine_detail, eye_pre_op_xylocaine_other, eye_pre_op_xylocaine_result, patient_id, user_id,eyePreOpNotesImagepath,image_type,isSyn)

                    eyePreOpNotesList.add(eyePreOpNote)
                }
            }

            val adapter= Adapter_Pre_Op_Notes(requireContext(),eyePreOpNotesList)
            binding.RecyclerViewVital.adapter=adapter
            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())

        })


    }
}