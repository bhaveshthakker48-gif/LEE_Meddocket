package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity

@Dao
interface AudiometryDao {

    @Query("SELECT * FROM audiometry_table")
    fun getAll_Ent_Audiometry_Follow_ups(): LiveData<List<AudiometryEntity>>

    @Query("SELECT * FROM audiometry_table WHERE app_id = :appId")
    fun getEntAudiometrySyncData(appId: String = "1"): LiveData<List<AudiometryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAudiometryDetails(audiometryEntity: AudiometryEntity) : Long

    @Query("SELECT * FROM audiometry_table WHERE uniqueId = :localFormId AND patientId = :patientId")
    fun getAudiometrysById(localFormId:Int, patientId:Int): List<AudiometryEntity>

    @Query("SELECT * FROM audiometry_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedAudiometryDetailsNow(): List<AudiometryEntity>

    @Query("UPDATE audiometry_table SET app_id = :appId WHERE uniqueId = :id")
    suspend fun updateAudiometryDetailsAppId(id: Int, appId: String)

    @Query("SELECT * FROM audiometry_table WHERE patientId = :patientId AND campId = :campId AND uniqueId = :uniqueId AND userId = :userId LIMIT 1")
    suspend fun getAudiometryDetailsByPatientAndCamp(patientId: Int, campId: Int, uniqueId: Int, userId: Int): AudiometryEntity?
    @Update
    suspend fun updatePostOpNotesDetails(entity: AudiometryEntity)

    @Query("DELETE FROM audiometry_table WHERE app_id IS NOT NULL AND app_id != ''")
    fun clearSyncedAudiometryDetails()
}
