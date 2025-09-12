package org.impactindiafoundation.iifllemeddocket.architecture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.MeasurementConverter
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.MeasurementDataListConvertor
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.OrthosisTypeConvertor
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.OrthosisTypeListConvertor
import org.impactindiafoundation.iifllemeddocket.architecture.dao.CampDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.CampPatientDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem

@Database(entities = [CampModel::class], version = 1)
abstract class CampDataBase : RoomDatabase() {
    abstract fun campDao(): CampDao

    companion object {
        @Volatile
        private var INSTANCE: CampDataBase? = null

        fun getDatabase(context: Context): CampDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CampDataBase::class.java,
                    Constants.CAMP_DATABASE_NAME
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