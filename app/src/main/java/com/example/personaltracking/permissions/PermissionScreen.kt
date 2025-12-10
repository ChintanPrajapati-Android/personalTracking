package com.example.personaltracking.permissions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun PermissionScreen(
    onSuccess: () -> Unit
) {
    var permissionState by remember { mutableStateOf(PermissionState()) }

    val requestPermissions = rememberPermissionManager { state ->
        permissionState = state
    }

    LaunchedEffect(Unit) {
        requestPermissions()
    }

    when {
        permissionState.allGranted -> onSuccess()

        permissionState.shouldShowRationale -> {
            Text("Location permission is required to track your path.")
        }
    }
}
