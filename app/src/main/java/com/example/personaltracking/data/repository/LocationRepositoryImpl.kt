package com.example.personaltracking.data.repository

import android.location.Geocoder
import android.location.Location
import android.os.Build
import com.example.personaltracking.data.local.dao.LocationDao
import com.example.personaltracking.data.mapper.toDomain
import com.example.personaltracking.data.mapper.toEntity
import com.example.personaltracking.domain.model.DayWiseLocations
import com.example.personaltracking.domain.model.TrackedLocation
import com.example.personaltracking.domain.repository.LocationRepository
import com.example.personaltracking.location.LocationDataSource
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationRepositoryImpl @Inject constructor(
    private val dataSource: LocationDataSource,
    private val dao: LocationDao,
    private val geocoder: Geocoder
) : LocationRepository {

    override fun locationUpdates(): Flow<Location> =
        dataSource.locationUpdates()

    override fun observeSavedLocations(): Flow<List<TrackedLocation>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeTodaySavedLocations(): Flow<List<TrackedLocation>> =
        dao.observeToday(startOfTodayMillis()).map { list -> list.map { it.toDomain() } }

    override fun observeLocationsByDate(): Flow<List<DayWiseLocations>> {
        return dao.observeAll().map { list ->
           val smp = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            list.groupBy { smp.format(it.timestamp)}
                .map { (date, items) ->
                    val sorted = items.sortedBy { it.timestamp }
                    DayWiseLocations(
                        date = date,
                        data = items,
                        source = sorted.firstOrNull()?.address,
                        destination = sorted.lastOrNull()?.address
                    )
                }
                .sortedByDescending { it.date }
        }
    }

    override suspend fun saveIfAccurate(location: Location) {
        if (location.accuracy > 10f) return
        val address = getAddress(geocoder, location.latitude, location.longitude)
        val tracked = TrackedLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = location.altitude,
            accuracy = location.accuracy,
            address = address,
            timestamp = System.currentTimeMillis()
        )

        dao.insert(tracked.toEntity())
    }

    override suspend fun clear() {
        dao.clear()
    }

    suspend fun getAddress(
        geocoder: Geocoder,
        lat: Double,
        lng: Double
    ): String? = suspendCancellableCoroutine { continuation ->

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lng, 1) { list ->
                val address = list.firstOrNull()?.getAddressLine(0)
                if (!continuation.isCompleted) {
                    continuation.resume(address) { cause, _, _ ->
                        // cancellation callback
                    }
                }
            }
        } else {
            try {
                val list = geocoder.getFromLocation(lat, lng, 1)
                val address = list?.firstOrNull()?.getAddressLine(0)
                if (!continuation.isCompleted) {
                    continuation.resume(address) { cause, _, _ -> }
                }
            } catch (e: Exception) {
                if (!continuation.isCompleted) {
                    continuation.resume(null) { cause, _, _ -> }
                }
            }
        }
    }

    fun startOfTodayMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
