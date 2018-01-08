package com.syleiman.myfootprints.presentationLayer.activities.myWorld

import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.ISyncResultChannel
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncCompletedReceiver

import javax.inject.Inject

class MyWorldActivityModel
@Inject
constructor()
{
    private var syncCompletedReceiver: SyncCompletedReceiver? = null

    companion object
    {
        private var showInitMessage = true
    }

    /** Return help message  */
    val helpMessage: Int
        get() = R.string.my_world_activity_help

    /**
     * Get message when activity init
     * @return id of massage or null if not message needed
     */
    val initMessage: Int?               // Only once in app lifetime
        get()
        {
            if (showInitMessage)
            {
                showInitMessage = false
                return helpMessage
            }
            return null
        }

    /** Start receive sync events  */
    fun startReceiveSyncStatus(syncResultChannel: ISyncResultChannel)
    {
        syncCompletedReceiver = SyncCompletedReceiver(syncResultChannel)
    }

    /** Start receive sync events  */
    fun stopReceiveSyncStatus()
    {
        if (syncCompletedReceiver != null)
            syncCompletedReceiver!!.unregister()
    }
}
