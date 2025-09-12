package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SharedPrefUtil
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.Measurement
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.CampPatientViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OrthosisViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityCampPatientListBinding
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.CampPatientAdapter

class CampPatientListActivity : BaseActivity() {
    private lateinit var binding: ActivityCampPatientListBinding
    private lateinit var campPatientAdapter: CampPatientAdapter
    private val campPatientList = ArrayList<CampPatientDataItem>()
    private val tempCampPatientList = ArrayList<CampPatientDataItem>()
    private val campPatientListNew = ArrayList<OrthosisPatientForm>()
    private val orthosisPatientForm = ArrayList<OrthosisPatientForm>()
    private val campPatientViewModel: CampPatientViewModel by viewModels()
    lateinit var sessionManager: SessionManager

    private var leastCampId = 0
    private var isCampComplete = false
    private var campPatientPresent = false

    private val filteredCampPatientList = ArrayList<CampPatientDataItem>()
    private var progressForThis = progress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCampPatientListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply padding to the activity content (this handles all root layouts properly)
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        sessionManager = SessionManager(this)
        initUi()
        setUpCampPatientRecyclerView()
        showImpactLoader()
        getCampPatientList()
        Handler(Looper.getMainLooper()).postDelayed({
            getLocalPatientList()

        }, 2000)

