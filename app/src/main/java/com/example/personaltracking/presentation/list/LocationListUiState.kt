package com.example.personaltracking.presentation.list

import com.example.personaltracking.domain.model.DayWiseLocations


data class LocationListUiState(
    val daysWiselocations: List<DayWiseLocations> = emptyList()
)
