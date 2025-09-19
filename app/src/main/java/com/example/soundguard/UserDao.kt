package com.example.soundguard

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE login = :login AND password = :password")
    suspend fun getUser(login: String, password: String): User?

    @Query("SELECT * FROM users WHERE login = :login")
    suspend fun getUserByLogin(login: String): User?

    @Query("SELECT * FROM users WHERE is_synced = 0")
    suspend fun getUnsyncedUsers(): List<User>

    @Update
    suspend fun update(user: User)
}

// SoundMeasurementDao.kt
@Dao
interface SoundMeasurementDao {
    @Insert
    suspend fun insert(measurement: SoundMeasurement)

    @Query("SELECT * FROM sound_measurements WHERE user_id = :userId ORDER BY timestamp DESC")
    fun getMeasurementsByUser(userId: Long): Flow<List<SoundMeasurement>>

    @Query("SELECT * FROM sound_measurements WHERE is_synced = 0")
    suspend fun getUnsyncedMeasurements(): List<SoundMeasurement>

    @Query("UPDATE sound_measurements SET is_synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)
}