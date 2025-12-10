package com.example.personaltracking.domain.model

data class TrackedLocation(
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double?,
    val accuracy: Float,
    val address: String?,
    val timestamp: Long
)
