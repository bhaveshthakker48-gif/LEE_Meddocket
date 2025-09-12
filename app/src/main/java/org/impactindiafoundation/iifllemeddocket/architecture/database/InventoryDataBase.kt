package org.impactindiafoundation.iifllemeddocket.architecture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CurrentInventoryLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.InventoryUnitLocal
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.MeasurementConverter
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.MeasurementDataListConvertor
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.OrthosisTypeConvertor
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.OrthosisTypeListConvertor
import org.impactindiafoundation.iifllemeddocket.architecture.dao.CurrentInventoryDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OrthosisFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm

/**
 * Created by JOSUS PRAISER on 06-12-2024.
 */
@Database(entities = [CurrentInventoryLocal::class,
    InventoryUnitLocal::class,], version = 3)
abstract class InventoryDataBase : RoomDatabase() {

    abstract fun currentInventoryDao(): CurrentInventoryDao

    companion object {
        @Volatile
        private var INSTANCE: InventoryDataBase? = null

        fun getDatabase(context: Context): InventoryDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventoryDataBase::class.java,
                    Constants.INVENTORY_DATABASE_NAME
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