package org.impactindiafoundation.iifllemeddocket.services

import OpdFormRequest
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ACTION_FORM_SYNC_COMPLETED
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.OPD_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class OpdFormService : LifecycleService() {

    private val TAG = "OpdFormService"
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
            Log.d(TAG, "Received QUERY_PARAMS: $dataMap")
            dataMap?.let { queryParams -> getDataFromLocal(queryParams) }
                ?: Log.d(TAG, "No QUERY_PARAMS found")
        } ?: Log.d(TAG, "Intent is null")
        return START_NOT_STICKY
    }

    private fun getDataFromLocal(data: HashMap<String, Any>) {
        Log.d(TAG, "Fetching local OPD forms...")
        serviceScope.launch {
            try {
                val opdForms = repository.getOpdForm()
                val unsyncedList = opdForms.filter { it.isSyn == 0 }
                val totalUnsyncedBefore = unsyncedList.size

                Log.d(TAG, "Total OPD forms: ${opdForms.size}")
                Log.d(TAG, "Unsynced before sync: $totalUnsyncedBefore")

                if (unsyncedList.isNotEmpty()) {
                    val opdFormRequest = OpdFormRequest(unsyncedList)
                    Log.d(TAG, "Prepared OPD form request: ${Gson().toJson(opdFormRequest)}")
                    syncDataToServer(opdFormRequest, totalUnsyncedBefore)
                } else {
                    Log.d(TAG, "No unsynced data found — sending broadcast to continue queue.")
                    val intent = Intent(ACTION_FORM_SYNC_COMPLETED).apply {
                        putExtra("formType", "OPD")
                        putExtra("syncedCount", 0)
                        putExtra("unsyncedCount", 0)
                    }
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                    stopSelf()
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error fetching local OPD forms", e)
            }
        }
    }

    private fun syncDataToServer(opdFormRequest: OpdFormRequest, totalUnsyncedBefore: Int) {
        Log.d(TAG, "Syncing data to server...")
        serviceScope.launch {
            try {
                delay(500)
                val response = repository.syncNewOpdForm(opdFormRequest)
                Log.d(TAG, "Response code: ${response.code()} | Message: ${response.message()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    val successList = ArrayList<Int>()

                    if (body != null && body.opd_Investigationses.isNotEmpty()) {
                        body.opd_Investigationses.forEach {
                            successList.add(it._id.toInt())
                        }
                    } else {
                        Log.e(TAG, "Server rejected form or returned empty list: ${body?.ErrorMessage}")
                    }

                    if (successList.isNotEmpty()) {
                        Log.d(TAG, "Successfully synced IDs: $successList")
                        updateLocalDb(successList, totalUnsyncedBefore)
                    } else {
                        Log.e(TAG, "No forms synced successfully.")
                        stopSelf()
                    }
                } else {
                    Log.e(TAG, "Network request failed: ${response.message()}")
                    stopSelf()
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error syncing data to server", e)
                stopSelf()
            }
        }
    }

    private fun updateLocalDb(successIdList: List<Int>, totalUnsyncedBefore: Int) {
        Log.d(TAG, "Updating local database for IDs: $successIdList")
        serviceScope.launch {
            try {
                repository.updateOpdForms(successIdList)
                repository.updatePatientForms(successIdList, OPD_FORM)
                Log.d(TAG, "Local DB updated successfully")

                // Check remaining unsynced after update
                val allOpdForms = repository.getOpdForm()
                val remainingUnsynced = allOpdForms.count { it.isSyn == 0 }

                val syncedCount = totalUnsyncedBefore - remainingUnsynced
                val unsyncedCount = remainingUnsynced

                Log.d(TAG, "✅ Synced: $syncedCount | ❌ Unsynced: $unsyncedCount")

                // Broadcast results
                val intent = Intent(ACTION_FORM_SYNC_COMPLETED).apply {
                    putExtra("formType", "OPD")          // or Vitals / Refractive / VisualAcuity
                    putExtra("syncedCount", syncedCount)
                    putExtra("unsyncedCount", unsyncedCount)
                }
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)


            } catch (e: Exception) {
                Log.e(TAG, "Error updating local database", e)
            } finally {
                Log.d(TAG, "Stopping service after DB update")
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
        serviceJob.cancel()
        sharedDispatcher.close()
    }

}
