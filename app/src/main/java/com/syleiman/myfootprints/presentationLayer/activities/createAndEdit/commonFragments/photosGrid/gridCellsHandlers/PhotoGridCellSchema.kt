package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.gridCellsHandlers

import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.IPhotosGridFragmentPresenterGridClicks

/** Factory for photo grid cells handlers  */
class PhotoGridCellSchema(presenter: IPhotosGridFragmentPresenterGridClicks)
{
    private val camera: PhotoGridCellCamera = PhotoGridCellCamera(presenter)
    private val gallery: PhotoGridCellGallery = PhotoGridCellGallery(presenter)
    private val photo: PhotoGridCellPhoto = PhotoGridCellPhoto(presenter)

    fun getHandler(cellIndex: Int): PhotoGridCellHandlerBase
    {
        if (cellIndex == camera.getCellOffset())
            return camera
        if (cellIndex == gallery.getCellOffset())
            return gallery
        return photo
    }
}
