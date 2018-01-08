package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.gridCellsHandlers

import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.IPhotosGridFragmentPresenterGridClicks

/** Handler for gallery */
class PhotoGridCellGallery(presenter: IPhotosGridFragmentPresenterGridClicks) : PhotoGridCellHandlerBase(presenter)
{
    override fun onCellClick(indexOfCell: Int) = presenter.startGetPhotoFromGallery()

    override fun getCellType(): PhotoGridCellHandlerBase.CellType = PhotoGridCellHandlerBase.CellType.Gallery

    override fun getCellOffset(): Int = 1
}
