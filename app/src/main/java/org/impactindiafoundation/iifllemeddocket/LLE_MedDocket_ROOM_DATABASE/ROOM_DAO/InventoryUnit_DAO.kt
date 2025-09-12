package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CurrentInventoryLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.InventoryUnitLocal


@Dao
interface InventoryUnit_DAO {

    @Query("SELECT * FROM InventoryUnitLocal")
    fun getAllInventoryUnit(): LiveData<List<InventoryUnitLocal>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInventoryUnit(inventoryUnitList: List<InventoryUnitLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInventoryUnit(InventoryUnitLocal: InventoryUnitLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInventoryUnit1(InventoryUnitLocal: InventoryUnitLocal):Long


    @Delete
    fun deleteInventoryUnit(InventoryUnitLocal: InventoryUnitLocal)

    @Query("DELETE FROM InventoryUnitLocal")
    suspend fun deleteAllInventoryUnit()
}