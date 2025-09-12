package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Post_Op_AND_Follow_ups
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity

@Dao
interface PreOpDetailsDao {

    @Query("SELECT * FROM preop_details_table")
    fun getAll_Ent_Pro_Op_Follow_ups(): LiveData<List<EntPreOpDetailsEntity>>

    @Query("SELECT * FROM preop_details_table WHERE app_id = :appId")
    fun getEntPreOpDetailsSyncData(appId: String = "1"): LiveData<List<EntPreOpDetailsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPreOpDetails(entity: EntPreOpDetailsEntity): Long

    @Query("SELECT * FROM preop_details_table WHERE uniqueId = :localFormId AND patientId = :patientId")
    fun getPreOpDetailsById(localFormId:Int, patientId: Int): List<EntPreOpDetailsEntity>

    @Query("SELECT * FROM preop_details_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedPreOpDetails(): List<EntPreOpDetailsEntity>

    @Query("UPDATE preop_details_table SET app_id = :appId WHERE uniqueId = :id")
    suspend fun updatePreOpDetailsAppId(id: Int, appId: String)

    @Query("SELECT * FROM preop_details_table WHERE patientId = :patientId AND campId = :campId AND uniqueId = :id AND userId = :userId LIMIT 1")
    suspend fun getByPatientIdAndCampId(patientId: Int, campId: Int, id: Int, userId: Int): EntPreOpDetailsEntity?

    @Update
    suspend fun updatePreOpDetails(entity: EntPreOpDetailsEntity)

    @Query("DELETE FROM preop_details_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun deleteSyncedPreOpDetails()
}