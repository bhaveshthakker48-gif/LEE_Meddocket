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
import org.impactindiafoundation.iifllemeddocket.Adapter.Adpter_RefractiveError
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
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

class RefractiveErrorFragment:Fragment() {

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
        viewModel1.allRefractive_Error.observe(viewLifecycleOwner, Observer {
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

        viewModel1.allRefractive_Error.observe(viewLifecycleOwner, Observer {
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
                        val todayRecords: MutableList<RefractiveError> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()
//
//// Get the total count of records for today
                        Log.d(ConstantsApp.TAG,"todayRecords=>"+todayRecords)
                        val adapter= Adpter_RefractiveError(requireContext(),todayRecords)
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

// Get the total count of records for today
                                val todayRecords: MutableList<RefractiveError> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()
//
//// Get the total count of records for today
                                Log.d(ConstantsApp.TAG,"todayRecords=>"+todayRecords)
                                val adapter= Adpter_RefractiveError(requireContext(),todayRecords)
                                binding.RecyclerViewVital.adapter=adapter
                                binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())

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
                            val todayRecords: MutableList<RefractiveError> = vitalList.filter { it.createdDate.startsWith(formattedSelectedDate) }.toMutableList()
//
//// Get the total count of records for today
                            Log.d(ConstantsApp.TAG,"todayRecords=>"+todayRecords)
                            val adapter= Adpter_RefractiveError(requireContext(),todayRecords)
                            binding.RecyclerViewVital.adapter=adapter
                            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())
                        }
                    }
                }
            }
        })

    }


    private fun getAllDataPatientIDWise(patientId: Int) {

        viewModel1.allRefractive_Error.observe(viewLifecycleOwner, Observer {
                vitalList->

            val Refractive_ErrorList = mutableListOf<RefractiveError>()

            for (data in vitalList)
            {
                if (patientId==data.patient_id)
                {
                    val _id =data._id
                    val camp_id =data.camp_id
                    val createdDate =data.createdDate
                    val fundus_notes =data.fundus_notes
                    val is_given =data.is_given
                    val is_ordered =data.is_ordered
                    val patient_id =data.patient_id
                    val presc_type =data.presc_type
                    val re_bvd =data.re_bvd
                    val re_distant_vision_axis_left =data.re_distant_vision_axis_left
                    val re_distant_vision_axis_right =data.re_distant_vision_axis_right
                    val re_distant_vision_cylinder_left =data.re_distant_vision_cylinder_left
                    val re_distant_vision_cylinder_right =data.re_distant_vision_cylinder_right
                    val re_distant_vision_left =data.re_distant_vision_left
                    val re_distant_vision_right =data.re_distant_vision_right
                    val re_distant_vision_sphere_left =data.re_distant_vision_sphere_left
                    val re_distant_vision_sphere_right =data.re_distant_vision_sphere_right
                    val re_distant_vision_unit_left =data.re_distant_vision_unit_left
                    val re_distant_vision_unit_right =data.re_distant_vision_unit_right
                    val re_near_vision_axis_left =data.re_near_vision_axis_left
                    val re_near_vision_axis_right =data.re_near_vision_axis_right
                    val re_near_vision_cylinder_left =data.re_near_vision_cylinder_left
                    val re_near_vision_cylinder_right =data.re_near_vision_cylinder_right
                    val re_near_vision_left =data.re_near_vision_left
                    val re_near_vision_right =data.re_near_vision_right
                    val re_near_vision_sphere_left =data.re_near_vision_sphere_left
                    val re_near_vision_sphere_right =data.re_near_vision_sphere_right
                    val re_prism_left =data.re_prism_left
                    val re_prism_right =data.re_prism_right
                    val re_prism_unit_left =data.re_prism_unit_left
                    val re_prism_unit_right =data.re_prism_unit_right
                    val re_pupipllary_distance =data.re_pupipllary_distance
                    val re_reading_addition_left =data.re_reading_addition_left
                    val re_reading_addition_left_details =data.re_reading_addition_left_details
                    val re_reading_addition_right =data.re_reading_addition_right
                    val re_reading_addition_right_details =data.re_reading_addition_right_details
                    val re_remark_left =data.re_remark_left
                    val re_remark_right =data.re_remark_right
                    val re_remarks =data.re_remarks
                    val reading_glass =data.reading_glass
                    val userId=data.userId
                    val frameCode = data.frameCode
                    val frameColor = data.frameColor
                    val isSyn=data.isSyn


                    val RefractiveError=
                        RefractiveError(
                            _id,
                            camp_id,
                            createdDate,
                            fundus_notes,
                            is_given,
                            is_ordered,
                            patient_id,
                            presc_type,
                            re_bvd,
                            re_distant_vision_axis_left,
                            re_distant_vision_axis_right,
                            re_distant_vision_cylinder_left,
                            re_distant_vision_cylinder_right,
                            re_distant_vision_left,
                            re_distant_vision_right,
                            re_distant_vision_sphere_left,
                            re_distant_vision_sphere_right,
                            re_distant_vision_unit_left,
                            re_distant_vision_unit_right,
                            re_near_vision_axis_left,
                            re_near_vision_axis_right,
                            re_near_vision_cylinder_left,
                            re_near_vision_cylinder_right,
                            re_near_vision_left,
                            re_near_vision_right,
                            re_near_vision_sphere_left,
                            re_near_vision_sphere_right,
                            re_prism_left,
                            re_prism_right,
                            re_prism_unit_left,
                            re_prism_unit_right,
                            re_pupipllary_distance,
                            re_reading_addition_left,
                            re_reading_addition_left_details,
                            re_reading_addition_right,
                            re_reading_addition_right_details,
                            re_remark_left,
                            re_remark_right,
                            re_remarks,
                            reading_glass,
                            userId,
                            frameCode,
                            frameColor,
                            isSyn
                        )
                    Refractive_ErrorList.add(RefractiveError)
                }
            }

            val adapter= Adpter_RefractiveError(requireContext(),Refractive_ErrorList)
            binding.RecyclerViewVital.adapter=adapter
            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())

        })


    }
}