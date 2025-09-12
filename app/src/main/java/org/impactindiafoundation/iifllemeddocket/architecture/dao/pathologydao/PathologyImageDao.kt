package org.impactindiafoundation.iifllemeddocket.architecture.dao.pathologydao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.PreOpImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyImageEntity

@Dao
interface PathologyImageDao {

    @Query("SELECT * FROM pathology_image_table")
    fun getAllPathologyImages(): LiveData<List<PathologyImageEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPathologyImageDetails(audiometryImageEntity: PathologyImageEntity): Long

    @Query("SELECT * FROM pathology_image_table WHERE formId = :localFormId")
    fun getImagesByPatientId(localFormId: Int): LiveData<List<PathologyImageEntity>>

    @Query("UPDATE pathology_image_table SET filename = :newPath, appCreatedDate = :date WHERE patientId = :patientId AND reportType = :reportType")
    suspend fun updateImageInRoom(patientId: Int, reportType: String, newPath: String, date: String)

    @Query("SELECT * FROM pathology_image_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedPathologyImageDetailsNow(): List<PathologyImageEntity>
    @Query("UPDATE pathology_image_table SET app_id = :appId WHERE id = :id")
    suspend fun updateAudiometryImageDetailsAppId(id: Int, appId: String)

    @Query("DELETE FROM pathology_image_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun clearSyncedPathologyImage()

    @Query("""
        SELECT * FROM pathology_image_table 
        WHERE patientId = :patientId 
          AND campId = :campId 
          AND userId = :userId 
          AND reportType = :reportType
          AND formId = :uniqueId
        LIMIT 1
    """)
    suspend fun getPathologyImageByPatientCampUser(
        patientId: Int,
        campId: Int,
        userId: Int,
        reportType: String,
        uniqueId: Int,
        ): PathologyImageEntity?

    @Update
    suspend fun updatePreOpImageDetails(entity: PathologyImageEntity)

}
