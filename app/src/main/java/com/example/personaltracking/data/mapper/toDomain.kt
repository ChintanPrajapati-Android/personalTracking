package com.example.personaltracking.data.mapper

import com.example.personaltracking.data.local.entity.TrackedLocationEntity
import com.example.personaltracking.domain.model.TrackedLocation


fun TrackedLocationEntity.toDomain() = TrackedLocation(
    id = id,
    latitude = latitude,
    longitude = longitude,
    altitude = altitude,
    accuracy = accuracy,
    address = address,
    timestamp = timestamp
)

fun TrackedLocation.toEntity() = TrackedLocationEntity(
    id = id,
    latitude = latitude,
    longitude = longitude,
    altitude = altitude,
    accuracy = accuracy,
    address = address,
    timestamp = timestamp
)
