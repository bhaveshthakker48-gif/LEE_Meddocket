package org.impactindiafoundation.iifllemeddocket.services

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
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.NewRefractiveErrorRequest
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.ACTION_FORM_SYNC_COMPLETED
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.REFRACTIVE_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class RefractiveFormService : LifecycleService() {

    private val TAG = "RefractiveFormService"
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
        intent?.let {
            val dataMap = it.getSerializableExtra("QUERY_PARAMS") as? HashMap<String, Any>
            dataMap?.let { queryParams -> getDataFromLocal(queryParams) }
        }
        return START_NOT_STICKY
    }

    private fun getDataFromLocal(data: HashMap<String, Any>) {
        serviceScope.launch {
            try {
                val refractiveForms = repository.getRefractiveForm()
                val unsyncedForms = refractiveForms.filter { it.isSyn == 0 }
                val totalUnsyncedBefore = unsyncedForms.size

                Log.d(TAG, "Total refractive forms: ${refractiveForms.size}")
                Log.d(TAG, "Unsynced before sync: $totalUnsyncedBefore")

                if (unsyncedForms.isNotEmpty()) {
                    val request = NewRefractiveErrorRequest(unsyncedForms)
                    Log.d(TAG, "Prepared refractive sync request: ${Gson().toJson(request)}")
                    syncDataToServer(request, totalUnsyncedBefore)
                } else {
                    Log.d(TAG, "No unsynced data found — sending broadcast to continue queue.")
                    val intent = Intent(ACTION_FORM_SYNC_COMPLETED).apply {
                        putExtra("formType", "Refractive")
                        putExtra("syncedCount", 0)
                        putExtra("unsyncedCount", 0)
                    }
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                    stopSelf()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching refractive forms", e)
                stopSelf()
            }
        }
    }

    private fun syncDataToServer(request: NewRefractiveErrorRequest, totalUnsyncedBefore: Int) {
        serviceScope.launch {
            try {
                delay(500)
                val response = repository.syncRefractiveErrorForm(request)
                Log.d(TAG, "Response code: ${response.code()} | Message: ${response.message()}")

                if (response.isSuccessful && response.body() != null) {
                    val successList = response.body()!!.refractiveErrors.map { it._id.toInt() }
                    Log.d(TAG, "Synced IDs: $successList")
                    updateLocalDb(successList, totalUnsyncedBefore)
                } else {
                    val errorMsg = response.body()?.ErrorMessage ?: "Unexpected Network Error"
                    Log.e(TAG, errorMsg)
                    stopSelf()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error syncing data to server", e)
                stopSelf()
            }
        }
    }

    private fun updateLocalDb(successIdList: List<Int>, totalUnsyncedBefore: Int) {
        serviceScope.launch {
            try {
                repository.updateSyncedRefractiveForms(successIdList)
                repository.updatePatientForms(successIdList, REFRACTIVE_FORM)

                val allForms = repository.getRefractiveForm()
                val remainingUnsynced = allForms.count { it.isSyn == 0 }

                val syncedCount = totalUnsyncedBefore - remainingUnsynced
                val unsyncedCount = remainingUnsynced

                Log.d(TAG, "✅ Synced: $syncedCount | ❌ Unsynced: $unsyncedCount")

                val intent = Intent(ACTION_FORM_SYNC_COMPLETED).apply {
                    putExtra("formType", "Refractive")          // or Vitals / Refractive / VisualAcuity
                    putExtra("syncedCount", syncedCount)
                    putExtra("unsyncedCount", unsyncedCount)
                }
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Error updating local DB", e)
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
