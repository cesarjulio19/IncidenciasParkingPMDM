package com.example.incidenciasparkingpmdm

import com.example.incidenciasparkingpmdm.api.CsrfInterceptor
import com.example.incidenciasparkingpmdm.api.IncidentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    const val baseURL = "http://192.168.1.60:8080/"
    @Provides
    @Singleton
    fun provideIncidentService(interceptor: CsrfInterceptor): IncidentService {
        return IncidentService(interceptor)
    }

    /*@Provides
    fun provideRetrofit(csrfInterceptor: CsrfInterceptor):Retrofit {
        return Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(csrfInterceptor)
                .build())
            .build()
    }*/

    @Provides
    @Singleton
    fun provideInterceptor(/*@Named("token") tokenDeferred: Deferred<String>*/): CsrfInterceptor {
        //val token = runBlocking { tokenDeferred.await() }
        val email = "manolo72@gmail.com"
        val password = "1234"
        val credentials = basic(email, password)
        return CsrfInterceptor(credentials/*, token*/)
    }

    /*@Provides
    @Singleton
    @Named("token")
    fun provideToken(retrofit: Retrofit):Deferred<String> = GlobalScope.async(Dispatchers.IO) {
        try {
            val api = retrofit.create(IncidentApi::class.java)
            val response = api.getCsrf().execute()
            response.body()?.token?: ""
        } catch (e: IOException) {
            Log.e("ERROR", e.message.toString())
            ""
        }
    }

    @Provides
    @Singleton
    fun provideRetrofitNoToken(): Retrofit {
        return Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }*/
}