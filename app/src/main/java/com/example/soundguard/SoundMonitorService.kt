package com.example.soundguard

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SoundMonitorService : Service() {
    private lateinit var repository: HearingHealthRepository
    private var currentUserId: Long = 0

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getDatabase(this)
        repository = HearingHealthRepository(db.userDao(), db.soundMeasurementDao())

        // Get current user ID from SharedPreferences
        val sharedPref: SharedPreferences = getSharedPreferences("SoundGuardPrefs", Context.MODE_PRIVATE)
        currentUserId = sharedPref.getLong("CURRENT_USER_ID", 0)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start sound monitoring logic here
        startSoundMonitoring()
        return START_STICKY
    }

    private fun startSoundMonitoring() {
        // Implement your sound level monitoring logic here
        // When you capture a sound measurement:
        val measurement = SoundMeasurement(
            decibelLevel = 85.5f, // Example value
            durationSeconds = 120, // Example value
            userId = currentUserId
        )

        // Use a coroutine scope since we're in a Service
        CoroutineScope(Dispatchers.IO).launch {
            repository.addMeasurement(measurement)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}