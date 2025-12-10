package com.example.personaltracking.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltracking.domain.usecase.ObserveLocationsUseCase
import com.example.personaltracking.presentation.navigation.MapScreenActions
import com.google.maps.android.compose.MapType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    observeLocationsUseCase: ObserveLocationsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MapUiState())
    val state: StateFlow<MapUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<MapScreenActions>()
    val actions = _actions.asSharedFlow()

    init {
        // Observe DB changes immediately
        viewModelScope.launch {
            observeLocationsUseCase.getTodaysLocations()
                .map { locations -> locations.sortedByDescending { it.timestamp } }
                .collect { sorted ->
                    _state.update { it.copy(locations = sorted) }
                }
        }
    }

    fun onListClick() {
        viewModelScope.launch {
            _actions.emit(MapScreenActions.OpenListScreen)
        }
    }

    fun updateMapType(type: MapType) {
        _state.update { it.copy(mapType = type) }
    }
}
