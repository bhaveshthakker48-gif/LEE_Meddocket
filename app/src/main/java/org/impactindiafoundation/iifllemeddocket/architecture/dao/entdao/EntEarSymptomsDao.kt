package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntEarType

@Dao
interface EntEarSymptomsDao {

    @Query("SELECT * FROM eye_type")
    suspend fun getEntEarType(): List<EntEarType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntEarType(entEarType: EntEarType): Long
}
