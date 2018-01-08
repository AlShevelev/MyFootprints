package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import com.activeandroid.query.Select
import com.syleiman.myfootprints.businessLayer.localDbService.TransactionWrapper
import com.syleiman.myfootprints.modelLayer.dto.LocationDto
import com.syleiman.myfootprints.modelLayer.dbEntities.DbLocation
import java.sql.SQLException

/** Repository for last location  */
class LastLocationRepository : ILastLocationRepository
{
    /**
     * Get last location from Db
     * @return last location or null
     */
    private fun geLastLocationInternal(): DbLocation?
    {
        val dbResult = Select().from(DbLocation::class.java).where("IsLast = ?", 1).execute<DbLocation>()

        if (dbResult.size > 0)
            return dbResult[0]
        return null
    }

    /**
     * Get last location from Db
     * @return last location or null
     */
    @Throws(SQLException::class)
    override fun getLastLocation(): LocationDto? = TransactionWrapper.processWithoutTransaction<LocationDto?> { this.geLastLocationInternal()?.let(::LocationDto) }

    /** Update or insert last location  */
    @Throws(SQLException::class)
    override fun updateOrAddLastLocation(location: LocationDto)
    {
        TransactionWrapper.processInTransaction {
            (this.geLastLocationInternal() ?: DbLocation()).apply {
                latitude = location.latitude
                longitude = location.longitude
                time = location.time
                isLast = true
            }.save()
        }
    }
}