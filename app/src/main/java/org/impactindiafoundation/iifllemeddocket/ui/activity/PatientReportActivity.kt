package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.OPD_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.REFRACTIVE_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.VISUAL_ACUITY_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.VITALS_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.PatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPatientReportBinding

class PatientReportActivity : BaseActivity() {

    private lateinit var binding:ActivityPatientReportBinding
    private val patientReportVM: PatientReportViewModel by viewModels()
    private var patientReportList = ArrayList<PatientReport>()
    private var report = ""
    private lateinit var patientReportAdapter: PatientReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientReportBinding.inflate(layoutInflater)
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

        initUi()
        patientReportVM.getPatientReportList()
        initObserver()
        setUpRecyclerView()
    }

    private fun initUi(){
        report = intent.getStringExtra("report").toString()
        if (report == OPD_FORM){
                    binding.campPatientToolBar.toolbar.title = "OPD Report"
        }
        else if (report == VITALS_FORM){
                    binding.campPatientToolBar.toolbar.title = "Vitals Report"
        }
        else if (report == VISUAL_ACUITY_FORM){
                    binding.campPatientToolBar.toolbar.title = "Visual Acuity Report"
        }
        else if (report == REFRACTIVE_FORM){
                    binding.campPatientToolBar.toolbar.title = "Refractive Error Report"
        }
    }
    private fun initObserver(){
        patientReportVM.patientReportList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            patientReportList.clear()

                            var filteredReport = ArrayList<PatientReport>()
                            if (report == OPD_FORM){
                                filteredReport = it.data.filter { it.formType == OPD_FORM } as ArrayList<PatientReport>
                            }
                            else if (report == VITALS_FORM){
                                filteredReport =  it.data.filter { it.formType == VITALS_FORM } as ArrayList<PatientReport>

                            }
                            else if (report == VISUAL_ACUITY_FORM){
                                filteredReport =  it.data.filter { it.formType == VISUAL_ACUITY_FORM } as ArrayList<PatientReport>
                            }
                            else if (report == REFRACTIVE_FORM){
                                filteredReport =  it.data.filter { it.formType == REFRACTIVE_FORM } as ArrayList<PatientReport>
                            }
                            patientReportList.addAll(filteredReport)
                            patientReportAdapter.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@PatientReportActivity, e.message.toString())

                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@PatientReportActivity, "Unexpected error")
                }
            }
        }

    }

    private fun setUpRecyclerView() {
        patientReportAdapter =
            PatientReportAdapter(this@PatientReportActivity, patientReportList, object : PatientReportAdapter.PatientReportAdapterEvent {
                override fun onItemClick(position: Int) {
                    val patientReport = patientReportList[position]
                    if (patientReport.isSynced == 0){
                        if (patientReport.formType == OPD_FORM){
                            val intent = Intent(this@PatientReportActivity,NewOpdInvestigationsActivity::class.java)
                            intent.putExtra("localFormId",patientReport.formId)
                            intent.putExtra("reportFormId",patientReport.id)
                            startActivity(intent)
                        }else if (patientReport.formType == VITALS_FORM){
                            val intent = Intent(this@PatientReportActivity,NewVitalsActivity::class.java)
                            intent.putExtra("localFormId",patientReport.formId)
                            intent.putExtra("reportFormId",patientReport.id)
                            startActivity(intent)
                        }else if (patientReport.formType == VISUAL_ACUITY_FORM){
                            val intent = Intent(this@PatientReportActivity,NewVisualAcuityActivity::class.java)
                            intent.putExtra("localFormId",patientReport.formId)
                            intent.putExtra("reportFormId",patientReport.id)
                            startActivity(intent)
                        }else if (patientReport.formType == REFRACTIVE_FORM){
                            val intent = Intent(this@PatientReportActivity,RefractiveErrorFormActivity::class.java)
                            intent.putExtra("localFormId",patientReport.formId)
                            intent.putExtra("reportFormId",patientReport.id)
                            startActivity(intent)
                        }
                    }
                    else{
                        Utility.infoToast(this@PatientReportActivity,"Cannot Edit Synced Data")
                    }

                }

            })

        binding.rvPatientReport.apply {
            adapter = patientReportAdapter
            layoutManager = LinearLayoutManager(this@PatientReportActivity)
        }
    }
}