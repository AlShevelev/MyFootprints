package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity

import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.IActivityBaseCallback
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.CreateEditFootprintActivityStates

/** Activities interface for presenter  */
interface IEditFootprintActivityCallbacks : IActivityBaseCallback
{
    /**
     * Show photos grid
     * @param oldState - state in switch moment
     */
    fun switchToPhotoGrid(oldState: CreateEditFootprintActivityStates)

    /** Show second step - create fragment (if current fragment is photo grid)  */
    fun switchToEditStepPhotoFromPhotoGrid()

    /** Switch from map to photo  */
    fun switchToEditStepPhotoFromMap()

    /** Switch from photo to map */
    fun switchToEditStepMapFromPhoto()

    /**
     * Finish activity
     * @param saved true if footprint was saved
     */
    fun close(saved: Boolean)

    /** Show Photo editor  */
    fun showEditPhotoActivity(photo: IFileSingleOperation)
}
