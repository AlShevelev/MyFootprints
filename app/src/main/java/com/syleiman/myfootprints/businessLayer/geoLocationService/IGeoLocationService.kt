package com.syleiman.myfootprints.businessLayer.geoLocationService

import android.location.Location
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.GeoLocationReceiver
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.IGeoLocationChannel

/** Get current location and gps-receiver state  */
interface IGeoLocationService
{
    /** Get last location  */
    fun getLastLocation(): Location

    /** Store last location in Db  */
    fun saveLastLocation(location: Location)

    /**Is Gps enabled  */
    fun isGeoLocationEnabled(): Boolean

    /** Get locations receiver from geolocation service  */
    fun getLocationsReceiver(channelProxy: IGeoLocationChannel): GeoLocationReceiver
}
