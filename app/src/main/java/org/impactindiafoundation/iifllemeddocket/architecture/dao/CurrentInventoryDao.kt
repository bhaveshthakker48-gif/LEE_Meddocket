package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CurrentInventoryLocal

@Dao
interface CurrentInventoryDao {

    @Query("SELECT * FROM CurrentInventoryLocal")
    fun getAllCurrentInventory(): LiveData<List<CurrentInventoryLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentInventory(inventoryList: List<CurrentInventoryLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentInventory(CurrentInventoryLocal: CurrentInventoryLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentInventory1(CurrentInventoryLocal: CurrentInventoryLocal):Long
}