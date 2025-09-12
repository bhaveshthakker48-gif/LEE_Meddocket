package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Patient_RegistrationModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Prescription_Model
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Tuple


@Dao
interface Registration_DAO {

    @Query("SELECT * FROM Patient_RegistrationModel")
    fun getAllRegistrationData(): LiveData<List<Patient_RegistrationModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistrations(Patient_RegistrationModel: Patient_RegistrationModel)

    @Query("UPDATE Vitals SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateRegistration(id: Int, newIsSyn: Int)

    @Delete
    fun deleteRegistration(Patient_RegistrationModel: Patient_RegistrationModel)

    @Query("SELECT * FROM Patient_RegistrationModel WHERE patient_id = :patientId")
    suspend fun getRegistrationByPatientId(patientId: Int): List<Patient_RegistrationModel>


    @Query("SELECT * FROM Patient_RegistrationModel WHERE aadharno = :aadharno")
    suspend fun getPatientIdAndAadharNoByAadharNo(aadharno: String): List<Patient_RegistrationModel>
}