        initObserver()
    }


    private fun initUi() {
        leastCampId = intent.getIntExtra(Constants.LEAST_CAMP_ID, 0)
        if (leastCampId == 0) {
            leastCampId =
                SharedPrefUtil.getPrfInt(this@CampPatientListActivity, SharedPrefUtil.USER_CAMP)
        }
        isCampComplete = intent.getBooleanExtra(Constants.IS_CAMP_COMPLETE, false)

        binding.campPatientToolBar.toolbar.title = "Patient Report"

        binding.etPatientNameSearch.doAfterTextChanged { editable ->
            val searchText = editable.toString().trim()
            if (searchText.isNotEmpty()) {
                filteredCampPatientList.clear()
                filteredCampPatientList.addAll(campPatientList.filter {
                    it.patient_name.contains(
                        searchText,
                        ignoreCase = true
                    )
                })
                campPatientAdapter.notifyDataSetChanged()
            } else {
                filteredCampPatientList.clear()
                filteredCampPatientList.addAll(campPatientList)
                campPatientAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun initObserver() {
        campPatientViewModel.campPatientList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showImpactLoader()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            campPatientPresent = true
                            tempCampPatientList.addAll(it.data)
                            binding.rvCampPatientList.visibility = View.VISIBLE
                            binding.tvNoDataFound.visibility = View.GONE
                        } else {
                            campPatientPresent = false
                            binding.rvCampPatientList.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            stopImpactLoader()
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@CampPatientListActivity, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@CampPatientListActivity, "Unexpected error")
                }
            }
        }

        campPatientViewModel.orthosisPatientFormList.observe(this) { patient ->
            when (patient.status) {
                Status.LOADING -> {
                    showImpactLoader()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!patient.data.isNullOrEmpty()) {
                            val tempPatientList = ArrayList<CampPatientDataItem>()
                            for (i in patient.data) {
                                val orthosisDta = ArrayList<OrthosisPatientData>()
                                if (i.isSynced == 0) {
                                    orthosisDta.addAll(i.orthosisList)
                                    val campPatientMap = CampPatientDataItem(
                                        camp_id = i.campId.toString(),
                                        camp_name = i.campName,
                                        id = i.id,
                                        orthosis_date = i.orthosisDate,
                                        patientOrthosisTypes = orthosisDta,
                                        patient_age_years = i.patientAgeYears,
                                        patient_contact_no = i.patientContactNo,
                                        patient_gender = i.patientGender,
                                        patient_height_cm = i.patientHeightCm,
                                        patient_id = i.patientId.toString(),
                                        patient_name = i.patientName,
                                        patient_weight_kg = i.patientWeightKg,
                                        diagnosis = i.diagnosis,
                                        diagnosisId = i.diagnosisId,
                                        equipment_support = i.equipmentSupport,
                                        equipment_category = i.equipmentCategory,
                                        equipmentId = i.equipmentId,
                                        equipment_status = i.equipmentStatus,
                                        equipment_status_notes = i.equipmentStatusNotes,
                                        temp_patient_id = i.tempPatientId,
                                        isLocal = i.isLocal,
                                        isSynced = i.isSynced,
                                        isEdited = i.isEdited
                                    )
                                    tempPatientList.add(campPatientMap)
                                }
                            }
                            tempCampPatientList.withIndex().forEach { (index, form) ->
                                val matchingTempPatient =
                                    tempPatientList.find { it.temp_patient_id == form.temp_patient_id }
                                if (matchingTempPatient != null) {
                                    form.fromDevice = true
                                    println("Duplicate found: $form")
                                    if (matchingTempPatient.isEdited) {
                                        tempCampPatientList[index].isEdited = true
                                    }
                                }
                            }
                            val filteredList = tempPatientList.filter { it.isSynced == 0 }
                            val filteredListForEdited = tempCampPatientList.filter { !it.isEdited }
                            campPatientList.addAll(filteredList)
                            campPatientList.addAll(filteredListForEdited)
                            filteredCampPatientList.addAll(campPatientList)
                            campPatientAdapter.notifyDataSetChanged()

                            if (!campPatientPresent) {
                                binding.rvCampPatientList.visibility = View.VISIBLE
                                binding.tvNoDataFound.visibility = View.GONE
                            }

                        } else {
                            if (!campPatientPresent) {
                                binding.rvCampPatientList.visibility = View.GONE
                                binding.tvNoDataFound.visibility = View.VISIBLE
                                stopImpactLoader()
                            } else {
                                campPatientList.addAll(tempCampPatientList)
                                filteredCampPatientList.addAll(campPatientList)
                                campPatientAdapter.notifyDataSetChanged()
                                binding.rvCampPatientList.visibility = View.VISIBLE
                                binding.tvNoDataFound.visibility = View.GONE
                            }
                        }

                    } catch (e: Exception) {
                        Utility.errorToast(this@CampPatientListActivity, e.message.toString())
                    }
                    stopImpactLoader()
                }

                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@CampPatientListActivity, "Unexpected error")
                }
            }
        }
    }

    private fun setUpCampPatientRecyclerView() {
        campPatientAdapter = CampPatientAdapter(
            this@CampPatientListActivity,
            filteredCampPatientList,
            object : CampPatientAdapter.CampPatientAdapterEvent {
                override fun onItemClick(position: Int) {
                    val patientData = filteredCampPatientList[position]
                    if (patientData.isLocal) {
                        if (leastCampId == 0) {
                            val intent =
                                Intent(this@CampPatientListActivity, OrthosisActivity::class.java)
                            intent.putExtra("screen", "Camp_List")
                            intent.putExtra("temp_id", patientData.temp_patient_id)
                            intent.putExtra("local_patient_id", patientData.id.toString())
                            if (patientData.isSynced == 0) {
                                intent.putExtra("edit", true)
                            } else {
                                intent.putExtra("edit", false)
                            }
                            startActivity(intent)
                        } else {
                            if (patientData.camp_id.toInt() == leastCampId && isCampComplete) {
                                Utility.infoToast(this@CampPatientListActivity, "Camp Completed")
                            } else {
                                if (patientData.camp_id.toInt() > leastCampId || patientData.camp_id.toInt() != leastCampId) {
                                    Utility.infoToast(
                                        this@CampPatientListActivity,
                                        "Complete Previous Camp"
                                    )
                                } else {
                                    val intent =
                                        Intent(
                                            this@CampPatientListActivity,
                                            OrthosisActivity::class.java
                                        )
                                    intent.putExtra("screen", "Camp_List")
                                    intent.putExtra("temp_id", patientData.temp_patient_id)
                                    intent.putExtra("local_patient_id", patientData.id.toString())
                                    if (patientData.isSynced == 0) {
                                        intent.putExtra("edit", true)
                                    } else {
                                        intent.putExtra("edit", false)
                                    }
                                    startActivity(intent)
                                }
                            }
                        }
                    } else {
                        if (patientData.camp_id.toInt() == leastCampId && isCampComplete) {
                            Utility.infoToast(this@CampPatientListActivity, "Camp Completed")
                        } else {
                            if (patientData.camp_id.toInt() > leastCampId || patientData.camp_id.toInt() != leastCampId) {
                                if (leastCampId == 0) {
                                    val intent = Intent(
                                        this@CampPatientListActivity,
                                        OrthosisFittingActivity::class.java
                                    )
                                    intent.putExtra("screen", "Camp_List")
                                    intent.putExtra("temp_id", patientData.temp_patient_id)
                                    intent.putExtra("local_patient_id", patientData.id)
                                    //for edited patients who's data are present in orthosis form table
                                    intent.putExtra("is_edited", patientData.isEdited)
                                    intent.putExtra("form_id", patientData.id)
                                    if (patientData.isSynced == 0) {
                                        intent.putExtra("edit", true)
                                    } else {
                                        intent.putExtra("edit", false)
                                    }
                                    startActivity(intent)
                                } else {
                                    Utility.infoToast(
                                        this@CampPatientListActivity,
                                        "Complete Previous Camp"
                                    )
                                }
                            } else {
                                val intent = Intent(
                                    this@CampPatientListActivity,
                                    OrthosisFittingActivity::class.java
                                )
                                intent.putExtra("screen", "Camp_List")
                                intent.putExtra("temp_id", patientData.temp_patient_id)
                                intent.putExtra("local_patient_id", patientData.id)
                                intent.putExtra("is_edited", patientData.isEdited)
                                intent.putExtra("form_id", patientData.id)
                                if (patientData.isSynced == 0) {
                                    intent.putExtra("edit", true)
                                } else {
                                    intent.putExtra("edit", false)
                                }
                                startActivity(intent)
                            }
                        }
                    }
                }
            })
        binding.rvCampPatientList.apply {
            adapter = campPatientAdapter
            layoutManager = LinearLayoutManager(this@CampPatientListActivity)
        }
    }

    private fun getCampPatientList() {
        campPatientViewModel.getCampPatientList()
    }

    private fun getLocalPatientList() {
        campPatientViewModel.getOrthosisPatientForm()
    }

    private fun showImpactLoader(){
        binding.tvNoDataFound.visibility = View.GONE
        binding.ivImpactLoader.visibility = View.VISIBLE

        Glide.with(this)
            .asGif()
            .load(R.raw.preloader) // Replace with your GIF resource
            .into(binding.ivImpactLoader)
    }

    private fun stopImpactLoader(){
        binding.ivImpactLoader.visibility = View.GONE

        Glide.with(this).clear(binding.ivImpactLoader)
    }
}