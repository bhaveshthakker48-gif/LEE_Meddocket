package org.impactindiafoundation.iifllemeddocket.architecture.apiCall

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor (val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        return chain.proceed(request.build())
    }
}