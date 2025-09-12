package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm


@Dao
interface VitalsFormDao {

    @Query("SELECT * FROM Vitals")
    fun getAllVitailsData(): LiveData<List<Vitals>>

    @Query("SELECT COUNT(DISTINCT patient_id) FROM Vitals")
    fun getUniquePatientCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVitalsForm(vitals: Vitals):Long

    @Query("SELECT * FROM Vitals WHERE _id = :localFormId")
    fun getVitalsFormById(localFormId:Int): List<Vitals>

    @Query("SELECT * FROM Vitals")
    suspend fun getVitalsForm(): List<Vitals>

    @Query("UPDATE Vitals SET isSyn = 1 WHERE _id IN (:ids)")
    fun updateVitalsForms(ids: List<Int>)
}