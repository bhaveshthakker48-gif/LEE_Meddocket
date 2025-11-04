package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.OpdSyncTable
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.PharmaMainViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityOpdSyncTableBinding
import org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler.OpdSyncTableAdapter

class OpdSyncTableActivity : BaseActivity() {

    private lateinit var binding : ActivityOpdSyncTableBinding
    private lateinit var opdSyncTableAdapter: OpdSyncTableAdapter
    private val opdSynctTable = ArrayList<OpdSyncTable>()
    private val pharmaMainViewModel: PharmaMainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOpdSyncTableBinding.inflate(layoutInflater)
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

        pharmaMainViewModel.getOpdSyncTable()
        initObserver()
    }

    private fun initUi(){
        binding.toolbarMedicineReport.toolbar.title="OPD Sync Report"
    }

    private fun initObserver(){
        pharmaMainViewModel.opdSyncList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progress.show()
                }

                Status.SUCCESS -> {
                    progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            var totalsyncedCount = 0
                            var totalUnsyncedCount = 0
                            for (i in it.data){
                                totalsyncedCount += i.syncedCount
                                totalUnsyncedCount += i.unsyncFormCount
                            }
                            opdSynctTable.clear()
                            opdSynctTable.addAll(it.data)
                            val opdSyncItem = OpdSyncTable(
                                id = 0,
                                dateTime = "",
                                syncedCount = 0,
                                unsyncFormCount = 0
                            )
                            opdSynctTable.add(opdSyncItem)
                            setUpSyncTableRecyclerView(totalsyncedCount, totalUnsyncedCount)
                            binding.llPatientColumn.visibility = View.VISIBLE
                            binding.rvOpdSyncTable.visibility = View.VISIBLE
                            binding.tvNoDataFound.visibility = View.GONE
                        } else {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.llPatientColumn.visibility = View.GONE
                            binding.rvOpdSyncTable.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        Utility.errorToast(this@OpdSyncTableActivity, e.message.toString())
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()

                    Utility.errorToast(this@OpdSyncTableActivity, "Unexpected error")

                }
            }
        }
    }

    private fun setUpSyncTableRecyclerView(totalSyncedCount:Int, totalUnSyncedCount: Int){
        opdSyncTableAdapter = OpdSyncTableAdapter(this@OpdSyncTableActivity,opdSynctTable,totalSyncedCount,totalUnSyncedCount)
        binding.rvOpdSyncTable.apply {
            adapter = opdSyncTableAdapter
            layoutManager = LinearLayoutManager(this@OpdSyncTableActivity)
        }
    }
}