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
import org.impactindiafoundation.iifllemeddocket.Adapter.Adpter_EyeOPDDoctorNote
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
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_OPD_Doctors_Note
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.Eyeopddocnote
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

class Eye_OPD_Doctor_NoteFragment:Fragment() {

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
                Log.d(ConstantsApp.TAG, "patientData is null")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setDates(patientId: Int) {
        viewModel1.allEye_OPD_Doctors_Note.observe(viewLifecycleOwner, Observer {
                vitalList->

            for (data in vitalList)
            {
                if (data.patient_id==patientId)
                {
                    Log.d(ConstantsApp.TAG,"dates=>"+data.createdDate)

                    val datesInOriginalFormat = mutableListOf(data.createdDate)

                    val uniqueDatesSet = HashSet<String>(ReportDatesArrayList!!)
                    uniqueDatesSet.addAll(convertDatesToDesiredFormat(datesInOriginalFormat))
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
                val parsedDate =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(originalDate)
                convertedDates.add(desiredDateFormat.format(parsedDate!!))
            } catch (e: Exception) {
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
                        //getAllDataPatientIDWise(patientId)
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

        viewModel1.allEye_OPD_Doctors_Note.observe(viewLifecycleOwner, Observer {
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
                        val todayDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val todayRecords: MutableList<Eye_OPD_Doctors_Note> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()
                        Log.d(ConstantsApp.TAG,""+todayRecords)
                        val adapter= Adpter_EyeOPDDoctorNote(requireContext(),todayRecords)
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
                                val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                val todayRecords: MutableList<Eye_OPD_Doctors_Note> = vitalList.filter { it.createdDate.startsWith(todayDate) }.toMutableList()
                                val adapter= Adpter_EyeOPDDoctorNote(requireContext(),todayRecords)
                                binding.RecyclerViewVital.adapter=adapter
                                binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())
                            }
                        }
                        else->
                        {
                            val selectedDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val parsedSelectedDate = selectedDateFormat.parse(selectedDate)

                            val formattedSelectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedSelectedDate)
                            Log.d(ConstantsApp.TAG, "Formatted selected date: $formattedSelectedDate")
                            val todayRecords: MutableList<Eye_OPD_Doctors_Note> = vitalList.filter { it.createdDate.startsWith(formattedSelectedDate) }.toMutableList()
                            Log.d(ConstantsApp.TAG,"todayRecords=>"+todayRecords)
                            val adapter= Adpter_EyeOPDDoctorNote(requireContext(),todayRecords)
                            binding.RecyclerViewVital.adapter=adapter
                            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())
                        }
                    }
                }
            }
        })

    }


    private fun getAllDataPatientIDWise(patientId: Int) {

        viewModel1.allEye_OPD_Doctors_Note.observe(viewLifecycleOwner, Observer {
                vitalList->

            val eyeOPDDoctorNoteList: MutableList<Eyeopddocnote> = mutableListOf()

            for (data in vitalList) {
                if (patientId == data.patient_id) {
                    val _id = data._id
                    val camp_id = data.camp_id
                    val createdDate = data.createdDate
                    val opd_eye_diagnosis = data.opd_eye_diagnosis
                    val opd_eye_diagnosis_description = data.opd_eye_diagnosis_description
                    val opd_eye_examination = data.opd_eye_examination
                    val opd_eye_examination_description = data.opd_eye_examination_description
                    val opd_eye_notes = data.opd_eye_notes
                    val opd_eye_recommended = data.opd_eye_recommended
                    val opd_eye_symptoms = data.opd_eye_symptoms
                    val opd_eye_symptoms_description = data.opd_eye_symptoms_description
                    val patient_id = data.patient_id
                    val userId = data.user_id

                    val eyeOpdDoctorsNote = Eyeopddocnote(
                        _id, camp_id, createdDate, opd_eye_diagnosis,
                        opd_eye_diagnosis_description, opd_eye_examination, opd_eye_examination_description,
                        opd_eye_notes, opd_eye_recommended, opd_eye_symptoms, opd_eye_symptoms_description,
                        patient_id, userId
                    )
                    eyeOPDDoctorNoteList.add(eyeOpdDoctorsNote)
                }
            }


            val convertedList: MutableList<Eye_OPD_Doctors_Note> = eyeOPDDoctorNoteList.map { eyeOpdDoctorsNote ->
                Eye_OPD_Doctors_Note(
                    eyeOpdDoctorsNote._id,
                    eyeOpdDoctorsNote.camp_id,
                    eyeOpdDoctorsNote.createdDate,
                    eyeOpdDoctorsNote.opd_eye_diagnosis,
                    eyeOpdDoctorsNote.opd_eye_diagnosis_description,
                    eyeOpdDoctorsNote.opd_eye_examination,
                    eyeOpdDoctorsNote.opd_eye_examination_description,
                    eyeOpdDoctorsNote.opd_eye_notes,
                    eyeOpdDoctorsNote.opd_eye_recommended,
                    eyeOpdDoctorsNote.opd_eye_symptoms,
                    eyeOpdDoctorsNote.opd_eye_symptoms_description,
                    eyeOpdDoctorsNote.patient_id,
                    eyeOpdDoctorsNote.user_id
                )
            }.toMutableList()
            val adapter= Adpter_EyeOPDDoctorNote(requireContext(),convertedList)
            binding.RecyclerViewVital.adapter=adapter
            binding.RecyclerViewVital.layoutManager=LinearLayoutManager(requireContext())
        })
    }
}