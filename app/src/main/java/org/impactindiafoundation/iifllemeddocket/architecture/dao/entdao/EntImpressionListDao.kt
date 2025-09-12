package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntImpressionEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntSymptomsEntity

@Dao
interface EntImpressionListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(impression: EntImpressionEntity) : Long

    @Query("SELECT * FROM impression_table")
    fun allSymptoms(): LiveData<List<EntImpressionEntity>>

    @Query("SELECT * FROM impression_table WHERE formId = :intentFormId")
    suspend fun getImpressionListById(intentFormId:Int): List<EntImpressionEntity>

    @Query("SELECT * FROM impression_table WHERE formId = :formId")
    suspend fun getImpressionsByFormId(formId: Int): List<EntImpressionEntity>
    @Delete
    suspend fun deleteImpression(impression: EntImpressionEntity)

    @Query("SELECT * FROM impression_table") // change to your actual table
    suspend fun getAllImpressions(): List<EntImpressionEntity>


    @Query("SELECT * FROM impression_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedInvestigationNow(): List<EntImpressionEntity>

    @Query("UPDATE impression_table SET app_id = :appId WHERE uniqueId = :id")
    suspend fun updateImpressionAppId(id: Int, appId: String)

    @Query("SELECT * FROM impression_table WHERE patientId = :patientId AND campId = :campId AND uniqueId = :uniqueId AND formId = :formId AND userId = :userId LIMIT 1")
    suspend fun getImpressionByPatientCampAndServerId(
        patientId: Int,
        campId: Int,
        uniqueId: Int,
        formId : Int,
        userId : Int
    ): EntImpressionEntity?

    @Query("""
        DELETE FROM impression_table 
        WHERE patientId = :patientId 
        AND campId = :campId 
        AND userId = :userId 
        AND formId = :formId
    """)
    suspend fun deleteImpressionsByPatientCampUserForm(
        patientId: String,
        campId: String,
        userId: String,
        formId: String
    )
    @Update
    suspend fun updateImpressionDetails(entity: EntImpressionEntity)

    @Query("DELETE FROM impression_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun clearSyncedImpression()
}