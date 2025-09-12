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
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.AddRefractiveErrorRequest
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.NewRefractiveErrorRequest
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.getRefractiveErrorResponse
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants.REFRACTIVE_FORM
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientFormMap
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class RefractiveFormService : LifecycleService() {

    private val ERR_TAG = "RefractiveFormService"
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
                val refractiveErrorForm = repository.getRefractiveForm()
                val unsyncedrefractiveErrorForm = refractiveErrorForm.filter { it.isSyn == 0 }
                val AddRefractiveErrorRequest = NewRefractiveErrorRequest(unsyncedrefractiveErrorForm)
                if (!unsyncedrefractiveErrorForm.isNullOrEmpty()) {
                    val gson = Gson()
                    syncDataToServer(AddRefractiveErrorRequest)
                }
                else{
                    stopSelf()
                }
            } catch (e: Exception) {
                Log.e(ERR_TAG, "Error fetching phone book matches", e)
            }
        }
    }

    private fun syncDataToServer(unsyncedData:  NewRefractiveErrorRequest) {
        serviceScope.launch {
            try {
                delay(500)
                val response = repository.syncRefractiveErrorForm(unsyncedData)
                Log.d("RESPONSE",response.message())
                if (response.isSuccessful) {
                    var succesList = ArrayList<Int>()
                    for (i in response.body()!!.refractiveErrors){
                        succesList.add(i._id.toInt())
                    }
                    updateLocalDb(succesList)
                } else {
                    val error = response.body()?.ErrorMessage ?: "Unexpected Network Error"
                    Log.d(ERR_TAG,error)
                }
            } catch (e: Exception) {
                Log.d(ERR_TAG,e.message.toString())
            }
        }

    }

    private fun updateLocalDb(successIdList:List<Int>){
        serviceScope.launch {
            repository.updateSyncedRefractiveForms(successIdList)
            repository.updatePatientForms(successIdList,REFRACTIVE_FORM)
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceJob.cancel()
        sharedDispatcher.close()
        stopSelf()
    }

}