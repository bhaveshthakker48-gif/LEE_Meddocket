package org.impactindiafoundation.iifllemeddocket.Utils

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Configuration
import android.widget.EditText
import es.dmoral.toasty.Toasty
import org.impactindiafoundation.iifllemeddocket.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Utility {
    val HIDE_PREVIOUS_DATES = 1
    interface DateListener{
        fun onDateSelected(date:String)
    }

    interface ExaminationDateListener{
        fun onDateSelected(date:String)
    }

    private fun dateInSimpleFormatToSend1(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
    fun getLocaleForCalendarInstance(context: Context): Locale {
        val locale = Locale("en")
        Locale.setDefault(locale)
        val config: Configuration = context.resources.configuration
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
        return locale
    }

    fun openDatePicker(
        context: Context,
        hideFlag: Int,
        et: EditText,
        dateEvent: DateListener
    ) {
        val calendar = Calendar.getInstance(getLocaleForCalendarInstance(context))
        val year18YearsAgo = calendar.get(Calendar.YEAR)
        val month18YearsAgo = calendar.get(Calendar.MONTH)
        val day18YearsAgo = calendar.get(Calendar.DAY_OF_MONTH)
        val date18YearsAgoInMillis = calendar.timeInMillis
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.BlueDatePickerDialogTheme,
            { _, year, month, day ->
                val formattedDate = dateInSimpleFormatToSend1(year, month, day)
                et.setText(formattedDate)
                dateEvent.onDateSelected(formattedDate)
            },
            year18YearsAgo,
            month18YearsAgo,
            day18YearsAgo
        )

        datePickerDialog.datePicker.maxDate = date18YearsAgoInMillis

        if (hideFlag == HIDE_PREVIOUS_DATES) {
            calendar.set(Calendar.YEAR, 1900) // Example: Set the minimum date to January 1, 1900
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
        }

        datePickerDialog.show()
    }

    fun parseDateToCalendar(dateString: String): Calendar {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.ENGLISH)
        val date = dateFormat.parse(dateString)

        // Initialize a Calendar instance and set the parsed date
        return Calendar.getInstance().apply {
            time = date!!
        }
    }
    fun errorToast(applicationContext: Context, message: String) {
        Toasty.error(applicationContext, message, Toasty.LENGTH_SHORT).show()
    }

    fun successToast(applicationContext: Context, message: String) {
        Toasty.success(applicationContext, message, Toasty.LENGTH_SHORT).show()
    }

    fun infoToast(applicationContext: Context, message: String) {
        Toasty.info(applicationContext, message, Toasty.LENGTH_SHORT).show()
    }

    fun warningToast(applicationContext: Context, message: String) {
        Toasty.warning(applicationContext, message, Toasty.LENGTH_SHORT).show()
    }
}