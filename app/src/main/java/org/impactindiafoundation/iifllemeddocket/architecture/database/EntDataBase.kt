package org.impactindiafoundation.iifllemeddocket.architecture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.impactindiafoundation.iifllemeddocket.architecture.dao.PrescriptionPatientReportDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.AudiometryDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.AudiometryImageDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.DoctorNoteInvestigationDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.EntImpressionListDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.EntPatientReportDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.EntPostOPNotesDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.EntSymptomDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.PreOpDetailsDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.PreOpImageDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.SurgicalNotesDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.pathologydao.PathalogyDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.pathologydao.PathologyImageDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.AudiometryImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.DoctorNoteInvestigationEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntImpressionEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPostOpNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPreOpDetailsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntSymptomsEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.PreOpImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.SurgicalNotesEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyImageEntity
import org.impactindiafoundation.iifllemeddocket.architecture.model.pathalogydatabasemodel.PathologyEntity

@Database(entities = [EntSymptomsEntity::class, EntImpressionEntity::class, DoctorNoteInvestigationEntity::class, EntPreOpDetailsEntity::class, SurgicalNotesEntity::class, EntPostOpNotesEntity::class, AudiometryEntity::class, AudiometryImageEntity::class, EntPatientReport::class, PathologyEntity::class, PathologyImageEntity::class, PreOpImageEntity::class, PrescriptionPatientReport::class], version = 3)
abstract class EntDataBase : RoomDatabase() {
    abstract fun entSymptomsDao(): EntSymptomDao
    abstract fun entImpressionListDao(): EntImpressionListDao
    abstract fun doctorNoteInvestigationDao(): DoctorNoteInvestigationDao
    abstract fun preOpDetailsDao(): PreOpDetailsDao
    abstract fun preOpImageDao(): PreOpImageDao
    abstract fun surgicalNotesDao(): SurgicalNotesDao
    abstract fun entPostOPNotesDao(): EntPostOPNotesDao
    abstract fun audimetryDao(): AudiometryDao
    abstract fun audimetryImageDao(): AudiometryImageDao
    abstract fun entPatientReportDao(): EntPatientReportDao
    abstract fun pathologyImageDao(): PathologyImageDao
    abstract fun pathology(): PathalogyDao
    abstract fun prescriptionPatientReportDao(): PrescriptionPatientReportDao


    companion object {
        @Volatile
        private var INSTANCE: EntDataBase? = null

        fun getDatabase(context: Context): EntDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EntDataBase::class.java,
                    Constants.ENT_DATABASE_NAME
                )
                    .addMigrations(*migrations)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `prescription_patient_report` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `formId` INTEGER NOT NULL,
                `patientFname` TEXT NOT NULL,
                `patientLname` TEXT NOT NULL,
                `patientId` INTEGER NOT NULL,
                `patientGen` TEXT NOT NULL,
                `patientAge` INTEGER NOT NULL,
                `camp_id` INTEGER NOT NULL,
                `AgeUnit` TEXT NOT NULL,
                `location` TEXT NOT NULL,
                `isSyn` INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
                )
            }
        }


        val migrations = arrayOf(MIGRATION_1_2)
    }
}