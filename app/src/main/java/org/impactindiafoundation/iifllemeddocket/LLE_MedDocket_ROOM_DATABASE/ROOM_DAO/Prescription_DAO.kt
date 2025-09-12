package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Prescription_Model


@Dao
interface Prescription_DAO {

    @Query("SELECT * FROM Prescription_Model")
    fun getAllPrescriptionData(): LiveData<List<Prescription_Model>>

    @Query("SELECT * FROM Prescription_Model WHERE patient_id = :patientId")
    suspend fun getAllPrescriptionPatientWiseData(patientId: Int): List<Prescription_Model>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrescription(Prescription_Model: Prescription_Model)

    @Query("SELECT * FROM Prescription_Model WHERE patient_id = :patientId")
    suspend fun getPrescriptionsByPatientId(patientId: Int): List<Prescription_Model>

    @Delete
    fun deletePrescription(Prescription_Model: Prescription_Model)

    @Query("SELECT * FROM Prescription_Model WHERE patient_id = :patientId")
    suspend fun getPatientById(patientId: Int): Prescription_Model?

    @Query("SELECT * FROM Prescription_Model WHERE patient_id = :patientId")
    suspend fun getPatientByIdAndName(patientId: Int): Prescription_Model?

}