package com.example.soundguard

import kotlinx.coroutines.flow.Flow

class HearingHealthRepository(private val userDao: UserDao, private val measurementDao: SoundMeasurementDao) {

    // User operations
    suspend fun addUser(user: User): Long = userDao.insert(user)

    suspend fun getUser(login: String, password: String): User? = userDao.getUser(login, password)

    suspend fun getUserByLogin(login: String): User? = userDao.getUserByLogin(login)

    // Measurement operations
    suspend fun addMeasurement(measurement: SoundMeasurement) = measurementDao.insert(measurement)

    fun getMeasurementsByUser(userId: Long): Flow<List<SoundMeasurement>> =
        measurementDao.getMeasurementsByUser(userId)

    // Sync operations
    suspend fun getUnsyncedUsers(): List<User> = userDao.getUnsyncedUsers()

    suspend fun getUnsyncedMeasurements(): List<SoundMeasurement> =
        measurementDao.getUnsyncedMeasurements()

    suspend fun markMeasurementAsSynced(id: String) = measurementDao.markAsSynced(id)

    suspend fun updateUser(user: User) = userDao.update(user)
}