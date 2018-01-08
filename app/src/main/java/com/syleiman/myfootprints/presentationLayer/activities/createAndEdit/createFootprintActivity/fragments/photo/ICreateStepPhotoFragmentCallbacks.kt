package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.photo

import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.ICreateStepFragmentCallbacksBase

/**
 * Fragment callbacks for presenter
 */
interface ICreateStepPhotoFragmentCallbacks : ICreateStepFragmentCallbacksBase
{
    /** Show photo  */
    fun showPhoto(photoFile: IFileSingleOperation)
}
