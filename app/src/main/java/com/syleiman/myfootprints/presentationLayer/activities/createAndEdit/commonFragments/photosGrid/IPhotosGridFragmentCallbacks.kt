package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid

/**
 * Callbacks of PhotosGridFragment for presenter
 */
interface IPhotosGridFragmentCallbacks
{
    /** Hide photos' grid and show progressBar bar	 */
    fun showProgress()

    /** Show/hide full-screen progress when open photo */
    fun setProgressFullScreenVisibility(isVisible : Boolean)

    /**
     * Hide progressBar bar and show photos' grid
     * @param photosUrls urls of photos on local device
     */
    fun showGrid(photosUrls: List<String>)

    /** Show an error */
    fun showMessage(stringResId: Int)
}