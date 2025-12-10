package com.example.personaltracking.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationDataSource @Inject constructor(
    private val fusedClient: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    fun locationUpdates(
        intervalMillis: Long = 5000L,
        fastestIntervalMillis: Long = 2000L
    ): Flow<Location> = callbackFlow {

        // Build request
        val request = LocationRequest.Builder(intervalMillis)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(fastestIntervalMillis)
            .build()

        // Callback listener
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    trySend(location)
                }
            }
        }

        // Start updates
        fusedClient.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )

        // Cleanup when Flow collector stops
        awaitClose {
            fusedClient.removeLocationUpdates(callback)
        }
    }
}
