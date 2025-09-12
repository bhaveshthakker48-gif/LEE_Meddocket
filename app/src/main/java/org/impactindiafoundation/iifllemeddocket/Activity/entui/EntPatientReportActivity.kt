package org.impactindiafoundation.iifllemeddocket.Activity.entui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.Activity.pathologyui.PathologyActivity
import org.impactindiafoundation.iifllemeddocket.Adapter.entadapter.EntPatientReportAdapter
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_AUDIOMETRY
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_OPD_DOCTOR_NOTES
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_POST_OP_NOTES
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_PRE_OP_DETAILS
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ENT_SURGICAL_NOTES
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.PATHOLOGY_REPORTS
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityEntPatientReportBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity

class EntPatientReportActivity : BaseActivity() {

    private lateinit var binding: ActivityEntPatientReportBinding
    private val patientReportVM: EntPatientReportViewModel by viewModels()
    private val patientReportList = ArrayList<EntPatientReport>()
    private lateinit var entPatientReportAdapter: EntPatientReportAdapter

    private var report: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEntPatientReportBinding.inflate(layoutInflater)
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

        report = intent.getStringExtra("report") ?: ""
        setupToolbarTitle()
        setupRecyclerView()
        observeViewModel()
        patientReportVM.getPatientReportList()
    }

    private fun setupToolbarTitle() {
        val title = when (report) {
            ENT_OPD_DOCTOR_NOTES -> "ENT OPD Doctor Notes"
            ENT_AUDIOMETRY -> "ENT Audiometry"
            ENT_PRE_OP_DETAILS -> "ENT Pre OP Details"
            ENT_SURGICAL_NOTES -> "ENT Surgical Notes"
            ENT_POST_OP_NOTES -> "ENT Post Op Details"
            PATHOLOGY_REPORTS -> "Pathology Reports"
            else -> "Patient Reports"
        }
        binding.campPatientToolBar.toolbar.title = title
    }

    private fun observeViewModel() {
        patientReportVM.patientReportList.observe(this) { result ->
            when (result.status) {
                Status.LOADING -> progress.show()
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        patientReportList.clear()
                        val formTypes = result.data?.map { it.formType }?.toSet()
                        Log.d("PatientReports", "Available formTypes: $formTypes")

                        val filtered = result.data?.filter {
                            Log.d(
                                "PatientReports",
                                "Comparing: formType='${it.formType}' == report='${report}'"
                            )
                            it.formType.trim().equals(report.trim(), ignoreCase = true)
                        } ?: emptyList()
                        Log.d(
                            "PatientReports",
                            "Filtered '$report' reports count: ${filtered.size}"
                        )

                        patientReportList.addAll(filtered)

                        binding.textView.text = "Total Forms: ${filtered.size}"

                        entPatientReportAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                        Utility.errorToast(this, e.message.toString())
                    }
                }

                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this, "Unexpected error")
                }
            }
        }
    }

    private fun setupRecyclerView() {
        entPatientReportAdapter = EntPatientReportAdapter(
            this,
            patientReportList,
            object : EntPatientReportAdapter.PatientReportAdapterEvent {
                override fun onItemClick(position: Int) {
                    val patientReport = patientReportList[position]
                    Log.d("PatientReports", "Clicked formType: '${patientReport.formType}'")
                    val intent = when (patientReport.formType) {
                        ENT_OPD_DOCTOR_NOTES -> Intent(
                            this@EntPatientReportActivity,
                            OpEntDoctorNotesActivity::class.java
                        )

                        ENT_AUDIOMETRY -> Intent(
                            this@EntPatientReportActivity,
                            EntAudiometryActivity::class.java
                        )

                        ENT_SURGICAL_NOTES -> Intent(
                            this@EntPatientReportActivity,
                            EntSurgicalNotesActivity::class.java
                        )

                        ENT_PRE_OP_DETAILS -> Intent(
                            this@EntPatientReportActivity,
                            EntPreOpDetailsActivity::class.java
                        )

                        ENT_POST_OP_NOTES -> Intent(
                            this@EntPatientReportActivity,
                            EntPostOpNotesActivity::class.java
                        )

                        PATHOLOGY_REPORTS -> Intent(
                            this@EntPatientReportActivity,
                            PathologyActivity::class.java
                        )

                        else -> null
                    }

                    intent?.apply {
                        putExtra("localFormId", patientReport.formId)
                        putExtra("reportFormId", patientReport.id)
                        putExtra("patientFname", patientReport.patientFname)
                        putExtra("patientLname", patientReport.patientLname)
                        putExtra("patientId", patientReport.patientId)
                        putExtra("patientAge", patientReport.patientAge)
                        putExtra("patientGen", patientReport.patientGen)
                        putExtra("campId", patientReport.camp_id)
                        putExtra("location", patientReport.location)
                        putExtra("AgeUnit", patientReport.AgeUnit)
                        startActivity(this)
                    } ?: Utility.infoToast(
                        this@EntPatientReportActivity,
                        "Form type not supported."
                    )
                }
            }
        )

        binding.rvPatientReport.apply {
            adapter = entPatientReportAdapter
            layoutManager = LinearLayoutManager(this@EntPatientReportActivity)
        }
    }
}
