package org.impactindiafoundation.iifllemeddocket.Utils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import org.impactindiafoundation.iifllemeddocket.R

class DoctorProgress(private val activity: AppCompatActivity) {
    private var count: Int = 0
    private var dialog: Dialog? = null
    private val dismissHandler = Handler(Looper.getMainLooper())
    private val dismissAction = Runnable {
        dismiss()
    }

    @Synchronized
    fun show() {
        if (activity.isDestroyed) {
            return
        }
        if (count == 0) {
            dialog = Dialog(activity)
            dialog?.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCancelable(false)
                setContentView(R.layout.doctor_progress)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }
            dismissHandler.removeCallbacks(dismissAction) // Remove any previously posted dismiss action
            dismissHandler.postDelayed(dismissAction, 5000) // Post a delayed action to dismiss after 15 seconds
        }
        count++
    }

    @Synchronized
    fun dismiss() {
        if (count > 0) {
            count--
            if (count == 0) {
                dialog?.let {
                    if (it.isShowing) {
                        it.dismiss()
                    }
                }
                dismissHandler.removeCallbacks(dismissAction) // Remove the dismiss action if the dialog is dismissed manually
            }
        }
    }

    @Synchronized
    fun reset() {
        count = 0
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        dismissHandler.removeCallbacks(dismissAction) // Remove the dismiss action if the dialog is reset
    }

    @Synchronized
    fun isShowing() = count > 0
}
