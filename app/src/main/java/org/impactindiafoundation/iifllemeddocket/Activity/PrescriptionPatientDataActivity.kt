package org.impactindiafoundation.iifllemeddocket.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.Adapter.PrescriptionPatientReportAdapter
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.ent.EntPatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPrescriptionPatientDataBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity

class PrescriptionPatientDataActivity : BaseActivity(), PrescriptionPatientReportAdapter.PatientReportAdapterEvent {

    private lateinit var binding: ActivityPrescriptionPatientDataBinding
    private val patientReportVM: EntPatientReportViewModel by viewModels()
    private val patientReportList = ArrayList<PrescriptionPatientReport>()
    private lateinit var prescriptionPatientReportAdapter: PrescriptionPatientReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionPatientDataBinding.inflate(LayoutInflater.from(this))
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
        setUpRecyclerView()
        observeViewModel()
        setUpOfToolbar()

        patientReportVM.getPrescriptionPatientReportList()
    }

    private fun setUpOfToolbar(){
        binding.campPatientToolBar.toolbar.title = "Prescription Patients Reports"
    }

    private fun setUpRecyclerView() {
        prescriptionPatientReportAdapter = PrescriptionPatientReportAdapter(this, patientReportList, this)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PrescriptionPatientDataActivity)
            adapter = prescriptionPatientReportAdapter
        }
    }

    private fun observeViewModel() {
        patientReportVM.prescriptionPatientReportList.observe(this) { result ->
            when (result.status) {
                Status.LOADING -> progress.show()
                Status.SUCCESS -> {
                    progress.dismiss()   // ✅ add this
                    result.data?.let { reports ->
                        patientReportList.clear()
                        patientReportList.addAll(reports)
                        prescriptionPatientReportAdapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this, "Unexpected error")
                }
            }
        }
    }


    override fun onItemClick(position: Int) {
        val patientReport = patientReportList[position]
        val intent = Intent(this, PrescriptionDisbributionActivity::class.java).apply {
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
        }
        startActivity(intent)
    }
}
