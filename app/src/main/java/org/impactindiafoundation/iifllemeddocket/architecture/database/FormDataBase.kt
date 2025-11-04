package org.impactindiafoundation.iifllemeddocket.architecture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OPDFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.PatientReportDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.RefractiveErrorFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.VisualAcuityFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.VitalsFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport

/**
 * Created by JOSUS PRAISER on 14-10-2024.
 * VERY IMPORTANT NOTE - If updating the db by adding or removing any field or table, please update DB version and add update notes
 */
//Latest DB Version 4 - added isSynced field in patient report.
@Database(entities = [RefractiveError::class, Vitals::class, OPD_Investigations::class, VisualAcuity::class, PatientReport::class], version = 5)
abstract class FormDataBase : RoomDatabase() {

    abstract fun refractiveFormDao(): RefractiveErrorFormDao
    abstract fun vitalsFormDao(): VitalsFormDao
    abstract fun opdFormDao(): OPDFormDao
    abstract fun visualAcuityFormDao(): VisualAcuityFormDao
    abstract fun patientReportDao(): PatientReportDao

    companion object {
        @Volatile
        private var INSTANCE: FormDataBase? = null

        fun getDatabase(context: Context): FormDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FormDataBase::class.java,
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