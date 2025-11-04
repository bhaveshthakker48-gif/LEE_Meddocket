package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
    private val campPatientViewModel: CampPatientViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    private var leastCampId = 0
    private var isCampComplete = false
    private var campPatientPresent = false
    private val filteredCampPatientList = ArrayList<CampPatientDataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCampPatientListBinding.inflate(layoutInflater)
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
            leastCampId = SharedPrefUtil.getPrfInt(this@CampPatientListActivity, SharedPrefUtil.USER_CAMP)
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
                    Log.d("Raju", "Fetching camp patient list from API...")
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            campPatientPresent = true

                            // ✅ Remove duplicates from server records
                            val distinctServerList = it.data.distinctBy { item -> item.temp_patient_id ?: item.patient_id }

                            tempCampPatientList.clear()
                            tempCampPatientList.addAll(distinctServerList)

                            Log.d("Raju", "API patients received: ${it.data.size}")
                            Log.d("Raju", "Distinct server patients: ${distinctServerList.size}")
                            Log.d("Raju", "Total tempCampPatientList count: ${tempCampPatientList.size}")

                            binding.rvCampPatientList.visibility = View.VISIBLE
                            binding.tvNoDataFound.visibility = View.GONE
                        } else {
                            campPatientPresent = false
                            Log.d("Raju", "No camp patient data found from API.")
                            binding.rvCampPatientList.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            stopImpactLoader()
                        }
                    } catch (e: Exception) {
                        Log.e("Raju", "Error processing API data: ${e.message}")
                        Utility.errorToast(this@CampPatientListActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Log.e("Raju", "API call failed: ${it.message}")
                    Utility.errorToast(this@CampPatientListActivity, "Unexpected error")
                }
            }
        }

        campPatientViewModel.orthosisPatientFormList.observe(this) { patient ->
            when (patient.status) {
                Status.LOADING -> {
                    showImpactLoader()
                    Log.d("Raju", "Fetching local orthosis patient list...")
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!patient.data.isNullOrEmpty()) {
                            Log.d("Raju", "Local patients found: ${patient.data.size}")

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

                            Log.d("Raju", "Filtered unsynced local patients: ${tempPatientList.size}")

                            tempCampPatientList.withIndex().forEach { (index, form) ->
                                val matchingTempPatient =
                                    tempPatientList.find { it.temp_patient_id == form.temp_patient_id }
                                if (matchingTempPatient != null) {
                                    form.fromDevice = true
                                    Log.d("Raju", "Duplicate found for temp_patient_id: ${form.temp_patient_id}")
                                    if (matchingTempPatient.isEdited) {
                                        tempCampPatientList[index].isEdited = true
                                    }
                                }
                            }

                            val filteredList = tempPatientList.filter { it.isSynced == 0 }
                            val filteredListForEdited = tempCampPatientList.filter { !it.isEdited }

                            Log.d("Raju", "filteredList (local unsynced): ${filteredList.size}")
                            Log.d("Raju", "filteredListForEdited (remote not edited): ${filteredListForEdited.size}")

                            val combinedList = ArrayList<CampPatientDataItem>()
                            combinedList.addAll(filteredList)
                            combinedList.addAll(filteredListForEdited)

                            Log.d("Raju", "Before distinct: ${combinedList.size}")

                            val uniqueList = ArrayList<CampPatientDataItem>()

                            combinedList.groupBy { it.temp_patient_id ?: it.patient_id }.forEach { (_, items) ->
                                val hasUnsynced = items.any { it.isSynced == 0 }
                                if (hasUnsynced) {
                                    uniqueList.addAll(items) // show duplicates for unsynced ❌
                                } else {
                                    uniqueList.add(items.first()) // keep only one for synced
                                }
                            }


                            Log.d("Raju", "After distinct (unique patients): ${uniqueList.size}")

                            campPatientList.clear()
                            campPatientList.addAll(uniqueList)

                            filteredCampPatientList.clear()
                            filteredCampPatientList.addAll(uniqueList)

                            Log.d("Raju", "Recycler list final count: ${filteredCampPatientList.size}")

                            campPatientAdapter.notifyDataSetChanged()

                            if (!campPatientPresent) {
                                binding.rvCampPatientList.visibility = View.VISIBLE
                                binding.tvNoDataFound.visibility = View.GONE
                            }
                        } else {
                            if (!campPatientPresent) {
                                Log.d("Raju", "No local patients found.")
                                binding.rvCampPatientList.visibility = View.GONE
                                binding.tvNoDataFound.visibility = View.VISIBLE
                                stopImpactLoader()
                            } else {
                                Log.d("Raju", "Local empty, showing API data: ${tempCampPatientList.size}")
                                campPatientList.addAll(tempCampPatientList)
                                filteredCampPatientList.addAll(campPatientList)
                                campPatientAdapter.notifyDataSetChanged()
                                binding.rvCampPatientList.visibility = View.VISIBLE
                                binding.tvNoDataFound.visibility = View.GONE
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Raju", "Error in merging lists: ${e.message}")
                        Utility.errorToast(this@CampPatientListActivity, e.message.toString())
                    }
                    stopImpactLoader()
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Log.e("Raju", "Error fetching local data: ${patient.message}")
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

                    Log.d(
                        "sahil",
                        "Clicked Patient -> name=${patientData.id}, isLocal=${patientData.isLocal}, camp_id=${patientData.camp_id}, leastCampId=$leastCampId, isCampComplete=$isCampComplete, isSynced=${patientData.isSynced}"
                    )

                    if (patientData.isLocal) {
                        if (leastCampId == 0) {
                            Log.d("sahil", "Opening OrthosisActivity for local patient (leastCampId=0)")
                            val intent = Intent(this@CampPatientListActivity, OrthosisActivity::class.java)
                            intent.putExtra("screen", "Camp_List")
                            intent.putExtra("temp_id", patientData.temp_patient_id)
                            intent.putExtra("local_patient_id", patientData.id.toString())
                            intent.putExtra("edit", patientData.isSynced == 0)
                            startActivity(intent)
                        } else {
                            if (patientData.camp_id.toInt() == leastCampId && isCampComplete) {
                                Log.d("sahil", "Camp already completed for local patient")
                                Utility.infoToast(this@CampPatientListActivity, "Camp Completed")
                            } else if (patientData.camp_id.toInt() > leastCampId || patientData.camp_id.toInt() != leastCampId) {
                                Log.d("sahil", "Local patient from future/other camp → Complete previous camp first")
                                Utility.infoToast(this@CampPatientListActivity, "Complete Previous Camp")
                            } else {
                                Log.d("sahil", "Opening OrthosisActivity for local patient (valid camp)")
                                val intent = Intent(this@CampPatientListActivity, OrthosisActivity::class.java)
                                intent.putExtra("screen", "Camp_List")
                                intent.putExtra("temp_id", patientData.temp_patient_id)
                                intent.putExtra("local_patient_id", patientData.id.toString())
                                intent.putExtra("edit", patientData.isSynced == 0)
                                startActivity(intent)
                            }
                        }
                    } else {
                        if (patientData.camp_id.toInt() == leastCampId && isCampComplete) {
                            Log.d("sahil", "Camp already completed for synced patient")
                            Utility.infoToast(this@CampPatientListActivity, "Camp Completed")
                        } else if (patientData.camp_id.toInt() > leastCampId || patientData.camp_id.toInt() != leastCampId) {
                            if (leastCampId == 0) {
                                Log.d("sahil", "Opening OrthosisFittingActivity for synced patient (leastCampId=0)")
                                val intent = Intent(this@CampPatientListActivity, OrthosisFittingActivity::class.java)
                                intent.putExtra("screen", "Camp_List")
                                intent.putExtra("temp_id", patientData.temp_patient_id)
                                intent.putExtra("local_patient_id", patientData.id)
                                intent.putExtra("is_edited", patientData.isEdited)
                                intent.putExtra("form_id", patientData.id)
                                intent.putExtra("edit", patientData.isSynced == 0)
                                startActivity(intent)
                            } else {
                                Log.d("sahil", "Synced patient from future/other camp → Complete previous camp first")
                                Utility.infoToast(this@CampPatientListActivity, "Complete Previous Camp")
                            }
                        } else {
                            Log.d("sahil", "Opening OrthosisFittingActivity for synced patient (valid camp)")
                            val intent = Intent(this@CampPatientListActivity, OrthosisFittingActivity::class.java)
                            intent.putExtra("screen", "Camp_List")
                            intent.putExtra("temp_id", patientData.temp_patient_id)
                            intent.putExtra("local_patient_id", patientData.id)
                            intent.putExtra("is_edited", patientData.isEdited)
                            intent.putExtra("form_id", patientData.id)
                            intent.putExtra("edit", patientData.isSynced == 0)
                            startActivity(intent)
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

//                            campPatientList.addAll(filteredList)
//                            campPatientList.addAll(filteredListForEdited)
//                            filteredCampPatientList.addAll(campPatientList)
//                            campPatientAdapter.notifyDataSetChanged()
