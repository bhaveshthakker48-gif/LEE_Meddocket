package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisSynTable
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionSynTable

@Dao
interface PrescriptionSyncDao {

    @Query("SELECT * FROM orthosis_sync_table")
    fun getAllSynData(): LiveData<List<PrescriptionSynTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSynData(synTable: PrescriptionSynTable)

    @Delete
    fun deleteSynData(synTable: PrescriptionSynTable)

}