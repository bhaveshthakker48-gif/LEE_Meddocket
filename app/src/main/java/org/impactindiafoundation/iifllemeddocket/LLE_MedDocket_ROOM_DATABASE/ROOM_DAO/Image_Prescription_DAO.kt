package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImagePrescriptionModel

@Dao
interface Image_Prescription_DAO {

    @Query("SELECT * FROM ImagePrescriptionModel")
    fun getAllImagePrescriptionModel(): LiveData<List<ImagePrescriptionModel>>

    @Query("SELECT * FROM ImagePrescriptionModel WHERE isSyn = 0")
     fun getImagePrescriptionsIsSyn0(): LiveData<List<ImagePrescriptionModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImagePrescriptionModel(imageModel: ImagePrescriptionModel)

    @Delete
    fun deleteImagePrescriptionModel(imageModel: ImagePrescriptionModel)

    @Query("UPDATE ImagePrescriptionModel SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateImagePrescriptionModel(id: Int, newIsSyn: Int)
}