package org.impactindiafoundation.iifllemeddocket.services

import NewVitalsRequest
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ACTION_FORM_SYNC_COMPLETED
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.VITALS_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class VitalsFormService : LifecycleService() {

    private val TAG = "VitalsFormService"

    private val serviceJob = Job()
    private val sharedDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    private val serviceScope = CoroutineScope(sharedDispatcher + serviceJob)

    @Inject
    lateinit var repository: NewMainRepository

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        intent?.let {
            val dataMap = it.getSerializableExtra("QUERY_PARAMS") as? HashMap<String, Any>
            dataMap?.let { queryParams -> getDataFromLocal(queryParams) }
        }
        return START_NOT_STICKY
    }

    private fun getDataFromLocal(data: HashMap<String, Any>) {
        serviceScope.launch {
            try {
                val vitalsFormList = repository.getVitalsForm()
                val unsyncedList = vitalsFormList.filter { it.isSyn == 0 }
                val totalUnsyncedBefore = unsyncedList.size

                Log.d(TAG, "Unsynced before sync: $totalUnsyncedBefore")

                if (unsyncedList.isNotEmpty()) {
                    val request = NewVitalsRequest(unsyncedList)
                    syncDataToServer(request, totalUnsyncedBefore)
                } else {
                    Log.d(TAG, "No unsynced data found — sending broadcast to continue queue.")
                    val intent = Intent(ACTION_FORM_SYNC_COMPLETED).apply {
                        putExtra("formType", "Vitals")
                        putExtra("syncedCount", 0)
                        putExtra("unsyncedCount", 0)
                    }
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                    stopSelf()
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error fetching data: ${e.message}", e)
            }
        }
    }

    private fun syncDataToServer(request: NewVitalsRequest, totalUnsyncedBefore: Int) {
        serviceScope.launch {
            try {
                val response = repository.syncNewVitalsForm(request)

                if (response.isSuccessful) {
                    Log.d(TAG, "Sync successful")
                    val syncedIds = request.vitalList.map { it._id }
                    updateLocalDb(syncedIds, totalUnsyncedBefore)
                } else {
                    Log.e(TAG, "Sync failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Sync error: ${e.message}", e)
            }
        }
    }

    private fun updateLocalDb(successIdList: List<Int>, totalUnsyncedBefore: Int) {
        serviceScope.launch {
            try {
                repository.updateVitalsForms(successIdList)
                repository.updatePatientForms(successIdList, VITALS_FORM)

                // After updating DB, check remaining unsynced
                val allVitals = repository.getVitalsForm()
                val remainingUnsynced = allVitals.count { it.isSyn == 0 }

                val syncedCount = totalUnsyncedBefore - remainingUnsynced
                val unsyncedCount = remainingUnsynced

                Log.d(TAG, "✅ Synced: $syncedCount | ❌ Unsynced: $unsyncedCount")

                // Broadcast the counts to activity
                val intent = Intent(ACTION_FORM_SYNC_COMPLETED).apply {
                    putExtra("formType", "Vitals")          // or Vitals / Refractive / VisualAcuity
                    putExtra("syncedCount", syncedCount)
                    putExtra("unsyncedCount", unsyncedCount)
                }
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            } catch (e: Exception) {
                Log.e(TAG, "DB update error: ${e.message}", e)
            } finally {
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        sharedDispatcher.close()
        Log.d(TAG, "Service destroyed")
    }

}
