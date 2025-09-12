package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityMedicineDetailsBinding
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.MedicineItemAdapter

class MedicineDetailsActivity : BaseActivity() {

    private lateinit var binding:ActivityMedicineDetailsBinding
    private lateinit var medicineAdapter : MedicineItemAdapter
    private var medicineList = ArrayList<PatientMedicine.PrescriptionItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMedicineDetailsBinding.inflate(layoutInflater)
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

        setUpMedicineRecyclerView()
        initUi()
    }

    private fun initUi(){
        binding.toolbarMedicineReport.toolbar.title="Medicine Details"

        try {
            val medicineListIntent = intent.getSerializableExtra("medicineList") as? ArrayList<PatientMedicine.PrescriptionItem>
            medicineList.addAll(medicineListIntent!!)
            medicineAdapter.notifyDataSetChanged()
            if (medicineList.isNullOrEmpty()){
                binding.rvMedicineList.visibility = View.GONE
                binding.llMedicineColumn.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
            }
            else{
                binding.rvMedicineList.visibility = View.VISIBLE
                binding.llMedicineColumn.visibility = View.VISIBLE
                binding.tvNoDataFound.visibility = View.GONE
            }



        }catch (e:Exception){
            Log.d("ERROR",e.message.toString())
        }


    }

    private fun setUpMedicineRecyclerView(){
        medicineAdapter = MedicineItemAdapter(this@MedicineDetailsActivity,medicineList)
        binding.rvMedicineList.apply {
            adapter = medicineAdapter
            layoutManager = LinearLayoutManager(this@MedicineDetailsActivity)
        }

    }
}