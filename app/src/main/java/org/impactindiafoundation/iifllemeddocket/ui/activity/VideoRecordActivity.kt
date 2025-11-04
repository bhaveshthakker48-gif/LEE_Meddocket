package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.util.Util
import es.dmoral.toasty.Toasty
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.VIDEO_URL
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityVideoRecordBinding
import java.io.File
import java.util.concurrent.Executors

class VideoRecordActivity : AppCompatActivity() {

    private lateinit var binding:ActivityVideoRecordBinding

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private val executor = Executors.newSingleThreadExecutor()
    private var isRecording = false

    private var videoUrl = ""
    private val permissionRequestCode = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //enableEdgeToEdge()

        binding = ActivityVideoRecordBinding.inflate(layoutInflater)
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
        initObserver()
    }

    private fun initUi(){
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        binding.btnStartRecord.setOnClickListener {

            if (allPermissionsGranted()) {
                startRecording()
            } else {
                requestPermissions()
            }
        }
        binding.btnStopRecord.setOnClickListener { stopRecording() }

        binding.btnDoneRecording.setOnClickListener {
            val data = "Message from SecondActivity"

            // Create an Intent to hold the result data
            val resultIntent = Intent()
            resultIntent.putExtra(VIDEO_URL, videoUrl)

            // Set the result to OK and attach the intent
            setResult(Activity.RESULT_OK, resultIntent)
            onBackPressed()
        }
    }

    private fun initObserver(){

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(findViewById<PreviewView>(R.id.cameraPv).surfaceProvider)
            }

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HD))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, videoCapture)
            } catch (e: Exception) {
                Log.e("VideoRecording", "Failed to bind camera use cases", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun startRecording() {
        val videoFile = File(filesDir, "recorded_video.mp4")
        val outputOptions = FileOutputOptions.Builder(videoFile).build()
        recording = videoCapture?.output
            ?.prepareRecording(this, outputOptions)
            ?.apply { withAudioEnabled() }
            ?.start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        binding.btnStartRecord.visibility = Button.GONE
                        binding.btnDoneRecording.visibility = Button.GONE
                        binding.btnStopRecord.visibility = Button.VISIBLE
                        startProgressBar()
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (recordEvent.error == VideoRecordEvent.Finalize.ERROR_NONE) {
                            Log.i("VideoRecording", "Video saved at: ${videoFile.absolutePath}")
                            // Save to local database here
                            videoUrl = videoFile.absolutePath
                        }
                    }
                }
            }
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
        binding.btnStartRecord.visibility = Button.VISIBLE
        binding.btnDoneRecording.visibility = Button.VISIBLE
        binding.btnStopRecord.visibility = Button.GONE
        binding.pbVideo.visibility = View.GONE
        binding.pbVideo.progress = 0
    }

    private fun startProgressBar() {
        // Assume max duration of 30 seconds for demonstration purposes
        val maxDuration = 30
        binding.pbVideo.visibility = View.VISIBLE
        binding.pbVideo.max = maxDuration
        binding.pbVideo.progress = 0

        // Example of updating the progress bar
        executor.execute {
            for (i in 1..maxDuration) {
                Thread.sleep(1000)
                binding.pbVideo.progress = i
            }
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
            permissionRequestCode
        )
    }

    // Check if all permissions are granted
    private fun allPermissionsGranted() = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    ).all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun deleteVideoFromAppStorage(filePath: String): Boolean {
        val tempPath = "/data/data/org.impactindiafoundation.iifllemeddocket/files/recorded_video.mp4"
        val file = File(filePath)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            val permissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }

            if (permissionsGranted) {
                // All permissions are granted
                // You can start the camera, audio recording, or other actions here
                startCamera()
            } else {
                // Some permissions are denied
                Utility.warningToast(this@VideoRecordActivity,"Need Permission")
                requestPermissions()
            }
        }
    }
}