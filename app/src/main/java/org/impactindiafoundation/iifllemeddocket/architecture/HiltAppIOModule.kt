package org.impactindiafoundation.iifllemeddocket.architecture

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.impactindiafoundation.iifllemeddocket.architecture.apiCall.APIClient
import org.impactindiafoundation.iifllemeddocket.architecture.apiCall.AuthInterceptor
import org.impactindiafoundation.iifllemeddocket.architecture.dao.CurrentInventoryDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OPDFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OpdSyncDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.OrthosisEquipmentMasterDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.PatientReportDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.PrescriptionsDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.RefractiveErrorFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.UserDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.VisualAcuityFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.dao.VitalsFormDao
import org.impactindiafoundation.iifllemeddocket.architecture.database.CampDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.CampPatientDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.EntDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.FormDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.InventoryDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.OpdFormDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.OrthosisFileDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.OrthosisFormDataBase
import org.impactindiafoundation.iifllemeddocket.architecture.database.UserDatabase
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltAppIOModule {

    @Provides
    fun provideBaseUrl() = Constants.NEW_BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context) = run {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = AuthInterceptor(context)
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(ChuckerInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()
    @Provides
    @Singleton
    fun provideAPIClient(retrofit: Retrofit) = retrofit.create(APIClient::class.java)!!
    @Provides
    fun provideUserDao(@ApplicationContext appContext: Context) : UserDatabase {
        return UserDatabase.getDatabase(appContext)
    }

    @Provides
    fun provideOrthosisFormDB(@ApplicationContext appContext: Context) : OrthosisFormDataBase {
        return OrthosisFormDataBase.getDatabase(appContext)
    }

    @Provides
    fun provideCampPatientsDB(@ApplicationContext appContext: Context) : CampPatientDataBase {
        return CampPatientDataBase.getDatabase(appContext)
    }

    @Provides
    fun provideFormDB(@ApplicationContext appContext: Context) : FormDataBase {
        return FormDataBase.getDatabase(appContext)
    }

    @Provides
    fun provideOrthosisFileDB(@ApplicationContext appContext: Context) : OrthosisFileDataBase {
        return OrthosisFileDataBase.getDatabase(appContext)
    }

    @Provides
    fun provideCampDB(@ApplicationContext appContext: Context) : CampDataBase {
        return CampDataBase.getDatabase(appContext)
    }

    @Provides
    fun provideInventoryDB(@ApplicationContext appContext: Context) : InventoryDataBase {
        return InventoryDataBase.getDatabase(appContext)
    }

    @Provides
    fun provideCurrentInventoryDao(@ApplicationContext appContext: Context): CurrentInventoryDao {
        return InventoryDataBase.getDatabase(appContext).currentInventoryDao()
    }

    @Provides
    fun provideRefractiveErrorFormDao(@ApplicationContext appContext: Context): RefractiveErrorFormDao {
        return FormDataBase.getDatabase(appContext).refractiveFormDao()
    }

    @Provides
    fun provideEquipmentMasterDao(@ApplicationContext appContext: Context): OrthosisEquipmentMasterDao {
        return UserDatabase.getDatabase(appContext).orthosisEquipmentMasterDao()
    }

    @Provides
    fun provideVitalsFormDao(@ApplicationContext appContext: Context): VitalsFormDao {
        return FormDataBase.getDatabase(appContext).vitalsFormDao()
    }

    @Provides
    fun provideOpdFormDao(@ApplicationContext appContext: Context): OPDFormDao {
        return FormDataBase.getDatabase(appContext).opdFormDao()
    }

    @Provides
    fun provideVisualAcuityFormDao(@ApplicationContext appContext: Context): VisualAcuityFormDao {
        return FormDataBase.getDatabase(appContext).visualAcuityFormDao()
    }

    @Provides
    fun providePatientReportDao(@ApplicationContext appContext: Context): PatientReportDao {
        return FormDataBase.getDatabase(appContext).patientReportDao()
    }

    @Provides
    fun provideOpdPrescriptionDao(@ApplicationContext appContext: Context): PrescriptionsDao {
        return OpdFormDataBase.getDatabase(appContext).prescriptionsDao()
    }

    @Provides
    fun provideOpdSyncDao(@ApplicationContext appContext: Context): OpdSyncDao {
        return OpdFormDataBase.getDatabase(appContext).opdSyncDao()
    }

    @Provides
    fun provideOpdDB(@ApplicationContext appContext: Context) : OpdFormDataBase {
        return OpdFormDataBase.getDatabase(appContext)
    }

    @Provides
    fun provideEntDb(@ApplicationContext appContext: Context) : EntDataBase {
        return EntDataBase.getDatabase(appContext)
    }
}