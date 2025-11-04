package org.impactindiafoundation.iifllemeddocket.Activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.architecture.model.AgeGroupCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.BrandUsage
import org.impactindiafoundation.iifllemeddocket.architecture.model.GenericUsage
import org.impactindiafoundation.iifllemeddocket.architecture.model.SpecialityCount
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OpdPrescriptionFormViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPharmaAnalyticsBinding
import org.impactindiafoundation.iifllemeddocket.ui.activity.BaseActivity
import kotlin.math.roundToInt

class PharmaAnalyticsActivity : BaseActivity() {

    private lateinit var binding: ActivityPharmaAnalyticsBinding
    private val opdPrescriptionViewModel: OpdPrescriptionFormViewModel by viewModels()
    private val specialityColors = mapOf(
        "Ophthalmologist" to Color.parseColor("#F44336"), // Red
        "ENT Surgeon" to Color.parseColor("#2196F3"),     // Blue
        "Plastic Surgeon" to Color.parseColor("#9C27B0"), // Purple
        "Dentist" to Color.parseColor("#4CAF50"),         // Green
        "Physician" to Color.parseColor("#FF9800"),       // Orange
        "Gynaecologist" to Color.parseColor("#009688"),   // Teal
        "Oncologist" to Color.parseColor("#795548"),      // Brown
        "Orthopaedic Surgeon" to Color.parseColor("#3F51B5") // Indigo
    )
    private var currentPage = 0
    private val itemsPerPage = 10
    private var allGenericUsageList: List<GenericUsage> = emptyList()

    private var currentBrandPage = 0
    private val brandItemsPerPage = 10
    private var allBrandUsageList: List<BrandUsage> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmaAnalyticsBinding.inflate(LayoutInflater.from(this))
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

        opdPrescriptionViewModel.totalPatientCount.observe(this) { count ->
            binding.totalPatientCount.text = count.toString()
        }

        opdPrescriptionViewModel.totalFormCount.observe(this) { count ->
            binding.totalFormCount.text = count.toString()
        }

        opdPrescriptionViewModel.malePatientCount.observe(this) {
            binding.malePatientCount.text = it.toString()
        }

        opdPrescriptionViewModel.femalePatientCount.observe(this) {
            binding.femalePatientCount.text = it.toString()
        }

        opdPrescriptionViewModel.otherPatientCount.observe(this) {
            binding.otherPatientCount.text = it.toString()
        }

        opdPrescriptionViewModel.specialityCountList.observe(this) { list ->
            setupPieChart(list)
        }

        opdPrescriptionViewModel.fetchSpecialityCountData()


        opdPrescriptionViewModel.ageGroupCounts.observe(this) { list ->
            showAgeGroupPieChart(list)
        }
        opdPrescriptionViewModel.fetchAgeGroupCounts()

        opdPrescriptionViewModel.genericUsageList.observe(this) { list ->
            allGenericUsageList = list.sortedByDescending { it.totalQuantity } // sort by quantity
            currentPage = 0
            showGenericUsagePage(currentPage)
        }

        opdPrescriptionViewModel.fetchGenericUsageData()

        binding.nextButton.setOnClickListener {
            currentPage++
            showGenericUsagePage(currentPage)
        }

        binding.backButton.setOnClickListener {
            if (currentPage > 0) {
                currentPage--
                showGenericUsagePage(currentPage)
            }
        }

        opdPrescriptionViewModel.brandUsageList.observe(this) { list ->
            allBrandUsageList = list.sortedByDescending { it.totalQuantity } // sort by quantity
            currentBrandPage = 0
            showBrandUsagePage(currentBrandPage)
        }

        opdPrescriptionViewModel.fetchBrandUsageData()

        binding.nextBrandButton.setOnClickListener {
            currentBrandPage++
            showBrandUsagePage(currentBrandPage)
        }

