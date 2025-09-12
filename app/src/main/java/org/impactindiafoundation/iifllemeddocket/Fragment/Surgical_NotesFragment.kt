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
import org.impactindiafoundation.iifllemeddocket.Adapter.Adpter_Surgical_Notes
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Cataract_Surgery_Notes
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

class Surgical_NotesFragment:Fragment() {

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
        viewModel1.allCataract_Surgery_Notes.observe(viewLifecycleOwner, Observer {
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

        viewModel1.allCataract_Surgery_Notes.observe(viewLifecycleOwner, Observer {
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
                        val todayRecords: MutableList<Cataract_Surgery_Notes> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()
//
//// Get the total count of records for today
                        Log.d(ConstantsApp.TAG,"todayRecords=>"+todayRecords)
                        val adapter= Adpter_Surgical_Notes(requireContext(),todayRecords)
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
                                val todayRecords: MutableList<Cataract_Surgery_Notes> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()
//
//// Get the total count of records for today
                                Log.d(ConstantsApp.TAG,"todayRecords=>"+todayRecords)
                                val adapter= Adpter_Surgical_Notes(requireContext(),todayRecords)
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
                            val todayRecords: MutableList<Cataract_Surgery_Notes> = vitalList.filter { it.createdDate.startsWith(formattedSelectedDate) }.toMutableList()
//
//// Get the total count of records for today
                            Log.d(ConstantsApp.TAG,"todayRecords=>"+todayRecords)
                            val adapter= Adpter_Surgical_Notes(requireContext(),todayRecords)
                            binding.RecyclerViewVital.adapter=adapter
                            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())
                        }
                    }
                }
            }
        })

    }


    private fun getAllDataPatientIDWise(patientId: Int) {

        viewModel1.allCataract_Surgery_Notes.observe(viewLifecycleOwner, Observer {
                vitalList->

            val CataractSurgeryList= mutableListOf<Cataract_Surgery_Notes>()

            for (data in vitalList)
            {
                if (patientId==data.patient_id)
                {
                    val _id  =data._id
                    val camp_id  =data.camp_id
                    val createdDate  =data.createdDate
                    val patient_id  =data.patient_id
                    val sn_airway  =data.sn_airway
                    val sn_airway_detail  =data.sn_airway_detail
                    val sn_anaesthetist_concern  =data.sn_anaesthetist_concern
                    val sn_anticoagulant  =data.sn_anticoagulant
                    val sn_anticoagulant_detail  =data.sn_anticoagulant_detail
                    val sn_before_incision_all_team  =data.sn_before_incision_all_team
                    val sn_before_or_instrument  =data.sn_before_or_instrument
                    val sn_before_or_key  =data.sn_before_or_key
                    val sn_before_or_key_detail  =data.sn_before_or_key_detail
                    val sn_before_or_specimen  =data.sn_before_or_specimen
                    val sn_before_or_weather  =data.sn_before_or_weather
                    val sn_before_or_weather_detail  =data.sn_before_or_weather_detail
                    val sn_cataract_capsulotomy  =data.sn_cataract_capsulotomy
                    val sn_cataract_capsulotomy_detail  =data.sn_cataract_capsulotomy_detail
                    val sn_cataract_castroviejo  =data.sn_cataract_castroviejo
                    val sn_cataract_castroviejo_detail  =data.sn_cataract_castroviejo_detail
                    val sn_cataract_colibri  =data.sn_cataract_colibri
                    val sn_cataract_colibri_detail  =data.sn_cataract_colibri_detail
                    val sn_cataract_formed  =data.sn_cataract_formed
                    val sn_cataract_formed_detail  =data.sn_cataract_formed_detail
                    val sn_cataract_hydrodissectiirs  =data.sn_cataract_hydrodissectiirs
                    val sn_cataract_hydrodissectiirs_detail  =data.sn_cataract_hydrodissectiirs_detail
                    val sn_cataract_irrigation  =data.sn_cataract_irrigation
                    val sn_cataract_irrigation_detail  =data.sn_cataract_irrigation_detail
                    val sn_cataract_keretome  =data.sn_cataract_keretome
                    val sn_cataract_keretome_detail  =data.sn_cataract_keretome_detail
                    val sn_cataract_keretome_phaco  =data.sn_cataract_keretome_phaco
                    val sn_cataract_keretome_phaco_detail  =data.sn_cataract_keretome_phaco_detail
                    val sn_cataract_knife  =data.sn_cataract_knife
                    val sn_cataract_knife_detail  =data.sn_cataract_knife_detail
                    val sn_cataract_lieberman  =data.sn_cataract_lieberman
                    val sn_cataract_lieberman_detail  =data.sn_cataract_lieberman_detail
                    val sn_cataract_limb  =data.sn_cataract_limb
                    val sn_cataract_limb_detail  =data.sn_cataract_limb_detail
                    val sn_cataract_mac  =data.sn_cataract_mac
                    val sn_cataract_mac_detail  =data.sn_cataract_mac_detail
                    val sn_cataract_nucleus  =data.sn_cataract_nucleus
                    val sn_cataract_nucleus_detail  =data.sn_cataract_nucleus_detail
                    val sn_cataract_sinsky  =data.sn_cataract_sinsky
                    val sn_cataract_sinsky_detail  =data.sn_cataract_sinsky_detail
                    val sn_cataract_universal  =data.sn_cataract_universal
                    val sn_cataract_universal_detail  =data.sn_cataract_universal_detail
                    val sn_cataract_viscoelastic  =data.sn_cataract_viscoelastic
                    val sn_cataract_viscoelastic_detail  =data.sn_cataract_viscoelastic_detail
                    val sn_common_dislocation  =data.sn_common_dislocation
                    val sn_common_dislocation_detail  =data.sn_common_dislocation_detail
                    val sn_common_endophthalmitis  =data.sn_common_endophthalmitis
                    val sn_common_endophthalmitis_detail  =data.sn_common_endophthalmitis_detail
                    val sn_common_endothelial  =data.sn_common_endothelial
                    val sn_common_endothelial_detail  =data.sn_common_endothelial_detail
                    val sn_common_fluid  =data.sn_common_fluid
                    val sn_common_fluid_detail  =data.sn_common_fluid_detail
                    val sn_common_hyphema  =data.sn_common_hyphema
                    val sn_common_hyphema_detail  =data.sn_common_hyphema_detail
                    val sn_common_light  =data.sn_common_light
                    val sn_common_light_detail  =data.sn_common_light_detail
                    val sn_common_macular  =data.sn_common_macular
                    val sn_common_macular_detail  =data.sn_common_macular_detail
                    val sn_common_ocular  =data.sn_common_ocular
                    val sn_common_ocular_detail  =data.sn_common_ocular_detail
                    val sn_common_posterior_opacification  =data.sn_common_posterior_opacification
                    val sn_common_posterior_opacification_detail  =data.sn_common_posterior_opacification_detail
                    val sn_common_posterior_rent  =data.sn_common_posterior_rent
                    val sn_common_posterior_rent_detail  =data.sn_common_posterior_rent_detail
                    val sn_common_retinal  =data.sn_common_retinal
                    val sn_common_retinal_detail  =data.sn_common_retinal_detail
                    val sn_common_vitreous  =data.sn_common_vitreous
                    val sn_common_vitreous_detail  =data.sn_common_vitreous_detail
                    val sn_date_of_surgery  =data.sn_date_of_surgery
                    val sn_flomax  =data.sn_flomax
                    val sn_has_confirmed_allergies  =data.sn_has_confirmed_allergies
                    val sn_has_confirmed_consent  =data.sn_has_confirmed_consent
                    val sn_has_confirmed_identity  =data.sn_has_confirmed_identity
                    val sn_has_confirmed_procedure  =data.sn_has_confirmed_procedure
                    val sn_has_confirmed_site  =data.sn_has_confirmed_site
                    val sn_incision_cornea  =data.sn_incision_cornea
                    val sn_incision_sclera_1  =data.sn_incision_sclera_1
                    val sn_incision_sclera_2  =data.sn_incision_sclera_2
                    val sn_intra_adrenaline  =data.sn_intra_adrenaline
                    val sn_intra_adrenaline_detail  =data.sn_intra_adrenaline_detail
                    val sn_intra_combination  =data.sn_intra_combination
                    val sn_intra_combination_detail  =data.sn_intra_combination_detail
                    val sn_intra_gentamycin  =data.sn_intra_gentamycin
                    val sn_intra_gentamycin_detail  =data.sn_intra_gentamycin_detail
                    val sn_intra_intasol  =data.sn_intra_intasol
                    val sn_intra_intasol_detail  =data.sn_intra_intasol_detail
                    val sn_intra_mannitol  =data.sn_intra_mannitol
                    val sn_intra_mannitol_detail  =data.sn_intra_mannitol_detail
                    val sn_intra_moxifloxacin  =data.sn_intra_moxifloxacin
                    val sn_intra_moxifloxacin_detail  =data.sn_intra_moxifloxacin_detail
                    val sn_intra_occular_lens  =data.sn_intra_occular_lens
                    val sn_intra_prednisolone  =data.sn_intra_prednisolone
                    val sn_intra_prednisolone_detail  =data.sn_intra_prednisolone_detail
                    val sn_intra_vigamox  =data.sn_intra_vigamox
                    val sn_intra_vigamox_detail  =data.sn_intra_vigamox_detail
                    val sn_intra_visco  =data.sn_intra_visco
                    val sn_intra_visco_detail  =data.sn_intra_visco_detail
                    val sn_local_anaesthesia  =data.sn_local_anaesthesia
                    val sn_nurse_age  =data.sn_nurse_age
                    val sn_nurse_age_unit  =data.sn_nurse_age_unit
                    val sn_nurse_anaesthesia  =data.sn_nurse_anaesthesia
                    val sn_nurse_anaesthetist  =data.sn_nurse_anaesthetist
                    val sn_nurse_bp_diastolic  =data.sn_nurse_bp_diastolic
                    val sn_nurse_bp_interpretation  =data.sn_nurse_bp_interpretation
                    val sn_nurse_bp_sistolic  =data.sn_nurse_bp_sistolic
                    val sn_nurse_concern  =data.sn_nurse_concern
                    val sn_nurse_diagnosis  =data.sn_nurse_diagnosis
                    val sn_nurse_duration  =data.sn_nurse_duration
                    val sn_nurse_equipment_issue  =data.sn_nurse_equipment_issue
                    val sn_nurse_implant_detail  =data.sn_nurse_implant_detail
                    val sn_nurse_name  =data.sn_nurse_name
                    val sn_nurse_orally_confirm  =data.sn_nurse_orally_confirm
                    val sn_nurse_rbs  =data.sn_nurse_rbs
                    val sn_nurse_rbs_interpretation  =data.sn_nurse_rbs_interpretation
                    val sn_nurse_registered  =data.sn_nurse_registered
                    val sn_nurse_scrub  =data.sn_nurse_scrub
                    val sn_nurse_serial  =data.sn_nurse_serial
                    val sn_nurse_sex  =data.sn_nurse_sex
                    val sn_nurse_specimen_biopsy  =data.sn_nurse_specimen_biopsy
                    val sn_nurse_specimen_detail  =data.sn_nurse_specimen_detail
                    val sn_nurse_sterility  =data.sn_nurse_sterility
                    val sn_nurse_surgeon  =data.sn_nurse_surgeon
                    val sn_nurse_surgery_name  =data.sn_nurse_surgery_name
                    val sn_nurse_viral_serology  =data.sn_nurse_viral_serology
                    val sn_post_cifloxacin  =data.sn_post_cifloxacin
                    val sn_post_cifloxacin_detail  =data.sn_post_cifloxacin_detail
                    val sn_post_diclofenac  =data.sn_post_diclofenac
                    val sn_post_diclofenac_detail  =data.sn_post_diclofenac_detail
                    val sn_post_dimox  =data.sn_post_dimox
                    val sn_post_dimox_detail  =data.sn_post_dimox_detail
                    val sn_post_eye_1  =data.sn_post_eye_1
                    val sn_post_eye_1_detail  =data.sn_post_eye_1_detail
                    val sn_post_eye_2  =data.sn_post_eye_2
                    val sn_post_eye_2_detail  =data.sn_post_eye_2_detail
                    val sn_post_eye_3  =data.sn_post_eye_3
                    val sn_post_eye_3_detail  =data.sn_post_eye_3_detail
                    val sn_post_eye_4  =data.sn_post_eye_4
                    val sn_post_eye_4_detail  =data.sn_post_eye_4_detail
                    val sn_post_eye_5  =data.sn_post_eye_5
                    val sn_post_eye_5_detail  =data.sn_post_eye_5_detail
                    val sn_post_eye_homide  =data.sn_post_eye_homide
                    val sn_post_eye_homide_detail  =data.sn_post_eye_homide_detail
                    val sn_post_eye_hypersol  =data.sn_post_eye_hypersol
                    val sn_post_eye_hypersol_detail  =data.sn_post_eye_hypersol_detail
                    val sn_post_eye_lubricant  =data.sn_post_eye_lubricant
                    val sn_post_eye_lubricant_detail  =data.sn_post_eye_lubricant_detail
                    val sn_post_eye_moxifloxacin  =data.sn_post_eye_moxifloxacin
                    val sn_post_eye_moxifloxacin_detail  =data.sn_post_eye_moxifloxacin_detail
                    val sn_post_eye_timolol  =data.sn_post_eye_timolol
                    val sn_post_eye_timolol_detail  =data.sn_post_eye_timolol_detail
                    val sn_post_pantaprezol  =data.sn_post_pantaprezol
                    val sn_post_pantaprezol_detail  =data.sn_post_pantaprezol_detail
                    val sn_site_marked_history  =data.sn_site_marked_history
                    val sn_site_marked_pre_anaesthesia  =data.sn_site_marked_pre_anaesthesia
                    val sn_site_marked_pre_surgical  =data.sn_site_marked_pre_surgical
                    val sn_type_of_surgery  =data.sn_type_of_surgery
                    val sn_type_of_surgery_other  =data.sn_type_of_surgery_other
                    val sn_unexpected_step  =data.sn_unexpected_step
                    val sn_unexpected_step_detail  =data.sn_unexpected_step_detail
                    val userId  =data.userId
                    val filepath=data.filepath
                    val isSyn  =data.isSyn


                    val surgicaldata=
                        Cataract_Surgery_Notes(_id, camp_id, createdDate, patient_id, sn_airway,
                        sn_airway_detail, sn_anaesthetist_concern, sn_anticoagulant,
                        sn_anticoagulant_detail, sn_before_incision_all_team,
                        sn_before_or_instrument, sn_before_or_key, sn_before_or_key_detail,
                        sn_before_or_specimen, sn_before_or_weather, sn_before_or_weather_detail,
                        sn_cataract_capsulotomy, sn_cataract_capsulotomy_detail,
                        sn_cataract_castroviejo, sn_cataract_castroviejo_detail,
                        sn_cataract_colibri, sn_cataract_colibri_detail,
                        sn_cataract_formed, sn_cataract_formed_detail,
                        sn_cataract_hydrodissectiirs, sn_cataract_hydrodissectiirs_detail,
                        sn_cataract_irrigation, sn_cataract_irrigation_detail,
                        sn_cataract_keretome, sn_cataract_keretome_detail,
                        sn_cataract_keretome_phaco, sn_cataract_keretome_phaco_detail,
                        sn_cataract_knife, sn_cataract_knife_detail,
                        sn_cataract_lieberman, sn_cataract_lieberman_detail,
                        sn_cataract_limb, sn_cataract_limb_detail,
                        sn_cataract_mac, sn_cataract_mac_detail,
                        sn_cataract_nucleus, sn_cataract_nucleus_detail,
                        sn_cataract_sinsky, sn_cataract_sinsky_detail,
                        sn_cataract_universal, sn_cataract_universal_detail,
                        sn_cataract_viscoelastic, sn_cataract_viscoelastic_detail,
                        sn_common_dislocation, sn_common_dislocation_detail,
                        sn_common_endophthalmitis, sn_common_endophthalmitis_detail,
                        sn_common_endothelial, sn_common_endothelial_detail, sn_common_fluid,
                        sn_common_fluid_detail, sn_common_hyphema, sn_common_hyphema_detail,
                        sn_common_light, sn_common_light_detail, sn_common_macular,
                        sn_common_macular_detail, sn_common_ocular,
                        sn_common_ocular_detail, sn_common_posterior_opacification,
                        sn_common_posterior_opacification_detail, sn_common_posterior_rent,
                        sn_common_posterior_rent_detail, sn_common_retinal,
                        sn_common_retinal_detail, sn_common_vitreous,
                        sn_common_vitreous_detail, sn_date_of_surgery,
                        sn_flomax, sn_has_confirmed_allergies, sn_has_confirmed_consent,
                        sn_has_confirmed_identity, sn_has_confirmed_procedure,
                        sn_has_confirmed_site, sn_incision_cornea,
                        sn_incision_sclera_1, sn_incision_sclera_2,
                        sn_intra_adrenaline, sn_intra_adrenaline_detail,
                        sn_intra_combination, sn_intra_combination_detail,
                        sn_intra_gentamycin, sn_intra_gentamycin_detail,
                        sn_intra_intasol, sn_intra_intasol_detail,
                        sn_intra_mannitol, sn_intra_mannitol_detail,
                        sn_intra_moxifloxacin, sn_intra_moxifloxacin_detail,
                        sn_intra_occular_lens, sn_intra_prednisolone,
                        sn_intra_prednisolone_detail, sn_intra_vigamox,
                        sn_intra_vigamox_detail, sn_intra_visco,
                        sn_intra_visco_detail, sn_local_anaesthesia,
                        sn_nurse_age, sn_nurse_age_unit, sn_nurse_anaesthesia, sn_nurse_anaesthetist, sn_nurse_bp_diastolic, sn_nurse_bp_interpretation, sn_nurse_bp_sistolic, sn_nurse_concern, sn_nurse_diagnosis, sn_nurse_duration, sn_nurse_equipment_issue, sn_nurse_implant_detail, sn_nurse_name, sn_nurse_orally_confirm, sn_nurse_rbs, sn_nurse_rbs_interpretation, sn_nurse_registered, sn_nurse_scrub, sn_nurse_serial, sn_nurse_sex, sn_nurse_specimen_biopsy, sn_nurse_specimen_detail, sn_nurse_sterility, sn_nurse_surgeon, sn_nurse_surgery_name, sn_nurse_viral_serology, sn_post_cifloxacin, sn_post_cifloxacin_detail, sn_post_diclofenac, sn_post_diclofenac_detail, sn_post_dimox, sn_post_dimox_detail, sn_post_eye_1, sn_post_eye_1_detail, sn_post_eye_2, sn_post_eye_2_detail, sn_post_eye_3, sn_post_eye_3_detail, sn_post_eye_4, sn_post_eye_4_detail, sn_post_eye_5, sn_post_eye_5_detail, sn_post_eye_homide, sn_post_eye_homide_detail, sn_post_eye_hypersol, sn_post_eye_hypersol_detail, sn_post_eye_lubricant, sn_post_eye_lubricant_detail, sn_post_eye_moxifloxacin, sn_post_eye_moxifloxacin_detail, sn_post_eye_timolol, sn_post_eye_timolol_detail, sn_post_pantaprezol, sn_post_pantaprezol_detail, sn_site_marked_history, sn_site_marked_pre_anaesthesia, sn_site_marked_pre_surgical, sn_type_of_surgery, sn_type_of_surgery_other, sn_unexpected_step, sn_unexpected_step_detail, userId,filepath,isSyn)

                    CataractSurgeryList.add(surgicaldata)
                }
            }

            val adapter= Adpter_Surgical_Notes(requireContext(),CataractSurgeryList)
            binding.RecyclerViewVital.adapter=adapter
            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())

        })
    }
}