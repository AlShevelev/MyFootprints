package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.tracking

import android.location.Location

import com.syleiman.myfootprints.businessLayer.geoLocationService.IGeoLocationService
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.GeoLocationSender

/**
 * Current location tracking with notification when location was changed
 */
class TrackingNotificationsCalculator(geoLocationCommonLogic: IGeoLocationService, updatesInterval: Long) : TrackingCalculator(geoLocationCommonLogic, updatesInterval)
{
    private val broadcastSender: GeoLocationSender = GeoLocationSender()

    /** Notification about location changing  */
    override fun notifyLocationChanged(location: Location)
    {
        broadcastSender.locationUpdated(location)
    }
}
