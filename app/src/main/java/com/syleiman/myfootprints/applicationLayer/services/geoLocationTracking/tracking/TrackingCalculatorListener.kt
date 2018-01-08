package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.tracking

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

/**
 * Listener for location data updated
 */
class TrackingCalculatorListener(private val provider: String, private val geoLocationUpdate: IGeoLocationTrackingCalculatorUpdate) : LocationListener
{

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle)
    {
    }

    override fun onProviderEnabled(provider: String)
    {
    }

    override fun onProviderDisabled(provider: String)
    {
    }

    override fun onLocationChanged(location: Location)
    {
        if (provider == LocationManager.GPS_PROVIDER)           // We always trust GPS
            geoLocationUpdate.doLocationUpdate(location, true)
    }
}