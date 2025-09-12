package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity


@Dao
interface VisualAcuity_DAO {

    @Query("SELECT * FROM VisualAcuity")
    fun getAllVisualAcuityData(): LiveData<List<VisualAcuity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVisualAcuity(visualAcuity: VisualAcuity)

    @Query("SELECT COUNT(DISTINCT patient_id) FROM VisualAcuity")
    fun getUniquePatientCount(): LiveData<Int>

    @Query("UPDATE VisualAcuity SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateVisualActivities(id: Int, newIsSyn: Int)

    @Delete
    fun deleteVisualAcuity(visualAcuity: VisualAcuity)
}