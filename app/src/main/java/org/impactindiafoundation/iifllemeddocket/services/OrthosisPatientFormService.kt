package org.impactindiafoundation.iifllemeddocket.services

import android.content.Intent
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
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientFormMap
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OrthosisMainViewModel
import java.util.Objects
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class OrthosisPatientFormService : LifecycleService() {

    private val ERR_TAG = "OrthosisPatientFormService"
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
                val localOrthosisForm = repository.getOrthosisPatientForm()
                val unsyncedOrthosisForm = localOrthosisForm.filter { it.isSynced == 0 }
                val patientFormMap = PatientFormMap(unsyncedOrthosisForm)

                if (!unsyncedOrthosisForm.isNullOrEmpty()) {
                    val gson = Gson()
                    orthosisFormJson = gson.toJson(patientFormMap)
                    syncDataToServer(patientFormMap)
                }
                else{
                    stopSelf()
                }
            } catch (e: Exception) {
                Log.e(ERR_TAG, "Error fetching phone book matches", e)
            }
        }
    }

    private fun syncDataToServer(unsyncedData: PatientFormMap) {
        serviceScope.launch {
            try {
                delay(500)
                val response = repository.syncOrthosisPatientForNew(unsyncedData)
                Log.d("SyncForms", "Server Response: ${response.message()}")
                if (response.isSuccessful) {
                    Log.d("SyncForms", "Sync success, updating local DB with IDs: ${response.body()!!.successSyncId}")
                    updateLocalDb(response.body()!!.successSyncId)
                } else {
                    val error = response.body()?.message ?: "Unexpected Network Error"
                    Log.e("SyncForms", "Sync failed: $error")
                }
            } catch (e: Exception) {
                Log.e("SyncForms", "Sync Exception: ${e.message}", e)
            }
        }
    }

    private fun updateLocalDb(successIdList: List<Int>) {
        serviceScope.launch {
            Log.d("SyncForms", "Updating local DB with synced IDs: $successIdList")
            repository.updateSyncedForms(successIdList)

            Log.d("SyncForms", "Local DB updated. Sending broadcast to refresh UI.")
            val intent = Intent(ACTION_TASK_COMPLETED)
            LocalBroadcastManager.getInstance(this@OrthosisPatientFormService).sendBroadcast(intent)

            Log.d("SyncForms", "Broadcast sent successfully.")
            stopSelf()
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        serviceJob.cancel()
        sharedDispatcher.close()
        stopSelf()
    }


    companion object {
        const val ACTION_PROGRESS_UPDATE = "PROGRESS_UPDATE"
        const val ACTION_TASK_COMPLETED = "TASK_COMPLETION"
        const val ACTION_SERVICE_RUNNING = "SERVICE_RUNNING"
    }
}