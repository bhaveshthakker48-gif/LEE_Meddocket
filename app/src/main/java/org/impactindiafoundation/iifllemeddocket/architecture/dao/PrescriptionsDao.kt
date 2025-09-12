package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine


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
}