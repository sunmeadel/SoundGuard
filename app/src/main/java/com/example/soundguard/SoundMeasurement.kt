package com.example.soundguard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "sound_measurements")
data class SoundMeasurement(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "decibel_level") val decibelLevel: Float,
    @ColumnInfo(name = "duration_seconds") val durationSeconds: Long,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "latitude") val latitude: Double? = null,
    @ColumnInfo(name = "longitude") val longitude: Double? = null,
    @ColumnInfo(name = "user_id") val userId: Long, // Foreign key to users table
    @ColumnInfo(name = "is_synced") val isSynced: Boolean = false
)