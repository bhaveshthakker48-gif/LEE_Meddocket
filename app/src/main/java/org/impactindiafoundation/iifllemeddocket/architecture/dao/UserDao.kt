package org.impactindiafoundation.iifllemeddocket.architecture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(userList:UserModel):Long

    @Query("DELETE FROM users")
    suspend fun clearUserTable()
}