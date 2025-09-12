package org.impactindiafoundation.iifllemeddocket.Utils

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.R


class CustomLayoutManager(val context: Context) : LinearLayoutManager(context) {
    var itemsPerPage: Int = 0 // Number of items per page

    fun measureAndLayoutHeader(canvas: Canvas, pageWidth: Int): Int {
        // Inflate header view
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val headerView = inflater.inflate(R.layout.header_layout, null)

        val textviewCamp = headerView.findViewById<TextView>(R.id.TextView_CampName)
        val textviewPatientName = headerView.findViewById<TextView>(R.id.TextView_PatientName)
        val textviewGenderAge = headerView.findViewById<TextView>(R.id.TextView_PatientID)

        headerView.measure(
            View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        // Layout header view
        headerView.layout(0, 0, pageWidth, headerView.measuredHeight)

        // Draw header onto canvas
        headerView.draw(canvas)

        // Return the measured height of the header
        return headerView.measuredHeight
    }
}

