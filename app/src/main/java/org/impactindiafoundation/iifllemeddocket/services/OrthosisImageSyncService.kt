package org.impactindiafoundation.iifllemeddocket.services

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientFormMap
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class OrthosisImageSyncService : LifecycleService() {

    private val ERR_TAG = "FormImageSyncService"
    private val serviceJob = Job()
    private val sharedDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher() // Limit to 2 concurrent tasks
    private val serviceScope = CoroutineScope(sharedDispatcher + serviceJob)

    @Inject
    lateinit var repository: NewMainRepository

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val dataMap = it.getSerializableExtra("QUERY_PARAMS") as? HashMap<String, Any>
            dataMap?.let { queryParams ->
                getDataFromLocal(queryParams)
            }
        }
        return START_NOT_STICKY
    }

    private fun getDataFromLocal(data: HashMap<String, Any>) {
        serviceScope.launch {
            try {
                var orthosisFormJson = ""
                val localOrthosisForm = repository.getFormOrthosisImages()
                val unsyncedOrthosisImages = localOrthosisForm.filter { it.isSynced == 0 }
                if (!unsyncedOrthosisImages.isNullOrEmpty()) {
                    for(i in unsyncedOrthosisImages){
                        syncDataToServer(i)
                    }
                }
                else{
                    stopSelf()
                }
            } catch (e: Exception) {
                Log.e(ERR_TAG, "Error fetching phone book matches", e)
            }
        }
    }

    private fun syncDataToServer(orthosisImage:OrthosisImages) {
        serviceScope.launch {
            try {
                val dataMap = HashMap<String, String>()
                val uriMap = HashMap<String, Uri>()
                val fileMap = HashMap<String, File>()
                File(orthosisImage.images)?.let { fileMap["images"] = it }
                dataMap["file_type"] = "image"
                dataMap["temp_patient_id"] = orthosisImage.temp_patient_id
                dataMap["camp_id"] = orthosisImage.camp_id
                dataMap["orthosis_id"] = orthosisImage.orthosis_id
                dataMap["amputation_side"] = orthosisImage.amputation_side
                dataMap["patient_id"] = ""
                dataMap["id"] = orthosisImage.id.toString()

                val response = repository.uploadOrthoImageFile(
                    applicationContext,
                    uriMap,
                    fileMap,
                    dataMap,
                    "",
                    type = "OrthosisImage"
                )
                Log.d("RESPONSE", response.toString())
                stopSelf()
            } catch (e: Exception) {
                Log.d(ERR_TAG,e.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        stopSelf()
    }

}