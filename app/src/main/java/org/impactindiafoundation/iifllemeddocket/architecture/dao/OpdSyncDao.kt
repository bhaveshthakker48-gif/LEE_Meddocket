package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.OpdSyncTable
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine

@Dao
interface OpdSyncDao {

    @Query("SELECT * FROM OpdSyncTable")
    fun getOpdSyncTable(): List<OpdSyncTable>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOpdSyncTable(opdSyncItem: OpdSyncTable):Long
}