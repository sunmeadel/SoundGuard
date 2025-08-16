package com.example.soundguard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs
class VisualizerView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }


    private var amplitudes = ShortArray(0)
    private var currentColor = Color.parseColor("#9CD08F")


    // buffer = all inputs
    // size = valid inputs
    // dB = volume
    fun updateAmplitudes(buffer: ShortArray, size: Int, db: Double) {
        amplitudes = buffer.copyOf(size)


        val targetColor = when {
            db < 88 -> Color.parseColor("#9CD08F") // Safe zone = 80 - 88 dB (green)
            db < 94 -> Color.parseColor("#FCD581") // Medium zone = 89 - 94 dB (yellow)
            else -> Color.parseColor("#D52941")    // Danger zone = volume > 94dB (red)
        }

        // Smoothes gradually by 10%(~?)
        currentColor = fadeColor(currentColor, targetColor, 0.1f)

        // Request to redraw
        invalidate()
    }



    // Amplitude data -> visual bars
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = currentColor

        if (amplitudes.isEmpty()) return
        val barWidth = width.toFloat() / amplitudes.size

        val centerY = height / 2f // The centre line
        val radius = barWidth / 2f // Rounded lines

        amplitudes.forEachIndexed { index, amp ->
            val barHeight = (abs(amp.toFloat()) / Short.MAX_VALUE) * height

            // Horizontal pos
            val left = index * barWidth
            val right = left + barWidth * 0.9f // Slight gap between bars for clarity.

            // Vertical pos
            val top = centerY - barHeight / 2f
            val bottom = centerY + barHeight / 2f

            canvas.drawRoundRect(left, top, right, bottom, radius, radius, paint)
        }
    }

    // Color fade:
    private fun fadeColor(fromColor: Int, toColor: Int, fraction: Float): Int {
        val r = (Color.red(fromColor) + fraction * (Color.red(toColor) - Color.red(fromColor))).toInt()
        val g = (Color.green(fromColor) + fraction * (Color.green(toColor) - Color.green(fromColor))).toInt()
        val b = (Color.blue(fromColor) + fraction * (Color.blue(toColor) - Color.blue(fromColor))).toInt()
        return Color.rgb(r, g, b)
    }
}