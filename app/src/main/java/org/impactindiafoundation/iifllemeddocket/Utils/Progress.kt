package org.impactindiafoundation.iifllemeddocket.Utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import org.impactindiafoundation.iifllemeddocket.R

class Progress(private val activity: AppCompatActivity) {
    private var count: Int = 0
    private var dialog: Dialog? = null
    private val dismissHandler = Handler(Looper.getMainLooper())
    private val dismissAction = Runnable { safeDismiss() }

    @Synchronized
    fun show() {
        if (activity.isFinishing || activity.isDestroyed) return

        if (count == 0) {
            dialog = Dialog(activity).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCancelable(false)
                setContentView(R.layout.progress)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                if (!(activity.isFinishing || activity.isDestroyed)) {
                    show()
                }
            }
            // Cancel any previous delayed dismiss
            dismissHandler.removeCallbacks(dismissAction)
            // Auto-dismiss safeguard (after 15 seconds)
            dismissHandler.postDelayed(dismissAction, 15000)
        }
        count++
    }

    @Synchronized
    fun dismiss() {
        if (count > 0) count--
        if (count == 0) safeDismiss()
    }

    @Synchronized
    private fun safeDismiss() {
        try {
            dismissHandler.removeCallbacks(dismissAction)
            val dialog = dialog ?: return
            val ctx = dialog.context
            if (ctx is Activity && (ctx.isFinishing || ctx.isDestroyed)) return

            if (dialog.isShowing) {
                dialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun reset() {
        count = 0
        safeDismiss()
    }

    @Synchronized
    fun isShowing() = dialog?.isShowing == true
}
