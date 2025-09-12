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
import org.impactindiafoundation.iifllemeddocket.Adapter.Adpter_Eye_Post_Op_and_Follow_Ups
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Post_Op_AND_Follow_ups
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

class Eye_Post_Op_and_Follow_UpsFragment:Fragment() {

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
        viewModel1.allEye_Post_Op_AND_Follow_ups.observe(viewLifecycleOwner, Observer {
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

        viewModel1.allEye_Post_Op_AND_Follow_ups.observe(viewLifecycleOwner, Observer {
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
                        val todayRecords: MutableList<Eye_Post_Op_AND_Follow_ups> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()


                        Log.d(ConstantsApp.TAG,""+todayRecords)
                        val adapter=Adpter_Eye_Post_Op_and_Follow_Ups(requireContext(),todayRecords)
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
                                val todayRecords: MutableList<Eye_Post_Op_AND_Follow_ups> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()

// Get the total count of records for today
                                val adapter=Adpter_Eye_Post_Op_and_Follow_Ups(requireContext(),todayRecords)
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
                            val todayRecords: MutableList<Eye_Post_Op_AND_Follow_ups> = vitalList.filter { it.createdDate.startsWith(formattedSelectedDate) }.toMutableList()
//
//// Get the total count of records for today
                            Log.d(ConstantsApp.TAG,"todayRecords=>"+todayRecords)
                            val adapter=Adpter_Eye_Post_Op_and_Follow_Ups(requireContext(),todayRecords)
                            binding.RecyclerViewVital.adapter=adapter
                            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())
                        }
                    }
                }
            }
        })

    }


    private fun getAllDataPatientIDWise(patientId: Int) {

        viewModel1.allEye_Post_Op_AND_Follow_ups.observe(viewLifecycleOwner, Observer {
                vitalList->

            Log.d(ConstantsApp.TAG,"vitalList=>"+vitalList)


            val Eye_Post_Op_AND_Follow_upsList= mutableListOf<Eye_Post_Op_AND_Follow_ups>()

            for (data in vitalList)
            {
                if (patientId==data.patient_id)
                {
                    val _id  =data._id
                    val camp_id  =data.camp_id
                    val createdDate =data.createdDate
                    val eye_post_op_2nd_date =data.eye_post_op_2nd_date
                    val eye_post_op_3rd_date =data.eye_post_op_3rd_date
                    val eye_post_op_addi_detail_left =data.eye_post_op_addi_detail_left
                    val eye_post_op_addi_detail_right =data.eye_post_op_addi_detail_right
                    val eye_post_op_asses_imedi =data.eye_post_op_asses_imedi
                    val eye_post_op_assess_catract =data.eye_post_op_assess_catract
                    val eye_post_op_assess_catract_detail =data.eye_post_op_assess_catract_detail
                    val eye_post_op_bp =data.eye_post_op_bp
                    val eye_post_op_check_pupil =data.eye_post_op_check_pupil
                    val eye_post_op_check_pupil_detail =data.eye_post_op_check_pupil_detail
                    val eye_post_op_cifloxacin =data.eye_post_op_cifloxacin
                    val eye_post_op_cifloxacin_detail =data.eye_post_op_cifloxacin_detail
                    val eye_post_op_counseling =data.eye_post_op_counseling
                    val eye_post_op_diclofenac =data.eye_post_op_diclofenac
                    val eye_post_op_diclofenac_detail =data.eye_post_op_diclofenac_detail
                    val eye_post_op_dimox =data.eye_post_op_dimox
                    val eye_post_op_dimox_detail =data.eye_post_op_dimox_detail
                    val eye_post_op_discharge_check =data.eye_post_op_discharge_check
                    val eye_post_op_distant_vision_left =data.eye_post_op_distant_vision_left
                    val eye_post_op_distant_vision_right =data.eye_post_op_distant_vision_right
                    val eye_post_op_distant_vision_unit_left =data.eye_post_op_distant_vision_unit_left
                    val eye_post_op_distant_vision_unit_right =data.eye_post_op_distant_vision_unit_right
                    val eye_post_op_early_post_op =data.eye_post_op_early_post_op
                    val eye_post_op_ed_homide =data.eye_post_op_ed_homide
                    val eye_post_op_ed_homide_detail =data.eye_post_op_ed_homide_detail
                    val eye_post_op_eye_1 =data.eye_post_op_eye_1
                    val eye_post_op_eye_1_detail =data.eye_post_op_eye_1_detail
                    val eye_post_op_eye_2 =data.eye_post_op_eye_2
                    val eye_post_op_eye_2_detail =data.eye_post_op_eye_2_detail
                    val eye_post_op_eye_3 =data.eye_post_op_eye_3
                    val eye_post_op_eye_3_detail =data.eye_post_op_eye_3_detail
                    val eye_post_op_eye_4 =data.eye_post_op_eye_4
                    val eye_post_op_eye_4_detail =data.eye_post_op_eye_4_detail
                    val eye_post_op_eye_5 =data.eye_post_op_eye_5
                    val eye_post_op_eye_5_detail =data.eye_post_op_eye_5_detail
                    val eye_post_op_fundus_exam =data.eye_post_op_fundus_exam
                    val eye_post_op_fundus_pathology =data.eye_post_op_fundus_pathology
                    val eye_post_op_head_position =data.eye_post_op_head_position
                    val eye_post_op_homide =data.eye_post_op_homide
                    val eye_post_op_homide_detail =data.eye_post_op_homide_detail
                    val eye_post_op_hypersol =data.eye_post_op_hypersol
                    val eye_post_op_hypersol_detail =data.eye_post_op_hypersol_detail
                    val eye_post_op_last_date =data.eye_post_op_last_date
                    val eye_post_op_location_centration =data.eye_post_op_location_centration
                    val eye_post_op_lubricant =data.eye_post_op_lubricant
                    val eye_post_op_lubricant_detail =data.eye_post_op_lubricant_detail
                    val eye_post_op_moxifloxacin =data.eye_post_op_moxifloxacin
                    val eye_post_op_moxifloxacin_detail =data.eye_post_op_moxifloxacin_detail
                    val eye_post_op_near_vision_left =data.eye_post_op_near_vision_left
                    val eye_post_op_near_vision_right =data.eye_post_op_near_vision_right
                    val eye_post_op_other =data.eye_post_op_other
                    val eye_post_op_other_detail =data.eye_post_op_other_detail
                    val eye_post_op_pantaprezol =data.eye_post_op_pantaprezol
                    val eye_post_op_pantaprezol_detail =data.eye_post_op_pantaprezol_detail
                    val eye_post_op_pr =data.eye_post_op_pr
                    val eye_post_op_pressure_regular =data.eye_post_op_pressure_regular
                    val eye_post_op_rr =data.eye_post_op_rr
                    val eye_post_op_slit_lamp_exam =data.eye_post_op_slit_lamp_exam
                    val eye_post_op_temp =data.eye_post_op_temp
                    val eye_post_op_temp_unit =data.eye_post_op_temp_unit
                    val eye_post_op_timolol =data.eye_post_op_timolol
                    val eye_post_op_timolol_detail =data.eye_post_op_timolol_detail
                    val eye_post_op_w_addi_detail_left =data.eye_post_op_w_addi_detail_left
                    val eye_post_op_w_addi_detail_right =data.eye_post_op_w_addi_detail_right
                    val eye_post_op_w_distant_vision_left =data.eye_post_op_w_distant_vision_left
                    val eye_post_op_w_distant_vision_right =data.eye_post_op_w_distant_vision_right
                    val eye_post_op_w_distant_vision_unit_left =data.eye_post_op_w_distant_vision_unit_left
                    val eye_post_op_w_distant_vision_unit_right =data.eye_post_op_w_distant_vision_unit_right
                    val eye_post_op_w_near_vision_left =data.eye_post_op_w_near_vision_left
                    val eye_post_op_w_near_vision_right =data.eye_post_op_w_near_vision_right
                    val eye_post_op_w_pinhole_improve_left =data.eye_post_op_w_pinhole_improve_left
                    val eye_post_op_w_pinhole_improve_right =data.eye_post_op_w_pinhole_improve_right
                    val eye_post_op_w_pinhole_improve_unit_left =data.eye_post_op_w_pinhole_improve_unit_left
                    val eye_post_op_w_pinhole_improve_unit_right =data.eye_post_op_w_pinhole_improve_unit_right
                    val eye_post_op_w_pinhole_left =data.eye_post_op_w_pinhole_left
                    val eye_post_op_w_pinhole_right =data.eye_post_op_w_pinhole_right
                    val patient_id  =data.patient_id
                    val user_id=data.userId
                    val eyePostOpAndFollowUps= Eye_Post_Op_AND_Follow_ups(_id, camp_id, createdDate, eye_post_op_2nd_date, eye_post_op_3rd_date, eye_post_op_addi_detail_left, eye_post_op_addi_detail_right, eye_post_op_asses_imedi, eye_post_op_assess_catract, eye_post_op_assess_catract_detail, eye_post_op_bp, eye_post_op_check_pupil, eye_post_op_check_pupil_detail, eye_post_op_cifloxacin, eye_post_op_cifloxacin_detail, eye_post_op_counseling, eye_post_op_diclofenac, eye_post_op_diclofenac_detail, eye_post_op_dimox, eye_post_op_dimox_detail, eye_post_op_discharge_check, eye_post_op_distant_vision_left, eye_post_op_distant_vision_right, eye_post_op_distant_vision_unit_left, eye_post_op_distant_vision_unit_right, eye_post_op_early_post_op, eye_post_op_ed_homide, eye_post_op_ed_homide_detail, eye_post_op_eye_1, eye_post_op_eye_1_detail, eye_post_op_eye_2, eye_post_op_eye_2_detail, eye_post_op_eye_3, eye_post_op_eye_3_detail, eye_post_op_eye_4, eye_post_op_eye_4_detail, eye_post_op_eye_5, eye_post_op_eye_5_detail, eye_post_op_fundus_exam, eye_post_op_fundus_pathology, eye_post_op_head_position, eye_post_op_homide, eye_post_op_homide_detail, eye_post_op_hypersol, eye_post_op_hypersol_detail, eye_post_op_last_date, eye_post_op_location_centration, eye_post_op_lubricant, eye_post_op_lubricant_detail, eye_post_op_moxifloxacin, eye_post_op_moxifloxacin_detail, eye_post_op_near_vision_left, eye_post_op_near_vision_right, eye_post_op_other, eye_post_op_other_detail, eye_post_op_pantaprezol, eye_post_op_pantaprezol_detail, eye_post_op_pr, eye_post_op_pressure_regular, eye_post_op_rr, eye_post_op_slit_lamp_exam, eye_post_op_temp, eye_post_op_temp_unit, eye_post_op_timolol, eye_post_op_timolol_detail, eye_post_op_w_addi_detail_left, eye_post_op_w_addi_detail_right, eye_post_op_w_distant_vision_left, eye_post_op_w_distant_vision_right, eye_post_op_w_distant_vision_unit_left, eye_post_op_w_distant_vision_unit_right, eye_post_op_w_near_vision_left, eye_post_op_w_near_vision_right, eye_post_op_w_pinhole_improve_left, eye_post_op_w_pinhole_improve_right, eye_post_op_w_pinhole_improve_unit_left, eye_post_op_w_pinhole_improve_unit_right, eye_post_op_w_pinhole_left, eye_post_op_w_pinhole_right, patient_id, user_id)
                    Eye_Post_Op_AND_Follow_upsList.add(eyePostOpAndFollowUps)

                }
                Log.d(ConstantsApp.TAG,"Eye_Post_Op_AND_Follow_upsList=>"+Eye_Post_Op_AND_Follow_upsList)

            }
            Log.d(ConstantsApp.TAG,"Eye_Post_Op_AND_Follow_upsList=>"+Eye_Post_Op_AND_Follow_upsList)

            val adapter= Adpter_Eye_Post_Op_and_Follow_Ups(requireContext(),Eye_Post_Op_AND_Follow_upsList)
            binding.RecyclerViewVital.adapter=adapter
            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())

        })
    }
}