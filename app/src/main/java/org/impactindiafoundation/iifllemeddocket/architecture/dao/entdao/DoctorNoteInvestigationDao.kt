package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntImpressionEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity

@Dao
interface DoctorNoteInvestigationDao {
    @Query("SELECT * FROM doctor_note_investigation_table")
    fun getAll_Ent_Opd_Doctor_Follow_ups(): LiveData<List<DoctorNoteInvestigationEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDoctorNoteInvestigation(entity: DoctorNoteInvestigationEntity) : Long
    @Update
    suspend fun updateInvestigation(entity: DoctorNoteInvestigationEntity)
    @Query("SELECT * FROM doctor_note_investigation_table WHERE patientId = :patientID LIMIT 1")
    suspend fun getDoctorNoteInvestigationByPatientId(patientID: Int): DoctorNoteInvestigationEntity?
    @Query("SELECT * FROM doctor_note_investigation_table WHERE uniqueId = :intentFormId")
    fun getDoctorNoteInvestigationListById(intentFormId:Int): List<DoctorNoteInvestigationEntity>
    @Query("SELECT * FROM doctor_note_investigation_table WHERE app_id IS NULL OR app_id = ''")
    suspend fun getUnSyncedInvestigationNow(): List<DoctorNoteInvestigationEntity>
    @Query("UPDATE doctor_note_investigation_table SET app_id = :appId WHERE uniqueId = :id")
    suspend fun updateInvestigationAppId(id: Int, appId: String)
    @Query("SELECT * FROM doctor_note_investigation_table WHERE patientId = :patientId AND campId = :campId AND uniqueId = :uniqueId AND userId = :userId LIMIT 1")
    suspend fun getDoctorInvestigationByPatientAndCamp(patientId: Int, campId: Int, uniqueId : Int, userId : Int): DoctorNoteInvestigationEntity?
    @Update
    suspend fun updateDoctorInvestigationDetails(entity: DoctorNoteInvestigationEntity)

    @Query("DELETE FROM doctor_note_investigation_table WHERE app_id IS NOT NULL AND app_id != ''")
    suspend fun clearSyncedDoctorInvestigationNotes()

    @Query("SELECT * FROM doctor_note_investigation_table WHERE app_id = :appId")
    fun getSyncedDoctorInvestigationByAppId(appId: String = "1"): LiveData<List<DoctorNoteInvestigationEntity>>

}
