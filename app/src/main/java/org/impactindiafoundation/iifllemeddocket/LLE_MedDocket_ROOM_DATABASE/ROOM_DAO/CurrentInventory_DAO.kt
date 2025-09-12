package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Cataract_Surgery_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CurrentInventoryLocal


@Dao
interface CurrentInventory_DAO {

    @Query("SELECT * FROM CurrentInventoryLocal")
    fun getAllCurrentInventory(): LiveData<List<CurrentInventoryLocal>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentInventory(inventoryList: List<CurrentInventoryLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentInventory(CurrentInventoryLocal: CurrentInventoryLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentInventory1(CurrentInventoryLocal: CurrentInventoryLocal):Long


    @Delete
    fun deleteCurrentInventory(CurrentInventoryLocal: CurrentInventoryLocal)

    @Query("DELETE FROM CurrentInventoryLocal")
    suspend fun deleteAllCurrentInventory()
}