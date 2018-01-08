package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.tracking

import android.location.Location

/** Updates geolocation data directly */
interface IGeoLocationTrackingCalculatorUpdate
{
    /**
     * Update location
     * @param location new location value
     * @param force
     */
    fun doLocationUpdate(location: Location, force: Boolean)
}
