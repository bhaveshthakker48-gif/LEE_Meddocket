package org.impactindiafoundation.iifllemeddocket.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.Adapter.Add_AddSymptomsArrayList_Adapter
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
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.AddSymptomsModel
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketProviderFactory
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketRespository
import org.impactindiafoundation.iifllemeddocket.ViewModel.LLE_MedDocketViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityEyeOpdDoctorsNotesBinding

class EyeOPDDoctorsNoteActivity:AppCompatActivity(), View.OnClickListener {

    lateinit var binding:ActivityEyeOpdDoctorsNotesBinding
    lateinit var customDropDownAdapter: CustomDropDownAdapter
    lateinit var viewModel: LLE_MedDocketViewModel
    lateinit var viewModel1: LLE_MedDocket_ViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    var EyeArrayList:ArrayList<String>?=null
    var SymptomsArrayList:ArrayList<String>?=null
    var ExaminationList:ArrayList<String>?=null
    var DiagnosisList:ArrayList<String>?=null
    var AddSymptomsArrayList:ArrayList<AddSymptomsModel>?=null
    var AddExaminationArrayList:ArrayList<AddSymptomsModel>?=null
    var AddDiagnosisArrayList:ArrayList<AddSymptomsModel>?=null
    var RecommendedradioButton:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEyeOpdDoctorsNotesBinding.inflate(layoutInflater)
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
    }

    override fun onResume() {
        super.onResume()
        binding.toolbarEyeOPDDoctorsNote.toolbar.title="Eye OPD Doctors Note"
        getViewModel()
        createRoomDatabase()
        initView()

        AddSymptomsArrayList= ArrayList()
        AddExaminationArrayList= ArrayList()
        AddDiagnosisArrayList= ArrayList()

        EyeArrayList=ArrayList()
        EyeArrayList!!.add("Select Eye")
        EyeArrayList!!.add("Right")
        EyeArrayList!!.add("Left")
        EyeArrayList!!.add("Both")

        SymptomsArrayList= ArrayList()
        SymptomsArrayList!!.add("Select Symptoms")
        SymptomsArrayList!!.add("Reduce Vision")
        SymptomsArrayList!!.add("Redness")
        SymptomsArrayList!!.add("Dryness")
        SymptomsArrayList!!.add("Watering of Eyes")
        SymptomsArrayList!!.add("Blurring of Vision")
        SymptomsArrayList!!.add("Blindness")
        SymptomsArrayList!!.add("Strain in Eyes")
        SymptomsArrayList!!.add("Others")

        ExaminationList= ArrayList()
        ExaminationList!!.add("Select Examination")
        ExaminationList!!.add("Eye Lid")
        ExaminationList!!.add("Cornea")
        ExaminationList!!.add("Fundus")
        ExaminationList!!.add("Lacrimal Gland")
        ExaminationList!!.add("Conjuntivitis")
        ExaminationList!!.add("Lense")
        ExaminationList!!.add("Others")

        DiagnosisList= ArrayList()
        DiagnosisList!!.add("Select Diagnosis")
        DiagnosisList!!.add("Cataract")
        DiagnosisList!!.add("Pterygium")
        DiagnosisList!!.add("Diabetic Fundus")
        DiagnosisList!!.add("Glaucoma")
        DiagnosisList!!.add("Refactory Error")
        DiagnosisList!!.add("Corneal Opacity")
        DiagnosisList!!.add("Squint")
        DiagnosisList!!.add("Others")

        binding.RecyclerViewSymptoms!!.visibility=View.GONE
        binding.RecyclerViewExamination!!.visibility=View.GONE
        binding.LinearLayoutSymptomsOther.visibility=View.GONE
        binding.LinearLayoutExaminationOther.visibility=View.GONE
        binding.LinearLayoutDiagnosisOther.visibility=View.GONE
        binding.LinearLayoutSymptomsList.visibility=View.GONE
        binding.LinearLayoutDiagnosisList.visibility=View.GONE
        binding.LinearLayoutExaminationList.visibility=View.GONE

        customDropDownAdapter = CustomDropDownAdapter(this, EyeArrayList!!)
        binding.spinnerSystomsEye!!.adapter=customDropDownAdapter
        customDropDownAdapter = CustomDropDownAdapter(this, EyeArrayList!!)
        binding.spinnerDiagnosisEye!!.adapter=customDropDownAdapter
        customDropDownAdapter = CustomDropDownAdapter(this, EyeArrayList!!)
        binding.spinnerExaminationEye!!.adapter=customDropDownAdapter
        customDropDownAdapter = CustomDropDownAdapter(this, SymptomsArrayList!!)
        binding.spinnerSystoms!!.adapter=customDropDownAdapter
        customDropDownAdapter = CustomDropDownAdapter(this, ExaminationList!!)
        binding.spinnerExamination!!.adapter=customDropDownAdapter
        customDropDownAdapter = CustomDropDownAdapter(this, DiagnosisList!!)
        binding.spinnerDiagnosis!!.adapter=customDropDownAdapter

        binding.cardViewSubmitSymptoms.setOnClickListener(this)
        binding.cardViewSubmitExamination.setOnClickListener(this)
        binding.cardViewSubmitDiagnosis.setOnClickListener(this)
        binding.cardViewSubmitEyeOpdDoctorsNote.setOnClickListener(this)


        binding.spinnerSystoms.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                handleSpinnerSelection(binding.spinnerSystoms, binding.LinearLayoutSymptomsOther,binding.edittextSystomes)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerExamination.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                handleSpinnerSelection(
                    binding.spinnerExamination,
                    binding.LinearLayoutExaminationOther,
                    binding.edittextExamination
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spinnerDiagnosis.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                handleSpinnerSelection(
                    binding.spinnerDiagnosis,
                    binding.LinearLayoutDiagnosisOther,
                    binding.edittextDiagnosis
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
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
        sessionManager= SessionManager(this)
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

    private fun handleSpinnerSelection(
        spinner: Spinner,
        layout: LinearLayout,
        editText:EditText
    ) {
        val selectedValue = spinner.selectedItem.toString()
        if (selectedValue == "Others") {
            layout.visibility = View.VISIBLE
            editText.visibility=View.GONE
        } else {
            layout.visibility = View.GONE
            editText.visibility=View.VISIBLE
        }
    }

    private fun initView() {
        val layoutManager= LinearLayoutManager(this)
        layoutManager.orientation= RecyclerView.VERTICAL
        binding.RecyclerViewSymptoms!!.layoutManager=layoutManager
        binding.RecyclerViewSymptoms!!.setHasFixedSize(true)

        val layoutManager1= LinearLayoutManager(this)
        layoutManager1.orientation= RecyclerView.VERTICAL
        binding.RecyclerViewExamination.layoutManager=layoutManager1
        binding.RecyclerViewExamination.setHasFixedSize(true)

        val layoutManager2= LinearLayoutManager(this)
        layoutManager2.orientation= RecyclerView.VERTICAL
        binding.RecyclerViewDiagnosis.layoutManager=layoutManager2
        binding.RecyclerViewDiagnosis.setHasFixedSize(true)
    }

    override fun onClick(v: View?) {
       when(v) {
           binding.cardViewSubmitSymptoms-> {
               var selected_eye=binding.spinnerSystomsEye.selectedItem.toString()
               var selected_symptoms=binding.spinnerSystoms.selectedItem.toString()
               var selected_eye_symptoms_details=binding.edittextSystomes.text.toString()
               var other_eye_symptoms_details=binding.EditTextSymptomOther.text.toString()+" "+binding.EditTextSymptomOtherDetails.text.toString()

               val resultDetails: String = when (selected_symptoms) {
                   "Others" -> other_eye_symptoms_details
                   else -> selected_eye_symptoms_details
               }
               add_Symptoms(selected_eye!!,selected_symptoms,resultDetails)
           }

           binding.cardViewSubmitExamination-> {
               var selected_eye=binding.spinnerExaminationEye.selectedItem.toString()
               var selected_symptoms=binding.spinnerExamination.selectedItem.toString()
               var selected_eye_symptoms_details=binding.edittextExamination.text.toString()
               var other_eye_symptoms_details=binding.EditTextOtherExamination.text.toString()+""+binding.EditTextOtherExaminationDetails.text.toString()

               val resultDetails: String = when (selected_symptoms) {
                   "Others" -> other_eye_symptoms_details
                   else -> selected_eye_symptoms_details
               }
               add_Examination(selected_eye!!,selected_symptoms,resultDetails)
           }

           binding.cardViewSubmitDiagnosis-> {
               var selected_eye=binding.spinnerDiagnosisEye.selectedItem.toString()
               var selected_symptoms=binding.spinnerDiagnosis.selectedItem.toString()
               var selected_eye_symptoms_details=binding.edittextDiagnosis.text.toString()
               var other_eye_symptoms_details=binding.EditTextDiagnosisOther.text.toString()+" "+binding.EditTextDiagnosisOtherDetails.text.toString()

               val resultDetails: String = when (selected_symptoms) {
                   "Others" -> other_eye_symptoms_details
                   else -> selected_eye_symptoms_details
               }
               add_Diagnosis(selected_eye!!,selected_symptoms,resultDetails)
           }

           binding.cardViewSubmitEyeOpdDoctorsNote-> {
               val radioGroup = binding.radioGroup
               val selectedRadioButtonId = radioGroup.checkedRadioButtonId
               if (selectedRadioButtonId != -1) {
                   val selectedRadioButton = binding.root.findViewById<RadioButton>(selectedRadioButtonId)
                    RecommendedradioButton = selectedRadioButton.text.toString()
               }

               val formattedStringSymptoms = AddSymptomsArrayList!!.joinToString { model ->
                   "${model.selected_symptoms} = ${model.selected_eye}"
               }
               val finalStringSymptomes = "{$formattedStringSymptoms}"

               val formattedStringSymptomsDetails = AddSymptomsArrayList!!.joinToString { model ->
                   "${model.selected_symptoms} = ${model.selected_eye_symptoms_details}"
               }
               val finalStringSymptomsDetails = "{$formattedStringSymptomsDetails}"

               val formattedStringExaminations = AddExaminationArrayList!!.joinToString { model ->
                   "${model.selected_symptoms} = ${model.selected_eye}"
               }
               val finalStringExaminations = "{$formattedStringExaminations}"

               val formattedStringExaminationsDetails = AddExaminationArrayList!!.joinToString { model ->
                   "${model.selected_symptoms} = ${model.selected_eye_symptoms_details}"
               }
               val finalStringExaminationsDetails = "{$formattedStringExaminationsDetails}"

               val formattedStringDiagnosis = AddDiagnosisArrayList!!.joinToString { model ->
                   "${model.selected_symptoms} = ${model.selected_eye}"
               }
               val finalStringDiagnosis = "{$formattedStringDiagnosis}"

               val formattedStringDiagnosisDetails = AddDiagnosisArrayList!!.joinToString { model ->
                   "${model.selected_symptoms} = ${model.selected_eye_symptoms_details}"
               }
               val finalStringDiagnosisDetails = "{$formattedStringDiagnosisDetails}"

               val notes=binding.EditTextNotes.text.toString()

               if (RecommendedradioButton!!.isNullOrEmpty()) {
                   Toast.makeText(this,"Recommendation required",Toast.LENGTH_SHORT).show()
               } else {
                   submitLocalEye_OPD_Doctors_Note(finalStringSymptomes,
                       finalStringSymptomsDetails,
                       finalStringExaminations,
                       finalStringExaminationsDetails,
                       finalStringDiagnosis,
                       finalStringDiagnosisDetails,
                       notes,
                       RecommendedradioButton
                   )
               }
           }
       }
    }

    private fun submitLocalEye_OPD_Doctors_Note(
        finalStringSymptomes: String,
        finalStringSymptomsDetails: String,
        finalStringExaminations: String,
        finalStringExaminationsDetails: String,
        finalStringDiagnosis: String,
        finalStringDiagnosisDetails: String,
        notes: String,
        recommendedradioButton: String?
    ) {
        val (patientId, campId, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
        val current_Date= ConstantsApp.getCurrentDate()
        val eyeOPD_Doctors_Note= Eye_OPD_Doctors_Note(0, campId!!,current_Date,
            finalStringDiagnosis,
            finalStringDiagnosisDetails,
            finalStringExaminations,
            finalStringExaminationsDetails,
            notes,
            recommendedradioButton!!,
            finalStringSymptomes,
            finalStringSymptomsDetails,
            patientId!!,
            userId!!
            )

        viewModel1.insertEyeOPD_Doctors_Note(eyeOPD_Doctors_Note)
        Response_EyeOPD_Doctors_Note()
    }

    private fun Response_EyeOPD_Doctors_Note() {
        viewModel1.toastMessage.observe(this
            , Observer { message ->
                showToast(message)
                gotoScreen(this,PatientForms::class.java)
            })
    }


    private fun add_Diagnosis(selected_eye: String, selected_symptoms: String, selected_eye_symptoms_details: String) {
        if (selected_eye == "Select Eye") {
            showToast("Please select eye")
        } else if (selected_symptoms == "Select Diagnosis") {
            showToast("Please select Diagnosis")
        } else {
            val isSymptomExists = AddDiagnosisArrayList?.any {
                it.selected_symptoms == selected_symptoms
            } ?: false

            if (isSymptomExists) {
                val existingIndex = AddDiagnosisArrayList?.indexOfFirst {
                    it.selected_symptoms == selected_symptoms
                } ?: -1

                if (existingIndex != -1) {
                    updateSymptom(existingIndex, selected_eye, selected_symptoms, selected_eye_symptoms_details,binding.RecyclerViewDiagnosis,AddDiagnosisArrayList!!,binding.LinearLayoutDiagnosisList!!)
                } else {
                    addNewSymptom(selected_eye, selected_symptoms, selected_eye_symptoms_details,binding.RecyclerViewDiagnosis,AddDiagnosisArrayList!!, binding.LinearLayoutDiagnosisList!!)
                }
            } else {
                addNewSymptom(selected_eye, selected_symptoms, selected_eye_symptoms_details,binding.RecyclerViewDiagnosis,AddDiagnosisArrayList!!,binding.LinearLayoutDiagnosisList!!)
            }
        }
    }

    private fun add_Examination(selected_eye: String, selected_symptoms: String, selected_eye_symptoms_details: String) {
        if (selected_eye == "Select Eye") {
            showToast("Please select eye")
        } else if (selected_symptoms == "Select Examination") {
            showToast("Please select Examination")
        } else {
            val isSymptomExists = AddExaminationArrayList?.any {
                it.selected_symptoms == selected_symptoms
            } ?: false

            if (isSymptomExists) {
                val existingIndex = AddExaminationArrayList?.indexOfFirst {
                    it.selected_symptoms == selected_symptoms
                } ?: -1

                if (existingIndex != -1) {
                    updateSymptom(existingIndex, selected_eye, selected_symptoms, selected_eye_symptoms_details,binding.RecyclerViewExamination,AddExaminationArrayList!!,binding.LinearLayoutExaminationList)
                } else {
                    addNewSymptom(selected_eye, selected_symptoms, selected_eye_symptoms_details,binding.RecyclerViewExamination,AddExaminationArrayList!!,binding.LinearLayoutExaminationList)
                }
            } else {
                addNewSymptom(selected_eye, selected_symptoms, selected_eye_symptoms_details,binding.RecyclerViewExamination,AddExaminationArrayList!!,binding.LinearLayoutExaminationList)
            }
        }
    }

    private fun add_Symptoms(selected_eye: String, selected_symptoms: String, selected_eye_symptoms_details: String) {
        if (selected_eye == "Select Eye") {
            showToast("Please select eye")
        }
        else if (selected_symptoms == "Select Symptoms") {
            showToast("Please select symptoms")
        } else {
            val isSymptomExists = AddSymptomsArrayList?.any {
                it.selected_symptoms == selected_symptoms
            } ?: false

            if (isSymptomExists) {
                val existingIndex = AddSymptomsArrayList?.indexOfFirst {
                    it.selected_symptoms == selected_symptoms
                } ?: -1

                if (existingIndex != -1) {
                    updateSymptom(existingIndex, selected_eye, selected_symptoms, selected_eye_symptoms_details,binding.RecyclerViewSymptoms,AddSymptomsArrayList!!,binding.LinearLayoutSymptomsList)
                } else {
                    addNewSymptom(selected_eye, selected_symptoms, selected_eye_symptoms_details,binding.RecyclerViewSymptoms,AddSymptomsArrayList!!,binding.LinearLayoutSymptomsList)
                }
            } else {
                addNewSymptom(selected_eye, selected_symptoms, selected_eye_symptoms_details,binding.RecyclerViewSymptoms,AddSymptomsArrayList!!,binding.LinearLayoutSymptomsList)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateSymptom(index: Int, selected_eye: String, selected_symptoms: String, selected_eye_symptoms_details: String,recyclerView: RecyclerView,arrayList:ArrayList<AddSymptomsModel>,layout: LinearLayout) {
        recyclerView.visibility=View.VISIBLE
        layout.visibility=View.VISIBLE

        arrayList?.get(index)?.let { existingItem ->
            val updatedItem = existingItem.copy(
                selected_eye = selected_eye,
                selected_symptoms = selected_symptoms,
                selected_eye_symptoms_details = selected_eye_symptoms_details
            )

            arrayList?.set(index, updatedItem)

            recyclerView.adapter?.notifyItemChanged(index)

            val dayWisePosition = ( binding.spinnerSystomsEye.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Eye")
            binding.spinnerSystomsEye.setSelection(dayWisePosition!!)

            val dayWisePosition1 = ( binding.spinnerSystoms.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Symptoms")
            binding.spinnerSystoms.setSelection(dayWisePosition1!!)
            binding.edittextSystomes.setText(null)
        }
    }

    private fun addNewSymptom(selected_eye: String, selected_symptoms: String, selected_eye_symptoms_details: String,recyclerView: RecyclerView,arrayList:ArrayList<AddSymptomsModel>,layout: LinearLayout) {
        recyclerView.visibility=View.VISIBLE
        layout.visibility=View.VISIBLE
        arrayList?.add(
            AddSymptomsModel(
                selected_eye,
                selected_symptoms,
                selected_eye_symptoms_details
            )
        )
        recyclerView.adapter =
            Add_AddSymptomsArrayList_Adapter(this, arrayList!!)

        val dayWisePosition = ( binding.spinnerSystomsEye.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Eye")
        binding.spinnerSystomsEye.setSelection(dayWisePosition!!)

        val dayWisePosition1 = ( binding.spinnerSystoms.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Symptoms")
        binding.spinnerSystoms.setSelection(dayWisePosition1!!)
        binding.edittextSystomes.setText(null)

        val dayWisePosition2 = ( binding.spinnerExaminationEye.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Eye")
        binding.spinnerExaminationEye.setSelection(dayWisePosition2!!)

        val dayWisePosition3 = ( binding.spinnerExamination.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Examination")
        binding.spinnerExamination.setSelection(dayWisePosition3!!)
        binding.edittextExamination.setText(null)

        val dayWisePosition4 = ( binding.spinnerDiagnosisEye.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Eye")
        binding.spinnerDiagnosisEye.setSelection(dayWisePosition4!!)

        val dayWisePosition5 = ( binding.spinnerDiagnosis.adapter as? CustomDropDownAdapter)?.dataSource?.indexOf("Select Diagnosis")
        binding.spinnerDiagnosis.setSelection(dayWisePosition5!!)
        binding.edittextDiagnosis.setText(null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        gotoScreen(this,PatientForms::class.java)
    }

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }
}