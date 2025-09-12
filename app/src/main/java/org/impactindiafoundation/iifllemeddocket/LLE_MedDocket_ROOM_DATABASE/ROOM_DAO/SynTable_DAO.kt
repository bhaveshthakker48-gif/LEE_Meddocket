package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals


@Dao
interface SynTable_DAO {

    @Query("SELECT * FROM SynTable")
    fun getAllSynData(): LiveData<List<SynTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSynData(synTable: SynTable)


    @Query("UPDATE SynTable SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateSynData(id: Int, newIsSyn: Int)

    @Delete
    fun deleteSynData(synTable: SynTable)
}