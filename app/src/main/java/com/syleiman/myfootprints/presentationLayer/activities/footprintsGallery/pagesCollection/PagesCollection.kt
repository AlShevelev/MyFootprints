package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.pagesCollection

import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGalleryDto
import java.util.*

/** Collection of page items  */
class PagesCollection(private val footprints : IFootprintsService)
{
    private var items: ArrayList<PagesCollectionItem>? = null

    /**
     * Init collection
     * @param startFootprintId
     * *
     * @param complete - complete callback (true - success)
     */
    fun init(startFootprintId: Long, normalSortOrder: Boolean, complete: (Boolean, Int) -> Unit)
    {
        footprints.getAllIdsOrderByCreation(normalSortOrder) { ids ->
            if (ids == null)
                complete.invoke(false, -1)
            else
            {
                items = ArrayList<PagesCollectionItem>()

                var startItemPosition = -1

                for (i in ids.indices)
                {
                    val footprintId = ids[i]
                    val item = PagesCollectionItem(footprintId, null)
                    items!!.add(item)

                    if (footprintId == startFootprintId)
                        startItemPosition = i
                }

                if (startItemPosition == -1)
                // Something strange happend - not found
                    complete.invoke(false, -1)
                else
                    complete.invoke(true, startItemPosition)
            }
        }
    }

    /**
     * Get footprints data by position and switch list to new position
     * @return null in case of error
     */
    fun getInPosition(position: Int): FootprintForGalleryDto
    {
        val item = items!![position]

        if (item.footprintData == null)
            item.footprintData = footprints.getForGallery(item.footprintId)

        return item.footprintData!!
    }

    /** Reload item from DB   */
    fun reloadCacheItem(position: Int): FootprintForGalleryDto
    {
        val item = items!![position]
        item.footprintData = footprints.getForGallery(item.footprintId)
        return item.footprintData!!
    }

    /** Get total pages  */
    val totalPages: Int
        get() = items!!.size

    /**
     * Remove element by index
     * @return index of new current element (-1 collection if empty)
     */
    fun remove(index: Int): Int
    {
        var newIndex = index

        if (index == items!!.size - 1)     // remove last item
            newIndex = items!!.size - 2

        items!!.removeAt(index)

        if (items!!.size == 0)
            newIndex = -1

        return newIndex
    }
}