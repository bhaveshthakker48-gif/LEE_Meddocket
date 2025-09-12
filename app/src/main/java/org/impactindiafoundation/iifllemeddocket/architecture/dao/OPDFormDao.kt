package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm


@Dao
interface OPDFormDao {

    @Query("SELECT * FROM OPD_Investigations")
    fun getAllOPD_InvestigationsData(): LiveData<List<OPD_Investigations>>

    @Query("SELECT COUNT(DISTINCT patient_id) FROM OPD_Investigations")
    fun getUniquePatientCount(): LiveData<Int>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOpdForm(opdForm: OPD_Investigations):Long

    @Query("SELECT * FROM OPD_Investigations")
    suspend fun getOpdForm(): List<OPD_Investigations>


    @Query("SELECT * FROM OPD_Investigations WHERE _id = :localFormId")
    fun getOpdFormById(localFormId:Int): List<OPD_Investigations>

    @Query("UPDATE OPD_Investigations SET isSyn = 1 WHERE _id IN (:ids)")
    fun updateOpdForms(ids: List<Int>)
}