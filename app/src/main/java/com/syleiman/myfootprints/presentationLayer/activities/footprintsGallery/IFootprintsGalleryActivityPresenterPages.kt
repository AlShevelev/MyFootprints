package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery

import com.syleiman.myfootprints.modelLayer.dto.FootprintForGalleryDto

/** Presenter's interface for paging  */
interface IFootprintsGalleryActivityPresenterPages
{
    /**
     * Get footprint dto by it's position
     * @param position (from 0)
     */
    fun getInPosition(position: Int): FootprintForGalleryDto

    /** Get total pages  */
    val totalPages: Int
}
