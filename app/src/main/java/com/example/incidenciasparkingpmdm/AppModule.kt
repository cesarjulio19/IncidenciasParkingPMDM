package com.example.incidenciasparkingpmdm

import com.example.incidenciasparkingpmdm.api.IncidentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideIncidentService(): IncidentService {
        return IncidentService()
    }
}