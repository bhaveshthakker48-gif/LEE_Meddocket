package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisSynTable

@Dao
interface OrthosisSyncDao {

    @Query("SELECT * FROM orthosis_sync_table")
    fun getAllSynData(): LiveData<List<OrthosisSynTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSynData(synTable: OrthosisSynTable)

    @Delete
    fun deleteSynData(synTable: OrthosisSynTable)

}