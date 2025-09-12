package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntThroatType

@Dao
interface EntThroatSymptomsDao {

    @Query("SELECT * FROM throat_type")
    suspend fun getEntThroatType(): List<EntThroatType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntThroatType(entThroatType: EntThroatType): Long
}
