package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport


@Dao
interface PatientReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPatientReport(patientReport: PatientReport):Long

    @Query("SELECT * FROM patient_report")
    suspend fun getPatientReport(): List<PatientReport>

    @Query("SELECT * FROM patient_report WHERE id = :localFormId")
    fun getPatientReportById(localFormId:Int): List<PatientReport>

    @Query("UPDATE patient_report SET isSynced = 1 WHERE formId IN (:formId) AND formType =:formType")
    fun updatePatientForms(formId: List<Int>,formType:String)


}