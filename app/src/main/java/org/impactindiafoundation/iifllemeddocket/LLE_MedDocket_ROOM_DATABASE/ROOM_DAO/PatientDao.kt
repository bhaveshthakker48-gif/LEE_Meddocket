package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal

@Dao
interface PatientDao {

    @Query("SELECT * FROM Patient")
    fun getAllpatientFromLocal(): LiveData<List<PatientDataLocal>>

    @Query("SELECT * FROM Patient WHERE patientId = :id LIMIT 1")
    suspend fun getPatientById(id: String): PatientDataLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPatient(patientDataLocal:PatientDataLocal)

    @Delete
    fun deletePatient(patientDataLocal: PatientDataLocal)
}