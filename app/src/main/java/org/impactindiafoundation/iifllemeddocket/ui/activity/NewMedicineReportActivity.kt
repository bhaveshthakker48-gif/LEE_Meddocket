package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.animation.core.LinearEasing
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.Activity.PharmaFormActivity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OpdPrescriptionFormViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityNewMedicineReportBinding
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.PatientMedicineAdapter

class NewMedicineReportActivity : BaseActivity() {

    private lateinit var binding:ActivityNewMedicineReportBinding
    private lateinit var patientMedicineAdapter: PatientMedicineAdapter
    private val patientMedicineList = ArrayList<PatientMedicine>()
    private val opdPrescriptionViewModel: OpdPrescriptionFormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewMedicineReportBinding.inflate(layoutInflater)
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

        initUi()
        opdPrescriptionViewModel.getPatientMedicineList()
        initObserver()
        setUpMedicineRecyclerView()
    }

    private fun initUi(){
        binding.toolbarMedicineReport.toolbar.title="Medicine Report"
    }

    private fun initObserver(){
        opdPrescriptionViewModel.patientMedicineList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            patientMedicineList.clear()
                            patientMedicineList.addAll(it.data)
                            patientMedicineAdapter.notifyDataSetChanged()
                            binding.llPatientColumn.visibility = View.VISIBLE
                            binding.rvPatientMedicine.visibility = View.VISIBLE
                            binding.tvNoDataFound.visibility = View.GONE
                        } else {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.llPatientColumn.visibility = View.GONE
                            binding.rvPatientMedicine.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@NewMedicineReportActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Utility.errorToast(this@NewMedicineReportActivity, "Unexpected error")
                }
            }
        }
    }


    private fun setUpMedicineRecyclerView() {
        patientMedicineAdapter = PatientMedicineAdapter(
            this@NewMedicineReportActivity,
            patientMedicineList,
            object : PatientMedicineAdapter.PatientMedicineAdapterEvent {
                override fun onItemClick(position: Int, isEdit: Boolean) {
                    val selectedItem = patientMedicineList[position]

                    if (isEdit) {
                        // ✅ Check if the item is synced (isSyn == 1)
                        if (selectedItem.isSyn == 1) {
                            Toast.makeText(
                                this@NewMedicineReportActivity,
                                "Synced items cannot be edited",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        // Proceed to edit if not synced
                        val intent = Intent(this@NewMedicineReportActivity, PharmaFormActivity::class.java)
                        intent.putExtra("formId", selectedItem._id)
                        intent.putExtra("editMode", true)
                        intent.putExtra("position", position)
                        startActivity(intent)
                    } else {
                        // View medicine details
                        val medicineList = selectedItem.prescriptionItems
                        val intent = Intent(this@NewMedicineReportActivity, MedicineDetailsActivity::class.java)
                        intent.putExtra("medicineList", ArrayList(medicineList)) // Convert to ArrayList for Serializable
                        startActivity(intent)
                    }
                }
            }
        )

        binding.rvPatientMedicine.apply {
            adapter = patientMedicineAdapter
            layoutManager = LinearLayoutManager(this@NewMedicineReportActivity)
        }
    }

}