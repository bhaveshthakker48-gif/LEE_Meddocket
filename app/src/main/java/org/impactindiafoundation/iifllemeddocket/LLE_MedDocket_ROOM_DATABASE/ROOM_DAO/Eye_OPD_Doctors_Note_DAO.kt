package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_OPD_Doctors_Note
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Investigation


@Dao
interface Eye_OPD_Doctors_Note_DAO {

    @Query("SELECT * FROM Eye_OPD_Doctors_Note")
    fun getAllEye_OPD_Doctors_Note(): LiveData<List<Eye_OPD_Doctors_Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEye_OPD_Doctors_Note(eyeOpdDoctorsNote: Eye_OPD_Doctors_Note)


    @Query("UPDATE Eye_OPD_Doctors_Note SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateEyeopddocnotes(id: Int, newIsSyn: Int)

    @Query("SELECT COUNT(DISTINCT patient_id) FROM Eye_OPD_Doctors_Note")
    fun getUniquePatientCount(): LiveData<Int>
    @Delete
    fun deleteEye_OPD_Doctors_Note(eyeOpdDoctorsNote: Eye_OPD_Doctors_Note)


    @Query("SELECT * FROM Eye_OPD_Doctors_Note WHERE isSyn = 0")
    suspend fun fetchUnsyncedOpDoctorNotes(): List<Eye_OPD_Doctors_Note>
}