package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import org.impactindiafoundation.iifllemeddocket.Adapter.OrthosisSynTableAdapter
import org.impactindiafoundation.iifllemeddocket.architecture.model.AgeGroupCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisStatusCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeCount
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OrthosisViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityOrthosisAnalyticsBinding
import kotlin.math.roundToInt

class OrthosisAnalyticsActivity : BaseActivity() {

    private lateinit var binding : ActivityOrthosisAnalyticsBinding
    private val orthosisViewModel: OrthosisViewModel by viewModels()
    private lateinit var adapter: OrthosisSynTableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrthosisAnalyticsBinding.inflate(LayoutInflater.from(this))
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

        binding.toolbarDataAnalytics.toolbar.title = "Data Analytics"

        orthosisViewModel.patientCount.observe(this) {
            binding.totalPatientCount.text = it.toString()
        }

        orthosisViewModel.formCount.observe(this) {
            binding.totalFormCount.text = it.toString()
        }

        orthosisViewModel.malePatientCount.observe(this) {
            binding.malePatientCount.text = it.toString()
        }

        orthosisViewModel.femalePatientCount.observe(this) {
            binding.femalePatientCount.text = it.toString()
        }

        orthosisViewModel.otherPatientCount.observe(this) {
            binding.otherPatientCount.text = it.toString()
        }

        orthosisViewModel.diagnosisCounts.observe(this) { list ->
            showDiagnosisPieChart(list)
        }
        orthosisViewModel.fetchDiagnosisCounts()

        orthosisViewModel.orthosisTypeCounts.observe(this) { list ->
            showOrthosisTypePieChart(list)
        }
        orthosisViewModel.fetchOrthosisTypeCounts()

        orthosisViewModel.orthosisStatusCounts.observe(this) { list ->
            showOrthosisStatusPieChart(list)
        }
        orthosisViewModel.fetchOrthosisStatusCounts()

        orthosisViewModel.ageGroupCounts.observe(this) { list ->
            showAgeGroupPieChart(list)
        }
        orthosisViewModel.fetchAgeGroupCounts()

