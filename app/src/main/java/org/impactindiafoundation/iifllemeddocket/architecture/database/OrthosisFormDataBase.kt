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
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OrthosisFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OrthosisSyncDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisSynTable

/**
 * Created by JOSUS PRAISER on 14-10-2024.
 */
@Database(entities = [OrthosisPatientForm::class, OrthosisSynTable::class], version = 6)
@TypeConverters(
    OrthosisTypeListConvertor::class, OrthosisTypeConvertor::class,
    MeasurementDataListConvertor::class, MeasurementConverter::class,
)
abstract class OrthosisFormDataBase : RoomDatabase() {

    abstract fun orthosisFormDao(): OrthosisFormDao

    abstract fun orthosisSyncDao() : OrthosisSyncDao

    companion object {
        @Volatile
        private var INSTANCE: OrthosisFormDataBase? = null

        fun getDatabase(context: Context): OrthosisFormDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrthosisFormDataBase::class.java,
                    Constants.ORTHOSIS_FORM_DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}