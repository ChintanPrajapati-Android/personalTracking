package com.example.personaltracking.permissions

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

data class PermissionState(
    val allGranted: Boolean = false,
    val shouldShowRationale: Boolean = false
)

@Composable
fun rememberPermissionManager(
    onPermissionsResult: (PermissionState) -> Unit
): () -> Unit {

    // Build permission list dynamically
    val permissions = buildList {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }.toTypedArray()

    var showRationale by rememberSaveable { mutableStateOf(false) }

    // Launcher
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->

        val granted = resultMap.values.all { it }

        val rationale = resultMap.values.any { !it }

        onPermissionsResult(
            PermissionState(
                allGranted = granted,
                shouldShowRationale = rationale
            )
        )

        showRationale = rationale
    }

    return {
        launcher.launch(permissions)
    }
}
