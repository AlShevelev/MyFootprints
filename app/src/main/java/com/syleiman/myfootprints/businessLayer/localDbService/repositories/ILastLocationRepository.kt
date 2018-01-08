package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import com.syleiman.myfootprints.modelLayer.dto.LocationDto

import java.sql.SQLException

/** Repository for last location  */
interface ILastLocationRepository
{
    /**
     * Get last location from Db
     * @return last location
     */
    fun getLastLocation(): LocationDto?

    /** Update or insert last location*/
    @Throws(SQLException::class)
    fun updateOrAddLastLocation(location: LocationDto)
}
