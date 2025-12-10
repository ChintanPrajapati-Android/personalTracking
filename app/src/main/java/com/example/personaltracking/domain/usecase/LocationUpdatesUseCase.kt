package com.example.personaltracking.domain.usecase

import com.example.personaltracking.domain.repository.LocationRepository
import javax.inject.Inject


class LocationUpdatesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke() = repository.locationUpdates()
}
