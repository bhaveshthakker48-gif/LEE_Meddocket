package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.ImpressionType

@Dao
interface EntImpressionDao {

    @Query("SELECT * FROM impression_type")
    suspend fun getEntImpressionType(): List<ImpressionType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntImpression(entEarType: ImpressionType): Long

}