package com.example.personaltracking.domain.model

import com.example.personaltracking.data.local.entity.TrackedLocationEntity

data class DayWiseLocations(
    val date: String ?= null,
    val data: List<TrackedLocationEntity>?= null,
    val source: String? = null,
    val destination: String? = null
)