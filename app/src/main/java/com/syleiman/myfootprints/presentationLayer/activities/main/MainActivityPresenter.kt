package com.syleiman.myfootprints.presentationLayer.activities.main

import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.GeoLocationTrackingService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.ISyncResultChannel
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult
import com.syleiman.myfootprints.common.letNull
import javax.inject.Inject

class MainActivityPresenter
@Inject
constructor(private val mainActivityCallbacks: IMainActivityCallbacks,  private val model: MainActivityModel)
{
    /** On activity created  */
    fun init()
    {
        GeoLocationTrackingService.startInPassiveMode()              // Start location tracking service when application started
        model.startReceiveSyncStatus(object : ISyncResultChannel
        {
            override fun syncCompleted(result: SyncResult)
            {
                mainActivityCallbacks.syncCompleted(result.footprintsWasChanged)       // Start receive events from sync
            }
        })
    }

    /** Init activity by total footprints  */
    fun updateCover()
    {
        model.getLastFootprint { coverImage, totalFootprints ->
            mainActivityCallbacks.setTotalFootprints(totalFootprints)
            coverImage.letNull({mainActivityCallbacks.setCover(it!!)}, {mainActivityCallbacks.showMessage(R.string.message_box_cant_read_last_photo)})
        }
    }

    /** Called from activity when it destroyed finally  */
    fun onDestroyUi()
    {
        model.stopReceiveSyncStatus()          // Stop receive events from sync
        GeoLocationTrackingService.stop()              // Stop location tracking service when app destroyed
    }
}
