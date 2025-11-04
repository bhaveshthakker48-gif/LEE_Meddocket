package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.architecture.model.AgeGroupCount
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.architecture.model.SpecialityCount


@Dao
interface PrescriptionsDao {

    @Query("SELECT * FROM PatientMedicine")
    fun getAllFinalPrescriptionDrugs(): List<PatientMedicine>

    @Query("SELECT * FROM PatientMedicine WHERE _id =:opdFormId")
    fun getFinalPrescriptionByFormId(opdFormId:Int): List<PatientMedicine>

    @Query("SELECT COUNT(*) FROM PatientMedicine WHERE isSyn = 0")
    fun getUnsyncedFormsCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFinalPrescriptionDrug(finalPrescriptionDrug: PatientMedicine):Long

    @Update
    suspend fun updatePrescription(prescription: PatientMedicine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFinalPrescriptionDrugs1(finalPrescriptionDrugs: List<PatientMedicine>)

    @Update
    suspend fun updateFinalPrescriptionDrug(finalPrescriptionDrug: PatientMedicine)

    @Query("UPDATE PatientMedicine SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateFinalPrescriptionDrug1(id: Int, newIsSyn: Int)

    @Delete
    fun deleteFinalPrescriptionDrug(finalPrescriptionDrug: PatientMedicine)

    @Query("SELECT COUNT(DISTINCT patient_temp_id) FROM PatientMedicine")
    fun getUniquePatientCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM PatientMedicine")
    fun getTotalFormCount(): LiveData<Int>

    @Query("SELECT COUNT(DISTINCT patient_temp_id) FROM PatientMedicine WHERE patient_geneder = 'Male'")
    fun getMalePatientCount(): LiveData<Int>

    @Query("SELECT COUNT(DISTINCT patient_temp_id) FROM PatientMedicine WHERE patient_geneder = 'Female'")
    fun getFemalePatientCount(): LiveData<Int>

    @Query("SELECT COUNT(DISTINCT patient_temp_id) FROM PatientMedicine WHERE patient_geneder = 'Other'")
    fun getOtherPatientCount(): LiveData<Int>

    @Query("SELECT doctor_specialty, COUNT(*) as count FROM PatientMedicine GROUP BY doctor_specialty")
    fun getSpecialityCounts(): LiveData<List<SpecialityCount>>

    @Query("""
    SELECT 
        CASE 
            WHEN patient_age BETWEEN 0 AND 5 THEN '0-5'
            WHEN patient_age BETWEEN 6 AND 12 THEN '6-12'
            WHEN patient_age BETWEEN 13 AND 18 THEN '13-18'
            WHEN patient_age BETWEEN 19 AND 30 THEN '19-30'
            WHEN patient_age BETWEEN 31 AND 45 THEN '31-45'
            WHEN patient_age BETWEEN 46 AND 60 THEN '46-60'
            ELSE '60+' 
        END AS ageGroup,
        COUNT(*) as count
    FROM PatientMedicine
    GROUP BY ageGroup
""")
    suspend fun getAgeGroupCounts(): List<AgeGroupCount>

    @Query("SELECT * FROM PatientMedicine")
    fun getAll(): List<PatientMedicine>
}