package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals

@Dao
interface RefractiveErrorFormDao {

    @Query("SELECT * FROM RefractiveError")
    fun getAllRefractiveErrorData(): LiveData<List<RefractiveError>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRefractiveForm(refractiveForm: RefractiveError):Long

    @Query("SELECT * FROM RefractiveError")
    suspend fun getRefractiveForm(): List<RefractiveError>
    @Query("SELECT COUNT(DISTINCT patient_id) FROM RefractiveError")
    fun getUniquePatientCount(): LiveData<Int>

    @Query("SELECT * FROM RefractiveError WHERE _id = :localFormId")
    fun getRefractiveFormById(localFormId:Int): List<RefractiveError>


    @Query("UPDATE RefractiveError SET isSyn = 1 WHERE _id IN (:ids)")
    fun updateRefractiveForms(ids: List<Int>)
}