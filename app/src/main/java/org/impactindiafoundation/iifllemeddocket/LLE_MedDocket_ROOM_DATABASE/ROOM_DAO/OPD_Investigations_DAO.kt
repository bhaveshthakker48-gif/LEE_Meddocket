package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations


@Dao
interface OPD_Investigations_DAO {

    @Query("SELECT * FROM OPD_Investigations")
    fun getAllOPD_InvestigationsData(): LiveData<List<OPD_Investigations>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOPD_Investigations(opdInvestigations:OPD_Investigations)

    @Query("SELECT COUNT(DISTINCT patient_id) FROM OPD_Investigations")
    fun getUniquePatientCount(): LiveData<Int>

    @Delete
    fun deleteOPD_Investigations(opdInvestigations: OPD_Investigations)

    @Query("UPDATE OPD_Investigations SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateOPDInvestigation(id: Int, newIsSyn: Int)
}