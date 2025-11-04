package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.SyncPrescriptionRecordEntity

@Dao
interface SyncPrescriptionRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncRecord(record: SyncPrescriptionRecordEntity)

    @Query("SELECT * FROM sync_prescription_record_table ORDER BY id DESC")
    fun getAllSyncRecords(): LiveData<List<SyncPrescriptionRecordEntity>>

    @Query("DELETE FROM sync_prescription_record_table")
    suspend fun clearAllSyncRecords()
}
