package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.SyncSummaryEntity

@Dao
interface SyncSummaryDao {
    @Insert
    suspend fun insertSyncSummary(syncSummary: SyncSummaryEntity)

    @Query("SELECT * FROM sync_summary_table ORDER BY id DESC")
    fun getAllSyncSummaries(): LiveData<List<SyncSummaryEntity>>

    @Query("DELETE FROM sync_summary_table")
    suspend fun clearAll()
}
