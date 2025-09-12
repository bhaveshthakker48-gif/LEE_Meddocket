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

@Dao
interface AudiometryImageDao {

    @Query("SELECT * FROM audiometry_image_table")
    fun getAllAudiometryImages(): LiveData<List<AudiometryImageEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAudiometryImageDetails(audiometryImageEntity: AudiometryImageEntity): Long

    @Query("SELECT * FROM audiometry_image_table WHERE id = :localFormId")
    fun getAudiometryImageById(localFormId:Int): List<AudiometryImageEntity>

    @Query("SELECT * FROM audiometry_image_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedAudiometryImageDetailsNow(): List<AudiometryImageEntity>

    @Query("UPDATE audiometry_image_table SET app_id = :appId WHERE id = :id")
    suspend fun updateAudiometryImageDetailsAppId(id: Int, appId: String)

    @Query("DELETE FROM audiometry_image_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun clearSyncedAudiometryImage()

    @Query("""
        SELECT * FROM audiometry_image_table 
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
        uniqueId: Int
    ): AudiometryImageEntity?

    @Update
    suspend fun updateAudiometryImageDetails(entity: AudiometryImageEntity)
}
