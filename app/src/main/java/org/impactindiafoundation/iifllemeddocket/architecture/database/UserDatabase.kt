package org.impactindiafoundation.iifllemeddocket.architecture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.DiagnosisTypeConvertor
import org.impactindiafoundation.iifllemeddocket.Utils.typeConvertors.MeasurementListConvertor
import org.impactindiafoundation.iifllemeddocket.architecture.dao.DiagnosisMasterDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OrthosisEquipmentMasterDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OrthosisMasterDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.UserDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.EntEarSymptomsDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.EntImpressionDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.EntNoseSymptomsDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.entdao.EntThroatSymptomsDao
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.model.DiagnosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.Equipment
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntEarType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntNoseType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntThroatType
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.ImpressionType

/**
 * Created by JOSUS PRAISER on 07-10-2024.
 */
@Database(entities = [UserModel::class,OrthosisType::class,DiagnosisType::class, Equipment::class, EntEarType::class, EntNoseType::class, EntThroatType::class, ImpressionType::class], version = 4)
@TypeConverters(
    MeasurementListConvertor::class,DiagnosisTypeConvertor::class
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun orthosisMasterDao(): OrthosisMasterDao
    abstract fun diagnosisMasterDao(): DiagnosisMasterDao
    abstract fun orthosisEquipmentMasterDao(): OrthosisEquipmentMasterDao
    abstract fun entEarSymptomsDao() : EntEarSymptomsDao
    abstract fun entNoseSymptomsDao() : EntNoseSymptomsDao
    abstract fun entThroatSymptomsDao() : EntThroatSymptomsDao
    abstract fun entImpressionDao() : EntImpressionDao




    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    Constants.USER_DATABASE_NAME
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