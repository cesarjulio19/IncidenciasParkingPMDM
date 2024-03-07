package com.example.incidenciasparkingpmdm

import com.example.incidenciasparkingpmdm.api.AuthInterceptor
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
    const val baseURL = "http://192.168.54.213:8080/"
    @Provides
    @Singleton
    fun provideIncidentService(interceptor: AuthInterceptor): IncidentService {
        return IncidentService(interceptor)
    }
}