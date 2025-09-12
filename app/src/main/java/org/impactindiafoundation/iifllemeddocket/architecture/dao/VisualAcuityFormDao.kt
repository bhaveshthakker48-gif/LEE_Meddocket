package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm


@Dao
interface VisualAcuityFormDao {
    @Query("SELECT * FROM VisualAcuity")
    fun getAllVisualAcuityData(): LiveData<List<VisualAcuity>>

    @Query("SELECT COUNT(DISTINCT patient_id) FROM VisualAcuity")
    fun getUniquePatientCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVisualAcuityForm(vitals: VisualAcuity):Long

    @Query("SELECT * FROM VisualAcuity")
    suspend fun getVisualAcuityForm(): List<VisualAcuity>

    @Query("SELECT * FROM VisualAcuity WHERE _id = :localFormId")
    fun getVisualAcuityFormById(localFormId:Int): List<VisualAcuity>

    @Query("UPDATE VisualAcuity SET isSyn = 1 WHERE _id IN (:ids)")
    fun updateVisualAcuityForms(ids: List<Int>)
}