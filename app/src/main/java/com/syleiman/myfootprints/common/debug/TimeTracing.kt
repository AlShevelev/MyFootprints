package com.syleiman.myfootprints.common.debug

import android.util.Log

import java.io.Closeable

/** Tracing time of some internal code  */
class TimeTracing(private val logTag: String, private val logOperation: String) : Closeable
{
    private val startTime: Long = System.currentTimeMillis()

    init
    {
        Log.d(logTag, logOperation + ": start tracing")
    }

    override fun close()
    {
        val totalTimes = System.currentTimeMillis() - startTime
        Log.d(logTag, "$logOperation: tracing complete. Total time is: $totalTimes[ms]")
    }
}
