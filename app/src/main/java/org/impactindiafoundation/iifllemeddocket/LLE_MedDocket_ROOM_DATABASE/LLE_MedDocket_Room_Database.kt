package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Cataract_Surgery_Notes_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.CreatePrescriptionDAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.CurrentInventory_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Eye_OPD_Doctors_Note_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Eye_Post_Op_AND_Follow_ups_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Eye_Pre_Op_Investigation_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Eye_Pre_Op_Notes_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.FinalPrescriptionDrugDAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Final_Prescription_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Image_Prescription_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Image_Upload_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.InventoryUnit_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.OPD_Investigations_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.PatientDao
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Prescription_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Refractive_Error_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Registration_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.SpectacleDisdributionStatus_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.SynTable_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.VisualAcuity_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DAO.Vital_DAO
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Cataract_Surgery_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CurrentInventoryLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_OPD_Doctors_Note
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Post_Op_AND_Follow_ups
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Investigation
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Notes
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImageModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.ImagePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.InventoryUnitLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Patient_RegistrationModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PrescriptionGlassesFinal
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PrescriptionItemTypeConverter
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Prescription_Model
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SpectacleDisdributionStatusModel
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals

@Database(entities = [Vitals::class,
    VisualAcuity::class,
    RefractiveError::class,
    OPD_Investigations::class,
    Eye_Pre_Op_Notes::class,
    Eye_Pre_Op_Investigation::class,
    Eye_Post_Op_AND_Follow_ups::class,
    Eye_OPD_Doctors_Note::class,
    Cataract_Surgery_Notes::class,
    PatientDataLocal::class,
    ImageModel::class,
    Patient_RegistrationModel::class,
    Prescription_Model::class,
    PrescriptionGlassesFinal::class,
    SpectacleDisdributionStatusModel::class,
                     SynTable::class,
    CurrentInventoryLocal::class,
                     InventoryUnitLocal::class,
    CreatePrescriptionModel::class,
    ImagePrescriptionModel::class,
    FinalPrescriptionDrug::class
                     ], version = 4, exportSchema = false)
@TypeConverters(PrescriptionItemTypeConverter::class) // Add TypeConverter here
abstract class LLE_MedDocket_Room_Database : RoomDatabase() {

    abstract fun Vital_DAO(): Vital_DAO
    abstract fun VisualAcuity_DAO(): VisualAcuity_DAO
    abstract fun Refractive_Error_DAO(): Refractive_Error_DAO
    abstract fun OPD_Investigations_DAO(): OPD_Investigations_DAO
    abstract fun Eye_Pre_Op_Notes_DAO(): Eye_Pre_Op_Notes_DAO
    abstract fun Eye_Pre_Op_Investigation_DAO(): Eye_Pre_Op_Investigation_DAO
    abstract fun Eye_Post_Op_AND_Follow_ups_DAO(): Eye_Post_Op_AND_Follow_ups_DAO
    abstract fun Eye_OPD_Doctors_Note_DAO(): Eye_OPD_Doctors_Note_DAO
    abstract fun Cataract_Surgery_Notes_DAO(): Cataract_Surgery_Notes_DAO
    abstract fun PatientDao():PatientDao
    abstract fun Image_Upload_DAO():Image_Upload_DAO
    abstract fun Registration_DAO(): Registration_DAO
    abstract fun Prescription_DAO(): Prescription_DAO
    abstract fun Final_Prescription_DAO():Final_Prescription_DAO
    abstract fun SpectacleDisdributionStatus_DAO(): SpectacleDisdributionStatus_DAO
    abstract fun SynTable_DAO(): SynTable_DAO
    abstract fun CurrentInventory_DAO(): CurrentInventory_DAO
    abstract fun InventoryUnit_DAO():InventoryUnit_DAO
    abstract fun CreatePrescriptionDAO(): CreatePrescriptionDAO
    abstract fun Image_Prescription_DAO(): Image_Prescription_DAO
    abstract fun FinalPrescriptionDrugDAO(): FinalPrescriptionDrugDAO





    companion object {
        @Volatile
        private var database: LLE_MedDocket_Room_Database? = null


        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic from version 1 to 2
                // Update the schema as needed



                database.execSQL("ALTER TABLE CreatePrescriptionModel ADD COLUMN device_id TEXT")
                database.execSQL("ALTER TABLE CreatePrescriptionModel ADD COLUMN device_name TEXT")
                database.execSQL("ALTER TABLE CreatePrescriptionModel ADD COLUMN patient_name TEXT DEFAULT NULL")




            }
        }

      /*  private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic from version 1 to 2
                // Update the schema as needed





            }
        }*/
     /*   private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic from version 1 to 2
                // Update the schema as needed



                database.execSQL("ALTER TABLE CreatePrescriptionModel ADD COLUMN device_name TEXT")

            }
        }*/







        fun getDatabase(context: Context): LLE_MedDocket_Room_Database {
            return database ?: synchronized(this) {
                database ?: buildDatabase(context).also { database = it }
            }
        }

        private fun buildDatabase(context: Context): LLE_MedDocket_Room_Database {
            return Room.databaseBuilder(
                context.applicationContext,
                LLE_MedDocket_Room_Database::class.java, "LLE_MedDocket_Room_Database"
            ).addMigrations(MIGRATION_1_2).fallbackToDestructiveMigration()
                .build()
        }


    }

    // Add a suspend function to perform database operations on a background thread
    suspend fun performDatabaseOperation(operation: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            operation()
        }
    }

    suspend fun <T> performDatabaseOperations(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }


    suspend fun <T> performDatabaseOperation5(databaseOperation: () -> LiveData<T>): LiveData<T> {
        return withContext(Dispatchers.IO) {
            databaseOperation.invoke()
        }
    }

    suspend fun <T> performDatabaseOperation1(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            block.invoke()
        }
    }

    suspend fun <T> performDatabaseOperation2(operation: suspend () -> T): T {
        return operation()
    }

    suspend fun <T> performDatabaseOperation3(
        operation: suspend () -> T
    ): T {
        return withContext(Dispatchers.IO) {
            try {
                operation()
            } catch (e: Exception) {
                throw RuntimeException("Database operation failed: ${e.message}")
            }
        }
    }

    suspend fun <T> performDatabaseOperation6(databaseOperation: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            databaseOperation.invoke()
        }
    }


}