        binding.backBrandButton.setOnClickListener {
            if (currentBrandPage > 0) {
                currentBrandPage--
                showBrandUsagePage(currentBrandPage)
            }
        }

    }

    private fun showBrandUsagePage(pageIndex: Int) {
        val fromIndex = pageIndex * brandItemsPerPage
        val toIndex = minOf(fromIndex + brandItemsPerPage, allBrandUsageList.size)

        if (fromIndex >= allBrandUsageList.size) return

        val currentList = allBrandUsageList.subList(fromIndex, toIndex)
        showBrandUsageBarChart(currentList)

        // Update button visibility
        binding.backBrandButton.visibility = if (pageIndex > 0) View.VISIBLE else View.GONE
        binding.nextBrandButton.visibility = if (toIndex < allBrandUsageList.size) View.VISIBLE else View.GONE
    }

    private fun showBrandUsageBarChart(list: List<BrandUsage>) {
        val colors = generateColors(list.size)

        val barEntries = list.mapIndexed { index, item ->
            com.github.mikephil.charting.data.BarEntry(index.toFloat(), item.totalQuantity.toFloat())
        }

        val barDataSet = com.github.mikephil.charting.data.BarDataSet(barEntries, "")
        barDataSet.colors = colors
        barDataSet.valueTextSize = 12f
        barDataSet.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        val barData = com.github.mikephil.charting.data.BarData(barDataSet)
        binding.brandUsageBarChart.data = barData

        val xAxis = binding.brandUsageBarChart.xAxis
        xAxis.setDrawLabels(false)
        xAxis.setDrawGridLines(false)
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM

        binding.brandUsageBarChart.axisLeft.axisMinimum = 0f
        binding.brandUsageBarChart.axisRight.isEnabled = false
        binding.brandUsageBarChart.description.isEnabled = false
        binding.brandUsageBarChart.legend.isEnabled = false

        binding.brandUsageBarChart.animateY(1000)
        binding.brandUsageBarChart.invalidate()

        populateBrandUsageTable(list, colors)
    }

    private fun populateBrandUsageTable(list: List<BrandUsage>, colors: List<Int>) {
        binding.BrandUsageTableContainer.removeAllViews()

        val outerBorderColor = Color.parseColor("#B0B0B0") // table border
        val dividerColor = Color.parseColor("#444444") // darker divider lines

        val rowLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        list.forEachIndexed { index, item ->
            // Each row container (with gray outer border)
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(outerBorderColor)
                layoutParams = rowLayoutParams
                gravity = Gravity.CENTER_VERTICAL
            }

            val innerRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
            }

            // Color indicator box
            val colorBox = View(this).apply {
                setBackgroundColor(colors[index])
                layoutParams = LinearLayout.LayoutParams(0, 40, 0.1f).apply {
                    rightMargin = 8
                    gravity = Gravity.CENTER
                }
            }

            // Divider after color
            val divider1 = View(this).apply {
                setBackgroundColor(dividerColor)
                layoutParams = LinearLayout.LayoutParams(3, LinearLayout.LayoutParams.MATCH_PARENT)
            }

            // Brand name
            val brandNameView = TextView(this).apply {
                text = item.brandName
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
                gravity = Gravity.CENTER
                textSize = 14f
                setTextColor(Color.BLACK)
            }

            val divider2 = View(this).apply {
                setBackgroundColor(dividerColor)
                layoutParams = LinearLayout.LayoutParams(3, LinearLayout.LayoutParams.MATCH_PARENT)
            }

            // Generic name
            val genericNameView = TextView(this).apply {
                text = item.genericName
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
                gravity = Gravity.CENTER
                textSize = 14f
                setTextColor(Color.BLACK)
            }

            val divider3 = View(this).apply {
                setBackgroundColor(dividerColor)
                layoutParams = LinearLayout.LayoutParams(3, LinearLayout.LayoutParams.MATCH_PARENT)
            }

            // Patient count
            val patientCountView = TextView(this).apply {
                text = item.patientCount.toString()
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.CENTER
                textSize = 14f
                setTextColor(Color.BLACK)
            }

            val divider4 = View(this).apply {
                setBackgroundColor(dividerColor)
                layoutParams = LinearLayout.LayoutParams(3, LinearLayout.LayoutParams.MATCH_PARENT)
            }

            // Quantity
            val qtyView = TextView(this).apply {
                text = "${item.totalQuantity} ${item.qtyName}"
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.CENTER
                textSize = 14f
                setTextColor(Color.BLACK)
            }

            // Add all views to inner row
            innerRow.addView(colorBox)
            innerRow.addView(divider1)
            innerRow.addView(brandNameView)
            innerRow.addView(divider2)
            innerRow.addView(genericNameView)
            innerRow.addView(divider3)
            innerRow.addView(patientCountView)
            innerRow.addView(divider4)
            innerRow.addView(qtyView)

            // Add inner row to bordered container
            row.addView(innerRow)
            binding.BrandUsageTableContainer.addView(row)

            // Add horizontal divider after each row
            val horizontalDivider = View(this).apply {
                setBackgroundColor(dividerColor)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3 // thickness
                ).apply {
                    topMargin = 6
                    bottomMargin = 6
                }
            }
            binding.BrandUsageTableContainer.addView(horizontalDivider)
        }
    }

    private fun showGenericUsagePage(pageIndex: Int) {
        val fromIndex = pageIndex * itemsPerPage
        val toIndex = minOf(fromIndex + itemsPerPage, allGenericUsageList.size)

        if (fromIndex >= allGenericUsageList.size) return

        val currentList = allGenericUsageList.subList(fromIndex, toIndex)
        showGenericUsageBarChart(currentList)

        // Update button visibility
        binding.backButton.visibility = if (pageIndex > 0) View.VISIBLE else View.GONE
        binding.nextButton.visibility = if (toIndex < allGenericUsageList.size) View.VISIBLE else View.GONE
    }

    private fun showGenericUsageBarChart(list: List<GenericUsage>) {
        // Generate unique random colors for each bar
        val colors = generateColors(list.size)

        // Create bar entries
        val barEntries = list.mapIndexed { index, item ->
            com.github.mikephil.charting.data.BarEntry(index.toFloat(), item.totalQuantity.toFloat())
        }

        val barDataSet = com.github.mikephil.charting.data.BarDataSet(barEntries, "")
        barDataSet.colors = colors
        barDataSet.valueTextSize = 12f
        barDataSet.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString() // show integer values only
            }
        }

        val barData = com.github.mikephil.charting.data.BarData(barDataSet)
        binding.genericUsageBarChart.data = barData

        // Hide x-axis labels (no generic names below bars)
        val xAxis = binding.genericUsageBarChart.xAxis
        xAxis.setDrawLabels(false)
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM

        // Customize chart appearance
        binding.genericUsageBarChart.axisLeft.axisMinimum = 0f
        binding.genericUsageBarChart.axisRight.isEnabled = false
        binding.genericUsageBarChart.description.isEnabled = false
        binding.genericUsageBarChart.legend.isEnabled = false

        binding.genericUsageBarChart.animateY(1000)
        binding.genericUsageBarChart.invalidate()

        // Also pass colors to table generation
        populateGenericUsageTable(list, colors)
    }

    private fun populateGenericUsageTable(list: List<GenericUsage>, colors: List<Int>) {
        binding.genericUsageTableContainer.removeAllViews()

        val outerBorderColor = Color.parseColor("#B0B0B0") // table border
        val dividerColor = Color.parseColor("#444444") // darker divider lines

        val rowLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        list.forEachIndexed { index, item ->
            // Each row container (with gray border background)
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(outerBorderColor)
                layoutParams = rowLayoutParams
                gravity = Gravity.CENTER_VERTICAL
            }

            val innerRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
            }

            // Color indicator box
            val colorBox = View(this).apply {
                setBackgroundColor(colors[index])
                layoutParams = LinearLayout.LayoutParams(0, 40, 0.1f).apply {
                    rightMargin = 8
                    gravity = Gravity.CENTER
                }
            }

            // Vertical divider after color
            val divider1 = View(this).apply {
                setBackgroundColor(dividerColor)
                layoutParams = LinearLayout.LayoutParams(3, LinearLayout.LayoutParams.MATCH_PARENT)
            }

            // Generic name
            val nameView = TextView(this).apply {
                text = item.genericName
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
                gravity = Gravity.CENTER
                textSize = 14f
                setTextColor(Color.BLACK)
            }

            val divider2 = View(this).apply {
                setBackgroundColor(dividerColor)
                layoutParams = LinearLayout.LayoutParams(3, LinearLayout.LayoutParams.MATCH_PARENT)
            }

            // Patient count
            val patientView = TextView(this).apply {
                text = item.patientCount.toString()
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.CENTER
                textSize = 14f
                setTextColor(Color.BLACK)
            }

            val divider3 = View(this).apply {
                setBackgroundColor(dividerColor)
                layoutParams = LinearLayout.LayoutParams(3, LinearLayout.LayoutParams.MATCH_PARENT)
            }

            // Quantity
            val qtyView = TextView(this).apply {
                text = "${item.totalQuantity} ${item.qtyName}" // <-- Combine quantity and unit name
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.CENTER
                textSize = 14f
                setTextColor(Color.BLACK)
            }


            // Add content row
            innerRow.addView(colorBox)
            innerRow.addView(divider1)
            innerRow.addView(nameView)
            innerRow.addView(divider2)
            innerRow.addView(patientView)
            innerRow.addView(divider3)
            innerRow.addView(qtyView)

            // Add main row to container
            row.addView(innerRow)
            binding.genericUsageTableContainer.addView(row)

            // 👇 Add a thicker and darker horizontal line after each row
            val horizontalDivider = View(this).apply {
                setBackgroundColor(dividerColor)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3 // increased from 1dp → 3dp for better visibility
                ).apply {
                    topMargin = 6
                    bottomMargin = 6
                }
            }
            binding.genericUsageTableContainer.addView(horizontalDivider)
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

    private fun generateColors(size: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val hueStep = 360f / size

        for (i in 0 until size) {
            val hsv = floatArrayOf((i * hueStep) % 360f, 0.75f, 0.9f)
            colors.add(Color.HSVToColor(hsv))
        }

        return colors
    }


    private fun setupPieChart(specialityListFromDB: List<SpecialityCount>) {
        // Ensure all specialities are included, even with 0 count
        val fullSpecialityList = specialityColors.keys.map { speciality ->
            specialityListFromDB.find { it.doctor_specialty == speciality }
                ?: SpecialityCount(speciality, 0)
        }

        val total = fullSpecialityList.sumOf { it.count }
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        fullSpecialityList.forEach { item ->
            entries.add(PieEntry(item.count.toFloat())) // No label here
            colors.add(specialityColors[item.doctor_specialty] ?: Color.LTGRAY)
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.sliceSpace = 2f
        dataSet.valueTextSize = 0f          // Hide values on slices
        dataSet.valueTextColor = Color.TRANSPARENT

        val pieData = PieData(dataSet)

        binding.specialityPieChart.apply {
            data = pieData
            description.isEnabled = false
            legend.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 45f
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            animateY(1200)
            invalidate()
        }

        updateLegend(fullSpecialityList, total)
    }

    private fun updateLegend(specialityList: List<SpecialityCount>, total: Int) {
        val container = binding.specialityLegendContainer
        container.removeAllViews()

        specialityList.forEach { item ->
            val color = specialityColors[item.doctor_specialty] ?: Color.LTGRAY
            val percentage = if (total == 0) 0 else (item.count.toFloat() / total * 100).toInt()

            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 8, 0, 8)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            val colorBox = View(this).apply {
                setBackgroundColor(color)
                layoutParams = LinearLayout.LayoutParams(40, 40).apply {
                    rightMargin = 16
                }
            }

            val textView = TextView(this).apply {
                text = "${item.doctor_specialty}: ${item.count} (${percentage}%)"
                textSize = 14f
                setTextColor(Color.DKGRAY)
            }

            row.addView(colorBox)
            row.addView(textView)
            container.addView(row)
        }
    }
}