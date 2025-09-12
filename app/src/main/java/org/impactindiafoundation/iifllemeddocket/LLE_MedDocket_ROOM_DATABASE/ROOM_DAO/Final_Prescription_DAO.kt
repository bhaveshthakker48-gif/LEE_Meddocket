package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PrescriptionGlassesFinal
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity


@Dao
interface Final_Prescription_DAO {

    @Query("SELECT * FROM PrescriptionGlassesFinal")
    fun getAll_PrescriptionGlassesFinal(): LiveData<List<PrescriptionGlassesFinal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrescription_Glasses_Final(prescriptionGlassesFinal: PrescriptionGlassesFinal): Long


    @Query("SELECT * FROM PrescriptionGlassesFinal WHERE _id = :localFormId AND patient_id = :patientId")
    fun getPostOpDetailsById(localFormId:Int, patientId:Int): List<PrescriptionGlassesFinal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrescriptionGlassesFinal(PrescriptionGlassesFinal: PrescriptionGlassesFinal):Long



    @Query("UPDATE PrescriptionGlassesFinal SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updatePrescription_Glasses_Final(id: Int, newIsSyn: Int)



    @Delete
    fun deletePrescription_Glasses_Final(PrescriptionGlassesFinal: PrescriptionGlassesFinal)
}