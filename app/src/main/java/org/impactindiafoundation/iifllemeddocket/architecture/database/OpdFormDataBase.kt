package org.impactindiafoundation.iifllemeddocket.architecture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.PatientMedicineTypeConverter
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OpdSyncDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.PrescriptionsDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.OpdSyncTable
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine

/**
 * Created by JOSUS PRAISER on 30-01-2025.
 * This DataBase is for OPD Prescriptions not of Volunteer Login - For OPD Login
 */
@Database(entities = [PatientMedicine::class, OpdSyncTable::class], version = 4)
@TypeConverters(PatientMedicineTypeConverter::class)
abstract class OpdFormDataBase : RoomDatabase() {

    abstract fun prescriptionsDao(): PrescriptionsDao
    abstract fun opdSyncDao(): OpdSyncDao

    companion object {
        @Volatile
        private var INSTANCE: OpdFormDataBase? = null

        fun getDatabase(context: Context): OpdFormDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OpdFormDataBase::class.java,
                    Constants.OPD_FORM_DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE PatientMedicine ADD COLUMN patient_geneder TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE PatientMedicine ADD COLUMN patient_age INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE OpdSyncTable ADD COLUMN unsyncFormCount INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}
