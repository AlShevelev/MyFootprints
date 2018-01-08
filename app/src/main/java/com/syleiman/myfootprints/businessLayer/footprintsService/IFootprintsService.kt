package com.syleiman.myfootprints.businessLayer.footprintsService

import android.app.Activity
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGalleryDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintSaveDto
import com.syleiman.myfootprints.businessLayer.footprintsService.extUpdateProcessing.ExpUpdateProcessorBase
import com.syleiman.myfootprints.modelLayer.dto.FootprintDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGridDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintMarkerDto
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto

/** Working with footprint  */
interface IFootprintsService
{
    /**
     * Create footprint
     * @param footprint footprint to saveToPrivateArea
     * *
     * @param result callback (true - success)
     */
    fun createFootprint(context: Activity, footprint: FootprintSaveDto, result: (Boolean) -> Unit)

    /**
     * Update footprint
     * @param footprint footprint to saveToPrivateArea
     * *
     * @param result callback (true - success)
     */
    fun updateFootprint(
        context: Activity,
        footprint: FootprintSaveDto,
        isPhotoUpdated: Boolean,
        isCommentUpdated: Boolean,
        isLocationUpdated: Boolean,
        result: (Boolean) -> Unit)

    /**
     * Get all footprints asynchronous
     * @param result list of footprints (null - in case of error)
     */
    fun getAllMarkers(result: (List<FootprintMarkerDto>?) -> Unit)

    /** Get all footprints for gallery sorted by create moment in descending order
     * @param result list of footprints (null - in case of error)
     */
    fun getAllForGrid(result: (List<FootprintForGridDto>?) -> Unit)

    /**
     * List of footprints' ids sorted by CreationDate
     * @return null in case of error
     */
    fun getAllIdsOrderByCreation(normalSortOrder: Boolean, result: (List<Long>?) -> Unit)

    /** Get footprint by its id for viewing in gallery
     * @return null in case of error
     */
    fun getForGallery(id: Long): FootprintForGalleryDto?

    /**
     * Delete footprint by its id
     * @param result false in case of error
     */
    fun deleteFootprint(id: Long, result: (Boolean) -> Unit)

    /**
     * Get footprint by id
     * @return null in case of error
     */
    fun getById(footprintId: Long): FootprintDto?

    /** Get processor for external log records  */
    fun getExpUpdateProcessor(logRecord: SyncLogDto): ExpUpdateProcessorBase
}
