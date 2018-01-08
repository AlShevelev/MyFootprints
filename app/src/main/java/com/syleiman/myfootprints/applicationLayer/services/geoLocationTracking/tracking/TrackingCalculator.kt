package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.tracking

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.support.v4.content.ContextCompat
import android.util.Log

import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.geoLocationService.IGeoLocationService
import com.syleiman.myfootprints.common.LogTags

import java.util.Date
import java.util.Timer
import java.util.TimerTask
import java.util.TreeSet

/** Current location tracking  */
open class TrackingCalculator(
    private val geoLocationCommonLogic: IGeoLocationService,
    private val updatesInterval: Long) : IGeoLocationTrackingCalculatorUpdate           // Update location interval [ms]
{
    companion object
    {
        private val minDistance = 50f            // [m] If distance between locations is less that this value locations are same
        private val maxDistance = 1000f          // [m] If distance between locations is greater that this value we must update last location
    }

    private val locationManager: LocationManager = App.context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val supportedProviders: TreeSet<String> = TreeSet()

    private var bestLocationTimer: Timer? = null             // Timer for check best location location

    /**  */
    private var lastLocationSync: Location? = null

    private var gpsListener: TrackingCalculatorListener? = null
    private var networkListener: TrackingCalculatorListener? = null

    private var started = false

    init
    {
        val allProviders = locationManager.allProviders              // Collect all supportable providers
        supportedProviders.addAll(allProviders)

        lastLocationSync = geoLocationCommonLogic.getLastLocation()
    }

    /**  */
    private fun isProviderSupported(provider: String): Boolean = supportedProviders.contains(provider)

    /** Get the last known location from a specific provider (network/gps)  */
    private fun getLocationByProvider(provider: String): Location?
    {
        if (!isProviderSupported(provider))
            return null

        try
        {
            if (locationManager.isProviderEnabled(provider))
                return locationManager.getLastKnownLocation(provider)
        }
        catch (e: SecurityException)
        {
            Log.e(LogTags.GEOLOCATION, e.toString())
        }

        return null
    }

    /** Try to get the 'best' location selected from all providers  */
    // if we have only one location available, the choice is easy
    // gps is current and available, gps is better than network
    // gps is old, we can't trust it. use network location
    // both are old return the newer of those two
    private fun getBestLocation(): Location?
    {
        val gpsLocation = getLocationByProvider(LocationManager.GPS_PROVIDER)
        val networkLocation = getLocationByProvider(LocationManager.NETWORK_PROVIDER)

        if(gpsLocation==null && networkLocation==null)
        {
            Log.d(LogTags.GEOLOCATION, "No Location available.")
            return null
        }

        if (gpsLocation == null)
        {
            Log.d(LogTags.GEOLOCATION, "No GPS Location available.")
            return networkLocation
        }
        if (networkLocation == null)
        {
            Log.d(LogTags.GEOLOCATION, "No Network Location available")
            return gpsLocation
        }

        val oldInterval = System.currentTimeMillis() - updatesInterval

        val gpsIsOld = gpsLocation.time < oldInterval
        val networkIsOld = networkLocation.time < oldInterval

        if (!gpsIsOld)
        {
            Log.d(LogTags.GEOLOCATION, "Returning current GPS Location")
            return gpsLocation
        }

        if (!networkIsOld)
        {
            Log.d(LogTags.GEOLOCATION, "GPS is old, Network is current, returning network")
            return networkLocation
        }
        if (gpsLocation.time > networkLocation.time)
        {
            Log.d(LogTags.GEOLOCATION, "Both are old, returning gps(newer)")
            return gpsLocation
        }
        else
        {
            Log.d(LogTags.GEOLOCATION, "Both are old, returning network(newer)")
            return networkLocation
        }
    }

    /** Start tracking location  */
    fun startTracking()
    {
        if (started || !hasPermissions())       // Can't start  - has no permissions
            return

        try
        {
            gpsListener = TrackingCalculatorListener(LocationManager.GPS_PROVIDER, this)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, updatesInterval, minDistance, gpsListener)

            networkListener = TrackingCalculatorListener(LocationManager.NETWORK_PROVIDER, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, updatesInterval, minDistance, networkListener)
        }
        catch (e: SecurityException)
        {
            Log.e(LogTags.GEOLOCATION, e.toString())
        }

        bestLocationTimer = Timer()                // start the best location choosing thread
        bestLocationTimer!!.scheduleAtFixedRate(object : TimerTask()
        {
            override fun run()
            {
                if (!isGeoLocationEnabled)                // Do nothing if geolocation is disabled
                    return

                if (!hasPermissions())                // Permissions can be revoked in amy moment
                    return

                getBestLocation()?.let { doLocationUpdate(it, false) }
            }
        }, 0, updatesInterval)

        started = true
    }

    /** Stop tracking location  */
    fun stopTracing()
    {
        if (!started)
            return

        if (bestLocationTimer != null)
        {
            bestLocationTimer!!.cancel()
            bestLocationTimer!!.purge()
        }

        try
        {
            locationManager.removeUpdates(gpsListener)
            locationManager.removeUpdates(networkListener)
        } catch (e: SecurityException)
        {
            Log.e(LogTags.GEOLOCATION, e.toString())
        }

        started = false
    }

    /**
     * Update location
     * @param location new location value
     * *
     * @param force
     */
    override fun doLocationUpdate(location: Location, force: Boolean)
    {
        Log.d(LogTags.GEOLOCATION, "update received:" + location)

        val lastLocationLocal = lastLocationSync

        if (lastLocationLocal != null)
        {
            val distance = location.distanceTo(lastLocationLocal)
            Log.d(LogTags.GEOLOCATION, "Distance to last: " + distance)

            if (!force && location.distanceTo(lastLocationLocal) < maxDistance)
            {
                if (location.distanceTo(lastLocationLocal) < minDistance)
                {
                    Log.d(LogTags.GEOLOCATION, "Position didn't change")
                    return
                }
                if (location.accuracy >= lastLocationLocal.accuracy && location.distanceTo(lastLocationLocal) < location.accuracy)
                {
                    Log.d(LogTags.GEOLOCATION, "Accuracy got worse and we are still within the accuracy range. Not updating")
                    return
                }
                if (location.time <= lastLocationLocal.time)
                {
                    Log.d(LogTags.GEOLOCATION, "Timestamp not never than last")
                    return
                }
            }

            Log.d(LogTags.GEOLOCATION, "Location updated")
            lastLocationSync = location
            geoLocationCommonLogic.saveLastLocation(location)
            notifyLocationChanged(location)
        }
    }

    /** Get first "fake" location */
    private // Palacio Real de Madrid (Plaza de la Armeria)
    val defaultLocation: Location
        get()
        {
            val location = Location(LocationManager.GPS_PROVIDER)
            location.latitude = 40.416505
            location.longitude = -3.714593
            location.time = Date().time

            return location
        }

    /** Is Gps enabled  */
    private val isGeoLocationEnabled: Boolean
        get() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    /** Has code permissions to use geolocation service  */
    private fun hasPermissions(): Boolean
    {
        return ContextCompat.checkSelfPermission(App.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && // for GPS
                ContextCompat.checkSelfPermission(App.context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED    // for network
    }

    /** Notification about location changing  */
    protected open fun notifyLocationChanged(location: Location)
    {
        // do nothing
    }
}