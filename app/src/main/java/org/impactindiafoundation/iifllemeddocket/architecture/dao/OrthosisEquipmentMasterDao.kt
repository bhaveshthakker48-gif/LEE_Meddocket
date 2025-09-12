package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.Equipment


@Dao
interface OrthosisEquipmentMasterDao {
    @Query("SELECT * FROM orthosis_equipment")
    suspend fun getEquipmentMaster(): List<Equipment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipmentMaster(equipmentMaster: Equipment):Long
}