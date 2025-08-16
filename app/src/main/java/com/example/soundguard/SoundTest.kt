package com.example.soundguard

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.log10
import kotlin.math.sqrt
import com.example.soundguard.VisualizerView

class SoundTest : AppCompatActivity() {

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 303
    }

    private var isMeasuring = false
    private var audioRecord: AudioRecord? = null
    private val sampleRate = 44100 // Hz

    // Reference to our custom visualizer view
    private lateinit var visualizerView: VisualizerView
    private lateinit var soundLevelText: TextView
    private lateinit var soundTestButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sound_test)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        visualizerView = findViewById(R.id.sound_visualizer)
        soundLevelText = findViewById(R.id.sound_level_text)
        soundTestButton = findViewById(R.id.sound_test_button)


        soundTestButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_RECORD_AUDIO_PERMISSION
                )
            } else {
                startSoundMeasurement()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSoundMeasurement()
            } else {
                Toast.makeText(
                    this,
                    "Microphone permission is required to measure the sound level",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startSoundMeasurement() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this,
                "Microphone permission is required to start measurement",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        val buffer = ShortArray(bufferSize)
        try {
            audioRecord?.startRecording()
        } catch (e: SecurityException) {
            Toast.makeText(this, "Unable to access microphone", Toast.LENGTH_LONG).show()
            return
        }

        isMeasuring = true

        Thread {
            while (isMeasuring) {
                val readCount = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (readCount > 0) {
                    val amplitude = calculateRMS(buffer, readCount)
                    val db = 20 * log10(amplitude / 32768.0).coerceAtLeast(0.0)

                    runOnUiThread {
                        soundLevelText.text = "Sound Level: %.2f dB".format(db)
                        visualizerView.updateAmplitudes(buffer, readCount, db)
                    }
                }
            }
        }.start()
    }

    private fun calculateRMS(buffer: ShortArray, readSize: Int): Double {
        var sum = 0.0
        for (i in 0 until readSize) {
            sum += buffer[i] * buffer[i]
        }
        return sqrt(sum / readSize)
    }

    private fun stopSoundMeasurement() {
        isMeasuring = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    override fun onPause() {
        super.onPause()
        stopSoundMeasurement()
    }
}








