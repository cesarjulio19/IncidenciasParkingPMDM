package com.example.incidenciasparkingpmdm

import android.util.Log
import com.example.incidenciasparkingpmdm.api.IncidentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
     fun provideIncidentService(): IncidentService {
        return IncidentService()
    }

   /* @Provides
    @Singleton
    @Named("token")
     fun getToken(): String {
        return try {
            val response = provideIncidentServiceWithoutToken().apiSinToken.getCsrfToken().execute()
            response.body()?.token ?: ""
        } catch (e: IOException) {
            Log.e("ERROR", e.message.toString())
            ""
        }
        /*var token = ""
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = provideIncidentService().apiSinToken.getCsrfToken().execute()
                token = response.body()!!.token
            } catch (e: IOException) {
                Log.e("ERROR", e.message.toString())
            }
        }
        return token*/
    }

    @Provides
    @Singleton
    fun provideIncidentServiceWithoutToken(): IncidentService {
        return IncidentService("")
    }*/
}