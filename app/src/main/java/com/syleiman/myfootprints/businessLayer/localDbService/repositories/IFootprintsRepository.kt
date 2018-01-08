package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import android.util.Pair
import com.syleiman.myfootprints.modelLayer.dto.FootprintDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGridDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintMarkerDto
import java.sql.SQLException

interface IFootprintsRepository
{
    /**
     * Save footprint
     * @return id of footprint in Db (or null in case of error)
     */
    @Throws(SQLException::class)
    fun create(footprint: FootprintDto): Long?

    /**
     * Save footprint from external data
     * @param uid unique if of footprint
     * @return id of footprint in Db (or null in case of error)
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun createExternal(footprint: FootprintDto, uid: String): Long?

    /**
     * Save footprint
     * @return names of files to delete
     */
    @Throws(SQLException::class)
    fun update(footprint: FootprintDto, isPhotoUpdated: Boolean, isCommentUpdated: Boolean, isLocationUpdated: Boolean): List<String>

    /**
     * Update footprint from external data
     * @param uid unique if of footprint
     * @return if of footprint in Db and names of files to delete
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun updateExternal(footprint: FootprintDto, uid: String): Pair<Long, List<String>>

    /** Update footprints geoinformation  */
    @Throws(SQLException::class)
    fun updateGeoInformation(footprintId: Long, countryName: String?, countryCode: String?, cityName: String?)

    /** Get all footprints  markers  */
    fun getAllMarkers(): List<FootprintMarkerDto>

    /**
     * Get all footprints for gallery sorted by create moment in descending order
     */
    fun getAllForGrid(): List<FootprintForGridDto>

    /** Sort footprints by CreationDate and get ids  */
    @Throws(SQLException::class)
    fun getAllIdsOrderByCreation(normalSortOrder: Boolean): List<Long>

    /** Get footprint by its id  */
    @Throws(SQLException::class)
    fun getById(id: Long): FootprintDto

    /**
     * Delete footprint
     * @return list of files names to delete (without path)
     */
    @Throws(SQLException::class)
    fun delete(id: Long): List<String>

    /**
     * Delete footprint from external data
     * @param uid unique if of footprint
     * @return if of footprint in Db and names of files to delete
     */
    @Throws(SQLException::class)
    fun deleteExternal(uid: String): Pair<Long, List<String>>

    /**
     * Get last footprint
     * @return null in case of error or not footprints found
     */
    fun getLastFootprint(): FootprintDto?

    /** Is exist footprint with such Uid  */
    @Throws(SQLException::class)
    fun isFootprintExist(uid: String): Boolean
}
