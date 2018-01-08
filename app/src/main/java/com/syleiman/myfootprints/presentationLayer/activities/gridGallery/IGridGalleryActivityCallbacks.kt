package com.syleiman.myfootprints.presentationLayer.activities.gridGallery

import com.syleiman.myfootprints.presentationLayer.activities.IActivityBaseCallback

/** Interface to inject in presenter  */
interface IGridGalleryActivityCallbacks : IActivityBaseCallback
{
    /** Hide photos' grid and show progressBar bar	  */
    fun showProgress()

    /** Hide progressBar bar and show photos' grid  */
    fun showGrid(startPosition: Int)

    /** Hide progressBar bar  */
    fun hideProgress()

    /** Show No footprints label  */
    fun showNoFootprints()

    /**
     * Sync was completed
     * @param needUpdate need update UI
     */
    fun syncCompleted(needUpdate: Boolean)
}
