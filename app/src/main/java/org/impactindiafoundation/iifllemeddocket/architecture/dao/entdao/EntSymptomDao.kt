package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntImpressionEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntSymptomsEntity

@Dao
interface EntSymptomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(symptom: EntSymptomsEntity) : Long

    @Query("SELECT * FROM symptoms_table")
    fun allSymptoms(): LiveData<List<EntSymptomsEntity>>


    @Query("SELECT * FROM symptoms_table WHERE formId = :intentFormId")
    suspend fun getSymptomsListById(intentFormId:Int): List<EntSymptomsEntity>

    @Query("SELECT * FROM symptoms_table WHERE formId = :formId")
    suspend fun getSymptomsByFormId(formId: Int): List<EntSymptomsEntity>
    @Delete
    suspend fun deleteSymptom(symptom: EntSymptomsEntity)

    @Query("SELECT * FROM symptoms_table")
    suspend fun getAllSymptoms(): List<EntSymptomsEntity>


    @Query("SELECT * FROM symptoms_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedSymptomsNow(): List<EntSymptomsEntity>

    @Query("UPDATE symptoms_table SET app_id = :appId WHERE uniqueId = :id")
    suspend fun updateSymptomsAppId(id: Int, appId: String)


    @Query("SELECT * FROM symptoms_table WHERE patientId = :patientId AND campId = :campId AND uniqueId = :uniqueId AND formId = :formId AND userId = :userId LIMIT 1")
    suspend fun getSymptomsByPatientAndCamp(
        patientId: Int,
        campId: Int,
        uniqueId: Int,
        formId : Int,
        userId : Int
    ): EntSymptomsEntity?

    @Update
    suspend fun updateSymptomsDetails(entity: EntSymptomsEntity)

    @Query("DELETE FROM symptoms_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun clearSyncedSymptoms()

    @Query("""
        DELETE FROM symptoms_table 
        WHERE patientId = :patientId 
        AND campId = :campId 
        AND userId = :userId 
        AND formId = :formId
    """)
    suspend fun deleteSymptomsByPatientCampUserForm(
        patientId: String,
        campId: String,
        userId: String,
        formId: String
    )

}