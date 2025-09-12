package org.impactindiafoundation.iifllemeddocket.architecture.dao.pathologydao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyEntity

@Dao
interface PathalogyDao {

    @Query("SELECT * FROM pathology_table")
    fun getAll_PATHOLOGY_Follow_ups(): LiveData<List<PathologyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPathologyDetails(pathology : PathologyEntity) : Long

    @Query("SELECT * FROM pathology_table WHERE uniqueId = :localFormId")
    fun getPathologyById( localFormId:Int): List<PathologyEntity>

    @Query("SELECT * FROM pathology_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedPathologyDetailsNow(): List<PathologyEntity>

    @Query("UPDATE pathology_table SET app_id = :appId WHERE uniqueId = :id")
    suspend fun updatePathologyDetailsAppId(id: Int, appId: String)

    @Query("SELECT * FROM pathology_table WHERE patientId = :patientId AND campId = :campId AND uniqueId = :uniqueId LIMIT 1")
    suspend fun getPathologyDetailsByPatientAndCamp(patientId: Int, campId: Int, uniqueId: Int): PathologyEntity?

    @Update
    suspend fun updatePathologyDetails(entity: PathologyEntity)

    @Query("DELETE FROM pathology_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun clearSyncedPathologyReports()
}
