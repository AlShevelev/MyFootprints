package com.syleiman.myfootprints.modelLayer.dto

import android.location.Location
import com.syleiman.myfootprints.modelLayer.dbEntities.DbLocation

/**
 * Geographic location
 * @param isLast - Last location - for service purpose
 */
data class LocationDto (var id: Long, var latitude: Double, var longitude: Double, var time: Long, var isLast: Boolean) {
    constructor(location: Location, isLast: Boolean) : this(0, location.latitude, location.longitude, location.time, isLast)

    constructor(
        dbLocation: DbLocation) : this(
            dbLocation.id!!,
            dbLocation.latitude,
            dbLocation.longitude,
            dbLocation.time,
            dbLocation.isLast)
}
