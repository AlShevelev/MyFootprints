package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.photo

import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.ChosenPhotoInfo
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.ICreateEditStepPhotoFragmentPresenter
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.ICreateFootprintActivityPresenterExt
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.CreateStepFragmentModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.CreateStepFragmentPresenterBase
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map.ICreateStepMapFragmentPresenterExt

import javax.inject.Inject

class CreateStepPhotoFragmentPresenter
@Inject
constructor(
        fragmentCallbacks: ICreateStepPhotoFragmentCallbacks,
        presentersRepository: UniversalRepository,
        model: CreateStepFragmentModel) : CreateStepFragmentPresenterBase<ICreateStepPhotoFragmentCallbacks>(fragmentCallbacks, presentersRepository, model), ICreateEditStepPhotoFragmentPresenter
{
    init
    {
        presentersRepository.registerEntity(ICreateEditStepPhotoFragmentPresenter::class.java, this)
    }

    /** Init view  */
    fun init()
    {
        val photoInfo = presentersRepository.getTempValue(ICreateEditStepPhotoFragmentPresenter::class.java)
        if (photoInfo != null)          // First call - extract url from temp value
            setPhoto((photoInfo as ChosenPhotoInfo?)!!)
    }

    /** Set photo to show  */
    override fun setPhoto(photoInfo: ChosenPhotoInfo)
    {
        model.setCurrentPhoto(photoInfo)
        fragmentCallbacks.showPhoto(photoInfo.photo)
    }

    /** */
    override fun updatePhotoAfterEdit(photo: IFileSingleOperation)
    {
        val photoInfo = model.getCurrentPhoto()!!
        photoInfo.photo = photo
        model.setCurrentPhoto(photoInfo)

        fragmentCallbacks.showPhoto(photo)
    }

    /** Switch to map  */
    fun switchToMap()
    {
        presentersRepository.getEntity(ICreateFootprintActivityPresenterExt::class.java)!!.switchToCreateStepMapFromPhoto()     // Switch to map
    }

    /** Memorize comment
     * @param saveButtonStateCallback - true - Save button enabled
     */
    fun storeComment(comment: String?, saveButtonStateCallback: Function1<Boolean, Unit>)
    {
        model.storeComment(comment, saveButtonStateCallback)
    }

    /**
     * Save footprint
     * @return true - success
     */
    fun save() : Boolean
    {
        presentersRepository.getEntity(ICreateStepMapFragmentPresenterExt::class.java)?.save() ?: return false      // Call map's presenter for saving
        return true
    }

    /** Close photo button clicked  */
    fun clickOnClosePhoto()
    {
        fragmentCallbacks.showQuery(R.string.create_footprint_dialog_back_to_gallery) {
            presentersRepository.getEntity(ICreateFootprintActivityPresenterExt::class.java)!!.switchToPhotoGrid()
         }
    }

    /** Edit photo button clicked  */
    fun clickOnEditPhoto()
    {
        val photoFile = model.getCurrentPhoto()!!.photo
        presentersRepository.getEntity(ICreateFootprintActivityPresenterExt::class.java)!!.showEditPhotoActivity(photoFile)
    }
}