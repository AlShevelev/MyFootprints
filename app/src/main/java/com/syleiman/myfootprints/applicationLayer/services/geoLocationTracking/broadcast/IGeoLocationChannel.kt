package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast

import android.location.Location

/**
 * Interface methods
 */
interface IGeoLocationChannel
{
    /**
     * Location was updated
     * @param location current location
     */
    fun locationUpdated(location: Location)
}
