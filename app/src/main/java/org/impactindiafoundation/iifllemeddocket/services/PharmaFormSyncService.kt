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
import org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel.FinalPrescriptionDrugServer
import org.impactindiafoundation.iifllemeddocket.Model.FinalPrescriptionDrugModel.SendFinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.OpdSyncTable
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class PharmaFormSyncService : LifecycleService() {

    private val ERR_TAG = "PharmaFormService"
    private val serviceJob = Job()
    private val sharedDispatcher =
        Executors.newFixedThreadPool(2).asCoroutineDispatcher() // Limit to 2 concurrent tasks
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


                val finalPrescriptionDrugList = mutableListOf<FinalPrescriptionDrugServer>()

                val finalPrescriptionList = repository.getPatientMedicineReport()

                if (finalPrescriptionList.size > 0) {

                    Log.d(ConstantsApp.TAG, "finalPrescriptionList=>" + finalPrescriptionList)

                    try {
                        finalPrescriptionList.forEach { prescription ->
                            // Check if isSyn is 0
                            if (prescription.isSyn == 0) {
                                val prescriptionItems = prescription.prescriptionItems.map { item ->
                                    FinalPrescriptionDrugServer.PrescriptionItem(
                                        batch_no = item.batch_no,
                                        brand_name = item.brand_name,
                                        dose = item.dose,
                                        duration = item.duration,
                                        duration_unit = item.duration_unit,
                                        frequency = item.frequency,
                                        given = item.given,
                                        item_name = item.item_name,
                                        procurementItem_id = item.procurementItem_id.toLong(),
                                        qty = item.qty,
                                        qty_name = item.qty_name,
                                        qty_unit_id = item.qty_unit_id.toLong(),
                                        route = item.route
                                    )
                                }

                                val finalPrescriptionDrug = FinalPrescriptionDrugServer(
                                    _id = prescription._id.toLong(),
                                    camp_id = prescription.camp_id!!.toLong(),
                                    created_by = prescription.created_by.toLong(),
                                    department = prescription.department,
                                    doctor_name = prescription.doctor_name,
                                    doctor_specialty = prescription.doctor_specialty,
                                    patient_name = prescription.patient_name,
                                    patient_temp_id = prescription.patient_temp_id.toLong(),
                                    prescriptionItems = prescriptionItems
                                )

                                finalPrescriptionDrugList.add(finalPrescriptionDrug)
                            }
                        }


                        if (finalPrescriptionDrugList.isNotEmpty()) {

                            Log.d(
                                ConstantsApp.TAG,
                                "finalPrescriptionDrugList=>" + finalPrescriptionDrugList
                            )


                            val finalData = SendFinalPrescriptionDrug(finalPrescriptionDrugList)

                            Log.d(ConstantsApp.TAG, "newPrescriptionList=>" + data)


                            syncDataToServer(finalData)

                        } else {
                            Log.d(
                                ConstantsApp.TAG,
                                "finalPrescriptionDrugList=>" + finalPrescriptionDrugList
                            )

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Log.d(ConstantsApp.TAG, "No Data to upload")
                    stopSelf()
                }
            } catch (e: Exception) {
                Log.e(ERR_TAG, "Error fetching phone book matches", e)
            }
        }
    }


    private fun syncDataToServer(pharmaFormData: SendFinalPrescriptionDrug) {
        serviceScope.launch {
            try {
                delay(500)
                val response = repository.InsertFinalPrescriptionDrug(pharmaFormData)
                Log.d("RESPONSE", response.message())
                if (response.isSuccessful) {
                    val data = response.body()
                    for (i in 0 until data!!.givenMedicine.size) {
                        val responseData = data.givenMedicine[i]
                        val id = responseData._id
                        Log.d(ConstantsApp.TAG, "id=>" + id)
                        val isSyn = 1
                        repository.updateFinalPrescriptionDrug1(id.toInt(), isSyn)
                    }
                    val opdSyncItem = OpdSyncTable(
                        id = 0,
                        dateTime = getCurrentDate(),
                        syncedCount = data.givenMedicine.size
                    )
                    repository.insertOpdSyncTable(opdSyncItem)
                } else {
                    val error = response.body()?.ErrorMessage ?: "Unexpected Network Error"
                    Log.d(ERR_TAG, error)
                }
            } catch (e: Exception) {
                Log.d(ERR_TAG, e.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceJob.cancel()
        sharedDispatcher.close()
        stopSelf()
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

}