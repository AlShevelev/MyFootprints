package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity

import com.syleiman.myfootprints.common.files.IFileSingleOperation

/** CreateFootprintActivityPresenter interface for other presenters  */
interface ICreateFootprintActivityPresenterExt
{
    /** Show photos grid  */
    fun switchToPhotoGrid()

    /** Show second step - create fragment  */
    fun switchToCreateStepPhotoFromPhotoGrid()

    /** Switch to map  */
    fun switchToCreateStepMapFromPhoto()

    /** Switch to photo  */
    fun switchToCreateStepPhotoFromMap()

    /** finish activity  */
    fun close()

    /** Show Photo editor  */
    fun showEditPhotoActivity(photo : IFileSingleOperation)

    /** Update photo after edit by EditPhotoActivity */
    fun updatePhotoAfterEdit(photo: IFileSingleOperation)
}
