package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.gridCellsHandlers

import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.IPhotosGridFragmentPresenterGridClicks

/** Handler for photo */
class PhotoGridCellPhoto(presenter: IPhotosGridFragmentPresenterGridClicks) : PhotoGridCellHandlerBase(presenter)
{
    override fun onCellClick(indexOfCell: Int) = presenter.clickOnPhoto(indexOfCell - 2)          // Skip first two cells

    override fun getCellType(): PhotoGridCellHandlerBase.CellType = PhotoGridCellHandlerBase.CellType.Photo

    override fun getCellOffset(): Int = 2
}
