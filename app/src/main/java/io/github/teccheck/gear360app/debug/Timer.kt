package io.github.teccheck.gear360app.debug

import android.util.Log

private const val TAG = "Timer"

class Timer {

    private var startTime: Long = 0
    private var currentTimingIndex = 0
    private var timings = IntArray(20)

    fun start() {
        startTime = System.currentTimeMillis()
    }

    fun stop(): Long {
        return stop(false)
    }

    fun stop(print: Boolean): Long {
        val timePassed = System.currentTimeMillis() - startTime
        if (print) Log.d(TAG, "Time passed: $timePassed")

        timings[currentTimingIndex] = timePassed.toInt()
        currentTimingIndex++

        if (currentTimingIndex > timings.lastIndex)
            currentTimingIndex = 0

        return timePassed
    }

    fun getAverage(): Int {
        var total = 0
        for (time in timings)
            total += time

        return total / timings.size
    }
}