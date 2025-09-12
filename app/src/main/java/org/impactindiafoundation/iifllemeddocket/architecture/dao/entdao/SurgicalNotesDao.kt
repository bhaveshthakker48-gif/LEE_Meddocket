package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity

@Dao
interface SurgicalNotesDao {
    @Query("SELECT * FROM surgical_note_table")
    fun getAll_Ent_Surgical_Follow_ups(): LiveData<List<SurgicalNotesEntity>>

    @Query("SELECT * FROM surgical_note_table WHERE app_id = :appId")
    fun getEntSurgicalNotesSyncData(appId: String = "1"): LiveData<List<SurgicalNotesEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSurgicalNotes(entity: SurgicalNotesEntity): Long

    @Query("SELECT * FROM surgical_note_table WHERE uniqueId = :localFormId AND patientId = :patientId")
    fun getSurgicalNotesById(localFormId:Int, patientId: Int): List<SurgicalNotesEntity>

    @Query("SELECT * FROM surgical_note_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedSurgicalNotes(): List<SurgicalNotesEntity>

    @Query("UPDATE surgical_note_table SET app_id = :appId WHERE uniqueId = :id")
    suspend fun updateSurgicalNotesAppId(id: Int, appId: String)

    @Query("SELECT * FROM surgical_note_table WHERE patientId = :patientId AND campId = :campId AND uniqueId = :uniqueId AND userId = :userId LIMIT 1")
    suspend fun getSurgicalNotesByPatientAndCamp(patientId: Int, campId: Int, uniqueId : Int, userId : Int): SurgicalNotesEntity?
    @Update
    suspend fun updateSurgicalNotesDetails(entity: SurgicalNotesEntity)

    @Query("DELETE FROM surgical_note_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun clearSyncedSurgicalNotes()

}