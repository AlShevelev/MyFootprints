package com.syleiman.myfootprints.businessLayer.geoLocationService

import android.location.Location
import android.location.LocationManager
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.GeoLocationReceiver
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.IGeoLocationChannel
import com.syleiman.myfootprints.modelLayer.dto.LocationDto
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.ILastLocationRepository
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import java.sql.SQLException
import java.util.*
import javax.inject.Inject

/** Get current location and gps-receiver state  */
class GeoLocationService
@Inject
constructor (
    private val sysInfo: ISystemInformationService,
    private val lastLocationDbRepository: ILastLocationRepository) : IGeoLocationService
{
    /** Get last location  */
    override fun getLastLocation(): Location = getLastLocationFromDb() ?: getDefaultLocation()

    /** Get first "fake" location  */
    private fun getDefaultLocation(): Location
    {
        return Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 40.417160           // Puerta del Sol, Madrid, España
            longitude = -3.703518
            time = Date().time
        }
    }

    /** Store last location in Db  */
    override fun saveLastLocation(location: Location) = saveLastLocationToDb(location)

    /**Is Gps enabled  */
    override fun isGeoLocationEnabled(): Boolean = sysInfo.isGeoLocationEnabled()

    /**
     * Get locations receiver from geolocation service
     */
    override fun getLocationsReceiver(channelProxy: IGeoLocationChannel): GeoLocationReceiver = GeoLocationReceiver(channelProxy)

    /** Get last location  */
    private fun getLastLocationFromDb(): Location?
    {
        try
        {
            val dbLastLocation = lastLocationDbRepository.getLastLocation()

            if (dbLastLocation != null)
            {
                val location = Location(LocationManager.GPS_PROVIDER)
                location.latitude = dbLastLocation.latitude
                location.longitude = dbLastLocation.longitude
                location.time = dbLastLocation.time
                return location
            }
        }
        catch (ignored: SQLException) { }
        return null
    }

    /** Store last location in Db  */
    private fun saveLastLocationToDb(location: Location)
    {
        try
        {
            lastLocationDbRepository.updateOrAddLastLocation(LocationDto(location, true))
        }
        catch (ignored: SQLException) { }
    }
}