package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug

@Dao
interface FinalPrescriptionDrugDAO {

    @Query("SELECT * FROM FinalPrescriptionDrug")
    fun getAllFinalPrescriptionDrugs(): LiveData<List<FinalPrescriptionDrug>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFinalPrescriptionDrug(finalPrescriptionDrug: FinalPrescriptionDrug)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFinalPrescriptionDrugs1(finalPrescriptionDrugs: List<FinalPrescriptionDrug>)

    @Update
    suspend fun updateFinalPrescriptionDrug(finalPrescriptionDrug: FinalPrescriptionDrug)

    @Query("UPDATE FinalPrescriptionDrug SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateFinalPrescriptionDrug1(id: Int, newIsSyn: Int)

    @Delete
    fun deleteFinalPrescriptionDrug(finalPrescriptionDrug: FinalPrescriptionDrug)
}
