package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Investigation
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Notes

@Dao
interface Eye_Pre_Op_Notes_DAO {

    @Query("SELECT * FROM Eye_Pre_Op_Notes")
    fun getAllEye_Pre_Op_Notes(): LiveData<List<Eye_Pre_Op_Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEye_Pre_Op_Notes(eyePreOpNotes: Eye_Pre_Op_Notes): Long

    @Query("SELECT COUNT(DISTINCT patient_id) FROM Eye_Pre_Op_Notes")
    fun getUniquePatientCount(): LiveData<Int>


    @Query("UPDATE Eye_Pre_Op_Notes SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateEye_Pre_Op_Notes(id: Int, newIsSyn: Int)

    @Query("SELECT * FROM Eye_Pre_Op_Notes WHERE eyePreOpNotesImagepath IS NOT NULL AND eyePreOpNotesImagepath != ''")
    fun getAllEye_Pre_Op_NotesWithImagePath(): LiveData<List<Eye_Pre_Op_Notes>>


    @Query("SELECT * FROM Eye_Pre_Op_Notes WHERE isSyn = 0")
    suspend fun fetchUnsyncedPreOpNotes(): List<Eye_Pre_Op_Notes>

    @Delete
    fun deleteEye_Pre_Op_Notes(eyePreOpNotes: Eye_Pre_Op_Notes)
}