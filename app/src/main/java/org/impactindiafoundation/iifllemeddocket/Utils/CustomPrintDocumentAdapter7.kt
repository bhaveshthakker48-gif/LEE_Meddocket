package org.impactindiafoundation.iifllemeddocket.Utils

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.R
import java.io.FileOutputStream

class CustomPrintDocumentAdapter7(
    private val context: Context,
    private val recyclerView: RecyclerView,
    val camp: String,
    val patientName: String,
    val genderAge: String
) : PrintDocumentAdapter() {

    private var totalPages = 0

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
    ) {
        val printDocumentInfo = PrintDocumentInfo.Builder("print_output.pdf")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
            .build()

        callback?.onLayoutFinished(printDocumentInfo, true)
    }


    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        val adapter = recyclerView.adapter ?: return
        val inflater = LayoutInflater.from(context)
        val document = PdfDocument()

        val pageWidth = 595   // A4 width in points
        val pageHeight = 842  // A4 height in points

        var itemIndex = 0
        val totalItems = adapter.itemCount
        var pageNumber = 1

        while (itemIndex < totalItems) {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            // --- HEADER ---
            val headerView = inflater.inflate(R.layout.header_layout, null)
            headerView.findViewById<TextView>(R.id.TextView_CampName).text = camp
            headerView.findViewById<TextView>(R.id.TextView_PatientName).text = patientName
            headerView.findViewById<TextView>(R.id.TextView_PatientID).text = genderAge
            headerView.measure(
                View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            headerView.layout(0, 0, pageWidth, headerView.measuredHeight)
            headerView.draw(canvas)
            var yPos = headerView.measuredHeight

            // --- FOOTER (measure to calculate space) ---
            val footerView = inflater.inflate(R.layout.footer_layout, null)
            footerView.findViewById<TextView>(R.id.textView_date).text = ConstantsApp.getCurrentDate()
            footerView.measure(
                View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val footerHeight = footerView.measuredHeight
            val availableHeight = pageHeight - headerView.measuredHeight - footerHeight

            var usedHeight = 0

            // --- ITEMS ---
            while (itemIndex < totalItems) {
                val vh = adapter.createViewHolder(recyclerView, adapter.getItemViewType(itemIndex))
                adapter.onBindViewHolder(vh, itemIndex)
                val itemView = vh.itemView

                itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                val itemHeight = itemView.measuredHeight

                // Check if this item fits
                if (usedHeight + itemHeight > availableHeight) {
                    break // doesn’t fit → go to next page
                }

                // Draw item
                itemView.layout(0, 0, pageWidth, itemHeight)
                canvas.save()
                canvas.translate(0f, yPos.toFloat())
                itemView.draw(canvas)
                canvas.restore()

                yPos += itemHeight
                usedHeight += itemHeight
                itemIndex++
            }

            // --- FOOTER ---
            val footerY = pageHeight - footerHeight
            footerView.layout(0, 0, pageWidth, footerHeight)
            canvas.save()
            canvas.translate(0f, footerY.toFloat())
            footerView.draw(canvas)
            canvas.restore()

            document.finishPage(page)
            pageNumber++
        }

        // Save document
        document.writeTo(FileOutputStream(destination?.fileDescriptor))
        document.close()

        callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
    }
}
