package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.photo

import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.ChosenPhotoInfo
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.ICreateEditStepPhotoFragmentPresenter
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.IEditFootprintActivityPresenterExt
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.EditStepFragmentModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.EditStepFragmentPresenterBase
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.IEditStepMapFragmentPresenterExt
import javax.inject.Inject

class EditStepPhotoFragmentPresenter
@Inject
constructor(
    fragmentCallbacks: IEditStepPhotoFragmentCallbacks,
    presentersRepository: UniversalRepository,
    model: EditStepFragmentModel) : EditStepFragmentPresenterBase<IEditStepPhotoFragmentCallbacks>(fragmentCallbacks, presentersRepository, model), ICreateEditStepPhotoFragmentPresenter
{
    init
    {
        presentersRepository.registerEntity(ICreateEditStepPhotoFragmentPresenter::class.java, this)
    }

    /** Init view  */
    fun init()
    {
        val footprintId = presentersRepository.getTempValue(ICreateEditStepPhotoFragmentPresenter::class.java) as Long?
        loadFootprint(footprintId!!)

        fragmentCallbacks.showPhoto(model.currentPhoto.photo)
        fragmentCallbacks.setComment(model.comment!!)
    }

    /** Set photo to show  */
    override fun setPhoto(photoInfo: ChosenPhotoInfo)
    {
        model.currentPhoto = photoInfo
        fragmentCallbacks.showPhoto(photoInfo.photo)
    }

    /** */
    override fun updatePhotoAfterEdit(photo: IFileSingleOperation)
    {
        val photoInfo = model.currentPhoto
        photoInfo.photo = photo
        model.currentPhoto = photoInfo

        fragmentCallbacks.showPhoto(photo)
    }

    /** Switch to map  */
    fun switchToMap()
    {
        presentersRepository.getEntity(IEditFootprintActivityPresenterExt::class.java)!!.switchToEditStepMapFromPhoto()     // Switch to map
    }

    /**
     * Save footprint
     * @return true - success
     */
    fun save() : Boolean
    {
        presentersRepository.getEntity(IEditStepMapFragmentPresenterExt::class.java)?.save() ?: return false        // Call map's presenter for saving
        return true
    }

    /** Memorize comment
     * @param saveButtonStateCallback - true - Save button enabled
     */
    fun storeComment(comment: String?, saveButtonStateCallback: Function1<Boolean, Unit>) = model.setComment(comment, saveButtonStateCallback)

    /** Close photo button clicked  */
    fun clickOnClosePhoto()
    {
        fragmentCallbacks.showQuery(R.string.edit_footprint_back_to_choose_photo) {
            presentersRepository.getEntity(IEditFootprintActivityPresenterExt::class.java)!!.switchToPhotoGrid()
        }
    }

    /** Edit photo button clicked  */
    fun clickOnEditPhoto()
    {
        val photoFile = model.currentPhoto.photo
        presentersRepository.getEntity(IEditFootprintActivityPresenterExt::class.java)!!.showEditPhotoActivity(photoFile)
    }
}