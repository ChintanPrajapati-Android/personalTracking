package com.example.personaltracking.presentation.map

import com.example.personaltracking.domain.model.TrackedLocation
import com.google.maps.android.compose.MapType

data class MapUiState(
    val locations: List<TrackedLocation> = emptyList(),
    val mapType: MapType = MapType.NORMAL
) {
    val currentLocation: TrackedLocation?
        get() = locations.firstOrNull()
}
