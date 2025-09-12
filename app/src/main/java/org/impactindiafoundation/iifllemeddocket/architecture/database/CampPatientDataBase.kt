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
import org.impactindiafoundation.iifllemeddocket.architecture.dao.CampPatientDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OrthosisFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm

/**
 * Created by JOSUS PRAISER on 15-10-2024.
 */

/**
 * DB Update Notes - VERY IMPORTANT, could crash and make unstable the app in live.
 * Latest DB Version 2 - updating field names similar to server response for equipment_status_notes, equipment_status etc .
 * Note - if updating the db by adding or removing any field or table, please update DB version and add update notes
 */
@Database(entities = [CampPatientDataItem::class], version = 2)
@TypeConverters(
    OrthosisTypeListConvertor::class, OrthosisTypeConvertor::class,
    MeasurementDataListConvertor::class, MeasurementConverter::class,
)
abstract class CampPatientDataBase : RoomDatabase() {

    abstract fun campPatientDao(): CampPatientDao

    companion object {
        @Volatile
        private var INSTANCE: CampPatientDataBase? = null

        fun getDatabase(context: Context): CampPatientDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CampPatientDataBase::class.java,
                    Constants.CAMP_PATIENT_DATABASE_NAME
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