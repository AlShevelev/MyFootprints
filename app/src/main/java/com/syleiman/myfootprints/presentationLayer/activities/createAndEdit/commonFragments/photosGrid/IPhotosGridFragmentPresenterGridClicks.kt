package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid

/** Presenter methods for cells clicks */
interface IPhotosGridFragmentPresenterGridClicks
{
    /**
     * Click on photo
     * @param indexOfPhoto index of photo in the list
     */
    fun clickOnPhoto(indexOfPhoto: Int)

    /** Click on photo camera  */
    fun startCapturePhoto()

    /** Click on gallery  */
    fun startGetPhotoFromGallery()
}
