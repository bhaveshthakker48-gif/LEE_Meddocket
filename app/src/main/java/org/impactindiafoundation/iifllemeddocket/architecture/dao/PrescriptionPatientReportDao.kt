package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport


@Dao
interface PrescriptionPatientReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrescriptionPatientReport(patientReport: PrescriptionPatientReport):Long

    @Query("SELECT * FROM prescription_patient_report")
    suspend fun getPrescriptionPatientReport(): List<PrescriptionPatientReport>

    @Query("SELECT * FROM prescription_patient_report WHERE id = :localFormId")
    fun getEntPatientReportById(localFormId:Int): List<PrescriptionPatientReport>

}