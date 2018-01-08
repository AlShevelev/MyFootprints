package com.syleiman.myfootprints.applicationLayer.services.sync.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications.ForegroundManager

/** Wrapper for SyncAdapter  */
class SyncService : Service()
{
    companion object
    {
        private var sSyncAdapter: SyncAdapter? = null         // Storage for an instance of the sync adapter
        private val sSyncAdapterLock = Any()            // Object to use as a thread-safe lock
    }

    /**  */
    override fun onCreate()
    {
        Log.d(LogTags.SYNC_PROCESS, "Start sync service")

        synchronized(sSyncAdapterLock) // Create the sync adapter as a singleton and disallow parallel syncs
        {
            if (sSyncAdapter == null)
                sSyncAdapter = SyncAdapter(ForegroundManager(this), applicationContext, true)
        }
    }

    /** Return an object that allows the system to invoke the sync adapter (by call onPerformSync() method)  */
    override fun onBind(intent: Intent): IBinder?
    {
        return sSyncAdapter!!.syncAdapterBinder
    }
}
