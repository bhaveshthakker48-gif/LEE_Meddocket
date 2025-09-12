package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntNoseType

@Dao
interface EntNoseSymptomsDao {

    @Query("SELECT * FROM nose_type")
    suspend fun getEntNoseType(): List<EntNoseType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntNoseType(entNoseType: EntNoseType): Long
}
