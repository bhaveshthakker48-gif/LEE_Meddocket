package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImageModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.PreOpImageEntity

@Dao
interface PreOpImageDao {

    @Query("SELECT * FROM preop_image_table WHERE filename IS NOT NULL AND filename != ''")
    fun getAllPreOpImages(): LiveData<List<PreOpImageEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveConcentFormImageLocally(preOpImageEntity: PreOpImageEntity): Long

    @Query("SELECT * FROM preop_image_table WHERE id = :localFormId")
    fun getPreOpImageById(localFormId:Int): List<PreOpImageEntity>

    @Query("SELECT * FROM preop_image_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedPreOpImageDetailsNow(): List<PreOpImageEntity>

    @Query("UPDATE preop_image_table SET app_id = :appId WHERE id = :id")
    suspend fun updatePreOpImageDetailsAppId(id: Int, appId: String)

    @Query("DELETE FROM preop_image_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun clearSyncedPreOpImage()

    @Query("""
        SELECT * FROM preop_image_table 
        WHERE patientId = :patientId 
          AND campId = :campId 
          AND userId = :userId 
          AND id = :uniqueId
        LIMIT 1
    """)
    suspend fun getImageByPatientCampUser(
        patientId: Int,
        campId: Int,
        userId: Int,
        uniqueId: Int,
    ): PreOpImageEntity?

    @Update
    suspend fun updatePreOpImageDetails(entity: PreOpImageEntity)
}
