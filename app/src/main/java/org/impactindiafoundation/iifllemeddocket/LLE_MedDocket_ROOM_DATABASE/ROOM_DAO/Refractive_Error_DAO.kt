package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError

@Dao
interface Refractive_Error_DAO {

    @Query("SELECT * FROM RefractiveError")
    fun getAllRefractiveErrorData(): LiveData<List<RefractiveError>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRefractiveError(refractiveError: RefractiveError)

    @Query("SELECT COUNT(DISTINCT patient_id) FROM RefractiveError")
    fun getUniquePatientCount(): LiveData<Int>

    @Query("UPDATE RefractiveError SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateRefractiveError(id: Int, newIsSyn: Int)

    @Delete
    fun deleteRefractiveError(refractiveError: RefractiveError)
}