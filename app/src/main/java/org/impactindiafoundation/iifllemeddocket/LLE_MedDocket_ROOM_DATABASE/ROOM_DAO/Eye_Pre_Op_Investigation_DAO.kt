package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Investigation
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity


@Dao
interface Eye_Pre_Op_Investigation_DAO {

    @Query("SELECT * FROM Eye_Pre_Op_Investigation")
    fun getAllEye_Pre_Op_Investigation(): LiveData<List<Eye_Pre_Op_Investigation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEye_Pre_Op_Investigation(eyePreOpInvestigation: Eye_Pre_Op_Investigation)

    @Query("SELECT COUNT(DISTINCT patient_id) FROM Eye_Pre_Op_Investigation")
    fun getUniquePatientCount(): LiveData<Int>
    @Delete
    fun deleteEye_Pre_Op_Investigation(eyePreOpInvestigation: Eye_Pre_Op_Investigation)


    @Query("UPDATE Eye_Pre_Op_Investigation SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateEye_Pre_Op_Investigations(id: Int, newIsSyn: Int)

    @Query("SELECT * FROM Eye_Pre_Op_Investigation WHERE isSyn = 0")
    suspend fun getUnsyncedInvestigationsOnce(): List<Eye_Pre_Op_Investigation>

}