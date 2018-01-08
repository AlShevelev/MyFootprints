package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.gridCellsHandlers

import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.IPhotosGridFragmentPresenterGridClicks

/** Base class fro photo grid cell handler  */
abstract class PhotoGridCellHandlerBase(protected val presenter: IPhotosGridFragmentPresenterGridClicks)
{
    enum class CellType
    {
        Camera,
        Gallery,
        Photo
    }

    abstract fun onCellClick(indexOfCell: Int)

    abstract fun getCellType(): CellType

    abstract fun getCellOffset(): Int
}