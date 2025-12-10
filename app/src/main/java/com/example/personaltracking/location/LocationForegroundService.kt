package com.example.personaltracking.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.personaltracking.MainActivity
import com.example.personaltracking.R
import com.example.personaltracking.domain.usecase.LocationUpdatesUseCase
import com.example.personaltracking.domain.usecase.SaveLocationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationForegroundService : Service() {

    @Inject
    lateinit var locationUpdatesUseCase: LocationUpdatesUseCase

    @Inject
    lateinit var saveLocationUseCase: SaveLocationUseCase

    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private var trackingJob: Job? = null

    private val CHANNEL_ID = "location_tracking_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildNotification("Tracking started"))

        startLocationTracking()
    }

    private fun startLocationTracking() {
        trackingJob = serviceScope.launch {
            locationUpdatesUseCase().collectLatest { location ->
                saveLocationUseCase(location)
            }
        }
    }

    private fun stopLocationTracking() {
        trackingJob?.cancel()
        trackingJob = null
    }

    override fun onDestroy() {
        stopLocationTracking()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ----------------------------
    // Foreground Notification Setup
    // ----------------------------

    private fun buildNotification(content: String): Notification {

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location tracking active")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // your icon
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Location Tracking",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    // ----------------------------
    // START + STOP HELPERS
    // ----------------------------

    companion object {
        fun startService(context: android.content.Context) {
            val intent = Intent(context, LocationForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: android.content.Context) {
            val intent = Intent(context, LocationForegroundService::class.java)
            context.stopService(intent)
        }
    }
}
