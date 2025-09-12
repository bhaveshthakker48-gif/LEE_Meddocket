package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImage
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideos
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages


@Dao
interface OrthosisFileDao {

    //orthosis image operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrthosisImage(orthosisImage: OrthosisImages):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrthosisImageList(orthosisImages:List<OrthosisImages>)

    @Query("SELECT * FROM orthosis_image")
    suspend fun getOrthosisImage(): List<OrthosisImages>

    @Query("SELECT * FROM orthosis_image WHERE patient_id = :formId")
    suspend fun getOrthosisImageByFormId(formId:String): List<OrthosisImages>

    @Query("UPDATE orthosis_image SET isSynced = 1 WHERE id IN (:ids)")
    fun updateSyncedOrthoImages(ids: List<Int>)

    @Query("UPDATE orthosis_image SET isSynced = 1 WHERE id = :id")
    suspend fun updateSyncedOrthoImage(id: Int)

    @Query("DELETE FROM orthosis_image WHERE images = :image")
    suspend fun deleteOrthosisImage(image: String)

    @Delete
    suspend fun deleteOrthosisImages(orthosisImage: List<OrthosisImages>)



    //form image oeprations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFormImage(formImage: FormImages):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertFormImageList(formImages: List<FormImages>): List<Long>

    @Delete
    suspend fun deleteFormImages(formImages: List<FormImages>)

    @Query("DELETE FROM form_images WHERE id IN (:formImagesId)")
    suspend fun deleteFormImagesById(formImagesId: List<Int>)

    @Query("SELECT * FROM form_images")
    suspend fun getFormImage(): List<FormImages>        

    @Query("SELECT * FROM form_images WHERE formId =:formId ")
    suspend fun getFormImageList(formId:Int): List<FormImages>

    @Query("UPDATE form_images SET isSynced = 1 WHERE id IN (:ids)")
    fun updateSyncedImages(ids: List<Int>)

    @Query("UPDATE form_images SET isSynced = 1 WHERE id = :id")
    suspend fun updateSyncedImage(id: Int)




    //form videos operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFormVideo(formVideo: FormVideos):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormVideoList(formVideos:List<FormVideos>)

    @Query("SELECT * FROM form_videos")
    suspend fun getFormVideo(): List<FormVideos>

    @Query("SELECT * FROM form_videos WHERE formId =:formId")
    suspend fun getFormVideoById(formId:Int): List<FormVideos>

    @Query("UPDATE form_videos SET isSynced = 1 WHERE id IN (:ids)")
    fun updateSyncedVideos(ids: List<Int>)

    @Query("UPDATE form_videos SET isSynced = 1 WHERE id = :id")
    suspend fun updateSyncedVideo(id: Int)

    @Query("DELETE FROM form_videos WHERE id IN (:formVideosId)")
    suspend fun deleteFormVideosById(formVideosId: List<Int>)

    //equipment image operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipmentImage(equipmentImage: EquipmentImage):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipmentImageList(equipmentImages:List<EquipmentImage>)

    @Query("SELECT * FROM equipment_image")
    suspend fun getEquipmentImage(): List<EquipmentImage>

    @Query("SELECT * FROM equipment_image WHERE patient_id = :formId")
    suspend fun getEquipmentImageByFormId(formId:String): List<EquipmentImage>

    @Query("UPDATE equipment_image SET isSynced = 1 WHERE id IN (:ids)")
    fun updateSyncedEquipmentImages(ids: List<Int>)

    @Query("UPDATE equipment_image SET isSynced = 1 WHERE id = :id")
    suspend fun updateSyncedEquipmentImage(id: Int)

    @Query("DELETE FROM equipment_image WHERE images = :image")
    suspend fun deleteEquipmentImage(image: String)

}