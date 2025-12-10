package com.example.personaltracking.data.local.dao

import androidx.room.*
import com.example.personaltracking.data.local.entity.TrackedLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: TrackedLocationEntity)

    @Query("SELECT * FROM tracked_location ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<TrackedLocationEntity>>

    @Query("SELECT * FROM tracked_location WHERE timestamp >= :startOfDay ORDER BY timestamp DESC")
    fun observeToday(startOfDay: Long): Flow<List<TrackedLocationEntity>>

    @Query("DELETE FROM tracked_location")
    suspend fun clear()
}
