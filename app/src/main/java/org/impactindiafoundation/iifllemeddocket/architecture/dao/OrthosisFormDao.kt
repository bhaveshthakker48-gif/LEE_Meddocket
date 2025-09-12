package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType


@Dao
interface OrthosisFormDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrthosisForm(orthosisForm:OrthosisPatientForm):Long

    @Query("SELECT * FROM orthosis_patient_form")
    suspend fun getOrthosisPatientForm(): List<OrthosisPatientForm>

    @Query("SELECT * FROM orthosis_patient_form WHERE id = :localPatientId")
    fun getOrthosisPatientFormById(localPatientId:Int): List<OrthosisPatientForm>

    @Query("SELECT * FROM orthosis_patient_form WHERE tempPatientId = :tempId")
    fun getOrthosisPatientFormByTempId(tempId:Int): List<OrthosisPatientForm>

    @Query("UPDATE orthosis_patient_form SET isSynced = 1 WHERE id IN (:ids)")
    fun updateSyncedForms(ids: List<Int>)
}