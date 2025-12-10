package com.example.personaltracking.domain.repository

import android.location.Location
import com.example.personaltracking.domain.model.DayWiseLocations
import com.example.personaltracking.domain.model.TrackedLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun locationUpdates(): Flow<Location>

    fun observeSavedLocations(): Flow<List<TrackedLocation>>

    suspend fun saveIfAccurate(location: Location)

    suspend fun clear()

    fun observeTodaySavedLocations(): Flow<List<TrackedLocation>>

    fun observeLocationsByDate(): Flow<List<DayWiseLocations>>
}
