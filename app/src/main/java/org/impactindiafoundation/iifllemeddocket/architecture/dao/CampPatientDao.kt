package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem

@Dao
interface CampPatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampPatient(campPatientList: List<CampPatientDataItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleCampPatient(campPatient:CampPatientDataItem):Long

    @Query("SELECT * FROM camp_patient_details")
    suspend fun getCampPatientList(): List<CampPatientDataItem>

    @Query("SELECT * FROM camp_patient_details WHERE temp_patient_id = :tempId")
    suspend fun getCampPatientListById(tempId:Int): List<CampPatientDataItem>

    @Query("DELETE FROM camp_patient_details WHERE id = :campPatientId")
    suspend fun deleteCampPatientData(campPatientId:Int)
}