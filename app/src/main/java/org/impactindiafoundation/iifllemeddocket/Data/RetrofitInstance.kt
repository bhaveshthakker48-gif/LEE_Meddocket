package org.impactindiafoundation.iifllemeddocket.Data

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder

import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp

class RetrofitInstance {

    companion object {
        private val gson = GsonBuilder()
            .setLenient()
            .create()

        private val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        private fun createRetrofit(baseUrl: String): Retrofit {
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        private val localRetrofit by lazy {
           createRetrofit(ConstantsApp.BASE_URL)
        }

        val localApi: LLE_MedDocketAPI by lazy {
            localRetrofit.create(LLE_MedDocketAPI::class.java)
        }
    }
}
