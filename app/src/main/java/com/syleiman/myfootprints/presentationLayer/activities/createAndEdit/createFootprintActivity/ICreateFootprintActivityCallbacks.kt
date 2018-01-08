package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity

import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.IActivityBaseCallback
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.CreateEditFootprintActivityStates

/** Activities interface for presenter */
interface ICreateFootprintActivityCallbacks : IActivityBaseCallback
{
    /**
     * Show photos grid
     * @param oldState - state in switch moment
     */
    fun switchToPhotoGrid(oldState: CreateEditFootprintActivityStates)

    /** Show second step - create fragment (if current fragment is photo grid)  */
    fun switchToCreateStepPhotoFromPhotoGrid()

    /** Switch from map to photo  */
    fun switchToCreateStepPhotoFromMap()

    /** Switch from photo to map */
    fun switchToCreateStepMapFromPhoto()

    /** finish activity  */
    fun close()

    /** Show geolocation settings of device  */
    fun showGeoLocationSettings()

    /** Show Photo editor  */
    fun showEditPhotoActivity(photo: IFileSingleOperation)
}
