package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImageModel

@Dao
interface Image_Upload_DAO {

    @Query("SELECT * FROM Image WHERE file_name IS NOT NULL AND file_name != ''")
    fun getAllValidImages(): LiveData<List<ImageModel>>

    @Query("SELECT * FROM Image")
    fun getAllImage(): LiveData<List<ImageModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(imageModel: ImageModel)

    @Delete
    fun deleteImages(imageModel: ImageModel)

    @Query("UPDATE Image SET isSyn = :newIsSyn WHERE _id = :id")
    suspend fun updateImage(id: Int, newIsSyn: Int)


}