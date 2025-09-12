package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Cataract_Surgery_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Post_Op_AND_Follow_ups

@Dao
interface Eye_Post_Op_AND_Follow_ups_DAO {

    @Query("SELECT * FROM Eye_Post_Op_AND_Follow_ups")
    fun getAllEye_Post_Op_AND_Follow_upsData(): LiveData<List<Eye_Post_Op_AND_Follow_ups>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEye_Post_Op_AND_Follow_ups(eyePreOpInvestigation: Eye_Post_Op_AND_Follow_ups)

    @Query("SELECT * FROM Eye_Post_Op_AND_Follow_ups WHERE isSyn = 0")
    suspend fun fetchUnsyncedPostOPNotes(): List<Eye_Post_Op_AND_Follow_ups>

    @Query("SELECT COUNT(DISTINCT patient_id) FROM Eye_Post_Op_AND_Follow_ups")
    fun getUniquePatientCount(): LiveData<Int>


    @Query("UPDATE Eye_Post_Op_AND_Follow_ups SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun UpdatePostOpAndFollowUps(id: Int, newIsSyn: Int)

    @Delete
    fun deleteEye_Post_Op_AND_Follow_ups(eyePreOpInvestigation: Eye_Post_Op_AND_Follow_ups)
}