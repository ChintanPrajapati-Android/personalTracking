package com.example.personaltracking.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltracking.domain.usecase.ObserveLocationsUseCase
import com.example.personaltracking.presentation.navigation.LocationListActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor(
    observeLocationsUseCase: ObserveLocationsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LocationListUiState())
    val state: StateFlow<LocationListUiState> = _state.asStateFlow()


    private val _actions = MutableSharedFlow<LocationListActions>()
    val actions = _actions.asSharedFlow()

    init {
        viewModelScope.launch {
            observeLocationsUseCase.getDayWiseLocations()
                .collect { data ->
                    _state.value = LocationListUiState(data)
                }
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _actions.emit(LocationListActions.Back)
        }
    }
}
