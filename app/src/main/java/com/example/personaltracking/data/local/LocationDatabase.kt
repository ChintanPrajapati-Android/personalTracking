package com.example.personaltracking.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.personaltracking.data.local.dao.LocationDao
import com.example.personaltracking.data.local.entity.TrackedLocationEntity

@Database(
    entities = [TrackedLocationEntity::class],
    version = 1
)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}
