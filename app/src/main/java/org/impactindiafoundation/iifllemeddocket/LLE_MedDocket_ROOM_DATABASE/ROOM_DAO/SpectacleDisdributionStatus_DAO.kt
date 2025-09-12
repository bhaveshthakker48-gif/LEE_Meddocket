package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_OPD_Doctors_Note
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleDisdributionStatusModel


@Dao
interface SpectacleDisdributionStatus_DAO {

    @Query("SELECT * FROM SpectacleDisdributionStatusModel")
    fun getAllSpectacleDisdributionStatusModel(): LiveData<List<SpectacleDisdributionStatusModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSpectacleDisdributionStatusModel(SpectacleDisdributionStatusModel: SpectacleDisdributionStatusModel)


    @Query("UPDATE SpectacleDisdributionStatusModel SET isSyn = :newIsSyn WHERE `index` = :id")
    suspend fun updateSpectacleDisdributionStatusModel(id: Int, newIsSyn: Int)

    @Delete
    fun deleteSpectacleDisdributionStatusModel(SpectacleDisdributionStatusModel: SpectacleDisdributionStatusModel)

    @Query("SELECT * FROM SpectacleDisdributionStatusModel WHERE patient_id = :patientId")
    suspend fun GetPrescriptionStatusDetails(patientId: Int): List<SpectacleDisdributionStatusModel>


    @Query("SELECT * FROM SpectacleDisdributionStatusModel WHERE patient_id = :patientId LIMIT :pageSize OFFSET :offset")
    suspend fun getPrescriptionStatusDetailsWithPagination(patientId: Int, pageSize: Int, offset: Int): List<SpectacleDisdributionStatusModel>

    @Query("SELECT * FROM SpectacleDisdributionStatusModel WHERE patient_id = :patientId")
    suspend fun getPrescriptionStatusDetailsWithPaginationNew(patientId: Int): List<SpectacleDisdributionStatusModel>


}