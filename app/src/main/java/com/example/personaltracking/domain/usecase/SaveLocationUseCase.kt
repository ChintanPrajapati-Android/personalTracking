package com.example.personaltracking.domain.usecase

import android.location.Location
import com.example.personaltracking.domain.repository.LocationRepository
import javax.inject.Inject

class SaveLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(location: Location) {
        repository.saveIfAccurate(location)
    }
}
