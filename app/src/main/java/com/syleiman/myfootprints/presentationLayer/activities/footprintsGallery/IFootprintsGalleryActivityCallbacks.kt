package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery

import com.syleiman.myfootprints.presentationLayer.activities.IActivityBaseCallback

/** Callback interface for presenter  */
interface IFootprintsGalleryActivityCallbacks : IActivityBaseCallback
{
    /** Is is init completed
     * @param isSuccess
     */
    fun initCompleted(isSuccess: Boolean, currentPosition: Int)

    /** Close activity  */
    fun close()

    /**  */
    fun showFullScreenProgress()

    /**  */
    fun hideFullScreenProgress()

    /** Page deleting completed  */
    fun pageDeleted(newCurrentPageIndex: Int)

    /** Redraw current page  */
    fun redrawAfterUpdate()
}
