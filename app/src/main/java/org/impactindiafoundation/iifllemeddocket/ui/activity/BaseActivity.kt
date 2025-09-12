package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import org.impactindiafoundation.iifllemeddocket.Utils.DoctorProgress
import org.impactindiafoundation.iifllemeddocket.Utils.Progress
import java.util.Locale

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    lateinit var locale: Locale
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val progress: Progress by lazy {
        Progress(this)
    }

    val doctorProgress: DoctorProgress by lazy {
        DoctorProgress(this)
    }

    val impactProgress: DoctorProgress by lazy {
        DoctorProgress(this)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}