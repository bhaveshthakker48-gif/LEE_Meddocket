package org.impactindiafoundation.iifllemeddocket.architecture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OrthosisFileDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OrthosisFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImage
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormVideos
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm

@Database(entities = [OrthosisImages::class,FormImages::class,FormVideos::class,EquipmentImage::class], version = 2)
abstract class OrthosisFileDataBase : RoomDatabase() {

    abstract fun orthosisFileDao(): OrthosisFileDao

    companion object {
        @Volatile
        private var INSTANCE: OrthosisFileDataBase? = null

        fun getDatabase(context: Context): OrthosisFileDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrthosisFileDataBase::class.java,
                    Constants.ORTHOSIS_FILE_DATABASE_NAME
                )
                    .addMigrations(*migrations)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val migrations = arrayOf<Migration>()

    }
}