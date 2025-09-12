package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity

@Dao
interface EntPostOPNotesDao {

    @Query("SELECT * FROM post_op_notes_table")
    fun getAll_Ent_Post_Follow_ups(): LiveData<List<EntPostOpNotesEntity>>

    @Query("SELECT * FROM post_op_notes_table WHERE app_id = :appId")
    fun getEntPostOpNotesSyncData(appId: String = "1"): LiveData<List<EntPostOpNotesEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPostOpNotes(entity: EntPostOpNotesEntity): Long

    @Query("SELECT * FROM post_op_notes_table WHERE uniqueId = :localFormId AND patientId = :patientId")
    fun getPostOpDetailsById(localFormId:Int, patientId:Int): List<EntPostOpNotesEntity>

    @Query("SELECT * FROM post_op_notes_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedPostOpNotes(): List<EntPostOpNotesEntity>

    @Query("UPDATE post_op_notes_table SET app_id = :appId WHERE uniqueId = :id")
    suspend fun updatePostOpNotesAppId(id: Int, appId: String)
    @Query("SELECT * FROM post_op_notes_table WHERE patientId = :patientId AND campId = :campId AND uniqueId = :uniqueId AND userId = :userId LIMIT 1")
    suspend fun getPostOpNotesByPatientAndCamp(patientId: Int, campId: Int, uniqueId : Int, userId: Int): EntPostOpNotesEntity?
    @Update
    suspend fun updatePostOpNotesDetails(entity: EntPostOpNotesEntity)

    @Query("DELETE FROM post_op_notes_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun clearSyncedPostOpNotes()
}