package org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport


@Dao
interface EntPatientReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntPatientReport(patientReport: EntPatientReport):Long

    @Query("SELECT * FROM ent_patient_report")
    suspend fun getEntPatientReport(): List<EntPatientReport>

    @Query("SELECT * FROM ent_patient_report WHERE id = :localFormId")
    fun getEntPatientReportById(localFormId:Int): List<EntPatientReport>

    @Query("UPDATE ent_patient_report SET app_id = :appId WHERE patientId = :patientId AND formId = :id")
    suspend fun updatePatientReportAppId(patientId: Int, id: Int, appId: String)


    @Query("DELETE FROM ent_patient_report WHERE app_id = '1'")
    suspend fun clearSyncedEntPatientReportList()


}