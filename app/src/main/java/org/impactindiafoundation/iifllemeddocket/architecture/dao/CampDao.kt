package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem


@Dao
interface CampDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampDetail(camp: List<CampModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSingleCamp(camp: CampModel)

    @Query("SELECT * FROM camp_table")
    suspend fun getCampList(): List<CampModel>

    @Query("SELECT * FROM camp_table WHERE campId = :campId")
    suspend fun getCampById(campId:Int): List<CampModel>
}