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
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientFormMap
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class CampPatientFormService : LifecycleService() {

    private val ERR_TAG = "CampPatientFormService"
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
                val editedOrthosisForm = localOrthosisForm.filter { it.isEdited }
                val serverForm = ArrayList<OrthosisPatientForm>()
                serverForm.addAll(unsyncedOrthosisForm)
                serverForm.addAll(editedOrthosisForm)
                val patientFormMap = PatientFormMap(serverForm)

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
                val response = repository.syncCampPatientForNew(unsyncedData)
                Log.d("RESPONSE",response.message())
                if (response.isSuccessful) {
                    updateLocalDb(response.body()!!.successSyncId)
                } else {
                    val error = response.body()?.message ?: "Unexpected Network Error"
                    Log.d(ERR_TAG,error)
                }
            } catch (e: Exception) {
                Log.d(ERR_TAG,e.message.toString())
            }
        }

    }

    private fun updateLocalDb(successIdList:List<Int>){
        serviceScope.launch {
            repository.updateSyncedForms(successIdList)
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        sharedDispatcher.close()
        stopSelf()
    }

    companion object {
        const val ACTION_TASK_COMPLETED = "TASK_COMPLETION"
        const val ACTION_SERVICE_RUNNING = "SERVICE_RUNNING"
    }
}