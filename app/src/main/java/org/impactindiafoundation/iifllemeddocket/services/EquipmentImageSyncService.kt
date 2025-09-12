package org.impactindiafoundation.iifllemeddocket.services

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImage
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class EquipmentImageSyncService : LifecycleService() {

    private val ERR_TAG = "EquipmentImageSyncService"
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
                val localEquiomentImages = repository.getEquipmentImage()
                val unsyncedEquipmentImages = localEquiomentImages.filter { it.isSynced == 0 }
                if (!unsyncedEquipmentImages.isNullOrEmpty()) {
                    for(i in unsyncedEquipmentImages){
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

    private fun syncDataToServer(equipmentImage: EquipmentImage) {
        serviceScope.launch {
            try {
                val dataMap = HashMap<String, String>()
                val uriMap = HashMap<String, Uri>()
                val fileMap = HashMap<String, File>()
                File(equipmentImage.images)?.let { fileMap["images"] = it }
                dataMap["file_type"] = "image"
                dataMap["temp_patient_id"] = equipmentImage.temp_patient_id
                dataMap["camp_id"] = equipmentImage.camp_id
                dataMap["patient_id"] = ""
                dataMap["id"] = equipmentImage.id.toString()
                dataMap["patient_image_type"] = "equipment"
                val response = repository.uploadEquipmentImageFile(
                    applicationContext,
                    uriMap,
                    fileMap,
                    dataMap,
                    "",
                    type = "EquipmentImage"
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