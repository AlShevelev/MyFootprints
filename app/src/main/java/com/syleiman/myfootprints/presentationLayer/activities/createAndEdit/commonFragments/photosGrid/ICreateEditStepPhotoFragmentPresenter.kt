package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid

import com.syleiman.myfootprints.common.files.IFileSingleOperation

/** Calls from PhotoGrid to external presenter  */
interface ICreateEditStepPhotoFragmentPresenter
{
    /** Set photo to show  */
    fun setPhoto(photoInfo: ChosenPhotoInfo)

    /** */
    fun updatePhotoAfterEdit(photo: IFileSingleOperation)
}
