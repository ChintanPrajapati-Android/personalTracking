package com.example.personaltracking.presentation.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.personaltracking.location.LocationForegroundService
import com.example.personaltracking.permissions.PermissionScreen
import com.example.personaltracking.presentation.navigation.MapScreenActions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onAction: (MapScreenActions) -> Unit
) {
    var permissionsGranted by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            onAction(action)
        }
    }

    if (!permissionsGranted) {
        PermissionScreen(
            onSuccess = { permissionsGranted = true }
        )
    } else {
        MapContent(
            viewModel = viewModel,
            onOpenList = viewModel::onListClick
        )
    }
}

@Composable
private fun MapContent(
    viewModel: MapViewModel,
    onOpenList: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var isMapReady by remember { mutableStateOf(false) }

    val currentLocation = state.currentLocation
    val pathPoints = state.locations.map {
        LatLng(it.latitude, it.longitude)
    }

    val camera = rememberCameraPositionState()

    // Move camera to current location
    LaunchedEffect(isMapReady , currentLocation) {
        if (isMapReady && currentLocation != null) {
            camera.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(currentLocation.latitude, currentLocation.longitude),
                    17f
                )
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Map Type Selector
        MapTypeSelector(
            currentType = state.mapType,
            onTypeSelected = viewModel::updateMapType
        )

        // Start / Stop Tracking Buttons
        TrackingControls()

        // Google Map
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = camera,
            properties = MapProperties(
                mapType = state.mapType,
                isMyLocationEnabled = true
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = true
            ),
            onMapLoaded = {
                isMapReady = true
            }
        ) {

            // Draw marker for current location
            currentLocation?.let {
                Marker(
                    state = MarkerState(
                        LatLng(it.latitude, it.longitude)
                    ),
                    title = "Current Location"
                )
            }

            // Draw path polyline
            if (pathPoints.size > 1) {
                Polyline(points = pathPoints)
            }
        }
    }

    Box(Modifier.fillMaxSize()) {

        // FAB to open list
        FloatingActionButton(
            onClick = onOpenList,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.List, contentDescription = "History List")
        }
    }
}

@Composable
fun MapTypeSelector(
    currentType: MapType,
    onTypeSelected: (MapType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf(
            MapType.NORMAL,
            MapType.SATELLITE,
            MapType.TERRAIN,
            MapType.HYBRID
        ).forEach { type ->
            FilterChip(
                selected = currentType == type,
                onClick = { onTypeSelected(type) },
                label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}

@Composable
fun TrackingControls() {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Button(
            onClick = { LocationForegroundService.startService(context) }
        ) {
            Text("Start Tracking")
        }

        OutlinedButton(
            onClick = { LocationForegroundService.stopService(context) }
        ) {
            Text("Stop Tracking")
        }
    }
}



