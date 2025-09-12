package org.impactindiafoundation.iifllemeddocket.Dialog


import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import org.impactindiafoundation.iifllemeddocket.R


class MyProgressDialog(context: Context?) : ProgressDialog(context) {
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_progress_dialog) // You need to create a custom layout for the progress dialog
        setMessage("Waiting for data") // Set the message here
        setCancelable(false) // Prevent the user from dismissing the dialog
    }
}

