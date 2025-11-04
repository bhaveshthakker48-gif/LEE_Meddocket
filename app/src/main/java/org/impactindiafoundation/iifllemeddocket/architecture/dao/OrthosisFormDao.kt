package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.AgeGroupCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm

@Dao
interface OrthosisFormDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrthosisForm(orthosisForm:OrthosisPatientForm):Long

    @Query("SELECT * FROM orthosis_patient_form")
    suspend fun getOrthosisPatientForm(): List<OrthosisPatientForm>

    @Query("SELECT * FROM orthosis_patient_form WHERE id = :localPatientId")
    fun getOrthosisPatientFormById(localPatientId:Int): List<OrthosisPatientForm>

    @Query("SELECT * FROM orthosis_patient_form WHERE tempPatientId = :tempId")
    fun getOrthosisPatientFormByTempId(tempId:Int): List<OrthosisPatientForm>

    @Query("UPDATE orthosis_patient_form SET isSynced = 1 WHERE id IN (:ids)")
    fun updateSyncedForms(ids: List<Int>)

    @Query("SELECT COUNT(DISTINCT patientId) FROM orthosis_patient_form")
    fun getPatientCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM orthosis_patient_form")
    fun getFormCount(): LiveData<Int>

    @Query("SELECT COUNT(DISTINCT patientId) FROM orthosis_patient_form WHERE patientGender = 'Male'")
    fun getMalePatientCount(): LiveData<Int>

    @Query("SELECT COUNT(DISTINCT patientId) FROM orthosis_patient_form WHERE patientGender = 'Female'")
    fun getFemalePatientCount(): LiveData<Int>

    @Query("SELECT COUNT(DISTINCT patientId) FROM orthosis_patient_form WHERE patientGender = 'Other'")
    fun getOtherPatientCount(): LiveData<Int>

    @Query("SELECT diagnosis, COUNT(*) as count FROM orthosis_patient_form GROUP BY diagnosis")
    suspend fun getDiagnosisCounts(): List<DiagnosisCount>

    @Query("""
    SELECT 
        CASE 
            WHEN patientAge BETWEEN 0 AND 5 THEN '0-5'
            WHEN patientAge BETWEEN 6 AND 12 THEN '6-12'
            WHEN patientAge BETWEEN 13 AND 18 THEN '13-18'
            WHEN patientAge BETWEEN 19 AND 30 THEN '19-30'
            WHEN patientAge BETWEEN 31 AND 45 THEN '31-45'
            WHEN patientAge BETWEEN 46 AND 60 THEN '46-60'
            ELSE '60+' 
        END AS ageGroup,
        COUNT(*) as count
    FROM orthosis_patient_form
    GROUP BY ageGroup
""")
    suspend fun getAgeGroupCounts(): List<AgeGroupCount>

}