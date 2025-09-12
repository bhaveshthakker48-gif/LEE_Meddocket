package org.impactindiafoundation.iifllemeddocket.architecture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PrescriptionItemTypeConverter
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
@Database(entities = [PatientMedicine::class, OpdSyncTable::class], version = 1)
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
                    .addMigrations(*migrations)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val migrations = arrayOf<Migration>()

    }
}