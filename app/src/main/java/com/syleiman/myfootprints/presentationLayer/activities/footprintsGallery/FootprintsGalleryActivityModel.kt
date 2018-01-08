package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery

import android.app.Activity

import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGalleryDto
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.pagesCollection.PagesCollection
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.result.FootprintsGalleryActivityResult
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.result.FootprintsGalleryActivityUpdateInfo
import com.syleiman.myfootprints.presentationLayer.externalIntents.send.FootprintSender

import java.util.LinkedList

import javax.inject.Inject

class FootprintsGalleryActivityModel
@Inject
constructor(private val footprints : IFootprintsService)
{
    private val pagesCollection: PagesCollection = PagesCollection(footprints)
    private val deletedFootprintIds: MutableList<Long> = LinkedList()
    private val updatesInfo: MutableList<FootprintsGalleryActivityUpdateInfo> = LinkedList()

    /**
     * Init collection
     * @param startFootprintId
     * @param complete - complete callback (true - success)
     */
    fun init(startFootprintId: Long, normalSortOrder: Boolean, complete: Function2<Boolean, Int, Unit>)
    {
        pagesCollection.init(startFootprintId, normalSortOrder, complete)
    }

    /**
     * Get footprints data by position and switch list to new position
     * @return null in case of error
     */
    fun getInPosition(position: Int): FootprintForGalleryDto = pagesCollection.getInPosition(position)

    /** Get total pages  */
    val totalPages: Int
        get() = pagesCollection.totalPages

    /**
     * Delete footprint
     * @param position index of deleted footprint
     * @param result index of new current footprint (null - error; -1 - collection is empty)
     */
    fun deleteFootprint(position: Int, result: Function1<Int?, Unit>)
    {
        val footprintId = pagesCollection.getInPosition(position).footprintId
        footprints.deleteFootprint(footprintId) { isSuccess ->
            if (isSuccess)
            {
                deletedFootprintIds.add(footprintId)
                result.invoke(pagesCollection.remove(position))
            } else
                result.invoke(null)
        }
    }

    /** Remove cached footprint info in some position   */
    fun updateAfterEdit(position: Int)
    {
        val (footprintId, photoFileName, comment, location) = pagesCollection.reloadCacheItem(position)
        updatesInfo.add(FootprintsGalleryActivityUpdateInfo(footprintId, photoFileName, location, comment))
    }

    /**  */
    fun createActivityResult(): FootprintsGalleryActivityResult
    {
        return FootprintsGalleryActivityResult(deletedFootprintIds.map { it }.toLongArray(), updatesInfo.map { it })
    }

    @Suppress("UNUSED_VARIABLE")
        /** Sent footprint to external systems  */
    fun sendToExternal(context: Activity, position: Int): Boolean
    {
        val (footprintId, photoFileName, comment, location) = pagesCollection.getInPosition(position)
        return FootprintSender.startSend(context, comment, photoFileName, location)
    }
}
