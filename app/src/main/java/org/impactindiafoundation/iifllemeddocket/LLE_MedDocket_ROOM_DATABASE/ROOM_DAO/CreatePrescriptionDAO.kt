package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CurrentInventoryLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Medicine_Report_Model


@Dao
interface CreatePrescriptionDAO {

    @Query("SELECT * FROM CreatePrescriptionModel")
    fun getAllCreatePrescription(): LiveData<List<CreatePrescriptionModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCreatePrescription(prescriptionList: List<CreatePrescriptionModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCreatePrescription(CreatePrescriptionModel: CreatePrescriptionModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCreatePrescription1(CreatePrescriptionModel: CreatePrescriptionModel):Long



    @Query("UPDATE CreatePrescriptionModel SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateCreatePrescriptionModel(id: Int, newIsSyn: Int)

    @Delete
    fun deleteCreatePrescription(CreatePrescriptionModel: CreatePrescriptionModel)

    @Query("DELETE FROM CreatePrescriptionModel")
    suspend fun deleteAllCreatePrescription()

    @Query("SELECT device_id, user_id, current_date AS date, COUNT(*) AS total_inserted, SUM(CASE WHEN isSyn = 1 THEN 1 ELSE 0 END) AS total_synced FROM CreatePrescriptionModel GROUP BY device_id, user_id, current_date")
    suspend fun getMedicineReports(): List<Medicine_Report_Model>

    @Query("SELECT * FROM CreatePrescriptionModel WHERE `current_date` = :date ORDER BY `current_date`")
    fun getMedicinePrescriptionsInsertedByDate(date: String): LiveData<List<CreatePrescriptionModel>>

    @Query("SELECT * FROM CreatePrescriptionModel WHERE isSyn=0 AND current_date = :date ORDER BY current_date")
    fun getMedicinePrescriptionsSyncedByDate(date: String): LiveData<List<CreatePrescriptionModel>>
}