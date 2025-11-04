package org.impactindiafoundation.iifllemeddocket.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientFormMap
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class OrthosisPatientFormService : LifecycleService() {

    private val TAG = "OrthosisPatientFormService"
    private val serviceJob = Job()
    private val sharedDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    private val serviceScope = CoroutineScope(sharedDispatcher + serviceJob)

    @Inject
    lateinit var repository: NewMainRepository

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
                val localOrthosisForm = repository.getOrthosisPatientForm()
                val unsyncedOrthosisForm = localOrthosisForm.filter { it.isSynced == 0 }
                val unsyncedCount = unsyncedOrthosisForm.size
                val patientFormMap = PatientFormMap(unsyncedOrthosisForm)

                if (unsyncedCount > 0) {
                    val gson = Gson()
                    val orthosisFormJson = gson.toJson(patientFormMap)
                    Log.d(TAG, "Sync payload: $orthosisFormJson")
                    Log.d(TAG, "Total unsynced forms to sync: $unsyncedCount")

                    syncDataToServer(patientFormMap, unsyncedCount)
                } else {
                    Log.d(TAG, "No unsynced forms found, stopping service.")
                    stopSelf()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching local forms", e)
                stopSelf()
            }
        }
    }

    private fun syncDataToServer(unsyncedData: PatientFormMap, totalCount: Int) {
        serviceScope.launch {
            try {
                delay(500) // small delay for thread sync
                val response = repository.syncOrthosisPatientForNew(unsyncedData)
                Log.d(TAG, "Server Response: ${response.message()}")

                if (response.isSuccessful) {
                    val successIds = response.body()?.successSyncId ?: emptyList()
                    Log.d(TAG, "Sync success: ${successIds.size} items out of $totalCount")

                    updateLocalDb(successIds, totalCount)
                } else {
                    val error = response.errorBody()?.string() ?: "Unexpected Network Error"
                    Log.e(TAG, "Sync failed: $error")

                    // All failed
                    repository.insertSyncData(
                        synType = "Forms",
                        syncItemCount = 0,
                        notSyncItemCount = totalCount
                    )
                    stopSelf()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Sync Exception: ${e.message}", e)
                repository.insertSyncData(
                    synType = "Forms",
                    syncItemCount = 0,
                    notSyncItemCount = totalCount
                )
                stopSelf()
            }
        }
    }

    private fun updateLocalDb(successIdList: List<Int>, totalCount: Int) {
        serviceScope.launch {
            try {
                Log.d(TAG, "Updating local DB with synced IDs: $successIdList")
                repository.updateSyncedForms(successIdList)

                val successCount = successIdList.size
                val failedCount = (totalCount - successCount).coerceAtLeast(0)

                Log.d(TAG, "Sync Summary → Total: $totalCount, Success: $successCount, Failed: $failedCount")

                // ✅ Record sync summary in local DB
                repository.insertSyncData(
                    synType = "Forms",
                    syncItemCount = successCount,
                    notSyncItemCount = failedCount
                )

                Log.d(TAG, "Local DB updated & sync summary inserted.")

                // ✅ Notify UI
                val intent = Intent(ACTION_TASK_COMPLETED)
                LocalBroadcastManager.getInstance(this@OrthosisPatientFormService).sendBroadcast(intent)

                Log.d(TAG, "Broadcast sent successfully.")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating local DB: ${e.message}", e)
            } finally {
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        sharedDispatcher.close()
        Log.d(TAG, "Service destroyed.")
        stopSelf()
    }

    companion object {
        const val ACTION_PROGRESS_UPDATE = "PROGRESS_UPDATE"
        const val ACTION_TASK_COMPLETED = "TASK_COMPLETION"
        const val ACTION_SERVICE_RUNNING = "SERVICE_RUNNING"
    }
}
