package com.example.personaltracking.di

import android.content.Context
import android.location.Geocoder
import androidx.room.Room
import com.example.personaltracking.data.local.LocationDatabase
import com.example.personaltracking.data.local.dao.LocationDao
import com.example.personaltracking.data.repository.LocationRepositoryImpl
import com.example.personaltracking.domain.repository.LocationRepository
import com.example.personaltracking.domain.usecase.LocationUpdatesUseCase
import com.example.personaltracking.domain.usecase.ObserveLocationsUseCase
import com.example.personaltracking.domain.usecase.SaveLocationUseCase
import com.example.personaltracking.location.LocationDataSource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ---------------------------
    // DATABASE
    // ---------------------------
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocationDatabase {
        return Room.databaseBuilder(
            context,
            LocationDatabase::class.java,
            "location_db"
        ).build()
    }

    @Provides
    fun provideLocationDao(db: LocationDatabase): LocationDao = db.locationDao()

    // ---------------------------
    // LOCATION + GEOCODER
    // ---------------------------
    @Provides
    @Singleton
    fun provideFusedLocationProvider(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder =
        Geocoder(context, Locale.getDefault())

    @Provides
    @Singleton
    fun provideLocationDataSource(
        fusedClient: FusedLocationProviderClient
    ): LocationDataSource = LocationDataSource(fusedClient)

    // ---------------------------
    // REPOSITORY
    // ---------------------------
    @Provides
    @Singleton
    fun provideLocationRepository(
        dataSource: LocationDataSource,
        dao: LocationDao,
        geocoder: Geocoder
    ): LocationRepository = LocationRepositoryImpl(
        dataSource = dataSource,
        dao = dao,
        geocoder = geocoder
    )

    // ---------------------------
    // USE CASES
    // ---------------------------
    @Provides
    @Singleton
    fun provideSaveLocationUseCase(
        repo: LocationRepository
    ): SaveLocationUseCase = SaveLocationUseCase(repo)

    @Provides
    @Singleton
    fun provideObserveLocationsUseCase(
        repo: LocationRepository
    ): ObserveLocationsUseCase = ObserveLocationsUseCase(repo)

    @Provides
    @Singleton
    fun provideLocationUpdatesUseCase(
        repo: LocationRepository
    ): LocationUpdatesUseCase = LocationUpdatesUseCase(repo)

}
