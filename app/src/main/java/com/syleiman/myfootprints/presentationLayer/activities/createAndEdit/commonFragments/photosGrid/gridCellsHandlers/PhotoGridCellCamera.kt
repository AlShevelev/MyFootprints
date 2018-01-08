package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.gridCellsHandlers

import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.IPhotosGridFragmentPresenterGridClicks

/** Handler for camera  */
class PhotoGridCellCamera(presenter: IPhotosGridFragmentPresenterGridClicks) : PhotoGridCellHandlerBase(presenter)
{
    override fun onCellClick(indexOfCell: Int) = presenter.startCapturePhoto()           // Explicity intents will be called here

    override fun getCellType(): PhotoGridCellHandlerBase.CellType = PhotoGridCellHandlerBase.CellType.Camera

    override fun getCellOffset(): Int = 0
}