        setupRecyclerView()
        observeSynTableData()
    }

    private fun showDiagnosisPieChart(list: List<DiagnosisCount>) {
        val entries = list.map { PieEntry(it.count.toFloat()) } // remove label
        val colors = generateColors(list.size)

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 0f // hide values on pie slices
        dataSet.sliceSpace = 2f

        val data = PieData(dataSet)
        binding.diagnosisiPieChart.data = data
        binding.diagnosisiPieChart.description.isEnabled = false
        binding.diagnosisiPieChart.setDrawEntryLabels(false) // hide labels
        binding.diagnosisiPieChart.setDrawCenterText(false)
        binding.diagnosisiPieChart.legend.isEnabled = false
        binding.diagnosisiPieChart.animateY(1000)
        binding.diagnosisiPieChart.invalidate()

        // Update legend with same colors
        showDiagnosisLegend(list, colors)
    }

    private fun generateColors(size: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val rnd = java.util.Random()
        repeat(size) {
            colors.add(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
        }
        return colors
    }

    private fun showDiagnosisLegend(list: List<DiagnosisCount>, colors: List<Int>) {
        binding.specialityLegendContainer.removeAllViews()
        val total = list.sumOf { it.count }

        list.forEachIndexed { index, item ->
            val percentage = (item.count.toFloat() / total * 100).roundToInt()

            val legendItem = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 8, 0, 8)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            // Colored dot
            val colorView = View(this).apply {
                setBackgroundColor(colors[index])
                layoutParams = LinearLayout.LayoutParams(30, 30).apply {
                    marginEnd = 16
                }
            }

            // Text with name, count, percentage
            val textView = TextView(this).apply {
                text = "${item.diagnosis} : ${item.count} ($percentage%)"
                setTextColor(Color.BLACK)
                textSize = 14f
            }

            legendItem.addView(colorView)
            legendItem.addView(textView)
            binding.specialityLegendContainer.addView(legendItem)
        }
    }

    private fun showOrthosisTypePieChart(list: List<OrthosisTypeCount>) {
        val entries = list.map { PieEntry(it.count.toFloat()) }
        val colors = generateColors(list.size)

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 0f
        dataSet.sliceSpace = 2f

        val data = PieData(dataSet)
        binding.orthosisTypePieChart.data = data
        binding.orthosisTypePieChart.description.isEnabled = false
        binding.orthosisTypePieChart.setDrawEntryLabels(false)
        binding.orthosisTypePieChart.setDrawCenterText(false)
        binding.orthosisTypePieChart.legend.isEnabled = false
        binding.orthosisTypePieChart.animateY(1000)
        binding.orthosisTypePieChart.invalidate()

        showOrthosisTypeLegend(list, colors)
    }

    private fun showOrthosisTypeLegend(list: List<OrthosisTypeCount>, colors: List<Int>) {
        binding.orthosisTypeLegendContainer.removeAllViews()
        val total = list.sumOf { it.count }

        list.forEachIndexed { index, item ->
            val percentage = (item.count.toFloat() / total * 100).roundToInt()

            val legendItem = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 8, 0, 8)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            val colorView = View(this).apply {
                setBackgroundColor(colors[index])
                layoutParams = LinearLayout.LayoutParams(30, 30).apply { marginEnd = 16 }
            }

            val textView = TextView(this).apply {
                text = "${item.typeName} : ${item.count} ($percentage%)"
                setTextColor(Color.BLACK)
                textSize = 14f
            }

            legendItem.addView(colorView)
            legendItem.addView(textView)
            binding.orthosisTypeLegendContainer.addView(legendItem)
        }
    }

    private fun showOrthosisStatusPieChart(list: List<OrthosisStatusCount>) {
        val entries = list.map { PieEntry(it.count.toFloat()) }
        val colors = generateColors(list.size)

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 0f
        dataSet.sliceSpace = 2f

        val data = PieData(dataSet)
        binding.orthosisStatusPieChart.data = data
        binding.orthosisStatusPieChart.description.isEnabled = false
        binding.orthosisStatusPieChart.setDrawEntryLabels(false)
        binding.orthosisStatusPieChart.setDrawCenterText(false)
        binding.orthosisStatusPieChart.legend.isEnabled = false
        binding.orthosisStatusPieChart.animateY(1000)
        binding.orthosisStatusPieChart.invalidate()

        showOrthosisStatusLegend(list, colors)
    }

    private fun showOrthosisStatusLegend(list: List<OrthosisStatusCount>, colors: List<Int>) {
        binding.orthosisStatusLegendContainer.removeAllViews()
        val total = list.sumOf { it.count }

        list.forEachIndexed { index, item ->
            val percentage = (item.count.toFloat() / total * 100).roundToInt()

            val legendItem = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 8, 0, 8)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            val colorView = View(this).apply {
                setBackgroundColor(colors[index])
                layoutParams = LinearLayout.LayoutParams(30, 30).apply { marginEnd = 16 }
            }

            val textView = TextView(this).apply {
                text = "${item.status} : ${item.count} ($percentage%)"
                setTextColor(Color.BLACK)
                textSize = 14f
            }

            legendItem.addView(colorView)
            legendItem.addView(textView)
            binding.orthosisStatusLegendContainer.addView(legendItem)
        }
    }

    private fun showAgeGroupPieChart(list: List<AgeGroupCount>) {
        val entries = list.map { PieEntry(it.count.toFloat()) }
        val colors = generateColors(list.size)

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 0f
        dataSet.sliceSpace = 2f

        val data = PieData(dataSet)
        binding.ageSelectionPieChart.data = data
        binding.ageSelectionPieChart.description.isEnabled = false
        binding.ageSelectionPieChart.setDrawEntryLabels(false)
        binding.ageSelectionPieChart.setDrawCenterText(false)
        binding.ageSelectionPieChart.legend.isEnabled = false
        binding.ageSelectionPieChart.animateY(1000)
        binding.ageSelectionPieChart.invalidate()

        showAgeGroupLegend(list, colors)
    }

    private fun showAgeGroupLegend(list: List<AgeGroupCount>, colors: List<Int>) {
        binding.ageSelectionLegendContainer.removeAllViews()
        val total = list.sumOf { it.count }

        list.forEachIndexed { index, item ->
            val percentage = (item.count.toFloat() / total * 100).roundToInt()

            val legendItem = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 8, 0, 8)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            val colorView = View(this).apply {
                setBackgroundColor(colors[index])
                layoutParams = LinearLayout.LayoutParams(30, 30).apply { marginEnd = 16 }
            }

            val textView = TextView(this).apply {
                text = "${item.ageGroup} : ${item.count} ($percentage%)"
                setTextColor(Color.BLACK)
                textSize = 14f
            }

            legendItem.addView(colorView)
            legendItem.addView(textView)
            binding.ageSelectionLegendContainer.addView(legendItem)
        }
    }

    private fun setupRecyclerView() {
        adapter = OrthosisSynTableAdapter(emptyList())
        binding.RecyclerViewSyncedData.layoutManager = LinearLayoutManager(this)
        binding.RecyclerViewSyncedData.adapter = adapter
    }

    private fun observeSynTableData() {
        orthosisViewModel.allSynData.observe(this) { list ->
            if (list.isNotEmpty()) {
                adapter.updateList(list)
            } else {
//                Toast.makeText(this, "No PSD synced data found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}