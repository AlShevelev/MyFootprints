package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.photo

import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.IEditStepFragmentCallbacksBase

/**
 * Fragment callbacks for presenter
 */
interface IEditStepPhotoFragmentCallbacks : IEditStepFragmentCallbacksBase
{
    /** Show photo  */
    fun showPhoto(photoFile: IFileSingleOperation)

    /** Set a comment  */
    fun setComment(comment: String)
}
