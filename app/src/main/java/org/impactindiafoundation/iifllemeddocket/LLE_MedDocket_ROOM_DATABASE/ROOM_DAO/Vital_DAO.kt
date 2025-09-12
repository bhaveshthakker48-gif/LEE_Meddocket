package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals

@Dao
interface Vital_DAO {

    @Query("SELECT * FROM Vitals")
    fun getAllVitailsData(): LiveData<List<Vitals>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVitals(vitals: Vitals)
    @Query("SELECT COUNT(DISTINCT patient_id) FROM Vitals")
    fun getUniquePatientCount(): LiveData<Int>


    @Query("UPDATE Vitals SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateVital(id: Int, newIsSyn: Int)

    @Delete
    fun deleteVitals(vitals: Vitals)
}