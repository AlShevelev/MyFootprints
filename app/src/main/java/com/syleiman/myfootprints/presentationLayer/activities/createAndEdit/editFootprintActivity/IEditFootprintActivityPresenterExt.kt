package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity

import com.syleiman.myfootprints.common.files.IFileSingleOperation

/**
 * EditFootprintActivityPresenter interface for other presenters
 */
interface IEditFootprintActivityPresenterExt
{
    /** Show photos grid  */
    fun switchToPhotoGrid()

    /** Show second step - create fragment  */
    fun switchToEditStepPhotoFromPhotoGrid()

    /** Switch to map  */
    fun switchToEditStepMapFromPhoto()

    /** Switch to photo  */
    fun switchToEditStepPhotoFromMap()

    /**
     * Finish activity
     * @param saved true if footprint was saved
     */
    fun close(saved: Boolean)

    /** Show Photo editor  */
    fun showEditPhotoActivity(photo: IFileSingleOperation)

    /** Update photo after edit by EditPhotoActivity */
    fun updatePhotoAfterEdit(photo: IFileSingleOperation)
}
