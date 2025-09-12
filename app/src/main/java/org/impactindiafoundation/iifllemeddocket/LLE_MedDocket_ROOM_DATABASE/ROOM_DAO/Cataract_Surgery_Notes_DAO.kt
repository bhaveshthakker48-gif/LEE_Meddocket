package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Cataract_Surgery_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Notes


@Dao
interface Cataract_Surgery_Notes_DAO {

    @Query("SELECT * FROM Cataract_Surgery_Notes")
    fun getAllCataract_Surgery_Notes(): LiveData<List<Cataract_Surgery_Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCataract_Surgery_Notes(cataractSurgeryNotes: Cataract_Surgery_Notes)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCataract_Surgery_Notes1(cataractSurgeryNotes: Cataract_Surgery_Notes):Long



    @Query("UPDATE Cataract_Surgery_Notes SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateCataract_Surgeries(id: Int, newIsSyn: Int)

    @Query("SELECT * FROM Cataract_Surgery_Notes WHERE isSyn = 0")
    suspend fun fetchUnsyncedSurgicalNotes(): List<Cataract_Surgery_Notes>

    @Query("SELECT COUNT(DISTINCT patient_id) FROM Cataract_Surgery_Notes")
    fun getUniquePatientCount(): LiveData<Int>

    @Delete
    fun deleteCataract_Surgery_Notes(cataractSurgeryNotes: Cataract_Surgery_Notes)
}