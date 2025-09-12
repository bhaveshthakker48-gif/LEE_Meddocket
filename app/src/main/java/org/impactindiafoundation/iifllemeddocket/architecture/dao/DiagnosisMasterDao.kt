package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType


@Dao
interface DiagnosisMasterDao {
    @Query("SELECT * FROM diagnosis_master")
    suspend fun getDiagnosisMaster(): List<DiagnosisType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDiagnosisMaster(diagnosisMaster: DiagnosisType):Long
}