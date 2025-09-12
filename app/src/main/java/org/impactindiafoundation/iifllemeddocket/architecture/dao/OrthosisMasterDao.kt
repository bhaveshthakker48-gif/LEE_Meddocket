package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisTypeModelItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel

@Dao
interface OrthosisMasterDao {
    @Query("SELECT * FROM orthosis_master")
    suspend fun getOrthosisMaster(): List<OrthosisType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrthosisMaster(orthosisMaster: OrthosisType):Long
}