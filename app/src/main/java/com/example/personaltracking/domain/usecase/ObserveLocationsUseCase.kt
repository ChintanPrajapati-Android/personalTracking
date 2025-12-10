package com.example.personaltracking.domain.usecase

import com.example.personaltracking.domain.repository.LocationRepository
import javax.inject.Inject

class ObserveLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    fun getLocations() = repository.observeSavedLocations()

    fun getTodaysLocations() = repository.observeTodaySavedLocations()

    fun getDayWiseLocations() = repository.observeLocationsByDate()
}
