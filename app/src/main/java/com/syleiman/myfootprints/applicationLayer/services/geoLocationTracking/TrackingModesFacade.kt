package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking

import android.util.Log

import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.businessLayer.geoLocationService.IGeoLocationService
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.tracking.TrackingCalculator
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.tracking.TrackingNotificationsCalculator

import javax.inject.Inject

/**
 * Facade to access geo location modes
 */
class TrackingModesFacade
@Inject
constructor(private val geoLocation : IGeoLocationService)
{

    private var currentMode: GeoLocationTrackingServiceRunModes? = null

    private var currentTracking: TrackingCalculator? = null

    init
    {
        currentMode = GeoLocationTrackingServiceRunModes.None
    }

    @Synchronized fun switchMode(mode: GeoLocationTrackingServiceRunModes)
    {
        Log.d(LogTags.GEOLOCATION, String.format("Switch $currentMode -> $mode", currentMode, mode))

        if (currentMode === mode)
            return

        stop()         // Stop tracking

        when (mode)
        {
            GeoLocationTrackingServiceRunModes.Active -> { currentTracking = TrackingNotificationsCalculator(geoLocation, 10000)} // 10s and notification
            GeoLocationTrackingServiceRunModes.Passive -> { currentTracking = TrackingCalculator(geoLocation, 600000)}       // 10m without notifications
            else -> { throw UnsupportedOperationException("This case is not supported") }
        }

        currentTracking!!.startTracking()
        currentMode = mode
    }

    /** Stop execution of current mode  */
    @Synchronized fun stop()
    {
        if (currentTracking != null)
        {
            currentTracking!!.stopTracing()
            Log.d(LogTags.GEOLOCATION, "Stop tracking: " + currentMode!!)
        }
    }
}